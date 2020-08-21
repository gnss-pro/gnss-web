package com.gnss.web.terminal.controller;

import com.gnss.web.annotations.ApiPageableParam;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.api.PageResultDTO;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.RecordFileDTO;
import com.gnss.web.terminal.api.RecordFileSearchDTO;
import com.gnss.web.terminal.domain.MediaFile;
import com.gnss.web.terminal.domain.RecordFile;
import com.gnss.web.terminal.mapper.RecordFileMapper;
import com.gnss.web.terminal.service.RecordFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 录像文件接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/29
 */
@Api(tags = "录像文件接口")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/terminal/recordFile")
public class RecordFileController {

    @Autowired
    private RecordFileService recordFileService;

    @ApiOperation("分页查询录像文件")
    @ApiPageableParam
    @GetMapping
    public PageResultDTO<RecordFileDTO> findByPage(@ApiParam("查询条件") RecordFileSearchDTO searchDTO,
                                                   @PageableDefault(sort = {"lastModifiedDate", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Specification<RecordFile> pageSpecification = getPageSpecification(searchDTO);
        Page<RecordFile> pageResult = recordFileService.findAll(pageSpecification, pageable);
        List<RecordFileDTO> mediaFileDTOList = RecordFileMapper.fromRecordFileList(pageResult.getContent());
        return new PageResultDTO<>(mediaFileDTOList, pageResult);
    }

    @ApiOperation("批量删除录像文件")
    @DeleteMapping
    public ApiResultDTO<Integer> batchDelete(@ApiParam("录像文件ID列表") @RequestBody @NotEmpty(message = "录像文件ID列表不能为空") List<Long> idList) throws Exception {
        int result = recordFileService.batchDelete(idList);
        return ApiResultDTO.success(result);
    }

    /**
     * 构造查询条件
     *
     * @param searchDTO
     * @return
     */
    private Specification<RecordFile> getPageSpecification(RecordFileSearchDTO searchDTO) {
        Specification<RecordFile> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            Join<MediaFile, Terminal> terminalJoin = root.join("terminal", JoinType.INNER);
            //名称查询条件
            String nameLike = searchDTO.getNameLike();
            if (StringUtils.isNotBlank(nameLike)) {
                Predicate predicate1 = criteriaBuilder.like(terminalJoin.get("vehicleNum").as(String.class), "%" + nameLike + "%");
                Predicate predicate2 = criteriaBuilder.like(terminalJoin.get("phoneNum").as(String.class), "%" + nameLike + "%");
                predicateList.add(criteriaBuilder.or(predicate1, predicate2));
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaQuery.where(predicateList.toArray(predicates)).getRestriction();
        };
        return spec;
    }
}
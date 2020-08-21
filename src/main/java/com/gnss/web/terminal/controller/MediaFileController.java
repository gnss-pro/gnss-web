package com.gnss.web.terminal.controller;

import com.gnss.web.annotations.ApiPageableParam;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.api.PageResultDTO;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.MediaFileDTO;
import com.gnss.web.terminal.api.MediaFileSearchDTO;
import com.gnss.web.terminal.domain.MediaFile;
import com.gnss.web.terminal.mapper.MediaFileMapper;
import com.gnss.web.terminal.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * <p>Description: 多媒体文件接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/6/29
 */
@Api(tags = "多媒体文件接口")
@Slf4j
@RestController
@RequestMapping("/api/v1/terminal/mediaFile")
public class MediaFileController {

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private ResourceLoader resourceLoader;

    @ApiOperation("分页查询多媒体文件")
    @ApiPageableParam
    @GetMapping
    public PageResultDTO<MediaFileDTO> findByPage(@ApiParam("查询条件") MediaFileSearchDTO searchDTO,
                                                  @PageableDefault(sort = {"lastModifiedDate", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Specification<MediaFile> pageSpecification = getPageSpecification(searchDTO);
        Page<MediaFile> pageResult = mediaFileService.findAll(pageSpecification, pageable);
        List<MediaFileDTO> mediaFileDTOList = MediaFileMapper.fromMediaFileList(pageResult.getContent());
        return new PageResultDTO<>(mediaFileDTOList, pageResult);
    }

    /**
     * 构造查询条件
     *
     * @param searchDTO
     * @return
     */
    private Specification<MediaFile> getPageSpecification(MediaFileSearchDTO searchDTO) {
        Specification<MediaFile> spec = (root, criteriaQuery, criteriaBuilder) -> {
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

    @ApiOperation("显示文件")
    @GetMapping("/display")
    public ResponseEntity<Resource> show(@ApiParam("路径") @RequestParam String path) {
        try {
            byte[] pathArr = Base64.getDecoder().decode(path);
            String filePath = new String(pathArr, "UTF8");
            return ResponseEntity.ok(resourceLoader.getResource("file:" + filePath));
        } catch (Exception e) {
            log.error("显示文件异常,base64路径:{}", path);
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation("批量删除多媒体文件")
    @DeleteMapping
    public ApiResultDTO<Integer> batchDelete(@ApiParam("多媒体文件ID列表") @RequestBody List<Long> idList) {
        int result = mediaFileService.batchDelete(idList);
        return ApiResultDTO.success(result);
    }
}
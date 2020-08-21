package com.gnss.web.log.controller;

import com.gnss.core.utils.TimeUtil;
import com.gnss.web.annotations.ApiPageableParam;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.api.PageResultDTO;
import com.gnss.web.log.api.Jt808LogDetailDTO;
import com.gnss.web.log.api.Jt808LogSearchDTO;
import com.gnss.web.log.domain.Jt808Log;
import com.gnss.web.log.service.Jt808LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: JT808日志接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@Api(tags = "JT808日志")
@RestController
@RequestMapping("/api/v1/logs/jt808")
@Slf4j
public class Jt808LogController {

    @Autowired
    private Jt808LogService jt808LogService;

    @ApiOperation("根据条件查询JT808日志")
    @ApiPageableParam
    @GetMapping("/all")
    public ApiResultDTO<List<Jt808LogDetailDTO>> findAll(@ApiParam("查询条件") Jt808LogSearchDTO searchDTO) {
        Specification<Jt808Log> spec = getQuerySpecification(searchDTO);
        List<Jt808Log> jt808LogList = jt808LogService.findAll(spec);
        List<Jt808LogDetailDTO> jt808LogDetailDTOList = toJt808LogDetailDTOList(jt808LogList);
        return new ApiResultDTO<>(jt808LogDetailDTOList);
    }

    @ApiOperation("根据条件分页查询JT808日志")
    @ApiPageableParam
    @GetMapping
    public PageResultDTO<Jt808LogDetailDTO> findByPage(@ApiParam("查询条件") Jt808LogSearchDTO searchDTO,
                                                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Specification<Jt808Log> spec = getQuerySpecification(searchDTO);
        Page<Jt808Log> pageResult = jt808LogService.findAll(spec, pageable);
        List<Jt808LogDetailDTO> jt808LogDetailDTOList = toJt808LogDetailDTOList(pageResult.getContent());
        return new PageResultDTO<>(jt808LogDetailDTOList, pageResult);
    }

    @ApiOperation("根据条件删除JT808日志")
    @PostMapping("/batchDelete")
    public ApiResultDTO<Integer> batchDelete(@ApiParam("查询条件") @RequestBody Jt808LogSearchDTO searchDTO) {
        int result = jt808LogService.batchDelete(searchDTO);
        return ApiResultDTO.success(result);
    }

    /**
     * 构造查询条件
     *
     * @param searchDTO
     * @return
     */
    private Specification<Jt808Log> getQuerySpecification(Jt808LogSearchDTO searchDTO) {
        Specification<Jt808Log> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            //终端手机号查询条件
            String phoneNo = searchDTO.getPhoneNum();
            if (!StringUtils.isEmpty(phoneNo)) {
                Predicate predicate = criteriaBuilder.equal(root.get("phoneNumber").as(String.class), phoneNo);
                predicateList.add(predicate);
            }

            //时间查询
            Long startTime = searchDTO.getStartTime();
            if (startTime != null) {
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("time").as(Long.class), startTime);
                predicateList.add(predicate);
            }
            Long endTime = searchDTO.getEndTime();
            if (endTime != null) {
                Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("time").as(Long.class), endTime);
                predicateList.add(predicate);
            }

            //上次查询最大ID
            String lastId = searchDTO.getLastId();
            if (!StringUtils.isEmpty(lastId)) {
                Predicate predicate = criteriaBuilder.greaterThan(root.get("id").as(Long.class), Long.valueOf(lastId));
                predicateList.add(predicate);
            }

            //根据id升序
            Order order = criteriaBuilder.asc(root.get("id").as(Long.class));
            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaQuery.where(predicateList.toArray(predicates)).orderBy(order).getRestriction();
        };
        return spec;
    }

    /**
     * JT808日志实体转DTO
     *
     * @param jt808LogList
     * @return
     */
    private List<Jt808LogDetailDTO> toJt808LogDetailDTOList(List<Jt808Log> jt808LogList) {
        if (CollectionUtils.isEmpty(jt808LogList)) {
            return Collections.emptyList();
        }
        List<Jt808LogDetailDTO> jt808LogDetailDTOList = jt808LogList.stream().map(jt808Log -> toJt808LogDetailDTO(jt808Log))
                .collect(Collectors.toList());
        return jt808LogDetailDTOList;
    }

    /**
     * JT808数据库实体转日志详情DTO
     *
     * @param jt808Log
     * @return
     */
    private Jt808LogDetailDTO toJt808LogDetailDTO(Jt808Log jt808Log) {
        Jt808LogDetailDTO jt808LogDetailDTO = new Jt808LogDetailDTO();
        jt808LogDetailDTO.setId(jt808Log.getId().toString());
        jt808LogDetailDTO.setTime(TimeUtil.formatTime(jt808Log.getTime()));
        jt808LogDetailDTO.setProtocolType(jt808Log.getProtocolType());
        jt808LogDetailDTO.setMsgIdHex(jt808Log.getMsgIdHex());
        jt808LogDetailDTO.setMsgIdDesc(jt808Log.getMsgIdDesc());
        jt808LogDetailDTO.setPhoneNumber(jt808Log.getPhoneNumber());
        jt808LogDetailDTO.setVehicleNum(jt808Log.getVehicleNum());
        jt808LogDetailDTO.setVehiclePlateColor(jt808Log.getVehiclePlateColor() == null ? null : jt808Log.getVehiclePlateColor().getDesc());
        jt808LogDetailDTO.setMsgFlowId(jt808Log.getMsgFlowId());
        jt808LogDetailDTO.setEncryptType(jt808Log.getEncryptType());
        jt808LogDetailDTO.setMsgBodyDesc(jt808Log.getMsgBodyDesc());
        jt808LogDetailDTO.setLinkType(jt808Log.getLinkType() == null ? "" : jt808Log.getLinkType().getMsgDirection());
        jt808LogDetailDTO.setErrorMsg(jt808Log.getErrorMsg());
        return jt808LogDetailDTO;
    }
}
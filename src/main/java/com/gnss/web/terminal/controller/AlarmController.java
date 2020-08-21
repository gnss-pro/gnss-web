package com.gnss.web.terminal.controller;

import com.gnss.core.constants.AlarmTypeEnum;
import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.common.api.PageResultDTO;
import com.gnss.web.constants.AlarmSourceEnum;
import com.gnss.web.constants.HandleTypeEnum;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.AlarmDTO;
import com.gnss.web.terminal.api.AlarmSearchDTO;
import com.gnss.web.terminal.domain.Alarm;
import com.gnss.web.terminal.mapper.AlarmMapper;
import com.gnss.web.terminal.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 报警接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/7/30
 */
@Api(tags = "报警接口")
@RestController
@RequestMapping("/api/v1/terminal/alarms")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @ApiOperation("根据条件查询报警信息")
    @GetMapping
    public PageResultDTO<AlarmDTO> findByPage(@ApiParam("查询条件") AlarmSearchDTO searchDTO,
                                              @PageableDefault(sort = {"lastModifiedDate", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Specification<Alarm> spec = buildQuerySpecification(searchDTO);
        Page<Alarm> pageResult = alarmService.findAll(spec, pageable);
        List<AlarmDTO> alarmDTOList = AlarmMapper.fromAlarms(pageResult.getContent());
        return new PageResultDTO<>(alarmDTOList, pageResult);
    }

    @ApiOperation("批量删除报警")
    @DeleteMapping
    public ApiResultDTO<Integer> deleteById(@ApiParam("报警ID列表") @RequestBody List<Long> idList) {
        int result = alarmService.batchDelete(idList);
        return ApiResultDTO.success(result);
    }

    /**
     * 构造查询条件
     *
     * @param searchDTO
     * @return
     */
    private Specification<Alarm> buildQuerySpecification(AlarmSearchDTO searchDTO) {
        Specification<Alarm> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            //根据时间查询
            Long startTime = searchDTO.getStartTime();
            if (startTime != null) {
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startTime").as(Long.class), startTime);
                predicateList.add(predicate);
            }
            Long endTime = searchDTO.getEndTime();
            if (endTime != null) {
                Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("startTime").as(Long.class), endTime);
                predicateList.add(predicate);
            }

            //根据名称
            String nameLike = searchDTO.getNameLike();
            if (StringUtils.isNotBlank(nameLike)) {
                Join<Alarm, Terminal> terminalJoin = root.join("terminal", JoinType.INNER);
                Predicate predicate = criteriaBuilder.like(terminalJoin.get("vehicleNum").as(String.class), "%" + nameLike + "%");
                Predicate predicate1 = criteriaBuilder.like(terminalJoin.get("phoneNum").as(String.class), "%" + nameLike + "%");
                predicateList.add(criteriaBuilder.or(predicate, predicate1));
            }

            //根据报警来源
            AlarmSourceEnum alarmSource = searchDTO.getAlarmSource();
            if (alarmSource != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("alarmSource").as(AlarmSourceEnum.class), alarmSource);
                predicateList.add(predicate);
            }

            //根据报警类型
            List<AlarmTypeEnum> alarmTypeList = searchDTO.getAlarmTypeList();
            if (CollectionUtils.isNotEmpty(alarmTypeList)) {
                CriteriaBuilder.In<AlarmTypeEnum> in = criteriaBuilder.in(root.get("alarmType").as(AlarmTypeEnum.class));
                alarmTypeList.forEach(alarmType -> in.value(alarmType));
                predicateList.add(in);
            }

            //根据处理情况
            HandleTypeEnum isHandle = searchDTO.getIsHandle();
            if (isHandle != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("isHandle").as(HandleTypeEnum.class), isHandle);
                predicateList.add(predicate);
            }

            //根据报警时长
            Long alarmDuration = searchDTO.getAlarmDuration();
            if (alarmDuration != null) {
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("alarmDuration").as(Long.class), alarmDuration);
                predicateList.add(predicate);
            }

            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaQuery.where(predicateList.toArray(predicates)).getRestriction();
        };
        return spec;
    }
}

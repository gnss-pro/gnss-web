package com.gnss.web.terminal.service;

import com.gnss.core.proto.TerminalStatusProto;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.api.LocationSearchDTO;
import com.gnss.web.terminal.domain.Location;
import com.gnss.web.terminal.mapper.MediaFileMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 位置服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/7/13
 */
@Service
public class LocationService extends BaseService<Location> {

    public Location save(TerminalStatusProto terminalStatusProto) {
        Location location = MediaFileMapper.locationProtoToLocation(terminalStatusProto.getLocation());
        location.setTerminal(new Terminal(terminalStatusProto.getTerminalInfo().getTerminalId()));
        return save(location);
    }

    @Transactional(readOnly = true)
    public Page<Location> findByPage(LocationSearchDTO searchDTO, Pageable pageable) {
        Specification<Location> spec = buildQuerySpecification(searchDTO);
        return findAll(spec, pageable);
    }

    /**
     * 构造查询条件
     *
     * @param searchDTO
     * @return
     */
    private Specification<Location> buildQuerySpecification(LocationSearchDTO searchDTO) {
        Specification<Location> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            //根据终端ID查询
            Long terminalId = searchDTO.getTerminalId();
            if (terminalId != null) {
                Terminal terminal = new Terminal(terminalId);
                Predicate predicate = criteriaBuilder.equal(root.get("terminal").as(Terminal.class), terminal);
                predicateList.add(predicate);
            }

            //根据名称
            String nameLike = searchDTO.getNameLike();
            if (StringUtils.isNotBlank(nameLike)) {
                Join<Location, Terminal> terminalJoin = root.join("terminal", JoinType.INNER);
                Predicate predicate = criteriaBuilder.like(terminalJoin.get("vehicleNum").as(String.class), "%" + nameLike + "%");
                Predicate predicate1 = criteriaBuilder.like(terminalJoin.get("phoneNum").as(String.class), "%" + nameLike + "%");
                predicateList.add(criteriaBuilder.or(predicate, predicate1));
            }

            //根据时间查询
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

            Predicate[] predicates = new Predicate[predicateList.size()];
            return criteriaQuery.where(predicateList.toArray(predicates)).getRestriction();
        };
        return spec;
    }
}
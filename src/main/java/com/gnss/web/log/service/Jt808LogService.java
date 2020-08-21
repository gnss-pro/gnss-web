package com.gnss.web.log.service;

import com.gnss.core.constants.jt808.Jt808LinkTypeEnum;
import com.gnss.core.proto.Jt808LogProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.log.api.Jt808LogSearchDTO;
import com.gnss.web.log.domain.Jt808Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: JT808日志服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
@Service
public class Jt808LogService extends BaseService<Jt808Log> {

    @PersistenceContext
    private EntityManager em;

    /**
     * 保存日志
     *
     * @param jt808LogProto
     * @return
     */
    public Jt808Log save(Jt808LogProto jt808LogProto) {
        Jt808Log jt808Log = new Jt808Log();
        jt808Log.setLinkType(jt808LogProto.getLinkType() == Jt808LinkTypeEnum.NULL ? null : jt808LogProto.getLinkType());
        jt808Log.setTime(jt808LogProto.getTime());
        jt808Log.setProtocolType(jt808LogProto.getProtocolType());
        jt808Log.setMsgIdHex(jt808LogProto.getMsgIdHex());
        jt808Log.setMsgIdDesc(jt808LogProto.getMsgIdDesc());
        jt808Log.setPhoneNumber(jt808LogProto.getPhoneNumber());
        jt808Log.setMsgFlowId(jt808LogProto.getMsgFlowId());
        jt808Log.setEncryptType(jt808LogProto.getEncryptType());
        jt808Log.setMsgBodyDesc(jt808LogProto.getMsgBodyDesc());
        jt808Log.setErrorMsg(jt808LogProto.getErrorMsg());
        TerminalProto terminalInfo = jt808LogProto.getTerminalInfo();
        if (terminalInfo != null) {
            jt808Log.setVehicleNum(terminalInfo.getVehicleNum());
            jt808Log.setVehiclePlateColor(terminalInfo.getVehiclePlateColor());
        }
        return save(jt808Log);
    }

    /**
     * 批量删除日志
     *
     * @param searchDTO
     * @return
     */
    public int batchDelete(Jt808LogSearchDTO searchDTO) {
        CriteriaDelete<Jt808Log> criteriaDelete = getCriteriaDelete(searchDTO);
        return em.createQuery(criteriaDelete).executeUpdate();
    }

    /**
     * 构造删除条件
     *
     * @param searchDTO
     * @return
     */
    private CriteriaDelete<Jt808Log> getCriteriaDelete(Jt808LogSearchDTO searchDTO) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<Jt808Log> criteriaDelete = criteriaBuilder.createCriteriaDelete(Jt808Log.class);
        Root<Jt808Log> root = criteriaDelete.from(Jt808Log.class);
        List<Predicate> predicateList = new ArrayList<>();

        //终端手机号条件
        String phoneNo = searchDTO.getPhoneNum();
        if (!StringUtils.isEmpty(phoneNo)) {
            Predicate predicate = criteriaBuilder.equal(root.get("phoneNumber").as(String.class), phoneNo);
            predicateList.add(predicate);
        }

        //时间条件
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
        return criteriaDelete.where(predicateList.toArray(predicates));
    }
}
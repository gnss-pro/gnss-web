package com.gnss.web.info.dao;

import com.gnss.web.common.dao.BaseRepository;
import com.gnss.web.info.domain.Terminal;

import java.util.Optional;

/**
 * <p>Description: 终端信息DAO</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
public interface TerminalRepository extends BaseRepository<Terminal> {

    /**
     * 根据终端手机号查询
     *
     * @param phoneNum
     * @return
     */
    Optional<Terminal> findByPhoneNum(String phoneNum);

    /**
     * 根据终端号查询
     *
     * @param terminalNum
     * @return
     */
    Optional<Terminal> findByTerminalNum(String terminalNum);

    /**
     * 根据车牌号查询
     *
     * @param vehicleNum
     * @return
     */
    Optional<Terminal> findByVehicleNum(String vehicleNum);
}
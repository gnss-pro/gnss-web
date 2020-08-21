package com.gnss.web.terminal.controller;

import com.gnss.web.annotations.ApiPageableParam;
import com.gnss.web.common.api.PageResultDTO;
import com.gnss.web.terminal.api.LocationSearchDTO;
import com.gnss.web.terminal.api.TrackDTO;
import com.gnss.web.terminal.domain.Location;
import com.gnss.web.terminal.mapper.LocationMapper;
import com.gnss.web.terminal.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Description: 位置接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/8/7
 */
@Api(tags = "位置接口")
@RestController
@RequestMapping("/api/v1/terminal/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @ApiOperation("根据条件分页查询轨迹")
    @ApiPageableParam
    @GetMapping
    public PageResultDTO<TrackDTO> findByPage(@ApiParam("查询条件") LocationSearchDTO searchDTO,
                                                 @PageableDefault(sort = {"time", "id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Location> pageResult = locationService.findByPage(searchDTO, pageable);
        List<TrackDTO> locationDTOList = LocationMapper.fromLocations(pageResult.getContent());
        return new PageResultDTO<>(locationDTOList, pageResult);
    }
}
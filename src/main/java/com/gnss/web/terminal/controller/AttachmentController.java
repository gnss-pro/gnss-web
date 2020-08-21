package com.gnss.web.terminal.controller;

import com.gnss.web.common.api.ApiResultDTO;
import com.gnss.web.terminal.api.AttachmentDetailDTO;
import com.gnss.web.terminal.api.SafetyAlarmDetailDTO;
import com.gnss.web.terminal.domain.AttachmentInfo;
import com.gnss.web.terminal.mapper.AttachmentMapper;
import com.gnss.web.terminal.service.AttachmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>Description: 主动安全附件接口</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/7/30
 */
@Api(tags = "主动安全附件接口")
@RestController
@RequestMapping("/api/v1/terminal/attachments")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @ApiOperation("根据主动安全报警ID查询附件列表")
    @GetMapping("/alarm/{alarmId}")
    public ApiResultDTO<SafetyAlarmDetailDTO> findByAlarmId(@ApiParam("主动安全报警ID") @PathVariable Long alarmId) {
        List<AttachmentInfo> attachmentList = attachmentService.findByAlarmId(alarmId);
        List<AttachmentDetailDTO> detailDTOList = AttachmentMapper.fromAttachments(attachmentList);
        return ApiResultDTO.success(detailDTOList);
    }
}
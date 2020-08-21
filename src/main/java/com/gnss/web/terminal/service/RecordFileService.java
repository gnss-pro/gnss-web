package com.gnss.web.terminal.service;

import com.alibaba.fastjson.JSON;
import com.gnss.core.constants.RpcEnum;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.rpc.FtpUploadCommand;
import com.gnss.mqutil.producer.RabbitMessageSender;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.terminal.dao.RecordFileRepository;
import com.gnss.web.terminal.domain.RecordFile;
import com.gnss.web.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>Description: 录像文件服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2019/8/11
 */
@Slf4j
@Service
public class RecordFileService extends BaseService<RecordFile> {

    @Autowired
    private RecordFileRepository recordFileRepository;

    @Autowired
    private RabbitMessageSender messageSender;

    /**
     * 更新消息流水号
     *
     * @param id
     * @param msgFlowId
     */
    public void updateMessageFlowId(Long id, int msgFlowId) {
        recordFileRepository.updateMessageFlowId(id, msgFlowId);
    }

    /**
     * 更新文件状态
     *
     * @param id
     * @param fileStatus
     */
    public void updateFileStatus(Long id, int fileStatus) {
        recordFileRepository.updateFileStatus(id, fileStatus);
    }

    /**
     * 完成FTP上传文件
     *
     * @param terminalInfo
     * @param contentJson
     */
    public void completeFtpFile(TerminalProto terminalInfo, String contentJson) throws Exception {
        FtpUploadCommand ftpUploadCommand = JSON.parseObject(contentJson, FtpUploadCommand.class);
        Long recordFileId = Long.valueOf(ftpUploadCommand.getRecordFileId());
        String filePath = ftpUploadCommand.getFilePath();
        Optional<RecordFile> optional = findById(recordFileId);
        if (optional.isPresent()) {
            //文件状态设置为已完成
            int fileStatus = 1;
            //文件路径转base64
            String base64FilePath = Base64.getEncoder().encodeToString(filePath.getBytes("utf-8"));
            Long fileSize = ftpUploadCommand.getFileSize();
            RecordFile recordFile = optional.get();
            recordFile.setFilePath(filePath);
            recordFile.setFileSize(fileSize);
            recordFile.setFileName(ftpUploadCommand.getFileName());
            recordFile.setBase64FilePath(base64FilePath);
            recordFile.setFileStatus(fileStatus);
            save(recordFile);
            log.info("完成FTP上传文件,终端手机号:{},终端号:{},路径:{}", terminalInfo.getTerminalSimCode(), terminalInfo.getTerminalNum(), filePath);
        } else {
            log.error("完成FTP上传文件,数据库无记录,终端手机号:{},终端号:{},路径:{}", terminalInfo.getTerminalSimCode(), terminalInfo.getTerminalNum(), filePath);
        }
    }

    /**
     * 批量删除录像文件
     *
     * @param idList
     */
    public int batchDelete(List<Long> idList) throws Exception {
        List<RecordFile> recordFileList = recordFileRepository.findByIdIn(idList);
        List<String> filePathList = recordFileList.stream()
                .filter(recordFile -> Objects.nonNull(recordFile.getFilePath()))
                .map(RecordFile::getFilePath)
                .collect(Collectors.toList());
        int fileCnt = recordFileRepository.deleteByIdIn(idList);
        Boolean deleteFileResult = Boolean.FALSE;
        if (!filePathList.isEmpty()) {
            //删除文件服务器的文件
            deleteFileResult = FileUtil.sendDeleteFileCommand(messageSender, RpcEnum.DELETE_FTP_FILE, filePathList);
        }
        log.info("批量删除录像文件,ID列表:{},删除数量:{},文件删除结果:{}", idList, fileCnt, deleteFileResult);
        return fileCnt;
    }
}
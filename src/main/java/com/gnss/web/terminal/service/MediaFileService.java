package com.gnss.web.terminal.service;

import com.gnss.core.constants.CommonConstant;
import com.gnss.core.proto.MediaFileProto;
import com.gnss.core.proto.TerminalProto;
import com.gnss.web.common.service.BaseService;
import com.gnss.web.info.domain.Terminal;
import com.gnss.web.terminal.dao.MediaFileRepository;
import com.gnss.web.terminal.domain.Location;
import com.gnss.web.terminal.domain.MediaFile;
import com.gnss.web.terminal.mapper.MediaFileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

/**
 * <p>Description: 多媒体文件服务</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2020/3/30
 */
@Slf4j
@Service
public class MediaFileService extends BaseService<MediaFile> {

    @Value(("${gnss.media.path}"))
    private String mediaBaseDir;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    public void save(MediaFileProto mediaFileProto) throws Exception {
        //查询下发指令时缓存的多媒体文件ID
        Long terminalId = mediaFileProto.getTerminalInfo().getTerminalId();
        String key = String.format("%d_%d", terminalId, mediaFileProto.getChannelId());
        //构建多媒体文件实体
        MediaFile mediaFile = MediaFileMapper.fromMediaFileProto(mediaFileProto);
        mediaFile.setTerminal(new Terminal(terminalId));
        //获取定位时间
        Location location = mediaFile.getLocation();
        long time;
        if (location != null) {
            time = location.getTime();
            location.setTerminal(mediaFile.getTerminal());
        } else {
            time = System.currentTimeMillis();
        }

        TerminalProto terminalInfo = mediaFileProto.getTerminalInfo();
        //构造文件存储路径
        Path filePath = buildMediaFilePath(terminalInfo, mediaFile, time);
        //写入文件
        Files.write(filePath, mediaFileProto.getMediaData());
        //文件路径采用base64加密
        String filePathStr = filePath.toString();
        String base64FilePath = Base64.getEncoder().encodeToString(filePathStr.getBytes("utf-8"));
        mediaFile.setFilePath(filePathStr);
        mediaFile.setBase64FilePath(base64FilePath);
        save(mediaFile);
        log.info("保存终端多媒体文件,终端号:{},终端手机号:{},路径:{}", terminalInfo.getTerminalNum(), terminalInfo.getTerminalSimCode(), filePath);
    }

    /**
     * 批量删除多媒体文件
     *
     * @param idList
     */
    public int batchDelete(List<Long> idList) {
        return mediaFileRepository.deleteByIdIn(idList);
    }

    /**
     * 构造多媒体文件存储路径
     *
     * @param terminalInfo
     * @param mediaFile
     * @param time
     * @return
     * @throws Exception
     */
    private Path buildMediaFilePath(TerminalProto terminalInfo, MediaFile mediaFile, long time) throws Exception {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), CommonConstant.ZONE_GMT8);
        //生成文件名
        String dateTimeStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(dateTime);
        String extName = mediaFile.getMediaFormatCode().getDesc();
        String fileName = String.format("%s_%s_%d.%s", terminalInfo.getTerminalSimCode(), dateTimeStr, mediaFile.getChannelId(), extName);
        mediaFile.setFileName(fileName);
        //父目录
        String dirName = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateTime);
        Path parentDir = Paths.get(mediaBaseDir, dirName);
        //创建目录
        if (Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        //文件路径
        Path filePath = Paths.get(parentDir.toString(), fileName);
        return filePath;
    }
}
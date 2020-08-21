package com.gnss.web.terminal.mapper;

import com.gnss.core.proto.AttachmentInfoProto;
import com.gnss.web.terminal.api.AttachmentDetailDTO;
import com.gnss.web.terminal.domain.AttachmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 由程序自动生成</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019/10/14
 */
public class AttachmentMapper {

    public static AttachmentInfo fromAttachmentProto(AttachmentInfoProto attachmentInfoProto) {
        if (attachmentInfoProto == null) {
            return null;
        }

        AttachmentInfo attachmentInfo = new AttachmentInfo();
        attachmentInfo.setFileName(attachmentInfoProto.getFileName());
        attachmentInfo.setFileSize(attachmentInfoProto.getFileSize());
        attachmentInfo.setFileType(attachmentInfoProto.getFileType());
        attachmentInfo.setFilePath(attachmentInfoProto.getFilePath());
        return attachmentInfo;
    }

    public static List<AttachmentDetailDTO> fromAttachments(List<AttachmentInfo> attachmentList) {
        if (attachmentList == null) {
            return null;
        }

        List<AttachmentDetailDTO> list = new ArrayList<AttachmentDetailDTO>(attachmentList.size());
        for (AttachmentInfo attachmentInfo : attachmentList) {
            list.add(fromAttachment(attachmentInfo));
        }

        return list;
    }

    public static AttachmentDetailDTO fromAttachment(AttachmentInfo attachmentInfo) {
        if (attachmentInfo == null) {
            return null;
        }

        AttachmentDetailDTO attachmentDetailDTO = new AttachmentDetailDTO();
        if (attachmentInfo.getId() != null) {
            attachmentDetailDTO.setAttachmentId(String.valueOf(attachmentInfo.getId()));
        }
        attachmentDetailDTO.setFileType(attachmentInfo.getFileType());
        if (attachmentInfo.getFileSize() != null) {
            attachmentDetailDTO.setFileSize(String.valueOf(attachmentInfo.getFileSize()));
        }
        attachmentDetailDTO.setFileName(attachmentInfo.getFileName());
        attachmentDetailDTO.setBase64FilePath(attachmentInfo.getBase64FilePath());

        return attachmentDetailDTO;
    }

}
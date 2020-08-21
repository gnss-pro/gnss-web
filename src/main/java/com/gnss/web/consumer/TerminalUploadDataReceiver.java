package com.gnss.web.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gnss.core.constants.UploadDataTypeEnum;
import com.gnss.core.model.jt1078.AvResourceInfo;
import com.gnss.core.model.jt1078.FtpUploadResult;
import com.gnss.core.model.jt1078.PassengerFlow;
import com.gnss.core.model.jt808.CanBusData;
import com.gnss.core.model.jt808.DriverReportInfo;
import com.gnss.core.model.jt808.EventReport;
import com.gnss.core.model.jt808.EwaybillInfo;
import com.gnss.core.model.jt808.Jt808Message;
import com.gnss.core.model.jt808.MediaEvent;
import com.gnss.core.model.jt808.MessagePlay;
import com.gnss.core.model.jt808.PassthroughData;
import com.gnss.core.model.jt808.TakePhotoResponse;
import com.gnss.core.model.jt808.TerminalAuthInfo;
import com.gnss.core.model.jt808.TerminalRegisterInfo;
import com.gnss.core.model.jt808.TerminalRsaPublicKey;
import com.gnss.core.model.jt808.TerminalUpgradeResult;
import com.gnss.core.model.jt808.VehicleTravelInfo;
import com.gnss.core.model.jt808.ZipData;
import com.gnss.core.proto.TerminalProto;
import com.gnss.core.proto.TerminalUploadDataProto;
import com.gnss.web.command.service.UnsupportJt808MessageService;
import com.gnss.web.constants.RabbitConstants;
import com.gnss.web.terminal.service.RecordFileService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Description: 终端上传数据订阅</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author Pendy
 * @version 1.0.1
 * @date 2019-1-18
 */
@Component
@Slf4j
@RabbitListener(queues = RabbitConstants.UPLOAD_DATA_QUEUE)
public class TerminalUploadDataReceiver {

    @Autowired
    private RecordFileService recordFileService;

    @Autowired
    private UnsupportJt808MessageService unsupportJt808MessageService;

    @RabbitHandler
    public void handleUploadData(TerminalUploadDataProto uploadDataProto, Channel channel, Message message) throws Exception {
        try {
            TerminalProto terminalInfo = uploadDataProto.getTerminalInfo();
            UploadDataTypeEnum uploadDataType = uploadDataProto.getUploadDataType();
            String contentJson = uploadDataProto.getContent();
            if (uploadDataType == UploadDataTypeEnum.FTP_UPLOADED_INFO) {
                //文件服务器FTP上传完成信息(由文件服务器的FTP服务判断完成)
                recordFileService.completeFtpFile(terminalInfo, contentJson);
            } else if (uploadDataType == UploadDataTypeEnum.FTP_UPLOAD_RESULT) {
                //FTP文件上传完成通知(JT1078协议0x1206)
                FtpUploadResult ftpUploadResult = JSON.parseObject(contentJson, FtpUploadResult.class);
            } else if (uploadDataType == UploadDataTypeEnum.UNSUPPORT_JT808_MSG) {
                //未支持的JT808消息
                Jt808Message jt808Msg = JSON.parseObject(contentJson, Jt808Message.class);
                unsupportJt808MessageService.process(terminalInfo, jt808Msg);
            } else if (uploadDataType == UploadDataTypeEnum.TERMINAL_REGISTER) {
                //终端注册信息
                TerminalRegisterInfo registerInfo = JSON.parseObject(contentJson, TerminalRegisterInfo.class);
            } else if (uploadDataType == UploadDataTypeEnum.TERMINAL_AUTH) {
                //终端鉴权
                TerminalAuthInfo authInfo = JSON.parseObject(contentJson, TerminalAuthInfo.class);
            } else if (uploadDataType == UploadDataTypeEnum.EWAYBILL) {
                //电子运单
                EwaybillInfo ewaybillInfo = JSON.parseObject(contentJson, EwaybillInfo.class);
            } else if (uploadDataType == UploadDataTypeEnum.DRIVER_INFO) {
                //驾驶员身份信息采集
                DriverReportInfo driverReportInfo = JSON.parseObject(contentJson, DriverReportInfo.class);
            } else if (uploadDataType == UploadDataTypeEnum.TERMINAL_RSA_PUBLIC_KEY) {
                //终端RSA公钥
                TerminalRsaPublicKey rsaPublicKey = JSON.parseObject(contentJson, TerminalRsaPublicKey.class);
            } else if (uploadDataType == UploadDataTypeEnum.TERMINAL_CANCEL) {
                //终端注销
                //内容为空
            } else if (uploadDataType == UploadDataTypeEnum.TERMINAL_UPGRADE_RESULT) {
                //终端升级结果
                TerminalUpgradeResult upgradeResult = JSON.parseObject(contentJson, TerminalUpgradeResult.class);
            } else if (uploadDataType == UploadDataTypeEnum.EVENT_REPORT) {
                //事件报告
                EventReport eventReport = JSON.parseObject(contentJson, EventReport.class);
            } else if (uploadDataType == UploadDataTypeEnum.MESSAGE_PLAY) {
                //信息点播/取消
                MessagePlay upgradeResult = JSON.parseObject(contentJson, MessagePlay.class);
            } else if (uploadDataType == UploadDataTypeEnum.VEHICLE_TRAVEL) {
                //行驶记录数据
                VehicleTravelInfo vehicleTravelInfo = JSON.parseObject(contentJson, VehicleTravelInfo.class);
            } else if (uploadDataType == UploadDataTypeEnum.CAN_BUS_DATA) {
                //CAN总线数据
                CanBusData canBusData = JSON.parseObject(contentJson, CanBusData.class);
            } else if (uploadDataType == UploadDataTypeEnum.MEDIA_EVENT) {
                //多媒体事件信息
                MediaEvent mediaEvent = JSON.parseObject(contentJson, MediaEvent.class);
            } else if (uploadDataType == UploadDataTypeEnum.TAKE_PHOTO_RESPONSE) {
                //摄像头立即拍摄命令应答
                TakePhotoResponse takePhotoResponse = JSON.parseObject(contentJson, TakePhotoResponse.class);
            } else if (uploadDataType == UploadDataTypeEnum.PASSTHROUGH) {
                //数据上行透传
                PassthroughData passthroughData = JSON.parseObject(contentJson, PassthroughData.class);
            } else if (uploadDataType == UploadDataTypeEnum.ZIP_DATA) {
                //数据压缩
                ZipData zipData = JSON.parseObject(contentJson, ZipData.class);
            } else if (uploadDataType == UploadDataTypeEnum.PASSENGER_FLOW) {
                //终端上传乘客流量
                PassengerFlow passengerFlow = JSON.parseObject(contentJson, PassengerFlow.class);
            } else if (uploadDataType == UploadDataTypeEnum.MEDIA_RESOURCE_LIST) {
                //音视频资源列表
                List<AvResourceInfo> avResourceInfoList = JSON.parseObject(contentJson, new TypeReference<List<AvResourceInfo>>() {
                });
            }

            log.info("收到终端上传数据,终端手机号:{},上传类型:{},上传内容:{}", terminalInfo.getTerminalSimCode(), uploadDataType, contentJson);
        } catch (Exception e) {
            log.error("终端上传数据异常:{}", uploadDataProto, e);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }
}

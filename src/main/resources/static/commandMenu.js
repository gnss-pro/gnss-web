layui.use(['tree', 'layer'], function () {
    var layer = layui.layer
        , $ = layui.jquery;
    var tree = layui.tree;

    tree.render({
        elem: '#commandMenu' //指定元素
        , target: '_blank' //是否新选项卡打开（比如节点返回href才有效）
        , click: function (item) { //点击节点回调
            if (item.data.url) {
                $("#form-view").load(item.data.url);
            }
        }
        , data: [ //节点
            {
                title: '终端参数指令'
                , id: 1
                , spread: true
                , children: [
                    {
                        title: '查询参数设置'
                        , id: 10000
                        , url: 'command/config/queryConfig.html'
                    }, {
                        title: '查询终端属性'
                        , id: 10001
                        , url: 'command/config/queryProperty.html'
                    }, {
                        title: '查询终端音视频属性'
                        , id: 10002
                        , url: 'command/config/queryAvProperty.html'
                    }, {
                        title: '通讯连接参数'
                        , id: 10003
                        , url: 'command/config/connectionConfig.html'
                    }, {
                        title: '服务器参数'
                        , id: 10004
                        , url: 'command/config/serverConfig.html'
                    }, {
                        title: '位置参数汇报'
                        , id: 10005
                        , url: 'command/config/locationConfig.html'
                    }, {
                        title: '电话号码参数'
                        , id: 10006
                        , url: 'command/config/phoneConfig.html'
                    }, {
                        title: '报警开关选项'
                        , id: 10007
                        , url: 'command/config/alarmOpenConfig.html'
                    }, {
                        title: '报警参数'
                        , id: 10008
                        , url: 'command/config/alarmConfig.html'
                    }, {
                        title: '定时|定距拍照控制'
                        , id: 10009
                        , url: 'command/config/photoCtrlConfig.html'
                    }, {
                        title: '照片质量'
                        , id: 10010
                        , url: 'command/config/photoQualityConfig.html'
                    }, {
                        title: '车辆参数'
                        , id: 10011
                        , url: 'command/config/vehicleConfig.html'
                    }, {
                        title: 'GNSS设置'
                        , id: 10012
                        , url: 'command/config/gnssConfig.html'
                    }, {
                        title: 'CAN设置'
                        , id: 10013
                        , url: 'command/config/canConfig.html'
                    }, {
                        title: '音视频参数设置'
                        , id: 10014
                        , url: 'command/config/avConfig.html'
                    }, {
                        title: '音视频通道设置'
                        , id: 10015
                        , url: 'command/config/channelConfig.html'
                    }, {
                        title: '单独音视频参数设置'
                        , id: 10016
                        , url: 'command/config/singleAvConfig.html'
                    }, {
                        title: '特殊报警录像参数设置'
                        , id: 10017
                        , url: 'command/config/specialAlarmRecordConfig.html'
                    }, {
                        title: '视频报警屏蔽设置'
                        , id: 10018
                        , url: 'command/config/alarmMaskConfig.html'
                    }, {
                        title: '图像分析报警设置'
                        , id: 10019
                        , url: 'command/config/imageAnalysisAlarmConfig.html'
                    }, {
                        title: '终端休眠唤醒设置'
                        , id: 10020
                        , url: 'command/config/terminalWakeUpConfig.html'
                    }
                ]
            }, {
                title: '终端控制指令'
                , id: 2
                , spread: true
                , children: [
                    {
                        title: '无线升级'
                        , id: 20001
                        , url: 'command/ctrl/wirelessUpgrade.html'
                    }, {
                        title: '服务器连接切换'
                        , id: 20002
                        , url: 'command/ctrl/connectServer.html'
                    }, {
                        title: '终端功能控制'
                        , id: 20003
                        , url: 'command/ctrl/functionCtrl.html'
                    }, {
                        title: '车辆控制'
                        , id: 20004
                        , url: 'command/ctrl/vehicleCtrl.html'
                    }, {
                        title: '点名定位'
                        , id: 20005
                        , url: 'command/ctrl/position.html'
                    }, {
                        title: '临时跟踪'
                        , id: 20006
                        , url: 'command/ctrl/tracking.html'
                    }, {
                        title: '电话回拨'
                        , id: 2007
                        , url: 'command/ctrl/phoneCallback.html'
                    }, {
                        title: '链路检测'
                        , id: 2008
                        , url: 'command/ctrl/linkTest.html'
                    }
                ]
            }
            , {
                title: '多媒体指令'
                , id: 3
                , spread: true
                , children: [
                    {
                        title: '拍照'
                        , id: 30001
                        , url: 'command/media/takePhoto.html'
                    }, {
                        title: '多媒体文件检索'
                        , id: 30002
                        , url: 'command/media/mediaRetrieval.html'
                    }, {
                        title: '存储多媒体上传'
                        , id: 30003
                        , url: 'command/media/storageUpload.html'
                    }, {
                        title: '录音'
                        , id: 30004
                        , url: 'command/media/soundRecord.html'
                    }
                ]
            }, {
                title: '信息指令'
                , id: 4
                , spread: true
                , children: [
                    {
                        title: '文本信息下发'
                        , id: 40001
                        , url: 'command/info/textInfo.html'
                    }, {
                        title: '事件设置'
                        , id: 40002
                        , url: 'command/info/eventConfig.html'
                    }, {
                        title: '提问下发'
                        , id: 40003
                        , url: 'command/info/questionConfig.html'
                    }, {
                        title: '信息点播菜单设置'
                        , id: 40004
                        , url: 'command/info/menuConfig.html'
                    }, {
                        title: '信息服务'
                        , id: 40005
                        , url: 'command/info/infoService.html'
                    }, {
                        title: '设置电话本'
                        , id: 40006
                        , url: 'command/info/phoneBookConfig.html'
                    }, {
                        title: '数据下行透传'
                        , id: 40007
                        , url: 'command/info/passthroughData.html'
                    }, {
                        title: '平台RSA公钥'
                        , id: 40008
                        , url: 'command/info/publicKey.html'
                    }
                ]
            }, {
                title: '区域设置'
                , id: 5
                , spread: true
                , children: [
                    {
                        title: '设置圆形区域'
                        , id: 50001
                        , url: 'command/region/circleRegionConfig.html'
                    }, {
                        title: '设置矩形区域'
                        , id: 50002
                        , url: 'command/region/rectangleRegionConfig.html'
                    }, {
                        title: '设置多边形区域'
                        , id: 50003
                        , url: 'command/region/polygonRegionConfig.html'
                    }, {
                        title: '设置路线'
                        , id: 50004
                        , url: 'command/region/driveLineConfig.html'
                    }
                ]
            }, {
                title: '数据采集'
                , id: 6
                , spread: true
                , children: [
                    {
                        title: '行驶记录数据采集'
                        , id: 60001
                        , url: 'command/collection/drivingData.html'
                    }, {
                        title: '上报驾驶员身份信息请求'
                        , id: 60002
                        , url: 'command/collection/driverInfo.html'
                    }
                ]
            }, {
                title: '音视频资源'
                , id: 7
                , spread: true
                , children: [
                    {
                        title: '查询资源列表'
                        , id: 70001
                        , url: 'command/av/fileResources.html'
                    }
                ]
            }, {
                title: '主动安全'
                , id: 8
                , spread: true
                , children: [
                    {
                        title: '外设状态查询'
                        , id: 80001
                        , url: 'command/safety/queryStatus.html'
                    }, {
                        title: '高级驾驶辅助系统参数'
                        , id: 80002
                        , url: 'command/safety/adasConfig.html'
                    }, {
                        title: '驾驶员状态监测系统参数'
                        , id: 80003
                        , url: 'command/safety/dsmConfig.html'
                    },{
                        title: '胎压监测系统参数'
                        , id: 80004
                        , url: 'command/safety/tpmsConfig.html'
                    },{
                        title: '盲区监测系统参数'
                        , id: 80005
                        , url: 'command/safety/bsdConfig.html'
                    }
                ]
            }
        ]
    });
});

layui.use('form', function () {
    var form = layui.form;
    form.verify({
        new_number: [/(^$)|^\d+$/, '只能填写数字']
    });
});

/**
 * 发送JT808指令
 * @param url
 * @param dataField form表单数据
 * @param timeout 指令等待应答时间
 * @param callback
 * @returns {boolean}
 */
function sendJt808Command(url, paramEntity, timeout, callback) {
    var phoneNum = $("#commandPhoneNum").val();
    if (!phoneNum) {
        layer.msg("终端手机号不能为空", {icon: 5});
        return false;
    }

    //根据终端手机号查询终端信息
    var terminalInfo = queryTerminalInfo(phoneNum);
    if (!terminalInfo) {
        layer.msg("找不到终端手机号:" + phoneNum + "的信息", {icon: 5});
        return false;
    }

    var params = {
        terminalId: terminalInfo.id,
        responseTimeout: timeout,
        paramsEntity: paramEntity
    };
    $.ajax({
        type: "POST",
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        url: appName + url,
        data: params == null ? null : JSON.stringify(params),
        success: function (ret) {
            if (typeof callback == "function") {
                callback(ret);
                return;
            }

            if (ret.code == 0) {
                if (ret.data.sendResult == "SUCCESS") {
                    layer.msg("发送成功", {icon: 1});
                } else {
                    layer.msg("发送失败:" + ret.data.resultDesc, {icon: 5});
                }
            } else {
                layer.msg("发送失败:" + ret.msg, {icon: 5});
            }
        }
    });
}
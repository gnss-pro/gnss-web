appName = "/gnss-web";

/**
 * 发送请求
 * @param params
 * @param callback
 */
function sendRequest(btnName, url, params, method, clearResponseInfo, callback, errorCallback) {
    if (clearResponseInfo) {
        $("#responseInfo").html("");
    }
    var requestData = params;
    if (params != null && method != "get") {
        requestData = JSON.stringify(params);
    }
    $.ajax({
        url: appName + url,
        type: method,
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        data: requestData,
        beforeSend: function (request) {
        },
        success: function (ret) {
            if (typeof callback == "function") {
                callback(ret);
                return;
            }

            if (ret.code == 0) {
                var data = ret.data;
                layer.msg("响应成功:" + JSON.stringify(data), {icon: 1});
            } else {
                layer.msg("请求失败:" + ret.msg, {icon: 5});
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (errorCallback && typeof callback == "function") {
                callback(jqXHR, textStatus, errorThrown);
                return;
            }
            layer.msg("请求异常:" + errorThrown, {icon: 5});
        }
    });
}

/**
 * 根据终端手机号查询终端信息
 * @param terminalSimCode 终端手机号
 * @returns
 */
function queryTerminalInfo(terminalSimCode) {
    var url = "/api/v1/info/terminal/" + terminalSimCode;
    var terminalInfo = null;
    $.ajax({
        url: appName + url,
        type: 'get',
        async: false,
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        beforeSend: function (request) {
        },
        success: function (ret) {
            if (ret.code == 0 && ret.data) {
                terminalInfo = ret.data;
            }
        }
    });
    return terminalInfo;
}

$(document).ready(function () {
    //初始化日志表格
    var logGrid = null;
    initLogGrid();
    var util;
    layui.use(['util'], function () {
        util = layui.util;
    });

    //接口文档
    $("#docManage").click(function () {
        window.open("/gnss-web/swagger-ui.html");
    });

    //服务器配置
    $("#serverInfoManage").click(function () {
        layer.open({
            type: 2,
            title: '服务器配置信息',
            shadeClose: true,
            offset: '10%',
            area: ['65%', '80%'],
            content: 'serverInfoManage.html'
        });
    });

    /**
     * 初始化日志表格
     */
    function initLogGrid() {
        logGrid = new dhtmlXGridObject('gridbox');
        logGrid.setImagePath("js/dhtmlx/dhtmlxGrid/codebase/imgs/");
        logGrid.setHeader("接收时间,协议类型,消息方向,终端手机号,车牌号,车牌颜色,消息ID,消息流水号,消息体,错误消息", null, ["text-align:center;", "text-align:center;", "text-align:center;", "text-align:center;", "text-align:center;", "text-align:center", "text-align:center", "text-align:center", "text-align:center"]);
        logGrid.setSkin("dhx_skyblue");
        logGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
        logGrid.setInitWidths("120,80,80,100,80,80,180,80,*,100");
        logGrid.setColAlign("left,left,left,center,center,center,left,center,left,left");
        logGrid.enableRowsHover(true, 'grid_hover');
        logGrid.enableTooltips("true,true,true,true,true,true,true,true,true,true");
        logGrid.init();
    }

    /**
     * 添加终端信息
     */
    $("#createTerminalInfo").click(function () {
        var url = "/api/v1/info/terminal";
        var btnName = $(this).html();
        var params = {
            "phoneNum": $("#terminalSimCode").val(),
            "terminalNum": $("#terminalNum").val(),
            "vehicleNum": $("#vehicleNum").val(),
            "vehiclePlateColor": $("#vehicleColor").val()
        };
        sendRequest(btnName, url, params, "post", true, function (ret) {
            displayTerminalInfo(btnName, ret);
        });
    });

    /**
     * 查询终端信息
     */
    $("#queryTerminalInfo").click(function () {
        var btnName = $(this).html();
        var terminalSimCode = $("#terminalSimCode1").val();
        if (!terminalSimCode) {
            layer.msg("终端手机号不能为空", {icon: 5});
            return;
        }
        var url = "/api/v1/info/terminal/" + terminalSimCode;
        sendRequest(btnName, url, null, "get", true, function (ret) {
            displayTerminalInfo(btnName, ret);
        });
    });

    /**
     * 查询压力测试状态
     */
    $("#queryLoadTestInfo").click(function () {
        var url = "/api/v1/terminal/loadtest/loadTestInfo";
        var btnName = $(this).html();
        sendRequest(btnName, url, null, "get", true, function (ret) {
            displayLoadTestInfo(ret);
        });
    });

    /**
     * 重置位置数量
     */
    $("#resetLocationCount").click(function () {
        var url = "/api/v1/terminal/loadtest/resetLocationCount";
        var btnName = $(this).html();
        sendRequest(btnName, url, null, "get", true, function (ret) {
            displayLoadTestInfo(ret);
        });
    });

    /**
     * 查询最后位置
     */
    $("#queryLastLocation").click(function () {
        var btnName = $(this).html();
        var phoneNum = $("#phoneNum").val();
        if (!phoneNum) {
            layer.msg("终端手机号不能为空", {icon: 5});
            return;
        }
        var url = "/api/v1/terminal/loadtest/location/" + phoneNum;
        sendRequest(btnName, url, null, "get", true, function (ret) {
            if (ret.code == 0) {
                if (ret.data) {
                    var location = ret.data.location;
                    var content = '<div style="padding: 20px 40px;">'
                    content += "车牌号:" + ret.data.vehicleNum + "<br/>";
                    content += "车牌颜色:" + ret.data.vehiclePlateColor + "<br/>";
                    content += "终端手机号:" + ret.data.simNum + "<br/>";
                    content += "车辆状态:" + ret.data.vehicleStatus + "<br/>";

                    if (location) {
                        //报警标志格式化十六进制(4字节)
                        var alarmFlagHex = location.alarmFlag.toString(16).toUpperCase();
                        while (alarmFlagHex.length < 8) {
                            alarmFlagHex = "0" + alarmFlagHex;
                        }
                        //状态格式化十六进制(4字节)
                        var statusHex = location.status.toString(16).toUpperCase();
                        while (statusHex.length < 8) {
                            statusHex = "0" + statusHex;
                        }
                        content += "<br/>报警标志:0x" + alarmFlagHex + "<br/>";
                        content += "状态:0x" + statusHex + "<br/>";
                        content += "纬度:" + location.lat + "<br/>";
                        content += "经度:" + location.lon + "<br/>";
                        content += "高程:" + location.altitude + "<br/>";
                        content += "速度:" + location.speed + "<br/>";
                        content += "方向:" + location.direction + "<br/>";
                        content += "时间:" + location.gpsTime + "<br/>";
                        content += "附加数据:" + location.extraInfo + "<br/>";
                    } else {
                        content += "未上传位置" + "<br/>";
                    }
                    content += "</div>";
                    layer.open({
                        type: 1
                        , title: '位置详情'
                        , area: ['30%', '50%']
                        , content: content
                        , shade: 0
                    });
                } else {
                    layer.msg("未上传位置", {icon: 5});
                }
            } else {
                layer.msg("发送失败:" + ret.msg, {icon: 5});
            }
        });
    });

    /**
     * 查询轨迹
     */
    $("#queryTrack").click(function () {
        layer.open({
            type: 2,
            title: '查询轨迹',
            shadeClose: true,
            offset: '10%',
            area: ['95%', '80%'],
            content: 'trackManage.html'
        });
    });

    /**
     * 查询报警
     */
    $("#queryAlarms").click(function () {
        layer.open({
            type: 2,
            title: '报警管理',
            shadeClose: true,
            offset: '10%',
            area: ['95%', '80%'],
            content: 'alarmManage.html'
        });
    });

    /**
     * 查询多媒体文件
     */
    $("#queryMediaFiles").click(function () {
        layer.open({
            type: 2,
            title: '多媒体文件管理',
            shadeClose: true,
            offset: '10%',
            area: ['90%', '80%'],
            content: 'command/media/jt808MediaFileManage.html'
        });
    });

    /**
     * 查询FTP上传文件
     */
    $("#queryRecordFiles").click(function () {
        layer.open({
            type: 2,
            title: 'FTP录像文件管理',
            shadeClose: true,
            offset: '10%',
            area: ['90%', '80%'],
            content: 'command/av/recordFileManage.html'
        });
    });

    /**
     * 查询主动安全报警
     */
    $("#querySafetyAlarms").click(function () {
        layer.open({
            type: 2,
            title: '主动安全报警管理',
            shadeClose: true,
            offset: '10%',
            area: ['95%', '80%'],
            content: 'safetyAlarmManage.html'
        });
    });

    /**
     * 显示终端信息
     * @param btnName
     * @param ret
     */
    function displayTerminalInfo(btnName, ret) {
        if (ret.code == 0) {
            var data = ret.data;
            if (data) {
                var content = '<div style="padding: 20px 40px;">'
                content += "终端ID:" + data.id + "<br/>";
                content += "车牌号:" + data.vehicleNum + "<br/>";
                content += "车牌颜色:" + data.vehiclePlateColor + "<br/>";
                content += "终端手机号:" + data.phoneNum + "<br/>";
                content += "终端号码:" + data.terminalNum + "<br/>";
                content += "</div>";
                layer.open({
                    type: 1
                    , title: '终端信息'
                    , area: ['30%', '50%']
                    , content: content
                    , shade: 0
                });
            } else {
                layer.msg("未找到终端信息", {icon: 5});
            }
        } else {
            layer.msg("请求失败:" + ret.msg, {icon: 5});
        }
    }

    /**
     * 显示压力测试信息
     * @param ret
     */
    function displayLoadTestInfo(ret) {
        if (ret.code == 0) {
            var data = ret.data;
            var content = '<div style="padding: 20px 40px;">'
            content += "终端总数:" + data.terminalTotalCount + "<br/>";
            content += "终端在线数:" + data.terminalOnlineCount + "<br/>";
            content += "位置数量:" + data.locationCount + "<br/>";
            content += "</div>";
            layer.open({
                type: 1
                , title: '压力测试状态'
                , area: ['20%', '50%']
                , content: content
                , shade: 0
            });
        } else {
            layer.msg("请求失败:" + ret.msg, {icon: 5});
        }
    }

    function queryJt808ServerInfo(form) {
        var url = "/api/v1/global/jt808ServerInfo";
        sendRequest("", url, null, "get", true, function (ret) {
            if (ret.code == 0) {
                var uploadLog = false;
                if (ret.data) {
                    uploadLog = ret.data["uploadLog"] || false;
                }
                $('input[name=logBtn]').prop('checked', uploadLog);
                form.render('checkbox');
            } else {
                layer.msg("查询JT808服务器信息异常:" + ret.msg, {icon: 5});
            }
        });
    }

    layui.use(['form', 'jquery', 'layer'], function () {
        var form = layui.form;
        var jquery = layui.jquery;
        var layer = layui.layer;
        queryJt808ServerInfo(form);

        form.on('submit(logForm)', function (data) {
            var logStartTimeStr = $("#logStartTime").val();
            var logEndTimeStr = $("#logEndTime").val();
            var logStartTime = null;
            var logEndTime = null;
            if (logStartTimeStr) {
                logStartTime = new Date(logStartTimeStr).getTime();
            }
            if (logEndTimeStr) {
                logEndTime = new Date(logEndTimeStr).getTime();
            }
            if (logStartTime && logEndTime && logStartTime > logEndTime) {
                layer.msg("日志开始时间不能大于结束时间", {icon: 5});
                return false;
            }
            if (queryLogObj != null) {
                clearTimeout(queryLogObj);
            }
            logGrid.clearAll(false);

            //重新初始化日志表格
            initLogGrid();

            lastLogId = 0;
            queryLog();
            return false;
        });

        form.on('submit(deleteLog)', function (data) {
            var logStartTimeStr = $("#logStartTime").val();
            var logEndTimeStr = $("#logEndTime").val();
            var logStartTime = null;
            var logEndTime = null;
            if (logStartTimeStr) {
                logStartTime = new Date(logStartTimeStr).getTime();
            }
            if (logEndTimeStr) {
                logEndTime = new Date(logEndTimeStr).getTime();
            }
            if (logStartTime && logEndTime && logStartTime > logEndTime) {
                layer.msg("日志开始时间不能大于结束时间", {icon: 5});
                return false;
            }

            if (confirm("确定删除日志?")) {
                var url = "/api/v1/logs/jt808/batchDelete";
                var params = {
                    "phoneNum": $("#phoneNum1").val(),
                    "startTime": logStartTime,
                    "endTime": logEndTime
                };
                sendRequest("删除日志", url, params, "post", false, function (ret) {
                    if (ret.code === 0) {
                        layer.msg('删除成功', {icon: 1});
                        if (ret.data > 0) {
                            if (queryLogObj != null) {
                                clearTimeout(queryLogObj);
                            }
                            logGrid.clearAll(false);
                            //重新初始化日志表格
                            initLogGrid();
                            lastLogId = 0;
                            queryLog();
                        }
                    } else {
                        layer.msg('删除失败:' + ret.msg, {icon: 5});
                    }
                });
            }
            return false;
        });

        //监听提交
        form.on('switch(switchLog)', function (data) {
            var isUploadLog = data.elem.checked;
            var url = "/api/v1/commands/jt808/ctrl/uploadLog/" + isUploadLog;
            var btnName = "设置是否开启日志" + isUploadLog;
            $('input[name=logBtn]').prop('checked', !isUploadLog);
            form.render('checkbox');
            sendRequest(btnName, url, null, "post", true, function (ret) {
                if (ret.code == 0) {
                    $('input[name=logBtn]').prop('checked', isUploadLog);
                    form.render('checkbox');
                    layer.msg("设置成功", {icon: 1})
                } else {
                    layer.msg("设置是否开启日志失败," + ret.msg, {icon: 5});
                }
            });
        });
    });

    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#logStartTime'
            , lang: 'cn'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'
            , theme: '#1E9FFF'
        });

        laydate.render({
            elem: '#logEndTime'
            , lang: 'cn'
            , type: 'datetime'
            , format: 'yyyy-MM-dd HH:mm:ss'
            , max: 1
            , theme: '#1E9FFF'
        });
    });

    //最后一条日志ID
    var lastLogId = 0;
    var queryLogObj = null;

    //查询日志记录
    function queryLog() {
        var logStartTimeStr = $("#logStartTime").val();
        var logEndTimeStr = $("#logEndTime").val();
        var logStartTime = null;
        var logEndTime = null;
        if (logStartTimeStr) {
            logStartTime = new Date(logStartTimeStr).getTime();
        }
        if (logEndTimeStr) {
            logEndTime = new Date(logEndTimeStr).getTime();
        }
        if (logStartTime && logEndTime && logStartTime > logEndTime) {
            layer.msg("日志开始时间不能大于结束时间");
            //5秒轮询日志
            queryLogObj = setTimeout(function () {
                queryLog();
            }, 5000);
            return;
        }

        var url = "/api/v1/logs/jt808";
        var params = {
            "phoneNum": $("#phoneNum1").val(),
            "lastId": lastLogId,
            "startTime": logStartTime,
            "endTime": logEndTime,
            "page": 0,
            "size": 15
        };
        sendRequest("", url, params, "get", false, function (ret) {
            if (ret.code == 0) {
                var data = ret.data;
                if (data && data.content && data.content.length > 0) {
                    var jt808Log;
                    for (var i = 0; i < data.content.length; i++) {
                        jt808Log = data.content[i];
                        var msgIdStr = "";
                        if (jt808Log.msgIdDesc) {
                            msgIdStr += jt808Log.msgIdDesc;
                        }
                        if (jt808Log.msgIdHex) {
                            msgIdStr += "(" + jt808Log.msgIdHex + ")";
                        }
                        var errorMsg = "";
                        if (jt808Log.errorMsg) {
                            errorMsg = "<label style='color:red'>" + jt808Log.errorMsg + "</label>";
                        }
                        logGrid.addRow(jt808Log.id, [jt808Log.time, jt808Log.protocolType, jt808Log.linkType, jt808Log.phoneNumber, jt808Log.vehicleNum, jt808Log.vehiclePlateColor, msgIdStr, jt808Log.msgFlowId, jt808Log.msgBodyDesc, errorMsg]);
                        //保存最后一个日志记录的下标
                        if (jt808Log.id > lastLogId) {
                            lastLogId = jt808Log.id;
                        }
                        //选中每次查询后的最新日志记录
                        logGrid.selectRowById(lastLogId);
                    }
                    if (data.totalPages > 1) {
                        queryLog();
                        return;
                    }
                }
            } else {
                console.info("查询日志失败:" + ret.msg);
            }
            //5秒轮询日志
            queryLogObj = setTimeout(function () {
                queryLog();
            }, 5000);
        }, function (jqXHR, textStatus, errorThrown) {
            //5秒轮询日志
            queryLogObj = setTimeout(function () {
                queryLog();
            }, 5000);
        });
    }
});
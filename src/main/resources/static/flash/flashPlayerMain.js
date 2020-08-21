appName = "/gnss-admin";
var swfObj;
$(document).ready(function () {
    loadSwfObj();

    //改变窗口个数
    $("#winNum").change(function () {
        swfObj.setWindowNum(this.value);
    });

    $("#video").click(function(){
        if (swfObj != null && swfObj["startVideo"]) {
            queryMediaServerInfo(function(result) {
                var mediaServerIp = result.data.serverIp;
                var mediaServerPort = result.data.httpFlvPort;
                var simcard = $("#simcard").val();
                var streamType = $("#streamType").val();
                swfObj.startVideo(1, "", simcard, 0, streamType, true, mediaServerIp, mediaServerPort);
            });
        } else {
            alert("swf未加载完成");
        }
    });
});

//加载swf
function loadSwfObj() {
    setTimeout(function () {
        swfObj = document.getElementsByName("flashPlayer")[0];
        if (swfObj != null && swfObj["setWindowNum"]) {
            swfObj.setWindowNum(4);
        } else {
            loadSwfObj();
        }
    }, 1000);
}

/**
 * 查询流媒体配置信息
 * @param callback
 */
function queryMediaServerInfo(callback) {
    var url = "/api/v1/global/configs/mediaServerInfo";
    $.ajax({
        url: appName + url,
        type:'get',
        dataType:'json',
        contentType: "application/json;charset=UTF-8",
        data: null,
        success:function (ret) {
            if(typeof callback == "function"){
                callback(ret);
            }
        }
    });
}


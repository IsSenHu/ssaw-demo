$(function () {
    //初始化一个WebSocket对象
    const ws = new WebSocket("ws://10.13.30.226:8080" + baseUrl + "/admin/adminMonitor/" + adminId);
    //建立 web socket 连接成功触发事件
    ws.onopen = function () {
        //使用send方法发送数据
        if(ws && ws.readyState === 1) {
            console.log("连接已建立");
        }
    };

    //接收服务端数据时触发事件
    ws.onmessage = function (ev) {
        const data = ev.data;
        if(data === 'forcedReturnAdmin') {
            location.href = '/logout';
        }
    };

    //断开web socket 时触发事件
    ws.onclose = function () {
        console.log("连接断开");
    };
});
/**
 * dataTables中文支持
 * */
const Zh_Ch = {
    "sProcessing": "处理中...",
    "sLengthMenu": "显示 _MENU_ 项结果",
    "sZeroRecords": "没有匹配结果",
    "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
    "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix": "",
    "sSearch": "搜索:",
    "sUrl": "",
    "sEmptyTable": "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands": ",",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上页",
        "sNext": "下页",
        "sLast": "末页"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
};

/**
 * datePicker设置
 * */
const dateSetting = {
    "monthNames" : ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
    "dayNamesMin" : ['日','一','二','三','四','五','六'],
    "dateFormat" : "yy-mm-dd",
    "changeMonth" : true,
    "monthNamesShort" : ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月','十月','十一月','十二月'],
    "changeYear" : true,
    "yearRange" : '1950:2030'
};

/**
 * 重置input
 * */
function resetInput() {
    if($(this).hasClass("errorMessage")){
        $(this).css("color", "black").val("").removeClass("errorMessage");
    }else if(($(this).hasClass("errorPSMessage"))) {
        $(this).prop("type", "password").css("color", "black").val("").removeClass("errorPSMessage");
    }
}

/**
 * 显示校验错误信息
 * */
function showError(select, message) {
    if($(select).prop("type") === "password") {
        $(select).prop("type", "text").css("color", "red").val(message).addClass("errorPSMessage")
    }else {
        $(select).css("color", "red").val(message).addClass("errorMessage");
    }
    $(select).focus(resetInput);
}

/**
 * 显示消息 并重新加载页面
 * */
function showMsg(msg1) {
    $.Pop(msg1, {
        Title : "提示",
        Close : false,
        Btn:{
            yes:{
                vla : '确认',
                class : 'btn btn-primary',
                ope : function(){

                }}
        }
    });
}

/**
 * 显示消息
 * */
function showMsgNoReload(msg1) {
    $.Pop(msg1, 'alert', function () {

    });
}

function postAndGetData(url, data, fn) {
    $.ajax({
        type : 'post',
        url : url,
        contentType : 'application/json;charset=UTF-8',
        data : JSON.stringify(data),
        success : function (data) {
            fn(data);
        }
    });
}

function syncPostAndGetData(url, data, fn) {
    $.ajax({
        type : 'post',
        url : url,
        contentType : 'application/json;charset=UTF-8',
        async : false,
        data : JSON.stringify(data),
        success : function (data) {
            fn(data);
        }
    });
}

function getRealValue(select) {
    const obj = $(select);
    return (obj.hasClass("errorMessage") || obj.hasClass("errorPSMessage")) ? "" : obj.val();
}

$(function () {
    /** 收缩图标点击事件 */
    $(".navImg").click(function () {
        let $img = $("#navImg");
        if($img.attr('src').toString().indexOf('navLogLeft') !== -1) {
            $img.attr('src', BASE_URL + '/dist/img/navLogRight.png');
        } else {
            $img.attr('src', BASE_URL + '/dist/img/navLogLeft.png');
        }
    });
});
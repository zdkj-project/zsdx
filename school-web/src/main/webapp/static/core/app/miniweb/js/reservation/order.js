$(function () {

    var userId = getUrlParam("userId");
    if (null == userId || '' == userId) {
        $.toast("未获取到学生信息", "cancel", function () {
            window.location.href = '/static/core/app/miniweb/jsp/hint.jsp';
        });
    } else {
        $("#userId").val(userId);
        $.ajax({
            cache: false,
            type: "get",
            async: false,
            url: $("#contextPath").val() + "/app/Reservation/getOrderList",
            data: {
                userid: userId
            },
            success: function (obj) {
                if (null == obj || "" == obj) {
                    $.toast("未找到该用户", "cancel", function (toast) {
                        window.location.href = '/static/core/app/miniweb/jsp/hint.jsp';
                    });
                    return;
                }
                var jsonList = JSON.parse(obj);
                if (!jsonList.success && jsonList.success != undefined) {
                    $.toast("网页异常.请尝试重新登录", "cancel", function (toast) {
                        window.location.href = '/static/core/app/miniweb/jsp/500.html';
                    });
                    return;
                }
                var str = '';
                var day = '';
                var data = '';
                var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
                for (var i = 0; i <= jsonList.rows.length - 1; i++) {
                    var dt = new Date(jsonList.rows[i].dinnerDate);
                    day = weekDay[dt.getDay()];
                    data = JSON.stringify(dt.Format("yyyy年MM月dd日"));
                    data = data.substring(0, data.length - 1);
                    data = data.substring(1, data.length);
                    str += ' <div class="weui-cells__title" style="font-size: 15px;">' + data+'</div>';
                    //      str += ' <div class="weui-cells__title" style="font-size: 15px;">' + data + '<sapn style="margin-left: 40%;color:#66CCCC;">' ;
                //    str += (jsonList.rows[i].dinnerGroup == 0 ? "" : "<span data-value='"+jsonList.rows[i].uuid+"' onclick='remark(this)'>备注</span>&nbsp;&nbsp;|&nbsp;&nbsp;<span data-value='"+jsonList.rows[i].remark+"' onclick='lookRemark(this)'>查看备注</span>") + '</sapn></div> ';
                    str += '  <div class="weui-cells weui-cells_checkbox">';
                    str += '   <label class="weui-cell weui-check__label">';
                    str += '   <div class="weui-cell__hd">';
                    str += '   <input type="checkbox" class="weui-check"  data-order="' + jsonList.rows[i].uuid + '" name="ischeck" data-date="' + jsonList.rows[i].dinnerDate + '" id="ischeck' + i + '" value="' + jsonList.rows[i].userId + '"'+ (jsonList.rows[i].dinnerGroup == 1?"checked='true'":null)+'/>';
                    str += '   <i class="weui-icon-checked"></i>';
                    str += '   </div>';
                    str += '   <div class="weui-cell__bd"> ';
                    str += '   <p><span>' + day + '</span>' + (jsonList.rows[i].dinnerGroup == 0 ? "<span style='color: red; margin-left: 50%;'>未订餐</span>" : "<span style='color:#04BE02;margin-left: 50%;' >已订餐</span>") + '</p></div></label>  </div>';
                }
                $("#list").append(str);
            }

        });
    }

})

//备注
function remark(orderId) {
    var id = orderId.dataset.value;
    $.prompt({
        text: "输入您的订餐备注,最多可以输入一百个字",
        title: "订餐备注",
        onOK: function(text) {
            $.ajax({
                cache: false,
                type: "get",
                async: false,
                url: $("#contextPath").val() + "/app/Reservation/doUpdate",
                data: {
                    userid: id,
                    remark:text
                },
                success: function (obj) {
                    var data = JSON.parse(obj)[0];
                    if (data.code == 0) {
                        $.toast(data.text, "cancel");
                    } else {
                        $.toast(data.text, function () {
                            location.reload();
                        });
                    }
                }
            })
        }
    });
}

//查看备注

function lookRemark(remark) {
    var l = remark.dataset.value;
    if ('null'==l){
        l ="暂无备注";
    }
    $.alert(l);
}

//全选,设置chheckbox name='all' tbody id=tb
$("input[name=all]").click(function () {
    if (this.checked) {
        $("input[name=ischeck]").prop("checked", true);
        $("input[name=ischeck]:eq(0)").prop("checked", false);
    } else {
        $("input[name=ischeck]").prop("checked", false);
    }

});

//订餐
$("#order").click(function () {
    var form = new FormData($("#addForm")[0]);
    var date = $("input[name='ischeck']:checked");
    if (date.length <= 0) {
        $.toptip('请选择订餐日期', 'warning');
        return;
    }
    var dateArr = new Array();
    var userId  = date[0].value;
    for (var i = 0; i <= date.length - 1; i++) {
        dateArr[i] = date[i].dataset.date;
    }
    form.append("dateArr", dateArr);
    form.append("userId", userId);
    $.ajax({
        cache: false,
        type: "post",
        processData: false,
        contentType: false,
        data: form,
        url: $("#contextPath").val() + "/app/Reservation/addOrder",
        success: function (obj) {
            var data = JSON.parse(obj)[0];
            if (data.code == 0) {
                $.toast(data.text, "cancel");
            } else {
                $.toast(data.text, function () {
                    location.reload();
                });
            }
        }
    })
})

//取消订餐
$("#notOrder").click(function () {
    var form = new FormData($("#addForm")[0]);
    var date = $("input[name='ischeck']:checked");
    if (date.length <= 0) {
        $.toptip('请选择一条取消', 'warning');
        return;
    }
    var order = new Array();
    for (var i = 0; i <= date.length - 1; i++) {
        order[i] = date[i].dataset.order;
    }
    form.append("order", order);
    $.ajax({
        cache: false,
        type: "post",
        processData: false,
        contentType: false,
        data: form,
        url: $("#contextPath").val() + "/app/Reservation/doCancel",
        success: function (obj) {
            var data = JSON.parse(obj)[0];
            if (data.code == 0) {
                $.toast(data.text, "cancel");
            } else {
                $.toast(data.text, function () {
                    location.reload();
                });

            }
        }
    })
})


//获得跳转路径值
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

//日期格式化
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
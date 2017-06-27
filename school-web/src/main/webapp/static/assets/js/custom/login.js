function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0, index + 1);
    return result;
}

function addCookie(name, value, days, path) { /**添加设置cookie**/
    var name = escape(name);
    var value = escape(value);
    var expires = new Date();
    expires.setTime(expires.getTime() + days * 3600000 * 24);
    //path=/，表示cookie能在整个网站下使用，path=/temp，表示cookie只能在temp目录下使用  
    path = path == "" ? "" : ";path=" + path;
    //GMT(Greenwich Mean Time)是格林尼治平时，现在的标准时间，协调世界时是UTC  
    //参数days只能是数字型  
    var _expires = (typeof days) == "string" ? "" : ";expires=" + expires.toUTCString();
    document.cookie = name + "=" + value + _expires + path;
}

function getCookieValue(name) { /**获取cookie的值，根据cookie的键获取值**/
    //用处理字符串的方式查找到key对应value  
    var name = escape(name);
    //读cookie属性，这将返回文档的所有cookie  
    var allcookies = document.cookie;
    //查找名为name的cookie的开始位置  
    name += "=";
    var pos = allcookies.indexOf(name);
    //如果找到了具有该名字的cookie，那么提取并使用它的值  
    if (pos != -1) { //如果pos值为-1则说明搜索"version="失败  
        var start = pos + name.length; //cookie值开始的位置  
        var end = allcookies.indexOf(";", start); //从cookie值开始的位置起搜索第一个";"的位置,即cookie值结尾的位置  
        if (end == -1) end = allcookies.length; //如果end值为-1说明cookie列表里只有一个cookie  
        var value = allcookies.substring(start, end); //提取cookie的值  
        return (value); //对它解码        
    } else { //搜索失败，返回空字符串  
        return "";
    }
}

function deleteCookie(name, path) { /**根据cookie的键，删除cookie，其实就是设置其失效**/
    var name = escape(name);
    var expires = new Date(0);
    path = path == "" ? "" : ";path=" + path;
    document.cookie = name + "=" + ";expires=" + expires.toUTCString() + path;
}

window.onload = function() {
    var userName = getCookieValue("userName");
    document.getElementById("name").value = userName;
    var userPwd = getCookieValue("userPwd");
    document.getElementById("psw").value = userPwd;
}


//全局验证码变量

function login() {
    //判断验证码
    $.ajax({
        url: getContextPath() + '/verifycode/check',
        dataType: 'text',
        type: 'POST',
        data: { "verifyCode": $('#yzm').val() },
        success: function(str) {
            if (str == "true") {
            	
            	$("#loginMsk").fadeIn(50);	//显示遮罩
            	
                var userName = $('#name').val();
                var userPwd = $('#psw').val();

                $.ajax({

                    url: getContextPath() + '/login/login',
                    dataType: 'json',
                    type: 'POST',
                    data: {
                        userName: $('#name').val(),
                        userPwd: $('#psw').val(),
                        rememberMe: $("input[name='rememberMe']:checked")
                            .val()
                    },
                    success: function(xmlRequest) {
                        var returninfo = xmlRequest.result
                        if (returninfo == 1) {
                            if ($("#rememberMe").is(":checked")) {
                                //添加cookie  

                                addCookie("userName", userName, 7, "/");
                                addCookie("userPwd", userPwd, 7, "/");
                            }
                            document.location.href = getContextPath() + "/login/desktop";
                            //window.opener=null;
                            //window.close();
                            //window.open (getContextPath()+'/login/desktop','newwindow','width='+(window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+ ',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');

                            //window.open (getContextPath()+'/login/desktop','newwindow',"height=100,width=400,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
                        } else if (returninfo == -1) {
                            jAlert("用户名有误或已被禁用", "提示");
                            $("#loginMsk").fadeOut();
                        } else if (returninfo == -2) {
                            jAlert("密码错误", "提示");
                            $("#loginMsk").fadeOut();
                        } else {
                            jAlert("服务器错误", "提示");
                            $("#loginMsk").fadeOut();
                        }
                    },
                    error: function(XMLHttpRequest, textStatus, errorThrown) {
                        alert(errorThrown + textStatus);
                        $("#loginMsk").fadeOut();
                    }
                });
            } else {
                jAlert("验证码错误", "提示");
                //alert("验证码错误。");
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown + textStatus);
            $("#loginMsk").fadeOut();
        }
    });

}

function yz() {
    document.getElementById("yzz").src = document.getElementById("yzz").src + "?s=" + Math.random();
}

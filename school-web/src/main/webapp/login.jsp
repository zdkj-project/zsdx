<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="utf-8">
<title>学员综合服务系统</title>

<meta name="Resource-type" content="Document" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">

<link rel="shortcut icon" href="${contextPath}/static/core/resources/images/favicon.ico" type="image/x-icon">


<link rel="stylesheet" type="text/css" href="${contextPath}/static/core/resources/css/login.css"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/static/core/resources/css/jquery.alerts.css" />

<script type="text/javascript" src="${contextPath}/static/core/resources/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="${contextPath}/static/core/resources/js/login.js"></script>
<script type="text/javascript" src="${contextPath}/static/core/resources/js/jquery.alerts.js"></script>
<script type="text/javascript" src="${contextPath}/static/core/resources/js/base64.js"></script>

<script language="JavaScript"> 
	function keyLogin(){  
	  	if (event.keyCode==13) {                        
		//按Enter键的键值为13  
		     document.getElementById("input2").click(); 
		     //document.getElementById("yzz").click();
		 }
	//调用登录按钮的登录事件  
	}  
</script>

</head>

<body onkeydown="keyLogin();">
<div class="body_bg">
	<div class="mg">
		<div class="top_title">
			<img class="fl logo" src="${contextPath}/static/core/resources/images/login/index_logo.png">		
		</div>
	</div>
	<div class="contian_box">
		<div class="login_box">
			<div class="login_bg">
				<div class="fl box_lefet">
					<img src="${contextPath}/static/core/resources/images/login/login_img.png">
				</div>
				<div class="fl box_right">
					<div class="input_top">
						<label class="textName">用户名：</label><input value="" name="name"  id="name"  type="text" placeholder="请输入用户名" /></br>
						<label class="textName">密　码：</label><input value="" name="psw" id="psw" type="password" placeholder="请输入密码" /></br>
						<label class="textName">验证码：</label><input type="text" class="yzm" id="yzm" style='width: 80px;' placeholder="验证码" />
						<div class="yzm-m" id="yzm-m">
							<img src="${contextPath}/verifycode/image" alt="点击重新生成"
								id="yzz" onclick="yz();">
						</div>
					</div>
					
					<div class="lable_box">
						<div class="fl">
						 <input class="check" type="checkbox" name="rememberMe" id="rememberMe" /><label>记住帐号</label>
						</div>
						<label class="fr"><a  href="javascript:void(0)" style="color:red" onclick="yz();">换一张</a></label>
					</div>
					
					<div class="but_box">
						<a class="but"  id="input1" href="javascript:void(0)" onclick="document.getElementById('input2').click();">登 录</a>
						<button onclick="login();" id="input2" hidden="true">登 录</button>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div class="bottom_box">
		建议使用1280X768以上分辨率、IE8以上浏览器、谷歌浏览器、360浏览器浏览本站
	</div>
</div>
</body>

</html>














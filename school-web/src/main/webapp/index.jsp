<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE HTML>
<html manifest="">
<head>

    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=10, user-scalable=yes">

    <link rel="shortcut icon" href="${contextPath}/static/core/resources/images/favicon.ico" type="image/x-icon">
    
    <title>学员综合服务系统</title>

    <script id="microloader" data-app="a051ef01-338e-42d7-941d-23b348b014f9" type="text/javascript" src="${contextPath}/static/core/bootstrap.js"></script>
</head>
<body>

<link rel="stylesheet" type="text/css" href="${contextPath}/static/core/resources/css/icon.css" />
<link rel="stylesheet" type="text/css"	href="${contextPath}/static/core/resources/examples/shared/example.css" />

<!--script type="text/javascript"  src="${contextPath}/static/core/resources/examples/shared/examples.js"></script>
<script type="text/javascript"  src="${contextPath}/ueditor/ueditor.config.js"></script>
<script type="text/javascript"  src="${contextPath}/ueditor/ueditor.all.min.js"></script>
<script type="text/javascript"  src="${contextPath}/ueditor/ueditor.parse.min.js"></script>
-->

<script type="text/javascript">
    var isLogin="${SESSION_SYS_USER.userName}";

    if(!isLogin){
        document.location.href ="${contextPath}/login.jsp";
    }


    var ExtCommLoad=function(){  

        var isLoad;
        if(typeof(comm) == 'undefined'){      // Ext namespace won't be defined yet...
            isLoad=null; 
        }else{
            isLoad = comm; 
        }
    
        if(!isLoad){            
            setTimeout(ExtCommLoad,100);
        }else{
            /*在这里把comm数据写入*/
            //console.log(comm);                    
            <!--加载分辨率大小-->
            var clientWidth = document.body.clientWidth;
            var clientHeight = document.body.clientHeight;
            var screenWidth = document.body.scrollWidth;
            var screenHeight = document.body.scrollHeight;
            var resolutionHeight = window.screen.height;
            var resolutionWidth = window.screen.width;
            comm.add("clientWidth", clientWidth);
            comm.add("clientHeight", clientHeight);
            comm.add("screenWidth", screenWidth);
            comm.add("screenHeight", screenHeight);
            comm.add("resolutionWidth", resolutionWidth);
            comm.add("resolutionHeight", resolutionHeight);

            comm.add("userName","${SESSION_SYS_USER.userName}");
            comm.add("xm","${SESSION_SYS_USER.xm}");
            comm.add("globalRoleId","${SESSION_SYS_USER.sysRoles}");
        }
    }
    ExtCommLoad();

  
</script>
    
</body>
</html>

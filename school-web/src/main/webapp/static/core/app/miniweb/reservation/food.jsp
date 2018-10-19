<%--
  Created by IntelliJ IDEA.
  User: yz
  Date: 2018/9/18
  Time: 14:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../include/hender.jsp" %>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>微信订餐</title>
    <link rel="stylesheet" href="${contextPath}/static/core/app/miniweb/css/weui/weui2.css"/>
</head>
<body ontouchstart style="background-color: #f8f8f8;">
<input name="userId" id="userId" type="hidden" value="<%=request.getParameter("userid")%>"/>
<input id="contextPath" type="hidden" value="${contextPath}"/>
<div id="table" style="display:none;">

</div>
<header class='demos-header'>
    <h3 class="demos-title">微信订餐</h3>
</header>
<div class="weui-weixin-content" id="content" style="margin: -10% 4% 0 4%;"><!--内容-->
</div>
<table class="weui-table" id="weitable"  style="table-layout:fixed;" align="right">
</table>
<div style='  margin: 10%;text-align: center'>
    <a href="/static/core/app/miniweb/reservation/order.jsp?userId=${userid}" class="weui_btn  bg-blue weui_btn_inline">点击订餐</a>
</div>
<script type="text/javascript" src="/static/core/app/miniweb/js/reservation/food.js"></script>
</body>
</html>

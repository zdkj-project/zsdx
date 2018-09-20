<%--
  Created by IntelliJ IDEA.
  User: yz
  Date: 2018/9/19
  Time: 9:05
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
<input name="userId" id="userId" type="hidden"/>
<input id="contextPath" type="hidden" value="${contextPath}"/>

<div class="weui-cells_checkbox" style="border-top: 0px solid #e5e5e5;background-color: #f8f8f8;">
    <label class="weui-cell weui-check__label" style="width: 25%">
        <div class="weui-cell__hd"><input type="checkbox" class="weui-check" name="all"> <i
                class="weui-icon-checked"></i></div>
        <div class="weui-cell__bd" style="color:#04BE02;">全选</p>

        </div>
    </label></div>
<div class="weui-cell__bd" style="margin: -9% 0 0 60%;width: 30%;" id="order">
    <img src="/static/core/app/miniweb/img/reservation/2.png" style=" margin-bottom: -4%;" width="17%"> 订餐
</div>
<div class="weui-cell__bd" style="margin: -6% 0 0 75%;"  id="notOrder">
    <img src="/static/core/app/miniweb/img/reservation/3.png" style=" margin-bottom: -4%;" width="17%"> 取消订餐
</div>
<form id="addForm">
    <div id="list"></div>
</form>
</body>
<script type="text/javascript" src="/static/core/app/miniweb/js/reservation/order.js"></script>
</html>

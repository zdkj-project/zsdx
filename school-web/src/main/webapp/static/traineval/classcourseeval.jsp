<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html>
<head>
  <meta charset="utf-8">
  <title>班级课程列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <link rel="stylesheet" href="layui/css/layui.css"  media="all">


  <style type="text/css">
    body{
      font-family: 微软雅黑;
    }
    .layui-table-cell {
        height: auto;
        line-height: unset;
        padding: 0px 5px;
    }

    .container {
        width: 100%;
        margin: 0 auto;
    }
    @media screen and (min-width: 1200px){
      .container {
          width: 1200px;
          margin: 0 auto;
      }
    }
    #className{
      text-align: center;
      display: block;
      font-size: 18px;
      padding: 10px 10px 5px 10px;
      color: #84120a;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

  </style>

</head>
<body>
  <div class="container">
    <span id="className">2018年中山市第三期科级公务员任职培训班</span>
    <table class="layui-hide" id="test" lay-filter="test"></table>
  </div>
<script src="layui/layui.js" charset="utf-8"></script>
 
<script>
layui.use('table', function(){
  var table = layui.table;
  
  var classId="${param.classId}";
  var className="${param.className}";
  document.getElementById("className").innerHTML=className;

  table.render({
    elem: '#test'
    ,url:'/app/traineval/listClassEvalCourse?classId='+classId
    ,page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
      layout: ['limit', 'count', 'prev', 'page', 'next', 'skip']//自定义分页布局
      //,curr: 5 //设定初始在第 5 页
      ,groups: 1 //只显示 1 个连续页码
      ,first: false//不显示首页
      ,last: false //不显示尾页
      
    }
    ,height: 'full-60' //高度最大化减去差值
    ,cols: [[
        {field:'courseName', minWidth: 200,title: '课程名称',sort: true,templet: '#titleTpl'}
        ,{fixed: 'right', width:70, align:'center', toolbar: '#barDemo'} //这里的toolbar值是模板元素的选择器
    ]]
    
  });

  //监听工具条
  table.on('tool(test)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的DOM对象

    if(layEvent === 'edit'){ //编辑
        //弹出链接地址框
        var evalUrl = "http://" + window.location.host + "/static/traineval/courseeval.jsp?courseId=" + data.classScheduleId;
        window.open(evalUrl);
    }
  });

});
</script>

<script type="text/html" id="titleTpl">
    <span style="color:#af1408">{{d.courseName}}</span>
    <br/>
    <span style="font-size: 12px;">时间：{{d.courseDate}} {{d.courseTime}}</span><span style="font-size: 12px;padding-left: 10px;">{{d.teacherName}}</span>
    
</script>

<script type="text/html" id="barDemo">

  {{#  if(d.isOver == 1 && d.evalState==1){ }}
    
     <a class="layui-btn" style=" font-size: 12px;padding: 0px 10px;line-height: 26px;height: 26px;background-color:#c31c10" lay-event="edit">评价</a>

  {{#  } else if(d.evalState==0) { }}
    <span style="color:red;font-size: 12px;">待开启评价</span>
  {{#  } else if(d.evalState==2) { }}
    <span style="color:green;font-size: 12px;">评价已结束</span>
  {{#  } else { }}
    <span style="font-size: 12px;">暂未上课</span>
  {{#  } }}
  
</script>


</body>
</html>
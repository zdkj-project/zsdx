<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>教学评估表</title>
		<link rel="stylesheet" type="text/css" href="dangxiao.css">
	</head>

	<body>

		<div class="container" id="evalStandForm">
			<!--表头-->
			<ul>
				<li class="t1" id="classCategory">&nbsp;</li>
				<li class="t2">教学评估表</li>
			</ul>
			<!--评估基本信息-->
			<ul class="itemUl">
				<li class="itemLi">评估基本信息</li>
			</ul>
			<div class="itemInfo">
				<ul>
					<li>课程名称：
						<a id="courseName">&nbsp;</a>
					</li>
					<li>课程类型：
						<a id="teachTypeName">&nbsp;</a>
					</li>
					<li>授课老师：
						<a id="teacherName">&nbsp;</a>
					</li>
				</ul>
			</div>
			<!--评估内容-->
			<ul class="itemUl">
				<li class="itemLi">评估内容</li>
			</ul>

			<!--'教學內容'-->
			<!--<ul class="s1">
					<li class="s2">教学内容:</li>
				</ul>
				
				<div>
					<li class="s3">1、紧密联系实际，针对性、实用性、系统性强;内容丰富，前瞻性、新颖性、时效性强。</li>
					<ul class="radioUl">
						<li class="radioLi"><a class="radioA" data-val="henmanyi">很满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="manyi">满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="jibenmanyi">基本满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="bumanyi">不满意</a></li>
						<div style="clear: both;"></div>
					</ul>
				</div>				
				
				
				<ul class="b1">
					<li class="b2">教学效果:</li>
				</ul>
				<div>
						<li class="b3">2、激发学习兴趣，启发思维，课堂气氛活跃；开阔视野、拓宽思路、增长知识，实用性强。</li>
						<ul class="radioUl">
						<li class="radioLi"><a class="radioA" data-val="henmanyi">很满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="manyi">满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="jibenmanyi">基本满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="bumanyi">不满意</a></li>
						<div style="clear: both;"></div>
						</ul>
				</div>
				
				<ul class="x1">
					<li class="x2">教学水平：</li>
				</ul>
				<div>
						<li class="x3">3、运用新的教学理念、手段；教学方法多样化；教学态度认真，思路清晰、表达生动。</li>
						<ul class="radioUl">
						<li class="radioLi"><a class="radioA" data-val="henmanyi">很满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="manyi">满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="jibenmanyi">基本满意</a></li>
						<li class="radioLi"><a class="radioA" data-val="bumanyi">不满意</a></li>
						<div style="clear: both;"></div>
						</ul>
				</div>
				<ul class="yj">
					<li class="y1">意见建议：</li>
				</ul>
				<textarea class="shuru" name="" placeholder="请输入您的意见建议..."></textarea>
				<input type="button" id="submit" value="提交" />-->
		</div>
	</body>

	<script src="flexible.js" type="text/javascript" charset="utf-8"></script>
	<script type="text/javascript" src="jquery-3.2.1.js"></script>
	<script src="layer/layer.js"></script>
	<script type="text/javascript">
		$(function() {

			var datas = {};
			var courseId="${param.courseId}";
			//var myLocation=location.protocol+ location.host;

			//显示界面数据
			$.ajax({
		        type: 'GET',
		        url:'/app/traineval/getCourseEvalStand?courseId='+courseId,
		        data: '',
		        //dataType: 'jsonp',//如果是异步使用jsonp
		        success: function(result){
		       		datas=result;

		       		//判断数据是否返回成功
			        if(datas.success) {		

				        var courseInfo = datas.evalCourse;
				        var evalStands = datas.evalStand;

				        //单独设置某些数据
				        $("#classCategory").html(courseInfo.classCategory);
				        $("#courseName").html(courseInfo.courseName);
				        $("#teachTypeName").html(courseInfo.teachTypeName);
				        $("#teacherName").html(courseInfo.teacherName);


				        //循环拼接list数据
				        var html = "";
						for(i = 0; i < evalStands.length; i++) {
							var es = evalStands[i];

							html += '<ul class="s1">' +
								'<li class="s2">' + es.indicatorName + '</li>' +
								'</ul>' + '<div>' +
								'<li class="s3">'+ es.indicatorStand + '</li>' +
								'<ul class="radioUl">' +
								'<li class="radioLi">' + '<a class="radioA radio' + i + ' selected" data-val="verySatisfactioncount">很满意</a></li>' +
								'<li class="radioLi">' + '<a class="radioA radio' + i + '" data-val="satisfactioncount">满意</a></li>' +
								'<li class="radioLi">' + '<a class="radioA radio' + i + '" data-val="basSatisfactioncount">基本满意</a></li>' +
								'<li class="radioLi">' + '<a class="radioA radio' + i + '" data-val="noSatisfactioncount">不满意</a></li>' +
								'<div style="clear: both;"></div>' +
								'</ul>' +
								'</div>'
						}
						html += '<ul class="itemUl">' +
							'<li class="y1">意见建议：</li>' +
							'</ul>';
						html += '<div class="bottomDiv"><textarea class="shuru" name="" placeholder="请输入您的意见建议..."></textarea>' +
							'<input type="button" id="submit" value="提交" /> </div>';
						$("#evalStandForm").append(html);
				 		

				 		//创建a标签点击事件【要等待元素写入到html中，所以放在这里执行】
				 		$("a.radioA").click(function() {
							var ul = $(this).parent("li.radioLi").parent("ul.radioUl")[0];
							$(ul).find("a.radioA").removeClass("selected");
							$(this).addClass("selected");
						})



				 		//提交事件【要等待元素写入到html中，所以放在这里执行】
				 		function submitFun(){
				 			
				 			var storage = window.localStorage;
							var uuid = courseInfo.classScheduleId;

							//当用户未评价此课程时，才允许评价
							if(!storage.getItem(uuid) || storage.getItem(uuid) != 1) {
								
								//组装后台数据
								var paramter = []; //声明数组的方式							
								for(var i = 0; i < evalStands.length; i++) {
									var obj = {};
									var es = evalStands[i];

									obj.classScheduleId = uuid;
									obj.courseId = courseInfo.courseId;
									obj.courseName = courseInfo.courseName;
									obj.indicatorId = es.indicatorId;
									obj.indicatorName = es.indicatorName;
									obj.standId = es.uuid;
									obj.indicatorStand = es.indicatorStand;
									obj.verySatisfactioncount = 0;
									obj.satisfactioncount = 0;
									obj.basSatisfactioncount = 0;
									obj.noSatisfactioncount = 0;

									var value = $($(".radio"+i+".selected")[0]).attr("data-val");
									obj[value] = 1;

									obj.advise = $(".shuru").val();
									paramter.push(obj);
								}

								//console.log(paramter);
								//console.log(JSON.stringify(paramter));
								//storage.setItem(uuid, 1);
								//alert("评价成功");
								$.ajax({
									type:"post",
									url:"/app/traineval/saveCourseEval",
									async:true,
									data:{eval:JSON.stringify(paramter)},
									success:function(data){
										console.log(data);
										if(data.success==true){
											storage.setItem(uuid,1);
											//alert("评价成功");
											layer.open({
						                        title: [
						                          '评价成功',
						                          'background-color: #FF4351; color:#fff;'
						                        ],
						                        content: '<span style="color:#c61414">感谢您参与此门课程的评价！</span>',
						                        shadeClose: false,
						                        btn: ['确定']
						                    });
										}else{
											//alert("评价失败！");
											layer.msg('评价失败！');
											storage.setItem(uuid,0);
										}
									},
									error:function(){
										//alert("请求失败，请稍后重试！");	
										layer.msg('请求失败，请稍后重试！');	
										storage.setItem(uuid,0);
									}
								});

							} else {
								//alert("不能重复评价");
								layer.msg('您已评价，不需重复评价！');
							}

							//重新绑定事件，恢复点击功能
							$("#submit").click(function() {		
					 			$("#submit").unbind();	//解除事件，防止连续点击	 				
								submitFun();							
							});
				 		}

				 		$("#submit").click(function() {		
				 			$("#submit").unbind();	//解除事件，防止连续点击	 				
							submitFun();							
						});
			        }else{
			        	layer.msg("请求数据失败，请稍后重试！");
			        }	       
		        },
		        error:function(){
		        	layer.msg("请求数据失败，请稍后重试！");
		        }
        	});

		})
	</script>
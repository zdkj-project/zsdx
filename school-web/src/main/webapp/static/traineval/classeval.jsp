<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>教学评估表</title>
		<link rel="stylesheet" type="text/css" href="gerenpj.css"/>
	</head>

	<body>

		<div class="container" id="evalStandForm">
			<!--表头-->
			<ul>
				<li class="t1" id="className">&nbsp</li>
				<li class="t2">教学评估表</li>
			</ul>

			<ul class="itemUl">
				<li class="itemLi">评估基本信息</li>
			</ul>
			<div class="itemInfo">
				<ul>
					<li>主办单位：<span id="holdUnit">&nbsp</span></li>
					<li>承办单位：<span id="undertaker">&nbsp</span></li>
					<li>学员人数：<span id="trainees">0</span>人</li>
					<li>培训时间：
						<a class="beginDate">2017年4月12日</a> 至
						<a class="endDate">2017年4月27日</a> 共
						<a class="trainDays"> 12天</a>天</li>
					<li class="teacher">班主任：
						<a class="bzr"></a>
					</li>
					<li class="telephone">联系电话:
						<a class="mobile"></a>
					</li>
				</ul>
			</div>

			<ul class="itemUl">
				<li class="itemLi">评估内容</li>
			</ul>

			<!--<ul class="s1">
				<li class="s2">培训实施:</li>
			</ul>
			<div>
				<li class="s3">1、课程设置、内容安排科学合理</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>
			<div>
				<li class="s4">2、优选师资、配备适当</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>
			<div>
				<li class="s5">3、教学流程控制严谨有序、张弛有度，讲究实效</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>
			<div>
				<li class="s6">4、班主任工作认真负责，管理细致，服务热情</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>

			<ul class="b1">
				<li class="b2">培训保障:</li>
			</ul>
			<div>
				<li class="b3">5、教学设施、培训资源满足需求，食宿卫生安全</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>
			<div>
				<li class="b4">6、服务管理严格规范，后勤保障到位</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>

			<ul class="x1">
				<li class="x2">培训效果：</li>
			</ul>
			<div>
				<li class="x3">7、有效提升党性修养、文化素养等综合素质</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>
			<div>
				<li class="x4">8、有效提高政策理论水平、业务工作能力</li>
				<ul class="radioUl">
					<li class="radioLi">
						<a class="radioA" data-val="henmanyi">很满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="manyi">满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="jibenmanyi">基本满意</a>
					</li>
					<li class="radioLi">
						<a class="radioA" data-val="bumanyi">不满意</a>
					</li>
				</ul>
			</div>
			
			<div class="foot">	
			<ul class="yj">
				<li class="y1">意见建议：</li>
			</ul>
			<textarea class="shuru" name="" placeholder="请输入您的意见建议..."></textarea>
			<input type="button" id="submit" value="提交" />
            </div>
            -->
		</div>
	</body>

	<script type="text/javascript" src="jquery-3.2.1.js"></script>
	<script type="text/javascript">
		$(function() {

			var datas = {};
			var classId="${param.classId}";
			//var myLocation=location.protocol+ location.host;

			//显示界面数据
			$.ajax({
		        type: 'GET',
		        url:'/app/traineval/getClassEvalStand?classId='+classId,
		        data: '',
		        //dataType: 'jsonp',//如果是异步使用jsonp
		        success: function(result){
		       		datas=result;

		       		//判断数据是否返回成功
			        if(datas.success) {		

				        var classInfo = datas.evalClass;
						var evalStands = datas.evalStand;

				        //单独设置某些数据
				        $("#className").html(classInfo.className);
				        $("#holdUnit").html(classInfo.holdUnit);
				        $("#holdUnit").html(classInfo.holdUnit);
				        $("#holdUnit").html(classInfo.holdUnit);
				        $(".beginDate").html(classInfo.beginDate);
						$(".endDate").html(classInfo.endDate);
						$(".trainDays").html(classInfo.trainDays);
						$(".bzr").html(classInfo.bzr);
						$(".mobile").html(classInfo.mobile);


				        //循环拼接list数据
				        var html = "";
						var indicatorId="";
						var index=1;
						for(i = 0; i < evalStands.length; i++) {
							var es = evalStands[i];
							
							if(es.indicatorId!=indicatorId){
								html += '<ul class="s1">'+
								'<li class="s2">' + es.indicatorName + '</li>' +
								'</ul>';
								index=1;
							}

							html +='<div>' +
							'<li class="s3">'+(index++)+". " + es.indicatorStand + '</li>' +
							'<ul class="radioUl">' +
							'<li class="radioLi">' + '<a class="radioA radio' + i + ' selected" data-val="verySatisfactioncount">很满意</a></li>' +
							'<li class="radioLi">' + '<a class="radioA radio' + i + '" data-val="satisfactioncount">满意</a></li>' +
							'<li class="radioLi">' + '<a class="radioA radio' + i + '" data-val="basSatisfactioncount">基本满意</a></li>' +
							'<li class="radioLi">' + '<a class="radioA radio' + i + '" data-val="noSatisfactioncount">不满意</a></li>' +
							'<div style="clear: both;"></div>' +
							'</ul>' +
							'</div>';
							
							indicatorId=es.indicatorId;
						}
						html += '<ul class="yj">' +
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
				 		function submitFun() {
					 			
							var storage = window.localStorage;
							var uuid = classInfo.classId;
	
							//当用户未评价此课程时，才允许评价
							if(!storage.getItem(uuid) || storage.getItem(uuid) != 1) {
								
								//组装后台数据
								var paramter = []; //声明数组的方式							
								for(var i = 0; i < evalStands.length; i++) {
									var obj = {};
									var es = evalStands[i];
		
									obj.classId = classInfo.classId;							
									obj.standId=es.uuid;
									obj.indicatorStand = es.indicatorStand;
									obj.indicatorName = es.indicatorName;
									obj.indicatorId = es.indicatorId;
									obj.verySatisfactioncount = 0;
									obj.satisfactioncount = 0;
									obj.basSatisfactioncount = 0;
									obj.noSatisfactioncount = 0;
		
									var value = $($(".radio"+i+".selected")[0]).attr("data-val");
									obj[value] = 1;
		
									obj.advise = $(".shuru").val();
									paramter.push(obj);
								}

								//提交数据到后台
								$.ajax({
									type:"post",
									url:"/app/traineval/saveClassEval",
									async:true,
									data:{eval:JSON.stringify(paramter)},
									success:function(data){
										//console.log(data);
										if(data.success==true){
											storage.setItem(uuid,1);
											alert("评价成功");
										}else{
											alert("评价失败！");
											storage.setItem(uuid,0);
										}

										//重新绑定事件，恢复点击功能
										$("#submit").click(function() {		
								 			$("#submit").unbind();	//解除事件，防止连续点击	 				
											submitFun();							
										});

									},
									error:function(){
										alert("请求失败，请稍后重试！");		
										storage.setItem(uuid,0);

										//重新绑定事件，恢复点击功能
										$("#submit").click(function() {		
								 			$("#submit").unbind();	//解除事件，防止连续点击	 				
											submitFun();							
										});
									}
								});

							} else {
								alert("不能重复评价");
								//重新绑定事件，恢复点击功能
								$("#submit").click(function() {		
						 			$("#submit").unbind();	//解除事件，防止连续点击	 				
									submitFun();							
								});
							}
							
					 	}

				 		$("#submit").click(function() {		
				 			$("#submit").unbind();	//解除事件，防止连续点击	 				
							submitFun();			
							return false;				
						});
			        }else{
			        	alert("请求数据失败，请稍后重试！");
			        }	       
		        },
		        error:function(){
		        	alert("请求数据失败，请稍后重试！");
		        }
        	});

		})
	</script>
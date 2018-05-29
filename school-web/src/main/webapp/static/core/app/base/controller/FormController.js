/**
 * 表单控制器
 */
Ext.define("core.base.controller.FormController",{
	extend:"Ext.app.Controller",
	requires:[
		"core.base.view.form.BaseComboBox",
		"core.base.view.form.BaseQueryField",
		"core.base.view.form.BaseTreeField"
	],
	initForm:function(){

		var self=this;
		var formCtr={
			"baseform":{
				render:function(form){
					var basePanel=form.up("basepanel");
					if(basePanel!=null){
						var funCode=basePanel.funCode;
						form.funCode=funCode;	
					}
							
					//史诗级bug，千万不要在这里设置itemId
					//form.itemId=funCode+"_baseform";	
				}
			},
			"basequeryform field":{
				specialkey: function (field, e) {
                    if (e.getKey() == e.ENTER) {
                        //得到组件                 
                        //self.Info("暂时无法搜索！");                    
	                    var queryPanel = field.up("basequeryform");
						var querySql1 = self.getQuerySql(queryPanel);
						var querySql = self.getQureyFilter(queryPanel);
						var funCode = queryPanel.funCode;
						var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");
						var baseFilter = basePanel.funData.filter;
						if(baseFilter){
							if (baseFilter.length > 0) {
								if (querySql.length > 0) {
									ss = baseFilter.substring(1, baseFilter.length);
									yy = querySql.substring(0, querySql.length - 1);
									querySql = yy + "," + ss;
								} else {
									querySql = baseFilter;
								}
							}
						}
						var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
						var store = baseGrid.getStore();
						var proxy = store.getProxy();
						proxy.extraParams.querySql = querySql1;
						proxy.extraParams.filter = querySql;
						store.loadPage(1);
                    }
                }
			},
			"basequeryform button[ref=gridSearchFormOk]":{
				click:function(btn){		
                    //self.Info("暂时无法搜索！");                    
                    var queryPanel = btn.up("basequeryform");
					var querySql1 = self.getQuerySql(queryPanel);
					var querySql = self.getQureyFilter(queryPanel);
					var funCode = queryPanel.funCode;
					var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");

					/* 此处的此属性，不再使用
					var baseFilter = basePanel.funData.filter;
					if(baseFilter){
						if (baseFilter.length > 0) {
							if (querySql.length > 0) {
								ss = baseFilter.substring(1, baseFilter.length);
								yy = querySql.substring(0, querySql.length - 1);
								querySql = yy + "," + ss;
							} else {
								querySql = baseFilter;
							}
						}
					}
					*/

					//加入basegrid默认的filter
					var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
					var gridFilter=[];
					var filter=[];

					//获取baseGrid中编写的默认filter值
                    var gridFilterStr=baseGrid.extParams.filter;
                    if(gridFilterStr&&gridFilterStr.trim()!=""){
                        gridFilter=JSON.parse(gridFilterStr); 
                    }

					if (querySql.trim().length > 0) {
						filter=JSON.parse(querySql);  
					
						for(var i=0;i<gridFilter.length;i++){
	                    	//判断gridFilter是否包含此值。
	                    	var isExist=false;
	                    	for(var j=0;j<filter.length;j++){
	                    		if(filter[j].field==gridFilter[i].field){                   
		                            isExist=true;
		                            break;
		                        }
	                    	}
	                    	if(isExist==false)
	                        	filter.push(gridFilter[i]);
	                 
	                    }
					}else{
						if(gridFilter.length>0){
							filter=gridFilter;
						}
					}    
                    
                                                
					var store = baseGrid.getStore();
					var proxy = store.getProxy();
					proxy.extraParams.querySql = querySql1;
					proxy.extraParams.filter = JSON.stringify(filter);
					store.loadPage(1);
                   
				}
			},
			"basequeryform button[ref=gridSearchFormReset]":{
				click:function(btn){
					//得到组件
                    //btn.up("form").reset();
                    //zzk 2017-4-5 修改					
                    var queryPanel=btn.up("basequeryform");
					self.resetQueryPanel(queryPanel);

					var funCode = queryPanel.funCode;
					var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");
					var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
					var store = baseGrid.getStore();
					var proxy = store.getProxy();

					//重置时，使用MainLayout中默认的funData.filter参数。			
					//var baseFilter = basePanel.funData.filter; 	//此属性不再使用
					
					var filterStr=[];
					//获取baseGrid中编写的默认filter值
                    var gridFilterStr=baseGrid.extParams.filter;
					if(gridFilterStr&&gridFilterStr.trim().length>0){
						filterStr=gridFilterStr;
					}
				
					//只重置这两个数据，还有一个extParams参数是来自baseGrid中设置的，不用去改变。
					proxy.extraParams.querySql = "";		//此属性也没有实际效果
					proxy.extraParams.filter = filterStr;
					store.load();
				}
			},

		}
		Ext.apply(self.ctr,formCtr);
	}
	
});
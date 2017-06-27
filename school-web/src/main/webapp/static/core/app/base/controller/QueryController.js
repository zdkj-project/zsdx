/**
 * 查询组件控制器
 */
Ext.define("core.base.controller.QueryController",{
	extend:"Ext.app.Controller",
	initQuery:function(){
		var self=this;
		var queryCtr={
			"mttreeview":{
				/**
				 * 注册树形的选中事件，选中指定父节点和子节点
				 */
				checkchange:function(node,checked,eOpts){
					//node.expand(true);
					
					node.expand();//只展开第一层
					//递归选中父节点
					var eachParent = function(node,checked){
                            if(!node.isRoot() && checked == true){
                                if(!Ext.isEmpty(node.get('checked'))){
                                    node.set('checked',checked);
                                    node.commit();
                                }
                                eachParent(node.parentNode,checked);
                            }
                        }
                      eachParent(node.parentNode,checked);
                      //递归选中孩子节点
                      var eachChild = function(node,checked){
                        node.eachChild(function(n){
                            if(!Ext.isEmpty(n.get('checked'))){
                                n.set('checked',checked);
                                n.commit();
                            }
                            eachChild(n,checked);
                        });
                    	};
                    	eachChild(node,checked);
				},
				//注册树形render事件
				render:function(tree){				
					var store=tree.getStore();
					tree=tree.up("mtsswinview").down("mttreeview");
					store.treeObj=tree;
				},
				/**
				 *tree数据加载后的事件
				 */
				load:function(store){
					var tree=store.treeObj;
					var win=tree.up("mtsswinview");				
					var renderTree=win.renderTree;
					if(renderTree){
						renderTree(tree);
					}
				}
			},
			/**
			 * 查询组件选中事件
			 */
			'mttreeview combobox[ref=queryTreeCBB]' : {
				select : function( field, value ){
						var id = value[0].data.id;//根据id
						var tree = field.up('mttreeview');
						var node = tree.getStore().getNodeById(id);
						if(node){
			   				if(tree.multiSelect == true){
			   					tree.expandPath(node.getPath());
			   					node.set('checked',true);
			   					node.commit();
			   				}else{
			   					tree.selectPath(node.getPath());
			   				}
							
						}
				}
			},
			/**
			 * 确定按钮事件
			 */
			"mtsswinview button[ref=ssOkBtn]":{
				click:function(btn){
					var win=btn.up("mtsswinview");
					//树形查询处理
					if(win.queryType=="mttreeview"){
						var tree=win.down("mttreeview");
						var selRecords=new Array();
						var records=tree.getChecked();
						if(records.length<=0){
							records=tree.getSelectionModel().getSelection();
						}
						//处理记录是否禁用
						Ext.each(records,function(rec){
							if(!rec.raw.disabled){
								selRecords.push(rec);
							}
						});
						if(selRecords.length>0 || win.isEmpty){
							win.callback(win,selRecords);
							win.close();
						}else{
							alert("你选中的信息错误，请重新选择!");
						}
					}
				}
			},
			/**
			 * 取消按钮
			 */
			"mtsswinview button[ref=ssCancelBtn]":{
				click:function(btn){
					var win=btn.up("mtsswinview");
					win.close();
				}
			},
			/**
			 * 查询panel渲染
			 */
			"basequerypanel":{
				render:function(panel){
					var basePanel=panel.up("basepanel");
					var funCode=basePanel.funCode;
					panel.funCode=funCode;
					//panel.itemId=funCode+"_basequerypanel";
				}
			},
			/**
			 * 组合查询的查询事件
			 */
			"basequerypanel button[ref=queryBtn]":{
				click:function(btn){
					//self.Warning("暂时无法执行此操作！");
					
					var queryPanel = btn.up("basequerypanel");
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
					store.load();
					/*
					var queryPanel=btn.up("basequerypanel");
					var querySql1=self.getQuerySql(queryPanel);
					var querySql = self.getQureyFilter(queryPanel);
					var funCode=queryPanel.funCode;
					var basePanel=queryPanel.up("basepanel[funCode="+funCode+"]");
					var baseGrid=basePanel.down("basegrid[funCode="+funCode+"]");
					var store=baseGrid.getStore();
					var proxy=store.getProxy();
					proxy.extraParams.querySql=querySql1;
					proxy.extraParams.filter=querySql;
					//proxy.extraParams.page=1;
					//proxy.extraParams.start=0;
					//store.load();
					
					

					store.loadPage(1); //使用这个修复查询时页面不跳转问题
					*/
					/*
					var queryPanel=btn.up("basequerypanel");
					var querySql1=self.getQuerySql(queryPanel);
					var querySql = self.getQureyFilter(queryPanel);
					var funCode=queryPanel.funCode;
					var basePanel=queryPanel.up("basepanel[funCode="+funCode+"]");
					var baseGrid=basePanel.down("basegrid[funCode="+funCode+"]");
					var store=baseGrid.getStore();
					var proxy=store.getProxy();
					proxy.extraParams.querySql=querySql1;
					proxy.extraParams.filter="[" + querySql + "]";

					store.loadPage(1); //使用这个修复查询时页面不跳转问题*/
				}
			},
			/**
			 * 组合查询的重置事件
			 */
			"basequerypanel button[ref=resetBtn]":{
				click:function(btn){
					btn.up("form").reset();
					/*
					var queryPanel=btn.up("basequerypanel");
					self.resetQueryPanel(queryPanel);
					
					var funCode=queryPanel.funCode;
					var basePanel=queryPanel.up("basepanel[funCode="+funCode+"]");
					var baseGrid=basePanel.down("basegrid[funCode="+funCode+"]");
					var store=baseGrid.getStore();
					var proxy=store.getProxy();
					proxy.extraParams.querySql="";
					proxy.extraParams.filter="";
					store.load();
					*/
				}
			}			
		}
		Ext.apply(self.ctr,queryCtr);
	},
	requires:[
		"core.base.view.query.MtssWindow",
		"core.base.view.query.TreeView",
		"core.base.view.query.MtFuncWindow",
		"core.base.view.form.BaseFuncField",
		"core.base.store.query.TreeStore"
	],
});
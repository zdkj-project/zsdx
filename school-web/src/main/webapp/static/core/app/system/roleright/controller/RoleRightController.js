Ext.define("core.system.roleright.controller.RoleRightController", {
	extend: "Ext.app.ViewController",
	mixins: {
		suppleUtil: "core.util.SuppleUtil",
		messageUtil: "core.util.MessageUtil",
		formUtil: "core.util.FormUtil",
		treeUtil: "core.util.TreeUtil",
		gridActionUtil: "core.util.GridActionUtil"
	},

	alias: 'controller.roleright.rolerightController',


	init: function() {
		var self = this;

		//console.log("初始化 roleright controler");

		//事件注册
		this.control({						
			/**
			 *  角色grid的单击事件
			 *  单击后刷新右边角色的菜单权限列表
			 * @type {[type]}
			 */
			"panel[xtype=roleright.rolegrid]": {
				beforeitemclick: function(grid, record, item, index, e, eOpts) {
					//debugger;
					var baseMainPanel = grid.up("panel[xtype=roleright.mainlayout]");
					var funCode = baseMainPanel.funCode;
					var funData = baseMainPanel.funData;
					var records = grid.getSelectionModel().getSelection();
					var itemGrid = baseMainPanel.down("panel[xtype=roleright.rolgerightgrid]");
					var funData = Ext.apply(funData, {
						roleId: record.get("uuid")
					});
					//加载角色的权限
					var store = itemGrid.getStore();
					var proxy = store.getProxy();

					proxy.extraParams = {
						roleId: record.get("uuid")
					};
					store.load();
					return false;
				}
			},

			/**
			 * 角色权限grid的授权按钮事件
			 * @type {[type]}
			 */
			"panel[xtype=roleright.rolgerightgrid] button[ref=gridAdd]": {
				beforeclick: function(btn) {

					var baseGrid = btn.up("basetreegrid");

					var funCode = baseGrid.funCode;
					var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
					//关键：window的视图控制器
                    var otherController=basePanel.otherController;  
                    if(!otherController)
                        otherController='';


					//得到配置信息
					var funData = basePanel.funData;
		
					if (!funData.roleId) {
	                    self.Error("清选择要授权的角色!");
	                    return false;
	                }

					var popFunData = Ext.apply(funData, {
						grid: baseGrid
					});
					var detCode = basePanel.detCode;
					var iconCls = "x-fa fa-user-secret";
					var winId = detCode + "_win";
					var win = Ext.getCmp(winId);
					if (!win) {
						win = Ext.create('core.base.view.BaseFormWin', {
							id: winId,
							title: "授权菜单选择",
							/*zzk: 必须指定一个viewController控制器，否则，里面的control无法生效*/
							controller:otherController,
							width: 800,
							height: 600,
							resizable: false,
							iconCls: iconCls,
							operType: "edit",
							txtformSave: "确定授权",
							funData: popFunData,
							funCode: detCode,
							items: [{
								xtype: "roleright.detaillayout"													
							}],
							listeners:{
								show :function(me){										
									//待选的菜单中要过滤已有的菜单
									var selectMenuLayout = me.down("panel[xtype=roleright.detaillayout]");
									selectMenuLayout.funData = popFunData;
									selectMenuLayout.funCode = detCode;
									var selectMenuGrid = selectMenuLayout.down("panel[xtype=roleright.selectmenugrid]");
									
									var selectMnuStore = selectMenuGrid.getStore();
									selectMnuStore.getProxy().extraParams.roleId=funData.roleId;									
																
									//selectMnuStore.load();
									/*zzk 此处bug：store的autoLoad必须为true，并且延迟load一次，数据才可显示出来 */
									setTimeout(function(){
										selectMnuStore.load();											
									},50);
								}
							}
						});
					}
					win.show(true,function(){},baseGrid);
					
					
								
					return false;
				}
			},

			/**
			 * 角色权限grid取消授权按钮事件
			 * @type {[type]}
			 */
			"panel[xtype=roleright.rolgerightgrid] button[ref=gridEdit]": {
				beforeclick: function(btn) {
					var baseGrid = btn.up("basetreegrid");
					var funCode = baseGrid.funCode;
					var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
					//得到配置信息
					var funData = basePanel.funData;
					var roleId = funData.roleId;
					var records = baseGrid.getSelectionModel().getSelection();
					var ids = new Array();
					Ext.each(records, function(rec) {
						var pkValue = rec.get("id");
						ids.push(pkValue);
					}, this);
					var title = "确定要取消对这些菜单的授权吗？";
					Ext.Msg.confirm('警告', title, function(btn, text) {
						if (btn == 'yes') {
							//发送ajax请求
							var resObj = self.ajax({
								url: comm.get('baseUrl') + "/sysrole/deleteright",
								params: {
									ids: ids.join(","),
									roleId: roleId
								}
							});
							if (resObj.success) {
								var store = baseGrid.getStore();
								var proxy = store.getProxy();
								proxy.extraParams = {
									roleId: roleId
								};
								store.load();
								self.msgbox(resObj.obj);
							} else {
								self.Error(resObj.obj);
							}
						}
					});
					//执行回调函数
					if (btn.callback) {
						btn.callback();
					}

					return false;
				}
			},

			/**
			 * 角色权限grid展开按钮事件
			 * @type {[type]}
			 */
			"panel[xtype=roleright.rolgerightgrid] button[ref=gridExpand]": {
				click: function(btn) {
					var baseGrid = btn.up("basetreegrid");
					self.doCollapseOrExpand(baseGrid, "expand");
				}
			},
			/**
			 * 角色权限grid折叠按钮事件
			 * @type {[type]}
			 */
			"panel[xtype=roleright.rolgerightgrid] button[ref=gridCollapse]": {
				click: function(btn) {
					var baseGrid = btn.up("basetreegrid");
					self.doCollapseOrExpand(baseGrid, "collapse");
				}
			},
			   

			
			
		});

	},

	//展开或折叠树形节点
	doCollapseOrExpand: function(grid, cmd) {
		//var baseGrid = btn.up("basetreegrid");

		switch (cmd) {
			case "expand":
				grid.expandAll();
				break;
			case "collapse":
				grid.collapseAll();
				break;
		}
	}

});
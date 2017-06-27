Ext.define("core.system.menu.controller.MenuController", {
	extend: "Ext.app.ViewController",
	mixins: {
		suppleUtil: "core.util.SuppleUtil",
		messageUtil: "core.util.MessageUtil",
		formUtil: "core.util.FormUtil",
		treeUtil: "core.util.TreeUtil",
		gridActionUtil: "core.util.GridActionUtil"
	},
	
	alias: 'controller.menu.menuController',
   
	
	init: function() {
		var self = this;


        console.log("初始化 menu controler");
        
        this.control({
			//刷新按钮事件
			"panel[xtype=menu.menutree] button[ref=gridRefresh]": {
				beforeclick: function(btn) {
					var baseGrid = btn.up("basetreegrid");
					var funCode = baseGrid.funCode;
					var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
					var funData = basePanel.funData;
					var store = baseGrid.getStore();
					var proxy = store.getProxy();
					proxy.extraParams = {
						whereSql: funData.whereSql,
						orderSql: funData.orderSql
					};
					store.load(); //刷新父窗体的grid
					return false;
				}
			},
			//增加下级按钮事件
			"panel[xtype=menu.menutree] button[ref=gridAdd]": {
				beforeclick: function(btn) {
					self.doDetail(btn, "child");

					return false;
				}
			},
			//增加同级按钮事件
			"panel[xtype=menu.menutree] button[ref=gridAddBrother]": {
				beforeclick: function(btn) {
					self.doDetail(btn, "brother");

					return false;
				}
			},

			//修改按钮事件
			"panel[xtype=menu.menutree] button[ref=gridEdit]": {
				beforeclick: function(btn) {
					self.doDetail(btn, "edit");

					return false;
				}
			},			
			//启用菜单事件
			"panel[xtype=menu.menutree] button[ref=gridUnLock]": {
				beforeclick: function(btn) {
					self.doLockOrUnlock(btn,"0");
					return false;
				}
			},
			//锁定菜单事件
			"panel[xtype=menu.menutree] button[ref=gridLock]": {
				beforeclick: function(btn) {
					self.doLockOrUnlock(btn,"1");
					return false;
				}
			},			
		});
       
    	
	},
    
    //增加或修改菜单事件
	doDetail: function(btn, cmd) {

		var self = this;
		var baseGrid = btn.up("basetreegrid");
		var funCode = baseGrid.funCode;
		var basePanel = baseGrid.up("panel[xtype=menu.mainlayout]");
		var funData = basePanel.funData;
		var detCode = basePanel.detCode;
		var detLayout = basePanel.detLayout;
		//处理特殊默认值
		var defaultObj = funData.defaultObj;
		var insertObj = self.getDefaultValue(defaultObj);
		var popFunData = Ext.apply(funData, {
			grid: baseGrid,
			whereSql: " and isDelete='0' "
		});

		//先确定要选择记录
		var records = baseGrid.getSelectionModel().getSelection();
		if (records.length != 1) {
			self.Error("请先选择菜单");
			return;
		}
		//当前节点
		var just = records[0].get("id");
		var justName = records[0].get("text");

		//当前节点的上级节点
		var parent = records[0].get("parent");
		var store = baseGrid.getStore();
		var parentNode = store.getNodeById(parent);
		var parentName = "ROOT";
		if (parentNode)
			parentName = parentNode.get("text");
		//根据选择的记录与操作确定form初始化的数据
		var iconCls = "x-fa fa-plus-square";
		var title = "增加下级菜单";
		var operType = cmd;

		switch (cmd) {
			case "child":
				iconCls = "x-fa fa-plus-square";
				operType = "add";
				insertObj = Ext.apply(insertObj, {
					parentNode: just,
					parentName: justName,
					uuid: ''
				});
				break;
			case "brother":
				title = "增加同级菜单";
				iconCls = "x-fa fa-plus-square-o";
				operType = "add";
				insertObj = Ext.apply(insertObj, {
					parentNode: parent,
					parentName: parentName,
					uuid: ''
				});
				break;
			case "edit":
				iconCls = "x-fa fa-pencil-square";
				operType = "edit";
				title = "修改菜单";
                
                insertObj = records[0].data;
				insertObj = Ext.apply(insertObj, {
					parentNode: parent,
					parentName: parentName,
					uuid: just,
					nodeText: justName
				});
				break;
		}
		var winId = detCode + "_win";
		var win = Ext.getCmp(winId);
		if (!win) {
			win = Ext.create('core.base.view.BaseFormWin', {
				id: winId,
				title: title,
				width: 600,
				height: 360,
				resizable: false,
				iconCls: iconCls,
				operType: operType,
				funData: popFunData,
				funCode: detCode,
				//给form赋初始值
				insertObj: insertObj,
				items: [{
					xtype: "menu.detaillayout"
				}]
			});
		}
		win.show();
		var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
		var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
		var formDeptObj = objDetForm.getForm();
		//表单赋值
		self.setFormValue(formDeptObj, insertObj);
	},

	//锁定或解锁菜单
	doLockOrUnlock: function(btn, cmd) {
		var self = this;
		var tree = btn.up("panel[xtype=menu.menutree]");
		var records = tree.getSelectionModel().getSelection();
		if (records.length <= 0) {
			self.Error("请选要处理的菜单!");
			return false;
		}
		var ids = new Array();
		Ext.each(records, function(rec) {
			var pkValue = rec.get("id");
			ids.push(pkValue);
		}, this);
		//var node = records[0];
		var resObj = self.ajax({
			url: comm.get('baseUrl') + "/BaseMenu/setlockflag",
			params: {
				ids: ids.join(","),
				lockFlag:cmd
			}
		});
		if (resObj.success) {
			tree.getStore().load();
			self.msgbox(resObj.obj);
		} else {
			alert(resObj.obj);
		}
	}
});
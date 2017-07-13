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
			
			//添加下级按钮
        	"basetreegrid button[ref=gridAdd_Tab]": {
                beforeclick: function(btn) {
                    this.doDetail_Tab(btn,"child");
                    return false;
                }
            },
            
            //添加同级按钮
            "basetreegrid button[ref=gridAddBrother_Tab]": {
                beforeclick: function(btn) {
                    this.doDetail_Tab(btn,"brother");
                    return false;
                }
            },
            //修改按钮
            "basetreegrid button[ref=gridEdit_Tab]": {
                beforeclick: function(btn) {
                    this.doDetail_Tab(btn,"edit");
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
    
	//增加修改菜单
	doDetail_Tab:function(btn, cmd, grid, record) {
        var self = this;
        var baseGrid = btn.up("basetreegrid");
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

        //得到配置信息
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");

        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
        
      //设置tab页的itemId
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);

        var insertObj = self.getDefaultValue(defaultObj);
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
		
        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        if (!tabItem) {
            var tabTitle = title;

            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                layout: 'fit',
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: 'add',
                    controller: otherController,         //指定重写事件的控制器
                    funCode: funCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: [{
                        xtype: detLayout
                    }]
                });
                tabItem.add(item);

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();

                self.setFormValue(formDeptObj, insertObj);

            }, 30);

        }

        tabPanel.setActiveTab(tabItem);


        //执行回调函数
        if (btn.callback) {
            btn.callback();
        }

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
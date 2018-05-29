Ext.define("core.systemset.dictionary.controller.dicController", {
	extend: "Ext.app.ViewController",
    alias: 'controller.dictionary.dictionaryController',
	
	mixins: {
		suppleUtil: "core.util.SuppleUtil",
		messageUtil: "core.util.MessageUtil",
		formUtil: "core.util.FormUtil",
		treeUtil: "core.util.TreeUtil",
		gridActionUtil: "core.util.GridActionUtil"
	},
	
	init: function() {
		var self = this
			//事件注册
		

		this.control({
			/**
			 * 树形节点点击事件
			 * 1.展开树 2.刷新右边的字典项 3.显示按钮
			 * @type {[type]}
			 */
			"panel[xtype=dic.dicgrid]": {
				itemclick: function(grid, record, item, index, e, eOpts) {
					var baseMainPanel = grid.up("panel[xtype=dictionary.mainlayout]");
					var funCode = baseMainPanel.funCode;
					var records = grid.getSelectionModel().getSelection();
					var itemGrid = baseMainPanel.down("panel[xtype=dic.itemgrid]");

					//加载对应的字典项信息
					var store = itemGrid.getStore();
					var proxy = store.getProxy();
					proxy.extraParams = {
						filter: "[{'type':'string','comparison':'=','value':'" + record.get("id") + "','field':'dicId'}]"
					};
					store.load();
				}
			},

			//增加下级按钮事件
			"panel[xtype=dic.dicgrid] button[ref=gridAdd]": {
				click: function(btn) {
					self.doDetail(btn, "child");
				}
			},
			//增加同级按钮事件
			"panel[xtype=dic.dicgrid] button[ref=gridAddBrother]": {
				click: function(btn) {
					self.doDetail(btn, "brother");
				}
			},

			//修改按钮事件
			"panel[xtype=dic.dicgrid] button[ref=gridEdit]": {
				click: function(btn) {
					self.doDetail(btn, "edit");
				}
			},
			//删除按钮事件
			"panel[xtype=dic.dicgrid] button[ref=gridDelete]": {
				beforeclick: function(btn) {
					var baseGrid = btn.up("basetreegrid");
					var funCode = baseGrid.funCode;
					var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
					//得到配置信息
					var funData = basePanel.funData;
					var pkName = funData.pkName;
					var records = baseGrid.getSelectionModel().getSelection();
					if (records.length == 0) {
						self.Error("请选择要删除的数据字典");
						return;
					}
					var ids = new Array();
					Ext.each(records, function(rec) {
						var pkValue = rec.get("id");
						var child = rec.childNodes.length;
						if (child == 0) {
							//仅能删除无子数据字典
							ids.push(pkValue);
						}
					});
					var title = "确定要删除所选的数据字典吗？";
					if (ids.length == 0) {
						self.Warning("所选数据字典都有子项，不能删除");
						return;
					}
					if (ids.length < records.length) {
						title = "有些数据字典有子项，仅删除不含子项的数据字典。确定吗？";
					}
					Ext.Msg.confirm('警告', title, function(btn, text) {
						if (btn == 'yes') {
							//发送ajax请求
							var resObj = self.ajax({
								url: funData.action + "/dodelete",
								params: {
									ids: ids.join(","),
									pkName: pkName
								}
							});
							if (resObj.success) {
								baseGrid.getStore().load(0);
								self.Info(resObj.obj);
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
			//刷新按钮事件
			"panel[xtype=dic.dicgrid] button[ref=gridRefresh]": {
				click: function(btn) {
					var baseGrid = btn.up("basetreegrid");
					var store = baseGrid.getStore();
					var proxy = store.getProxy();
					proxy.extraParams = {
						whereSql: "and isDelete='0'",
						orderSql: ""
					};
					store.load(); //刷新父窗体的grid

					return false;
				}
			},

			//字典项增加按钮事件
			"basegrid[xtype=dic.itemgrid] button[ref=gridAdd]": {
				click: function(btn) {
					self.doItmeDetail(btn, "add");
					return false;
				}
			},
			//字典项修改按钮事件
			"panel[xtype=dic.itemgrid] button[ref=gridEdit]": {
				beforeclick: function(btn) {
					self.doItmeDetail(btn, "edit");
					return false;
				}
			},
			
			
			"basegrid[xtype=dic.itemgrid] button[ref=gridAdd_Tab]": {
                beforeclick: function(btn) {
                    this.doDetail_Tab(btn,"add");
                    return false;
                }
            },

            "basegrid[xtype=dic.itemgrid] button[ref=gridEdit_Tab]": {
                beforeclick: function(btn) {
                    this.doDetail_Tab(btn,"edit");
                    return false;
                }
            },

			//字典项删除按钮事件
			"panel[xtype=dic.itemgrid] button[ref=gridDelete]": {
				beforeclick: function(btn) {
					var baseGrid = btn.up("basegrid");
					var pkName = "uuid";
					var records = baseGrid.getSelectionModel().getSelection();
					if (records.length == 0) {
						self.Error("请选择要删除的字典项");
						return;
					}
					var ids = new Array();
					var dicId = "";
					Ext.each(records, function(rec) {
						var pkValue = rec.get(pkName);
						dicId = rec.get("dicId");
						ids.push(pkValue);
					});
					var title = "确定要删除所选的字典项吗？";
					Ext.Msg.confirm('警告', title, function(btn, text) {
						if (btn == 'yes') {
							//发送ajax请求
							var resObj = self.ajax({
								url: comm.get('baseUrl') + "/BaseDicitem" + "/dodelete",
								params: {
									ids: ids.join(","),
									pkName: pkName
								}
							});
							if (resObj.success) {
								//baseGrid.getStore().load(0);
								var store = baseGrid.getStore();
								var proxy = store.getProxy();
								proxy.extraParams = {
									filter: "[{'type':'string','comparison':'=','value':'" + dicId + "','field':'dicId'}]"
								};
								store.load();
								self.Info(resObj.obj);
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
			}
		});

	},
	
	doDetail_Tab:function(btn, cmd, grid, record) {
        var self = this;
        var baseGrid = btn.up("basegrid");
		var funCode = baseGrid.funCode;
		var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
		var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");   //获取整个tabpanel
		
		var funData = basePanel.funData;
		var detCode =  basePanel.detCode;  
	    var detLayout = basePanel.detLayout;
	    var defaultObj = funData.defaultObj;
		
		var otherController = basePanel.otherController;
	        if (!otherController)
	            otherController = '';
		//选择的字典信息
		var dicGrid = baseGrid.up("panel[xtype=dictionary.mainlayout]").down("panel[xtype=dic.dicgrid]");
		var selectObject = dicGrid.getSelectionModel().getSelection();
		if (selectObject.length <= 0) {
			self.Warning("请选择数据字典!");
			return false;
		}
		//得到选择的字典
		var objDic = selectObject[0];
		var dicId = objDic.get("id");
		var dicName = objDic.get("text");
		var detCode = "dicItem_main";
		//处理特殊默认值
		var defaultObj = funData.defaultObj;
		var insertObj = self.getDefaultValue(defaultObj);
		var tabTitle = funData.tabConfig.addTitle;
		insertObj = Ext.apply(insertObj, {
			dicId: dicId,
			dicName: dicName
		});
		var popFunData = Ext.apply(funData, {
			grid: baseGrid,
			filter: "[{'type':'string','comparison':'=','value':'" + dicId + "','field':'dicId'}]"
		});
				
		
		//设置tab页的itemId
        var tabItemId=funCode+"_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue= null;
		var operType = cmd; 
        switch (cmd) {
            case "edit":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }
                //获取主键值
                var pkName = funData.pkName;
                pkValue= recordData[pkName];

                insertObj = recordData;
                tabTitle = funData.tabConfig.editTitle;
                tabItemId=funCode+"_gridEdit"; 
                break;
            case "detail":                
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }
                //获取主键值
                var pkName = funData.pkName;
                pkValue= recordData[pkName];
                insertObj = recordData;
                tabTitle =  funData.tabConfig.detailTitle;
                tabItemId=funCode+"_gridDetail"+pkValue; 
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem=tabPanel.getComponent(tabItemId);
        if(!tabItem){
            
            //创建一个新的TAB
            tabItem=Ext.create({
                xtype:'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
                scrollable :true, 
                itemId:tabItemId,
                itemPKV:pkValue,      //保存主键值
                layout:'fit', 
            });
            tabPanel.add(tabItem); 

            //延迟放入到tab中
            setTimeout(function(){
                //创建组件
                var item=Ext.widget("baseformtab",{
                    operType:operType,                            
                    controller:otherController,         //指定重写事件的控制器
                    funCode:funCode,                    //指定mainLayout的funcode
                    detCode:detCode,                    //指定detailLayout的funcode
                    tabItemId:tabItemId,                //指定tab页的itemId
                    insertObj:insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items:[{
                        xtype:"dic.itemlayout",                        
                        funCode: detCode,
                        items: [{
                            xtype: "dic.itemform",
                            funCode: detCode                  
                        }]
                    }]
                }); 
                tabItem.add(item);  

                //将数据显示到表单中（或者通过请求ajax后台数据之后，再对应的处理相应的数据，显示到界面中）             
                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                self.setFormValue(formDeptObj, insertObj);

                if(cmd=="detail"){
                    self.setFuncReadOnly(funData, objDetForm, true);
                }

            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);        
    },
    
    
    
	doDetail: function(btn, cmd) {
		//debugger;
		var self = this;
		var baseGrid = btn.up("basetreegrid");
		var funCode = baseGrid.funCode;
		var basePanel = baseGrid.up("panel[xtype=dictionary.mainlayout]");
		var funData = basePanel.funData;
		var detCode = basePanel.detCode;
		var detLayout = basePanel.detLayout;
		//处理特殊默认值
		var defaultObj = funData.defaultObj;
		var insertObj = self.getDefaultValue(defaultObj);
		var popFunData = Ext.apply(funData, {
			grid: baseGrid,
			whereSql: "and isDelete='0' "
		});

		//先确定要选择记录
		var records = baseGrid.getSelectionModel().getSelection();
		if (records.length != 1) {
			self.Error("请先选择字典");
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
		var iconCls = "x-fa fa-plus-circle";
		var title = "增加下级字典";
		var operType = cmd;

		switch (cmd) {
			case "child":
				operType = "add";
				insertObj = Ext.apply(insertObj, {
					parentNode: just,
					parentName: justName,
					uuid: ''
				});
				break;
			case "brother":
				title = "增加同级字典";
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
				title = "修改字典";
                
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
				width: 500,
				height: 370,
				resizable: false,
				iconCls: iconCls,
				operType: operType,
				funData: popFunData,
				funCode: detCode,
				//给form赋初始值
				insertObj: insertObj,
				items: [{
					xtype: "dic.detaillayout"
				}]
			});
		}
		win.show();
		var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
		var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
		var formDeptObj = objDetForm.getForm();
		//表单赋值
		console.log(insertObj);
		self.setFormValue(formDeptObj, insertObj);
	},

	//增加或修改字典项
//	doItmeDetail: function(btn, cmd) {
//		//debugger;
//		var self = this;
//		//当前的grid
//		var baseGrid = btn.up("basegrid");
//		var funCode = baseGrid.funCode;
//		var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
//		var funData = basePanel.funData;
//		//选择的字典信息
//		var dicGrid = baseGrid.up("panel[xtype=dictionary.mainlayout]").down("panel[xtype=dic.dicgrid]");
//		var selectObject = dicGrid.getSelectionModel().getSelection();
//		if (selectObject.length <= 0) {
//			self.Warning("请选择数据字典!");
//			return false;
//		}
//		//得到选择的字典
//		var objDic = selectObject[0];
//		var dicId = objDic.get("id");
//		var dicName = objDic.get("text");
//		var detCode = "dicItem_main";
//		//处理特殊默认值
//		var defaultObj = funData.defaultObj;
//		var insertObj = self.getDefaultValue(defaultObj);
//		insertObj = Ext.apply(insertObj, {
//			dicId: dicId,
//			dicName: dicName
//		});
//		var popFunData = Ext.apply(funData, {
//			grid: baseGrid,
//			filter: "[{'type':'string','comparison':'=','value':'" + dicId + "','field':'dicId'}]"
//		});
//		var iconCls = "x-fa fa-plus-circle";
//		if (cmd == "edit" || cmd == "detail") {
//			if (cmd == "edit")
//				iconCls = "x-fa fa-pencil-square";
//			else
//				iconCls = "x-fa fa-pencil-square";
//
//			var rescords = baseGrid.getSelectionModel().getSelection();
//			if (rescords.length != 1) {
//				self.msgbox("请选择数据");
//				return;
//			}
//			insertObj = rescords[0].data;
//			insertObj = Ext.apply(insertObj, {
//				dicId: dicId,
//				dicName: dicName
//			});
//		}
//		var winId = detCode + "_win";
//		var win = Ext.getCmp(winId);
//		if (!win) {
//			win = Ext.create('core.base.view.BaseFormWin', {
//				id: winId,
//				width: 400,
//				height: 320,
//				resizable: false,
//				iconCls: iconCls,
//				operType: cmd,
//				funData: popFunData,
//				funCode: detCode,
//				//给form赋初始值
//				insertObj: insertObj,
//				items: [{
//					xtype: "dic.itemlayout"
//				}]
//			});
//		}
//		win.show();
//		var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
//		var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
//		var formDeptObj = objDetForm.getForm();
//
//		//表单赋值
//		self.setFormValue(formDeptObj, insertObj);
//		//根据操作设置是否只读
//		if (cmd == "detail") {
//			self.setFuncReadOnly(funData, objDetForm, true);
//		}
//		//执行回调函数
//		if (btn.callback) {
//			btn.callback();
//		}
//	}
});
/**
 * 表格控制器
 */
Ext.define("core.base.controller.GridController", {
	extend: "Ext.app.Controller",
	requires:[
		"Ext.ux.ComboPageSize",
	],
	initGrid: function() {
		var self = this;
		var gridCtr = {
            "basepanel basegrid basequeryform": {
                /**
                 * zzk 2014-4-5加入，同时在FormController中是公共高级查询功能生效
                 * 表格的高级查询表单的render事件
                 */
                render: function(view) {
                    var basePanel = view.up("basepanel");
                    if (basePanel) {
                        var funCode = basePanel.funCode;
                        view.funCode = funCode;                      
                    }
                }
            },
			"basepanel basegrid": {
				/**
				 * 表格的render事件
				 */
				render: function(grid) {
					var basePanel = grid.up("basepanel");
					//改动，为单用baseGrid的时候规划数据字典显示
					if (basePanel) {
						var funCode = basePanel.funCode;
						grid.funCode = funCode;
						//grid.itemId = funCode + "_basegrid";
						//暂时放到这里赋值
						//basePanel.itemId=funCode+"_basepanel";
					}

				},
				/**
				 * 表格的双击事件
				 */
				 /*
				itemdblclick: function(grid, record, item, index, e, eOpts) {

					//得到组件
					var basePanel = grid.up("basepanel");
					var funCode = basePanel.funCode;
					var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
                   
                    var store = baseGrid.getStore();
                    //得到模型
                    var Model = store.model;
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.Info("请选择数据");
                        return;
                    }
                    var insertObj = rescords[0].data;
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });
                    var width = 1000;
                    var height = 650;
                    if (funData.width)
                        width = funData.width;
                    if (funData.height)
                        height = funData.height;
                    var win = Ext.create('core.base.view.BaseFormWin', {
                        iconCls: 'x-fa fa-pencil-square',
                        operType: 'edit',
                        width: width,
                        height: height,
                        funData: popFunData,
                        funCode: detCode,
                        insertObj:insertObj,
                        items: [{
                            xtype: detLayout
                        }]
                    });
                    win.show();
                    var detPanel = win.down("basepanel[funCode=" + detCode + "]");
                    var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");
                   // var formDeptObj = objDetForm.getForm();
                   // self.setFormValue(formDeptObj, insertObj);

                     objDetForm.loadRecord (rescords[0]); 
				},*/
				/**
				 * 表格单击事件
				 */
				beforeitemclick: function(grid, record, item, index, e, eOpts) {
					var basePanel = grid.up("basepanel");
					var funCode = basePanel.funCode;
					var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
					var records = baseGrid.getSelectionModel().getSelection();
					var btnEdit = baseGrid.down("button[ref=gridEdit]");
					var btnDelete = baseGrid.down("button[ref=gridDelete]");
					var btnDetail = baseGrid.down("button[ref=gridDetail]");
					
                    var btnEdit_Tab = baseGrid.down("button[ref=gridEdit_Tab]");
                    var btnDetail_Tab = baseGrid.down("button[ref=gridDetail_Tab]");


					if (records.length == 0) {
						if (btnEdit)
							btnEdit.setDisabled(true);
						if (btnDelete)
							btnDelete.setDisabled(true);
						if (btnDetail)
							btnDetail.setDisabled(true);

                        if (btnEdit_Tab)
                            btnEdit_Tab.setDisabled(true);
                        if (btnDetail_Tab)
                            btnDetail_Tab.setDisabled(true);

					} else if (records.length == 1) {
						if (btnEdit)
							btnEdit.setDisabled(false);
						if (btnDelete)
							btnDelete.setDisabled(false);
						if (btnDetail)
							btnDetail.setDisabled(false);

                        if (btnEdit_Tab)
                            btnEdit_Tab.setDisabled(false);
                        if (btnDetail_Tab)
                            btnDetail_Tab.setDisabled(false);
                        
					} else {
						if (btnEdit)
							btnEdit.setDisabled(true);
						if (btnDelete)
							btnDelete.setDisabled(false);
						if (btnDetail)
							btnDetail.setDisabled(true);

                        if (btnEdit_Tab)
                            btnEdit_Tab.setDisabled(true);
                        if (btnDetail_Tab)
                            btnDetail_Tab.setDisabled(true);
					}
					//console.log(1231);
				}
			},
			"basepanel basetreegrid": {
				render: function(grid) {
					var basePanel = grid.up("basepanel");
					if (basePanel) {
						var funCode = basePanel.funCode;
						grid.funCode = funCode;
						//grid.itemId = funCode + "_basetreegrid";
					}
				},
				/**
				 * 表格单击事件
				 */
				itemclick: function(grid, record, item, index, e, eOpts) {
					var basePanel = grid.up("basepanel");
					var funCode = basePanel.funCode;
					var baseGrid = basePanel.down("basetreegrid[funCode=" + funCode + "]");
					var records = baseGrid.getSelectionModel().getSelection();
					var btnEdit = baseGrid.down("button[ref=gridEdit_Tab]");
					//alert(btnEdit);
					if (!btnEdit) {
						return;
					}

					//因为树形图维护时可能有同级节点或下级节点增加，但操作时一定要先选择个节点
					var btnAdd = baseGrid.down("button[ref=gridAdd_Tab]");
					if (!btnAdd) {
						return;
					}
					var btnAddBrother = baseGrid.down("button[ref=gridAddBrother_Tab]");
					if (!btnAddBrother) {
						return;
					}
					var btnDelete = baseGrid.down("button[ref=gridDelete]");
					if (!btnDelete) {
						return;
					}
					if (records.length == 0) {
						btnEdit.setDisabled(true);
						btnAdd.setDisabled(true);
						btnAddBrother.setDisabled(true);
						btnDelete.setDisabled(true);
					} else if (records.length == 1) {
						btnEdit.setDisabled(false);
						btnAdd.setDisabled(false);
						btnAddBrother.setDisabled(false);
						btnDelete.setDisabled(false);
					} else {
						btnEdit.setDisabled(true);
						btnAdd.setDisabled(true);
						btnAddBrother.setDisabled(true);
						btnDelete.setDisabled(false);
					}
				}
			},

			/**
             * 操作列的操作事件
             */
            "basegrid actioncolumn": {
            	editClick: function(data) {

            		var baseGrid=data.view;
                    var record=data.record;
                  
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;
                    
                    //关键：window的视图控制器
                    var otherController=basePanel.otherController;  
                    if(!otherController)
                        otherController='';

                    var insertObj = record.data;
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });
                    var width = 1000;
                    var height = 650;
                    if (funData.width)
                        width = funData.width;
                    if (funData.height)
                        height = funData.height;
                    var win = Ext.create('core.base.view.BaseFormWin', {
                        iconCls: 'x-fa fa-pencil-square',
                        operType: 'edit',
                        width: width,
                        height: height,
                        controller:otherController,
                        funData: popFunData,
                        funCode: detCode,
                        insertObj:insertObj,
                        items: [{
                            xtype: detLayout
                        }]
                    });
                    win.show();
                    var detPanel = win.down("basepanel[funCode=" + detCode + "]");
                    var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");
                    
                    
                    var formDeptObj = objDetForm.getForm();
                    self.setFormValue(formDeptObj, insertObj);                    
                    //objDetForm.loadRecord (record); 

                },
                detailClick: function(data) {
                    //得到组件                    
                    var baseGrid=data.view;
                    var record=data.record;
                 	
                 	var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;

                    //关键：window的视图控制器
                    var otherController=basePanel.otherController;  
                    if(!otherController)
                        otherController='';

                    var insertObj = record.data;
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });
                    var width = 1000;
                    var height = 650;
                    if (funData.width)
                        width = funData.width;
                    if (funData.height)
                        height = funData.height;
                    var win = Ext.create('core.base.view.BaseFormWin', {
                        iconCls: 'x-fa fa-file-text',
                        operType: 'detail',
                        width: width,
                        height: height,
                        controller:otherController,
                        funData: popFunData,
                        funCode: detCode,
                        insertObj:insertObj,
                        items: [{
                            xtype: detLayout
                        }]
                    });
                    win.show();
                    var detPanel = win.down("basepanel[funCode=" + detCode + "]");
                    var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();
                    self.setFormValue(formDeptObj, insertObj);

                    self.setFuncReadOnly(funData, objDetForm, true);
                   
                },              
                deleteClick:function(data){
                	                                
                    //得到组件                    
                    var baseGrid=data.view;
                    var record=data.record;
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                  
                        
                    Ext.Msg.confirm('提示', '是否删除数据?', function(btn, text) {
                        if (btn == 'yes') {                        
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: funData.action + "/dodelete",
                                params: {
                                    ids: record.get(pkName),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                //baseGrid.getStore().load(0);

                                baseGrid.getStore().remove(record); //不刷新的方式

                                self.msgbox(resObj.obj);

                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                                      
                },

                //弹出tab页的方式
                editClick_Tab: function(data) {

                    var baseGrid=data.view;
                    var record=data.record;

                    var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

                    //得到配置信息
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                   
                    //设置tab页的itemId
                    var tabItemId=funCode+"_gridEdit";     //命名规则：funCode+'_ref名称',确保不重复
                    //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                    var tabItem=tabPanel.getComponent(tabItemId);
                    
                    //获取主键值
                    var pkName = funData.pkName;
                    var pkValue= record.get(pkName);
                    console.log(pkValue);
                    //判断是否已经存在tab了
                    if(!tabItem){

                        var detCode = basePanel.detCode;
                        var detLayout = basePanel.detLayout;
                        var defaultObj = funData.defaultObj;
                       
                        //关键：window的视图控制器
                        var otherController=basePanel.otherController;  
                        if(!otherController)
                            otherController='';

                        // var width = 1000;
                        // var height = 650;
                        // if (funData.width)
                        //     width = funData.width;
                        // if (funData.height)
                        //     height = funData.height;

                        //处理特殊默认值
                        var insertObj =  record.data;
                        var popFunData = Ext.apply(funData, {
                            grid: baseGrid
                        });

                        var tabTitle = funData.tabConfig.editTitle;

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
                                operType:'edit',
                                controller:otherController,         //指定重写事件的控制器
                                funCode:funCode,                    //指定mainLayout的funcode
                                detCode:detCode,                    //指定detailLayout的funcode
                                tabItemId:tabItemId,                //指定tab页的itemId
                                insertObj:insertObj,                //保存一些需要默认值，提供给提交事件中使用
                                funData: popFunData,                //保存funData数据，提供给提交事件中使用
                                items:[{
                                    xtype:detLayout
                                }]
                            }); 
                            tabItem.add(item);  

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);

                        },30);
                                       
                    }else if(tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
                        self.Warning("您当前已经打开了一个编辑窗口了！");
                        return;
                    }

                    tabPanel.setActiveTab( tabItem);
                },
                 //弹出tab页的方式
                detailClick_Tab: function(data) {

                    var baseGrid=data.view;
                    var record=data.record;

                    var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

                    //得到配置信息
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                   
                    //设置tab页的itemId
                    var tabItemId=funCode+"_gridDetail";     //命名规则：funCode+'_ref名称',确保不重复
                    //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                    var tabItem=tabPanel.getComponent(tabItemId);
                    
                    //获取主键值
                    var pkName = funData.pkName;
                    var pkValue= record.get(pkName);

                    //判断是否已经存在tab了
                    if(!tabItem){

                        var detLayout = basePanel.detLayout;
                        var defaultObj = funData.defaultObj;
                       
                        //关键：window的视图控制器
                        var otherController=basePanel.otherController;  
                        if(!otherController)
                            otherController='';

                        // var width = 1000;
                        // var height = 650;
                        // if (funData.width)
                        //     width = funData.width;
                        // if (funData.height)
                        //     height = funData.height;

                        //处理特殊默认值
                        var insertObj =  record.data;
                        var popFunData = Ext.apply(funData, {
                            grid: baseGrid
                        });

                        var tabTitle = funData.tabConfig.detailTitle;

                        tabItem=Ext.create({
                            xtype:'container',
                            title: tabTitle,
                            //iconCls: 'x-fa fa-clipboard',
                            scrollable :true, 
                            itemId:tabItemId,
                            layout:'fit', 
                        });
                        tabPanel.add(tabItem); 

                        //延迟放入到tab中
                        setTimeout(function(){
                            //创建组件
                            var item=Ext.widget("baseformtab",{
                                operType:'detail',
                                controller:otherController,         //指定重写事件的控制器
                                funCode:funCode,                    //指定mainLayout的funcode
                                detCode:detCode,                    //指定detailLayout的funcode
                                tabItemId:tabItemId,                //指定tab页的itemId
                                insertObj:insertObj,                //保存一些需要默认值，提供给提交事件中使用
                                funData: popFunData,                //保存funData数据，提供给提交事件中使用
                                items:[{
                                    xtype:detLayout
                                }]
                            }); 
                            tabItem.add(item);  

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);
                            self.setFuncReadOnly(funData, objDetForm, true);
                        },30);
                                       
                    }else if(tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据，不同则替换数据

                        var objDetForm = tabItem.down("baseform[funCode=" + detCode + "]");
                        var formDeptObj = objDetForm.getForm();
                        self.setFormValue(formDeptObj, record.data);
                        self.setFuncReadOnly(funData, objDetForm, true);

                    }

                    tabPanel.setActiveTab( tabItem);
                },
            },
            
			

		}
		Ext.apply(self.ctr, gridCtr);
	}
});
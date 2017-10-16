Ext.define("core.train.coursecheckrule.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursecheckrule.mainController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
    init: function() {
    },
    control: {
    "basegrid[xtype=coursecheckrule.maingrid]": {
        afterrender: function (grid, eOpts) {
            var btnAdd = grid.down("button[ref=gridAdd_Tab]");
            var btnEdit = grid.down("button[ref=gridEdit_Tab]");
            var btnDelete = grid.down("button[ref=gridDelete]");
            var btngridStartUsing = grid.down("button[ref=gridStartUsing]");
            var roleKey = comm.get("roleKey");
            if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                btnAdd.setHidden(true);
                btnEdit.setHidden(true);
                btnDelete.setHidden(true);
                btngridStartUsing.setHidden(true);
            }
        },
		beforeitemclick: function(grid, record, item, index, e, eOpts) {
			var basePanel = grid.up("basepanel");
			var funCode = basePanel.funCode;
			var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
			var records = baseGrid.getSelectionModel().getSelection();
			var btnEdit = baseGrid.down("button[ref=gridEdit]");
			var btnDelete = baseGrid.down("button[ref=gridDelete]");
			var btnDetail = baseGrid.down("button[ref=gridDetail]");
			var btnStartUsing = baseGrid.down("button[ref=gridStartUsing]");
			
            var btnEdit_Tab = baseGrid.down("button[ref=gridEdit_Tab]");
            var btnDetail_Tab = baseGrid.down("button[ref=gridDetail_Tab]");
            var StartUsing=false;

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
				if (record.data.startUsing == 1) {
	            	btnStartUsing.setDisabled(true);
	            	btnDelete.setDisabled(true);
	            }else{
	            	btnStartUsing.setDisabled(false);
	            	btnDelete.setDisabled(false);
	            }
				if (btnEdit)
					btnEdit.setDisabled(false);
				if (btnDetail)
					btnDetail.setDisabled(false);

                if (btnEdit_Tab)
                    btnEdit_Tab.setDisabled(false);
                if (btnDetail_Tab)
                    btnDetail_Tab.setDisabled(false);
                
			} 
			if (records.length > 1){
				if (btnEdit)
					btnEdit.setDisabled(true);
				for(var i=0;i<records.length;i++){
					if(records[i].data.startUsing==1){
						StartUsing=true;
					}
				}
				if (btnDelete)
					btnDelete.setDisabled(StartUsing);
				if (btnStartUsing)
					btnStartUsing.setDisabled(StartUsing);
				if (btnDetail)
					btnDetail.setDisabled(true);

                if (btnEdit_Tab)
                    btnEdit_Tab.setDisabled(true);
                if (btnDetail_Tab)
                    btnDetail_Tab.setDisabled(true);
			}
			 return false;
		}
        
    },
        "basegrid button[ref=gridAdd_Tab]": {
            beforeclick: function(btn) {
                this.doDetail_Tab(btn,"add");
                return false;
            }
        },

        "basegrid button[ref=gridDetail_Tab]": {
            beforeclick: function(btn) {
                this.doDetail_Tab(btn,"detail");
                return false;
            }
        },

        "basegrid[xtype=coursecheckrule.maingrid] button[ref=gridEdit_Tab]": {
            beforeclick: function(btn) {
                this.doDetail_Tab(btn,"edit");
                return false;
            }
        },

        "basegrid button[ref=gridAdd]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridDetail]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid[xtype=coursecheckrule.maingrid] button[ref=gridEdit]": {
            beforeclick: function(btn) {
                this.doDetail(btn,"edit");

                return false;
            }
        },

        "basegrid[xtype=coursecheckrule.maingrid] button[ref=gridDelete]": {
            beforeclick: function(btn) {
                var self=this;

                var baseGrid = btn.up("basegrid");
             
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length > 0) {
                    var mark=0;
                    Ext.each(records, function(rec) {                       
                        if(rec.get("startUsing")==1){                            
                            mark=1;
                            return;
                        }
                    });
                    if(mark==1){
                        self.Info("不能删除已启用的规则！");
                        return false;
                    }
                }
                
            }
        },

        "basegrid[xtype=coursecheckrule.maingrid] button[ref=gridStartUsing]": {
            beforeclick: function(btn) {
                var self=this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length > 0) {
                    //封装ids数组
                    Ext.Msg.confirm('提示', '是否启用规则？', function(btn, text) {
                        if (btn == 'yes') {
                            var ids = new Array();
                            Ext.each(records, function(rec) {
                                if(rec.get("startUsing")==0){   //只保存还未启用的规则
                                    var pkValue = rec.get(pkName);
                                    ids.push(pkValue);
                                }
                            });

                            if(ids.length==0){
                                self.Info("没有选择需要启用的规则！");
                                return false;
                            }

                            //发送ajax请求
                            var resObj = self.ajax({
                                url: funData.action + "/doStartUsing",
                                params: {
                                    ids: ids.join(","),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                baseGrid.getStore().load();
                                self.Info(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                } else {
                    self.Warning("请选择数据！");
                }
                return false;
            }
        },

        "basegrid[xtype=coursecheckrule.maingrid]  actioncolumn": {
            editClick_Tab:function(data){
                var baseGrid=data.view;
                var record=data.record;

                this.doDetail_Tab(null,"edit",baseGrid,record);

                return false;
            },
            detailClick_Tab: function(data) {
                var baseGrid=data.view;
                var record=data.record;

                this.doDetail_Tab(null,"detail",baseGrid,record);
                 return false;
            },
            editClick: function(data) {
                var baseGrid=data.view;
                var record=data.record;

                this.doDetail(null,"edit",baseGrid,record);

                return false;
            },
            detailClick: function(data) {
                console.log(data);
            },
            deleteClick: function(data) {  
                var self=this;
                           
                var record=data.record;
                if(record.get("startUsing")==1){
                    self.Info("已启用的规则，不允许被删除！");
                    return false;
                }
            },
            startUsingClick:function(data) {
                var self=this;
                //得到组件                    
                var baseGrid=data.view;
                var record=data.record;

                if(record.get("startUsing")==1){
                    self.Info("此规则已经启用！");
                    return false;
                }

                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
              
                Ext.Msg.confirm('提示', '是否启用规则？', function(btn, text) {
                    if (btn == 'yes') {                        
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: funData.action + "/doStartUsing",
                            params: {
                                ids: record.get(pkName),
                                pkName: pkName
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();                            
                            self.Info(resObj.obj);
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            },
        }
    },
    
    doDetail_Tab:function(btn, cmd, grid, record){
        var self = this;
        var baseGrid;
        var recordData;

        //根据点击的地方是按钮或者操作列，处理一些基本数据
        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.getData();
        }

        //得到组件
        var funCode = baseGrid.funCode; //coursecheckrule_main
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode +"]");
        var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");   //获取整个tabpanel

        //得到配置信息
        var funData = basePanel.funData;
        var detCode =  basePanel.detCode;  
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
                
        //关键：打开新的tab视图界面的控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //本方法只提供班级详情页使用
        var tabTitle = funData.tabConfig.addTitle;
        //设置tab页的itemId
        var tabItemId=funCode+"_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue= null;
        var operType = cmd;    // 只显示关闭按钮
        var itemXtype=[{
            xtype:detLayout,                        
            funCode: detCode             
        }];

        switch (cmd) {
            case "edit":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].getData();
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
                    recordData = rescords[0].getData();
                }
                //获取主键值
                var pkName = funData.pkName;
                pkValue= recordData[pkName];
                insertObj = recordData;
                tabTitle =  funData.tabConfig.detailTitle;
                tabItemId=funCode+"_gridDetail"+insertObj.uuid; 
                operType="Detail";

                itemXtype=[{
                    xtype:detLayout,
                    funCode:detCode,
                    items:[{
                        xtype:"coursecheckrule.DetailPanel"
                    }]
                }];

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
                    items:itemXtype
                }); 
                tabItem.add(item);  
               
                if(cmd=="edit"){
                    //将数据显示到表单中（或者通过请求ajax后台数据之后，再对应的处理相应的数据，显示到界面中）             
                    var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();
                    self.setFormValue(formDeptObj, insertObj);

                } else if(cmd=="detail"){
                    var detailhtmlpanel=item.down("container[xtype=coursecheckrule.DetailPanel]");                

                    // 处理数据字典字段的值
                    var ddItem=factory.DDCache.getItemByDDCode("CHECKMODE");
                    var resultVal="";
                    var value=recordData["checkMode"];
                    for(var i=0;i<ddItem.length;i++){
                        var ddObj=ddItem[i];
                        if (value==ddObj["itemCode"]) {
                            resultVal=ddObj["itemName"];
                            break;
                        }
                    } 
                    recordData.checkMode=resultVal;

                    detailhtmlpanel.setData(recordData);
                }
            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }
        tabPanel.setActiveTab( tabItem);        
    },
 
});

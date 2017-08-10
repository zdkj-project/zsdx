Ext.define("core.train.creditrule.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.creditrule.mainController',
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
    	"basegrid[xtype=creditrule.maingrid]": {
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

        "basegrid[xtype=creditrule.maingrid] button[ref=gridEdit_Tab]": {
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

        "basegrid[xtype=creditrule.maingrid] button[ref=gridEdit]": {
            beforeclick: function(btn) {
                this.doDetail(btn,"edit");

                return false;
            }
        },

        "basegrid[xtype=creditrule.maingrid] button[ref=gridDelete]": {
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

        "basegrid[xtype=creditrule.maingrid] button[ref=gridStartUsing]": {
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
                                self.msgbox(resObj.obj);
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

        "basegrid[xtype=creditrule.maingrid]  actioncolumn": {
            editClick_Tab:function(data){
                var baseGrid=data.view;
                var record=data.record;

                this.doDetail_Tab(null,"edit",baseGrid,record);

                return false;
            },
            detailClick_Tab:function(data){
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

                            self.msgbox(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            },
        }
    },
    
    doDetail_Tab:function(btn, cmd, grid, record) {
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
        var funCode = baseGrid.funCode; //creditrule_main
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
                        xtype:"creditrule.DetailPanel"
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
               
               
                //将数据显示到表单中（或者通过请求ajax后台数据之后，再对应的处理相应的数据，显示到界面中） 
                if (cmd=="edit") {
                    var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();
                    self.setFormValue(formDeptObj, insertObj);
                }else if(cmd=="detail"){
                     var detailhtmlpanel=item.down("container[xtype=creditrule.DetailPanel]");
    // 设置数据
                     detailhtmlpanel.setData(recordData);
                    }
            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);        
    },






    doDetail:function(btn,cmd,grid,record){
        
        var self=this;
        var baseGrid;
        var recordData;

        if(btn){
            baseGrid = btn.up("basegrid");
        }else{
            baseGrid=grid;
            recordData=record.data;
        }
       
        //得到模型
        var store = baseGrid.getStore();
        var Model = store.model;
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

        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);

        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        var width = 1000;
        var height = 650;
        if (funData.width)
            width = funData.width;
        if (funData.height)
            height = funData.height;

        var iconCls='x-fa fa-plus-circle';
        var operType=cmd;
        var title="增加";

        switch(cmd){
            case "edit":

                if(btn){
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData=rescords[0].data;
                }
                
                insertObj = recordData;

                iconCls='x-fa fa-pencil-square',
                operType="edit";
                title="编辑";
                break;
            case "detail":

                if(btn){
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData=rescords[0].data;
                }
                
                insertObj = recordData;

                iconCls='x-fa fa-file-text',
                operType="detail";
                title="详情";
                break;
        }

        var win = Ext.create('core.base.view.BaseFormWin', {
            title:title,
            iconCls: iconCls,
            operType: operType,
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

        //若是编辑，则隐藏启用规则字段
        if(cmd=="edit")
            formDeptObj.findField("startUsing").hide();
        else if(cmd=="detail")
            self.setFuncReadOnly(funData, objDetForm, true);
    }    
});
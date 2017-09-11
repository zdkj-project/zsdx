Ext.define("core.oa.terminal.controller.MainController", {
    extend: "Ext.app.ViewController",
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    
    alias: 'controller.terminal.terminalController',
/*    views: [
        "core.oa.terminal.view.mainLayout",
        "core.oa.terminal.view.detailLayout",
        "core.oa.terminal.view.listGrid",
        "core.oa.terminal.view.detailForm",
        "core.oa.terminal.view.readonlyForm"
    ],*/
    init: function() {
    },
    control: {
            "basegrid[funCode=terminal_main] button[ref=gridFastSearchBtn]": {
                beforeclick:function(btn){                
                    //得到组件
                    var baseGrid = btn.up("basegrid");
                        
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    var roomName=baseGrid.down("textfield[name=roomName]").getValue();     
                    if(roomName!=""){
                    	proxy.extraParams.filter = '[{"type":"string","value":"'+roomName+'","field":"roomName","comparison":""}]';
                        store.loadPage(1);
                    }else{
                    	proxy.extraParams.filter = "";
                    	store.loadPage(1);
                    }
                    return false;
                }
            },	
        	
            //快速搜索文本框回车事件
            "basegrid[funCode=terminal_main] field[funCode=girdFastSearchText]": {
                specialkey: function (field, e) {
                    if (e.getKey() == e.ENTER) {

                        //得到组件                 
                        var baseGrid = field.up("basegrid");
                        if (!baseGrid)
                            return false;

                        var toolBar = field.up("toolbar");
                        if (!toolBar)
                            return false;
                        var store = baseGrid.getStore();
                        var proxy = store.getProxy();
                        var roomName=baseGrid.down("textfield[name=roomName]").getValue();     
                        if(roomName!=""){
                        	proxy.extraParams.filter = '[{"type":"string","value":"'+roomName+'","field":"roomName","comparison":""}]';
                            store.loadPage(1);
                        }else{
                        	proxy.extraParams.filter = "";
                        	store.loadPage(1);
                        }
                        return false;
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

            "basegrid[xtype=terminal.maingrid] button[ref=gridEdit_Tab]": {
                beforeclick: function(btn) {
                    this.doDetail_Tab(btn,"edit");
                    return false;
                }
            },

            "basegrid[xtype=terminal.maingrid] button[ref=gridDelete]": {
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


            "basegrid[xtype=terminal.grid]  actioncolumn": {
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
            var funCode = baseGrid.funCode; //terminal_main
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
                funCode: detCode,             
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
                    tabItemId=funCode+"_gridDetail"+pkValue; 

                    itemXtype=[{
                        xtype:detLayout,                        
                        funCode: detCode,
                        items: [{
                            xtype: "terminal.detailhtml"
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
                   
                 
                    if(cmd=="detail"){
                        //self.setFuncReadOnly(funData, objDetForm, true);
                        var ddItem = factory.DDCache.getItemByDDCode("INFOTERTYPE");                      
                        var value=insertObj.termType;
                        for (var j = 0; j < ddItem.length; j++) {
                            var ddObj = ddItem[j];
                            if (value == ddObj["itemCode"]) {
                                insertObj.termType = ddObj["itemName"];
                                break;
                            }
                        }

                        var detailHtml = item.down("container[xtype=terminal.detailhtml]");
                        detailHtml.setData(insertObj);
                        console.log(insertObj);
                    }else{
                        //将数据显示到表单中（或者通过请求ajax后台数据之后，再对应的处理相应的数据，显示到界面中）             
                        var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                        var formDeptObj = objDetForm.getForm();
                        self.setFormValue(formDeptObj, insertObj);
                    }
                },30);
                               
            }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
                self.Warning("您当前已经打开了一个编辑窗口了！");
                return;
            }
            tabPanel.setActiveTab( tabItem);        
        },  
    });
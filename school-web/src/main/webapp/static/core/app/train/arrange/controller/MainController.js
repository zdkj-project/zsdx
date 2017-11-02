Ext.define("core.train.arrange.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.arrange.mainController',
    mixins: {

        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function() {
        /*control事件声明代码，可以写在这里
        this.control({

        });
        */
    },
    control: {
    	//对没有权限的角色隐藏按钮
    	 "basegrid[xtype=arrange.maingrid]": {
             afterrender: function (grid, eOpts) {
                 var btngridArrangeRoom = grid.down("button[ref=gridArrangeRoom_Tab]");
                 var btngridArrangeSite = grid.down("button[ref=gridArrangeSite_Tab]");
                 var btngridExportRoom = grid.down("button[ref=gridExportRoom]");
                 var btngridExportSite = grid.down("button[ref=gridExportSite]");
                 var btngridArrange = grid.down("button[ref=gridArrange]");
                 var roleKey = comm.get("roleKey");
                 if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1) {
                	 btngridArrangeRoom.setHidden(true);
                	 btngridArrangeSite.setHidden(true);
                	 btngridExportRoom.setHidden(true);
                	 btngridExportSite.setHidden(true);
                	 btngridArrange.setHidden(true);
                 }

                 if(roleKey.indexOf("DORMMANAGER")!= -1){
                    btngridArrangeRoom.setHidden(false);
                     btngridArrange.setHidden(false);
                 }
                 if(roleKey.indexOf("SITEMANAGER")!= -1){
                    btngridArrangeSite.setHidden(false);
                 }
             }
            },
//    
//        "basegrid[xtype=arrange.maingrid]": {
//            itemclick: function(grid, record, item, index, e, eOpts) {
//                var basePanel = grid.up("basepanel");
//                var funCode = basePanel.funCode;
//                var baseGrid = basePanel.down("basegrid[funCode=" +
//                    funCode + "]");
//                var records = baseGrid.getSelectionModel().getSelection();
//
//                var btnArrangeRoom = baseGrid.down("button[ref=gridArrangeRoom_Tab]");
//                var btnArrangeSite = baseGrid.down("button[ref=gridArrangeSite_Tab]");
//                var btnArrange = baseGrid.down("button[ref=gridArrange]");
//
//                if (records.length == 0) {
//                    if (btnArrangeRoom)
//                        btnArrangeRoom.setDisabled(true);
//                    if (btnArrangeSite)
//                        btnArrangeSite.setDisabled(true);
//                    if (btnArrange)
//                        btnArrange.setDisabled(true);
//
//                } else if (records.length == 1) {
//                    
//                    // if(record.get("isarrange")==1&&record.get("isuse")==2){
//                    //     if (btnArrangeRoom)
//                    //         btnArrangeRoom.setDisabled(true);
//                    //     if (btnArrangeSite)
//                    //         btnArrangeSite.setDisabled(true);
//                    //     if (btnArrange)
//                    //         btnArrange.setDisabled(true);                    
//                    // }else{
//                        if (btnArrangeRoom)
//                            btnArrangeRoom.setDisabled(false);
//                        if (btnArrangeSite)
//                            btnArrangeSite.setDisabled(false);
//                        if (btnArrange)
//                            btnArrange.setDisabled(false);
//                    //}
//
//                } else {
//                    if (btnArrangeRoom)
//                        btnArrangeRoom.setDisabled(true);
//                    if (btnArrangeSite)
//                        btnArrangeSite.setDisabled(true);
//                    if (btnArrange)
//                        btnArrange.setDisabled(true);
//                }
//                    //console.log(1231);
//                
//            }
//        },
        "basegrid[xtype=arrange.maingrid] button[ref=gridArrange]": {
            beforeclick: function(btn) {
                var self = this;

                //得到组件
                var baseGrid = btn.up("basegrid");

                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择数据!");
                    return;
                }

                var classId = records[0].get("uuid");
                var className = records[0].get("className");;

                if (!classId) {
                    self.Warning("信息有误，请选择班级！");
                    return false;
                }
                if(records[0].get("isuse")==2){
                    self.Warning("此班级修改后未提交，请等待提交后再安排！");
                    return false;
                }

                this.doSendUserDetail_Tab(btn, "edit");

                /*
                Ext.MessageBox.confirm('温馨提示', '<p>执行此操作后将会自动同步相关数据！</p><p style="color:red;font-size:14px;font-weight: 400;">你确定要执行安排操作吗？</p>', function(btn, text) {
                    if (btn == 'yes') {
                        Ext.Msg.wait('正在执行中,请稍后...', '温馨提示');
                        self.asyncAjax({
                            url: comm.get("baseUrl")  + "/TrainClass/doClassArrange",
                            params: {
                                classId:classId                             
                            },
                            timeout:1000*60*60, //1个小时
                            //回调代码必须写在里面
                            success: function(response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                                           
                                if(data.success){ 
                                    baseGrid.getStore().load();                                
                                    Ext.Msg.hide();
                                    self.Info(data.obj);
                                }else{                        
                                    Ext.Msg.hide();
                                    self.Error(data.obj);
                                }
                            },
                            failure: function(response) {                        
                                Ext.Msg.hide();
                                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                            }
                        });  
                    }
                });
                */
        
                return false;
            }
        },
        
        /**
         * 导出住宿信息
         */
        "basegrid[xtype=arrange.maingrid] button[ref=gridExportRoom]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if(records.length!=1){
                    self.Warning("请选择一个班级进行导出操作！");
                    return;
                }

                var rec=records[0];

                var title = "确定要导出【"+rec.get("className")+"】班级住宿安排信息吗？";
            
                var ids = new Array();
                var pkValue = rec.get(pkName);
                ids.push(pkValue);            
                // if (records.length > 0) {
                //     title = "将导出所选师资的信息";
                //     Ext.each(records, function (rec) {
                //         var pkValue = rec.get(pkName);
                //         ids.push(pkValue);
                //     });
                // }

                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClass/exportRoomExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClass/checkExportRoomEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.Info(data.obj);
                                        component.destroy();                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },
        /**
         * 导出场地信息
         */
        "basegrid[xtype=arrange.maingrid] button[ref=gridExportSite]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if(records.length!=1){
                    self.Warning("请选择一个班级进行导出操作！");
                    return;
                }

                var rec=records[0];
                
                if(rec.get("isuse")===2){
                    self.Warning("未提交数据无法导出！");
                    return;
                }

                var title = "确定要导出【"+rec.get("className")+"】班级课程场地安排信息吗？";
            
                var ids = new Array();
                var pkValue = rec.get(pkName);
                ids.push(pkValue);            
                // if (records.length > 0) {
                //     title = "将导出所选师资的信息";
                //     Ext.each(records, function (rec) {
                //         var pkValue = rec.get(pkName);
                //         ids.push(pkValue);
                //     });
                // }

                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClass/exportSiteExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClass/checkExportSiteEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.Info(data.obj);
                                        component.destroy();                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },
         /**
         * 导出就餐信息
         */
        "basegrid[xtype=arrange.maingrid] button[ref=gridExportDinner]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if(records.length!=1){
                    self.Warning("请选择一个班级进行导出操作！");
                    return;
                }

                var rec=records[0];
                
                if(rec.get("isuse")===2){
                    self.Warning("未提交数据无法导出！");
                    return;
                }

                var title = "确定要导出【"+rec.get("className")+"】班级就餐需求信息吗？";
            
                var ids = new Array();
                var pkValue = rec.get(pkName);
                ids.push(pkValue);            
                // if (records.length > 0) {
                //     title = "将导出所选师资的信息";
                //     Ext.each(records, function (rec) {
                //         var pkValue = rec.get(pkName);
                //         ids.push(pkValue);
                //     });
                // }

                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClass/exportDinnerExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClass/checkExportDinnerEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.Info(data.obj);
                                        component.destroy();                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },


        "basegrid[xtype=arrange.maingrid]  button[ref=gridArrangeRoom_Tab]": {
            beforeclick: function(btn) {                
                this.doArrangeRoomDetail_Tab(btn, "edit");

                return false;
            }
        },
        "basegrid[xtype=arrange.maingrid]  button[ref=gridArrangeSite_Tab]": {
            beforeclick: function(btn) {

                this.doArrangeSiteDetail_Tab(btn, "edit");

                return false;
            }
        },


        
        "basegrid[xtype=arrange.maingrid]  actioncolumn": {
            gridArrangeRoomClick_Tab: function(data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doArrangeRoomDetail_Tab(null, "edit", baseGrid, record);

                return false;
            },
            gridArrangeSiteClick_Tab: function(data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doArrangeSiteDetail_Tab(null, "edit", baseGrid, record);

                return false;
            },
            detailClick_Tab: function(data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doClassDetail_Tab(null, "detail", baseGrid, record);

                return false
            },
            gridBindCardClick_Tab:function(data){
                var baseGrid = data.view;
                var record = data.record;

                this.doBindCardDetail_Tab(null, "edit", baseGrid, record);

                return false
            },
            gridFoodDetailClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doFoodDetail_Tab(null, cmd, baseGrid, record);

                return false;
            },
            imageDetailClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doImageDetail_Tab(null, cmd, baseGrid, record);

                return false;
            }
        },

          
    },

    doArrangeRoomDetail_Tab: function(btn, cmd, grid, record) {

        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode +"]");
        var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "arrange_roomDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
                
        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = {};
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });


        //根据cmd操作类型，来设置不同的值
        var tabTitle = "住宿安排"; 
        //设置tab页的itemId
        var tabItemId=funCode+"_gridRoomDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue= null;
        var operType="detail";

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

                if(recordData.isuse==2){
                    self.Warning("此班级修改后未提交，请等待提交后再安排！");
                    return false;
                }

                insertObj = recordData;
                tabTitle = "住宿安排";
                tabItemId=funCode+"_gridRoomEdit"; 

                //获取主键值
                var pkName = funData.pkName;
                pkValue= recordData[pkName];
                    
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

                insertObj = recordData;
                tabTitle = "查看住宿安排";
                tabItemId=funCode+"_gridRoomDetail"+insertObj.classNumb; 
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem=tabPanel.getComponent(tabItemId);
        if(!tabItem){
    
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
                    operType:'noButton',                            
                    controller:otherController,         //指定重写事件的控制器
                    funCode:funCode,                    //指定mainLayout的funcode
                    detCode:detCode,                    //指定detailLayout的funcode
                    tabItemId:tabItemId,                //指定tab页的itemId
                    insertObj:insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items:[{
                        xtype:detLayout,
                        funCode: detCode, 
                        items: [{
                            xtype: "arrange.roomdetailform",
                            funCode: detCode                  
                        }]
                    }],
                    dockedItems : [{
                        xtype: 'toolbar',
                        dock: 'bottom',
                        ui: 'footer',
                        layout: {
                            pack: 'center'
                        },
                        items: [{
                            xtype: "button",
                            text: "完成",
                            ref: "formClose",
                            iconCls: "x-fa fa-reply"
                        }]
                    }]
                }); 
                tabItem.add(item);  
                
                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();              

                self.setFormValue(formDeptObj, insertObj);

                //查询班级的学员信息
                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassRoomTrainees",
                    params: {
                        classId: insertObj.uuid,
                        page:1,
                        start:0,
                        limit:-1    //-1表示不分页
                    },
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        var rows=data.rows;
                        //console.log(rows);
                        if(rows!=undefined){  //若存在rows数据，则表明请求正常
                            //获取班级学员列表信息
                            var arrangeRoomGrid=item.down("grid[ref=arrangeRoomGrid]");                   
                            arrangeRoomGrid.getStore().loadData(rows);

                        }else{
                            self.Error(data.obj);
                        }
                    }
                });            

            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);
    },


    doArrangeSiteDetail_Tab: function(btn, cmd, grid, record) {

        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }
        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode +"]");
        var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "arrange_siteDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
                
        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = {};
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "场地安排"; 
        //设置tab页的itemId
        var tabItemId=funCode+"_gridSiteDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue= null;
        var operType="detail";

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

                if(recordData.isuse==2){
                    self.Warning("此班级修改后未提交，请等待提交后再安排！");
                    return false;
                }

                insertObj = recordData;
                tabTitle = "场地安排";
                tabItemId=funCode+"_gridSiteEdit"; 

                //获取主键值
                var pkName = funData.pkName;
                pkValue= recordData[pkName];

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

                insertObj = recordData;
                tabTitle = "查看场地安排";
                tabItemId=funCode+"_gridSiteEdit"+insertObj.classNumb; 
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem=tabPanel.getComponent(tabItemId);
        if(!tabItem){
    
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
                    operType:'noButton',                            
                    controller:otherController,         //指定重写事件的控制器
                    funCode:funCode,                    //指定mainLayout的funcode
                    detCode:detCode,                    //指定detailLayout的funcode
                    tabItemId:tabItemId,                //指定tab页的itemId
                    insertObj:insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items:[{
                        xtype:detLayout,
                        funCode: detCode, 
                        items: [{
                            xtype: "arrange.sitedetailform"
                        }]
                    }],
                    dockedItems : [{
                        xtype: 'toolbar',
                        dock: 'bottom',
                        ui: 'footer',
                        layout: {
                            pack: 'center'
                        },
                        items: [{
                            xtype: "button",
                            text: "完成",
                            ref: "formClose",
                            iconCls: "x-fa fa-reply"
                        }]
                    }]
                }); 
                tabItem.add(item);  

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                self.setFormValue(formDeptObj, insertObj);
        
                var arrangeRoomGrid=item.down("grid[ref=arrangeSiteGrid]");    
                arrangeRoomGrid.getStore().getProxy().extraParams.filter= "[{'type':'string','comparison':'=','value':'"+ insertObj.uuid+"','field':'classId'}]";
                arrangeRoomGrid.getStore().load();
                
            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);
        
    },


    doClassDetail_Tab:function(btn, cmd, grid, record) {

        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }


        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode +"]");
        var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode =  basePanel.detCode;  
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
                
        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //本方法只提供班级详情页使用
        var tabTitle = funData.tabConfig.detailTitle;
        //设置tab页的itemId
        var tabItemId=funCode+"_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue= null;
        var operType = "detail";    // 只显示关闭按钮
               
        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择一条数据！");
                return;
            }
            recordData = rescords[0].data;
        }

        insertObj = recordData;
        tabItemId=funCode+"_gridDetail"+insertObj.classNumb;    //详细界面可以打开多个
        items=[{
            xtype:detLayout,
            defaults:null,
            margin:'0 0 0 10',
            items:[{
                xtype:'arrange.classdetailhtmlpanel'
            }]
        }];
        


        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem=tabPanel.getComponent(tabItemId);
        if(!tabItem){
    
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
                    items:items
                }); 
                tabItem.add(item);  
               
                //读取班级信息
                //读取学员信息
                //读取课程信息                                                    
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClass/getClassAllInfo",
                    params: {
                        classId: insertObj.uuid
                    },
                    //回调代码必须写在里面
                    success: function (response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                        if (data.success) {
                            var obj = data.obj;
                            var classInfoContainer = tabItem.down("container[ref=classInfo]");
                            classInfoContainer.setData(obj.classInfo);

                            var classTraineeTitle="（男生 "+obj.classInfo.traineeManNum+" 人；女生 "+obj.classInfo.traineeWomenNum+" 人）";
                            var classTraineeObj={
                                "classTraineeTitle":classTraineeTitle,
                                "classTrainee":obj.classTrainee
                            };
                            var classTraineeInfoContainer = tabItem.down("container[ref=classTraineeInfo]");                                
                            classTraineeInfoContainer.setData(classTraineeObj);
                            
                            var classCourseTitle="（需要评价的课程 "+obj.classInfo.isEvalNum+" 门）";
                            var classCourseObj={
                                "classCourseTitle":classCourseTitle,
                                "classCourse":obj.classCourse
                            };
                            var classCourseInfoContainer = tabItem.down("container[ref=classCourseInfo]");
                            classCourseInfoContainer.setData(classCourseObj);


                            var classFoodInfoContainer = tabItem.down("container[ref=classFoodInfo]");
                            var classFoodObj = {};
                             //统计
                            var classFoodTitle="";
                            var countMoney=(obj.classInfo.breakfastStand*obj.classInfo.foodBreakfastNum +
                                    obj.classInfo.lunchStand*obj.classInfo.foodLunchNum+obj.classInfo.dinnerStand*obj.classInfo.foodDinnerNum)*obj.classInfo.classDay;
                            var countNumberInDay=obj.classInfo.foodBreakfastNum*1 +obj.classInfo.foodLunchNum*1 +obj.classInfo.foodDinnerNum*1;
                            var countNumber=countNumberInDay*obj.classInfo.classDay;

                            classFoodObj.dinnerType = obj.classInfo.dinnerType;
                            if (obj.classInfo.dinnerType == 1){
                                classFoodObj.dinnerTypeName = "围餐";   
                                classFoodTitle="（总额 "+countMoney+" 元 ； 每天围数 "+countNumberInDay+" 围 ； 总围数 "+countNumber+" 围）";                                    
                            }
                            else if (obj.classInfo.dinnerType == 2){
                                classFoodObj.dinnerTypeName = "自助餐";   
                                classFoodTitle="（总额 "+countMoney+" 元 ； 每天份数 "+countNumberInDay+" 份 ； 总份数 "+countNumber+" 份）";                                      
                            }
                            else{
                                classFoodObj.dinnerTypeName = "快餐"; 
                                classFoodTitle="（总额 "+countMoney+" 元 ； 每天份数 "+countNumberInDay+" 份 ； 总份数 "+countNumber+" 份）";                                    
                            }
                           
                            classFoodObj.classFoodTitle=classFoodTitle;
                            classFoodObj.avgNumber = obj.classInfo.avgNumber;
                            classFoodObj.breakfastStand = obj.classInfo.breakfastStand;
                            classFoodObj.breakfastCount = obj.classInfo.foodBreakfastNum;
                            classFoodObj.lunchStand = obj.classInfo.lunchStand;
                            classFoodObj.lunchCount = obj.classInfo.foodLunchNum;
                            classFoodObj.dinnerStand = obj.classInfo.dinnerStand;
                            classFoodObj.dinnerCount = obj.classInfo.foodDinnerNum;
                            //默认为快餐，则显示学员数据   
                            if (obj.classInfo.dinnerType != "1" && obj.classInfo.dinnerType != "2") {

                                //查询班级的就餐信息
                                self.asyncAjax({
                                    url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
                                    params: {
                                        classId: insertObj.uuid,
                                        page: 1,
                                        start: 0,
                                        limit: -1    //-1表示不分页
                                    },
                                    //回调代码必须写在里面
                                    success: function (response) {
                                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                        var rows = data.rows;
                                        if (rows != undefined) { //若存在rows数据，则表明请求正常
                                            classFoodObj.rows = rows;
                                            classFoodInfoContainer.setData(classFoodObj);
                                        } else {
                                            self.Error(data.obj);
                                        }
                                    }
                                });
                            } else {
                                classFoodInfoContainer.setData(classFoodObj);
                            }

                        
                            //查询班级的住宿信息
                            self.asyncAjax({
                                url: comm.get("baseUrl") + "/TrainClasstrainee/getClassRoomTrainees",
                                params: {
                                    classId: insertObj.uuid,
                                    page: 1,
                                    start: 0,
                                    limit: -1    //-1表示不分页
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    var rows = data.rows;
                                    //console.log(rows);
                                    if (rows != undefined) {  //若存在rows数据，则表明请求正常
                                        var classRoomTitle="（午休人数-男 "+obj.classInfo.roomSiestaManNum+" 人 ； 午休人数-女 "+obj.classInfo.roomSiestaWomenNum+" 人 ； "+
                                            "晚宿人数-男 "+obj.classInfo.roomSleepManNum+" 人 ； 晚宿人数-女 "+obj.classInfo.roomSleepWomenNum+" 人 ；"+
                                            "不午休人数 "+obj.classInfo.roomNoSiestaNum+" 人 ； 不晚宿人数 "+obj.classInfo.roomNoSleepNum+" 人）";
                                        var classRoomObj={
                                            "classRoomTitle":classRoomTitle,
                                            "classRoom":rows
                                        };

                                        var classRoomInfoContainer = tabItem.down("container[ref=classRoomInfo]");
                                        classRoomInfoContainer.setData(classRoomObj);
                                    } else {
                                        self.Error(data.obj);
                                    }
                                }
                            });
                        } else {
                            self.Error(data.obj);
                        }

                    }
                });
               

            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);
        
        
    },

    doFoodDetail_Tab: function (btn, cmd, grid, record) {

        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "classarrange_foodDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = {};
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "就餐详情";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridFoodDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
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

                insertObj = recordData;
                tabTitle = "班级就餐申请";
                tabItemId = funCode + "_gridFoodEdit";

                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

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

                insertObj = recordData;
                tabTitle = "查看就餐详情";
                tabItemId = funCode + "_gridFoodDetail" + insertObj.classNumb;
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {

            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,      //保存主键值
                layout: 'fit',
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,         //指定重写事件的控制器
                    funCode: funCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: [{
                        xtype: detLayout,
                        funCode: detCode,
                        items: [{
                            xtype: "arrange.fooddetailform"
                        }]
                    }]
                });
                tabItem.add(item);

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("className").setVisible(true);
                formDeptObj.findField("beginDate").setVisible(true);
                formDeptObj.findField("endDate").setVisible(true);

                self.setFormValue(formDeptObj, insertObj);
                if (cmd == "detail") {
                    self.setFuncReadOnly(funData, objDetForm, true);
                }

                if(insertObj.dinnerType==3){
                    //查询班级的就餐信息
                    self.asyncAjax({
                        url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
                        params: {
                            classId: insertObj.uuid,
                            isDelete:1, //表示排除isDelete为1的数据
                            page: 1,
                            start: 0,
                            limit: -1    //-1表示不分页
                        },
                        //回调代码必须写在里面
                        success: function (response) {
                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                            var rows = data.rows;
                            //console.log(rows);
                            if (rows != undefined) {  //若存在rows数据，则表明请求正常
                                //获取班级学员就餐信息
                                var traineeRoomGrid = tabItem.down("grid[ref=traineeFoodGrid]");
                                traineeRoomGrid.getStore().loadData(rows);
                                
                                //new：初次加载时，计算总数
                                objDetForm.countFood3();//计算汇总数据2                

                            } else {
                                self.Error(data.obj);
                            }
                        }
                    });
                }

            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);

    },


    doImageDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
            var rescords = baseGrid.getSelectionModel().getSelection();
            // var indexAdd = cmd.indexOf("add");
            // if (indexAdd != -1) {
                ////不是增加的操作，只能选择一条记录
                if (rescords.length != 1) {
                    self.msgbox("请选择1条数据！");
                    return;
                }
                recordData = rescords[0].getData();
            //}
        } else {
            baseGrid = grid;
            recordData = record.getData();
        }

        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        var insertObj = self.getDefaultValue(defaultObj);

        var tabTitle = funData.tabConfig.addTitle;
        var tabItemId = funCode + "_gridImageDetail";
        var pkName = funData.pkName;
        var pkValue = null;
        var operType = cmd;
        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "detail":
                insertObj = Ext.apply(insertObj, recordData);
                tabTitle = insertObj.className+"-考勤图片";
                //获取主键值
                pkValue = recordData[pkName];

                tabItemId = funCode + "_gridImageDetail"+pkValue;

                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            classId:pkValue
        });
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,
                layout: 'fit'
            });
            tabPanel.add(tabItem);

            setTimeout(function () {
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,
                    funCode: funCode,
                    detCode: detCode,
                    tabItemId: tabItemId,
                    insertObj: insertObj,
                    funData: popFunData,
                    items: [{
                        xtype: detLayout,
                        funCode: detCode,
                        items: [{
                            xtype: "arrange.imagesgrid"
                        }]
                    }]
                });

                tabItem.add(item);
                //根据需要初始化详情页面数据
                switch (cmd) {
                    case "detail":
                        var detailPanel = item.down("basepanel[xtype=" + detLayout + "]");
                        var imageView =  detailPanel.down("panel[xtype=arrange.imagesgrid] dataview");
                        var imageStore = imageView.getStore();
                        var imageProxy = imageStore.getProxy();
                        var filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + pkValue + "\",\"field\":\"recordId\"}"+
                                    ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"TrainClass\",\"field\":\"entityName\"}"+
                                    ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"jpg\",\"field\":\"attachType\"}"+
                                "]";
                        imageProxy.extraParams = {
                            filter: filter
                        };
                        imageStore.loadPage(1);
                        
                        //在这里绑定stroe
                        var pagetool = detailPanel.down("pagingtoolbar");
                        pagetool.setStore(imageStore);
                        break;
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue && cmd != "detail") {
            self.Warning("你已经打开一个编辑窗口了");
            return;
        }
        tabPanel.setActiveTab(tabItem);
    },

    doSendUserDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "arrange_sendUserDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

      
        var items = [{
            xtype: detLayout
        }];
        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择1条数据！");
                return;
            }
            recordData = rescords[0].data;
        }

        var sendInfo="";
        if(recordData.isarrange==0){
            sendInfo="【" + recordData.className + "】培训班已安排完毕，请查看安排的情况并做好相应准备！（一卡通系统）";
        }else{
            sendInfo="【" + recordData.className + "】培训班有安排更新，请查看安排的情况并做好相应准备！（一卡通系统）"
        }
        insertObj = recordData;
        insertObj = Ext.apply(insertObj,{
            sendInfo:sendInfo
        });
        operType = "edit";
        items = [{
            xtype: detLayout,
            defaults: null,
            items: [{
                xtype: 'class.sendinfoform'
            }]
        }];
        //根据cmd操作类型，来设置不同的值
        var tabTitle = insertObj.className+"-班级安排";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridSendUser";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = insertObj.uuid;

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,      //保存主键值
                layout: 'fit'
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,         //指定重写事件的控制器
                    funCode: detCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: items
                });
                tabItem.add(item);
                var objDetForm = item.down("baseform[xtype=class.sendinfoform]");
                var formDeptObj = objDetForm.getForm();

                self.setFormValue(formDeptObj, insertObj);
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个班级安排窗口！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }
});

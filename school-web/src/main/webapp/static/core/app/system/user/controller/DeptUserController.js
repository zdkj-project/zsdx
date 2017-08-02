Ext.define("core.system.user.controller.DeptUserController", {
    extend: "Ext.app.ViewController",
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        treeUtil: "core.util.TreeUtil",
        gridActionUtil: "core.util.GridActionUtil"
    },
    
    alias: 'controller.user.deptUserController',

    init: function() {
        var self = this
            //事件注册
        this.control({
            "panel[xtype=user.usergrid] actiontextcolumn": {
                gridUserRoleClick:function(data){
                    var baseGrid = data.view;
                    var record = data.record;

                    this.doDetail_Tab(null, data.cmd, baseGrid, record);
                 
                    return false;
                },
                gridDeptJobClick:function(data){
                    var baseGrid = data.view;
                    var record = data.record;

                    this.doDetail_Tab(null, data.cmd, baseGrid, record);
                 
                    return false;
                }
            },

            //点击用户事件响应,刷新用户所属的角色
            "panel[xtype=user.usergrid]": {
                beforeitemclick: function(grid, record, item, index, e, eOpts) {
                    /*
                    var basePanel = grid.up("panel[xtype=user.mainlayout]");
                    var records = grid.getSelectionModel().getSelection();
                    var selUserId = records[0].get("uuid");
                    var roleGrid = basePanel.down("panel[xtype=user.userrolegrid]");
                    var roleStore = roleGrid.getStore();
                    var roleProxy = roleStore.getProxy();
                    roleProxy.extraParams = {
                        userId: selUserId
                    };
                    roleStore.load();
                    */
                }
            },
            //添加用户事件
            "panel[xtype=user.usergrid] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    var self = this;
                    var baseGrid = btn.up("basegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("panel[xtype=user.mainlayout]");
                    var funData = basePanel.funData;
                    var detCode = "selectsysuser_main";
                    var detLayout = "selectsysuser.mainlayout";
                    var deptId = funData.deptId;
                    var isRight = funData.isRight;
                    var deptType = funData.deptType; 
                    if (!deptId){
                        self.Warning("请选择一个部门");
                        return false;
                    }
                    if (isRight=="1"){
                        self.Warning("您无权限给此部门添加用户，请重新选择");
                        return false;                        
                    }
                    if (deptType=="04"||deptType=="05"||deptType=="06"){
                        self.Warning("年级、班级及学科的教师由其它模块维护");
                        return false;                         
                    }
                    // var filterArry = new Array();
                    // filterArry.push("{'type':'string','comparison':'=','value':'" + funData.deptId + "','field':'deptId'}");
                    // filterArry.push("{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}");
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                        //filter: "[" + filterArry.join(",") + "]"
                    });
                    // //选择的部门信息
                    // var deptTree = baseGrid.up("panel[xtype=user.mainlayout]").down("panel[xtype=user.depttree]");
                    // var selectDept = deptTree.getSelectionModel().getSelection();
                    // if (selectDept.length != 1) {
                    //     self.msgbox("请选择部门!");
                    //     return false;
                    // }
                    // var deptObj = selectDept[0];
                    // var deptId = deptObj.get("id");
                    // var deptName = deptObj.get("text");
                    // var deptCode = deptObj.get("code");
                    // //处理特殊默认值
                    // var defaultObj = funData.defaultObj;
                    // var insertObj = self.getDefaultValue(defaultObj);
                    // //根据选择的记录与操作确定form初始化的数据
                    // insertObj = Ext.apply(insertObj, {
                    //     deptId: deptId,
                    //     deptName: deptName
                    // }); //
                    var iconCls = "table_add";
                    var title = "新增用户";
                    var winId = detCode + "_win";
                    var win = Ext.getCmp(winId);
                    if (!win) {
                        win = Ext.create('core.app.base.BaseFormWin', {
                            id: winId,
                            title: title,
                            width: comm.get("clientWidth")*0.6,
                            height: 768,
                            resizable: false,
                            iconCls: iconCls,
                            operType: "addReturn",
                            funData: popFunData,
                            funCode: detCode,
                            items: [{
                                xtype: detLayout
                            }]
                        });
                    }
                    win.show();
                    return false;
                }
            },
            
            "panel[xtype=user.usergrid] button[ref=sync]": {
                beforeclick: function(btn) {
                	 var resObj = self.ajax({
                         url: "/usersync" + "/list"
                     });
                     if (resObj.success) {
                    	 self.msgbox("同步成功!");
                    	 btn.up("basegrid").getStore().load();
                     }else{
                    	 //self.msgbox("同步成功!");
                    	 self.msgbox("请先同步部门和岗位数据!");
                     }
                     return false;
                }
            },

            "panel[xtype=user.usergrid] button[ref=syncToUP]": {
                beforeclick: function(btn) {         
                     //同步人员数据事件                        
                    var baseGrid = btn.up("grid");
                   
                    Ext.MessageBox.confirm('同步人员数据到UP', '您确定要执行同步人员数据到UP操作吗？', function(btn, text) {                  
                        if (btn == 'yes') {
                            
                            Ext.Msg.wait('正在同步人员数据，请等待...','提示');
                            
                            setTimeout(function(){

                                //异步ajax加载
                                Ext.Ajax.request({
                                    url: comm.get('baseUrl') + "/sysuser/doSyncAllUserInfoToUp",
                                    params: { },
                                    timeout:1000*60*60*10,     //10个小时
                                    success: function(response){
                                        var result=JSON.parse(response.responseText);

                                        if (result.success) {   
                                            Ext.Msg.hide();                               
                                            self.msgbox(result.msg);
                                            baseGrid.getStore().loadPage(1);

                                                                                  
                                        } else {
                                            Ext.Msg.hide();  
                                            self.Error(result.msg);                                        
                                        }
                                       
                                       
                                    },
                                    failure: function(response, opts) {
                                        Ext.Msg.hide();  
                                        self.Error("请求失败，请联系管理员！");
                                      
                                    }
                                });                              
                            },100);                           
                        }
                    });

                    return false;
                }
            },
            
            "panel[xtype=user.usergrid] button[ref=syncCardInfoFromUP]": {
                beforeclick: function(btn) {         
                     //同步人员数据事件                        
                    var baseGrid = btn.up("grid");
                   
                    Ext.MessageBox.confirm('从UP同步发卡信息', '您确定要执行从UP同步发卡信息操作吗？', function(btn, text) {                  
                        if (btn == 'yes') {
                            
                            Ext.Msg.wait('正在从UP同步发卡信息，请等待...','提示');
                            
                            setTimeout(function(){

                                //异步ajax加载
                                Ext.Ajax.request({
                                    url: comm.get('baseUrl') + "/sysuser/doSyncAllCardInfoFromUp",
                                    params: { },
                                    timeout:1000*60*60*10,     //10个小时
                                    success: function(response){
                                        var result=JSON.parse(response.responseText);

                                        if (result.success) {  
                                            Ext.Msg.hide();                                
                                            self.msgbox(result.msg);
                                            baseGrid.getStore().loadPage(1);

                                                                                
                                        } else {
                                            Ext.Msg.hide(); 
                                            self.Error(result.msg);
                                          
                                        }
                                        
                                        
                                    },
                                    failure: function(response, opts) {
                                        Ext.Msg.hide();  
                                        self.Error("请求失败，请联系管理员！");                                       
                                    }
                                });                              
                            },100);                           
                        }
                    });

                    return false;
                }
            },
            


            //添加用户选择后确定事件
            "baseformwin[funCode=selectsysuser_main] button[ref=formSave]": {
                beforeclick: function(btn) {
                    var win = btn.up('window');
                    var funCode = win.funCode;
                    var funData = win.funData;
                    var deptId = funData.deptId;
                    var basePanel = win.down("basepanel[funCode=" + funCode + "]");
                    var isSelectGrid = basePanel.down("panel[xtype=selectsysuser.isselectusergrid]");
                    var isSelectStore = isSelectGrid.getStore();
                    var iCount = isSelectStore.getCount(); //已选的角色个数
                    //拼装所选择的角色
                    var ids = new Array();
                    for (var i = 0; i < iCount; i++) {
                        var record = isSelectStore.getAt(i);
                        var pkValue = record.get("uuid");
                        ids.push(pkValue);
                    }
                    if (ids.length > 0) {
                        var resObj = self.ajax({
                            url: funData.action + "/batchSetDept",
                            params: {
                                deptId: deptId,
                                ids: ids.join(",")
                            }
                        });
                        if (resObj.success) {
                            self.msgbox("保存成功!");
                            var grid = win.funData.grid; //窗体是否有grid参数
                            if (!Ext.isEmpty(grid)) {
                                var store = grid.getStore();
                                var proxy = store.getProxy();
                                proxy.extraParams = {
                                    whereSql: win.funData.whereSql,
                                    orderSql: win.funData.orderSql,
                                    deptId: deptId
                                };
                                store.load(); //刷新父窗体的grid
                                win.close();
                            }
                        } else {
                            if (!Ext.isEmpty(resObj.obj))
                                self.Info(resObj.obj);
                        }
                    } else {
                        self.Warning("没有选择用户");
                    }
                    if (btn.callback) {
                        btn.callback();
                    }

                    return false;
                }
            },

            //修改用户事件
            "panel[xtype=user.usergrid] button[ref=gridEdit]": {
                beforeclick: function(btn) {
                    self.doDetail(btn, "edit");
                    return false;
                }
            },

            //删除用户事件
            "panel[xtype=user.usergrid] button[ref=gridDelete]": {
                beforeclick: function(btn) {
                    var userGrid = btn.up("basegrid");
                    var mainLayout = userGrid.up("panel[xtype=user.mainlayout]");
                    var funData = mainLayout.funData;
                    var deptId = funData.deptId;
                    //用户所属角色的grid
                    var userRoleGrid = mainLayout.down("panel[xtype=user.userrolegrid]");
                    //选择的用户
                    var selectUser = userGrid.getSelectionModel().getSelection();
                    if (selectUser.length == 0) {
                        self.Error("请选择要删除的用户");
                        return false;
                    }

                    //拼装所选择的用户
                    var ids = new Array();
                    Ext.each(selectUser, function(rec) {
                        var pkValue = rec.get("uuid");
                        ids.push(pkValue);
                    });
                    var title = "确定删除所选择的用户吗？";
                    Ext.Msg.confirm('信息', title, function(btn, text) {
                        if (btn == 'yes') {
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: funData.action + "/dodelete",
                                params: {
                                    ids: ids.join(","),
                                    deptId:deptId
                                }
                            });
                            if (resObj.success) {
                                //刷新用户所属角色列表
                                var store = userRoleGrid.getStore();
                                var proxy = store.getProxy();
                                proxy.extraParams = {
                                    userId: "0"
                                };
                                store.load();

                                //刷新用户列表
                                var userStore = userGrid.getStore();
                                var userPoxy = userStore.getProxy();
                                var filterArry = new Array();
                                filterArry.push("{'type':'string','comparison':'=','value':'" + deptId + "','field':'deptId'}");
                                filterArry.push("{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}");
                                userPoxy.extraParams = {
                                    filter: "[" + filterArry.join(",") + "]",
                                    deptId:deptId
                                };
                                userStore.load();

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

            //锁定账户事件
            "panel[xtype=user.usergrid] button[ref=gridLock]": {
                click: function(btn) {
                    self.doList(btn, "lock");
                    return false;
                }
            },

            //解锁账户事件
            "panel[xtype=user.usergrid] button[ref=gridUnLock]": {
                click: function(btn) {
                    self.doList(btn, "unlock");
                    return false;
                }
            },

            //重置密码事件
            "panel[xtype=user.usergrid] button[ref=gridSetPwd]": {
                click: function(btn) {
                    self.doList(btn, "setpwd");
                    return false;
                }
            },

            

            

            //待选人员列表的事件
            "panel[xtype=selectsysuser.selectusergrid]":{
                beforeitemdblclick:function(grid, record, item, index, e, eOpts) {
                    selectStore = grid.getStore();
                    selectStore.removeAt(index);

                    var basePanel = grid.up("panel[xtype=selectsysuser.mainlayout]");
                    var isSelectGrid = basePanel.down("panel[xtype=selectsysuser.isselectusergrid]");
                    var isSelectStore = isSelectGrid.getStore();
                    isSelectStore.insert(0,[record]);
                    return false;
                }
            },
            //已选人员列表的事件
            "panel[xtype=selectsysuser.isselectusergrid]":{
                beforeitemdblclick:function(grid, record, item, index, e, eOpts) {
                    isSelectStore = grid.getStore();
                    isSelectStore.removeAt(index);

                    var basePanel = grid.up("panel[xtype=selectsysuser.mainlayout]");
                    var selectGrid = basePanel.down("panel[xtype=selectsysuser.selectusergrid]");
                    var selectStore = selectGrid.getStore();
                    selectStore.insert(0,[record]);
                    return false;
                }
            }            
        });
    },
    //用户增加、修改处理
    doDetail: function(btn, cmd) {
        var self = this;
        var baseGrid = btn.up("basegrid");
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("panel[xtype=user.mainlayout]");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        // var filterArry = new Array();
        // filterArry.push("{'type':'string','comparison':'=','value':'" + funData.deptId + "','field':'deptId'}");
        // filterArry.push("{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}");
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            //filter: "[" + filterArry.join(",") + "]",
            deptId: funData.deptId
        });
        //选择的部门信息
        var deptTree = baseGrid.up("panel[xtype=user.mainlayout]").down("panel[xtype=user.depttree]");
        var selectDept = deptTree.getSelectionModel().getSelection();
        if (selectDept.length != 1) {
            self.msgbox("请选择部门!");
            return false;
        }
        var deptObj = selectDept[0];
        var deptId = deptObj.get("id");
        var deptName = deptObj.get("text");
        var deptCode = deptObj.get("code");
        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);
        //根据选择的记录与操作确定form初始化的数据
        insertObj = Ext.apply(insertObj, {
            deptId: deptId,
            deptName: deptName
        }); //
        var iconCls = "x-fa fa-plus-circle";
        var title = "新增用户";
        switch (cmd) {
            case "add":
                break;
            case "edit":
                var records = baseGrid.getSelectionModel().getSelection();
                iconCls = "x-fa fa-pencil-square";
                operType = "edit";
                title = "修改用户";
                insertObj = Ext.apply(insertObj, records[0].data);
                break;
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                title: title,
                width: 600,
                height: 330,
                resizable: false,
                iconCls: iconCls,
                operType: cmd,
                funData: popFunData,
                funCode: detCode,
                //给form赋初始值
                insertObj: insertObj,
                items: [{
                    xtype: "user.userlayout"
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

    //锁定账户、解锁账户及重置密码
    doList: function(btn, cmd) {
        var self = this;
        var userGrid = btn.up("basegrid");
        var mainLayout = userGrid.up("panel[xtype=user.mainlayout]");
        //var userRoleGrid = mainLayout.down("panel[xtype=user.userrolegrid]");
        var funData = mainLayout.funData;
        var deptId = funData.deptId;
        var info = "";
        var title = "";
        var url = "";
        switch (cmd) {
            case "lock":
                info = "请选择要锁定的账户";
                title = "确定要锁定选择的账户吗？";
                url = funData.action + "/dolock";
                break;
            case "unlock":
                info = "请选择要解锁的账户";
                title = "确定要解锁选择的账户吗？";
                url = funData.action + "/dounlock";
                break;
            case "setpwd":
                info = "请选择要重置密码的账户";
                title = "确定要重置所选账户的密码吗？";
                url = funData.action + "/dosetpwd";
                break;
        }
        //选择的用户
        var selectUser = userGrid.getSelectionModel().getSelection();
        if (selectUser.length == 0) {
            self.Warning(info);
            return false;
        }
        //拼装所选择的用户
        var ids = new Array();
        Ext.each(selectUser, function(rec) {
            var pkValue = rec.get("uuid");
            ids.push(pkValue);
        });

        //ajax的方式提交数据
        Ext.Msg.confirm('信息', title, function(btn, text) {
            if (btn == 'yes') {
                //发送ajax请求
                var resObj = self.ajax({
                    url: url,
                    params: {
                        ids: ids.join(",")
                    }
                });
                if (resObj.success) {
                    //刷新用户所属角色列表
                    // var store = userRoleGrid.getStore();
                    // var proxy = store.getProxy();
                    // proxy.extraParams = {
                    //     userId: "0"
                    // };
                    // store.load();

                    //刷新用户列表
                    var userStore = userGrid.getStore();
                    var userPoxy = userStore.getProxy();
                    userPoxy.extraParams = {
                        deptId:deptId
                    };
                    userStore.load();

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
    },

    doDetail_Tab:function(btn, cmd, grid, record) {

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

        
        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择一条数据！");
                return;
            }
            recordData = rescords[0].data;
        }

        insertObj = recordData;

         //本方法只提供班级详情页使用
        var tabTitle =insertObj.xm+"-角色管理";
        //设置tab页的itemId
        var pkValue= null;
        var operType = "detail";    // 只显示关闭按钮
        var tabItemId=funCode+"_gridUserRole"+insertObj.classNumb;    //详细界面可以打开多个
        items=[{
            xtype:detLayout,
            defaults:null,
            items:[{
                xtype:'user.userrolegrid',
                title:null
            }]
        }];
        switch(cmd){
            case 'userRole':
                var tabTitle =insertObj.xm+"-角色管理";
                //设置tab页的itemId
                var operType = "detail";    // 只显示关闭按钮
                var tabItemId=funCode+"_gridUserRole"+insertObj.uuid;    //详细界面可以打开多个
                items=[{
                    xtype:detLayout,
                    defaults:null,
                    items:[{
                        xtype:'user.userrolegrid',
                        title:null
                    }]
                }];
                break;
            case 'deptJob':
                var tabTitle =insertObj.xm+"-部门岗位";
                //设置tab页的itemId
                var operType = "detail";    // 只显示关闭按钮
                var tabItemId=funCode+"_gridDeptJob"+insertObj.uuid;    //详细界面可以打开多个
                items=[{
                    xtype:detLayout,
                    defaults:null,
                    items:[{
                        xtype:'user.userdeptjobgrid',
                        title:null
                    }]
                }];
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
                
                switch(cmd){
                    case 'userRole':

                        var roleGrid = item.down("panel[xtype=user.userrolegrid]");
                        var roleStore = roleGrid.getStore();
                        var roleProxy = roleStore.getProxy();
                        roleProxy.extraParams = {
                            userId: insertObj.uuid
                        };
                        roleStore.load();

                        break;
                    case 'deptJob':
                        var deptJobGrid = item.down("panel[xtype=user.userdeptjobgrid]");
                        var deptJobStore = deptJobGrid.getStore();
                        var deptJobProxy = deptJobStore.getProxy();
                        deptJobProxy.extraParams = {
                            userId: insertObj.uuid
                        };
                        deptJobStore.load();
                        break;
                }

                

            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);
        
        
    }

});
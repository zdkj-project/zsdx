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
            //点击用户事件响应,刷新用户所属的角色
            "panel[xtype=user.usergrid]": {
                beforeitemclick: function(grid, record, item, index, e, eOpts) {
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
                                            self.msgbox(result.msg);
                                            baseGrid.getStore().loadPage(1);

                                            Ext.Msg.hide();                                        
                                        } else {
                                            self.Error(result.msg);
                                            Ext.Msg.hide(); 
                                        }
                                       
                                       
                                    },
                                    failure: function(response, opts) {
                                        self.Error("请求失败，请联系管理员！");
                                        Ext.Msg.hide(); 
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
                                            self.msgbox(result.msg);
                                            baseGrid.getStore().loadPage(1);

                                            Ext.Msg.hide();                                        
                                        } else {
                                            self.Error(result.msg);
                                            Ext.Msg.hide(); 
                                        }
                                        
                                        
                                    },
                                    failure: function(response, opts) {
                                        self.Error("请求失败，请联系管理员！");
                                        Ext.Msg.hide(); 
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

            //添加用户所属角色事件
            "panel[xtype=user.userrolegrid] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    var userRoleGrid = btn.up("basegrid");
                    var mainLayout = userRoleGrid.up("panel[xtype=user.mainlayout]");
                    var funData = mainLayout.funData;
                    //选择的用户
                    var userGrid = mainLayout.down("panel[xtype=user.usergrid]");
                    var selectUser = userGrid.getSelectionModel().getSelection();
                    if (selectUser.length == 0) {
                        self.Error("请选择要增加角色的用户");
                        return false;
                    }
                    var selectUserId = selectUser[0].get("uuid");
                    var detCode = "user_selectrolemain";
                    var popFunData = Ext.apply(funData, {
                        grid: userRoleGrid,
                        userId: selectUserId
                    }); //
                    var cmd = "edit";
                    var winId = detCode + "_win";
                    var win = Ext.getCmp(winId);
                    if (!win) {
                        win = Ext.create('core.base.view.BaseFormWin', {
                            id: winId,
                            title: "用户角色选择",
                            width: 1024,
                            height: 768,
                            resizable: false,
                            controller:'user.otherController',
                            iconCls: "x-fa fa-user",
                            operType: cmd,
                            funData: popFunData,
                            funCode: detCode,
                            txtformSave: "确定",
                            items: [{
                                xtype: "user.selectrolelayout"
                            }]
                        });
                    }
                    win.show();
                    //待选的项目中要过虑掉已选择的
                    var selectRoleGrid = win.down("panel[xtype=user.selectrolegrid]");
                    var selectRoletore = selectRoleGrid.getStore();
                    var selectRoleProxy = selectRoletore.getProxy();
                    selectRoleProxy.extraParams = {
                        userId: selectUserId
                    };
                    selectRoletore.load();
                    return false;
                }
            },

            //删除用户所属角色事件
            "panel[xtype=user.userrolegrid] button[ref=gridDelete]": {
                beforeclick: function(btn) {
                    var userRoleGrid = btn.up("basegrid");
                    var mainLayout = userRoleGrid.up("panel[xtype=user.mainlayout]");
                    var funData = mainLayout.funData;
                    //选择的用户
                    var userGrid = mainLayout.down("panel[xtype=user.usergrid]");
                    var selectUser = userGrid.getSelectionModel().getSelection();
                    if (selectUser.length == 0) {
                        self.Error("请选择要删除的角色");
                        return false;
                    }
                    var selectUserId = selectUser[0].get("uuid");

                    //选择的角色
                    var selectUserRole = userRoleGrid.getSelectionModel().getSelection();
                    if (selectUserRole.length == 0) {
                        self.Warning("没有选择要删除的角色，请选择");
                        return false;
                    }
                    var store = userRoleGrid.getStore();
                    var recdCount = store.getCount();
                    if (recdCount==1){
                        self.Warning("每个用户至少要包含一个角色，不能再删除");
                        return false;                       
                    }
                    if (recdCount==selectUserRole.length){
                        self.Warning("每个用户至少要包含一个角色，不能全部删除");
                        return false;      
                    }
                    //拼装所选择的角色
                    var ids = new Array();
                    Ext.each(selectUserRole, function(rec) {
                        var pkValue = rec.get("uuid");
                        ids.push(pkValue);
                    });
                    var title = "删除角色后，用户将不再拥有这些角色的权限，确定删除吗？";
                    Ext.Msg.confirm('警告', title, function(btn, text) {
                        if (btn == 'yes') {
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: funData.action + "/deleteUserRole",
                                params: {
                                    ids: ids.join(","),
                                    userId: selectUserId
                                }
                            });
                            if (resObj.success) {
                                var store = userRoleGrid.getStore();
                                var proxy = store.getProxy();
                                var filterArry = new Array();
                                filterArry.push("{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}");
                                proxy.extraParams = {
                                    filter: "[" + filterArry.join(",") + "]",
                                    userId: selectUserId
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
        var userRoleGrid = mainLayout.down("panel[xtype=user.userrolegrid]");
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
                    var store = userRoleGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        userId: "0"
                    };
                    store.load();

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
    }
});
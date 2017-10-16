/**
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.system.user.controller.OtherController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.user.otherController',
    
    /*把不需要使用的组件，移除掉*/
    mixins: {
        messageUtil: "core.util.MessageUtil",
        suppleUtil: "core.util.SuppleUtil", 
        formUtil: "core.util.FormUtil",
        /*gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil',*/
        queryUtil: "core.util.QueryUtil"
    },
   
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 other controler");           
    },

    /*该视图内的组件事件注册*/
    control:{    
    
        //用户所属角色选择后确定事件
        "baseformwin[funCode=user_selectrolemain] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;
                
                var win = btn.up('window');
                var funCode = win.funCode;
                var funData = win.funData;
                var userId = funData.userId;
                var basePanel = win.down("basepanel[funCode=" + funCode + "]");
                var isSelectGrid = basePanel.down("panel[xtype=user.isselectrolegrid]");
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
                        url: funData.action + "/addUserRole",
                        params: {
                            userId: userId,
                            ids: ids.join(",")
                        }
                    });
                    if (resObj.success) {
                        self.Info("保存成功!");
                        var grid = win.funData.grid; //窗体是否有grid参数
                        if (!Ext.isEmpty(grid)) {
                            var store = grid.getStore();
                            var proxy = store.getProxy();
                            proxy.extraParams = {
                                whereSql: win.funData.whereSql,
                                orderSql: win.funData.orderSql,
                                userId: userId
                            };
                            store.load(); //刷新父窗体的grid
                            win.close();
                        }
                    } else {
                        if (!Ext.isEmpty(resObj.obj))
                            self.Info(resObj.obj);
                    }
                } else {
                    self.Warning("没有设定角色");
                }
                if (btn.callback) {
                    btn.callback();
                }

                return false;
            }
        },
        
        //选择班主任，界面加载事件
        "window[funcPanel=selectsysuser.mainlayout]":{
            afterrender:function(win){

                var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem=tabPanel.getActiveTab();
                var formpanel=tabItem.down('form[xtype=' + win.formPanel + ']');
                //var formpanel = Ext.ComponentQuery.query('form[xtype=' + win.formPanel + ']')[0];
                var classId = formpanel.getForm().findField("uuid").getValue();
                var grid = win.down("grid[xtype=selectsysuser.isselectusergrid]");
                var store = grid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                	uuid: classId
                };
                store.load();
                return false;
            }
        },
        //选择班主任，勾选后提交事件
        "window[funcPanel=selectsysuser.mainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {
                
                var win=btn.up("window[funcPanel=selectsysuser.mainlayout]");
                var grid=win.down("grid[xtype=selectsysuser.isselectusergrid]");
                var baseGrid=win.down("basegrid");

                var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem=tabPanel.getActiveTab();
                var formpanel=tabItem.down('form[xtype=' + win.formPanel + ']');
                //var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                
                //var dataField=win.dataField;
                var store=grid.getStore();
                var nameArray=new Array();
                var idArray=new Array();                            

                for(var i=0;i<store.getCount();i++){
                    if(idArray.indexOf(store.getAt(i).get("uuid"))==-1||store.getAt(i).get("uuid")=="null"){
                        nameArray.push(store.getAt(i).get("xm"));
                        idArray.push(store.getAt(i).get("uuid")?store.getAt(i).get("uuid"):" ");  //为空的数据，要使用一个空格号隔开，否则后台split分割有误                        
                    }                        
                }
                            
                formpanel.getForm().findField("mettingEmpid").setValue(idArray.join(","));
                formpanel.getForm().findField("mettingEmpname").setValue(nameArray.join(","));                  

                //baseGrid.getStore().getProxy().extraParams.filter="[{'type':'string','comparison':'','value':'学生','field':'jobName'}]";
                baseGrid.getStore().getProxy().extraParams.filter="[]";
                win.close();
                return false;
            }
        },
        
        /*
        "baseformwin button[ref=formContinue]": {
            beforeclick:function(btn){
                console.log(btn);
            }
        },
        
        "baseformwin button[ref=formSave]": {
            beforeclick: function(btn) {
                console.log(btn);
            }
        },


        "baseformwin button[ref=formClose]": {
            beforeclick: function(btn) {
                console.log(btn);
            }
        },
        */

        //添加用户所属角色事件
        "panel[xtype=user.userrolegrid] button[ref=gridAdd]": {
            beforeclick: function(btn) {
                var self=this;

                var userRoleGrid = btn.up("basegrid");

                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");    
                    
                var funData = basePanel.funData;
                var insertObj = basetab.insertObj;

                //选择的用户
                var selectUserId = insertObj.uuid;
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
                        height: 600,
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
                var self=this;

                var userRoleGrid = btn.up("basegrid");
                
                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");    

                var funData = basePanel.funData;
                var insertObj = basetab.insertObj;

                //选择的用户
                var selectUserId =  insertObj.uuid;
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
                            self.Info(resObj.obj);
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });
               
                return false;
            }
        },

        //添加用户部门岗位事件
        "panel[xtype=user.userdeptjobgrid] button[ref=gridAdd]": {
            beforeclick: function(btn) {
                var self=this;
                var userDeptJobGrid = btn.up("basegrid");
                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");    

                var funData = basePanel.funData;
                var insertObj = basetab.insertObj;
                
               
                var userId = insertObj.uuid;
                var title = "选择部门岗位";

                var funcPanel = 'user.userdeptjob'; //仅仅是用于为编写确定按钮事件提供一个判断的标识

                var configInfo = {
                    rootId: "ROOT",
                    model: "com.zd.school.plartform.baseset.model.BaseDpetJobTree",
                    ddCode: "DEPTJOBTREE",
                    multiSelect: true,
                    whereSql: "",
                    orderSql: " ",
                    excludes: "",
                    url: comm.get('baseUrl') + "/BaseDeptjob/getDeptJobTree",
                };

                self.selTreeWin({
                    controller:'user.otherController',
                    model: configInfo.model,
                    title: title,
                    funcPanel: funcPanel, //为了方便在控制器中捕获此窗口的确定事件
                    multiSelect: configInfo.multiSelect,
                    haveButton: true,
                    isEmpty: true,
                    setIds: userId,
                    funData: funData,
                    grid: userDeptJobGrid,
                    config: {
                        url: configInfo.url,
                        params: {
                            node: configInfo.rootId,
                            ddCode: configInfo.ddCode,
                            whereSql: configInfo.whereSql,
                            orderSql: configInfo.orderSql,
                            excludes: configInfo.excludes,
                            expanded: true
                        }
                    }
                });

                return false;
            }
        },

        "panel[xtype=user.userdeptjobgrid] button[ref=gridDelete]": {
            beforeclick: function(btn) {
                var self=this;
                var deptJobGrid = btn.up("basegrid");
                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");    

                var funData = basePanel.funData;
                var insertObj = basetab.insertObj;

                var userId = insertObj.uuid;

                var records = deptJobGrid.getSelectionModel().getSelection();
                if (records.length < 1) {
                    self.Warning("请选择要解除的部门岗位");
                    return false;
                }
                var delJobs = new Array();
                var masterDept = 0;
                Ext.each(records, function(rec) {
                    //if (rec.get("masterDept") == 0) {
                        delJobs.push(rec.get("uuid"));
                    //} else {
                    //    masterDept = 1;
                    //}

                }, this);
                var title = "确定解除这些部门岗位吗？";
                /*if (delJobs.length == 0) {
                    self.Warning("没有选择岗位或所选岗位为主部门岗位，请重新选择");
                    return false;
                }*/
                Ext.Msg.confirm('解除确认', title, function(btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        self.asyncAjax({
                            url: funData.action+ "/removeUserFromDeptJob",
                            params: {
                                ids: delJobs.join(","),
                            },
                            timeout: 360000,
                            loadMask:true,
                            //回调代码必须写在里面
                            success: function(response) {
                                data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                Ext.Msg.hide(); //关闭loadMask

                                if (data.success) {
                                    var deptJobStore = deptJobGrid.getStore();
                                    // var proxy = deptJobStore.getProxy();
                                    // // var filter = "[{'type':'string','comparison':'=','value':'" + deptId + "','field':'deptId'}]";
                                    // proxy.extraParams = {
                                    //     ids: setIds
                                    // };
                                    deptJobStore.load();
                                    self.Info(data.obj);
                                } else
                                    self.Warning(data.obj);
                            }
                        });           
                    }
                });
                return false;
            }
        },
        
        "baseformtab[funCode=user_main] button[ref=formSave]": {
            beforeclick: function (btn) {
        		var self=this;
                //得到组件
        		var basetab = btn.up('baseformtab');
                var tabPanel = btn.up("tabpanel[xtype=app-main]");
                var tabItemId = basetab.tabItemId;
                var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页


                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var detPanel = basetab.down("basepanel[funCode=" + detCode + "]");
                var objForm = detPanel.down("baseform[funCode=" + detCode + "]");

                var formObj = objForm.getForm();
                var funData = detPanel.funData;
                var pkName = funData.pkName;
                var pkField = formObj.findField(pkName);
                var params = self.getFormValue(formObj);
                var orderIndex = 1;
                if (formObj.findField("orderIndex")) {
                    orderIndex = formObj.findField("orderIndex").getValue() + 1;
                }

                //判断当前是保存还是修改操作
                var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
                if (formObj.isValid()) {

                    var loading = new Ext.LoadMask(basetab, {
                        msg: '正在提交，请稍等...',
                        removeMask: true// 完成后移除
                    });
                    loading.show();

                    self.asyncAjax({
                        url: funData.action + "/" + act,
                        params: params,
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                self.Info("保存成功!");
                                var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                if (!Ext.isEmpty(grid)) {
                                    var store = grid.getStore();                                  
                                    store.loadPage(1); //刷新父窗体的grid                                     
                                }

                                loading.hide();
                                tabPanel.remove(tabItem);
                            } else {
                                self.Error(data.obj);
                                loading.hide();
                            }
                        }
                    });

                } else {
                    var errors = ["前台验证失败，错误信息："];
                    formObj.getFields().each(function (f) {
                        if (!f.isValid()) {
                            errors.push("<font color=red>" + f.fieldLabel + "</font>：" + f.getErrors().join(","));
                        }
                    });
                    self.msgbox(errors.join("<br/>"));
                }
                return false;
            },
        },
        

        "panel[xtype=user.userdeptjobgrid] button[ref=setMasterDept]": {
            beforeclick: function(btn) {
                var self=this;

                var deptJobGrid = btn.up("basegrid");
                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");    

                var funData = basePanel.funData;
                var insertObj = basetab.insertObj;

                var userId = insertObj.uuid;
    
                var records = deptJobGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("只能设置一个主部门岗位，请重新选择");
                    return false;
                }
                // if (records[0].get("masterDept") == 1) {
                //     self.Warning("所选岗位已是主部门岗位，无需再设置");
                //     return false;
                // }
                var ids = records[0].get("uuid");
                var title = "确定设置岗位为主部门岗位吗？";
                Ext.Msg.confirm('设置确认', title, function(btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        self.asyncAjax({
                            url: funData.action+ "/setMasterDeptJob",
                            params: {
                                ids: ids,
                                setIds: userId
                            },
                            timeout: 360000,
                            loadMask:true,
                            //回调代码必须写在里面
                            success: function(response) {
                                data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                Ext.Msg.hide(); //关闭loadMask

                                if (data.success) {
                                    var deptJobStore = deptJobGrid.getStore();
                                    // var proxy = deptJobStore.getProxy();
                                    // // var filter = "[{'type':'string','comparison':'=','value':'" + deptId + "','field':'deptId'}]";
                                    // proxy.extraParams = {
                                    //     ids: setIds
                                    // };
                                    deptJobStore.load();
                                    self.Info(data.obj);
                                } else
                                    self.Warning(data.obj);
                            }
                        });                    
                    }
                });
                return false;            
            }
        },

        /**
         * 教师设定岗位确定事件，只获取岗位
         * @type {[type]}
         */
        "mtsswinview[funcPanel=user.userdeptjob] button[ref=ssOkBtn]": {
            beforeclick: function(btn) {
                var self=this;

                var win = btn.up("mtsswinview");
                var setIds = win.setIds;
                var funData = win.funData;
                var deptJobGrid = win.grid;
                var arry = new Array();
                //树形查询处理
                if (win.queryType == "mttreeview") {                    
                    var tree = win.down("mttreeview");
                    var records = tree.getChecked();
                    if (records.length == 1) {                       
                        if (records[0].get("level") < 99) {
                            self.Warning("请选择岗位");
                            return false;
                        }
                    }
                    Ext.each(records, function(rec) {
                        if (rec.get("level") == 99)
                            arry.push(rec.get("id"));
                    });
                    if (arry.length == 0) {
                        self.Warning("请选择岗位");
                        return false;
                    }
                    
                    self.asyncAjax({
                        url: funData.action + "/addUserToDeptJob",
                        params: {
                            ids: arry.join(","),
                            setIds: setIds
                        },
                        timeout: 360000,
                        loadMask:true,
                        //回调代码必须写在里面
                        success: function(response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            Ext.Msg.hide(); //关闭loadMask

                            if (data.success) {
                                var deptJobStore = deptJobGrid.getStore();
                                // var proxy = deptJobStore.getProxy();
                                // // var filter = "[{'type':'string','comparison':'=','value':'" + deptId + "','field':'deptId'}]";
                                // proxy.extraParams = {
                                //     ids: setIds
                                // };
                                deptJobStore.load();
                                self.Info(data.obj);
                            } else
                                self.Warning(data.obj);
                            win.close();
                        }
                    });   
                }
                return false;
            }
        },

    }


});
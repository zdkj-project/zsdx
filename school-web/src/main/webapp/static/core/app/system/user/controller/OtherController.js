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
        /*
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        */
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
                        self.msgbox("保存成功!");
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
        }
        
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

    }

});
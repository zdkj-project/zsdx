/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.oa.meeting.meetinginfo.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.meetinginfo.otherController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function() {
    },
    /** 该视图内的组件事件注册 */
    control: {
        "baseformtab button[ref=formContinue]": {
            beforeclick: function(btn) {
                console.log(btn);
            }
        },

        "baseformtab button[ref=formSave]": {
            beforeclick: function (btn) {
                var self = this;
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

                //把checkbox的值转换为数字 ；    暂时测试时设置，
                params.needChecking=params.needChecking==true?1:0;
                //params.needSynctrainee=params.needSynctrainee==true?1:0;

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

                                self.msgbox("保存成功!");

                                var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                if (!Ext.isEmpty(grid)) {
                                    var store = grid.getStore();
                                    /* zzk：2017-4-6 直接使用store中自带的条件
                                     var proxy = store.getProxy();
                                     proxy.extraParams = {
                                     whereSql: win.funData.whereSql,
                                     orderSql: win.funData.orderSql,
                                     filter: win.funData.filter
                                     };*/                                 
                                    store.load(); //刷新父窗体的grid                                  
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
            }
        },

        "baseformtab button[ref=formClose]": {
            beforeclick: function(btn) {
               // console.log(btn);
            }
        },

        //选择班主任，界面加载事件
        "window[funcPanel=meetinginfo.selectsysuser.mainlayout]":{
            afterrender:function(win){

                var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem=tabPanel.getActiveTab();
                var formpanel=tabItem.down('form[xtype=' + win.formPanel + ']');
                //var formpanel = Ext.ComponentQuery.query('form[xtype=' + win.formPanel + ']')[0];
                var ids = formpanel.getForm().findField("mettingEmpid").getValue();
                var grid = win.down("grid[xtype=meetinginfo.selectsysuser.isselectusergrid]");
                var store = grid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    ids: ids
                };
                store.load();
                return false;
            }
        },
        //选择班主任，勾选后提交事件
        "window[funcPanel=meetinginfo.selectsysuser.mainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {
                
                var win=btn.up("window[funcPanel=meetinginfo.selectsysuser.mainlayout]");
                var grid=win.down("grid[xtype=meetinginfo.selectsysuser.isselectusergrid]");
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

        /**
         * 导入会议的保存按钮
         */
        "panel[xtype=meetinginfo.meetingimportform] button[ref=formSave]": {
            beforeclick: function (btn) {
                var self = this;
                var dicForm = btn.up('panel[xtype=meetinginfo.meetingimportform]');
                var objForm = dicForm.getForm();
                if (objForm.isValid()) {
                    objForm.submit({
                        url: comm.get('baseUrl') + "/OaMeeting/importData",
                        waitMsg: '正在导入文件...',
                        success: function (form, action) {
                            self.msgbox("导入成功！");

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure: function (form, action) {
                            if (action.result == undefined) {
                                self.Error("文件导入失败，文件有误或超过限制大小！");
                            } else {
                                self.Error(action.result.obj);
                            }

                        }
                    });
                } else {
                    self.Error("请选择要上传Excel文件！")
                }

                return false
            }
        },
        /**
         * 导入会议的关闭按钮
         */
        "panel[xtype=meetinginfo.meetingimportform] button[ref=formClose]": {
            click: function (btn) {
                var win = btn.up('window');
                //关闭窗体
                win.close();
                return false;
            }
        }
    
    }   
});
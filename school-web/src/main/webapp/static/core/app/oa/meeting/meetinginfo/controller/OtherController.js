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
        "panel[xtype=meetinginfo.imagesdeailform] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;

                var dicForm = btn.up('panel[xtype=meetinginfo.imagesdeailform]');
                var objForm = dicForm.getForm();
                var params = self.getFormValue(objForm);
                if (objForm.isValid()) {
                    
                    objForm.submit({
                        url: comm.get('baseUrl') + "/BaseAttachment/doUploadImage",
                        waitMsg: '正在上传文件...',
                        timeout : 300,
                        success: function(form, action) {
                            self.msgbox("上传图片成功！");

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure:function(form, action){
                            if(action.result==undefined){
                                self.Error("上传失败，文件超过限制大小！");
                            }else{
                                self.Error("请求失败，请重试或联系管理员！");
                            }   
                          
                        }
                    });
                } else {
                    self.Error("请按要求填入各项信息！")
                }
                
                return false 
            }
        },


        "panel[xtype=meetinginfo.imagesdeailform] button[ref=formClose]": {
            click: function(btn) {
                var win = btn.up('window');
                //var grid = win.grid;
                //刷新列表
                //grid.getStore().load();
                //关闭窗体
                win.close();
                return false;
            }
        },
        
        /**
         * 详细页面提交按钮事件
         */
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
                var checkruleName = params.checkruleName;
                if (params.needChecking==1&&Ext.isEmpty(checkruleName)){
                    self.Error("请选择考勤规则");
                    return false;
                }
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
        },

        /**
         * 参会人员选择后确定按钮事件
         */
        "mtfuncwindow[funcPanel=pubselect.selectuserlayout] button[ref=ssOkBtn]": {
            beforeclick: function (btn) {
                var self = this ;
                var win = btn.up("mtfuncwindow");
                var funcPanel = win.funcPanel;
                var winFunData = win.funData;
                var baseGrid = winFunData.userGird;
                var basePanel = win.down("panel[xtype=" + funcPanel + "]");
                var selectGrid = basePanel.down("grid[xtype=pubselect.isselectusergrid]");
                var records = selectGrid.getStore().data;
                var recordLen = records.length;
                if (recordLen==0){
                    self.Warning("没有选择参会人员，请重新选择！");
                    return false;
                }
                var userIds = new Array();
                var userNames = new Array();
                for (var i = 0; i < recordLen; i++) {
                    var r = records.getAt(i);
                    userIds.push(r.get("uuid"));
                    userNames.push(r.get("xm"));
                }
                var title = "确定添加这些参会人员吗？";
                Ext.Msg.confirm('提示', title, function (btnOper, text) {
                    if (btnOper == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: winFunData.action + "/doAddMeetingUser",
                            params: {
                                ids: winFunData.uuid,
                                userId: userIds.join(","),
                                userName:userNames.join(",")
                            }
                        });
                        if (resObj.success) {
                            var store = baseGrid.getStore();
                            store.load();
                            self.msgbox(resObj.obj);
                            win.close();
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });
                return false;
            }
        },
        /**
         * 参会人员选择取消按钮事件
         */
        "mtfuncwindow[funcPanel=pubselect.selectuserlayout] button[ref=ssCancelBtn]": {
            beforeclick: function (btn) {
                var self = this ;
                var win = btn.up("mtfuncwindow");
                if (win)
                    win.close();
                return false;
            }
        }
    }
});
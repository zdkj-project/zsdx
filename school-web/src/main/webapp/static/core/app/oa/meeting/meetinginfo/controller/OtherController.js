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
            beforeclick: function(btn) {
                console.log(btn);
            }
        },

        "baseformtab button[ref=formClose]": {
            beforeclick: function(btn) {
                console.log(btn);
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
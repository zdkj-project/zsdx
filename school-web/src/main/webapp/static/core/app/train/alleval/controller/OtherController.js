/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
 但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
 */
Ext.define("core.train.alleval.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.alleval.otherController',
    mixins: {

        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function () {
    },
    /** 该视图内的组件事件注册 */
    control: {
        /**
         * 增加或修改界面的提交按钮
         */
        "baseformtab button[ref=formSave]": {
            beforeclick: function (btn) {
                this.doSave(btn, "addSave");
                return false;
            }
        },

        /**
         * 导入的保存按钮
         */
        "panel[xtype=alleval.allevalimportform] button[ref=formSave]": {
            beforeclick: function (btn) {
                var self = this;
                var dicForm = btn.up('panel[xtype=alleval.allevalimportform]');
                var objForm = dicForm.getForm();
                if (objForm.isValid()) {
                    objForm.submit({
                        url: comm.get('baseUrl') + "/Trainalleval/importData",
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
         * 导入的关闭按钮
         */
        "panel[xtype=alleval.allevalimportform] button[ref=formClose]": {
            click: function (btn) {
                var win = btn.up('window');
                //关闭窗体
                win.close();
                return false;
            }
        },
    },

    doSave: function (btn, cmd) {
        var self = this;
        //得到组件
        var basetab = btn.up('baseformtab');
        var tabPanel = btn.up("tabpanel[xtype=app-main]");
        var tabItemId = basetab.tabItemId;
        var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页

        //得到配置信息
        var funCode = basetab.funCode;      //mainLayout的funcode
        var detCode = basetab.detCode;      //detailLayout的funcode

        var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + detCode + "]");
        var formObj = objForm.getForm();
        var funData = basePanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);

        //判断当前是保存还是修改操作
        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
        if (formObj.isValid()) {

            formObj.submit({
                url: funData.action + "/" + act,
                //params: params,       //表单的参数会自动上传
                submitEmptyText: false,     //不提交表单为空值的数据
                waitMsg: '正在提交，请等待...',
                success: function (fp, action) {
                    formObj.reset();
                    self.msgbox("保存成功!");

                    var grid = basetab.funData.grid; //此tab是否保存有grid参数
                    if (!Ext.isEmpty(grid)) {
                        var store = grid.getStore();
                             store.loadPage(1); //刷新父窗体的grid
                        tabPanel.remove(tabItem);
                    }

                },
                failure: function (form, action) {
                    if (!Ext.isEmpty(action.result.obj))
                        self.Info(action.result.obj);
                }
            });


        } else {

            var errors = ["前台验证失败，错误信息："];
            formObj.getFields().each(function (f) {
                if (!f.isValid()) {
                    errors.push("<font color=red>" + f.fieldLabel + "</font>:" + f.getErrors().join(","));
                }
            });
            self.msgbox(errors.join("<br/>"));
        }
    }
});
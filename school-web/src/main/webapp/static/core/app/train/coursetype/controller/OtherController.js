/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.train.coursetype.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursetype.otherController',
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
        "baseformtab button[ref=formSave]": {
            beforeclick: function (btn) {
                var self = this;
                var basetab = btn.up('baseformtab');
                var tabPanel = btn.up("tabpanel[xtype=app-main]");
                var tabItemId = basetab.tabItemId;
                var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页
                var mainGrid = basetab.funData.grid;
                var operType = basetab.operType;
                var cmd = basetab.cmd;
                var returnFalse = true;
                switch (operType){
                    case "add":
                    case "edit":
                        if (cmd==="order"){
                            var baseGrid = basetab.down("basegrid");
                            var recordData = baseGrid.getStore();
                            var iCount = recordData.getCount();
                            var ids = new Array();
                            var order = new Array();
                            var record;
                            for(var i=0;i<iCount;i++){
                                record = recordData.getAt(i);
                                ids.push(record.get("uuid"));
                                order.push(record.get("orderIndex"));
                            }

                            var title = "调整顺序会造成分类及课程的编码调整,确定吗？";
                            Ext.Msg.confirm('重新排序提醒', title, function (btn, text) {
                                if (btn == 'yes') {
                                    //发送ajax请求
                                    var resObj = self.ajax({
                                        url: "/TrainCoursecategory/changeorder",
                                        params: {
                                            ids: ids.join(","),
                                            order: order.join(",")
                                        }
                                    });
                                    if (resObj.success) {
                                        mainGrid.getStore().load();
                                        self.msgbox(resObj.obj);
                                        tabPanel.remove(tabItem);
                                    } else {
                                        self.Error(resObj.obj);
                                    }
                                }
                            });
                            returnFalse = false;
                        }
                        break;
                }
/*                var detPanel = basetab.down("basepanel[funCode=" + detCode + "]");
                var funData = detPanel.funData;
                var objForm = detPanel.down("baseform[funCode=" + detCode + "]");
                var formObj = objForm.getForm();*/

/*                var pkName = funData.pkName;
                 var pkField = formObj.findField(pkName);
                 var params = self.getFormValue(formObj);
                 var courseMode = params.courseMode == true ? 2 : 1;
                 params = Ext.apply(params, {
                 courseDesc: courseDesc,
                 courseMode: courseMode
                 }
                 );*/


                //判断当前是保存还是修改操作
/*                var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
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
                }*/
                if(!returnFalse)
                    return false;
            }
        }
    }   
});
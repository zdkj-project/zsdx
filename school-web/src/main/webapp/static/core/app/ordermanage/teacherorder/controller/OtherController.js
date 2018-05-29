/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.ordermanage.teacherorder.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.teacherorder.otherController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function() {},
    /** 该视图内的组件事件注册 */
    control: {

        'baseformwin button[ref=formSave]':{
            beforeclick:function(btn){
                var self=this;
                var win=btn.up("baseformwin")
                var objForm = win.down("baseform[xtype=teacherorder.mainform]");

                var formObj = objForm.getForm();

                var params = self.getFormValue(formObj);

                //判断当前是保存还是修改操作
                if (formObj.isValid()) {

                    Ext.Msg.confirm('温馨提示', "您确定要保存备注信息吗？", function (btn2, text) {
                        if (btn2 == "yes") {

                            var loading = new Ext.LoadMask(objForm, {
                                msg: '正在提交，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            self.asyncAjax({
                                url: comm.get("baseUrl") + "/TrainTeacherOrder/doUpdate",
                                params: params,
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        loading.hide();
                                        win.close();
                                        win.record.set("remark",params.remark);
                                        win.record.commit();
                                        self.Info("保存成功!");
                                    } else {
                                        loading.hide();
                                        self.Error(data.obj);
                                    }
                                },failure: function(response) {
                                    loading.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                                }
                            });
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

       }
    }
    
});

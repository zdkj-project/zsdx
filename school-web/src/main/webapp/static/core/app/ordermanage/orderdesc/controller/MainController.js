Ext.define("core.ordermanage.orderdesc.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.orderdesc.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function() {
        /*control事件声明代码，可以写在这里
        this.control({

        });
        */
    },
    control: {
        'baseform[xtype=orderdesc.mainform]':{
            afterrender: function (form, eOpts) {
                var self=this;

                //var objForm = form.up("baseform[xtype=orderdesc.mainform]");

                var loading = new Ext.LoadMask(form, {
                    msg: '正在读取数据...',
                    removeMask: true// 完成后移除
                });
                loading.show();

                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainTeacherOrderDesc/getOrderDesc",
                    //回调代码必须写在里面
                    success: function (response) {
                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if (data.success) {
                        
                            form.down("field[name=uuid]").setValue(data.obj.uuid);
                            form.down("htmleditor[name=orderDesc]").setValue(data.obj.orderDesc);
                          
                        }
                        loading.hide();
                    },failure: function(response) {
                        loading.hide();
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                    }
                });

           }
        },

        'baseform[xtype=orderdesc.mainform] button[ref=submitBtn]':{
            beforeclick:function(btn){
                var self=this;

                var objForm = btn.up("baseform[xtype=orderdesc.mainform]");

                var formObj = objForm.getForm();

              

                var params = self.getFormValue(formObj);
                var orderDesc=objForm.down("htmleditor[name=orderDesc]").getValue();
                params.orderDesc=orderDesc;
              

                //判断当前是保存还是修改操作
                var act = Ext.isEmpty(params.uuid) ? "doAdd" : "doUpdate";
                if (formObj.isValid()) {

                    Ext.Msg.confirm('温馨提示', "您确定要保存就餐说明信息吗？", function (btn2, text) {
                        if (btn2 == "yes") {

                            var loading = new Ext.LoadMask(objForm, {
                                msg: '正在提交，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            self.asyncAjax({
                                url: comm.get("baseUrl") + "/TrainTeacherOrderDesc/" + act,
                                params: params,
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        loading.hide();
                                        self.Info("保存成功!");
                                        formObj.findField("uuid").setValue(data.obj.uuid);
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

       },
       'baseform[xtype=orderdesc.mainform] button[ref=closeBtn]':{
            beforeclick:function(btn){
                //得到组件
                var basetab = btn.up('container[itemId]');
                var tabPanel = btn.up("tabpanel[xtype=app-main]");

                var tabItemId = basetab.itemId;
                var tabItem = tabPanel.getComponent(tabItemId);

                // var grid = win.funData.grid; //窗体是否有grid参数
                // if (!Ext.isEmpty(grid)) {
                //     grid.getStore().load(); //刷新父窗体的grid
                // }
                //关闭tab
                tabPanel.remove(tabItem);

                return false;
            }

       },
       
    }

});

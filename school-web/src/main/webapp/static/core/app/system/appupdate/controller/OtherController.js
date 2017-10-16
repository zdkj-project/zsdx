/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.system.appupdate.controller.OtherController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.appupdate.otherController',
    
    /*把不需要使用的组件，移除掉*/
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil"
    },
   
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 other controler");           
    },

    /*该视图内的组件事件注册*/
    control:{
        
        "panel[xtype=appupdate.deailform] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;
                var dicForm = btn.up('panel[xtype=appupdate.deailform]');
                var objForm = dicForm.getForm();
                var params = self.getFormValue(objForm);
                if (objForm.isValid()) {
                    objForm.submit({
                        url: comm.get('baseUrl') + "/SysAppinfo/doUploadApp",
                        waitMsg: '正在上传文件...',
                        timeout : 300000,
                        success: function(form, action) {
                            self.Info("上传文件成功！");

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure:function(form, action){
                            if(action.result==undefined){
                                self.Error("上传失败，请检查文件！");
                            }else{
                                self.Error(action.result.obj);
                            }
                          
                        }
                    });
                } else {
                    self.Error("请按要求填入各项信息！")
                }
                
                return false 
            }
        },


        "panel[xtype=appupdate.deailform] button[ref=formClose]": {
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

    }

});
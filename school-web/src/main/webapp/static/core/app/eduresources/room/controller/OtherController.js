
Ext.define("core.eduresources.room.controller.OtherController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.room.otherController',

    /*把不需要使用的组件，移除掉*/
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
   
    init: function() {
        var self = this;

        this.control({
            /*
            "button": {
                click : function(btn) {
                    console.log(this);
                   
                }
            }*/
        	 //批量添加保存
            "panel[xtype=room.BatchRoomForm] button[ref=formSave]": {
                click: function(btn) {
                    var baseGrid = btn.up('window').baseGrid;
                    var dicForm = btn.up("panel[xtype=room.BatchRoomForm]").getForm();
                    var params = self.getFormValue(dicForm);
                    var resObj = null;
                    if (dicForm.isValid()) {
                        resObj = self.ajax({
                            url: comm.get('baseUrl') + "/BuildRoominfo/batChdoAdd",
                            params: params
                        });
                        if (resObj.success) {
                            self.msgbox('成功');
                            baseGrid.getStore().load();
                        } else {
                            self.msgbox(resObj.obj);
                        }
                    }
                    return false;
                }
            },
            //批量添加关闭
            "panel[xtype=room.BatchRoomForm] button[ref=formClose]": {
            	click: function(btn) {
                    btn.up('window').close();
                }
            },
            
            
            "mtfuncwindow button[ref=ssOkBtn]":{
                beforeclick:function(btn){
                    console.log("重写mtfuncwindow的确定按钮");
                }
            }
    	

        });
    }



});
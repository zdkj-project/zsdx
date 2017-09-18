Ext.define("core.system.appupdate.controller.MainController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.appupdate.mainController',

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
            "basepanel panel[xtype=appupdate.appgrid] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    var basepanel=btn.up("basepanel[xtype=appupdate.mainlayout]");

                    var grid=btn.up("panel[xtype=appupdate.appgrid]");
                    var recordes=grid.getSelectionModel( ).getSelection();
                    if(recordes.length==1){
                       
                        Ext.MessageBox.confirm('启用此APP', '您确定要对选中的APP进行启用吗？', function(btn, text) {
                                     
                            if (btn == 'yes') {
                                //发送ajax请求
                                var resObj = self.ajax({
                                    url: comm.get('baseUrl') + "/SysAppinfo/doUpdateState",
                                    params: {
                                        id: recordes[0].data.uuid,
                                        appType:recordes[0].data.appType,
                                        appIsuse:1,
                                        appUrl:recordes[0].data.appUrl                                  
                                    }
                                });
                                if (resObj.success) {
                                    //刷新数据集
                                    grid.getStore().load();                                
                                    self.msgbox(resObj.obj);                                   
                                } else {
                                    self.Error(resObj.obj);
                                }
                            }
                        });  


                    }else{
                        self.msgbox("请选择一条数据！"); 
                    }

                    return false;
                }
            },

            "basepanel panel[xtype=appupdate.appgrid] button[ref=gridRemove]": {
                beforeclick: function(btn) {
                    var basepanel=btn.up("basepanel[xtype=appupdate.mainlayout]");

                    var grid=btn.up("panel[xtype=appupdate.appgrid]");
                    var recordes=grid.getSelectionModel( ).getSelection();
                    if(recordes.length==1){                    
                        Ext.MessageBox.confirm('取消启用此APP', '您确定要对选中的APP取消启用吗？', function(btn, text) {
                                     
                            if (btn == 'yes') {
                                //发送ajax请求
                                var resObj = self.ajax({
                                    url: comm.get('baseUrl') + "/SysAppinfo/doUpdateState",
                                    params: {
                                        id: recordes[0].data.uuid,
                                        appType:recordes[0].data.appType,
                                        appIsuse:0,
                                        appUrl:recordes[0].data.appUrl                                     
                                    }
                                });
                                if (resObj.success) {
                                    grid.getStore().load();     

                                    self.msgbox(resObj.obj);                                    
                                } else {
                                    self.Error(resObj.obj);
                                }
                            }
                        });  

                    }else{
                        self.msgbox("请选择一条数据！"); 
                    }

                    return false;
                }
            },

            
            "basepanel panel[xtype=appupdate.appgrid] button[ref=gridUpload]": {
                beforeclick: function(btn) {
                    //判断是否选择的活动ID
                    
                    var basepanel=btn.up("basepanel[xtype=appupdate.mainlayout]");
                           
                    var appGrid = basepanel.down("panel[xtype=appupdate.appgrid]");
                    var win = Ext.create('Ext.Window', {
                        title: "APP上传",
                        controller:'appupdate.otherController', //指定视图控制器
                        iconCls: 'fa fa-upload',
                        width: 450,
                        resizable: false,
                        constrain: true,
                        autoHeight: true,
                        modal: true,
                        closeAction: 'close',
                        plain: true,
                        grid: appGrid,
                        items: [{
                            xtype: "appupdate.deailform"
                        }],
                        listeners: {
                            beforerender:function(win){
                                //console.log(obj)
                            }
                        }
                    });
                
                    win.show();

                    return false;
                }
            },


            
        });
    }
});
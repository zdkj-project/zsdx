Ext.define("core.oa.meeting.checkrule.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.checkrule.mainController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        /*
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        */
    },
    init: function() {
    },
    control: {
        "basegrid button[ref=gridAdd_Tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridDetail_tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridEdit_tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid[xtype=checkrule.maingrid] button[ref=gridSetUse]": {
            beforeclick: function(btn) {
                var self = this;

                //得到组件
                var baseGrid = btn.up("basegrid");

                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择数据!");
                    return;
                }

                var uuid = records[0].get("uuid");
                var startUsing=records[0].get("startUsing");
                
                Ext.MessageBox.confirm('温馨提示', '你确定要设置启用状态吗？', function(btn, text) {
                    if (btn == 'yes') {
                        Ext.Msg.wait('正在执行中,请稍后...', '温馨提示');
                        self.asyncAjax({
                            url: comm.get("baseUrl")  + "/OaMeetingcheckrule/doUse",
                            params: {
                                uuid:uuid,
                                startUsing:startUsing==1?0:1                   
                            },
                            timeout:1000*60*60, //1个小时
                            //回调代码必须写在里面
                            success: function(response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                                           
                                if(data.success){ 
                                    baseGrid.getStore().load();
                                    Ext.Msg.hide();
                                    self.Info(data.obj);   
                                }else{
                                    Ext.Msg.hide();
                                    self.Error(data.obj);
                                }
                            },
                            failure: function(response) {
                                Ext.Msg.hide();
                                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                            }
                        });  
                    }
                });

        
                return false;
            }
        },

        "basegrid  actioncolumn": {
            editClick: function(data) {
                console.log(data);

            },
            detailClick: function(data) {
                console.log(data);

            },
            deleteClick: function(data) {
                console.log(data);

            },
        }
    }   
});
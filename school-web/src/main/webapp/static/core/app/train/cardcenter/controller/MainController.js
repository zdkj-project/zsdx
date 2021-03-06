Ext.define("core.train.cardcenter.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.cardcenter.mainController',
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
    	"basegrid[xtype=cardcenter.maingrid]": {
            afterrender: function (grid, eOpts) {
                // var btngridExportExcel = grid.down("button[ref=gridExportExcel]");
                // var roleKey = comm.get("roleKey");
                // if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1) {
                // 	btngridExportExcel.setHidden(true);
                // }
            }
           },
           
           /**
            * 导出
            */
           "basegrid[xtype=cardcenter.maingrid] button[ref=gridExport]": {
               beforeclick: function (btn) {
                   var self = this;
                   //得到组件
                   var baseGrid = btn.up("basegrid");
                   var mainlayout=baseGrid.up("panel[xtype=cardcenter.mainlayout]");
                   var classGrid=mainlayout.down("panel[xtype=cardcenter.classgrid]");
                   var classRecords = classGrid.getSelectionModel().getSelection();
                  if(classRecords.length!=1){
                       self.Warning("请选择一个班级!");
                       return;
                   }

                   var classId = classRecords[0].data.uuid;
                   var funCode = baseGrid.funCode;
                   var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                   //得到配置信息
                   var funData = basePanel.funData;
                   var pkName = funData.pkName;
                   
                   if(classId==null){
                	     self.Warning("请选择一个班级!");
                       return;
                   }
                   var title="学员卡务列表";
                   Ext.Msg.confirm('提示', title, function (btn, text) {
                       if (btn == "yes") {
                           Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                           var component = Ext.create('Ext.Component', {
                               title: 'HelloWorld',
                               width: 0,
                               height: 0,
                               hidden: true,
                               html: '<iframe src="' + comm.get('baseUrl') + '/TrainClasstrainee/exportCardExcel?classId=' + classId + '"></iframe>',
                               renderTo: Ext.getBody()
                           });

                           var time = function () {
                               self.syncAjax({
                                   url: comm.get('baseUrl') + '/TrainClasstrainee/checkExportCardEnd',
                                   timeout: 1000 * 60 * 30,        //半个小时
                                   //回调代码必须写在里面
                                   success: function (response) {
                                       data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                       if (data.success) {
                                           Ext.Msg.hide();
                                           self.Info(data.obj);
                                           component.destroy();
                                       } else {
                                           setTimeout(function () {
                                               time()
                                           }, 1000);
                                       }
                                   },
                                   failure: function (response) {
                                       Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                       component.destroy();
                                       clearInterval(interval)
                                   }
                               });
                           }
                           setTimeout(function () {
                               time()
                           }, 1000);    //延迟1秒执行
                       }
                   });
                   return false;
               }
           },
           
           
           
           
           
           
        /**
         * 导出班级学员发卡绑定模版
         */
        "basegrid[xtype=cardcenter.maingrid] button[ref=gridExportExcel]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件               
                var baseGrid = btn.up("basegrid");
                var basePanel = baseGrid.up("basepanel");
                var calssGrid=basePanel.down("basegrid[xtype=cardcenter.classgrid]");

                var records = calssGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择一个班级!");
                    return;
                }

                var rec=records[0];

                var title = "确定要导出【"+rec.get("className")+"】班级学员绑卡模版吗？";
        

                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClasstrainee/exportExcel?classId=' +rec.get("uuid") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClasstrainee/checkExportEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.Info(data.obj);
                                        component.destroy();                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },

       //重写学员查询按钮
        "basepanel basegrid[xtype=cardcenter.maingrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                if (!baseGrid)
                    return false;

                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;

                var filter=[];            
                var store = baseGrid.getStore();
                var proxy = store.getProxy();
                filter.push(JSON.parse(proxy.extraParams.filter)[0]);
               
                var girdSearchText = toolBar.query("field[funCode=girdFastSearchText]")[0];             
                filter.push({"type": "string", "value": girdSearchText.getValue(), "field": girdSearchText.getName(), "comparison": ""});                    

                proxy.extraParams.filter = JSON.stringify(filter);
                store.loadPage(1); 

                return false;        
            }
        },
       //学员更新发卡信息
       "basepanel basegrid[xtype=cardcenter.maingrid] button[ref=gridRefreshInfo]": {
            click: function (btn) {
                var self=this;  
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                var basePanel = baseGrid.up("basepanel");
                var calssGrid=basePanel.down("basegrid[xtype=cardcenter.classgrid]");

                var records = calssGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择一个班级!");
                    return;
                }
                
                Ext.Msg.wait('正在更新发卡数据,请稍后...', '温馨提示');
                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClasstrainee/doSyncClassCardInfoFromUp",
                    params: {
                        classId:records[0].get("uuid")                             
                    },
                    timeout:1000*60*60, //1个小时
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                                   
                        if(data.success){ 
                            baseGrid.getStore().loadPage(1);
                            Ext.Msg.hide();
                            self.Info(data.msg);   
                        }else{
                            Ext.Msg.hide();
                            self.Error(data.msg);
                        }
                    },
                    failure: function(response) {
                        Ext.Msg.hide();
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                    }
                });  
               
            }
        },

       "basegrid[xtype=cardcenter.classgrid]": {
            beforeitemclick: function (grid, record, item, index, e, eOpts) {
                var self = this;
                var mainLayout = grid.up("panel[xtype=cardcenter.mainlayout]");
                var filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + record.get("uuid") + "\",\"field\":\"classId\"}]";
                
               
                var maingrid = mainLayout.down("panel[xtype=cardcenter.maingrid]");
                var store = maingrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    filter: filter
                };
                store.loadPage(1); 
            
                return false;
            }
        },
        
        /**
         * 学员发卡绑定
         */
        "basegrid[xtype=cardcenter.maingrid] button[ref=gridBindCard]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件               
                var baseGrid = btn.up("basegrid");
                var basePanel = baseGrid.up("basepanel");
                var calssGrid=basePanel.down("basegrid[xtype=cardcenter.classgrid]");

                var calssRecords = calssGrid.getSelectionModel().getSelection();
                var traineeRecords = baseGrid.getSelectionModel().getSelection();
                
                if(calssRecords.length==0&&traineeRecords.length==0){
                	 self.Warning("请选择一个班级或者一些学员");
                     return;
                }
                
                if(calssRecords.length!=0&&traineeRecords.length!=0){
                	Ext.Msg.confirm('提示', '是否绑定这些学员?', function (btn, text) {
                        if (btn == 'yes') {

                            var loading = new Ext.LoadMask(basePanel, {
                                msg: '正在处理，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            var ids = new Array();
                            var classId = "";
                            Ext.each(traineeRecords, function (rec) {
                                var uuid = rec.data.uuid;
                                ids.push(uuid);
                            });

                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/cardBind",
                                params: {
                                    ids: ids.join(","),
                                    classId: classId
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        baseGrid.getStore().load();
                                        self.Info(data.obj);                                    
                                    } else {
                                        self.Error(data.obj);                                        
                                    }
                                    loading.hide();
                                },
                                failure: function(response) {                   
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });
                     }
                })
              }
                
                if(calssRecords.length!=0&&traineeRecords.length==0){
                	Ext.Msg.confirm('提示', '是否绑定班级学员?', function (btn, text) {
                        if (btn == 'yes') {

                            var loading = new Ext.LoadMask(basePanel, {
                                msg: '正在处理，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            var ids = "";
                            var classId = calssRecords[0].data.uuid;

                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/cardBind",
                                params: {
                                    ids: ids,
                                    classId: classId
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        baseGrid.getStore().load();
                                        self.Info(data.obj);                                    
                                    } else {
                                        self.Error(data.obj);                                        
                                    }
                                    loading.hide();
                                },
                                failure: function(response) {                   
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });                                      
                     }
                })
              }
                
            }   
        },
        
        /**
         * 学员发卡绑定
         */
        "basegrid[xtype=cardcenter.maingrid] button[ref=gridUnBindCard]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件               
                var baseGrid = btn.up("basegrid");
                var basePanel = baseGrid.up("basepanel");
                var calssGrid=basePanel.down("basegrid[xtype=cardcenter.classgrid]");

                var calssRecords = calssGrid.getSelectionModel().getSelection();
                var traineeRecords = baseGrid.getSelectionModel().getSelection();
                
                if(calssRecords.length==0&&traineeRecords.length==0){
                	 self.Warning("请选择一个班级或者一些学员");
                     return;
                }
                
                if(calssRecords.length!=0&&traineeRecords.length!=0){
                	Ext.Msg.confirm('提示', '是否解除绑定这些学员?', function (btn, text) {
                        if (btn == 'yes') {

                            var loading = new Ext.LoadMask(basePanel, {
                                msg: '正在处理，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            var ids = new Array();
                            var classId = "";
                            Ext.each(traineeRecords, function (rec) {
                                var uuid = rec.data.uuid;
                                ids.push(uuid);
                            });

                           
                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/cardUnBind",
                                params: {
                                    ids: ids.join(","),
                                    classId: classId
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        baseGrid.getStore().load();
                                        self.Info(data.obj);                                    
                                    } else {
                                        self.Error(data.obj);                                        
                                    }
                                    loading.hide();
                                },
                                failure: function(response) {                   
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });
                
                     }
                })
              }
                
              if(calssRecords.length!=0&&traineeRecords.length==0){
                	Ext.Msg.confirm('提示', '是否解除绑定班级学员?', function (btn, text) {
                        if (btn == 'yes') {

                            var loading = new Ext.LoadMask(basePanel, {
                                msg: '正在处理，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            var ids = "";
                            var classId = calssRecords[0].data.uuid;
                	         

                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/cardUnBind",
                                params: {
                                    ids: ids,
                                    classId: classId
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        baseGrid.getStore().load();
                                        self.Info(data.obj);                                    
                                    } else {
                                        self.Error(data.obj);                                        
                                    }
                                    loading.hide();
                                },
                                failure: function(response) {                   
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });
                          
                     }
                })
              }
                
            }   
        },
        
        /**
         * 学员列表操作列事件
         */
        "basegrid[xtype=cardcenter.maingrid] actioncolumn": {
            //操作列绑定
        	cardBindClick_Tab: function (data) {
        		var self = this;
        		var baseGrid=data.view;
        		var ids = data.record.data.uuid;
                	Ext.Msg.confirm('提示', '是否绑定此学员?', function (btn, text) {
                        if (btn == 'yes') {

                            var loading = new Ext.LoadMask(baseGrid, {
                                msg: '正在处理，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            var classId = "";
                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/cardBind",
                                params: {
                                    ids: ids,
                                    classId: classId
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        baseGrid.getStore().load();
                                        self.Info(data.obj);                                    
                                    } else {
                                        self.Error(data.obj);                                        
                                    }
                                    loading.hide();
                                },
                                failure: function(response) {                   
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });
                        
                     }
                })
            },
            //操作列解绑
            cardUnBindClick_Tab: function (data) {
            	var self = this;
            	var baseGrid=data.view;
        		  var ids =data.record.data.uuid;
                	Ext.Msg.confirm('提示', '是否解除绑定此学员?<br/>(只能针对已发卡、挂失卡学员操作)', function (btn, text) {
                        if (btn == 'yes') {

                            var loading = new Ext.LoadMask(baseGrid, {
                                msg: '正在处理，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            var classId = "";
                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/cardUnBind",
                                params: {
                                    ids: ids,
                                    classId: classId
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        baseGrid.getStore().load();
                                        self.Info(data.obj);                                    
                                    } else {
                                        self.Error(data.obj);                                        
                                    }
                                    loading.hide();
                                },
                                failure: function(response) {                   
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });
                     }
                })
            },
            //操作列挂失
            cardLoseClick: function (data) {
            	var self = this;
            	var baseGrid=data.view;
        		  var ids = data.record.data.uuid;
                	Ext.Msg.confirm('提示', '是否挂失此学员?', function (btn, text) {
                        if (btn == 'yes') {

                            var loading = new Ext.LoadMask(baseGrid, {
                                msg: '正在处理，请稍等...',
                                removeMask: true// 完成后移除
                            });
                            loading.show();

                            var classId = "";
                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/cardLose",
                                params: {
                                    ids: ids,
                                    classId: classId
                                },
                                //回调代码必须写在里面
                                success: function (response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if (data.success) {
                                        baseGrid.getStore().load();
                                        self.Info(data.obj);                                    
                                    } else {
                                        self.Error(data.obj);                                        
                                    }
                                    loading.hide();
                                },
                                failure: function(response) {                   
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });

                     }
                })
            },
        }
        
    }
});

/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.reportcenter.cashreport.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.cashreport.detailController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");
    },
    /** 该视图内的组件事件注册 */
    control: {   
        //快速搜索文本框回车事件
        "basepanel basegrid[xtype=cashreport.cashdetailgrid] field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {
                    //得到组件                 
                    var baseGrid = field.up("basegrid");
                    if (!baseGrid)
                        return false;

                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;
                    
                    var newFilter=[];
                    var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                    for (var i in girdSearchTexts) {
                        var name = girdSearchTexts[i].getName();
                        var value = girdSearchTexts[i].getValue();
                        newFilter.push({"type":"string","comparison":"","value":value,"field":name});
                    }
                  
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();

                    var filter=JSON.parse(proxy.extraParams.filter);               
                    for(var i=0;i<filter.length;i++){
                        if(filter[i].field=="expenseserialId"){
                            newFilter.push({"type":"string","comparison":"=","value":filter[i].value,"field":"expenseserialId"});
                            break;
                        }
                    }

                    proxy.extraParams.filter =JSON.stringify(newFilter);
                    store.loadPage(1);
                }
                return false;
            }
        },  
        //快速搜索按按钮
        "basepanel basegrid[xtype=cashreport.cashdetailgrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                if (!baseGrid)
                    return false;

                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;
                
                var newFilter=[];
                var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                for (var i in girdSearchTexts) {
                    var name = girdSearchTexts[i].getName();
                    var value = girdSearchTexts[i].getValue();
                    newFilter.push({"type":"string","comparison":"","value":value,"field":name});
                }
              
                var store = baseGrid.getStore();
                var proxy = store.getProxy();

                var filter=JSON.parse(proxy.extraParams.filter);               
                for(var i=0;i<filter.length;i++){
                    if(filter[i].field=="expenseserialId"){
                        newFilter.push({"type":"string","comparison":"=","value":filter[i].value,"field":"expenseserialId"});
                        break;
                    }
                }

                proxy.extraParams.filter =JSON.stringify(newFilter);
                store.loadPage(1);
               
                return false;
            }
        },

        /**
         * 导出班级就餐详情数据
         */
        "basepanel basegrid[xtype=cashreport.cashdetailgrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid=btn.up("basegrid");
                var baseFormTab=baseGrid.up("baseformtab");

                
                var expenseserialId=baseFormTab.insertObj.EXPENSESERIAL_ID;
                var consumeSerial=baseFormTab.insertObj.CONSUME_SERIAL;
                var params="?expenseserialId="+expenseserialId+"&consumeSerial="+consumeSerial;
                var title = "确定要导出此流水号的详细信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainReport/exportCashDetailExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainReport/checkCashDetailEnd',
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
        
    }
});

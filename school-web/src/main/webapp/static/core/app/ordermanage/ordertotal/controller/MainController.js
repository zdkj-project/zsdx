Ext.define("core.ordermanage.ordertotal.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.ordertotal.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil',    
        queryUtil:"core.util.QueryUtil"
    },
    init: function() {
        /*control事件声明代码，可以写在这里
        this.control({

        });
        */
    },
    control: { 
        /**
         * 导出订餐汇总数据
         */
        "basepanel basegrid button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid=btn.up("basegrid");
                //获取表格参数
                var extraParams = baseGrid.getStore().getProxy().extraParams;
                var params="?t=1";
                
                for(var i in extraParams){
                    params+="&"+i+"="+extraParams[i];
                }
               
                var title = "确定要导出订餐汇总信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainTeacherOrder/exportOrderTotalExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainTeacherOrder/checkExportOrderTotalEnd',
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

        

        "basegrid[xtype=ordertotal.maingrid]  actioncolumn": {
            detailClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;

                var self=this;
                //得到组件             

                var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

                //得到配置信息
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                var detCode = basePanel.detCode;
                var funData = basePanel.funData;

                //获取主键值              
                var pkValue = record.get("dinnerDate");
                //转换一下时间
                var reg = new RegExp("^[0-9]*$");    
                if(reg.test(pkValue)){
                    pkValue=Ext.Date.format(new Date(pkValue), 'Y-m-d');
                }

                //设置tab页的itemId
                var tabItemId = funCode + "_gridDetail"+pkValue;     //命名规则：funCode+'_ref名称',确保不重复
                //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                var tabItem = tabPanel.getComponent(tabItemId);
            

                //判断是否已经存在tab了
                if (!tabItem) {

                    var detLayout = basePanel.detLayout;
                    var defaultObj = funData.defaultObj;

                    //关键：window的视图控制器
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';
              

                    //处理特殊默认值
                    var insertObj = record.data;
                    insertObj.dinnerDate=pkValue;
                    
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });


                    var tabTitle = pkValue+"-订餐人员明细";

                    tabItem = Ext.create({
                        xtype: 'container',
                        title: tabTitle,
                        //iconCls: 'x-fa fa-clipboard',
                        scrollable: true,
                        itemId: tabItemId,
                        itemPKV: pkValue,    //保存主键值
                        layout: 'fit',
                    });
                    tabPanel.add(tabItem);

                    //延迟放入到tab中
                    setTimeout(function () {
                        //创建组件
                        var item = Ext.widget("baseformtab", {
                            operType: 'detail',
                            controller: otherController,         //指定重写事件的控制器
                            funCode: funCode,                    //指定mainLayout的funcode
                            detCode: detCode,                    //指定detailLayout的funcode
                            tabItemId: tabItemId,                //指定tab页的itemId
                            insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                            funData: popFunData,                //保存funData数据，提供给提交事件中使用
                            items: [{
                                xtype: detLayout                            
                            }]
                        });
                        tabItem.add(item);

                        var orderDetailGrid = tabItem.down("basegrid[xtype=ordertotal.orderdetailgrid]");
                        orderDetailGrid.getStore().getProxy().extraParams.dinnerDate = pkValue;
                        orderDetailGrid.getStore().load();

                    }, 30);

                }  else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
                    self.Warning("您当前已经打开了一个编辑窗口了！");
                    return;
                }

                tabPanel.setActiveTab(tabItem);

                return false;
            },
        }
    },
       

});

Ext.define("core.train.dinnertotal.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.dinnertotal.mainController',
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

        //手动加载数据，并给编写store的加载事件
        "basepanel basegrid[xtype=dinnertotal.maingrid]":{
            afterrender :function( me , eOpts ) {
                var self=this;
                var store=me.getStore();
                    
                this.loadDinnerTotalDatas(store,me);
                    
                // store.loadPage(1,{
                //     scope: this,
                //     callback: function(records, operation, success) {
                        
                //         self.syncAjax({
                //             url:comm.get('baseUrl') + "/TrainClassrealdinner/getDinnerTotalDatas",
                //             timeout: 1000*60*30,        //半个小时         
                //             //回调代码必须写在里面
                //             success: function(response) {
                //                 var result = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                //                 if(result.success){
                //                     var data=result.obj;
                //                     var html="计划总额："+data.COUNT_MONEY_PLAN+" 元&nbsp;&nbsp;&nbsp;实际总额："+data.COUNT_MONEY_REAL+" 元&nbsp;&nbsp;&nbsp;"+
                //                     "计划总围/人数："+(data.BREAKFAST_COUNT*1+data.LUNCH_COUNT*1+data.DINNER_COUNT*1)+"&nbsp;&nbsp;&nbsp;"+
                //                     "实际总围/人数："+(data.BREAKFAST_REAL*1+data.LUNCH_REAL*1+data.DINNER_REAL*1);
                                
                //                     me.down('panel[ref=dinnerTotalInfo]').setHtml(html);                              
                //                 }else{                                    
                //                     me.down('panel[ref=dinnerTotalInfo]').setHtml("请求失败！");   
                //                 }               
                //             },
                //             failure: function(response) {                                
                //                 self.Error("请求失败！");
                //             }
                //         });
                    
                //     }
                // }); 
            }
            
           
        } , 

        //快速搜索按按钮
        "basepanel basegrid button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                if (!baseGrid)
                    return false;

                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;
                
                var obj={};
                var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                for (var i in girdSearchTexts) {
                    var name = girdSearchTexts[i].getName();
                    var value = girdSearchTexts[i].getValue();
                    obj[name]=value;
                }

                var store = baseGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = obj;
                //store.loadPage(1);

                //加载数据，并且加载汇总数据
                this.loadDinnerTotalDatas(store,baseGrid);

                return false;
            }
        },
        //快速搜索文本框回车事件
        "basepanel basegrid field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {
                    //得到组件                 
                    var baseGrid = field.up("basegrid");
                    if (!baseGrid)
                        return false;

                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;
                    
                    var obj={};
                    var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                    for (var i in girdSearchTexts) {
                        var name = girdSearchTexts[i].getName();
                        var value = girdSearchTexts[i].getValue();
                        obj[name]=value;
                    }

                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = obj;
                    //store.loadPage(1);

                    //加载数据，并且加载汇总数据
                    this.loadDinnerTotalDatas(store,baseGrid);
                }
                return false;
            }
        },     
        "basequeryform[xtype=dinnertotal.mainquerypanel] field":{
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {
                    //得到组件                 
                    //self.Info("暂时无法搜索！");                    
                    var queryPanel = field.up("basequeryform");
                   
                    var obj={};
                    var queryFields=queryPanel.query("basequeryfield");                  
                    Ext.each(queryFields,function(queryField){
                        var fieldName=queryField.name;                       
                        var valueField=queryField.down("field[name="+fieldName+"_field]");
                        var value=valueField.getValue();
                        if(value)
                            obj[fieldName]=value;
                    });

                    var funCode = queryPanel.funCode;
                    var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");                        
                    var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");            
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams=obj;
                    //store.loadPage(1);

                    //加载数据，并且加载汇总数据
                    this.loadDinnerTotalDatas(store,baseGrid);
                }
                return false;
            }
        },
        "basequeryform[xtype=dinnertotal.mainquerypanel] button[ref=gridSearchFormOk]":{
            beforeclick:function(btn){        
                //self.Info("暂时无法搜索！");                    
                var queryPanel = btn.up("basequeryform");
                
                var obj={};
                var queryFields=queryPanel.query("basequeryfield");                  
                Ext.each(queryFields,function(queryField){
                    var fieldName=queryField.name;                       
                    var valueField=queryField.down("field[name="+fieldName+"_field]");
                    var value=valueField.getValue();
                    if(value)
                        obj[fieldName]=value;
                });

                var funCode = queryPanel.funCode;
                var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");                        
                var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");            
                var store = baseGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams=obj;
                //store.loadPage(1);  

                //加载数据，并且加载汇总数据
                this.loadDinnerTotalDatas(store,baseGrid);
                

                return false;
            }
        },
        "basequeryform[xtype=dinnertotal.mainquerypanel] button[ref=gridSearchFormReset]":{
            beforeclick:function(btn){      
                var self=this;
                          
                var queryPanel=btn.up("basequeryform");
                self.resetQueryPanel(queryPanel);

                //触发点击提交事件
                var submitBtn=queryPanel.down("button[ref=gridSearchFormOk]");            
                submitBtn.fireEvent('beforeclick',submitBtn);

                /*
                var funCode = queryPanel.funCode;
                var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");
                var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
                var store = baseGrid.getStore();
                var proxy = store.getProxy();
            
                //只重置这两个数据，还有一个extParams参数是来自baseGrid中设置的，不用去改变。
                proxy.extraParams=null;
                store.load();
                */
                return false;
            }
        },  

        /**
         * 导出班级就餐汇总数据
         */
        "basepanel basegrid button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid=btn.up("basegrid");
                //获取表格参数
                var extraParams = baseGrid.getStore().getProxy().extraParams;
                var params="?t=1";
                //Object {beginDate: "2017-08-02", endDate: "2017-08-05", className: "阿萨德", classNumb: "撒旦"}
                for(var i in extraParams){
                    console.log(i);
                    params+="&"+i+"="+extraParams[i];
                }
               
                var title = "确定要导出就餐汇总信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassrealdinner/exportTotalExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClassrealdinner/checkExportTotalEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
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

        /**
         * 导出班级就餐详情数据
         */
        "basepanel basegrid button[ref=gridDinnerDetailExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid=btn.up("basegrid");
               
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择一个班级！");
                    return;
                }

                var params="?classId="+records[0].get("CLASS_ID");
                var title = "确定要导出["+records[0].get("CLASS_NAME")+"]的班级详细信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassrealdinner/exportDinnerDetailExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClassrealdinner/checkExportDinnerDetailEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
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
        
        /**
         * 按照财务报表导出
         */
        "basepanel basegrid button[ref=gridClassTotalExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid=btn.up("basegrid");
               
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择一个班级！");
                    return;
                }

                var params="?classId="+records[0].get("CLASS_ID");
                var title = "确定要导出["+records[0].get("CLASS_NAME")+"]的班级详细信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassrealdinner/exportClassTotalExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClassrealdinner/checkExportClassTotalEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
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

        /**
         *通用表格详情事件（弹出tab的形式）
         */
        "basepanel basegrid button[ref=gridDetail_Tab]": {
            beforeclick: function (btn) {
                var self=this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择一条数据！");
                    return;
                }

                var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

                //得到配置信息
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                var detCode = basePanel.detCode;
                var funData = basePanel.funData;

                //获取主键值              
                var pkValue = records[0].get("CLASS_ID");

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
                    var insertObj = records[0].data;
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });


                    var tabTitle = records[0].get("CLASS_NAME")+"-就餐详情";

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

                        var classDinnerGrid = tabItem.down("basegrid[xtype=dinnertotal.classdinnergrid]");
                        classDinnerGrid.getStore().getProxy().extraParams.filter = '[{"type":"string","comparison":"=","value":"' + pkValue + '","field":"classId"}]';
                        classDinnerGrid.getStore().load();

                        
                        var html="计划总额："+records[0].get("COUNT_MONEY_PLAN")+" 元&nbsp;&nbsp;&nbsp;实际总额："+records[0].get("COUNT_MONEY_REAL")+" 元&nbsp;&nbsp;&nbsp;"+
                            "计划总围/人数："+(records[0].get("BREAKFAST_COUNT")*1+records[0].get("LUNCH_COUNT")*1+records[0].get("DINNER_COUNT")*1)+"&nbsp;&nbsp;&nbsp;"+
                            "实际总围/人数："+(records[0].get("BREAKFAST_REAL")*1+records[0].get("LUNCH_REAL")*1+records[0].get("DINNER_REAL")*1);
                        
                        classDinnerGrid.down('panel[ref=totalInfo]').setHtml(html);

                    }, 30);

                } 

                tabPanel.setActiveTab(tabItem);

                return false;
            }
        },

         "basegrid[xtype=dinnertotal.maingrid]  actioncolumn": {
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

                //设置tab页的itemId
                var tabItemId = funCode + "_gridDetail";     //命名规则：funCode+'_ref名称',确保不重复
                //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                var tabItem = tabPanel.getComponent(tabItemId);

                //获取主键值              
                var pkValue = record.get("CLASS_ID");

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
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });


                    var tabTitle = record.get("CLASS_NAME")+"-就餐详情";

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

                        var classDinnerGrid = tabItem.down("basegrid[xtype=dinnertotal.classdinnergrid]");
                        classDinnerGrid.getStore().getProxy().extraParams.filter = '[{"type":"string","comparison":"=","value":"' + pkValue + '","field":"classId"}]';
                        classDinnerGrid.getStore().load();

                        var html="计划总额："+record.get("COUNT_MONEY_PLAN")+" 元&nbsp;&nbsp;&nbsp;实际总额："+record.get("COUNT_MONEY_REAL")+" 元&nbsp;&nbsp;&nbsp;"+
                            "计划总围/人数："+(record.get("BREAKFAST_COUNT")*1+record.get("LUNCH_COUNT")*1+record.get("DINNER_COUNT")*1)+"&nbsp;&nbsp;&nbsp;"+
                            "实际总围/人数："+(record.get("BREAKFAST_REAL")*1+record.get("LUNCH_REAL")*1+record.get("DINNER_REAL")*1);
                        
                        classDinnerGrid.down('panel[ref=totalInfo]').setHtml(html);

                    }, 30);

                } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据

                    var objDetForm = tabItem.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();                
                }

                tabPanel.setActiveTab(tabItem);

                return false;
            },
        }
    },

    loadDinnerTotalDatas:function(store,baseGrid){
        var self=this;
        store.loadPage(1,{
            scope: this,
            callback: function(records, operation, success) {
                
                self.syncAjax({
                    url:comm.get('baseUrl') + "/TrainClassrealdinner/getDinnerTotalDatas",
                    timeout: 1000*60*30,        //半个小时         
                    //回调代码必须写在里面
                    success: function(response) {
                        var result = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                        if(result.success){
                            var data=result.obj;
                            var html="计划总额："+data.COUNT_MONEY_PLAN+" 元&nbsp;&nbsp;&nbsp;实际总额："+data.COUNT_MONEY_REAL+" 元&nbsp;&nbsp;&nbsp;"+
                            "计划总围/人数："+(data.BREAKFAST_COUNT*1+data.LUNCH_COUNT*1+data.DINNER_COUNT*1)+"&nbsp;&nbsp;&nbsp;"+
                            "实际总围/人数："+(data.BREAKFAST_REAL*1+data.LUNCH_REAL*1+data.DINNER_REAL*1);
                        
                            baseGrid.down('panel[ref=dinnerTotalInfo]').setHtml(html);                              
                        }else{                                    
                            baseGrid.down('panel[ref=dinnerTotalInfo]').setHtml("请求失败！");   
                        }               
                    },
                    failure: function(response) {                                
                        self.Error("请求失败！");
                    }
                });
            
            }
        });
    }    
       

});

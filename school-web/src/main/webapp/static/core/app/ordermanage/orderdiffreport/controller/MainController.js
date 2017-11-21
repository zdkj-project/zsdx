Ext.define("core.ordermanage.orderdiffreport.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.orderdiffreport.mainController',
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
        "basepanel basegrid[xtype=orderdiffreport.maingrid]":{
            afterrender :function( me , eOpts ) {
                var self=this;
                var store=me.getStore();
                    
                this.loadDinnerTotalDatas(store,me);             
            }                
        },     

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
                var name = girdSearchTexts[0].getName();
                var value = girdSearchTexts[0].getValue();
                if(value){
                    obj["beginDate"]=value;
                    obj["endDate"]=value;
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
        
        "basequeryform[xtype=orderdiffreport.mainquerypanel] button[ref=gridSearchFormOk]":{
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

                //处理分组类别
                var GROUP_TYPES='';
                var groupTypes=queryPanel.down("checkboxgroup").getValue().GROUP_TYPE;
                if(Array.isArray(groupTypes)){
                    GROUP_TYPES=groupTypes.join(',');
                }else{
                    GROUP_TYPES=groupTypes;
                }
                if(GROUP_TYPES!=undefined&&GROUP_TYPES!=""){
                    if(GROUP_TYPES.indexOf("DAY(dinnerDate)")!=-1){
                        if(GROUP_TYPES.indexOf("MONTH(dinnerDate)")==-1){
                            GROUP_TYPES="YEAR(dinnerDate),MONTH(dinnerDate),"+GROUP_TYPES;
                        }
                        if(GROUP_TYPES.indexOf("YEAR(dinnerDate)")==-1){
                            GROUP_TYPES="YEAR(dinnerDate),"+GROUP_TYPES;
                        }
                    }
                    
                    if(GROUP_TYPES.indexOf("MONTH(dinnerDate)")!=-1){
                        if(GROUP_TYPES.indexOf("YEAR(dinnerDate)")==-1){
                            GROUP_TYPES="YEAR(dinnerDate),"+GROUP_TYPES;
                        }
                    }
                }
                obj.GROUP_TYPE=GROUP_TYPES;

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
        "basequeryform[xtype=orderdiffreport.mainquerypanel] button[ref=gridSearchFormReset]":{
            beforeclick:function(btn){      
                var self=this;
                          
                var queryPanel=btn.up("basequeryform");
                self.resetQueryPanel(queryPanel);

                queryPanel.down("checkboxgroup").reset();

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
                
                for(var i in extraParams){
                    if(extraParams[i]!=undefined)
                        params+="&"+i+"="+extraParams[i];
                }
               
                var title = "确定要导出订餐差异信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainTeacherOrder/exportDinnerDiffTotalExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainTeacherOrder/checkExportDinnerDiffTotalEnd',
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

         "basegrid actioncolumn": {
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
                var year = record.get("YEAR");
                var month = record.get("MONTH");           
                var day = record.get("DAY");
                
                var dinnerDate=record.get("dinnerDate");

                //设置tab页的itemId
                var tabItemId = funCode + "_gridDetail"+dinnerDate;     //命名规则：funCode+'_ref名称',确保不重复
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
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });


                    var tabTitle = record.get("dinnerDate")+"-订餐就餐明细";

                    tabItem = Ext.create({
                        xtype: 'container',
                        title: tabTitle,
                        //iconCls: 'x-fa fa-clipboard',
                        scrollable: true,
                        itemId: tabItemId,
                        itemPKV: dinnerDate,    //保存主键值
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
                                xtype: detLayout,  
                                layout: 'hbox',                              
                                items: [{
                                    xtype: "orderdiffreport.ordereatdetailgrid",
                                    title:'就餐名单（已订餐）',
                                    flex:1,
                                    split:true,
                                    height:'100%',                                    
                                    border:1
                                },{
                                    xtype: "orderdiffreport.ordernoteatdetailgrid",
                                    title:'未就餐名单（已订餐）',
                                    flex:1,
                                    split:true,
                                    height:'100%',
                                    border:1
                                },{
                                    xtype: "orderdiffreport.notordereatdetailgrid",
                                    title:'就餐名单（未订餐）',
                                    flex:1,
                                    split:true,
                                    height:'100%',
                                    border:1
                                }],
                          
                            }]
                        });
                        tabItem.add(item);
                        
                        var orderEatDetailGrid = tabItem.down("basegrid[xtype=orderdiffreport.ordereatdetailgrid]");
                        var params1={
                            YEAR:year,
                            MONTH:month,
                            DAY:day,
                            type:"1"
                        };
                        orderEatDetailGrid.getStore().getProxy().extraParams=params1;
                        orderEatDetailGrid.getStore().load();

                        var orderNotEatDetailGrid = tabItem.down("basegrid[xtype=orderdiffreport.ordernoteatdetailgrid]");
                        var params2={
                            YEAR:year,
                            MONTH:month,
                            DAY:day,
                            type:"2"
                        };
                        orderNotEatDetailGrid.getStore().getProxy().extraParams=params2;
                        orderNotEatDetailGrid.getStore().load();

                        var notOrderEatDetailGrid = tabItem.down("basegrid[xtype=orderdiffreport.notordereatdetailgrid]");
                        var params3={
                            YEAR:year,
                            MONTH:month,
                            DAY:day,
                            type:"3"
                        };
                        notOrderEatDetailGrid.getStore().getProxy().extraParams=params3;
                        notOrderEatDetailGrid.getStore().load();

                    }, 30);

                }  else if(tabItem.itemPKV&&tabItem.itemPKV!=dinnerDate){     //判断是否点击的是同一条数据
                    self.Warning("您当前已经打开了一个编辑窗口了！");
                    return;
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
                    url:comm.get('baseUrl') + "/TrainTeacherOrder/getDinnerDiffTotalDatas",
                    timeout: 1000*60*30,        //半个小时         
                    //回调代码必须写在里面
                    success: function(response) {
                        var result = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                        if(result.success){
                            var data=result.obj;
                            var html="订餐总数："+data.dinnerCount+" &nbsp;&nbsp;&nbsp;A套餐总数："+data.dinnerCountA+" &nbsp;&nbsp;&nbsp;"+
                            "B套餐总数："+data.dinnerCountB+" &nbsp;&nbsp;&nbsp;实际就餐总数："+data.realCount+" &nbsp;&nbsp;&nbsp;"+
                            "就餐总数（已订餐）："+data.realDinnerCount+" &nbsp;&nbsp;&nbsp;就餐总数（未订餐）："+data.notDinnerEatCount+" &nbsp;&nbsp;&nbsp;"+
                            "未就餐总数（已订餐）："+data.notEatdinnerCount;
                            //"找零金额："+data.CHANGE_PAY+" 元&nbsp;&nbsp;&nbsp;";
                        
                            baseGrid.down('panel[ref=dinnerDiffTotalInfo]').setHtml(html);                              
                        }else{                                    
                            baseGrid.down('panel[ref=dinnerDiffTotalInfo]').setHtml("请求失败！");   
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
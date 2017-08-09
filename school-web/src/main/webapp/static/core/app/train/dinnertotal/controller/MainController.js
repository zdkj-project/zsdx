Ext.define("core.train.dinnertotal.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.dinnertotal.mainController',
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
                store.loadPage(1);

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
                    store.loadPage(1);
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
                    store.loadPage(1);
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
                store.loadPage(1);  

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

                //设置tab页的itemId
                var tabItemId = funCode + "_gridDetail";     //命名规则：funCode+'_ref名称',确保不重复
                //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                var tabItem = tabPanel.getComponent(tabItemId);

                //获取主键值              
                var pkValue = records[0].get("CLASS_ID");

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


                    }, 30);

                } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据

                    var objDetForm = tabItem.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();                
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


                    }, 30);

                } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据

                    var objDetForm = tabItem.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();                
                }

                tabPanel.setActiveTab(tabItem);

                return false;
            },
        }
    }    
       

});

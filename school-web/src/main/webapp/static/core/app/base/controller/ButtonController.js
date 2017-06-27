/**
 * 程序主控制器
 */
Ext.define("core.base.controller.ButtonController", {
    extend: "Ext.app.Controller",
    initBtn: function () {
        var self = this;
        var btnCtr = {

            //高级搜索按钮
            "basepanel basegrid button[ref=gridHignSearch]": {
                click: function (btn) {
                    //得到组件
                    var baseGrid = btn.up("basegrid");
                    var baseQueryForm = baseGrid.down("basequeryform");

                    if (baseQueryForm) {
                        var isHidden = baseQueryForm.hidden;
                        if (isHidden)
                            baseQueryForm.show();
                        else
                            baseQueryForm.hide();
                    }
                }
            },
            //快速搜索按按钮
            "basepanel basegrid button[ref=gridFastSearchBtn]": {
                click: function (btn) {
                    //得到组件                 
                    var baseGrid = btn.up("basegrid");
                    if (!baseGrid)
                        return false;

                    var toolBar = btn.up("toolbar");
                    if (!toolBar)
                        return false;

                    var filter = [];
                    var gridFilter=[];
                    //获取baseGrid中编写的默认filter值
                    var gridFilterStr=baseGrid.extParams.filter;
                    if(gridFilterStr&&gridFilterStr.trim()!=""){
                        gridFilter=JSON.parse(gridFilterStr);
                        filter=gridFilter;
                    }
                   
                    var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                    for (var i in girdSearchTexts) {
                        var name = girdSearchTexts[i].getName();
                        var value = girdSearchTexts[i].getValue();

                        //判断gridFilter是否包含此值。
                        var isExist=false;
                        for(var j=0;j<gridFilter.length;j++){
                            if(gridFilter[j].field==name){
                                filter[j]={"type": "string", "value": value, "field": name, "comparison": ""};
                                isExist=true;
                                break;
                            }
                        }
                        if(isExist==false)
                            filter.push({"type": "string", "value": value, "field": name, "comparison": ""});
                    }

                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams.filter = JSON.stringify(filter);
                    store.loadPage(1);

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

                        var filter = [];
                        var gridFilter=[];
                        //获取baseGrid中编写的默认filter值
                        var gridFilterStr=baseGrid.extParams.filter;
                        if(gridFilterStr&&gridFilterStr.trim()!=""){
                            gridFilter=JSON.parse(gridFilterStr);
                            filter=gridFilter;
                        }
                       
                        var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                        for (var i in girdSearchTexts) {
                            var name = girdSearchTexts[i].getName();
                            var value = girdSearchTexts[i].getValue();

                            //判断gridFilter是否包含此值。
                            var isExist=false;
                            for(var j=0;j<gridFilter.length;j++){
                                if(gridFilter[j].field==name){
                                    filter[j]={"type": "string", "value": value, "field": name, "comparison": ""};
                                    isExist=true;
                                    break;
                                }
                            }
                            if(isExist==false)
                                filter.push({"type": "string", "value": value, "field": name, "comparison": ""});
                        }

                       
                        var store = baseGrid.getStore();
                        var proxy = store.getProxy();
                        proxy.extraParams.filter = JSON.stringify(filter);
                        store.loadPage(1);
                    }
                }
            },


            /**
             *通用表格添加事件（弹出tab的形式）
             */
            "basepanel basegrid button[ref=gridAdd_Tab]": {
                click: function (btn) {

                    //得到组件
                    var baseGrid = btn.up("basegrid");
                    var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

                    //得到配置信息
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");

                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;
                    var defaultObj = funData.defaultObj;

                    //关键：window的视图控制器
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';

                    //设置tab页的itemId
                    var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复

                    //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                    var tabItem = tabPanel.getComponent(tabItemId);

                    // var width = 1000;
                    // var height = 650;
                    // if (funData.width)
                    //     width = funData.width;
                    // if (funData.height)
                    //     height = funData.height;
                    //处理特殊默认值
                    var insertObj = self.getDefaultValue(defaultObj);
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });

                    if (!tabItem) {
                        var tabTitle = funData.tabConfig.addTitle;

                        tabItem = Ext.create({
                            xtype: 'container',
                            title: tabTitle,
                            //iconCls: 'x-fa fa-clipboard',
                            scrollable: true,
                            itemId: tabItemId,
                            layout: 'fit',
                        });
                        tabPanel.add(tabItem);

                        //延迟放入到tab中
                        setTimeout(function () {
                            //创建组件
                            var item = Ext.widget("baseformtab", {
                                operType: 'add',
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

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);

                        }, 30);

                    }

                    tabPanel.setActiveTab(tabItem);


                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },
            
            
            /**
             *通用treegird表格添加事件（弹出tab的形式）
             */
            "basepanel basetreegrid button[ref=gridAdd_Tab]": {
                click: function (btn) {

                    //得到组件
                    var baseGrid = btn.up("basetreegrid");
                    var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

                    //得到配置信息
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");

                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;
                    var defaultObj = funData.defaultObj;

                    //关键：window的视图控制器
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';

                    //设置tab页的itemId
                    var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复

                    //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                    var tabItem = tabPanel.getComponent(tabItemId);

                    // var width = 1000;
                    // var height = 650;
                    // if (funData.width)
                    //     width = funData.width;
                    // if (funData.height)
                    //     height = funData.height;
                    //处理特殊默认值
                    var insertObj = self.getDefaultValue(defaultObj);
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });

                    if (!tabItem) {
                        var tabTitle = funData.tabConfig.addTitle;

                        tabItem = Ext.create({
                            xtype: 'container',
                            title: tabTitle,
                            //iconCls: 'x-fa fa-clipboard',
                            scrollable: true,
                            itemId: tabItemId,
                            layout: 'fit',
                        });
                        tabPanel.add(tabItem);

                        //延迟放入到tab中
                        setTimeout(function () {
                            //创建组件
                            var item = Ext.widget("baseformtab", {
                                operType: 'add',
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

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);

                        }, 30);

                    }

                    tabPanel.setActiveTab(tabItem);


                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },
            
            
            /**
             *通用treegird表格添加事件（弹出tab的形式）
             */
            "basepanel basetreegrid button[ref=gridAddBrother_Tab]": {
                click: function (btn) {

                    //得到组件
                    var baseGrid = btn.up("basetreegrid");
                    var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

                    //得到配置信息
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");

                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;
                    var defaultObj = funData.defaultObj;

                    //关键：window的视图控制器
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';

                    //设置tab页的itemId
                    var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复

                    //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                    var tabItem = tabPanel.getComponent(tabItemId);

                    // var width = 1000;
                    // var height = 650;
                    // if (funData.width)
                    //     width = funData.width;
                    // if (funData.height)
                    //     height = funData.height;
                    //处理特殊默认值
                    var insertObj = self.getDefaultValue(defaultObj);
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });

                    if (!tabItem) {
                        var tabTitle = funData.tabConfig.AddBrotherTitle;

                        tabItem = Ext.create({
                            xtype: 'container',
                            title: tabTitle,
                            //iconCls: 'x-fa fa-clipboard',
                            scrollable: true,
                            itemId: tabItemId,
                            layout: 'fit',
                        });
                        tabPanel.add(tabItem);

                        //延迟放入到tab中
                        setTimeout(function () {
                            //创建组件
                            var item = Ext.widget("baseformtab", {
                                operType: 'add',
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

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);

                        }, 30);

                    }

                    tabPanel.setActiveTab(tabItem);


                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },
            

            /**
             *通用表格编辑事件（弹出tab的形式）
             */
            "basepanel basetreegrid button[ref=gridEdit_Tab]": {
                click: function (btn) {

                    //得到组件
                    var baseGrid = btn.up("basetreegrid");
                    var records = baseGrid.getSelectionModel().getSelection();
                    if (records.length != 1) {
                        self.Warning("请选择一条数据！");
                        return;
                    }

                    var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

                    //得到配置信息
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;

                    //设置tab页的itemId
                    var tabItemId = funCode + "_gridEdit";     //命名规则：funCode+'_ref名称',确保不重复
                    //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                    var tabItem = tabPanel.getComponent(tabItemId);

                    //获取主键值
                    var pkName = funData.pkName;
                    var pkValue = records[0].get(pkName);

                    //判断是否已经存在tab了
                    if (!tabItem) {

                        var detCode = basePanel.detCode;
                        var detLayout = basePanel.detLayout;
                        var defaultObj = funData.defaultObj;

                        //关键：window的视图控制器
                        var otherController = basePanel.otherController;
                        if (!otherController)
                            otherController = '';

                        // var width = 1000;
                        // var height = 650;
                        // if (funData.width)
                        //     width = funData.width;
                        // if (funData.height)
                        //     height = funData.height;

                        //处理特殊默认值
                        var insertObj = records[0].data;
                        var popFunData = Ext.apply(funData, {
                            grid: baseGrid
                        });

                        var tabTitle = funData.tabConfig.editTitle;

                        tabItem = Ext.create({
                            xtype: 'container',
                            title: tabTitle,
                            //iconCls: 'x-fa fa-clipboard',
                            scrollable: true,
                            itemId: tabItemId,
                            itemPKV: pkValue,      //保存主键值
                            layout: 'fit',
                        });
                        tabPanel.add(tabItem);

                        //延迟放入到tab中
                        setTimeout(function () {
                            //创建组件
                            var item = Ext.widget("baseformtab", {
                                operType: 'edit',
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

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);

                        }, 30);

                    } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
                        self.Warning("您当前已经打开了一个编辑窗口了！");
                        return;
                    }

                    tabPanel.setActiveTab(tabItem);

                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },
            

            /**
             *通用表格编辑事件（弹出tab的形式）
             */
            "basepanel basegrid button[ref=gridEdit_Tab]": {
                click: function (btn) {

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
                    var funData = basePanel.funData;

                    //设置tab页的itemId
                    var tabItemId = funCode + "_gridEdit";     //命名规则：funCode+'_ref名称',确保不重复
                    //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
                    var tabItem = tabPanel.getComponent(tabItemId);

                    //获取主键值
                    var pkName = funData.pkName;
                    var pkValue = records[0].get(pkName);

                    //判断是否已经存在tab了
                    if (!tabItem) {

                        var detCode = basePanel.detCode;
                        var detLayout = basePanel.detLayout;
                        var defaultObj = funData.defaultObj;

                        //关键：window的视图控制器
                        var otherController = basePanel.otherController;
                        if (!otherController)
                            otherController = '';

                        // var width = 1000;
                        // var height = 650;
                        // if (funData.width)
                        //     width = funData.width;
                        // if (funData.height)
                        //     height = funData.height;

                        //处理特殊默认值
                        var insertObj = records[0].data;
                        var popFunData = Ext.apply(funData, {
                            grid: baseGrid
                        });

                        var tabTitle = funData.tabConfig.editTitle;

                        tabItem = Ext.create({
                            xtype: 'container',
                            title: tabTitle,
                            //iconCls: 'x-fa fa-clipboard',
                            scrollable: true,
                            itemId: tabItemId,
                            itemPKV: pkValue,      //保存主键值
                            layout: 'fit',
                        });
                        tabPanel.add(tabItem);

                        //延迟放入到tab中
                        setTimeout(function () {
                            //创建组件
                            var item = Ext.widget("baseformtab", {
                                operType: 'edit',
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

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);

                        }, 30);

                    } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
                        self.Warning("您当前已经打开了一个编辑窗口了！");
                        return;
                    }

                    tabPanel.setActiveTab(tabItem);

                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },
            /**
             *通用表格详情事件（弹出tab的形式）
             */
            "basepanel basegrid button[ref=gridDetail_Tab]": {
                click: function (btn) {

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
                    var pkName = funData.pkName;
                    var pkValue = records[0].get(pkName);

                    //判断是否已经存在tab了
                    if (!tabItem) {

                        var detLayout = basePanel.detLayout;
                        var defaultObj = funData.defaultObj;

                        //关键：window的视图控制器
                        var otherController = basePanel.otherController;
                        if (!otherController)
                            otherController = '';

                        // var width = 1000;
                        // var height = 650;
                        // if (funData.width)
                        //     width = funData.width;
                        // if (funData.height)
                        //     height = funData.height;

                        //处理特殊默认值
                        var insertObj = records[0].data;
                        var popFunData = Ext.apply(funData, {
                            grid: baseGrid
                        });


                        var tabTitle = funData.tabConfig.detailTitle;

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

                            var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                            var formDeptObj = objDetForm.getForm();

                            self.setFormValue(formDeptObj, insertObj);

                            self.setFuncReadOnly(funData, objDetForm, true);
                        }, 30);

                    } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据

                        var objDetForm = tabItem.down("baseform[funCode=" + detCode + "]");
                        var formDeptObj = objDetForm.getForm();
                        self.setFormValue(formDeptObj, records[0].data);
                        self.setFuncReadOnly(funData, objDetForm, true);

                    }

                    tabPanel.setActiveTab(tabItem);

                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },


            /**
             *通用表格添加事件
             */
            "basepanel basegrid button[ref=gridAdd]": {
                click: function (btn) {

                    //得到组件
                    var baseGrid = btn.up("basegrid");
                    var store = baseGrid.getStore();
                    //得到模型
                    var Model = store.model;
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息

                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;
                    var defaultObj = funData.defaultObj;

                    //关键：window的视图控制器
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';

                    var width = 1000;
                    var height = 650;
                    if (funData.width)
                        width = funData.width;
                    if (funData.height)
                        height = funData.height;
                    //处理特殊默认值
                    var insertObj = self.getDefaultValue(defaultObj);
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });
                    var win = Ext.create('core.base.view.BaseFormWin', {
                        iconCls: 'x-fa fa-plus-circle',
                        operType: 'add',
                        width: width,
                        height: height,
                        controller: otherController, //指定视图控制器，从而能够使指定的控制器的事件生效
                        funData: popFunData,
                        funCode: detCode,
                        insertObj: insertObj,
                        items: [{
                            xtype: detLayout
                        }]
                    });
                    win.show();
                    var detPanel = win.down("basepanel[funCode=" + detCode + "]");
                    var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();

                    self.setFormValue(formDeptObj, insertObj);

                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },
            /**
             * 通用表格详细点击事件
             * @type {[type]}
             */
            "basepanel basegrid button[ref=gridDetail]": {
                click: function (btn) {
                    //得到组件
                    var baseGrid = btn.up("basegrid");

                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.Info("请选择数据");
                        return;
                    }


                    //得到模型
                    var store = baseGrid.getStore();
                    var Model = store.model;
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;

                    //关键：window的视图控制器
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';

                    var insertObj = rescords[0].data;
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });
                    var width = 1000;
                    var height = 650;
                    if (funData.width)
                        width = funData.width;
                    if (funData.height)
                        height = funData.height;
                    var win = Ext.create('core.base.view.BaseFormWin', {
                        iconCls: 'x-fa fa-file-text',
                        operType: 'detail',
                        width: width,
                        height: height,
                        controller: otherController,
                        funData: popFunData,
                        funCode: detCode,
                        insertObj: insertObj,
                        items: [{
                            xtype: detLayout
                        }]
                    });
                    win.show();
                    var detPanel = win.down("basepanel[funCode=" + detCode + "]");
                    var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();
                    self.setFormValue(formDeptObj, insertObj);

                    self.setFuncReadOnly(funData, objDetForm, true);
                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }
                }
            },
            /**
             * 通用表格编辑事件
             */
            "basepanel basegrid button[ref=gridEdit]": {
                click: function (btn) {

                    //得到组件
                    var baseGrid = btn.up("basegrid");

                    var records = baseGrid.getSelectionModel().getSelection();
                    if (records.length != 1) {
                        self.Info("请选择数据");
                        return;
                    }


                    //得到模型
                    var store = baseGrid.getStore();
                    var Model = store.model;
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;

                    //关键：window的视图控制器
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';


                    var insertObj = records[0].data;
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });
                    var width = 1000;
                    var height = 650;
                    if (funData.width)
                        width = funData.width;
                    if (funData.height)
                        height = funData.height;
                    var win = Ext.create('core.base.view.BaseFormWin', {
                        iconCls: 'x-fa fa-pencil-square',
                        operType: 'edit',
                        width: width,
                        height: height,
                        controller: otherController,
                        funData: popFunData,
                        funCode: detCode,
                        insertObj: insertObj,
                        items: [{
                            xtype: detLayout
                        }]
                    });
                    win.show();
                    var detPanel = win.down("basepanel[funCode=" + detCode + "]");
                    var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");


                    var formDeptObj = objDetForm.getForm();
                    self.setFormValue(formDeptObj, insertObj);


                    //objDetForm.loadRecord (records[0]);                    
                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }
                }
            },
            /**
             *  通用表格删除事件
             */
            "basepanel basegrid button[ref=gridDelete]": {
                click: function (btn) {
                    /*
                     var grid=btn.up("grid");
                     var records=grid.getSelectionModel().getSelection();
                     if(records.length==1){
                     Ext.Msg.confirm('提示', '确定要删除这条数据么?', function(btn, text) {
                     if (btn == 'yes') {
                     grid.getStore().remove(records[0]);
                     //records[0].erase();
                     }
                     });

                     }else{
                     Ext.Msg.alert('温馨提示', '请选择一条数据！');
                     }*/


                    //得到组件
                    var baseGrid = btn.up("basegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                    //得到选中数据
                    var records = baseGrid.getSelectionModel().getSelection();
                    if (records.length > 0) {
                        //封装ids数组
                        Ext.Msg.confirm('提示', '是否删除数据?', function (btn, text) {
                            if (btn == 'yes') {
                                var ids = new Array();
                                Ext.each(records, function (rec) {
                                    var pkValue = rec.get(pkName);
                                    ids.push(pkValue);
                                });
                                //发送ajax请求
                                var resObj = self.ajax({
                                    url: funData.action + "/dodelete",
                                    params: {
                                        ids: ids.join(","),
                                        pkName: pkName
                                    }
                                });
                                if (resObj.success) {
                                    baseGrid.getStore().load();
                                    self.msgbox(resObj.obj);
                                } else {
                                    self.Error(resObj.obj);
                                }
                            }
                        });
                    } else {
                        self.Warning("请选择数据");
                    }
                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }

                }
            },
            /*"basegrid button[ref=gridExcel]": {
             click: function(btn) {
             var baseGrid = btn.up("basegrid");
             var funCode = baseGrid.funCode;
             var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
             var funData = basePanel.funData;
             var excelInfo = funData.excelInfo;
             if (excelInfo) {
             if (excelInfo.excelFields) {
             var fieldNames = new Array();
             var fieldCodes = new Array();
             Ext.each(excelInfo.excelFields, function(field) {
             fieldNames.push(field.name);
             fieldCodes.push(field.code);
             });
             var tableCode = funData.tableName;
             var store = baseGrid.getStore();
             var params = store.getProxy().extraParams;
             params.fieldNames = fieldNames.join(",");
             params.fieldCodes = fieldCodes.join(",");
             params.tableCode = tableCode;
             var resObj = self.ajax({
             url: "/core/excelReportAction!gridReport.action",
             params: params
             });
             if (resObj.success) {
             window.open(resObj.obj.url);
             } else {
             self.Info(resObj.obj);
             }
             } else {
             self.Info("请配置导出字段信息！");
             }
             } else {
             self.Info("请配置导出信息！");
             }
             }
             },*/
            /**
             * 通用导出Excel
             * @type {[type]}
             */
            "basepanel basegrid button[ref=exportExcel]": {
                click: function (btn) {
                    var baseGrid = btn.up("basegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                    funData = Ext.apply(funData, {
                        modelName: baseGrid.model,
                        fileName: baseGrid.fileName,
                        exWhereSql: baseGrid.exWhereSql
                    });
                    var win = Ext.create('Ext.Window', {
                        title: "导出Excel",
                        iconCls: 'application_form',
                        width: 620,
                        resizable: false,
                        constrain: true,
                        //autoHeight: true,
                        height: 600,
                        modal: true,
                        closeAction: 'destroy',
                        plain: true,
                        items: [{
                            xtype: "baseexportexcel",
                            funData: funData
                        }]
                    });
                    win.show();
                    /*var detailPanel = win.down("panel[xtype=base.exportexcel]");
                     var formObj = detailPanel.form;
                     var store = formObj.findField("itemselector").getStore();
                     var proxy = store.getProxy();
                     proxy.extraParams= {
                     modelName : funData.modelName
                     }
                     store.load();*/
                    //detailPanel.modelName=funData.modelName;

                    /* detailPanel.funData = Ext.apply(funData, {
                     modelName:funData.modelName,
                     fileName:"funData.fileName"
                     });   */
                    return false;
                }
            },
            /**
             * 通用下载导入模版
             * @type {[type]}
             */
            "basepanel basegrid button[ref=dlImportModel]": {
                click: function (btn) {
                    var baseGrid = btn.up("basegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                    Ext.Msg.wait('正在生成中,请稍后...', '温馨提示');
                    Ext.create('Ext.panel.Panel', {
                        title: 'Hello',
                        width: 200,
                        html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="' + comm.get('baseUrl') + '/ExcelFactory/dlImportModel?modelName=' + funData.modelName + '&fileName=' + funData.fileName + '模版' + '"></iframe>',
                        renderTo: Ext.getBody(),
                        listeners: {
                            afterrender: function () {
                                var task = new Ext.util.DelayedTask(function () {
                                    Ext.Msg.hide();
                                });
                                task.delay(3000);
                            }
                        }
                    });
                }
            },
            "basepanel basegrid button[ref=importExcel]": {
                click: function (btn) {
                    var baseGrid = btn.up("basegrid")
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                    var win = Ext.create('core.base.view.BaseFormWin', {
                        title: "文件上传",
                        iconCls: 'application_form',
                        operType: 'addReturn',
                        width: 450,
                        height: 120,
                        grid: baseGrid,
                        funData: funData,
                        items: [{
                            xtype: "baseimportexcel"
                        }]
                    });
                    win.show();
                    return false;
                }
            },
            // "baseformwin button[ref=formSave],panel[xtype=baseimportexcel]": {
            //      click: function(btn) {
            //         var win = btn.up('window');

            //         var panel = win.down("panel");
            //         var formObj = panel.getForm();
            //         //formObj.getFields("modelName").setValue(win.funData.modelName);

            //         if (formObj.isValid()) {
            //             formObj.submit({
            //                 url: comm.get('baseUrl') + "/ExcelFactory/importExcel?modelName="+win.funData.modelName,
            //                 waitMsg: '正在上传文件...',
            //                 success: function(form, action) {
            //                     self.Info("文件上传成功！");
            //                 },error:function(){
            //                     self.Info("文件上传失败！");
            //                 }
            //             });
            //         } else {
            //             self.Error("请选择要上传文件！");
            //         }
            //         return false;
            //     }
            // },
            /**
             * 通用弹出窗体保存事件
             * 采用ajax的模式提交数据，并清空所有输入项后可以重新输入
             */
            "baseformwin button[ref=formContinue]": {
                click: function (btn) {
                    var win = btn.up("window");
                    var funCode = win.funCode;
                    var basePanel = win.down("basepanel[funCode=" + funCode + "]");
                    var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
                    var formObj = objForm.getForm();
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                    var pkField = formObj.findField(pkName);
                    var params = self.getFormValue(formObj);
                    var orderIndex = 1;
                    if (formObj.findField("orderIndex")) {
                        orderIndex = formObj.findField("orderIndex").getValue() + 1;
                    }

                    //判断当前是保存还是修改操作
                    var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
                    if (formObj.isValid()) {
                        var resObj = self.ajax({
                            url: funData.action + "/" + act,
                            params: params
                        });
                        if (resObj.success) {
                            formObj.reset();
                            self.msgbox("保存成功!");
                            //给窗体赋默认值
                            var insertObj = win.insertObj;
                            insertObj = Ext.apply(insertObj, {
                                orderIndex: orderIndex
                            });
                            self.setFormValue(formObj, insertObj);
                            var grid = win.funData.grid; //窗体是否有grid参数
                            if (!Ext.isEmpty(grid)) {
                                var store = grid.getStore();
                                /* zzk：2017-4-6 直接使用store中自带的条件
                                 var proxy = store.getProxy();
                                 proxy.extraParams = {
                                 whereSql: win.funData.whereSql,
                                 orderSql: win.funData.orderSql,
                                 filter: win.funData.filter
                                 };*/
                                store.loadPage(1); //刷新父窗体的grid
                            }
                        } else {
                            self.Error(resObj.obj);
                        }
                    } else {
                        var errors = ["前台验证失败，错误信息："];
                        formObj.getFields().each(function (f) {
                            if (!f.isValid()) {
                                errors.push("<font color=red>" + f.fieldLabel + "</font>:" + f.getErrors().join(","));
                            }
                        });
                        self.msgbox(errors.join("<br/>"));
                    }

                    if (btn.callback) {
                        btn.callback();
                    }
                }
            },
            /**
             * 通用弹出窗体保存事件
             * 采用ajax的模式提交数据，并返回提交的数据
             */
            "baseformwin button[ref=formSave]": {
                click: function (btn) {
                    var win = btn.up('window');
                    var funCode = win.funCode;
                    var basePanel = win.down("basepanel[funCode=" + funCode + "]");
                    var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
                    var formObj = objForm.getForm();
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                    var pkField = formObj.findField(pkName);
                    var params = self.getFormValue(formObj);
                    var deptId = "";
                    if (win.funData.deptId) {
                        deptId = win.funData.deptId;
                    }
                    //判断当前是保存还是修改操作
                    var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
                    if (formObj.isValid()) {

                        var resObj = self.ajax({
                            url: funData.action + "/" + act,
                            params: params
                        });
                        if (resObj.success) {
                            //采用返回的数据刷新表单
                            //self.setFormValue(formObj, resObj.obj);

                            self.msgbox("保存成功!");
                            var grid = win.funData.grid; //窗体是否有grid参数
                            if (!Ext.isEmpty(grid)) {
                                var store = grid.getStore();
                                /* zzk：2017-4-6 直接使用store中自带的条件
                                 var proxy = store.getProxy();
                                 proxy.extraParams = {
                                 whereSql: win.funData.whereSql,
                                 orderSql: win.funData.orderSql,
                                 filter: win.funData.filter,
                                 deptId: deptId
                                 };*/
                                store.loadPage(1); //刷新父窗体的grid

                                //var record=store.insert(0,resObj.obj);   
                                //record.save();    //会自动根据是否存在id去执行proxy中的create或update的请求 ，待验证
                            }
                            win.close();
                        } else {
                            if (!Ext.isEmpty(resObj.obj)) self.Error(resObj.obj);
                        }


                    } else {

                        var errors = ["前台验证失败，错误信息："];
                        formObj.getFields().each(function (f) {
                            if (!f.isValid()) {
                                errors.push("<font color=red>" + f.fieldLabel + "</font>:" + f.getErrors().join(","));
                            }
                        });
                        self.msgbox(errors.join("<br/>"));
                    }
                    if (btn.callback) {
                        btn.callback();
                    }
                }
            },
            /**
             * 通用弹出窗体关闭事件
             * 直接关闭窗体了
             */
            "baseformwin button[ref=formClose]": {
                click: function (btn) {
                    var win = btn.up('window');
                    // var grid = win.funData.grid; //窗体是否有grid参数
                    // if (!Ext.isEmpty(grid)) {
                    //     grid.getStore().load(); //刷新父窗体的grid
                    // }
                    //关闭窗体
                    win.close();
                }
            },


            /**
             * 标签页中的三个按钮事件
             */
            "baseformtab button[ref=formContinue]": {
                click: function (btn) {

                    var basetab = btn.up('baseformtab');
                    var tabPanel = btn.up("tabpanel[xtype=app-main]");
                    var tabItemId = basetab.tabItemId;
                    var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页


                    var funCode = basetab.funCode;      //mainLayout的funcode
                    var detCode = basetab.detCode;      //detailLayout的funcode

                    var detPanel = basetab.down("basepanel[funCode=" + detCode + "]");
                    var objForm = detPanel.down("baseform[funCode=" + detCode + "]");

                    var formObj = objForm.getForm();
                    var funData = detPanel.funData;
                    var pkName = funData.pkName;
                    var pkField = formObj.findField(pkName);
                    var params = self.getFormValue(formObj);
                    var orderIndex = 1;
                    if (formObj.findField("orderIndex")) {
                        orderIndex = formObj.findField("orderIndex").getValue() + 1;
                    }


                    //把checkbox的值转换为数字 ；    暂时测试时设置，
                    //params.needChecking=params.needChecking==true?1:0;
                    //params.needSynctrainee=params.needSynctrainee==true?1:0;


                    //判断当前是保存还是修改操作
                    var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
                    if (formObj.isValid()) {

                        var loading = new Ext.LoadMask(basetab, {
                            msg: '正在提交，请稍等...',
                            removeMask: true// 完成后移除
                        });
                        loading.show();

                        self.asyncAjax({
                            url: funData.action + "/" + act,
                            params: params,
                            //回调代码必须写在里面
                            success: function (response) {
                                data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                if (data.success) {
                                    formObj.reset();
                                    self.msgbox("保存成功!");
                                    //给窗体赋默认值
                                    var insertObj = basetab.insertObj;
                                    insertObj = Ext.apply(insertObj, {
                                        orderIndex: orderIndex
                                    });
                                    self.setFormValue(formObj, insertObj);

                                    var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                    if (!Ext.isEmpty(grid)) {
                                        var store = grid.getStore();
                                        /* zzk：2017-4-6 直接使用store中自带的条件
                                         var proxy = store.getProxy();
                                         proxy.extraParams = {
                                         whereSql: win.funData.whereSql,
                                         orderSql: win.funData.orderSql,
                                         filter: win.funData.filter
                                         };*/
                                        store.loadPage(1); //刷新父窗体的grid
                                    }

                                    loading.hide();
                                } else {
                                    self.Error(data.obj);
                                    loading.hide();
                                }
                            }
                        });

                    } else {
                        var errors = ["前台验证失败，错误信息："];
                        formObj.getFields().each(function (f) {
                            if (!f.isValid()) {
                                errors.push("<font color=red>" + f.fieldLabel + "</font>：" + f.getErrors().join(","));
                            }
                        });
                        self.msgbox(errors.join("<br/>"));
                    }

                    if (btn.callback) {
                        btn.callback();
                    }
                }
            },
            "baseformtab button[ref=formSave]": {
                click: function (btn) {

                    var basetab = btn.up('baseformtab');
                    var tabPanel = btn.up("tabpanel[xtype=app-main]");
                    var tabItemId = basetab.tabItemId;
                    var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页


                    var funCode = basetab.funCode;      //mainLayout的funcode
                    var detCode = basetab.detCode;      //detailLayout的funcode

                    var detPanel = basetab.down("basepanel[funCode=" + detCode + "]");
                    var objForm = detPanel.down("baseform[funCode=" + detCode + "]");

                    var formObj = objForm.getForm();
                    var funData = detPanel.funData;
                    var pkName = funData.pkName;
                    var pkField = formObj.findField(pkName);
                    var params = self.getFormValue(formObj);
                    var orderIndex = 1;
                    if (formObj.findField("orderIndex")) {
                        orderIndex = formObj.findField("orderIndex").getValue() + 1;
                    }

                    //把checkbox的值转换为数字 ；    暂时测试时设置，
                    //params.needChecking=params.needChecking==true?1:0;
                    //params.needSynctrainee=params.needSynctrainee==true?1:0;

                    //判断当前是保存还是修改操作
                    var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
                    if (formObj.isValid()) {

                        var loading = new Ext.LoadMask(basetab, {
                            msg: '正在提交，请稍等...',
                            removeMask: true// 完成后移除
                        });
                        loading.show();

                        self.asyncAjax({
                            url: funData.action + "/" + act,
                            params: params,
                            //回调代码必须写在里面
                            success: function (response) {
                                data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                if (data.success) {

                                    self.msgbox("保存成功!");

                                    var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                    if (!Ext.isEmpty(grid)) {
                                        var store = grid.getStore();
                                        /* zzk：2017-4-6 直接使用store中自带的条件
                                         var proxy = store.getProxy();
                                         proxy.extraParams = {
                                         whereSql: win.funData.whereSql,
                                         orderSql: win.funData.orderSql,
                                         filter: win.funData.filter
                                         };*/
                                        store.load(); //刷新父窗体的grid
                                    }

                                    loading.hide();
                                    tabPanel.remove(tabItem);
                                } else {
                                    self.Error(data.obj);
                                    loading.hide();
                                }
                            }
                        });

                    } else {
                        var errors = ["前台验证失败，错误信息："];
                        formObj.getFields().each(function (f) {
                            if (!f.isValid()) {
                                errors.push("<font color=red>" + f.fieldLabel + "</font>：" + f.getErrors().join(","));
                            }
                        });
                        self.msgbox(errors.join("<br/>"));
                    }

                    if (btn.callback) {
                        btn.callback();
                    }
                }
            },
            "baseformtab button[ref=formClose]": {
                click: function (btn) {
                    //得到组件
                    var basetab = btn.up('baseformtab');
                    var tabPanel = btn.up("tabpanel[xtype=app-main]");

                    var tabItemId = basetab.tabItemId;
                    var tabItem = tabPanel.getComponent(tabItemId);

                    // var grid = win.funData.grid; //窗体是否有grid参数
                    // if (!Ext.isEmpty(grid)) {
                    //     grid.getStore().load(); //刷新父窗体的grid
                    // }
                    //关闭tab
                    tabPanel.remove(tabItem);
                }
            }
        }

        Ext.apply(self.ctr, btnCtr);
    }
});
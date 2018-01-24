Ext.define("core.train.courseeval.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.courseeval.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function () {
    },
    control: {
        "basegrid[xtype=courseeval.evalgrid]": {
            afterrender: function (grid, eOpts) {
                var btngridSumEval = grid.down("button[ref=gridSumEval_Tab]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                	btngridSumEval.setHidden(true);
                }
            }
        },
    	
        "basegrid[xtype=courseeval.maingrid]": {
        	afterrender: function (grid, eOpts) {
                var btnAdd = grid.down("button[ref=gridAdd_Tab]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                    btnAdd.setHidden(true);
                }
            },
            beforeitemclick: function (grid, record, item, index, e, eOpts) {
                var self = this;
                var mainLayout = grid.up("panel[xtype=courseeval.mainlayout]");
                var filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'classId'}]";
                var evalUrl = "http://" + window.location.host + "/static/traineval/classeval.jsp?classId=" + record.get("uuid");
                var classInfo = "[" + record.get("className") + "]管理评价地址：" + evalUrl;
                var funData = mainLayout.funData;
                funData = Ext.apply(funData, {
                    classId: record.get("uuid"),
                    filter: filter,
                    classEvalUrl: evalUrl,
                    classGrid: grid,
                    classRecord: record
                });
                mainLayout.funData = funData;
                //加载该评定为该星级的班级信息
                var refreshgrid = mainLayout.down("panel[xtype=courseeval.evalgrid]");
                var store = refreshgrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    classId: record.get("uuid")
                };
                store.load(); // 给form赋值
                if (record.get("isEval") === 1 ) {
                    refreshgrid.down('panel[ref=evalClassInfoPanel] label[ref=label1]').setText(classInfo);
                    var btnPreview = refreshgrid.down("button[ref=btnPreview]");
                	btnPreview.setHidden(false);
                }else if(record.get("isEval") === 2 ){
                	refreshgrid.down('panel[ref=evalClassInfoPanel] label[ref=label1]').setText("");
                	var btnPreview = refreshgrid.down("button[ref=btnPreview]");
                	btnPreview.setHidden(true);
                }else {
                    refreshgrid.down('panel[ref=evalClassInfoPanel] label[ref=label1]').setText("[" + record.get("className") + "]未开启管理评价");
                    var btnPreview = refreshgrid.down("button[ref=btnPreview]");
                	btnPreview.setHidden(true);
                }

                return false;
            }
            /*            groupclick:function  (view, node, group, e, eOpts) {
             var self = this;
             console.log(view);
             console.log(node);
             console.log(group);
             }*/
        },
        /**
         * 启动评价按钮
         * @type {[type]}
         */
        "basegrid[xtype=courseeval.maingrid] button[ref=gridAdd_Tab]": {
            beforeclick: function (btn) {
                var self = this;
                var grid = btn.up("basegrid");
                var records = grid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择一个要启动评价的班级");
                    return false;
                }
                this.doStartEval_Tab(btn, "startEval", grid, records[0]);
                return false;
            }
        },
        /**
         * 汇总评价按钮
         * @type {[type]}
         */
        "basegrid[xtype=courseeval.evalgrid] button[ref=gridSumEval_Tab]": {
            beforeclick: function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var basePanel = baseGrid.up("panel[xtype=courseeval.mainlayout]");
                var funData = basePanel.funData;
                if (Ext.isEmpty(funData.classId)) {
                    self.Warning("请选择要汇总的班级");
                    return false;
                }
                var record = baseGrid.getStore().getAt(0);
                //self.doMainGridDetail_Tab(null, "rankCourse",baseGrid,record);
                this.doSumEval_Tab(null, "sumEval", baseGrid, record);
                return false;
                /*                var self = this;
                 var grid = btn.up("basegrid");
                 var records = grid.getSelectionModel().getSelection();
                 if (records.length != 1) {
                 self.Warning("请选择一个要评价的班级");
                 return false;
                 }
                 this.doStartEval_Tab(btn, "startEval", grid, records[0]);
                 return false;*/
            }
        },
        /**
         * 列表操作列事件
         */
        "basegrid[xtype=courseeval.evalgrid]  actioncolumn": {
            //操作列预览评价表
            evalGridPreviewEvalClick_Tab: function (data) {
                var self = this;
                var baseGrid = data.view;
                var recordData = data.record.data;
                //得到组件
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

                //得到配置信息
                var funData = basePanel.funData;

                //处理特殊默认值
                var defaultObj = funData.defaultObj;
                //弹出链接地址框
                var evalUrl = "http://" + window.location.host + "/static/traineval/courseeval.jsp?courseId=" + recordData.classScheduleId;
                window.open(evalUrl);
                return false;
            },
            //操作列关闭评价
            evalGridCourseEvalResultClick_Tab: function (data) {              
                console.log(22);
                this.doCourseEvelDetail_Tab(null, data.cmd, data.view, data.record);             
                return false;
            },
            //查看地址
            mainGridEvalUrlClick_Tab: function (data) {
                var self = this;
                var baseGrid = data.view;
                var recordData = data.record.data;
                //得到组件
                var funCode = baseGrid.funCode;
                var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
                var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

                //得到配置信息
                var funData = basePanel.funData;
                var detCode = basePanel.detCode;
                var detLayout = basePanel.detLayout;
                var popFunData = Ext.apply(funData, {
                    grid: baseGrid
                });
                //关键：window的视图控制器
                var otherController = basePanel.otherController;
                if (!otherController)
                    otherController = '';

                //处理特殊默认值
                var defaultObj = funData.defaultObj;
                var insertObj = self.getDefaultValue(defaultObj);
                var itemXtype = "courseeval.evalurlform";
                //弹出链接地址框
                var evalUrl = "http://" + window.location.host + "/static/traineval/courseeval.jsp?courseId=" + recordData.classScheduleId;
                insertObj = Ext.apply(insertObj, {
                    evalUrl: evalUrl
                });
                self.openEvalUrl(detCode, otherController, popFunData, insertObj, detLayout, itemXtype);
            }
        },

        /**
         * 预览班级评价表
         */
        "basegrid[xtype=courseeval.evalgrid] button[ref=btnPreview]":{
            click:function(btn){
                var self = this;
                var mainLayout = btn.up("panel[xtype=courseeval.mainlayout]");
                var funData = mainLayout.funData;
                var classEvalUrl = funData.classEvalUrl;
                var record = funData.classRecord;
                if (record.get("isEval") === 1 || record.get("isEval") === 2) {
                    window.open(classEvalUrl);
                } else {
                    self.Warning("当前班级未开始评价，无法预览")
                }
            }
        }
    },
    /**
     * 启动评价
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     * @returns {boolean}
     */
    doStartEval_Tab: function (btn, cmd, grid, record) {
        var self = this;

        var baseGrid = grid;
        var recordData = record.data;
        //得到组件
        var funCode = baseGrid.funCode;
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });
        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);
        var itemXtype = "courseeval.evalurlform";
        //不能重复启动
        /*
        if (recordData.isEval > 0) {
            self.Warning("此培训班已启动评价，不能重复启动");
            return false;
        }
        */
        
        var title = "确定要启动对此班级的评价吗？";
        Ext.Msg.confirm('提示', title, function (btn, text) {
            if (btn == "yes") {
                Ext.Msg.wait('正在启动中,请稍后...', '温馨提示');
                var component = Ext.create('Ext.Component', {
                    title: 'HelloWorld',
                    width: 0,
                    height: 0,
                    hidden: true,
                    html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassevalresult/starteval?ids=' + recordData.uuid + '"></iframe>',
                    renderTo: Ext.getBody()
                });

                var time = function () {
                    self.syncAjax({
                        url: comm.get('baseUrl') + '/TrainClassevalresult/checkStartEnd',
                        timeout: 1000 * 60 * 30, //半个小时
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                Ext.Msg.hide();
                                self.Info(data.obj);
                                component.destroy();
                                //刷新班级列表
                                baseGrid.getStore().load();
                                //刷新班级课程列表
                                self.doRefreshEvalCourseGrid(baseGrid, record);

                                //给课程列表界面下面的班级赋值
                                var mainLayout = baseGrid.up("panel[xtype=courseeval.mainlayout]");
                                var refreshgrid = mainLayout.down("panel[xtype=courseeval.evalgrid]");
                                var evalUrl = "http://" + window.location.host + "/static/traineval/classeval.jsp?classId=" + recordData.uuid;
                                var classInfo = "[" + record.get("className") + "]管理评价地址：" + evalUrl;
                                refreshgrid.down('panel[ref=evalClassInfoPanel] label[ref=label1]').setText(classInfo);
                                //refreshgrid.down('panel[ref=evalClassInfoPanel] label[ref=label2]').setText('班级名称: ' + record.get("className"));
                                //弹出链接地址框
                                /*                                var evalUrl = "http://" + window.location.host + "/static/traineval/courseeval.jsp?courseId=" + recordData.classScheduleId;
                                 insertObj = Ext.apply(insertObj, {
                                 evalUrl: evalUrl
                                 });
                                 self.openEvalUrl(detCode, otherController, popFunData, insertObj, detLayout, itemXtype);*/
                            } else {
                                if (data.obj == 0) { //当为此值，则表明导出失败
                                    Ext.Msg.hide();
                                    self.Error("启动失败，请重试或联系管理员！");
                                    component.destroy();
                                } else {
                                    setTimeout(function () {
                                        time()
                                    }, 1000);
                                }
                            }
                        },
                        failure: function (response) {
                            Ext.Msg.hide();
                            Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                            component.destroy();
                        }
                    });
                };
                setTimeout(function () {
                    time()
                }, 1000); //延迟1秒执行
            }
        });
    },
    /**
     * 评价汇总
     * @param  {[type]} btn    [description]
     * @param  {[type]} cmd    [description]
     * @param  {[type]} grid   [description]
     * @param  {[type]} record [description]
     * @return {[type]}        [description]
     */
    doSumEval_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;

        var operType = cmd;
        var pkValue = null;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);

        //默认的tab参数
        var tabTitle = funData.tabConfig.addTitle; //标签页的标题
        var tabItemId = funCode + "_gridAdd"; //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "alleval.detailform";

        var title = "确定要启动对此班级的评价进行汇总统计吗？";
        Ext.Msg.confirm('提示', title, function (btn, text) {
            if (btn == "yes") {
                Ext.Msg.wait('正在汇总统计中,请稍后...', '温馨提示');
                var component = Ext.create('Ext.Component', {
                    title: 'HelloWorld',
                    width: 0,
                    height: 0,
                    hidden: true,
                    html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassevalresult/sumeval?ids=' + recordData.classId + '"></iframe>',
                    renderTo: Ext.getBody()
                });

                var time = function () {
                    self.syncAjax({
                        url: comm.get('baseUrl') + '/TrainClassevalresult/checkSumEnd',
                        timeout: 1000 * 60 * 30, //半个小时
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                Ext.Msg.hide();
                                self.Info(data.obj);
                                component.destroy();

                                baseGrid.getStore().load();
                                //转汇总结果显示的tab页面
                            } else {
                                if (data.obj == 0) { //当为此值，则表明导出失败
                                    Ext.Msg.hide();
                                    self.Error("汇总失败，请重试或联系管理员！");
                                    component.destroy();
                                } else {
                                    setTimeout(function () {
                                        time()
                                    }, 1000);
                                }
                            }
                        },
                        failure: function (response) {
                            Ext.Msg.hide();
                            Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                            component.destroy();
                        }
                    });
                };
                setTimeout(function () {
                    time()
                }, 1000); //延迟1秒执行
            }
        });
        return;

        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "edit":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }
                insertObj = recordData;
                tabTitle = funData.tabConfig.editTitle;
                tabItemId = funCode + "_gridEdit";
                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];
                break;
            case "detail":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }
                insertObj = recordData;
                tabTitle = funData.tabConfig.editTitle;
                tabItemId = funCode + "_gridDetail";
                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];
                break;
            case "trainrecord":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }
                insertObj = recordData;
                tabTitle = insertObj.xm + "-培训记录";
                tabItemId = funCode + "_gridTrainRecord";
                itemXtype = "alleval.trainrecordgrid";
                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            xm: insertObj.xm
        });
        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);

        //判断是否已经存在tab了
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue, //保存主键值
                layout: 'fit',
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController, //指定重写事件的控制器
                    funCode: funCode, //指定mainLayout的funcode
                    detCode: detCode, //指定detailLayout的funcode
                    tabItemId: tabItemId, //指定tab页的itemId
                    insertObj: insertObj, //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData, //保存funData数据，提供给提交事件中使用
                    items: [{
                        xtype: detLayout,
                        items: [{
                            xtype: itemXtype
                        }]
                    }]
                });
                tabItem.add(item);
                if (cmd != "trainrecord") {
                    var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();

                    self.setFormValue(formDeptObj, insertObj);
                    if (cmd == "detail")
                        self.setFuncReadOnly(funData, objDetForm, true);
                    //显示封面图片
                    objDetForm.down('image[ref=newsImage]').setSrc(insertObj.zp);
                } else {
                    var trainRecordGrid = tabItem.down("grid[xtype=alleval.trainrecordgrid]");
                    var proxy = trainRecordGrid.getStore().getProxy();
                    proxy.extraParams.filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'allevalId'}]";
                    trainRecordGrid.getStore().loadPage(1);
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) { //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },

    /**
     * 打开评价链接地址窗口
     * @param funCode
     * @param controller
     * @param funData
     * @param insertObj
     * @param detLayout
     * @param itemXtype
     */
    openEvalUrl: function (funCode, controller, funData, insertObj, detLayout, itemXtype) {
        var self = this;
        var winId = funCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                iconCls: 'x-fa fa-plus-circle',
                operType: 'detail',
                width: 800,
                height: 180,
                controller: controller, //指定视图控制器，从而能够使指定的控制器的事件生效
                funData: funData,
                funCode: funCode,
                insertObj: insertObj,
                items: [{
                    xtype: detLayout,
                    items: [{
                        xtype: itemXtype
                    }]
                }]
            });
        }
        win.show();
        var objDetForm = win.down("baseform[funCode=" + funCode + "]");
        var formDeptObj = objDetForm.getForm();

        self.setFormValue(formDeptObj, insertObj);
    },
    /**
     * 刷新班级的待评价课程信息
     * @param grid
     * @param record
     */
    doRefreshEvalCourseGrid: function (grid, record) {
        var mainLayout = grid.up("panel[xtype=courseeval.mainlayout]");
        var filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'classId'}]"
        var funData = mainLayout.funData;
        funData = Ext.apply(funData, {
            classId: record.get("classId"),
            filter: filter
        });
        mainLayout.funData = funData;

        var refreshgrid = mainLayout.down("panel[xtype=courseeval.evalgrid]");
        var store = refreshgrid.getStore();
        var proxy = store.getProxy();
        proxy.extraParams = {
            classId: record.get("uuid")
        };
        store.load(); // 给form赋值
    },

    /**
     * 
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doCourseEvelDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.getData();
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

       
        var operType = cmd;
        var pkValue = null;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
      
        //一些要传递的参数
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });


        var pkName = "classScheduleId";   //funData.pkName;    //classScheduleId
        pkValue = recordData[pkName];
        var couseName = recordData["courseName"];

        //默认的tab参数
        var tabTitle = couseName+"-评价结果"; //标签页的标题
        var tabItemId = funCode + "_gridCourseEvel"+pkValue;     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "courseeval.coursevaldetailpanel";

        
        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择1条数据！");
                return;
            }
            recordData = rescords[0].getData();
        }

        insertObj = recordData;
     
       
         
     
        //获取tabItem；若不存在，则表示·要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);

        //判断是否已经存在tab了
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,    //保存主键值
                layout: 'fit',
                margin: 5
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,         //指定重写事件的控制器
                    funCode: funCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: [{
                        xtype: detLayout,
                        items: [{
                            xtype: itemXtype
                        }]
                    }]
                });
                tabItem.add(item);

                
                self.asyncAjax({
                    url:comm.get("baseUrl") + "/TrainCourseevalresult/getCourseEvalResult",
                    params: {
                        ids:pkValue
                    },
                    method :'GET',
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                       
                        //console.log(data);
                                                                    
                        var detailhtmlpanel = tabItem.down("container[xtype=courseeval.coursevaldetailpanel]");
                        detailhtmlpanel.setData(data);
                    }
                });
                                  
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }
});
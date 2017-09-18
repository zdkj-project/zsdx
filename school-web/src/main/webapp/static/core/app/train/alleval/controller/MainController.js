Ext.define("core.train.alleval.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.alleval.mainController',
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
        /**
         * 班级列表相关事件
         */
        "basegrid[xtype=alleval.maingrid]": {
        	afterrender: function (grid, eOpts) {
                var btngridExport = grid.down("button[ref=gridExport]");
                console.log(btngridExport);
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PXPJMANGER") == -1) {
               	 btngridExport.setHidden(true);
                }
            },
            //列表点击事件
            beforeitemclick: function (grid, record, item, index, e, eOpts) {
                var self = this;

              
                var mainLayout = grid.up("panel[xtype=alleval.mainlayout]");
                
                var filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'classId'}]";
                var funData = mainLayout.funData;
                funData = Ext.apply(funData, {
                    classId: record.get("uuid"),
                    filter: filter,
                    classGrid:grid,
                    classRecord:record
                });
                mainLayout.funData = funData;
                var refreshgrid = mainLayout.down("panel[xtype=alleval.evalgrid]");
                var store = refreshgrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    classId: record.get("uuid"),
                    orderSql: " order by ranking asc "
                };
                store.load(function( records ){
                    var btngridExport = mainLayout.down("basegrid[xtype=alleval.maingrid] button[ref=gridExport]");
                    if(records.length==0){                    
                        btngridExport.setDisabled(true);                        
                    }else{
                        btngridExport.setDisabled(false);  
                    }
                   
                }); 
                return false;
            }
        },
        
        //给右侧列表导出按钮增加权限控制
        "basegrid[xtype=alleval.evalgrid]": {
        	afterrender: function (grid, eOpts) {
                var btngridExport = grid.down("button[ref=gridExport]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PXPJMANGER") == -1) {
               	 btngridExport.setHidden(true);
                }
            }
        },
        
        
        /**
         * 班级列表导出事件
         */
        "basegrid[xtype=alleval.maingrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                var title = "班级相关评价信息导出";
                //var ids = [];
                if (records.length!=1) {
                    self.Warning("只能按班级导出，请重新选择");
                    return false;
                }
                var ids = records[0].get("uuid");
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassevalresult/exportEvalExcel?ids=' + ids + '"></iframe>',
                            renderTo: Ext.getBody()
                        });

                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClassevalresult/checkEvalExportEnd?ids=' + ids,
                                timeout: 1000 * 60 * 30,        //半个小时
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
                                        component.destroy();
                                    } else {
                                        if (data.obj == 0) {    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
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
         * 列表查看课程排名
         */
        "basegrid[xtype=alleval.evalgrid] button[ref=gridCourseRank]": {
            beforeclick: function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("panel[xtype=alleval.mainlayout]");
                var funData = basePanel.funData;
                if(Ext.isEmpty(funData.classId)){
                    self.Warning("请选择培训班级");
                    return false;
                }
                var record = baseGrid.getStore().getAt(0);
                self.doMainGridDetail_Tab(null, "rankCourse",baseGrid,record);
                return false;
            }
        },
        /**
         * 列表查看班级评价
         */
        "basegrid[xtype=alleval.evalgrid] button[ref=gridClassEval]": {
            beforeclick: function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("panel[xtype=alleval.mainlayout]"); //组件容器、主键primary key
                var funData = basePanel.funData;
                if(Ext.isEmpty(funData.classId)){
                    self.Warning("请选择培训班级");
                    return false;
                }

                var classGrid=funData.classGrid;
                classGrid.funCode=funCode;
                var records = classGrid.getSelectionModel().getSelection();     
                if(records.length!=1){
                    self.Warning("请选择一个培训班级");
                    return false;
                }
                recordData = records[0];

                this.doClassEvelDetail_Tab(null, "classEval",classGrid,recordData);
                return false;
            }
        },
        
       //课程列表的导出功能
        "basegrid[xtype=alleval.evalgrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[xtype=alleval.mainlayout]");
                var classGrid = basePanel.down("basegrid[xtype=alleval.maingrid]");
                
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                var title = "课程相关评价信息导出";
                //var ids = [];
                if (records.length!=1) {
                    self.Warning("每次只能选择一门课程导出，请重新选择");
                    return false;
                }
                var ids = records[0].get("classScheduleId");
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainCourseevalresult/exportExcel?ids=' + ids + '"></iframe>',
                            renderTo: Ext.getBody()
                        });

                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainCourseevalresult/checkExportEnd?ids=' + ids,
                                timeout: 1000 * 60 * 30,        //半个小时
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
                                        component.destroy();
                                    } else {
                                        if (data.obj == 0) {    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
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
         * 列表操作列事件
         */
        "basegrid  actioncolumn": {
            //操作列启动评价
            mainGridStartEvalClick_Tab: function (data) {
                this.doStartEval_Tab(null, "edit", data.view, data.record);
                return false;
            },
            //操作列关闭评价
            mainGridEndEvalClick_Tab: function (data) {
                //this.doStartEval_Tab(null, "edit", data.view, data.record);
                var self = this;
                var title = "确定要关闭对此班级的评价吗？";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn === "yes") {
                        var resObj = self.ajax({
                            url: comm.get('baseUrl') + '/TrainClassevalresult/endeval',
                            params: {
                                ids: data.record.data.uuid
                            }
                        });
                        if (resObj.success) {
                            var store = data.view.getStore();
                            store.load();
                            self.msgbox(resObj.obj);
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });
                return false;
            },
            //操作列评价汇总
            mainGridSumEvalClick_Tab: function (data) {
                this.doSumEval_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            //操作列课程排名
            mainGridRankCourseClick_Tab: function (data) {
                this.doMainGridDetail_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            evalGridCourseDetailClick_Tab:function(data){

                this.doCourseEvelDetail_Tab(null, data.cmd, data.view, data.record);
                return false;
            }
        }
    },

    /**
     * 
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doClassEvelDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        
        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.getData();
        }
        //得到组件
        var funCode = baseGrid.funCode;
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");

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

       
        //一些要传递的参数
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });
        //console.log(recordData);
        var pkName = "uuid";   //funData.pkName;    //classScheduleId
        pkValue = recordData[pkName];
        var couseName = recordData["className"];

        var insertObj = recordData;
     
         

        //默认的tab参数
        var tabTitle = couseName+"-班级评价详情"; //标签页的标题
        var tabItemId = funCode + "_gridClassEvel"+pkValue;     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "alleval.classevaldetailpanel";

        
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
                    url:comm.get("baseUrl") + "/TrainClassevalresult/getClassEvalResult",
                    params: {
                        ids:pkValue
                    },                
                    method :'GET',
                    //回调代码必须写在里面
                    success: function(response) {
                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                       
                        //console.log(data);
                                                                    
                        var detailhtmlpanel = tabItem.down("container[xtype=alleval.classevaldetailpanel]");
                        detailhtmlpanel.setData(data);
                    }
                });
                                  
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
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
        var tabTitle = couseName+"-课程评价详情"; //标签页的标题
        var tabItemId = funCode + "_gridCourseEvel"+pkValue;     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "alleval.coursevaldetailpanel";

        
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
                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                       
                        //console.log(data);
                                                                    
                        var detailhtmlpanel = tabItem.down("container[xtype=alleval.coursevaldetailpanel]");
                        detailhtmlpanel.setData(data);
                    }
                });
                                  
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },


    doStartEval_Tab: function (btn, cmd, grid, record) {
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
                        timeout: 1000 * 60 * 30,        //半个小时
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                Ext.Msg.hide();
                                self.msgbox(data.obj);
                                component.destroy();

                                baseGrid.getStore().load();
                                //弹出链接 地址框
                            } else {
                                if (data.obj == 0) {    //当为此值，则表明导出失败
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
                }, 1000);    //延迟1秒执行
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
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
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
                    html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassevalresult/sumeval?ids=' + recordData.uuid + '"></iframe>',
                    renderTo: Ext.getBody()
                });

                var time = function () {
                    self.syncAjax({
                        url: comm.get('baseUrl') + '/TrainClassevalresult/checkSumEnd',
                        timeout: 1000 * 60 * 30,        //半个小时
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                Ext.Msg.hide();
                                self.msgbox(data.obj);
                                component.destroy();

                                //baseGrid.getStore().load();
                                //转汇总结果显示的tab页面
                            } else {
                                if (data.obj == 0) {    //当为此值，则表明导出失败
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
                }, 1000);    //延迟1秒执行
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
                itemPKV: pkValue,    //保存主键值
                layout: 'fit',
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

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },

    doMainGridDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;
        //默认值
        var insertObj = self.getDefaultValue(defaultObj);

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
            insertObj = Ext.apply(insertObj, recordData);
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
        insertObj = Ext.apply(insertObj, funData.classRecord);
        var operType = cmd;
        var pkValue = null;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //一些要传递的参数
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //默认的tab参数
        var tabTitle = funData.tabConfig.addTitle; //标签页的标题
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "course.detailform";
        var tabConfigInfo = {
            tabTitle: funData.tabConfig.addTitle,
            tabItemId: tabItemId,
            itemXtype: itemXtype
        };
        //根据操作命令组装不同的数据
        self.getTabConfig(cmd, funCode, tabConfigInfo);
        tabTitle = tabConfigInfo.tabTitle; //标签页的标题
        tabItemId = tabConfigInfo.tabItemId;     //命名规则：funCode+'_ref名称',确保不重复
        itemXtype = tabConfigInfo.itemXtype;
        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
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
                //return;
                //根据不同的操作进行初始化
                self.initDetailInfo(cmd, tabItem, insertObj);

            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },

    getTabConfig: function (cmd, funCode, tabConfigInfo) {
        switch (cmd) {
            case "rankCourse":
                tabConfigInfo.tabTitle = "课程评价排名";
                tabConfigInfo.tabItemId = funCode + "_ClassCourseRank";
                // tabConfigInfo.itemXtype = "alleval.courserankgrid";
                tabConfigInfo.itemXtype = "alleval.courserankhtml";
                break;
        }
    },
    initDetailInfo: function (cmd, tab, insertObj) {
        var self = this;
        //var filter = "[{'type':'string','comparison':'=','value':'" + record.get("indicatorId") + "','field':'indicatorId'}]";
        switch (cmd) {
            case "rankCourse":
                //初始化班级信息
/*                var rankingGrid = tab.down("grid[xtype=alleval.courserankgrid]");
                var proxy = rankingGrid.getStore().getProxy();
                proxy.extraParams.classId = insertObj.uuid;
                proxy.extraParams.limit = 0;
                rankingGrid.getStore().loadPage(1);*/

                var classContainer = tab.down("container[ref=classInfo]");
                classContainer.setData(insertObj);
                // var filter = "[{'type':'string','comparison':'=','value':'" + insertObj.uuid + "','field':'classId'}]";
                //初始化班级课程排名
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClassschedule/listClassEvalCourse",
                    params: {
                        classId: insertObj.classId,
                        orderSql: " order by ranking asc",
                        page: 1,
                        start: 0,
                        limit: 200
                    },
                    success: function (response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                        var classCourseRankContainer = tab.down("container[ref=classCourseRank]");
                        classCourseRankContainer.setData(data);
                    }
                });
                break;
        }
    },
   
      
})
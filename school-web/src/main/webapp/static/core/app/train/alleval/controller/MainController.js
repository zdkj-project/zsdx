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
         * 导入
         */
        "basegrid[xtype=alleval.maingrid] button[ref=gridImport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");

                var win = Ext.create('Ext.Window', {
                    title: "导入学员数据",
                    iconCls: 'x-fa fa-clipboard',
                    width: 400,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'alleval.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "alleval.allevalimportform"
                    }]
                });
                win.show();
                return false;
            }
        },
        /**
         * 导出
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
                var title = "将导出所有的学员信息";
                var ids = [];
                if (records.length > 0) {
                    title = "将导出所选学员的信息";
                    Ext.each(records, function (rec) {
                        var pkValue = rec.get(pkName);
                        ids.push(pkValue);
                    });

                }
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/Trainalleval/exportExcel?ids=" + ids.join(",");
                        window.open(comm.get('baseUrl') + "/Trainalleval/exportExcel?ids=" + ids.join(",") + "&orderSql= order by workUnit");
                        var inter = 100;
                        var tt = Ext.Ajax.request({
                            url: comm.get('baseUrl') + '/Trainalleval/checkExportEnd',
                            success: function (resp, opts) {
                                inter += 100;
                                var result = JSON.parse(resp.responseText);
                                if (result.success) {
                                    //Ext.Msg.hide();
                                }
                            },
                            failure: function (resp, opts) {
                                task.delay(1200);
                                var respText = Ext.util.JSON.decode(resp.responseText);
                                Ext.Msg.alert('错误', respText.error);
                            }
                        });

                        var sh;
                        sh = setInterval(tt, 1000);
                        var task = new Ext.util.DelayedTask(function () {
                            Ext.Msg.hide();
                        });
                        task.delay(5000 + parseInt(inter));
                        clearInterval(sh);

                    }
                });
                return false;
            }
        },

        /**
         * 下载模板
         */
        "basegrid[xtype=alleval.maingrid] button[ref=gridDownTemplate]": {
            beforeclick: function (btn) {
                var self = this;
                var title = "下载导入模板";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        window.location.href = comm.get('baseUrl') + "/static/upload/template/alleval.xls";
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
            }
        }
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

        var categoryId = funData.categoryId;  //选择的分类ID
        var categoryName = funData.categoryName; //选择的分类名称
        var categoryCode = funData.categoryCode; //选择的分类 的编码

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
        /*        if (cmd != "add") {
         if (btn) {
         var rescords = baseGrid.getSelectionModel().getSelection();
         if (rescords.length != 1) {
         self.msgbox("请选择1条数据！");
         return;
         }
         recordData = rescords[0].data;
         }

         insertObj = recordData;
         //获取主键值
         var pkName = funData.pkName;
         pkValue = recordData[pkName];
         switch (cmd) {
         case "edit":
         tabTitle = funData.tabConfig.editTitle;
         tabItemId = funCode + "_gridEdit";
         break
         case "courseEval":
         tabTitle = "课程评价_" + insertObj.courseName;
         tabItemId = funCode + "_gridCourseEval";
         itemXtype = "course.courseevalpanel";
         break;
         case"coursDesc":
         tabTitle = "课程简介_" + insertObj.courseName;
         tabItemId = funCode + "_gridCourseDesc";
         itemXtype = "course.coursedescpanel";
         break;
         case "teacherDesc":
         tabTitle = "教师简介_" + insertObj.courseName;
         tabItemId = funCode + "_gridDeacherDesc";
         itemXtype = "course.detailhtmlpanel";
         break
         }
         }*/
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
                /*                switch (cmd) {
                 case "edit":
                 var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                 var formDeptObj = objDetForm.getForm();
                 var courseDesc = objDetForm.down("htmleditor");

                 self.setFormValue(formDeptObj, insertObj);
                 courseDesc.setValue(insertObj.courseDesc);
                 if (insertObj.courseMode === 2) {
                 formDeptObj.findField("courseMode").setValue(true);
                 } else
                 formDeptObj.findField("courseMode").setValue(false);
                 //根据需要设置一些字段为只读
                 formDeptObj.findField("courseName").setDisabled(true);
                 formDeptObj.findField("courseMode").setDisabled(true);
                 formDeptObj.findField("mainTeacherName").setDisabled(true);
                 break;
                 case "coursDesc":
                 var detailhtmlpanel = item.down("container[xtype=course.coursedescpanel]");
                 detailhtmlpanel.setData(recordData);
                 detailhtmlpanel.show();
                 break;
                 case "teacherDesc":

                 // var ddCode="XLM";
                 // var store=Ext.create("Ext.data.Store",{
                 //     fields:factory.ModelFactory.getFields({modelName:"com.zd.school.plartform.baseset.model.BaseDicitem",excludes:""}),
                 //     data:factory.DDCache.getItemByDDCode(ddCode)
                 // });
                 self.asyncAjax({
                 url: funData.action + "/courseteacher",
                 params: {
                 courseId: insertObj.uuid
                 },
                 //回调代码必须写在里面
                 success: function (response) {
                 data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                 //console.log(data);
                 if (data.rows != undefined) {
                 //将数据字典数据赋值到组件属性上
                 //var storeData=store.getData();
                 // for(var i=0;i<rescordsData.length;i++){
                 //     for(var j=0;j<storeData.length;j++){
                 //         var rec=storeData.items[j];
                 //         if(rec.get("itemCode")==rescordsData[i].xlm){
                 //             rescordsData[i].xlm=rec.get("itemName");
                 //             break;
                 //         }
                 //     }
                 // }
                 var rescordsData = data.rows;
                 var obj = [];
                 for (var index = 0; index < rescordsData.length; index++) {
                 var recordData = rescordsData[index];
                 var ddCodes = ['XBM', 'TECHNICAL', 'XLM', 'ZYM', 'INOUT', 'HEADSHIPLEVEL'];
                 var propNames = ['xbm', 'technical', 'xlm', 'zym', 'inout', 'headshipLevel'];
                 for (var i = 0; i < ddCodes.length; i++) {
                 var ddItem = factory.DDCache.getItemByDDCode(ddCodes[i]);
                 var resultVal = "";
                 var value = recordData[propNames[i]];
                 for (var j = 0; j < ddItem.length; j++) {
                 var ddObj = ddItem[j];
                 if (value == ddObj["itemCode"]) {
                 resultVal = ddObj["itemName"];
                 break;
                 }
                 }
                 recordData[propNames[i]] = resultVal;
                 }
                 obj.push(recordData);
                 }

                 var detailhtmlpanel = tabItem.down("container[xtype=course.detailhtmlpanel]");
                 detailhtmlpanel.setData(obj);
                 detailhtmlpanel.show();


                 } else {
                 self.Error(data.obj ? data.obj : "数据读取失败，请刷新页面！");
                 }
                 }
                 });
                 break;
                 case "courseEval":
                 var trainRecordGrid = tabItem.down("grid[xtype=course.courseevalpanel]");
                 var proxy = trainRecordGrid.getStore().getProxy();
                 proxy.extraParams.propName = "courseId,courseName";
                 proxy.extraParams.propValue = record.get("uuid") + "," + record.get("courseName");
                 proxy.extraParams.joinMethod = "or";
                 trainRecordGrid.getStore().loadPage(1);
                 break;
                 }*/
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
                tabConfigInfo.itemXtype = "alleval.courserankgrid";
                // tabConfigInfo.itemXtype = "alleval.courserankhtml";
                break;
            case "courseEval":
                tabTitle = "课程评价_" + insertObj.courseName;
                tabItemId = funCode + "_gridCourseEval";
                itemXtype = "alleval.courserankhtml";
                break;
            case"coursDesc":
                tabTitle = "课程简介_" + insertObj.courseName;
                tabItemId = funCode + "_gridCourseDesc";
                itemXtype = "course.coursedescpanel";
                break;
            case "teacherDesc":
                tabTitle = "教师简介_" + insertObj.courseName;
                tabItemId = funCode + "_gridDeacherDesc";
                itemXtype = "course.detailhtmlpanel";
                break
        }
    },
    initDetailInfo: function (cmd, tab, insertObj) {
        var self = this;
        //var filter = "[{'type':'string','comparison':'=','value':'" + record.get("indicatorId") + "','field':'indicatorId'}]";
        switch (cmd) {
            case "rankCourse":
                //初始化班级信息
                var rankingGrid = tab.down("grid[xtype=alleval.courserankgrid]");
                var proxy = rankingGrid.getStore().getProxy();
                proxy.extraParams.classId = insertObj.uuid;
                proxy.extraParams.limit = 0;
                rankingGrid.getStore().loadPage(1);

/*                var classContainer = tab.down("container[ref=classInfo]");
                classContainer.setData(insertObj);
                var filter = "[{'type':'string','comparison':'=','value':'" + insertObj.uuid + "','field':'classId'}]";
                //初始化班级课程排名
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClassschedule/listClassEvalCourse",
                    params: {
                        classId: insertObj.uuid,
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
                });*/
                break;
        }
    }
})
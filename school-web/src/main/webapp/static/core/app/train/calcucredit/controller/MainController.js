Ext.define("core.train.calcucredit.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.calcucredit.mainController',
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
        "basegrid[xtype=calcucredit.traineesgrid]": {
            afterrender: function (grid, eOpts) {
                var btngridSumcredit = grid.down("button[ref=gridSumcredit]");
                var btngridExport = grid.down("button[ref=gridExport]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                	btngridSumcredit.setHidden(true);
                	btngridExport.setHidden(true);
                }
            }
        },
    	
        /**
         * 班级列表相关事件
         */
        "basegrid[xtype=calcucredit.maingrid]": {
        	afterrender: function (grid, eOpts) {
                var btngridExport = grid.down("button[ref=gridExport]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                	btngridExport.setHidden(true);
                }
            },
            //列表点击事件
            beforeitemclick: function (grid, record, item, index, e, eOpts) {
                var self = this;
                var mainLayout = grid.up("panel[xtype=calcucredit.mainlayout]");
                var filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'classId'}]";
                var funData = mainLayout.funData;
                funData = Ext.apply(funData, {
                    classId: record.get("uuid"),
                    filter: filter,
                    classGrid: grid,
                    classRecord: record
                });
                mainLayout.funData = funData;
                var refreshgrid = mainLayout.down("panel[xtype=calcucredit.traineesgrid]");
                var store = refreshgrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    classId: record.get("uuid"),
                    orderSql: " order by createTime asc ",
                    filter: filter
                };
                store.load(); // 给form赋值
                return false;
            }
        },
        /**
         * 班级列表导出按钮事件
         */
        "basegrid[xtype=calcucredit.maingrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("只能按班级导出，请重新选择");
                    return false;
                }
                var record = records[0].data;
                var title = "确定要导出此班级学员的学分信息吗？";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClasstrainee/exportCredit?ids=' + record.uuid + '"></iframe>',
                            renderTo: Ext.getBody()
                        });

                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClasstrainee/checkExportCreditEnd',
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
                        };
                        setTimeout(function () {
                            time()
                        }, 1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },
        /**
         * 学员列表汇总学分
         */
        "basegrid[xtype=calcucredit.traineesgrid] button[ref=gridSumcredit]": {
            beforeclick: function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var basePanel = baseGrid.up("panel[xtype=calcucredit.mainlayout]");
                var funData = basePanel.funData;
                if (Ext.isEmpty(funData.classId)) {
                    self.Warning("请选择培训班级");
                    return false;
                }
                var record = baseGrid.getStore().getAt(0);
                self.doSumTraineesCredit(null, "sumCredit", baseGrid, record);
                return false;
            }
        },
        /**
         * 学员列表操作列事件
         */
        "basegrid[xtype=calcucredit.traineesgrid]  actioncolumn": {
            gridTraineeCreditClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doTranieeDetail_Tab(null, cmd, baseGrid, record);

                return false;
            }
        },
        /**
         * 学分列表导出按钮事件
         */
        "basegrid[xtype=calcucredit.traineesgrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var records = baseGrid.getSelectionModel().getSelection();
                //获取班级ID
                var mainlayout=baseGrid.up("panel[xtype=calcucredit.mainlayout]");
                var mainGrid=mainlayout.down("panel[xtype=calcucredit.maingrid]");
                var records1=mainGrid.getSelectionModel().getSelection();
                var classId = records1[0].data.uuid;
                var ids=records[0].data.uuid;
                if (records.length != 1) {
                    self.Warning("请选择一名学员");
                    return false;
                }
                var record = records[0].data;
                var title = "确定要导出此学员的学分信息吗？";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClasstrainee/getCreditsexportExcel?ids=' + ids +'&classId='+classId+'&classId='+classId+'&classId='+classId+'&classId='+classId+'&classId='+classId+'&classId='+classId+'&classId='+classId+'"></iframe>',
                            renderTo: Ext.getBody()
                        });

                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClasstrainee/getCreditscheckExportEnd',
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
                        };
                        setTimeout(function () {
                            time()
                        }, 1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },
    },
    /**
     * 学分汇总
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doSumTraineesCredit: function (btn, cmd, grid, record) {
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
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

        var title = "确定要汇总此班级学员的学分吗？";
        Ext.Msg.confirm('提示', title, function (btn, text) {
            if (btn == "yes") {
                Ext.Msg.wait('正在汇总统计中,请稍后...', '温馨提示');
                var component = Ext.create('Ext.Component', {
                    title: 'HelloWorld',
                    width: 0,
                    height: 0,
                    hidden: true,
                    html: '<iframe src="' + comm.get('baseUrl') + '/TrainClass/sumcredit?ids=' + recordData.classId + '"></iframe>',
                    renderTo: Ext.getBody()
                });

                var time = function () {
                    self.syncAjax({
                        url: comm.get('baseUrl') + '/TrainClass/checkSumCreditEnd',
                        timeout: 1000 * 60 * 30,        //半个小时
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                Ext.Msg.hide();
                                self.msgbox(data.obj);
                                component.destroy();

                                baseGrid.getStore().load();
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
    },

    /**
     * 查看学员的学分详细情况
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doTranieeDetail_Tab: function (btn, cmd, grid, record) {
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
        var tabTitle = recordData.xm + "-学分详情"; //标签页的标题
        var tabItemId = funCode + "_gridDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "calcucredit.detailhtml";

        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "creditDetail": //学分详情
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }
                 //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

                insertObj = recordData;
                tabTitle = recordData.xm + "-学分详情";
                tabItemId = funCode + "_gridDetail"+pkValue;
               
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
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
                layout: 'fit'
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
                var classTraineeContainer = tabItem.down("container[ref=classTraineeInfo]");
                classTraineeContainer.setData(insertObj);
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClasstrainee/getCreditsDetail",
                    params: {
                        ids: recordData.uuid
                    },
                    success: function (response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                        var indicatorStandContainer = tabItem.down("container[ref=classTraineeCredits]");
                        indicatorStandContainer.setData(data);
                    }
                });
/*                if (cmd != "trainrecord") {
                    var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();

                    self.setFormValue(formDeptObj, insertObj);
                    if (cmd == "detail")
                        self.setFuncReadOnly(funData, objDetForm, true);
                    //显示封面图片
                    objDetForm.down('image[ref=newsImage]').setSrc(insertObj.zp);
                } else {
                    var trainRecordGrid = tabItem.down("grid[xtype=trainee.trainrecordgrid]");
                    var proxy = trainRecordGrid.getStore().getProxy();
                    proxy.extraParams.filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'traineeId'}]";
                    trainRecordGrid.getStore().loadPage(1);
                }*/
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }

});
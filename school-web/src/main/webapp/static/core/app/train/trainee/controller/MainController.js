Ext.define("core.train.trainee.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.trainee.mainController',
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
        "basegrid[xtype=trainee.maingrid] button[ref=gridImport]": {
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
                    controller: 'trainee.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "trainee.traineeimportform"
                    }]
                });
                win.show();
                return false;
            }
        },
        /**
         * 导出
         */
        "basegrid[xtype=trainee.maingrid] button[ref=gridExport]": {
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
                var ids = new Array();
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
                        //window.location.href = comm.get('baseUrl') + "/TrainTrainee/exportExcel?ids=" + ids.join(",");
                        window.open(comm.get('baseUrl') + "/TrainTrainee/exportExcel?ids=" + ids.join(",") + "&orderSql= order by workUnit");
                        var inter = 100;
                        var tt = Ext.Ajax.request({
                            url: comm.get('baseUrl') + '/TrainTrainee/checkExportEnd',
                            success: function (resp, opts) {
                                inter+=100;
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
        "basegrid[xtype=trainee.maingrid] button[ref=gridDownTemplate]": {
            beforeclick: function (btn) {
                var self = this;
                var title = "下载导入模板";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        window.location.href = comm.get('baseUrl') + "/static/upload/template/trainee.xls";
                    }
                });
                return false;
            }
        },
        /**
         * 学员列表操作列事件
         */
        "basegrid  actioncolumn": {
            //操作列编辑
            editClick_Tab: function (data) {
                this.doTeacherDetail_Tab(null, "edit", data.view, data.record);
                return false;
            },
            //操作列详细
            detailClick_Tab: function (data) {
                this.doTeacherDetail_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            //操作列删除
            deleteClick: function (data) {
            },
            //操作列培训记录
            trainRecordClick_Tab: function (data) {
                this.doTeacherDetail_Tab(null, data.cmd, data.view, data.record);

                return false;
            },
        }
    },

    doTeacherDetail_Tab: function (btn, cmd, grid, record) {
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
        var itemXtype = "trainee.detailform";

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
                itemXtype = "trainee.trainrecordgrid";
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
                    var trainRecordGrid = tabItem.down("grid[xtype=trainee.trainrecordgrid]");
                    var proxy = trainRecordGrid.getStore().getProxy();
                    proxy.extraParams.filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'traineeId'}]";
                    trainRecordGrid.getStore().loadPage(1);
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }
});
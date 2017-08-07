Ext.define("core.oa.meeting.checkresult.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.checkresult.mainController',
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
         * 导考勤结果信息
         */
        "basegrid button[ref=gridExport]": {
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
                var title = "将导出所有的会议信息";
                var ids = new Array();
                if (records.length > 0) {
                    title = "将导出所选会议的信息";
                    Ext.each(records, function (rec) {
                        var pkValue = rec.get(pkName);
                        ids.push(pkValue);
                    });

                }

                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/OaMeeting/exportMeetingExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });


                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/OaMeeting/checkExportMeetingEnd',
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


        "basegrid  actioncolumn": {
            //查看考勤情况
            checkResultClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doDetail_Tab(null, cmd, baseGrid, record);
                return false;
            }
        }
    },
    /**
     * 详细操作的处理
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
            var rescords = baseGrid.getSelectionModel().getSelection();
            var indexAdd = cmd.IndexOf("add");
            if (indexAdd != -1) {
                //不是增加的操作，只能选择一条记录
                if (rescords.length != 1) {
                    self.msgbox("请选择1条数据！");
                    return;
                }
                recordData = rescords[0].getData();
            }
        } else {
            baseGrid = grid;
            recordData = record.getData();
        }

        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        var insertObj = self.getDefaultValue(defaultObj);

        var tabTitle = funData.tabConfig.addTitle;
        var tabItemId = funCode + "_gridDetail";
        var pkName = funData.pkName;
        var pkValue = null;
        var operType = cmd;
        var itemXtype = "checkresult.detailform";
        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "checkResult": //参会人员
                insertObj = Ext.apply(insertObj, recordData);
                tabTitle = "会议考勤结果";
                tabItemId = funCode + "_gridcheckResult";
                itemXtype = "checkresult.checkresultgrid";
                //获取主键值
                pkValue = recordData[pkName];
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,
                layout: 'fit'
            });
            tabPanel.add(tabItem);

            setTimeout(function () {
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,
                    funCode: funCode,
                    detCode: detCode,
                    tabItemId: tabItemId,
                    insertObj: insertObj,
                    funData: popFunData,
                    items: [{
                        xtype: detLayout,
                        funCode: detCode,
                        items: [{
                            xtype: itemXtype
                        }]
                    }]
                });

                tabItem.add(item);
                //根据需要初始化详情页面数据
                switch (cmd) {
                    case "checkResult":
                        var filter = "[{'type':'string','value':'" + insertObj.uuid + "','field':'meetingId','comparison':'='}]";
                        var meetingUserGrid = tabItem.down("basegrid[xtype=checkresult.checkresultgrid]");
                        var proxy = meetingUserGrid.getStore().getProxy();
                        proxy.extraParams.filter = filter;
                        meetingUserGrid.getStore().load();
                        break;
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue && cmd != "detail") {
            self.Warning("你已经打开一个编辑窗口了");
            return;
        }
        tabPanel.setActiveTab(tabItem);
    }
});
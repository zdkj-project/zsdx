// noinspection JSAnnotator
Ext.define("core.train.coursechkresult.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursechkresult.mainController',
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
        "basegrid[xtype=coursechkresult.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btngridAnalyze = grid.down("button[ref=gridAnalyze]");
                var btngridExport = grid.down("button[ref=gridExport]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                    btngridAnalyze.setHidden(true);
                    btngridExport.setHidden(true);
                }
            }
        },

        "basegrid  actioncolumn": {
            detailClick_Tab: function (data) {
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
        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "detail":
                insertObj = Ext.apply(insertObj, recordData);
                tabTitle = funData.tabConfig.detailTitle;
                tabItemId = funCode + "_gridDetail";
                itemXtype = "meetinginfo.DetailPanel";
                //获取主键值
                pkValue = recordData[pkName];
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            classId:recordData["uuid"]
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
                        funCode: detCode
                    }]
                });

                tabItem.add(item);
                //根据需要初始化详情页面数据
                switch (cmd) {
                    case "detail":
                        var detailPanel = item.down("basepanel[xtype=" + detLayout + "]");
                        var courseGrid = detailPanel.down("basegrid[xtype=coursechkresult.classcoursegrid]");
                        var courseStore = courseGrid.getStore();
                        var coursseProxy = courseStore.getProxy();
                        var filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + pkValue + "\",\"field\":\"classId\"}]";
                        coursseProxy.extraParams = {
                            filter: filter
                        };
                        courseStore.loadPage(1);
                        /*                        var detailhtmlpanel = item.down("container[xtype=meetinginfo.DetailPanel]");
                                                //处理数据字典的值
                                                var ddItem = factory.DDCache.getItemByDDCode("MEETINGCATEGORY");
                                                var resultVal = "";
                                                var value = recordData["meetingCategory"];
                                                for (var i = 0; i < ddItem.length; i++) {
                                                    var ddObj = ddItem[i];
                                                    if (value == ddObj["itemCode"]) {
                                                        resultVal = ddObj["itemName"];
                                                        break;
                                                    }
                                                }
                        /!*                        recordData.meetingCategory = resultVal;
                                                recordData.needChecking = recordData.needChecking == 1 ? "需要考勤" : "不考勤";
                                                detailhtmlpanel.setData(recordData);*!/*/
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
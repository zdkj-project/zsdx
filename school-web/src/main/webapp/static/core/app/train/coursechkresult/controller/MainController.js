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
                var btngridExport = grid.down("button[ref=gridExport]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                    btngridExport.setHidden(true);
                }
            }
        },

        "basegrid[xtype=coursechkresult.maingrid] button[ref=gridDetail_Tab]": {
           beforeclick: function (btn) {
                this.doDetail_Tab(btn, "detail");
                return false;
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
        },
        
        
        "basegrid[xtype=coursechkresult.maingrid] button[ref=gridExport]": {
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
                if (records.length != 1) {
                    self.Warning("请选择一个班进行导出操作！");
                    return;
                }

                var rec = records[0];

                var title = "确定要导出【" + rec.get("className") + "】考勤信息吗？";

                var ids = new Array();
                var pkValue = rec.get(pkName);
                ids.push(pkValue);
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: "HelloWord",
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClass/exportCourseCheckExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });


                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClass/checkCourseCheckExcelEnd',
                                timeout: 1000 * 60 * 30,        //半个小时
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                        Ext.Msg.hide();
                                        self.Info(data.obj);
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
            // var indexAdd = cmd.indexOf("add");
            // if (indexAdd != -1) {
                ////不是增加的操作，只能选择一条记录
                if (rescords.length != 1) {
                    self.msgbox("请选择1条数据！");
                    return;
                }
                recordData = rescords[0].getData();
            //}
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
                tabTitle = recordData["className"] +"_"+funData.tabConfig.detailTitle;

                //获取主键值
                pkValue = recordData[pkName];

                tabItemId = funCode + "_gridDetail"+pkValue;
                itemXtype = "meetinginfo.DetailPanel";
             
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

                        //设置表格的默认filter为这个有classId的filter，使得快速查询正常使用
                        courseGrid.extParams.filter=filter;
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
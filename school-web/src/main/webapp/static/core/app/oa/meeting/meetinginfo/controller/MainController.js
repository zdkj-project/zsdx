Ext.define("core.oa.meeting.meetinginfo.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.meetinginfo.mainController',
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
    	"basegrid[xtype=meetinginfo.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btngridExport = grid.down("button[ref=gridExport]");
                var btnsync = grid.down("button[ref=sync]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("HYKQMANAGER") == -1) {
                	btngridExport.setHidden(true);
                	btnsync.setHidden(true);
                }
            }
        },
    	
        /**
         * 会议列表grid加载后根据权限控制按钮的显示
         */
        "basegrid[xtype=meetinginfo.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btnAdd = grid.down("button[ref=gridExport]");
                var btnDelete = grid.down("button[ref=sync]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("HYKQMANAGER") == -1) {
                    btnAdd.setHidden(true);
                    btnDelete.setHidden(true);
                }
            }
        },
        /**
         * 从OA同步会议按钮事件
         */
        "panel[xtype=meetinginfo.maingrid] button[ref=sync]": {
            beforeclick: function (btn) {
                var self = this;
                var title = "确定从OA同步会议信息吗？";
                Ext.Msg.confirm('提示', title, function (btn2, text) {
                    if (btn2 == "yes") {
                        Ext.Msg.wait('正在同步中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/meetingsync/meeting"></iframe>',
                            renderTo: Ext.getBody()
                        });

                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/meetingsync/checkSyncEnd',
                                timeout: 1000 * 60 * 30,        //半个小时
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
                                        component.destroy();

                                        btn.up("basegrid").getStore().loadPage(1);
                                    } else {
                                        setTimeout(function () {
                                            time()
                                        }, 1000);
                                    }
                                },
                                failure: function (response) {
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                    clearInterval(interval)
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
         * 导出按钮事件
         */
        "basegrid[xtype=meetinginfo.maingrid] button[ref=gridExport]": {
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

        /**
         * 操作列事件
         */
        "basegrid  actioncolumn": {
            //编辑
            editClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doDetail_Tab(null, cmd, baseGrid, record);
                return false;
            },
            // 详细
            detailClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doDetail_Tab(null, cmd, baseGrid, record);
                return false;
            },
            // 会议人员
            meetingUserClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doDetail_Tab(null, cmd, baseGrid, record);
                return false;
            },
            //上传图片
            imageDetailClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doImageDetail_Tab(null, cmd, baseGrid, record);

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
        var itemXtype = "meetinginfo.detailform";
        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "edit":
                insertObj = Ext.apply(insertObj, recordData);
                tabTitle = recordData["meetingTitle"]+"_"+funData.tabConfig.editTitle;
                tabItemId = funCode + "_gridEdit";
                //获取主键值
                pkValue = recordData[pkName];
                break;
            case "detail":
                insertObj = Ext.apply(insertObj, recordData);

                 //获取主键值
                pkValue = recordData[pkName];

                tabTitle = recordData["meetingTitle"]+"_"+funData.tabConfig.detailTitle;
                tabItemId = funCode + "_gridDetail"+pkValue;
                itemXtype = "meetinginfo.DetailPanel";
               
                break;
            case "meetingEmp": //参会人员
                insertObj = Ext.apply(insertObj, recordData);
                tabTitle = recordData["meetingTitle"]+"_"+"参会人员";
                tabItemId = funCode + "_gridMeetingUser";
                itemXtype = "meetinginfo.meetingusergrid";
                //获取主键值
                pkValue = recordData[pkName];
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            uuid:recordData.uuid
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
                var filter="[{'type':'string','value':'"+insertObj.uuid+"','field':'meetingId','comparison':'='}]";
                switch (cmd) {
                    case "edit":
                        var objDetailForm = item.down("baseform[funCode=" + detCode + "]");
                        var formDetailObj = objDetailForm.getForm();
                        self.setFormValue(formDetailObj, insertObj);
                        formDetailObj.findField("mettingEmpname").setVisible(false);
                        break;
                    case "detail":
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
                        recordData.meetingCategory = resultVal;
                        recordData.needChecking = recordData.needChecking == 1 ? "需要考勤" : "不考勤";
                        var meetingInfoContainer = tabItem.down("container[ref=meetingInfo]");
                        meetingInfoContainer.setData(recordData);
                        self.asyncAjax({
                            url: comm.get('baseUrl') + "/OaMeetingemp/list",
                            params: {
                                filter: filter,
                                page: 1,
                                start: 0,
                                limit: 0
                            },
                            success: function (response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                var meetingUserContainer = tabItem.down("container[ref=meetingUsers]");
                                meetingUserContainer.setData(data);
                            }
                        });
                        break;
                    case "meetingEmp":
                        //var filter="[{'type':'string','value':'"+insertObj.uuid+"','field':'meetingId','comparison':'='}]";
                        var meetingUserGrid = tabItem.down("basegrid[xtype=meetinginfo.meetingusergrid]");
                        var proxy = meetingUserGrid.getStore().getProxy();
                        proxy.extraParams.filter = filter;
                        meetingUserGrid.getStore().load();
                        break;
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue && cmd!="detail") {
            self.Warning("你已经打开一个编辑窗口了");
            return;
        }
        tabPanel.setActiveTab(tabItem);
    },


    doImageDetail_Tab: function (btn, cmd, grid, record) {
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
        var tabItemId = funCode + "_gridImageDetail";
        var pkName = funData.pkName;
        var pkValue = null;
        var operType = cmd;
        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "detail":
                insertObj = Ext.apply(insertObj, recordData);
                tabTitle = insertObj.meetingTitle+"-考勤图片";
                //获取主键值
                pkValue = recordData[pkName];

                tabItemId = funCode + "_gridImageDetail"+pkValue;

                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            meetingId:pkValue
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
                            xtype: "meetinginfo.imagesgrid"
                        }]
                    }]
                });

                tabItem.add(item);
                //根据需要初始化详情页面数据
                switch (cmd) {
                    case "detail":
                        var detailPanel = item.down("basepanel[xtype=" + detLayout + "]");
                        var imageView =  detailPanel.down("panel[xtype=meetinginfo.imagesgrid] dataview");
                        var imageStore = imageView.getStore();
                        var imageProxy = imageStore.getProxy();
                        var filter = "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"" + pkValue + "\",\"field\":\"recordId\"}"+
                                    ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"OaMeeting\",\"field\":\"entityName\"}"+
                                    ",{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"jpg\",\"field\":\"attachType\"}"+
                                "]";
                        imageProxy.extraParams = {
                            filter: filter
                        };
                        imageStore.loadPage(1);
                        
                        //在这里绑定stroe
                        var pagetool = detailPanel.down("pagingtoolbar");
                        pagetool.setStore(imageStore);
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
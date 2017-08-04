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
/*                var resObj = this.ajax({
                    url: "/meetingsync" + "/meeting"
                });
                if (resObj.success) {
                    this.msgbox("同步成功!");
                    btn.up("basegrid").getStore().load();
                } else {
                    //self.msgbox("同步成功!");
                    this.msgbox("同步失败！");
                }*/
                return false;
            }
        },

        //导入
        "basegrid[xtype=meetinginfo.maingrid] button[ref=gridImport]": {
            beforeclick: function (btn) {
                var self = this;

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级

                //得到组件
                var baseGrid = btn.up("basegrid");

                var win = Ext.create('Ext.Window', {
                    title: "导入会议数据",
                    iconCls: 'x-fa fa-clipboard',
                    width: 400,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'meetinginfo.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "meetinginfo.meetingimportform"
                    }]
                });
                win.show();
                return false;
            }
        },

        /**
         * 导出
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
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/OaMeeting/exportMeetingExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/OaMeeting/checkExportMeetingEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
                                        component.destroy();                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                
                return false;
            }
        },

        /**
         * 增加按钮事件
         */
        "basegrid button[ref=gridAdd_Tab]": {
            beforeclick: function (btn) {
                console.log(btn);
                //return false;
            }
        },

        /**
         * 操作列事件
         */
        "basegrid  actioncolumn": {
            editClick: function (data) {
                console.log(data);

            },
            detailClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                this.doDetail_Tab(null, "detail", baseGrid, record);
                return false;
            },
            deleteClick: function (data) {
                console.log(data);
            },
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
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });


        var tabTitle = funData.tabConfig.addTitle;
        var tabItemId = funCode + "_gridDetail";
        var pkValue = null;
        var operType = cmd;
        switch (cmd) {
            case "detail":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].getData();
                }
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

                insertObj = recordData;
                tabTitle = funData.tabConfig.detailTitle;
                tabItemId = funCode + "_gridDetail" + insertObj.uuid;
                operType = "Detail";
                break;
        }


        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,
                layout: 'fit',
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
                            xtype: "meetinginfo.DetailPanel"
                        }]
                    }]
                });

                tabItem.add(item);


                var detailhtmlpanel = item.down("container[xtype=meetinginfo.DetailPanel]");

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
                detailhtmlpanel.setData(recordData);

            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {
            self.Warning("你已经打开一个编辑窗口了");
            return;
        }
        tabPanel.setActiveTab(tabItem);
    },
});
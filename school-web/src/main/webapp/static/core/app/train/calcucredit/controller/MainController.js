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
    init: function() {
    },
    control: {
        /**
         * 班级列表相关事件
         */
        "basegrid[xtype=calcucredit.maingrid]": {
            //列表点击事件
            beforeitemclick: function (grid, record, item, index, e, eOpts) {
                var self = this;
                var mainLayout = grid.up("panel[xtype=calcucredit.mainlayout]");
                var filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'classId'}]";
                var funData = mainLayout.funData;
                funData = Ext.apply(funData, {
                    classId: record.get("uuid"),
                    filter: filter,
                    classGrid:grid,
                    classRecord:record
                });
                mainLayout.funData = funData;
                var refreshgrid = mainLayout.down("panel[xtype=calcucredit.traineesgrid]");
                var store = refreshgrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    classId: record.get("uuid"),
                    orderSql: " order by createTime asc ",
                    filter:filter
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
                if(records.length!=1){
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
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClass/exportCredit?ids=' + record.uuid + '"></iframe>',
                            renderTo: Ext.getBody()
                        });

                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClass/checkExportCreditEnd',
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
                if(Ext.isEmpty(funData.classId)){
                    self.Warning("请选择培训班级");
                    return false;
                }
                var record = baseGrid.getStore().getAt(0);
                self.doSumTraineesCredit(null, "sumCredit",baseGrid,record);
                return false;
            }
        }
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
    }
});
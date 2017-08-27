Ext.define("core.oa.roomterminal.controller.MainController", {
    extend: "Ext.app.ViewController",
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        treeUtil: "core.util.TreeUtil",
        gridActionUtil: "core.util.GridActionUtil"
    },
    
    alias: 'controller.roomterminal.roomterminalController',
    
/*    views: [
        "core.oa.roomterminal.view.mainLayout",
        "core.oa.roomterminal.view.roomTree",
        "core.oa.roomterminal.view.listGrid",
        "core.oa.roomterminal.view.detailLayout",
        "core.oa.roomterminal.view.selectGrid",
        "core.oa.roomterminal.view.roomForm"
        //"core.oa.roomterminal.view.FormLayout",
        // "core.oa.roomterminal.view.GridLayout",
        // "core.oa.roomterminal.view.roomterminalGrid",
        // "core.oa.roomterminal.view.roomterminalForm"
    ],*/
/*    stores: [
        "core.oa.roomterminal.store.roomTreeStore"
    ],*/
    init: function() {
        var self = this
        this.control({

            "panel[xtype=roomterminal.roomtree]": {
                itemclick: function (tree, record, item, index, e) {
                    var self = this;
                    var mainLayout = tree.up("panel[xtype=roomterminal.mainlayout]");
                    var treePanel = mainLayout.down("panel[xtype=roomterminal.roomtree]");
                    //var filter = "[{'type':'string','comparison':'=','value':'" + record.get("id") + "','field':'roomId'}]";
                    var funData = mainLayout.funData;
                    var roomId = record.get("id");
                    var map = self.eachChildNode(record);
                    var ids = new Array();
                    map.eachKey(function (key) {
                        ids.push (key);
                    });
                    var filter = "[{'type':'string','comparison':'in','value':'" + ids.join(",") + "','field':'roomId'}]";
                    mainLayout.funData = Ext.apply(funData, {
                        roomId: record.get("id"),
                        roomLevel: record.get("level"),
                        roomName: record.get("text"),
                        roomInfo: record,
                        filter: filter
                    });
                    // 加载房间配置的终端
                    var storeyGrid = mainLayout.down("panel[xtype=roomterminal.listgrid]");
                    var store = storeyGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        filter: filter,
                        page: 1
                    };
                    store.load(); // 给form赋值

                    return false;
                }
            },

            /**
             * 分配按钮事件响应,此处拦截了公用的增加事件
             * @type {[type]}
             */
            "basegrid[funCode=roomterminal_main] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    self.doDetail(btn, "addReturn");
                    return false;
                }
            },
            /**
             * 取消按钮事件响应,此处拦截了公用的删除事件
             * @type {[type]}
             */
            "basegrid[funCode=roomterminal_main] button[ref=gridDelete]": {
                beforeclick: function(btn) {
                    self.doDelete(btn, "delete");
                    return false;
                }
            },
            "basegrid[funCode=roomterminal_main] button[ref=exportExcel]": {
                beforeclick: function(btn) {
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
                    var title = "将导出系统中所有的终端信息";
                    var ids = new Array();
                    if (records.length > 0) {
                        title = "将导出选择的终端分配的信息";
                        Ext.each(records, function (rec) {
                            var pkValue = rec.get(pkName);
                            ids.push(pkValue);
                        });

                    }
                    Ext.Msg.confirm('提示', title, function (btn, text) {
                        if (btn == "yes") {
                            Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                            var component = Ext.create('Ext.Component', {
                                title: 'HelloWorld',
                                width: 0,
                                height: 0,
                                hidden: true,
                                html: '<iframe src="' + comm.get('baseUrl') + '/OaInfoterm/exportExcel?ids=' + ids + '"></iframe>',
                                renderTo: Ext.getBody()
                            });

                            var time = function () {
                                self.syncAjax({
                                    url: comm.get('baseUrl') + '/OaInfoterm/checkExportEnd?ids=' + ids,
                                    timeout: 1000 * 60 * 30,        //半个小时
                                    //回调代码必须写在里面
                                    success: function (response) {
                                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                        if (data.success) {
                                            Ext.Msg.hide();
                                            self.msgbox(data.obj);
                                            component.destroy();
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
             * 操作列的操作事件
             */
            "basegrid[funCode=roomterminal_main] actioncolumn[ref=roomterminaldetail]": {
                detailClick: function(grid, cmd, rowIndex) {
                    switch (cmd) {
                        case "delete":
                            self.doDelete(grid, cmd, rowIndex);
                            break;
                    }

                    return false;
                }
            },

            "baseformwin[funCode=roomterminal_detail] button[ref=formSave]": {
                beforeclick: function(btn) {
                    self.doSave(btn);
                    return false;
                }
            }
        });
    },
    doDetail: function(btn, cmd, rowIndex) {
        var self = this;
        var basePanel = btn.up("panel[xtype=roomterminal.mainlayout]");
        var baseGrid = basePanel.down("basegrid");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
        var roomId = funData.roomId;
        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            roomId: roomId,
            roomName: funData.roomName
        });
        
        
        var otherController=basePanel.otherController;  
        if(!otherController)
            otherController='';
        
        var iconCls = "x-fa fa-plus-circle";
        var title = "分配终端";
        var operType = cmd;
        var winId = detCode + "_win";
        var closeAction = "destroy";
        var width = funData.width;
        var height = funData.height;
        var itemXtype = "roomterminal.roomform";
        switch (cmd) {
            case "addReturn":
                title = "分配终端";
                iconCls = "x-fa fa-plus-circle";
                break;
            case "edit":
                title = "编辑";
                iconCls = "x-fa fa-pencil-square";
                if (!Ext.isEmpty(rowIndex)) {
                    //通过Action列修改
                    records = baseGrid.getStore().getAt(rowIndex);
                    insertObj = records.data;
                    selCount = 1;
                } else {
                    //选中后点击修改按钮修改
                    records = baseGrid.getSelectionModel().getSelection();
                    if (records.length != 0) {
                        insertObj = records[0].data;
                        selCount = records.length;
                    } else {
                        selCount = 0;
                    }
                }
                if (selCount != 1) {
                    self.Warning("请选择要修改的数据");
                    return;
                }
                break;
            case "read":
                title = "详情";
                height = 600;
                itemXtype = "terminal.readonlyform";
                if (!Ext.isEmpty(rowIndex)) {
                    records = baseGrid.getStore().getAt(rowIndex);
                    insertObj = records.data;
                    selCount = 1;
                } else {
                    //选中后点击修改按钮修改
                    records = baseGrid.getSelectionModel().getSelection();
                    insertObj = records[0].data;
                    selCount = records.length;
                }
                if (selCount != 1) {
                    self.Warning("请选择要查看的数据");
                    return;
                }
                break;
        }
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                title: title,
                id: winId,
                controller:otherController,
                closeAction: closeAction,
                width: width,
                height: height,
                iconCls: iconCls,
                operType: operType,
                cmd: cmd,
                funData: popFunData,
                funCode: detCode,
                items: [{
                    xtype: detLayout,
                    items: [{
                        xtype: itemXtype
                    }]
                }]
            });
        }
        win.show();
        var objDetForm = null;
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetailForm = detailPanel.down("panel[xtype=" + itemXtype + "]");
        //获取要分配的房间的详细信息
        var resObj = self.ajax({
            url: funData.action + "/getRoomTermInfo",
            params: {
                roomId: roomId,
            }
        });
        if (resObj.success) {
            insertObj = resObj.obj;
        } else {
            if (!Ext.isEmpty(resObj.obj)) self.msgbox(resObj.obj);
        }
        var detailForm = objDetailForm.getForm();
        self.setFormValue(detailForm, insertObj);
        // if (cmd == "read")
        //     self.setFuncReadOnly(funData, objDetailForm, true);
    },
    /**
     * 保存事件响应
     * @param  {[type]} btn [description]
     * @param  {[type]} cmd [description]
     * @return {[type]}     [description]
     */
    doSave: function(btn) {
        var self = this;
        var win = btn.up('window');
        var funCode = win.funCode;
        var doType = win.cmd;
        var grid = win.funData.grid;
        var roomId = funData.roomId;
        var roomName = funData.roomName;
        var filter = funData.filter;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();
        var upData = new Array();
        var termId, termCode, mpNumb;
        for (var i = 0; i < 5; i++) {
            var k = i + 1;
            termId = Ext.value(formObj.findField("termId" + k).getValue(), null);
            termCode = Ext.value(formObj.findField("termCode" + k).getValue(), null);
            mpNumb = Ext.value(formObj.findField("houseNumb" + k).getValue(), null);
            if (!Ext.isEmpty(mpNumb)) {
                for (var j = i + 1; j < 5; j++) {
                    var l = j + 1
                    var termCodett = Ext.value(formObj.findField("termCode" + l).getValue(), null);
                    if (termCode!=null&&termCode === termCodett) {
                        self.Warning("终端配置重复，请重新设置");
                        return false;
                        break;
                    } else {
                        if (!Ext.isEmpty(mpNumb) && !(Ext.isEmpty(termCode))) {
                            upData.push("{'uuid':'" + termId + "','termCode':'" + termCode + "','houseNumb':'" + mpNumb + "'}");
                        }
                        break;
                    }
                }
            } else {
                break;
            }
        }
        var resObj = self.ajax({
            url: funData.action + "/doSetTerminal",
            params: {
                terminals: "[" + upData.join(",") + "]",
                roomId: roomId,
                roomName: roomName
            }
        });
        if (resObj.success) {
            self.msgbox(resObj.obj);
            var store = grid.getStore();
            var proxy = store.getProxy();
            proxy.extraParams = {
                filter: filter,
            }
            store.load();
            win.close();
        } else {
            if (!Ext.isEmpty(resObj.obj)) self.msgbox(resObj.obj);
        }
    },

    /**
     * 取消的响应操作
     */
    doDelete: function(btn, cmd, rowIndex) {
        var self = this;
        var basePanel = btn.up("panel[xtype=roomterminal.mainlayout]");
        var baseGrid = basePanel.down("panel[xtype=roomterminal.listgrid]");
        var funData = basePanel.funData;
        var filter = funData.filter;
        var pkName = funData.pkName;
        var pkValue;
        var ids = new Array();
        if (!Ext.isEmpty(rowIndex)) {
            //通过Action列删除
            records = baseGrid.getStore().getAt(rowIndex);
            pkValue = records.get(pkName);
            ids.push(pkValue);
        } else {
            //选中后点击修改按钮修改
            records = baseGrid.getSelectionModel().getSelection();
            Ext.each(records, function(rec) {
                pkValue = rec.get(pkName);
                ids.push(pkValue);
            });
        }
        if (ids.length == 0) {
            self.Warning("请选择要从此房间取消的终端");
            return;
        }
        var title = "确定要取消所选终端的分配吗？";
        Ext.Msg.confirm('警告', title, function(btn, text) {
            if (btn == 'yes') {
                //发送ajax请求
                var resObj = self.ajax({
                    url: funData.action + "/dodelete",
                    params: {
                        ids: ids.join(","),
                        pkName: pkName
                    }
                });
                if (resObj.success) {
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        filter: filter
                    }
                    store.load();
                    self.msgbox(resObj.obj);
                } else {
                    self.Error(resObj.obj);
                }
            }
        });
    },

});
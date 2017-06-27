Ext.define("core.oa.terminal.controller.MainController", {
    extend: "Ext.app.ViewController",
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    
    alias: 'controller.terminal.terminalController',
/*    views: [
        "core.oa.terminal.view.mainLayout",
        "core.oa.terminal.view.detailLayout",
        "core.oa.terminal.view.listGrid",
        "core.oa.terminal.view.detailForm",
        "core.oa.terminal.view.readonlyForm"
    ],*/
    init: function() {
        var self = this;
        this.control({
            "basegrid[funCode=terminal_main] button[ref=gridFastSearchBtn]": {
                beforeclick:function(btn){                
                    //得到组件
                    var baseGrid = btn.up("basegrid");
                        
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();


                    var roomName=baseGrid.down("textfield[name=roomName]").getValue();     
                    
                    proxy.extraParams.filter = '[{"type":"string","value":"'+roomName+'","field":"roomName","comparison":""}]';
                    store.loadPage(1);

                    return false;
                }
            },	
        	
        	
        	
        
            /**
             * 增加按钮事件响应,此处拦截了公用的增加事件
             * @type {[type]}
             */
            "basegrid[funCode=terminal_main] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    self.doDetail(btn, "addReturn");
                    return false;
                }
            },
            /**
             * 操作列的操作事件
             */
            "basegrid[funCode=terminal_main] actioncolumn[ref=terminaldetail]": {
                detailClick: function(grid, cmd, rowIndex) {
                    switch (cmd) {
                        case "edit":
                            self.doDetail(grid, cmd, rowIndex);
                            break;
                        case "read":
                            self.doDetail(grid, cmd, rowIndex);
                            break;
                    }

                    return false;
                }
            },
        });
    },
    doDetail: function(btn, cmd, rowIndex) {
        var self = this;
        var basePanel = btn.up("panel[xtype=terminal.mainlayout]");
        var baseGrid = basePanel.down("basegrid");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });
        var iconCls = "x-fa fa-plus-circle";
        var title = "增加";
        var operType = cmd;
        var winId = detCode + "_win";
        var closeAction = "destroy";
        var width = funData.width;
        var height = funData.height;
        var itemXtype = "terminal.detailform";
        var pkValue = "";
        switch (cmd) {
            case "add":
                title = "增加";
                height = 400;
                iconCls = "x-fa fa-plus-circle";
                break;
            case "edit":
                title = "编辑";
                height = 400;
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
                height = 620;
                width=650;
                itemXtype = "terminal.readonlyform";
                if (!Ext.isEmpty(rowIndex)) {
                    records = baseGrid.getStore().getAt(rowIndex);
                    insertObj = records.data;
                    pkValue = records.get("uuid");
                    selCount = 1;
                } else {
                    //选中后点击修改按钮修改
                    records = baseGrid.getSelectionModel().getSelection();
                    insertObj = records[0].data;
                    pkValue = records[0].get("uuid");
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

        var detailForm = objDetailForm.getForm();
        self.setFormValue(detailForm, insertObj);
        if (cmd == "read")
            self.setFuncReadOnly(funData, objDetailForm, true);
        var filter="[{'type':'string','comparison':'=','value':'" + pkValue +"','field':'termId'}]"
        var termUseGrid = detailPanel.down("basegrid[ref=termUseGrid]");
        var store = termUseGrid.getStore();
        var proxy = store.getProxy();
        proxy.extraParams = {
            filter: filter,
            limit:20
        };
        store.load(); // 给form赋值
    },
    /**
     * 保存事件响应
     * @param  {[type]} btn [description]
     * @param  {[type]} cmd [description]
     * @return {[type]}     [description]
     */
    doSave: function(btn, cmd) {
        var self = this;
        var win = btn.up('window');
        var funCode = win.funCode;
        var doType = win.cmd;
        var grid = win.funData.grid;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();

        var funData = basePanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);

        //判断当前是保存还是修改操作
        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
        if (formObj.isValid()) {
            var resObj = self.ajax({
                url: funData.action + "/" + act,
                params: params
            });

            if (resObj.success) {
                //采用返回的数据刷新表单
                self.setFormValue(formObj, resObj.obj);
                self.msgbox("保存成功!");
                var grid = win.funData.grid; //窗体是否有grid参数
                if (!Ext.isEmpty(grid)) {
                    var store = grid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        whereSql: win.funData.whereSql,
                        orderSql: win.funData.orderSql,
                    };
                    store.load(); //刷新父窗体的grid
                }
                win.close();
            } else {
                if (!Ext.isEmpty(resObj.obj)) self.Info(resObj.obj);
            }
        } else {
            var errors = ["前台验证失败，错误信息："];
            formObj.getFields().each(function(f) {
                if (!f.isValid()) {
                    errors.push("<font color=red>" + f.fieldLabel + "</font>:" + f.getErrors().join(","));
                }
            });
            self.msgbox(errors.join("<br/>"));
        }

    },
});
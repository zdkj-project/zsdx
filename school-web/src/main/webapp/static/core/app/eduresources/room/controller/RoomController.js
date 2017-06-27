Ext.define("core.eduresources.room.controller.RoomController", {
    extend: "Ext.app.ViewController",
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        treeUtil: "core.util.TreeUtil",
        gridActionUtil: "core.util.GridActionUtil"
    },

    alias: 'controller.room.roomController',

    /*    views: ["core.eduresources.room.view.MainLayout",
     "core.eduresources.room.view.areaGrid",
     "core.eduresources.room.view.areaDetailLayout",
     "core.eduresources.room.view.areaForm",
     "core.eduresources.room.view.RoomLayout",
     "core.eduresources.room.view.RoomGrid",
     "core.eduresources.room.view.RoomForm",
     "core.eduresources.room.view.BatchRoomForm",
     ],*/
    init: function () {
        var self = this
        // 事件注册
        this.control({
            //批量添加房间
            "panel[xtype=room.RoomGrid] button[ref=roomAdd]": {
                beforeclick: function (btn) {
                    // 得到组件
                    var baseGrid = btn.up("basegrid");
                    var tree = baseGrid.up("panel[xtype=room.mainlayout]").down("panel[xtype=room.areagrid]");
                    var selectObject = tree.getSelectionModel().getSelection()[0];
                    var areaId = "";
                    var level = "";
                    var areaName = ""

                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var otherController = basePanel.otherController;
                    if (!otherController)
                        otherController = '';

                    if (selectObject == null) {
                        self.Warning("请选择楼层!");
                        return false;
                    } else {
                        areaId = selectObject.get("id");
                        level = selectObject.get("level");
                        areaName = selectObject.get("text");
                    }
                    if (level != 3) {
                        self.Warning("只能选择楼层添加!");
                        return false;
                    }
                    var win = Ext.create('Ext.Window', {
                        title: "批量添加房间",
                        controller: otherController,
                        baseGrid: baseGrid,
                        iconCls: 'x-fa fa-plus-circle',
                        resizable: false,
                        modal: true,
                        width: 400,
                        height: 400,
                        iconCls: 'x-fa fa-plus-circle',
                        items: [{
                            xtype: "room.BatchRoomForm"
                        }]
                    });
                    win.show();
                    var detailPanel = win.down("panel");
                    //var objDetForm = detailPanel.down("form");
                    var gerForm = detailPanel.getForm();
                    //表单赋值
                    // self.setFormValue(formDeptObj, insertObj);

                    // var dicForm = Ext.getCmp("room.BatchRoomForm"); // 这是手动的获取上级部门的ID
                    //  var gerForm = dicForm.getForm();
                    gerForm.findField('areaId').setValue(areaId);
                    gerForm.findField('areaName').setValue(areaName);
                    return false;
                }
            },
            //批量添加保存
            "panel[xtype=room.BatchRoomForm] button[ref=formSave]": {
                beforeclick: function (btn) {
                    var baseGrid = btn.up('window').baseGrid;
                    var dicForm = btn.up("panel[xtype=room.BatchRoomForm]").getForm();
                    var params = self.getFormValue(dicForm);
                    var resObj = null;
                    if (dicForm.isValid()) {
                        resObj = self.ajax({
                            url: comm.get('baseUrl') + "/BuildRoominfo/batChdoAdd",
                            params: params
                        });
                        if (resObj.success) {
                            self.msgbox('成功');
                            baseGrid.getStore().load();
                        } else {
                            self.msgbox(resObj.obj);
                        }
                    }
                    return false;
                }
            },
            //批量添加关闭
            "panel[xtype=room.BatchRoomForm] button[ref=formClose]": {
                beforeclick: function (btn) {
                    btn.up('window').close();
                    return false;
                }
            },
            //区域列表点击事件
            "panel[xtype=room.areagrid]": {
                itemclick: function (tree, record, item, index, e, eOpts) {
                    var mainLayout = tree.up("panel[xtype=room.mainlayout]");
                    var filter = "[{'type':'string','comparison':'=','value':'" + record.get("id") + "','field':'areaId'}]"
                    var funData = mainLayout.funData;
                    mainLayout.funData = Ext.apply(funData, {
                        areaId: record.get("id"),
                        areaType: record.get("areaType"),
                        areaName: record.get("text"),
                        filter: filter,
                    });
                    // 加载对应的房间信息
                    var sort = "[{property: 'orderIndex', direction: 'ASC' }]";
                    var roomGrid = mainLayout.down("panel[xtype=room.RoomGrid]");
                    var store = roomGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        filter: filter,
                        sort: sort
                    };
                    store.load(); // 给form赋值
                    // return false;
                }
            },
            //区域列表刷新按钮事件
            "panel[xtype=room.areagrid] button[ref=gridRefresh]": {
                click: function (btn) {
                    var baseGrid = btn.up("basetreegrid");
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        whereSql: "and isDelete='0'",
                        orderSql: ""
                    };
                    store.load(); //刷新父窗体的grid
                    return false;
                }
            },
            //区域列表删除按钮事件
            "panel[xtype=room.areagrid] button[ref=gridDelete]": {
                beforeclick: function (btn) {
                    var baseGrid = btn.up("basetreegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                    var records = baseGrid.getSelectionModel().getSelection();
                    if (records.length == 0) {
                        self.Error("请选择要删除的区域");
                        return;
                    }
                    var ids = new Array();
                    Ext.each(records, function (rec) {
                        var pkValue = rec.get("id");
                        var roomCount = rec.get("roomCount");
                        var child = rec.childNodes.length;
                        //仅能删除无子节点并且无房间的区域
                        if (child == 0 && roomCount == 0) {
                            ids.push(pkValue);
                        }
                    });
                    var title = "确定要删除所选的区域吗？";
                    if (ids.length == 0) {
                        self.Warning("所选区域都有子项或房间，不能删除");
                        return;
                    }
                    if (ids.length < records.length) {
                        title = "有些区域有子项或房间，仅删除不含子项(房间)的区域。确定吗？";
                    }
                    Ext.Msg.confirm('警告', title, function (btn, text) {
                        if (btn == 'yes') {
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: comm.get('baseUrl') + "/BuildRoomarea/dodelete",
                                params: {
                                    ids: ids.join(","),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                baseGrid.getStore().load(0);
                                self.msgbox(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }
                    return false;
                }
            },
            //区域列表增加下级按钮事件
            "panel[xtype=room.areagrid] button[ref=gridAdd]": {
                click: function (btn) {
                    self.doDetail(btn, "child");
                }
            },
            //区域列表增加同级按钮事件
            "panel[xtype=room.areagrid] button[ref=gridAddBrother]": {
                click: function (btn) {
                    self.doDetail(btn, "brother");
                }
            },
            //区域列表修改按钮事件
            "panel[xtype=room.areagrid] button[ref=gridEdit]": {
                click: function (btn) {
                    self.doDetail(btn, "edit");
                }
            },
            //grid删除
            "panel[xtype=room.RoomGrid] button[ref=gridDelete]": {
                beforeclick: function (btn) {
                    var grid = btn.up("panel[xtype=room.RoomGrid]");
                    var rows = grid.getSelectionModel().getSelection();
                    //判断性别
                    for (var i = 0; i < rows.length; i++) {
                        //将学生主键加入到list
                        var areaStatu = rows[i].get('areaStatu');
                        if (areaStatu == 1) {
                            self.Warning("已分配的房间不能删除。!");
                            return false;
                        }
                    }
                }
            },
            //房间修改按钮事件
            "panel[xtype=room.RoomGrid] button[ref=gridEdit]": {
                beforeclick: function (btn) {
                    self.doRoomDetail(btn, "edit");
                    return false;
                }
            },
            //房间添加按钮事件
            "panel[xtype=room.RoomGrid] button[ref=gridAdd]": {
                beforeclick: function (btn) {
                    self.doRoomDetail(btn, "add");
                    return false;
                }
            },
        });
    },
    //增加修改区域
    doDetail: function (btn, cmd) {
        //debugger;
        var self = this;
        var baseGrid = btn.up("basetreegrid");
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("panel[xtype=room.mainlayout]");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            whereSql: " and isDelete='0' "
        });
        //先确定要选择记录
        var records = baseGrid.getSelectionModel().getSelection();
        if (records.length != 1) {
            self.Error("请先选择区域");
            return;
        }
        //当前节点
        var just = records[0].get("id");
        var justName = records[0].get("text");
        var justType = records[0].get("areaType");
        //当前节点的上级节点
        var parent = records[0].get("parentNode");
        var store = baseGrid.getStore();
        var parentNode = store.getNodeById(parent);
        var parentName = "ROOT";
        var parentType = "01";
        if (parentNode) {
            parentName = parentNode.get("text");
            parentType = parentNode.get("areaType");
        }
        //当前节点的下级类型
        var childType = "02";
        switch (justType) {
            case "01":
                childType = "02";
                break;
            case "02":
                childType = "03";
                break;
            case "03":
                childType = "04";
                break;
        }
        //根据选择的记录与操作确定form初始化的数据
        var iconCls = "x-fa fa-plus-circle";
        var title = "增加下级区域";
        var operType = cmd;
        switch (cmd) {
            case "child":
                operType = "add";
                if (justType == "04") {
                    self.Error("区域类型为楼层，只能增加同级");
                    return;
                }
                insertObj = Ext.apply(insertObj, {
                    parentNode: just,
                    parentName: justName,
                    uuid: null,
                    areaType: childType,
                    parentType: parentType
                });
                break;
            case "brother":
                title = "增加同级区域";
                if (justType == "01") {
                    self.Error("区域类型为学校，只能增加下级");
                    return;
                }
                operType = "add";
                insertObj = Ext.apply(insertObj, {
                    parentNode: parent,
                    parentName: parentName,
                    uuid: null,
                    areaType: justType,
                    parentType: parentType
                });
                break;
            case "edit":
                iconCls = "x-fa fa-pencil-square";
                operType = "edit";
                title = "修改区域";
                insertObj = records[0].data;
                insertObj = Ext.apply(insertObj, {
                    parentNode: parent,
                    parentName: parentName,
                    uuid: just,
                    nodeText: justName,
                    parentType: parentType
                });
                break;
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                title: title,
                width: 600,
                height: 400,
                resizable: false,
                iconCls: iconCls,
                operType: operType,
                funData: popFunData,
                funCode: detCode,
                //给form赋初始值
                insertObj: insertObj,
                items: [{
                    xtype: "room.areadetaillayout"
                }]
            });
        }
        win.show();
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
        var formDeptObj = objDetForm.getForm();
        //表单赋值
        self.setFormValue(formDeptObj, insertObj);
    },
    //增加或修改房间
    doRoomDetail: function (btn, cmd) {
        //debugger;
        var self = this;
        //当前的grid
        var baseGrid = btn.up("basegrid");
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var funData = basePanel.funData;
        var areaId = funData.areaId;
        var areaType = funData.areaType;
        var areaName = funData.areaName;
        if (areaType != "04") {
            self.Warning("请选择楼层!");
            return false;
        }
        detCode = "room_roomdetail";
        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);
        insertObj = Ext.apply(insertObj, {
            areaId: areaId,
            areaName: areaName
        });
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            filter: "[{'type':'string','comparison':'=','value':'" + areaId + "','field':'areaId'}]"
        });
        var iconCls = "x-fa fa-plus-circle";
        if (cmd == "edit" || cmd == "detail") {
            if (cmd == "edit") iconCls = "x-fa fa-pencil-square";
            else iconCls = "x-fa fa-pencil-square";
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择数据");
                return;
            }
            insertObj = rescords[0].data;
            insertObj = Ext.apply(insertObj, {
                areaId: areaId,
                areaName: areaName
            });
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                width: 400,
                height: 500,
                resizable: false,
                iconCls: iconCls,
                operType: cmd,
                funData: popFunData,
                funCode: detCode,
                //给form赋初始值
                insertObj: insertObj,
                items: [{
                    xtype: "room.roomdetaillayout"
                }]
            });
        }
        win.show();
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
        var formDeptObj = objDetForm.getForm();
        //表单赋值
        self.setFormValue(formDeptObj, insertObj);
        //根据操作设置是否只读
        if (cmd == "detail") {
            self.setFuncReadOnly(funData, objDetForm, true);
        }
        //执行回调函数
        if (btn.callback) {
            btn.callback();
        }
    }
});
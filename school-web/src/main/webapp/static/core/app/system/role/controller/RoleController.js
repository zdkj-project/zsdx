Ext.define("core.system.role.controller.RoleController", {
    extend: "Ext.app.ViewController",
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        treeUtil: "core.util.TreeUtil",
        gridActionUtil: "core.util.GridActionUtil"
    },

  
    alias: 'controller.role.roleController',

  
    init: function() {
        var self = this
            //事件注册
        this.control({
            //快速搜索按按钮
            "basegrid[funCode=role_main] button[ref=gridFastSearchBtn]": {
                beforeclick:function(btn){                
                    //得到组件
                    
                    var baseGrid = btn.up("basegrid");
                        
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();


                    var roleName=baseGrid.down("textfield[name=roleName]").getValue();               
                    
                    proxy.extraParams.filter = '[{"type":"string","value":"'+roleName+'","field":"roleName","comparison":""}]';
                    store.loadPage(1);

                    return false;

                }
            },

            /**
             * 增加按钮事件响应
             * @type {[type]}
             */
            "basegrid[funCode=role_main] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    self.doDetail(btn, "add");
                    return false;
                }
            },
            /**
             * 修改按钮事件响应
             * @type {[type]}
             */
            "basegrid[funCode=role_main] button[ref=gridEdit]": {
                beforeclick: function(btn) {
                    self.doDetail(btn, "edit");
                    return false;
                }
            },
            //删除按钮
            "basegrid[funCode=role_main] button[ref=gridDelete]": {
                beforeclick: function(btn) {
                    //得到组件
                    var baseGrid = btn.up("basegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                    //得到选中数据
                    var records = baseGrid.getSelectionModel().getSelection();
                    var ids = new Array();
                    Ext.each(records, function(rec) {
                        var pkValue = rec.get(pkName);
                        var issystem = rec.get("issystem");
                        if (issystem === 1)
                            ids.push(pkValue);
                    });
                    if (ids.length == 0) {
                        self.Error("所选的角色为系统内置角色，不能删除。请重新选择！");
                        return false;
                    }
                    var title = "确定要删除所选的角色吗？";
                    if (ids.length < records.length) {
                        title = "有些角色为内置角色，只能删除非内置角色。确定删除吗？"
                    }
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
                                //baseGrid.getStore().load(0);
                                var store = baseGrid.getStore();
                                var proxy = store.getProxy();
                                proxy.extraParams = {
                                    filter: "[{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}]"
                                };
                                store.load();
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

             /**
             * 修改按钮事件响应
             * @type {[type]}
             */
            "basegrid[funCode=role_main] button[ref=gridDetail]": {
                beforeclick: function(btn) {
                    var baseGrid = btn.up("basegrid");
                    var records = baseGrid.getSelectionModel().getSelection();
                    if (records.length != 1) {
                        self.msgbox("请选择数据");
                        return;
                    }

                    self.doRoleUserDetail(baseGrid, records[0]);    
                    return false;
                }
            },
            /**
             * 操作列的操作事件
             */
            "basegrid[funCode=role_main] actioncolumn[ref=roledetail]": {
                detailClick: function(grid, record) {
                    self.doRoleUserDetail(grid, record);               
                    return false;
                },
                editClick: function(data) {
                  
                    var baseGrid=data.view;
                    var record=data.record;
                 
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                    var detCode = basePanel.detCode;
                    var detLayout = basePanel.detLayout;
                    var defaultObj = funData.defaultObj;
                    //处理特殊默认值
                    var insertObj = self.getDefaultValue(defaultObj);
                    var popFunData = Ext.apply(funData, {
                        grid: baseGrid
                    });
                    var iconCls = "x-fa fa-pencil-square";
                    var issystem = 1;
                  
                    var issystem = record.get("issystem");
                    insertObj = record.data;
                    
                    if (issystem === 0) {
                        self.Error("系统内置角色，不能编辑！");
                        return false;
                    }
                    var winId = detCode + "_win";
                    var win = Ext.getCmp(winId);
                    if (!win) {
                        win = Ext.create('core.base.view.BaseFormWin', {
                            id: winId,
                            width: 400,
                            height: 290,
                            iconCls: iconCls,
                            operType: 'edit',
                            funData: popFunData,
                            funCode: detCode,
                            insertObj: insertObj,
                            items: [{
                                xtype: detLayout
                            }]
                        });
                    }
                    win.show();
                    var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
                    var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();

                    //表单赋值
                    self.setFormValue(formDeptObj, insertObj);
                   
                  
                    return false;
                },
                deleteClick: function(data) {
                   
                    var baseGrid=data.view;
                    var record=data.record;

                     //得到组件
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                    //得到选中数据
                    var records = record;
                    var ids = new Array();
                    Ext.each(records, function(rec) {
                        var pkValue = rec.get(pkName);
                        var issystem = rec.get("issystem");
                        if (issystem === 1)
                            ids.push(pkValue);
                    });
                    if (ids.length == 0) {
                        self.Error("所选的角色为系统内置角色，不能删除。请重新选择！");
                        return false;
                    }
                    var title = "确定要删除所选的角色吗？";
                    if (ids.length < records.length) {
                        title = "有些角色为内置角色，只能删除非内置角色。确定删除吗？"
                    }
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
                                //baseGrid.getStore().load(0);
                                var store = baseGrid.getStore();
                                var proxy = store.getProxy();
                                proxy.extraParams = {
                                    filter: "[{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}]"
                                };
                                store.load();
                                self.msgbox(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                   

                    return false;
                }
            },
        })
    },

    //数据维护操作
    doDetail: function(btn, cmd , gird) {
        var self = this;
        var baseGrid = btn.up("basegrid");
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
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
        var issystem = 1;
        if (cmd == "edit" || cmd == "detail") {
            if (cmd == "edit")
                iconCls = "x-fa fa-pencil-square";
            else
                iconCls = "x-fa fa-file-text";

            var records = baseGrid.getSelectionModel().getSelection();
            if (records.length != 1) {
                self.msgbox("请选择数据");
                return;
            }
            var issystem = records[0].get("issystem");
            insertObj = records[0].data;
        }
        if (issystem === 0) {
            self.Error("系统内置角色，不能编辑！");
            return;
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                width: 400,
                height: 290,
                iconCls: iconCls,
                operType: cmd,
                funData: popFunData,
                funCode: detCode,
                insertObj: insertObj,
                items: [{
                    xtype: detLayout
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
    },

    doRoleUserDetail:function(grid, record){
        var basePanel = grid.up("panel[xtype=role.mainlayout]");
        var baseGrid = basePanel.down("basegrid");
        var funData = basePanel.funData;
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var itemXtype = "recexamines.detailform";
        title = "角色用户列表"

        var roleId = record.get("uuid");
        insertObj = record.data;
        itemXtype = "role.roleusergrid";
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                title: title,
                id: winId,
                width: 400,
                height: 500,
                //txtformSave: "批阅完毕",
                iconCls: "x-fa fa-user",
                //operType: cmd,
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
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var roleUserGrid = detailPanel.down("panel[xtype=role.roleusergrid]");
        var store = roleUserGrid.getStore();
        var proxy = store.getProxy();
        proxy.extraParams = {
            whereSql: win.funData.whereSql,
            orderSql: win.funData.orderSql,
            roleId: roleId
        };
        store.load(); //刷新父窗体的grid         
    }
});
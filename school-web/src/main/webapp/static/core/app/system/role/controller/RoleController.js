Ext.define("core.system.role.controller.RoleController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.role.roleController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil"
    },
    init: function () {
    },
    control: {
        /**
         * 角色列表操作列事件
         */
        "basegrid  actioncolumn": {
            //操作列详情、角色用户
            detailClick_Tab: function (data) {
                var self = this;
                self.doDetailClick_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            //操作列删除
            deleteClick: function (data) {
                var self = this;
                self.doDeleteClick(null, data.cmd, data.view, data.record);
                return false;
            }
        },
        /**
         * 角色列表删除按钮事件
         */
        "basegrid[funCode=role_main] button[ref=gridDelete]": {
            beforeclick: function (btn) {
                var self = this;
                self.doDeleteClick(btn);
                return false;
            }
        },
        /**
         * 角色用户列表增加按钮事件
         */
        "basegrid[xtype=role.roleusergrid] button[ref=gridAddUser]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +funCode + "]");
                var basetab = btn.up('baseformtab');
                var tabFunData = basetab.funData;
                //得到配置信息
                var funData = basePanel.funData;
                var detCode = "selectuser_detail"; //这个值换为其他，防止多个window误入other控制器中的同一个事件
                var detLayout = "role.selectuserlayout";
                var defaultObj = funData.defaultObj;
                //关键：window的视图控制器
                var otherController = basePanel.otherController;
                if (!otherController)
                    otherController = '';
                //处理特殊默认值
                var insertObj = self.getDefaultValue(defaultObj);
                //填入选择的班级的值
                insertObj = Ext.apply(insertObj, {
                    roleId: tabFunData.roleId,
                    roleName: tabFunData.roleName

                });
                var popFunData = Ext.apply(funData, {
                    grid: baseGrid,
                    roleId: tabFunData.roleId,
                    roleName: tabFunData.roleName
                });
                var width = 1200;
                var height = 600;
                var win = Ext.create('core.base.view.BaseFormWin', {
                    iconCls: 'x-fa fa-plus-circle',
                    operType: 'add',
                    width: width,
                    height: height,
                    controller: otherController, //指定视图控制器，从而能够使指定的控制器的事件生效
                    funData: popFunData,
                    funCode: detCode,
                    insertObj: insertObj,
                    items: [{
                        xtype: detLayout,
                        funCode: detCode //这里将funcode修改为刚刚的detcode值
                    }]
                });
                win.show();
                var selectGrid = win.down("basegrid[xtype=role.selectusergrid]");
                var selectStore = selectGrid.getStore();
                var selectProxy = selectStore.getProxy();
                selectProxy.extraParams = {
                    roleId:tabFunData.roleId
                };
                selectStore.loadPage(1);
                return false;
            }
        },
        /**
         * 角色用户列表删除按钮事件
         */
        "basegrid[xtype=role.roleusergrid] button[ref=gridDelUser]": {
            beforeclick: function (btn) {
                var self = this;
                self.doDeleteUerClick(btn);
                return false;
            }
        }
    },
    /**
     * 详细事件的响应
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doDetailClick_Tab: function (btn, cmd, grid, record) {
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
            recordData = record.data;
        }
        //得到组件
        var funCode = baseGrid.funCode;
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        var operType = cmd;
        var pkName = funData.pkName;
        var pkValue = "";

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        //默认的tab参数
        var tabTitle = funData.tabConfig.roleUserTitle; //标签页的标题
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "role.deailform";
        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "edit":
                insertObj = Ext.apply(insertObj, recordData);
                tabTitle = funData.tabConfig.editTitle;
                tabItemId = funCode + "_gridEdit";
                //获取主键值
                pkValue = recordData[pkName];
                break;
            case "detail":
                insertObj = Ext.apply(insertObj, recordData);
                //获取主键值
                pkValue = recordData[pkName];

                tabTitle = insertObj.roleName +"-"+funData.tabConfig.detailTitle;
                tabItemId = funCode + "_gridDetail"+pkValue;
                itemXtype = "role.detailhtml";
                
                break;
            case "roleUser": //角色用户
                insertObj = Ext.apply(insertObj, recordData);
                //获取主键值
                pkValue = recordData[pkName];

                tabTitle = insertObj.roleName + "-角色用户";
                tabItemId = funCode + "_gridRoleUser"+pkValue;
                itemXtype = "role.roleusergrid";
                
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            roleName: insertObj.roleName,
            roleId: insertObj.uuid
        });

        //获取tabItem；若不存在，则表示·要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);

        //判断是否已经存在tab了
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,    //保存主键值
                layout: 'fit',
                margin: 5
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,         //指定重写事件的控制器
                    funCode: funCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: [{
                        xtype: detLayout,
                        items: [{
                            xtype: itemXtype
                        }]
                    }]
                });
                tabItem.add(item);
                //根据需要初始化详情页面数据
                switch (cmd) {
                    case "edit":
                        var objDetailForm = item.down("baseform[funCode=" + detCode + "]");
                        var formDetailObj = objDetailForm.getForm();
                        self.setFormValue(formDetailObj, insertObj);
                        break;
                    case "detail":
                        var roleInfoContainer = tabItem.down("container[ref=roleInfo]");
                        roleInfoContainer.setData(insertObj);
                        self.asyncAjax({
                            url: comm.get("baseUrl") + "/sysrole/getRoleUser",
                            params: {
                                page: 1,
                                start: 0,
                                limit: 0,
                                ids: insertObj.uuid
                            },
                            success: function (response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                var roleUserContainer = tabItem.down("container[ref=roleUser]");
                                roleUserContainer.setData(data);
                            }
                        });
                        break;
                    case "roleUser":
                        var roleUserGrid = tabItem.down("basegrid[xtype=role.roleusergrid]");
                        var proxy = roleUserGrid.getStore().getProxy();
                        proxy.extraParams.ids = insertObj.uuid;
                        roleUserGrid.getStore().load();
                        break;
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },
    /**
     * 删除事件的响应处理
     */
    doDeleteClick: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var ids = new Array();
        var funCode;
        var basePanel;
        var funData;
        var pkName;
        var title = "确定要删除所选的角色吗？";

        if (btn) {
            baseGrid = btn.up("basegrid");
            funCode = baseGrid.funCode;
            basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
            funData = basePanel.funData;
            pkName = funData.pkName;
            var records = baseGrid.getSelectionModel().getSelection();
            Ext.each(records, function (rec) {
                var pkValue = rec.get(pkName);
                var issystem = rec.get("issystem");
                if (issystem === 0)
                    ids.push(pkValue);
            });
            if (ids.length == 0) {
                self.Error("所选的角色为系统内置角色，不能删除。请重新选择！");
                return false;
            }
            if (ids.length < records.length) {
                title = "有些角色为内置角色，只能删除非内置角色。确定删除吗？"
            }
        } else {
            baseGrid = grid;
            funCode = baseGrid.funCode;
            basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
            funData = basePanel.funData;
            pkName = funData.pkName;
            ids.push(record.get(pkName));
        }
        Ext.Msg.confirm('警告', title, function (btnOper, text) {
            if (btnOper == 'yes') {
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
                    store.load();
                    self.Info(resObj.obj);
                } else {
                    self.Error(resObj.obj);
                }
            }
        });
    },

    /**
     * 删除角色用户
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doDeleteUerClick: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var ids = new Array();
        var funCode;
        var basePanel;
        var funData;
        var pkName;
        var title = "确定要删除所选的用户吗？";

        if (btn) {
            baseGrid = btn.up("basegrid");
            funCode = baseGrid.funCode;
            basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
            funData = basePanel.funData;
            pkName = funData.pkName;
            var records = baseGrid.getSelectionModel().getSelection();
            Ext.each(records, function (rec) {
                var pkValue = rec.get(pkName);
                ids.push(pkValue);
            });
            if (ids.length == 0) {
                self.Error("没有选择要删除的角色用户！");
                return false;
            }
        } else {
            baseGrid = grid;
            funCode = baseGrid.funCode;
            basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
            funData = basePanel.funData;
            pkName = funData.pkName;
            ids.push(record.get(pkName));
        }
        var basetab = btn.up('baseformtab');
        var tabFunData = basetab.funData;

        Ext.Msg.confirm('警告', title, function (btnOper, text) {
            if (btnOper == 'yes') {
                //发送ajax请求
                var resObj = self.ajax({
                    url: funData.action + "/doDeleteRoleUser",
                    params: {
                        ids: tabFunData.roleId,
                        userId: ids.join(",")
                    }
                });
                if (resObj.success) {
                    var store = baseGrid.getStore();
                    store.load();
                    self.Info(resObj.obj);
                } else {
                    self.Error(resObj.obj);
                }
            }
        });
    }
});
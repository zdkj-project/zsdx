Ext.define("core.system.role.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.role.OtherController',
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
         * 角色用户选择保存按钮事件
         */
        "baseformwin[funCode=selectuser_detail] button[ref=formSave]": {
            beforeclick: function (btn) {
                var self = this;
                var win = btn.up('window');
                var funCode = win.funCode;
                var winFunData = win.funData;
                var roleId = winFunData.roleId;
                var baseGrid = winFunData.grid;
                var basePanel = win.down("basepanel[funCode=" + funCode + "]");
                var isSelectGrid = basePanel.down("grid[xtype=role.isselectusergrid]");
                var isSelectStore = isSelectGrid.getStore();
                var storeCount = isSelectStore.getCount();
                if (storeCount == 0) {
                    self.Warning("没有要设置的用户，请重新选择");
                    return false;
                }
                var userIds = new Array();
                for (var i = 0; i < storeCount; i++) {
                    userIds.push(isSelectStore.getAt(i).get("uuid"));
                }
                var title = "确定设置这些用户吗？";
                Ext.Msg.confirm('提示', title, function (btnOper, text) {
                    if (btnOper == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: funData.action + "/doAddRoleUser",
                            params: {
                                ids: roleId,
                                userId: userIds.join(",")
                            }
                        });
                        if (resObj.success) {
                            var store = baseGrid.getStore();
                            store.load();
                            self.Info(resObj.obj);
                            win.close();
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });
                return false;
            }
        },

        /**
         * 角色用户选择列表 快速搜索文本框回车事件
         */
        "basepanel basegrid[xtype=role.selectusergrid] field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                var self = this;
                if (e.getKey() == e.ENTER) {
                    self.doFastSearch(field);
                    console.log(field);
                    return false;
                }
            }
        },
        /**
         * 角色用户选择列表 快速搜索按钮事件
         */
        "basepanel basegrid[xtype=role.selectusergrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                var self = this;
                self.doFastSearch(btn);
                return false;
            }
        }
    },
    /**
     * 执行快速搜索
     * @param component
     * @returns {boolean}
     */
    doFastSearch: function (component) {
        //得到组件
        var baseGrid = component.up("basegrid");
        if (!baseGrid)
            return false;

        var toolBar = component.up("toolbar");
        if (!toolBar)
            return false;

        var win = baseGrid.up("window");
        var winFunData = win.funData;
        var roleId = winFunData.roleId;

        var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
        //这里快速搜索就姓名与部门，固定写死查询的条件
        var filter = new Array();
        if (girdSearchTexts[0].getValue() != "")
            filter.push("{'type': 'string', 'comparison': '', 'value':'" + girdSearchTexts[0].getValue() + "', 'field': 'xm'}");
        if (girdSearchTexts[1].getValue() != "")
            filter.push("{'type': 'string', 'comparison': '=', 'value':'" + girdSearchTexts[1].getValue() + "', 'field': 'deptId'}");
        filter = "[" + filter.join(",") + "]";

        var selectStore = baseGrid.getStore();
        var selectProxy = selectStore.getProxy();
        selectProxy.extraParams = {
            roleId: roleId,
            filter: filter
        };
        selectStore.loadPage(1);
    }
});
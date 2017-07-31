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
        "baseformwin[funCode=selectuser_detail] button[ref=formSave]": {
            beforeclick: function(btn) {
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
                if (storeCount==0){
                    self.Warning("没有要设置的用户，请重新选择");
                    return false;
                }
                var userIds = new Array();
                for(var i=0;i<storeCount;i++){
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
                            self.msgbox(resObj.obj);
                            win.close();
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });
                return false;
            }
        },

        "mtsswinview button[ref=ssOkBtn]":{
            beforeclick:function (btn) {
                var self = this;
                self.Warning("test");
                return false;
            }
        }
    }
});
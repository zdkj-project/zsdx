/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
 */
Ext.define("core.oa.meeting.meetinginfo.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.meetinginfo.detailController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function () {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");     
    },
    /** 该视图内的组件事件注册 */
    control: {
        /**
         * 参会人列表增加参会人员按钮事件
         */
        "panel[xtype=meetinginfo.meetingusergrid] button[ref=gridAddUser]": {
            beforeclick: function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var basetab = btn.up('baseformtab');
                var tabFunData = basetab.funData;
                var funcPanel = "pubselect.selectuserlayout";
                tabFunData = Ext.apply(tabFunData, {
                    userGird: baseGrid
                });
                var isUser = new Array();
                records = baseGrid.getStore().data;
                var len = records.length;
                for (var i=0;i<len;i++){
                    isUser.push(records.getAt(i).get("employeeId"));
                }
                //var whereSql = "and uuid not in ('" + isUser.join("','")+"')";
                var buttons = [{
                    text: '确定',
                    ref: 'ssOkBtn',
                    iconCls: 'x-fa fa-check-square'

                }, {
                    text: '取消',
                    ref: 'ssCancelBtn',
                    iconCls: 'x-fa fa-reply'
                }];
                var win = Ext.createWidget("mtfuncwindow", {
                    title: "参会人员选择",
                    iconCls: 'x-fa fa-bars',
                    items: {
                        xtype: "pubselect.selectuserlayout",
                        minWidth: null	//去掉mainLayout中的最小宽度
                    },
                    buttons: buttons,
                    width: 1200,
                    height: 800,
                    funcPanel: funcPanel,
                    controller: "meetinginfo.otherController",
                    funData: tabFunData,
                    listeners: {
                        render: function (win) {
                            var basePanel = win.down("panel[xtype=" + funcPanel + "]");
                            var baseGrid = basePanel.down("basegrid");
                            var baseProxy = baseGrid.getStore().getProxy();	//zzk修复，2017-4-10
                            baseProxy.url = comm.get("baseUrl") + "/OaMeetingemp/getNotMeetingUserList";
                            baseProxy.extraParams.whereSql = "";
                            baseProxy.extraParams.meetingId = tabFunData.uuid;
                        }
                    }
                }).show();
                return false;
            }
        },

        /**
         * 参会人员列表删除人员按钮事件
         */
        "panel[xtype=meetinginfo.meetingusergrid] button[ref=gridDelUser]": {
            beforeclick: function (btn) {
                var self = this;
                self.doDeleteClick(btn);
                return false;
            }
        },
        "panel basegrid[xtype=meetinginfo.meetingusergrid] field[funCode=girdFastSearchText]": {
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
        "panel basegrid[xtype=meetinginfo.meetingusergrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                var self = this;
                self.doFastSearch(btn);
                return false;
            }
        }
    },
    /**
     * 删除事件的响应处理
     */
    doDeleteClick: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var userIds = new Array();
        var title = "确定要删除所选的参会人员吗？";
        var basetab = btn.up('baseformtab');
        var tabFunData = basetab.funData;
        if (btn) {
            baseGrid = btn.up("basegrid");
            var records = baseGrid.getSelectionModel().getSelection();
            Ext.each(records, function (rec) {
                var pkValue = rec.get("uuid");
                userIds.push(pkValue);
            });
            if (userIds.length == 0) {
                self.Error("没有选择要删除的参会人员。请重新选择！");
                return false;
            }
        } else {
            baseGrid = grid;
            userIds.push(record.get("uuid"));
        }
        Ext.Msg.confirm('警告', title, function (btnOper, text) {
            if (btnOper == 'yes') {
                //发送ajax请求
                var resObj = self.ajax({
                    url: tabFunData.action + "/doDeleteMeetingUser",
                    params: {
                        userId: userIds.join(","),
                        ids: tabFunData.uuid
                    }
                });
                if (resObj.success) {
                    var store = baseGrid.getStore();
                    store.load();
                    self.msgbox(resObj.obj);
                } else {
                    self.Error(resObj.obj);
                }
            }
        });
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

        var basetab = baseGrid.up('baseformtab');
        var tabFunData = basetab.funData;
        var meetingId = tabFunData.uuid;

        var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");

        var filter = new Array();
        filter.push("{'type': 'string', 'comparison': '=', 'value':'" + meetingId + "', 'field': 'meetingId'}");
        if (girdSearchTexts[0].getValue() != "")
            filter.push("{'type': 'string', 'comparison': '', 'value':'" + girdSearchTexts[0].getValue() + "', 'field': 'xm'}");
        filter = "[" + filter.join(",") + "]";

        var selectStore = baseGrid.getStore();
        var selectProxy = selectStore.getProxy();
        selectProxy.extraParams = {
            filter: filter
        };
        selectStore.loadPage(1);
    }
});
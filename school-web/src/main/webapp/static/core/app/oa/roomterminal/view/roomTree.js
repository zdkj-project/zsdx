Ext.define("core.oa.roomterminal.view.roomTree", {
    //extend: "Ext.tree.Panel",
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.roomterminal.roomtree",
    //displayField: "text",
    //rootVisible: false,
    //store: "core.oa.roomterminal.store.roomTreeStore",
    dataUrl: comm.get('baseUrl') + "/BuildRoominfo/allRoomTree?whereSql=",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.comm.model.CommTree", "checked").modelName,
    extParams: {
        orderSql: "",
        excludes: "checked"
        //filter: "[{'type':'string','comparison':'=','value':'0','field':'isDelete'}]"
    },
    title: "房间列表",
    selModel: null,
    tools: [{
        type: 'refresh',
        qtip: '刷新',
        handler: function (event, toolEl, header) {
            var tree = header.ownerCt
            tree.getStore().load();
            tree.getSelectionModel().deselectAll(true);
        }
    }],
/*    listeners: {
        itemclick: function (view, record, item, index, e) {
            var self = this;
            var mainLayout = view.up("panel[xtype=roomterminal.mainlayout]");
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
    },*/
/*    columns: [{
        text: "房间名称",
        dataIndex: "text",
        xtype: 'treecolumn',
        flex: 1
    }, {
        text: "主键",
        dataIndex: 'id',
        hidden: true
    }]*/
});
Ext.define("core.system.user.view.DeptTree", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.user.depttree",
    dataUrl: comm.get('baseUrl') + "/BaseOrg/treelist",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.baseset.model.BaseOrgTree", "checked").modelName,
    al: true,
    scrollable:true,
    title: "部门列表",
    selModel: {
        selType: ""
    },
    extParams: {
        whereSql: " and isDelete='0' ",
        orderSql: " order by parentNode,orderIndex asc",
        excludes:"checked"
    },
    tools: [{
        type: 'refresh',
        qtip: '刷新',
        handler: function(event, toolEl, header) {
            var tree = header.ownerCt
            tree.getStore().load();
            tree.getSelectionModel().deselectAll(true);
        }
    }],
    columns: [{
        header: '部门名称',
        dataIndex: 'text',
        xtype: 'treecolumn',
        flex: 1
    },/* {
        header: '主负责人',
        dataIndex: 'mainLeader'
    }, {
        header: '副负责人',
        dataIndex: 'viceLeader'
    }*/],
    listeners: {
        itemclick: function(grid, record, item, index, e) {
            var mainLayout = grid.up("panel[xtype=user.mainlayout]");
            var funData = mainLayout.funData;
            funData = Ext.apply(funData, {
                deptId: record.get("id"),
                isRight:record.get("isRight"),
                deptType:record.get("deptType")
            });
            //加载人员信息
            var userGrid = mainLayout.down("panel[xtype=user.usergrid]");
            var store = userGrid.getStore();
            var proxy = store.getProxy();
            var filterArry = new Array();
            proxy.extraParams = {
                deptId: record.get("id")
            };
            store.load();
            // var mainLayout = view.up("panel[xtype=teachercourse.mainlayout]");
            // var treePanel = mainLayout.down("panel[xtype=classteacher.classtree]");
            // var filter = "[{'type':'string','comparison':'=','value':'" + record.get("id") + "','field':'claiId'}";
            // filter += ",{'type':'integer','comparison':'=','value':0,'field':'isDelete'}]";
            // var funData = mainLayout.funData;
            // mainLayout.funData = Ext.apply(funData, {
            //     claiId: record.get("id"),
            //     claiLevel: record.get("level"),
            //     filter: filter
            // });
            // // 加载人员信息
            // var storeyGrid = mainLayout.down("panel[xtype=teachercourse.TeacherCourseGrid]");
            // var store = storeyGrid.getStore();
            // var proxy = store.getProxy();
            // proxy.extraParams = {
            //     filter: filter,
            //     page: 1
            // };
            // store.load(); // 给form赋值
        }
    }
});
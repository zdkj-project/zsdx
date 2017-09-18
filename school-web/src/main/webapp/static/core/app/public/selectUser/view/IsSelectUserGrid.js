Ext.define("ccore.public.selectUser.view.IsSelectUserGrid", {
    extend: "Ext.grid.Panel",
    alias: "widget.pubselect.isselectusergrid",
    ref: 'isselectusergrid',
    title: "<font color='#ffeb00'>已选用户(选中后向左拖动或双击移除）</font>",
    columnLines: true,
    loadMask: true,
    multiSelect: true,
    selModel: {
        selType: "checkboxmodel",
        width: 10
    },
    viewConfig: {
        stripeRows: true
    },
    /*
    store: {
        type: "class.isselectedteacherStore"
    },*/
    columns: [{
        xtype: "rownumberer",
        flex: 0,
        width: 50,
        text: '序号',
        align: 'center'
    }, {
        width: 70,
        text: "姓名",
        dataIndex: "xm"
    }, {
        width: 50,
        text: "性别",
        dataIndex: "xbm",
        renderer: function(value){
            return value == 1 ? "男" : "女";
        }
    }, {
        flex:1,
        text: "部门",
        dataIndex: "deptName"
    }, {
        width: 120,
        text: "岗位",
        dataIndex: "jobName"
    }],
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            ddGroup: "DrapDropGroup"
        },
        listeners: {
            drop: function (node, data, dropRec, dropPosition) {
            },
            beforeitemdblclick: function (grid, record, item, index, e, eOpts) {
                IsSelectStore = grid.getStore();
                IsSelectStore.removeAt(index);

                var basePanel = grid.up("panel[xtype=pubselect.selectuserlayout]");
                var selectGrid = basePanel.down("panel[xtype=pubselect.selectusergrid]");
                var selectStore = selectGrid.getStore();
                selectStore.insert(0, [record]);
                return false;
            }
        }
    }
});
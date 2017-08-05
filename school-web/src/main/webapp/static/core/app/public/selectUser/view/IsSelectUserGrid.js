Ext.define("ccore.public.selectuser.view.IsSelectUserGrid", {
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
    store: {
        type: "class.isselectedteacherStore"
    },
    columns: [{
        xtype: "rownumberer",
        flex: 0,
        width: 50,
        text: '序号',
        align: 'center'
    }, {
        width: 120,
        text: "用户名",
        dataIndex: "userName"
    }, {
        width: 70,
        text: "姓名",
        dataIndex: "xm"
    }, {
        width: 50,
        text: "性别",
        dataIndex: "xbm",
        columnType: "basecombobox",
        ddCode: "XBM"
    }, {
        width: 150,
        text: "部门",
        dataIndex: "deptName"
    }, {
        width: 110,
        text: "岗位",
        dataIndex: "jobName"
    }, {
        text: "编制",
        dataIndex: "zxxbzlb",
        ddCode: "ZXXBZLB",
        columnType: "basecombobox",
        minWidth: 100,
        flex: 1,
        align: 'left'
    }],
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            ddGroup: "DrapDropGroup"
            //dragGroup: 'secondGridDDGroup',
            //dropGroup: 'firstGridDDGroup'
        },
        listeners: {
            drop: function (node, data, dropRec, dropPosition) {
                //var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
                //Ext.example.msg("Drag from right to left", 'Dropped ' + data.records[0].get('name') + dropOn);
            },
            beforeitemdblclick: function (grid, record, item, index, e, eOpts) {
                IsSelectStore = grid.getStore();
                IsSelectStore.removeAt(index);

                var basePanel = grid.up("panel[xtype=role.selectuserlayout]");
                var selectGrid = basePanel.down("panel[xtype=role.selectusergrid]");
                var selectStore = selectGrid.getStore();
                selectStore.insert(0, [record]);
                return false;
            }
        }
    },
});
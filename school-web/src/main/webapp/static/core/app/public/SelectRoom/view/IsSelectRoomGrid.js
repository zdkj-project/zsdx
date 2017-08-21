Ext.define("ccore.public.SelectRoom.view.IsSelectRoomGrid", {
    extend: "Ext.grid.Panel",
    alias: "widget.pubselect.isselectroomgrid",
    ref: 'isselectroomgrid',
    title: "<font color='#ffeb00'>已选房间(选中后向左拖动或双击移除）</font>",
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
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 100,
            text: "所属楼栋",
            dataIndex: "areaUpName"
        }, {
            width: 100,
            text: "所属楼层",
            dataIndex: "areaName"
        }, {
            flex: 1,
            text: "房间名称",
            dataIndex: "roomName"
        }/*, {
            width: 100,
            text: "房间类型",
            dataIndex: "roomType",
            columnType: "basecombobox",
            ddCode: "FJLX"
        }*/]
    },
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

                var basePanel = grid.up("panel[xtype=pubselect.selectroomlayout]");
                var selectGrid = basePanel.down("panel[xtype=pubselect.selectroomgrid]");
                var selectStore = selectGrid.getStore();
                selectStore.insert(0, [record]);
                return false;
            }
        }
    }
});
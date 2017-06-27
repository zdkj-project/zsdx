/**
 * Created by luoyibo on 2017-05-26.
 */
Ext.define("core.train.class.view.IsSelectBzrGrid", {
    extend: "Ext.grid.Panel",
    alias: "widget.class.isselectedbzrgrid",
    ref:'isselectedbzrgrid',
    title: "<font color='#ffeb00'>已选班主任(选中后向左拖动或双击移除）</font>",
    columnLines: true,
    loadMask: true,
    multiSelect: true,
    selModel: {
        selType: "checkboxmodel",
        width:10
    },
    viewConfig: {
        stripeRows: true
    },
    store:{
        type:"class.isselectedbzrStore"
    },
    columns: [
        { text: '教师ID',  dataIndex: 'uuid', hidden:true },
        { text: '电话号码',  dataIndex: 'mobilePhone', hidden:true },
        { text: '身份证号码',  dataIndex: 'sfzjh', hidden:true },
        { text: '姓名', dataIndex: 'xm', flex: 1 },
        { text: '性别',  dataIndex: 'xbm', flex: 1,
            renderer:function(value){
                if(value=="1")
                    return "男";
                else if(value=="2")
                    return "女";
                else
                    return "";
            }
        },
        { text: '工作单位', dataIndex: 'workUnits', flex: 2 },
    ],
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            ddGroup: "DrapDropGroup"
            //dragGroup: 'secondGridDDGroup',
            //dropGroup: 'firstGridDDGroup'
        },
        listeners: {
            drop: function(node, data, dropRec, dropPosition) {
                //var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
                //Ext.example.msg("Drag from right to left", 'Dropped ' + data.records[0].get('name') + dropOn);
            },
            beforeitemdblclick: function(grid, record, item, index, e, eOpts) {
                selectStore = grid.getStore();
                selectStore.removeAt(index);

                var basePanel = grid.up("panel[xtype=class.selectbzr.mainlayout]");
                var isSelectGrid = basePanel.down("panel[xtype=class.selectteachergrid]");
                var isSelectStore = isSelectGrid.getStore();
                isSelectStore.insert(0, [record]);
                return false;
            }
        }
    },
});
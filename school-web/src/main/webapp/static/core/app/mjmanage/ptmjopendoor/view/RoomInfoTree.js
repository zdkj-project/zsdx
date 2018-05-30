Ext.define("core.mjmanage.ptmjopendoor.view.RoomInfoTree", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.mjmanage.ptmjopendoor.roominfotree",
    //dataUrl: comm.get('baseUrl') + "/BaseMjUserright/treelist",
    dataUrl: comm.get('baseUrl') + "/BaseMjUserright/treelist",
    model: "com.zd.school.plartform.comm.model.CommTree",
    expandFirst:true,
    sortableColumns:false,
    multiSelect:false,
    selModel: {
    },
    tbar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '区域信息',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:600
            }
        }, '->',{
            xtype: 'button',
            text: '刷新',
            ref: 'gridRefresh',
            iconCls: 'x-fa fa-refresh',
            titleAlign:'right',
        }]
    },
    extParams: {
        whereSql: "",
        orderSql: "",
        excludes:"checked"
    },
    columns:{
        defaults:{
            titleAlign:"center"
        },
        items:[{
            text: "区域名称",
            dataIndex: "text",
            xtype:'treecolumn',
            flex: 1
        },/* {
            text: "顺序号",
            dataIndex: "orderIndex",
            width:60
        },*/{
            text:"主键",
            dataIndex:'id',
            hidden:true
        }]
    }
})
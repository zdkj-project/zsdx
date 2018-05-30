Ext.define("core.mjmanage.userauthority.view.DeptTree", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.mjmanage.userauthority.depttree",
    dataUrl: comm.get('baseUrl') + "/BaseOrg/treelist",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.baseset.model.BaseOrgTree", "checked").modelName,
    al: true,
    expandFirst:true,
    scrollable:true,
   // title: "部门列表",
    selModel: {
        selType: ""
    },
    extParams: {
        whereSql: " and isDelete='0' ",
        orderSql: " order by parentNode,orderIndex asc",
        excludes:"checked"      //排除这个字段，不显示复选框
    },

    columnLines:false,
    multiSelect: false,
    lines:true,
    useArrows: false,
    viewConfig: {
        stripeRows: false
    },
     tbar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '部门列表',
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
    columns:{
        defaults:{
            titleAlign:"center"
        },
        items:[{
            text: "部门名称",
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
    },
});

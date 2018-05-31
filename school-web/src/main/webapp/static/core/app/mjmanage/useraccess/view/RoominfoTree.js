Ext.define("core.mjmanage.useraccess.view.RoominfoTree", {
	extend: "core.base.view.BaseTreeGrid",
    alias: "widget.mjmanage.useraccess.roominfotree",
    dataUrl: comm.get('baseUrl') + "/BaseMjUserright/treelist",
    model: "com.zd.school.plartform.comm.model.CommTree",
    al: true,
    expandFirst:true,
    forceFit:true,
    sortableColumns:false,
    selModel: {
      
    },
    extParams: {
        whereSql: "",
        excludes:"checked"
    },
    tbar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '区域信息',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        }, '->',{
            xtype: 'button',
            text: '刷新',
            ref: 'gridRefresh',
            iconCls: 'x-fa fa-refresh'
        }]
    },
    columns:  {        
        defaults:{
            titleAlign:"center"
        },
        items: [{
            text: "区域名称",
            dataIndex: "text",
            xtype: 'treecolumn',
        }, {
            text: "主键",
            dataIndex: 'id',
            hidden: true
        }]
    },
});
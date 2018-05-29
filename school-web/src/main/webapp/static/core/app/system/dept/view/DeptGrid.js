Ext.define("core.system.dept.view.DeptGrid", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.dept.deptgrid",
    //dataUrl: comm.get('baseUrl') + "/BaseOrg/treelist",
    //model: factory.ModelFactory.getModelByName("com.zd.school.plartform.baseset.model.BaseOrgTree", "checked").modelName,
    dataUrl: comm.get('baseUrl') + "/BaseOrg/chktreelist",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.baseset.model.BaseOrgChkTree", "checked").modelName,
    al: true,
    selModel: null,    
    extParams: {
        whereSql: "and isDelete='0' ",
        orderSql: " order by parentNode,orderIndex asc"
    },
    tbar: [{
        xtype: 'button',
        text: '添加下级',
        ref: 'gridAdd_Tab',
        funCode: 'girdFuntionBtn',
        iconCls: 'x-fa fa-plus-circle',
        disabled:true
    }, {
        xtype: 'button',
        text: '添加同级',
        ref: 'gridAddBrother_Tab',
        funCode: 'girdFuntionBtn',
        iconCls: 'x-fa fa-plus-circle',
        disabled:true
    }/*, {
        xtype: 'button',
        text: '修改',
        ref: 'gridEdit_Tab',
        funCode: 'girdFuntionBtn',
        iconCls: 'x-fa fa-pencil-square',
        disabled:true
    }*/, {
        xtype: 'button',
        text: '同步数据',
        ref: 'sync',
        funCode:'girdFuntionBtn',         
        iconCls: 'x-fa fa-rss'
    },{
        xtype: 'button',
        text: '删除',
        ref: 'gridDelete',
        iconCls: 'x-fa fa-minus-circle',
        disabled:true
    }, {
        xtype: 'button',
        text: '刷新',
        ref: 'gridRefresh',
        iconCls: 'x-fa fa-refresh'
    }],
    
    columns:  {        
        defaults:{
            flex:1,
            align:'center',
            titleAlign:"center"
        },
        items: [{
            header: '部门名称',
            dataIndex: 'text',
            xtype: 'treecolumn',
            //flex:false,
            align:'left',
            width: 300
        }, {
            header: '排序号',
            width:80,
            dataIndex: 'orderIndex'
        }, {
            header: '部门类型',
            width:100,
            dataIndex: 'deptType',
            columnType: "basecombobox", //列类型
            ddCode: "DEPTTYPE" //字典代码

        }, {
            header: '主负责岗位',
            dataIndex: 'mainLeaderName'
        }, {
            header: '副负责岗位',
            dataIndex: 'viceLeaderName'
        }, {
            header: '内线电话',
            dataIndex: 'outPhone'
        }, {
            header: '外线电话',
            dataIndex: 'inPhone'
        }]
    }
});
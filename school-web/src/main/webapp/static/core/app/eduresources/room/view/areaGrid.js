Ext.define("core.eduresources.room.view.areaGrid", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.room.areagrid",
    dataUrl: comm.get('baseUrl') + "/BuildRoomarea/list",
    model: "com.zd.school.build.define.model.BuildRoomAreaTree",
    al: true,
    selModel: {
        selType: "checkboxmodel",
        headerWidth: 50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        mode:'single'
    },
    extParams: {
        whereSql: "and isDelete='0' ",
        orderSql: "",
        excludes:"checked"
            //filter: "[{'type':'string','comparison':'=','value':'0','field':'isDelete'}]"
    },
    title: "区域信息",
    tbar: [{
        xtype: 'button',
        text: '添加下级',
        ref: 'gridAdd',
        iconCls: 'x-fa fa-plus-circle'
        //disabled: true
    }, {
        xtype: 'button',
        text: '添加同级',
        ref: 'gridAddBrother',
        iconCls: 'x-fa fa-plus-circle'
        //disabled: true
    }, {
        xtype: 'button',
        text: '修改',
        ref: 'gridEdit',
        iconCls: 'x-fa fa-pencil-square'
        //disabled: true
    }, {
        xtype: 'button',
        text: '删除',
        ref: 'gridDelete',
        iconCls: 'x-fa fa-minus-circle'
        //disabled: true
    }, {
        xtype: 'button',
        text: '刷新',
        ref: 'gridRefresh',
        iconCls: 'x-fa fa-refresh'
    }],
    columns:  {        
        defaults:{
            titleAlign:"center"
        },
    items: [{
        text: "区域名称",
        dataIndex: "text",
        xtype: 'treecolumn',
        //width: 250
        flex:2
    }, {
        text: "区域类型",
        dataIndex: "areaType",
        columnType: "basecombobox", //列类型
        ddCode: "BUILDAREATYPE", //字典代码        
        flex:1
    }, {
        text: "顺序号",
        dataIndex: "orderIndex",
        flex:1,
    },{
        text: "区域说明",
        dataIndex: "areaDesc",   
        flex:2
    }, {
        text: "主键",
        dataIndex: 'id',
        hidden: true
    }]
    }
})
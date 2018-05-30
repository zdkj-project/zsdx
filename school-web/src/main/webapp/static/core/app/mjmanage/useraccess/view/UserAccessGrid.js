Ext.define("core.mjmanage.useraccess.view.UserAccessGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.useraccess.useraccessgrid",
    dataUrl: comm.get('baseUrl') + "/BaseMjUserright/list",
    model: "com.zd.school.control.device.model.MjUserright",
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '删除权限',
            ref: 'gridDelete',
            iconCls: 'x-fa fa-minus-circle'
        },'->'],
    }, 
    panelButtomBar:null,
    
    //排序字段及模式定义
    defSort: [{
        property: 'createTime',
        direction: 'DESC'
    }],
    extParams: {},
    columns:  {        
        defaults:{
            titleAlign:"center",
            align:'center'
        },
        items: [{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        },{
            text: "设备主键",
            dataIndex: "termId",
            hidden: true,
        },{
            text: "用户姓名",
            dataIndex: "xm",
            minWidth:100,
            flex:1
        },{
            text: "房间名称",
            dataIndex: "roomName",
            minWidth:100,
            flex:1
        },{
            text: "人员主键",
            dataIndex: "stuId",
            hidden: true,
        },{
            text: "设备名称",
            dataIndex: "termName",
            minWidth:100,
            flex:1
        },{
            text: "设备序列号",
            dataIndex: "termSN",
            minWidth:100,
            flex:1
        }]
    }    
});
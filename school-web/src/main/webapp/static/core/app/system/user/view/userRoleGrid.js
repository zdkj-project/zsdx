Ext.define("core.system.user.view.userRoleGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.user.userrolegrid",
    dataUrl: comm.get('baseUrl') + "/sysuser/userrolelist",
    title:"用户所属角色",
    noPagging:true,
    al:false,
    remoteSort:false,
  
    tbar: [{
        xtype: 'button',
        text: '添加',
        ref: 'gridAdd',
        iconCls: 'x-fa fa-plus-circle'
    }, {
        xtype: 'button',
        text: '删除',
        ref: 'gridDelete',
        iconCls: 'x-fa fa-minus-circle'
    }],
    panelTopBar:null,
    panelButtomBar:null,
    //排序字段及模式定义
    defSort: [{
        property: 'orderIndex',
        direction: 'DESC'
    }],
    extParams: {
        whereSql: "",
        filter:"[{'type':'numeric','comparison':'=','value':0,'field':'isDelete'},{'type':'string','comparison':'=','value':'0','field':'userId'}]"
    },
    model: 'com.zd.school.plartform.system.model.SysRole',

    columns: { 
        defaults:{
            //flex:1,     //【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            align:'center',
            titleAlign:"center"
        },
        items:[{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        }, {
            text: "角色名称",
            dataIndex: "roleName",
            flex:1,
        }, {
            text: "角色编码",
            dataIndex: "roleCode",
            flex:1,
        }]
    }
});
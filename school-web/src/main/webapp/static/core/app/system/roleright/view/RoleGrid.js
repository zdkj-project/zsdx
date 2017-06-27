Ext.define("core.system.roleright.view.RoleGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.roleright.rolegrid",
    dataUrl: comm.get('baseUrl') + "/sysrole/list",
    model: 'com.zd.school.plartform.system.model.SysRole',
    selModel: {
        selType: ""
    },      
    //排序字段及模式定义
    defSort: [{
        property: 'orderIndex',
        direction: 'DESC'
    }],
    extParams: {
        whereSql: "",
        filter:"[{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}]"
    },
    title: "系统角色",
    tbar: [],
    panelTopBar:false,
    panelButtomBar:false,
    columns: { 
        defaults:{
            flex:1,     //【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            align:'center',
            titleAlign:"center"
        },
        items:[{
            xtype: "rownumberer",
            flex:0,
            width: 60,
            text: '序号',
            align: 'center'
        },{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        }, {
            text: "角色名称",
            dataIndex: "roleName"
        }, {
            text: "角色编码",
            dataIndex: "roleCode"
        },{
            text: "角色说明",
            dataIndex: "remark",
            renderer: function(value,metaData) {  

                var title=" 角色说明 ";

                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';  
                return value;  
            }  

        }]
    }
})
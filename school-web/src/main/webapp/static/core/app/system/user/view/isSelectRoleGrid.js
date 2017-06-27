Ext.define("core.system.user.view.isSelectRoleGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.user.isselectrolegrid",
    dataUrl: comm.get('baseUrl') + "/sysrole/selectlist",
    title: "已选角色",
    al: false,
    noPagging: true,
    tbar: [],
    panelTopBar:null,
    panelButtomBar:null,
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            dragGroup: 'secondGridDDGroup',
            dropGroup: 'firstGridDDGroup'
        },
        listeners: {
            drop: function(node, data, dropRec, dropPosition) {
                var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
                //Ext.example.msg("Drag from right to left", 'Dropped ' + data.records[0].get('name') + dropOn);  
            }
        }
    },
    //排序字段及模式定义
    defSort: [{
        property: 'orderIndex',
        direction: 'DESC'
    }],
    extParams: {
        whereSql: "",
        filter: "[{'type':'numeric','comparison':'=','value':0,'field':'isDelete'}]"
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
            flex:1
        }, {
            text: "角色编码",
            dataIndex: "roleCode",
            flex:1
        }]
    }
});
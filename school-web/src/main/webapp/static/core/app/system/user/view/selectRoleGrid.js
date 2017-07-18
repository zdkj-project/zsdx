Ext.define("core.system.user.view.selectRoleGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.user.selectrolegrid",
    dataUrl: comm.get('baseUrl') + "/sysrole/selectlist",
    title: "待选角色(选中后拖动添加)",
    al: false,
    tbar: [],
    panelTopBar:{
        xtype:'toolbar',
        items: [ {
            xtype: 'tbtext',
            html: '待选角色(选中后拖动添加)',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        }/*,'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'roleName',
            funCode:'girdFastSearchText', 
            isNotForm:true,   //由于文本框重写了baseform下面的funcode值，所以使用这个属性，防止重写这里设定的fundcode值。
            emptyText: '请输入角色名称'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search',  
        }*/],
    },
    panelButtomBar:null,
    viewConfig: {
        stripeRows: false,
        plugins: {
            ptype: 'gridviewdragdrop',
            dragGroup: 'firstGridDDGroup',
            dropGroup: 'secondGridDDGroup'
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
    columns:  { 
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
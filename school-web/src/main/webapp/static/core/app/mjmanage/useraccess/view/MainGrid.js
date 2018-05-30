Ext.define("core.mjmanage.useraccess.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.useraccess.maingrid",
    dataUrl: comm.get('baseUrl') + "/BaseMjUserright/list",
    model: "com.zd.school.control.device.model.MjUserright",
    al:false,
    pageDisplayInfo:false,
    menuCode:"USERACCESS",
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '设备权限角色',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        },'->',{
            xtype: 'button',
            text: '删除权限',
            ref: 'gridDeletePer',
            iconCls: 'x-fa fa-minus-circle',
            disabled:true,
        },{
            xtype: 'button',
            text: '批量删除人员权限',
            ref: 'gridDeleteAll',
            iconCls: 'x-fa fa-minus-circle',
            disabled:true,
        }],
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
        	text: "角色名",
        	dataIndex: "xm",
        	flex:1,
            minWidth:100,
        },{
        	text: "设备名称",
        	dataIndex: "termName",
        	width:150
        },{
            text: "设备序列号",
            dataIndex: "termSN",
            width:150
        }]
    }    
});
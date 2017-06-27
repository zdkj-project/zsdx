Ext.define("core.good.signup.view.MainGrid", {
    extend: "Ext.grid.Panel",
    alias: "widget.signup.maingrid",

    title:'表格数据',

    forceFit: true,
    columnLines: true,
    frame: true,

    tbar: [{
        xtype: 'button',
        text: '添加',
        ref: 'add',
        iconCls: 'x-fa fa-plus-circle'
        // listeners:{
        //     beforeclick:funtion(){
        //         console.log("点击前");
        //     }
        // },
    },{
        xtype: 'button',
        text: '编辑',
        ref: 'edit',
        iconCls: 'x-fa fa-pencil-square',
        handler:'doEditClick'
    },{
        xtype: 'button',
        text: '详细',
        ref: 'detail',
        iconCls: 'x-fa fa-file-text',
        handler:'doDetailClick'
    },{
        xtype: 'button',
        text: '删除',
        ref: 'delete',
        iconCls: 'x-fa fa-minus-circle',
        handler:'doDeleteClick'
    }],

    //store: 'core.good.signup.store.SignupGridStore',  //这种方式，系统报错，所以使用下面的方式
    store: {
        type: 'signup.maingridstore',

        //.......这里可以写传入这个store的其他参数
        //model:'core.good.signup.model.SignupGridModel',
    },
    listeners:{
        itemdblclick: 'onItemdblClick'
    },
    columns: {
        items:[{
            xtype: "rownumberer",
            flex:0,
            width: 70,
            text: '序号',
            align: 'center'
        },{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        },{
            text: "活动标题",
            dataIndex: "actTitle"    
        },{
            text: "活动时间",
            dataIndex: "actDate" ,
            renderer:function(v){                    
                return Ext.Date.format(new Date(v), 'Y-m-d H:i:s');              
            }      
        }],
        defaults:{
            flex:1,
            align:'left'
        }
    },

    dockedItems: [{
        xtype: 'pagingtoolbar',
        dock: 'bottom',
        store: Ext.data.StoreManager.lookup('signup.maingridstore'),
        displayInfo: true,       
        emptyMsg: "没有可显示的数据"
    }],
});
Ext.define("core.system.ip.view.ipGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.ip.ipgrid",
    dataUrl: comm.get('baseUrl') + "/SysIp/list",
/*    tbar: [
        { xtype: 'button', text: '添加', ref: 'gridAdd', iconCls: 'x-fa fa-plus-circle' },
        { xtype: 'button', text: '编辑', ref: 'gridEdit', iconCls: 'x-fa fa-pencil-square', disabled: true },
        { xtype: 'button', text: '删除', ref: 'gridDelete', iconCls: 'x-fa fa-minus-circle' }
    ],
    panelTopBar:false,
    panelButtomBar:false,
    */
    tbar:[],
    panelTopBar:{ 
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        },{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-minus-circle'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'jobName',
            emptyText: '请输入职务名称'
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型 
            iconCls: 'x-fa fa-search',  
        }],
    },
    panelBottomBar:false,
    
    
    //排序字段及模式定义
    defSort: [{
        property: 'ipName',
        direction: 'ASC'
    }],
    extParams: {},
    model: 'com.zd.school.IpControl.IpControl.model',
    
    columns:  {        
        defaults:{        
            //align:'center',
            titleAlign:"center"
        },
    items: [{
        text: "主键",
        dataIndex: "uuid",
        hidden: true
    }, {
        text: "ip名称",
        dataIndex: "ipName",
        flex:1,
        width: 420
    }, {
        text: "ip地址",
        dataIndex: "ipUrl",
        flex:1,
        width: 420
    },{
        xtype:'actiontextcolumn',
        text:'操作',
        align:'center',
        width:200,
        fixed:true,
        items:[{
            text:'编辑',
            style:'font-size:12px;',
            tooltip:'编辑',
            ref:'gridreact',
            getClass:function(v,metadata,record,rowIndex,colIndex,store){
                if(record.get("gridreact")==1)
                    return 'x-hidden-display';
                 else
                    return null;       
            },
            handler:function(view,rowIndex,colIndex,item){
                var rec=view.getStore().getAt(rowIndex);
                this.fireEvent('gridreactClick_Tab',{
                    view:view.grid,
                    record:rec
                    });
            }
        },{
            text:'删除',
            style:'font-size:12px;',
            tooltip:'删除',
            ref:'gridDelete',
            handler:function(view,rowIndex,colIndex,item){
                 var rec=view.getStore().getAt(rowIndex);
                 this.fireEvent('deleteClick',{
                  view:view.grid,
                  record:rec
                 });
             }
        }]
    }]
    },   
});
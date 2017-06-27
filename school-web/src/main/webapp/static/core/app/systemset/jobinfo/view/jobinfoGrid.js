Ext.define("core.systemset.jobinfo.view.jobinfoGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.jobinfo.jobinfogrid",
    dataUrl: comm.get('baseUrl') + "/basejob/list",
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
        }, {
            xtype: 'button',
            text: '同步数据',
            ref: 'sync',
            funCode:'girdFuntionBtn',         
            iconCls: 'x-fa fa-rss'
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
        property: 'orderIndex',
        direction: 'ASC'
    }],
    extParams: {},
    model: 'com.zd.school.plartform.baseset.model.BaseJob',
    
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
        text: "名称",
        dataIndex: "jobName",
        //flex:1,
        width:180
    }, {
        text: "级别",
        dataIndex: "orderIndex",
        //flex:1,
        width:120
    }]
    }    
});
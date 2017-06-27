Ext.define("core.base.view.BaseQueryForm",{
	extend:"Ext.form.Panel",
	alias:"widget.basequeryform",
	layout:'column',

	frame:true,
	autoScroll : true,
	
    width:'100%',
    bodyPadding : '5',
    margin : '0',
    border:false,    
    height:50,  
    bodyStyle: 'background:#fafafa;',    
    defaults:{
        margin:"5 10 0 0",
        xtype : 'textfield',
        labelAlign : 'right',
        msgTarget: 'qtip',      
        //columnWidth : 0.5, 
    },
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:100,
        labelAlign: "right",
        width:'100%'
    },   

    items: [{    
        width:300,
        xtype: "textfield",
        fieldLabel: "姓名",
        name: "name"
    },{
        width:300,
        xtype: "combobox",
        fieldLabel: "性别",
        name: "sex",
        store: Ext.create('Ext.data.Store', {
            fields: ['name', 'value'],
            data : [
                {"name":"男", "value":true},    
                {"name":"女", "value":false}
            ]
        }),
        queryMode: 'local',
        displayField: 'name',
        valueField: 'value',
        editable:false,
    },{            
        xtype:'button',
        text:'搜 索',
        ref:'gridSearchFormOk',
        iconCls:'x-fa fa-search',
    },{            
        xtype:'button',
        text:'重 置',
        ref:'gridSearchFormReset',
        iconCls:'x-fa fa-undo',
    }]
});
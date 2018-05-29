Ext.define("core.reportcenter.traineeconsumereport.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.traineeconsumereport.mainquerypanel",
	layout: "form",
	frame: false,
	height: 100,

	fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:100,
        labelAlign: "right",
        width:'100%'
    },   
   
	items: [{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth:0.2,
			xtype: "basequeryfield",	
			queryType: "datetimefield",
			dateType:'date',		//指定这个组件的格式，date或者datetime
			dataType:'date',		//指定查询设置filter时的进行判断的类型，date或者datetime
			operationType:">=",	
			name: "beginDate",
			fieldLabel: "班级开始日期",
			format:"Y年m月d日"
		},{
			columnWidth:0.2,
			xtype: "basequeryfield",		
			queryType: "datetimefield",
			dateType:'date',
			dataType:'date',
			operationType:"<=",		//运算符
			name: "endDate",
			fieldLabel: "班级结束日期",	
			format:"Y年m月d日"
		}, {
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "CLASS_NAME",
			fieldLabel: "班级名称",
			queryType: "textfield",
		}, {
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "CLASS_NUMB",
			fieldLabel: "班级编号称",
			queryType: "textfield",
		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "DINNER_TYPE",
			fieldLabel: "就餐类型",
			queryType: "combobox",		
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"全部", "value":""},
	                {"name":"围餐", "value":"1"},	              
	                {"name":"自助餐", "value":"2"},           
	                {"name":"快餐", "value":"3"},  
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
	        value:"",
	        editable:false	
		}]
	}],
	buttonAlign: "center",
	buttons: [{
		xtype: 'button',
		text: '搜 索',
		ref: 'gridSearchFormOk',
		iconCls: 'x-fa fa-search',
	}, {
		xtype: 'button',
		text: '重 置',
		ref: 'gridSearchFormReset',
		iconCls: 'x-fa fa-undo',
	}]
});
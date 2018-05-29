Ext.define("core.reportcenter.cashreport.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.cashreport.mainquerypanel",
	layout: "form",
	frame: false,
	height: 180,

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
			fieldLabel: "收银开始日期",
			format:"Y年m月d日"
		},{
			columnWidth:0.2,
			xtype: "basequeryfield",		
			queryType: "datetimefield",
			dateType:'date',
			dataType:'date',
			operationType:"<=",		//运算符
			name: "endDate",
			fieldLabel: "收银结束日期",	
			format:"Y年m月d日"
		}, {
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "CONSUME_SERIAL",
			fieldLabel: "流水号",
			queryType: "textfield",
		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "CONSUME_USER",
			fieldLabel: "消费人",
			queryType: "combobox",		
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"全部", "value":""},
	                {"name":"个人", "value":"个人"},	              
	                {"name":"单位", "value":"单位"},           
	             
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
	        value:"",
	        editable:false

		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "CONSUME_TYPE",
			fieldLabel: "消费类型",
			queryType: "combobox",		
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"全部", "value":""},
	                {"name":"点餐", "value":"点餐"},	              
	                {"name":"围餐", "value":"围餐"},           
	                {"name":"快餐", "value":"快餐"},  
	                {"name":"普通快餐", "value":"普通快餐"},  
	                {"name":"招待快餐", "value":"招待快餐"},  
	                {"name":"无卡快餐", "value":"无卡快餐"}, 
	                {"name":"加班快餐", "value":"加班快餐"}
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
	        value:"",
	        editable:false	
		}]
	},{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "RECEPTION_DEPT",
			fieldLabel: "部门",
			queryType: "textfield",
		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "VISITOR_UNIT",
			fieldLabel: "单位",
			queryType: "textfield",
		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "CONSUME_STATE",
			fieldLabel: "交易状态",
			queryType: "combobox",		
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"全部", "value":""},
	                {"name":"成功", "value":"成功"},	 
	                {"name":"销单", "value":"销单"},            
	                {"name":"取消", "value":"取消"}
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
	        value:"",
	        editable:false
		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "CLEARING_FORM",
			fieldLabel: "结算方式",
			queryType: "combobox",		
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"全部", "value":""},
	                {"name":"现金", "value":"1"},	              
	                {"name":"刷卡", "value":"2"},
	                {"name":"签单", "value":"3"}
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
	        value:"",
	        editable:false
		},{
			columnWidth: 0.2,
			xtype: "basequeryfield",
			name: "MEAL_TYPE",
			fieldLabel: "用餐类型",
			queryType: "combobox",		
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"全部", "value":""},
	                {"name":"早餐", "value":"1"},	              
	                {"name":"午餐", "value":"2"},
	                {"name":"晚餐", "value":"3"},
	                {"name":"夜宵", "value":"4"}
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
	        value:"",
	        editable:false	
		}]
	},{
		xtype: "container",
		layout: "column",
		items: [{
			columnWidth: 0.8,
	        xtype: 'checkboxgroup',
	        fieldLabel: '统计方式',
	        // Arrange checkboxes into two columns, distributed vertically
	        //columns: 2,
	        vertical: true,
	        items: [
	            { boxLabel: '按年', name: 'GROUP_TYPE', inputValue: 'YEAR(CONSUME_DATE)',checked: false},
	            { boxLabel: '按月', name: 'GROUP_TYPE', inputValue: 'MONTH(CONSUME_DATE)',checked: false  },
	            { boxLabel: '按日', name: 'GROUP_TYPE', inputValue: 'DAY(CONSUME_DATE)',checked: false },
	            { boxLabel: '消费人', name: 'GROUP_TYPE', inputValue: 'CONSUME_USER' },	          
	            { boxLabel: '消费类型', name: 'GROUP_TYPE', inputValue: 'CONSUME_TYPE' },
	            { boxLabel: '快餐类型', name: 'GROUP_TYPE', inputValue: 'MEAL_TYPE' },
	            { boxLabel: '接待部门', name: 'GROUP_TYPE', inputValue: 'RECEPTION_DEPT' },
	            { boxLabel: '来访单位', name: 'GROUP_TYPE', inputValue: 'VISITOR_UNIT' },
	            { boxLabel: '交易状态', name: 'GROUP_TYPE', inputValue: 'CONSUME_STATE' },
	            { boxLabel: '结算方式', name: 'GROUP_TYPE', inputValue: 'CLEARING_FORM' }
	        ]
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
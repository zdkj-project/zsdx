Ext.define("core.train.class.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.class.mainquerypanel",
	layout: "form",
	frame: false,
	height: 140,

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
			columnWidth:0.25,
			xtype: "basequeryfield",
			name: "beginDate",
			fieldLabel: "开始日期",
			queryType: "datetimefield",
			dateType:'date',		//指定这个组件的格式，date或者datetime
			dataType:'date',		//指定查询设置filter时的进行判断的类型，date或者datetime
			operationType:">=",	
			
		},{
			columnWidth:0.25,
			xtype: "basequeryfield",
			name: "endDate",
			fieldLabel: "结束日期",
			queryType: "datetimefield",
			dateType:'date',
			dataType:'date',
			operationType:"<=",		//运算符

		}]
	}, {
		xtype: "container",
		layout: "column",
		items: [ {
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "classCategory",
			fieldLabel: "班级类型",
			queryType: "basecombobox",
			config: {
				ddCode: "ZXXBJLX"
			}
		},{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "className",
			fieldLabel: "班级名称",
			queryType: "textfield",		
		}/*, {
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "classNumb",
			fieldLabel: "班级编号",
			queryType: "textfield",
		},*/
		,{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			name: "contactPerson",
			fieldLabel: "联系人",
			queryType: "textfield",
		},{
			columnWidth: 0.25,
			xtype: "basequeryfield",
			queryType: "combobox",
			dataType:"numberfield",		//指定filter的数据类型为 数字
			name: "isuse",
			fieldLabel: "是否启用",
			store: Ext.create('Ext.data.Store', {
            	fields: ['name', 'value'],
	            data : [
	                {"name":"已启用", "value":1},    
	                {"name":"未启用", "value":0}
	            ]
	        }),
	        queryMode: 'local',
	        displayField: 'name',
	        valueField: 'value',
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
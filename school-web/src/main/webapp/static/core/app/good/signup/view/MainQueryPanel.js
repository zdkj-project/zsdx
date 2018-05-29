
Ext.define('core.good.signup.view.MainQueryPanel', {
	extend : "Ext.form.Panel",
	alias : "widget.signup.mainquerypanel",

	title : '筛选条件',
	border : 1,
	frame : true,
	autoScroll : true,
	animCollapse : true,
	collapsible : true,

	//width : 300,
	buttonAlign : "center",

	layout:"hbox",	
	bodyPadding : '10 20 0 20',


	defaults:{
        margin:"5 10 0 0",
        xtype : 'textfield',
        labelAlign : 'right',
        msgTarget: 'qtip',
        labelSeparator: '：', // 分隔符
        labelWidth:80, 
        //columnWidth : .3,
        flex:1        
    },

	items: [{
        xtype: 'numberfield',
        name: 'standYear',
        fieldLabel: '收费年度',
        //editable:false,
        value: new Date().getFullYear(),
        maxValue: new Date().getFullYear()+1,
        minValue: 0,
        value:'',
        editable:true,
        regex:/^[0-9]*$/,
        regexText : '只能输入数字',
        emptyText: '为空则不筛选'
    },{   
        xtype: 'numberfield',
        name: 'standPC',
        fieldLabel: '收费批次',
        //editable:false,
        value:'',
        minValue: 0,
        editable:true,
        regex:/^[0-9]*$/,
        regexText : '只能输入数字',
        emptyText: '为空则不筛选'
    },{
        fieldLabel: "班级名称",
        name: "className",
        xtype: "textfield",
        emptyText: '为空则不筛选'
    }],
	buttons: [{
		text: '查询',
		ref: 'queryBtn',
		iconCls: 'x-fa fa-search'
	}, {
		text: '重置',
		ref: 'resetBtn',
		iconCls: 'x-fa fa-undo',
		handler: function() {
            this.up('form').getForm().reset();
        }
	}]
})
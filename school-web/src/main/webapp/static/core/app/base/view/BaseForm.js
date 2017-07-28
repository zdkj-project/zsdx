Ext.define("core.base.view.BaseForm",{
	extend:"Ext.form.Panel",
	alias:"widget.baseform",
	frame:false,
	border:false,
	//layout:"column",
	layout:"form",
	autoScroll : true,
	animCollapse : false,
	bodyPadding : '10 20 0 5',
	defaults:{
		width:'100%',
		margin:"10 5",
		xtype : 'textfield',
		labelAlign : 'right',
		//columnWidth : 0.5,
		msgTarget: 'qtip',
	},
	fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:100
    },

	buttonAlign :'center',
	/*
	buttons: [{
        text: '提交',
        ref: 'submitBtn',
        iconCls: 'x-fa fa-search',
        formBind: true, //only enabled once the form is valid
        disabled: true
    }, {
        text: '重置',
        ref: 'resetBtn',
        iconCls: 'x-fa fa-undo',
        handler:function() {
            this.up('form').getForm().reset();
        }
        
    }] 
    */
});
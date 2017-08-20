Ext.define("core.train.creditrule.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.creditrule.detailform",
    xtype: 'creditruledeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
        labelAlign: "right"
    },
    items: [{
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,		
    		fieldLabel: "规则名称",
    		columnWidth: 0.5,
            name: "ruleName",
            xtype: "textfield",    
    		emptyText: "请输入规则名称",
            maxLength:36,
            maxLengthText:"最多36个字符,汉字占2个字符",	
        },{
            columnWidth: 0.5,
            xtype: "label",
            margin:'5 0 0 5 ',
            html: " （<font color=red,size=14>正常出勤的分数为对应课程分数，缺勤0分；迟到、早退会从课程分数中减去分数</font>）",
        }
  //       { 		
  //           beforeLabelTextTpl: comm.get("required"),
  //           allowBlank: false,
  //   		fieldLabel: "正常出勤学分",
  //           columnWidth: 0.5,
  //           name: "normalCredits",
  //           xtype: "numberfield",    
  //   		emptyText: "请输入正常出勤学分",
  //           minValue: 0, 
  //           maxValue:99999,     
  //           value:4
		// }
        ]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,		
    		fieldLabel: "迟到扣除学分",
    		columnWidth: 0.5,
            name: "lateCredits",
            xtype: "numberfield",
    		emptyText: "请输入迟到扣除学分",
            minValue: 0, 
            maxValue:99999,   
        },{ 		
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,      
    		fieldLabel: "早退扣除学分",
            columnWidth: 0.5,
            name: "earlyCredits",
            xtype: "numberfield",
    		emptyText: "请输入早退扣除学分",
            minValue: 0, 
            maxValue:99999,  
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items:[{         
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "规则是否启用",
            columnWidth: 0.5,
            name: "startUsing",
            xtype: "checkbox",
            boxLabel: "启用",
            value:0   
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [ {           
            fieldLabel: "规则说明",
            columnWidth: 1,
            name: "ruleDesc",
            xtype: "textarea",
            emptyText: "请输入规则说明",
            maxLength: 255,
            maxLengthText: "最多255个字符,汉字占2个字符",
        }]
	}]
});


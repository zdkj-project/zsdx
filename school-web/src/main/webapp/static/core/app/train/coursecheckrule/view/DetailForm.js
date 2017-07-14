Ext.define("core.train.coursecheckrule.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.coursecheckrule.detailform",
    xtype: 'coursecheckruledeailform',
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
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "规则名称",
            columnWidth: 0.5,
            name: "ruleName",
            xtype: "textfield",
            emptyText: "请输入规则名称",
            maxLength: 36,
            maxLengthText: "最多36个字符,汉字占2个字符",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            fieldLabel: "考勤模式",
            columnWidth: 0.5,
            name: "checkMode",
            xtype: "basecombobox",
            emptyText: "请输入考勤模式",
            ddCode: "CHECKMODE"

        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "签到提前分钟不能为空",
            fieldLabel: "签到提前分钟",
            columnWidth: 0.5,
            name: "inBefore",
            xtype: "numberfield",        
            minValue: 0,
            maxValue:99999, 
            emptyText: "请输入签到提前分钟"
        },{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            fieldLabel: "缺勤分钟",
            columnWidth: 0.5,
            name: "absenteeism",
            xtype: "numberfield",
            emptyText: "请输入缺勤分钟",    
            minValue: 0,          
            maxValue:99999,      
            value:0
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "迟到分钟不能为空",
            fieldLabel: "迟到分钟",
            columnWidth: 0.5,
            name: "beLate",
            xtype: "numberfield",
            emptyText: "请输入迟到分钟",    
            minValue: 0,
            maxValue:99999, 
        }]    
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            fieldLabel: "是否需要签退",
            columnWidth: 0.5,
            name: "needCheckout",
            xtype: "checkbox",
            boxLabel: "签退",
            value:1,
            listeners: {
                change: function(field, newValue , oldValue ) {
                    
                    var currentForm=field.up("baseform[xtype=coursecheckrule.detailform]");
                    var checkoutCtrs=currentForm.query("container[ref=checkoutCtr]");               
                    if (newValue == true) {                                                                        
                        for(var i=0;i<checkoutCtrs.length;i++){
                            checkoutCtrs[i].setVisible(true);                          
                        }                     

                    } else  {
                        for(var i=0;i<checkoutCtrs.length;i++){
                            checkoutCtrs[i].setVisible(false);                          
                        }   
                    } 
                    
                }
            }
        },{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "规则是否启用",
            columnWidth: 0.5,
            name: "startUsing",
            xtype: "checkbox",
            boxLabel: "启用",
            value:0   
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        ref:'checkoutCtr',
        items: [,{         
            fieldLabel: "签退提前分钟",
            columnWidth: 0.5,
            name: "outBefore",
            xtype: "numberfield",
            emptyText: "请输入签退提前分钟",
            minValue: 0,    
            maxValue:99999,     
            value:0
        },{           
            fieldLabel: "签退延迟分钟",
            columnWidth: 0.5,
            name: "outLate",
            xtype: "numberfield",
            emptyText: "请输入签退延迟分钟",
            minValue: 0,    
            maxValue:99999,     
            value:0
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        ref:'checkoutCtr',
        items: [{           
            fieldLabel: "早退分钟",
            columnWidth: 0.5,
            name: "leaveEarly",
            xtype: "numberfield",
            emptyText: "请输入早退分钟",
            minValue: 0,    
            maxValue:99999,     
            value:0
        }]
    },
    {
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
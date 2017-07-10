Ext.define("core.oa.meeting.checkrule.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.checkrule.detailform",
    xtype: 'checkruledeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
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
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "规则名称不能为空",
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
        blankText: "签到提前分钟不能为空",
        fieldLabel: "签到提前分钟",
        columnWidth: 0.5,
        name: "inBefore",
        xtype: "numberfield",
        emptyText: "请输入签到提前分钟",
        maxLength: 2,
        maxLengthText: "最多2个字符,汉字占2个字符",
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "迟到分钟不能为空",
        fieldLabel: "迟到分钟",
        columnWidth: 0.5,
        name: "beLate",
        xtype: "numberfield",
        emptyText: "请输入迟到分钟",
        maxLength: 2,
        maxLengthText: "最多2个字符,汉字占2个字符",
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "缺勤分钟不能为空",
        fieldLabel: "缺勤分钟",
        columnWidth: 0.5,
        name: "absenteeism",
        xtype: "numberfield",
        emptyText: "请输入缺勤分钟",
        maxLength: 2,
        maxLengthText: "最多2个字符,汉字占2个字符",
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
        	
                fieldLabel: "是否需要签退",
                columnWidth: 0.5,
                name: "needCheckout",
                xtype: "checkbox",
                boxLabel: "需要",
                emptyText: "请输入是否需要签退",
                maxLength: 2,
                maxLengthText: "最多2个字符,汉字占2个字符",
                value:1,
                listeners: {
                    change: function(field, newValue , oldValue ) {
                        
                        var currentForm=field.up("baseform[xtype=checkrule.detailform]");
                        var checkoutFields=currentForm.query("field[ref=checkoutField]");               
                        if (newValue == true) {                                                                        
                            for(var i=0;i<checkoutFields.length;i++){
                                checkoutFields[i].setVisible(true);                          
                            }                     

                        } else  {
                            for(var i=0;i<checkoutFields.length;i++){
                                checkoutFields[i].setVisible(false);                          
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
    },{
        ref:'checkoutField',
        fieldLabel: "签退提前分钟",
        columnWidth: 0.5,
        name: "outBefore",
        xtype: "numberfield",
        emptyText: "请输入签退提前分钟",
        maxLength: 2,
        maxLengthText: "最多2个字符,汉字占2个字符",
    }, {
        ref:'checkoutField',
        fieldLabel: "签退延迟分钟",
        columnWidth: 0.5,
        name: "outLate",
        xtype: "numberfield",
        emptyText: "请输入签退延迟分钟",
        maxLength: 2,
        maxLengthText: "最多2个字符,汉字占2个字符",
    }, {
        ref:'checkoutField',
        fieldLabel: "早退分钟",
        columnWidth: 0.5,
        name: "leaveEarly",
        xtype: "numberfield",
        emptyText: "请输入早退分钟",
        maxLength: 2,
        maxLengthText: "最多2个字符,汉字占2个字符",
    }, {
        fieldLabel: "规则说明",
        columnWidth: 0.5,
        name: "ruleDesc",
        xtype: "textarea",
        height: 80,
        emptyText: "请输入规则说明",
        maxLength: 255,
        maxLengthText: "最多255个字符,汉字占2个字符",
    }]
});
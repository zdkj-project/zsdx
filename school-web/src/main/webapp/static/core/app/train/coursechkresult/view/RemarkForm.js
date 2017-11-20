Ext.define("core.train.coursechkresult.view.RemarkForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.coursechkresult.remarkform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '0 15 10 0',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 60,
        labelAlign: "right"
    },
    tbar: [/*{
        xtype: 'button',
        ref: 'gridEdit',
        iconCls: 'x-fa fa-pencil-square',
        title:'编辑'
    }*/],

    items: [{
        fieldLabel: "班级",
        name: "classId",
        xtype: "textfield",
        hidden: true
    },{
        fieldLabel: "班级学员",
        name: "classTraineeId",
        xtype: "textfield",
        hidden: true
    },{
        fieldLabel: "班级课程",
        name: "classScheduleId",
        xtype: "textfield",
        hidden: true
    },{        
        fieldLabel: "备注",
        columnWidth: 1,
        name: "remark",
        xtype: "textarea",
        height:80,
        //readOnly:true,
        emptyText: "请输入备注",
        maxLength:500,
        maxLengthText:"最多500个字符,汉字占2个字符",
    }],
    // buttons: [{
    //     text: '提交',
    //     ref: 'submitBtn',
    //     iconCls: 'x-fa fa-check-square',
    //     //formBind: true, //only enabled once the form is valid
    //     //disabled: true
    // }, '  ',{
    //     text: '关闭',
    //     ref: 'closeBtn',
    //     iconCls: 'x-fa fa-reply',
    //     handler:function() {
    //         this.up('form').getForm().reset();
    //     }
        
    // }] 
});
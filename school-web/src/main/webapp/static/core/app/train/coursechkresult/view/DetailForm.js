Ext.define("core.train.coursechkresult.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.coursechkresult.detailform",
    xtype: 'coursechkresultdeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '0 10 10 0',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
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
            fieldLabel: "开始日期",
            columnWidth: 0.5,
            name: "beginDate",
            xtype: "datetimefield",
            dateType: 'date',
            format: "Y-m-d",
            emptyText: "请输入开始日期",
            maxLength: 8,
            maxLengthText: "最多8个字符,汉字占2个字符",
        }, {
            fieldLabel: "结束日期",
            columnWidth: 0.5,
            name: "endDate",
            xtype: "datetimefield",
            dateType: 'date',
            format: "Y-m-d",
            emptyText: "请输入结束日期",
            maxLength: 8,
            maxLengthText: "最多8个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "班级名称",
            columnWidth: 0.5,
            name: "className",
            xtype: "textfield",
            emptyText: "请输入班级名称",
            maxLength: 64,
            maxLengthText: "最多64个字符,汉字占2个字符",
        }, {
            fieldLabel: "班级类型",
            columnWidth: 0.5,
            name: "classCategory",
            xtype: "basecombobox",
            ddCode: "CLASSCATEGORY",
            emptyText: "请输入班级类型",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "班级编号",
            columnWidth: 0.5,
            name: "classNumb",
            xtype: "textfield",
            emptyText: "请输入班级编号",
            maxLength: 10,
            maxLengthText: "最多10个字符,汉字占2个字符",
        }, {
            fieldLabel: "是否考勤 0-不需要，1-需要",
            columnWidth: 0.5,
            name: "needChecking",
            xtype: "checkbox",
            boxLabel: "考勤",
            emptyText: "请输入是否考勤 0-不需要，1-需要",
            maxLength: 2,
            maxLengthText: "最多2个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "是否同步学员",
            columnWidth: 0.5,
            name: "needSynctrainee",
            xtype: "checkbox",
            boxLabel: "同步",
            emptyText: "请输入是否同步学员",
            maxLength: 2,
            maxLengthText: "最多2个字符,汉字占2个字符",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "班级简介不能为空",
            fieldLabel: "班级简介",
            columnWidth: 0.5,
            name: "classDesc",
            xtype: "textfield",
            emptyText: "请输入班级简介",
            maxLength: 256,
            maxLengthText: "最多256个字符,汉字占2个字符",
        }]
    }]
});
Ext.define("core.train.coursecategory.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.coursecategory.detailform",
    xtype: 'coursecategorydeailform',
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
            fieldLabel: "课程分类ID",
            columnWidth: 0.5,
            name: "categoryId",
            xtype: "textfield",
            emptyText: "请输入课程分类ID",
            maxLength: 36,
            maxLengthText: "最多36个字符,汉字占2个字符",
        }, {
            fieldLabel: "课程名称",
            columnWidth: 0.5,
            name: "courseName",
            xtype: "textfield",

            emptyText: "请输入课程名称",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "课程编码不能为空",
            fieldLabel: "课程编码",
            columnWidth: 0.5,
            name: "courseCode",
            xtype: "textfield",
            emptyText: "请输入课程编码",
            maxLength: 32,
            maxLengthText: "最多32个字符,汉字占2个字符",
        }, {
            fieldLabel: "课时",
            columnWidth: 0.5,
            name: "period",
            xtype: "textfield",
            emptyText: "请输入课时",
            maxLength: 2,
            maxLengthText: "最多2个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "课时时长",
            columnWidth: 0.5,
            name: "periodTime",
            xtype: "textfield",
            emptyText: "请输入课时时长",
            maxLength: 2,
            maxLengthText: "最多2个字符,汉字占2个字符",
        }, {
            fieldLabel: "学分",
            columnWidth: 0.5,
            name: "credits",
            xtype: "textfield",

            emptyText: "请输入学分",
            maxLength: 2,
            maxLengthText: "最多2个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "课程简介不能为空",
            fieldLabel: "课程简介",
            columnWidth: 0.5,
            name: "courseDesc",
            xtype: "textfield",
            emptyText: "请输入课程简介",
            maxLength: 1024,
            maxLengthText: "最多1024个字符,汉字占2个字符",
        }]
    }]
});
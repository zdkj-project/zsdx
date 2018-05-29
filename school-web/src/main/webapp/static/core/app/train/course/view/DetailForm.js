Ext.define("core.train.course.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.course.detailform",
    xtype: 'coursedeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '0 15 10 0',
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
        fieldLabel: "课程分类ID",
        name: "categoryId",
        xtype: "textfield",
        hidden: true
    }, {
        fieldLabel: "课程分类编码",
        name: "categoryCode",
        xtype: "textfield",
        hidden: true
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            fieldLabel: "课程名称",
            columnWidth: 0.5,
            name: "courseName",
            xtype: "textfield",
            emptyText: "请输入课程名称",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            columnWidth: 0.5,
            fieldLabel: "课程分类",
            name: "categoryName",
            xtype: "basetreefield",
            rootId: "ROOT",
            configInfo: {
                width: 500,
                height: 600,
                multiSelect: false,
                fieldInfo: "categoryName~categoryId~categoryCode,text~id~nodeCode",
                whereSql: "and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex",
                url: comm.get('baseUrl') + "/TrainCoursecategory/treelist",
            },
            allowBlank: false,
            emptyText: "请选择课程分类"
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: "",
            allowBlank: true,
            columnWidth: 0.5,
            xtype: "basefuncfield",
            refController: "course.otherController", //该功能主控制器
            funcPanel: "course.selectteacher.mainlayout", //该功能显示的主视图
            formPanel: "course.detailform",
            funcTitle: "选择主讲教师", //查询窗口的标题
            configInfo: {
                width: 1200,
                height: 650,
                fieldInfo: "mainTeacherId~mainTeacherName,uuid~xm",
                whereSql: "and isDelete='0' ",
                orderSql: " order by createTime DESC ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '主讲教师',
            emptyText: "请选择主讲教师",
            name: "mainTeacherName"
        }, {
            fieldLabel: "主讲老师id",
            name: "mainTeacherId",
            xtype: "textfield",
            hidden: true
        }, {
            beforeLabelTextTpl: "",
            allowBlank: false,
            fieldLabel: "是否团队",
            columnWidth: 0.333,
            name: "courseMode",
            xtype: "checkbox",
            boxLabel: "<font color='red'>(主讲教师是团队时请勾选此项)</font>",
            emptyText: "主讲教师是团队时请勾选此项",
            maxLength: 2,
            maxLengthText: "最多2个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: "",
            allowBlank: true,
            blankText: "",
            fieldLabel: "教学形式",
            columnWidth: 0.5,
            name: "teachType",
            xtype: "basecombobox",
            ddCode: "TEACHTYPE",
            emptyText: "请选择教学形式",
            maxLength: 32,
            maxLengthText: "最多32个字符,汉字占2个字符"
        }, {
            fieldLabel: "课时时长",
            columnWidth: 0.5,
            name: "periodTime",
            xtype: "numberfield",
            emptyText: "请输入课时时长",
            minValue: 0,
            maxValue: 99999,
            value:150
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "学分",
            columnWidth: 0.5,
            name: "credits",
            xtype: "numberfield",
            emptyText: "请输入学分",
            minValue: 0,
            maxValue: 100
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            blankText: "课程简介不能为空",
            fieldLabel: "课程简介",
            columnWidth: 1,
            name: "courseDesc",
            xtype: "htmleditor",
            height: 360,
            emptyText: "请输入课程简介",
            maxLength: 4096,
            maxLengthText: "最多1024个字符,汉字占2个字符",
        }]
    }]
});
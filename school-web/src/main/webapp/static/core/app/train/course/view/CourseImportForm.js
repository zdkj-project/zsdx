Ext.define("core.train.course.view.CourseImportForm", {
    extend: "Ext.form.Panel",
    alias: "widget.course.courseimportform",
    //xtype: 'classcourseimportform',
    //layout: "auto",
    //align: "left",
    frame: false,
    bodyPadding: 10,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:80,
        labelAlign : 'right',
        margin:'10',
        anchor: '100%',
    },

    items: [{
        xtype: 'label',
        text: "支持文件格式：xls、xlsx",
    }/*, {
        fieldLabel: "课程分类id",
        name: "categoryId",
        xtype: "textfield",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get("required"),
        columnWidth: 0.5,
        fieldLabel: "课程分类",
        name: "categoryName",
        xtype: "basetreefield",
        rootId: "ROOT",          
        configInfo: {
            width:400,
            height:500,
            multiSelect: false,
            fieldInfo: "categoryName~categoryId,text~id",
            whereSql: "and isDelete='0' ",
            orderSql: " order by createTime DESC,parentCategory",
            url:comm.get('baseUrl') + "/TrainCoursecategory/treelist",
        },
        allowBlank: false,
        emptyText: "请选择课程分类"
    }*/,{
        beforeLabelTextTpl: comm.get("required"),
        xtype: 'filefield',
        fieldLabel: '文 件',
        name: 'file',
        allowBlank: false,
        blankText: '请上传Excel文件',
        emptyText:'请上传Excel文件',
        buttonText:"选择文件",
    }],
    buttonAlign: 'center',
    buttons: [ {
        xtype: "button",
        text: "导入",
        ref: "formSave",
        iconCls: "x-fa fa-check-square"

    }, {
        xtype: "button",
        text: "关闭",
        ref: "formClose",
        iconCls: "x-fa fa-reply"
    }]

});
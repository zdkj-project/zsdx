Ext.define("core.train.class.view.CourseImportForm", {
    extend: "Ext.form.Panel",
    alias: "widget.class.courseimportform",
    //xtype: 'classcourseimportform',
    //layout: "auto",
    //align: "left",
    frame: false,
    bodyPadding: 10,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelWidth:70,
        labelAlign : 'right',
        margin:'10',
        anchor: '100%',
    },

    items: [{
        xtype: 'label',
        text: "支持文件格式：xls、xlsx",
    }, {
        fieldLabel: "所属班级id",
        name: "classId",
        xtype: "textfield",
        hidden: true
    }, {
        fieldLabel: "所属班级",
        name: "className",
        xtype: "textfield",
        readOnly:true
    },{
        beforeLabelTextTpl: comm.get("required"),
        xtype: 'filefield',
        fieldLabel: '文 件',
        name: 'file',
        allowBlank: false,
        blankText: '请上传Excel文件',
        emptyText:'请上传Excel文件',
        buttonText:"选择文件",
    },{
        xtype: 'label',
        html: "<span style='padding:5px;color: #de3a2e;font-weight: 400;'>* 温馨提示：若在导入的数据中，存在与此班级课程相同课程名、教师的课程时，将会自动替换此班级中现有的数据； 当课程库中不存在此课程时，则会自动将此课程同步录入到课程库！<span>",
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
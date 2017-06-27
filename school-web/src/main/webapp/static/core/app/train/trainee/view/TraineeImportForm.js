Ext.define("core.train.trainee.view.TraineeImportForm", {
    extend: "Ext.form.Panel",
    alias: "widget.trainee.traineeimportform",
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
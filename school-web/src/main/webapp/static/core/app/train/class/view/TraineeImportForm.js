Ext.define("core.train.class.view.TraineeImportForm", {
    extend: "Ext.form.Panel",
    alias: "widget.class.traineeimportform",
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
        fieldLabel: "是否同步",
        name: "needSynctrainee",
        xtype: "checkbox",
        //value: '1',
        boxLabel: "同步到学员库" 
    },{
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
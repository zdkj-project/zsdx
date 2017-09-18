Ext.define("core.system.appupdate.view.DetailForm", {
    extend: "Ext.form.Panel",
    alias: "widget.appupdate.deailform",
    xtype: 'detailform',
    //layout: "auto",
    //align: "left",
    fileUpload: true,
    bodyPadding: 10,
    defaults: { // defaults are applied to items, not the container
        labelWidth:80,
        anchor: '100%',
        labelAlign:'right',
        margin:'10'
    },
    items: [{
        xtype: 'label',
        text: "支持文件格式：APK | IPA",
        margin:'10'
    }, {
        fieldLabel: '主键',
        xtype: "textfield",
        name: "uuid",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get("required"),
        xtype: 'filefield',
        fieldLabel: '文 件',
        fileUpload: true,
        name: 'file',
        allowBlank: false,
        blankText: '请上传文件',
        buttonText:"选择文件",
    },{
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        xtype: "textfield",
        fieldLabel: "APP标题",
        blankText : "APP标题不能为空",
        name: "appTitle",
        maxLength: 20,
        emptyText: '请输入APP标题(最大20个字符)'
    },{
        beforeLabelTextTpl: comm.get("required"),
        xtype: "basecombobox",
        fieldLabel: "APP类型",
        name: "appType",
        ddCode: "APPTYPE",
        allowBlank: false,
        blankText: "",
    },{
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        emptyText: 'APP版本号',
        blankText: "APP版本号不能为空",
        fieldLabel: 'APP版本号',
        xtype: 'numberfield',
        name: "appVersion",
        flex: 1
    },{
        beforeLabelTextTpl: "",
        allowBlank: true,
        xtype: "textarea",
        fieldLabel: "APP描述",
        name: "appIntro",
        maxLength: 100
    }],
    buttonAlign: 'center',
    buttons: [{
        xtype: "button",
        text: "保存",
        ref: "formSave",
        iconCls: "x-fa fa-check-square"

    }, '  ', {
        xtype: "button",
        text: "关闭",
        ref: "formClose",
        iconCls: "x-fa fa-reply"
    }]

});
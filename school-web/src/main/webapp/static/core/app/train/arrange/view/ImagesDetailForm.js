Ext.define("core.train.arrange.view.ImagesDetailForm", {
    extend: "Ext.form.Panel",
    alias: "widget.arrange.imagesdeailform",
    //layout: "auto",
    //align: "left",
    frame: true,
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
        text: "支持文件格式：PNG | JPG | JPEG（推荐比例：3:4）",
        margin:'10'
    }, {
        fieldLabel: '主键',
        xtype: "textfield",
        name: "uuid",
        hidden: true
    },{
        fieldLabel: '实体名称',
        xtype: "textfield",
        name: "entityName",
        hidden: true
    },{
        fieldLabel: '班级ID',
        xtype: "textfield",
        name: "recordId",
        hidden: true
    },{
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        xtype: "textfield",
        fieldLabel: "班级标题",
        blankText : "班级标题不能为空",
        name: "className",
        readOnly:true
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
        blankText : "图片标题不能为空",
        fieldLabel: "图片标题",
        name: "attachName",
        maxLength: 20,
        emptyText: '请输入图片标题(最大20个字符)'
    }],
    buttonAlign: 'center',
    buttons: [ {
        xtype: "button",
        text: "保存",
        ref: "formSave",
        iconCls: "x-fa fa-check-square"

    }, {
        xtype: "button",
        text: "关闭",
        ref: "formClose",
        iconCls: "x-fa fa-reply"
    }]

});
Ext.define("core.base.view.BaseImportExcel", {
    extend: "Ext.form.Panel",
    alias: "widget.baseimportexcel",
    layout: "auto",
    align : "right",
    frame: true,
    fieldDefaults: { //统一设置表单字段默认属性
        labelSeparator: '：', //分隔符
        labelWidth: 120, //标签宽度
        msgTarget: 'side',
        labelStyle : "text-align:right;", 
    },
    items: [{
        xtype: 'label',
        text: "支持文件格式：xls、xlsx"
    }, {
        xtype: 'fileuploadfield',
        id: 'form-file',
        emptyText: '选择要上传的文件',
        fieldLabel: '文件',
        name: 'file',
        buttonText: '选择....',
        buttonCfg: {
            iconCls: 'upload-icon'
        }
    }]
});

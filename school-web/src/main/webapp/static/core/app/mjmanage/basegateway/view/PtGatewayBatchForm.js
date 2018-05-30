Ext.define("core.mjmanage.basegateway.view.PtGatewayBatchForm", {
    extend: "Ext.form.Panel",
    alias: "widget.mjmanage.basegateway.ptgatewaybatchform",
    layout: "form", //从上往下布局
    //autoHeight: true,
    height:100,
    frame: false,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        margin:'10',
    },
    items: [ {
        beforeLabelTextTpl: comm.get("required"),
        xtype: "combobox",
        store: {
                type: 'mjmanage.basegateway.ptgatewaystore',
            //.......这里可以写传入这个store的其他参数
            //model:'core.good.signup.model.SignupGridModel',
        },
        fieldLabel: "综合前置",
        name: "frontserverId",
        displayField: 'frontServerName',
        valueField: 'uuid',
        allowBlank: false,
        blankText: "综合前置不能为空",
        editable:false
    }],
    buttonAlign: 'center',
    buttons: [{
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
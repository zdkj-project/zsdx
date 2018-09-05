Ext.define("core.train.class.view.SendInfoForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.sendinfoform",
    xtype: 'sendinfoform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 20 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
        labelAlign: "right"
    },
    defaults: {
        width: '100%',
        margin: "10 5 0 5"
    },
    items: [{
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    }, {
        fieldLabel: "主键",
        name: "sendUserId",
        xtype: "textfield",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        width:600,
        xtype: "basefuncfield",
        refController: "class.otherController", //该功能主控制器，这里重新指定为当前视图的控制器了
        funcPanel: "selectuser.selectuserlayout", //该功能显示的主视图
        formPanel: "class.sendinfoform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
        funcTitle: "短信通知人员选择", //查询窗口的标题
        configInfo: {
            width: 1200,
            height: 650,
            fieldInfo: "sendUserId~sendUserName,uuid~xm",
            //whereSql: "and isDelete='0' ",
            //orderSql: " order by createTime DESC ",
            muiltSelect: true //是否多选
        },
        fieldLabel: '短信通知人员',
        emptyText: "请选择短信通知人员",
        name: "sendUserName"
    }, {
        beforeLabelTextTpl: comm.get('required'),
        fieldLabel: "消息内容",
        name: "sendInfo",
        xtype: "textarea",
        width:600,
        height:150
    }]
    //}]
});
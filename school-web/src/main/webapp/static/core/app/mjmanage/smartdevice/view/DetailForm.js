Ext.define("core.mjmanage.smartdevice.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.smartdevice.detailform",
    layout: "form",
    autoHeight: true,
    frame: false,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        msgTarget: 'qtip',
        labelAlign: "right",
    },
    items: [{
        xtype: "textfield",
        name: "uuid",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "combobox",
        name: "gatewayID",
        displayField: 'gatewayName',
        valueField: 'uuid',
        allowBlank: false,
        fieldLabel: "网关",
        emptyText: '请选择...',
        blankText: '请选择网关',
        mode: 'local',
        editable: false,
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "textfield",
        fieldLabel: "设备名称",
        emptyText: '设备名称',
        name: "termName"
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "textfield",
        fieldLabel: "设备类型",
        emptyText: '设备类型',
        name: "termTypeID",
        columnType: "basecombobox", //列类型
        ddCode: "PTTERMTYPE", //字典代码
        readOnly:true
    }, {
        xtype: "combobox",
        fieldLabel: "设备状态",
        displayField: 'termStatus',
        valueField: 'id',
        name: "termStatus",
        value: 0,
        triggerAction: 'all',
        emptyText: '请选择...',
        blankText: '请选择状态',
        editable: false,
        mode: 'local'
    },{
        columnWidth: .5,
        beforeLabelTextTpl: comm.get("required"),
        fieldLabel: "所属房间",
        name: "roomName",
        allowBlank: false,
        xtype: "basetreefield",
        rootId: "ROOT",
        configInfo: {
            multiSelect: false,
            fieldInfo: "roomName~roomId,text~id",
            url: comm.get('baseUrl') + "/PtTerm/treelist",
        }
    }, {
        xtype: "textfield",
        name: "roomId",
        hidden:true
    }]

});
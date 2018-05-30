var combostore = new Ext.data.ArrayStore({
    fields: ['id', 'termStatus'],
    data: [
        [0, '禁用'],
        [1, '启用']
    ]
});
Ext.define("core.mjmanage.deviceallot.view.DetailForm", {
	extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.deviceallot.detailform",
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        msgTarget: 'qtip',
        labelAlign: "right",
        labelWidth: 100,     //label 的寬度
    },
    items: [{
        xtype: "textfield",
        fieldLabel: "主键",
        name: "uuid",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "textfield",
        fieldLabel: "设备名称",
        emptyText: '设备名称',
        name: "termName"
    }, {
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
            url: comm.get('baseUrl') + "/BasePtIrRoomDevice/treelist",
        }
    }, {
        xtype: "textfield",
        name: "roomId",
        hidden: true
    },{
        columnWidth:0.5,
        xtype: "combobox",
        store: combostore,
        fieldLabel: "设备状态",
        displayField: 'termStatus',
        valueField: 'id',
        name: "termStatus",
        value: "0",
        triggerAction: 'all',
        emptyText: '请选择...',
        blankText: '请选择设备状态',
        editable: false,
        mode: 'local'
    }]
});
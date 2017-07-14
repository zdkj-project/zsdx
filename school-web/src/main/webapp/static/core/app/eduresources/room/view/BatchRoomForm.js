var combostore = new Ext.data.ArrayStore({
    fields: ['id', 'roomNet'],
    data: [
        ["0", '有网络'],
        ["1", '无网络']
    ]
});
Ext.define("core.eduresources.room.view.BatchRoomForm", {
	extend: "core.base.view.BaseForm",
    //id: "room.BatchRoomForm",
    alias: "widget.room.BatchRoomForm",
    layout: "form",
    frame: false,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    items: [{
        xtype: "textfield",
        fieldLabel: "主键",
        name: "uuid",
        hidden: true
    }, {
        xtype: "textfield",
        fieldLabel: "区域ID",
        name: "areaId",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "textfield",
        fieldLabel: "房号前辍",
        name: "roomName",
        allowBlank: false,
        // regex: /^[A-Z].*\d$/,
        // regexText: '只能以大写字母开头数字结尾'
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "numberfield",
        fieldLabel: "房间数",
        allowDecimals: false,
        value: 1,
        name: "roomCount",
        allowBlank: false,
        blankText: "房间数不能为空"
    }, {
        beforeLabelTextTpl: comm.get('required'),
        fieldLabel: "房间类型", //字段中文名
        name: "roomType", //字段名
        xtype: "basecombobox", //列类型
        ddCode: "FJLX" //字典代码
    },{
        beforeLabelTextTpl: comm.get('required'),
        xtype: "combobox",
        store: combostore,
        fieldLabel: "网络状态",
        displayField: 'roomNet',
        valueField: 'id',
        value: "0",
        name: "roomNet",
        triggerAction: 'all',
        emptyText: '请选择...',
        blankText: '请选择网络状态',
        editable: false,
        mode: 'local'
    }, {
        xtype: "textfield",
        fieldLabel: "所属区域",
        name: "areaName",
        readOnly: true
    }],
});
var combostore = new Ext.data.ArrayStore({
    fields: ['id', 'roomNet'],
    data: [
        ["0", '有网络'],
        ["1", '无网络']
    ]
});

Ext.define("core.eduresources.room.view.RoomForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.room.RoomForm",
    layout: "form", //从上往下布局
    autoHeight: true,
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
        xtype: "textfield",
        fieldLabel: "区域名称",
        name: "areaName",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "textfield",
        fieldLabel: "房间名称",
        name: "roomName",
        allowBlank: false,
        emptyText: '房间名称',
        blankText: "房间名称不能为空"
    }, {
        beforeLabelTextTpl: comm.get('required'),
        fieldLabel: "房间类型", //字段中文名
        name: "roomType", //字段名
        xtype: "basecombobox", //列类型
        ddCode: "FJLX" //字典代码
    },{
        beforeLabelTextTpl: comm.get('required'),
        xtype: "textfield",
        fieldLabel: "门牌号1",
        name: "extField01",
        allowBlank: false,
        emptyText: '门牌号1',
        blankText: "门牌号1不能为空",
        vtype:'notEqual',
        initialTextField:'extField02'
    }, {
        xtype: "textfield",
        fieldLabel: "门牌号2",
        name: "extField02",
        vtype:'notEqual',
        initialTextField:'extField01',
    }, {
        xtype: "textfield",
        fieldLabel: "门牌号3",
        name: "extField03",
        vtype:'notEqual',
        initialTextField:'extField02'
    }, {
        xtype: "textfield",
        fieldLabel: "门牌号4",
        name: "extField04",
        vtype:'notEqual',
        initialTextField:'extField03'
    }, {
        xtype: "textfield",
        fieldLabel: "门牌号5",
        name: "extField04",
        vtype:'notEqual',
        initialTextField:'extField04'
    }, {
        xtype: "textarea",
        fieldLabel: "房间描述",
        name: "roomDesc"
    }, {
        xtype: "combobox",
        store: combostore,
        fieldLabel: "网络状态",
        displayField: 'roomNet',
        valueField: 'id',
        name: "roomNet",
        value: "0",
        triggerAction: 'all',
        emptyText: '请选择...',
        blankText: '请选择网络状态',
        editable: false,
        mode: 'local'
    }]
});
Ext.define("core.system.role.view.RoleDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.role.deailform",
    xtype: 'roledetailform',
    layout: "form",  
    autoHeight: true,
    frame: false,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        msgTarget: 'qtip',
        labelAlign: "right",
        labelWidth:80
    },
    items: [{
        fieldLabel: '主键',
        name: "uuid",
        hidden: true
    }, {
        fieldLabel: '排序号',
        name: "orderIndex",
        hidden: true
    }, {
        fieldLabel: '系统内置',
        name: "issystem",
        hidden: true
    },{
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        blankText: "角色名称不能为空",
        fieldLabel: '角色名称',
        name: "roleName",
        maxLength: 32,
        emptyText: '请输入角色名称(最大32个字符)'
    }, {
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        blankText: "角色编码不能为空",
        fieldLabel: '角色编码',
        name: "roleCode",
        maxLength: 12,
        emptyText: '请输入角色编码(最大12个字符)'
    }, {
        beforeLabelTextTpl: "",
        allowBlank: true,
        blankText: "备注不能为空",
        fieldLabel: '备注',
        name: "remark",
        emptyText: '请选择备注',
        xtype:"textarea"
    }]

});
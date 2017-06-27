Ext.define("core.systemset.jobinfo.view.jobinfoDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.jobinfo.deailform",
    xtype: 'jobinfodetailform',
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
        fieldLabel: "主键",
        name: "uuid",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "岗位名称不能为空",        
        xtype: "textfield",
        fieldLabel: "岗位名称",
        name: "jobName",
        maxLength: 32,
        emptyText: '请输入岗位名称(最大32个字符)'
    }, {
        beforeLabelTextTpl: comm.get("required"),
        allowBlank: false,
        blankText: "岗位编码能为空",            
        xtype: "textfield",
        fieldLabel: "岗位编码",
        name: "jobCode",
        maxLength: 16,
        emptyText: '请输入岗位名称(最大16个字符)'        
    }]

});
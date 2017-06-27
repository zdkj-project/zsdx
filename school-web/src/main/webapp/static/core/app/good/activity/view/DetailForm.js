Ext.define("core.good.activity.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.activity.deailform",
   // layout: "anchor",
    autoHeight: true,
    frame: false,
    //bodyPadding : '0',
    /*
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        msgTarget: 'none',
        labelWidth: 100,
        labelAlign: "right",
        anchor:'100%'
    },*/
    items: [{
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        maxLength: 10,
        emptyText: '请输入姓名(最大10个字符)',
        blankText: "姓名不能为空",
        fieldLabel: '姓名',
        name: "name"
    },{
        beforeLabelTextTpl: comm.get('required'),
        fieldLabel: '性别',
        name: "sex",
        xtype: 'combo',
        store: Ext.create('Ext.data.Store', {
            fields: ['name', 'value'],
            data : [
                {"name":"男", "value":true},    
                {"name":"女", "value":false}
            ]
        }),
        queryMode: 'local',
        displayField: 'name',
        valueField: 'value',
        allowBlank: false,
        editable:false,
        emptyText: '请选择性别',
        blankText: "性别不能为空",
    },{
        beforeLabelTextTpl: comm.get('required'),
        xtype:'numberfield',
        allowBlank: false,
   
        maxValue: 99,
        minValue: 0,
        value: 1,
        emptyText: '请输入年龄',
        blankText: "年龄不能为空",
        fieldLabel: '年龄',
        name: "age"
    },{
        beforeLabelTextTpl: comm.get('required'),
        xtype: 'datetimefield',  
        fieldLabel: '出生年月',
        name: 'birthday',
        editable:false,
        allowBlank: false    
    }, {
        fieldLabel: '主键',
        name: "uuid",
        hidden: true
    }]
});
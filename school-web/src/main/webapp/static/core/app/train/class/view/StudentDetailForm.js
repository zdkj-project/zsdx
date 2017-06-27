/*
    已弃用
*/
Ext.define("core.train.class.view.StudentDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.studentdetailform",
    
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '0 10 10 0',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    items: [{
        fieldLabel: "主键",
        name: "uuid", 
        xtype: "textfield",
        hidden: true
    },{
        fieldLabel: "学员id",
        name: "traineeId",
        xtype: "textfield",
        hidden: true
    },{
        fieldLabel: "所属班级id",
        name: "classId",
        xtype: "textfield",
        hidden: true
    },{
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        fieldLabel: "所属班级",
        name: "className",
        xtype: "textfield",          
        readOnly:true
    },{
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,      
        xtype: "basefuncfield",
        funcPanel: "trainee.mainlayout", //该功能显示的主视图
        //refController:'class.otherController',      //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
        funcTitle: "学员选择", //查询窗口的标题
        configInfo: {
            width:800,
            fieldInfo: "traineeId~xm,uuid~xm",
            whereSql: " and isDelete='0' ",
            orderSql: " order by jobCode ",
            muiltSelect: false //是否多选
        },
        fieldLabel: '学员姓名',
        emptyText: "请选择学员",
        name: "xm",
    }, {
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        fieldLabel: "性别",
        name: "xbm",
        xtype: "basecombobox",
        ddCode: "XBM",
        emptyText: "请选择性别",
        editable:false
        
    }, {
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        fieldLabel: "移动电话",    
        name: "mobilePhone",
        xtype: "textfield",
        emptyText: "请输入移动电话",
        maxLength: 11,
        maxLengthText: "最多11个字符",
    }]
});
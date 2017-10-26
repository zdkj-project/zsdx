Ext.define("core.reportcenter.traineeconsumereport.view.MainForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.traineeconsumereport.mainform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '0 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
        labelAlign: "right"
    },
    defaults:{
        width:'100%', 
    },
    tbar: [ '->', {
        xtype: 'tbtext',
        html: '快速搜索：'
    }, {
        xtype: "datetimefield",
        name: "dinnerDate",
        queryType: "datetimefield",
        dateType:'date',        //指定这个组件的格式，date或者datetime
        ref: 'girdFastSearchText',
        value:new Date(),
        emptyText: '请输入就餐日期',
        triggers: {
            picker: {
                handler: 'onTriggerClick',
                scope: 'this',
                focusOnMousedown: true
            },
            clear: {
                cls:'x-fa fa-refresh',
                handler:function(btn){
                    var me=this;
                    me.reset();
                },
                weight:-1,
                scope: 'this',
                focusOnMousedown: true
            },
        },
    }, {
        xtype: 'button',
        funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
        ref: 'gridFastSearchBtn',
        iconCls: 'x-fa fa-search'
    },{
        xtype: 'button',
        text: '导出当天登记情况',
        ref: 'gridExport',
        funCode: 'girdFuntionBtn',
        iconCls: 'x-fa fa-clipboard'
    }],

    items: [
    // {
    //     fieldLabel: "主键",
    //     name: "uuid",
    //     xtype: "textfield",
    //     hidden: true
    // },{
    //     xtype: "container",
    //     layout: "column",
    //     labelAlign: "right",
    //     items:[{
    //         beforeLabelTextTpl: comm.get("required"),
    //         allowBlank: false,
    //         blankText: "姓名不能为空",        
    //         fieldLabel: "姓名",
    //         columnWidth: 0.5,
    //         name: "xm",
    //         xtype: "textfield",

    //         emptyText: "请输入姓名",
    //         maxLength:64,
    //         maxLengthText:"最多64个字符,汉字占2个字符",    
    //     },{ 
    //         beforeLabelTextTpl: comm.get("required"),
    //         allowBlank: false,
    //         blankText: "性别不能为空",        
    //         fieldLabel: "性别",
    //         columnWidth: 0.5,
    //         name: "xbm",
    //         xtype: "basecombobox",
    //         ddCode: "XBM",

    //         emptyText: "请输入性别",
    //         maxLength:1,
    //         maxLengthText:"最多1个字符,汉字占2个字符",
    //     }]
    // }
    ]
});
Ext.define("core.ordermanage.orderdesc.view.MainForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.orderdesc.mainform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '20 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    defaults:{
        width:'100%', 
    },
    tbar: [/*{
        xtype: 'button',
        ref: 'gridEdit',
        iconCls: 'x-fa fa-pencil-square',
        title:'编辑'
    }*/],

    items: [{
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items:[{ 
            allowBlank: false,
            blankText: "订餐说明不能为空",        
            fieldLabel: "订餐说明",
            columnWidth: 1,
            name: "orderDesc",
            xtype: "htmleditor",
            height:400,
            //readOnly:true,
            emptyText: "请输入订餐说明",
            maxLength:2048,
            maxLengthText:"最多2048个字符,汉字占2个字符",
        }]
    }],
    buttons: [{
        text: '提交',
        ref: 'submitBtn',
        iconCls: 'x-fa fa-check-square',
        //formBind: true, //only enabled once the form is valid
        //disabled: true
    }, '  ',{
        text: '关闭',
        ref: 'closeBtn',
        iconCls: 'x-fa fa-reply',
        handler:function() {
            this.up('form').getForm().reset();
        }
        
    }] 
});
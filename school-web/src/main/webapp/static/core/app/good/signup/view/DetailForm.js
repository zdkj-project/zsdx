Ext.define("core.good.signup.view.DetailForm", {
    extend: "Ext.form.Panel",
    alias: "widget.signup.deteailform",
    
    url:'www.baidu.com',
    frame: true,
    
    bodyPadding : 10,
    layout: 'anchor',
    defaults: {
        anchor: '100%',
        margin:10,
        labelAlign : 'right',
        msgTarget: 'qtip',
        labelSeparator: '：', // 分隔符
        labelWidth:80, 
    },
    buttonAlign :'center',
    defaultType: 'textfield',

    items: [{
        fieldLabel: '主键',
        name: "uuid",
        hidden: true
    },{
        fieldLabel: '活动标题',
        name: 'actTitle',
        allowBlank: false    
    },{
        xtype: 'datefield',
        format:'Y-m-d',
        fieldLabel: '活动时间',
        name: 'actDate',
        allowBlank: false        
    }],
    buttons: [{
        text: '提交',
        ref: 'submitBtn',
        iconCls: 'x-fa fa-check-square',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler:'doSave'
        /*
        handler: function() {
            var form = this.up('form').getForm();
            if (form.isValid()) {
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', action.result.msg);
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('Failed', action.result.msg);
                    }
                });
            }
        }*/
    }, {
        text: '重置',
        ref: 'resetBtn',
        iconCls: 'x-fa fa-undo',
        handler:function() {
            this.up('form').getForm().reset();
        }
        
    }] 

});
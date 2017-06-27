Ext.define("core.base.view.BaseFormWin", {
    extend: 'Ext.window.Window',
    alias: "widget.baseformwin",
    //width: 850,
    //height: 700,
    width: 1000,
    height: 660,
    border: false,
    layout: 'fit',
    plain: true,
    frame:true,
    saveParams: {},
    closeAction: 'destroy',
    closable: true,
    maximizable:true,
    resizable:false,
    modal: true,
    iconCls: 'application_form',
    operType: '', // 操作类型,
    funData: "", //自定义的配置参数
    funCode: "",
    //txtformSave:"保存",
    //txtformContinue:"保存继续",
    txtformSave:"提交",
    txtformContinue:"继续新增",
    txtformClose:"关闭",
    insertObj:{},
    initComponent: function() {
        var me = this;
        funData = me.funData;

        switch (me.operType) {
            case 'add':
                if (me.title == null)
                    me.title = '新增';
                me.dockedItems = [{
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    layout: {
                        pack: 'center'
                    },
                    items: [/*{取消保存继续功能
                        xtype: "button",
                        text: me.txtformContinue,
                        ref: "formContinue",
                        iconCls: "x-fa fa-plus-square"

                    }, '  ',*/ {
                        xtype: "button",
                        text: me.txtformSave,
                        ref: "formSave",
                        iconCls: "x-fa fa-check-square"

                    }, '  ', {
                        xtype: "button",
                        text: me.txtformClose,
                        ref: "formClose",
                        iconCls: "x-fa fa-reply"
                    }]
                }]
                break;
            case 'addReturn':
                if (me.title == null)
                    me.title = '新增';
                me.dockedItems = [{
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    layout: {
                        pack: 'center'
                    },
                    items: [{
                        xtype: "button",
                        text: me.txtformSave,
                        ref: "formSave",
                        iconCls: "x-fa fa-check-square"

                    }, '  ', {
                        xtype: "button",
                        text: me.txtformClose,
                        ref: "formClose",
                        iconCls: "x-fa fa-reply"
                    }]
                }];
                break;
            case 'edit':
                if (me.title == null)
                    me.title = '修改';
                me.dockedItems = [{
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    layout: {
                        pack: 'center'
                    },
                    items: [{
                        xtype: "button",
                        text: me.txtformSave,
                        ref: "formSave",
                        iconCls: "x-fa fa-check-square"

                    }, '  ', {
                        xtype: "button",
                        text: me.txtformClose,
                        ref: "formClose",
                        iconCls: "x-fa fa-reply"
                    }]
                }];
                break;
            default:
                if (me.title == null)
                    me.title = '查看';
                me.dockedItems = [{
                    xtype: 'toolbar',
                    dock: 'bottom',
                    ui: 'footer',
                    layout: {
                        pack: 'center'
                    },
                    items: [{
                        xtype: "button",
                        text: me.txtformClose,
                        ref: "formClose",
                        iconCls: "x-fa fa-reply"
                    }]
                }];
                break;
        };

        me.callParent(arguments);
    }
});
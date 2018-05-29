Ext.define("core.base.view.BaseFormTab", {
    extend: 'Ext.panel.Panel',
    alias: "widget.baseformtab",
    border: false,
    layout: 'fit',
    /*设置最小宽度，并且自动滚动*/
    //minWidth:1000,
    scrollable:true, 
    saveParams: {},
    operType: '', // 操作类型,
    funData: "", //自定义的配置参数
    funCode: "",
    txtformSave:"提交",
    txtformContinue:"继续新增",
    txtformClose:"关闭",
    insertObj:{},
    initComponent: function() {
        var me = this;
        

        switch (me.operType) {
            case 'add':
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
            case 'noButton':                
                break;
            default:
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
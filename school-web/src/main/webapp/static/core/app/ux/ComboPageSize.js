Ext.define('Ext.ux.ComboPageSize', {
    requires: [
        'Ext.form.field.ComboBox'
    ],
    pageSizes: [10, 20, 30, 40, 50],
    constructor: function(config) {
        if (config) {
            Ext.apply(this, config);
        }
    },
    init: function(pbar) {
        var combo,
            me = this;
        combo = Ext.widget('combo', {
            width: 60,
            editable: false,
            store: me.pageSizes,
            triggers: {
                clear: {
                    cls:Ext.baseCSSPrefix + 'form-clear-trigger',
                    handler:function(btn){
                        var me=this;
                        me.reset();
                    },
                    weight:-1,
                    hidden:true,    //隐藏
                    scope: 'this',
                    focusOnMousedown: true
                },
            },
            listeners: {
                change: function(s, v, o) {
                    pbar.store.pageSize = v;
                    if (o)
                        pbar.store.loadPage(1);
                    //pbar.store.loadPage(1);
                }
            }
        });
        /*
        pbar.add(10, '-');
        pbar.add(10, combo);
        */
        pbar.add(0, '-');
        pbar.add(0, combo);

        combo.setValue(pbar.store.pageSize);
    }
});
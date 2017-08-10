Ext.define('baseUx.form.textfield.TextTrigger', {
    extend:'Ext.form.field.Text',
    alias: 'widget.texttrigger',
    triggers: {
        clear: {
            cls: Ext.baseCSSPrefix + 'form-clear-trigger',
            handler: function(me) {
                me.setValue("");
            }    
        }
    }
});
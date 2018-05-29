Ext.define('core.good.signup.model.MainGridModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'uuid',     type: 'string' },
        { name: 'actTitle',      type: 'string' },
        { name: 'actDate',    type: 'date', 
        
            //defaultValue: new Date(), 
            /*加上之后，修改了record，将不会修改成功
            convert: function (value) {
                console.log(Ext.Date.format(new Date(value),'Y-m-d'));
                return Ext.Date.format(new Date(value),'Y-m-d');
            }*/
        }
    ],

    validators: {       
        actTitle: { type: 'length', max: 10 }    
    }
});
Ext.define('core.good.news.model.MainGridModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'uuid',     type: 'string' },
        { name: 'newTitle',      type: 'string' },
        { name: 'newIntro',      type: 'string' },
        { name: 'newContent',      type: 'string' },
        { name: 'createUser',      type: 'string' },
        { name: 'createTime',    type: 'date', 
        
            //defaultValue: new Date(), 
            /*加上之后，修改了record，将不会修改成功
            convert: function (value) {
                console.log(Ext.Date.format(new Date(value),'Y-m-d'));
                return Ext.Date.format(new Date(value),'Y-m-d');
            }*/
        }
    ],

    //通过获取是否验证成功model.isValid();  
    validators: {       
        newTitle: { type: 'length', max: 10 }    
    }
});
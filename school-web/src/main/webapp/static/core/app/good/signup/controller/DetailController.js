Ext.define('core.good.signup.controller.DetailController', {
    extend: 'Ext.app.ViewController',
        
    alias: 'controller.signup.detailController',

    init: function() {
        var self = this;
        //在主视图中写一次，即可加载，重复在不同的是视图中写，会多次加载,造成事件方法会执行多次
        console.log("初始化signup detailController");

        this.control({   //与以前一样的写法
            //只要一经声明，则全局生效
            /*
            "button": {
                click : function(btn) {
                    console.log(11);
                   
                }
            }
            */
           
    
        });
    },
    doSave:function(btn){
        var form = btn.up('form');
        var formObj=form.getForm();
        if (formObj.isValid()) {
            var values=formObj.getValues();
            console.log(values.uuid);
            if(!values.uuid){
                values.uuid='1111';
                var grid=Ext.ComponentQuery.query('grid[xtype=signup.maingrid]')[0];
                var record=grid.getStore().insert(0,values);              
                //record.save();    //会自动根据是否存在id去执行proxy中的create或update的请求 ，待验证
                form.up("panel[xtype=signup.detaillayout]").hide();
            }else{
                /*
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', action.result.msg);
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('Failed', action.result.msg);
                    }
                });
                */
                var record=formObj.getRecord();
                record.set(formObj.getValues());
                //record.save();    //会自动根据是否存在id去执行proxy中的create或update的请求 ，待验证
                form.up("panel[xtype=signup.detaillayout]").hide();
            }
            
        }
    }

});

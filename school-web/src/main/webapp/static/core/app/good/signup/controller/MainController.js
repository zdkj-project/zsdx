Ext.define('core.good.signup.controller.MainController', {
    extend: 'Ext.app.ViewController',
        
    alias: 'controller.signup.mainController',

    init: function() {
        var self = this;
        //在主视图中写一次，即可加载，重复在不同的是视图中写，会多次加载,造成事件方法会执行多次
        console.log("初始化signup controller");

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
    onItemdblClick: function (view,record , item , index) {    
        var detailLayout=Ext.widget('signup.detaillayout',{
            width: 500,                

            title:'编辑数据',
            iconCls: 'x-fa fa-pencil-square',              
            /*
            viewModel : {
                data: {
                    singupGridData: record
                }
            },*/
                      
        }).show();  

        detailLayout.down('form').loadRecord (record);                
    },

    doAddClick: function (choice) {
        var detailLayout=Ext.widget('signup.detaillayout',{
            width: 500,                

            title:'新增数据',
            iconCls: 'x-fa fa-plus-circle',              
            /*
            viewModel : {
                data: {
                    singupGridData: record
                }
            },*/
                      
        }).show();  

        //detailLayout.down('form').loadRecord (record);        
    },

    doEditClick:function(btn){

        var grid=btn.up("grid");
        var records=grid.getSelectionModel().getSelection();

        if(records.length==1){
            var detailLayout=Ext.widget('signup.detaillayout',{
                width: 500,                

                title:'编辑数据',
                iconCls: 'x-fa fa-pencil-square',              
                /*
                viewModel : {
                    data: {
                        singupGridData: record
                    }
                },*/
                          
            }).show();  

            detailLayout.down('form').loadRecord (records[0]); 
        }else{
            Ext.Msg.alert('温馨提示', '请选择一条数据！');
        }

        

         
    },
    doDetailClick:function(btn){
        var grid=btn.up("grid");
        var records=grid.getSelectionModel().getSelection();
        if(records.length==1){
            var detailLayout=Ext.widget('signup.detaillayout',{
                width: 500,                

                title:'编辑数据',
                iconCls: 'x-fa fa-pencil-square',              
                /*
                viewModel : {
                    data: {
                        singupGridData: record
                    }
                },*/
                items: [{
                    xtype: "signup.deteailform",
                    buttons: [{
                        text: '关闭',
                        ref: 'closeBtn',
                        iconCls: 'x-fa fa-close',                                            
                        handler: function() {
                            detailLayout.hide();
                        }
                    }]
                }]          
            }).show();  

            detailLayout.down('form').loadRecord (records[0]); 
            
        }else{
            Ext.Msg.alert('温馨提示', '请选择一条数据！');
        }
    },
    //若与其他控制器存在相同的方法名，则仅仅会执行这个view里面的事件
    doDeleteClick:function(btn){   
        var grid=btn.up("grid");
        var records=grid.getSelectionModel().getSelection();
        if(records.length==1){
            Ext.Msg.confirm('提示', '确定要删除这条数据么?', function(btn, text) {
                if (btn == 'yes') {
                   console.log( grid.getStore().remove(records[0]));
                   //records[0].erase();    
                }
            });
            
        }else{
            Ext.Msg.alert('温馨提示', '请选择一条数据！');
        }
    }

});

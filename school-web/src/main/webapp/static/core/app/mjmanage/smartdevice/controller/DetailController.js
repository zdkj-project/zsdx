Ext.define("core.mjmanage.smartdevice.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.smartdevice.detailcontroller',
    mixins: {},
    init: function () {
        /*执行一些初始化的代码*/
    },
    /** 该视图内的组件事件注册 */
    control: {
    	'baseform[xtype=mjmanage.smartdevice.skbaseparamform] button[ref=checkall]':{
            click:function(btn){
                var objForm=btn.up("baseform[xtype=mjmanage.smartdevice.skbaseparamform]");
                for(var i=0;i<4;i++){
                    var checkbox = objForm.down("checkboxgroup[ref=sKBaseParamForm_lblOperationBehaviors" + (i+1) + "]");    
                    for (var j = 0; j < checkbox.items.items.length; j++) {
                        if (checkbox.items.items[j].checked == true) {
                            checkbox.items.items[j].setValue(false)
                        } else {
                            checkbox.items.items[j].setValue(true)
                        }
                    };
                }
                
            }
        },
        'baseform[xtype=mjmanage.smartdevice.doorcontrolform] button[ref=checkall]':{
            click:function(btn){
                var objForm=btn.up("baseform[xtype=mjmanage.smartdevice.doorcontrolform]");    
                var checkbox = objForm.down("checkboxgroup[ref=KrateForm_lblOperationBehaviors]");  

                for (var j = 0; j < checkbox.items.items.length; j++) {
                    if (checkbox.items.items[j].checked == true) {
                        checkbox.items.items[j].setValue(false)
                    } else {
                        checkbox.items.items[j].setValue(true)
                    }
                };                        
            }
        },

    },
    onChangeWorkPattern:function(btn){
        var value = btn.value;
        var viewModel = this.getView().getViewModel();
        var timeWorkPattern = "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>秒</font>";
        var meterWorkPattern = "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>脉冲</font>";
        if(value == 0){//计时实时模式
         viewModel.set("workPatternType.value",timeWorkPattern);
        }else if (value == 17){//计量实时模式
            viewModel.set("workPatternType.value",meterWorkPattern);

        }else if (value == 51){//计时预扣模式
            viewModel.set("workPatternType.value",timeWorkPattern);

        }else if(value == 68){//计量预扣模式
            viewModel.set("workPatternType.value",meterWorkPattern);

        }
    },
});
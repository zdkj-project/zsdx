/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.good.activity.controller.OtherController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.activity.otherController',
    
    /*把不需要使用的组件，移除掉*/
    mixins: {
        /*
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        */
    },
   
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 other controler");           
    },

    /*该视图内的组件事件注册*/
    control:{
        "baseformwin button[ref=formContinue]": {
            beforeclick:function(btn){
                console.log(btn);
            }
        },
        
        "baseformwin button[ref=formSave]": {
            beforeclick: function(btn) {
                console.log(btn);
            }
        },


        "baseformwin button[ref=formClose]": {
            beforeclick: function(btn) {
                console.log(btn);
            }
        },

    }

});
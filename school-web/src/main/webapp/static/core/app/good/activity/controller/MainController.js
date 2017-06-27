/**
    ( *必须的 )
    此视图控制器，提供于MainLayout范围内的界面组件注册事件
*/
Ext.define("core.good.activity.controller.MainController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.activity.mainController',
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
    /*
    statics: {  
        needInit:true  
    },  
    */
    
    init: function() {
        /*执行一些初始化的代码*/
        /*此方式已放弃使用，有严重bug，不符合规则
        if (core.good.activity.controller.MainController.needInit) {  
            this.control({});
            core.good.activity.controller.MainController.needInit = false;  
        } 
        */
        //console.log("初始化 activity controler");        
    },

    control:{    
        "basegrid button[ref=gridAdd]":{
            beforeclick : function(btn) {
                console.log(btn);            
                //return false;
            }
        },

        "basegrid button[ref=gridDetail]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridEdit]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid  actioncolumn": {
            editClick: function(data) {
                console.log(data);

            },
            detailClick: function(data) {
                console.log(data);

            },
            deleteClick: function(data) {
                console.log(data);

            },
        }

    }
});
/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.train.coursecheckrule.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursecheckrule.detailController',
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
        //console.log("初始化 detail controler");     
    },
    /** 该视图内的组件事件注册 */
    control: {
            /*
        "button": {
            click : function(btn) {
                console.log(this);
               
            }
        }*/
    }   
});
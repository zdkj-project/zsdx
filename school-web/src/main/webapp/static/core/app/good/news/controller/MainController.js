Ext.define("core.good.news.controller.MainController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.news.mainController',
    statics: {  
        needInit:true  
    },  
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
   

    init: function() {
        
        var self = this;

        if (core.good.news.controller.MainController.needInit) {  

            console.log("初始化 news controler");
            this.control({
                
            });
            core.good.news.controller.MainController.needInit = false;  
        }  

        
    },
});
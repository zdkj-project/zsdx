Ext.define("core.system.operatelog.controller.otherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.operatelog.otherController',
	
    // 工具类，可以注释掉
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },

    control:{
    }
         
});
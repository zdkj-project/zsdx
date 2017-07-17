Ext.define("core.system.ip.controller.otherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.ip.otherController',
	
    // 工具类，可以注释掉
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },

    control:{
        "baseformtab[detCode=creditrule_detail] button[ref=formSave]":{
            beforeclick:function(btn){
                this.doSave_Tab(btn,"save");
                return false;
            }
        }
    }
});
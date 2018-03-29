Ext.define("core.system.operatelog.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.operatelog.mainController',
	
    
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
/*    views: [
        "core.systemset.jobinfo.view.jobinfoMainLayout",
        "core.systemset.jobinfo.view.jobinfoDetailLayout",
        "core.systemset.jobinfo.view.jobinfoGrid",
        "core.systemset.jobinfo.view.jobinfoDetailForm"
    ],*/
    init: function() {
        var self = this;
        
        this.control({
/*            "basegrid[funCode=jobinfo_main] button[ref=gridFastSearchBtn]": {
                beforeclick:function(btn){                
                    //得到组件
                    var baseGrid = btn.up("basegrid");
                        
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();


                    var jobName=baseGrid.down("textfield[name=jobName]").getValue();               
                    
                    proxy.extraParams.filter = '[{"type":"string","value":"'+jobName+'","field":"jobName","comparison":""}]';
                    store.loadPage(1);

                    return false;
                }
            },
        	*/
        	
        });
    },

});
Ext.define("core.cashier.mealtime.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mealtime.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function () {
    },
    control: {
    	"basegrid[xtype=mealtime.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btngridEdit_Tab = grid.down("button[ref=gridEdit_Tab]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1) {
                	btngridEdit_Tab.setHidden(true);
                }
            }
           },
    	
    },

});
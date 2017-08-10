Ext.define("core.train.coursechkresult.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursechkresult.mainController',
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
    },
    control: {
    	"basegrid[xtype=coursechkresult.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btngridAnalyze = grid.down("button[ref=gridAnalyze]");
                var btngridExport = grid.down("button[ref=gridExport]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1) {
                	btngridAnalyze.setHidden(true);
                    btngridExport.setHidden(true);
                }
            }
        },
    	
    	
    	
        "basegrid button[ref=gridAdd]": {
            beforeclick: function(btn) {
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
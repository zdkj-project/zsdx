Ext.define("core.oa.meeting.checkresult.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.checkresult.mainController',
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
        "basegrid button[ref=gridAdd_tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridDetail_Tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridEdit_tab]": {
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
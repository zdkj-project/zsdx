Ext.define("core.train.coursecategory.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursecategory.mainController',
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
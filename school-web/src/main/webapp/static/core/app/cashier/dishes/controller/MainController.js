Ext.define("core.cashier.dishes.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.dishes.mainController',
    mixins: {

        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function () {
    },
    control: {},

});
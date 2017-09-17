/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
 */
Ext.define("core.cashier.mealtime.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mealtime.detailController',
    mixins: {
         suppleUtil: "core.util.SuppleUtil",
         messageUtil: "core.util.MessageUtil",
         formUtil: "core.util.FormUtil",
         gridActionUtil: "core.util.GridActionUtil",
         dateUtil: 'core.util.DateUtil'
    },
    init: function () {
    },
    /** 该视图内的组件事件注册 */
    control: {},
});
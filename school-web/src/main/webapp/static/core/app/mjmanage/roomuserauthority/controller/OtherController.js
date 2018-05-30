Ext.define("core.mjmanage.roomuserauthority.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.roomuserauthority.othercontroller',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function () {
        /*执行一些初始化的代码*/
    },
    /** 该视图内的组件事件注册 */
    control: {
    }
});
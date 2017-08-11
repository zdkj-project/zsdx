/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.train.coursechkresult.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursechkresult.detailController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");     
    },
    /** 该视图内的组件事件注册 */
    control: {
        "basegrid[xtype=coursechkresult.classcoursegrid]": {
            beforeitemclick: function (grid, record, item, index, e, eOpts) {
                var self = this;
                var mainLayout = grid.up("panel[xtype=coursechkresult.detaillayout]");
                var mainTab = mainLayout.up("baseformtab");
                var classId = mainTab.funData.classId;
                var maingrid = mainLayout.down("panel[xtype=coursechkresult.classstudentgrid]");
                var classScheduleId = record.get("uuid");
                var store = maingrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    classId: classId,
                    classScheduleId: classScheduleId
                };
                store.loadPage(1);

                return false;
            }
        }
    }   
});
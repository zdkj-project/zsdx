/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
 */
Ext.define("core.cashier.mealset.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mealset.detailController',
    mixins: {
        /*
         suppleUtil: "core.util.SuppleUtil",
         messageUtil: "core.util.MessageUtil",
         formUtil: "core.util.FormUtil",
         gridActionUtil: "core.util.GridActionUtil",
         dateUtil: 'core.util.DateUtil'
         */
    },
    init: function () {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");     
    },
    /** 该视图内的组件事件注册 */
    control: {
        /**
         * 增加一个菜品
         */
        "basegrid[xtype=mealset.standgrid] button[ref=gridAddStand]": {
            beforeclick:function (btn) {
                var baseGrid = btn.up("basegrid");
                var store = baseGrid.getStore();
                var p ={
                    uuid:'',
                    cashDishesStand:''
                };
                store.insert(0,p);
            }
        },
        /**
         * 删除一个菜品
         */
        "basegrid[xtype=mealset.standgrid] button[ref=gridDelStand]": {
            beforeclick:function (btn) {
                var self = this;
                var baseGrid = btn.up("basegrid");
                var store = baseGrid.getStore();
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length===0){
                    self.Error("没有选择删除项");
                    return false;
                }
                Ext.Msg.confirm('系统提示','确定要删除？',function(btn){
                    if(btn=='yes'){
                        Ext.each(records, function (rec) {
                            store.remove(rec);
                        });
                    }
                });
                return false;
            }
        }
    },
});
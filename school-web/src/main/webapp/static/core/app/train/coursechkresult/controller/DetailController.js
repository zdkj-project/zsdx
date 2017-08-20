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

                //var mainLayout = grid.up("panel[xtype=coursechkresult.detaillayout]");
                var mainTab = grid.up("baseformtab");

                //var classId = mainTab.funData.classId;
                var maingrid = mainTab.down("panel[xtype=coursechkresult.classstudentgrid]");
                var classId = record.get("classId");
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
        },

        //快速搜索按按钮
        "basepanel basegrid button[ref=gridFastSearchBtn]": {
            click: function (btn) {
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                if (!baseGrid)
                    return false;

                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;
                
                var store = baseGrid.getStore();
                var proxy = store.getProxy();

                var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                for (var i in girdSearchTexts) {
                    var name = girdSearchTexts[i].getName();
                    var value = girdSearchTexts[i].getValue();

                    proxy.extraParams[name]=value;
                }

                store.loadPage(1);
                return false;
            }
        },
        //快速搜索文本框回车事件
        "basepanel basegrid field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {

                    //得到组件                 
                    var baseGrid = field.up("basegrid");
                    if (!baseGrid)
                        return false;

                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;
                  

                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();

                    var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                    for (var i in girdSearchTexts) {
                        var name = girdSearchTexts[i].getName();
                        var value = girdSearchTexts[i].getValue();

                        proxy.extraParams[name]=value;
                    }

                    store.loadPage(1);
                }
                return false;
            }
        },
    }   
});
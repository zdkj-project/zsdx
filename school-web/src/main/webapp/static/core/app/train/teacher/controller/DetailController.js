/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.train.teacher.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.teacher.detailController',
    mixins: {
        
        //suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        //formUtil: "core.util.FormUtil",
        //gridActionUtil: "core.util.GridActionUtil",
        //dateUtil: 'core.util.DateUtil'
        
    },
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");     
    },
    /** 该视图内的组件事件注册 */
    control: {
        "basegrid  actioncolumn": {
            gridEvalClick: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doTeachingClick_Tab(null, cmd, baseGrid, record);
                return false;
            }
        }
    },
    /**
     * 操作列查看评价情况处理
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doTeachingClick_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");
        var basetab = baseGrid.up('baseformtab');

        //得到配置信息
        var tabFunData = basetab.funData;
        var funData = basePanel.funData;
        var detCode = "teacher_courseEvalDetail";   //修改此funCode，方便用于捕获window的确定按钮
        //var detLayout = basePanel.detLayout;
        var detLayout = "teacher.detaillayout";
        var defaultObj = funData.defaultObj;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = {};
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "评价情况";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridCourseEvalDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
        var operType = cmd;

        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择1条数据！");
                return;
            }
            recordData = rescords[0].data;
        }

        insertObj = recordData;
        pkValue = insertObj.uuid;
        tabTitle = tabFunData.xm + "-课程评价";
        tabItemId = funCode + "_gridCourseEvalDetail" + tabFunData.uuid;

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,      //保存主键值
                layout: 'fit',
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,         //指定重写事件的控制器
                    funCode: funCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: [{
                        xtype: detLayout,
                        margin: 10,
                        funCode: detCode,
                        items: [{
                            xtype: "teacher.teachingrid"
                        }]
                    }]
                });
                tabItem.add(item);

                //var coursegrid=win.down("grid[xtype=teacher.coursegrid]");
                var teachingGrid = tabItem.down("grid[xtype=teacher.teachingrid]");
                var proxy = teachingGrid.getStore().getProxy();
                proxy.extraParams.propName="courseId,courseName";
                proxy.extraParams.propValue=record.get("uuid") + "," + record.get("courseName");
                proxy.extraParams.joinMethod = "or";
                //proxy.extraParams.filter = "[{'type':'string','comparison':'','value':'" + record.get("uuid") + "','field':'mainTeacherId'}]";
                teachingGrid.getStore().loadPage(1);
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },
});
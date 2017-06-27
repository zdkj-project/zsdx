/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
 */
Ext.define("core.train.alleval.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.alleval.detailController',
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
         * 培训记录操作列
         */
        "basegrid  actioncolumn": {
            gridRecordCourseClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doRecordCourseClick_Tab(null, cmd, baseGrid, record);
                return false;
            }
        }
    },

    /**
     * 学员培训记录中查看培训课程
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doRecordCourseClick_Tab: function (btn, cmd, grid, record) {
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
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");
        var basetab = baseGrid.up('baseformtab');

        //得到配置信息
        var tabFunData = basetab.funData;
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;

        var operType = cmd;
        var pkValue = null;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        //var defaultObj = funData.defaultObj;
        var insertObj = null;

        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //默认的tab参数
        var tabTitle = "查看课程详情"; //标签页的标题
        var tabItemId = funCode + "_gridTrainRecord_gridCourse";     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "alleval.trainrecordcoursegrid";

        //根据不同的操作对数据进行组装
        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择1条数据！");
                return;
            }
            recordData = rescords[0].data;
        }
        insertObj = recordData;
        tabTitle = tabFunData.xm + "_" + insertObj.className + "-课程详情";
        tabItemId = funCode + "_gridTrainRecord_gridCourse" + tabFunData.xm;

        //获取主键值
        var pkName = funData.pkName;
        pkValue = recordData[pkName];


        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);

        //判断是否已经存在tab了
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,    //保存主键值
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
                        items: [{
                            xtype: itemXtype
                        }]
                    }]
                });
                tabItem.add(item);
                var trainRecordGrid = tabItem.down("grid[xtype=alleval.trainrecordcoursegrid]");
                var proxy = trainRecordGrid.getStore().getProxy();
                proxy.extraParams.filter = "[{'type':'string','comparison':'=','value':'" + record.get("classId") + "','field':'classId'}]";
                trainRecordGrid.getStore().loadPage(1);
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }

});
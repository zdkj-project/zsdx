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
    control: {
    	"basegrid[xtype=dishes.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btngridAdd_Tab = grid.down("button[ref=gridAdd_Tab]");
                var btngridDelete = grid.down("button[ref=gridDelete]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 
                    && roleKey.indexOf("ZONGWUROLE") == -1 && roleKey.indexOf("FOODMANAGER") == -1) {
                	btngridAdd_Tab.setHidden(true);
                	btngridDelete.setHidden(true);
                }
            }
           },
    	
           /**
            * 指标列表操作列事件
            */
           "basegrid  actioncolumn": {
               //操作列编辑、详细
               editClick_Tab: function (data) {
                   this.doMainDetail_Tab(null, data.cmd, data.view, data.record);
                   return false;
               },
               //操作列删除
               deleteClick: function (data) {
               }
           }
    },
    
    /**
     * 增加、修改、详细
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doMainDetail_Tab: function (btn, cmd, grid, record) {
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

        //得到配置信息
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
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);

        //默认的tab参数
        var tabTitle = funData.tabConfig.addTitle; //标签页的标题
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "dishes.detailform";

        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "edit":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }
                insertObj = recordData;
                tabTitle = funData.tabConfig.editTitle;
                tabItemId = funCode + "_gridEdit";
                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
        });
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
                var filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'uuid'}]";
                switch (cmd) {
                    case "edit":
                        var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                        var formDeptObj = objDetForm.getForm();

                        self.setFormValue(formDeptObj, insertObj);

                        var standgrid = tabItem.down("grid[xtype=dishes.standgrid]");
                        var btngridAddStand = standgrid.down("button[ref=gridAddStand]");
                        var btngridDelStand = standgrid.down("button[ref=gridDelStand]");
                        btngridAddStand.setHidden(true);
                        btngridDelStand.setHidden(true);
                        var proxy = standgrid.getStore().getProxy();
                        proxy.extraParams.filter = filter;
                        standgrid.getStore().loadPage(1);
                        break;
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }
});
Ext.define("core.train.coursetype.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursetype.mainController',
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
        /**
         * 列表增加事件
         */
        "basetreegrid[xtype=coursetype.coursecategorytree] button[ref=gridAdd_Tab]": {
            beforeclick: function (btn) {
                this.doMainGridDetail_Tab(btn, "child");
                return false;
            }
        },
        /**
         * 列表修改事件
         */
        "basetreegrid[xtype=coursetype.coursecategorytree] button[ref=gridEdit]": {
            beforeclick: function (btn) {
                this.doMainGridDetail_Tab(btn, "edit");
                return false;
            }
        },

        /**
         * 列表删除事件
         */
        "basetreegrid[xtype=coursetype.coursecategorytree] button[ref=gridDelete]": {
            beforeclick: function (btn) {
                this.doMainGridDelete(btn, "delete");

                return false;

            }
        },
        "basetreegrid[xtype=coursetype.coursecategorytree] button[ref=gridRefresh]": {
            beforeclick: function (btn) {
                var baseGrid = btn.up("basetreegrid");
                var store = baseGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    whereSql: "and isDelete='0'",
                    orderSql: " order by parentNode,orderIndex asc"
                };
                store.load(); //刷新父窗体的grid

                return false;
            }
        },

        /**
         * 列表操作列事件
         */
        "basetreegrid  actioncolumn": {
            //操作列编辑
            mainGridEditClick_Tab: function (data) {
                this.doMainGridDetail_Tab(null, "edit", data.view, data.record);
                return false;
            },
            //操作列排序
            mainGridOrderClick_Tab: function (data) {
                this.doMainGridDetail_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            //操作列删除
            deleteClick: function (data) {
                this.doMainGridDelete(null, data.cmd, data.view, data.record);
                return false;
            }
        }

    },
    /**
     * 详细相关操作处理
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doMainGridDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;
        var selCount = 0;

        if (btn) {
            baseGrid = btn.up("basetreegrid");
            recordData = baseGrid.getSelectionModel().getSelection();
            selCount = recordData.length;
        } else {
            baseGrid = grid;
            recordData = record.getData();
            selCount = 1;
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

        //当前节点
        var pkValue;
        var just = "3EF578B0-61EA-45EF·-8731-839F798BA3CA";
        var justName = "课程分类 ";
        var parentCategory = "3EF578B0-61EA-45EF-8731-839F798BA3CA";
        var parentName = "课程分类";
        var parentLevel = 0;
        var store = baseGrid.getStore();

        var tempData;
        if (selCount == 1) {
            if (btn)
                tempData = recordData[0].data;
            else
                tempData = recordData;
            //当前节点
            pkValue = tempData.id;
            just = tempData.id;
            justName = tempData.text;

            //当前节点的上级节点
            parent = tempData.parent;
            parentCategory = store.getNodeById(parent);
            if (parentCategory) {
                parentName = parentCategory.get("text");
                parentLevel = parentCategory.get("level");
            }
        }

        if(justName=="未分类"){
            self.Warning("【未分类】不允许被操作！");
            return;
        }


        //默认的tab参数
        var tabTitle = funData.tabConfig.addTitle; //标签页的标题
        var tabItemId = funCode + "_mainGridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "coursetype.coursecategoryform";

        //根据不同的操作对数据进行组装
        switch (cmd) {
            case "child":
                operType = "add";
                insertObj = Ext.apply(insertObj, {
                    parentNode: just,
                    parentName: justName,
                    parentLevel: parentLevel,
                    uuid: ''
                });
                break;
            case "edit":
                if (selCount != 1) {
                    self.msgbox("请选择1条数据！");
                    return;
                }
                /*                if (btn) {
                 var rescords = baseGrid.getSelectionModel().getSelection();
                 if (rescords.length != 1) {
                 self.msgbox("请选择1条数据！");
                 return;
                 }
                 recordData = rescords[0].data;
                 }*/
                insertObj = tempData;
                //console.log(insertObj);
                insertObj = Ext.apply(insertObj, {
                    parentNode: parent,
                    parentName: parentName,
                    parentLevel: parentLevel,
                    uuid: just,
                    nodeText: justName,
                });
                tabTitle = funData.tabConfig.editTitle;
                tabItemId = funCode + "_mainGridEdit";
                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];
                break;
            case "order":
                if (selCount != 1) {
                    self.msgbox("请选择1条数据！");
                    return;
                }
                tabTitle = "重设排序";
                tabItemId = funCode + "_mainGridOrder";
                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];
                itemXtype = "coursetype.changeordergrid";
                operType = "edit";
                break;
        }
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            parentNode: just,
            parentName: justName,
            parentLevel: parentLevel,

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
                    cmd:cmd,
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
                var objDetForm = null;
                var formDeptObj = null;
                switch (cmd) {
                    case "child":
                    case "edit":
                        objDetForm = item.down("baseform[funCode=" + detCode + "]");
                        formDeptObj = objDetForm.getForm();                
                        self.setFormValue(formDeptObj, insertObj);
                        break;
                    case "order":
                        var changeOrderGrid = tabItem.down("grid[xtype=coursetype.changeordergrid]");
                        var proxy = changeOrderGrid.getStore().getProxy();
                        proxy.extraParams.filter= "[{'type':'string','comparison':'=','value':'" + record.get("parent") +"','field':'parentNode'}]"
                        changeOrderGrid.getStore().loadPage(1);
                        break;
                }
            }, 30);
        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },

    /**
     * 删除操作的处理
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doMainGridDelete: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;
        var selCount = 0;
        if (btn) {
            baseGrid = btn.up("basetreegrid");
            recordData = baseGrid.getSelectionModel().getSelection();
            selCount = recordData.lenght;
        } else {
            baseGrid = grid;
            recordData = record;
            selCount = 1;
        }
        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

        //得到配置信息
        var funData = basePanel.funData;
        var pkName = funData.pkName;
        if (selCount === 0) {
            self.Error("请选择要删除的课程分类");
            return;
        }
        //组装要删除的数据
        var ids = new Array();
        var hasWFL=0;   //是否包含【未分类】名称
        Ext.each(recordData, function (rec) {
            var pkValue = rec.get("id");
            var textValue = rec.get("text");
            var child = rec.childNodes.length;
            if (child === 0) {
                //仅能删除无子分类的数据
                ids.push(pkValue);
            }
            if(textValue=="未分类"){
                hasWFL=1;
            }
        });
        if (ids.length === 0) {
            self.Error("所选课程分类都有子项，不能删除");
            return;
        }
        if(hasWFL==1){
            self.Warning("【未分类】不允许被操作！");
            return;
        }

        var title = "删除时一并删除该分类下的课程,确定吗？";
        if (ids.length < recordData.length) {
            title = "仅删除无子分类的数据，\n删除时一并删除该分类下的课程，确定吗？";
        }
        Ext.Msg.confirm('删除提醒', title, function (btn, text) {
            if (btn == 'yes') {
                //发送ajax请求
                var resObj = self.ajax({
                    url: "/TrainCoursecategory/dodelete",
                    params: {
                        ids: ids.join(","),
                        pkName: pkName
                    }
                });
                if (resObj.success) {
                    baseGrid.getStore().load();
                    self.msgbox(resObj.obj);
                } else {
                    self.Error(resObj.obj);
                }
            }
        });
    },

    /*
     doDetail: function (btn, cmd) {
     //debugger;
     var self = this;
     var baseGrid = btn.up("basetreegrid");
     var funCode = baseGrid.funCode;
     var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");
     var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");
     var funData = basePanel.funData;
     var detCode = basePanel.detCode;
     var detLayout = basePanel.detLayout;
     //处理特殊默认值
     var defaultObj = funData.defaultObj;
     var insertObj = self.getDefaultValue(defaultObj);
     var popFunData = Ext.apply(funData, {
     grid: baseGrid,
     whereSql: " and isDelete='0' "
     });

     //先确定要选择记录
     var records = baseGrid.getSelectionModel().getSelection();
     if (records.length != 1 && cmd != "child") {
     self.Error("请先选择分类");
     return;
     }

     //当前节点
     var pkValue;
     var just = "ROOT";
     var justName = "ROOT";
     var parentCategory = "ROOT";
     var parentName = "ROOT";
     var parentLevel = 0;
     var store = baseGrid.getStore();

     if (records.length == 1) {
     //当前节点
     pkValue = records[0].get("id");
     just = records[0].get("id");
     justName = records[0].get("text");

     //当前节点的上级节点
     parent = records[0].get("parent");
     parentCategory = store.getNodeById(parent);
     if (parentCategory) {
     parentName = parentCategory.get("text");
     parentLevel = parentCategory.get("level");
     }
     }


     //根据选择的记录与操作确定form初始化的数据
     var iconCls = "x-fa fa-plus-circle";
     var title = "新建课程分类";
     var operType = cmd;

     //设置tab页的itemId
     var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
     //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
     var tabItem = tabPanel.getComponent(tabItemId);

     //判断是否已经存在tab了
     if (!tabItem) {
     var detLayout = basePanel.detLayout;
     var defaultObj = funData.defaultObj;

     //关键：window的视图控制器
     var otherController = basePanel.otherController;
     if (!otherController)
     otherController = '';

     //处理特殊默认值
     switch (cmd) {
     case "child":
     operType = "add";
     insertObj = Ext.apply(insertObj, {
     parentNode: just,
     parentName: justName,
     parentLevel: parentLevel,
     uuid: ''
     });
     break;
     case "edit":
     iconCls = "x-fa fa-pencil-square";
     operType = "edit";
     insertObj = records[0].data;
     insertObj = Ext.apply(insertObj, {
     parentNode: parent,
     parentName: parentName,
     parentLevel: parentLevel,
     uuid: just,
     nodeText: justName
     });
     break;
     }
     ;

     var tabTitle = funData.tabConfig.addTitle;

     tabItem = Ext.create({
     xtype: 'container',
     title: tabTitle,
     //iconCls: 'x-fa fa-clipboard',
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
     xtype: detLayout
     }]
     });
     tabItem.add(item);

     var objDetForm = item.down("baseform[funCode=" + detCode + "]");
     var formDeptObj = objDetForm.getForm();

     self.setFormValue(formDeptObj, insertObj);
     }, 30);

     } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
     var objDetForm = tabItem.down("baseform[funCode=" + detCode + "]");
     var formDeptObj = objDetForm.getForm();
     self.setFormValue(formDeptObj, records[0].data);
     self.setFuncReadOnly(funData, objDetForm, true);

     }

     tabPanel.setActiveTab(tabItem);

     /!*        switch (cmd) {
     case "child":
     operType = "add";
     insertObj = Ext.apply(insertObj, {
     parentNode: just,
     parentName: justName,
     parentLevel:parentLevel,
     uuid: ''
     });
     break;
     case "brother":
     title = "增加同级分类";
     operType = "add";
     insertObj = Ext.apply(insertObj, {
     parentNode: parent,
     parentName: parentName,
     parentLevel:parentLevel,
     uuid: ''
     });
     break;
     case "edit":
     iconCls = "x-fa fa-pencil-square";
     operType = "edit";
     title = "修改分类";

     insertObj = records[0].data;
     insertObj = Ext.apply(insertObj, {
     parentNode: parent,
     parentName: parentName,
     parentLevel:parentLevel,
     uuid: just,
     nodeText: justName
     });
     break;
     }
     var winId = detCode + "_win";
     var win = Ext.getCmp(winId);
     if (!win) {
     win = Ext.create('core.base.view.BaseFormWin', {
     id: winId,
     title: title,
     width: 500,
     height: 350,
     resizable: false,
     iconCls: iconCls,
     controller: 'coursetype.otherController',
     operType: operType,
     funData: popFunData,
     funCode: detCode,
     //给form赋初始值
     insertObj: insertObj,
     items: [{
     xtype: "coursetype.detaillayout",
     funData: {
     action: comm.get("baseUrl") + "/TrainCoursecategory", //请求Action
     whereSql: "", //表格查询条件
     orderSql: "", //表格排序条件
     pkName: "uuid",
     defaultObj: {}
     },
     items: [{
     xtype: "coursetype.coursecategoryform"
     }]
     }]
     });
     }
     win.show();*!/
     /!*        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
     var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
     var formDeptObj = objDetForm.getForm();
     //表单赋值

     self.setFormValue(formDeptObj, insertObj);*!/
     },

     */

});
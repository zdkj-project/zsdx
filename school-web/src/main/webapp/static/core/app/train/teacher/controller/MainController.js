Ext.define("core.train.teacher.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.teacher.mainController',
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
        /*导入*/
        "basegrid[xtype=teacher.maingrid] button[ref=gridImport]": {
            beforeclick: function (btn) {
                var self = this;

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级

                //得到组件
                var baseGrid = btn.up("basegrid");

                var win = Ext.create('Ext.Window', {
                    title: "导入师资数据",
                    iconCls: 'x-fa fa-clipboard',
                    width: 400,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'teacher.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "teacher.teacherimportform"
                    }]
                });
                win.show();
                return false;
            }
        },
        /**
         * 导出
         */
        "basegrid[xtype=teacher.maingrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                var title = "将导出所有的师资信息";
                var ids = new Array();
                if (records.length > 0) {
                    title = "将导出所选师资的信息";
                    Ext.each(records, function (rec) {
                        var pkValue = rec.get(pkName);
                        ids.push(pkValue);
                    });

                }
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        window.location.href = comm.get('baseUrl') + "/TrainTeacher/exportExcel?ids=" + ids.join(",");
                        /*                  Ext.create('Ext.panel.Panel', {
                         title: 'HelloWorld',
                         width: 200,
                         html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="' + comm.get('baseUrl') + '/TrainTeacher/exportExcel?ids=' + ids.join(",") + '"></iframe>',
                         renderTo: Ext.getBody()
                         });*/
                        // var task = new Ext.util.DelayedTask(function () {
                        //     Ext.Msg.hide();
                        // });
                        Ext.Ajax.request({
                            url: comm.get('baseUrl') + '/TrainTeacher/checkExportEnd',
                            success: function (resp, opts) {
                                //task.delay(1200);
                                Ext.Msg.hide();
                            },
                            failure: function (resp, opts) {
                                task.delay(1200);
                                var respText = Ext.util.JSON.decode(resp.responseText);
                                Ext.Msg.alert('错误', respText.error);
                            }
                        });
                    }
                });
                return false;
            }
        },

        /**
         * 下载模板
         */
        "basegrid[xtype=teacher.maingrid] button[ref=gridDownTemplate]": {
            beforeclick: function (btn) {
                var self = this;
                var title = "下载师资导入模板";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        window.location.href = comm.get('baseUrl') + "/static/upload/template/teacher.xls";
                        /*           Ext.create('Ext.panel.Panel', {
                         width: 200,
                         html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="' + comm.get('baseUrl') + '/static/upload/template/teacher.xls"></iframe>',
                         renderTo: Ext.getBody(),
                         listeners: {
                         afterrender: function () {
                         var task = new Ext.util.DelayedTask(function () {
                         Ext.Msg.hide();
                         });
                         task.delay(3000);
                         }
                         }
                         });*/
                    }
                });
                return false;
            }
        },

        /**
         * 师资列表操作列
         */
        "basegrid  actioncolumn": {
            //操作列编辑
            editClick_Tab: function (data) {
                this.doTeacherDetail_Tab(null, "edit", data.view, data.record);
                return false;
            },
            //操作列详细
            detailClick_Tab: function (data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doTeacherInfo_Tab(null, "detail", baseGrid, record);
                //this.doTeacherDetail_Tab(null, "detail", baseGrid, record);
                return false;
            },
            //操作列删除
            deleteClick: function (data) {
                console.log(data);
            },
            //操作列个人简介
            gridTeacherInfoClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                this.doTeacherInfo_Tab(null, "teacherInfo", baseGrid, record);

                return false;
            },
            //操作列主讲课程
            gridTeacherCourseClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doTeacherCourse_Tab(null, cmd, baseGrid, record);

                return false;
            },
            //操作列上课记录
            gridTeachingClick_Tab:function(data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd;
                this.doTeachingClick_Tab(null, cmd, baseGrid, record);
                return false;
            }
        }
    },

    /**
     * 增加或修改师资详细处理
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doTeacherDetail_Tab: function (btn, cmd, grid, record) {
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
        var defaultObj = funData.defaultObj;

        var operType = cmd;
        var pkValue = null;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);

        //一些要传递的参数
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            whereSql: " and isDelete='0' ",
        });

        //默认的tab参数
        var tabTitle = funData.tabConfig.addTitle; //标签页的标题
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复

        //根据操作命令组装不同的数据
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
                        xtype: detLayout
                    }]
                });
                tabItem.add(item);

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                var courseDesc = objDetForm.down("htmleditor");

                self.setFormValue(formDeptObj, insertObj);
                //显示照片
                objDetForm.down('image[ref=newsImage]').setSrc(insertObj.zp);
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },

    /**
     * 查看主讲课程处理
     */
    doTeacherCourse_Tab: function (btn, cmd, grid, record) {
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

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "teacher_courseDetail";   //修改此funCode，方便用于捕获window的确定按钮
        //var detCode = basePanel.detCode;   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = {};
        //根据cmd操作类型，来设置不同的值
        var tabTitle = "主讲课程";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridTeacherCourseDetail";     //命名规则：funCode+'_ref名称',确保不重复
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
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            teacherId:insertObj.uuid,
            xm:insertObj.xm
        });
        pkValue = insertObj.uuid;
        tabTitle = insertObj.xm + "-主讲课程";
        tabItemId = funCode + "_gridTeacherCourseDetail" + insertObj.xm;

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
                        //padding:5,
                        margin: 10,
                        funCode: detCode,
                        items: [{
                            xtype: "teacher.coursegrid"
                        }]
                    }]
                });
                tabItem.add(item);

                //var coursegrid=win.down("grid[xtype=teacher.coursegrid]");
                var coursegrid = tabItem.down("grid[xtype=teacher.coursegrid]");
                var proxy = coursegrid.getStore().getProxy();
                proxy.extraParams.filter = "[{'type':'string','comparison':'','value':'" + record.get("uuid") + "','field':'mainTeacherId'}]";
                coursegrid.getStore().loadPage(1);
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },
    /**
     * 操作列个人简介处理
     */
    doTeacherInfo_Tab: function (btn, cmd, grid, record) {
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

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "teacher_courseDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
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

        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择1条数据！");
                return;
            }
            recordData = rescords[0].data;
        }

        insertObj = recordData;

        //根据cmd操作类型，来设置不同的值
        var tabTitle = insertObj.xm +"-教师详情";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridDetail_Tab" + insertObj.uuid;     //命名规则：funCode+'_ref名称',确保不重复
        var ItemXtype="teacher.detailhtmlpanel";
        var pkValue = null;
        var operType = cmd;

        if(cmd=="teacherInfo"){
            tabTitle = insertObj.xm + "-个人简介";
            tabItemId = funCode + "_gridTeacherInfoDetail" + insertObj.uuid;
            ItemXtype="teacher.teacherinfohtmlpanel";
            operType="detail";
        }
       
/*
        var ddCode = "XLM";
        var store = Ext.create("Ext.data.Store", {
            fields: factory.ModelFactory.getFields({
                modelName: "com.zd.school.plartform.baseset.model.BaseDicitem",
                excludes: ""
            }),
            data: factory.DDCache.getItemByDDCode("XLM")
        });
        //将数据字典数据赋值到组件属性上
        var dicData = {};
        var storeData = store.getData();
        for (var i = 0; i < storeData.length; i++) {
            var rec = storeData.items[i];
            if (rec.get("itemCode") == insertObj.xlm) {
                insertObj.xlm = rec.get("itemName");
                break;
            }
        }*/
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
                        //padding:5,
                        margin: 10,
                        funCode: detCode,
                        items: [{
                            xtype:ItemXtype
                            //data: insertObj
                        }]
                    }]
                });
                tabItem.add(item);

                var ddCodes=['XBM','TECHNICAL','XLM','ZYM','INOUT','HEADSHIPLEVEL'];
                var propNames=['xbm','technical','xlm','zym','inout','headshipLevel'];
                for(var i=0;i<ddCodes.length;i++){                
                    var ddItem = factory.DDCache.getItemByDDCode(ddCodes[i]);
                    var resultVal="";
                    var value=insertObj[propNames[i]];
                    for (var j = 0; j < ddItem.length; j++) {
                        var ddObj = ddItem[j];
                        if (value == ddObj["itemCode"]) {
                            resultVal = ddObj["itemName"];
                            break;
                        }
                    }
                    insertObj[propNames[i]]=resultVal;
                }
               
                var teacherInfoContainer=item.down("container[ref=teacherInfo]");
                teacherInfoContainer.setData(insertObj);

                if(cmd=="detail"){
                    //读取主讲课程信息
                    self.asyncAjax({
                        url: comm.get("baseUrl")  + "/TrainCourseinfo/list",
                        params: {
                            filter: "[{'type':'string','comparison':'','value':'" + insertObj.uuid + "','field':'mainTeacherId'}]",
                            page:1,
                            start:0,
                            limit:-1    //-1表示不分页
                        },
                        //回调代码必须写在里面
                        success: function(response) {
                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            var rows=data.rows;
                            //console.log(rows);
                            if(rows!=undefined){  //若存在rows数据，则表明请求正常
                                var classCourseInfoContainer=item.down("container[ref=classCourseInfo]");  
                                classCourseInfoContainer.setData(rows);     
                            }else{
                                self.Error(data.obj);
                            }
                        }
                    });  

                    //读取课程记录信息
                    self.asyncAjax({
                        url: comm.get("baseUrl")  + "/TrainClassschedule/listCourseEval",
                        params: {
                            propName:"teacherId,teacherName",
                            propValue:insertObj.uuid + "," + insertObj.xm,
                            joinMethod : "or",
                            page:1,
                            start:0,
                            limit:-1    //-1表示不分页
                        },
                        //回调代码必须写在里面
                        success: function(response) {
                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            var rows=data.rows;
                            //console.log(rows);
                            if(rows!=undefined){  //若存在rows数据，则表明请求正常
                                var classCourseRecordContainer=item.down("container[ref=classCourseRecord]");  
                                classCourseRecordContainer.setData(rows);     
                            }else{
                                self.Error(data.obj);
                            }
                        }
                    });  
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },

    /**
     * 操作列查看上课记录处理
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

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "teacher_courseDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = {};
/*        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });*/

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "上课记录";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridTeacherTeachingDetail";     //命名规则：funCode+'_ref名称',确保不重复
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
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            teacherId:insertObj.uuid,
            xm:insertObj.xm
        });
        tabTitle = insertObj.xm + "-上课记录";
        tabItemId = funCode + "_gridTeacherTeachingDetail" + insertObj.xm;

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
                        //padding:5,
                        margin: 10,
                        funCode: detCode,
                        items: [{
                            xtype: "teacher.teachingrid"
                        }]
                    }]
                });
                tabItem.add(item);

                var teachingGrid = tabItem.down("grid[xtype=teacher.teachingrid]");
                var proxy = teachingGrid.getStore().getProxy();
                proxy.extraParams.propName="teacherId,teacherName";
                proxy.extraParams.propValue=record.get("uuid") + "," + record.get("xm");
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
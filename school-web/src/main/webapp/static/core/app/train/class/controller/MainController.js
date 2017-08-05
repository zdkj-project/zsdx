Ext.define("core.train.class.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.class.mainController',
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
         * 导出班级信息（学员、课程、住宿、用餐)
         */
        "basegrid[xtype=class.maingrid] button[ref=gridExport]": {
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
                if (records.length != 1) {
                    self.Warning("请选择一个班级进行导出操作！");
                    return;
                }

                var rec = records[0];

                var title = "确定要导出【" + rec.get("className") + "】班级信息吗？";

                var ids = new Array();
                var pkValue = rec.get(pkName);
                ids.push(pkValue);
                // if (records.length > 0) {
                //     title = "将导出所选师资的信息";
                //     Ext.each(records, function (rec) {
                //         var pkValue = rec.get(pkName);
                //         ids.push(pkValue);
                //     });
                // }

                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClass/exportExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });


                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClass/checkExportEnd',
                                timeout: 1000 * 60 * 30,        //半个小时
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
                                        component.destroy();
                                    } else {
                                        if (data.obj == 0) {    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();
                                        } else {
                                            setTimeout(function () {
                                                time()
                                            }, 1000);
                                        }
                                    }
                                },
                                failure: function (response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function () {
                            time()
                        }, 1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },

        //maingrid的表格按钮事件
        "basegrid[xtype=class.maingrid] button[ref=gridAdd_Tab]": {
            beforeclick: function (btn) {

                this.doClassDetail_Tab(btn, "add");

                return false;
            }
        },
        "basegrid[xtype=class.maingrid] button[ref=gridEdit_Tab]": {
            beforeclick: function (btn) {

                this.doClassDetail_Tab(btn, "edit");

                return false;
            }
        },
        "basegrid[xtype=class.maingrid] button[ref=gridDetail_Tab]": {
            beforeclick: function (btn) {

                this.doClassDetail_Tab(btn, "detail");

                return false;
            }
        },
        /**
         * 提交按钮事件
         */
        "basegrid[xtype=class.maingrid] button[ref=gridUse]": {
            beforeclick: function (btn) {
                var self = this;
                this.doSendUserDetail_Tab(btn, "add");
                //得到组件
                /*                var baseGrid = btn.up("basegrid");
                                var funCode = baseGrid.funCode;
                                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                                var funData = basePanel.funData;
                                var detCode = basePanel.detCode;
                                var detLayout = basePanel.detLayout;

                                var records = baseGrid.getSelectionModel().getSelection();
                                if (records.length != 1) {
                                    self.Warning("请选择数据!");
                                    return;
                                }
                                var classId = records[0].get("uuid");
                                var className = records[0].get("className");

                                if (!classId) {
                                    self.Warning("信息有误，请选择班级！");
                                    return false;
                                }
                                if(records[0].get("isuse")==1||records[0].get("isuse")==3){
                                    self.Warning("此班级已是提交状态，不必重复提交！");
                                    return false;
                                }
                                var popFunData = Ext.apply(funData, {
                                    grid: baseGrid,
                                    classId: classId,
                                    className: className
                                });
                                var win = Ext.create('core.base.view.BaseFormWin', {
                                    iconCls: 'x-fa fa-plus-circle',
                                    operType: "addReutn",
                                    funData: popFunData,
                                    funCode: detCode,
                                    width:600,
                                    height:450,
                                    items: [{
                                        xtype:detLayout,
                                        defaults:null,
                                        items:[{
                                            xtype:'class.sendinfoform'
                                        }]
                                    }]
                                });
                                win.show();*/

                /*
                                Ext.MessageBox.confirm('温馨提示', '<p>提交此班级信息之后，将会自动通知总务去安排培训信息！</p><p style="color:red;font-size:14px;font-weight: 400;">你确定要提交吗？</p>', function(btn, text) {
                                    if (btn == 'yes') {
                                        Ext.Msg.wait('正在执行中,请稍后...', '温馨提示');
                                        self.asyncAjax({
                                            url: comm.get("baseUrl")  + "/TrainClass/doClassUse",
                                            params: {
                                                classId:classId
                                            },
                                            timeout:1000*60*60, //1个小时
                                            //回调代码必须写在里面
                                            success: function(response) {
                                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                                if(data.success){
                                                    baseGrid.getStore().load();
                                                    Ext.Msg.hide();
                                                    self.Info(data.obj);
                                                }else{
                                                    Ext.Msg.hide();
                                                    self.Error(data.obj);
                                                }
                                            },
                                            failure: function(response) {
                                                Ext.Msg.hide();
                                                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                            }
                                        });
                                    }
                                });*/


                return false;
            }
        },

        /*导入*/
        "basegrid[xtype=class.maingrid] button[ref=gridImport]": {
            beforeclick: function (btn) {
                var self = this;

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级

                //得到组件
                var baseGrid = btn.up("basegrid");

                var win = Ext.create('Ext.Window', {
                    title: "导入班级数据",
                    iconCls: 'x-fa fa-clipboard',
                    width: 450,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'class.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "class.classimportform"
                    }]
                });
                win.show();

                return false;
            }
        },
        "basegrid[xtype=class.maingrid] button[ref=gridImportTrainee]": {
            beforeclick: function (btn) {
                var self = this;

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级

                //得到组件
                var baseGrid = btn.up("basegrid");

                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择数据!");
                    return;
                }

                var classId = records[0].get("uuid");
                var className = records[0].get("className");
                ;

                if (!classId) {
                    self.Warning("信息有误，请选择班级！");
                    return false;
                }

                var win = Ext.create('Ext.Window', {
                    title: "导入班级学员",
                    iconCls: 'x-fa fa-clipboard',
                    width: 450,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'class.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "class.traineeimportform"
                    }]
                });
                win.show();

                var objDetForm = win.down(
                    "form[xtype=class.traineeimportform]");
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("classId").setValue(classId);
                formDeptObj.findField("className").setValue(
                    className);

                return false;
            }
        },
        "basegrid[xtype=class.maingrid] button[ref=gridImportCourse]": {
            beforeclick: function (btn) {
                var self = this;

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级

                //得到组件
                var baseGrid = btn.up("basegrid");

                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择数据!");
                    return;
                }

                var classId = records[0].get("uuid");
                var className = records[0].get("className");
                ;

                if (!classId) {
                    self.Warning("信息有误，请选择班级！");
                    return false;
                }

                var win = Ext.create('Ext.Window', {
                    title: "导入班级课程",
                    iconCls: 'x-fa fa-clipboard',
                    width: 450,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'class.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "class.courseimportform"
                    }]
                });
                win.show();

                var objDetForm = win.down(
                    "form[xtype=class.courseimportform]");
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("classId").setValue(classId);
                formDeptObj.findField("className").setValue(
                    className);

                return false;
            }
        },
        "basegrid[xtype=class.maingrid]": {
            itemclick: function (grid, record, item, index, e, eOpts) {
                var basePanel = grid.up("basepanel");
                var funCode = basePanel.funCode;
                var baseGrid = basePanel.down("basegrid[funCode=" +
                    funCode + "]");
                var records = baseGrid.getSelectionModel().getSelection();
                var btnImportTrainee = baseGrid.down("button[ref=gridImportTrainee]");
                var btnImportCourse = baseGrid.down("button[ref=gridImportCourse]");
                var btnUse = baseGrid.down("button[ref=gridUse]");

                var btnEdit = baseGrid.down("button[ref=gridEdit_Tab]");
                var btnDelete = baseGrid.down("button[ref=gridDelete]");

                if (records.length == 0) {
                    if (btnImportTrainee)
                        btnImportTrainee.setDisabled(true);
                    if (btnImportCourse)
                        btnImportCourse.setDisabled(true);
                    if (btnUse)
                        btnUse.setDisabled(true);

                } else if (records.length == 1) {

                    if (record.get("isuse") == 1 || record.get("isuse") == 3) {
                        if (btnImportTrainee)
                            btnImportTrainee.setDisabled(true);
                        if (btnImportCourse)
                            btnImportCourse.setDisabled(true);
                        if (btnUse)
                            btnUse.setDisabled(true);
                        if (btnDelete)
                            btnDelete.setDisabled(true);
                        if (btnEdit)
                            btnEdit.setDisabled(false);
                    } else if (record.get("isuse") == 2) {
                        if (btnImportTrainee)
                            btnImportTrainee.setDisabled(true);
                        if (btnImportCourse)
                            btnImportCourse.setDisabled(true);
                        if (btnDelete)
                            btnDelete.setDisabled(true);
                        if (btnUse)
                            btnUse.setDisabled(false);
                        if (btnEdit)
                            btnEdit.setDisabled(false);
                    } else {
                        if (btnImportTrainee)
                            btnImportTrainee.setDisabled(false);
                        if (btnImportCourse)
                            btnImportCourse.setDisabled(false);
                        if (btnUse)
                            btnUse.setDisabled(false);
                    }


                } else {
                    if (btnImportTrainee)
                        btnImportTrainee.setDisabled(true);
                    if (btnImportCourse)
                        btnImportCourse.setDisabled(true);
                    if (btnUse)
                        btnUse.setDisabled(true);
                }
                //console.log(1231);
            }
            /* 已不使用
        itemclick:function(grid,record){
            var basePanel = grid.up("basepanel");
            var courseGrid = basePanel.down("basegrid[xtype=class.coursegrid]");
            var studentGrid= basePanel.down("basegrid[xtype=class.studentgrid]");

            var filter = "[{'type':'string','comparison':'=','value':'" + record.get("uuid") + "','field':'classId'}]"


            var courseStore = courseGrid.getStore();
            var courseProxy = courseStore.getProxy();
            courseProxy.extraParams = {
                filter: filter
            };
            courseStore.loadPage(1); // 给form赋值

            var studentStore = studentGrid.getStore();
            var studentProxy = studentStore.getProxy();
            studentProxy.extraParams = {
                filter: filter
            };
            studentStore.loadPage(1); // 给form赋值
        }*/
        },

        "basegrid[xtype=class.coursegrid] button[ref=gridAdd]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var store = baseGrid.getStore();
                //得到模型
                var Model = store.model;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");

                //判断是否选择了班级
                var classGrid = basePanel.down(
                    "basegrid[xtype=class.maingrid]");
                var selectObject = classGrid.getSelectionModel().getSelection();
                if (selectObject.length != 1) {
                    self.Warning("请选择一项班级数据!");
                    return false;
                }


                //得到配置信息
                var funData = basePanel.funData;
                var detCode = "coursegrid_detail"; //这个值换为其他，防止多个window误入other控制器中的同一个事件
                var detLayout = basePanel.detLayout;
                var defaultObj = funData.defaultObj;

                //关键：window的视图控制器
                var otherController = basePanel.otherController;
                if (!otherController)
                    otherController = '';

                var width = 600;
                var height = 300;

                //处理特殊默认值
                var insertObj = self.getDefaultValue(defaultObj);
                //填入选择的班级的值
                insertObj = Ext.apply(insertObj, {
                    classId: selectObject[0].get("uuid"),
                    className: selectObject[0].get(
                        "className"),

                });


                var popFunData = Ext.apply(funData, {
                    grid: baseGrid,
                    filter: "[{'type':'string','comparison':'=','value':'" +
                    insertObj.classId +
                    "','field':'classId'}]"
                });
                var win = Ext.create('core.base.view.BaseFormWin', {
                    iconCls: 'x-fa fa-plus-circle',
                    operType: 'add',
                    width: width,
                    height: height,
                    controller: otherController, //指定视图控制器，从而能够使指定的控制器的事件生效
                    funData: popFunData,
                    funCode: detCode,
                    insertObj: insertObj,
                    items: [{
                        xtype: detLayout,
                        funCode: detCode, //这里将funcode修改为刚刚的detcode值
                        funData: {
                            action: comm.get(
                                "baseUrl") +
                            "/TrainClassschedule", //请求Action
                            whereSql: "", //表格查询条件
                            orderSql: "", //表格排序条件
                            pkName: "uuid",
                            defaultObj: {}
                        },
                        items: [{
                            xtype: "class.coursedetailform",
                            funCode: detCode, //这里将funcode修改为刚刚的detcode值
                        }]
                    }]
                });
                win.show();
                var detPanel = win.down("basepanel[funCode=" +
                    detCode + "]");
                var objDetForm = detPanel.down("baseform[funCode=" +
                    detCode + "]");
                var formDeptObj = objDetForm.getForm();

                self.setFormValue(formDeptObj, insertObj);

                //执行回调函数
                if (btn.callback) {
                    btn.callback();
                }
                return false;
            }
        },
        "basegrid[xtype=class.studentgrid]  button[ref=gridAdd]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var store = baseGrid.getStore();
                //得到模型
                var Model = store.model;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");

                //判断是否选择了班级
                var classGrid = basePanel.down(
                    "basegrid[xtype=class.maingrid]");
                var selectObject = classGrid.getSelectionModel().getSelection();
                if (selectObject.length != 1) {
                    self.Warning("请选择一项班级数据!");
                    return false;
                }

                //得到配置信息
                var funData = basePanel.funData;
                var detCode = "studentgrid_detail"; //这个值换为自定义值，防止多个window误入other控制器中的同一个事件
                var detLayout = basePanel.detLayout;
                var defaultObj = funData.defaultObj;

                //关键：window的视图控制器
                var otherController = basePanel.otherController;
                if (!otherController)
                    otherController = '';

                var width = 500;
                var height = 300;

                //处理特殊默认值
                var insertObj = self.getDefaultValue(defaultObj);
                //填入选择的班级的值
                insertObj = Ext.apply(insertObj, {
                    classId: selectObject[0].get("uuid"),
                    className: selectObject[0].get(
                        "className")
                });

                var popFunData = Ext.apply(funData, {
                    grid: baseGrid,
                    filter: "[{'type':'string','comparison':'=','value':'" +
                    insertObj.classId +
                    "','field':'classId'}]"
                });
                var win = Ext.create('core.base.view.BaseFormWin', {
                    iconCls: 'x-fa fa-plus-circle',
                    operType: 'add',
                    width: width,
                    height: height,
                    controller: otherController, //指定视图控制器，从而能够使指定的控制器的事件生效
                    funData: popFunData,
                    funCode: detCode,
                    insertObj: insertObj,
                    items: [{
                        xtype: detLayout,
                        funCode: detCode, //这里将funcode修改为刚刚的detcode值
                        funData: {
                            action: comm.get(
                                "baseUrl") +
                            "/TrainClasstrainee", //请求Action
                            whereSql: "", //表格查询条件
                            orderSql: "", //表格排序条件
                            pkName: "uuid",
                            defaultObj: {}
                        },
                        items: [{
                            xtype: "class.studentdetailform",
                            funCode: detCode, //这里将funcode修改为刚刚的detcode值
                        }]
                    }]
                });
                win.show();
                var detPanel = win.down("basepanel[funCode=" +
                    detCode + "]");
                var objDetForm = detPanel.down("baseform[funCode=" +
                    detCode + "]");
                var formDeptObj = objDetForm.getForm();

                self.setFormValue(formDeptObj, insertObj);

                //执行回调函数
                if (btn.callback) {
                    btn.callback();
                }
                return false;
            }
        },
        "basegrid[xtype=class.studentgrid] button[ref=gridDelete]": {
            beforeclick: function (btn) {
                var self = this;

                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length > 0) {
                    //封装ids数组
                    Ext.Msg.confirm('提示', '是否删除数据?', function (btn,
                                                               text) {
                        if (btn == 'yes') {
                            var ids = new Array();
                            Ext.each(records, function (rec) {
                                var pkValue = rec.get(
                                    pkName);
                                ids.push(pkValue);
                            });
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: comm.get(
                                    "baseUrl") +
                                "/TrainClasstrainee/dodelete",
                                params: {
                                    ids: ids.join(
                                        ","),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                baseGrid.getStore().load(0);
                                self.Info(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                } else {
                    self.Warning("请选择数据");
                }
                return false;
            }
        },
        "basegrid[xtype=class.coursegrid] button[ref=gridDelete]": {
            beforeclick: function (btn) {
                var self = this;

                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length > 0) {
                    //封装ids数组
                    Ext.Msg.confirm('提示', '是否删除数据?', function (btn,
                                                               text) {
                        if (btn == 'yes') {
                            var ids = new Array();
                            Ext.each(records, function (rec) {
                                var pkValue = rec.get(
                                    pkName);
                                ids.push(pkValue);
                            });
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: comm.get(
                                    "baseUrl") +
                                "/TrainClassschedule/dodelete",
                                params: {
                                    ids: ids.join(
                                        ","),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                baseGrid.getStore().load(0);
                                self.Info(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                } else {
                    self.Warning("请选择数据");
                }
                return false;
            }
        },


        "basegrid[xtype=class.maingrid] button[ref=gridDelete]": {
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
                if (records.length > 0) {
                    //封装ids数组
                    Ext.Msg.confirm('温馨提示', '是否删除数据？（已提交的班级，不会被删除）', function (btn, text) {
                        if (btn == 'yes') {
                            var ids = new Array();
                            Ext.each(records, function (rec) {
                                if (rec.get("isuse") != 1) {
                                    var pkValue = rec.get(pkName);
                                    ids.push(pkValue);
                                }
                            });
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: funData.action + "/dodelete",
                                params: {
                                    ids: ids.join(","),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                baseGrid.getStore().load(0);
                                self.Info(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                } else {
                    self.Warning("请选择数据");
                }
                return false;
            }
        },
        "basegrid[xtype=class.maingrid]  button[ref=gridDetail]": {
            beforeclick: function (btn) {

                this.doDetail(btn, "detail");

                return false;
            }
        },
        "basegrid[xtype=class.maingrid]  button[ref=gridEdit]": {
            beforeclick: function (btn) {

                this.doDetail(btn, "edit");

                return false;
            }
        },
        "basegrid[xtype=class.maingrid]  actioncolumn": {
            gridTranieeClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doTranieeDetail_Tab(null, cmd, baseGrid, record);

                return false;
            },
            gridCourseClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doCourseDetail_Tab(null, cmd, baseGrid, record);

                return false;
            },
            gridRoomClick: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doRoomDetail(null, cmd, baseGrid, record);

                return false;
            },

            gridFoodClick: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doFoodDetail(null, cmd, baseGrid, record);

                return false;
            },
            editClick: function (data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doDetail(null, "edit", baseGrid, record);

                return false;
            },
            detailClick: function (data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doDetail(null, "detail", baseGrid, record);

                return false
            },
            editClick_Tab: function (data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doClassDetail_Tab(null, "edit", baseGrid, record);

                return false;
            },
            detailClick_Tab: function (data) {

                var baseGrid = data.view;
                var record = data.record;

                this.doClassDetail_Tab(null, "detail", baseGrid, record);

                return false
            },
            gridRoomClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doRoomDetail_Tab(null, cmd, baseGrid, record);

                return false;
            },
            gridFoodClick_Tab: function (data) {
                var baseGrid = data.view;
                var record = data.record;
                var cmd = data.cmd
                this.doFoodDetail_Tab(null, cmd, baseGrid, record);

                return false;
            },
        },


        "basegrid[xtype=class.coursegrid]  actioncolumn": {
            editClick: function (data) {
                //console.log(data);
            },
            detailClick: function (data) {
                // console.log(data);
            },
            deleteClick: function (data) {
                var self = this;

                var baseGrid = data.view;
                var record = data.record;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;


                Ext.Msg.confirm('提示', '是否删除数据?', function (btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                            "/TrainClassschedule/dodelete",
                            params: {
                                ids: record.get(
                                    pkName),
                                pkName: pkName
                            }
                        });
                        if (resObj.success) {
                            //baseGrid.getStore().load(0);

                            baseGrid.getStore().remove(
                                record); //不刷新的方式
                            baseGrid.getView().refresh();
                            self.Info(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            },
        },
        "basegrid[xtype=class.studentgrid]  actioncolumn": {
            editClick: function (data) {
                console.log(data);

            },
            detailClick: function (data) {
                console.log(data);
            },
            deleteClick: function (data) {
                var self = this;

                var baseGrid = data.view;
                var record = data.record;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;


                Ext.Msg.confirm('提示', '是否删除数据?', function (btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                            "/TrainClasstrainee/dodelete",
                            params: {
                                ids: record.get(
                                    pkName),
                                classId: record.get("classId"),
                                pkName: pkName
                            }
                        });
                        if (resObj.success) {
                            //baseGrid.getStore().load(0);

                            baseGrid.getStore().remove(
                                record); //不刷新的方式
                            baseGrid.getView().refresh();
                            self.Info(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            },
        }

    },

    doDetail: function (btn, cmd, grid, record) {

        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到模型
        var store = baseGrid.getStore();
        var Model = store.model;
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode +
            "]");
        //得到配置信息
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);

        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        var width = 1000;
        var height = 650;
        if (funData.width)
            width = funData.width;
        if (funData.height)
            height = funData.height;

        var iconCls = 'x-fa fa-plus-circle';
        var operType = cmd;
        var title = "增加";

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

                iconCls = 'x-fa fa-pencil-square',
                    operType = "edit";
                title = "编辑";
                break;
            case "detail":

                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;

                iconCls = 'x-fa fa-file-text',
                    operType = "detail";
                title = "详情";
                break;
        }

        var win = Ext.create('core.base.view.BaseFormWin', {
            title: title,
            iconCls: iconCls,
            operType: operType,
            width: width,
            height: height,
            controller: otherController,
            funData: popFunData,
            funCode: detCode,
            insertObj: insertObj,
            items: [{
                xtype: detLayout
            }]
        });
        win.show();


        var detPanel = win.down("basepanel[funCode=" + detCode +
            "]");
        var objDetForm = detPanel.down("baseform[funCode=" +
            detCode + "]");
        var formDeptObj = objDetForm.getForm();

        //获取班级学员和课程信息
        self.asyncAjax({
            url: funData.action + "/getClassInfo",
            params: {
                classId: insertObj.uuid
            },
            timeout: 1000 * 30 * 60,
            success: function (response) {
                resObj = Ext.decode(Ext.valueFrom(
                    response.responseText, '{}'
                ));

                if (resObj.success) {
                    insertObj = Ext.apply(insertObj, {
                        classTraineeId: resObj.obj
                            .classTraineeId,
                        traineeId: resObj.obj.traineeId,
                        traineeName: resObj.obj
                            .traineeName,
                        traineeXbm: resObj.obj.traineeXbm,
                        courseId: resObj.obj.courseId,
                        courseName: resObj.obj.courseName
                    });

                    self.setFormValue(formDeptObj,
                        insertObj);
                } else {

                    self.setFormValue(formDeptObj,
                        insertObj);

                    self.Error(data.obj);
                }
            }
        });


        //取消此field的只读状态
        var courseNameField = formDeptObj.findField("courseName");
        courseNameField.configInfo.filter =
            "[{'type':'string','comparison':'=','value':'" +
            insertObj.uuid + "','field':'classId'}]";
        courseNameField.setReadOnly(false);

        if (cmd == "detail") {
            self.setFuncReadOnly(funData, objDetForm, true);
            courseNameField.setReadOnly(true);
        }
    },

    doFoodDetail: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到模型
        var store = baseGrid.getStore();
        var Model = store.model;
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "class_footDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值    
        var insertObj = {};

        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        var width = 700;
        var height = 650;

        var iconCls = 'x-fa fa-plus-circle';
        var operType = cmd;
        var title = "增加";

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

                iconCls = 'x-fa fa-pencil-square',
                    operType = "edit";
                title = "就餐申请";
                break;
            case "detail":

                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;

                iconCls = 'x-fa fa-file-text',
                    operType = "detail";
                title = "就餐申请详请";
                break;
        }

        var win = Ext.create('core.base.view.BaseFormWin', {
            title: title,
            iconCls: iconCls,
            operType: operType,
            width: width,
            height: height,
            controller: otherController,
            funData: popFunData,
            funCode: detCode,    //修改此funCode，方便用于捕获window的确定按钮
            insertObj: insertObj,
            items: [{
                xtype: detLayout,
                funCode: detCode,
                items: [{
                    xtype: "class.fooddetailform"
                }]
            }]
        });
        win.show();


        var detPanel = win.down("basepanel[funCode=" + detCode +
            "]");
        var objDetForm = detPanel.down("baseform[funCode=" +
            detCode + "]");
        var formDeptObj = objDetForm.getForm();

        /*默认金额，在model中设置默认值比较好些*/
        if (insertObj.breakfastStand == 0) {
            insertObj.breakfastStand = 20;
        }
        if (insertObj.lunchStand == 0) {
            insertObj.lunchStand = 50;
        }
        if (insertObj.dinnerStand == 0) {
            insertObj.dinnerStand = 50;
        }

        self.setFormValue(formDeptObj, insertObj);

        //查询班级的学员信息
        self.asyncAjax({
            url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
            params: {
                classId: insertObj.uuid,
                page: 1,
                start: 0,
                limit: -1    //-1表示不分页
            },
            //回调代码必须写在里面
            success: function (response) {
                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                var rows = data.rows;
                //console.log(rows);
                if (rows != undefined) { //若存在rows数据，则表明请求正常
                    //获取班级学员列表信息
                    var traineeFoodGrid = win.down("grid[ref=traineeFoodGrid]");
                    /*
                    traineeFoodGrid.getStore().loadData ([
                        { name: '张三', traineeId: '111', breakfast: false,lunch: true,dinner: false },
                        { name: '李四', traineeId: '222', breakfast: false,lunch: true,dinner: false },
                        { name: '王五', traineeId: '333', breakfast: false,lunch: true,dinner: false },
                        { name: '赵六', traineeId: '444', breakfast: false,lunch: true,dinner: false }
                    ]);*/
                    traineeFoodGrid.getStore().loadData(rows);

                } else {
                    self.Error(data.obj);
                }
            }
        });

        if (cmd == "detail") {
            self.setFuncReadOnly(funData, objDetForm, true);
            //courseNameField.setReadOnly(true);
        }
    },

    doRoomDetail: function (btn, cmd, grid, record) {

        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到模型
        var store = baseGrid.getStore();
        var Model = store.model;
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "class_roomDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = {};

        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        var width = 600;
        var height = 500;

        var iconCls = 'x-fa fa-plus-circle';
        var operType = cmd;
        var title = "增加";

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

                iconCls = 'x-fa fa-pencil-square',
                    operType = "edit";
                title = "宿舍申请";
                break;
            case "detail":

                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;

                iconCls = 'x-fa fa-file-text',
                    operType = "detail";
                title = "宿舍申请详请";
                break;
        }

        var win = Ext.create('core.base.view.BaseFormWin', {
            title: title,
            iconCls: iconCls,
            operType: operType,
            width: width,
            height: height,
            controller: otherController,
            funData: popFunData,
            funCode: detCode,    //修改此funCode，方便用于捕获window的确定按钮
            insertObj: insertObj,
            items: [{
                xtype: detLayout,
                funCode: detCode,
                items: [{
                    xtype: "class.roomdetailform"
                }]
            }]
        });
        win.show();


        var detPanel = win.down("basepanel[funCode=" + detCode +
            "]");
        var objDetForm = detPanel.down("baseform[funCode=" +
            detCode + "]");
        var formDeptObj = objDetForm.getForm();


        //查询班级的学员信息
        self.asyncAjax({
            url: comm.get("baseUrl") + "/TrainClasstrainee/getClassRoomTrainees",
            params: {
                classId: insertObj.uuid,
                page: 1,
                start: 0,
                limit: -1    //-1表示不分页
            },
            //回调代码必须写在里面
            success: function (response) {
                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                var rows = data.rows;
                //console.log(rows);
                if (rows != undefined) {  //若存在rows数据，则表明请求正常
                    //获取班级学员列表信息
                    var traineeRoomGrid = win.down("grid[ref=traineeRoomGrid]");
                    /*                
                    //获取班级学员列表信息
                    var traineeRoomGrid=win.down("grid[ref=traineeRoomGrid]");
                    traineeRoomGrid.getStore().loadData ([
                        { name: '张三', traineeId: '111', xbm: "2",siesta: true,sleep: false },
                        { name: '李四', traineeId: '222', xbm: "1",siesta: true,sleep: false },
                        { name: '王五', traineeId: '333', xbm: "2",siesta: false,sleep: false },
                        { name: '赵六', traineeId: '444', xbm: "1",siesta: true,sleep: false }
                    ]);*/
                    traineeRoomGrid.getStore().loadData(rows);

                } else {
                    self.Error(data.obj);
                }
            }
        });

        self.setFormValue(formDeptObj, insertObj);

        if (cmd == "detail") {
            self.setFuncReadOnly(funData, objDetForm, true);
            //courseNameField.setReadOnly(true);
        }
    },

    doTranieeDetail_Tab: function (btn, cmd, grid, record) {

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
        var detCode = "class_traineeDetail";   //修改此funCode，方便用于捕获window的确定按钮
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

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "班级学员管理";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridTraineeDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
        var operType = cmd;

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
                tabTitle = "班级学员管理";
                tabItemId = funCode + "_gridTraineeEdit";

                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

                break;
            case "detail":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "班级学员管理";
                tabItemId = funCode + "_gridTraineeDetail" + insertObj.classNumb;
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {

            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
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
                        funCode: detCode,
                        layout: 'border',
                        items: [{
                            xtype: 'baseform',
                            height: 40,
                            region: 'north',
                            bodyPadding: 0,
                            bodyStyle: {
                                backgroundColor: '#Fafafa'
                            },
                            items: [{
                                fieldLabel: "所属班级id",
                                name: "uuid",
                                xtype: "textfield",
                                hidden: true,
                                value: insertObj.uuid
                            }, {
                                name: "className",
                                xtype: "textfield",
                                hidden: true,
                                value: insertObj.className
                            }, {
                                fieldLabel: "<span style='font-weight:400;font-size:14px'>所属班级</span>",
                                name: "classNameShow",
                                xtype: "displayfield",
                                width: 300,
                                labelWidth: 80,
                                margin: 5,
                                value: "<span style='font-weight:400;font-size:14px'>" + insertObj.className + "</span>"
                            }]
                        }, {
                            xtype: "class.classstudentgrid",
                            region: 'center',
                            extParams: {
                                whereSql: "",
                                //查询的过滤字段
                                //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
                                filter: "[{'type':'string','comparison':'=','value':'" + insertObj.uuid + "','field':'classId'}]"
                            }
                        }]
                    }]
                });
                tabItem.add(item);


            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);

    },

    doCourseDetail_Tab: function (btn, cmd, grid, record) {

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
        var detCode = "class_courseDetail";   //修改此funCode，方便用于捕获window的确定按钮
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

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "班级课程管理";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridCourseDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
        var operType = cmd;

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
                tabTitle = "班级课程管理";
                tabItemId = funCode + "_gridCourseEdit";

                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

                break;
            case "detail":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "班级课程管理";
                tabItemId = funCode + "_gridCourseDetail" + insertObj.classNumb;
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {

            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
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
                        funCode: detCode,
                        layout: 'border',
                        items: [{
                            xtype: 'baseform',
                            height: 40,
                            region: 'north',
                            bodyPadding: 0,
                            bodyStyle: {
                                backgroundColor: '#Fafafa'
                            },
                            items: [{
                                fieldLabel: "所属班级id",
                                name: "uuid",
                                xtype: "textfield",
                                hidden: true,
                                value: insertObj.uuid
                            }, {
                                name: "className",
                                xtype: "textfield",
                                hidden: true,
                                value: insertObj.className
                            }, {
                                fieldLabel: "<span style='font-weight:400;font-size:14px'>所属班级</span>",
                                name: "classNameShow",
                                xtype: "displayfield",
                                width: 300,
                                labelWidth: 80,
                                margin: 5,
                                value: "<span style='font-weight:400;font-size:14px'>" + insertObj.className + "</span>"
                            }]
                        }, {
                            xtype: "class.classcoursegrid",
                            region: 'center',
                            extParams: {
                                whereSql: "",
                                //查询的过滤字段
                                //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
                                filter: "[{'type':'string','comparison':'=','value':'" + insertObj.uuid + "','field':'classId'}]"
                            }
                        }]
                    }]
                });
                tabItem.add(item);


            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);

    },

    doClassDetail_Tab: function (btn, cmd, grid, record) {

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
        var detCode = 'class_classdetail'; // basePanel.detCode;
        var detLayout = 'class.classdetaillayout';// basePanel.detLayout;\
        var defaultObj = funData.defaultObj;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //根据cmd操作类型，来设置不同的值
        var tabTitle = funData.tabConfig.addTitle;
        //设置tab页的itemId
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
        var operType = "detail";    // 只显示关闭按钮
        var items = [{
            xtype: detLayout
        }];
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
                // if(recordData.isuse==1){
                //     self.msgbox("班级已经启用，无法再修改！");
                //     return;
                // }
                insertObj = recordData;
                tabTitle = funData.tabConfig.editTitle;
                tabItemId = funCode + "_gridEdit";

                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

                break;
            case "detail":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = funData.tabConfig.detailTitle;
                tabItemId = funCode + "_senduser" + insertObj.classNumb;    //详细界面可以打开多个
                items = [{
                    xtype: detLayout,
                    defaults: null,
                    items: [{
                        xtype: 'class.classdetailhtmlpanel'
                    }]
                }];
                break;
        }


        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {

            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,      //保存主键值
                layout: 'fit'
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
                    items: items
                });
                tabItem.add(item);

                if (cmd != "detail") {
                    var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                    var formDeptObj = objDetForm.getForm();

                    self.setFormValue(formDeptObj, insertObj);
                }

                if (cmd == "edit" || cmd == "detail") {
                    if (cmd == "edit") {
                        //初始化一些数据
                        var classCourseGrid = tabItem.down("basegrid[xtype=class.classcoursegrid]");
                        classCourseGrid.getStore().getProxy().extraParams.filter = '[{"type":"string","comparison":"=","value":"' + insertObj.uuid + '","field":"classId"}]';

                        var classStudentGrid = tabItem.down("basegrid[xtype=class.classstudentgrid]");
                        classStudentGrid.getStore().getProxy().extraParams.filter = '[{"type":"string","comparison":"=","value":"' + insertObj.uuid + '","field":"classId"}]';


                        var foodDetailForm = tabItem.down("baseform[xtype=class.fooddetailform]");
                        self.setFormValue(foodDetailForm.getForm(), insertObj);

                        var roomDetailForm = tabItem.down("baseform[xtype=class.roomdetailform]");
                        roomDetailForm.getForm().findField("uuid").setValue(insertObj.uuid);

                        //查询班级的就餐信息
                        self.asyncAjax({
                            url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
                            params: {
                                classId: insertObj.uuid,
                                page: 1,
                                start: 0,
                                limit: -1    //-1表示不分页
                            },
                            //回调代码必须写在里面
                            success: function (response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                var rows = data.rows;
                                //console.log(rows);
                                if (rows != undefined) { //若存在rows数据，则表明请求正常
                                    //获取班级学员列表信息
                                    var traineeFoodGrid = tabItem.down("grid[ref=traineeFoodGrid]");
                                    /*
                                    traineeFoodGrid.getStore().loadData ([
                                        { name: '张三', traineeId: '111', breakfast: false,lunch: true,dinner: false },
                                        { name: '李四', traineeId: '222', breakfast: false,lunch: true,dinner: false },
                                        { name: '王五', traineeId: '333', breakfast: false,lunch: true,dinner: false },
                                        { name: '赵六', traineeId: '444', breakfast: false,lunch: true,dinner: false }
                                    ]);*/
                                    traineeFoodGrid.getStore().loadData(rows);

                                } else {
                                    self.Error(data.obj);
                                }
                            }
                        });

                        //查询班级的住宿信息
                        self.asyncAjax({
                            url: comm.get("baseUrl") + "/TrainClasstrainee/getClassRoomTrainees",
                            params: {
                                classId: insertObj.uuid,
                                page: 1,
                                start: 0,
                                limit: -1    //-1表示不分页
                            },
                            //回调代码必须写在里面
                            success: function (response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                var rows = data.rows;
                                //console.log(rows);
                                if (rows != undefined) {  //若存在rows数据，则表明请求正常
                                    //获取班级学员列表信息
                                    var traineeRoomGrid = tabItem.down("grid[ref=traineeRoomGrid]");
                                    /*                
                                    //获取班级学员列表信息
                                    var traineeRoomGrid=win.down("grid[ref=traineeRoomGrid]");
                                    traineeRoomGrid.getStore().loadData ([
                                        { name: '张三', traineeId: '111', xbm: "2",siesta: true,sleep: false },
                                        { name: '李四', traineeId: '222', xbm: "1",siesta: true,sleep: false },
                                        { name: '王五', traineeId: '333', xbm: "2",siesta: false,sleep: false },
                                        { name: '赵六', traineeId: '444', xbm: "1",siesta: true,sleep: false }
                                    ]);*/
                                    traineeRoomGrid.getStore().loadData(rows);

                                } else {
                                    self.Error(data.obj);
                                }
                            }
                        });

                    } else {
                        //读取班级信息
                        //读取学员信息
                        //读取课程信息

                        self.asyncAjax({
                            url: comm.get("baseUrl") + "/TrainClass/getClassAllInfo",
                            params: {
                                classId: insertObj.uuid
                            },
                            //回调代码必须写在里面
                            success: function (response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                if (data.success) {
                                    var obj = data.obj;
                                    var classInfoContainer = tabItem.down("container[ref=classInfo]");
                                    classInfoContainer.setData(obj.classInfo);

                                    var classTraineeInfoContainer = tabItem.down("container[ref=classTraineeInfo]");
                                    classTraineeInfoContainer.setData(obj.classTrainee);

                                    var classCourseInfoContainer = tabItem.down("container[ref=classCourseInfo]");
                                    classCourseInfoContainer.setData(obj.classCourse);

                                    var classFoodInfoContainer = tabItem.down("container[ref=classFoodInfo]");
                                    var classFoodObj = {};
                                    classFoodObj.dinnerType = obj.classInfo.dinnerType;
                                    if (obj.classInfo.dinnerType == 1)
                                        classFoodObj.dinnerTypeName = "围餐";
                                    else if (obj.classInfo.dinnerType == 2)
                                        classFoodObj.dinnerTypeName = "自助餐";
                                    else
                                        classFoodObj.dinnerTypeName = "快餐";
                                    classFoodObj.avgNumber = obj.classInfo.avgNumber;
                                    classFoodObj.breakfastStand = obj.classInfo.breakfastStand;
                                    classFoodObj.breakfastCount = obj.classInfo.breakfastCount;
                                    classFoodObj.lunchStand = obj.classInfo.lunchStand;
                                    classFoodObj.lunchCount = obj.classInfo.lunchCount;
                                    classFoodObj.dinnerStand = obj.classInfo.dinnerStand;
                                    classFoodObj.dinnerCount = obj.classInfo.dinnerCount;
                                    //默认为快餐，则显示学员数据   
                                    if (obj.classInfo.dinnerType != "1" && obj.classInfo.dinnerType != "2") {

                                        //查询班级的就餐信息
                                        self.asyncAjax({
                                            url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
                                            params: {
                                                classId: insertObj.uuid,
                                                page: 1,
                                                start: 0,
                                                limit: -1    //-1表示不分页
                                            },
                                            //回调代码必须写在里面
                                            success: function (response) {
                                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                                var rows = data.rows;
                                                if (rows != undefined) { //若存在rows数据，则表明请求正常
                                                    classFoodObj.rows = rows;
                                                    classFoodInfoContainer.setData(classFoodObj);
                                                } else {
                                                    self.Error(data.obj);
                                                }
                                            }
                                        });
                                    } else {
                                        classFoodInfoContainer.setData(classFoodObj);
                                    }


                                    //查询班级的住宿信息
                                    self.asyncAjax({
                                        url: comm.get("baseUrl") + "/TrainClasstrainee/getClassRoomTrainees",
                                        params: {
                                            classId: insertObj.uuid,
                                            page: 1,
                                            start: 0,
                                            limit: -1    //-1表示不分页
                                        },
                                        //回调代码必须写在里面
                                        success: function (response) {
                                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                            var rows = data.rows;
                                            //console.log(rows);
                                            if (rows != undefined) {  //若存在rows数据，则表明请求正常
                                                var classRoomInfoContainer = tabItem.down("container[ref=classRoomInfo]");
                                                classRoomInfoContainer.setData(rows);
                                            } else {
                                                self.Error(data.obj);
                                            }
                                        }
                                    });
                                } else {
                                    self.Error(data.obj);
                                }

                            }
                        });
                    }
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);


        /*隐藏操作列*/
        /*
        var actioncolumn=coursegrid.query("actioncolumn");
        for(var i in actioncolumn){
            actioncolumn[i].setVisible(false);
        } */

        /*
                coursegrid.extParams.filter ="[{'type':'string','comparison':'=','value':'" + insertObj.uuid + "','field':'classId'}]";
        */
    },

    doRoomDetail_Tab: function (btn, cmd, grid, record) {

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
        var detCode = "class_roomDetail";   //修改此funCode，方便用于捕获window的确定按钮
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

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "住宿申请";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridRoomDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
        var operType = cmd;

        switch (cmd) {
            case "edit":

                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "编辑住宿信息";
                tabItemId = funCode + "_gridRoomEdit";

                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

                break;
            case "detail":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "查看住宿信息";
                tabItemId = funCode + "_gridRoomDetail" + insertObj.classNumb;
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {

            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
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
                        funCode: detCode,
                        items: [{
                            xtype: "class.roomdetailform"
                        }]
                    }]
                });
                tabItem.add(item);

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("className").setVisible(true);
                formDeptObj.findField("classNumb").setVisible(true);
                formDeptObj.findField("beginDate").setVisible(true);
                formDeptObj.findField("endDate").setVisible(true);

                self.setFormValue(formDeptObj, insertObj);


                //查询班级的住宿信息
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClasstrainee/getClassRoomTrainees",
                    params: {
                        classId: insertObj.uuid,
                        page: 1,
                        start: 0,
                        limit: -1    //-1表示不分页
                    },
                    //回调代码必须写在里面
                    success: function (response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        var rows = data.rows;
                        //console.log(rows);
                        if (rows != undefined) {  //若存在rows数据，则表明请求正常
                            //获取班级学员列表信息
                            var traineeRoomGrid = tabItem.down("grid[ref=traineeRoomGrid]");
                            /*                
                            //获取班级学员列表信息
                            var traineeRoomGrid=win.down("grid[ref=traineeRoomGrid]");
                            traineeRoomGrid.getStore().loadData ([
                                { name: '张三', traineeId: '111', xbm: "2",siesta: true,sleep: false },
                                { name: '李四', traineeId: '222', xbm: "1",siesta: true,sleep: false },
                                { name: '王五', traineeId: '333', xbm: "2",siesta: false,sleep: false },
                                { name: '赵六', traineeId: '444', xbm: "1",siesta: true,sleep: false }
                            ]);*/
                            traineeRoomGrid.getStore().loadData(rows);

                        } else {
                            self.Error(data.obj);
                        }
                    }
                });


            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    },

    doFoodDetail_Tab: function (btn, cmd, grid, record) {

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
        var detCode = "class_foodDetail";   //修改此funCode，方便用于捕获window的确定按钮
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

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "就餐申请";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridFoodDetail";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
        var operType = cmd;

        switch (cmd) {
            case "edit":

                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "编辑就餐信息";
                tabItemId = funCode + "_gridFoodEdit";

                //获取主键值
                var pkName = funData.pkName;
                pkValue = recordData[pkName];

                break;
            case "detail":
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "查看就餐信息";
                tabItemId = funCode + "_gridFoodDetail" + insertObj.classNumb;
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {

            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
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
                        funCode: detCode,
                        items: [{
                            xtype: "class.fooddetailform"
                        }]
                    }]
                });
                tabItem.add(item);

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("className").setVisible(true);
                formDeptObj.findField("beginDate").setVisible(true);
                formDeptObj.findField("endDate").setVisible(true);

                self.setFormValue(formDeptObj, insertObj);
                if (cmd == "detail") {
                    self.setFuncReadOnly(funData, objDetForm, true);
                }

                //查询班级的住宿信息
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
                    params: {
                        classId: insertObj.uuid,
                        page: 1,
                        start: 0,
                        limit: -1    //-1表示不分页
                    },
                    //回调代码必须写在里面
                    success: function (response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        var rows = data.rows;
                        //console.log(rows);
                        if (rows != undefined) {  //若存在rows数据，则表明请求正常
                            //获取班级学员就餐信息
                            var traineeRoomGrid = tabItem.down("grid[ref=traineeFoodGrid]");
                            traineeRoomGrid.getStore().loadData(rows);

                        } else {
                            self.Error(data.obj);
                        }
                    }
                });

            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);

    },

    doSendUserDetail_Tab: function (btn, cmd, grid, record) {
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
        var detCode = "class_sendUserDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "短信通知设置";
        //设置tab页的itemId
        var tabItemId = funCode + "_gridSendUser";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue = null;
        var items = [{
            xtype: detLayout
        }];
        if (btn) {
            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1) {
                self.msgbox("请选择1条数据！");
                return;
            }
            recordData = rescords[0].data;
        }

        insertObj = recordData;
        insertObj = Ext.apply(insertObj,{
            sendInfo:"请安排培训班:[" + recordData.className + "]的场地及住宿"
        });
        tabTitle = funData.tabConfig.detailTitle;
        tabItemId = funCode + "_gridDetail" + insertObj.classNumb;    //详细界面可以打开多个
        operType = "edit";
        items = [{
            xtype: detLayout,
            defaults: null,
            items: [{
                xtype: 'class.sendinfoform'
            }]
        }];

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,      //保存主键值
                layout: 'fit'
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,         //指定重写事件的控制器
                    funCode: detCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: items
                });
                tabItem.add(item);
                var objDetForm = item.down("baseform[xtype=class.sendinfoform]");
                var formDeptObj = objDetForm.getForm();

                self.setFormValue(formDeptObj, insertObj);
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }

});

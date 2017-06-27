/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
 但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
 */
Ext.define("core.train.course.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.course.otherController',
    mixins: {
        formUtil: "core.util.FormUtil",
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil',
        sqlUtil: 'core.util.SqlUtil'
    },
    init: function () {
    },
    /** 该视图内的组件事件注册 */
    control: {
        "panel[xtype=course.courseimportform] button[ref=formSave]": {
            beforeclick: function (btn) {
                var self = this;
                var dicForm = btn.up('panel[xtype=course.courseimportform]');
                var objForm = dicForm.getForm();
                if (objForm.isValid()) {
                    objForm.submit({
                        url: comm.get('baseUrl') + "/TrainCourseinfo/importData",
                        waitMsg: '正在导入文件...',
                        success: function (form, action) {
                            self.msgbox("导入成功！");

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure: function (form, action) {
                            if (action.result == undefined) {
                                self.Error("文件导入失败，文件有误或超过限制大小！");
                            } else {
                                self.Error(action.result.obj);
                            }

                        }
                    });
                } else {
                    self.Error("请选择要上传Excel文件！")
                }

                return false
            }
        },
        "panel[xtype=course.courseimportform] button[ref=formClose]": {
            click: function (btn) {
                var win = btn.up('window');
                //关闭窗体
                win.close();
                return false;
            }
        },

        /**
         * 打开主讲教师选择窗口
         * 打开时自动加载已选择的教师
         */
        "window[funcPanel=course.selectteacher.mainlayout]": {
            afterrender: function (win) {
                var tabPanel = Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem = tabPanel.getActiveTab();
                var formpanel = tabItem.down('form[xtype=' + win.formPanel + ']');

                // var formpanel = Ext.ComponentQuery.query('form[xtype=' + win.formPanel + ']')[0];
                var courseId = formpanel.getForm().findField("uuid").getValue();
                var grid = win.down("grid[ref=isselectedteachergrid]");
                var store = grid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    courseId: courseId
                };
                store.load();
                return false;
            }
        },
        /**
         * 主讲教师选择保存按钮事件
         */
        "window[funcPanel=course.selectteacher.mainlayout] button[ref=ssOkBtn]": {
            beforeclick: function (btn) {
                var win = btn.up("window[funcPanel=course.selectteacher.mainlayout]");
                var tabPanel = Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem = tabPanel.getActiveTab();
                var formpanel = tabItem.down('form[xtype=' + win.formPanel + ']');

                var grid = win.down("grid[ref=isselectedteachergrid]");
                //var formpanel = Ext.ComponentQuery.query('form[xtype=' + win.formPanel + ']')[0];
                var idArray = new Array();
                var nameArray = new Array();

                var store = grid.getStore();
                for (var i = 0; i < store.getCount(); i++) {
                    if (idArray.indexOf(store.getAt(i).get("uuid")) == -1 || store.getAt(i).get("uuid") == "null") {
                        nameArray.push(store.getAt(i).get("xm"));
                        idArray.push(store.getAt(i).get("uuid") ? store.getAt(i).get("uuid") : " ");  //为空的数据，要使用一个空格号隔开，否则后台split分割有误
                    }
                }

                formpanel.getForm().findField("mainTeacherId").setValue(idArray.join(","));
                ;
                formpanel.getForm().findField("mainTeacherName").setValue(nameArray.join(","));
                win.close();
                return false;
            }
        },
        "menuitem[ref=addCateory]": {
            click: function (item) {
                var self = this;
                //var nodeData = item.parentMenu.contextNode.data;
                var node = item.parentMenu.contextNode;
                self.doCourseCateory(node, "add");
                return false;
            },
        },

        /**
         * 因htmleditor的需要重写输入界面的保存事件
         */
        "baseformtab button[ref=formSave]": {
            beforeclick: function (btn) {
                var self = this;
                var basetab = btn.up('baseformtab');
                var tabPanel = btn.up("tabpanel[xtype=app-main]");
                var tabItemId = basetab.tabItemId;
                var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页


                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var detPanel = basetab.down("basepanel[funCode=" + detCode + "]");
                var objForm = detPanel.down("baseform[funCode=" + detCode + "]");
                var objCourseDesc = objForm.down("htmleditor");
                var courseDesc = objCourseDesc.getValue();
                var formObj = objForm.getForm();
                var funData = detPanel.funData;
                var pkName = funData.pkName;
                var pkField = formObj.findField(pkName);
                var params = self.getFormValue(formObj);
                var courseMode = params.courseMode == true ? 2 : 1;
                params = Ext.apply(params, {
                        courseDesc: courseDesc,
                        courseMode: courseMode
                    }
                );
                if (courseDesc.length > 8000) {
                    self.Warning("课程简介文字太长，请调整");
                    return;
                }

                //判断当前是保存还是修改操作
                var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
                if (formObj.isValid()) {
                    var loading = new Ext.LoadMask(basetab, {
                        msg: '正在提交，请稍等...',
                        removeMask: true// 完成后移除
                    });
                    loading.show();

                    self.asyncAjax({
                        url: funData.action + "/" + act,
                        params: params,
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                self.msgbox("保存成功!");
                                var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                if (!Ext.isEmpty(grid)) {
                                    var store = grid.getStore();
                                    store.loadPage(1); //刷新父窗体的grid
                                }

                                loading.hide();
                                tabPanel.remove(tabItem);
                            } else {
                                self.Error(data.obj);
                                loading.hide();
                            }
                        }
                    });

                } else {
                    var errors = ["前台验证失败，错误信息："];
                    formObj.getFields().each(function (f) {
                        if (!f.isValid()) {
                            errors.push("<font color=red>" + f.fieldLabel + "</font>：" + f.getErrors().join(","));
                        }
                    });
                    self.msgbox(errors.join("<br/>"));
                }

                if (btn.callback) {
                    btn.callback();
                }
                return false;
            }
        },
    },
    doCourseCateory: function (node, cmd) {
        var self = this;
        var nodeId = node.data.id;
        var nodeText = node.data.text;
        var tree = node.getOwnerTree();
        var funCode = tree.funCode;
        var basePanel = tree.up("panel[funCode=" + funCode + "]");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var width = funData.width;
        var height = funData.height;
        if (!width || !height) {
            width = 600;
            height = 450;
        }
        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: tree,
            whereSql: " and isDelete='0' "
        });
        //根据选择的记录与操作确定form初始化的数据
        var iconCls = "x-fa fa-plus-circle";
        var title = "增加分类";
        var operType = cmd;

        switch (cmd) {
            case "add":
                operType = "add";
                insertObj = Ext.apply(insertObj, {
                    parentCategory: nodeId,
                    parentName: nodeText,
                    uuid: ''
                });
                break;
            case "edit":
                iconCls = "x-fa fa-pencil-square";
                operType = "edit";
                title = "修改分类";

                // insertObj = records[0].data;
                // insertObj = Ext.apply(insertObj, {
                //   parentCategory: parent,
                //   parentName: parentName,
                //   uuid: just,
                //   nodeText: justName
                // });
                break;
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                title: title,
                width: width,
                height: height,
                resizable: false,
                iconCls: iconCls,
                operType: operType,
                funData: popFunData,
                funCode: detCode,
                //给form赋初始值
                insertObj: insertObj,
                items: [{
                    xtype: "course.detaillayout",
                    funData: {
                        action: comm.get("baseUrl") + "/TrainCoursecategory", //请求Action
                        whereSql: "", //表格查询条件
                        orderSql: "", //表格排序条件
                        pkName: "uuid",
                        defaultObj: {}
                    },
                    items: [{
                        xtype: "train.coursecategoryform"
                    }]
                }]
            });
        }
        win.show();
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
        var formDeptObj = objDetForm.getForm();
        //表单赋值

        self.setFormValue(formDeptObj, insertObj);
    }
});

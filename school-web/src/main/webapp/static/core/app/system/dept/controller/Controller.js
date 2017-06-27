Ext.define("core.system.dept.controller.Controller", {
    extend: "Ext.app.ViewController",
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        treeUtil: "core.util.TreeUtil",
        gridActionUtil: "core.util.GridActionUtil"
    },
    
    
    alias: 'controller.dept.deptController',
/*    views: [
        "core.system.dept.view.MainLayout",
        "core.system.dept.view.DeptLayout",
        "core.system.dept.view.DeptGrid",
        "core.system.dept.view.DeptForm"
    ],
    stores: [
        "core.system.dept.store.DeptStore",
        "core.system.dept.store.CourseStore"
    ],*/
    init: function() {
        var self = this;

        this.control({
            //增加下级按钮事件
            "basetreegrid button[ref=gridAdd_Tab]": {
                beforeclick: function(btn) {
                    console.log(btn);
                    //return false;
                }
            },
            //增加同级按钮事件
            "basetreegrid button[ref=gridAdd_Tab]": {
                beforeclick: function(btn) {
                    console.log(btn);
                    //return false;
                }
            },

            //修改按钮事件
            "basetreegrid button[ref=gridEdit_Tab]": {
                beforeclick: function(btn) {
                    console.log(btn);
                    //return false;
                }
            },
            //删除按钮事件
            "panel[xtype=dept.deptgrid] button[ref=gridDelete]": {
                beforeclick: function(btn) {
                    var baseGrid = btn.up("basetreegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                    //var records = baseGrid.getSelectionModel().getSelection();
                    records = baseGrid.getView().getChecked();
                    if (records.length == 0) {
                        self.Error("请选择要删除的部门");
                        return;
                    }
                    var ids = new Array();
                    Ext.each(records, function(rec) {
                        var pkValue = rec.get("id");
                        var deptType = rec.get("deptType");
                        var child = rec.childNodes.length;
                        if (child == 0 && deptType != "01" && deptType != "02") {
                            //仅能删除无子部门而且类型不为学校的部门
                            ids.push(pkValue);
                        }
                    });
                    var title = "确定要删除所选的部门吗？";
                    if (ids.length == 0) {
                        self.Warning("所选部门都有子部门或是类别为学校，不能删除");
                        return;
                    }
                    if (ids.length < records.length) {
                        title = "有些部门有子部门，仅删除不含子部门的部门。确定吗？";
                    }
                    Ext.Msg.confirm('警告', title, function(btn, text) {
                        if (btn == 'yes') {
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
                                self.msgbox(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                    //执行回调函数
                    if (btn.callback) {
                        btn.callback();
                    }
                    return false;
                }
            },

            //刷新按钮事件
            "panel[xtype=dept.deptgrid] button[ref=gridRefresh]": {
                click: function(btn) {
                    var baseGrid = btn.up("basetreegrid");
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    var funData = basePanel.funData;
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        whereSql: funData.whereSql,
                        orderSql: funData.orderSql
                    };
                    store.load(); //刷新父窗体的grid

                    return false;
                }
            },
            
            "panel[xtype=dept.deptgrid] button[ref=sync]": {
                beforeclick: function(btn) {
                	 var resObj = self.ajax({
                         url: "/usersync" + "/dept"
                     });
                     if (resObj.success) {
                    	 self.msgbox("同步成功!");
                    	 btn.up("basegrid").getStore().load();
                     }else{
                    	 self.msgbox(resObj.obj);
                     }
                     return false;
                }
            },
            
           
            
            "panel[xtype=dept.deptgrid]": {
                checkchange: function(node, checked, options) {
                    // if (node.data.leaf == false) {
                    //     if (checked) {
                    //         //打开节点
                    //         node.expand();
                    //         //遍历孩子
                    //         node.eachChild(function(n) {
                    //             n.set('checked', checked);
                    //             //n.data.checked = true;
                    //             // n.updateInfo({
                    //             //  checked: true
                    //             // });
                    //         });
                    //     } else {
                    //         node.expand();
                    //         node.eachChild(function(n) {
                    //             n.set('checked', checked);
                    //             // n.updateInfo({
                    //             //  checked: false
                    //             // });
                    //         });
                    //     }
                    // } else { //单击叶子时候
                    //     if (checked) { //未被选中时，取消父节点的选择状态
                    //         //node.parentNode.set('checked', checked);
                    //         //node.parentNode.data.checked = true;
                    //         // node.parentNode.updateInfo({
                    //         //  checked: true
                    //         // });
                    //     }
                    // }

                    // node.expand(true);
                    node.expand();  //只展开第一层
                    //递归选中父节点
                    var eachParent = function(node, checked) {
                        if (!node.isRoot() && checked == true) {
                            if (!Ext.isEmpty(node.get('checked'))) {
                                node.set('checked', checked);
                                node.commit();
                            }
                            eachParent(node.parentNode, checked);
                        }
                    }
                    eachParent(node.parentNode, checked);
                    //递归选中孩子节点
                    var eachChild = function(node, checked) {
                        node.eachChild(function(n) {
                            if (!Ext.isEmpty(n.get('checked'))) {
                                n.set('checked', checked);
                                n.commit();
                            }
                            eachChild(n, checked);
                        });
                    };
                    eachChild(node, checked);                    
                    return false;
                }
            },

            /**
             * 通用弹出窗体保存事件
             * 采用ajax的模式提交数据，并返回提交的数据
             * 因本模块功能要求，对公用的弹出窗体事件进行拦截处理
             */
            "baseformtab[funCode=deptinfo_detail] button[ref=formSave]": {
                beforeclick: function(btn) {
                    self.doSave(btn, "addSave");
                    return false;
                }
            },
            /**
             * 通用弹出窗体保存事件 保存继续
             * 采用ajax的模式提交数据，并返回提交的数据
             * 因本模块功能要求，对公用的弹出窗体事件进行拦截处理
             */
            "baseformtab[funCode=deptinfo_detail] button[ref=formContinue]": {
                beforeclick: function(btn) {
                    self.doSave(btn, "addcSave");
                    return false;
                }
            }
        });
    },
/*    doDetail: function(btn, cmd) {
        //debugger;
        var self = this;
        var baseGrid = btn.up("basetreegrid");
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //先确定要选择记录
        var records = baseGrid.getSelectionModel().getSelection();
        if (records.length != 1) {
            self.Error("请先选择部门");
            return;
        }
        //当前节点
        var just = records[0].get("id");
        var justName = records[0].get("text");
        var justType = records[0].get("deptType");

        //当前节点的上级节点
        var parent = records[0].get("parent");
        var store = baseGrid.getStore();
        var parentNode = store.getNodeById(parent);
        var parentName = "ROOT";
        var parentType = "01";
        if (parentNode) {
            parentName = parentNode.get("text");
            parentType = parentNode.get("deptType");
        }
        
        
        var otherController=basePanel.otherController;  
        if(!otherController)
            otherController='';
        
        //根据选择的记录与操作确定form初始化的数据
        var iconCls = "x-fa fa-plus-circle";
        var title = "增加下级部门";
        var operType = cmd;
        switch (cmd) {
            case "child":
                operType = "add";
                if (justType == "01" || justType == "05" || justType == "06") {
                    self.Error("部门类型为学校、学科或班级时不能增加下级部门");
                    return;
                }
                insertObj = Ext.apply(insertObj, {
                    parentNode: just,
                    parentName: justName,
                    uuid: null,
                    deptType: '03',
                    parentType: justType
                });
                break;
            case "brother":
                if (justType == "01" || justType == "02") {
                    self.Error("部门类型为学校或校区时不能增加同级部门");
                    return;
                }
                title = "增加同级部门";
                operType = "add";
                insertObj = Ext.apply(insertObj, {
                    parentNode: parent,
                    parentName: parentName,
                    uuid: null,
                    deptType: justType,
                    parentType: parentType
                });
                break;
            case "edit":
                if (justType == "01" || justType == "02") {
                    self.Error("部门类型为学校或校区，此处不能修改");
                    return;
                }else if(justType == '04'){ //年级部门
                    var resObj = self.ajax({    //获取年级信息
                        url: comm.get('baseUrl')  + "/gradeinfo/getGradeInfo",
                        params: {
                            id:just
                        }
                    });
                    if (resObj.success) {
                        //采用返回的数据刷新表单
                       
                        var obj = resObj.obj;
                        insertObj.nj=obj.nj;
                        insertObj.sectionCode=obj.sectionCode;
                                            
                    } 
                }
            


                iconCls = "x-fa fa-pencil-square";
                operType = "edit";
                title = "修改部门";
                insertObj = Ext.apply(insertObj, {
                    parentNode: parent,
                    parentName: parentName,
                    uuid: just,
                    nodeText: justName,
                    parentType: parentType
                }, records[0].data);
                break;
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                controller:otherController,
                title: title,
                width: 650,
                height: 400,
                resizable: false,
                iconCls: iconCls,
                operType: operType,
                cmd: cmd,
                funData: popFunData,
                funCode: detCode,
                items: [{
                    xtype: detLayout
                }]
            });
        }
        win.show();
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
        var formDeptObj = objDetForm.getForm();
        //表单赋值
        self.setFormValue(formDeptObj, insertObj);
    },*/

    //保存处理的操作
    doSave: function(btn, cmd) {
        var self = this;
        var win = btn.up('window');
        var funCode = win.funCode;
        var operType = win.cmd;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();
        var funData = basePanel.funData;
        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);
        var parent = formObj.findField("parentNode").getValue();
        var parentName = formObj.findField("parentName").getValue();
        var deptType = formObj.findField("deptType").getValue();
        var orderIndex = formObj.findField("orderIndex").getValue() + 1;
        var parentType = formObj.findField("parentType").getValue();
        //根据上级部门的类型业控制本部门的类型
        var info = "";
/*        switch (deptType) {
            case "01":
                info = "部门类型不能为学校";
                break;
            case "02":
                info = "部门类型不能为校区"
                break;
            case "03":
                if (parentType != "03" && parentType != "02") {
                    info = "上级部门类型只能是部门或校区";
                }
                break;
            case "04":
                if (parentType != "03") {
                    info = "年级的上级部门类型只能是部门";
                }
                break;
            case "05":
                if (parentType != "04") {
                    info = "班级的上级部门类型只能是年级";
                }
                break;
            case "06":
                if (parentType != "03") {
                    info = "学科的上级部门的类型只能是部门";
                }
                break;
        }*/
        if (info.length>0){
            self.Warning(info);
            return;
        }
        // if (deptType == "06" || deptType == "04") {
        //     //当前部门类型为学科组或年级组，上级部门只能是职能部门
        //     if (parentType != "03") {
        //         self.Warning("只能在类型为部门的部门下添加学科或年级");
        //         return;
        //     }
        // }
        // if (deptType == "03") {
        //     //当前部门类型为部门，上级部门只能是部门或校区
        //     if (parentType != "03" && parentType != "02") {
        //         self.Warning("当前部门类型为部门时，上级部门类型只能是部门或校区");
        //         return;
        //     }
        // }        
        // if (parentType == "03") {
        //     if (deptType != "04") {
        //         self.Warning("上级部门类型为年级时,只能添加班级");
        //         return;
        //     }
        // } else {
        //     if (deptType == "04") {
        //         self.Warning("上级部门类型不为年级时,不能直接添加班级");
        //         return;
        //     }
        // }
        //判断当前是保存还是修改操作
        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
        if (formObj.isValid()) {
            var resObj = self.ajax({
                url: funData.action + "/" + act,
                params: params
            });
            if (resObj.success) {
                
                //采用返回的数据刷新表单
                if (cmd == "addcSave") {
                    insertObj = Ext.apply(insertObj, {
                        parentNode: parent,
                        parentName: parentName,
                        deptType: deptType,
                        orderIndex: orderIndex,
                        parentType: parentType
                    });
                } else {

                    insertObj = Ext.apply(params, resObj.obj);

                   // insertObj = resObj.obj;

                }
                self.setFormValue(formObj, insertObj);
                self.msgbox("保存成功!");
                var grid = win.funData.grid; //窗体是否有grid参数
                if (!Ext.isEmpty(grid)) {
                    var store = grid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        whereSql: win.funData.whereSql,
                        orderSql: win.funData.orderSql
                    };
                    store.load(); //刷新父窗体的grid
                }
            } else {
                if (!Ext.isEmpty(resObj.obj))
                    self.Info(resObj.obj);
            }
        } else {
            var errors = ["前台验证失败，错误信息："];
            formObj.getFields().each(function(f) {
                if (!f.isValid()) {
                    errors.push("<font color=red>" + f.fieldLabel + "</font>:" + f.getErrors().join(","));
                }
            });
            self.msgbox(errors.join("<br/>"));
        }
    }
});
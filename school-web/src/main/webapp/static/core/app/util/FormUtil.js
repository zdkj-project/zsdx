Ext.define("core.util.FormUtil", {
    /**
     * 设置表单的值
     * @param {} formObj
     * @param {} obj
     */
    setFormValue: function (formObj, obj) {
        var fields = formObj.getFields().items;
        Ext.each(fields, function (field) {
            var objValue = obj[field.name];
            if (objValue != undefined) {
                var value = Ext.valueFrom(objValue, null);
                //如果是日期，则转换一下
                var fieldXtype = field.xtype;
                if (fieldXtype == "datefield" || fieldXtype == "datetimefield") {
                    //一些浏览器不能对 2017-1-1格式的日期进行转换，只能是2017/1/1
                    var date = objValue.replace(new RegExp(/-/gm), "/");
                    value = new Date(date);
                    formObj.findField(field.name).setValue(value);
                } else if (fieldXtype == "htmleditor") {
                    formObj.findField(field.name).setHtml(value);
                } else {
                    formObj.findField(field.name).setValue(value);
                }
            }
        });
    },
    /**
     * 获取表单数据
     * @param {} forObj
     * @return {}
     */
    getFormValue: function (formObj) {
        var fields = formObj.getFields().items;
        var obj = {};
        Ext.each(fields, function (field) {
            var value = Ext.valueFrom(field.getValue(), null);
            obj[field.name] = value;
        });
        return obj;
    },
    /**
     * 设置功能表单只读
     * @param {} funData
     * @param {} mainForm
     */
    setFuncReadOnly: function (funData, mainForm, flag) {
        mainForm.getForm().setItemsReadOnly(flag);
        if (funData.children) {
            Ext.each(funData.children, function (child) {
                if (child.funCode) {
                    //拿到子功能的布局对象
                    var childPanel = mainForm.down("basepanel[funCode=" + child.funCode + "]");
                    //加载子功能数据
                    var childGrid = childPanel.down("basegrid[funCode=" + child.funCode + "]");
                    //如果是子功能的话，表格是不会执行render事件的,只有激活了tab这个面板才会执行render事件
                    if (!childGrid) {
                        childGrid = childPanel.down("basegrid");
                    }
                    var childForm = childPanel.down("baseform[funCode=" + child.funCode + "]");
                    //如果是子功能的话，表格是不会执行render事件的,只有激活了tab这个面板才会执行render事件
                    if (!childGrid) {
                        childGrid = childPanel.down("basegrid");
                    }
                    if (!childForm) {
                        childForm = childPanel.down("baseform");
                    }
                    var insertBtn = childGrid.down("button[ref=gridInsertF]");
                    if (insertBtn) {
                        if (flag) {
                            insertBtn.hide();
                        } else if (!insertBtn.initialConfig.hidden) {
                            insertBtn.show();
                        }
                    }
                    var insertFBtn = childGrid.down("button[ref=gridInsert]");
                    if (insertFBtn) {
                        if (flag) {
                            insertFBtn.hide();
                        } else if (!insertFBtn.initialConfig.hidden) {
                            insertFBtn.show();
                        }
                    }
                    var gridDelete = childGrid.down("button[ref=gridDelete]");
                    if (gridDelete) {
                        if (flag) {
                            gridDelete.hide();
                        } else if (!gridDelete.initialConfig.hidden) {
                            gridDelete.show();
                        }
                    }
                    var gridSave = childGrid.down("button[ref=gridSave]");
                    if (gridSave) {
                        if (flag) {
                            gridSave.hide();
                        } else if (!gridSave.initialConfig.hidden) {
                            gridSave.show();
                        }
                    }
                    var formSave = childForm.down("button[ref=formSave]");
                    if (formSave) {
                        if (flag) {
                            formSave.hide();
                        } else if (!formSave.initialConfig.hidden) {
                            formSave.show();
                        }
                    }
                }
            });
        }
    },

    // approveComplete: function(taskId, variables) {
    //     alert("我的处理完成");
    // }

});

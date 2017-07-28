/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.train.coursecheckrule.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursecheckrule.otherController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
    init: function() {
    },
    /** 该视图内的组件事件注册 */
    /** 该视图内的组件事件注册 */
    control: {
        "baseformtab[detCode=coursecheckrule_detail] button[ref=formSave]":{
            beforeclick: function(btn) {
                this.doSave_Tab(btn, "save");
                return false;
            }
        },

        "baseformwin[funCode=coursecheckrule_detail] button[ref=formContinue]": {
            beforeclick: function(btn) {
                this.doSave(btn, "saveContinue");
                return false;
            }
        },

        "baseformwin[funCode=coursecheckrule_detail] button[ref=formSave]": {
            beforeclick: function(btn) {
                this.doSave(btn, "save");

                return false;
            }
        },

        "baseformwin button[ref=formClose]": {
            beforeclick: function(btn) {
                console.log(btn);
            }
        }
    },

    doSave_Tab:function(btn,cmd){
        var self=this;
        //获取基本的容器
        var basetab = btn.up('baseformtab');
        var tabPanel = btn.up("tabpanel[xtype=app-main]");
        var tabItemId = basetab.tabItemId;
        var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页


        //这两个数据是从MainController中传递过来的
        var funCode = basetab.funCode;      //mainLayout的funcode
        var detCode = basetab.detCode;      //detailLayout的funcode

        //获取当前按钮对应的表单
        var detPanel = basetab.down("basepanel[funCode=" + detCode + "]");
        var objForm = detPanel.down("baseform[funCode=" + detCode + "]");


        //获取表单的实际数据
        var formObj = objForm.getForm();
        var funData = detPanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);
        var orderIndex = 1;
        if (formObj.findField("orderIndex")) {
            orderIndex = formObj.findField("orderIndex").getValue() + 1;
        }

        //把checkbox的值转换为数字 ；    暂时测试时设置，
        params.needCheckout=params.needCheckout==true?1:0;
        params.startUsing=params.startUsing==true?1:0;

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
//                            store.load(); //刷新父窗体的grid
                        }

                        loading.hide();
                        tabPanel.remove(tabItem);
                    } else {
                        self.Error(data.obj);
                        loading.hide();
                    }
                },
                failure: function(response) {
                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                    loading.hide();
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
    },

    doSave:function(btn,cmd){

        var self=this;

        var win = btn.up('window');
        var funCode = win.funCode;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();
        var funData = basePanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);
        
        //把checkbox的值转换为数字
        params.startUsing=params.startUsing==true?1:0;
        
        
        //判断当前是保存还是修改操作
        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
        if (formObj.isValid()) {
                                 
            var resObj = self.ajax({
                url: funData.action + "/" + act,
                params: params
            });
            if (resObj.success) {
                //采用返回的数据刷新表单
                //self.setFormValue(formObj, resObj.obj);

                self.msgbox("保存成功!");

                if(cmd=="saveContinue"){
                    formObj.reset();
                   
                    //给窗体赋默认值
                    var insertObj = win.insertObj;                   
                    self.setFormValue(formObj, insertObj);
                    
                }else{
                     win.close();
                }

                var grid = win.funData.grid; //窗体是否有grid参数
                if (!Ext.isEmpty(grid)) {
                    var store = grid.getStore();
                    /*
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        whereSql: win.funData.whereSql,
                        orderSql: win.funData.orderSql,
                        filter: win.funData.filter
                    };*/
                    store.loadPage(1); //刷新父窗体的grid
                }
               
            } else {
                if (!Ext.isEmpty(resObj.obj)) self.Info(resObj.obj);
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
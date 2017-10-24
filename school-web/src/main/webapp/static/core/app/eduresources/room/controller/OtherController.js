
Ext.define("core.eduresources.room.controller.OtherController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.room.otherController',

    /*把不需要使用的组件，移除掉*/
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
   
    init: function() {
        var self = this;

        this.control({
            "mtfuncwindow button[ref=ssOkBtn]":{
                beforeclick:function(btn){
                    console.log("重写mtfuncwindow的确定按钮");
                }
            },
            
            "baseformtab[detCode=room_roomdetail] button[ref=formSave]":{
                beforeclick: function(btn) {
                    this.doSave_Tab(btn, "save");
                    return false;
                }
            },
            "baseformtab[detCode=room_batchroomdetail] button[ref=formSave]": {
                beforeclick: function(btn) {
                    this.doBatchSave_Tab(btn, "save");
                    return false;
                }
            },
        });
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
        console.log(params);
        //把checkbox的值转换为数字 ；    暂时测试时设置，
        params.roomType=params.roomType+"";

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
                         loading.hide();
                        self.Info("保存成功!");

                        var grid = basetab.funData.grid; //此tab是否保存有grid参数
                        if (!Ext.isEmpty(grid)) {
                            var store = grid.getStore();                           
                            store.load(); //刷新父窗体的grid
                        }

                       
                        tabPanel.remove(tabItem);
                    } else {
                        loading.hide();
                        self.Error(data.obj);
                        
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
    
    
    doBatchSave_Tab:function(btn,cmd){
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
        //判断当前是保存还是修改操作
        var act = "batChdoAdd";
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
                        loading.hide();
                        self.Info("保存成功!");

                        var grid = basetab.funData.grid; //此tab是否保存有grid参数
                        if (!Ext.isEmpty(grid)) {
                            var store = grid.getStore();                           
                            store.load(); //刷新父窗体的grid
                        }

                       
                        tabPanel.remove(tabItem);
                    } else {
                        loading.hide();
                        self.Error(data.obj);
                       
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
    
});
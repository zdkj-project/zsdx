/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
 但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
 */
Ext.define("core.cashier.mealtime.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mealtime.otherController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function () {
    },
    /** 该视图内的组件事件注册 */
    control: {
        /**
         * 增加或修改界面的提交按钮
         */
        "baseformtab button[ref=formSave]": {
            beforeclick: function (btn) {
                this.doSave(btn, "addSave");
                return false;
            }
        },

    },

    doSave: function (btn, cmd) {
        var self = this;
        //得到组件
        var basetab = btn.up('baseformtab');
        var tabPanel = btn.up("tabpanel[xtype=app-main]");
        var tabItemId = basetab.tabItemId;
        var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页

        //得到配置信息
//        var funCode = basetab.funCode;      //mainLayout的funcode
        var detCode = basetab.detCode;      //detailLayout的funcode

        var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + detCode + "]");
        
        var formObj = objForm.getForm();
        var funData = basePanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);
        params.beginTime = "1900-01-01 "+params.beginTime;
        params.endTime = "1900-01-01 "+params.endTime;
        //判断当前是保存还是修改操作
        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
        //发送ajax请求
        var resObj = self.ajax({
            url: funData.action + "/" + act,
            params:params
        });
        if (resObj.success) {
          var grid = basetab.funData.grid; //此tab是否保存有grid参数
             if (!Ext.isEmpty(grid)) {
                 var store = grid.getStore();
                 store.loadPage(1);
                 self.Info("保存成功!");
             }//刷新父窗体的grid
            tabPanel.remove(tabItem);
        } else {
            self.Error(resObj.obj);
        }
    },
    
    /**
     * 增加、修改、详细
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    
});
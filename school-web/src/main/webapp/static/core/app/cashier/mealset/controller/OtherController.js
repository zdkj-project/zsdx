/**
 ( *非必须，只要需要使用时，才创建他 )
 此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
 但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
 */
Ext.define("core.cashier.mealset.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mealset.otherController',
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
        //处理单选按钮组
        var radioObject = objForm.down("radiogroup[ref=mealType]").getChecked();
        var sValue = radioObject[0].inputValue;
        
        //组装指标评估标准对象
        var standGrid = basetab.down("basegrid");
        var recordData = standGrid.getStore();
        var stand = new Array();
        var iLen = recordData.getCount();
        var record;
        var emptyStand = 0;
        for (var i = 0; i < iLen; i++) {
            record = recordData.getAt(i);
            if (record.data.mealName === ""||record.data.mealPrice === "") {
                emptyStand = 1;
                break;
            }
        }
        if (emptyStand > 0) {
            self.Error("有标准项未设置内容，请设置");
            return false;
        }
        for (var i = 0; i < iLen; i++) {
            record = recordData.getAt(i);
            stand.push('{"uuid":"' + record.get("uuid") + '","mealName":"' + record.get("mealName") +'","mealType":"' + sValue + '","mealPrice":"' + record.get("mealPrice")+'","mealExplain":"' + record.get("mealExplain")+'"}');
        }

        var mealStand = "[" + stand.join(",") + "]";

        //判断当前是保存还是修改操作
        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
        //发送ajax请求
        var resObj = self.ajax({
            url: funData.action + "/" + act,
            params: {
            	mealStand: mealStand
            }
        });
        if (resObj.success) {
            self.msgbox(resObj.obj);
          var grid = basetab.funData.grid; //此tab是否保存有grid参数
             if (!Ext.isEmpty(grid)) {
                 var store = grid.getStore();
                 store.loadPage(1);
             }//刷新父窗体的grid
            tabPanel.remove(tabItem);
        } else {
            self.Error(resObj.obj);
        }
    }
});
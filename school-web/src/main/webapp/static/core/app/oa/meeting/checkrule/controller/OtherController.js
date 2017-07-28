/**
( *非必须，只要需要使用时，才创建他 )
此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.oa.meeting.checkrule.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.checkrule.otherController',
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
control: {
    "baseformtab[detCode=checkrule_detail] button[ref=formContinue]": {
        beforeclick: function(btn) {
            this.doSave(btn, "saveContinue");
            return false;
        }
    },

    "baseformtab[detCode=checkrule_detail] button[ref=formSave]": {
        beforeclick: function(btn) {
        	//alert("1");
            this.doSave(btn, "save");

            return false;
        }
    },

    "baseformtab button[ref=formClose]": {
        beforeclick: function(btn) {
            console.log(btn);
        }
    }
},

doSave:function(btn,cmd){

    var self=this;

    //var win = btn.up('window');
    
    var basetab = btn.up('baseformtab');
    var tabPanel=btn.up("tabpanel[xtype=app-main]");
    
    var tabItemId=basetab.tabItemId;   
    var tabItem=tabPanel.getComponent(tabItemId);
    
//    var detCode = win.detCode;
//    var basePanel = win.down("basepanel[detCode=" + detCode + "]");
	var funCode = basetab.detCode;
	var basePanel = basetab.down("basepanel[funCode=" + funCode + "]");
    var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
    var formObj = objForm.getForm();
    var funData = basePanel.funData;
    var pkName = funData.pkName;
    var pkField = formObj.findField(pkName);
    var params = self.getFormValue(formObj);
    
    //把checkbox的值转换为数字
    params.startUsing=params.startUsing==true?1:0;
    params.needCheckout=params.needCheckout==true?1:0;
    
    
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
                var insertObj = basetab.insertObj;                   
                self.setFormValue(formObj, insertObj);
                
            }else{
                 //win.close();
                //关闭tab
                tabPanel.remove(tabItem);
            }

            var grid = basetab.funData.grid; //窗体是否有grid参数
            if (!Ext.isEmpty(grid)) {
                var store = grid.getStore();
                /*
                var proxy = store.getProxy();
                proxy.extraParams = {
                    whereSql: win.funData.whereSql,
                    orderSql: win.funData.orderSql,
                    filter: win.funData.filter
                };*/
                // store.loadPage(1); //刷新父窗体的grid
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
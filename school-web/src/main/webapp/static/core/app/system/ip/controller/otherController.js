Ext.define("core.system.ip.controller.otherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.ip.otherController',
	
    // 工具类，可以注释掉
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },

    control:{
        "baseformtab[detCode=creditrule_detail] button[ref=formSave]":{
            beforeclick:function(btn){
                this.doSave_Tab(btn,"save");
                return false;
            }
        },
        doSave_Tab:function(btn,cmd){
            var self=this;
      // 获取基本的容器
            var basetab=btn.up('baseformtab');
            var tabPanel=btn.up("tabpanel[xtype=app-main]");
            var tabItemId=basetab.tabItemId;
            var tabItem=tabPanel.getComponent(tabItemId);

            var funCode=basetab.funCode;
            var detCode=basetab.detCode;
      // 
            var detPanel=basetab.down("basepanel[funCode="+funCode+"]");
            var objForm=detPanel.down("baseform[funCode="+funCode+"]");

      // 获取表单的实际数据

            var formObj=objForm.getForm();
            var funData=detPanel.funData;
            var pkName=funData.pkName;
            var pkField=formObj.findField(pkName);
            var params=self.getFormValue(formObj);
            var orderIndex=1;
            if (formObj.findField("orderIndex")) {
               orderIndex=formObj.findField("orderIndex").getValue()+1;
            }
            params.startUsing=params.startUsing==true?1:0;

      }
    }
         
});
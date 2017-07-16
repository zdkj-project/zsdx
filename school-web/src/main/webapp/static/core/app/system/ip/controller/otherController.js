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
        var act=Ext.isEmpty(pkField.getValue())? "doadd":"doupdate";
            if (formObj.isValid()) {
                var loading=new Ext.loadMask(basetab,{
                    msg:'正在提交，请稍后.......',
                    removeMask:true    
                });
                loading.show();

                self.asyncAjax({
                    url:funData.action + "/"+ act,
                    params:params,
                success:function(response){
                    data=Ext.decode(Ext.valueForm(response.responseText,'{}'));
                    if (data.success) {
                        self.msgbox("保存成功");

                    var grid=basetab.funData.grid;
                    if (!Ext.isEmpty(grid)) {
                        var store=grid.getStore();
                        store.load();
                    }
                    loading.hide();
                    tabPanel.remove(tabItem);
                    }else {
                        self.Error(data.obj);
                        loading.hide();
                    }
                },
                failure:function(response){
                    Ext.Msg.alert('请求失败','错误信息：\n'+response.responseText);
                    loading.hide();
                }
                });

            }else{
                var errors=["前台验证失败，错误信息："];
                formObj.getFields().each(function(f){
                    if (!f.isValid()) {
                        errors.push("<font color=red>"+f.fieldLabel+"</form>:"+f.getErrors().join(","));
                    }
                });
                self.msgbox(errors.join("<br/>"));  
            }   
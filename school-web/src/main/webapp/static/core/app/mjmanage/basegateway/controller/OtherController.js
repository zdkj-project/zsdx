Ext.define("core.mjmanage.basegateway.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.basegateway.othercontroller',
     mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil"
    },
    init: function () {
    	this.control({
    		//网络参数单个设置
    		 "baseformtab[detCode=networkform] button[ref=formSave]": {
                beforeclick: function (btn) {                
                    this.saveDetail_Tab(btn,"netWork");
                    return false;
                }
            },
            //网关参数设置
    		 "baseformtab[detCode=baseandhighform] button[ref=formSave]": {
                beforeclick: function (btn) {                
                    this.saveDetail_Tab(btn,"baseAndHigh");
                    return false;
                }
            },
            
            "panel[xtype=mjmanage.basegateway.ptgatewaybatchform] button[ref=formSave]": {
                beforeclick: function(btn) {
                    this.saveSetFront(btn);
                    return false;                
                }
            },
            //批量添加关闭
            "panel[xtype=mjmanage.basegateway.ptgatewaybatchform] button[ref=formClose]": {
                beforeclick: function(btn) {
                    btn.up('window').close();
                    return false;
                }
            },
            
        })
    },

    saveDetail_Tab:function(btn,cmd){
        var self=this;
        var basetab = btn.up('baseformtab');
        var tabPanel = btn.up("tabpanel[xtype=app-main]");
        var tabItemId = basetab.tabItemId;
        var tabItem = tabPanel.getComponent(tabItemId);   //当前tab页
        var uuid = tabItem.uuid;

        var detCode = basetab.detCode;
        var detLayout = basetab.detLayout;  

        var objForm = basetab.down("baseform");
        var formObj = objForm.getForm();
        var params = self.getFormValue(formObj); 
        var url="";  

        /*处理提交的参数*/
        switch (cmd) {

            case "netWork":
                Ext.apply(params, objForm.formData, {
                    uuid: uuid,
                    gatewayIP:formObj.findField("tlvs[0].valStr").getValue(),
                    netGatewayIp:formObj.findField("tlvs[1].valStr").getValue(),
                    gatewayMask:formObj.findField("tlvs[2].valStr").getValue(),
                    gatewayMac:formObj.findField("tlvs[6].valStr").getValue(),
                    //frontServerStatus:formObj.findField("tlvs[5].valInt").getValue(),
                    //frontServerIP:formObj.findField("tlvs[3].valStr").getValue(),
                    //frontServerPort:formObj.findField("tlvs[4].valInt").getValue(),
                });
	            url=comm.get('baseUrl') + "/PtGateway/doGatewayParam";
                break;
            case "baseAndHigh":
                //基本参数	            	  
	            Ext.apply(params, objForm.baseFormData);

                //高级参数
                var valInt = '';
                for (var i = 1; i <= 4; i++) {
                    valInt += Ext.util.Format.date("0 "+params["time" + i + ""], 'H:i') + "|";
                };
                valInt = valInt.substring(0, valInt.length - 1);
                var highData={
                    'tlvs[2].valStr': valInt,
                    "tlvs[2].len": objForm.highFormData["tlvs[0].len"],//设备重启时间列表
                    "tlvs[2].type": objForm.highFormData["tlvs[0].type"],
                    "tlvs[2].tag":objForm.highFormData["tlvs[0].tag"]
                }
                Ext.apply(params, highData);
                params.gatewayRadio=objForm.down("radiogroup[ref=gatewayRadio]").getChecked()[0].inputValue;
                
	            url= comm.get('baseUrl') + "/PtGateway/doBaseAndHighParam";//基础和高级sava合并一个方法
	            break;
         }
        
        //判断当前是保存还是修改操作
        if (formObj.isValid()) {

            var loading = new Ext.LoadMask(basetab, {
                msg: '正在提交，请稍等...',
                removeMask: true// 完成后移除
            });
            loading.show();

            self.asyncAjax({
            	url: url,
                params: params,
                //回调代码必须写在里面
                success: function (response) {
                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                    if (data.success) {

                        self.msgbox("提交成功!");
                        basetab.baseGrid.getStore().load();
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
    
    saveSetFront:function(btn){
        var self=this;
        var win=btn.up('window');
        var baseGrid =win.baseGrid;
        var dicForm = btn.up("panel[xtype=mjmanage.basegateway.ptgatewaybatchform]").getForm();
        var params = self.getFormValue(dicForm);
        var uuid = "";
       
        if (dicForm.isValid()) {

            var selectObject = baseGrid.getSelectionModel().getSelection();
            for (var i = 0; i < selectObject.length; i++) {
                uuid = selectObject[i].get('uuid') + "," + uuid;
            };

            var loading = new Ext.LoadMask(win, {
                msg: '正在提交，请稍等...',
                removeMask: true// 完成后移除
            });
            loading.show();

            self.asyncAjax({
                url: comm.get('baseUrl') + "/PtGateway/doupdateBatch",
                params: {
                    frontserverId: params.frontserverId,
                    uuid: uuid
                },                       
                success: function(response) {
                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                    if(data.success){
                        loading.hide();
                        win.close();
                        self.msgbox('处理成功');
                        baseGrid.getStore().load();         
                    }else {
                        loading.hide();
                        self.Error(data.obj);
                    }         

                },
                failure: function(response) {                   
                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                    loading.hide();
                }
            }); 
        }
        return false;
    }
});
Ext.define("core.mjmanage.smartdevice.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.smartdevice.othercontroller',
    mixins: {
    	  suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil"
    },
    init: function () {
    },
    /** 该视图内的组件事件注册 */
    control: {
        "baseformtab[detCode=highparam_detail] button[ref=formSave]": {
            beforeclick: function (btn) {                
                this.saveHighParam_Tab(btn,"setHighParam");
                return false;
            }
        },
        "baseformtab[detCode=baseparam_detail] button[ref=formSave]": {
            beforeclick: function (btn) {                
                this.saveBaseParam_Tab(btn,"setBaseParam");
                return false;
            }
        },
    },
    
    saveHighParam_Tab:function(btn,cmd){
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
       
        //高级参数
        var valInt = '';
        for (var i = 1; i <= 4; i++) {
            valInt += Ext.util.Format.date("0 "+params["time" + i + ""], 'H:i') + "|";
        };
        valInt = valInt.substring(0, valInt.length - 1);
                        
        Ext.apply(params, objForm.highFormData,{
            'tlvs[0].valStr': valInt
        });
        params.termRadio=objForm.down("radiogroup[ref=termRadio]").getChecked()[0].inputValue;
        params.termTypeID= basetab.recordData.termTypeID;
        
        //判断当前是保存还是修改操作
        if (formObj.isValid()) {

            var loading = new Ext.LoadMask(basetab, {
                msg: '正在提交，请稍等...',
                removeMask: true// 完成后移除
            });
            loading.show();

            self.asyncAjax({
                url: comm.get('baseUrl') + "/BasePtTerm/doSetHighParam",
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

    saveBaseParam_Tab:function(btn,cmd){
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
       
        //批量参数
        params.termRadio=objForm.down("radiogroup[ref=termRadio]").getChecked()[0].inputValue;
        //设备类型
        params.termTypeID= basetab.recordData.termTypeID;

        if(params.termTypeID=='9'){
            //电控基础参数               
            

        }else if (params.termTypeID == '4') {     
            //门禁基础参数
            var str = '';
            var checkboxgroup = objForm.down("checkboxgroup[ref=KrateForm_lblOperationBehaviors]");
            for (var i = 0; i < checkboxgroup.items.items.length; i++) {
                if (checkboxgroup.items.items[i].checked == true) {
                    str += "1";
                } else {
                    str += "0";
                }
            };
            params['tlvs[2].valStr'] = str;

            params['tlvs[1].valInt']=params['tlvs[1].valInt']==true?1:0;
            params['tlvs[3].valInt']=params['tlvs[3].valInt']==true?1:0;
            params['tlvs[4].valInt']=params['tlvs[4].valInt']==true?1:0;
            params['tlvs[5].valInt']=params['tlvs[5].valInt']==true?1:0;
            params['tlvs[7].valInt']=params['tlvs[7].valInt']==true?1:0;
            params['tlvs[8].valInt']=params['tlvs[8].valInt']==true?1:0;
            params['tlvs[10].valInt']=params['tlvs[10].valInt']==true?1:0;
            params['tlvs[12].valInt']=params['tlvs[12].valInt']==true?1:0;
            params['tlvs[13].valInt']=params['tlvs[13].valInt']==true?1:0;

        }else if (params.termTypeID == '17') {
            //17灯控开关
            var items = null;
            var controlVal = null;
            var str = '';
            var notes = '';
            var fieldsetCpt = null;
            for (var i = 1; i <= 4; i++) {
                fieldsetCpt = objForm.down("fieldset[ref=lItems" + i + "]");
                
                var time = '';
                var status = '';
                for (var k = 0; k < 4; k++) {
                    controlVal = fieldsetCpt.down("field[name=time" + k + "]").getValue();
                    if (controlVal != null) {
                        time += controlVal + "|";  
                        //使用down的方式去查找数据，获取到第一个按钮【关】，即：当关为false返回1，反之亦然                    
                        controlVal = fieldsetCpt.down("field[name='items"+i+".status" + k + "']").getValue();
                        if (controlVal != null) {
                            if(controlVal==false){
                                status+=1;
                            }else{
                                status+=0;
                            }

                        }
                    }
                };
                time = time.substring(0, time.length - 1);
                controlVal = fieldsetCpt.down('field[name=type]').getValue();
                if (controlVal != null) {
                    str += i + "#" + controlVal + "#" + time + "#" + status + "&";
                }
                var s;
                var on=fieldsetCpt.down('field[name=on]').getValue();
                if(on=="on"||on=="1"){
                    s="1"
                }else{
                    s="0"
                }
                notes += controlVal + ':'+fieldsetCpt.down('field[name=notes]').getValue() +':'+s+"|";
            };
            str = str.substring(0, str.length - 1);
            notes = notes.substring(0, notes.length - 1);
            var xtjg = objForm.down('[ref=tlvsvalInt]').getValue();
            var switchConnectType=objForm.down('[ref=tlvsva2Int]').getValue();
          
            params['tlvs[1].valStr']= str;
            params['tlvs[0].valInt']= xtjg;
            params['tlvs[2].valInt']= parseInt(switchConnectType);
            params["notes"]=notes
            

        } else if (params.termTypeID == '11') {
            //11红外
            var str = '1#';
            var time = '';
            var status = '';
            for (var i = 0; i < 4; i++) {
                time += params["time" + i] + "|";
            };
            time = time.substring(0, time.length - 1);
            for (var i = 0; i < 4; i++) {
                //使用表单获取值的方式去查找数据（貌似根据勾选的inputValue的1和0来得到的true或false）
                if(params["status" + i]==true){
                    status+=1;
                }else{
                    status+=0;
                }
            };
            str += params.type + "#" + time + "#" + status;
            params['tlvs[3].valStr']=str;           

        } else if (params.termTypeID == '8') {            
            //水控基础参数
            var str = '';
            var tlvs5 = '';
            // var numitems = params['tlvs[5].valStr'];
            // for (var i = 0; i < numitems.length; i++) {
            //     tlvs5 += numitems[i] + "|"
            // };
            // params['tlvs[5].valStr'] = tlvs5;
            for (var i = 0; i < 4; i++) {
                var check = objForm.down("checkboxgroup[ref=sKBaseParamForm_lblOperationBehaviors" + (i+1) + "]");    
                var numberItems = objForm.down("container[ref=termparam.KrateForm" + (i+1) + "]");             
                if (check != null) {
                    for (var k = 0; k < check.items.items.length; k++) {
                        if (check.items.items[k].checked == true) {
                            str += "1";
                        } else {
                            str += "0";
                        }
                    };
                }
                if (numberItems != null) {
                    for (var k = 0; k < numberItems.items.items.length; k++) {
                         tlvs5 += numberItems.items.items[k].getValue() + "|";
                    };
                }
            }
            params['tlvs[5].valStr'] = tlvs5;
            params['tlvs[11].valStr'] = str;
        }

        Ext.apply(params, objForm.baseFormData);
        
        
        //判断当前是保存还是修改操作
        if (formObj.isValid()) {

            var loading = new Ext.LoadMask(basetab, {
                msg: '正在提交，请稍等...',
                removeMask: true// 完成后移除
            });
            loading.show();

            self.asyncAjax({
                url: comm.get('baseUrl') + "/BasePtTerm/doSetBaseParam",
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
   
    
});
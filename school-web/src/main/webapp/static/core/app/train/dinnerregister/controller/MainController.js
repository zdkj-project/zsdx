Ext.define("core.train.dinnerregister.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.dinnerregister.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function() {
        /*control事件声明代码，可以写在这里
        this.control({

        });
        */
    },
    control: {
       'basepanel[xtype=dinnerregister.mainlayout]':{
            render:function(cpt){
                var form=cpt.down("baseform[xtype=dinnerregister.mainform]");
                var btngridExport = form.down("button[ref=gridExport]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1
                 && roleKey.indexOf("ZONGWUROLE") == -1 && roleKey.indexOf("FOODMANAGER") == -1) {
                	btngridExport.setHidden(true);
                }
                var params={
                    start:0,
                    limit:-1,
                    filter: '[{"type":"date","comparison":"=","value":"'+Ext.Date.format(new Date(), 'Y-m-d')+'","field":"dinnerDate"}]'  
                    //classId:classId                             
                };
                this.loadInfo(form,params);  
            }
       },

       "fieldset[xtype=dinnerregister.mainformfieldset]": {
           afterrender: function (fieldset, eOpts) {
               var btnsubmitRegister = fieldset.down("button[ref=submitRegister]");
               var roleKey = comm.get("roleKey");
               if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 
                    && roleKey.indexOf("ZONGWUROLE") == -1 && roleKey.indexOf("FOODMANAGER") == -1) {
            	   btnsubmitRegister.setHidden(true);
               }
           }
       },
       
       'fieldset[xtype=dinnerregister.mainformfieldset] button[ref=submitRegister]':{
            beforeclick:function(btn){

                var self=this;
                
                var fieldset=btn.up("fieldset");
                var uuid=fieldset.down("field[name=uuid]").getValue();
                var breakfastRealText=fieldset.down("field[name=breakfastReal]");
                var lunchRealText=fieldset.down("field[name=lunchReal]");
                var dinnerRealText=fieldset.down("field[name=dinnerReal]");
                var breakfastStandRealText=fieldset.down("field[name=breakfastStandReal]");
                var lunchStandRealText=fieldset.down("field[name=lunchStandReal]");
                var dinnerStandRealText=fieldset.down("field[name=dinnerStandReal]");
                var addDinnerStandText=fieldset.down("field[name=addDinnerStand]");
                var tissueStandText=fieldset.down("field[name=tissueStand]");
                var otherStandText=fieldset.down("field[name=otherStand]");
                    
                if(!breakfastRealText.isValid()||!lunchRealText.isValid()||!dinnerRealText.isValid()
                    ||!breakfastStandRealText.isValid()||!lunchStandRealText.isValid()||!dinnerStandRealText.isValid()
                    ||!addDinnerStandText.isValid()||!tissueStandText.isValid()||!otherStandText.isValid()){
                    self.Warning("输入的数据有误，请检查！");
                    return false;
                }

                //Ext.Msg.wait('正在提交中,请稍后...', '温馨提示');
                var loading = new Ext.LoadMask(baseGrid, {
                    msg: '正在提交数据...',
                    removeMask: true// 完成后移除
                });
                loading.show();

                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClassrealdinner/doupdate",
                    params: {
                        uuid:uuid,
                        breakfastReal:breakfastRealText.getValue(),
                        lunchReal:lunchRealText.getValue(),
                        dinnerReal:dinnerRealText.getValue(),
                        breakfastStandReal:breakfastStandRealText.getValue(),
                        lunchStandReal:lunchStandRealText.getValue(),
                        dinnerStandReal:dinnerStandRealText.getValue(),
                        addDinnerStand:addDinnerStandText.getValue(),
                        tissueStand:tissueStandText.getValue(),
                        otherStand:otherStandText.getValue()                         
                    },
                    timeout:1000*60*60, //1个小时
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));                
                        if(data.success){
                            //Ext.Msg.hide();
                            loading.hide();
                            self.Info("登记成功！");
                        }else{
                            //Ext.Msg.hide();
                            loading.hide();
                            self.Error(data.obj);
                        }
                    },
                    failure: function(response) {
                       // Ext.Msg.hide();
                        loading.hide();
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                    }
                });

                return false;
            }
       },

       'baseform button[ref=gridFastSearchBtn]':{
            click:function(btn){
                var form = btn.up("baseform");
                var girdSearchText = form.down("datetimefield[ref=girdFastSearchText]");
                    
                var name = girdSearchText.getName();
                var value = girdSearchText.getValue();
                
                var params={
                    start:0,
                    limit:-1,
                    filter: '[{"type":"date","comparison":"=","value":"'+value+'","field":"'+name+'"}]'                          
                };
                this.loadInfo(form,params);

            }
        },

         /**
         * 导出某天的信息
         */
        "baseform button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var form = btn.up("baseform");
                var girdSearchText = form.down("datetimefield[ref=girdFastSearchText]");
                            
                var dateValue = girdSearchText.getValue();  //日期
                var rawValue=girdSearchText.getRawValue();  //日期
            
                var title = "确定要导出【"+rawValue+"】就餐登记信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassrealdinner/exportExcel?date=' + dateValue + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClassrealdinner/checkExportEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.Info(data.obj);
                                        component.destroy();                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },
       
    },


    loadInfo:function(form,params){
        var self=this;

        // var loading = new Ext.LoadMask(form, {
        //     msg: '正在获取数据，请稍等...',
        //     removeMask: true// 完成后移除
        // });
        // loading.show();

        //Ext.Msg.wait('正在查询中,请稍后...', '温馨提示');

        //var form=cpt.down("baseform[xtype=dinnerregister.mainform]");
        form.removeAll();

        self.asyncAjax({
            url: comm.get("baseUrl")  + "/TrainClassrealdinner/list",
            params: params,
            timeout:1000*60*60, //1个小时
            //回调代码必须写在里面
            success: function(response) {
                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
               
                var items=[];
                for(var i=0;i<data.totalCount;i++){
                    var recordData=data.rows[i];
                    //var fieldset=Ext.create('core.train.dinnerregister.view.MainFormFieldSet',{
                    items.push({
                        xtype:'dinnerregister.mainformfieldset',
                        title: recordData.className,     
                        defaults:{
                            width:'100%',
                            margin:"10 5",
                            labelAlign : 'right',
                            //columnWidth : 0.5,
                            msgTarget: 'qtip',
                        },                        
                        items :[ {
                            xtype: "container",
                            layout: "column",                             
                            items:[{
                                name: 'uuid',
                                xtype : 'displayfield',
                                hidden:true,
                                value:recordData.uuid
                            },{
                                columnWidth: 0.3,   
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },                             
                                fieldLabel: '就餐日期',
                                name: 'dinnerDate',
                                xtype : 'displayfield',
                                value: self.formatDateStr(recordData.dinnerDate)
                            }, {
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '联系人',
                                name: 'contactPerson',
                                xtype : 'displayfield',
                                value: recordData.contactPerson
                            },{ 
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '联系电话',
                                name: 'contactPhone',
                                xtype : 'displayfield',
                                value: recordData.contactPhone
                            }]
                        },{
                            xtype:'container',
                            layout:'column',
                            items:[{
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '预定早餐围/人数',
                                name: 'breakfastCount',
                                xtype : 'displayfield',
                                value: recordData.breakfastCount
                            }, {
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '预定午餐围/人数',
                                name: 'lunchCount',
                                xtype : 'displayfield',
                                value: recordData.lunchCount
                            }, {
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '预定晚餐围/人数',
                                name: 'dinnerCount',
                                xtype : 'displayfield',
                                value: recordData.dinnerCount
                            }]
                        },{
                            xtype:'container',
                            layout:'column',
                            items:[{
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '预定早餐餐标',
                                name: 'breakfastStand',
                                xtype : 'displayfield',
                                value: recordData.breakfastStand
                            }, {
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '预定午餐餐标',
                                name: 'lunchStand',
                                xtype : 'displayfield',
                                value: recordData.lunchStand
                            }, {
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: '预定晚餐餐标',
                                name: 'dinnerStand',
                                xtype : 'displayfield',
                                value: recordData.dinnerStand
                            }]
                        },{
                            xtype:'container',
                            layout:'column',
                            items:[{
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,
                                fieldLabel: '实际早餐围/人数',
                                name: 'breakfastReal',
                                xtype: "numberfield",        
                                minValue: 0,
                                maxValue:999, 
                                value: recordData.breakfastReal,
                                emptyText: "请输入实际数值",
                                listeners: {
                                    change: function(field, record, index) {                  
                                        var currentForm=field.up("baseform[xtype=dinnerregister.mainform]");
                                        var breakfastStandReal=currentForm.down("field[name=breakfastStandReal]").getValue();
                                        var lunchStandReal=currentForm.down("field[name=lunchStandReal]").getValue();
                                        var dinnerStandReal=currentForm.down("field[name=dinnerStandReal]").getValue();
                                        var breakfastReal=currentForm.down("field[name=breakfastReal]").getValue();
                                        var lunchReal=currentForm.down("field[name=lunchReal]").getValue();
                                        var dinnerReal=currentForm.down("field[name=dinnerReal]").getValue();

                                        currentForm.down("field[name=breakfastInfCount]").setValue(breakfastStandReal*breakfastReal);
                                        currentForm.down("field[name=lunchInfCount]").setValue(lunchStandReal*lunchReal);
                                        currentForm.down("field[name=dinnerInfCount]").setValue(dinnerStandReal*dinnerReal);
                                    }
                                }      
                            }, {
                               
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,
                                fieldLabel: '实际午餐围/人数',
                                name: 'lunchReal',
                                xtype: "numberfield",        
                                minValue: 0,
                                maxValue:999, 
                                value: recordData.lunchReal,
                                emptyText: "请输入实际数值",
                                listeners: {
                                    change: function(field, record, index) {                  
                                    	 var currentForm=field.up("baseform[xtype=dinnerregister.mainform]");
                                         var breakfastStandReal=currentForm.down("field[name=breakfastStandReal]").getValue();
                                         var lunchStandReal=currentForm.down("field[name=lunchStandReal]").getValue();
                                         var dinnerStandReal=currentForm.down("field[name=dinnerStandReal]").getValue();
                                         var breakfastReal=currentForm.down("field[name=breakfastReal]").getValue();
                                         var lunchReal=currentForm.down("field[name=lunchReal]").getValue();
                                         var dinnerReal=currentForm.down("field[name=dinnerReal]").getValue();

                                         currentForm.down("field[name=breakfastInfCount]").setValue(breakfastStandReal*breakfastReal);
                                         currentForm.down("field[name=lunchInfCount]").setValue(lunchStandReal*lunchReal);
                                         currentForm.down("field[name=dinnerInfCount]").setValue(dinnerStandReal*dinnerReal);
                                    }
                                }      
                            }, {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,
                                fieldLabel: '实际晚餐围/人数',
                                name: 'dinnerReal',
                                xtype: "numberfield",
                                minValue: 0,
                                maxValue:999, 
                                value: recordData.dinnerReal,
                                emptyText: "请输入实际数值",
                                listeners: {
                                    change: function(field, record, index) {                  
                                    	 var currentForm=field.up("baseform[xtype=dinnerregister.mainform]");
                                         var breakfastStandReal=currentForm.down("field[name=breakfastStandReal]").getValue();
                                         var lunchStandReal=currentForm.down("field[name=lunchStandReal]").getValue();
                                         var dinnerStandReal=currentForm.down("field[name=dinnerStandReal]").getValue();
                                         var breakfastReal=currentForm.down("field[name=breakfastReal]").getValue();
                                         var lunchReal=currentForm.down("field[name=lunchReal]").getValue();
                                         var dinnerReal=currentForm.down("field[name=dinnerReal]").getValue();

                                         currentForm.down("field[name=breakfastInfCount]").setValue(breakfastStandReal*breakfastReal);
                                         currentForm.down("field[name=lunchInfCount]").setValue(lunchStandReal*lunchReal);
                                         currentForm.down("field[name=dinnerInfCount]").setValue(dinnerStandReal*dinnerReal);
                                    }
                                }      
                            }]
                        },{                        
                            xtype: "container",
                            layout: "column",
                            labelAlign: "right",
                            items:[ {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,                            
                                fieldLabel: '实际早餐餐标',
                                name: 'breakfastStandReal',
                                xtype : 'numberfield',
                                minValue: 0,
                                maxValue:9999, 
                                value: recordData.breakfastStandReal,
                                listeners: {
                                    change: function(field, record, index) {                  
                                    	 var currentForm=field.up("baseform[xtype=dinnerregister.mainform]");
                                         var breakfastStandReal=currentForm.down("field[name=breakfastStandReal]").getValue();
                                         var lunchStandReal=currentForm.down("field[name=lunchStandReal]").getValue();
                                         var dinnerStandReal=currentForm.down("field[name=dinnerStandReal]").getValue();
                                         var breakfastReal=currentForm.down("field[name=breakfastReal]").getValue();
                                         var lunchReal=currentForm.down("field[name=lunchReal]").getValue();
                                         var dinnerReal=currentForm.down("field[name=dinnerReal]").getValue();

                                         currentForm.down("field[name=breakfastInfCount]").setValue(breakfastStandReal*breakfastReal);
                                         currentForm.down("field[name=lunchInfCount]").setValue(lunchStandReal*lunchReal);
                                         currentForm.down("field[name=dinnerInfCount]").setValue(dinnerStandReal*dinnerReal);
                                    }
                                }      
                            }, {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,                               
                                fieldLabel: '实际午餐餐标',
                                name: 'lunchStandReal',
                                xtype : 'numberfield',
                                minValue: 0,
                                maxValue:9999, 
                                value: recordData.lunchStandReal,
                                listeners: {
                                    change: function(field, record, index) {                  
                                    	 var currentForm=field.up("baseform[xtype=dinnerregister.mainform]");
                                         var breakfastStandReal=currentForm.down("field[name=breakfastStandReal]").getValue();
                                         var lunchStandReal=currentForm.down("field[name=lunchStandReal]").getValue();
                                         var dinnerStandReal=currentForm.down("field[name=dinnerStandReal]").getValue();
                                         var breakfastReal=currentForm.down("field[name=breakfastReal]").getValue();
                                         var lunchReal=currentForm.down("field[name=lunchReal]").getValue();
                                         var dinnerReal=currentForm.down("field[name=dinnerReal]").getValue();

                                         currentForm.down("field[name=breakfastInfCount]").setValue(breakfastStandReal*breakfastReal);
                                         currentForm.down("field[name=lunchInfCount]").setValue(lunchStandReal*lunchReal);
                                         currentForm.down("field[name=dinnerInfCount]").setValue(dinnerStandReal*dinnerReal);
                                    }
                                }      
                            }, {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,                                
                                fieldLabel: '实际晚餐餐标',
                                name: 'dinnerStandReal',
                                xtype : 'numberfield',
                                minValue: 0,
                                maxValue:9999, 
                                value: recordData.dinnerStandReal,
                                listeners: {
                                    change: function(field, record, index) {                  
                                    	 var currentForm=field.up("baseform[xtype=dinnerregister.mainform]");
                                         var breakfastStandReal=currentForm.down("field[name=breakfastStandReal]").getValue();
                                         var lunchStandReal=currentForm.down("field[name=lunchStandReal]").getValue();
                                         var dinnerStandReal=currentForm.down("field[name=dinnerStandReal]").getValue();
                                         var breakfastReal=currentForm.down("field[name=breakfastReal]").getValue();
                                         var lunchReal=currentForm.down("field[name=lunchReal]").getValue();
                                         var dinnerReal=currentForm.down("field[name=dinnerReal]").getValue();

                                         currentForm.down("field[name=breakfastInfCount]").setValue(breakfastStandReal*breakfastReal);
                                         currentForm.down("field[name=lunchInfCount]").setValue(lunchStandReal*lunchReal);
                                         currentForm.down("field[name=dinnerInfCount]").setValue(dinnerStandReal*dinnerReal);
                                    }
                                }      
                            }]
                        },{
                            xtype: "container",
                            layout: "column",
                            labelAlign: "right",
                            items: [{            
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: "实际早餐总额",    
                                name: "breakfastInfCount",
                                xtype : 'displayfield',
                                value:recordData.breakfastReal*recordData.breakfastStandReal,
                                readOnly :true  
                            },{          
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: "实际午餐总额",    
                                name: "lunchInfCount",
                                xtype : 'displayfield',
                                value:recordData.lunchReal*recordData.lunchStandReal,
                                readOnly :true      
                            },{    
                                columnWidth: 0.3,
                                fieldStyle : {                                          
                                    fontSize:'18px',
                                },
                                fieldLabel: "实际晚餐总额",    
                                name: "dinnerInfCount",
                                xtype : 'displayfield',
                                value:recordData.dinnerReal*recordData.dinnerStandReal,
                                readOnly :true          
                            }]
                        },{                        
                            xtype: "container",
                            layout: "column",
                            labelAlign: "right",
                            items:[ {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,                            
                                fieldLabel: '加菜金额',
                                name: 'addDinnerStand',
                                xtype : 'numberfield',
                                minValue: 0,
                                maxValue:9999, 
                                value: recordData.addDinnerStand
                            }, {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,                               
                                fieldLabel: '纸巾金额',
                                name: 'tissueStand',
                                xtype : 'numberfield',
                                minValue: 0,
                                maxValue:9999, 
                                value: recordData.tissueStand                          
                            }, {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,                                
                                fieldLabel: '其他金额',
                                name: 'otherStand',
                                xtype : 'numberfield',
                                minValue: 0,
                                maxValue:9999, 
                                value: recordData.otherStand                       
                            },{            
                                columnAlign:'center',
                                xtype:'button',
                                columnWidth: 0.1,
                                //width:120,
                                margin:"0 0 0 20",
                                text:'确定登记',
                                ref:'submitRegister',
                                iconCls:'x-fa fa-check-square',
                            }]
                        }],
                    });                                                        
                }
                form.add(items);

                if(data.totalCount==0){
                    form.setHtml("<span style='padding: 20px;font-size: 20px;text-align: center;  display: inline-block;width: 100%;'>今日没有需要就餐登记的班级！</span>");
                }else{
                    form.setHtml("");
                }
                //loading.hide();
                
                                                             
            },
            failure: function(response) {
                //loading.hide();
                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
            }
        }); 
    }

});

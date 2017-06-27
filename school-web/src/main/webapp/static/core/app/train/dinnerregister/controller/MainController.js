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
                var params={
                    start:0,
                    limit:-1,
                    filter: '[{"type":"date","comparison":"=","value":"'+Ext.Date.format(new Date(), 'Y-m-d')+'","field":"dinnerDate"}]'  
                    //classId:classId                             
                };
                this.loadInfo(form,params);  
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
                    
                if(!breakfastRealText.isValid()||!lunchRealText.isValid()||!dinnerRealText.isValid()){
                    self.Warning("输入的数据有误，请检查！");
                    return false;
                }

                //Ext.Msg.wait('正在提交中,请稍后...', '温馨提示');

                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClassrealdinner/doupdate",
                    params: {
                        uuid:uuid,
                        breakfastReal:breakfastRealText.getValue(),
                        lunchReal:lunchRealText.getValue(),
                        dinnerReal:dinnerRealText.getValue()                 
                    },
                    timeout:1000*60*60, //1个小时
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));                
                        if(data.success){
                            //Ext.Msg.hide();
                            self.msgbox("登记成功！");
                        }else{
                            //Ext.Msg.hide();
                            self.Error(data.obj);
                        }
                    },
                    failure: function(response) {
                       // Ext.Msg.hide();
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
                                        self.msgbox(data.obj);
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
        Ext.Msg.wait('正在查询中,请稍后...', '温馨提示');

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
                            xtype: "container",
                            layout: "column",
                            labelAlign: "right",
                            items:[ {
                                beforeLabelTextTpl: comm.get('required'),
                                allowBlank: false, 
                                columnWidth: 0.3,
                                fieldLabel: '实际早餐围/人数',
                                name: 'breakfastReal',
                                xtype: "numberfield",        
                                minValue: 0,
                                maxValue:999, 
                                value: recordData.breakfastReal,
                                emptyText: "请输入实际数值"      
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
                                emptyText: "请输入实际数值"      
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
                                emptyText: "请输入实际数值"      
                            }, {            
                                columnAlign:'center',
                                xtype:'button',
                                columnWidth: 0.1,
                                //width:120,
                                margin:"0 0 0 20",
                                text:'确定登记',
                                ref:'submitRegister',
                                iconCls:'x-fa fa-check-square',
                            }]
                        }]
                    });                                                        
                }
                form.add(items);

                setTimeout(function(){
                    Ext.Msg.hide();    
                },500);
                                                             
            },
            failure: function(response) {
                Ext.Msg.hide();
                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
            }
        }); 
    }

});

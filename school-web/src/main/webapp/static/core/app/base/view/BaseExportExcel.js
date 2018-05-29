Ext.define("core.base.view.BaseExportExcel", {
        extend: "Ext.form.Panel",
        alias: "widget.baseexportexcel",
        layout: "auto",
        align: "left",
        frame: true,
        funData:"",
        store:"",
        fieldDefaults: { //统一设置表单字段默认属性
            labelSeparator: '：', //分隔符
            labelWidth: 90, //标签宽度
            msgTarget: 'side',
            width: 400
        }, 
        items : [ {  
                    xtype: 'itemselector',
                    name: 'itemselector',
                    id: 'itemselector-field',
                    anchor: '100%',
                    //fieldLabel: 'ItemSelector',
                    imagePath: "images",
                    width:580,
                    store: this.store,
                    displayField: 'name',
                    valueField: 'name',
                    //value: ['3', '4', '6'],
                    allowBlank: false,
                    msgTarget: 'side',
                    fromTitle: '待导出字段',
                    toTitle: '已选择字段'
                }],
        //dockedItems: createDockedItems('itemselector-field',this.modelName,this.fileName),
        //dockedItems:[{}],
        constructor:function(obj){
            var me = this;
            var funData = obj.funData;
            me.dockedItems = createDockedItems('itemselector-field',funData.modelName,funData.fileName);
            me.store = createStore(funData.modelName);
            me.items[0].store = me.store;
            me.callParent(arguments);      
        }

});
function createStore(modelName){
    return new Ext.data.Store({  
                            autoLoad : true,  
                            model: factory.ModelFactory.getModelByName("com.zd.school.excel.ExcelCellField", "checked").modelName,
                            proxy :{
                                type:"ajax",
                                url:comm.get('baseUrl') + "/ExcelFactory/getMapperCellFields",
                                extraParams: {
                                    modelName: modelName
                                }
                            } 
                        }); 
}
function createDockedItems(fieldId,modelName,fileName) {
        return [{
            xtype: 'toolbar',
            dock: 'bottom',
            ui: 'footer',
            defaults: {
                minWidth: 75
            },
            items: ['->', {
                text: '清空',
                handler: function(){
                    var field = Ext.getCmp(fieldId);
                    if (!field.disabled) {
                        field.clearValue();
                    }
                }
            }, {
                text: '重置',
                handler: function() {
                    Ext.getCmp(fieldId).up('form').getForm().reset();
                }
            }, {
                text: '导出',
                handler: function(){
                    var value = Ext.getCmp(fieldId).getValue();
                    Ext.Msg.wait('正在导出中,请稍后...','温馨提示');
                    Ext.create('Ext.panel.Panel', {
                            title: 'Hello',
                            width: 200,
                            html: '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="' + comm.get('baseUrl') + '/ExcelFactory/exportExcel?modelName='+modelName+'&fileName='+fileName+'&exportField='+value+'"></iframe>',
                            renderTo: Ext.getBody(),
                            listeners: {
                               afterrender: function() { 
                                    /*var task = new Ext.util.DelayedTask(function(){  
                                        Ext.Msg.hide();
                                    }); 
                                    task.delay(5000);*/
                                }
                            }
                    });
                    Ext.Ajax.request({     
                        url:comm.get('baseUrl') + '/ExcelFactory/exportExcel?modelName='+modelName+'&fileName='+fileName+'&exportField='+value,  
                        success: function(resp,opts) {   
                            var task = new Ext.util.DelayedTask(function(){  
                                        Ext.Msg.hide();
                                    }); 
                            task.delay(1200);
                        },   
                        failure: function(resp,opts) {   
                                 var respText = Ext.util.JSON.decode(resp.responseText);   
                                 Ext.Msg.alert('错误', respText.error);   
                        }     
                    });
                }
            }]
        }];
}
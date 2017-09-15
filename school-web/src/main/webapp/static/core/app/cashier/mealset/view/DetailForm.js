Ext.define("core.cashier.mealset.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mealset.detailform",
    xtype: 'mealsetdeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
        labelAlign: "right"
    },
    items: [{
        xtype: 'fieldset',
        title: '餐类',
        defaultType: 'textfield',
        border:1,
        style: {
            backgroundColor: '#f5f5f5',
            fontSize:'14px',
            fontFamily: '微软雅黑',
            fontWeight:800,
            color:'#C3190C',
            borderColor: '#C6CBD6',
            borderStyle: 'solid'
        },
        defaults: {anchor: '100%'},
        layout: 'column',
        items: [ {
            fieldLabel: "餐类",
            width:600,
            xtype: 'radiogroup',
            ref:'indicatorObject',
            items: [
                { boxLabel: '早餐', name: 'indicatorObject', inputValue: '1', checked: true },
                { boxLabel: '午餐', name: 'indicatorObject', inputValue: '2' },
                { boxLabel: '晚餐', name: 'indicatorObject', inputValue: '3' },
                { boxLabel: '夜宵', name: 'indicatorObject', inputValue: '4' },
            ],
            listeners: {
                change: function(field, record, index) {  
                    var currentForm=field.up("baseform[xtype=mealset.detailform]");
                    var standgrid = currentForm.down("basegrid[xtype=mealset.standgrid]");
                    var mealType = record.indicatorObject;
                    var standgridStore = standgrid.getStore();
                    var standgridProxy = standgridStore.getProxy();
                    standgridProxy.extraParams = {
                    		filter: "[{'type':'short','comparison':'=','value':'"+mealType+"','field':'mealType'}]",
                    }
                    standgridStore.load();
                }  
            }
        }]
    },{
        xtype: 'fieldset',
        title: '快餐',
        border:1,
        style: {
            fontSize:'14px',
            fontFamily: '微软雅黑',
            fontWeight:800,
            color:'#C3190C',
            borderColor: '#C6CBD6',
            borderStyle: 'solid'
        },
        items:[{
            xtype:'mealset.standgrid',
            height:600
        }]
    }]
});
               

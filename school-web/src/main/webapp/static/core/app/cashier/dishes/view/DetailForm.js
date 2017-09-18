Ext.define("core.cashier.dishes.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.dishes.detailform",
    xtype: 'dishesdeailform',
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
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    },{
        xtype: 'fieldset',
        title: '菜品分类',
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
            fieldLabel: "菜品类型",
            width:600,
            xtype: 'radiogroup',
            ref:'dishesType',
            items: [
                { boxLabel: '荤菜类', name: 'dishesType', inputValue: '1', checked: true },
                { boxLabel: '素菜类', name: 'dishesType', inputValue: '2' },
                { boxLabel: '汤类', name: 'dishesType', inputValue: '3' },
                { boxLabel: '主食类', name: 'dishesType', inputValue: '4' },
                { boxLabel: '酒水类', name: 'dishesType', inputValue: '5' }
            ],
//            listeners: {
//                change: function(field, record, index) {  
//                    var currentForm=field.up("baseform[xtype=dishes.detailform]");
//                    var standgrid = currentForm.down("basegrid[xtype=dishes.standgrid]");
//                    var dishesType = record.dishesType;
//                    var standgridStore = standgrid.getStore();
//                    var standgridProxy = standgridStore.getProxy();
//                    standgridProxy.extraParams = {
//                    		filter: "[{'type':'short','comparison':'=','value':'"+dishesType+"','field':'dishesType'}]",
//                    }
//                    standgridStore.load();
//                }  
//            }
        }]
    },{
        xtype: 'fieldset',
        title: '菜单',
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
            xtype:'dishes.standgrid',
            height:600
        }]
    }]
});
               

Ext.define("core.train.dinnerregister.view.MainFormFieldSet", {
    extend: "Ext.form.FieldSet",
    alias: "widget.dinnerregister.mainformfieldset",
    layout: "form", //从上往下布局
    border:1, 
    margin:'0 5 20 5', 
    style: {
        backgroundColor: '#f5f5f5',
        fontSize:'20px',
        fontFamily: '微软雅黑',
        fontWeight:800,
        color:'#C3190C',
        borderColor: '#C6CBD6',
        borderStyle: 'solid',
    },              
    //bodyPadding: '0 15 30 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 150,
        labelAlign: "right"
    },
    defaults:{
        width:'100%',
        margin:"0",                   
        //columnWidth : 0.5,
    },
    // items :[ {
    //     xtype: "container",
    //     layout: "column",
    //     labelAlign: "right",
    //     items:[{
    //         columnWidth: 0.18,
    //         fieldLabel: '就餐日期',
    //         name: 'dinnerDate',
    //         xtype : 'displayfield',
    //         value: recordData.dinnerDate
    //     }, {
    //         columnWidth: 0.18,
    //         fieldLabel: '联系人',
    //         name: 'contactPerson',
    //         xtype : 'displayfield',
    //         value: recordData.contactPerson
    //     },{
    //         fieldLabel: '联系电话',
    //         columnWidth: 0.18,
    //         name: 'contactPhone',
    //         xtype : 'displayfield',
    //         value: recordData.contactPhone
    //     },{
    //         columnWidth: 0.15,
    //         fieldLabel: '预定早餐围/人数',
    //         name: 'breakfastCount',
    //         xtype : 'displayfield',
    //         value: recordData.breakfastCount
    //     }, {
    //         columnWidth: 0.15,
    //         fieldLabel: '预定午餐围/人数',
    //         name: 'lunchCount',
    //         xtype : 'displayfield',
    //         value: recordData.lunchCount
    //     }, {
    //         columnWidth: 0.15,
    //         fieldLabel: '预定晚餐围/人数',
    //         name: 'dinnerCount',
    //         xtype : 'displayfield',
    //         value: recordData.dinnerCount
    //     }]
    // },{                        
    //     xtype: "container",
    //     layout: "column",
    //     labelAlign: "right",
    //     items:[ {
    //         labelWidth: 150,
    //         beforeLabelTextTpl: comm.get('required'),
    //         allowBlank: false, 
    //         columnWidth: 0.25,
    //         fieldLabel: '实际早餐围/人数',
    //         name: 'breakfastReal',
    //         xtype: "numberfield",        
    //         minValue: 0,
    //         maxValue:999, 
    //         value: recordData.breakfastReal,
    //         emptyText: "请输入实际数值"      
    //     }, {
    //         labelWidth: 150,
    //         beforeLabelTextTpl: comm.get('required'),
    //         allowBlank: false, 
    //         columnWidth: 0.25,
    //         fieldLabel: '实际午餐围/人数',
    //         name: 'lunchReal',
    //         xtype: "numberfield",        
    //         minValue: 0,
    //         maxValue:999, 
    //         value: recordData.lunchReal,
    //         emptyText: "请输入实际数值"      
    //     }, {
    //         labelWidth: 150,
    //         beforeLabelTextTpl: comm.get('required'),
    //         allowBlank: false, 
    //         columnWidth: 0.25,
    //         fieldLabel: '实际晚餐围/人数',
    //         name: 'dinnerReal',
    //         xtype: "numberfield",        
    //         minValue: 0,
    //         maxValue:999, 
    //         value: recordData.dinnerReal,
    //         emptyText: "请输入实际数值"      
    //     }, {            
    //         columnAlign:'center',
    //         xtype:'button',
    //         //columnWidth: 0.25,
    //         width:120,
    //         margin:"0 0 0 20",
    //         text:'确定登记',
    //         ref:'submitRegister',
    //         iconCls:'x-fa fa-check-square',
    //     }]
    // }]
})
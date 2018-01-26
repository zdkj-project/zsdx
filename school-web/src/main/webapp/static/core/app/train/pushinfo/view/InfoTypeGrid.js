Ext.define("core.train.pushinfo.view.InfoTypeGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.pushinfo.infotypegrid",
    store: {
        fields: ['name', 'value'],
        data: [
            { name: '今日收信', value: '1' },
            { name: '今日发信', value: '2'},
            { name: '历史收信', value: '3'},
            { name: '历史发信', value: '4'},   
        ]       
    },
    selModel:{ 
        mode:'single',
    },
    noPagging: true,
    //工具栏操作按钮
    tbar: null,    
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '信息分类',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800,
                lineHeight:'32px'
            }
        }]
    },
    panelButtomBar:null,
    columns: {
      defaults:{
            flex:1,     //【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            align:'center',
            titleAlign:"center"
        },
        items: [{
            text: "主键",
            dataIndex: "value",
            hidden: true
        }, {
            text: "信息分类",
            dataIndex: "name"
        }]
    },

});
Ext.define("core.good.activity.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.activity.maingrid",

    frame:false,
    columnLines: false,
    /*使用basegrid自动去获取store
    dataUrl: comm.get('baseUrl') + "/news/list",  
    model: 'com.zd.school.good.model.good.GoodNews',    //模型类

    //排序字段及模式定义
    defSort: [{
        property: 'orderIndex',
        direction: 'DESC'
    }],
    //分组字段
    defGroup: [],
    //扩展参数
    extParams: {},
    */

    /*使用store和model手动装载*/  
    
    noPagging:true,
    store: {
        type: 'activity.maingridstore',
        //.......这里可以写传入这个store的其他参数
        //model:'core.good.signup.model.SignupGridModel',
    },

    tbar:[],    //不使用默认的tbar
    panelButtomBar:{
        xtype:'activity.mainquerypanel',
    },
    dockedItems: [{
        xtype: 'pagingtoolbar',
        dock: 'bottom',
        store: Ext.data.StoreManager.lookup('activity.maingridstore'),
        displayInfo: true,       
        emptyMsg: "没有可显示的数据",
        plugins: [          
            Ext.create('Ext.ux.ComboPageSize', {})
        ],
    }],
    
    
    columns: {        
        defaults:{
            //flex:1,     //【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            align:'center',
            titleAlign:"center"
        },
        items:[{
            xtype: "rownumberer",
            flex:0,
            width: 60,
            text: '序号',
            align: 'center',
            resizable :false   
        },{
            xtype:'actioncolumn',
            text: "操作",
            width:100,
            resizable :false,
            items: [{
                iconCls: 'x-fa fa-pencil-square',            
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function(view, rowIndex, colIndex, item) {                 
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick', {
                        view:view.grid,
                        record: rec
                    });
                }
            },{
                iconCls:'x-fa fa-file-text',
                tooltip: '详细',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {             
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick', {
                        view:view.grid,
                        record: rec
                    });
                }
            },{
                iconCls:'x-fa fa-minus-circle',
                tooltip: '删除',
                ref: 'gridDelete',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view:view.grid,
                        record: rec
                    });
                }
            }]
        },{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        },{
            text: "姓名",
            flex:3,
            dataIndex: "name"
        },{
            text:'性别',
            dataIndex:"sex",        
            renderer:function(v){   
                return v==true?'男':'女';              
            },
            flex:1
        },{
            text:'年龄',
            dataIndex:"age",
            flex:1
        },{
            text:'生日',
            dataIndex:"birthday",
            flex:1,
            renderer:function(v){                    
                return Ext.Date.format(new Date(v), 'Y年m月d h:i:s');              
            }     
        }]
    },
   
});
Ext.define("core.good.news.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.news.maingrid",

    title:'表格数据',
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
        type: 'news.maingridstore',
        //.......这里可以写传入这个store的其他参数
        //model:'core.good.signup.model.SignupGridModel',
    },
    dockedItems: [{
        xtype: 'pagingtoolbar',
        dock: 'bottom',
        store: Ext.data.StoreManager.lookup('news.maingridstore'),
        displayInfo: true,       
        emptyMsg: "没有可显示的数据"
    }],
    columns: {
        items:[{
            xtype: "rownumberer",
            flex:0,
            width: 70,
            text: '序号',
            align: 'center'
        },{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        },{
            text: "快讯标题",
            flex:3,
            dataIndex: "newTitle"
        },{
        	text:'发布人',
        	dataIndex:"createUser",
        	flex:1
        },{
        	text:'发布时间',
        	dataIndex:"createTime",
        	flex:1,
            renderer:function(v){                    
                return Ext.Date.format(new Date(v), 'Y-m-d H:i:s');              
            }     
        }],
        defaults:{
            flex:1,
            align:'left'
        }
    },
   
});
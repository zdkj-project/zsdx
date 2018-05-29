Ext.define("core.good.news.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.news.mainlayout',

    requires: [    
        'core.good.news.controller.MainController',
        'core.good.news.model.MainGridModel',
        'core.good.news.store.MainGridStore', 
        'core.good.news.view.MainGrid',
        'core.good.news.view.MainQueryPanel',
        "core.good.news.view.DetailLayout",
   
    ],

    controller: 'news.mainController',


    funCode: "news_main",
    detCode: 'news_detail',
    detLayout: 'news.detaillayout',
    layout:'border',
    funData: {
        action: comm.get('baseUrl') + "/goodnews", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {},
        width:1000,
        height:700,
    },
   
    items: [{
        xtype: "news.mainquerypanel",
        region: "north",
        margin:'0 0 2 0',
    }, {
	    xtype: "news.maingrid",
	    region: "center"
    }]
  
});
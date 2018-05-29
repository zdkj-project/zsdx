Ext.define("core.system.menu.view.DetailLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.menu.detaillayout',
    funCode: "menu_detail",
    funData: {
        action: comm.get('baseUrl') + "/BaseMenu", //请求controller路径
        whereSql: "and isDelete='0'",
        orderSql: " order by parentNode,isHidden,orderIndex asc",
        pkName: "uuid", //主键id    
        defaultObj: {
            orderIndex: 1
        }
    },
    
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    
    items: [{
        xtype: "menu.menuform"
    }]
})
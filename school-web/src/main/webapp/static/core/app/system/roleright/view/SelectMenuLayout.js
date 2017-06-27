Ext.define("core.system.roleright.view.SelectMenuLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.roleright.detaillayout',

    controller: 'roleright.detailController',

    funCode: "roleright_selectmenu",
    funData: {
        action: comm.get('baseUrl') + "/BasePayitem", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {}
    },
    layout:'fit',
    items: [{
        xtype: "roleright.selectmenugrid",
        flex:1
    }]
})
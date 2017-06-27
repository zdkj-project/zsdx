Ext.define("core.system.dept.store.DeptStore", {
    extend: "Ext.data.TreeStore",

    alias: 'store.dept.deptstore',

    defaultRootId: "0",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.baseset.model.BaseOrgTree", "checked").modelName,
    proxy: {
        type: "ajax",
        url: comm.get('baseUrl') + "/BaseOrg/treelist",
        extraParams: {
            excludes: 'checked',
            whereSql: "  and isDelete='0'",
            orderSql: ' ORDER BY NODELEVEL,ORDER_INDEX'
        },
        reader: {
            type: "json"
        },
        writer: {
            type: "json"
        }
    },
    autoLoad: true
});
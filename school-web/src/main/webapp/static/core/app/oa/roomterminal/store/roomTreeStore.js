Ext.define("core.oa.roomterminal.store.roomTreeStore", {
    extend: "Ext.data.TreeStore",
    defaultRootId: "ROOT",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.comm.model.CommTree", "checked").modelName,
    proxy: {
        type: "ajax",
        url: comm.get('baseUrl') + "/BuildRoominfo/allRoomTree",
        //url: comm.get('baseUrl') + "/JwClassRoomAllot/treelist",
        extraParams: {
            excludes: 'checked',
            whereSql: ""
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
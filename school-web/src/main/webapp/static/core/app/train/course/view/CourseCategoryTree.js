Ext.define("core.train.course.view.CourseCategoryTree", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.course.coursecategorytree",
    dataUrl: comm.get('baseUrl') + "/TrainCoursecategory/treelist",
    model: factory.ModelFactory.getModelByName(
        "com.zd.school.jw.train.model.TrainCoursecategoryTree", "checked").modelName,
    al: true,
    extParams: {
        whereSql: "and isDelete='0' ",
        orderSql: " order by parentNode,orderIndex"
    },
    columnLines:false,
    selModel: null,
    lines:true,
    useArrows: false,
    viewConfig: {
        stripeRows: false
    },
    // tools: [{
    //     type: 'refresh',
    //     qtip: '刷新',
    //     handler: function(event, toolEl, header) {
    //         var tree = header.ownerCt
    //         tree.getStore().load();
    //         tree.getSelectionModel().deselectAll(true);
    //     }
    // }],
})

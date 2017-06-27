Ext.define("core.train.courseeval.store.MainGridStore",{
    extend:"Ext.data.Store",

    alias: 'store.courseeval.maingridstore',

    model:factory.ModelFactory.getModelByName('com.zd.school.jw.train.model.TrainCourseinfo', "checked").modelName,

    proxy: {
        type: "ajax",
        url: comm.get("baseUrl") + "/TrainClassschedule/listClassEvalCourse",
        extraParams: {},
        reader: {
            type: "json",
            rootProperty: "rows",
            totalProperty: 'totalCount'
        },
        writer: {
            type: "json"
        }
    },
    autoLoad:true

});

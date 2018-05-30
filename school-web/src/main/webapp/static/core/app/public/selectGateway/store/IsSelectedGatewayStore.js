
Ext.define("core.public.selectGateway.store.IsSelectedGatewayStore",{
    extend:"Ext.data.Store",

    alias: 'store.public.selectGateway.isselectedgatewaystore',

 
    //fields:['uuid', 'xm','mobilePhone','sfzjh','xbm','workUnits']
   
    model: factory.ModelFactory.getModelByName("com.zd.school.control.device.model.PtGateway", "checked").modelName,
    /*
    proxy: {
        type: 'ajax',
        url: comm.get("baseUrl") + "/TrainClass/CourseTeacher",
        extParams: {
            classId: "",
            //查询的过滤字段
            //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
            //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
        },
        reader: {
            type: 'json',
            rootProperty: 'rows',
            totalProperty: 'totalCount'
        },
        writer: {
            type: 'json'
        }
    },
    */
});
Ext.define("core.eduresources.room.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.room.mainlayout',
    requires: [    
    	"core.eduresources.room.view.MainLayout",
        "core.eduresources.room.view.areaGrid", 
        "core.eduresources.room.view.areaDetailLayout",
        "core.eduresources.room.view.areaForm",
        "core.eduresources.room.view.RoomLayout", 
        "core.eduresources.room.view.RoomGrid", 
        "core.eduresources.room.view.RoomForm", 
        "core.eduresources.room.view.BatchRoomForm",
   
    ],
    
    controller: 'room.roomController',
    otherController:'room.otherController',
    
    funCode: "room_main",
    detCode: 'room_areadetail',
    detLayout: 'room.detaillayout',
    border: false,
    funData: {
        action: comm.get('baseUrl') + "/BuildRoominfo", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter:"",
        pkName: "uuid",
        defaultObj: {
            orderIndex: 1,
            roomNet:"0"
        }
    },    
    layout: 'border',
    //bodyPadding: 2,
    items: [{
        collapsible: true,
        split: true,
        xtype: "room.areagrid",
        region: "west",
        style:{
            border: '1px solid #ddd'
        },
        width: comm.get("clientWidth") * 0.32
    }, {
        xtype: "room.RoomGrid",
        style:{
            border: '1px solid #ddd'
        },
        region: "center"
    }]
})
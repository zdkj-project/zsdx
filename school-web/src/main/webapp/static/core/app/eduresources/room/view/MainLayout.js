Ext.define("core.eduresources.room.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.room.mainlayout',
    requires: [   
    	"core.eduresources.room.controller.RoomController",
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
    detLayout: 'room.roomdetaillayout',
    det1Layout: 'room.batchroomdetaillayout',
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
        },
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
        	addTitle:'添加房间',
        	editTitle:'编辑房间',
        	detailTitle:'房间详情',
        	batchaddTitle:'批量添加房间',
        }
    },    
    layout: 'border',
    
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
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
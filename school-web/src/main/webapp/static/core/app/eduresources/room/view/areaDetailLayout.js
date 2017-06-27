Ext.define("core.eduresources.room.view.areaDetailLayout", {
    extend: "core.base.view.BasePanel",
	alias: 'widget.room.areadetaillayout',
	funCode: "room_areadetail",
	funData: {
		action: comm.get('baseUrl') + "/BuildRoomarea", //请求Action
        whereSql: "", //表格查询条件
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {
			orderIndex: 1,
		}
	},
	layout: 'fit',
	bodyPadding: 2,
	items: [{
		xtype: "room.areaform"
	}]
})
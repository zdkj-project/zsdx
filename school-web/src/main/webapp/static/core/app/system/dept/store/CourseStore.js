Ext.define("core.system.dept.store.CourseStore", {
	extend: "Ext.data.Store",
	model: factory.ModelFactory.getModelByName("com.zd.school.jw.eduresources.model.JwTBasecourse", "checked").modelName,
	proxy: {
		type: "ajax",
		url: comm.get('baseUrl') + "/basecourse/list", //对应后台controller路径or方法
		extParams: {
			filter: "[{'type':'string','comparison':'=','value':'aaaa','field':'uuid'}]"
		},
		reader: {
			type: "json",
			root: "rows",
			totalProperty: 'totalCount'
		},
		writer: {
			type: "json"
		}
	},
	autoLoad: true
})
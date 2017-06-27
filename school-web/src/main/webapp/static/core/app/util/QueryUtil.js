/*
树形弹出框的查询功能，待修改
*/
Ext.define("core.util.QueryUtil",{
	/**
	 * 查询树形
	 * @param {} config
	 */
	selTreeWin:function(config){		
		config.queryType="mttreeview";
		var win=Ext.create("core.base.view.query.MtssWindow",config);
		win.show();
	},
	resetQueryPanel:function(queryPanel){
		
		var queryFields=queryPanel.query("basequeryfield");
		var querySql="";
		Ext.each(queryFields,function(queryField){
			var fieldName=queryField.name;
			var valueField=queryField.down("field[name="+fieldName+"_field]");
			var startField=queryField.down("field[name="+fieldName+"_start]");
			var endField=queryField.down("field[name="+fieldName+"_end]");
			if(valueField){
				valueField.reset();
			}
			if(startField){
				startField.reset();
			}
			if(endField){
				endField.reset();
			}
		});
	}
});
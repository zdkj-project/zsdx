/*
消息提示工具
*/
Ext.define("core.util.MessageUtil", {
	/**
	 *  提示信息
	 * @param {} config
	 */
	 
	msgbox: function(config) {
		var title = "提示";
		var context = "";
		if (typeof(config) == "string") {
			context = config;
		}
		Ext.example.msg(title, context);
	},
	
	// 警告提示
	Warning: function(config) {
		var title = "警告";
		var context = "";
		if (typeof(config) == "string") {
			context = config;
		}
		Ext.MessageBox.show({
			title: title,
			msg: context,
			buttons: Ext.MessageBox.OK,
			icon: Ext.MessageBox.WARNING
		})
	},
	Error: function(config) {
		var title = "错误";
		var content = "";
		if (typeof(config) == "string") {
			context = config;
		}
		Ext.MessageBox.show({
			title: title,
			msg: context,
			buttons: Ext.MessageBox.OK,
			icon: Ext.MessageBox.ERROR
		})
	},
	Info: function(config) {
		var title = "信息";
		var content = "";
		if (typeof(config) == "string") {
			context = config;
		}
		Ext.MessageBox.show({
			title: title,
			msg: context,
			buttons: Ext.MessageBox.OK,
			icon: Ext.MessageBox.INFO
		})
	}
});
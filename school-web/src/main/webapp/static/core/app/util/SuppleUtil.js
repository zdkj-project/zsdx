Ext.define("core.util.SuppleUtil", {
	/**
	 * 同步请求Ajax
	 * @param {} config
	 * @return {}
	 	var resObj = self.syncAjax({
            url: funData.action + "/signup",
            params: {
                actId: '016A2C1A-8775-43B2-BCF1-7D251DDBCB9D',
                factNumb: "-1393581090",
                signDate: '2016-06-03 10:21:43.927'
            }
        });
        //回调代码可以放在外面写
        if (resObj.success) {	
            self.msgbox(resObj.obj);
        };
	 */
	syncAjax: function(config) {
		var data = {};
		var request = {
			method: "POST",
			async: false,
			timeout: 60000,
			success: function(response) {
				data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
			},
			failure: function(response) {
				alert('数据请求出错了！！！！\n错误信息：\n' + response.responseText);
			}
		};
		var request = Ext.apply(request, config);
		Ext.Ajax.request(request);
		return data;
	},
	/*
	异步ajax
		var resObj = self.asyncAjax({
            url: funData.action + "/signup",
            params: {
                actId: '016A2C1A-8775-43B2-BCF1-7D251DDBCB9D',
                factNumb: "-1393581090",
                signDate: '2016-06-03 10:21:43.927'
            },
            //回调代码必须写在里面
            success: function(response) {
				data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

				self.msgbox(data.obj);
			}
        });             
	*/
	asyncAjax: function(config) {
		var data = {};
		var request = {
			method: "POST",
			async: true,
			timeout: 60000,
			success: function(response) {
				//data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
			},
			failure: function(response) {
				Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
			}
		};
		var request = Ext.apply(request, config);
		Ext.Ajax.request(request);
		return data;
	},
	
	ajax: function(config) {
		var data = {};
		var request = {
			method: "POST",
			async: false,
			timeout: 60000,
			success: function(response) {
				data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
			},
			failure: function(response) {
				alert('数据请求出错了！！！！\n错误信息：\n' + response.responseText);
			}
		};
		var request = Ext.apply(request, config);
		Ext.Ajax.request(request);
		return data;
	}
});
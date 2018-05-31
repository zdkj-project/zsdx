Ext.define("core.mjmanage.roomallot.controller.OtherController", {
	extend: "Ext.app.ViewController",
	alias: 'controller.mjmanage.roomallot.othercontroller',
	mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        queryUtil: "core.util.QueryUtil"
    },
    init: function () {
		var self=this;
		this.control({
			"baseformwin[funCode=roomallot_detail] button[ref=formSave]": {
				beforeclick: function (btn) {    
					this.saveDetail_Win(btn);
					return false;
				}
			},
		})
	},

  	saveDetail_Win:function(btn){
        var self=this;
        var win = btn.up('window');
        var funCode = win.funCode;
        //找到详细布局视图
        var selectTeacherlayout = win.down("basepanel[funCode=" + funCode + "]");
        var isselectteachergrid = selectTeacherlayout.down("panel[xtype=mjmanage.roomallot.isselectteachergrid]");
        var selectroomgrid = selectTeacherlayout.down("basegrid");
        var getCount = isselectteachergrid.getStore().getCount();
        if (getCount <= 0) {
        	self.msgbox("有数据才能继续操作!");
        	return;
        }
        var uuid = new Array();
        var isSelectStore = isselectteachergrid.getStore();
        for (var i = 0; i < getCount; i++) {
        	 var record = isSelectStore.getAt(i);
        	 var pkValue = record.get("uuid");
        	 if(uuid.indexOf(pkValue)==-1)
                uuid.push(pkValue);
        };
        var roomId = win.roomId;
        if (uuid.length > 0) {
	        // var loading = self.LoadMask(win);

	        self.asyncAjax({
	        	url: comm.get('baseUrl') + "/BaseOfficeAllot/doAdd",
	        	params: {
	        		tteacId: uuid.join(","),
	        		roomId: roomId
	        	},              
	                //回调代码必须写在里面
	                success: function (response) {
	                	var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                        var baseGrid = win.funData.grid; //此tab是否保存有grid参数
	                	if (data.success) {
                            self.msgbox("提交成功!");
	                        if (!Ext.isEmpty(baseGrid)) {
	                        	var store = baseGrid.getStore();
	                        	store.load();                         
	                        }
	                        // loading.hide();
	                        win.close();

	                    } else {
	                    	// loading.hide();
	                    	self.Warning(data.obj);
	                    	var store = baseGrid.getStore();
	                        store.load();      
	                    	win.close();
	                    }
	                },
	                failure: function(response) {                   
	                	Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
	                	// loading.hide();
	                }
	            });
		    }else {
	            self.Warning("没有选择房间");
	        }

    },


});
Ext.define("core.mjmanage.deviceallot.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.deviceallot.maincontroller',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    
    init: function() {
    },
    
    control: {

        "basepanel basegrid[xtype=mjmanage.deviceallot.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btnAdd = grid.down("button[ref=gridAdd_Tab]");
                var btnGridEdit = grid.down("button[ref=gridEdit_Tab]");
                var btnDelete = grid.down("button[ref=gridDelete]");
                    
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1) {
                      btnAdd.setHidden(true);
                      btnGridEdit.setHidden(true);
                      btnDelete.setHidden(true);
                    }
            },
            beforeitemclick: function(grid) {
                    this.disabledFuncBtn(grid);              
                    return false;
            },

        },
    	
        //区域列表刷新按钮
        "basetreegrid[xtype=mjmanage.deviceallot.roominfotree] button[ref=gridRefresh]": {
            beforeclick: function(btn) {
                btn.up('basetreegrid').getStore().load();
                var mainlayout = btn.up("basepanel[xtype=mjmanage.deviceallot.mainlayout]");
                var mianGrid = mainlayout.down("basegrid[xtype=mjmanage.deviceallot.maingrid]");
                var store = mianGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams.roomId="";
                proxy.extraParams.roomLeaf="";
                return false;
            }
        },
    	
        //分配设备按钮
    	"basegrid[xtype=mjmanage.deviceallot.maingrid] button[ref=gridAdd_Tab]": {
            beforeclick: function(btn) {
           	    this.doAllot(btn);
                return false;
            }
        },
                
    	 "basegrid[xtype=mjmanage.deviceallot.maingrid] button[ref=gridDelete]": {
            beforeclick: function(btn) {
                this.doDeleteRecords(btn);
                return false;
            }
        },

         /**
         * 操作列的操作事件
         */
         "basegrid[xtype=mjmanage.deviceallot.maingrid] actioncolumn": {

            deleteClick: function(data) {
                this.doDeleteRecords(null,data.view,data.record);
                return false;
            },
            
        }, 
        
        "basegrid[xtype=mjmanage.deviceallot.maingrid] button[ref=gridExport]": {
            beforeclick: function(btn) {
                this.doExport(btn);
                return false;
            }
        },
        
    },
    
    //分配设备事件
    doAllot: function (btn) {
      	var self = this;
      	 //得到组件
      	var baseGrid=btn.up("basegrid");

      	var basePanel = baseGrid.up("basepanel");
        var detCode =  "deviceallot_layout";               //打开的tab也的detCode标识，可自定指定，用于查找唯一组件
         
      	var otherController = basePanel.otherController;    //关键：打开的tab页面的视图控制器
      	if (!otherController)
      		  otherController = '';  
         var itemXtype = "mjmanage.deviceallot.deviceallotlayout";
         var xItemType=[{
            xtype:itemXtype,
            funCode:detCode
          }];
          var win = Ext.create('core.base.view.BaseFormWin', {
                      title: "分配设备",
                      width: 1150,
                      height: 650,
                      operType: "add",
                      controller: otherController,
                      detCode: detCode,
                      iconCls: 'x-fa fa-plus-circle',
                      baseGrid:baseGrid,
                      items:xItemType,
           }).show();
    },
    doDeleteRecords:function(btn,grid,record){
        var self=this;
        var records;
        var baseGrid = grid;
        if(!baseGrid){
            baseGrid=btn.up("basegrid");
            records = baseGrid.getSelectionModel().getSelection();
        }else{
            records=new Array();
            records.push(record);
        }
        funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        //得到配置信息
        var funData = basePanel.funData;  
        var uuid = '';
        if (records.length <= 0) {
            self.msgbox('请选择一条数据');
            return;
        }

        if (records.length > 0) {
            //封装ids数组
            Ext.Msg.confirm('提示',"是否删除设备？", function (btn, text) {
                if (btn == 'yes') {
                    
                    var loading = new Ext.LoadMask(baseGrid, {
                        msg: '正在提交，请稍等...',
                        removeMask: true// 完成后移除
                    });
                    loading.show();


                    for (var i = 0; i < records.length; i++) {            
                        uuid += records[i].get('uuid') + ',';
                    };

                    self.asyncAjax({
                        url: funData.action + "/dodelete",
                        params: {
                             uuid: uuid                       
                        },                    
                        success: function(response) {
                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                            if(data.success){
                                 baseGrid.getStore().remove(records); //不刷新的方式
                                 self.msgbox(data.obj);                               
                            }else {
                                self.Error(data.obj);
                            }           
                            loading.hide();
                        },
                        failure: function(response) {                   
                            Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                            loading.hide();
                        }
                    });     
                }
            });
        } else {
            self.msgbox("请选择数据");
        }
    },
    
    doExport:function(btn){
        var self = this;
        var baseGrid = btn.up("basegrid");
        var mainlayout=baseGrid.up("panel[xtype=mjmanage.deviceallot.mainlayout]");
       
        var roominfotreegrid=mainlayout.down("panel[xtype=mjmanage.deviceallot.roominfotree]");
        var records = roominfotreegrid.getSelectionModel().getSelection();
        var roomId ="";
        var roomLeaf ="";
        if(records.length>0){
            roomId = records[0].get('id');
            roomLeaf = records[0].get("leaf");
            if(roomLeaf==true)
                roomLeaf="1";
            else
                roomLeaf="0";
        }
        var userGrid = mainlayout.down("basegrid[xtype=mjmanage.deviceallot.maingrid]");
        //获取快速搜索栏数据
        var girdSearchTexts = userGrid.query("field[funCode=girdFastSearchText]");
        var termSN ="";
        var termSN1 ="";
        if(girdSearchTexts[0]!=null){
        	termSN1 = girdSearchTexts[0].getValue();
        }
        
        //获取高级搜索栏数据
        var queryPanel = userGrid.down("basequeryform[xtype=mjmanage.deviceallot.mainquerypanel]"); 
        var queryFields=queryPanel.query("basequeryfield");
        var termSN2="";
        var termNo="";
        var termName ="";
        Ext.each(queryFields,function(queryField){
			var fieldName=queryField.name;
			var type=queryField.operationType;
			var valueField=queryField.down("field[name="+fieldName+"_field]");
			var value=valueField.getValue();
			if(fieldName=="termSN"){
				termSN2=value;
			}
			if(fieldName=="termNo"){
				termNo=value;
			}
			if(fieldName=="termName"){
				termName=value;
			}
        });
        
        if(termSN2!=""){
        	termSN=termSN2
        }else{
        	termSN=termSN1
        }
        
        var title = "确定要导出智能设备管理的信息吗？";
        Ext.Msg.confirm('提示', title, function (btn, text) {
            if (btn == "yes") {
                Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                var component = Ext.create('Ext.Component', {
                    title: 'HelloWorld',
                    width: 0,
                    height: 0,
                    hidden: true,
                    html: '<iframe src="' + comm.get('baseUrl') + '/PtTerm/doExportPtTermAllotExcel?termSN='+termSN+'&roomId='+roomId+'&termSN='+termSN+'&termNo='+termNo+'&termName='+termName+'&roomLeaf='+roomLeaf+'"></iframe>',
                    renderTo: Ext.getBody()
                });

                var time = function () {
                    self.syncAjax({
                        url: comm.get('baseUrl') + '/PtTerm/checkPtTermAllotExportEnd',
                        timeout: 1000 * 60 * 30,        //半个小时
                        //回调代码必须写在里面
                        success: function (response) {
                            data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                            if (data.success) {
                                Ext.Msg.hide();
                                self.msgbox(data.obj);
                                component.destroy();
                            } else {
                                if (data.obj == 0) {    //当为此值，则表明导出失败
                                    Ext.Msg.hide();
                                    self.Error("导出失败，请重试或联系管理员！");
                                    component.destroy();
                                } else {
                                    setTimeout(function () {
                                        time()
                                    }, 1000);
                                }
                            }
                        },
                        failure: function (response) {
                            Ext.Msg.hide();
                            Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                            component.destroy();
                        }
                    });
                };
                setTimeout(function () {
                    time()
                }, 1000);    //延迟1秒执行
            }
        });
       return false;
 	},

    disabledFuncBtn:function(grid){
        var basePanel = grid.up("basepanel");
        var basegrid = basePanel.down("basegrid[xtype=mjmanage.basefrontserver.maingrid]");
        var records = basegrid.getSelectionModel().getSelection();

        var btnGridEdit = basegrid.down("button[ref=gridEdit_Tab]");
        var btnDelete = basegrid.down("button[ref=gridDelete]");

        if (records.length == 0) {
            btnGridEdit.setDisabled(true);
            btnDelete.setDisabled(true);
        } else if (records.length == 1) {
            btnGridEdit.setDisabled(false);
            btnDelete.setDisabled(false);
        } else {
            btnGridEdit.setDisabled(true);
        }
    },
});
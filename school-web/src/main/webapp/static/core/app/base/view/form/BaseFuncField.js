Ext.define("core.base.view.form.BaseFuncField",{
	//extend:"Ext.form.field.Trigger",
	extend:"Ext.form.field.Picker",
	alias: 'widget.basefuncfield',
	mixins: {
		sqlUtil:"core.util.SqlUtil",
		messageUtil: "core.util.MessageUtil"
	},
	editable : false,
    //trigger1Cls: Ext.baseCSSPrefix + 'form-clear-trigger',    
    //trigger2Cls: Ext.baseCSSPrefix + 'form-search-trigger',
	initComponent: function(){
		var self=this;
		//增加两个事件。 查询前，查询后
		//self.addEvents('beforequery','query');
		this.callParent(arguments);
	},


    
    triggers: {
    	//picker这个是系统默认的，可以修改，
        picker: {			
        	cls:Ext.baseCSSPrefix + 'form-search-trigger',
            handler: 'onTriggerClick',
            scope: 'this',
            focusOnMousedown: true
        },
        //clear自己新加入的，命名不限定
        clear: {
            cls:Ext.baseCSSPrefix + 'form-clear-trigger',
            handler: 'onTrigger1Click',
            weight:-1,
            scope: 'this',
            focusOnMousedown: true
        },
    },

	/**
	 * 清除按钮事件
	 */
	onTrigger1Click : function(){
		var self=this;
		self.reset();
		var configInfo=self.configInfo;
		var fieldInfo=configInfo.fieldInfo;
		var fieldArray=fieldInfo.split(",");
		var dataField=fieldArray[0].split("~");
		var bf,rec;//表格和表单，如果是表格的化则更新表格的字段值，如果是表单则更新表单
		if(self.ownerCt.xtype=='editor'){
			var grid = self.ownerCt.floatParent;
			rec = grid.getSelectionModel().getSelection()[0];
		}else{
			bf = self.up('form').getForm();
		}
		Ext.each(dataField,function(f,index){
			if(rec){
				rec.set(f,null);
			}else{
				var bff=bf.findField(f);
				if(bff){
					bff.setValue(null);
				}
			}	
		});
	},
	onTriggerClick : function(){
		var self=this;
		//得到功能信息
		var funcController=self.funcController;
		var funcPanel=self.funcPanel;
		var funcTitle=self.funcTitle;
		var configInfo=self.configInfo;

		var formPanel=self.formPanel;	//调用处的表单信息

		//得到配置信息
		var fieldInfo=configInfo.fieldInfo;
		var width = configInfo.width;
		var height = configInfo.height;
		if (!width)
			width = comm.get("resolutionWidth")*0.5;
		if (!height)
			height = comm.get("resolutionHeight")*0.5;
		var muiltSelect=configInfo.muiltSelect;
		var fieldArray=fieldInfo.split(",");
		var dataField=fieldArray[0].split("~");
		var gridField=fieldArray[1].split("~");
		 var flag = self.fireEvent('beforequery', self);
		 if(flag!=false){
		 	whereSql=configInfo.whereSql;
		 	filter = configInfo.filter;
		 	if(whereSql){
			 	var bf,rec;//表格和表单，如果是表格的化则更新表格的字段值，如果是表单则更新表单
				if(self.ownerCt.xtype=='editor'){
					var grid = self.ownerCt.floatParent;
					rec = grid.getSelectionModel().getSelection()[0];
				}else{
					bf = self.up('form').getForm();
				}
				whereSql=self.parseWhereSql(whereSql,bf,rec);
		 	}
			//加载控制器
			/*不需要
			var controller=coreApp.getController(funcController);
			if(!controller.inited){controller.init();controller.inited=true};
			*/			
			var refController=self.refController;
			if(!refController){
				refController='';
			}

			//zzk 2017-4-10加入按钮配置
			var buttons=[{
				text: '确定',
				ref: 'ssOkBtn',
				iconCls: 'x-fa fa-check-square'

			}, {
				text: '取消',
				ref: 'ssCancelBtn',
				iconCls: 'x-fa fa-reply'
			}];
			if(configInfo.buttons)
				buttons=configInfo.buttons;

			var window=Ext.createWidget("mtfuncwindow",{
				title:funcTitle,	
				iconCls:'x-fa fa-bars',		
				items:{
					xtype:funcPanel,
					minWidth:null	//去掉mainLayout中的最小宽度
				},
				buttons: buttons,
				width:width,
				height:height,
				controller:refController,	//指定控制器，方便在调用处重写按钮事件； 这里暂时置空
				funcPanel:funcPanel,
				formPanel:formPanel,	//方便其他地方重写事件时调用
				dataField:dataField,
				gridField:gridField,
				listeners:{
					render:function(win){

						var basePanel=win.down("panel[xtype="+funcPanel+"]");
						var funCode=basePanel.funCode;
						var baseGrid=basePanel.down("basegrid");
						var baseProxy=baseGrid.getStore().getProxy();	//zzk修复，2017-4-10

						baseProxy.extraParams.whereSql = whereSql;					
						
						var old_wheresql=baseGrid.extParams.whereSql;	//从配置文件中获取

						if(!configInfo.showTbar){	//当此值不为true时，隐藏tbar
							//隐藏按钮
						
							
							/*隐藏其他无关的tbar工具栏*/
							var tbars=basePanel.query("toolbar[ref!=panelTopBar][dock=top]");
							for(var i in tbars){
								tbars[i].removeAll();
							}

							/*zzk：隐藏功能按钮*/
							var toolbar=baseGrid.down("toolbar[ref=panelTopBar]");
							if(toolbar){
								var buttons=toolbar.query("button[funCode=girdFuntionBtn]");
								for(var i in buttons){
									buttons[i].setVisible(false);
								}
							}else{
								toolbar=baseGrid.down("toolbar");
								if(toolbar)
									toolbar.removeAll();
							}

							/*zzk：隐藏操作列*/
							var actioncolumn=baseGrid.query("actioncolumn");
							for(var i in actioncolumn){
								actioncolumn[i].setVisible(false);
							}
						}

						var old_filter=baseGrid.extParams.filter;	//从配置文件中获取
					
						if (!Ext.isEmpty(filter)) {
							baseProxy.extraParams.filter = filter;
						}
						//baseGrid.getStore().load();

						if(!muiltSelect){
							//将表格设置单选
							var selModel=baseGrid.getSelectionModel();
							selModel.setSelectionMode("SINGLE");
						}
						var okBtn=win.down("button[ref=ssOkBtn]");
						okBtn.on("click",function(btn){
							//查询的OK事件
							var records=baseGrid.getSelectionModel().getSelection();
							if(records.length>0){
								//点击确定之后会得到选中的数据做处理
								if(muiltSelect){
									//因为是多选，将值用逗号隔开放入字段中
									Ext.each(dataField,function(f,index){
										var valueArray=new Array();
										Ext.each(records,function(r){
											valueArray.push(r.get(gridField[index]));
										});
										if(rec){
											rec.set(f,valueArray.join(","));
										}else{
											var bff=bf.findField(f);
											if(bff){
												bff.setValue(valueArray.join(","));
												}
										}	
									});
								//单选操作处理
								}else{
									var record=records[0];
									Ext.each(dataField,function(f,index){
										if(rec){
											rec.set(f,record.get(gridField[index]));
										}else{
											var bff=bf.findField(f);
											if(bff){
												bff.setValue(record.get(gridField[index]));
											}
										}	
									});
								}
								self.fireEvent('query',self, records);

								baseProxy.extraParams.whereSql =old_wheresql;
								baseProxy.extraParams.filter = old_filter;	//恢复原始参数 2017-2-14.ZZK
								win.close();
							}else{
								self.msgbox("请至少选择一条数据！");
							}
						});
						var calBtn=win.down("button[ref=ssCancelBtn]");
						if(calBtn){
							calBtn.on("click",function(btn){
								baseProxy.extraParams.whereSql =old_wheresql;
								baseProxy.extraParams.filter = old_filter;	//恢复原始参数 2017-2-14.ZZK
								win.close();							
							});
						}
					}
				}
			}).show();
			//隐藏按钮
			
		 }
	}
});
Ext.define("core.base.view.form.BaseTreeField",{
	//extend:"Ext.form.field.Trigger",
	extend:"Ext.form.field.Picker",
	alias: 'widget.basetreefield',
	mixins: {
			queryUtil:"core.util.QueryUtil"
	},	
	editable : false,
    //trigger1Cls: Ext.baseCSSPrefix + 'form-clear-trigger',    
    //trigger2Cls: Ext.baseCSSPrefix + 'form-search-trigger',
	initComponent: function(){
		var self=this;
		//增加两个事件。 查询前，查询后
		 //注：5.0版本后，此方法已被移除；  此代码：self.fireEvent('beforeclick', self)可以直接生效，不需要事先addEvents； zzk
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
		var ddCode=self.ddCode;
		var rootId=self.rootId;
		if(!rootId){
			rootId="ROOT";
		}
		var configInfo=self.configInfo;
		var fieldInfo=configInfo.fieldInfo;
		var url = configInfo.url;
		var fieldArray=fieldInfo.split(",");
		var dataField=fieldArray[0].split("~");
		var treeField=fieldArray[1].split("~");

		var windowWidth=500;
		var windowHeight=600
		if(configInfo.width){
			windowWidth=configInfo.width;
		}
		if(configInfo.height){
			windowHeight=configInfo.height;
		}

		var flag = self.fireEvent('beforequery', self);
		if(flag!=false){
			self.selTreeWin({
				model:self.model,
				title:self.fieldLabel,
				funcPanel:self.funcPanel,	//zzk：2017-2-13加入，用于辨别此窗口是由哪里打开的
				multiSelect:configInfo.multiSelect,
				haveButton:true,
				isEmpty:true,
				width:windowWidth,
				height:windowHeight,
				config:{
					url:url,
					params:{
						node:rootId,
						ddCode:ddCode,
						whereSql:configInfo.whereSql,
						orderSql:configInfo.orderSql,
						expanded:true
					}
				},
				renderTree:function(tree){
					if(self.renderer){
						var renderer=self.renderer;
						renderer(self,tree);
					}					
				},
				callback:function(win,records){					
					var bf,rec;//表格和表单，如果是表格的化则更新表格的字段值，如果是表单则更新表单
					if(self.ownerCt.xtype=='editor'){
						var grid = self.ownerCt.floatParent;
						rec = grid.getSelectionModel().getSelection()[0];
					}else{
						bf = self.up('form').getForm();
					}
					
					//点击确定之后会得到选中的数据做处理
					if(configInfo.multiSelect){
						//因为是多选，将值用逗号隔开放入字段中
						Ext.each(dataField,function(f,index){
							var valueArray=new Array();
							Ext.each(records,function(r){
								valueArray.push(r.get(treeField[index]));
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
								rec.set(f,record.get(treeField[index]));
							}else{
								var bff=bf.findField(f);							
								if(bff){
									bff.setValue(record.get(treeField[index]));
								}else{
									/*zzk 2017-4-5加入，在查询按钮中使用此值，并赋值*/
									var valueField=self.up('form').down("field[name="+f+"]");
									if(valueField)
										valueField.setValue(record.get(treeField[index]));
								}
							}	
						});
					}
					self.fireEvent('query',self, records);
				}
			});
		 }
	}
});
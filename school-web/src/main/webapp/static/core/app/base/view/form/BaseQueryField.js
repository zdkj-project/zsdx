Ext.define("core.base.view.form.BaseQueryField",{
	extend:"Ext.form.FieldContainer",
	alias: 'widget.basequeryfield',
	layout:"hbox",
	defaults: {
	    hideLabel: true,
	    xtype : 'textfield'
	},
	dataType:'datetime',		//数据类型，用于提交查询获取filtr的时候； sort、date、datetime、
	queryType:"textfield",		//实际的extjs组件别名
	operationType:'like',		//查询的方式  like、between、= < > != >= <=
	initComponent: function(){		
		var self=this;
		/*zzk: 2017-3-8 更新，将apply替换为applyIf*/

		var columnWidth=this.columnWidth;
		var queryType=this.queryType;
		var items=new Array();
		//声明下拉框的store
		
		/*
		var store=Ext.create("Ext.data.Store",{
			fields:['name','value']
		});
		
		var combobox=Ext.create("Ext.form.field.ComboBox",{
			hidden:true,	//隐藏
			queryMode: 'local',
			displayField: 'name',
			valueField: 'value',
			editable:false,
			name:this.name+"_type",
			width : 60,
			margin:this.config.margin,
			listeners:{
				change:function(cb,newValue,oldValue){
					//从区间切换成精确
					if(oldValue=="between" && newValue=="="){
						var fc=cb.up("basequeryfield[name="+self.name+"]");
						var start=fc.down("field[name="+self.name+"_start]");
						var end=fc.down("field[name="+self.name+"_end]");
						
						start.name=self.name+"_field";
						var startDom=start.getEl();
						start.setWidth(startDom.getWidth()*2);
						end.hide();

						//隐藏波浪线
						var	df=fc.down("field[name="+self.name+"_df]");
						if(df)
							df.hide();
					//从精确切换到区间
					}else if(oldValue=="=" && newValue=="between"){
						var fc=cb.up("basequeryfield[name="+self.name+"]");
						var start=fc.down("field[name="+self.name+"_field]");
						start.name=self.name+"_start";
						var startDom=start.getEl();
						start.setWidth(startDom.getWidth()/2);
						var end=fc.down("field[name="+self.name+"_end]");
						end.show();

						//显示波浪线
						var	df=fc.down("field[name="+self.name+"_df]");
						if(df)
							df.show();					
					}
				}
			}
		});
		*/

		if(queryType=="textfield"){	
			var obj={
				xtype:queryType,
				name:this.name+"_field"
			};
			if(this.config){
				obj=Ext.applyIf(obj,this.config);
			}

			if(!this.operationType)
				this.operationType='like';	//设置为模糊

			items.push(obj);
			/*
			store.loadData([{name:"模糊",value:"like"},{name:"精确",value:"="}]);
			combobox.setStore(store);
			combobox.setValue("like");
			items.push(combobox);
			*/
		}
		else if(queryType=="datetimefield"){//如果是时间字段
			var obj={
				xtype:"datetimefield",
				name:this.name+"_field",
				dateType:this.dataType
			};
			if(this.config){
				obj=Ext.applyIf(obj,this.config);		
			}
			if(!this.operationType)
				this.operationType='>=';	//= != > < >= <= 符合
			items.push(obj);
			/*
			store.loadData([{name:"区间",value:"between"},{name:"精确",value:"="}]);
			combobox.setStore(store);
			combobox.setValue("between");
			items.push(combobox);
			*/
			//columnWidth=1;
		}
		else if(queryType=="datefield"){//如果是日期字段
			var obj={
				xtype:"datefield",
				name:this.name+"_field",
				dataType:this.dataType
			};
			
			if(this.config){
				obj=Ext.applyIf(obj,this.config);
			}
			if(!this.operationType)
				this.operationType='>=';	//设置为大于等于或者小于等于之类
			items.push(obj);
			/*
			store.loadData([{name:"区间",value:"between"},{name:"精确",value:"="}]);
			combobox.setStore(store);
			combobox.setValue("between");
			items.push(combobox);
			*/
			//columnWidth=1;
		}
		else if(queryType=="numberfield"){
			var start={
				xtype:"numberfield",
				value:null,
				name:this.name+"_start"
			};
			var end={
				xtype:"numberfield",
				value:null,
				name:this.name+"_end"
			};
			if(this.config){
				start=Ext.applyIf(start,this.config);
				end=Ext.applyIf(end,this.config);
			}
			if(!this.operationType)
				this.operationType='between';	//设置为区间
			items.push(start);
			items.push({xtype: 'displayfield',  cls:'BaseQuery_DisplayField',  value: '~',name : this.name+'_df',width:10});
			items.push(end);
			
			/*
			store.loadData([{name:"区间",value:"between"},{name:"精确",value:"="}]);
			combobox.setStore(store);
			combobox.setValue("between");
			items.push(combobox);
			*/
			//columnWidth=1;
		}
		else{
			//columnWidth=.5;
			var obj={				
				xtype:queryType,
				name:this.name+"_field",
				dataType:this.dataType
			};
			
			if(this.config){
				obj=Ext.applyIf(obj,this.config);
			}
			this.operationType=null;	//其他组件

			items.push(obj);
		}



		self.items=items;

		self.columnWidth=columnWidth;
		
		this.callParent(arguments);
	}
});
Ext.define("core.util.SqlUtil",{
	/**
	 * 构建执行的Sql字符串
	 * @param {} updateArray
	 * @param {} modelName
	 * @param {} pkName
	 */
	getUpdateSql:function(updateArray,modelName,pkName){
		var datas=new Array();
		Ext.each(updateArray,function(obj){
			var pkValue="";
			var setArray=new Array();
			var fields=new Array();
			for(var f in obj){
				var value=Ext.value(obj[f],'');
				if(f==pkName){
					pkValue=obj[pkName];
				}else{
					fields.push(f);
					if(typeof(value)=="string"){
						value=value.replace("'","''");//因为sql语句执行的'有特殊意义，前面加一个单引号标识转义
						setArray.push(f+"='"+value+"'");
					}else if(typeof(value)=="int" || typeof(value)=="float"){
						setArray.push(f+"="+value);
					}else{
						setArray.push(f+"='"+value+"'");
					}
				}
			}
			datas.push("{pkValue:'"+pkValue+"',fields:'"+fields.join(",")+"',sql:\"update "+modelName+" set "+setArray+" where "+pkName+"='"+pkValue+"'\"}")
		});
		return "["+datas.join(",")+"]";
	},
	/**
	 * 得到查询的querySql
	 * @param {} queryPanel
	 */
	/*getQuerySql:function(queryPanel){
		var queryFields=queryPanel.query("basequeryfield");
		var querySql="";
		Ext.each(queryFields,function(queryField){
			var fieldName=queryField.name;
			var queryTypeField=queryField.down("combobox[name="+fieldName+"_type]");
			if(queryTypeField){
				//代表是系统查询组件
				//1.解析文本类型字段
				var type=queryTypeField.getValue();
				if(type=="like"){//模糊查询
					var valueField=queryField.down("field[name="+fieldName+"_field]");
					var value=valueField.getValue();
					if(value){
						querySql+=" and "+fieldName+" like '%"+value+"%'";
					}
				}else if(type=="="){//精确查询
					var valueField=queryField.down("field[name="+fieldName+"_field]");
					var value=valueField.getValue();
					if(value && valueField.xtype!="numberfield"){
						querySql+=" and "+fieldName+" = '"+value+"'";
					}else if(value && valueField.xtype=="numberfield"){
						querySql+=" and "+fieldName+" = "+value+"";

					}
				}else if(type=="between"){//区间查询
					var startField=queryField.down("field[name="+fieldName+"_start]");
					var endField=queryField.down("field[name="+fieldName+"_end]");
					var startValue=startField.getValue();
					var endValue=endField.getValue();
					if(startValue && endValue && startField.xtype!="numberfield"){
						querySql+=" and "+fieldName+" >= '"+startValue+"' and "+fieldName+" <='"+endValue+"'";
					}else if(startValue && endValue && startField.xtype=="numberfield"){
						querySql+=" and "+fieldName+" >= "+startValue+" and "+fieldName+" <="+endValue+"";
					}
				}
			}else{// 其他组件查询
				var valueField=queryField.down("field[name="+fieldName+"_field]");
				var value=valueField.getValue();
				if(value){
					querySql+=" and "+fieldName+"='"+value+"'";
				}
			}
		});
		return querySql;
	},*/
	getQuerySql:function(queryPanel){
		var queryFields=queryPanel.query("basequeryfield");
		var querySql="";
		Ext.each(queryFields,function(queryField){
			var fieldName=queryField.name;
			var type=queryField.operationType;

			if(type=="like"){//模糊查询
				var valueField=queryField.down("field[name="+fieldName+"_field]");
				var value=valueField.getValue();
				if(value){
					querySql+=" and "+fieldName+" like '%"+value+"%'";
				}
			}else if(type=="="||type=="!="||type==">="||type=="<="||type==">"||type=="<"){		//运算符号查询
				var valueField=queryField.down("field[name="+fieldName+"_field]");
				var value=valueField.getValue();
				if(value){
					if(valueField.xtype=="numberfield")
						querySql+=" and "+fieldName+type+value;
					else
						querySql+=" and "+fieldName+type+"'"+value+"'";		
				}
			}else if(type=="between"){//区间查询
				var startField=queryField.down("field[name="+fieldName+"_start]");
				var endField=queryField.down("field[name="+fieldName+"_end]");
				var startValue=startField.getValue();
				var endValue=endField.getValue();
				if(startValue && endValue && startField.xtype=="numberfield"){
					querySql+=" and "+fieldName+" >= "+startValue+" and "+fieldName+" <="+endValue;
				}else if(startValue && endValue ){
					querySql+=" and "+fieldName+" >= '"+startValue+"' and "+fieldName+" <='"+endValue+"'";
				}
			}else{// 其他组件查询
				var valueField=queryField.down("field[name="+fieldName+"_field]");
				var value=valueField.getValue();
				if(value){
					querySql+=" and "+fieldName+" = '"+value+"'";
				}
			}
		});
		return querySql;
	},
	/**
	 * 解析whereSql
	 * @param {} whereSql
	 * @param {} form
	 * @param {} rec
	 */
	parseWhereSql:function(whereSql,form,rec){
		while(whereSql.indexOf("#")>=0){
			var startIndex=whereSql.indexOf("#");
			var endIndex=whereSql.indexOf("#",startIndex+1);
			var fieldName=whereSql.substring(startIndex+1,endIndex);
			var value=null;
			if(form){
				var field=form.findField(fieldName);
				if(field){
					value=field.getValue();
				}
			}else{
				value=rec.get(fieldName);
			}
			whereSql=whereSql.replace("#"+fieldName+"#",value);
		}
		return whereSql;
	},
	getQureyFilter:function(queryPanel){

		var queryFields=queryPanel.query("basequeryfield");
		var querySql="";
		var filterFidlds = "";
		Ext.each(queryFields,function(queryField){
			var fieldName=queryField.name;
			var type=queryField.operationType;
			
			if(type=="like"){//模糊查询
				var valueField=queryField.down("field[name="+fieldName+"_field]");
				var value=valueField.getValue();
				if(value){
					filterFidlds += "{\"type\":\"string\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"\"},";				
				}
			}else if(type=="="||type=="!="||type==">="||type=="<="||type==">"||type=="<"){		//运算符号查询
		
				var valueField=queryField.down("field[name="+fieldName+"_field]");
				var value=valueField.getValue();
				//zzk 2017-4-12加入，更精确的来拼接过滤的参数				
				var fieldDataType=valueField.config.dataType;

				if(value){
					if(valueField.xtype=="numberfield"){
						if(fieldDataType &&fieldDataType=="short"){
							filterFidlds += "{\"type\":\"short\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\""+type+"\"},";
						}else{				
							filterFidlds += "{\"type\":\"numeric\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\""+type+"\"},";
						}
					}else{
						if ( valueField.xtype=="datefield"||valueField.xtype=="datetimefield") {						
							if(fieldDataType=="date")
								filterFidlds += "{\"type\":\"date\",\"value\":\"" + value  +"\",\"field\":\"" + fieldName+"\",\"comparison\":\""+type+"\"},";
							else  
								filterFidlds += "{\"type\":\"time\",\"value\":\"" + value  +"\",\"field\":\"" + fieldName+"\",\"comparison\":\""+type+"\"},";					
						}else {											
							filterFidlds += "{\"type\":\"string\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\""+type+"\"},";
						}
					}
				}

			}else if(type=="between"){//区间查询
				var startField=queryField.down("field[name="+fieldName+"_start]");
				var endField=queryField.down("field[name="+fieldName+"_end]");
				var startValue=startField.getValue();
				var endValue=endField.getValue();

				//zzk 2017-4-12加入，更精确的来拼接过滤的参数				
				var fieldDataType=startField.config.dataType;
				/*
				if(startValue && endValue && startField.xtype!="numberfield"){
					//这里没有什么用处了
					if (startField.xtype=="datetimefield" || startField.xtype=="datefield"){
						//querySql+=" and "+fieldName+" >= '"+startValue+"' and "+fieldName+" <='"+endValue+"'";
						filterFidlds += "{\"type\":\"date\",\"value\":\"" + new startValue+"\",\"field\":\"" + fieldName+"\",\"comparison\":\">=\"},";
						filterFidlds += "{\"type\":\"date\",\"value\":\"" + new endValue +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"<=\"},";
					} else {
						filterFidlds += "{\"type\":\"string\",\"value\":\"" + startValue +"\",\"field\":\"" + fieldName+"\",\"comparison\":\">=\"},";
						filterFidlds += "{\"type\":\"string\",\"value\":\"" + endValue +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"<=\"},";						
					}
				}else 
				*/
				if(startValue && endValue && startField.xtype=="numberfield"){
					if(fieldDataType &&fieldDataType=="short"){
						filterFidlds += "{\"type\":\"short\",\"value\":\"" + startValue +"\",\"field\":\"" + fieldName+"\",\"comparison\":\">=\"},";
						filterFidlds += "{\"type\":\"short\",\"value\":\"" + endValue +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"<=\"},";											
					}else{
						//querySql+=" and "+fieldName+" >= "+startValue+" and "+fieldName+" <="+endValue+"";
						filterFidlds += "{\"type\":\"numeric\",\"value\":\"" + startValue +"\",\"field\":\"" + fieldName+"\",\"comparison\":\">=\"},";
						filterFidlds += "{\"type\":\"numeric\",\"value\":\"" + endValue +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"<=\"},";						
					}
				}

			}else{// 其他组件查询
				var valueField=queryField.down("field[name="+fieldName+"_field]");
				var value=valueField.getValue();
				//zzk 2017-4-12加入，更精确的来拼接过滤的参数				
				var fieldDataType=valueField.config.dataType;
				if(fieldDataType){
					if(value!=null &&fieldDataType=="numberfield"){
						//querySql+=" and "+fieldName+" = "+value+"";
						filterFidlds += "{\"type\":\"numeric\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"=\"},";
					}else if(value!=null && fieldDataType=="short"){
						//querySql+=" and "+fieldName+" = '"+value+"'";
						filterFidlds += "{\"type\":\"short\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"=\"},";
					}else if(value){
						//querySql+=" and "+fieldName+" = '"+value+"'";
						filterFidlds += "{\"type\":\"string\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"=\"},";			
					}				
				}else{
					if(value!=null && valueField.xtype!="numberfield"){
						//querySql+=" and "+fieldName+" = '"+value+"'";
						filterFidlds += "{\"type\":\"string\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"=\"},";
					}else if(valueField.xtype=="numberfield"){
						//querySql+=" and "+fieldName+" = "+value+"";
						filterFidlds += "{\"type\":\"numeric\",\"value\":\"" + value +"\",\"field\":\"" + fieldName+"\",\"comparison\":\"=\"},";
					}
				}
			}
			
		});
		if (filterFidlds.length>0){
			filterFidlds  =  "[" + filterFidlds.substring(0,filterFidlds.length-1) + "]";
		}
		return filterFidlds;
	},	
});
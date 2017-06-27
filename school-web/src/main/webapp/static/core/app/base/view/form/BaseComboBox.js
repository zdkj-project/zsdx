Ext.define("core.base.view.form.BaseComboBox",{
	extend:"Ext.form.field.ComboBox",
	alias: 'widget.basecombobox',
	/*	没看出来改了什么重大地方，而且还显示不太正确
	fieldSubTpl: [
        '<input id="{id}" type="{type}" {inputAttrTpl}',
            ' size="1"',
            '<tpl if="name"> name="{name}"</tpl>',
            '<tpl if="value"> value="{value}"</tpl>',
            '<tpl if="placeholder"> placeholder="{placeholder}"</tpl>',
            '<tpl if="maxLength !== undefined"> maxlength="{maxLength}"</tpl>',
            '<tpl if="readOnly"> readonly="readonly"</tpl>',
            '<tpl if="disabled"> disabled="disabled"</tpl>',
            '<tpl if="tabIdx"> tabIndex="{tabIdx}"</tpl>',
            'style="padding:0 10px;<tpl if="fieldStyle">{fieldStyle}</tpl>"',
        ' class="{fieldCls} {typeCls} {editableCls}" autocomplete="off"/>',
        {
            disableFormats: true
        }
    ],*/
	queryMode: 'local',
	displayField: 'itemName',
	valueField: 'itemCode',
	trigger1Cls: Ext.baseCSSPrefix + 'form-clear-trigger',
    trigger2Cls: Ext.baseCSSPrefix + 'form-arrow-trigger',
//    trigger3Cls: Ext.baseCSSPrefix + 'form-search-trigger',
    editable:false,
    readOnly:false,
	initComponent: function(){
		var self=this;
		var ddCode=this.ddCode;
		var store=Ext.create("Ext.data.Store",{
			fields:factory.ModelFactory.getFields({modelName:"com.zd.school.plartform.baseset.model.BaseDicitem",excludes:""}),
			data:factory.DDCache.getItemByDDCode(ddCode)
		});
		//将数据字典数据赋值到组件属性上
		var dicData={};
		store.each(function(rec){
			dicData[rec.get(this.valueField)]=dicData[rec.get(this.displayField)];
		});
		this.store=store;
		self.dicData=dicData;
		self.onTrigger2Click = Ext.clone(self.onTrigger1Click);
		self.onTrigger1Click = function(){
			self.reset();//重置
//			me.setConfigInfo();
		}
		this.callParent(arguments);
	},
	/**
	 * 获取字典的项名称
	 * @param {} val
	 * @return {}
	 */
	getText:function(val){
		var self=this;
		return self.dic[val];
	}
});
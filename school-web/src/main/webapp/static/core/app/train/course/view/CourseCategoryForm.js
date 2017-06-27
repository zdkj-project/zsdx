Ext.define("core.train.course.view.CourseCategoryForm", {
	extend: "core.base.view.BaseForm",
	alias: "widget.train.coursecategoryform",

	layout: "form",
	autoHeight: true,
	frame: false,
	bodyPadding: '10 20',
	fieldDefaults: { // 统一设置表单字段默认属性
		labelSeparator: '：', // 分隔符
		msgTarget: 'qtip',
		labelAlign: "right",
		labelWidth: 120,
	},
	items: [{
		fieldLabel: '主键',
		name: "uuid",
		hidden: true
	}, {
		xtype: "textfield",
		fieldLabel: "上级分类Id",
		name: "parentCategory",
		hidden: true
	}, {
		beforeLabelTextTpl: comm.get("required"),
		xtype: "textfield",
		fieldLabel: "上级课程分类",
		name: "parentName",
		//readOnly: true
		xtype: "basetreefield",
		ddCode: "DEPTTREE",
		rootId: "ROOT",
		configInfo: {
			multiSelect: false,
			fieldInfo: "parentName~parentCategory~parentType,text~id",
			whereSql: "",
			orderSql: "",
			url: comm.get('baseUrl') + "/TrainCoursecategory/treelist",
		}
	}, {
		beforeLabelTextTpl: comm.get('required'),
		allowBlank: false,
		blankText: "课程分类名称不能为空",
		fieldLabel: '课程分类名称',
		name: "categoryName",
		maxLength: 36,
		emptyText: '请输入课程分类名称(最大36个字符)'
	}, {
		beforeLabelTextTpl: comm.get('required'),
		allowBlank: false,
		blankText: "课程分类编码不能为空",
		fieldLabel: '课程分类编码',
		name: "categoryCode",
		maxLength: 16,
		emptyText: '请输入课程分类编码(最大16个字符)'
	}, {
		beforeLabelTextTpl: comm.get("required"),
		fieldLabel: "排序号",
		xtype: "numberfield",
		name: "orderIndex",
		allowBlank: false,
		emptyText: "同级别的分类的显示顺序",
		blankText: "排序号不能为空"
	}, {
		beforeLabelTextTpl: '',
		blankText: "课程分类说明不能为空",
		fieldLabel: '课程分类说明',
		name: "categoryDesc",
		maxLength: 256,
		emptyText: '请输入课程分类说明(最大256个字符)',
		xtype: "textareafield",
	}]
});

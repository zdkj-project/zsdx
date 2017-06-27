Ext.define("core.train.coursetype.view.CourseCategoryForm", {
	extend: "core.base.view.BaseForm",
	alias: "widget.coursetype.coursecategoryform",

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
		name: "parentNode",
		hidden:true
	}, {
/*		beforeLabelTextTpl: comm.get("required"),
		xtype: "textfield",
		fieldLabel: "上级课程分类",
		name: "parentName",
		readOnly: true*/

            beforeLabelTextTpl: comm.get("required"),
            //columnWidth: 0.5,
            fieldLabel: "上级分类",
            name: "parentName",
            xtype: "basetreefield",
            rootId: "ROOT",
            configInfo: {
                width: 500,
                height: 600,
                multiSelect: false,
                fieldInfo: "parentName~parentNode,text~id",
                whereSql: " and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex",
                url: comm.get('baseUrl') + "/TrainCoursecategory/treelist",
            },
            allowBlank: false,
            emptyText: "请选择课程分类"

	}, {
		beforeLabelTextTpl: comm.get('required'),
		allowBlank: false,
		blankText: "课程分类名称不能为空",
		fieldLabel: '课程分类名称',
		name: "nodeText",
		maxLength: 36,
		emptyText: '请输入课程分类名称(最大36个字符)'
	}/*, {
		beforeLabelTextTpl: comm.get('required'),
		allowBlank: false,
		blankText: "排序号不能为空",
		fieldLabel: '排序号',
		name: "orderIndex",
		maxLength: 16,
		emptyText: '请输入排序序号'
	}*/, {
		beforeLabelTextTpl: '',	
		blankText: "课程分类说明不能为空",
		fieldLabel: '课程分类说明',
		name: "categoryDesc",
		maxLength: 256,
		emptyText: '请输入课程分类说明(最大256个字符)',
		xtype: "textareafield",
	}]
});
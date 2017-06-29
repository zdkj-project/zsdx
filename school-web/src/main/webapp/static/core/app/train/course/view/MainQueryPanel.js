Ext.define("core.train.course.view.MainQueryPanel", {
	extend: "core.base.view.BaseQueryForm",
	alias: "widget.course.mainquerypanel",
	layout: "form",
	frame: false,
	height: 50,
    width:'100%',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        labelAlign: "right",
        labelWidth: 80,
        width: '100%'
    },
	items: [{
        xtype: "container",
        layout: "column",
        items: [{
            columnWidth: 0.2,
            xtype: "basequeryfield",
            name: "courseCode",
            fieldLabel: "课程编码",
            queryType: "textfield"
        }, {
             columnWidth: 0.2,
            xtype: "basequeryfield",
            name: "courseName",
            fieldLabel: "课程名称",
            queryType: "textfield"
        }, {
            columnWidth: 0.2,
            xtype: "basequeryfield",
            fieldLabel: "主讲教师",
            name: "mainTeacherName",
            queryType: "textfield"
        }, {
            //xtype: "basequeryfield",
            columnWidth: 0.2,
            xtype: "textfield",
            fieldLabel: "所属分类ID",
            name: "categoryId",
            hidden: true
        },{
            columnWidth: 0.2,
            labelAlign: "right",
            fieldLabel: "所属类别",
            name: "categoryName",
            xtype: "basetreefield",
            rootId: "ROOT",
            configInfo: {
                width:600,
                height:650,
                multiSelect: false,
                fieldInfo: "categoryName~categoryId,text~id",
                whereSql: " and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex asc ",
                url:comm.get('baseUrl') + "/TrainCoursecategory/treelist"
            }
        }, {
            columnWidth: 0.2,
             xtype: "basequeryfield",
            name: "teachType",
            fieldLabel: "教学形式",
            queryType: "basecombobox",
            config: {
                ddCode: "TEACHTYPE"
            }
        },{
            xtype: "container",
            layout: "column",
            items:[{
                xtype: "button",
                align: "right",
                text: '搜 索',
                ref: 'gridSearchFormOk',
                iconCls: 'x-fa fa-search',
                margin:'0 0 0 10'
            }, {
                xtype: "button",
                align: "right",
                text: '重 置',
                ref: 'gridSearchFormReset',
                iconCls: 'x-fa fa-undo',
                margin:'0 0 0 10'
            }]
        }]
    }]
});
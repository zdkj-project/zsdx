Ext.define("core.oa.meeting.meetinginfo.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.meetinginfo.detailform",
    xtype: 'meetinginfodeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '10 25 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    items: [{
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "会议主题不能为空",
            fieldLabel: "会议主题",
            columnWidth: 0.49,
            name: "meetingTitle",
            xtype: "textfield",
            emptyText: "请输入会议主题",
            maxLength: 50,
            maxLengthText: "最多50个字符,汉字占2个字符",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "会议名称不能为空",
            fieldLabel: "会议名称",
            columnWidth: 0.49,
            name: "meetingName",
            xtype: "textfield",
            emptyText: "请输入会议名称",
            maxLength: 50,
            maxLengthText: "最多50个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "会议类型不能为空",
            fieldLabel: "会议类型",
            columnWidth: 0.49,
            name: "meetingCategory",
            xtype: "basecombobox",
            ddCode: "MEETINGCATEGORY",
            emptyText: "请选择会议类型"
        }, {
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: "是否考勤",
            columnWidth: 0.19,
            name: "needChecking",
            xtype: "checkbox",
            boxLabel: "考勤",
            inputValue: "1",
            checked: true,
            listeners: {
                change: function (field, newValue, oldValue) {
                    var currentForm = field.up("baseform[xtype=meetinginfo.detailform]");
                    var checkoutFields = currentForm.query("field[ref=checkoutField]");
                    if (newValue == true) {
                        for (var i = 0; i < checkoutFields.length; i++) {
                            checkoutFields[i].setVisible(true);
                        }
                    } else {
                        for (var i = 0; i < checkoutFields.length; i++) {
                            checkoutFields[i].setVisible(false);
                        }
                    }
                }
            }
        }, {
            fieldLabel: "考勤规则ID",
            name: "checkruleId",
            xtype: "textfield",
            hidden: true
        }, {
            ref: 'checkoutField',
            beforeLabelTextTpl: "",
            allowBlank: true,
            blankText: "考勤规则不能为空",
            columnWidth: 0.3,
            xtype: "basefuncfield",
            refController: "meetinginfo.otherController", //该功能主控制器，这里重新指定为当前视图的控制器了
            funcPanel: "checkrule.mainlayout", //该功能显示的主视图
            formPanel: "meetinginfo.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "考勤规则选择", //查询窗口的标题
            configInfo: {
                width: 1200,
                height: 650,
                fieldInfo: "checkruleId~checkruleName,uuid~ruleName",
                whereSql: " and isDelete='0' and startUsing=1 ",
                orderSql: " order by createTime DESC ",
                muiltSelect: false //是否多选
            },
            fieldLabel: "考勤规则",
            emptyText: "请选择考勤规则",
            name: "checkruleName"
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "开始时间不能为空",
            fieldLabel: "开始时间",
            columnWidth: 0.49,
            name: "beginTime",
            xtype: "datetimefield",
            dateType: 'datetime',
            format: "Y-m-d H:i:s",
            emptyText: "请选择开始时间"
        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "结束时间不能为空",
            fieldLabel: "结束时间",
            columnWidth: 0.49,
            name: "endTime",
            xtype: "datetimefield",
            dateType: 'datetime',
            format: "Y-m-d H:i:s",
            emptyText: "请选择结束时间"
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "主持人ID",
            name: "emceeId",
            xtype: "textfield",
            hidden: true
        }, {
            columnWidth: 0.49,
            xtype: "basefuncfield",
            refController: "pubselect.selectusercontroller",
            funcPanel: "pubselect.selectuserlayout", //该功能显示的主视图
            funcGrid: "pubselect.isselectusergrid", //该功能显示的主视图
            formPanel: "meetinginfo.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "主持人选择", //查询窗口的标题
            configInfo: {
                width: 1200,
                height: 800,
                fieldInfo: "emceeId~emcee,uuid~xm",
                whereSql: " and isDelete='0' ",
                orderSql: " order by createTime DESC ",
                muiltSelect: false //是否多选
            },
            fieldLabel: '主持人',
            emptyText: "请选择主持人",
            name: "emcee"

        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "会议地点不能为空",
            columnWidth: 0.49,
            xtype: "basefuncfield",
            // refController: "meetinginfo.otherController", //该功能主控制器，这里重新指定为当前视图的控制器了
            refController: "pubselect.selectroomcontroller",
            funcPanel: "pubselect.selectroomlayout", //该功能显示的主视图
            funcGrid: "pubselect.isselectroomgrid", //该功能显示的主视图
            formPanel: "meetinginfo.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "会议地点选择", //查询窗口的标题
            configInfo: {
                width: 1200,
                height: 800,
                fieldInfo: "roomId~roomName,uuid~roomName",
                whereSql: " and isDelete='0' ",
                orderSql: " order by areaUpName, areaName,orderIndex asc",
                muiltSelect: false //是否多选
            },
            fieldLabel: "会议地点",
            emptyText: "请选择会议地点",
            name: "roomName"

        }, {
            fieldLabel: "房间ID",
            // columnWidth: 0.5,
            name: "roomId",
            xtype: "textfield",
            emptyText: "请输入房间ID",
            maxLength: 36,
            maxLengthText: "最多36个字符,汉字占2个字符",
            hidden: true
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: " 会议人员ID",
            name: "mettingEmpid",
            xtype: "textfield",
            hidden: true
        }, {
            columnWidth: 0.49,
            xtype: "basefuncfield",
            refController: "pubselect.selectusercontroller",
            funcPanel: "pubselect.selectuserlayout", //该功能显示的主视图
            funcGrid: "pubselect.isselectusergrid", //该功能显示的主视图
            formPanel: "meetinginfo.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "参会人员", //查询窗口的标题
            configInfo: {
                width: 1200,
                height: 800,
                fieldInfo: "mettingEmpid~mettingEmpname,uuid~xm",
                whereSql: " and isDelete='0' ",
                orderSql: " order by deptName,jobName,xm DESC ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '参会人员',
            name: "mettingEmpname",
            allowBlank: true
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "会议内容",
            columnWidth: 0.98,
            name: "meetingContent",
            xtype: "ueditor",
            height: 200,
            listeners: {
                'change': function () {
                    var me = this;
                    me.isChanged = true;
                }
            }
        }]
    }]
});
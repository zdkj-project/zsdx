Ext.define("core.oa.meeting.checkresult.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.checkresult.detailform",
    xtype: 'checkresultdeailform',
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
        },{
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
            emptyText: "请选择会议类型",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: "是否考勤",
            columnWidth: 0.49,
            name: "needChecking",
            xtype: "checkbox",
            boxLabel: "考勤",
            inputValue:"1",
            checked: true,
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
            emptyText: "请选择开始时间",
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
            emptyText: "请选择结束时间",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "主持人不能为空",
            fieldLabel: "主持人",
            columnWidth: 0.49,
            name: "emcee",
            xtype: "textfield",
            emptyText: "请输入主持人",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "会议地点不能为空",
            fieldLabel: "会议地点",
            columnWidth: 0.49,
            name: "roomName",
            xtype: "textfield",
            emptyText: "请输入会议地点",
            maxLength: 32,
            maxLengthText: "最多32个字符,汉字占2个字符",
        }, /*{
            fieldLabel: "房间ID",
           // columnWidth: 0.5,
            name: "roomId",
            xtype: "textfield",
            emptyText: "请输入房间ID",
            maxLength: 36,
            maxLengthText: "最多36个字符,汉字占2个字符",
            hidden: true
        }*/]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: " 会议人员ID",
            name: "mettingEmpid",
            xtype: "textfield",
            hidden:true
        }, {

            columnWidth: 0.49,
            xtype: "basefuncfield",
            //funcController: "core.systemset.jobinfo.controller.jobinfoController", //该属性现在不需要了
            funcPanel: "meetinginfo.selectsysuser.mainlayout", //该功能显示的主视图
            refController:'meetinginfo.otherController',             //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
            formPanel:"meetinginfo.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "参会人员", //查询窗口的标题
            configInfo: {
                width:1200,
                height:650,
                fieldInfo: "mettingEmpid~mettingEmpname,uuid~xm",
                whereSql: " and isDelete='0' ",
                orderSql: "",
                muiltSelect: true //是否多选
            },
            fieldLabel: '参会人员',
            name: "mettingEmpname",
            allowBlank: true,
        }]
    },{
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
                'change': function() {
                    var me = this;
                    me.isChanged = true;
                }
            }
        }]
    }]
});
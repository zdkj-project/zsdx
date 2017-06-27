Ext.define("core.oa.meeting.checkresult.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.checkresult.detailform",
    xtype: 'checkresultdeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '0 10 10 0',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
        labelAlign: "right"
    },
    items: [{
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
        beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "会议主题不能为空",		
		fieldLabel: "会议主题",
		columnWidth: 0.5,
        name: "meetingTitle",
            xtype: "textfield",
	
		emptyText: "请输入会议主题",
        maxLength:36,
        maxLengthText:"最多36个字符,汉字占2个字符",	
    }
,{ 		
		fieldLabel: "是否考勤 ",
        columnWidth: 0.5,
        name: "needChecking",
            xtype: "checkbox",
			boxLabel:"0",
	
		emptyText: "请输入是否考勤 ",
        maxLength:2,
        maxLengthText:"最多2个字符,汉字占2个字符",
		}]
	},{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
        		
		fieldLabel: "会议名称",
		columnWidth: 0.5,
        name: "meetingName",
            xtype: "textfield",
	
		emptyText: "请输入会议名称",
        maxLength:36,
        maxLengthText:"最多36个字符,汉字占2个字符",	
    }
,{ beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "开始时间不能为空",		
		fieldLabel: "开始时间",
        columnWidth: 0.5,
        name: "beginTime",
            xtype: "datetimefield",
            dateType: 'datetime',
            format: "Y-m-d H:i:s",
	
		emptyText: "请输入开始时间",
        maxLength:8,
        maxLengthText:"最多8个字符,汉字占2个字符",
		}]
	},{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
        beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "结束时间不能为空",		
		fieldLabel: "结束时间",
		columnWidth: 0.5,
        name: "endTime",
            xtype: "datetimefield",
            dateType: 'datetime',
            format: "Y-m-d H:i:s",
	
		emptyText: "请输入结束时间",
        maxLength:8,
        maxLengthText:"最多8个字符,汉字占2个字符",	
    }
,{ beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "主持人不能为空",		
		fieldLabel: "主持人",
        columnWidth: 0.5,
        name: "emcee",
            xtype: "textfield",
	
		emptyText: "请输入主持人",
        maxLength:16,
        maxLengthText:"最多16个字符,汉字占2个字符",
		}]
	},{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
        beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "会议地点不能为空",		
		fieldLabel: "会议地点",
		columnWidth: 0.5,
        name: "roomName",
            xtype: "textfield",
	
		emptyText: "请输入会议地点",
        maxLength:32,
        maxLengthText:"最多32个字符,汉字占2个字符",	
    }
,{ beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "房间ID不能为空",		
		fieldLabel: "房间ID",
        columnWidth: 0.5,
        name: "roomId",
            xtype: "textfield",
	
		emptyText: "请输入房间ID",
        maxLength:36,
        maxLengthText:"最多36个字符,汉字占2个字符",
		}]
	},{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
        		
		fieldLabel: "考勤结果",
		columnWidth: 0.5,
        name: "attendResult",
            xtype: "textfield",
	
		emptyText: "请输入考勤结果",
        maxLength:16,
        maxLengthText:"最多16个字符,汉字占2个字符",	
    }
,{ beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: " 会议人员ID不能为空",		
		fieldLabel: " 会议人员ID",
        columnWidth: 0.5,
        name: "mettingEmpid",
            xtype: "textfield",
	
		emptyText: "请输入 会议人员ID",
        maxLength:2048,
        maxLengthText:"最多2048个字符,汉字占2个字符",
		}]
	},{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
        beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "会议人员姓名不能为空",		
		fieldLabel: "会议人员姓名",
		columnWidth: 0.5,
        name: "meetingEmpnmae",
            xtype: "textfield",
	
		emptyText: "请输入会议人员姓名",
        maxLength:1024,
        maxLengthText:"最多1024个字符,汉字占2个字符",	
    }
,{ beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "会议类型不能为空",		
		fieldLabel: "会议类型",
        columnWidth: 0.5,
        name: "meetingCategory",
       xtype: "basecombobox",
       ddCode: " MEETINGCATEGORY",
	
		emptyText: "请输入会议类型",
        maxLength:16,
        maxLengthText:"最多16个字符,汉字占2个字符",
		}]
	},{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
		items:[{
        beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "会议状态不能为空",		
		fieldLabel: "会议状态",
		columnWidth: 0.5,
        name: "meetingState",
       xtype: "basecombobox",
       ddCode: "MEETINGSTATE",
	
		emptyText: "请输入会议状态",
        maxLength:2,
        maxLengthText:"最多2个字符,汉字占2个字符",	
    }
,{ beforeLabelTextTpl: comm.get("required"),
		allowBlank: false,
		blankText: "会议内容不能为空",		
		fieldLabel: "会议内容",
        columnWidth: 0.5,
        name: "meetingContent",
            xtype: "textfield",
	
		emptyText: "请输入会议内容",
        maxLength:1024,
        maxLengthText:"最多1024个字符,汉字占2个字符",
		}]
	}]
});


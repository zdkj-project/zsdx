    /**
     * Created by lhy on 2017-07-17.
     */
    Ext.define('core.oa.meeting.checkresult.view.DetailPanel', {
        extend: 'Ext.Container',
        alias: "widget.checkresult.DetailPanel",
        layout: "form", //从上往下布局
        autoHeight: true,
        frame: false,
        style: {
            border: '1px solid #ddd'
        },
        fieldDefaults: { // 统一设置表单字段默认属性
            labelSeparator: "：", // 分隔符
            msgTarget: "qtip",
            labelWidth: 100,
            labelAlign: "right"
        },
        scrollable:true,
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
                '<div class="trainClass_title">考勤详情：</div>',
                '<ul>',
                    '<li style="width:30%">会议主题：{meetingTitle}</li>',
                    '<li style="width:30%">会议名称：{meetingName}</li>',
                    '<li style="width:30%">会议类型：{meetingCategory}</li>',
                    '<li style="width:30%">是否考勤：<tpl if="needChecking == 1">需要考勤<tpl else>不考勤</tpl></li>',
                    '<li style="width:30%">开始时间：{beginTime}</li>',
                    '<li style="width:30%">结束时间：{endTime}</li>',
                    '<li style="width:30%">主持人：{emcee}</li>',
                    '<li style="width:30%">会议地点：{roomName}</li>',
                    '<li style="width:30%">参会人员:{mettingEmpname}</li>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>',
            '<div class="trainClass_classInfo">',
                '<div class="trainClass_title">会议内容：</div>',
                '<ul>',
                  '<tpl if="!meetingContent">',
                        '<span>请输入会议内容!</span>',
                    '<tpl else>',
                        '<span>{meetingContent}</span>',
                    '</tpl>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>'
         ),
        data:{ }
    });
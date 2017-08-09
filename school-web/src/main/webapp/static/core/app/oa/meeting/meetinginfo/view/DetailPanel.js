/**
 * Created by luoyibo on 2017-06-06.
 */
Ext.define('core.oa.meeting.meetinginfo.view.DetailPanel', {
    extend: 'Ext.Container',
    alias: "widget.meetinginfo.DetailPanel",
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
    scrollable: true,
    width:'100%',
    items: [{
        xtype:'container',
        ref:'meetingInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
            '<div class="trainClass_title">会议详情：</div>',
            '<ul>',
            '<li style="width:30%">会议主题：{meetingTitle}</li>',
            '<li style="width:30%">会议名称：{meetingName}</li>',
            '<li style="width:30%">会议类型：{meetingCategory}</li>',
            '<li style="width:30%">是否考勤：{needChecking}</li>',
            '<li style="width:30%">考勤规则：{checkruleName}</li>',
            '<li style="width:30%">开始时间：{beginTime}</li>',
            '<li style="width:30%">结束时间：{endTime}</li>',
            '<li style="width:30%">主持人：{emcee}</li>',
            '<li style="width:30%">会议地点：{roomName}</li>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>',
            '<div class="trainClass_classInfo">',
            '<div class="trainClass_title">会议内容：</div>',
            '<ul>',
            '<tpl if="!meetingContent">',
            '<span>暂无会议内容!</span>',
            '<tpl else>',
            '<span>{meetingContent}</span>',
            '</tpl>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data: {}
    },{
        xtype:'container',
        ref:"meetingUsers",
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
            '<div class="trainClass_title">参会人员：</div>',
            '<ul class="trainClass_gridUl trainClassTrainee_gridUl">',
            '<li><span style="width:5%">序号</span><span style="width:10%" data-align="center">姓名</span>' +
            '<span style="width:10%" data-align="center">性别</span><span style="width:15%" data-align="center">部门</span>' +
            '<span style="width:10%" data-align="center">岗位</span><span style="width:10%" data-align="center">签到时间</span>' +
            '<span style="width:10%" data-align="center">签退时间</span><span style="width:10%" data-align="center">考勤结果</span>' +
            '<span style="width:15%" data-align="center">结果说明</span></li>',
            '{% if (values.length == 0) %}',
            '<li style="width:100%;font-size: 20px;font-weight: 400;text-align: center;line-height: 100px;">此指标暂无评价标准...</li>',
            '{% if (values.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
            '<tpl for="rows">',
            '<li><span style="width:5%">{[xindex]}</span><span style="width:10%" data-align="center">{xm}</span>' +
            '<span style="width:10%" data-align="center">{xbm}</span><span style="width:15%" data-align="center">{deptName}</span>' +
            '<span style="width:10%" data-align="center">{jobName}</span><span style="width:10%" data-align="center">{incardTime}</span>' +
            '<span style="width:10%" data-align="center">{outcardTime}</span>' +
            '<tpl if="attendResult==1">'+
            '<span style="width:10%" data-align="center">正常</span>' +
            '<tpl elseif="attendResult==2">'+
            '<span style="width:10%" data-align="center">迟到</span>' +
            '<tpl elseif="attendResult==3">'+
            '<span style="width:10%" data-align="center">早退</span>' +
            '<tpl elseif="attendResult==4">'+
            '<span style="width:10%" data-align="center">缺勤</span>' +
            '<tpl elseif="attendResult==5">'+
            '<span style="width:10%" data-align="center">迟到早退</span>' +
            '<tpl else >'+
            '<span style="width:10%" data-align="center"></span>' +
            '</tpl>' +
            '<span style="width:15%" data-align="center">{resultDesc}</span></li>',
            '</tpl>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data:{  }
    }]
});
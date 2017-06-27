Ext.define("core.train.alleval.view.CourseRankHtml", {
    extend: "Ext.Container",
    alias: "widget.alleval.courserankhtml",

    //bodyPadding: '0 10 10 0',
    margin: '0 0 0 10',
    scrollable: true,
    width: '100%',
    items: [{
        xtype: 'container',
        ref: 'classInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
            '<div class="trainClass_title">班级基本信息：</div>',
            '<ul>' +
            '<li>班级名称：{className}</li>',
            '<li>班主任：{bzrName}</li>',
            '<li>联系人电话：{contactPhone}</li>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data: {}
    }, {
        xtype: 'container',
        ref: 'classCourseRank',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
            '<div class="trainClass_title">班级课程评价排名：</div>',
            '<ul class="trainClass_gridUl">',
            '<li><span style="width:5%">序号</span><span style="width:30%" data-align="center">课程名称</span>',
            '<span style="width:10%" data-align="center">课程类型</span>',
            '<span style="width:20%" data-align="center">上课教师</span>',
            '<span style="width:10%" data-align="center">很满意度</span>',
            '<span style="width:10%" data-align="center">满意度</span>',
            '<span style="width:10%" data-align="center">很满意排名</span>',
            '</li>',
            '<tpl for="rows">',
            '<li><span style="width:5%">{[xindex]}</span><span style="width: 30%;text-align:left;" >{courseName}</span>',
            '<span style="width:10%; text-align:left;">{teachTypeName}</span>',
            '<span style="width:20%; text-align:left;">{teacherName}</span>',
            '<span style="width:10%; text-align:left;">{verySatisfaction}</span>',
            '<span style="width:10%; text-align:left;">{satisfaction}</span>',
            '<span style="width:10%; text-align:left;">{ranking}</span>',
            '</li>',
            '</tpl>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data: {}
    }]
});

    /**
     * Created by luoyibo on 2017-06-06.
     */
    Ext.define('core.train.course.view.CourseDescPanel', {
        extend: 'Ext.Container',
        alias: "widget.course.coursedescpanel",
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
                '<div class="trainClass_title">课程基本信息：</div>',
                '<ul>',
                    '<li style="width:30%">课程名称：{courseName}</li>',
                    '<li style="width:30%">课程分类：{categoryName}</li>',
                    '<li style="width:30%">课程编号：{courseCode}</li>',
                    '<li style="width:30%">主讲老师：{mainTeacherName}</li>',
                    '<li style="width:30%">联系电话：{mobilePhone}</li>',
                    '<li style="width:30%">教学类型：{teachTypeName}</li>',
                    '<li style="width:30%">学分：{credits}</li>',
                    '<li style="width:30%">课程时长：{periodTime}</li>',
                    '<li style="width:30%">授课模式：',
                        '<tpl if="courseMode==2">',
                            '团队模式',
                        '<tpl else>',
                            '单一模式',
                        '</tpl>',
                     '</li>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>',
            '<div class="trainClass_classInfo">',
                '<div class="trainClass_title">课程简介：</div>',
                '<ul>',
                  '<tpl if="!courseDesc">',
                        '<span>暂无课程简介!</span>',
                    '<tpl else>',
                        '<span>{courseDesc}</span>',
                    '</tpl>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>'
         ),
        data:{ }
    });


 
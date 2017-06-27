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
        '<div class="teacherDetailSlideDiv" style="	width: 850px; height: 440px;padding: 0 10px;">',
        '{courseDesc}',
        '</div>'
    ),
    data:{  }
});
/**
 * Created by luoyibo on 2017-06-07.
 */
Ext.define('core.train.course.view.CourseEvalPanel', {
    extend: "core.base.view.BaseGrid",
    alias: "widget.course.courseevalpanel",
    frame: false,
    columnLines: false,
    style: {
        border: '1px solid #ddd'
    },
    dataUrl: comm.get("baseUrl") + "/TrainClassschedule/listCourseEval", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainCourseEval", //对应的数据模型
    al: true,
    panelButtomBar: false,
    panelTopBar: false,
    /** 排序字段定义 */
    defSort: [{
        property: "categoryOrderindex",
        direction: "ASC"
    }, {
        property: "orderIndex", //字段名
        direction: "ASC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            //flex:0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 200,
            text: "班级类型",
            dataIndex: "classCategory",
            align: 'left'
        }, {
            flex: 1,
            text: "班级名称",
            dataIndex: "className",
            align: 'left',
        }, {
            width: 200,
            text: "上课日期",
            dataIndex: "courseDate",
            align: 'left',
        }, {
            width: 200,
            text: "上课时间",
            dataIndex: "courseTime",
            align: 'left',
        }, {
            text: '评价情况',
            columns: [{
                width: 100,
                text: "很满意度(%)",
                dataIndex: "verySatisfaction",
                align: 'left'
            }, {
                width: 100,
                text: "满意度(%)",
                dataIndex: "satisfaction",
                align: 'left'
            }, {
                width: 80,
                text: "排名",
                dataIndex: "ranking",
                align: 'left'
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
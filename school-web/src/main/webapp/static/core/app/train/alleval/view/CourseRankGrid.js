Ext.define("core.train.alleval.view.CourseRankGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.alleval.courserankgrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassschedule/listClassEvalCourse",
    model: "com.zd.school.jw.train.model.vo.TrainClassCourseEval", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: false,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: false,
    /** 排序字段定义 */
    defSort: [],
    /** 扩展参数 */
    extParams: {
        orderSql: " order by ranking asc "
        //查询的过滤字段
    },
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 100,
            text: "班级类型",
            dataIndex: "classCategory",
            columnType: "basecombobox", //列类型
            ddCode: "ZXXBJLX" //字典代码
        }, {
            width:200,
            text: "班级名称",
            dataIndex: "className"
        }, {
            flex: 1,
            minWidth: 200,
            text: "课程名称",
            dataIndex: "courseName"
        }, {
            width: 120,
            text: "课程类型",
            dataIndex: "teachTypeName"
        }, {
            width: 120,
            text: "上课教师",
            dataIndex: "teacherName",
            renderer: function (value, metaData) {
                var title = "上课教师";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
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
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 200,
            fixed: true,
            items: [{
                text: '详情',
                style: 'font-size:12px;',
                tooltip: '详情',
                ref: 'gridTraniee',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('courseRankGridDetail_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'detail'
                    });
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
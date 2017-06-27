/**
 * Created by luoyibo on 2017-06-09.
 * 教师上课记录列表
 */
Ext.define('core.train.teacher.view.TeachingGrid', {
    extend: "core.base.view.BaseGrid",
    alias: "widget.teacher.teachingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassschedule/listCourseEval", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainCourseinfo", //对应的数据模型
    al: false,
    style: {
        border: '1px solid #ddd'
    },
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
    defSort: [{
        property: "createTime", //字段名
        direction: "DESC" //升降序
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
            //align:'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 120,
            text: "班级类型",
            dataIndex: "classCategory",
            align: 'left'
        }, {
            flex: 1,
            text: "班级名称",
            dataIndex: "className",
            align: 'left'
        }, {
            width: 300,
            text: "课程名称",
            dataIndex: "courseName",
            align: 'left'
        }, {
            width: 150,
            text: "上课日期",
            dataIndex: "courseDate",
            align: 'left'
        }, {
            width: 120,
            text: "上课时间",
            dataIndex: "courseTime",
            align: 'left'
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
        }/*,{
         xtype:'actiontextcolumn',
         text: "评价情况",
         width:100,
         fixed:true,
         items: [{
         text:'评价情况',
         style:'font-size:12px;',
         tooltip: '评价情况',
         ref: 'gridDetail',
         handler: function(view, rowIndex, colIndex, item) {
         var rec = view.getStore().getAt(rowIndex);
         this.fireEvent('gridEvalClick', {
         view:view.grid,
         record: rec
         });
         }
         }]
         }*/]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
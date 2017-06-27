Ext.define("core.train.class.view.SelectCourseGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.class.coursegrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassschedule/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClassschedule", //对应的数据模型
    al:true,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:null,
    /**
     * 高级查询面板
     */
    panelButtomBar: null,

    tbar:[{
        xtype: 'button',
        text: '添加',
        ref: 'gridAdd',
        funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
        iconCls: 'x-fa fa-plus-circle'
    }, {
        xtype: 'button',
        text: '导入课程信息',
        ref: 'gridImport',
        funCode: 'girdFuntionBtn',
        iconCls: 'x-fa fa-clipboard'
    }],
    /** 排序字段定义 */
    //defSort: [{
    //    property: "salaryitemType", //字段名
    //    direction: "asc" //升降序
    //}],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    columns: {
        defaults: {
            //align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 60,
            text: '序号',
            align: 'center'
        }, {
            width:150,
            text: "所属班级",
            dataIndex: "className",
        },{
            flex: 1,
            minWidth:150,
            text: "课程名称",
            dataIndex: "courseName"
        }, {
            width:150,
            text: "开始日期",
            dataIndex: "beginTime",            
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "开始日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i:s')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            width:150,
            text: "结束日期",
            dataIndex: "endTime",            
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "结束日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i:s')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        },{
            width:150,
            text: "讲师",
            dataIndex: "mainTeacherName"          
        },{
            flex: 1,
            minWidth:150,
            text: "授课地点",
            dataIndex: "scheduleAddress",
            renderer: function(value, metaData) {
                var title = "授课地点";            
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        },{
            width:120,
            text: "授课模式",
            dataIndex: "courseMode",
            columnType: "basecombobox", //列类型
            ddCode: "COURSEMODE" //字典代码  
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 60,
            fixed: true,
            items: [ {
                text:'删除',  
                style:'font-size:12px;',  
                tooltip: '删除',
                ref: 'gridDelete',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
Ext.define("core.train.dinnertotal.view.ClassDinnerGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.dinnertotal.classdinnergrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassrealdinner/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClassrealdinner", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: null,

    bottomInfoPanelRef:'totalInfo',
    /** 排序字段定义 */
    defSort: [{
       property: "dinnerDate", //字段名
       direction: "ASC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
    },
    columns: {
        defaults: {
            //align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        },{
            flex:1,
            minWidth:100,
            text: "班级名称",
            dataIndex: "className",
            renderer: function(value, metaData) {                
                var title = "班级名称";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        }, {
            width:90,
            text: "就餐日期",
            dataIndex: "dinnerDate",
            renderer: function(value, metaData) {
                //var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(value*1), 'Y-m-d')           
                return ss;
            }
        },{
            width:120,
            text: "计划早餐围/人数",
            dataIndex: "breakfastCount"
        }, {
            width:120,
            text: "计划午餐围/人数",
            dataIndex: "lunchCount"
        }, {
            width:120,
            text: "计划晚餐围/人数",
            dataIndex: "dinnerCount"
        }, {
            width:120,
            text: "实际早餐围/人数",
            dataIndex: "breakfastReal"
        }, {
            width:120,
            text: "实际午餐围/人数",
            dataIndex: "lunchReal"
        }, {
            width:120,
            text: "实际晚餐围/人数",
            dataIndex: "dinnerReal"
        },{
            width:100,
            text: "计划金额",
            dataIndex: "countMoneyPlan"
        }, {
            width:100,
            text: "实际金额",
            dataIndex: "countMoneyReal"
        },{
            flex:1,
            minWidth:100,
            text: "备注",
            dataIndex: "remark",
            renderer: function(value, metaData) {                
                var title = "备注";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        }, {
            width: 130,
            text: "更新时间",
            dataIndex: "updateTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')           
                return ss;
            }
        },{
            width: 80,
            text: "登记状态",
            dataIndex: "version",
            align:'center',
            renderer: function(value, metaData) {
                if(!value)
                    return "<span style='color:red'>未登记</span>";
                else
                    return "<span style='color:green'>已登记</span>";               
            }
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
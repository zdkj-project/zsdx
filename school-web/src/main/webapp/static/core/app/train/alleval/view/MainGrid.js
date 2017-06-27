Ext.define("core.train.alleval.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.alleval.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClass/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClass", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'alleval.mainquerypanel'
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '启动评价',
            ref: 'gridAdd_Tab',
            funCode: 'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-file'
        }, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'allevalName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入指标名称'
        }, {
            xtype: 'button',
            funCode: 'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search',
        }, ' ', {
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }],
    },
    /** 排序字段定义 */
    defSort: [{
        property: "createTime", //字段名
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
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
            width: 120,
            text: "班级类型",
            dataIndex: "classCategory",
            columnType: "basecombobox", //列类型
            ddCode: "ZXXBJLX" //字典代码
        }, {
            flex: 1,
            minWidth: 120,
            text: "班级名称",
            dataIndex: "className",
        }, {
            width: 100,
            text: "开始日期",
            dataIndex: "beginDate",
            renderer: function (value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "开始日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            width: 100,
            text: "结束日期",
            dataIndex: "endDate",
            renderer: function (value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "结束日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            width: 120,
            text: "班主任",
            dataIndex: "bzrName",
            renderer: function (value, metaData) {
                var title = "班主任";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        }, {
            width: 100,
            text: "联系人",
            dataIndex: "contactPerson"
        }, {
            width: 120,
            text: "联系电话",
            dataIndex: "contactPhone"
        },{
            text:"评价情况",
            columns: [{
                width: 80,
                text: "评价状态",
                dataIndex: "isEval",
                align: 'center',
                renderer: function (value, metaData) {
                    var str = value;
                    switch (value){
                        case 0:
                            str = "<span style='color:red'>待评价</span>";
                            break;
                        case 1:
                            str = "<span style='color:blue'>评价中</span>";
                            break;
                        case 2:
                            str = "<span style='color:green'>已评价</span>";
                            break;
                    }
                    return str;
                }
            }, {
                width: 100,
                text: "很满意度(%)",
                dataIndex: "verySatisfaction",
                align: 'left'
            }, {
                width: 100,
                text: "满意度(%)",
                dataIndex: "satisfaction",
                align: 'left'
            }]
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 200,
            fixed: true,
            items: [{
                text: '启动评价',
                style: 'font-size:12px;',
                tooltip: '启动评价',
                ref: 'gridTraniee',
                getClass :function(v,metadata,record){
                    if(record.get("isEval")===1 || record.get("isEval")===2)
                        return 'x-hidden-display';
                    else
                        return null;
                },
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('mainGridStartEvalClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'startEval'
                    });
                }
            }, {
                text: '关闭评价',
                style: 'font-size:12px;',
                tooltip: '关闭评价',
                ref: 'gridTraniee',
                getClass :function(v,metadata,record){
                    if(record.get("isEval")===2||record.get("isEval")===0)
                        return 'x-hidden-display';
                    else
                        return null;
                },
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('mainGridEndEvalClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'startEval'
                    });
                }
            },{
                text: '评价汇总',
                style: 'font-size:12px;',
                tooltip: '评价汇总',
                ref: 'gridCourse',
                getClass :function(v,metadata,record){
                    if(record.get("isEval")===1 || record.get("isEval")===0)
                        return 'x-hidden-display';
                    else
                        return null;
                },
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    //var cmd=rec.get("isuse")==1?'detail':'edit';
                    this.fireEvent('mainGridSumEvalClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'sumEval'
                    });
                }
            },{
                text: '排名',
                style: 'font-size:12px;',
                tooltip: '排名',
                ref: 'gridCourse',
/*                getClass :function(v,metadata,record){
                    if(record.get("isEval")===1 || record.get("isEval")===0)
                        return 'x-hidden-display';
                    else
                        return null;
                },*/
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    //var cmd=rec.get("isuse")==1?'detail':'edit';
                    this.fireEvent('mainGridRankCourseClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'rankCourse'
                    });
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
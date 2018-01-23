Ext.define('core.train.alleval.view.EvalGrid', {
    extend: "core.base.view.BaseGrid",
    alias: "widget.alleval.evalgrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassschedule/listClassEvalCourse", //数据获取地址
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
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '班级评价结果',
            ref: 'gridClassEval',
            funCode: 'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle',          
        },{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file'
        }, 
        {
            xtype: 'button',
            text: '课程排名',
            ref: 'gridCourseRank',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-file'
        },
        /* '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'courseName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入课程名称'
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
        }*/]
    },
    /** 扩展参数 */
    extParams: {
        orderSql: " order by ranking asc "
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
            flex: 1,
            minWidth:100,
            text: "班级名称",
            dataIndex: "className",
            align: 'left',
            renderer: function (value, metaData) {
                var title = "班级名称";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 120,
            text: "课程名称",
            dataIndex: "courseName",
            align: 'left',
            renderer: function (value, metaData) {
                var title = "课程名称";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 100,
            text: "上课日期",
            dataIndex: "courseDate",
            align: 'left'
        }, {
            width: 100,
            text: "上课教师",
            dataIndex: "teacherName",
            align: 'left',
            renderer: function (value, metaData) {
                var title = "上课教师";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 100,
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
                width: 100,
                text: "很满意排名",
                dataIndex: "ranking",
                align: 'left'
            }]
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 60,
            fixed: true,
            items: [{
                text: '详情',
                style: 'font-size:12px;',
                tooltip: '详情',
                ref: 'gridTraniee',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('evalGridCourseDetailClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'startEval'
                    });
                }
            }/*  {
             text: '导出',
             style: 'font-size:12px;',
             tooltip: '',
             ref: 'gridTraniee',
             getClass: function (v, metadata, record) {
             if (record.get("evalState") === 0 || record.get("evalState") === 2)
             return 'x-hidden-display';
             else
             return null;
             },
             handler: function (view, rowIndex, colIndex, item) {
             var rec = view.getStore().getAt(rowIndex);
             this.fireEvent('mainGridEvalUrlClick_Tab', {
             view: view.grid,
             record: rec,
             cmd: 'readEval'
             });
             }
             }, {
             text: '关闭评价',
             style: 'font-size:12px;',
             tooltip: '关闭评价',
             ref: 'gridTraniee',
             getClass: function (v, metadata, record) {
             if (record.get("evalState") === 2 || record.get("evalState") === 0)
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
             }, {
             text: '评价汇总',
             style: 'font-size:12px;',
             tooltip: '评价汇总',
             ref: 'gridCourse',
             getClass: function (v, metadata, record) {
             if (record.get("evalState") === 1 || record.get("evalState") === 0)
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
             }*/]
        }]
    }
    /*    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>',
     dockedItems: [{
     xtype: 'pagingtoolbar',
     plugins: [
     Ext.create('Ext.ux.ComboPageSize', {
     pageSizes: [20, 50, 100, 1000, 10000]
     })
     ],
     store: Ext.data.StoreManager.lookup('courseeval.maingridstore'),   // same store GridPanel is using
     dock: 'bottom',
     displayInfo: true
     }, {
     xtype: 'panel',
     ref: 'evalClassInfoPanel',
     height: 40,
     bodyStyle: {
     backgroundColor: '#dfe9f6',
     border: '1px solid #99bce8',
     padding: '5px 0px 0px 5px',
     color:'red',
     fontSize:'18px'
     },
     dock: 'bottom',
     items: [{
     xtype: 'label',
     ref: 'label1',
     text: '班级信息：0',
     padding: '0 10 0 5'
     }, {
     xtype: 'button',
     ref: 'btnPreview',
     text: '预览评价',
     padding: '0 10 0 5',
     style:{
     fontSize:'14px'
     }
     }]
     //html:'合计：'
     }]*/
});
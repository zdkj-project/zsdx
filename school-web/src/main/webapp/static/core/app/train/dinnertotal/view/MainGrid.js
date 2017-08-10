Ext.define("core.train.dinnertotal.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.dinnertotal.maingrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassrealdinner/getDinnerTotalList", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClassrealdinner", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'dinnertotal.mainquerypanel',
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [ {
                xtype: 'button',
                text: '就餐详细',
                ref: 'gridDetail_Tab',
                funCode: 'girdFuntionBtn',
                disabled: true,
                iconCls: 'x-fa fa-file-text'
            },  {
                xtype: 'button',
                text: '导出汇总报表',
                ref: 'gridExport',
                funCode: 'girdFuntionBtn',
                iconCls: 'x-fa fa-file'
            },{
                xtype: 'button',
                text: '导出班级就餐详情',
                ref: 'gridDinnerDetailExport',
                funCode: 'girdFuntionBtn',
                iconCls: 'x-fa fa-file'
            },  '->', {
                xtype: 'tbtext',
                html: '快速搜索：'
            }, {
                xtype: 'textfield',
                name: 'className',
                funCode: 'girdFastSearchText',
                emptyText: '请输入班级名称'
            }, {
                xtype: 'button',
                funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
                ref: 'gridFastSearchBtn',
                iconCls: 'x-fa fa-search',
            },         
            ' ', {
                xtype: 'button',
                text: '高级搜索',
                ref: 'gridHignSearch',
                iconCls: 'x-fa fa-sliders'
            }
        ],
    },

    bottomInfoPanelRef:'dinnerTotalInfo',

    /** 排序字段定义 */
    defSort: [],
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
            text: "班级id",
            dataIndex: "CLASS_ID",
            hidden:true
        },{
            flex:1,
            minWidth:100,
            text: "班级名称",
            dataIndex: "CLASS_NAME",
            renderer: function(value, metaData) {                
                var title = "班级名称";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        },{
            width:100,
            text: "班级编号",
            dataIndex: "CLASS_NUMB"
        }, {
            width:80,
            text: "就餐类型",
            dataIndex: "DINNER_TYPE",
            renderer: function(value, metaData) {                
                if(value==1){
                    return "围餐";
                }else if(value==2){
                    return "自助餐";
                }else{
                    return "快餐";
                }
            }
        }, {
            width:120,
            text: "计划早餐围/人数",
            dataIndex: "BREAKFAST_COUNT"
        }, {
            width:120,
            text: "计划午餐围/人数",
            dataIndex: "LUNCH_COUNT"
        }, {
            width:120,
            text: "计划晚餐围/人数",
            dataIndex: "DINNER_COUNT"
        }, {
            width:120,
            text: "实际早餐围/人数",
            dataIndex: "BREAKFAST_REAL"
        }, {
            width:120,
            text: "实际午餐围/人数",
            dataIndex: "LUNCH_REAL"
        }, {
            width:120,
            text: "实际晚餐围/人数",
            dataIndex: "DINNER_REAL"
        }, {
            width:100,
            text: "计划总额",
            dataIndex: "COUNT_MONEY_PLAN"
        }, {
            width:100,
            text: "实际总额",
            dataIndex: "COUNT_MONEY_REAL"
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align:'center',
            width: 80,
            fixed: true,
            items: [ {
                text:'就餐详情',  
                style:'font-size:12px;',  
                tooltip: '就餐详情',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
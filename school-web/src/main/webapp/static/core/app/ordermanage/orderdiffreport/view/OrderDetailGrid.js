Ext.define("core.ordermanage.orderdiffreport.view.OrderDetailGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.cashreport.cashdetailgrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/CashExpensedetail/list", //数据获取地址
    model: "com.zd.school.cashier.model.CashExpensedetail", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [ {
                xtype: 'button',
                text: '导出流水明细报表',
                ref: 'gridExport',
                funCode: 'girdFuntionBtn',
                iconCls: 'x-fa fa-file'
            },  '->', {
                xtype: 'tbtext',
                html: '快速搜索：'
            }, {
                xtype: 'textfield',
                name: 'detailName',
                funCode: 'girdFastSearchText',
                emptyText: '请输入消费名称'
            }, {
                xtype: 'button',
                funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
                ref: 'gridFastSearchBtn',
                iconCls: 'x-fa fa-search',
            }
        ],
    },

    //bottomInfoPanelRef:'totalInfo',
    /** 排序字段定义 */
    defSort: [{
       property: "createTime", //字段名
       direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //filter:'[{"type":"string","comparison":"=","value":"null","field":"expenseserialId"}]'
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
            text: "消费名称",
            dataIndex: "detailName",
            renderer: function(value, metaData) {                
                var title = "消费名称";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        }, {
            width:200,
            text: "数量",
            dataIndex: "detailCount"
        }, {
            width:200,
            text: "单价",
            dataIndex: "detailPrice"
        },{
            width: 130,
            text: "发生时间",
            dataIndex: "createTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')           
                return ss;
            }
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
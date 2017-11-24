/*没订餐但吃过早餐的*/
Ext.define("core.ordermanage.orderdiffreport.view.NotOrderEatDetailGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.orderdiffreport.notordereatdetailgrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTeacherOrder/getOrderDetailList", //数据获取地址
    //model: "com.zd.school.cashier.model.CashExpensedetail", //对应的数据模型
    noPagging:true,
    remoteSort:false,
    selModel:null,
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
    panelTopBar:null,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    // panelTopBar:{
    //     xtype:'toolbar',
    //     items: [ {
    //         xtype: 'tbtext',
    //         html: '就餐名单（未订餐）',
    //         style: {
    //             fontSize: '16px',
    //             color: '#C44444',
    //             fontWeight:800
    //         }
    //     }]
    // },
    // panelTopBar: {
    //     xtype: 'toolbar',
    //     items: [ {
    //             xtype: 'button',
    //             text: '导出流水明细报表',
    //             ref: 'gridExport',
    //             funCode: 'girdFuntionBtn',
    //             iconCls: 'x-fa fa-file'
    //         },  '->', {
    //             xtype: 'tbtext',
    //             html: '快速搜索：'
    //         }, {
    //             xtype: 'textfield',
    //             name: 'detailName',
    //             funCode: 'girdFastSearchText',
    //             emptyText: '请输入消费名称'
    //         }, {
    //             xtype: 'button',
    //             funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
    //             ref: 'gridFastSearchBtn',
    //             iconCls: 'x-fa fa-search',
    //         }
    //     ],
    // },

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
        }, {
            flex:1,
            minWidth:90,
            text: "姓名",
            dataIndex: "EmployeeName"
        }, {
            flex:1,
            minWidth:90,
            text: "人员编号",
            dataIndex: "EmployeeStrID"
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
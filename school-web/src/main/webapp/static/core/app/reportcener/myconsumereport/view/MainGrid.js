Ext.define("core.reportcenter.myconsumereport.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.myconsumereport.maingrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainReport/getMyConsumeTotalList", //数据获取地址
    //model: "com.zd.school.cashier.model.CashExpenseserial", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'myconsumereport.mainquerypanel',
    },
    selModel: null,
    sortableColumns:false,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [ {
                xtype: 'button',
                text: '导出报表',
                ref: 'gridExport',
                funCode: 'girdFuntionBtn',
                iconCls: 'x-fa fa-file'
            },  '->',        
            ' ', {
                xtype: 'button',
                text: '高级搜索',
                ref: 'gridHignSearch',
                iconCls: 'x-fa fa-sliders'
            }
        ],
    },

    bottomInfoPanelRef:'consumeTotalInfo',

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
            width:150,
            text: "消费日期",
            dataIndex: "ConsumeDate",
            renderer: function(value) {  
                //if(typeof(value)!="undefined"&&v.trim()!=""){
                var reg = new RegExp("^[0-9]*$");    
                if(reg.test(value)){
                    return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
                }  
                return value;                           
            }
        }, {
            flex:1,
            minWidth:100,
            text: "人员姓名",
            dataIndex: "EmployeeName"
        },{
            flex:1,
            minWidth:100,
            text: "人员编号",
            dataIndex: "EmployeeStrID"
        },{
            flex:1,
            minWidth:100,
            text: "消费设备",
            dataIndex: "TermName"
        },{
            flex:1,
            minWidth:100,
            text: "就餐类型",
            dataIndex: "MealTypeName"
        },{
            flex:1,
            minWidth:100,
            text: "结算账户",
            dataIndex: "AccountName"
        },{
            width:100,
            text: "消费金额",
            dataIndex: "ConsumeValue"
        },{ 
            width:100,
            text: "消费笔数",
            dataIndex: "ConsumeNumber"
        },{
            width:100,
            text: "余额",
            dataIndex: "CardValue"
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
Ext.define("core.reportcenter.traineeconsumereport.view.TraineeDetailGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.traineeconsumereport.traineedetailgrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainReport/getTraineeConsumeDetailList", //数据获取地址
    model: "com.zd.school.cashier.model.CashExpenseserial", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'traineeconsumereport.traineequerypanel',
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
                text: '导出班级点券就餐明细报表',
                ref: 'gridExport',
                funCode: 'girdFuntionBtn',
                iconCls: 'x-fa fa-file'
            },  '->', {
                xtype: 'tbtext',
                html: '快速搜索：'
            }, {
                xtype: 'textfield',
                name: 'EmployeeName',
                funCode: 'girdFastSearchText',
                emptyText: '请输入人员姓名'
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

    bottomInfoPanelRef:'traineeDetailInfo',

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
            text: "卡片编号",
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
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
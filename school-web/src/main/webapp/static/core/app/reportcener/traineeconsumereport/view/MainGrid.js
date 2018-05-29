Ext.define("core.reportcenter.traineeconsumereport.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.traineeconsumereport.maingrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainReport/getTraineeConsumeTotalList", //数据获取地址
    //model: "com.zd.school.cashier.model.CashExpenseserial", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'traineeconsumereport.mainquerypanel',
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
            },  '->', {
                xtype: 'tbtext',
                html: '快速搜索：'
            }, {
                xtype: 'textfield',
                name: 'CLASS_NAME',
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
            text: "班级ID",
            dataIndex: "CLASS_ID",
            hidden:true
        },{
            flex:1,
            minWidth:120,
            text: "班级名称",
            dataIndex: "CLASS_NAME"
        }, {
            width:100,
            text: "班级编号",
            dataIndex: "CLASS_NUMB"
        },{
            width:100,
            text: "班级开始日期",
            dataIndex: "BEGIN_DATE",
            renderer: function(value) {  
                //if(typeof(value)!="undefined"&&v.trim()!=""){
                var reg = new RegExp("^[0-9]*$");    
                if(reg.test(value)){
                    return Ext.Date.format(new Date(value), 'Y-m-d');
                }  
                return value;                           
            }
        },{
            width:100,
            text: "班级结束日期",
            dataIndex: "END_DATE",
            renderer: function(value) {  
                //if(typeof(value)!="undefined"&&v.trim()!=""){
                var reg = new RegExp("^[0-9]*$");    
                if(reg.test(value)){
                    return Ext.Date.format(new Date(value), 'Y-m-d');
                }  
                return value;                           
            }
        },{
            width:80,
            text: "就餐类型",
            dataIndex: "DINNER_TYPE",
            renderer: function(value, metaData) {                
                if(value==1){
                    return "围餐";
                }else if(value==2){
                    return "自助餐";
                }else if(value==3){
                    return "快餐";
                }else{
                    return value;
                }
            }
        },{
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
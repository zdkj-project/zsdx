Ext.define("core.reportcenter.cashreport.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.cashreport.maingrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainReport/getCashTotalList", //数据获取地址
    model: "com.zd.school.cashier.model.CashExpenseserial", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'cashreport.mainquerypanel',
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
                name: 'CONSUME_SERIAL',
                funCode: 'girdFastSearchText',
                emptyText: '请输入流水号'
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
            text: "收银ID",
            dataIndex: "EXPENSESERIAL_ID",
            hidden:true
        },{
            width:140,
            text: "收银日期",
            dataIndex: "CONSUME_DATE",
            renderer: function(value) {  
                //if(typeof(value)!="undefined"&&v.trim()!=""){
                var reg = new RegExp("^[0-9]*$");    
                if(reg.test(value)){
                    return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
                }  
                return value;                           
            }
        },{
            
            width:170,
            text: "流水号",
            dataIndex: "CONSUME_SERIAL"
        }, {
            width:100,
            text: "消费人",
            dataIndex: "CONSUME_USER"
        },{
            flex:1,
            minWidth:80,
            text: "部门",
            dataIndex: "RECEPTION_DEPT"
        },{
            flex:1,
            minWidth:80,
            text: "单位",
            dataIndex: "VISITOR_UNIT"
        },/*{
            width:100,
            text: "操作者",
            dataIndex: "OPERTIONER"
        },*/{
            width:100,
            text: "消费类型",
            dataIndex: "CONSUME_TYPE"
        },{
            width:80,
            text: "交易状态",
            dataIndex: "CONSUME_STATE",
            renderer: function(value, metaData) {                
                if(value=="成功"){
                    return "<span style='color:green'>成功</span>";
                }else if(value=="销单"){
                    return "<span style='color:red'>销单</span>";
                }else if(value=="取消"){
                    return "<span style='color:black'>取消</span>";
                }else{
                    return value;
                }
            }
        },{
            width:80,
            text: "结算方式",
            dataIndex: "CLEARING_FORM",
            renderer: function(value, metaData) {                
                if(value==1){
                    return "现金";
                }else if(value==2){
                    return "刷卡";
                }else if(value==3){
                    return "签单";
                }else{
                    return value;
                }
            }
        },{
            width:80,
            text: "用餐类型",
            dataIndex: "MEAL_TYPE",
            renderer: function(value, metaData) {                
                if(value==1){
                    return "早餐";
                }else if(value==2){
                    return "午餐";
                }else if(value==3){
                    return "晚餐";
                }else if(value==4){
                    return "夜宵";
                }else{
                    return value;
                }
            }
        }, {
            width:80,
            text: "交易笔数",
            dataIndex: "CASH_NUMBER"
        }, {
            width:100,
            text: "消费总额",
            dataIndex: "CONSUME_TOTAL"
        }, {
            width:100,
            text: "实付金额",
            dataIndex: "REAL_PAY"
        },/* {
            width:100,
            text: "找零金额",
            dataIndex: "CHANGE_PAY"
        },*/{
            xtype: 'actiontextcolumn',
            text: "操作",
            align:'center',
            width: 100,
            fixed: true,
            items: [ {
                text:'收银明细',  
                style:'font-size:12px;',  
                tooltip: '收银明细',
                ref: 'gridDetail',
                getClass: function (v, metadata, record) {
                    if (record.get("EXPENSESERIAL_ID")==null){
                        return 'x-hidden-display';
                    } else
                        return null;
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            },{
                text:'销单',  
                style:'font-size:12px;',  
                tooltip: '废除错误录入的单',
                ref: 'gridDestroy',
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if(record.get("EXPENSESERIAL_ID")==null || record.get("CONSUME_STATE")!="成功"
                        ||(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 
                            && roleKey.indexOf("ZONGWUROLE") == -1 && roleKey.indexOf("FOODMANAGER") == -1))
                        return 'x-hidden-display';
                    else
                        return null;
            
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('destroyClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
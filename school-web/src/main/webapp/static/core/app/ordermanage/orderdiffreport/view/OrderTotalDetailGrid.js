Ext.define("core.ordermanage.orderdiffreport.view.OrderTotalDetailGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.orderdiffreport.ordertotaldetailgrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTeacherOrder/getTeacherOrderDiffTotalList", //数据获取地址
    //model: "com.zd.school.cashier.model.CashExpensedetail", //对应的数据模型
    remoteSort:false,
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
            text: '导出汇总报表',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file'
        }, /* '->', {
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
        }*/],
    },

    //bottomInfoPanelRef:'totalInfo',
    /** 排序字段定义 */
    // defSort: [{
    //    property: "createTime", //字段名
    //    direction: "DESC" //升降序
    // }],
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
            dataIndex: "EmployeeName",    
            text:"姓名"         
        },{
            flex:1,
            minWidth:100,
            dataIndex: "EmployeeStrID", 
            text:"人员编号"            
        },{
            flex:1,
            minWidth:100,
            dataIndex: "realCount", 
            text:"就餐总数",
            renderer: function(value, metaData) {               
                return value+" 份";
            }            
        },{
            flex:1,
            minWidth:100,
            dataIndex: "dinnerCount",    
            text:"订餐总数",
            renderer: function(value, metaData) {               
                return value+" 份";
            }         
        },{
            flex:1.5,
            minWidth:150,
            dataIndex: "realDinnerCount", 
            text:"就餐总数（已订餐）",       
            renderer: function(value, metaData) {
                return "<span style='color:#0087ff'>"+value+" 份</span>";                   
            }    
        },{
            flex:1.5,
            minWidth:150,
            dataIndex: "notDinnerEatCount",  
            text:"就餐总数（未订餐）",
            renderer: function(value, metaData) {               
                return "<span style='color:#ff00ae'>"+value+" 份</span>";
            }      
        },{
            flex:1,
            minWidth:100,
            dataIndex: "exceedEatCount",     
            text:"多刷份数",
            renderer: function(value, metaData) {               
                return "<span style='color:#7e2561'>"+value+" 份</span>";
            }
        },{
            flex:1.6,
            minWidth:160,
            dataIndex: "notEatdinnerCount",   
            text:"未就餐总数（已订餐）",
            renderer: function(value, metaData) {               
                return "<span style='color:#ff6c00'>"+value+" 份</span>";
            }          
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
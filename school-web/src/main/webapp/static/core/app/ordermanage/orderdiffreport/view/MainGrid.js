Ext.define("core.ordermanage.orderdiffreport.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.orderdiffreport.maingrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTeacherOrder/getDinnerDiffTotalList", //数据获取地址
    //model: "com.zd.school.cashier.model.CashExpenseserial", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'orderdiffreport.mainquerypanel',
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
            },  '->',{
                xtype: 'tbtext',
                html: '快速搜索：'
            }, {                
                xtype: "datetimefield",
                name: "dinnerDate",
                queryType: "datetimefield",
                dateType:'date',        //指定这个组件的格式，date或者datetime
                funCode: 'girdFastSearchText',
                //value:new Date(),
                emptyText: '请选择订餐日期',
                format: "Y年m月d日",   //显示的格式
                triggers: {
                    picker: {
                        handler: 'onTriggerClick',
                        scope: 'this',
                        focusOnMousedown: true
                    },
                    clear: {
                        cls:'x-fa fa-times',
                        handler:function(btn){
                            var me=this;
                            me.reset();
                        },
                        weight:-1,
                        scope: 'this',
                        focusOnMousedown: true
                    },
                },

            }, {
                xtype: 'button',
                funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
                ref: 'gridFastSearchBtn',
                iconCls: 'x-fa fa-search',
            },' ',{
                xtype: 'button',
                text: '高级搜索',
                ref: 'gridHignSearch',
                iconCls: 'x-fa fa-sliders'
            }
        ],
    },

    bottomInfoPanelRef:'dinnerDiffTotalInfo',

    /** 排序字段定义 */
    defSort: [],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
    },
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        },{
            flex:1.2,
            minWidth:120,
            text: "订餐日期",
            dataIndex: "dinnerDate",
            // renderer: function(value) {  
            //     //if(typeof(value)!="undefined"&&v.trim()!=""){
            //     var reg = new RegExp("^[0-9]*$");    
            //     if(reg.test(value)){
            //         return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
            //     }  
            //     return value;                           
            // }
        },{  
            flex:1,      
            minWidth:100,
            text: "订餐总数",
            dataIndex: "dinnerCount",
            renderer: function(value, metaData) {               
                return value+" 份";
            }
        }, {
            flex:1,      
            minWidth:100,
            text: "A套餐总数",
            dataIndex: "dinnerCountA",
            renderer: function(value, metaData) {               
                return value+" 份";
            }
        },{
            flex:1,      
            minWidth:100,
            text: "B套餐总数",
            dataIndex: "dinnerCountB",
            renderer: function(value, metaData) {               
                return value+" 份";
            }
        },{
            flex:1,      
            minWidth:100,
            text: "实际就餐总数",
            dataIndex: "realCount",
            renderer: function(value, metaData) {               
                return "<span style='color:green'>"+value+" 份</span>";
            }
        },{
            flex:1.5,      
            minWidth:150,
            text: "就餐总数（已订餐）",
            dataIndex: "realDinnerCount",
            renderer: function(value, metaData) {               
                return "<span style='color:#0087ff'>"+value+" 份</span>";
            }
        },{
            flex:1.5,      
            minWidth:150,
            text: "就餐总数（未订餐）",
            dataIndex: "notDinnerEatCount",
            renderer: function(value, metaData) {               
                return "<span style='color:#ff00ae'>"+value+" 份</span>";
            }
        },{
            flex:1.6,      
            minWidth:160,
            text: "未就餐总数（已订餐）",
            dataIndex: "notEatdinnerCount",
            renderer: function(value, metaData) {               
                return "<span style='color:#ff6c00'>"+value+" 份</span>";
            }
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align:'center',
            width: 100,
            fixed: true,
            items: [ {
                text:'订餐明细',  
                style:'font-size:12px;',  
                tooltip: '收银明细',
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
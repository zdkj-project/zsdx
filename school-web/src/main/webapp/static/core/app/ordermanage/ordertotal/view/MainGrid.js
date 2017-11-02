Ext.define("core.ordermanage.ordertotal.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.ordertotal.maingrid",
    al:true,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTeacherOrder/getOrderTotal", //数据获取地址
    //model: "com.zd.school.cashier.model.CashExpenseserial", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
    selModel: {
        type: "checkboxmodel",   
        headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        mode:'single',  //multi,simple,single；默认为多选multi
        //checkOnly:false,    //如果值为true，则只用点击checkbox列才能选中此条记录
        //allowDeselect:true, //如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    },
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
        },{
            xtype: 'tbtext',
            html: '（*只显示未来10天的订餐数据）',
            style: {
                fontSize: '14px',
                color: '#C44444',
                fontWeight:800
            }
        }],
    },

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
            width: 200,
            text: "订餐日期",
            dataIndex: "dinnerDate",
            renderer: function(value, metaData) {
                var reg = new RegExp("^[0-9]*$");    
                if(reg.test(value)){
                    return Ext.Date.format(new Date(value), 'Y年m月d日');
                }
                return value;
            }
        },{
            flex:1,
            minWidth:200,
            text: "总订餐人数",
            dataIndex: "count"
        }, {        
            flex:1,
            minWidth:200,
            text: "A套餐人数",
            dataIndex: "countA"
        },{
            flex:1,
            minWidth:200,
            text: "B套餐人数",
            dataIndex: "countB"
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align:'center',
            width: 80,
            fixed: true,
            items: [ {
                text:'订餐人员',  
                style:'font-size:12px;',  
                tooltip: '订餐人员',
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
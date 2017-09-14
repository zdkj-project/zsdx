Ext.define("core.cashier.mealset.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mealset.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/CashDinneritem/list", //数据获取地址
    model: "com.zd.school.cashier.model.CashDinneritem", //对应的数据模型
//    /**
//     * 高级查询面板
//     */
//    panelButtomBar: {
//        xtype: 'trainee.mainquerypanel'
//    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd_Tab',
            funCode: 'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-minus-circle'
        }, /*'->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'xm',
            funCode: 'girdFastSearchText',
            emptyText: '请输入姓名'
        }, {
            xtype: 'button',
            funCode: 'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search',
        }, ' ', {
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }*/],
    },
    /** 排序字段定义 */
    defSort: [{
        property: "updateTime", //字段名
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
    },
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            width: 50,
            text: '序号',
            align: 'center'
        },{
            width: 120,
            text: "菜品类别",
            dataIndex: "dishesType",
            columnType: "basecombobox", //列类型
            ddCode: "DISHESTYPE" //字典代码			
        },{
            width: 120,
            text: "菜品编号",
            dataIndex: "dishesCode"
        },{
            width: 220,
            text: "菜品名称",
            dataIndex: "dishesName"
        },{
            width: 120,
            text: "菜品单价",
            dataIndex: "dishesPrice",
        }, {
        	flex: 1,
            minWidth: 150,
            text: "菜品说明",
            dataIndex: "dishesExplain"
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 180,
            //resizable: false,
            fixed: true,
            align: 'center',
            items: [{
                text: '编辑',
                style: 'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd:"edit"
                    });
                },
//                getClass: function (v, metadata, record) {
//                    var roleKey = comm.get("roleKey");
//                    if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("PEIXUNROLE") == -1) {
//                        return 'x-hidden-display';
//                    } else
//                        return null;
//                }
            }, {
                text: '删除',
                style: 'font-size:12px;',
                tooltip: '删除',
                ref: 'gridDelete',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view: view.grid,
                        record: rec
                    });
                },
//                getClass: function (v, metadata, record) {
//                    var roleKey = comm.get("roleKey");
//                    if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("PEIXUNROLE") == -1) {
//                        return 'x-hidden-display';
//                    } else
//                        return null;
//                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
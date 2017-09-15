Ext.define("core.cashier.mealtime.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mealtime.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/CashDinnertime/list", //数据获取地址
    model: "com.zd.school.cashier.model.CashDinnertime", //对应的数据模型
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit_Tab',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-pencil-square'
        }],
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
            text: "快餐类别",
            dataIndex: "mealType",
            columnType: "basecombobox", //列类型
            ddCode: "MEALTYPE" //字典代码			
        },{
            width: 270,
            text: "开始时间",
            dataIndex: "beginTime"
        },{
            width: 270,
            text: "结束时间",
            dataIndex: "endTime"
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
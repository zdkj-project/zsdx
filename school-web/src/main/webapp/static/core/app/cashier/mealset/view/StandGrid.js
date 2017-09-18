Ext.define("core.cashier.mealset.view.StandGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mealset.standgrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/CashDinneritem/list", //数据获取地址
    model: "com.zd.school.cashier.model.CashDinneritem", //对应的数据模型
    style: {
        border: '1px solid #ddd'
    },
    //margin: '0 0 0 10',
    /**
     * 高级查询面板
     */
    panelButtomBar: false,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAddStand',
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
            xtype: 'button',
            text: '删除',
            ref: 'gridDelStand',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-pencil-square'
        }]
    },
    /** 排序字段定义 */
    defSort: [{
        property: "createTime", //字段名
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        filter: "[{'type':'short','comparison':'=','value':'22','field':'mealType'}]"
    },
    plugins: {
        ptype: 'cellediting',
        clicksToEdit: 1
    },
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
        	width: 220,
            text: "餐名",
            dataIndex: "mealName",
            editor: {
                allowBlank: false,
                type: 'string'
            },
            renderer: function (value, metaData) {
                var title = "餐名";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        },{
            width: 120,
            text: "餐标",
            dataIndex: "mealPrice",
            editor: {
                allowBlank: false,
                xtype: 'numberfield'
            },
            renderer: function (value, metaData) {
                  var title = "餐标";
                  var html = value;
                  metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                  return html;
            }	
        }, {
        	flex: 1,
            minWidth: 150,
            text: "说明",
            dataIndex: "mealExplain",
            editor: {
                allowBlank: true,
                type: 'string'
            },
            renderer: function (value, metaData) {
                  var title = "说明";
                  var html = value;
                  metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                  return html;
            }	
        },]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
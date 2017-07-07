Ext.define("core.train.alleval.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.alleval.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClass/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClass", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'alleval.mainquerypanel'
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [/*{
            xtype: 'button',
            text: '启动评价',
            ref: 'gridAdd_Tab',
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        },*/ {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-file'
        }, '->', {
            xtype: 'textfield',
            name: 'className',
            funCode: 'girdFastSearchText',
            emptyText: '请输入班级名称'
        }, {
            xtype: 'button',
            funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search',
        }]
    },
    /** 排序字段定义 */
    defSort: [{
        property: "classCategory", //字段名
        direction: "ASC" //升降序
    }
        /*, {
         property: "isEval", //字段名
         direction: "ASC" //升降序
         }*/
    ],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    features: [{
        ftype: 'grouping',
        //groupHeaderTpl:'<span style="color: red;font-family: 微软雅黑;font-size: 13px;font-weight: bold">班级类型：{name}</span>',
        groupHeaderTpl: Ext.create('Ext.XTemplate',
            '<div><tpl for="children[0].data">',
            '<tpl if="!classCategoryName">', // <-- Note that the > is encoded
            '<span style="color:#00c4ff;font-family: 微软雅黑;font-size: 13px;font-weight: bold">无类型</span>',
            '<tpl else >',
            '<span style="color:#aa3e38;font-family: 微软雅黑;font-size: 13px;font-weight: bold">{classCategoryName}</span>',
            '</tpl></tpl></div>'
        ),
        expandTip: '点击展开. 按下CTRL键再点击折叠其它',
        collapseTip: '点击折叠. 按下CTRL键再点击折叠其它',
    }],
    defGroup: 'classCategory',
    sortableColumns :false,
    columns: {
        defaults: {
            titleAlign: "center",
            menuDisabled:true   //隐藏menu菜单
        },
        items: [{
            flex: 1,
            minWidth: 120,
            text: "班级名称",
            dataIndex: "className",
        }, {
            width: 100,
            text: "班主任",
            dataIndex: "bzrName",
            align: 'center',
            renderer: function (value, metaData) {
                var title = "班主任";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
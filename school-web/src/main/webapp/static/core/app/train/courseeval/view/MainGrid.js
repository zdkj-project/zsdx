Ext.define("core.train.courseeval.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.courseeval.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClass/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClass", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'courseeval.mainquerypanel'
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '启动评价',
            ref: 'gridAdd_Tab',
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'className',
            funCode: 'girdFastSearchText',
            emptyText: '请输入班级名称'
        }, {
            xtype: 'button',
            funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search',
        }/*, ' ', {
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }*/]
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
            menuDisabled:true,  //隐藏menu菜单
        },
        items: [{
                flex: 1,
                minWidth: 120,
                text: "班级名称",
                dataIndex: "className",
            }, {
                width: 80,
                text: "评价状态",
                dataIndex: "isEval",
                align: 'center',
                renderer: function(value, metaData) {
                    var str = value;
                    switch (value) {
                        case 0:
                            str = "<span style='color:red'>待评价</span>";
                            break;
                        case 1:
                            str = "<span style='color:blue'>评价中</span>";
                            break;
                        case 2:
                            str = "<span style='color:green'>已评价</span>";
                            break;
                    }
                    return str;
                }
            }
            /*, {
                     width: 100,
                     text: "开始日期",
                     dataIndex: "beginDate",
                     renderer: function (value, metaData) {
                     var date = value.replace(new RegExp(/-/gm), "/");
                     var title = "开始日期";
                     var ss = Ext.Date.format(new Date(date), 'Y-m-d')
                     var html = ss;
                     metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                     return ss;
                     }
                     }, {
                     width: 100,
                     text: "结束日期",
                     dataIndex: "endDate",
                     renderer: function (value, metaData) {
                     var date = value.replace(new RegExp(/-/gm), "/");
                     var title = "结束日期";
                     var ss = Ext.Date.format(new Date(date), 'Y-m-d')
                     var html = ss;
                     metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                     return ss;
                     }
                     }, {
                     width: 100,
                     text: "班主任",
                     dataIndex: "bzrName",
                     renderer: function (value, metaData) {
                     var title = "班主任";
                     metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                     return value;
                     }
                     }*/
        ]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
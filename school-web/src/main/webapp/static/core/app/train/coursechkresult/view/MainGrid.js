Ext.define("core.train.coursechkresult.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.coursechkresult.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClass/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClassschedule", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'coursechkresult.mainquerypanel',
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '分析',
            ref: 'gridAnalyze',
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
            xtype: 'button',
            text: '详细',
            ref: 'gridDetail',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-file-text'
        }, {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-file'
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
        }, ' ', {
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }],
    },
    /** 排序字段定义 */
    defSort: [
//    	{
//        property: "createTime", //字段名
//        direction: "DESC" //升降序
//    },
    {
        property: "updateTime", //字段名
        direction: "DESC" //升降序
    }
    	],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 60,
            text: '序号',
            align: 'center'
        }, {
            width: 150,
            text: "开始日期",
            dataIndex: "beginDate",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "开始日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            width: 150,
            text: "结束日期",
            dataIndex: "endDate",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "结束日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            flex:1,
            width: 120,
            text: "班级名称",
            dataIndex: "className",
        }, {
            width: 150,
            text: "班级类型",
            dataIndex: "classCategory",
            columnType: "basecombobox", //列类型
            ddCode: "CLASSCATEGORY" //字典代码			
        }, {
            width: 150,
            text: "班级编号",
            dataIndex: "classNumb",
        },{
            width: 150,
            text: "创建时间",
            dataIndex: "createTime",
            align:'left',
        },{
            width: 150,
            text: "更新时间",
            dataIndex: "updateTime",
            align:'left',
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 100,
            fixed: true,
            items: [{
                //iconCls: 'x-fa fa-pencil-square',
            	text:'编辑',  
                style:'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick', {
                        view: view.grid,
                        record: rec
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }, {
                //iconCls: 'x-fa fa-file-text',
            	text:'详细',  
                style:'font-size:12px;',  
                tooltip: '详细',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                //iconCls: 'x-fa fa-minus-circle',
            	text:'删除',  
                style:'font-size:12px;',  
                tooltip: '删除',
                ref: 'gridDelete',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view: view.grid,
                        record: rec
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
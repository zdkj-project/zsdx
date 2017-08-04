Ext.define("core.oa.meeting.checkresult.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.checkresult.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/OaMeeting/list", //数据获取地址
    model: "com.zd.school.oa.meeting.model.OaMeeting", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'checkresult.mainquerypanel',
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [/*{
            xtype: 'button',
            text: '分析',
            ref: 'gridAnalyze',
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        },*/ {
            xtype: 'button',
            text: '详细',
            ref: 'gridDetail_Tab',
            funCode: 'girdFuntionBtn',
            //disabled: true,
            iconCls: 'x-fa fa-file-text'
        }, {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file'
        }, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'meetingTitle',
            funCode: 'girdFastSearchText',
            emptyText: '请输入会议主题'
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
    defSort: [{
       property: "updateTime", //字段名
       direction: "DESC" //升降序
    }],
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
            flex: 1,
        	minWidth:200,
            text: "会议主题",
            dataIndex: "meetingTitle",
        }, {
            //flex: 1,
        	width:160,
            text: "开始时间",
            dataIndex: "beginTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "开始时间";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d  H:i:s')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            //flex: 1,
        	width:160,
            text: "结束时间",
            dataIndex: "endTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "结束时间";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d  H:i:s')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }
        /*, {
            flex: 1,
            text: "主持人",
            dataIndex: "emcee",
        }*/
        , {
            //flex: 1,
            width:120,
            text: "会议地点",
            dataIndex: "roomName",
        }, {
            //flex: 1,
        	width:80,
            text: "会议状态",
            dataIndex: "meetingState",
            columnType: "basecombobox", //列类型
            ddCode: "MEETINGSTATE" //字典代码			
        },{
            width: 150,
            text: "创建时间",
            dataIndex: "createTime",
            align: 'left'
        }, {
            width: 150,
            text: "更新时间",
            dataIndex: "updateTime",
            align: 'left'
        }, {
            //flex: 1,
        	width:80,
            text: "考勤结果",
            dataIndex: "attendResult",
            renderer: function(value, metaData) {
                var html = value;
                var title='考勤结果';
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return value;
            }
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 80,
            fixed: true,
            items: [
                /*{
                    iconCls: 'x-fa fa-pencil-square',
                    tooltip: '编辑',
                    ref: 'gridEdit',
                    handler: function(view, rowIndex, colIndex, item) {
                        var rec = view.getStore().getAt(rowIndex);
                        this.fireEvent('editClick', {
                            view: view.grid,
                            record: rec
                        });
                    }
                            },*/
                {
                    //iconCls: 'x-fa fa-file-text',
                	text:'详细',  
                    style:'font-size:12px;',
                    tooltip: '详细',
                    ref: 'gridDetail_Tab',
                    handler: function(view, rowIndex, colIndex, item) {
                        var rec = view.getStore().getAt(rowIndex);
                        this.fireEvent('detailClick_Tab', {
                            view: view.grid,
                            record: rec
                        });
                    }
                }/*, {
                    //iconCls: 'x-fa fa-minus-circle',
                	text:'分析',  
                    style:'font-size:12px;',
                    tooltip: '分析',
                    ref: 'gridAnalyze',
                    handler: function(view, rowIndex, colIndex, item) {
                        var rec = view.getStore().getAt(rowIndex);
                        this.fireEvent('analyzeClick', {
                            view: view.grid,
                            record: rec
                        });
                    }
                }*/
            ]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
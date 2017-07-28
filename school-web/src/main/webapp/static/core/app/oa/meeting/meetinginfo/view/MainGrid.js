Ext.define("core.oa.meeting.meetinginfo.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.meetinginfo.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/OaMeeting/list", //数据获取地址
    model: "com.zd.school.oa.meeting.model.OaMeeting", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'meetinginfo.mainquerypanel',
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [/*{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd_Tab',     //命名规则：后面多加入了一个后缀【_Tab】
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit_Tab',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-pencil-square'
        }, {
            xtype: 'button',
            text: '详细',
            ref: 'gridDetail_Tab',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-file-text'
        }, {
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-minus-circle'
        }, {
            xtype: 'button',
            text: '导入',
            ref: 'gridImport',
            funCode: 'girdFuntionBtn',
            //disabled: true,
            iconCls: 'x-fa fa-clipboard'
        }, */{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            //disabled: true,
            iconCls: 'x-fa fa-file'
        },{
            xtype: 'button',
            text: '同步数据',
            ref: 'sync',
            funCode:'girdFuntionBtn',         
            iconCls: 'x-fa fa-rss'
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
        }]
    },
    /** 排序字段定义 */
    defSort: [{
       property: "updateTime", //字段名
       direction: "desc" //升降序
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
            //align: 'left',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            //flex: 0,
            width: 60,
            text: '序号',
            align: 'center'
        }, {
            flex: 1,
            minWidth:200,
        	//width:200,
            text: "会议主题",
            dataIndex: "meetingTitle",
            align: 'left',
        }, {
            //flex: 1,
        	width:180,
            text: "开始时间",
            dataIndex: "beginTime",
            align: 'left',
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
            align: 'left',
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
        	width: 130,
            text: "会议地点",
            dataIndex: "roomName",
            align: 'left',
        },{
            //flex: 1,
        	width:100,
            text: "会议类型",
            align: 'left',
            dataIndex: "meetingCategory",
            columnType: "basecombobox", //列类型
            ddCode: "MEETINGCATEGORY" //字典代码
        }
        /*, {
            //flex: 1,
        	width:80,
            text: "会议状态",
            dataIndex: "meetingState",
            columnType: "basecombobox", //列类型
            ddCode: "MEETINGSTATE" //字典代码			
        }*/
    ,{
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
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 120,
            fixed: true,
            align: 'center',
            items: [/*{
                iconCls: 'x-fa fa-pencil-square',
            	text:'编辑',  
                style:'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, */{
                //iconCls: 'x-fa fa-file-text',
            	text:'详细',  
                style:'font-size:12px;',
                tooltip: '详细',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }/*, {
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
                }
            }*/]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
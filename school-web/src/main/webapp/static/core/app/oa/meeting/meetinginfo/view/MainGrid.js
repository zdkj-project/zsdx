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
        items: [{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
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
    selModel: {
        type: "checkboxmodel",   
        headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        mode:'single',  //multi,simple,single；默认为多选multi
        //checkOnly:false,    //如果值为true，则只用点击checkbox列才能选中此条记录
        //allowDeselect:true, //如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    },
    /** 排序字段定义 */
    defSort: [{
       property: "beginTime", //字段名
       direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: ""
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
        }, {
            flex: 1,
            minWidth:150,
            text: "会议主题",
            dataIndex: "meetingTitle",
            align: 'left'
        },{
            width:80,
            text: "会议类型",
            align: 'left',
            dataIndex: "meetingCategory",
            columnType: "basecombobox", //列类型
            ddCode: "MEETINGCATEGORY" //字典代码
        }, {
            width: 150,
            text: "会议地点",
            dataIndex: "roomName",
            align: 'left'
        }, {
        	width:150,
            text: "开始时间",
            dataIndex: "beginTime",
            align: 'left',
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "开始时间";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d  H:i:s');
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
        	width:150,
            text: "结束时间",
            dataIndex: "endTime",
            align: 'left',
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "结束时间";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d  H:i:s');
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            width: 130,
            text: "更新时间",
            dataIndex: "updateTime",
            renderer: function(value, metaData) {
                if(value){
                    var date = value.replace(new RegExp(/-/gm), "/");    
                    var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i');    
                    return ss;
                } 
                return value;            
            }
        },  {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 200,
            fixed: true,
            align: 'center',
            items: [{
            	text:'编辑',  
                style:'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd:"edit"
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("HYKQMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            },{
                text:'参会人员',
                style:'font-size:12px;',
                tooltip: '参会人员',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('meetingUserClick_Tab', {
                        view:view.grid,
                        record: rec,
                        cmd:"meetingEmp"
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("HYKQMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }, {
            	text:'详细',  
                style:'font-size:12px;',
                tooltip: '详细',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd:"detail"
                    });
                }
            },{
                //iconCls: 'x-fa fa-file-text',
                text:'上传图片',  
                style:'font-size:12px;',  
                tooltip: '上传图片',
                ref: 'gridImageDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('imageDetailClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd:"detail"
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("HYKQMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
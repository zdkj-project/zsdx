Ext.define("core.train.trainee.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.trainee.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTrainee/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainTrainee", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'trainee.mainquerypanel'
    },
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
        }, {
            xtype: 'button',
            text: '导入',
            ref: 'gridImport',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-clipboard'
        }, {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-file'
        }, {
            xtype: 'button',
            text: '下载模板',
            ref: 'gridDownTemplate',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file-text'
        }, '->', {
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
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 80,
            text: "姓名",
            dataIndex: "xm"
        }, {
            width: 50,
            text: "性别",
            dataIndex: "xbm",
            columnType: "basecombobox", //列类型
            ddCode: "XBM" //字典代码			
        }, {
            width: 80,
            text: "民族",
            dataIndex: "mzm",
            columnType: "basecombobox", //列类型
            ddCode: "MZM" //字典代码	
        }, {
            width: 90,
            text: "职务",
            dataIndex: "position",
            renderer: function (value, metaData) {
                var title = "职务";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 90,
            text: "行政级别",
            dataIndex: "headshipLevel",
            columnType: "basecombobox", //列类型
            ddCode: "HEADSHIPLEVEL" //字典代码
        }, {
            width: 100,
            text: "手机号码",
            dataIndex: "mobilePhone"
        }, {
            flex: 1,
            minWidth: 100,
            text: "所在单位",
            dataIndex: "workUnit",
            renderer: function (value, metaData) {
                var title = "所在单位";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 100,
            text: "学员类别",
            dataIndex: "traineeCategory",
            columnType: "basecombobox", //列类型
            ddCode: "TRAINEECATEGORY" //字典代码
        },{
            width: 120,
            text: "学号",
            dataIndex: "traineeNumber",
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
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 130,
            //resizable: false,
            fixed: true,
            align: 'center',
            items: [{
                text: '培训记录',
                style: 'font-size:12px;',
                tooltip: '培训记录',
                ref: 'gridDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('trainRecordClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd:"trainrecord"
                    });
                }
            }, {
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
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("XYMANGER") == -1) {
                        return 'x-hidden-display';
                    } else
                        return null;
                }
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
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("XYMANGER") == -1) {
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
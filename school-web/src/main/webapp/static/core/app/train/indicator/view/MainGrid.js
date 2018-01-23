Ext.define("core.train.indicator.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.indicator.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainIndicatorStand/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainIndicatorStand", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'indicator.mainquerypanel'
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
        }, /*{
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
        },*/ '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'indicatorName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入指标名称'
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
    defSort: [{
       property: "updateTime", //字段名
       direction: "DESC" //升降序
    }/*,{
        property: "indicatorObject", //字段名
        direction: "EDSC" //升降序
    },{
        property: "indicatorName", //字段名
        direction: "DESC" //升降序
    }*/],
    // defGroup:'indicatorName',
    // features: [{ftype:'grouping'}],
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
            width: 300,
            // flex:1,
            text: "指标名称",
            dataIndex: "indicatorName",
        }, {
            width: 150,
            text: "评价类型",
            dataIndex: "indicatorObject",
            renderer: function (value, metaData) {
                var html = value;
                switch (value) {
                    case 1:
                        html = "课程评价";
                        break;
                    case 2:
                        html = "班级评价";
                        break;
                    case 3:
                        html = "课程/班级评价";
                        break;
                }
                return html;
            }
        }, {
            //width: 300,
            flex: 1,
            minWidth:150,
            text: "评价标准",
            dataIndex: "indicatorStand",
            renderer: function (value, metaData) {
                var title = "评价标准";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 130,
            text: "创建时间",
            dataIndex: "createTime",
            renderer: function(value, metaData) {
                if(value){
                    var date = value.replace(new RegExp(/-/gm), "/");    
                    var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i');    
                    return ss;
                } 
                return value;            
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
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 150,
            resizable: false,
            align:'center',
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
                        cmd: "edit"
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
                text: '详情',
                style: 'font-size:12px;',
                tooltip: '详情',
                ref: 'gridEdit',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: "detail"
                    });
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
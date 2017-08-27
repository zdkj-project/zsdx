Ext.define("core.oa.meeting.checkrule.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.checkrule.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/OaMeetingcheckrule/list", //数据获取地址
    model: "com.zd.school.oa.meeting.model.OaMeetingcheckrule", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'checkrule.mainquerypanel'
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
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
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
            text: '启用规则',
            ref: 'gridSetUse',
            funCode: 'girdFuntionBtn',
            //disabled: true,
            iconCls: 'x-fa fa-check-square'
        }, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'ruleName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入规则名称'
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
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {},
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            flex: 1,
            minWidth: 150,
            text: "规则名称",
            dataIndex: "ruleName",
            align: 'left'
        }, {
            width: 120,
            text: "签到提前分钟",
            dataIndex: "inBefore",
            align: 'left'
        }, {
            width: 100,
            text: "迟到分钟",
            dataIndex: "beLate",
            align: 'left'
        }, {
            width: 100,
            text: "缺勤分钟",
            dataIndex: "absenteeism",
            align: 'left'
        }, {
            width: 100,
            text: "是否需要签退",
            dataIndex: "needCheckout",
            renderer: function (value, metaData) {
                if (value == 1)
                    return "<span style='color:green'>是</span>";
                else
                    return "<span style='color:red'>否</span>";
            }
        }, {
            width: 80,
            text: "启用状态",
            dataIndex: "startUsing",
            renderer: function (value, metaData) {
                if (value == 1)
                    return "<span style='color:green'>启用</span>";
                else
                    return "<span style='color:red'>禁用</span>";
            }
        }, {
            flex: 1,
            minWidth: 150,
            text: "规则说明",
            dataIndex: "ruleDesc",
            renderer: function (value, metaData) {
                var html = value;
                var title = '规则说明';
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
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
        },  {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 120,
            fixed: true,
            items: [{
                //iconCls: 'x-fa fa-pencil-square',
                text: '编辑',
                style: 'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec
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
                //iconCls: 'x-fa fa-file-text',
                text: '详细',
                style: 'font-size:12px;',
                tooltip: '详细',
                ref: 'gridDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                //iconCls: 'x-fa fa-minus-circle',
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
                    if (record.get("startUsing")==1||(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("HYKQMANAGER") == -1)){
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
Ext.define("core.train.coursecheckrule.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.coursecheckrule.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainCheckrule/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainCheckrule", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'coursecheckrule.mainquerypanel',
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
            text: '启用',
            ref: 'gridStartUsing',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-star'
        }/*, {
            xtype: 'button',
            text: '导入',
            ref: 'gridImport',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-clipboard'
        }, {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-file'
        }*/, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype:'textfield',
            name:'ruleName',
            funCode:'girdFastSearchText', 
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
            //align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 60,
            text: '序号',
            align: 'center'
        },  {
            width:120,
            text: "规则名称",
            dataIndex: "ruleName",
        }, {
            width:120,
            text: "考勤模式",
            dataIndex: "checkMode",          
            columnType: "basecombobox", //列类型
            ddCode: "CHECKMODE" //字典代码
        }, {
            width:120,
            text: "签到提前分钟",
            dataIndex: "inBefore",
        }, {
            width:120,
            text: "迟到分钟",
            dataIndex: "beLate",
        }, {
            width:120,
            text: "缺勤分钟",
            dataIndex: "absenteeism",
        }, {
            width:100,
            text: "是否启用签退",
            dataIndex: "needCheckout",
            renderer: function(value,metaData) {  
                if(value==1){
                    return "<span style='color:green'>启用签退</span>";
                }else{
                    return "<span style='color:red'>未启用签退</span>";
                }
            }  
        },/* {
            flex: 1,
            text: "签退提前分钟",
            dataIndex: "outBefore",
        }, {
            flex: 1,
            text: "早退分钟",
            dataIndex: "leaveEarly",
        }, {
            flex: 1,
            text: "签退延迟分钟",
            dataIndex: "outLate",
        }, */{
            width:100,
            text: "规则是否启用",
            dataIndex: "startUsing",
            renderer: function(value,metaData) {  
                if(value==1){
                    return "<span style='color:green'>启用规则";
                }else{
                    return "<span style='color:red'>未启用规则</span>";
                }
            }  
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
            align: 'center',
            width: 150,
            fixed: true,
            items: [{
                text:'启用',  
                style:'font-size:12px;', 
                tooltip: '启用',
                ref: 'gridStartUsing',
                getClass :function(v,metadata,record,rowIndex,colIndex,store){
                	 var roleKey = comm.get("roleKey");
                    if(record.get("startUsing")==1||(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1))
                        return 'x-hidden-display';
                    else
                        return null;
                },  
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('startUsingClick', {
                        view: view.grid,
                        record: rec
                    });
                }
            },{
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
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1){
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
                        record: rec
                    });
                }
            }, {
                text:'删除',  
                style:'font-size:12px;', 
                tooltip: '删除',
                ref: 'gridDelete',
                getClass :function(v,metadata,record,rowIndex,colIndex,store){
                	var roleKey = comm.get("roleKey");
                    if(record.get("startUsing")==1||(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("PEIXUNROLE") == -1))
                        return 'x-hidden-display';
                    else
                        return null;
                },  
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
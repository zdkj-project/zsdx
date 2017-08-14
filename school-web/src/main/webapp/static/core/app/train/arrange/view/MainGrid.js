Ext.define("core.train.arrange.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.arrange.maingrid",
    frame: false,
    columnLines: false,

    dataUrl: comm.get("baseUrl") + "/TrainClass/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClass", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'arrange.mainquerypanel'
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '住宿安排',
            ref: 'gridArrangeRoom_Tab',
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
            xtype: 'button',
            text: '场地安排',
            ref: 'gridArrangeSite_Tab',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-pencil-square'
        }, {
            xtype: 'button',
            text: '导出住宿',
            ref: 'gridExportRoom',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-clipboard'
        }, {
            xtype: 'button',
            text: '导出场地',
            ref: 'gridExportSite',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-clipboard'
        },  {
            xtype: 'button',
            text: '正式安排',
            ref: 'gridArrange',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-check-square'
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
            iconCls: 'x-fa fa-search'
        }, {
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }]
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
        //zzk:2017-6-19 若写在这，必须按标准格式编写json字符串（即属性和值使用双引号扩囊）
        filter: '[{"type":"numeric","comparison":"!=","value":0,"field":"isuse"}]' 
    },
    columns: {
        defaults: {
            //align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width:100,
            text: "班级类型",
            dataIndex: "classCategory",
            columnType: "basecombobox", //列类型
            ddCode: "ZXXBJLX" //字典代码
        }, {
            flex:1,
            minWidth:100,
            text: "班级名称",
            dataIndex: "className",
            renderer: function(value, metaData) {                
                var title = "班级名称";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        },/* {
            width:100,
            text: "班级编号",
            dataIndex: "classNumb"
        },  */{
            width:90,
            text: "开始日期",
            dataIndex: "beginDate",
            renderer: function (value, metaData) {
                var date = value.replace(new RegExp(/-/gm),"/");
                var ss = Ext.Date.format(new Date(date), 'Y-m-d');
                return ss;
            }
        }, {
            width:90,
            text: "结束日期",
            dataIndex: "endDate",
            renderer: function (value, metaData) {
                var date = value.replace(new RegExp(/-/gm),"/");
                var ss = Ext.Date.format(new Date(date), 'Y-m-d');
                return ss;
            }
        },{
            width:100,
            text: "班主任",
            dataIndex: "bzrName",
            renderer: function (value, metaData) {
                var title = " 班主任 ";
                metaData.tdAttr = 'data-qtitle="' + title +
                    '" data-qtip="' + value + '"';
                return value;
            }
        }, {
            width:80,
            text: "联系人",
            dataIndex: "contactPerson"
        }, {
            width:100,
            text: "联系电话",
            dataIndex: "contactPhone"
        },{
            width: 130,
            text: "更新时间",
            dataIndex: "updateTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')           
                return ss;
            }
        },{
            width: 80,
            text: "提交状态",
            dataIndex: "isuse",
            align:'center',
            renderer: function(value, metaData) {
                if(value==1)
                    return "<span style='color:green'>已提交</span>";
                else if(value==2)
                    return "<span style='color:#2476FF'>修改未提交</span>";
                else if(value==3)
                    return "<span style='color:green'>修改并提交</span>";
                else
                    return "<span style='color:red'>未提交</span>";
            }
        },{
            width: 80,
            text: "安排状态",
            dataIndex: "isarrange",
            align:'center',
            renderer: function(value, metaData,record ) {
                var isuse = record.get('isuse');
                if(value==1){
                    if(isuse==1)
                        return "<span style='color:green'>安排完毕</span>";
                    else if(isuse==2)
                        return "<span style='color:#2476FF'>等待提交</span>";
                    else if(isuse==3)
                        return "<span style='color:#FFAC00'>可更新安排</span>";
                }
                else
                    return "<span style='color:red'>未安排</span>";
            }
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 250,
            align:'center',
            fixed: true,
            items: [{
                text: '住宿安排',
                style: 'font-size:12px;',
                tooltip: '住宿安排',
                ref: 'gridArrangeSite',
                getClass :function(v,metadata,record){
                	var roleKey = comm.get("roleKey");
                    if(record.get("isarrange")==1&&record.get("isuse")==2||(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1))
                        return 'x-hidden-display';
                    else
                        return null;
                }, 
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridArrangeRoomClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                text: '场地安排',
                style: 'font-size:12px;',
                tooltip: '场地安排',
                ref: 'gridArrangeRoom',
                getClass :function(v,metadata,record){
                	var roleKey = comm.get("roleKey");
                    if(record.get("isarrange")==1&&record.get("isuse")==2||(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1))
                        return 'x-hidden-display';
                    else
                        return null;
                }, 
                handler: function (view, rowIndex,
                                   colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridArrangeSiteClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                text: '就餐详情',
                style: 'font-size:12px;',
                tooltip: '就餐详情',
                ref: 'gridFoodDetail',        
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridFoodDetailClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'detail'
                    });
                }
            }, {
                text:'班级详情',  
                style:'font-size:12px;',  
                tooltip: '班级详情',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }]
    },
});

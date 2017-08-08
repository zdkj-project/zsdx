Ext.define("core.train.class.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.class.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClass/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClass", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'class.mainquerypanel',
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
            }, /*{
                xtype: 'button',
                text: '导入班级',
                ref: 'gridImport',
                funCode: 'girdFuntionBtn',
                disabled: false,
                iconCls: 'x-fa fa-clipboard'
            }, {
                xtype: 'button',
                text: '导入学员',
                ref: 'gridImportTrainee',
                funCode: 'girdFuntionBtn',
                disabled: true,
                iconCls: 'x-fa fa-clipboard'
            },{
                xtype: 'button',
                text: '导入课程',
                ref: 'gridImportCourse',
                funCode: 'girdFuntionBtn',
                disabled: true,
                iconCls: 'x-fa fa-clipboard'
            },*/  {
                xtype: 'button',
                text: '导出',
                ref: 'gridExport',
                funCode: 'girdFuntionBtn',
                disabled: false,
                iconCls: 'x-fa fa-file'
            }, {
                xtype: 'button',
                text: '提交',
                ref: 'gridUse',
                funCode: 'girdFuntionBtn',
                disabled: true,
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
                iconCls: 'x-fa fa-search',
            },
            /*{
                        xtype: 'button',
                        text: '本月',
                        funCode:'girdSearchBtn',
                        ref: 'gridSearchMonth',  
                    },{
                        xtype: 'button',
                        text: '本周',
                        funCode:'girdSearchBtn',          
                        ref: 'gridSearchWeek',   
                    },{
                        xtype: 'button',
                        text: '本日',
                        funCode:'girdSearchBtn',
                        ref: 'gridSearchDay',
                    },{
                        xtype: 'button',
                        text: '全 部',
                        funCode:'girdSearchBtn',
                        ref: 'gridSearchAll',
                        iconCls: 'x-fa fa-star'
                    },*/
            ' ', {
                xtype: 'button',
                text: '高级搜索',
                ref: 'gridHignSearch',
                iconCls: 'x-fa fa-sliders'
            }
        ],
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
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width:100,
            text: "班级类型",
            dataIndex: "classCategory",
            columnType: "basecombobox", //列类型
            ddCode: "ZXXBJLX" //字典代码 
        },{
            flex:1,
            minWidth:100,
            text: "班级名称",
            dataIndex: "className",
            renderer: function(value, metaData) {                
                var title = "班级名称";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        },  {
            width:90,
            text: "开始日期",
            dataIndex: "beginDate",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y-m-d')           
                return ss;
            }
        }, {
            width:90,
            text: "结束日期",
            dataIndex: "endDate",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y-m-d')           
                return ss;
            }
        }, {
            width:120,
            text: "班主任",
            dataIndex: "bzrName",
            renderer: function(value, metaData) {
                var title = "班主任";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        },{
            width:80,
            text: "联系人",
            dataIndex: "contactPerson"
        }, {
            width:100,
            text: "联系电话",
            dataIndex: "contactPhone"
        },  {
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
                    return "<span style='color:green'>修改已提交</span>";
                else
                    return "<span style='color:red'>未提交</span>";
            }
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align:'center',
            width: 330,
            fixed: true,
            items: [{
                text:'学员管理',  
                style:'font-size:12px;',
                tooltip: '学员管理',
                ref: 'gridTraniee',
                // getClass :function(v,metadata,record){
                //     if(record.get("isuse")==1)
                //         return 'x-hidden-display';
                //     else
                //         return null;
                // }, 
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    //var cmd=rec.get("isuse")==1?'detail':'edit';
                    this.fireEvent('gridTranieeClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'detail'
                    });
                }
            },{
                text:'课程管理',  
                style:'font-size:12px;',
                tooltip: '课程管理',
                ref: 'gridCourse',
                // getClass :function(v,metadata,record){
                //     if(record.get("isuse")==1)
                //         return 'x-hidden-display';
                //     else
                //         return null;
                // }, 
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    //var cmd=rec.get("isuse")==1?'detail':'edit';
                    this.fireEvent('gridCourseClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'detail'
                    });
                }
            },{
                text:'就餐申请',  
                style:'font-size:12px;',
                tooltip: '就餐申请',
                ref: 'gridFood',
                // getClass :function(v,metadata,record){
                //     if(record.get("isuse")==1)
                //         return 'x-hidden-display';
                //     else
                //         return null;
                // }, 
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    //var cmd=rec.get("isuse")==1?'detail':'edit';
                    this.fireEvent('gridFoodClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'edit'
                    });
                }
            },{
                text:'住宿申请',  
                style:'font-size:12px;',
                tooltip: '住宿申请',
                ref: 'gridRoom',
                // getClass :function(v,metadata,record){
                //     if(record.get("isuse")==1)
                //         return 'x-hidden-display';
                //     else
                //         return null;
                // }, 
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    //var cmd=rec.get("isuse")==1?'detail':'edit';
                    this.fireEvent('gridRoomClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'edit'
                    });
                }
            }, {
                text:'详情',  
                style:'font-size:12px;',  
                tooltip: '详情',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            },{
                text:'编辑',  
                style:'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridEdit',
                // getClass :function(v,metadata,record){
                //     if(record.get("isuse")!=null&&record.get("isuse")!=0)
                //         return 'x-hidden-display';
                //     else
                //         return null;
                // }, 
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                text:'删除',  
                style:'font-size:12px;',  
                tooltip: '删除',
                ref: 'gridDelete',
                getClass :function(v,metadata,record){
                    if(record.get("isuse")!=null&&record.get("isuse")!=0)
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
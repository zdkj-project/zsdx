Ext.define("core.train.class.view.ClassCourseGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.class.classcoursegrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassschedule/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClassschedule", //对应的数据模型
    al:true,
  
    /**
     * 高级查询面板
     */
    panelButtomBar: null,

    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd_Tab',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '修改',
            ref: 'gridEdit_Tab',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-pencil-square-o'
        },{
            xtype: 'button',
            text: '导入课程信息',
            ref: 'gridImport',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-clipboard'
        },{
            xtype: 'button',
            text: '下载模板',
            ref: 'gridDownTemplate',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file-text'
        }, {
            xtype: 'button',
            text: '打开/关闭评价',
            ref: 'gridIsEval',
            funCode: 'girdFuntionBtn',
            //disabled: true,
            iconCls: 'x-fa fa-pencil-square'
        },{
            xtype: 'button',
            text: '选择场地',   //只能是未安排的班级才能修改
            ref: 'gridSetRoom',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '取消场地',
            ref: 'gridCancelRoom',
            iconCls: 'x-fa fa-minus-circle'
        },{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode: 'girdFuntionBtn',
            //disabled: true,
            iconCls: 'x-fa fa-minus-circle'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'courseName',
            funCode:'girdFastSearchText', 
            isNotForm:true,   //由于文本框重写了baseform下面的funcode值，所以使用这个属性，防止重写这里设定的fundcode值。
            emptyText: '请输入课程名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search',  
        }],
    },
    /** 排序字段定义 */
    defSort: [{
        property: "beginTime", //字段名
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        filter: "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"null\",\"field\":\"classId\"}]" //默认是查不出数据的    
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
        }, /*{
            width:150,
            text: "所属班级",
            dataIndex: "className",
        },*/{
            flex: 1,
            minWidth:100,
            text: "课程名称",
            dataIndex: "courseName"
        }, {
            width:120,
            text: "教学形式",
            dataIndex: "teachTypeName", 
        },{
            width:125,
            text: "开始日期",
            dataIndex: "beginTime",            
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "开始日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            width:125,
            text: "结束日期",
            dataIndex: "endTime",            
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "结束日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        },{
            width:120,
            text: "讲师",
            dataIndex: "mainTeacherName"          
        },/*{
            width:120,
            text: "授课模式",
            dataIndex: "courseMode",
            columnType: "basecombobox", //列类型
            ddCode: "COURSEMODE" //字典代码  
        },*/{
            flex: 1,
            minWidth:100,
            text: "授课地点",
            dataIndex: "scheduleAddress",
            renderer: function(value, metaData) {
                var title = "授课地点";            
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        },{
            width:80,
            text: "是否评价",
            dataIndex: "isEval",
            renderer: function(value, metaData) {
                if(value==1)
                    return "<span style='color:green'>是</span>";
                else
                    return "<span style='color:red'>否</span>";
            }
        },{
            width:80,
            text: "课程学分",
            dataIndex: "credits"
        },{
            width:80,
            text: "是否选修",
            dataIndex: "isOptional",
            renderer: function(value, metaData) {
                if(value==1)
                    return "<span style='color:green'>是</span>";
                else
                    return "<span style='color:red'>否</span>";
            }
        },{
            width: 80,
            text: "课程分班",
            dataIndex: "classGroup",
            columnType: "basecombobox", //列类型
            ddCode: "CLASSGROUP" //字典代码
        },{
            width:80,
            text: "课程状态",
            dataIndex: "isDelete",
            renderer: function(value, metaData) {
                if(value==0)
                    return "<span style='color:green'>正常</span>";
                else if(value==1)
                    return "<span style='color:red'>删除</span>";
                else if(value==2)
                    return "<span style='color:#FFAC00'>新增</span>";            
            }
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 100,
            fixed: true,
            items: [{
                text:'开启评价',  
                style:'font-size:12px;color:green',
                tooltip: '开启评价',
                ref: 'gridOpenEval',
                getClass :function(v,metadata,record){
                    if(record.get("isEval")==1)
                        return 'x-hidden-display';
                    else
                        return null;
                }, 
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('setEvalClick', {
                        view: view.grid,
                        record: rec,
                        value:1
                    });
                }
            },{
                text:'关闭评价',  
                style:'font-size:12px;',
                tooltip: '关闭评价',
                ref: 'gridCloseEval',
                getClass :function(v,metadata,record){
                    if(record.get("isEval")==0)
                        return 'x-hidden-display';
                    else
                        return null;
                }, 
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('setEvalClick', {
                        view: view.grid,
                        record: rec,
                        value:0
                    });
                }
            }, {
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
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
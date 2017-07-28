Ext.define("core.train.course.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.course.maingrid",
    frame:false,
    columnLines: false,    
    dataUrl: comm.get("baseUrl") + "/TrainCourseinfo/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainCourseinfo", //对应的数据模型
    al:true,
    /**
    * 高级查询面板
    */  
    panelButtomBar:{
        xtype:'course.mainquerypanel',
    },    
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
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }/*,{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit_Tab',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        },{
            xtype: 'button',
            text: '详细',
            ref: 'gridDetail',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-file-text'
        }*/,{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-minus-circle'
        },{
            xtype: 'button',
            text: '导入',
            ref: 'gridImport',
            funCode:'girdFuntionBtn',
            disabled:false,
            iconCls: 'x-fa fa-clipboard'
        },{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode:'girdFuntionBtn',
            disabled:false,
            iconCls: 'x-fa fa-file'
        }, {
            xtype: 'button',
            text: '下载模板',
            ref: 'gridDownTemplate',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file-text'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'courseName',
            funCode:'girdFastSearchText', 
            emptyText: '请输入课程名称'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search',  
        },' ',{
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }],
    },
    /** 排序字段定义 */
    defSort: [{
        property:"categoryOrderindex",
        direction:"ASC"
    },{
        property: "orderIndex", //字段名
        direction: "ASC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    columns: {
        defaults:{
            align:'center',
            titleAlign:"center"
        },
        items:[{
            xtype: "rownumberer",
            //flex:0,
            width: 50,
            text: '序号',
            align: 'center'
        },{
            width: 100,
            text: "课程编码",
            dataIndex: "courseCode",
            align:'left'
        },{
            flex:1,
            minWidth:200,
            text: "课程名称",
			dataIndex: "courseName",
            align:'left',
            renderer: function (value, metaData) {
                var title = "课程名称";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        },{
            width: 150,
            text: "所属类别",
            dataIndex: "categoryName",
            align:'left'
        },{
            width: 80,
            text: "教学形式",
            dataIndex:"teachType",
            columnType: "basecombobox", //列类型
            ddCode: "TEACHTYPE", //字典代码
            align:'left'
        }, {
            width: 80,
            text: "教学时长",
			dataIndex: "periodTime",
            align:'left'
		}, {
            width: 80,
            text: "课程学分",
			dataIndex: "credits",
            align:'left'
	    }, {
            width: 160,
            text: "主讲教师",
            dataIndex: "mainTeacherName",
            align:'left',
            renderer: function (value, metaData) {
                var title = "主讲教师";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 160,
            text: "手机号码",
            dataIndex: "mobilePhone",
            align:'left',
            renderer: function (value, metaData) {
                var title = "手机号码";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            width: 150,
            text: "创建时间",
            dataIndex: "createTime",
            align:'left',
        },{
            width: 150,
            text: "更新时间",
            dataIndex: "updateTime",
            align:'left',
        },{
            xtype:'actiontextcolumn',
            text: "操作",
            width:250,
            fixed:true,
            items: [{
                text:'教师简介',
                style:'font-size:12px;',
                tooltip: '教师简介',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridTeacherClick', {
                        view:view.grid,
                        record: rec,
                        cmd:"teacherDesc"
                    });
                }
            },{
                text:'课程简介',
                style:'font-size:12px;',
                tooltip: '课程简介',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridCourseClick', {
                        view:view.grid,
                        record: rec,
                        cmd:"coursDesc"
                    });
                }
            },{
                text:'评价情况',
                style:'font-size:12px;',
                tooltip: '评价情况',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridEvalClick', {
                        view:view.grid,
                        record: rec,
                        cmd:"courseEval"
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
                        view:view.grid,
                        record: rec,
                        cmd:"edit"
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("PEIXUNROLE") == -1&&roleKey.indexOf("TCMANAGER") == -1) {
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            },{
                text:'删除',  
                style:'font-size:12px;',
                tooltip: '删除',
                ref: 'gridDelete',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view:view.grid,
                        record: rec
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("PEIXUNROLE") == -1&&roleKey.indexOf("TCMANAGER") == -1) {
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }]
        }]
    },
	emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
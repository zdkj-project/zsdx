Ext.define("core.train.coursecategory.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.coursecategory.maingrid",
    frame:false,
    columnLines: false,    
    dataUrl: comm.get("baseUrl") + "/TrainCoursecategory/list", //数据获取地址
    model: "com.zd.school.train.coursecategory.model.TrainCourseinfo", //对应的数据模型
    /**
    * 高级查询面板
    */  
    panelButtomBar:{
        xtype:'coursecategory.mainquerypanel',
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
            ref: 'gridAdd',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit',
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
        },{
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
            disabled:true,
            iconCls: 'x-fa fa-clipboard'
        },{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-file'
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
        },/*{
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
        },*/' ',{
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }],
    },
    /** 排序字段定义 */
    //defSort: [{
    //    property: "salaryitemType", //字段名
    //    direction: "asc" //升降序
    //}],
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
            flex:0,
            width: 70,
            text: '序号',
            align: 'center'
        },{
            xtype:'actioncolumn',
            text: "操作",
            width:100,
            fixed:true,
            items: [{
                iconCls: 'x-fa fa-pencil-square',            
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function(view, rowIndex, colIndex, item) {                 
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick', {
                        view:view.grid,
                        record: rec
                    });
                }
            },{
                iconCls:'x-fa fa-file-text',
                tooltip: '详细',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {             
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick', {
                        view:view.grid,
                        record: rec
                    });
                }
            },{
                iconCls:'x-fa fa-minus-circle',
                tooltip: '删除',
                ref: 'gridDelete',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view:view.grid,
                        record: rec
                    });
                }
            }]
        }, {
			flex:1,
            text: "课程名称",
			dataIndex: "courseName",
			        }, {
			flex:1,
            text: "课程编码",
			dataIndex: "courseCode",
			        }, {
			flex:1,
            text: "课时",
			dataIndex: "period",
			        }, {
			flex:1,
            text: "课时时长",
			dataIndex: "periodTime",
			        }, {
			flex:1,
            text: "学分",
			dataIndex: "credits",
			        }]
    },
	emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
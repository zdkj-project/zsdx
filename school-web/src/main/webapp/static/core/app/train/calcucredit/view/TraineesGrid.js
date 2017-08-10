Ext.define('core.train.calcucredit.view.TraineesGrid', {
    extend: "core.base.view.BaseGrid",
    alias: "widget.calcucredit.traineesgrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClasstrainee/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClasstrainee", //对应的数据模型
    al: false,
    style: {
        border: '1px solid #ddd'
    },
    /**
     * 高级查询面板
     */
    panelButtomBar: false,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '学分汇总',
            ref: 'gridSumcredit',
            funCode: 'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }/*, {
            xtype: 'button',
            text: '课程排名',
            ref: 'gridCourseRank',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-file'
        }, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'className',
            funCode: 'girdFastSearchText',
            emptyText: '请输入课程名称'
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
        }*/]
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
        filter: "[{'type':'string','comparison':'=','value':'null','field':'classId'}]" //默认是查不出数据的
    },
    defGroup:false,
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 100,
            text: "姓名",
            dataIndex: "xm"
        }, {
            width: 50,
            text: "性别",
            dataIndex: "xbm",
            columnType: "basecombobox", //列类型
            ddCode: "XBM" //字典代码
        }, {
            width: 100,
            text: "移动电话",
            dataIndex: "mobilePhone"
        },{
            flex:1,
            minWidth: 120,
            text: "所在单位",
            dataIndex: "workUnit"
        },{
            width:120,
            text: "职务",
            dataIndex: "position"
        },{
            width:120,
            text: "行政级别",
            dataIndex: "headshipLevel",
            columnType: "basecombobox", //列类型
            ddCode: "HEADSHIPLEVEL" //字典代码
        },{
            width:100,
            text: "学分 ",
            dataIndex: "realRredit"
        },{
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
            align: 'center',
            width: 100,
            fixed: true,
            items: [{
                text: '详情',
                style: 'font-size:12px;',
                tooltip: '详情',
                ref: 'gridTraniee',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridTraineeCreditClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: 'creditDetail'
                    });
                }
            }/*  {
             text: '导出',
             style: 'font-size:12px;',
             tooltip: '',
             ref: 'gridTraniee',
             getClass: function (v, metadata, record) {
             if (record.get("evalState") === 0 || record.get("evalState") === 2)
             return 'x-hidden-display';
             else
             return null;
             },
             handler: function (view, rowIndex, colIndex, item) {
             var rec = view.getStore().getAt(rowIndex);
             this.fireEvent('mainGridEvalUrlClick_Tab', {
             view: view.grid,
             record: rec,
             cmd: 'readEval'
             });
             }
             }, {
             text: '关闭评价',
             style: 'font-size:12px;',
             tooltip: '关闭评价',
             ref: 'gridTraniee',
             getClass: function (v, metadata, record) {
             if (record.get("evalState") === 2 || record.get("evalState") === 0)
             return 'x-hidden-display';
             else
             return null;
             },
             handler: function (view, rowIndex, colIndex, item) {
             var rec = view.getStore().getAt(rowIndex);
             this.fireEvent('mainGridEndEvalClick_Tab', {
             view: view.grid,
             record: rec,
             cmd: 'startEval'
             });
             }
             }, {
             text: '评价汇总',
             style: 'font-size:12px;',
             tooltip: '评价汇总',
             ref: 'gridCourse',
             getClass: function (v, metadata, record) {
             if (record.get("evalState") === 1 || record.get("evalState") === 0)
             return 'x-hidden-display';
             else
             return null;
             },
             handler: function (view, rowIndex, colIndex, item) {
             var rec = view.getStore().getAt(rowIndex);
             //var cmd=rec.get("isuse")==1?'detail':'edit';
             this.fireEvent('mainGridSumEvalClick_Tab', {
             view: view.grid,
             record: rec,
             cmd: 'sumEval'
             });
             }
             }*/]
        }]
    }
    /*    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>',
     dockedItems: [{
     xtype: 'pagingtoolbar',
     plugins: [
     Ext.create('Ext.ux.ComboPageSize', {
     pageSizes: [20, 50, 100, 1000, 10000]
     })
     ],
     store: Ext.data.StoreManager.lookup('courseeval.maingridstore'),   // same store GridPanel is using
     dock: 'bottom',
     displayInfo: true
     }, {
     xtype: 'panel',
     ref: 'evalClassInfoPanel',
     height: 40,
     bodyStyle: {
     backgroundColor: '#dfe9f6',
     border: '1px solid #99bce8',
     padding: '5px 0px 0px 5px',
     color:'red',
     fontSize:'18px'
     },
     dock: 'bottom',
     items: [{
     xtype: 'label',
     ref: 'label1',
     text: '班级信息：0',
     padding: '0 10 0 5'
     }, {
     xtype: 'button',
     ref: 'btnPreview',
     text: '预览评价',
     padding: '0 10 0 5',
     style:{
     fontSize:'14px'
     }
     }]
     //html:'合计：'
     }]*/
});
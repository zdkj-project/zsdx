/*
* 班级学员表
*/
Ext.define("core.train.class.view.ClassStudentGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.class.classstudentgrid",
    al:true,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClasstrainee/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClasstrainee", //对应的数据模型
    
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '导入学员信息',
            ref: 'gridImport',
            funCode:'girdFuntionBtn',
            disabled:false,
            iconCls: 'x-fa fa-clipboard'
        },{
            xtype: 'button',
            text: '同步班级学员到学员库',
            ref: 'gridSyncTrainee',
            funCode:'girdFuntionBtn',
            disabled:false,
            iconCls: 'x-fa fa-rss'
        },{
            xtype: 'button',
            text: '下载模板',
            ref: 'gridDownTemplate',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file-text'
        }, {
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
            name:'xm',
            funCode:'girdFastSearchText', 
            isNotForm:true,   //由于文本框重写了baseform下面的funcode值，所以使用这个属性，防止重写这里设定的fundcode值。
            emptyText: '请输入姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search',  
        }],
    },
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
     
    /** 排序字段定义 */
    defSort: [{
        property: "createTime", //字段名
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
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 100,
            text: "姓名",
            dataIndex: "xm",
        }, {
            width: 80,
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
            width: 120,
            text: "职务",
            dataIndex: "position",
        }, {
            width: 100,
            text: "行政级别",
            dataIndex: "headshipLevel",
            columnType: "basecombobox", //列类型
            ddCode: "HEADSHIPLEVEL" //字典代码
        },/* {
            width: 180,
            text: "身份证件号",
            dataIndex: "sfzjh",
        }, */{
            width: 120,
            text: "手机号码",
            dataIndex: "mobilePhone",
        }, {
            flex: 1,
            minWidth: 150,
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
            width: 150,
            text: "更新时间",
            dataIndex: "updateTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')           
                return ss;
            }
        },
        {
            width:80,
            text: "学员状态",
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
            width: 60,
            fixed: true,
            items: [ {
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
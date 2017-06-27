Ext.define("core.oa.roomterminal.view.listGrid", {
    extend: "core.base.view.BaseGrid",
    alias: 'widget.roomterminal.listgrid',
    dataUrl: comm.get("baseUrl") + "/OaInfoterm/list", //数据获取地址
    model: "com.zd.school.oa.terminal.model.OaInfoterm", //对应的数据模型
    fileName:"信息终端",//导出Excel文件名称
    exWhereSql:" and isUse=1", //导出Excel 过滤
    //工具栏操作按钮
    tbar: [{
        xtype: 'button',
        text: '分配',
        ref: 'gridAdd',
        iconCls:  'x-fa fa-plus-circle'
    }, {
        xtype: 'button',
        text: '取消',
        ref: 'gridDelete',
        iconCls: 'x-fa fa-pencil-square',
        disabled: true
    }, {
        xtype: 'button',
        text: '导出Excel',
        ref: 'exportExcel',
        iconCls: 'x-fa fa-minus-circle'
    }],
	panelTopBar:false,
	panelButtomBar:false,
    //排序字段
    defSort: [{
        property: "termCode", //排序字段
        direction: "ASC" //升降充
    }],
    //分组字段
    defGroup: [],
    //扩展参数
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段
        filter: "[{'type':'string','comparison':'=','value':'0','field':'roomId'}]"
    },
    columns: {
        defaults:{
            //flex:1,
            //align:'center',
            titleAlign:"center"
        },
        items: [ {
            text: "终端号",
            flex:1,
            //width:100,
            dataIndex: "termCode",
            renderer: function(value, metaData) {
                var title = "终端号";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return value;
            }
        }, {
            text: "类型",
            width:100,
            //flex:1,
            dataIndex: "termType",
            columnType: "basecombobox", //列类型
            ddCode: "INFOTERTYPE" //字典代码
        }, {
            text: "规格",
            //flex:1,
            width:100,
            dataIndex: "termSpec",
            renderer: function(value, metaData) {
                var title = "规格";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return value;
            }
        }, {
            text: "分配人",
            //flex:1,
            width:100,
            dataIndex: "updateUser",
            renderer: function(value, metaData) {
                var title = "分配人";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return value;
            }
        }, {
            text: "房间名称",
            //flex:1,
            width:100,
            dataIndex: "roomName",
            renderer: function(value, metaData) {
                var title = "房间名称";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return value;
            }
        }, {
            text: "门牌号",
            //flex:1,
            width:100,
            dataIndex: "houseNumb",
            renderer: function(value, metaData) {
                var title = "门牌号";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return value;
            }
        },{
            width: 100,    
            text: "操作",
            xtype: "actiontextcolumn",
            ref: "roomterminaldetail",
            align: "center",
            sortable: false,
            fixed: true,
            items: [{ 
                text:'取消',  
                style:'font-size:12px;',    
                tooltip: "取消",
                handler: function(grid, rowIndex, colIndex, item) {
                    this.fireEvent("detailClick", grid, "delete", rowIndex);
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'    
})
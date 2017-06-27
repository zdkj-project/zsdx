Ext.define("core.oa.roomterminal.view.selectGrid", {
    extend: "core.base.view.BaseGrid",
    alias: 'widget.roomterminal.selectGrid',
    dataUrl: comm.get("baseUrl") + "/OaInfoterm/list", //数据获取地址
    model: "com.zd.school.oa.terminal.model.OaInfoterm", //对应的数据模型
    //工具栏操作按钮
    tbar: null,
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
        filter: "[{'type':'numeric','comparison':'=','value':'0','field':'isUse'}]"
    },
    columns: [{
        text: "终端号",
        dataIndex: "termCode",
        renderer: function(value, metaData) {
            var title = "终端号";
            var html = value;
            metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
            return value;
        }
    }, {
        text: "类型",
        dataIndex: "termType",
        columnType: "basecombobox", //列类型
        ddCode: "INFOTERTYPE" //字典代码
    }, {
        text: "规格",
        dataIndex: "termSpec",
        renderer: function(value, metaData) {
            var title = "规格";
            var html = value;
            metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
            return value;
        }
    }],
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'    
})
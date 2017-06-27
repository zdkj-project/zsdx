Ext.define("core.oa.terminal.view.readonlyForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.terminal.readonlyform",
    xtype: 'terminalreadonlyform',
    layout: "form", //从上往下布局
    frame: false,
    bodyPadding: '0 10 0 10',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    items: [{
        title: "基本信息",
        xtype: "fieldset",
        //collapsible: "true",
        height: 160,
        items: [{
            fieldLabel: "终端号",
            name: "termCode",
            xtype: "textfield",
            emptyText: "请输入规格",
            maxLength: 32,
            maxLengthText: "最多32个字符,汉字占2个字符"
        }, {
            beforeLabelTextTpl: "",
            allowBlank: false,
            blankText: "终端类型不能为空",
            fieldLabel: "终端类型",
            name: "termType",
            xtype: "basecombobox",
            ddCode: "INFOTERTYPE",
            emptyText: "请选择终端类型",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符"
        }, {
            fieldLabel: "规格",
            name: "termSpec",
            xtype: "textfield",
            emptyText: "请输入规格",
            maxLength: 32,
            maxLengthText: "最多32个字符,汉字占2个字符"
        }]
    }, {
        title: '分配历史',
        xtype: 'fieldset',
        //collapsible: "true",
        height: 320,
        layout: "fit",
        items: [{
            padding: '2 0 10 0',
            xtype: "basegrid",
            ref: 'termUseGrid',
            noPagging: true,
            selModel: null,
            tbar: false,
            panelBottomBar:false,
            panelTopBar:false,
            dataUrl: comm.get("baseUrl") + "/OaInfotermuse/list", //数据获取地址
            model: "com.zd.school.oa.terminal.model.OaInfotermuse", //对应的数据模型 
            //排序字段
            defSort: [{
                property: "createTime", //排序字段
                direction: "DESC" //升降充
            }],
            //height:
            columns: [{
                xtype: "rownumberer",
                width: 50,
                text: '序号',
                align: 'center'
            }, {
                text: "分配房间",
                dataIndex: "roomName",
                width: 80,
                type: "string",
                sortable: false,
                menuDisabled: true,
                renderer: function(value, metaData) {
                    var title = "房间名称";
                    var html = value;
                    metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                    return value;
                }
            }, {
                text: "分配时间",
                dataIndex: "createTime",
                width: 150,
                sortable: false,
                menuDisabled: true,
                type: "string",
                renderer: function(value, metaData) {
                    var date = value.replace(new RegExp(/-/gm), "/");
                    var title = "分配时间";
                    var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i:s')
                    var html = ss;
                    metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                    return ss;
                }
            }, {
                text: "分配人",
                dataIndex: "createUser",
                sortable: false,
                menuDisabled: true,
                type: "string",
                renderer: function(value, metaData) {
                    var title = "分配人";
                    var html = value;
                    metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                    return value;
                }
            }],
            emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
        }]
    }]
});
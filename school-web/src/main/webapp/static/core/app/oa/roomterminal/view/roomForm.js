Ext.define("core.oa.roomterminal.view.roomForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.roomterminal.roomform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    items: [{
        xtype: "textfield",
        fieldLabel: "主键",
        name: "roomId",
        hidden: true
    }, {
        beforeLabelTextTpl: comm.get('required'),
        xtype: "textfield",
        fieldLabel: "房间名称",
        name: "roomName",
        readOnly: true
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "终端ID1",
            name: "termId1",
            hidden: true
        }, {
            columnWidth: 0.4,
            fieldLabel: "门牌号1",
            xtype: "textfield",
            name: "houseNumb1",
            readOnly: true
        }, {
            columnWidth: 0.6,
            fieldLabel: "分配终端",
            xtype: "basefuncfield",
            name: "termCode1",
            //funcController: "core.oa.terminal.controller.MainController", //该功能主控制器
            funcPanel: "terminal.mainlayout", //该功能显示的主视图
            
            funcTitle: "选择终端", //查询窗口的标题
            configInfo: {
                fieldInfo: "termId1~termCode1,uuid~termCode",
                whereSql: " and 1=1 and isDelete='0' and isUse='0' ",
                filter: "[{'type':'numeric','comparison':'=','value':'0','field':'isUse'}]",
                muiltSelect: false //是否多选
            }
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "终端ID2",
            name: "termId2",
            hidden: true
        }, {
            columnWidth: 0.4,
            xtype: "textfield",
            fieldLabel: "门牌号2",
            name: "houseNumb2",
            readOnly: true
        }, {
            columnWidth: 0.6,
            fieldLabel: "分配终端",
            xtype: "basefuncfield",
            name: "termCode2",
            funcController: "core.oa.terminal.controller.MainController", //该功能主控制器
            funcPanel: "terminal.mainlayout", //该功能显示的主视图
            funcTitle: "选择终端", //查询窗口的标题
            configInfo: {
                fieldInfo: "termId2~termCode2,uuid~termCode",
                whereSql: " and 1=1 and isDelete='0' ",
                filter: "[{'type':'numeric','comparison':'=','value':'0','field':'isUse'}]",
                muiltSelect: false //是否多选
            }
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "终端ID3",
            name: "termId3",
            hidden: true
        }, {
            columnWidth: 0.4,
            xtype: "textfield",
            fieldLabel: "门牌号3",
            name: "houseNumb3",
            readOnly: true
        }, {
            columnWidth: 0.6,
            fieldLabel: "分配终端",
            xtype: "basefuncfield",
            name: "termCode3",
            funcController: "core.oa.terminal.controller.MainController", //该功能主控制器
            funcPanel: "terminal.mainlayout", //该功能显示的主视图
            funcTitle: "选择终端", //查询窗口的标题
            configInfo: {
                fieldInfo: "termId3~termCode3,uuid~termCode",
                whereSql: " and 1=1 and isDelete='0' ",
                filter: "[{'type':'numeric','comparison':'=','value':'0','field':'isUse'}]",
                muiltSelect: false //是否多选
            }
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "终端ID4",
            name: "termId4",
            hidden: true
        }, {
            columnWidth: 0.4,
            xtype: "textfield",
            fieldLabel: "门牌号4",
            name: "houseNumb4",
            readOnly: true
        }, {
            columnWidth: 0.6,
            fieldLabel: "分配终端",
            xtype: "basefuncfield",
            name: "termCode4",
            funcController: "core.oa.terminal.controller.MainController", //该功能主控制器
            funcPanel: "terminal.mainlayout", //该功能显示的主视图
            funcTitle: "选择终端", //查询窗口的标题
            configInfo: {
                fieldInfo: "termId4~termCode4,uuid~termCode",
                whereSql: " and 1=1 and isDelete='0' ",
                filter: "[{'type':'numeric','comparison':'=','value':'0','field':'isUse'}]",
                muiltSelect: false //是否多选
            }
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "终端ID5",
            name: "termId5",
            hidden: true
        }, {
            columnWidth: 0.4,
            xtype: "textfield",
            fieldLabel: "门牌号5",
            name: "houseNumb5",
            readOnly: true
        }, {
            columnWidth: 0.6,
            fieldLabel: "分配终端",
            xtype: "basefuncfield",
            name: "termCode5",
            funcController: "core.oa.terminal.controller.MainController", //该功能主控制器
            funcPanel: "terminal.mainlayout", //该功能显示的主视图
            funcTitle: "选择终端", //查询窗口的标题
            configInfo: {
                fieldInfo: "termId5~termCode5,uuid~termCode",
                whereSql: " and 1=1 and isDelete='0' and isUse='0' ",
                filter: "[{'type':'numeric','comparison':'=','value':'0','field':'isUse'}]",
                muiltSelect: false //是否多选
            }
        }]
    }]
});
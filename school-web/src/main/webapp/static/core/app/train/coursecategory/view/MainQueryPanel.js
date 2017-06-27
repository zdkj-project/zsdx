Ext.define("core.train.coursecategory.view.MainQueryPanel", {
    extend: "core.base.view.BaseQueryForm",
    alias: "widget.coursecategory.mainquerypanel",
    layout: "form",
    frame: false,
    height: 180,
    items: [{
        xtype: "container",
        layout: "column",
        items: [{
            //columnWidth: 0.5,
            labelAlign: "right",
            xtype: "basequeryfield",
            name: "courseName",
            fieldLabel: "课程名称",
            queryType: "textfield",
            queryType: "textfield",
        }, {
            //columnWidth: 0.5,
            labelAlign: "right",
            xtype: "basequeryfield",
            name: "courseCode",
            fieldLabel: "课程编码",
            queryType: "textfield",
            queryType: "textfield",

        }]
    }],
    buttonAlign: "center",
    buttons: [{
        xtype: 'button',
        text: '搜 索',
        ref: 'gridSearchFormOk',
        iconCls: 'x-fa fa-search',
    }, {
        xtype: 'button',
        text: '重 置',
        ref: 'gridSearchFormReset',
        iconCls: 'x-fa fa-undo',
    }]
});
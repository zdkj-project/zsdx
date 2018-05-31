Ext.define("core.mjmanage.roomallot.view.SelectTeacherLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.mjmanage.roomallot.selectteacherlayout',
    funCode: "roomallot_detail",
    border: false,
    funData: {
        action: comm.get('baseUrl') + "/BaseOfficeAllot", //请求controller
        pkName: "uuid", //主键
        //默认的初始值设置
        defaultObj: {
        }
    },

    minWidth:1200,
    scrollable:'x',
    layout:'border',
    items: [{
        xtype:'mjmanage.roomallot.selectteachergrid',
        region: "west",
        width: comm.get("clientWidth") * 0.43,
        margin:'5'
    }, {
        xtype: "mjmanage.roomallot.isselectteachergrid",
        region: "center",
        margin:'5'
    }]
})

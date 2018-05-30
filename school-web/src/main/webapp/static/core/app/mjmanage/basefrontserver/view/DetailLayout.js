Ext.define("core.mjmanage.basefrontserver.view.DetailLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.mjmanage.basefrontserver.detaillayout',
    funCode: "basefrontserver_detail",
    funData: {
        action: comm.get('baseUrl') + "/SysFrontServer", //请求Action
        pkName: "uuid",
        modelName: "com.zd.school.build.define.model.SysFrontServer", //实体全路径
    },
     /*设置最小宽度，并且自动滚动*/
    minWidth:1000,
    scrollable:true,

    /*关联此视图控制器*/
    controller: 'mjmanage.basefrontserver.detailcontroller',
    items: [{
        xtype: "mjmanage.basefrontserver.detailform"
    }]
})
Ext.define("core.mjmanage.basegateway.view.DetailLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.mjmanage.basegateway.detaillayout',
    funCode: "basegateway_detail",
    funData: {
        action: comm.get('baseUrl') + "/PtGateway", //请求Action
        pkName: "uuid",
       // modelName: "com.zd.school.control.device.model.PtGateway", //实体全路径
        defaultObj: {}
    },
    /*关联此视图控制器*/
    controller: 'mjmanage.basegateway.detailcontroller',
    /*设置最小宽度，并且自动滚动*/
    minWidth:1000,
    scrollable:true,
    items: [{
        xtype: "mjmanage.basegateway.detailform"
    }]
})
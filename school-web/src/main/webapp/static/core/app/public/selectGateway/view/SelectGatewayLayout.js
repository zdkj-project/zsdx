
Ext.define("core.public.selectGateway.view.SelectGatewayLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.public.selectGateway.selectgatewaylayout',
    controller: 'public.selectGateway.selectcatewaycontroller',
    
    /** 页面代码定义 */
    funCode: "selectgateway_main",
    layout:'border',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/BaseGateway", //请求Action
        pkName: "uuid"
    },
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    items: [{
        xtype:'public.selectGateway.selectgatewaygrid',
        //width:600,
        flex:1,
        region: "center",
        margin:'5'
    }, {
        xtype: "public.selectGateway.isselectgatewaygrid",
        region: "east",
        width:600,
        margin:'5'
    }]
})
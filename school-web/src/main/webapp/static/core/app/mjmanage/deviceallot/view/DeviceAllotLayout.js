Ext.define("core.mjmanage.deviceallot.view.DeviceAllotLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.mjmanage.deviceallot.deviceallotlayout',
    layout:'border',
    border: false,
    
    funData: {
        action: comm.get('baseUrl') + "/PtTerm", //请求Action
        pkName: "uuid",
        modelName: "com.zd.school.control.device.model.PtTerm", //实体全路径
        defaultObj: {
            roomId:"",
            leaf:true
        },
    },
    items: [{
    	split: true,//对模块分开的线条
        xtype: "mjmanage.deviceallot.roominfotree2",
        width: 250,
        region: "west",
    }, {
    	flex:1,
    	split: true,
        xtype: "mjmanage.deviceallot.deviceallotgrid",
        region: "center",
        //width:600,
    }, {
    	split: true,
    	width: 360,
    	xtype: "mjmanage.deviceallot.devicesysgrid",
        region: "east",
    }]
})
Ext.define("core.mjmanage.deviceallot.view.RoominfoTree2", {
	extend: "core.base.view.BaseTreeGrid",
    alias: "widget.mjmanage.deviceallot.roominfotree2",
    dataUrl: comm.get('baseUrl') + "/MjUserright/treelist",
    model: "com.zd.school.build.define.model.BuildRoomAreaTree",
    al: true,
    expandFirst:true,
    forceFit:true,
    sortableColumns:false,
    multiSelect: false,
    selModel: {
      
    },
    extParams: {
        whereSql: "",
        orderSql: "",
        excludes:"checked"
    },
    tbar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '区域信息',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        }, '->',{
            xtype: 'button',
            text: '刷新',
            ref: 'gridRefresh',
            iconCls: 'x-fa fa-refresh'
        }]
    },
    columns:  {        
        defaults:{
            titleAlign:"center"
        },
        items: [{
            text: "区域名称",
            dataIndex: "text",
            xtype: 'treecolumn',
        }, {
            text: "主键",
            dataIndex: 'id',
            hidden: true
        }]
    },
    
    listeners: {
        itemclick: function (tree, record, item, index, e, eOpts) {
            /*不需要点击树
            var mainLayout = tree.up("panel[xtype=mjmanage.deviceallot.deviceallotlayout]");
            var funData = mainLayout.funData;
            var roomId=record.get("id");
            var leaf = record.get("leaf");
            if(leaf==true){
                // mainLayout.funData = Ext.apply(funData, {
                //     roomId: roomId,
                //     leaf : record.get("leaf"),//true: 房间 false:区域
                //     arealevel: record.get("level"),
                // });
                // 加载信息
                
                var mianGrid = mainLayout.down("panel[xtype=mjmanage.deviceallot.devicesysgrid]");
                var store = mianGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams.roomId=roomId;
                store.loadPage(1); 
                
                 // 加载信息
                var allotGrid = mainLayout.down("panel[xtype=mjmanage.deviceallot.deviceallotgrid]");
                allotGrid.getStore().loadPage(1); 
            }
            return false;
            */
        }
    }
   
});
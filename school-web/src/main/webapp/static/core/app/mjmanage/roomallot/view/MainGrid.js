Ext.define("core.mjmanage.roomallot.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.roomallot.maingrid",
    dataUrl: comm.get('baseUrl') + "/BaseOfficeAllot/list",
    model: "com.zd.school.build.allot.model.JwOfficeAllot",
    extParams: {
    },
    al:false,
    menuCode:"BASEROOMALLOT", //new：此表格与权限相关的菜单编码
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '数据列表',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        },'->',{
            xtype: 'button',
            text: '分配房间',
            ref: 'allotOffRoom',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '解除设置',
            ref: 'gridDelete',
            iconCls: 'x-fa fa-minus-circle',
            disabled:true,
        }/*,{
            xtype: 'button',
            text: '推送消息',
            ref: 'officeTs',
            iconCls: 'x-fa fa-paper-plane',
        }*/]
    }, 
   panelButtomBar:{},
   defSort: [{
        property: 'updateTime',
        direction: 'DESC'
    },/*{
        property: 'createTime',
        direction: 'DESC'
    }*/],
   columns:  {        
    defaults:{
            titleAlign:"center"
        },
    items:[{
        xtype: "rownumberer",
        width: 60,
        text: '序号',
        align: 'center'
    }, {
        text: "主键",
        dataIndex: "uuid",
        hidden:true
    }, {
        text: "教师老师主键",
        dataIndex: "tteacId",
        hidden:true
    }, {
        text: "房间主键",
        dataIndex: "roomId",
        hidden:true
    }, {
        flex: 1,
        minWidth: 120,
        text: "老师姓名",
        dataIndex: "xm",
    }, {
        flex: 1,
        minWidth: 120,
        text: "房间名",
        dataIndex: "roomName",
    }, {
        flex: 1,
        minWidth: 120,
        text: "所属楼层",
        dataIndex: "areaName",
    }, {
        flex: 1,
        minWidth: 120,
        text: "所属楼栋",
        dataIndex: "upAreaName",
    },{
        xtype: 'actiontextcolumn',
        text: "操作",
        align: 'center',
        width: 100,
        fixed: true,
        items: [{
            text:'解除设置',  
            style:'font-size:12px;', 
            tooltip: '解除设置',
            ref: 'gridDelete',
            getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("TCMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
            },
            handler: function(view, rowIndex, colIndex, item) {
                var rec = view.getStore().getAt(rowIndex);
                this.fireEvent('deleteClick', {
                    view: view.grid,
                    record: rec
                });
            }
        }]
    }]
}
});
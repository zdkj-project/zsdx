Ext.define("core.mjmanage.roomuserauthority.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.roomuserauthority.maingrid",
    dataUrl: comm.get("baseUrl") + "/BaseMjUserright/roomUserRightList", //数据获取地址
    model:"com.zd.school.teacher.teacherinfo.model.ViewUserRoom", //对应的数据模型
    menuCode:"ROOM_ACCESS_CONTROL",
    al: false,
    //pageDisplayInfo:false,
    //工具栏操作按钮
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
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'XM',
            funCode: 'girdFastSearchText',
            emptyText: '请输入姓名'
        },/*{
            xtype:'textfield',
            name:'termName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入设备名称'
        },*/{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型  
            iconCls: 'x-fa fa-search',  
        }]
    },
    defSort: [{
        property: 'ROOM_CODE',
        direction: 'ASC'
    }],
    panelButtomBar:{},
    //扩展参数
    extParams: {
        //orderSql :"order by USER_ID"
    },
    columns: {        
        defaults:{
            titleAlign:"center",
            align:'center'
        },
        items: [{
        xtype: "rownumberer",
        width: 50,
        text: '序号',
        align: 'center'
    }, {
        text: "主键",
        dataIndex: "uuid",
        hidden: true
    }, {
        text: "用户ID",
        dataIndex: "user_ID",
        hidden: true
    },{
        text: "用户姓名",
        dataIndex: "xm",
        minWidth:100,
        flex:1,
    },{
        text: "房间编号",
        dataIndex: "room_CODE",
        minWidth:80,
        flex:0.8,
    },{
        text: "房间名称",
        dataIndex: "room_NAME",
        minWidth:80,
        flex:0.8,
    },{
        text: "房间类型",
        dataIndex: "room_TYPE",
        minWidth:80,
        flex:0.8,
    }/*{
        text: "房间名称",
        dataIndex: "roomName",
        minWidth:100,
        flex:1,
    },{
        text: "设备名称",
        dataIndex: "termName",
        minWidth:100,
        flex:1,
    },{
        text: "设备序列号",
        dataIndex: "termSN",
        minWidth:100,
        flex:1,
    }*/]
    }
});
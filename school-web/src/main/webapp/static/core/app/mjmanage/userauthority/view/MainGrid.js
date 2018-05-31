Ext.define("core.mjmanage.userauthority.view.MainGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.mjmanage.userauthority.maingrid",
	dataUrl: comm.get("baseUrl") + "/BaseMjUserright/mjrightlist", //数据获取地址
	model:"com.zd.school.teacher.teacherinfo.model.ViewUserRoom", //对应的数据模型
    menuCode:"USER_ACCESS_CONTROL",
    al: false,
    pageDisplayInfo:false,
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
        },/*{使用门禁记录的方式
            xtype:'textfield',
            name:'termName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入设备名称',
            width:120
        }*/{
            xtype:'textfield',
            name:'roomName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入房间名称',
            width:120
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型  
            iconCls: 'x-fa fa-search',  
        }]
    },
    defSort: [{
        property:'ROOM_NAME',//'termId',
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
        minWidth:80,
        flex:0.8,
    },{
        text: "设备名称",
        dataIndex: "termName",
        minWidth:80,
        flex:0.8,
    },{
        text: "设备序列号",
        dataIndex: "termSN",
        minWidth:120,
        flex:1,
    }*/]
    }
});
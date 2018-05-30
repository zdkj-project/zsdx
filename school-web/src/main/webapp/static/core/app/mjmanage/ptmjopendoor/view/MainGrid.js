Ext.define("core.mjmanage.ptmjopendoor.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.ptmjopendoor.maingrid",
    dataUrl: comm.get('baseUrl') + "/PtMjOpenDoor/list",
    model: "com.zd.school.control.device.model.PtMjOpenDoor",
    menuCode:"PtMjOpenDoor",
    al: false,
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
        },{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-file-excel-o'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype: "basequeryfield",
            queryType: "datetimefield",
            dateType:'date',        //指定这个组件的格式，date或者datetime
            name: "openDate",
            funCode: 'girdFastSearchText',
            emptyText: '开门的起始时间',
            //format: "Y-m-d",
        },{
            xtype: "basequeryfield",
            queryType: "datetimefield",
            dateType:'date',        //指定这个组件的格式，date或者datetime
            name: "openDate",
            funCode: 'girdFastSearchText',
            emptyText: '开门的终止时间',
            //format: "Y-m-d",
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型  
            iconCls: 'x-fa fa-search',  
        }]
    },
    defSort: [{
        property: 'openDate',
        direction: 'DESC'
    }],
    panelButtomBar:{},
    //扩展参数
    extParams: {
        filter: '[{"type":"string","comparison":"!=","value":"0","field":"roomType"}]'  //只显示已定义的房间
    },
    columns: {        
        defaults:{
            titleAlign:"center"
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
        text: "房间名称",
        dataIndex: "roomName",
        field: {
            xtype: "textfield"
        },
        flex:1,
        minWidth:100
    }, {
        text: "设备名称",
        dataIndex: "termName",
        field: {
            xtype: "textfield"
        },
        flex:1,
        minWidth:100
    }, {
        text: "开门人员姓名",
        dataIndex: "userName",
        field: {
            xtype: "textfield"
        },
        flex:1,
        minWidth:100
    }, {
        text: "开门时间",
        dataIndex: "openDate",
        field: {
            xtype: "textfield"
        },
        width:140
    }, {
        text: "房间所在区域",
        dataIndex: "roomArea",
        field: {
            xtype: "textfield"
        },
        flex:2,
        minWidth:200
    }, {
        text: "进出标识",
        dataIndex: "inoutType",
        field: {
            xtype: "textfield"
        },
        width:80
    }, {
        text: "开门类型",
        dataIndex: "openType",
        field: {
            xtype: "textfield"
        },
        width:100
    }]
    }
});
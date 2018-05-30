Ext.define("core.mjmanage.deviceallot.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.deviceallot.maingrid",
    dataUrl: comm.get('baseUrl') + "/PtTerm/list",
    model: "com.zd.school.control.device.model.PtTerm",
    al:false,
    menuCode:"DEVICEALLOT",
    extParams: {filter: '[{"type":"string","comparison":"=","value":"4","field":"termTypeID"}]'},
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '已存在设备',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        },'->',{
            xtype: 'button',
            text: '分配设备',
            ref: 'gridAdd_Tab',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit_Tab',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        },{
            xtype: 'button',
            text: '移除',
            msg:'是否要移除此设备？',
            ref: 'gridDelete',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-minus-circle'
        },{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-file-excel-o'
        },{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'termSN',
            funCode: 'girdFastSearchText',
            emptyText: '请输入序列号'
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型 
            iconCls: 'x-fa fa-search',  
        },' ',{
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }],
    }, 
    panelButtomBar:{
        xtype:'mjmanage.deviceallot.mainquerypanel'
    },
    //排序字段及模式定义
    defSort: [{
        property: 'updateTime',
        direction: 'DESC'
    }/*{
        property: 'createTime',
        direction: 'DESC'
    }*/],
    extParams: {},
    columns:  {        
        defaults:{
            titleAlign:"center"
        },
        items: [{
            xtype: "rownumberer",
            width: 50,
            text: '序号',
            align: 'center'
        },{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        }, {
            text: "序列号",
            dataIndex: "termSN",
            width: 120,
        },{
            text: "机号",
            dataIndex: "termNo",
            width: 120,
        }, {
            text: "设备名称",
            dataIndex: "termName",
            flex:1,
            minWidth:100,
        }, {
            text: "房间名称",
            dataIndex: "roomName",
            hidden: true
        }, {
            text: "网关名称",
            dataIndex: "gatewayName",
            width: 120,	
        }, {
            text: "设备类型",
            dataIndex: "termTypeID",
            columnType: "basecombobox", //列类型
            ddCode: "PTTERMTYPE", //字典代码
            width: 120,
        }, {
            text: "设备状态",
            dataIndex: "termStatus",
            width: 120,
            renderer: function(value) {
                switch (value) {
                    case 0:
                        return '<font color=red>禁用</font>';
                        break;
                    case 1:
                        return '<font color=green>启用</font>';
                        break;
                }
            }
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 200,
            fixed: true,
            items: [{
                text:'编辑',  
                style:'font-size:12px;', 
                tooltip: '编辑',
                ref: 'gridEdit',
                getClass :function(v,metadata,record,rowIndex,colIndex,store){                            
                    if(comm.get("isAdmin")!="1"){
                        var menuCode="DEVICEALLOT";     // 此菜单的前缀
                        var userBtn=comm.get("userBtn");                 
                        if(userBtn.indexOf(menuCode+"_gridEdit_Tab")==-1){
                            return 'x-hidden-display';
                        }
                    }
                    return null; 
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            },{
                text:'移除',  
                style:'font-size:12px;', 
                tooltip: '移除此设备',
                ref: 'gridDelete',
                getClass :function(v,metadata,record,rowIndex,colIndex,store){                            
                    if(comm.get("isAdmin")!="1"){
                        var menuCode="DEVICEALLOT";     // 此菜单的前缀
                        var userBtn=comm.get("userBtn");                 
                        if(userBtn.indexOf(menuCode+"_gridDelete")==-1){
                            return 'x-hidden-display';
                        }
                    }
                    return null; 
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view: view.grid,
                        record: rec,
                        msg:'是否要移除此设备？'
                    });
                }
            }]
        }]
    }    
});
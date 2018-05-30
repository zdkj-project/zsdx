
Ext.define("core.mjmanage.smartdevice.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.smartdevice.maingrid",
    dataUrl: comm.get('baseUrl') + "/PtTerm/list",
    model: "com.zd.school.control.device.model.PtTerm",
    al:false,
    menuCode:"BASESMARTDEVICE", //new：此表格与权限相关的菜单编码

    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '智能设备列表',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        },/*{
            xtype: 'button',
            text: '导入房间与设备对应表',
            ref: 'gridExcel',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        },*/ /*{
            xtype: 'button',
            text: '同步数据',
            ref: 'sync',
            funCode:'girdFuntionBtn',         
            iconCls: 'x-fa fa-rss'
        },*/'->',{
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
            name:'termName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入设备名称'
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型 
            iconCls: 'x-fa fa-search',  
        }],
    }, 
    panelButtomBar:null,
    
    //排序字段及模式定义
    defSort: [{
        property: 'updateTime',
        direction: 'DESC'
    }/*,{
        property: 'createTime',
        direction: 'DESC'
    }*/],

    extParams: {
        whereSql: " termTypeID='4' "
    },
    columns:  {        
        defaults:{
            titleAlign:"center"
        },
        items: [{
            xtype: "rownumberer",
            flex:0,
            width: 50,
            text: '序号',
            align: 'center'
        },{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        },{
            flex: 1.5,
            minWidth: 150,
            text: "设备序列号",
            dataIndex: "termSN",            
        }, {
            flex: 1.5,
            minWidth: 150,
            text: "设备名称",
            dataIndex: "termName",            
        }, {     
            flex: 1,
            minWidth: 100,
            text: "网关名称",
            dataIndex: "gatewayName"
        }, {
            flex: 1,
            minWidth: 100,
            text: "所属房间",
            dataIndex: "roomName",
        }, {
            width: 100,
            text: "设备类型",
            dataIndex: "termTypeID",
            columnType: "basecombobox", //列类型
            ddCode: "PTTERMTYPE" //字典代码
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 150,
            fixed: true,
            items: [{
                text:'高级参数',  
                style:'font-size:12px;', 
                tooltip: '设置高级参数',
                ref: 'gridSetHigh',
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("TCMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('setHighParamClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                text:'基础参数',  
                style:'font-size:12px;', 
                tooltip: '设置基础参数',
                ref: 'gridSetBase',
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("TCMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('setBaseParamClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }]
    }    
});

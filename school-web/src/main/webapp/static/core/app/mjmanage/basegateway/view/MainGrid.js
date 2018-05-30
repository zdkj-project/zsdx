Ext.define("core.mjmanage.basegateway.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.basegateway.miangrid",
    dataUrl: comm.get('baseUrl') + "/PtGateway/list",
    model: "com.zd.school.control.device.model.PtGateway",
    forceFit: false,
    al: false,
    extParams: {
     // filter: '[{"type":"string","comparison":"=","value":"0","field":"isDelete"}]'
    },
    menuCode:"BASEGATEWAY", //new：此表格与权限相关的菜单编码
    //title: "数据列表",
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
            text: '批量设置前置',
            ref: 'gridSetFront',
            iconCls: 'x-fa fa-cogs',
            disabled:true,
        },/*{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd_Tab',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, */{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit_Tab',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        }, {
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-minus-circle'
        }],
    }, 
    panelButtomBar:{},
   //排序字段及模式定义
    defSort: [/*{
        property: 'createTime',
        direction: 'DESC'
    },*/{
        property: 'updateTime',
        direction: 'DESC'
    },{
        property: "gatewayNo", //字段名
        direction: "ASC" //升降序
    }],
 
    columns:  {        
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
        },{
            text: "前置主键",
            dataIndex: "frontserverId",
            hidden: true
        }, {
            text: "机号",
            dataIndex: "gatewayNo",
            field: {
                xtype: "textfield"
            },
            width: 120,
        }, {
            text: "网关名称",
            dataIndex: "gatewayName",
            field: {
                xtype: "textfield"
            },
            width: 120,
        }, {
            width: 120,
            text: "前置名称",
            dataIndex: "frontServerName",
            field: {
                xtype: "textfield"
            }
        }, {
            width: 120,
            text: "序列号",
            dataIndex: "gatewaySN",
            field: {
                xtype: "textfield"
            }
        }, {
            flex: 1,
            minWidth:150,
            text: "网关IP",
            dataIndex: "gatewayIP",
            field: {
                xtype: "textfield"
            }
        }, {
            width: 80,
            text: "网关状态",
            dataIndex: "gatewayStatus",
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
        }, {
            width: 120,
            text: "前置IP",
            dataIndex: "frontServerIP",
            field: {
                xtype: "textfield"
            }
        }, {
            width: 100,
            text: "前置端口",
            dataIndex: "frontServerPort",
            field: {
                xtype: "textfield"
            }
        }, {
            width: 80,
            text: "前置状态",
            dataIndex: "frontServerStatus",
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
        }, {
            width: 120,
            text: "备注",
            dataIndex: "notes",
            field: {
                xtype: "textfield"
            },
            renderer:function(value,metaData){
                var title="备注";
                metaData.tdAttr= 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;  

            }
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 200,
            fixed: true,
            items: [{
                text:'设备参数',  
                style:'font-size:12px;', 
                tooltip: '设备参数',
                ref: 'buttonBaseAndHigh',
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("TCMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('baseAndHighClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }/*, {
                注：2018-3-8 何工暂时不采用这里的方式进行网络参数设定，暂时隐藏此功能
                text:'网络参数',  
                style:'font-size:12px;', 
                tooltip: '网络参数',
                ref: 'buttonNetWork',
                getClass :function(v,metadata,record,rowIndex,colIndex,store){                            
                    if(comm.get("isAdmin")!="1"){
                        var menuCode="BASEGATEWAY";     // 此菜单的前缀
                        var userBtn=comm.get("userBtn");                 
                        if(userBtn.indexOf(menuCode+"_buttonNetWork")==-1){
                            return 'x-hidden-display';
                        }
                    }
                    return null; 
                },
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('netWorkClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }*/]
        }]
   },

});
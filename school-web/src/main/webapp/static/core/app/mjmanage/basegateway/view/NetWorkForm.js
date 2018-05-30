var combostore = new Ext.data.ArrayStore({
    fields: ['id', 'dhcp'],
    data: [
        [0, '禁用'],
        [1, '启用']
    ]
});
Ext.define("core.mjmanage.basegateway.view.NetWorkForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.basegateway.networkform",
    items: [{
        xtype: "fieldset",
        layout:'form',
        title: '网关参数',
        style: {
            fontSize: '16px',
            color: '#C44444',
            fontWeight:400,
            border: '#097db5 1px solid'
        },
        defaults:{
            width:'100%',
            margin:"10 5 0 5",
            xtype: "textfield",
            //labelAlign : 'right',
            //columnWidth : 0.5,
            //msgTarget: 'qtip',
        },
        items:[{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                beforeLabelTextTpl: comm.get("required"),
                allowBlank: false,
                columnWidth:0.6,
                fieldLabel: '网关IP',
                xtype: 'textfield',
                name: 'tlvs[0].valStr',
                vtype:'ipCode'
            }, {
                columnWidth: 0.4,
                xtype: "label",
                html: "  <font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>（网关IP格式为：xxx.xxx.xxx.xxx）</font>",
            }]
        }, {
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                beforeLabelTextTpl: comm.get("required"),
                allowBlank: false,
                columnWidth:0.6,
                fieldLabel: 'MAC',
                xtype: 'textfield',
                name: 'tlvs[6].valStr',
                vtype:'macCode'           
            }, {
                columnWidth: 0.4,
                xtype: "label",
                html: "  <font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>（MAC格式为：FF-FF-FF-FF-FF-FF，第一组必须为偶数）</font>",
            }]
        },{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                beforeLabelTextTpl: comm.get("required"),
                allowBlank: false,
                columnWidth:0.6,
                fieldLabel: '掩码',
                xtype: 'textfield',
                name: 'tlvs[2].valStr',
                vtype:'ipCode'
            }, {
                columnWidth: 0.4,
                xtype: "label",
                html: "  <font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>（掩码IP格式为：xxx.xxx.xxx.xxx）</font>",
            }]
        },{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [ {
                beforeLabelTextTpl: comm.get("required"),
                allowBlank: false,
                columnWidth:0.6,
                fieldLabel: '接入网关',
                xtype: 'textfield',
                name: 'tlvs[1].valStr',
                vtype:'ipCode'
            }, {
                columnWidth: 0.4,
                xtype: "label",
                html:"  <font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>（网关IP格式为：xxx.xxx.xxx.xxx）</font>",
            }]
        }]        
    },{
        xtype: "fieldset",
        layout:'form',
        title: '服务器参数',
        style: {
            fontSize: '16px',
            color: '#C44444',
            fontWeight:400,
            border: '#097db5 1px solid'
        },
        defaults:{
            width:'100%',
            margin:"10 5 0 5",
            xtype: "textfield",
            //labelAlign : 'right',
            //columnWidth : 0.5,
            //msgTarget: 'qtip',
        },
        items:[{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,
                beforeLabelTextTpl: comm.get("required"),
                xtype: "combobox",
                store: combostore,
                fieldLabel: "DHCP",
                displayField: 'dhcp',
                valueField: 'id',
                name: "tlvs[5].valInt",
                value: 1,
                triggerAction: 'all',
                emptyText: '请选择...',
                blankText: '请选择',
                editable: false,
                mode: 'local',
                readOnly: true
            }]
        },{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,
                beforeLabelTextTpl: comm.get("required"),            
                allowBlank: false,
                fieldLabel: '服务器IP',
                xtype: 'textfield',
                name: 'tlvs[3].valStr',
                vtype:'ipCode',
                readOnly: true        
            },{
                columnWidth: 0.4,
                xtype: "label",
                html: "  <font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>（服务器IP格式为：xxx.xxx.xxx.xxx）</font>"
            }]
        },{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{ 
                columnWidth:0.6,
                beforeLabelTextTpl: comm.get("required"),
                allowBlank: false,
                fieldLabel: '服务器Port',
                xtype: 'numberfield',
                name: 'tlvs[4].valInt',
                value: 1,
                minValue: 1,
                maxValue: 65335,
                readOnly: true
            }]
        },{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,        
                fieldLabel: 'SSID密码',
                xtype: 'textfield',
                readOnly: true
            }]
        }]
    }],



    formData: {
        "tlvs[0].len": 4, //设备IP地址  HostIP  0x3002  4
        "tlvs[0].type": 6,
        "tlvs[0].tag": 0x3002,
        "tlvs[1].len": 4, //设备网关地址  HostGW  0x3004  4
        "tlvs[1].type": 6,
        "tlvs[1].tag": 0x3004,
        "tlvs[2].len": 4, //设备子网掩码  HostMask        4
        "tlvs[2].type": 6,
        "tlvs[2].tag": 0x3005,
        "tlvs[3].len": 4, //服务器IP地址 ServerIP        4
        "tlvs[3].type": 6,
        "tlvs[3].tag": 0x3000,
        "tlvs[4].len": 2, //服务器端口   serverPort      2
        "tlvs[4].type": 1,
        "tlvs[4].tag": 0x3001,
        "tlvs[5].len": 1, //DHCP使能位 DhcpEnable      1
        "tlvs[5].type": 1,
        "tlvs[5].tag": 0x3008,
        "tlvs[6].len": 6, //设备MAC值  HostMac 0x3003  6
        "tlvs[6].type": 7,
        "tlvs[6].tag": 0x3003
    }
});
Ext.define("core.mjmanage.basegateway.view.BaseAndHighForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.basegateway.baseandhighform",
    items :[{
        xtype: "fieldset",
        title: '基础参数',
        layout:'form',
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
        items: [{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,
                name: 'tlvs[1].valInt',
                fieldLabel: '心跳间隔：',
                xtype: 'numberfield',
                value: 60,
                maxValue: 300,
                minValue: 1,
                allowDecimals: false
            }, {
                columnWidth: 0.4,
                xtype: "label",
                html: "&nbsp;秒 <font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>(取值范围1-300)</font>",
            }]
        }, {
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,
                fieldLabel: '通讯超时：',
                name: 'tlvs[0].valInt',
                xtype: 'numberfield',
                value: 5000,
                maxValue: 60000,
                minValue: 5000,
                allowDecimals: false
            }, {
                columnWidth: 0.4,
                xtype: "label",
                html: "&nbsp;毫秒 <font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>(取值范围5000-60000)</font>",
            }]
        }],
   },  {
       xtype: "fieldset",
       title: '高级参数',
       layout:'form',
       style: {
            fontSize: '16px',
            color: '#C44444',
            fontWeight:400,
            border: '#097db5 1px solid'
        },
        defaults:{
            width:'100%',
            margin:"10 5 0 5",
            xtype: "textfield"
        },
        items :[{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,
                xtype: 'timefield',
                name: 'time1',
                format: 'H:i',
                fieldLabel: '重启时间1',
                minValue: '00:00',
                maxValue: '23:59',
                increment: 10,
                value:'00:00',
                 allowBlank: false,
                anchor: '100%',
                invalidText: "时间格式不正确，例如：00:00"
            },{
                columnWidth: 0.4,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间1（每天到时间点就重启，0代表不重启）</font>",
            }]
        },{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{ 
                columnWidth: 0.6,
                xtype: 'timefield',
                name: 'time2',
                format: 'H:i',
                fieldLabel: '重启时间2',
                minValue: '00:00',
                maxValue: '23:59',
                increment: 10,
                value:'00:00',
                anchor: '100%',
                allowBlank: false,
                invalidText: "时间格式不正确，例如：00:00"
            },{
                columnWidth: 0.4,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间2</font>",
            }]
        },
        {
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,
                xtype: 'timefield',
                name: 'time3',
                format: 'H:i',
                fieldLabel: '重启时间3',
                minValue: '00:00',
                maxValue: '23:59',
                increment: 10,
                value:'00:00',
                anchor: '100%',
                allowBlank: false,
                invalidText: "时间格式不正确，例如：00:00"
            },{
                columnWidth: 0.4,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间3</font>",
            }]
        },
        {
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.6,
                xtype: 'timefield',
                name: 'time4',
                format: 'H:i',
                fieldLabel: '重启时间4',
                minValue: '00:00',
                maxValue: '23:59',
                increment: 10,
                value:'00:00',
                anchor: '100%',
                allowBlank: false,
                invalidText: "时间格式不正确，例如：00:00"
            },{
                columnWidth: 0.4,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间4</font>",
            }]
        }]
    }, {
       xtype: "fieldset",
       title: '批量设置其他网关',
       layout:'form',
       style: {
            fontSize: '16px',
            color: '#C44444',
            fontWeight:400,
            border: '#097db5 1px solid'
        },
        defaults:{
            width:'100%',
            margin:"10 5 0 5",
            xtype: "textfield"
        },
        items:[{
            xtype: 'radiogroup',
            ref:'gatewayRadio',
            fieldLabel: '批量设置',
            columns: 5,
            vertical: true,
            items: [            
                { boxLabel: '不批量设置', name: 'gatewayRadio', inputValue: 0,checked: true  },
                { boxLabel: '指定网关', name: 'gatewayRadio', inputValue: 1},
                { boxLabel: '当前服务器的所有网关', name: 'gatewayRadio', inputValue: 2},
            ],
            listeners:{
                change:function( filed, newValue, oldValue, eOpts ){
                    var form=filed.up("form");                      
                    var formBase=form.getForm();
                    var gatewayField = formBase.findField("gatewayNames");
                    
                    if(newValue.gatewayRadio==1){                                    
                        gatewayField.show();                                   
                    }else {
                        gatewayField.hide();                                              
                    }
                    //console.log(newValue);
                }
            }
            
        },{
            fieldLabel: "指定网关",
            name: "gatewayIds",
            xtype: "textfield",
            hidden: true
        }, {
            hidden:true,
            xtype: "basefuncfield",         
            funcPanel: "public.selectGateway.selectgatewaylayout", //该功能显示的主视图
            funcGrid:'public.selectGateway.isselectgatewaygrid',    //指定多选时，获取数据的表格别名
            //refController:'system.dept.othercontroller',             //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
            formPanel: "system.dept.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "网关选择", //查询窗口的标题
            configInfo: {
                width:1250,
                height:600,
                fieldInfo: "gatewayIds~gatewayNames,uuid~gatewayName",
                whereSql: " where isDelete=0",
                //orderSql: " order by jobCode ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '选择网关',
            name: "gatewayNames",
            allowBlank: true,
            emptyText: "选择需要设置的网关",
        }]
    }],
       
    baseFormData: {
        "tlvs[0].len": 4,//TCP接收超时时间
        "tlvs[0].type": 1,
        "tlvs[0].tag": 0x1010,
        
        "tlvs[1].len": 4,//设备心跳间隔
        "tlvs[1].type":1,
        "tlvs[1].tag": 0x1006
    },
    highFormData: {
        "tlvs[0].len": 8,//设备重启时间列表
        "tlvs[0].type": 2,
        "tlvs[0].tag": 0x2000
    }
});
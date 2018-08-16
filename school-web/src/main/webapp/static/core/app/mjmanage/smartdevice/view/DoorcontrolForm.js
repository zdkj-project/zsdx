Ext.define("core.mjmanage.smartdevice.view.DoorcontrolForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.smartdevice.doorcontrolform",
    autoScroll: true,
    items :[ {
        xtype: "fieldset",
        title: '门禁基础参数',
        layout:'hbox',
        width:'99%',
        style: {
            fontSize: '16px',
            color: '#C44444',
            fontWeight:400,
            border: '#097db5 1px solid'
        },
        // defaults:{
        //     width:'100%',
        //     margin:"10 5 0 5",
        //     xtype: "textfield"            
        // },
        items: [{           
            margin:"5 5 5 0",
            flex: 1,
            xtype: "fieldset",
            height:315,
            title: null,
            layout:'form',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:400,
                border: '#ececec 1px solid'
            },
            defaults:{
                width:'100%',
                margin:"10 5 0 5",
                xtype: "textfield",
                labelWidth:80
            },
            items: [{
                xtype: "container",
                layout: "column", // 从左往右的布局
                items: [{
                    columnWidth: 0.5,
                    fieldLabel: '心跳间隔',
                    labelWidth:80,
                    xtype: 'numberfield',
                    name: 'tlvs[0].valInt',
                    value: 60,
                    maxValue: 300,
                    minValue: 1,
                    allowBlank: false,
                    allowDecimals: false
                }, {
                    columnWidth: 0.5,
                    xtype: "label",
                    html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>秒（取值范围1-300）</font>"
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '卡片类型',
                //name: 'tlvs[1].valInt',
                vertical: true,
                items: [{
                    width:100,
                    boxLabel: 'IC卡',
                    inputValue: 0,
                    name: 'tlvs[1].valInt',
                    checked: true
                }, {
                    width:100,
                    boxLabel: 'CPU卡',
                    name: 'tlvs[1].valInt',
                    inputValue: 1,
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '认证方式',
                //name: 'tlvs[5].valInt',
                vertical: true,
                items: [{
                    width:100,
                    boxLabel: '物理卡号',
                    name: 'tlvs[5].valInt',
                    inputValue: 0,
                    checked: true
                }, {
                    width:100,
                    boxLabel: '卡流水号',
                    inputValue: 1,
                    name: 'tlvs[5].valInt',
                    checked: true,
                    hidden:true
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '自动锁卡',
                //name: 'tlvs[4].valInt',
                vertical: true,
                items: [{
                    width:100,
                    boxLabel: '不锁',
                    name: 'tlvs[4].valInt',
                    inputValue: 0
                }, {
                    width:100,
                    boxLabel: '锁卡',
                    inputValue: 1,
                    name: 'tlvs[4].valInt',
                    checked: true
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '未关门报警',
                //name: 'tlvs[10].valInt',
                vertical: true,
                items: [{
                    width:100,
                    boxLabel: '不报警',
                    inputValue: 0,
                    name: 'tlvs[10].valInt',
                    checked: true
                }, {
                    width:100,
                    boxLabel: '报警',
                    name: 'tlvs[10].valInt',
                    inputValue: 1,
                }]
            }, {
                xtype: "container",
                layout: "column", // 从左往右的布局
                items: [{
                    columnWidth: 0.5,
                    labelWidth:80,
                    fieldLabel: '开门延迟',
                    xtype: 'numberfield',
                    name: 'tlvs[9].valInt',
                    value: 3,
                    maxValue: 300,
                    minValue: 1,
                    allowBlank: false,
                    allowDecimals: false
                }, {
                    columnWidth: 0.5,
                    xtype: "label",
                    html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>秒（取值范围1-300）</font>"
                }]
            }, {
                xtype: "container",
                layout: "column", // 从左往右的布局
                items: [{
                    columnWidth:0.5,
                    labelWidth:80,
                    fieldLabel: '关门延时',
                    xtype: 'numberfield',
                    name: 'tlvs[11].valInt',
                    value: 3,
                    maxValue: 300,
                    minValue: 1,
                    allowBlank: false,
                    allowDecimals: false
                }, {
                    columnWidth: 0.5,
                    xtype: "label",
                    html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>秒（取值范围1-300）</font>"
                }]
            }]
        },{           
            margin:"5 5 5 0",
            flex: 1,
            xtype: "fieldset",
            height:315,
            title: null,
            layout:'form',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:400,
                border: '#ececec 1px solid'
            },
            defaults:{
                width:'100%',
                margin:"10 5 0 5",
                xtype: "textfield",
                labelWidth:80
            },
            items: [{
                xtype: "container",
                layout: "column", // 从左往右的布局
                items: [{
                    columnWidth: 0.5,
                    fieldLabel: '通讯超时',
                    labelWidth:80,
                    name: 'tlvs[6].valInt',
                    xtype: 'numberfield',
                    value: 5000,
                    maxValue: 60000,
                    minValue: 5000,                
                    allowBlank: false,
                    allowDecimals: false
                }, {
                    columnWidth: 0.5,
                    xtype: "label",
                    html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>毫秒 （取值范围5000-60000）</font>"     
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '卡片有效期',
                //name: 'tlvs[7].valInt',
                vertical: true,
                items: [{
                    width: 100,
                    boxLabel: '不启用',
                    inputValue: 0,
                    name: 'tlvs[7].valInt',
                    checked: true
                }, {
                    width: 100,
                    boxLabel: '启用',
                    name: 'tlvs[7].valInt',
                    inputValue: 1,
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '门状态',
                //name: 'tlvs[3].valInt',            
                vertical: true,
                items: [{
                    width: 100,
                    boxLabel: '常闭',
                    name: 'tlvs[3].valInt',
                    inputValue: 0,
                    checked: true
                }, {
                    width: 100,
                    boxLabel: '常开',
                    inputValue: 1,
                    name: 'tlvs[3].valInt',
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '首卡开门',
                //name: 'tlvs[8].valInt',
                vertical: true,
                items: [{
                    width: 100,
                    boxLabel: '不支持',
                    inputValue: 0,
                    name: 'tlvs[8].valInt',
                    checked: true
                }, {
                    width: 100,
                    boxLabel: '支持',
                    name: 'tlvs[8].valInt',
                    inputValue: 1,
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '刷卡提示',
                //name: 'tlvs[12].valInt',
                vertical: true,
                items: [{
                    width: 100,
                    boxLabel: '禁用',
                    name: 'tlvs[12].valInt',
                    inputValue: 0,
                }, {
                    width: 100,
                    boxLabel: '启用',
                    name: 'tlvs[12].valInt',
                    inputValue: 1,
                    checked: true
                }]
            }, {
                xtype: 'radiogroup',
                fieldLabel: '锁状态检测',
                //name: 'tlvs[13].valInt',
                vertical: true,
                items: [{
                    width: 100,
                    boxLabel: '启用',
                    name: 'tlvs[13].valInt',
                    inputValue: 0,
                }, {
                    width: 100,
                    boxLabel: '不启用',
                    name: 'tlvs[13].valInt',
                    inputValue: 1,
                    checked: true
                }]
            }]
        }]
    },{
        width:'99%',
        xtype: "fieldset",
        title: '允许卡类',
        layout:'column',
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
            labelWidth:80
        },
        items: [{
            width:100,
            xtype: "button",
            text: "全选（反选）",
            ref: "checkall",        
        },{
            xtype: 'checkboxgroup',
            columns: 8,
            vertical: true,
            ref: 'KrateForm_lblOperationBehaviors',
            items: [],
            listeners: {
                render: function(component) {
                    for (var i = 1; i <= 32; i++) {
                        var checkbox = new Ext.form.Checkbox({
                            boxLabel: i + '类卡', //"Title"指的是返回的名字.
                            width: 100,
                            height: 25,
                            name: 'tlvs[2].valStr',
                            inputValue: i,
                            checked: true
                        });
                        
                        component.items.add(checkbox);
                    }
                }
            }
        }]
    },{
       width:'99%',
       xtype: "fieldset",
       title: '批量设置此类设备',
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
            labelWidth:80
        },
        items:[{
            xtype: 'radiogroup',
            ref:'termRadio',
            fieldLabel: '批量设置',
            columns: 6,
            vertical: true,
            items: [            
                { boxLabel: '不批量设置', name: 'termRadio', inputValue: 0,checked: true , width:100 },        
                { boxLabel: '本楼层', name: 'termRadio', inputValue: 4},   //以下4个数据，对应QYLX区域类型的编号
                { boxLabel: '本楼栋', name: 'termRadio', inputValue: 3},
                { boxLabel: '本校区', name: 'termRadio', inputValue: 2},
                { boxLabel: '本学校', name: 'termRadio', inputValue: 1},
               // { boxLabel: '指定设备', name: 'termRadio', inputValue: 5}
            ],
            listeners:{
                // change:function( filed, newValue, oldValue, eOpts ){
                //     var form=filed.up("form");                      
                //     var formBase=form.getForm();
                //     var termField = formBase.findField("termNames");
                    
                //     if(newValue.termRadio==5){                                    
                //         termField.show();                                   
                //     }else {
                //         termField.hide();                                              
                //     }
                //     //console.log(newValue);
                // }
            }
            
        }/*,{
            fieldLabel: "指定设备",
            name: "termIds",
            xtype: "textfield",
            hidden: true
        }, {
            hidden:true,
            xtype: "basefuncfield",         
            funcPanel: "public.selectGateway.selectgatewaylayout", //该功能显示的主视图
            funcGrid:'public.selectGateway.isselectgatewaygrid',    //指定多选时，获取数据的表格别名
            //refController:'system.dept.othercontroller',             //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
            formPanel: "mjmanage.smartdevice.highparamform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "设备选择", //查询窗口的标题
            configInfo: {
                width:1250,
                height:600,
                fieldInfo: "termIds~termNames,uuid~termIds",
                whereSql: " and isDelete='0' ",
                //orderSql: " order by jobCode ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '选择设备',
            name: "termNames",
            allowBlank: true,
            emptyText: "选择需要设置的设备",
        }*/]
    }],

    baseFormData: {
        "tlvs[0].len": 2, //心跳间隔时间 Ok .
        "tlvs[0].type": 1,
        "tlvs[0].tag": 0x1006,
        "tlvs[1].len": 1, //设备允许读的卡类 r1
        "tlvs[1].type": 1,
        "tlvs[1].tag": 0x1016,
        "tlvs[2].len": 4, //允许使用的卡类  namei
        "tlvs[2].type": 4,
        "tlvs[2].tag": 0x1015,
        "tlvs[3].len": 1, //锁类型 r6
        "tlvs[3].type": 1,
        "tlvs[3].tag": 0x1018,
        "tlvs[4].len": 1, //自动锁卡标记 r3
        "tlvs[4].type": 1,
        "tlvs[4].tag": 0x1019,
        "tlvs[5].len": 1, //黑白名单判断标记 r2
        "tlvs[5].type": 1,
        "tlvs[5].tag": 0x1021,
        "tlvs[6].len": 2, //通讯超时时间
        "tlvs[6].type": 1,
        "tlvs[6].tag": 0x1010,
        "tlvs[7].len": 1, //用户卡有效期判断启用标记 r5
        "tlvs[7].type": 1,
        "tlvs[7].tag": 0x1017,
        "tlvs[8].len": 1, //支持首卡开门标记 r7
        "tlvs[8].type": 1,
        "tlvs[8].tag": 0x9000,
        "tlvs[9].len": 2, //刷卡开门延迟时间 OK
        "tlvs[9].type": 1,
        "tlvs[9].tag": 0x9001,
        "tlvs[10].len": 1, //启用门未关报警 r4
        "tlvs[10].type": 1,
        "tlvs[10].tag": 0x9002,
        "tlvs[11].len": 2, //未关门判断延迟时间 OK 
        "tlvs[11].type": 1,
        "tlvs[11].tag": 0x9003,
        "tlvs[12].len": 1, //蜂鸣器使能  r8
        "tlvs[12].type": 1,
        "tlvs[12].tag": 0x9004,
        "tlvs[13].len": 1, //卡机绑定标记 r9
        "tlvs[13].type": 1,
        "tlvs[13].tag": 0x9009,
    }
});
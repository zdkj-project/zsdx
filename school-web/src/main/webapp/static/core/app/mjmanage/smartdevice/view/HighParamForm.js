Ext.define("core.mjmanage.smartdevice.view.HighParamForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.mjmanage.smartdevice.highparamform",
    autoScroll: false,
    items :[ {
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
                columnWidth: 0.4,
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
                columnWidth: 0.6,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间1（每天到时间点就重启，0代表不重启）</font>",
            }]
        },{
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{ 
                columnWidth: 0.4,
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
                columnWidth: 0.6,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间2</font>",
            }]
        },
        {
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.4,
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
                columnWidth: 0.6,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间3</font>",
            }]
        },
        {
            xtype: "container",
            layout: "column", // 从左往右的布局
            items: [{
                columnWidth: 0.4,
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
                columnWidth: 0.6,
                xtype: "label",
                html: "<font style='color: rgb(196, 68, 68);font-size: 14px;line-height: 30px;padding-left: 10px;'>设备自动重启的时间4</font>",
            }]
        }]
    }, {
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
            xtype: "textfield"
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

    highFormData: {
        "tlvs[0].len": 8,//设备重启时间列表
        "tlvs[0].type": 2,
        "tlvs[0].tag": 0x2000
    }
});
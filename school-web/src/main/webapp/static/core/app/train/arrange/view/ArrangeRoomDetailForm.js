Ext.define("core.train.arrange.view.ArrangeRoomDetailForm", {
    extend: "core.base.view.BaseForm",
    requires: [
        'core.train.arrange.store.ArrangeRoomGridStore',    //在该组件内，若不写此代码，则会在使用此组件时才加载（若写了，当加载此组件时，也加载requires里面的）
    ],
    alias: "widget.arrange.roomdetailform",
    //layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '10 25 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    layout:'vbox',
    items:[{
        xtype:'container',
        layout: "form", //从上往下布局
        height:80,
        defaults:{
            width:'100%',
            margin:"10 5",
            xtype : 'textfield',
            labelAlign : 'right',
            //columnWidth : 0.5,
            msgTarget: 'qtip',
        },
        items: [{
            fieldLabel: "主键",
            name: "uuid",
            xtype: "textfield",
            hidden: true
        }, {
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{          
                columnWidth: 0.5,
                readOnly:true,
                fieldLabel: "班级名称",    
                name: "className",
                xtype: "textfield"    
            },{           
                columnWidth: 0.5,
                readOnly:true,
                fieldLabel: "班级编号",    
                name: "classNumb",
                xtype: "textfield"  
            }]
        },{
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{           
                fieldLabel: "开始日期",
                columnWidth: 0.5,
                name: "beginDate",
                xtype: "datefield",
                editable:false,
                readOnly:true,
                //dateType: 'date',
                format: "Y-m-d",
                formatText:'',
                emptyText: "请输入开始日期",

                vtype:'beginDate',
                compareField:'endDate'
                /*
                maxLength: 10,
                maxLengthText: "最多8个字符,汉字占2个字符",*/
            }, {           
                fieldLabel: "结束日期",
                columnWidth: 0.5,
                name: "endDate",
                xtype: "datefield",
                editable:true,
                readOnly:true,
                //dateType: 'date',
                format: "Y-m-d",
                formatText:'',
                emptyText: "请输入结束日期",
                maxLength: 10,
                maxLengthText: "最多10个字符,汉字占2个字符",

                vtype:'endDate',
                compareField:'beginDate'
            }]
        }]
    },{
        xtype: "container",
        layout: "fit",
        labelAlign: "right",
        flex:1,
        minHeight: 300,
        items: [{
            xtype:'grid',
            ref:'arrangeRoomGrid',
            selModel: {
                type: "checkboxmodel",   
                headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他               
            },

            frame:false,
            margin:'0 0 0 20',
            style:{
                border: '1px solid #ddd'
            },
            viewConfig: {
                stripeRows: false   //不展示隔行变色
            },
            columnWidth:1,
            //height:500,
            store:{
                type:"arrange.arrangeroomgridStore"
            },
            emptyText:'<span style="display: block;text-align:center">没有需要显示的数据！</span>',
            tbar:[{
                xtype: 'button',
                text: '选择宿舍',
                ref: 'gridSetRoom',
                iconCls: 'x-fa fa-plus-circle'
            },{
                xtype: 'button',
                text: '取消宿舍',
                ref: 'gridCancelRoom',
                iconCls: 'x-fa fa-minus-circle'
            },{
                xtype: 'tbtext',
                html: '（每间宿舍最多只能安排三人，并且同性别）',
                style: {
                    fontSize: '14px',
                    color: '#C44444',
                    fontWeight:800
                }
            }],
            columns: [
                {
                    xtype: "rownumberer",
                    flex: 0,
                    width: 50,
                    text: '序号',
                    align: 'center'
                },
                { align: 'center',titleAlign: "center",text: '姓名', dataIndex: 'xm', flex: 1 },
                { align: 'center',titleAlign: "center", text: '性别', dataIndex: 'xbm', flex: 1,
                    renderer : function(v, p, record){                                 
                        return v=="1"?"男":"女";      
                    }
                },
                { align: 'center',titleAlign: "center",text: '单位', dataIndex: 'workUnit', flex: 1.5 },
                { 
                    text: '午休', dataIndex: 'siesta', flex: 1,
                    align: 'center',titleAlign: "center",
                    renderer : function(v, p, record){     
                        if(v==true)             
                            return '<span class="x-grid-checkcolumn x-grid-checkcolumn-checked" role="button" tabindex="-1"  data-tabindex-value="0" data-tabindex-counter="1"></span>';
                        else
                            return '<span class="x-grid-checkcolumn" role="button" tabindex="-1"  data-tabindex-value="0" data-tabindex-counter="1"></span>';
                    }
                },
                { 
                    text: '晚宿', dataIndex: 'sleep', flex: 1,
                    align: 'center',titleAlign: "center",
                    renderer : function(v, p, record){     
                        if(v==true)             
                            return '<span class="x-grid-checkcolumn x-grid-checkcolumn-checked" role="button" tabindex="-1"  data-tabindex-value="0" data-tabindex-counter="1"></span>';
                        else
                            return '<span class="x-grid-checkcolumn" role="button" tabindex="-1"  data-tabindex-value="0" data-tabindex-counter="1"></span>';
                    }
                },               
                { align: 'center',titleAlign: "center", text: '宿舍名称', dataIndex: 'roomName', flex: 1},
                {
                    align: 'center',
                    titleAlign: "center",
                    width:80,
                    text: "课程分班",
                    dataIndex: "classGroup",
                    columnType: "basecombobox", //列类型
                    ddCode: "CLASSGROUP" //字典代码
                },
                { align: 'center',titleAlign: "center", text: '学员状态', dataIndex: 'isDelete',width:80,
                    renderer: function(value, metaData) {
                        if(value==0)
                            return "<span style='color:green'>正常</span>";
                        else if(value==1)
                            return "<span style='color:red'>删除</span>";
                        else if(value==2)
                            return "<span style='color:#FFAC00'>新增</span>";            
                    }
                },
                /* 不可编辑的时候
                { 
                    //xtype: 'checkcolumn',
                    text: '晚餐', dataIndex: 'haveDinner', flex: 1,
                    align: 'center',titleAlign: "center",
                    renderer : function(v, p, record){     
                        if(v==true)             
                            return '<span class="x-grid-checkcolumn x-grid-checkcolumn-checked" role="button" tabindex="-1"  data-tabindex-value="0" data-tabindex-counter="1"></span>';
                        else
                            return '<span class="x-grid-checkcolumn" role="button" tabindex="-1"  data-tabindex-value="0" data-tabindex-counter="1"></span>';
                    }
                }
                */
            ]
        }]
    }]
});
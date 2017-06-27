Ext.define("core.train.class.view.RoomDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.roomdetailform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 90,
        labelAlign: "right"
    },
    items: [{
        allowBlank: false,
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [ {
            fieldLabel: "所属班级",
            name: "className",
            xtype: "textfield",
            columnWidth: 0.5,
            readOnly:true,
            hidden: true
        },{        
            columnWidth: 0.5,
            readOnly:true,
            fieldLabel: "班级编号",    
            name: "classNumb",
            xtype: "textfield",
            hidden: true  
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [ {
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
            hidden: true,

            vtype:'beginDate',
            compareField:'endDate'
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
            hidden: true,

            vtype:'endDate',
            compareField:'beginDate'
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            xtype:'grid',
            ref:'traineeRoomGrid',
            cls :'checkboxGrid',
            frame:false,
            margin:'0 0 0 20',
            style:{
                border: '1px solid #ddd'
            },
            viewConfig: {
                stripeRows: false   //不展示隔行变色
            },
            columnWidth:1,
            height: 350,
            store:{
                type:"class.trainroomgridStore"
            },
            emptyText:'<span style="display: block;text-align:center">没有需要显示的数据！</span>',
            columns: [
                {
                    xtype: "rownumberer",
                    flex: 0,
                    width: 50,
                    text: '序号',
                    align: 'center'
                },
                { align: 'center',titleAlign: "center",text: '姓名', dataIndex: 'xm', flex: 1.5 },
                { align: 'center',titleAlign: "center", text: '性别', dataIndex: 'xbm', flex: 1,
                    renderer : function(v, p, record){                                 
                        return v=="1"?"男":"女";      
                    }
                },
                { xtype: 'checkcolumn', headerCheckbox:true,  text: '午休', dataIndex: 'siesta', flex: 1 },
                { xtype: 'checkcolumn', headerCheckbox:true, text: '晚宿', dataIndex: 'sleep', flex: 1},
                { align: 'center',titleAlign: "center", text: '学员状态', dataIndex: 'isDelete',width:80,
                    renderer: function(value, metaData) {
                        if(value==0)
                            return "<span style='color:green'>正常</span>";
                        else if(value==1)
                            return "<span style='color:red'>取消</span>";
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
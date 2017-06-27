Ext.define("core.train.class.view.FoodDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.fooddetailform",
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
        items: [{
            fieldLabel: "所属班级",
            name: "className",
            xtype: "textfield",
            columnWidth: 0.333,
            readOnly:true,
            hidden: true
        },{
            fieldLabel: "开始日期",
            columnWidth: 0.333,
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
            columnWidth: 0.333,
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
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            columnWidth: 0.333,
            xtype: "combobox",
            name: "dinnerType",
            fieldLabel: "就餐类型",
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value'],
                data : [
                    {"name":"围餐", "value":1},    
                    {"name":"自助餐", "value":2},
                    {"name":"快餐", "value":3} 
                ]
            }),
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
            value:3,
            editable:false,
            listeners: {
                change: function(combo, record, index) {
                    
                    var currentForm=combo.up("baseform[xtype=class.fooddetailform]");

                    if (record == 1) {

                        var food2=currentForm.query("container[ref=food2]")[0];
                        food2.setVisible (true);                        
                        
                        var fields2=food2.query("textfield");
                        for(var i=0;i<fields2.length;i++){
                            var label=fields2[i].getFieldLabel();
                            fields2[i].setFieldLabel ( label.replace("人数","围数"));
                        }
                     
                        var food3=currentForm.query("container[ref=food3]");
                        for(var i=0;i<food3.length;i++){
                            food3[i].setVisible (false);
                        }

                        var food1=currentForm.query("container[ref=food1]");
                        for(var i=0;i<food1.length;i++){
                            food1[i].setVisible (true);
                        }

                        currentForm.setHeight(235);

                    } else  if (record == 2) {

                        var food3=currentForm.query("container[ref=food3]");
                        for(var i=0;i<food3.length;i++){
                            food3[i].setVisible (false);
                        }

                        var food1=currentForm.query("container[ref=food1]");
                        for(var i=0;i<food1.length;i++){
                            food1[i].setVisible (false);
                        }

                        var food2=currentForm.query("container[ref=food2]")[0];
                        food2.setVisible (true);                        
                        
                        var fields2=food2.query("textfield");
                        for(var i=0;i<fields2.length;i++){
                            var label=fields2[i].getFieldLabel();
                            fields2[i].setFieldLabel ( label.replace("围数","人数"));
                        }

                        currentForm.setHeight(195);
                        
                    } else if (record == 3) {

                        var food2=currentForm.query("container[ref=food2]");
                        for(var i=0;i<food2.length;i++){
                            food2[i].setVisible (false);
                        }
                        
                        var food1=currentForm.query("container[ref=food1]");
                        for(var i=0;i<food1.length;i++){
                            food1[i].setVisible (false);
                        }

                        var food3=currentForm.query("container[ref=food3]");
                        for(var i=0;i<food3.length;i++){
                            food3[i].setVisible (true);
                        }

                        currentForm.setHeight(510);
                    }
                    
                }
            }
        }]
    },{
        ref:'food1',
        hidden:true,
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "每围人数",
            columnWidth: 0.333,
            name: "avgNumber",
            xtype: "numberfield",        
            minValue: 0,
            maxValue:999, 
            emptyText: "请输入每桌人数"   
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "早餐餐标",
            columnWidth: 0.333,
            name: "breakfastStand",
            xtype: "numberfield",  
            minValue: 0,
            maxValue:9999, 
            emptyText: "请输入早餐价格"
        },{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "午餐餐标",
            columnWidth: 0.333,
            name: "lunchStand",
            xtype: "numberfield",
            minValue: 0,
            maxValue:9999,
            emptyText: "请输入午餐价格"
        },{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "晚餐餐标",
            columnWidth: 0.333,
            name: "dinnerStand",
            xtype: "numberfield", 
            minValue: 0,
            maxValue:9999, 
            emptyText: "请输入晚餐价格"   
        }]
    },{ 
        ref:'food2',
        hidden:true,
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            columnWidth: 0.333,
            allowBlank: false,
            fieldLabel: "早餐围数",    
            name: "breakfastCount",
            xtype: "numberfield",        
            minValue: 0,
            maxValue:999, 
            emptyText: "请输入开餐围数/人数"      
        },{
            beforeLabelTextTpl: comm.get('required'),
            columnWidth: 0.333,
            allowBlank: false,
            fieldLabel: "午餐围数",    
            name: "lunchCount",
            xtype: "numberfield",        
            minValue: 0,
            maxValue:999, 
            emptyText: "请输入开餐围数/人数"      
        },{
            beforeLabelTextTpl: comm.get('required'),
            columnWidth: 0.333,
            allowBlank: false,
            fieldLabel: "晚餐围数",    
            name: "dinnerCount",
            xtype: "numberfield",        
            minValue: 0,
            maxValue:999, 
            emptyText: "请输入开餐围数/人数"      
        }]
    }, {
        ref:'food3',
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            xtype:'grid',
            ref:'traineeFoodGrid',
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
                type:"class.trainfoodgridStore"
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
                { xtype: 'checkcolumn', headerCheckbox:true, text: '早餐', dataIndex: 'breakfast', flex: 1 },
                { xtype: 'checkcolumn', headerCheckbox:true,  text: '午餐', dataIndex: 'lunch', flex: 1 },
                { xtype: 'checkcolumn', headerCheckbox:true, text: '晚餐', dataIndex: 'dinner', flex: 1},
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
Ext.define("core.train.class.view.FoodDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.fooddetailform",

    autoHeight: true,
    frame: false,
    //bodyPadding: '10 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 110,
        labelAlign: "right"
    },
    layout:'vbox',
    items:[{
        xtype:'container',
        layout: "form", //从上往下布局
        height:'auto',  //自动高度
        defaults:{
            width:'100%',
            margin:"10 5",
            xtype : 'textfield',
            labelAlign : 'right',
            //columnWidth : 0.5,
            msgTarget: 'qtip',
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

                            var countFood3=currentForm.query("container[ref=countFood3]");
                            for(var i=0;i<countFood3.length;i++){
                                countFood3[i].setVisible (false);
                            }

                            var countFood2=currentForm.query("container[ref=countFood2]");
                            for(var i=0;i<countFood2.length;i++){
                                countFood2[i].setVisible (false);
                            }

                            var countFood1=currentForm.query("container[ref=countFood1]");
                            for(var i=0;i<countFood1.length;i++){
                                countFood1[i].setVisible (true);
                            }

                            //currentForm.clearFood();
                            currentForm.countFood1();//计算汇总数据1
                            currentForm.setHeight(300);

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

                            var countFood3=currentForm.query("container[ref=countFood3]");
                            for(var i=0;i<countFood3.length;i++){
                                countFood3[i].setVisible (false);
                            }

                            var countFood1=currentForm.query("container[ref=countFood1]");
                            for(var i=0;i<countFood1.length;i++){
                                countFood1[i].setVisible (false);                        
                            }

                            var countFood2=currentForm.query("container[ref=countFood2]");
                            for(var i=0;i<countFood2.length;i++){
                                countFood2[i].setVisible (true);
                            }

                            //currentForm.clearFood();
                            currentForm.countFood2();//计算汇总数据1
                            currentForm.setHeight(260);
                            
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

                            var countFood2=currentForm.query("container[ref=countFood2]");
                            for(var i=0;i<countFood2.length;i++){
                                countFood2[i].setVisible (false);
                            }

                            var countFood1=currentForm.query("container[ref=countFood1]");
                            for(var i=0;i<countFood1.length;i++){
                                countFood1[i].setVisible (false);
                            }

                            var countFood3=currentForm.query("container[ref=countFood3]");
                            for(var i=0;i<countFood3.length;i++){
                                countFood3[i].setVisible (true);
                            }
                            //currentForm.clearFood();
                            currentForm.countFood3();
                            currentForm.setHeight(580);
                        }
                        
                    }
                }
            },{
                columnWidth: 0.333,
                xtype: "label",
                margin:'5 0 0 5 ',
                html: " （<font color=red,size=14>更换就餐类型后，请注意检查就餐的各项数据，修改后请提交</font>）",
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
                emptyText: "请输入每桌人数",
                listeners: {
                    change: function(field, record, index) {                  
                        var currentForm=field.up("baseform[xtype=class.fooddetailform]");
                        var dinnerType=currentForm.down("field[name=dinnerType]").getValue();                   
                        if (dinnerType == 1) {
                            currentForm.countFood1();//计算汇总数据1
                        } else if (dinnerType == 2) {
                            currentForm.countFood2();//计算汇总数据2
                        }                             
                    }
                }   
            },{
                columnWidth: 0.333,
                xtype: "label",
                margin:'5 0 0 5 ',
                html: " （<font color=red,size=14>此处餐标是按每围来设定</font>）",
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
                emptyText: "请输入早餐价格",
                listeners: {
                    change: function(field, record, index) {                  
                        var currentForm=field.up("baseform[xtype=class.fooddetailform]");
                        var dinnerType=currentForm.down("field[name=dinnerType]").getValue();                  
                        if (dinnerType == 1) {
                            currentForm.countFood1();//计算汇总数据1
                        }  else if (dinnerType == 2) {
                            currentForm.countFood2();//计算汇总数据2
                        }  else {
                            currentForm.countFood3();//计算汇总数据2
                        }                                      
                    }
                }   
            },{
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: false,
                fieldLabel: "午餐餐标",
                columnWidth: 0.333,
                name: "lunchStand",
                xtype: "numberfield",
                minValue: 0,
                maxValue:9999,
                emptyText: "请输入午餐价格",
                listeners: {
                    change: function(field, record, index) {                  
                        var currentForm=field.up("baseform[xtype=class.fooddetailform]");
                        var dinnerType=currentForm.down("field[name=dinnerType]").getValue();                  
                        if (dinnerType == 1) {
                            currentForm.countFood1();//计算汇总数据1
                        }  else if (dinnerType == 2) {
                            currentForm.countFood2();//计算汇总数据2
                        }  else {
                            currentForm.countFood3();//计算汇总数据2
                        }                                      
                    }
                }   
            },{
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: false,
                fieldLabel: "晚餐餐标",
                columnWidth: 0.333,
                name: "dinnerStand",
                xtype: "numberfield", 
                minValue: 0,
                maxValue:9999, 
                emptyText: "请输入晚餐价格",
                listeners: {
                    change: function(field, record, index) {                  
                        var currentForm=field.up("baseform[xtype=class.fooddetailform]");
                        var dinnerType=currentForm.down("field[name=dinnerType]").getValue();                  
                        if (dinnerType == 1) {
                            currentForm.countFood1();//计算汇总数据1
                        } else if (dinnerType == 2) {
                            currentForm.countFood2();//计算汇总数据2
                        } else {
                            currentForm.countFood3();//计算汇总数据2
                        }                                        
                    }
                }   
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
                fieldLabel: "计划早餐围数",    
                name: "breakfastCount",
                xtype: "numberfield",        
                minValue: 0,
                maxValue:999, 
                emptyText: "请输入开餐围数/人数" ,
                listeners: {
                    change: function(field, record, index) {                  
                        var currentForm=field.up("baseform[xtype=class.fooddetailform]");
                        var dinnerType=currentForm.down("field[name=dinnerType]").getValue();                   
                        if (dinnerType == 1) {
                            currentForm.countFood1();//计算汇总数据1
                        }  else if (dinnerType == 2) {
                            currentForm.countFood2();//计算汇总数据2
                        }                                       
                    }
                }   
            },{
                beforeLabelTextTpl: comm.get('required'),
                columnWidth: 0.333,
                allowBlank: false,
                fieldLabel: "计划午餐围数",    
                name: "lunchCount",
                xtype: "numberfield",        
                minValue: 0,
                maxValue:999, 
                emptyText: "请输入开餐围数/人数",
                listeners: {
                    change: function(field, record, index) {                  
                        var currentForm=field.up("baseform[xtype=class.fooddetailform]");
                        var dinnerType=currentForm.down("field[name=dinnerType]").getValue();                 
                        if (dinnerType == 1) {
                            currentForm.countFood1();//计算汇总数据1
                        }  else if (dinnerType == 2) {
                            currentForm.countFood2();//计算汇总数据2
                        }                                       
                    }
                }       
            },{
                beforeLabelTextTpl: comm.get('required'),
                columnWidth: 0.333,
                allowBlank: false,
                fieldLabel: "计划晚餐围数",    
                name: "dinnerCount",
                xtype: "numberfield",        
                minValue: 0,
                maxValue:999, 
                emptyText: "请输入开餐围数/人数",
                listeners: {
                    change: function(field, record, index) {                  
                        var currentForm=field.up("baseform[xtype=class.fooddetailform]");
                        var dinnerType=currentForm.down("field[name=dinnerType]").getValue();               
                        if (dinnerType == 1) {
                            currentForm.countFood1();//计算汇总数据1
                        }  else if (dinnerType == 2) {
                            currentForm.countFood2();//计算汇总数据2
                        }                                       
                    }
                }      
            }]
        }, { 
            ref:'countFood1',
            hidden:true,
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{            
                columnWidth: 0.333,
                fieldLabel: "总额",    
                name: "countMoney",
                xtype: "textfield",        
                value:0,
                readOnly :true  
            },{          
                columnWidth: 0.333,
                fieldLabel: "每天围数",    
                name: "countNumberInDay",
                xtype: "textfield",
                value:0,
                readOnly :true      
            
            },{    
                columnWidth: 0.333,
                fieldLabel: "总围数",    
                name: "countNumber",
                xtype: "textfield",        
                value:0,
                readOnly :true          
            }]
        }, { 
            ref:'countFood2',
            hidden:true,
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{            
                columnWidth: 0.333,
                fieldLabel: "总额",    
                name: "countMoney2",
                xtype: "textfield",        
                value:0,
                readOnly :true  
            },{          
                columnWidth: 0.333,
                fieldLabel: "每天份数",    
                name: "countNumberInDay2",
                xtype: "textfield",
                value:0,
                readOnly :true      
            
            },{    
                columnWidth: 0.333,
                fieldLabel: "总份数",    
                name: "countNumber2",
                xtype: "textfield",        
                value:0,
                readOnly :true          
            }]
        },{ 
            ref:'countFood3',
            //hidden:true,
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{            
                columnWidth: 0.333,
                fieldLabel: "早餐人数",    
                name: "breakfastCount3",
                xtype: "textfield",        
                value:0,
                readOnly :true  
            },{          
                columnWidth: 0.333,
                fieldLabel: "午餐人数",    
                name: "lunchCount3",
                xtype: "textfield",
                value:0,
                readOnly :true      
            
            },{    
                columnWidth: 0.333,
                fieldLabel: "晚餐人数",    
                name: "dinnerCount3",
                xtype: "textfield",        
                value:0,
                readOnly :true          
            }]
        },{ 
            ref:'countFood3',
            //hidden:true,
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{            
                columnWidth: 0.333,
                fieldLabel: "总额",    
                name: "countMoney3",
                xtype: "textfield",        
                value:0,
                readOnly :true  
            },{          
                columnWidth: 0.333,
                fieldLabel: "每天份数",    
                name: "countNumberInDay3",
                xtype: "textfield",
                value:0,
                readOnly :true      
            
            },{    
                columnWidth: 0.333,
                fieldLabel: "总份数",    
                name: "countNumber3",
                xtype: "textfield",        
                value:0,
                readOnly :true          
            }]
        }]
    },{
        ref:'food3',
        xtype: "container",
        layout: "fit",
        labelAlign: "right",
        flex:1,
        minHeight: 300,
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
            tbar: ['->',{
                xtype: 'tbtext', 
                html:'快速搜索：'
            },{
                xtype:'textfield',
                name:'xm',
                width:100,
                funCode:'girdFastSearchText', 
                emptyText: '姓名'
            },{
                xtype:'textfield',
                name:'traineeNumber',
                width:100,
                funCode:'girdFastSearchText', 
                emptyText: '学号'
            },{
                xtype: 'button',
                funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
                ref: 'gridFastSearchBtn',   
                iconCls: 'x-fa fa-search',  
            }],
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
                { align: 'center',titleAlign: "center",text: '学号', dataIndex: 'traineeNumber', flex: 1.5 },  
                { align: 'center',titleAlign: "center", text: '性别', dataIndex: 'xbm', flex: 1,
                    renderer : function(v, p, record){                                 
                        return v=="1"?"男":"女";      
                    }
                },
                { xtype: 'checkcolumn', headerCheckbox:true, text: '早餐', dataIndex: 'breakfast', flex: 1,
                    listeners:{
                        headercheckchange:function( me , checked , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.fooddetailform]");
                            currentForm.countFood3();

                            if(checked==true){
                                //更新单个人员的住宿情况
                                var basetab = currentForm.up('baseformtab');
                                var roomGrid=basetab.down("grid[ref=traineeRoomGrid]");
                                if(roomGrid){
                                    var roomStore=roomGrid.getStore();
                                    var len=roomStore.data.length;
                                    for(var i=0;i<len;i++){
                                        var rec=roomStore.getAt(i);
                                        rec.set("sleep",1);
                                        rec.commit();
                                    } 
                                    var roomForm=basetab.down("baseform[xtype=class.roomdetailform]");
                                    if(roomForm)
                                        roomForm.countRoom();                               
                                }
                            }
                        },
                        checkchange :function ( me , rowIndex , checked , record , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.fooddetailform]");
                            currentForm.countFood3();

                            if(checked==true){
                                //更新单个人员的住宿情况
                                var basetab = currentForm.up('baseformtab');
                                var roomGrid=basetab.down("grid[ref=traineeRoomGrid]");
                                if(roomGrid){
                                    var roomStore=roomGrid.getStore();
                                    var rec=roomStore.findRecord("uuid",record.get("uuid"));
                                    rec.set("sleep",1);
                                    rec.commit();
                                }
                                var roomForm=basetab.down("baseform[xtype=class.roomdetailform]");                        
                                if(roomForm)
                                    roomForm.countRoom();       
                            }
                          
                        }
                    }
                },
                { xtype: 'checkcolumn', headerCheckbox:true,  text: '午餐', dataIndex: 'lunch', flex: 1,
                    listeners:{
                        headercheckchange:function ( me , checked , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.fooddetailform]");
                            currentForm.countFood3();
                        },
                        checkchange:function ( me , rowIndex , checked , record , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.fooddetailform]");
                            currentForm.countFood3();
                        }
                    }
                },
                { xtype: 'checkcolumn', headerCheckbox:true, text: '晚餐', dataIndex: 'dinner', flex: 1,
                    listeners:{
                        headercheckchange:function ( me , checked , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.fooddetailform]");
                            currentForm.countFood3();

                            if(checked==true){
                                //更新单个人员的住宿情况
                                var basetab = currentForm.up('baseformtab');
                                var roomGrid=basetab.down("grid[ref=traineeRoomGrid]");
                                if(roomGrid){
                                    var roomStore=roomGrid.getStore();
                                    var len=roomStore.data.length;
                                    for(var i=0;i<len;i++){
                                        var rec=roomStore.getAt(i);
                                        rec.set("sleep",1);
                                        rec.commit();
                                    }   

                                    var roomForm=basetab.down("baseform[xtype=class.roomdetailform]");
                                    if(roomForm)
                                        roomForm.countRoom();             
                                }
                            }                        
                        },
                        checkchange:function ( me , rowIndex , checked , record , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.fooddetailform]");
                            currentForm.countFood3();

                            if(checked==true){
                                //更新单个人员的住宿情况
                                var basetab = currentForm.up('baseformtab');
                                var roomGrid=basetab.down("grid[ref=traineeRoomGrid]");
                                if(roomGrid){
                                    var roomStore=roomGrid.getStore();
                                    var rec=roomStore.findRecord("uuid",record.get("uuid"));
                                    rec.set("sleep",1);
                                    rec.commit();
                                }
                                var roomForm=basetab.down("baseform[xtype=class.roomdetailform]");
                                if(roomForm)
                                    roomForm.countRoom(); 
                            }            
                        }
                    }
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
    }],
    clearFood:function(){
        this.down("field[name=breakfastStand]").setValue(0);
        this.down("field[name=lunchStand]").setValue(0);
        this.down("field[name=dinnerStand]").setValue(0);
        this.down("field[name=breakfastCount]").setValue(0);
        this.down("field[name=lunchCount]").setValue(0);
        this.down("field[name=dinnerCount]").setValue(0);
    },
    countFood1:function(){
        
        var countMoney=0,countNumberInDay=0,countNumber=0;
        var breakfastStand=this.down("field[name=breakfastStand]").getValue();
        var lunchStand=this.down("field[name=lunchStand]").getValue();
        var dinnerStand=this.down("field[name=dinnerStand]").getValue();
        var breakfastCount=this.down("field[name=breakfastCount]").getValue();
        var lunchCount=this.down("field[name=lunchCount]").getValue();
        var dinnerCount=this.down("field[name=dinnerCount]").getValue();

        var beginDate=this.down("field[name=beginDate]").getValue();
        var endDate=this.down("field[name=endDate]").getValue();
        var day=(new Date(endDate).getTime()-new Date(beginDate).getTime())/(1000*60*60*24)+1;

        this.down("field[name=countMoney]").setValue((breakfastStand*breakfastCount+lunchStand*lunchCount+dinnerStand*dinnerCount)*day);
        this.down("field[name=countNumberInDay]").setValue(breakfastCount+lunchCount+dinnerCount);
        this.down("field[name=countNumber]").setValue((breakfastCount+lunchCount+dinnerCount)*day);

    },
    countFood2:function(){
        
        var countMoney=0,countNumberInDay=0,countNumber=0;
        var breakfastStand=this.down("field[name=breakfastStand]").getValue();
        var lunchStand=this.down("field[name=lunchStand]").getValue();
        var dinnerStand=this.down("field[name=dinnerStand]").getValue();
        var breakfastCount=this.down("field[name=breakfastCount]").getValue();
        var lunchCount=this.down("field[name=lunchCount]").getValue();
        var dinnerCount=this.down("field[name=dinnerCount]").getValue();

        var beginDate=this.down("field[name=beginDate]").getValue();
        var endDate=this.down("field[name=endDate]").getValue();
        var day=(new Date(endDate).getTime()-new Date(beginDate).getTime())/(1000*60*60*24)+1;

        this.down("field[name=countMoney2]").setValue((breakfastStand*breakfastCount+lunchStand*lunchCount+dinnerStand*dinnerCount)*day);
        this.down("field[name=countNumberInDay2]").setValue(breakfastCount+lunchCount+dinnerCount);
        this.down("field[name=countNumber2]").setValue((breakfastCount+lunchCount+dinnerCount)*day);

    },
    countFood3:function(){
        var traineeFoodGrid = this.down("grid[ref=traineeFoodGrid]");
        var traineeFoodStore = traineeFoodGrid.getStore();
        var breakfastNum=0,lunchNum=0,dinnerNum=0;

        for (var i = 0; i < traineeFoodStore.getCount(); i++) {
            var rowData = traineeFoodStore.getAt(i).getData();

            breakfastNum += rowData.breakfast == true ? 1 : 0;
            lunchNum += rowData.lunch == true ? 1 : 0;
            dinnerNum += rowData.dinner == true ? 1 : 0;           
        }
        
        var breakfastStand=this.down("field[name=breakfastStand]").getValue();
        var lunchStand=this.down("field[name=lunchStand]").getValue();
        var dinnerStand=this.down("field[name=dinnerStand]").getValue();
        var beginDate=this.down("field[name=beginDate]").getValue();
        var endDate=this.down("field[name=endDate]").getValue();
        var day=(new Date(endDate).getTime()-new Date(beginDate).getTime())/(1000*60*60*24)+1;

        this.down("field[name=breakfastCount3]").setValue(breakfastNum);
        this.down("field[name=lunchCount3]").setValue(lunchNum);
        this.down("field[name=dinnerCount3]").setValue(dinnerNum);
        this.down("field[name=countMoney3]").setValue((breakfastStand*breakfastNum+lunchStand*lunchNum+dinnerStand*dinnerNum)*day);
        this.down("field[name=countNumberInDay3]").setValue(breakfastNum+lunchNum+dinnerNum);
        this.down("field[name=countNumber3]").setValue((breakfastNum+lunchNum+dinnerNum)*day);
    }
});
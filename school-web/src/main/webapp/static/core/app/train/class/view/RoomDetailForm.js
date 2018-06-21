Ext.define("core.train.class.view.RoomDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.roomdetailform",
    //layout: "form", //从上往下布局
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
                columnWidth: 0.333,
                fieldLabel: "午休人数（男）",    
                name: "siestaManNum",
                xtype: "textfield",        
                value:0,
                readOnly :true  
            },{          
                columnWidth: 0.333,
                fieldLabel: "午休人数（女）",    
                name: "siestaWomenNum",
                xtype: "textfield",
                value:0,
                readOnly :true      
            
            },{    
                columnWidth: 0.333,
                fieldLabel: "不午休人数",    
                name: "noSiestaNum",
                xtype: "textfield",        
                value:0,
                readOnly :true          
            }]
        },{ 
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{            
                columnWidth: 0.333,
                fieldLabel: "晚宿人数（男）",    
                name: "sleepManNum",
                xtype: "textfield",        
                value:0,
                readOnly :true  
            },{          
                columnWidth: 0.333,
                fieldLabel: "晚宿人数（女）",    
                name: "sleepWomenNum",
                xtype: "textfield",
                value:0,
                readOnly :true      
            
            },{    
                columnWidth: 0.333,
                fieldLabel: "不晚宿人数",    
                name: "noSleepNum",
                xtype: "textfield",        
                value:0,
                readOnly :true          
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
            //height: 350,
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
                { align: 'center',titleAlign: "center",text: '学号', dataIndex: 'traineeNumber', flex: 1.5 },        
                { align: 'center',titleAlign: "center", text: '性别', dataIndex: 'xbm', flex: 1,
                    renderer : function(v, p, record){                                 
                        return v=="1"?"男":"女";      
                    }
                },
                { xtype: 'checkcolumn', headerCheckbox:true,  text: '午休', dataIndex: 'siesta', flex: 1 ,
                    listeners:{
                        headercheckchange:function ( me , checked , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.roomdetailform]");
                            currentForm.countRoom();
                        },
                        checkchange:function ( me , rowIndex , checked , record , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.roomdetailform]");
                            currentForm.countRoom();
                        }
                    }
                },
                { xtype: 'checkcolumn', headerCheckbox:true, text: '晚宿', dataIndex: 'sleep', flex: 1,
                    listeners:{
                        headercheckchange:function ( me , checked , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.roomdetailform]");
                            currentForm.countRoom();

                            if(checked==true){
                                //更新单个人员的住宿情况
                                var basetab = currentForm.up('baseformtab');
                                var foodGrid=basetab.down("grid[ref=traineeFoodGrid]");
                                if(foodGrid){
                                    var foodStore=foodGrid.getStore();
                                    var len=foodStore.data.length;
                                    for(var i=0;i<len;i++){
                                        var rec=foodStore.getAt(i);
                                        rec.set("breakfast",1);
                                        rec.set("dinner",1);
                                        rec.commit();
                                    } 
                                    var foodForm=basetab.down("baseform[xtype=class.fooddetailform]");                                  
                                    if(foodForm)
                                        foodForm.countFood3();                               
                                }
                            }

                        },
                        checkchange:function ( me , rowIndex , checked , record , e , eOpts ) {
                            var currentForm=me.up("baseform[xtype=class.roomdetailform]");
                            currentForm.countRoom();

                            if(checked==true){
                                //更新单个人员的住宿情况
                                var basetab = currentForm.up('baseformtab');
                                var foodGrid=basetab.down("grid[ref=traineeFoodGrid]");
                                if(foodGrid){
                                    var foodStore=foodGrid.getStore();
                                    var rec=foodStore.findRecord("uuid",record.get("uuid"));
                                    rec.set("breakfast",1);
                                    rec.set("dinner",1);
                                    rec.commit();
                                }
                                var foodForm=basetab.down("baseform[xtype=class.fooddetailform]");
                                if(foodForm)
                                    foodForm.countFood3();    
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
    countRoom:function(){
        var traineeRoomGrid = this.down("grid[ref=traineeRoomGrid]");
        var traineeRoomStore = traineeRoomGrid.getStore();
        var siestaManNum=0,siestaWomenNum=0,noSiestaNum=0,sleepManNum=0,sleepWomenNum=0,noSleepNum=0;

        for (var i = 0; i < traineeRoomStore.getCount(); i++) {
            var rowData = traineeRoomStore.getAt(i).getData();

            if(rowData.siesta == true){
                if(rowData.xbm==1)
                    siestaManNum+=1;
                else
                    siestaWomenNum+=1;
            }else{
                noSiestaNum+=1;
            }

            if(rowData.sleep == true){
                if(rowData.xbm==1)
                    sleepManNum+=1;
                else
                    sleepWomenNum+=1;
            }else{
                noSleepNum+=1;
            }      
        }
       
        this.down("field[name=siestaManNum]").setValue(siestaManNum);
        this.down("field[name=siestaWomenNum]").setValue(siestaWomenNum);
        this.down("field[name=noSiestaNum]").setValue(noSiestaNum);
        this.down("field[name=sleepManNum]").setValue(sleepManNum);
        this.down("field[name=sleepWomenNum]").setValue(sleepWomenNum);
        this.down("field[name=noSleepNum]").setValue(noSleepNum);
    }
});
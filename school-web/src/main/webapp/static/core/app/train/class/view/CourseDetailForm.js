Ext.define("core.train.class.view.CourseDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.coursedetailform",
  
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '10 15 10 15',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    items: [{
        fieldLabel: "主键",
        name: "uuid",
        xtype: "textfield",
        hidden: true
    },{
        fieldLabel: "所属班级id",
        name: "classId",
        xtype: "textfield",
        hidden: true
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "所属班级",
            name: "className",
            xtype: "textfield",
            columnWidth: 0.5,
            readOnly:true
        }/*,{        
            xtype: "label",
            columnWidth: 0.5,
        }*/]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "课程日期",
            name: "courseDate",
            xtype: "datetimefield",
            dateType: 'date',
            emptyText: "请选择课程日期",    
            value:new Date(),
            //format: "Y年m月d日 H:i",   //显示的格式
            //submitFormat:'Y-m-d H:i',   //真正提交的格式
            columnWidth: 0.5
        },{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "课程学分",                  
            name: "credits",
            xtype: "numberfield",        
            minValue: 0,
            maxValue:999, 
            emptyText: "请输入课程学分",     
            value:4,
            columnWidth: 0.5
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "上课时间",
            columnWidth: 0.5,
            name: "courseBeginTime",
            xtype: "timefield",
            format:'H:i',
            submitFormat:'H:i',
            minValue: '6:00',
            maxValue: '22:00',
            increment: 10,
            editable:false,
            vtype:'beginDate',
            emptyText: "请选择上课时间",  
            compareField:'courseEndTime'        
        }, {
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "下课时间",
            columnWidth: 0.5,
            name: "courseEndTime",
            xtype: "timefield",
            format:'H:i',
            submitFormat:'H:i',
            minValue: '6:00',
            maxValue: '22:00',
            increment: 10,
            dateType: 'date',    
            editable:false ,
            vtype:'endDate',
            emptyText: "请选择下课时间",  
            compareField:'courseBeginTime'   
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [/*{
            xtype: "textfield",
            fieldLabel: "课程id",
            name: "courseId",
            hidden: true
        }, {
            xtype: "textfield",
            fieldLabel: "讲师id",
            name: "mainTeacherId",
            hidden: true
        }
        ,{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            columnWidth: 0.5,         
            xtype: "basefuncfield",
            funcPanel: "course.mainlayout", //该功能显示的主视图
            formPanel:"class.coursedetailform",
            refController:'class.otherController',      //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
            funcTitle: "课程选择", //查询窗口的标题
            configInfo: {
                width:1000,
                fieldInfo: "courseId~courseName,uuid~courseName",
                whereSql: " and isDelete='0' ",
                orderSql: " order by jobCode ",
                muiltSelect: false //是否多选
            },
            fieldLabel: '课程名称',
            name: "courseName",
            editable:true,
            
            //清除按钮事件
            onTrigger1Click : function(){
                var self=this;
                self.reset();
                var configInfo=self.configInfo;
                var fieldInfo=configInfo.fieldInfo;
                var fieldArray=fieldInfo.split(",");
                var dataField=fieldArray[0].split("~");
                var bf,rec;//表格和表单，如果是表格的化则更新表格的字段值，如果是表单则更新表单
                if(self.ownerCt.xtype=='editor'){
                    var grid = self.ownerCt.floatParent;
                    rec = grid.getSelectionModel().getSelection()[0];
                }else{
                    bf = self.up('form').getForm();
                }
                Ext.each(dataField,function(f,index){
                    if(rec){
                        rec.set(f,null);
                    }else{
                        var bff=bf.findField(f);
                        if(bff){
                            bff.setValue(null);
                        }
                    }   
                });

                //让此按钮可以手动输入
                self.setEditable(true);
                //清除讲师文本框的数据
                var form=self.up("form[xtype=class.coursedetailform]").getForm();
                form.findField("mainTeacherId").reset();
                form.findField("mainTeacherName").reset();
                form.findField("mainTeacherName").setEditable(true);
            },
        }, {
            columnWidth: 0.5,        
            xtype: "basefuncfield",        
            funcPanel: "teacher.mainlayout", //该功能显示的主视图
            formPanel:"class.coursedetailform",
            refController:'class.otherController',      //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
            funcTitle: "讲师选择", //查询窗口的标题
            configInfo: {
                fieldInfo: "mainTeacherId~mainTeacherName,uuid~xm",
                whereSql: " and isDelete='0' ",
                orderSql: " order by jobCode ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '讲师',
            name: "mainTeacherName",
            allowBlank: true,
            editable:true,

            //清除按钮事件
            onTrigger1Click : function(){
                var self=this;
                self.reset();
                var configInfo=self.configInfo;
                var fieldInfo=configInfo.fieldInfo;
                var fieldArray=fieldInfo.split(",");
                var dataField=fieldArray[0].split("~");
                var bf,rec;//表格和表单，如果是表格的化则更新表格的字段值，如果是表单则更新表单
                if(self.ownerCt.xtype=='editor'){
                    var grid = self.ownerCt.floatParent;
                    rec = grid.getSelectionModel().getSelection()[0];
                }else{
                    bf = self.up('form').getForm();
                }
                Ext.each(dataField,function(f,index){
                    if(rec){
                        rec.set(f,null);
                    }else{
                        var bff=bf.findField(f);
                        if(bff){
                            bff.setValue(null);
                        }
                    }   
                });

                //让此按钮可以手动输入
                self.setEditable(true);                
            },
        }
        */
        {
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "课程名称",
            columnWidth: 0.5,
            name: "courseName",
            xtype: "textfield",
            emptyText: "请输入课程名称",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符",
        }, {
            xtype: "textfield",
            fieldLabel: "讲师id",
            name: "mainTeacherId",
            hidden: true
        },{
            xtype: "textfield",
            fieldLabel: "讲师性别",
            name: "mainTeacherXbm",
            hidden: true
        },{
            xtype: "textfield",
            fieldLabel: "讲师工作单位",
            name: "mainTeacherWork",
            hidden: true
        },{
            beforeLabelTextTpl: comm.get('required'),        
            allowBlank: false,      
            columnWidth: 0.5,
            xtype: "basefuncfield",
            refController: "class.otherController", //该功能主控制器，这里重新指定为当前视图的控制器了
            funcPanel: "class.selectteacher.mainlayout", //该功能显示的主视图
            formPanel:"class.coursedetailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "主讲老师选择", //查询窗口的标题
            configInfo: {
                //width:comm.get("clientWidth")*0.8,
                width:1200,
                height:650,
                fieldInfo: "mainTeacherId~mainTeacherName~mainTeacherXbm~mainTeacherWork,uuid~xm~xbm~workUnits",
                whereSql: " and isDelete='0' ",
                orderSql: " order by createTime DESC ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '主讲老师',
            emptyText: "请选择主讲老师",
            name: "mainTeacherName",        
        }]
    },  {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "教学形式",
            columnWidth: 0.5,
            name: "teachType",
            xtype: "basecombobox",
            ddCode: "TEACHTYPE",
            value:"01",
            emptyText: "请选择教学形式",
            editable:false
        },{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            editable:false,
            fieldLabel: "是否评价",
            columnWidth: 0.5,
            name: "isEval",
            xtype: 'combo',
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value'],
                data : [
                    {"name":"是", "value":1},    
                    {"name":"否", "value":0}
                ]
            }),
            value:1,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            fieldLabel: "课程分班",
            columnWidth: 0.5,
            name: "classGroup",
            xtype: "basecombobox",
            ddCode: "CLASSGROUP",
            value:"0",
            emptyText: "请选择课程分班",
            editable:false
        },{
            beforeLabelTextTpl: comm.get('required'),
            allowBlank: false,
            editable:false,
            fieldLabel: "是否选修课",
            columnWidth: 0.5,
            name: "isOptional",
            xtype: 'combo',
            store: Ext.create('Ext.data.Store', {
                fields: ['name', 'value'],
                data : [
                    {"name":"是", "value":1},    
                    {"name":"否", "value":0}
                ]
            }),
            value:0,
            queryMode: 'local',
            displayField: 'name',
            valueField: 'value',
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [ /*{
            fieldLabel: "授课地点",
            columnWidth: 1,
            name: "scheduleAddress",
            xtype: "textarea",
            emptyText: "请输入授课地点",
            maxLength: 64,
            maxLengthText: "最多64个字符,汉字占2个字符",
        }*/
        /* 暂时取消掉这里的选择场地功能，改为由总务处进行安排场地
         {
            columnWidth: 0.5,
            xtype: "basefuncfield",
            // refController: "meetinginfo.otherController", //该功能主控制器，这里重新指定为当前视图的控制器了
            refController: "pubselect.selectroomcontroller",
            funcPanel: "pubselect.selectroomlayout", //该功能显示的主视图
            funcGrid: "pubselect.isselectroomgrid", //该功能显示的主视图
            formPanel: "class.coursedetailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
            funcTitle: "授课地点选择", //查询窗口的标题
            configInfo: {
                width: 1200,
                height: 700,
                fieldInfo: "roomId~scheduleAddress,uuid~roomName",
                whereSql: " and isDelete='0' ",
                orderSql: " order by areaUpName, areaName,orderIndex asc",
                muiltSelect: true //是否多选
            },
            fieldLabel: "授课地点",
            emptyText: "请选择授课地点",
            name: "scheduleAddress"

        }, {
            fieldLabel: "房间ID",
            // columnWidth: 0.5,
            name: "roomId",
            xtype: "textfield",
            emptyText: "请输入房间ID",
            hidden: true
        }*/
        ]
    }]
});
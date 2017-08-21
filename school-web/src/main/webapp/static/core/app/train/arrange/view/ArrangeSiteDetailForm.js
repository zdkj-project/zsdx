Ext.define("core.train.arrange.view.ArrangeSiteDetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.arrange.sitedetailform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: '10 25 10 5',
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
    },  {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            xtype:'basegrid',
            ref:'arrangeSiteGrid',
            //cls :'checkboxGrid',

            dataUrl: comm.get("baseUrl") + "/TrainClassschedule/list", //数据获取地址
            model: "com.zd.school.jw.train.model.TrainClassschedule", //对应的数据模型
            noPagging:true,
            storePageSize:-1,
            panelButtomBar:null,
            panelTopBar: null,
            al:false,

            margin:'0 0 0 20',
            style:{
                border: '1px solid #ddd'
            },          
            columnWidth:1,
            height: 500,
            // store:{
            //     type:"arrange.arrangesitegridStore"
            // },
            tbar:[{
                xtype: 'button',
                text: '选择场地',
                ref: 'gridSetRoom',
                iconCls: 'x-fa fa-plus-circle'
            },{
                xtype: 'button',
                text: '取消场地',
                ref: 'gridCancelRoom',
                iconCls: 'x-fa fa-minus-circle'
            },{
                xtype: 'tbtext',
                html: '（一个教室，在同一个时间段，只能安排一个班级上课，不要冲突）',
                style: {
                    fontSize: '14px',
                    color: '#C44444',
                    fontWeight:800
                }
            }],
            columns:{
                defaults: {
                    align: 'center',
                    titleAlign: "center"
                },
                items: [{
                    xtype: "rownumberer",
                    flex: 0,
                    width: 50,
                    text: '序号',
                    align: 'center'
                }, /*{
                    width:150,
                    text: "所属班级",
                    dataIndex: "className",
                },*/{
                    flex: 1,
                    minWidth:150,
                    text: "课程名称",
                    dataIndex: "courseName"
                }, {
                    width:120,
                    text: "教学形式",
                    dataIndex: "teachTypeName", 
                },{
                    width:150,
                    text: "开始日期",
                    dataIndex: "beginTime",            
                    renderer: function(value, metaData) {
                        var date = value.replace(new RegExp(/-/gm), "/");
                        var title = "开始日期";
                        var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')
                        var html = ss;
                        metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                        return ss;
                    }
                }, {
                    width:150,
                    text: "结束日期",
                    dataIndex: "endTime",            
                    renderer: function(value, metaData) {
                        var date = value.replace(new RegExp(/-/gm), "/");
                        var title = "结束日期";
                        var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')
                        var html = ss;
                        metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                        return ss;
                    }
                },{
                    width:150,
                    text: "讲师",
                    dataIndex: "mainTeacherName"          
                },/*{
                    width:120,
                    text: "授课模式",
                    dataIndex: "courseMode",
                    columnType: "basecombobox", //列类型
                    ddCode: "COURSEMODE" //字典代码  
                },*/{
                    flex: 1,
                    minWidth:150,
                    text: "授课地点",
                    dataIndex: "scheduleAddress",
                    renderer: function(value, metaData) {
                        var title = "授课地点";            
                        metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                        return value;
                    }
                },{
                    width:100,
                    text: "是否评价",
                    dataIndex: "isEval",
                    renderer: function(value, metaData) {
                        if(value==1)
                            return "<span style='color:green'>是</span>";
                        else
                            return "<span style='color:red'>否</span>";
                    }
                },{ 
                    align: 'center',titleAlign: "center", text: '课程状态', dataIndex: 'isDelete',width:80,
                    renderer: function(value, metaData) {
                        if(value==0)
                            return "<span style='color:green'>正常</span>";
                        else if(value==1)
                            return "<span style='color:red'>删除</span>";
                        else if(value==2)
                            return "<span style='color:#FFAC00'>新增</span>";            
                    }
                },]
            }
            /*[
                { align: 'center',titleAlign: "center",text: '课程名称', dataIndex: 'courseName', flex: 1.5 },
                { align: 'center',titleAlign: "center",text: '授课模式', dataIndex: 'courseMode', flex: 1,columnType: "basecombobox",  ddCode: "COURSEMODE" },
                { align: 'center',titleAlign: "center",text: '主讲老师', dataIndex: 'mainTeacherName', flex: 1 },
                { align: 'center',titleAlign: "center",text: '课程名称', dataIndex: 'courseName', flex: 1 },              
                { align: 'center',titleAlign: "center", text: '授课地点', dataIndex: 'scheduleAddress', flex: 2,
                    renderer: function(value, metaData) {                    
                        var title = "授课地点";                       
                        metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                        return value;                       
                    }
                } 
                          
            ]  */ 
        }]
    }]
});
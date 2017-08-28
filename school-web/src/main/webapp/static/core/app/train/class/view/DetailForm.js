Ext.define("core.train.class.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.class.detailform",
    xtype: 'classdeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 20 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 90,
        labelAlign: "right"
    },
    // items: [{
    //     margin:0,
    //     padding:'5 5 10 5',
    //     xtype:'fieldset',
    //     title: '班级基本信息',
    //     border:1,
    //     style: {
    //         backgroundColor: '#f5f5f5',
    //         fontSize:'14px',
    //         fontFamily: '微软雅黑',
    //         fontWeight:800,
    //         color:'#C3190C',
    //         borderColor: '#C6CBD6',
    //         borderStyle: 'solid'
    //     },
        defaults:{
            width:'100%',
            margin:"10 5 0 5",
        },
        items:[{
            fieldLabel: "主键",
            name: "uuid",
            xtype: "textfield",
            hidden: true
        }, {
            fieldLabel: "用餐类型",
            name: "dinnerType",
            xtype: "textfield",
            hidden: true
        },{
            fieldLabel: "每围人数",
            name: "avgNumber",
            xtype: "textfield",
            hidden: true
        },{
            fieldLabel: "早餐餐标",
            name: "breakfastStand",
            xtype: "textfield",
            hidden: true
        },{
            fieldLabel: "早餐围/位数",
            name: "breakfastCount",
            xtype: "textfield",
            hidden: true
        },{
            fieldLabel: "午餐餐标",
            name: "lunchStand",
            xtype: "textfield",
            hidden: true
        },{
            fieldLabel: "午餐围/位数",
            name: "lunchCount",
            xtype: "textfield",
            hidden: true
        },{
            fieldLabel: "晚餐餐标",
            name: "dinnerStand",
            xtype: "textfield",
            hidden: true
        },{
            fieldLabel: "晚餐围/位数",
            name: "dinnerCount",
            xtype: "textfield",
            hidden: true
        },{
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: false,
                fieldLabel: "开始日期",
                columnWidth: 0.25,
                name: "beginDate",
                xtype: "datefield",
                editable:false,
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
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: false,
                fieldLabel: "结束日期",
                columnWidth: 0.25,
                name: "endDate",
                xtype: "datefield",
                editable:false,
                //dateType: 'date',
                format: "Y-m-d",
                formatText:'',
                emptyText: "请输入结束日期",
                vtype:'endDate',
                compareField:'beginDate'
            },{ 
                fieldLabel: "主办单位",
                columnWidth: 0.25,
                name: "holdUnit",
                xtype: "textfield",
                emptyText: "请输入主办单位名称",
                maxLength: 64,
                maxLengthText: "最多64个字符,汉字占2个字符"
            },{ 
                fieldLabel: "承办单位",
                columnWidth: 0.25,
                name: "undertaker",
                xtype: "textfield",
                emptyText: "请输入承办单位名称",
                maxLength: 64,
                maxLengthText: "最多64个字符,汉字占2个字符"
            }]
        }, {
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            items: [{
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: false,
                fieldLabel: "班级类型",
                columnWidth: 0.25,
                name: "classCategory",
                xtype: "basecombobox",
                //editable:true,
                //readOnly:true,
                ddCode: "ZXXBJLX",
                emptyText: "请输入班级类型",
                maxLength: 16,
                maxLengthText: "最多16个字符,汉字占2个字符",
            },{
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: false,
                fieldLabel: "班级名称",
                columnWidth: 0.25,
                name: "className",
                xtype: "textfield",
                emptyText: "请输入班级名称",
                maxLength: 64,
                maxLengthText: "最多64个字符,汉字占2个字符",
            },
            // {
            //     beforeLabelTextTpl: comm.get('required'),
            //     allowBlank: false,
            //     fieldLabel: "班级编号",
            //     columnWidth: 0.5,
            //     name: "classNumb",
            //     xtype: "textfield",
            //     emptyText: "请输入班级编号",
            //     maxLength: 10,
            //     maxLengthText: "最多10个字符,汉字占2个字符",
            // },
            {
                fieldLabel: "班主任id",
                name: "bzrId",
                xtype: "textfield",
                hidden: true
            },{
                
                beforeLabelTextTpl: comm.get('required'),        
                allowBlank: false,      
                columnWidth: 0.25,
                xtype: "basefuncfield",
                refController: "class.otherController", //该功能主控制器，这里重新指定为当前视图的控制器了
                funcPanel: "class.selectbzr.mainlayout", //该功能显示的主视图
                formPanel:"class.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
                funcTitle: "班主任选择", //查询窗口的标题
                configInfo: {
                    //width:comm.get("clientWidth")*0.8,
                    width:1200,
                    height:650,
                    fieldInfo: "bzrId~bzrName,uuid~xm",
                    whereSql: " and isDelete='0' ",
                    orderSql: " order by createTime DESC ",
                    muiltSelect: true //是否多选
                },
                fieldLabel: '班主任',
                emptyText: "请选择班主任",
                name: "bzrName",        
            },{
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: false,
                fieldLabel: "是否考勤",
                columnWidth: 0.25,
                name: "needChecking",
                xtype: "checkbox",
                //value: 0,
                boxLabel: "考勤",
                emptyText: "请输入是否考勤",
                maxLength: 2,
                maxLengthText: "最多2个字符,汉字占2个字符",
                listeners: {
                    change: function (field, newValue, oldValue) {
                        var currentForm = field.up("baseform[xtype=class.detailform]");
                        var checkoutCTR = currentForm.down("container[ref=checkoutCTR]");

                        if (newValue == true) {
                            checkoutCTR.setVisible (true);
                            currentForm.setHeight(180);
                        } else {                                
                            checkoutCTR.setVisible (false);
                            currentForm.setHeight(150);
                        }
                    }
                }
            },
            // {
            //     beforeLabelTextTpl: comm.get('required'),
            //     allowBlank: false,
            //     fieldLabel: "是否同步学员",
            //     hidden:true,
            //     labelWidth: 110,
            //     columnWidth: 0.17,
            //     name: "needSynctrainee",
            //     xtype: "checkbox",
            //     //value: '1',
            //     boxLabel: "同步",
            //     emptyText: "请输入是否同步学员",
            //     maxLength: 2,
            //     maxLengthText: "最多2个字符,汉字占2个字符",
            // }
            ]
        },
        {
            xtype: "container",
            layout: "column",
            labelAlign: "right",
            hidden: true,
            ref:'checkoutCTR',
            items: [{
                fieldLabel: "考勤规则ID",
                name: "checkruleId",
                xtype: "textfield",
                hidden: true
            }, {
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: true,
               // blankText: "考勤规则不能为空",
                columnWidth:0.25,
                xtype: "basefuncfield",
                refController: "", //该功能主控制器，这里重新指定为当前视图的控制器了
                funcPanel: "coursecheckrule.mainlayout", //该功能显示的主视图
                formPanel: "class.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
                funcTitle: "考勤规则选择", //查询窗口的标题
                configInfo: {
                    width: 1200,
                    height: 650,
                    fieldInfo: "checkruleId~checkruleName,uuid~ruleName",
                    whereSql: " and isDelete='0' and startUsing=1 ",
                    orderSql: " order by createTime DESC ",
                    muiltSelect: false //是否多选
                },
                fieldLabel: "考勤规则",
                emptyText: "请选择考勤规则",
                name: "checkruleName"
            },{
                fieldLabel: "学分规则ID",
                name: "creditsruleId",
                xtype: "textfield",
                hidden: true
            }, {
                beforeLabelTextTpl: comm.get('required'),
                allowBlank: true,
                //blankText: "学分规则不能为空",
                columnWidth: 0.25,
                xtype: "basefuncfield",
                refController: "", //该功能主控制器，这里重新指定为当前视图的控制器了
                funcPanel: "creditrule.mainlayout", //该功能显示的主视图
                formPanel: "class.detailform",   //指定当前表单的别名，方便其他地方能找到这个表单组件
                funcTitle: "学分规则选择", //查询窗口的标题
                configInfo: {
                    width: 1200,
                    height: 650,
                    fieldInfo: "creditsruleId~creditsruleName,uuid~ruleName",
                    whereSql: " and isDelete='0' and startUsing=1 ",
                    orderSql: " order by createTime DESC ",
                    muiltSelect: false //是否多选
                },
                fieldLabel: "学分规则",
                emptyText: "请选择学分规则",
                name: "creditsruleName"
            }]
        },
        // {
        //     xtype: "container",
        //     layout: "column",
        //     labelAlign: "right",
        //     items: [ {
        //         beforeLabelTextTpl: comm.get("required"),
        //         allowBlank: false,
        //         columnWidth: 1,
        //         blankText: "班级简介不能为空",
        //         fieldLabel: "班级简介",     
        //         name: "classDesc",
        //         xtype: "textareafield",
        //         emptyText: "请输入班级简介",
        //         maxLength: 256,
        //         maxLengthText: "最多256个字符,汉字占2个字符",
        //     }]
        // },
        // {
        //     xtype: "container",
        //     layout: "column",
        //     labelAlign: "right",
        //     items: [ {
        //         xtype: "textfield",
        //         fieldLabel: "班级学员ID",
        //         name: "classTraineeId",
        //         hidden: true
        //     },{
        //         xtype: "textfield",
        //         fieldLabel: "学员ID",
        //         name: "traineeId",
        //         hidden: true
        //     },{
        //         xtype: "textfield",
        //         fieldLabel: "学员性别",
        //         name: "traineeXbm",
        //         hidden: true
        //     },{
        //         xtype: "textfield",
        //         fieldLabel: "学员移动电话",
        //         name: "traineePhone",
        //         hidden: true
        //     },{
        //         xtype: "textfield",
        //         fieldLabel: "学员身份证件号",
        //         name: "traineeSfzjh",
        //         hidden: true
        //     }, {
        //         columnWidth: 1,
        //         hidden: true,
        //         //beforeLabelTextTpl: comm.get("required"),
        //         xtype: "basefuncfield",
        //         refController: "class.otherController", //该功能主控制器
        //         funcPanel: "class.selectstudent.mainlayout", //该功能显示的主视图
        //         funcTitle: "选择学员", //查询窗口的标题
        //         formPanel:"class.detailform",
        //         configInfo: {
        //             width:1200,
        //             height:650,
        //             fieldInfo: "traineeId~traineeName~traineeXbm~traineePhone,uuid~xm~xbm~mobilePhone",
        //             whereSql: " and isDelete='0' ",
        //            // orderSql: " order by jobCode ",
        //             muiltSelect: true //是否多选
        //         },
        //         fieldLabel: '班级学员',
        //         name: "traineeName",
        //         emptyText: '选择班级学员'
        //         //allowBlank: true,
        //         //blankText: "所属岗位不能为空"
        //     }]
        // },{
        //     xtype: "container",
        //     layout: "column",
        //     labelAlign: "right",
        //     items: [ {
        //         xtype: "textfield",
        //         fieldLabel: "课程ID",
        //         name: "courseId",
        //         hidden: true
        //     }, {
        //         columnWidth: 1,
        //         hidden: true,
        //         //beforeLabelTextTpl: comm.get("required"),
        //         xtype: "basefuncfield",
        //         refController: "class.otherController", //该功能主控制器
        //         funcPanel: "class.coursemainlayout", //该功能显示的主视图
        //         funcTitle: "班级课程管理", //查询窗口的标题
        //         formPanel:"class.detailform",
        //         configInfo: {
        //             showTbar:true,
        //             width:1200,
        //             height:650,
        //             fieldInfo: "courseId~courseName,uuid~courseName",
        //             whereSql: " and isDelete='0' ",
        //             buttons:[{
        //                 text: '确定并关闭',
        //                 ref: 'ssOkBtn',
        //                 iconCls: 'x-fa fa-reply'                                
        //             }],
        //             //filter: "[{'type':'string','comparison':'=','value':'" + uuid + "','field':'classId'}]",
        //            // orderSql: " order by jobCode ",
        //             muiltSelect: true //是否多选
        //         },
        //         fieldLabel: '班级课程',
        //         name: "courseName",
        //         emptyText: '添加班级成功后，才可添加班级课程',
        //         readOnly:true,
        //         trigger1Cls: null,
        //         /**
        //          * 清除按钮事件
        //          */
        //         onTrigger1Click : function(){
        //            return false;          
        //         },
        //         //allowBlank: true,
        //         //blankText: "所属岗位不能为空"
        //     }]
        // }
        ]
    //}]
});
Ext.define("core.train.teacher.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.teacher.detailform",
    xtype: 'teacherdeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false, 
    //bodyPadding: '0 10 10 0',
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
        //labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "姓名不能为空",
            fieldLabel: "姓名",
            columnWidth: 0.5,
            name: "xm",
            xtype: "textfield",
            emptyText: "请输入姓名",
            maxLength: 64,
            maxLengthText: "最多64个字符,汉字占2个字符",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "性别不能为空",
            fieldLabel: "性别",
            columnWidth: 0.5,
            name: "xbm",
            xtype: "basecombobox",
            ddCode: "XBM",
            emptyText: "请选择性别",
            maxLength: 1,
            maxLengthText: "最多1个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "移动电话不能为空",
            fieldLabel: "移动电话",
            columnWidth: 0.5,
            name: "mobilePhone",
            xtype: "textfield",
            emptyText: "请输入移动电话",
            maxLength: 36,
            maxLengthText: "最多36个字符,汉字占2个字符",
            vtype:'phoneCode',
            submitValue :false      //调用submit方法时，不会提交这里的值，从而可以实现修改值后，在通过params属性传入参数
        }, {
            fieldLabel: "身份证件号",
            columnWidth: 0.5,
            name: "sfzjh",
            xtype: "textfield",
            emptyText: "请输入身份证件号",
            maxLength: 20,
            maxLengthText: "最多20个字符,汉字占2个字符",
            vtype:'idCode',
            submitValue :false
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "职务",
            columnWidth: 0.5,
            name: "position",
            xtype: "textfield",
            emptyText: "请输入职务",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符",
        }, {
            fieldLabel: "行政级别",
            columnWidth: 0.5,
            name: "headshipLevel",
            xtype: "basecombobox",
            ddCode: "HEADSHIPLEVEL",
            emptyText: "请选择行政级别",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{         
            fieldLabel: "学历",
            columnWidth: 0.5,
            name: "xlm",
            xtype: "basecombobox",
            ddCode: "XLM",
            emptyText: "请选择学历",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }, {            
            fieldLabel: "专业",
            columnWidth: 0.5,
            name: "zym",
            xtype:"textfield",
/*            xtype: "basecombobox",
            ddCode: "ZYM",*/
            emptyText: "请输入专业",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "职称",
            columnWidth: 0.5,
            name: "technical",
            xtype: "basecombobox",
            ddCode: "TECHNICAL",
            emptyText: "职称"
        },{
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "校内/校外不能为空",
            fieldLabel: "校内/校外",
            columnWidth: 0.5,
            name: "inout",
            xtype: "basecombobox",
            ddCode: "INOUT",
            emptyText: "请选择校内/校外",
            maxLength: 6,
            maxLengthText: "最多6个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{           
            fieldLabel: "银行",
            columnWidth: 0.5,
            name: "bank",
            xtype: "textfield",
            emptyText: "请输入银行",
            maxLength: 32,
            maxLengthText: "最多32个字符",
        }, {            
            fieldLabel: "银行账户",
            columnWidth: 0.5,
            name: "bankAccount",
            xtype: "textfield",
            emptyText: "请输入银行账户",
            maxLength: 32,
            maxLengthText: "最多32个字符",
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{           
            fieldLabel: "电子邮件",
            columnWidth: 0.5,
            name: "dzxx",
            xtype: "textfield",
            emptyText: "请输入电子邮件",
            maxLength: 32,
            maxLengthText: "最多32个字符,汉字占2个字符",
            vtype:'email'
        }, {            
            fieldLabel: "通讯地址",
            columnWidth: 0.5,
            name: "address",
            xtype: "textfield",
            emptyText: "请输入通讯地址",
            maxLength: 64,
            maxLengthText: "最多64个字符,汉字占2个字符",
        }]
    },{
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: "工作单位",
            allowBlank: false,
            columnWidth: 1,
            name: "workUnits",
            xtype: "textfield",
            emptyText: "请输入工作单位",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{            
            fieldLabel: "老师基本信息",        
            columnWidth:1,
            grow: true,
            height:90,
            name: "teaDesc",
            xtype: "textarea",
            emptyText: "请输入基本信息",
            maxLength: 256,
            maxLengthText: "最多256个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{           
            fieldLabel: "主要研究方向",
            xtype: "textarea",
            columnWidth:1,
            grow: true,
            height:90,
            name: "researchArea",
            emptyText: "请输入主要研究方向",
            maxLength: 1024,
            maxLengthText: "最多1024个字符,汉字占2个字符"
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            //width:450,
            columnWidth:1, 
            xtype: "container",
            layout: "vbox",
            labelAlign: "right",
            items: [{           
                fieldLabel: "主要研究成果",
                width:'100%',
                grow: true,
                height:100,
                name: "researchResult",
                xtype: "textarea",
                emptyText: "请输入主要研究成果",
                maxLength: 1024,
                maxLengthText: "最多1024个字符,汉字占2个字符"
            }, {           
                fieldLabel: "主要讲授专题",
                width:'100%',
                grow: true,
                height:100,
                name: "teachingProject",
                xtype: "textarea",
                emptyText: "请输入主要讲授专题",
                maxLength: 1024,
                maxLengthText: "最多1024个字符,汉字占2个字符"
            },{
                fieldLabel: "照片地址", //用于表单提交时，提交此数据
                name: "zp",
                xtype: "textfield",
                hidden: true
            },{                    
                width:'100%',
                xtype: 'filefield',
                fieldLabel: '照片',
                fileUpload: true,
                name: 'file',
                buttonText:"选择照片",
                emptyText :'支持文件格式：PNG | JPG | JPEG',
                maxLength: 128,
                //maxLengthText: "最多128个字符,汉字占2个字符",
            }]
        },{
            xtype: "container",
            width:180,                  //这里设置的具体的宽度，那么上边的容器设置的columnWidth就会自动减少可用距离
            margin:'0 0 0 10', 
            labelAlign: "right",
            items: [{
                width:'100%',
                height:240,
                xtype:'image',
                ref:'newsImage',
                style: {
                    background: '#f5f5f5',
                    border: '1px solid #e1e1e1'
                },
                src: '',
            }]
        }]
    }]
});
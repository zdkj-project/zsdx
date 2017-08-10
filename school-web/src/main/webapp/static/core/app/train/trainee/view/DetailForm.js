Ext.define("core.train.trainee.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.trainee.detailform",
    xtype: 'traineedeailform',
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '10 15 10 5',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 120,
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
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "姓名不能为空",
            fieldLabel: "姓名",
            columnWidth: 0.5,
            name: "xm",
            xtype: "textfield",
            emptyText: "请输入姓名",
            maxLength: 64,
            maxLengthText: "最多64个字符,汉字占2个字符"
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
            fieldLabel: "移动电话",
            columnWidth: 0.5,
            name: "mobilePhone",
            xtype: "textfield",
            vtype: 'phoneCode',
            emptyText: "请输入移动电话",
            maxLength: 36,
            maxLengthText: "最多36个字符,汉字占2个字符",
        }, {
            beforeLabelTextTpl: comm.get("required"),
            allowBlank: false,
            blankText: "身份证件号不能为空",
            fieldLabel: "身份证件号",
            columnWidth: 0.5,
            name: "sfzjh",
            xtype: "textfield",
            vtype: 'idCode',
            emptyText: "请输入身份证件号",
            maxLength: 20,
            maxLengthText: "最多20个字符,汉字占2个字符",
            inputType: 'password',
            disabled:true
  //          hidden:true
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
            maxLengthText: "最多128个字符,汉字占2个字符"
        }, {
            fieldLabel: "行政级别",
            columnWidth: 0.5,
            name: "headshipLevel",
            xtype: "basecombobox",
            ddCode: "HEADSHIPLEVEL",
            emptyText: "请选择行政级别"
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "电子邮件",
            columnWidth: 0.5,
            name: "dzxx",
            xtype: "textfield",
            vtype: 'email',
            emptyText: "请输入电子邮件",
            maxLength: 32,
            maxLengthText: "最多32个字符,汉字占2个字符",
        },{
            fieldLabel: "学员类型",
            columnWidth: 0.5,
            name: "traineeCategory",
            xtype: "basecombobox",
            ddCode: "TRAINEECATEGORY"
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "政治面貌",
            columnWidth: 0.5,
            name: "zzmmm",
            xtype: "basecombobox",
            ddCode: "ZZMMM",
            emptyText: "请选择政治面貌",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        },{
            fieldLabel: "民族",
            columnWidth: 0.5,
            name: "mzm",
            xtype: "basecombobox",
            ddCode: "MZM",
            emptyText: "请选择民族",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "所在单位",
            columnWidth: 1,
            name: "workUnit",
            xtype: "textfield",
            emptyText: "请输入所在单位",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符",
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
            emptyText: "请输入学历",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }, {
            fieldLabel: "专业",
            columnWidth: 0.5,
            name: "zym",
            xtype:"textfield",
            // xtype: "basecombobox",
            // ddCode: "ZYM",
            emptyText: "请输入专业",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "学位",
            columnWidth: 0.5,
            name: "xwm",
            xtype: "basecombobox",
            ddCode: "XWM",
            emptyText: "请输入学位",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }, {
            fieldLabel: "毕业学校",
            columnWidth: 0.5,
            name: "graduateSchool",
            xtype: "textfield",
            emptyText: "请输入毕业学校",
            maxLength: 128,
            maxLengthText: "最多128个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            fieldLabel: "党校培训证书号",
            columnWidth: 0.5,
            name: "partySchoolNumb",
            xtype: "textfield",
            emptyText: "请输入党校培训证书号",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }, {
            fieldLabel: "行院培训证书号",
            columnWidth: 0.5,
            name: "nationalSchoolNumb",
            xtype: "textfield",
            emptyText: "请输入行院培训证书号",
            maxLength: 16,
            maxLengthText: "最多16个字符,汉字占2个字符",
        }]
    }, {
        xtype: "container",
        layout: "column",
        labelAlign: "right",
        items: [{
            //width:450,
            columnWidth: 1,
            xtype: "container",
            layout: "vbox",
            labelAlign: "right",
            items: [{
                fieldLabel: "通讯地址",
                width: '100%',
                //height:80,
                grow: true,
                height:110,
                name: "address",
                xtype: "textarea",
                emptyText: "请输入通讯地址",
                maxLength: 64,
                maxLengthText: "最多64个字符,汉字占2个字符",
            }, {
                fieldLabel: "照片地址",     //用于表单提交时，提交此数据
                name: "zp",
                xtype: "textfield",
                hidden: true
            }, {
                width: '100%',
                xtype: 'filefield',
                fieldLabel: '照片',
                fileUpload: true,
                name: 'file',
                buttonText: "选择照片",
                emptyText: '支持文件格式：PNG | JPG | JPEG',
                maxLength: 128,
                maxLengthText: "最多128个字符,汉字占2个字符",
            }]
        }, {
            xtype: "container",
            width:120,  
            margin: '0 0 0 10',
            labelAlign: "right",
            items: [{
                width: '100%',
                height: 150,
                xtype: 'image',
                ref: 'newsImage',
                style: {
                    background: '#f5f5f5',
                    border: '1px solid #e1e1e1'
                },
                src: '',
            }]
        }]
    }]
});
               

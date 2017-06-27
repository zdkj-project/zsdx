Ext.define("core.system.user.view.UserForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.user.userform",
    xtype: 'userdetialform',
    layout: "form",
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
        xtype: "textfield",
        fieldLabel: "主键",
        name: "uuid",
        hidden: true
    }, {
        xtype: "textfield",
        fieldLabel: "默认密码",
        name: "userPwd",
        hidden: true
    }, {
        xtype: "textfield",
        fieldLabel: "排序号",
        name: "orderIndex",
        hidden: true
    }, {
        xtype: 'container',
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: '用户名',
            name: "userName",
            xtype: 'textfield',
            allowBlank: false,
            emptyText: '请输入用户名',
            blankText: "用户名不能为空"
        }, {
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: '真实姓名',
            name: "xm",
            xtype: 'textfield',
            allowBlank: false,
            emptyText: '请输入真实姓名',
            blankText: "真实姓名不能为空"
        }]
    }, {
        xtype: 'container',
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "basecombobox",
            fieldLabel: "性别",
            name: "xbm",
            ddCode: "XBM",
            allowBlank: false,
            blankText: "性别不能为空"
        }, {
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "basecombobox",
            fieldLabel: "编制",
            name: "zxxbzlb",
            ddCode: "ZXXBZLB",
            allowBlank: false,
            blankText: ""
        }]
    }, {
        xtype: 'container',
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "部门ID",
            name: "deptId",
            hidden: true
        }, {
            xtype: "textfield",
            fieldLabel: "岗位ID",
            name: "jobId",
            hidden: true
        }, /*{
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            //xtype: "textfield",
            fieldLabel: "所属部门",
            name: "deptName",
            allowBlank: false,
            blankText: "所属部门不能为空",
            //readOnly: true,
            xtype: "basetreefield",
            ddCode: "DEPTTREE",
            rootId: "ROOT",
            configInfo: {
                multiSelect: false,
                fieldInfo: "deptName~deptId,text~id",
                whereSql: " and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex asc"
            } //
        },*/ {
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "basefuncfield",
            funcController: "core.systemset.jobinfo.controller.jobinfoController", //该功能主控制器
            funcPanel: "jobinfo.mainlayout", //该功能显示的主视图
            funcTitle: "岗位选择", //查询窗口的标题
            configInfo: {
                fieldInfo: "jobId~jobName,uuid~jobName",
                whereSql: " and isDelete='0' ",
                orderSql: " order by jobCode ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '所属岗位',
            name: "jobName",
            allowBlank: true,
            blankText: "所属岗位不能为空"
        }]
    }, {
        xtype: 'container',
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "basecombobox",
            fieldLabel: "账号状态",
            name: "state",
            ddCode: "ACCOUNTSTATE",
            allowBlank: false,
            blankText: ""
        }, {
            columnWidth: .5,
            beforeLabelTextTpl: "",
            fieldLabel: '移动电话',
            name: "mobile",
            xtype: 'textfield',
            allowBlank: true,
            emptyText: '请输入移动电话',
            blankText: "移动电话不能为空"
        }]
    }, {
        xtype: 'container',
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: .5,
            beforeLabelTextTpl: "",
            fieldLabel: '固定电话',
            name: "telphone",
            xtype: 'textfield',
            allowBlank: true,
            emptyText: '请输入固定电话',
            blankText: "固定电话不能为空"
        }, {
            columnWidth: .5,
            beforeLabelTextTpl: "",
            fieldLabel: '电子邮箱',
            name: "eMail",
            xtype: 'textfield',
            allowBlank: true,
            emptyText: '请输入电子邮箱',
            blankText: "电子邮箱不能为空"
        }]
    }]
});
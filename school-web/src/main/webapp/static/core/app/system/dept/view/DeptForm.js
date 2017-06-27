Ext.define("core.system.dept.view.DeptForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.dept.deptform",
    xtype: "deptdeailform",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    bodyPadding: "10 20",

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
        fieldLabel: "上级部门类型",
        name: "parentType",
        hidden: true
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 0.6,
            beforeLabelTextTpl: comm.get("required"),
            fieldLabel: "顺序号",
            xtype: "numberfield",
            name: "orderIndex",
            allowBlank: false,
            emptyText: "同级别的部门的显示顺序",
            blankText: "顺序号不能为空"
        }, {
            columnWidth: 0.4,
            xtype: "label",
            margin:'5 0 0 5 ',
            html: "  (<font color=red,size=12>同级别的部门的显示顺序,不能重复)</font>",
        }]
    },  {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "上级部门ID",
            name: "parentNode",
            hidden: true
        }, 
        {
       
            columnWidth: .5,
            beforeLabelTextTpl: comm.get("required"),          
            fieldLabel: "上级部门",
            name: "parentName",
            allowBlank: false,      

            xtype: "basetreefield",
            ddCode: "DEPTTREE",
            rootId: "ROOT",
            configInfo: {
                multiSelect: false,
                fieldInfo: "parentName~parentNode~parentType,text~id~parentType",
                whereSql: " and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex asc",
                url: comm.get('baseUrl') + "/BaseOrg/treelist",
            } 
        }, 
        {
            columnWidth: 0.5,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "basecombobox",
            fieldLabel: "部门类型",
            name: "deptType",
            ddCode: "DEPTTYPE",
            emptyText: '部门类型',
            blankText: "部门类型不能为空",
            listeners: {
                change: function(combo, record, index) {
                    

                    var store = combo.up("panel").down("combobox[ref=comboxCourse]").getStore();
                    var proxy = store.getProxy();
                    var filter = "[{'type':'string','comparison':'=','value':'aaaa','field':'uuid'}]";
                    if (record == "06") {
                        proxy.extraParams = {
                           filter: ""
                        };
                        store.load(); 
                        combo.up("panel").down("combobox[ref=comboxCourse]").setEditable (false);
                    } else  if (record == "04") {
                        var baseform= combo.up("baseform");
                        var gradeContainer=baseform.down("container[ref=gradeContainer]");                       
                        gradeContainer.setVisible(true);
                     
                        baseform.down("combobox[name=sectionCode]").allowBlank=false;                    
                        baseform.down("combobox[name=nj]").allowBlank=false;
                        
                        combo.up("panel").down("combobox[ref=comboxCourse]").setEditable(true);

                        baseform.up("window").setHeight(435);

                        //gradeContainer.doLayout();
                        //baseform.doLayout();
                    } else {

                        proxy.extraParams = {
                           filter: filter
                        };
                        store.load(); 

                        combo.up("panel").down("combobox[ref=comboxCourse]").setEditable(true);
                    }


                    if(record!="04"){
                        var baseform= combo.up("baseform");
                        var gradeContainer=baseform.down("container[ref=gradeContainer]");                       
                        gradeContainer.setVisible(false);                    
                        baseform.down("combobox[name=sectionCode]").allowBlank=true;                    
                        baseform.down("combobox[name=nj]").allowBlank=true;

/*                        if(baseform.up("window").getHeight()!=400)
                            baseform.up("window").setHeight(400);*/

                    }
                }
            }
        }]
    },{
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 0.5,
            beforeLabelTextTpl: comm.get("required"),
            xtype: "combobox",
            ref: "comboxCourse",
            //store: "core.system.dept.store.CourseStore",
            store: {
                type: 'dept.deptstore',
                //.......这里可以写传入这个store的其他参数
                //model:'core.good.signup.model.SignupGridModel',
            },
           
            fieldLabel: "部门名称",
            name: "nodeText",
            displayField: 'courseName',
            valueField: 'courseName',
            // allowBlank: false,
            // emptyText: '部门名称',
            // blankText: "部门名称不能为空"

        }, {
            columnWidth: 0.5,
            beforeLabelTextTpl: "",
            xtype: "textfield",
            fieldLabel: "传真号码",
            name: "fax",
            allowBlank: true,
            emptyText: '传真号码',
            blankText: ""
        }]
    },    {
        xtype: "container",
        layout: "column", // 从左往右的布局
        ref:'gradeContainer',
        hidden:true,
        items: [{
            columnWidth: 0.5,
            beforeLabelTextTpl: comm.get('required'),
            xtype: "basecombobox",
            fieldLabel: "学段",
            name: "sectionCode",
            ddCode: "RKXD",
            //allowBlank: false,
            emptyText: '请选择学段',
            blankText: "学段不能为空",
            
        }, {
            columnWidth: 0.5,
            beforeLabelTextTpl: comm.get('required'),
            xtype: "basecombobox",
            fieldLabel: "年级",
            name: "nj",
            ddCode: "NJ",
            //allowBlank: false,
            emptyText: '请选择年级',
            blankText: "年级不能为空",
         
        }]
    },  {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 0.5,
            beforeLabelTextTpl: "",
            xtype: "textfield",
            fieldLabel: "内线电话",
            name: "inPhone",
            allowBlank: true,
            emptyText: '内线电话',
            blankText: ""
        }, {
            columnWidth: 0.5,
            beforeLabelTextTpl: "",
            xtype: "textfield",
            fieldLabel: "外线电话",
            name: "outPhone",
            allowBlank: true,
            emptyText: '外线电话',
            blankText: ""
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            xtype: "textfield",
            fieldLabel: "主负责岗位ID",
            name: "mainLeader",
            hidden: true
        }, {
            xtype: "textfield",
            fieldLabel: "副负责岗位ID",
            name: "viceLeader",
            hidden: true
        },{
            columnWidth: 0.5,
            // beforeLabelTextTpl: '',
            // xtype: "textfield",
            // fieldLabel: "副负责人",
            // name: "viceLeader",
            // allowBlank: true,
            // emptyText: '副负责人',
            // blankText: ''
            xtype: "basefuncfield",
            //funcController: "core.systemset.jobinfo.controller.jobinfoController", //该属性现在不需要了
            funcPanel: "jobinfo.mainlayout", //该功能显示的主视图
            refController:'dept.otherController',             //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
            funcTitle: "主岗位选择", //查询窗口的标题
            configInfo: {

                fieldInfo: "mainLeader~mainLeaderName,uuid~jobName",
                whereSql: " and isDelete='0' ",
                orderSql: " order by jobCode ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '主负责岗位',
            name: "mainLeaderName",
            allowBlank: true,
        }, {
            columnWidth: 0.5,
            // beforeLabelTextTpl: '',
            // xtype: "textfield",
            // fieldLabel: "副负责人",
            // name: "viceLeader",
            // allowBlank: true,
            // emptyText: '副负责人',
            // blankText: ''
            xtype: "basefuncfield",
            //funcController: "core.systemset.jobinfo.controller.jobinfoController", //该属性现在不需要了
            funcPanel: "jobinfo.mainlayout", //该功能显示的主视图
            refController:'dept.otherController',            //指定弹出的window引用的控制器，方便方法重写。 若不需要重写，则不配置此项
            funcTitle: "副岗位选择", //查询窗口的标题
            configInfo: {
                fieldInfo: "viceLeader~viceLeaderName,uuid~jobName",
                whereSql: " and isDelete='0' ",
                orderSql: " order by jobCode ",
                muiltSelect: true //是否多选
            },
            fieldLabel: '副负责岗位',
            name: "viceLeaderName",
            allowBlank: true,
        }]
    }, {
        xtype: "container",
        layout: "column", // 从左往右的布局
        items: [{
            columnWidth: 01,
            beforeLabelTextTpl: "",
            xtype: "textarea",
            fieldLabel: "备注",
            name: "remark",
            height: 100,
            allowBlank: true,
            emptyText: '备注',
            blankText: "备注不能为空"
        }]
    }]
});
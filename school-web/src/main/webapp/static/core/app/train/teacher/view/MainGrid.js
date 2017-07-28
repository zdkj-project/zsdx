Ext.define("core.train.teacher.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.teacher.maingrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTeacher/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainTeacher", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: {
        xtype: 'teacher.mainquerypanel',
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd_Tab',
            funCode: 'girdFuntionBtn', //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
         xtype: 'button',
         text: '发送短信',
         ref: 'gridEdit',
         funCode: 'girdFuntionBtn',
         disabled: true,
         iconCls: 'x-fa fa-pencil-square'
         }, /*{
         xtype: 'button',
         text: '主讲课程',
         ref: 'gridCourseDetail',
         funCode: 'girdFuntionBtn',
         disabled: false,
         iconCls: 'x-fa fa-book'
         }, {
         xtype: 'button',
         text: '上课记录',
         ref: 'gridTeachDetail',
         funCode: 'girdFuntionBtn',
         disabled: false,
         iconCls: 'x-fa fa-history'
         },*/ {
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode: 'girdFuntionBtn',
            disabled: true,
            iconCls: 'x-fa fa-minus-circle'
        }, {
            xtype: 'button',
            text: '导入',
            ref: 'gridImport',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-clipboard'
        }, {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-file'
        }, {
            xtype: 'button',
            text: '下载模板',
            ref: 'gridDownTemplate',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file-text'
        }, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'xm',
            funCode: 'girdFastSearchText',
            emptyText: '请输入姓名'
        }, {
            xtype: 'button',
            funCode: 'girdSearchBtn', //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search',
        }, ' ', {
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }],
    },
    /** 排序字段定义 */
    defSort: [{
        property: "updateTime", //字段名
        direction: "DESC" //升降序
    },{
        property: "createTime", //字段名
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 60,
            text: '序号',
            align: 'center'
        }, {
            width: 100,
            text: "姓名",
            dataIndex: "xm",
            align: 'left'
        }, {
            width: 80,
            text: "性别",
            dataIndex: "xbm",
            columnType: "basecombobox", //列类型
            ddCode: "XBM", //字典代码
            align: 'left'
        }, {
            width: 160,
            text: "职务",
            dataIndex: "position",
            align: 'left'
        }, {
            width: 120,
            text: "职称",
            dataIndex: "technical",
            columnType: "basecombobox", //列类型
            ddCode: "TECHNICAL", //字典代码
            align: 'left'
        }, {
            width: 150,
            text: "学历",
            dataIndex: "xlm",
            columnType: "basecombobox", //列类型
            ddCode: "XLM", //字典代码
            align: 'left'
        }, {
            width: 150,
            text: "手机号码",
            dataIndex: "mobilePhone",
            align: 'left'
        }, {
            width: 100,
            text: "校内/校外",
            dataIndex: "inout",
            columnType: "basecombobox", //列类型
            ddCode: "INOUT", //字典代码
            align: 'left'
        }, {
            flex: 1,
            minWidth: 100,
            text: "工作单位",
            dataIndex: "workUnits",
            align: 'left'
        }, {
            width: 150,
            text: "创建时间",
            dataIndex: "createTime",
            align: 'left'
        }, {
            width: 150,
            text: "更新时间",
            dataIndex: "updateTime",
            align: 'left'
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 300,
            fixed: true,
            items: [{
                text: '个人简介',
                style: 'font-size:12px;',
                tooltip: '个人简介',
                ref: 'gridDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridTeacherInfoClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                text: '主讲课程',
                style: 'font-size:12px;',
                tooltip: '主讲课程',
                ref: 'gridTeachDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridTeacherCourseClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: "detail"
                    });
                }
            }, {
                text: '上课记录',
                style: 'font-size:12px;',
                tooltip: '上课记录',
                ref: 'gridTechingDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridTeachingClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                text: '编辑',
                style: 'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridEdit',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("TCMANAGER") == -1){
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }, {
                text: '详细',
                style: 'font-size:12px;',
                tooltip: '详细',
                ref: 'gridEdit',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view: view.grid,
                        record: rec
                    });
                }
            }, {
                text: '删除',
                style: 'font-size:12px;',
                tooltip: '删除',
                ref: 'gridDelete',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view: view.grid,
                        record: rec
                    });
                },
                getClass: function (v, metadata, record) {
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("TCMANAGER") == -1) {
                        return 'x-hidden-display';
                    } else
                        return null;
                }
            }]
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});

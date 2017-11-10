Ext.define("core.oa.meeting.checkresult.view.CheckResultGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.checkresult.checkresultgrid",
    dataUrl: comm.get('baseUrl') + "/OaMeetingemp/list",
    model: "com.zd.school.oa.meeting.model.OaMeetingemp", //对应的数据模型
    al: false,
    frame: false,
    columnLines: false,
    
    //排序字段及模式定义
    defSort: [{
        property: 'xm',
        direction: 'ASC'
    }],
    extParams: {},
    tbar: [],
    panelTopBar:{
        xtype:'toolbar',
        items: [/*{
            xtype: 'button',
            text: '添加',
            ref: 'gridAddUser',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelUser',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-minus-circle'
        },*/'->',{
            xtype: 'tbtext',
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'xm',
            funCode:'girdFastSearchText',
            isNotForm:true,   //由于文本框重写了baseform下面的funcode值，所以使用这个属性，防止重写这里设定的fundcode值。
            emptyText: '请输入姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search'
        }]
    },
    panelBottomBar: false,
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        }, {
            text: "姓名",
            dataIndex: "xm",
            width: 100
        }, {
            text: "性别",
            width: 60,
            dataIndex:"xbm"
        }, {
            text: "部门",
            dataIndex: "deptName",
            width: 120
        }, {
            text: "岗位",
            dataIndex: "jobName",
            width: 100
        }, {
            text: "签到时间",
            dataIndex: "incardTime",
            width: 150,
            renderer: function (value, metaData) {
                var title = "签到时间";
                if (Ext.isEmpty(value))
                    return value;
                else {
                    var date = value.replace(new RegExp(/-/gm), "/");
                    var ss = Ext.Date.format(new Date(date), 'Y-m-d  H:i:s');
                    var html = ss;
                    metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                    return ss;
                }
            }
        }, {
            text: "签退时间",
            dataIndex: "outcardTime",
            width: 150,
            renderer: function (value, metaData) {
                var title = "签退时间";
                if (Ext.isEmpty(value))
                    return value;
                else {
                    var date = value.replace(new RegExp(/-/gm), "/");
                    var ss = Ext.Date.format(new Date(date), 'Y-m-d  H:i:s');
                    var html = ss;
                    metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                    return ss;
                }
            }
        }, {
            text: "考勤结果",
            dataIndex: "attendResult",
            width: 100,
            renderer: function (value, metaData) {
                //1-正常 2-迟到 3-早退 4-缺勤5-迟到早退
                var html = value;
                switch (value) {
                    case "1":
                        html = "正常";
                        break;
                    case "2":
                        html = "迟到";
                        break;
                    case"3":
                        html = "早退";
                        break;
                    case "4":
                        html = "缺勤";
                        break;
                    case "5":
                        html = "迟到早退";
                        break;
                    default:
                        html="未考勤";
                        break;
                }
                //metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            text: "结果说明",
            dataIndex: "resultDesc",
            flex: 1
        }]
    }
});
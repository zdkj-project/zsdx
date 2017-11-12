Ext.define("core.oa.meeting.meetinginfo.view.MeetingUserGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.meetinginfo.meetingusergrid",
	dataUrl: comm.get('baseUrl') + "/OaMeetingemp/list",
    model: "com.zd.school.oa.meeting.model.OaMeetingemp", //对应的数据模型
	al: false,
    frame: false,
    columnLines: false,
    selModel:null,
    // style: {
    //     border: '1px solid #ddd'
    // },
	//排序字段及模式定义
	defSort: [{
		property: 'xm',
		direction: 'ASC'
	}],
	extParams: {
	},
    tbar: [],
    //panelTopBar:false,
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
        },{
            xtype: 'button',
            text: '请假',
            ref: 'gridSetLeave',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '补录考勤',
            ref: 'gridSetAttend',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-minus-circle'
        },{
            xtype: 'button',
            text: '备注',
            ref: 'gridSetRemark',
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
    panelBottomBar:false,
	columns: { 
        defaults:{
            align:'center',
            titleAlign:"center"
        },
        items:[{
            xtype: "rownumberer",
            width: 50,
            text: '序号',
            align: 'center'
        },{
			text: "主键",
			dataIndex: "uuid",
			hidden: true
		},{
			text: "姓名",
			dataIndex: "xm",
			width:100
		}, {
			text: "性别",
			dataIndex: "xbm",
			columnType: "basecombobox",
			ddCode: "XBM",
			width:70
		}, {
			text: "部门",
			dataIndex: "deptName",
			width:150
		}, {
            text: "岗位",
            dataIndex: "jobName",
            width:100
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
        },/* {
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
        },*/ {
            text: "考勤结果",
            dataIndex: "attendResult",
            width:100,
            renderer: function(value, metaData) {
                //1-正常 2-迟到 3-早退 4-缺勤5-迟到早退
                var html = value;
                switch (value){
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
        },{
            text:"是否请假",
            dataIndex:"isLeave",
            width:80,
            renderer: function(value, metaData) {
                if(value=="1"){
                    return "<span style='color:red'>是</span>"
                }else{
                    return "<span style='color:green'>否</span>"
                }
            }
        },{
        	text:"备注与说明",
			dataIndex:"resultDesc",
			flex:1,
            minWidth:100,
            renderer: function (value, metaData) {
                var title = "备注与说明";
               
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
                
            }
		},{
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 200,
            align:'center',
            fixed: true,
            items: [{
                text: ' 请假 ',
                style: 'font-size:13px;',
                tooltip: '请假',
                ref: 'gridDetail',        
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridSetLeave', {
                        view: view.grid,
                        record: rec,
                        value:1
                    });
                },
                getClass :function(v,metadata,record){
                
                    if(record.get("isLeave")==1){
                        return 'x-hidden-display';
                    }
                    else
                        return null;
                }, 
            },{
                text: ' 取消请假 ',
                style: 'font-size:13px;',
                tooltip: '取消请假',
                ref: 'gridDetail',        
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridSetLeave', {
                        view: view.grid,
                        record: rec,
                        value:0
                    });
                },
                getClass :function(v,metadata,record){
                
                    if(record.get("isLeave")!=1){
                        return 'x-hidden-display';
                    }
                    else
                        return null;
                }, 
            },{
                text:' 补录考勤 ',  
                style:'font-size:13px;',  
                tooltip: '补录考勤',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridSetAttend', {
                        view: view.grid,
                        record: rec
                    });
                }
            },{
                text:' 备注 ',  
                style:'font-size:13px;',  
                tooltip: '备注',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridRemark', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }]
	}
});
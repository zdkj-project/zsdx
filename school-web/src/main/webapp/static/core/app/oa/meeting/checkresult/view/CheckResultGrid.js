Ext.define("core.oa.meeting.checkresult.view.CheckResultGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.checkresult.checkresultgrid",
	dataUrl: comm.get('baseUrl') + "/OaMeetingemp/list",
    model: "com.zd.school.oa.meeting.model.OaMeetingemp", //对应的数据模型
	al: false,
    frame: false,
    columnLines: false,
    style: {
        border: '1px solid #ddd'
    },
	//排序字段及模式定义
	defSort: [{
		property: 'xm',
		direction: 'ASC'
	}],
	extParams: {
	},
    tbar: [],
    panelTopBar:false,
    panelBottomBar:false,
	columns: { 
        defaults:{
            align:'center',
            titleAlign:"center"
        },
        items:[{
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
			width:100
		}, {
			text: "部门",
			dataIndex: "deptName",
			width:100
		}, {
            text: "岗位",
            dataIndex: "jobName",
            width:100
        }, {
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

                }
                //metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        },{
        	text:"结果说明",
			dataIndex:"resultDesc",
			flex:1
		}]
	}
});
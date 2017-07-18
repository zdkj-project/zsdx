Ext.define("core.oa.meeting.meetinginfo.view.selectUserGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.meetinginfo.selectsysuser.selectusergrid",
	dataUrl: comm.get('baseUrl') + "/sysuser/teacherlist",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
	al: true,
	//排序字段及模式定义
	defSort: [{
		property: 'userNumb',
		direction: 'ASC'
	}, {
		property: 'state',
		direction: 'DESC'
	}],
	extParams: {
		whereSql: "",
		orderSql: "",
		filter: '[{"type":"numeric","comparison":"=","value":0,"field":"isDelete"},{"type":"string","comparison":"=","value":"1","field":"category"}, {"type": "numeric","comparison": "=","value": "1","field": "issystem"}]'
	},
	//title: "<font color=red>可选人员(选中后向右拖动或双击添加)</font>",
	viewConfig: {
		plugins: {
			ptype: 'gridviewdragdrop',
			dragGroup: 'firstGridDDGroup',
			dropGroup: 'secondGridDDGroup'
		},
		listeners: {
			drop: function(node, data, dropRec, dropPosition) {
				var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
				//Ext.example.msg("Drag from right to left", 'Dropped ' + data.records[0].get('name') + dropOn);
			}
		}
	},
	tbar: null,
	panelTopBar:{
        xtype:'toolbar',
        items: ['->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'xm',
            funCode:'girdFastSearchText', 
            emptyText: '请输入姓名'
        }]
    },
	panelButtomBar:null,
	columns: { 
        defaults:{
            //flex:1,     //【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            align:'center',
            titleAlign:"center"
        },
        items:[{
			text: "主键",
			dataIndex: "uuid",
			hidden: true
		}, {
			text: "编号",
			dataIndex: "userNumb",
			flex:1, 
		}, {
			text: "姓名",
			dataIndex: "xm",
			flex:1, 
		}, {
			text: "性别",
			dataIndex: "xbm",
			columnType: "basecombobox",
			ddCode: "XBM",
			flex:1, 
		}, {
			text: "编制",
			dataIndex: "zxxbzlb",
			ddCode: "ZXXBZLB",
			columnType: "basecombobox",
			flex:1, 
		}, {
			text: "岗位",
			dataIndex: "jobName",
			flex:1, 
		}]
	}
});
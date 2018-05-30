Ext.define("core.mjmanage.userauthority.view.UserGrid", {
    extend: "core.base.view.BaseGrid",
	alias: "widget.mjmanage.userauthority.usergrid",
	//dataUrl: comm.get('baseUrl') + "/teacherBase/list",
	dataUrl: comm.get('baseUrl') + "/sysuser/list",
	model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
	al: false,
    pageDisplayInfo:false,
	multiSelect: true,
	menuCode:"USER_ACCESS_CONTROL",
	//排序字段及模式定义
    defSort: [{
        property: 'userNumb',
        direction: 'ASC'
    }],
    extParams: {
        //filter: '[{"type":"string","comparison":"=","value":"1","field":"category"}]'
    },
	selModel:null,
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '人员列表',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'xm',
            funCode: 'girdFastSearchText',
            emptyText: '请输入姓名'
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型  
            iconCls: 'x-fa fa-search',  
        }]
    },
    panelButtomBar:null,
    columns: {        
    	defaults:{
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
    	}, {
    		text: "编号",
    		dataIndex: "userNumb",
    		flex:1,
    		minWidth:80
    	}, {
    		text: "姓名",
    		dataIndex: "xm",
    		flex:1,
            minWidth:80
    	}, {
    		text: "性别",
    		dataIndex: "xbm",
    		columnType: "basecombobox",
    		ddCode: "XBM",
    		flex:1,
            minWidth:80
    	}, {
    		text: "主部门岗位",
    		dataIndex: "deptName",
    		flex:1,
            minWidth:80,
            renderer: function(value, metaData,record) {    
                var deptName=record.get("deptName");
                var jobName=record.get("jobName");
                if(deptName&&jobName)
                    return  deptName+"-"+jobName;       
            }
    	}]
    },
    /*
	listeners: {
        beforeitemdblclick: function(grid, record, item, index, e, eOpts) {
            return false;
        },
        beforeitemmousedown: function(grid, record, item, index, e, eOpts) {
            var model = grid.getSelectionModel();  
            var flag=model.isSelected(index);
            if (flag) {
                model.deselect(index);
                return false;
            }
        }
    }
    */
});
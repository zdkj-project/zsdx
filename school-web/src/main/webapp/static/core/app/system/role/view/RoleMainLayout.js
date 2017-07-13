Ext.define("core.system.role.view.RoleMainLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.role.mainlayout',

    requires: [    
        'core.system.role.controller.RoleController',
        "core.system.role.view.RoleGrid",
        'core.system.role.view.RoleUserGrid', 
        "core.system.role.view.RoleDetailLayout",
        'core.system.role.view.RoleDetailForm',
    ],
   
    controller:'role.roleController',
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    //otherController:'role.otherController',


    funCode: "role_main",
    detCode: 'role_detail',
    detLayout: 'role.detaillayout',
    funData: {
        action: comm.get('baseUrl') + "/sysrole", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "uuid",
        defaultObj: {
            orderIndex: 1,
            issystem: 1
        },
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
    	addTitle:'添加角色',
    	editTitle:'编辑角色',
    	detailTitle:'角色用户'
        }
    },
    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{        
        xtype: "role.rolegrid",
        region: "center",
        /*style:{
            border: '1px solid #ddd'
        },*/
    }]
})
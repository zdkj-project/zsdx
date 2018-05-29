Ext.define("core.system.dept.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    
    requires: [    
        "core.system.dept.view.MainLayout",
        "core.system.dept.view.DeptLayout",
        "core.system.dept.view.DeptGrid",
        "core.system.dept.view.DeptForm",
        "core.system.dept.store.DeptStore",
        "core.system.dept.store.CourseStore"
   
    ],
    
    controller: 'dept.deptController',
    otherController:'dept.otherController',
    
    alias: 'widget.dept.mainlayout',
    funCode: "deptinfo_main",
    detCode: "deptinfo_detail",
    detLayout: "deptinfo.detaillayout",
    funData: {
        action: comm.get('baseUrl') + "/BaseOrg", //请求controller路径
        whereSql: "and isDelete='0'",
        orderSql: ' order by parentNode,orderIndex asc',
        pkName: "uuid", //主键id    
        defaultObj: {
            orderIndex: 1
        },
		tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
		    addTitle:'添加下级',
		    editTitle:'编辑',
		    AddBrotherTitle:'添加同级'
		}
    },
    items: [{
        xtype: "dept.deptgrid",
        
    }]
})
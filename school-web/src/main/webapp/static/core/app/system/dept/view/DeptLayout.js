Ext.define("core.system.dept.view.DeptLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.deptinfo.detaillayout',
    funCode: "deptinfo_detail",
    funData: {
        action: comm.get('baseUrl') + "/BaseOrg", //请求controller路径
        whereSql: "  and isDelete='0'",
        orderSql: ' order by parentNode,orderIndex asc',
        pkName: "uuid", //主键id    
        defaultObj: {
            orderIndex: 1
        }
    },
    
	/*关联此视图控制器*/

    items: [{
        xtype: "dept.deptform"
    }]
})
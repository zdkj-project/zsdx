/**
 * Created by luoyibo on 2017-05-26.
 */
Ext.define("core.train.course.view.SelectTeacherLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.course.selectteacher.mainlayout',
    funCode: "selectteacher_detail",
    border: false,
    //funData用来定义一些常规的参数
    funData: {
        action: comm.get('baseUrl') + "/sysuser", //请求controller
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "", //表格过滤条件
        pkName: "uuid", //主键
        //默认的初始值设置
        defaultObj: {
        }
    },
    layout: 'border',
    items: [{
        xtype:'course.selectteachergrid',
        width: 800,
        region: "west",
        margin:'5',
    }, {
        xtype: "course.isselectedteachergrid",
        region: "center",
        margin:'5',
    }]
})
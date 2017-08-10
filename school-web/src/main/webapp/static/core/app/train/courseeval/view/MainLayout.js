Ext.define("core.train.courseeval.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.courseeval.mainlayout",
    /** 引入必须的文件 */
    requires: [ 
        'core.train.courseeval.controller.MainController',
        'core.train.courseeval.view.MainGrid',
        'core.train.courseeval.view.MainQueryPanel',
        "core.train.courseeval.view.DetailLayout",
    ],    
    /** 关联此视图控制器 */
    controller: 'courseeval.mainController',
    /** 页面代码定义 */
    funCode: "courseeval_main",
    detCode: "courseeval_detail",
    detLayout: "courseeval.detaillayout",
    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController:'courseeval.otherController',
    layout:'border',
    border:false,
    funData: {
        action: comm.get("baseUrl") + "/TrainEvalcourseeval", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        defaultObj: {} ,
        tabConfig: {
            addTitle: '添加指标',
            editTitle: '编辑指标',
            detailTitle: '指标详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,
    
    items: [{
            xtype: "courseeval.maingrid",
            region: "west",
            width: 410,
            //height:300,
            split: true,
            style: {
                border: '1px solid #ddd'
            },
            frame: false
        }, {
            xtype: "courseeval.evalgrid",
            region: "center",
            flex: 1.5,
            style: {
                border: '1px solid #ddd'
            }
        }]
})

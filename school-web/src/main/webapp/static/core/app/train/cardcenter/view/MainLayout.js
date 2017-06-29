Ext.define("core.train.cardcenter.view.MainLayout", {
    extend: "core.base.view.BasePanel",
    alias: "widget.cardcenter.mainlayout",
    /** 引入必须的文件 */
    requires: [
        'core.train.cardcenter.controller.MainController'
    ],
    /** 关联此视图控制器 */
    controller: 'cardcenter.mainController',

    /** 页面代码定义 */
    funCode: "cardcenter_main",
    detCode: "cardcenter_detail",
    detLayout: "cardcenter.detaillayout",

    /*标注这个视图控制器的别名，以此提供给window处使用*/
    otherController: 'cardcenter.otherController',

    layout: 'border',
    border: false,
    funData: {
        action: comm.get("baseUrl") + "/TrainClasstrainee", //请求Action
        whereSql: "", //表格查询条件
        orderSql: "", //表格排序条件
        filter: "",
        pkName: "uuid",
        width: 1000,     //定义弹出window的宽度
        height: 600,    //定义弹出window的高度
        defaultObj: {},
        tabConfig:{         //zzk：2017-6-1加入，用于对tab操作提供基本配置数据
            addTitle:'添加班级',
            editTitle:'编辑班级',
            detailTitle:'班级详情'
        }
    },

    /*设置最小宽度，并且自动滚动*/
    minWidth:1200,
    scrollable:true,

   
    items: [{
            xtype: "cardcenter.classgrid",
            //title:'班级列表',
            region: "west",
            width: 455,
            //height:300,
            split: true,
            style: {
                border: '1px solid #ddd'
            },
            frame: false
        }, {
            xtype: "cardcenter.maingrid",
            region: "center",
            flex: 1.5,
            style: {
                border: '1px solid #ddd'
            }
        }]
});

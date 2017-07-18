Ext.define("core.train.coursetype.view.ChangeOrderGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.coursetype.changeordergrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainCoursecategory/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainCoursecategory", //对应的数据模型
    al: true,
    /**
     * 高级查询面板
     */
    panelButtomBar: false,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: false,
    /** 排序字段定义 */
    remoteSort:false,
    defSort: [{
        property: "orderIndex", //字段名
        direction: "ASC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: ""
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    viewConfig: {
        plugins: {
            ptype: "gridviewdragdrop",
            dragText: "可用鼠标拖拽进行上下排序",
            dragGroup: 'secondGridDDGroup',
            dropGroup: 'secondGridDDGroup'
        },
        listeners: {
            drop: function(node, data, dropRec, dropPosition) {
            }
        }
    },
    plugins: {
        ptype: 'cellediting',
        clicksToEdit: 1
    },
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [/*{
            xtype: "rownumberer",
            width: 60,
            text: '序号',
            align: 'center'
        }, */{
            width: 100,
            text: "分类名称",
            dataIndex: "nodeText",
            align: 'left'
        }, {
            flex: 1,
            text: "排序号",
            dataIndex: "orderIndex",
            align: 'left',
            editor: {
                allowBlank: false,
                xtype: 'numberfield',
                minValue:0
            }
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
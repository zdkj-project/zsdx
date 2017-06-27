Ext.define("core.train.class.view.SelectStudentGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.class.studentgrid",
    al:true,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTrainee/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainTrainee", //对应的数据模型
    
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '导入学员信息',
            ref: 'gridTraineeImport',
            funCode:'girdFuntionBtn',
            disabled:false,
            iconCls: 'x-fa fa-clipboard'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'xm',
            funCode:'girdFastSearchText', 
            emptyText: '请输入姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search',  
        }],
    },
    /**
     * 高级查询面板
     */
    panelButtomBar: null,

    //title: "<font color=white>可选人员(选中后向右拖动)</font>",
    viewConfig: {
        stripeRows: false,
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
    
    /** 排序字段定义 */
    defSort: [{
        property: "createTime", //字段名
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 70,
            text: '序号',
            align: 'center'
        }, {
            flex: 2,
            text: "姓名",
            dataIndex: "xm"
        }, {
            flex: 1,
            text: "性别",
            dataIndex: "xbm",
            columnType: "basecombobox", //列类型
            ddCode: "XBM" //字典代码  
        }, {
            flex: 2,
            text: "移动电话",
            dataIndex: "mobilePhone" 
        },{
            flex: 2,
            text: "身份证件号",
            dataIndex: "sfzjh",
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
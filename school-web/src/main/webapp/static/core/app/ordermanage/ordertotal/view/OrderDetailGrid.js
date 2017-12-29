Ext.define("core.ordermanage.ordertotal.view.OrderDetailGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.ordertotal.orderdetailgrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainTeacherOrder/getOrderUserList", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainTeacherOrder", //对应的数据模型
    sortableColumns:false,
    selModel: {
        type: "checkboxmodel",   
        headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        mode:'single',  //multi,simple,single；默认为多选multi
        //checkOnly:false,    //如果值为true，则只用点击checkbox列才能选中此条记录
        //allowDeselect:true, //如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    },
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [ {
            xtype: 'button',
            text: '导出订餐人员报表',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            iconCls: 'x-fa fa-file'
        }],
    },

    //bottomInfoPanelRef:'totalInfo',
    /** 排序字段定义 */
    defSort: [{
       property: "createTime", //字段名
       direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //filter:'[{"type":"string","comparison":"=","value":"null","field":"expenseserialId"}]'
    },
    columns: {
        defaults: {
            //align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        },{
            flex:1,
            minWidth: 200,
            text: "姓名",
            dataIndex: "xm",
        }, {
            flex:1,
            minWidth: 200,
            text: "预定套餐",
            dataIndex: "dinnerGroup",
            renderer: function(value, metaData) {
                /*
                if(value==1)
                    return "<span style='color:green'>A套餐</span>";
                else if(value==2)
                    return "<span style='color:blue'>B套餐</span>";
                else
                    return "其他";
                */
                 return "<span style='color:green'>已订餐</span>";
            }
        }, {
            flex:1,
            minWidth: 200,
            text: "备注",
            dataIndex: "remark",
            renderer: function (value, metaData) {
                var title = " 备注 ";
                metaData.tdAttr = 'data-qtitle="' + title +
                    '" data-qtip="' + value + '"';
                return value;
            }
        },{
            flex:1,
            minWidth: 200,
            text: "订餐时间",
            dataIndex: "createTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')           
                return ss;
            }
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
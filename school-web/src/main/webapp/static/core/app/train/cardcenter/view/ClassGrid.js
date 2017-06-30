Ext.define('core.train.cardcenter.view.ClassGrid', {
    extend: "core.base.view.BaseGrid",
    alias: "widget.cardcenter.classgrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClass/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClass", //对应的数据模型
    al: false,
    
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
    panelButtomBar: false,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [/*{
            xtype: 'button',
            text: '启动评价',
            ref: 'gridAdd_Tab',
            funCode: 'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        }, {
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode: 'girdFuntionBtn',
            disabled: false,
            iconCls: 'x-fa fa-file'
        },*/ {
            xtype: 'tbtext',
            html: '班级列表',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        }, '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'className',
            funCode: 'girdFastSearchText',
            emptyText: '请输入班级名称'
        }, {
            xtype: 'button',
            funCode: 'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search',
        }/*,{
            xtype: 'button',
            text:'更新信息',
            ref: 'gridRefreshInfo',
            iconCls: 'x-fa fa-refresh',
        }*/]
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
        //zzk:2017-6-19 若写在这，必须按标准格式编写json字符串（即属性和值使用双引号扩囊）
        filter: '[{"type":"numeric","comparison":"!=","value":0,"field":"isuse"}]' 
    },
    columns: {
        defaults: {
            //align:'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            flex:1,
            text: "班级名称",
            dataIndex: "className"
        },{
            width:100,
            text: "开始日期",
            dataIndex: "beginDate",
            renderer: function (value, metaData) {
                var date = value.replace(new RegExp(/-/gm),"/");
                var ss = Ext.Date.format(new Date(date), 'Y-m-d');
                return ss;
            }
        }, {
            width:100,
            text: "结束日期",
            dataIndex: "endDate",
            renderer: function (value, metaData) {
                var date = value.replace(new RegExp(/-/gm),"/");
                var ss = Ext.Date.format(new Date(date), 'Y-m-d');
                return ss;
            }
        }]
    }
});
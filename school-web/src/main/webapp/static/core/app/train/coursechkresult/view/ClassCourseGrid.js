Ext.define("core.train.coursechkresult.view.ClassCourseGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.coursechkresult.classcoursegrid",
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClassschedule/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClassschedule", //对应的数据模型
    al:true,
    selModel: {
        type: "checkboxmodel",
        headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        mode:'single'  //multi,simple,single；默认为多选multi
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
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '班级课程',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        }, '->', {
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'courseName',
            funCode:'girdFastSearchText', 
            isNotForm:true,   //由于文本框重写了baseform下面的funcode值，所以使用这个属性，防止重写这里设定的fundcode值。
            emptyText: '请输入课程名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search'
        }]
    },
    /** 排序字段定义 */
    defSort: [{
        property: "beginTime", //字段名
        direction: "DESC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        filter: "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"null\",\"field\":\"classId\"}]" //默认是查不出数据的    
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
        }, {
            flex: 1,
            minWidth:100,
            text: "课程名称",
            dataIndex: "courseName"
        },{
            width:100,
            text: "上课日期",
            dataIndex: "beginTime",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");
                var title = "上课日期";
                var ss = Ext.Date.format(new Date(date), 'Y-m-d');
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        }, {
            width:150,
            text: "上课时间",
            dataIndex: "endTime",            
            renderer: function(value, metaData,record) {
                var beginDate = record.get("beginTime").replace(new RegExp(/-/gm), "/");
                var endDate = value.replace(new RegExp(/-/gm), "/");
                var title = "上课时间";
                var ss = Ext.Date.format(new Date(beginDate), 'H:i') + "----"  + Ext.Date.format(new Date(endDate), 'H:i');
                var html = ss;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return ss;
            }
        },{
            width:100,
            text: "授课地点",
            dataIndex: "scheduleAddress",
            renderer: function(value, metaData) {
                var title = "授课地点";            
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
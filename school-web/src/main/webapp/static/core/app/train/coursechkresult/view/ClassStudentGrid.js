/*
* 班级学员表
*/
Ext.define("core.train.coursechkresult.view.ClassStudentGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.coursechkresult.classstudentgrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClasstrainee/getCheckList", //数据获取地址
    model: "com.zd.school.jw.train.model.vo.VoTrainClassCheck", //对应的数据模型
    selModel: {
        type: "checkboxmodel",
        headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        mode:'single'  //multi,simple,single；默认为多选multi
        //checkOnly:false,    //如果值为true，则只用点击checkbox列才能选中此条记录
        //allowDeselect:true, //如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    },
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '班级学员',
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
            name:'xm',
            funCode:'girdFastSearchText', 
            isNotForm:true,   //由于文本框重写了baseform下面的funcode值，所以使用这个属性，防止重写这里设定的fundcode值。
            emptyText: '请输入姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search'
        }],
    },
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
     
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
        //filter: "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"null\",\"field\":\"classId\"}]" //默认是查不出数据的
    },  
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 80,
            text: "姓名",
            dataIndex: "xm"
        },{
            width: 100,
            text: "手机号码",
            dataIndex: "mobilePhone",
        }, {
            width: 100,
            text: "所在单位",
            dataIndex: "workUnit",
            renderer: function (value, metaData) {
                var title = "所在单位";
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        }, {
            text: "签到时间",
            dataIndex: "incardTime",
            width: 100,
            renderer: function (value, metaData) {
                var title = "签到时间";
                if (Ext.isEmpty(value))
                    return value;
                else {
                    var date = value.replace(new RegExp(/-/gm), "/");
                    var ss = Ext.Date.format(new Date(date), 'H:i:s');
                    var html = ss;
                    metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                    return ss;
                }
            }
        }, {
            text: "签退时间",
            dataIndex: "outcardTime",
            width: 100,
            renderer: function (value, metaData) {
                var title = "签退时间";
                if (Ext.isEmpty(value))
                    return value;
                else {
                    var date = value.replace(new RegExp(/-/gm), "/");
                    var ss = Ext.Date.format(new Date(date), 'H:i:s');
                    var html = ss;
                    metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                    return ss;
                }
            }
        }, {
            text: "上课时长",
            dataIndex: "attendMinute",
            width: 100,
            renderer: function (value, metaData) {
                return value+" 分钟";
            }
        }, {
            text: "考勤结果",
            dataIndex: "attendResult",
            width: 80,
            renderer: function(value, metaData) {
                //1-正常 2-迟到 3-早退 4-缺勤5-迟到早退
                var html = value;
                switch (value){
                    case "1":
                        html = "<span style='color:green'>正常</span>";
                        break;
                    case "2":
                        html = "<span style='color:#ff9b00'>迟到</span>";
                        break;
                    case"3":
                        html =  "<span style='color:#0070ff'>早退</span>";
                        break;
                    case "4":
                        html =  "<span style='color:red'>缺勤</span>";
                        break;
                    case "5":
                        html =  "<span style='color:#ff008b'>迟到早退</span>";
                        break;
                    default:
                        html="未考勤";
                        break;

                }
                //metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
            }
        },{
            text:"是否请假",
            dataIndex:"isLeave",
            width:80,
            renderer: function(value, metaData) {
                if(value=="1"){
                    return "<span style='color:red'>是</span>"
                }else{
                    return "<span style='color:green'>否</span>"
                }
            }
        },{
            text:"备注与说明",
            dataIndex:"remark",
            flex:1,
            minWidth:100,
            renderer: function (value, metaData) {
                var title = "备注与说明";
               
                var html = value;
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
                return html;
                
            }
        },{
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 100,
            align:'center',
            fixed: true,
            items: [{
                text: ' 请假 ',
                style: 'font-size:13px;',
                tooltip: '请假',
                ref: 'gridDetail',        
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridSetLeave', {
                        view: view.grid,
                        record: rec,
                        value:1
                    });
                },
                getClass :function(v,metadata,record){

                    if(record.get("isLeave")==1){
                        return 'x-hidden-display';
                    }

                    var roleKey = comm.get("roleKey");
                    if(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 
                            && roleKey.indexOf("ZONGWUROLE") == -1)
                        return 'x-hidden-display';
                    else
                        return null;                
                }, 
            },{
                text: ' 取消请假 ',
                style: 'font-size:13px;',
                tooltip: '取消请假',
                ref: 'gridDetail',        
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridSetLeave', {
                        view: view.grid,
                        record: rec,
                        value:0
                    });
                },
                getClass :function(v,metadata,record){
                
                    if(record.get("isLeave")!=1){
                        return 'x-hidden-display';
                    }

                    var roleKey = comm.get("roleKey");
                    if(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 
                            && roleKey.indexOf("ZONGWUROLE") == -1)
                        return 'x-hidden-display';
                    else
                        return null;
                }, 
            },{
                text:' 备注 ',  
                style:'font-size:13px;',  
                tooltip: '备注',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridRemark', {
                        view: view.grid,
                        record: rec
                    });
                },
                getClass :function(v,metadata,record){                        

                    var roleKey = comm.get("roleKey");
                    if(roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 
                            && roleKey.indexOf("ZONGWUROLE") == -1)
                        return 'x-hidden-display';
                    else
                        return null;
                }, 
            }]
        }]
    },
    
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
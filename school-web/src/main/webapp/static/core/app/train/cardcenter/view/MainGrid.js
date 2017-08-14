/*
* 班级学员表
*/
Ext.define("core.train.cardcenter.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.cardcenter.maingrid",
    al:true,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/TrainClasstrainee/list", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainClasstrainee", //对应的数据模型
    
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [ {
            xtype: 'tbtext',
            html: '班级学员列表',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        },'->',{
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
            iconCls: 'x-fa fa-search',  
        },{
            xtype: 'button',
            text:'更新发卡信息',
            ref: 'gridRefreshInfo',
            iconCls: 'x-fa fa-refresh',
        },{
            xtype: 'button',
            text:'下载学员发卡模版',
            ref: 'gridExportExcel',
            iconCls: 'x-fa fa-file',
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
        filter: "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"null\",\"field\":\"classId\"}]" //默认是查不出数据的
    },
    columns: [{
        xtype: "rownumberer",
        flex: 0,
        width: 50,
        text: '序号',
        align: 'center'
    }, {
        flex:1,
        minWidth: 100,
        text: "姓名",
        dataIndex: "xm"
    }, {
        width: 80,
        text: "性别",
        dataIndex: "xbm",
        columnType: "basecombobox", //列类型
        ddCode: "XBM" //字典代码  
    }, {
        width: 120,
        text: "移动电话",
        dataIndex: "mobilePhone" 
    },{
        width:80,
        text: "学员状态",
        dataIndex: "isDelete",
        renderer: function(value, metaData) {
            if(value==0)
                return "<span style='color:green'>正常</span>";
            else if(value==1)
                return "<span style='color:red'>取消</span>";
            else if(value==2)
                return "<span style='color:#FFAC00'>新增</span>";            
        }
    },{
        width:100,
        text: "卡片编号",
        dataIndex: "cardNo"        
    },{
        width:100,
        text: "发卡状态",
        dataIndex: "useState",
        renderer: function(value, metaData) {          
            if(value==0)
                return "<span style='color:red'>未发卡</span>";
            else if(value==1)
                return "<span style='color:green'>已发卡</span>";            
            else 
                return "<span style='color:#FFAC00'>卡片失效</span>";            
        }        
    }],
    
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
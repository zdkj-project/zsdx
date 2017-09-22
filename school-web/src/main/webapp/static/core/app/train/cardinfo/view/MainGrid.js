/*
* 班级学员表
*/
Ext.define("core.train.cardinfo.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.cardinfo.maingrid",
    al:true,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/CardUserInfo/list", //数据获取地址
    model: "com.zd.school.plartform.system.model.CardUserInfo", //对应的数据模型
    
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [ '->', {
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'cardPrintId',
            funCode: 'girdFastSearchText',
            emptyText: '请输印刷卡号'
        }, {
            xtype: 'button',
            funCode: 'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search',
        }]
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
    extParams: {},
    columns: [{
        xtype: "rownumberer",
        flex: 0,
        width: 50,
        text: '序号',
        align: 'center'
    },{
    	flex:0.5,
        width:100,
        text: "印刷卡号",
        dataIndex: "cardPrintId",
    }, {
    	flex:0.5,
        text: "卡的使用状态",
        dataIndex: "useState",
        columnType: "basecombobox", //列类型
        ddCode: "USESTATE" //字典代码  
    }],
    
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
Ext.define("core.system.operatelog.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.operatelog.maingrid",
    dataUrl: comm.get('baseUrl') + "/OperateLog/list",
/*    tbar: [
        { xtype: 'button', text: '添加', ref: 'gridAdd', iconCls: 'x-fa fa-plus-circle' },
        { xtype: 'button', text: '编辑', ref: 'gridEdit', iconCls: 'x-fa fa-pencil-square', disabled: true },
        { xtype: 'button', text: '删除', ref: 'gridDelete', iconCls: 'x-fa fa-minus-circle' }
    ],
    panelTopBar:false,
    panelButtomBar:false,
    */
    tbar:[],
    panelTopBar:{ 
        xtype:'toolbar',
        items: ['->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'ipHost',
            emptyText: '请输入IP地址',
            funCode: 'girdFastSearchText'
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型 
            iconCls: 'x-fa fa-search',  
        }],
    },
    panelBottomBar:false,
    
    
    //排序字段及模式定义
    defSort: [{
        property: 'operateDate',
        direction: 'DESC'
    }],
    extParams: {},
    model: 'com.zd.school.plartform.system.model.SysOperateLog',
    
    columns:  {        
        defaults:{        
            //align:'center',
            titleAlign:"center"
        },
        items: [{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        }, {
            text: "IP地址",
            dataIndex: "ipHost",
            width:140
        }, {
            text: "访问方法",
            dataIndex: "methodName",
            flex:1,
            renderer: function(value, metaData) {
                var title = "访问方法";            
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        }, {
            text: "参数",
            dataIndex: "methodParams",
            flex:1,
            renderer: function(value, metaData) {
                var title = "参数";      
                var v=value.replace( new RegExp('"',"gm"),''); 
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + v + '"';
                return v;
            }
        }, {
            text: "返回结果",
            dataIndex: "methodResult",
            flex:1,
            renderer: function(value, metaData) {
                var title = "返回结果";  
                //var v=Ext.decode(value);        
                var v=value.replace( new RegExp('"',"gm"),'');  
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + v + '"';
                return value;
            }
        }, {
            text: "操作时间",
            dataIndex: "operateDate",        
            width:140,
            renderer: function(value, metaData) {
                if(value){
                    var date = value.replace(new RegExp(/-/gm), "/");    
                    var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i');    
                    return ss;
                } 
                return value;            
            }
        }]
    },   
});
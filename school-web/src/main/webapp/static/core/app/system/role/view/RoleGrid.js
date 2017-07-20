Ext.define("core.system.role.view.RoleGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.role.rolegrid",
    dataUrl: comm.get('baseUrl') + "/sysrole/list",
    model: 'com.zd.school.plartform.system.model.SysRole',

    tbar:[],
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd_Tab',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit_Tab',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        },{
            xtype: 'button',
            text: '角色用户',
            ref: 'gridDetail_Tab',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-user'
        },{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            disabled:true,
            iconCls: 'x-fa fa-minus-circle'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'roleName',
            funCode:'girdFastSearchText', 
            emptyText: '请输入角色名称'
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
        property: 'orderIndex',
        direction: 'DESC'
    }],
    extParams: {
        whereSql: ""
    },
  
    columns:  {        
        defaults:{
            //flex:1,       //【若使用了 selType: "checkboxmodel"；则不要在这设定此属性了，否则多选框的宽度也会变大 】
            //align:'center',
            titleAlign:"center"
        },
        items:[{
            xtype: "rownumberer",
            flex:0,
            width: 60,
            text: '序号',
            align: 'center',
            resizable :false
            
        },{
            text: "主键",
            dataIndex: "uuid",
            hidden: true    
        }, {
            text: "角色名称",
            dataIndex: "roleName",
            flex:2,
        }, {
            text: "角色编码",
            dataIndex: "roleCode",
            flex:1,
        }, {
            text: "是否系统角色",
            dataIndex: "issystem",
            renderer: function(value) {
                return value=="1"?"<font color=red>是</font>":"<font color=green>否</font>"
            }
        }, {
            text: "角色说明",
            dataIndex: "remark",
            flex:2,
            renderer: function(value,metaData) {  
                var title=" 角色说明 ";

                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';  
                return value;  
            }  
        },{
            xtype:'actiontextcolumn',
            ref: "roledetail",
            text: "操作",
            width:130,
            resizable :false,
            items: [{
                text:'编辑',  
                style:'font-size:12px;',                    
                tooltip: '编辑',      
                ref: 'gridEdit',
                handler: function(view, rowIndex, colIndex, item) {                 
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('editClick_Tab', {
                        view:view.grid,
                        record: rec
                    });                    
                },
                               
            },{               
                text:'角色用户',
                style:'font-size:12px;',
                tooltip: '角色用户',
                ref: 'gridDetail',
                //hidden:true,            
                handler: function(view, rowIndex, colIndex, item) {             
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('detailClick_Tab', {
                        view:view.grid,
                        record: rec
                    });
                }
            },{              
                text:'删除',
                style:'font-size:12px;',
                tooltip: '删除',
                ref: 'gridDelete',
                getClass :function(v,metadata,record,rowIndex,colIndex,store){
                    if(record.get("issystem")==1)
                        return 'x-hidden-display';
                    else
                        return null;
                },  
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view:view.grid,
                        record: rec
                    });
                }
            }]
        }]
    }
});
Ext.define("core.system.roleright.view.SelectMenuGrid", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.roleright.selectmenugrid",
    title: "",
    dataUrl: comm.get('baseUrl') + "/BaseMenu/userpertorole",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysMenuChkTree", "checked").modelName,
    al: true,
    
    selModel: {
        //selType: "checkboxmodel"
    },
    extParams: {
        whereSql: " and isDelete='0' ",
        orderSql: " order by parentNode,orderIndex asc"
    },

    tbar: [{
        xtype: 'button',
        text: '展开',
        ref: 'gridExpand',
        iconCls: 'x-fa fa-minus-square-o'
    }, {
        xtype: 'button',
        text: '折叠',
        ref: 'gridCollapse',
        iconCls:'x-fa fa-plus-square-o'
    }],

    /*this.control中声明事件无效，原因已找到，layout要声明controller*/
    /*
    listeners:{
        checkchange:'selectMenuCheckChange' 
    },*/
    columns: {
        defaults:{
            flex:1,
            align:'center',
            titleAlign:"center"
        },
        items:[{
            header: '菜单名称',
            dataIndex: 'text',
            xtype: 'treecolumn',
            flex: 3,
            align:'left',
            titleAlign:"left"
        }, {
            header: '排序号',
            dataIndex: 'orderIndex',
             titleAlign:"center",
            flex: 1
        }, {
            header: '菜单类型',
            dataIndex: 'menuType',
            columnType: "basecombobox", //列类型
            ddCode: "MENUTYPE", //字典代码
            flex: 1

        }, {
            header: '菜单编码',
            dataIndex: 'menuCode',
            flex: 2
        }
        /*, {
                header: '图标',
                dataIndex: 'smallIcon',
                renderer: function(value) {
                    return "<img src=\"" + value + "\" width=16 hight=16/>'";
                },
                width: 30
            }*/
        ]
    }
});
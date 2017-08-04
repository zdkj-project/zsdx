Ext.define("core.train.coursetype.view.CourseCategoryTree", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.coursetype.coursecategorytree",
    dataUrl: comm.get('baseUrl') + "/TrainCoursecategory/treelist",
    model: factory.ModelFactory.getModelByName("com.zd.school.jw.train.model.TrainCoursecategoryTree", "checked").modelName,
    al: true,
    extParams: {
        whereSql: " and isDelete='0' ",
        orderSql: " order by parentNode,orderIndex asc"
    },
    tbar: [{
        xtype: 'button',
        text: '添加',
        ref: 'gridAdd_Tab',
        iconCls: 'x-fa fa-plus-square',
        disabled: false
    }/*, {
     xtype: 'button',
     text: '添加同级',
     ref: 'gridAddBrother',
     iconCls: 'x-fa fa-plus-square-o',
     disabled: false
     }*/, {
        xtype: 'button',
        text: '修改',
        ref: 'gridEdit',
        iconCls: 'x-fa fa-pencil-square',
        disabled: false
    }, {
        xtype: 'button',
        text: '删除',
        ref: 'gridDelete',
        iconCls: 'x-fa fa-minus-circle',
        disabled: false
    }, {
        xtype: 'button',
        text: '刷新',
        ref: 'gridRefresh',
        iconCls: 'x-fa fa-refresh'
    },{
        xtype: 'button',
        text: '导出',
        ref: 'gridExport',
        funCode: 'girdFuntionBtn',
        disabled: false,
        iconCls: 'x-fa fa-file'
    },],
    columns: {
        defaults: {
            //flex:1,     //不设定此属性了，否则多选框的宽度也会变大
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            header: '分类名称',
            dataIndex: 'text',
            xtype: 'treecolumn',
            width: 300,
            align: 'left'
        }, {
            header: '分类编码',
            dataIndex: 'nodeCode',
            width: 150,
            align: 'left'
        }, {
            header: '分类说明',
            dataIndex: 'categoryDesc',
            flex: 1,
            align: 'left',
            renderer: function (value, metaData) {
                var title = " 课程分类说明 ";
                metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';
                return value;
            }
        }, {
            xtype: 'actiontextcolumn',
            text: "操作",
            width: 150,
            resizable: false,
            items: [{
                text: '编辑',
                style: 'font-size:12px;',
                tooltip: '编辑',
                ref: 'gridDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('mainGridEditClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: "edit"
                    });
                }
            },{
                text: '排序',
                style: 'font-size:12px;',
                tooltip: '排序',
                ref: 'gridDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('mainGridOrderClick_Tab', {
                        view: view.grid,
                        record: rec,
                        cmd: "order"
                    });
                }
            },{
                text: '删除',
                style: 'font-size:12px;',
                tooltip: '删除',
                ref: 'gridDetail',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('deleteClick', {
                        view: view.grid,
                        record: rec,
                        cmd: "delete"
                    });
                }
            }]
        }]
    }
    /*    listeners: {
     itemclick: function(tree, record, item, index, e) {
     var mainLayout = tree.up("panel[xtype=coursetype.mainlayout]");
     var filter = "[{'type':'string','comparison':'=','value':'" + record.get("id") + "','field':'categoryId'}]"

     var funData = mainLayout.funData;
     mainLayout.funData = Ext.apply(funData, {
     filter: filter
     });
     record.expand();
     }
     }*/
})
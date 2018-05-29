Ext.define("core.base.view.BaseTreeGrid", {
    extend: "Ext.tree.Panel",
    alias: "widget.basetreegrid",
    forceFit: false,    //zzk：不推荐设置为true，一直刷新表格界面column宽度会一直变化，动态隐藏列出现bug
    columnLines: true,
    loadMask: true,
    useArrows: false,
    rootVisible: false,
    multiSelect: true,
    viewConfig: {
        stripeRows: false
    },
    selModel: {
        selType: "checkboxmodel",
        headerWidth: 50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
    },
    dataUrl: "",
    columns: false,
    enableKeyNav: true, //可以使用键盘控制上下
    al: true,
    emptyText: '<span style="display: block;text-align:center">没有需要显示的数据！</span>',
    initComponent: function () {
        this.store = Ext.create('Ext.data.TreeStore', {
            defaultRootId: "ROOT",
            autoLoad: this.al,
            model: factory.ModelFactory.getModelByName(this.model, "checked").modelName,
            sorters: this.defSort,
            groupers: this.defGroup,
            proxy: {
                type: 'ajax',
                url: this.dataUrl,
                extraParams: this.extParams,
                reader: {
                    type: 'json',
                    rootProperty: ''
                },
                writer: {
                    type: 'json'
                }
            }
        });


        var columns = new Array();

        if (this.columns) {
            var columnIitems = this.columns.items;  //取items里的数据，现在多了个配置项defaults
            if (!columnIitems)
                columnIitems = this.columns;          //若没有，则取columns

            Ext.each(columnIitems, function (col) {
                //字典项转换
                if (col.columnType == "basecombobox" || (col.field && col.field.xtype && col.field.xtype == "basecombobox")) {
                    col.renderer = function (value, data, record) {
                        var val = value;
                        var ddCode = col.ddCode;
                        var ddItem = factory.DDCache.getItemByDDCode(ddCode);
                        //console.log(ddItem)
                        for (var i = 0; i < ddItem.length; i++) {
                            var ddObj = ddItem[i];
                            var displayField = 'itemName';
                            var valueField = 'itemCode';
                            if (col.field && col.field.displayField) {
                                displayField = column.field.displayField;
                            } else if (col.displayField) {
                                displayField = col.displayField;
                            }
                            if (col.field && col.field.valueField) {
                                displayField = col.field.valueField;
                            } else if (col.displayField) {
                                displayField = col.displayField;
                            }
                            if (value == ddObj[valueField]) {
                                val = ddObj[displayField];
                                break;
                            }
                        }
                        return val;
                    }
                }
                columns.push(col);
            });
        }
        this.callParent(arguments);
    }
});
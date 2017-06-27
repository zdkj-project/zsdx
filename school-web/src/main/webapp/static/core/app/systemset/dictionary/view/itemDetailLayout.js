Ext.define("core.systemset.dictionary.view.ItemDetailLayout", {
    extend: "core.base.view.BasePanel",
    alias: 'widget.dic.itemdetaillayout',
    funCode: "dicItem_detail",
    funData: {
        action: comm.get('baseUrl') + "/BaseDic", //请求Action
        whereSql: "", //表格查询条件
        orderSql: " order by orderIndex", //表格排序条件
        pkName: "jcId",
        modelName: "com.zd.school.plartform.baseset.model.BaseDicTree", //实体全路径
        tableName: "" //表名       
    },
    items: [{
        xtype: "dic.itemform"
    }]
})
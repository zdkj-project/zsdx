Ext.define("core.system.role.view.SelectUserGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.role.selectusergrid",
    al:false,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/sysuser/getUserNotInRoleId", //数据获取地址
    model: "com.zd.school.plartform.system.model.SysUser",
    /**
     * 工具栏操作按E钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'xm',
            funCode:'girdFastSearchText',
            emptyText: '请输入姓名'
        },{
            xtype:'textfield',
            name:'deptId',
            hidden:true,
            funCode:"girdFastSearchText"
        },{
            width:200,
            emptyText: '请选择部门',
            xtype: "basetreefield",
            ddCode: "DEPTTREE",
            name:"deptName",
            rootId: "ROOT",
            funCode:'girdFastSearchText',
            configInfo: {
                //controller: 'role.OtherController',
                multiSelect: false,
                fieldInfo: "deptName~deptId,text~id",
                whereSql: " and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex asc",
                url:comm.get('baseUrl') + "/BaseOrg/chktreelist"
            }
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',
            iconCls: 'x-fa fa-search'
        }]
    },
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            ddGroup: "DrapDropGroup"            //与下面的2行代码一样的效果
        },
        listeners: {
            drop: function(node, data, dropRec, dropPosition) {
            },
            beforeitemdblclick: function(grid, record, item, index, e, eOpts) {
                selectStore = grid.getStore();
                selectStore.removeAt(index);

                var basePanel = grid.up("panel[xtype=role.selectuserlayout]");
                var isSelectGrid;
                if(basePanel){
                    isSelectGrid = basePanel.down("panel[xtype=role.isselectusergrid]");
                    var isSelectStore = isSelectGrid.getStore();
                    isSelectStore.insert(0, [record]);
                }
                
                return false;
            }
        }
    },

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
        //filter: "[{'type':'string','comparison':'=','value':'','field':'claiId'}]"
    },
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex:0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width:120,
            text: "用户名",
            dataIndex: "userName"
        },{
            width:70,
            text: "姓名",
            dataIndex: "xm"
        }, {
            width:50,
            text: "性别",
            dataIndex: "xbm",
            columnType: "basecombobox",
            ddCode: "XBM"
        }, {
            width:150,
            text: "部门",
            dataIndex: "deptName"
        }, {
            width:110,
            text: "岗位",
            dataIndex: "jobName"
        }, {
            text: "编制",
            dataIndex: "zxxbzlb",
            ddCode: "ZXXBZLB",
            columnType: "basecombobox",
            minWidth:100,
            flex:1,
            align:'left'
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
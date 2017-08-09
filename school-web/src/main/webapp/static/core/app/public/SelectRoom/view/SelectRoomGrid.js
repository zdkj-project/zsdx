Ext.define("core.public.SelectRoom.view.SelectRoomGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.pubselect.selectroomgrid",
    al: true,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/BuildRoominfo/list", //数据获取地址
    model: "com.zd.school.build.define.model.BuildRoominfo",
    /**
     * 工具栏操作按E钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar: {
        xtype: 'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '快速搜索：'
        }, {
            xtype: 'textfield',
            name: 'roomName',
            funCode: 'girdFastSearchText',
            emptyText: '请输入房间名称'
        }/*, {
            xtype: 'textfield',
            name: 'roomName',
            hidden: true,
            funCode: "girdFastSearchText"
        }, {
            width: 200,
            emptyText: '请选择区域',
            xtype: "basetreefield",
            ddCode: "DEPTTREE",
            name: "deptName",
            rootId: "ROOT",
            funCode: 'girdFastSearchText',
            configInfo: {
                multiSelect: false,
                fieldInfo: "deptName~deptId,text~id",
                whereSql: " and isDelete='0' ",
                orderSql: " order by parentNode,orderIndex asc",
                url: comm.get('baseUrl') + "/BaseOrg/chktreelist"
            }
        }*/, {
            xtype: 'button',
            funCode: 'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
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
            drop: function (node, data, dropRec, dropPosition) {
            },
            beforeitemdblclick: function (grid, record, item, index, e, eOpts) {
                selectStore = grid.getStore();
                selectStore.removeAt(index);

                var basePanel = grid.up("panel[xtype=pubselect.selectroomlayout]");
                var isSelectGrid;
                if (basePanel) {
                    isSelectGrid = basePanel.down("panel[xtype=pubselect.isselectroomgrid]");
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
        whereSql: ""
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
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 100,
            text: "所属楼栋",
            dataIndex: "areaUpName"
        }, {
            width: 100,
            text: "所属楼层",
            dataIndex: "areaName"
        }, {
            flex: 100,
            text: "房间名称",
            dataIndex: "roomName"
        }, {
            width: 100,
            text: "房间类型",
            dataIndex: "roomType",
            columnType: "basecombobox",
            ddCode: "FJLX"
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
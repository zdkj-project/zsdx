Ext.define("core.public.selectGateway.view.SelectGatewayGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.public.selectGateway.selectgatewaygrid",
    al:true,
    frame: false,
    columnLines: false,
    dataUrl: comm.get("baseUrl") + "/PtGateway/list", //数据获取地址
    model: "com.zd.school.control.device.model.PtGateway",
    selModel: {
        type: "checkboxmodel",   
        headerWidth:30,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        //mode:'single',  //multi,simple,single；默认为多选multi
        checkOnly:true,    //如果值为true，则只用点击checkbox列才能选中此条记录
        //allowDeselect:true, //如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    },
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
            name:'gatewayName',
            funCode:'girdFastSearchText',
            emptyText: '请输入网关名称'
        },{
            xtype:'textfield',
            name:'frontserverId',
            hidden:true,
            funCode:"girdFastSearchText"
        },{
            width:200,
            emptyText: '请选择服务器',
            xtype: "basetreefield",
            //ddCode: "DEPTTREE",
            name:"frontServerName",
            rootId: "ROOT",
            funCode:'girdFastSearchText',
            configInfo: {
                multiSelect: false,
                fieldInfo: "frontServerName~frontserverId,text~id",
                //whereSql: " and isDelete='0' ",
                //orderSql: " order by parentNode,orderIndex asc",
                url:comm.get('baseUrl') + "/PtGateway/treelist"
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

                var basePanel = grid.up("panel[xtype=public.selectGateway.selectgatewaylayout]");
                var isSelectGrid;
                var data = record.data;
                if(basePanel){
                    isSelectGrid = basePanel.down("panel[xtype=public.selectGateway.isselectgatewaygrid]");
                    
                    if(isSelectGrid.isVisible()==true){
                        var isSelectStore = isSelectGrid.getStore();
                        for (var i = 0; i < isSelectStore.getCount(); i++) {
                            if (data.uuid == isSelectStore.getAt(i).get('uuid')) {
                                Ext.Msg.alert("提示", data.gatewayName+"已存在!");
                                return ;
                            }
                        };
                        isSelectStore.insert(0, [record]);

                        var selectStore = grid.getStore();
                        selectStore.removeAt(index);
                    }
                }
                
                return false;
            }
        }
    },

    /** 排序字段定义 */
    defSort: [{
        property: "gatewayNo", //字段名
        direction: "ASC" //升降序
    }],
    /** 扩展参数 */
    extParams: {
        // whereSql: ""
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
            text: "机号",
            dataIndex: "gatewayNo",            
            width: 100,
        }, {
            text: "网关名称",
            dataIndex: "gatewayName",            
            minWidth: 100,
            flex:1
        }, {
            width: 100,
            text: "前置名称",
            dataIndex: "frontServerName",           
        }, {
            width: 100,
            text: "序列号",
            dataIndex: "gatewaySN",            
        },{
            width: 100,
            text: "网关IP",
            dataIndex: "gatewayIP",
        }]
    },
    emptyText: '<span style="width:100%;text-align:center;display: block;">暂无数据</span>'
});
Ext.define("core.mjmanage.roomuserauthority.view.MjUserrightTree", {
    extend: "core.base.view.BaseTreeGrid",
    alias: "widget.mjmanage.roomuserauthority.mjuserrighttree",
    dataUrl: comm.get('baseUrl') + "/BaseMjUserright/treelist",
    model: "com.zd.school.plartform.comm.model.CommTree",
    expandFirst:true,
    sortableColumns:false,
    selModel:null,
    /*
    selModel: {
        mode : 'single',
        listeners: {
            selectionchange:function(model,selected,eOpts){
                var grid=model.view;
                var selectRow=model.getSelection();
                var querySql1="";
                var querySql2="";
                var uuids=new Array();
                if(selectRow.length==1){
                	querySql1 = " and ROOM_ID ="+"'"+selectRow[0].data.id+"'";
                }
                if(selectRow.length>1){
                	for (var i = 0; i < selectRow.length; i++) {
                      var temp=selectRow[i].data;
                      uuids.push("'"+temp.id+"'");
                  }
                querySql1 = " and ROOM_ID in (" + uuids.join(",") + ")";
                }
                var mainlayout = grid.up('panel[xtype=mjmanage.roomuserauthority.mainlayout]');
                var baseGrid = mainlayout.down('basegrid[xtype=mjmanage.roomuserauthority.maingrid]');
                //获取快速搜索框的值
                var girdSearchTexts = baseGrid.query("field[funCode=girdFastSearchText]");
                var filter=new Array();
                if(girdSearchTexts[0].getValue()){
                    querySql2+=" and XM like "+"'%"+girdSearchTexts[0].getValue()+"%'";
                    //filter.push({"type": "string", "value": girdSearchTexts[0].getValue(), "field": "XM", "comparison": ""});
                }
                if(girdSearchTexts[1].getValue()){
                    querySql2+=" and ROOM_NAME like "+"'%"+girdSearchTexts[1].getValue()+"%'";
                   // filter.push({"type": "string", "value": girdSearchTexts[1].getValue(), "field": "ROOM_NAME", "comparison": ""});
                }

                var stores = baseGrid.getStore();
                var proxy = stores.getProxy();
                proxy.extraParams={
                    querySql:querySql1,
                    querySql2:querySql2
                };
               // proxy.extraParams.querySql = querySql1;
                stores.load(); //刷新
            }
        }
    },*/
     tbar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '区域信息',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:600
            }
        }, '->',{
            xtype: 'button',
            text: '刷新',
            ref: 'gridRefresh',
            iconCls: 'x-fa fa-refresh',
            titleAlign:'right',
        }]
    },
    extParams: {
        whereSql: "",
        orderSql: "",
        excludes:"checked"
    },
    columns:{
        defaults:{
            titleAlign:"center"
        },
        items:[{
            text: "区域名称",
            dataIndex: "text",
            xtype:'treecolumn',
            flex: 1
        },/* {
            text: "顺序号",
            dataIndex: "orderIndex",
            width:60
        },*/{
            text:"主键",
            dataIndex:'id',
            hidden:true
        }]
    },
})
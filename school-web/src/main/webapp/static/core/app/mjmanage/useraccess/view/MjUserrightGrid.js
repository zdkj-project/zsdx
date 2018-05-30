Ext.define("core.mjmanage.useraccess.view.MjUserrightGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.mjmanage.useraccess.mjuserrightgrid",
    al: false,
    dataUrl: comm.get('baseUrl') + "/PtTerm/list",
    model: "com.zd.school.control.device.model.PtTerm",
    pageDisplayInfo:false,
    menuCode:"USERACCESS",
    selModel: {
        selType: "checkboxmodel",
        width: 10,
        //mode:'single',
        // listeners: {
        //     selectionchange:function(model,selected,eOpts){
        //         var grid=model.view;
        //         var selectRow=model.getSelection();
        //         var uuids=new Array();
        //         for (var i = 0; i < selectRow.length; i++) {
        //             var temp=selectRow[i].data;
        //             uuids.push(temp.uuid);
        //         }
        //         var uuid = uuids.join(",");
        //         var mainlayout = grid.up('panel[xtype=mjmanage.useraccess.mainlayout]');
        //         var baseGrid = mainlayout.down('panel[xtype=mjmanage.useraccess.maingrid]');
        //         var stores = baseGrid.getStore();
        //         var proxys = stores.getProxy();
        //         var filter = "[{'type':'string','comparison':'in','value':'"+uuid +"','field':'termId'}]";
        //         proxys.extraParams = {
        //         		filter: filter
        //         };
        //         stores.load(); //刷新
        //     }
        // }
    },
    
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            html: '房间下属设备',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        },'->',{
            xtype: 'button',
            text: '选择人员',
            ref: 'selectPersonnel',
            iconCls: 'x-fa fa-plus-circle',
            disabled:true,
        }]
    }, 
    panelButtomBar:null,
    
    extParams: {
        whereSql: ""
    },
    columns: {        
        defaults:{
            titleAlign:"center",
            align:'center'
        },
        items:[{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        }, {
            xtype: "rownumberer",
            width: 50,
            text: '序号',
            align: 'center'
        },{
            text: "设备名称",
            dataIndex: "termName",
            flex:1,
            minWidth:80,
        }, {
            text: "序列编号",
            dataIndex: "termSN",
            flex:1,
            minWidth:80,
        }, {
            text: "设备类型",
            dataIndex: "termTypeID",
            columnType: "basecombobox", //列类型
            ddCode: "PTTERMTYPE", //字典代码
            flex:1,
            width:80	
        },{
            text: "所属房间",
            dataIndex: "roomName",
            flex:1,
            minWidth:120,
        }, 
        /*{
            xtype: 'actiontextcolumn',
            text: "操作",
            align: 'center',
            width: 200,
            fixed: true,
            items: [{
                text:'选择人员',  
                style:'font-size:12px;', 
                tooltip: '选择人员',
                ref: 'selectPer',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('selectPersonnel_Win', {
                        view: view.grid,
                        record: rec
                    });
                }
            }]
        }*/
        ]
    },
    listeners: {
        beforeitemdblclick: function(grid, record, item, index, e, eOpts) {
            return false;
        },
        beforeitemmousedown: function(grid, record, item, index, e, eOpts) {
            // var model = grid.getSelectionModel();  
            // var flag=model.isSelected(index);
            // if (flag) {
            //     model.deselect(index);
            //     return false;
            // }
            return false;
        },
        beforeitemclick: function(gridview, record, item, index, e, eOpts) {          
            //var grid=grid.view;    
            var grid=gridview.grid;
            var selectRow=grid.getSelection();
            var uuids=new Array();
            for (var i = 0; i < selectRow.length; i++) {
                var temp=selectRow[i].data;
                uuids.push(temp.uuid);
            }
            var uuid = uuids.join(",");
                     

            //var uuid = record.get("uuid");

            var mainlayout = grid.up('panel[xtype=mjmanage.useraccess.mainlayout]');
            var baseGrid = mainlayout.down('panel[xtype=mjmanage.useraccess.maingrid]');
            var stores = baseGrid.getStore();
            var proxys = stores.getProxy();
            var filter = "[{'type':'string','comparison':'in','value':'"+uuid +"','field':'termId'}]";
            proxys.extraParams = {
                 filter: filter
            };
            stores.loadPage(1); //刷新
        }
    }
});
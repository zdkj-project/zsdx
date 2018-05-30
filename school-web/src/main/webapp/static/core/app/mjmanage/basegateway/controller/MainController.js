Ext.define("core.mjmanage.basegateway.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.basegateway.maincontroller',
    mixins: {
       
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
       
    },
    init: function () {
         this.control({
            
           "basegrid[xtype=mjmanage.basegateway.miangrid]": {
                afterrender: function (grid, eOpts) {
                    var btngridExportExcel = grid.down("button[ref=gridExportExcel]");
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1) {
                    	btngridExportExcel.setHidden(true);
                    }
                },
                beforeitemclick: function(grid) {
                    this.disabledFuncBtn(grid);
                },
            },
    
              //区域列表刷新按钮事件
            "basetreegrid[xtype=mjmanage.basegateway.ptgatewaytree] button[ref=gridRefresh]": {
                click: function(btn) {
                    var baseGrid = btn.up("basetreegrid");
                    var store = baseGrid.getStore();
                    store.load(); //刷新父窗体的grid
                    return false;
                }
            },

            /**
             * 操作列的操作事件
             */
            "basegrid[xtype=mjmanage.basegateway.miangrid] actioncolumn": {
              
                //弹出tab页的方式
                baseAndHighClick_Tab: function(data) {
                    this.open_Tab(data.view,data.record,"baseAndHigh");        
                },
                 //弹出tab页的方式
                netWorkClick_Tab: function(data) {
                    this.open_Tab(data.view,data.record,"netWork");
                },
            },

            "basegrid[xtype=mjmanage.basegateway.miangrid] button[ref=gridSetFront]": {
                beforeclick:function(btn){
                    this.open_Win(btn);
                }            
            },

         });
    },

    open_Tab: function(grid,record,cmd) {
        var self = this;
        //得到组件
        var baseGrid = grid;
        var recordData=record.getData();
        var uuid =recordData.uuid;
        var gatewayName =recordData.gatewayName;
        var basePanel = baseGrid.up("basepanel");
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");
        var otherController = basePanel.otherController;    //关键：打开的tab页面的视图控制器
        if (!otherController)
            otherController = '';  

        //得到配置信息
        var funCode = basePanel.funCode;          //主界面的funCode
        var detCode = "basegateway_detail";               //打开的tab也的detCode标识，可自定指定，用于查找唯一组件
        var detLayout = "mjmanage.basegateway.detaillayout";            //打开的tab页的布局视图
        
       

        //获取Tab相关数据,根据cmd的类型，来获取不同的数据
        
        var tabTitle = ""; 
        var tabItemId ="";
        var operType="";
        var xItemType="";
        switch (cmd) {

            case "baseAndHigh":
                tabTitle = gatewayName+"-设备参数"; 
                tabItemId = funCode + "_gridBaseAndHigh";    //命名规则：funCode+'_ref名称',确保不重复
                operType="add";
                detCode="baseandhighform";
                xItemType="mjmanage.basegateway.baseandhighform";
                break;
            case "netWork":
                tabTitle = gatewayName+"-网络参数";
                tabItemId=funCode+"_gridNetWork"; 
                operType="add";
                detCode="networkform";
                xItemType="mjmanage.basegateway.networkform";
                break;
         }
        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem=tabPanel.getComponent(tabItemId);
        if(!tabItem){

            //创建tabItem
            var tabItem = Ext.create({
                xtype:'container',
                title: tabTitle,
                scrollable :true, 
                itemId:tabItemId,            
                layout:'fit', 
                uuid: uuid,      //主键值
            });
            tabPanel.add(tabItem); 

            //延迟放入到tab中
            setTimeout(function(){
                var item=Ext.widget("baseformtab",{
                    operType:operType,                            
                    controller:otherController,         //指定重写事件的控制器
                    funCode:funCode,                    //指定mainLayout的funcode
                    detCode:detCode,
                    detLayout:detLayout,                   
                    tabItemId:tabItemId,                //指定tab页的itemId
                    recordData:recordData,                    //保存一些需要默认值，提供给提交事件中使用
                    baseGrid:baseGrid,                     //保存funData数据，提供给提交事件中使用
                    items:[{
                        xtype:detLayout,
                        items:[{
                            xtype:xItemType
                        }]
                    }]
                }); 
              
                tabItem.add(item);  
              
                //处理打开界面之后，显示的初始数据
                var objForm = item.down("baseform");
                var formObj = objForm.getForm();                
                if(cmd=="netWork"){       //网络参数设置                           
                    var frontServerIP  = recordData.frontServerIP;
                    var frontServerPort = recordData.frontServerPort;
                    var frontServerStatus = recordData.frontServerStatus;                
                    var gatewayIP = recordData.gatewayIP;
                    var netGatewayIp = recordData.netgatewayIp;                
                    var gatewayMask =recordData.gatewayMask;
                    var gatewayMac = recordData.gatewayMac;
                    formObj.findField("tlvs[0].valStr").setValue(gatewayIP);
                    formObj.findField("tlvs[1].valStr").setValue(netGatewayIp);
                    formObj.findField("tlvs[2].valStr").setValue(gatewayMask);
                    formObj.findField("tlvs[3].valStr").setValue(frontServerIP);
                    formObj.findField("tlvs[4].valInt").setValue(frontServerPort);
                    formObj.findField("tlvs[5].valInt").setValue(frontServerStatus);
                    formObj.findField("tlvs[6].valStr").setValue(gatewayMac);
                    // var params =  objForm.formData;
                    // params.uuid=uuid;  
                    // var resObj = self.ajax({
                    //      url: comm.get('baseUrl') + "/BaseGateway/gatewayParam_read",
                    //      params: params
                    //   }); 
                    // var valInt = null;
                    // var valStr = null;
                    // var controlVal = null;
                    // if (resObj.length > 0) {
                    //     for (var i = 0; i < resObj.length; i++) {
                    //         valInt = "tlvs[" + i + "].valInt";
                    //         valStr = "tlvs[" + i + "].valStr";
                    //         controlVal = formObj.findField(valInt);
                    //         if (controlVal != null) {
                    //             controlVal.setValue(resObj[i].valInt);
                    //         } else {
                    //             controlVal = formObj.findField(valStr);
                    //             controlVal.setValue(resObj[i].valStr);
                    //         }
                    //     };
                    // };   

                   // self.setFormValue(formObj, resObj);
                }else if(cmd=="baseAndHigh"){   //设备参数设置
                    //基础参数
                    var baseParams =  objForm.baseFormData;
                    baseParams.uuid = uuid;
                    var baseObj = self.ajax({
                        url: comm.get('baseUrl') + "/PtGateway/baseParam_read",
                        params: baseParams
                    });

                    var valInt = null;
                    var controlVal = null;
                    if (baseObj.length > 0) {
                        for (var i = 0; i < baseObj.length; i++) {
                            valInt = "tlvs[" + i + "].valInt";
                            controlVal = formObj.findField(valInt);
                            if (controlVal != null) {
                                controlVal.setValue(baseObj[i].valInt);
                            }
                        };
                    };
                   // baseAndHighObj=Ext.apply(baseAndHighObj,baseObj);

                    //高级参数
                    var highParams =  objForm.highFormData;
                    highParams.uuid = uuid;
                    var highObj = self.ajax({
                        url: comm.get('baseUrl') + "/PtGateway/highParam_read",
                        params: highParams
                    });
                    var valInt = null;
                    var controlVal = null;
                    if (highObj.length > 0) {
                        var valStr = highObj[0].valStr.split("|");
                        for (var j = 1; j <= valStr.length; j++) {
                            valInt = "time" + j + "";
                            controlVal = formObj.findField(valInt);
                            if (controlVal != null) {
                                controlVal.setValue(valStr[j - 1]);
                            }
                        };
                    };

                   
                    //baseAndHighObj=Ext.apply(baseAndHighObj,highObj);

                    //self.setFormValue(formObj, baseAndHighObj);
                }
        
          },30);
                           
        }else if(tabItem.uuid&&tabItem.uuid!=uuid){     //判断是否点击的是同一条数据
            self.msgbox("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);   
    },
    open_Win:function(btn){
        var self=this;
        var baseGrid = btn.up('basegrid');
        if (baseGrid.getSelectionModel().getSelection().length <= 0) {
            self.msgbox("请选择列表中要批量操作的数据!");
            return false;
        }
        var win = Ext.create('Ext.Window', {
            title: "批量设置前置",
            baseGrid: baseGrid,
            iconCls: 'x-fa fa-cogs',
            resizable: false,
            modal: true,
            width: 400,
            //height: 400,
            controller: 'mjmanage.basegateway.othercontroller',
            items: [{
                xtype: "mjmanage.basegateway.ptgatewaybatchform"
            }]
        }).show();
        return false;
    },

//    hideFuncBtn:function(grid){    
//        if(comm.get("isAdmin")!="1"){
//            var menuCode="BASEGATEWAY";     // 此菜单的前缀
//            var userBtn=comm.get("userBtn");
//            if(userBtn.indexOf(menuCode+"_gridSetFront")==-1){
//                var btnSetFront = grid.down("button[ref=gridSetFront]");
//                btnSetFront.setHidden(true);
//                
//             }
//         }
//    },

    disabledFuncBtn:function(grid){
        var basePanel = grid.up("basepanel");
        var basegrid = basePanel.down("basegrid[xtype=mjmanage.basegateway.miangrid]");
        var records = basegrid.getSelectionModel().getSelection();
        var btnSetFront = basegrid.down("button[ref=gridSetFront]");
        if (records.length == 0) {
            btnSetFront.setDisabled(true);
        } else if (records.length == 1) {
            btnSetFront.setDisabled(false);
        } else {
            btnSetFront.setDisabled(false);
        }
    }
});
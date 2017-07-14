Ext.define("core.system.ip.controller.ipController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.ip.ipController',
	
    
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
/*    views: [
        "core.systemset.jobinfo.view.jobinfoMainLayout",
        "core.systemset.jobinfo.view.jobinfoDetailLayout",
        "core.systemset.jobinfo.view.jobinfoGrid",
        "core.systemset.jobinfo.view.jobinfoDetailForm"
    ],*/
    init: function() {
        var self = this;
        
        this.control({
/*            "basegrid[funCode=jobinfo_main] button[ref=gridFastSearchBtn]": {
                beforeclick:function(btn){                
                    //得到组件
                    var baseGrid = btn.up("basegrid");
                        
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();


                    var jobName=baseGrid.down("textfield[name=jobName]").getValue();               
                    
                    proxy.extraParams.filter = '[{"type":"string","value":"'+jobName+'","field":"jobName","comparison":""}]';
                    store.loadPage(1);

                    return false;
                }
            },
        	*/
        	
           
            /**
             * 增加按钮事件响应
             * @type {[type]}
             */
            "basegrid[funCode=ip_main] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    //self.doDetail(btn, "add");
                    self.doDetail_Tab(btn,"add");
                    return false;
                }
            },
            /**
             * 修改按钮事件响应
             * @type {[type]}
             */

            "basegrid[funCode=ip_main] button[ref=gridEdit]": {
                beforeclick: function(btn) {
                    //self.doDetail(btn, "edit");
                    self.doDetail_Tab(btn,"react");
                    return false;
                }
            },

             "basegrid button[ref=girdreact]":{
                  beforeclick:function(btn){
                    console.log(btn);
                    return false;
                  }
             },
             
             "basegrid button[ref=griddelete]":{
                  beforeclick:function(btn){
                    console.log(btn);
                    return false;
                }
             },  
               
            




             "basegrid[xtype=ip.ipgrid] actiontextcolumn":{

                gridreactClick_Tab:function(data){
                    var baseGrid=data.view;
                    var record=data.record;
                    this.doDetail_Tab(null,"react",baseGrid,record);

                     return false;
                },

                deleteClick: function (data) {                                  
                     //得到组件                    
                    var baseGrid=data.view;
                    var record=data.record;
                    var funCode = baseGrid.funCode;
                    var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                    //得到配置信息
                    var funData = basePanel.funData;
                    var pkName = funData.pkName;
                  
                        
                    Ext.Msg.confirm('提示', '是否删除数据?', function(btn, text) {
                        if (btn == 'yes') {                        
                            //发送ajax请求
                            var resObj = self.ajax({
                                url: funData.action + "/dodelete",
                                params: {
                                    ids: record.get(pkName),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                //baseGrid.getStore().load(0);

                                baseGrid.getStore().remove(record); //不刷新的方式

                                self.msgbox(resObj.obj);

                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                    
                    console.log("重写")       
                
                    return false;
                },


            },
        });
    },
         
         
        doDetail_Tab:function(btn,cmd,grid,record){
                        var self=this;
                        var baseGrid;
                        var recordData;

            if (btn) {
                baseGrid=btn.up("basegrid");
            }else{
                baseGrid=grid;
                recordData=record.data;
            }

    
        var funCode=baseGrid.funCode;
        var basePanel=baseGrid.up("basepanel[funCode="+funCode+"]");
        var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

                                                                             
        var funData=basePanel.funData;
        var detCode=basePanel.detCode;
        var detLayout=basePanel.detLayout;
        var defaultObj=funData.defaultObj;

                                                                            
        var otherController=basePanel.otherController;
        if (!otherController) 
            otherController='';
                                                                            
        var insertObj=self.getDefaultValue(defaultObj);
        var popFunData=Ext.apply(funData,{
              grid:baseGrid
        });


       var tabTitle=funData.tabConfig.addTitle;
       var tabItemId=funCode+"_gridAdd";
       var pkValue=null;
       var operType=cmd;
       switch(cmd){
            case "react":
                if (btn) {
                    var rescords=baseGrid.getSelectionModel().getSelection();
                    if (rescords.length !=1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData=rescords[0].data;
                }
                var pkName=funData.pkName;
                pkValue=recordData[pkName];

                insertObj=recordData;
                tabTitle=funData.tabConfig.reactTitle;
                tabItemId=funCode+"_gridReact";
                operType="edit";
                break;



            case "delete":
               if (btn) {
                  var rescords=baseGrid.getSelectionModel().getSelection();
                    if (tabItem.itemPKV&&tabItem.itemPKV!=pkValue) {
                        self.msgbox("是否删除数据？");
                        return;
                    }
                    recordData=rescords[0].data;
                }

              var pkName=funData.pkName;
              pkValue=recordData[pkName];
              insertObj=recordData;
              tabTitle=funData.tabConfig.deleteTitle;
              tabItemId=funCode+"_gridDelete"+pkValue;
              break;
        }
    
    
        var tabItem=tabPanel.getComponent(tabItemId);
        if (!tabItem) {
            tabItem=Ext.create({
                xtype:'container',
                title:tabTitle,
                scrollable:true,
                itemId:tabItemId,
                itemPKV:pkValue,
                layout:'fit',
            });
            tabPanel.add(tabItem);
            
                              
            setTimeout(function(){
                var item=Ext.widget("baseformtab",{
                    operType:operType,
                    controller:otherController,
                    funCode:funCode,
                    detCode:detCode,
                    tabItemId:tabItemId,
                    insertObj:insertObj,
                    funData:popFunData,
                    items:[{
                        xtype:detLayout,
                        funCode:detCode,
                    }]
                });

                tabItem.add(item);
                var objDetForm=item.down("baseform[funCode="+detCode+"]");
                var formDeptObj=objDetForm.getForm();
                self.setFormValue(formDeptObj,insertObj);
                
                if (cmd=="detail") {
                    self.setFuncReadOnly(funData,objDetForm,true);
                }
            },30);

        }else if (tabItem.itemPKV&&tabItem.itemPKV!=pkValue){
            self.Warning("你已经打开一个编辑窗口了");
            return;
        }
        tabPanel.setActiveTab(tabItem);
        
    },

    //数据维护操作
    doDetail: function(btn, cmd) {
        var self = this;
        var baseGrid = btn.up("basegrid");
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
    //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });
        var iconCls = "x-fa fa-plus-circle";
        if (cmd=="edit" || cmd=="detail"){
            if (cmd=="edit")
                iconCls = "x-fa fa-pencil-square";
            else
                iconCls = "x-fa fa-pencil-square";

            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1){
                 self.msgbox("请选择数据");
                 return;
            }
            insertObj = rescords[0].data;
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                width: 400,
                height: 210,
                resizable:false,
                iconCls: iconCls,
                operType: cmd,
                funData: popFunData,
                funCode: detCode,
                items: [{
                    xtype: detLayout
                }]
            });
        }
        win.show();
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
        var formDeptObj = objDetForm.getForm();

        //表单赋值
        self.setFormValue(formDeptObj, insertObj);
        //根据操作设置是否只读
        if (cmd == "detail") {
            self.setFuncReadOnly(funData, objDetForm, true);
        }
        //执行回调函数
        if (btn.callback) {
            btn.callback();
        }
    },

});
Ext.define("core.oa.meeting.checkresult.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.checkresult.mainController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",

        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
    init: function() {
    },
    control: {
        "basegrid button[ref=gridAdd_tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridDetail_Tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },
        
        "basegrid button[ref=gridEdit_tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid  actioncolumn": {
            editClick: function(data) {
                console.log(data);

            },
             detailClick_Tab: function(data) {
                var baseGrid=data.view;
                var record=data.record;
                this.doDetail_Tab(null,"detail",baseGrid,record);
                 return false;
            },
            deleteClick: function(data) {
                console.log(data);

            },
        }
    },
           doDetail_Tab:function(btn,cmd,grid,record){
                var self=this;
                var baseGrid;
                var recordData;
// 根据点击的地方是按钮或者是操作列，处理一些数据
                if (btn) {
                    baseGrid=btn.up("basegrid");
                }else{
                    baseGrid=grid;
                    recordData=record.data;
                }
                
                var funCode=baseGrid.funCode;

                var basePanel=baseGrid.up("basepanel[funCode="+funCode+"]");
                var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");
       
 // 得到配置信息
                var funData=basePanel.funData;
                var detCode=basePanel.detCode;
                var detLayout=basePanel.detLayout;
                var defaultObj=funData.defaultObj;

// 打开新的视图界面的控制器
                var otherController=basePanel.otherController;
                if (!otherController)
                    otherController='';

                var insertObj=self.getDefaultValue(defaultObj);
                var popFunData=Ext.apply(funData,{
                    grid:baseGrid
                });

                var tabTitle=funData.tabConfig.addTitl;
                var tabItemId=funCode+"_gridDetail";
                var pkValue=null;
                var operType=cmd;
                switch(cmd){
                    case "detail":
                      if (btn) {
                            var records=baseGrid.getSelectionModel().getSelection();
                            if (records.length !=1) {
                                self.msgbox("请选择一条数据!");
                                return;
                            }
                            recordData=records[0].data;
                        }
                            var pkName=funData.pkName;
                            pkValue=recordData[pkName];

                            insertObj=recordData;
                            tabTitle=funData.tabConfig.detailTitle;
                            tabItemId=funCode+"_gridDetail";                           
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
                                items:[{
                                    xtype:"checkresult.DetailPanel"
                                }]
                            }]
                        });
                        tabItem.add(item);
                        var detailhtmlpanel=item.down("container[xtype=checkresult.DetailPanel]");
                        detailhtmlpanel.setData(recordData);
                    },30);
                }else if (tabItem.itemPKV&&tabItem.itemPKV!=pkValue){
                    self.Warning("你已经打开一个编辑窗口了！");
                    return;
                }
                  tabPanel.setActiveTab(tabItem);
        },   
});
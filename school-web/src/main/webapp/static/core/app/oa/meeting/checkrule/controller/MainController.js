Ext.define("core.oa.meeting.checkrule.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.checkrule.mainController',
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
        /**
         * grid加载后根据权限控制按钮的显示
         */
        "basegrid[xtype=checkrule.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btnAdd = grid.down("button[ref=gridAdd_Tab]");
                var btnDelete = grid.down("button[ref=gridDelete]");
                var btnUse = grid.down("button[ref=gridSetUse]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1&&roleKey.indexOf("SCHOOLADMIN") == -1&&roleKey.indexOf("HYKQMANAGER") == -1) {
                    btnAdd.setHidden(true);
                    btnDelete.setHidden(true);
                    btnUse.setHidden(true);
                }
            }
        },
        /**
         * 设置规则的启用与禁用
         */
        "basegrid[xtype=checkrule.maingrid] button[ref=gridSetUse]": {
            beforeclick: function(btn) {
                var self = this;

                //得到组件
                var baseGrid = btn.up("basegrid");

                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择数据!");
                    return;
                }

                var uuid = records[0].get("uuid");
                var startUsing=records[0].get("startUsing");
                
                Ext.MessageBox.confirm('温馨提示', '你确定要设置启用/禁用状态吗？', function(btn, text) {
                    if (btn == 'yes') {
                        Ext.Msg.wait('正在执行中,请稍后...', '温馨提示');
                        self.asyncAjax({
                            url: comm.get("baseUrl")  + "/OaMeetingcheckrule/doUse",
                            params: {
                                uuid:uuid,
                                startUsing:startUsing==1?0:1                   
                            },
                            timeout:1000*60*60, //1个小时
                            //回调代码必须写在里面
                            success: function(response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                                           
                                if(data.success){ 
                                    baseGrid.getStore().load();
                                    Ext.Msg.hide();
                                    self.msgbox(data.obj);
                                }else{
                                    Ext.Msg.hide();
                                    self.Error(data.obj);
                                }
                            },
                            failure: function(response) {
                                Ext.Msg.hide();
                                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                            }
                        });  
                    }
                });
                // return false;
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
           var tabItemId=funCode+"_gridDetail";
           var pkValue=null;
           var operType=cmd;
       switch(cmd){
            case "detail":
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
                tabTitle=funData.tabConfig.detailTitle;
                tabItemId=funCode+"_gridDetail"+insertObj.uuid;
                operType="Detail";
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
                                items: [{
                                    xtype: "checkrule.DetailPanel"
                                }]
                            }]
                        });

                        tabItem.add(item);
                        
                        var detailhtmlpanel = item.down("container[xtype=checkrule.DetailPanel]");
                        detailhtmlpanel.setData(recordData);                       
                        
                    },30);

                }else if (tabItem.itemPKV&&tabItem.itemPKV!=pkValue){
                    self.Warning("你已经打开一个编辑窗口了");
                    return;
                }
                tabPanel.setActiveTab(tabItem);  
    },
});
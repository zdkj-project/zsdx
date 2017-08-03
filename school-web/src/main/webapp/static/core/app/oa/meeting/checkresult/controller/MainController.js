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
                this.doDetail_Tab(btn,"detail");
                return false;
            }
        },
        
        "basegrid button[ref=gridEdit_tab]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },
        /**
         * 导出住宿信息
         */
        "basegrid button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();              
                var title = "将导出所有的会议信息";
                var ids = new Array();
                if (records.length > 0) {
                    title = "将导出所选会议的信息";
                    Ext.each(records, function (rec) {
                        var pkValue = rec.get(pkName);
                        ids.push(pkValue);
                    });

                }
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/OaMeeting/exportMeetingExcel?ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/OaMeeting/checkExportMeetingEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
                                        component.destroy();                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
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
                    recordData=record.getData();
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
                            recordData=records[0].getData();
                        }
                        var pkName=funData.pkName;
                        pkValue=recordData[pkName];

                        insertObj=recordData;
                        tabTitle=funData.tabConfig.detailTitle;
                        tabItemId=funCode+"_gridDetail"+insertObj.uuid;                           
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

                        //处理数据字典字段的值                                       
                        var ddItem = factory.DDCache.getItemByDDCode("MEETINGCATEGORY");
                        var resultVal="";
                        var value=recordData["meetingCategory"];
                        for (var i = 0; i < ddItem.length; i++) {
                            var ddObj = ddItem[i];
                            if (value == ddObj["itemCode"]) {
                                resultVal = ddObj["itemName"];
                                break;
                            }                                                    
                        }
                        recordData.meetingCategory=resultVal;  

                        detailhtmlpanel.setData(recordData);
                    },30);
                }else if (tabItem.itemPKV&&tabItem.itemPKV!=pkValue){
                    self.Warning("你已经打开一个编辑窗口了！");
                    return;
                }
                  tabPanel.setActiveTab(tabItem);
        },   
});
Ext.define("core.cashier.mealtime.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mealtime.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function () {
    },
    control: {
    	"basegrid[xtype=mealtime.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btngridEdit_Tab = grid.down("button[ref=gridEdit_Tab]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1
                   && roleKey.indexOf("ZONGWUROLE") == -1 && roleKey.indexOf("FOODMANAGER") == -1) {
                	btngridEdit_Tab.setHidden(true);
                }
            }
           },
           
           /**
            * 列表操作列事件
            */
           "basegrid  actioncolumn": {
        	   editClick_Tab: function (data) {
                   var baseGrid = data.view;
                   var record = data.record;
                   var cmd = data.cmd;
                   this.doTimeDetail_Tab(null, cmd, baseGrid, record);
                   return false;
               }
           },
    	
       /**
        *通用表格编辑事件（弹出tab的形式）
        */
       "basepanel basegrid button[ref=gridEdit_Tab]": {
           click: function (btn) {
        	   var self=this;
               //得到组件
               var baseGrid = btn.up("basegrid");
               var records = baseGrid.getSelectionModel().getSelection();
               if (records.length != 1) {
                   self.Warning("请选择一条数据！");
                   return;
               }

               var tabPanel = baseGrid.up("tabpanel[xtype=app-main]");

               //得到配置信息
               var funCode = baseGrid.funCode;
               var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
               var funData = basePanel.funData;

               //设置tab页的itemId
               var tabItemId = funCode + "_gridEdit";     //命名规则：funCode+'_ref名称',确保不重复
               //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
               var tabItem = tabPanel.getComponent(tabItemId);

               //获取主键值
               var pkName = funData.pkName;
               var pkValue = records[0].get(pkName);

               //判断是否已经存在tab了
               if (!tabItem) {
                   var detCode = basePanel.detCode;
                   var detLayout = basePanel.detLayout;
                   var defaultObj = funData.defaultObj;

                   //关键：window的视图控制器
                   var otherController = basePanel.otherController;
                   if (!otherController)
                       otherController = '';

                   //处理特殊默认值
                   var insertObj = records[0].data;
                   var beginTime =(insertObj.beginTime).substring(11);
                   var endTime =(insertObj.endTime).substring(11);
                   insertObj.beginTime=beginTime;
                   insertObj.endTime=endTime;
                   var popFunData = Ext.apply(funData, {
                       grid: baseGrid
                   });

                   var tabTitle = funData.tabConfig.editTitle;

                   tabItem = Ext.create({
                       xtype: 'container',
                       title: tabTitle,
                       //iconCls: 'x-fa fa-clipboard',
                       scrollable: true,
                       itemId: tabItemId,
                       itemPKV: pkValue,      //保存主键值
                       layout: 'fit',
                   });
                   tabPanel.add(tabItem);

                   //延迟放入到tab中
                   setTimeout(function () {
                       //创建组件
                       var item = Ext.widget("baseformtab", {
                           operType: 'edit',
                           controller: otherController,         //指定重写事件的控制器
                           funCode: funCode,                    //指定mainLayout的funcode
                           detCode: detCode,                    //指定detailLayout的funcode
                           tabItemId: tabItemId,                //指定tab页的itemId
                           insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                           funData: popFunData,                //保存funData数据，提供给提交事件中使用
                           items: [{
                               xtype: detLayout
                           }]
                       });
                       tabItem.add(item);

                       var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                       var formDeptObj = objDetForm.getForm();

                       self.setFormValue(formDeptObj, insertObj);

                   }, 30);

               } else if (tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据
                   self.Warning("您当前已经打开了一个编辑窗口了！");
                   return;
               }
               tabPanel.setActiveTab(tabItem);
               //执行回调函数
               if (btn.callback) {
                   btn.callback();
               }
           }
       }
       
    },
    
    //弹出tab页的方式
    doTimeDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

        //得到配置信息
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var funData = basePanel.funData;
       
        //设置tab页的itemId
        var tabItemId=funCode+"_gridEdit";     //命名规则：funCode+'_ref名称',确保不重复
        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem=tabPanel.getComponent(tabItemId);
        
        //获取主键值
        var pkName = funData.pkName;
        var pkValue= record.get(pkName);
        console.log(pkValue);
        //判断是否已经存在tab了
        if(!tabItem){

            var detCode = basePanel.detCode;
            var detLayout = basePanel.detLayout;
            var defaultObj = funData.defaultObj;
           
            //关键：window的视图控制器
            var otherController=basePanel.otherController;  
            if(!otherController)
                otherController='';
            //处理特殊默认值
            var insertObj =  record.data;
            var beginTime =(insertObj.beginTime).substring(11);
            var endTime =(insertObj.endTime).substring(11);
            insertObj.beginTime=beginTime;
            insertObj.endTime=endTime;
            var popFunData = Ext.apply(funData, {
                grid: baseGrid
            });

            var tabTitle = funData.tabConfig.editTitle;

            tabItem=Ext.create({
                xtype:'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
                scrollable :true, 
                itemId:tabItemId,
                itemPKV:pkValue,      //保存主键值
                layout:'fit', 
            });
            tabPanel.add(tabItem); 

            //延迟放入到tab中
            setTimeout(function(){
                //创建组件
                var item=Ext.widget("baseformtab",{
                    operType:'edit',
                    controller:otherController,         //指定重写事件的控制器
                    funCode:funCode,                    //指定mainLayout的funcode
                    detCode:detCode,                    //指定detailLayout的funcode
                    tabItemId:tabItemId,                //指定tab页的itemId
                    insertObj:insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items:[{
                        xtype:detLayout
                    }]
                }); 
                tabItem.add(item);  

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                self.setFormValue(formDeptObj, insertObj);

            },30);
                           
        }else if(tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);
    },

});
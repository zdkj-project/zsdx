Ext.define("core.mjmanage.roomallot.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.roomallot.maincontroller',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        treeUtil: "core.util.TreeUtil",
        gridActionUtil: "core.util.GridActionUtil"
    },
    init: function() {
        var self = this
        // 事件注册
        this.control({
            // "basepanel basegrid[xtype=mjmanage.roomallot.maingrid]": {
            //     afterrender : function(grid) {
            //         this.hideFuncBtn(grid);
            //     },
            //  },
             "basetreegrid[xtype=mjmanage.roomallot.roomallottree]": {
                /*
                    当点击了这个树的子项后，在查询列表的条件中，要做如下工作：
                    1. 附带树节点的相关参数
                    2. 当存在basegrid的默认参数，则附带上去
                    3. 附带快速搜索中的参数（为了防止文本框的数据与实际查询的数据不一致，所以在下面代码中主动获取了文本框的数据）
                    4. reset清除高级搜索中的条件数据 以及 proxy.extraParams中的相关数据
                */
                itemclick: function(tree, record, item, index, e, eOpts) {                   
                    this.loadMainGridStore(tree,record);   
                    return false;
               }
            },
            //区域列表刷新按钮事件
            "panel[xtype=mjmanage.roomallot.roomallottree] button[ref=gridRefresh]": {
                click: function(btn) {
                    var baseGrid = btn.up("basetreegrid");
                    var store = baseGrid.getStore();
                    store.load(); //刷新父窗体的grid
                    return false;
                }
            },
           "basegrid[xtype=mjmanage.roomallot.maingrid] button[ref=allotOffRoom]": {
                beforeclick: function(btn) {
                    self.openRoomAllot_Win(btn);
                    return false;
                }
            },
            "basegrid[xtype=mjmanage.roomallot.maingrid] button[ref=gridDelete]": {
                beforeclick: function(btn) {
                    self.doDeleteRecords(btn);
                    return false;
                }
            },
            "basegrid[xtype=mjmanage.roomallot.maingrid] button[ref=officeTs]": {
                beforeclick: function(btn) {
                    self.officeTs(btn);
                    return false;
                 
                }
            },
          
             /**
             * 操作列的操作事件
             */
             "basegrid[xtype=mjmanage.roomallot.maingrid] actioncolumn": {

                deleteClick: function(data) {
                    self.doDeleteRecords(null,data.view,data.record);
                     return false;
                },
                
            },

            "basepanel basegrid[xtype=mjmanage.roomallot.maingrid]": {
                afterrender: function (grid, eOpts) {
                    var btnallotOffRoom = grid.down("button[ref=allotOffRoom]");
                    var btngridDelete = grid.down("button[ref=gridDelete]");
                    
                    var roleKey = comm.get("roleKey");
                    if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("ZONGWUROLE") == -1) {
                      btnallotOffRoom.setHidden(true);
                      btngridDelete.setHidden(true);
                    }
                },
                beforeitemclick: function(grid) {
                    this.disabledFuncBtn(grid);              
                    return false;
                },

            }, 
 
        });
    },

    openRoomAllot_Win: function(btn) {
        var self = this;

        //得到组件
        var baseGrid=btn.up("basegrid");
        var basePanel = baseGrid.up("basepanel");

        //得到配置信息
        var funData = basePanel.funData;                //主界面的配置信息  
        var pkName=funData.pkName;

        var funCode = basePanel.funCode;          //主界面的funCode
        var detCode =  basePanel.detCode;               //打开的tab也的detCode标识，可自定指定，用于查找唯一组件
        var detLayout = basePanel.detLayout;            //打开的tab页的布局视图
        
        var otherController = basePanel.otherController;    //关键：打开的tab页面的视图控制器
        if (!otherController)
            otherController = '';  

        //设置window的参数
        var width = 1200;
        var height = 550;
        var iconCls= 'x-fa fa-plus-circle';
        var winTitle = "教师列表";
        var recordData=null;
        var operType="add";

         // 选择的字典项信息
         var basetreegrid = baseGrid.up("panel[xtype=mjmanage.roomallot.mainlayout]").down("panel[xtype=mjmanage.roomallot.roomallottree]");
         var selectObject = basetreegrid.getSelectionModel().getSelection();
         if (selectObject.length <= 0) {
             self.msgbox("请选择办公室!");
             return;
         }
                            // 得到选择的字典
         var objDic = selectObject[0];
         var leaf = objDic.get("leaf");
         var roomId = objDic.get("id");
         if (leaf != true) {//true: 房间 false:区域
            self.msgbox("只能选择办公室操作!"); 
            return;
          }
         //创建tab内部组件                     
         var insertObj =  Ext.apply(new Object(),funData.defaultObj);
         var popFunData = Ext.apply(funData, {   //将一些必要的信息，统一存放于此，提高给处理提交代码使用。
            grid: baseGrid
         });
         if(recordData!=null){
          insertObj=recordData;
          }  

         var win = Ext.create('core.base.view.BaseFormWin', {
                    iconCls:iconCls,
                    title: winTitle,
                    operType: operType,
                    width: width,
                    height: height,
                    roomId: roomId,
                    controller: otherController,
                    funData: popFunData,
                    funCode: detCode,
                    insertObj: insertObj,        
                    items: [{
                        xtype: detLayout
                    }]
        }).show();
        var selecTeacherPanel = win.down("basepanel[xtype=mjmanage.roomallot.selectteacherlayout]");
        var selectGrid=selecTeacherPanel.down("basegrid[xtype=mjmanage.roomallot.selectteachergrid]");
        var selectStore = selectGrid.getStore();
        var selectProxy = selectStore.getProxy();
        selectProxy.extraParams = {
            filter: '[{"type":"string","comparison":"=","value":"1","field":"category"}]',
        };
        selectStore.loadPage(1);         
    },

   
    doDeleteRecords:function(btn,grid,record){
        var self=this;
        var records;
        var baseGrid = grid;
        if(!baseGrid){
            baseGrid=btn.up("basegrid");
            records = baseGrid.getSelectionModel().getSelection();
        }else{
            records=new Array();
            records.push(record);
        }
        funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        //得到配置信息
        var funData = basePanel.funData;
        var tteacId = '';
        var uuid = '';
        var roomId ='';
        if (records.length <= 0) {
            self.msgbox('请选择一条数据');
            return;
        }
       // var roomId = records[0].get('roomId');
        for (var i = 0; i < records.length; i++) {
            tteacId += records[i].get('tteacId') + ',';
            uuid += records[i].get('uuid') + ',';
            roomId += records[i].get('roomId')+ ',';
        };
        if (records.length > 0) {
            //封装ids数组
            Ext.Msg.confirm('提示',"是否解除设置", function (btn, text) {
                if (btn == 'yes') {
                    
                    var loading = new Ext.LoadMask(baseGrid, {
                        msg: '正在提交，请稍等...',
                        removeMask: true// 完成后移除
                    });
                    loading.show();

                    self.asyncAjax({
                        url: funData.action + "/doDelete",
                        params: {
                             uuid: uuid,
                             roomId: roomId,
                             tteacId: tteacId
                        },                    
                        success: function(response) {
                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                            if(data.success){
                                 baseGrid.getStore().load(); //不刷新的方式
                                 /*
                                setTimeout(function(){
                                   self.ajax({
                                      url: funData.action + "/doSetOff",//这个方法直接放在delete方法中一并执行了，防止事务问题
                                      params: {
                                         roomId: roomId,
                                        },
                                    })   

                                },30);
                               */
                       
                                self.msgbox(data.obj);                               
                            }else {
                                self.Error(data.obj);
                            }
                            loading.hide();
                        },
                        failure: function(response) {                   
                            Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                            loading.hide();
                        }
                    });     
                }
            });
        } else {
            self.msgbox("请选择数据");
        }
    },

    officeTs:function(btn) {
        var self=this;
        var mainLayout = btn.up("basepanel[xtype=mjmanage.roomallot.mainlayout]");
        var storeyGrid = mainLayout.down("basegrid[xtype=mjmanage.roomallot.maingrid]");
        var jwtrTree = mainLayout.down("basetreegrid[xtype=mjmanage.roomallot.roomallottree]");
        var selectTreeObject = jwtrTree.getSelectionModel().getSelection();
        if (selectTreeObject.length <= 0) {
            self.msgbox("请选择要推送的办公室！");
            return;
        }
        var objDic = selectTreeObject[0];
        var roomId = objDic.get("id");
        var leaf = objDic.get("leaf");
        if (leaf != true) {//true: 房间 false:区域
            self.msgbox("推送消息时，请选择单个办公室!"); 
            return;
        }
        var count = storeyGrid.getStore().getCount();
        if (count <= 0) {
            self.msgbox("列表中无任何数据!");
            return;
        }
        Ext.Msg.confirm("推送消息", "您确定要推送信息吗？", function(btns) {
            if (btns == 'yes') {
                var loading = self.LoadMask(mainLayout,'正在推送消息中，请等待...');
                self.asyncAjax({
                  url: comm.get('baseUrl') + "/BaseOfficeAllot/doPushMessage",
                  params: {
                     roomId: roomId,
                 },                 
                 success: function(response) {
                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                    if(data.success){
                            self.msgbox(data.obj); 
                        }else {
                            self.Error(data.obj);
                        }           
                        loading.hide();
                    },
                    failure: function(response) {                   
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                        loading.hide();
                    }
                }); 

             }
        })

    },

    hideFuncBtn:function(grid){    
        if(comm.get("isAdmin")!="1"){
            var menuCode="BASEROOMALLOT";     // 此菜单的前缀
            var userBtn=comm.get("userBtn");
            if(userBtn.indexOf(menuCode+"_allotOffRoom")==-1){
                var btnAllotOff = grid.down("button[ref=allotOffRoom]");
                btnAllotOff.setHidden(true);
                
            }
            if(userBtn.indexOf(menuCode+"_officeTs")==-1){
                var btnOfficeTs = grid.down("button[ref=officeTs]");
                btnOfficeTs.setHidden(true);
                
            }
        }
    },

    loadMainGridStore:function(tree,record){                
        var self = this;
        var mainLayout = tree.up("panel[xtype=mjmanage.roomallot.mainlayout]");

        var storeGrid = mainLayout.down("panel[xtype=mjmanage.roomallot.maingrid]");
        var store = storeGrid.getStore();
        var proxy = store.getProxy();

        //获取点击树节点的参数        
        var roomId=record.get("id");
        var roomLeaf=record.get("leaf");
        Ext.apply(mainLayout.funData, {                       
            roomId: roomId,
            leaf:roomLeaf
        });
        

        if(roomLeaf==true)
            roomLeaf="1";
        else
            roomLeaf="0";

        //附带参赛
        proxy.extraParams={
            roomId:roomId,
            roomLeaf:roomLeaf
        }
        store.loadPage(1); 
    },

    disabledFuncBtn:function(grid){
      var basePanel = grid.up("basepanel");
      var basegrid = basePanel.down("basegrid[xtype=mjmanage.roomallot.maingrid]");
      var records = basegrid.getSelectionModel().getSelection();

      var btnDelete = basegrid.down("button[ref=gridDelete]");

      if (records.length == 0) {
        btnDelete.setDisabled(true);
      } else if (records.length == 1) {
        btnDelete.setDisabled(false);
      } else {
       btnDelete.setDisabled(false);
      }
    },

});
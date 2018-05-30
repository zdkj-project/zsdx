Ext.define("core.mjmanage.ptmjopendoor.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.ptmjopendoor.maincontroller',
    mixins: {
      suppleUtil: "core.util.SuppleUtil",
      messageUtil: "core.util.MessageUtil",
      formUtil: "core.util.FormUtil",
      gridActionUtil: "core.util.GridActionUtil",
      dateUtil: 'core.util.DateUtil' 
  },
  init: function () {
        /*执行一些初始化的代码*/
    },
    /** 该视图内的组件事件注册 */
    control: {
        "basetreegrid[xtype=mjmanage.ptmjopendoor.roominfotree] button[ref=gridRefresh]": {
            beforeclick: function(btn) {
                btn.up('basetreegrid').getStore().load();
                var mainlayout = btn.up("basepanel[xtype=mjmanage.ptmjopendoor.mainlayout]");
                var mianGrid = mainlayout.down("basegrid[xtype=mjmanage.ptmjopendoor.maingrid]");
                var store = mianGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams.roomId="";
                proxy.extraParams.roomLeaf="";
                return false;
            }
        },
        "basetreegrid[xtype=mjmanage.ptmjopendoor.roominfotree]": {
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
        "basegrid[xtype=mjmanage.ptmjopendoor.maingrid] button[ref=gridExport]": {
            /*
                目前的导出原则，界面上面筛选的什么条件，就查询出什么条件
                不分页，一次性导出符合条件的数据
            */
            beforeclick: function(btn) {
                this.doExport(btn);
                return false;
            }
        },

        "basepanel basegrid button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
               this.queryFastSearchForm(btn);
                return false;
              }
        },

        "basepanel basegrid field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {
                   this.queryFastSearchForm(field);
                   return false;
                   
               }
           }
        }
           
    },
    doExport:function(btn){
        var self = this;
        var baseGrid = btn.up("basegrid[xtype=mjmanage.ptmjopendoor.maingrid]");
        var basepanel = baseGrid.up('basepanel');
        var roominfotreegrid = basepanel.down("basetreegrid[xtype=mjmanage.ptmjopendoor.roominfotree]");
        var records = roominfotreegrid.getSelectionModel().getSelection();
        var roomId ="";
        var roomLeaf ="";
        if(records.length>0){
            roomId = records[0].get('id');
            roomLeaf = records[0].get("leaf");
            if(roomLeaf==true)
                roomLeaf="1";
            else
                roomLeaf="0";
        }

        var toolBar = btn.up("toolbar");
        var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
        var openDateStart= "";
        var openDateEnd = "";
        if(girdSearchTexts[0].getValue()){
            openDateStart = girdSearchTexts[0].getValue();
        }
        if(girdSearchTexts[1].getValue()){
            openDateEnd = girdSearchTexts[1].getValue();
        }


        var title = "确定要导出门禁开门记录吗？";
        Ext.Msg.confirm('提示', title, function (btn, text) {
        if (btn == "yes") {
            Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
            var component = Ext.create('Ext.Component', {
                title: null,
                width: 0,
                height: 0,
                hidden: true,
                html: '<iframe src="' + comm.get('baseUrl') + '/PtMjOpenDoor/doExportExcel?'+
                    'roomId='+roomId+'&roomLeaf='+roomLeaf+
                    '&openDateStart='+openDateStart+'&openDateEnd='+openDateEnd+'"></iframe>',
                renderTo: Ext.getBody()
            });

            var time = function () {
                self.syncAjax({
                    url: comm.get('baseUrl') + '/PtMjOpenDoor/checkExportEnd',
                    timeout: 1000 * 60 * 30,      
                    success: function (response) {
                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                        if (data.success) {
                            Ext.Msg.hide();
                            self.msgbox(data.obj);
                            component.destroy();
                        } else {
                            if (data.obj == 0) {    
                                Ext.Msg.hide();
                                self.Error("导出失败，请重试或联系管理员！");
                                component.destroy();
                            } else {
                                setTimeout(function () {
                                    time()
                                }, 1000);
                            }
                        }
                    },
                    failure: function (response) {
                        Ext.Msg.hide();
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                        component.destroy();
                    }
                });
            };
            setTimeout(function () {
                time()
            }, 1000);   
          }
        });
        return false;
    },

    queryFastSearchForm:function(btn){
        var self = this;
        var baseGrid = btn.up("basegrid");
        var toolBar = btn.up("toolbar");

        var filter=self.getFastSearchFilter(toolBar);

        var store = baseGrid.getStore();
        var proxy = store.getProxy();
        if(filter.length==0)
            delete proxy.extraParams.filter;
        else
            proxy.extraParams.filter = JSON.stringify(filter);
        store.loadPage(1);

    },

    getFastSearchFilter:function(cpt){
        var girdSearchTexts = cpt.query("field[funCode=girdFastSearchText]");
        var filter=new Array();
        if(girdSearchTexts[0].getValue()){
            filter.push({"type": "date", "value": girdSearchTexts[0].getValue(), "field": "openDate", "comparison": ">="})
        }
        if(girdSearchTexts[1].getValue()){
            filter.push({"type": "date", "value": girdSearchTexts[1].getValue(), "field": "openDate", "comparison": "<="})
        }
        return filter;
    },

    loadMainGridStore:function(tree,record){
        var self = this;
        var mainLayout = tree.up("panel[xtype=mjmanage.ptmjopendoor.mainlayout]");
        mainLayout.funData.roomId=record.get("id");

        var storeGrid = mainLayout.down("panel[xtype=mjmanage.ptmjopendoor.maingrid]");
        var store = storeGrid.getStore();
        var proxy = store.getProxy();

        //获取右边筛选框中的条件数据
        var filter=self.getFastSearchFilter(storeGrid);      
        filter.push({"type":"string","comparison":"!=","value":"0","field":"roomType"});     //默认参数     
        if(filter.length==0)
            filter=null;
        else
            filter = JSON.stringify(filter);

        //获取点击树节点的参数
        var roomId= record.get("id");
        var roomLeaf=record.get("leaf");
        if(roomLeaf==true)
            roomLeaf="1";
        else
            roomLeaf="0";
        
        //附带参赛
        proxy.extraParams={
            roomId:roomId,
            roomLeaf:roomLeaf,
            filter:filter
        }
        store.loadPage(1); 
    }
});
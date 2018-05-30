Ext.define("core.mjmanage.userauthority.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.userauthority.maincontroller',
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
      "basetreegrid[xtype=mjmanage.userauthority.depttree] button[ref=gridRefresh]": {
          beforeclick: function(btn) {
              this.refreshTreeStore(btn);
             return false;
         }
      },
      "basetreegrid[xtype=mjmanage.userauthority.depttree]": {
            itemclick: function(tree, record, item, index, e, eOpts) {
                this.loadUserGridStore(tree,record);
                return false;
           }
        },
      "basegrid[xtype=mjmanage.userauthority.maingrid] button[ref=gridFastSearchBtn]": {
          beforeclick: function (btn) {
           this.queryFastSearchForm(btn);
           return false;
         }
       },

      "basegrid[xtype=mjmanage.userauthority.maingrid] field[funCode=girdFastSearchText]": {
          specialkey: function (field, e) {
            if (e.getKey() == e.ENTER) {
              this.queryFastSearchForm(field);
            return false;
          }
        },
      },

     "basegrid[xtype=mjmanage.userauthority.usergrid]": {
          beforeitemclick: function (grid, record, item) {
              this.loadUserRightInfo(grid,record);                  
              return false;
          }
      },
    },
    getFastSearchFilter:function(cpt){
        var girdSearchTexts = cpt.query("field[funCode=girdFastSearchText]");
        var filter=new Array();
        if(girdSearchTexts[0].getValue()){
            filter.push({"type": "string", "value": girdSearchTexts[0].getValue(), "field": "xm", "comparison": ""})
        }
        return filter;
    },
    queryFastSearchForm:function(btn){
        var self = this;

        var baseGrid = btn.up("basegrid");
        var toolBar = btn.up("toolbar");    

        //获取快速搜索框的值
        var girdSearchTexts = baseGrid.query("field[funCode=girdFastSearchText]");
        var value1 =girdSearchTexts[0].getValue();
        var filter=new Array();
        if(value1){
            filter.push({"type": "string", "value": value1, "field": "termName", "comparison": ""})
        }          
        if(filter.length!=0)
            filter = JSON.stringify(filter);
        else
            filter = "";

        
        var store = baseGrid.getStore();
        var proxy = store.getProxy();
        proxy.extraParams.filter = filter;
        store.loadPage(1);
    },

    loadUserRightInfo:function(grid,record){
        var mainlayout = grid.up('panel[xtype=mjmanage.userauthority.mainlayout]');
        var baseGrid = mainlayout.down('panel[xtype=mjmanage.userauthority.maingrid]');
                        
        //获取快速搜索框的值
        var girdSearchTexts = baseGrid.query("field[funCode=girdFastSearchText]");
        var value1 =girdSearchTexts[0].getValue();
        var filter=new Array();
        if(value1){
            filter.push({"type": "string", "value": value1, "field": "termName", "comparison": ""})
        }          
        if(filter.length!=0)
            filter = JSON.stringify(filter);
        else
            filter = "";
    
        var stores = baseGrid.getStore();
        var proxy = stores.getProxy();

        proxy.extraParams={
            userId:record.get("uuid"),
            filter:filter
        };
        stores.loadPage(1); //刷新
    },

    refreshTreeStore:function(btn){  
       btn.up('basetreegrid').getStore().load();
       var baseGrid = btn.up("basetreegrid");
       var mainlayout = baseGrid.up("basepanel[xtype=mjmanage.userauthority.mainlayout]");
       var mianGrid = mainlayout.down("basegrid[xtype=mjmanage.userauthority.usergrid]");
       var store = mianGrid.getStore();
       var proxy = store.getProxy();
       proxy.extraParams.deptId="";
    },

    loadUserGridStore:function(tree,record){    
        var self = this;
        var mainLayout = tree.up("basepanel[xtype=mjmanage.userauthority.mainlayout]");
        var funData = mainLayout.funData;
        funData = Ext.apply(funData, {
          deptId: record.get("id"),
          /*isRight:record.get("isRight"),
          deptType:record.get("deptType")*/
        });

        var storeGrid = mainLayout.down("panel[xtype=mjmanage.userauthority.usergrid]");
        var store = storeGrid.getStore();
        var proxy = store.getProxy();

        //获取右边筛选框中的条件数据
        var filter=self.getFastSearchFilter(storeGrid);
        //filter.push({"type":"string","comparison":"=","value":"1","field":"category"})
        if(filter.length==0)
            filter=null;
        else
            filter = JSON.stringify(filter); 
         //附带参赛
        proxy.extraParams = {
          deptId: record.get("id"),
          filter:filter
        };
        store.loadPage(1); 
    }
});
Ext.define("core.mjmanage.roomuserauthority.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.roomuserauthority.maincontroller',
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
                 // 树刷新
        "basetreegrid[xtype=mjmanage.roomuserauthority.mjuserrighttree] button[ref=gridRefresh]": {
            beforeclick: function(btn) {
                var baseGrid = btn.up("panel[xtype=mjmanage.roomuserauthority.mjuserrighttree]");
                var store = baseGrid.getStore();
                store.load(); // 刷新父窗体的grid

                var mainLayout= baseGrid.up("panel[xtype=mjmanage.roomuserauthority.mainlayout]");
                var storeyGrid = mainLayout.down("panel[xtype=mjmanage.roomuserauthority.maingrid]");
                var store = storeyGrid.getStore();
                store.removeAll();
                store.getProxy().extraParams.querySql="";
                
                return false;
            }
        },
        "basetreegrid[xtype=mjmanage.roomuserauthority.mjuserrighttree]": {
            /*
                当点击了这个树的子项后，在查询列表的条件中，要做如下工作：
                1. 附带树节点的相关参数
                2. 当存在basegrid的默认参数，则附带上去
                3. 附带快速搜索中的参数（为了防止文本框的数据与实际查询的数据不一致，所以在下面代码中主动获取了文本框的数据）
                4. reset清除高级搜索中的条件数据 以及 proxy.extraParams中的相关数据
            */
            itemclick: function(tree, record, item, index, e, eOpts) {            
                var self = this;
                var mainLayout = tree.up("panel[xtype=mjmanage.roomuserauthority.mainlayout]");
                mainLayout.funData.roomId=record.get("id");

                var storeGrid = mainLayout.down("panel[xtype=mjmanage.roomuserauthority.maingrid]");
                var store = storeGrid.getStore();
                var proxy = store.getProxy();

                //获取右边筛选框中的条件数据
                var filter=self.getFastSearchFilter(storeGrid);            
                if(filter.length!=0)
                    filter = JSON.stringify(filter);
                else
                    filter = "";

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
                    //querySql2:querySql2,
                    filter:filter
                }
                store.loadPage(1); 
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
     },
    },
    queryFastSearchForm:function(btn){
        var self = this;

        var baseGrid = btn.up("basegrid");
        var toolBar = btn.up("toolbar");    

        //获取快速搜索框的值    
        var filter=self.getFastSearchFilter(toolBar);
        
        var store = baseGrid.getStore();
        var proxy = store.getProxy();

        if(filter.length!=0)
            filter = JSON.stringify(filter);
        else
            filter = "";
        //proxy.extraParams.querySql2 =  querySql2 ;
        proxy.extraParams.filter = filter;
        store.loadPage(1);
    },

    getFastSearchFilter:function(cpt){
        var girdSearchTexts = cpt.query("field[funCode=girdFastSearchText]");
        var value1 =girdSearchTexts[0].getValue();
        var value2 =girdSearchTexts[1].getValue();

        var filter=new Array();
        if(value1){
            filter.push({"type": "string", "value": value1, "field": "xm", "comparison": ""})
        }
        if(value2){
            filter.push({"type": "string", "value": value2, "field": "termName", "comparison": ""})
        }

        /*
        var querySql="";
        if(value1){    
            querySql+=" and XM like '%"+value1+"%'";
        }
        if(value2){
            querySql+=" and ROOM_NAME like '%"+value2+"%'";
        }*/
        return filter;
    }
});
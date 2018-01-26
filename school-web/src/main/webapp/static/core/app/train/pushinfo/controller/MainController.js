Ext.define("core.train.pushinfo.controller.MainController", {
	extend: "Ext.app.ViewController",
	alias: 'controller.pushinfo.maincontroller',
	mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil',
        QueryUtil:"core.util.QueryUtil",
        SqlUtil:"core.util.SqlUtil",
    },
	init: function () {
		/*执行一些初始化的代码*/
	},
	/** 该视图内的组件事件注册 */
	control: {
		"basegrid[xtype=pushinfo.infotypegrid]": {
              /*
                当点击了这个树的子项后，在查询列表的条件中，要做如下工作：
                1. 附带树节点的相关参数
                2. 当存在basegrid的默认参数，则附带上去
                3. 附带快速搜索中的参数（为了防止文本框的数据与实际查询的数据不一致，所以在下面代码中主动获取了文本框的数据）
                4. reset清除高级搜索中的条件数据 以及 proxy.extraParams中的相关数据
            */
			beforeitemclick: function(grid, record, item, index, e, eOpts) {
                var self = this;
                var mainLayout = grid.up("panel[xtype=pushinfo.mainlayout]");        
                var baseGrid = mainLayout.down("basegrid[xtype=pushinfo.maingrid]");
                var store=baseGrid.getStore();
                var proxy = store.getProxy();


                var value=record.get("value");
                if(value==1){
                    proxy.url= comm.get("baseUrl") + "/PushInfo/list";
                    proxy.extraParams.infoType=value;
                    mainLayout.getViewModel().set("textName","今日收信");

                }else if(value==2){
                    proxy.url= comm.get("baseUrl") + "/PushInfo/list";
                    proxy.extraParams.infoType=value;
                    mainLayout.getViewModel().set("textName","今日发信");

                }else if(value==3){
                    proxy.url= comm.get("baseUrl") + "/PushInfo/getPushInfoHistory";
                    proxy.extraParams.infoType=value;
                    mainLayout.getViewModel().set("textName","历史收信");

                }if(value==4){
                    proxy.url= comm.get("baseUrl") + "/PushInfo/getPushInfoHistory";
                    proxy.extraParams.infoType=value;
                    mainLayout.getViewModel().set("textName","历史发信");
                }   

                store.load();

                return false;
            }
        },

        //快速搜索按按钮
        "basepanel basegrid button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                this.queryFastSearchForm(btn);
                return false;
            }
        },
        //快速搜索文本框回车事件
        "basepanel basegrid field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {
                    this.queryFastSearchForm(field);                
                }
                return false;
            }
        },
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
            filter.push({"type": "date", "value": girdSearchTexts[0].getValue(), "field": "regTime", "comparison": ">="})
        }
        if(girdSearchTexts[1].getValue()){
            filter.push({"type": "date", "value": girdSearchTexts[1].getValue(), "field": "regTime", "comparison": "<="})
        }
        return filter;
    }
});
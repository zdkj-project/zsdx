Ext.define("core.train.cardinfo.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.cardinfo.mainController',
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
        //快速搜索按按钮
        "basepanel basegrid button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                if (!baseGrid)
                    return false;

                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;

                var filter = [{"type":"numeric","value":3,"field":"cardTypeId","comparison":"="}];               
               
                var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                for (var i in girdSearchTexts) {
                    var name = girdSearchTexts[i].getName();
                    var value = girdSearchTexts[i].getValue();

                    if(value)
                        filter.push({"type": "string", "value": value, "field": name, "comparison": ""});
                }

                var store = baseGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams.filter = JSON.stringify(filter);
                store.loadPage(1);

                return false;
            }
        },
        //快速搜索文本框回车事件
        "basepanel basegrid field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {

                    //得到组件                 
                    var baseGrid = field.up("basegrid");
                    if (!baseGrid)
                        return false;

                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;

                    var filter = [];
                    var gridFilter=[];
                    //获取baseGrid中编写的默认filter值
                    var gridFilterStr=baseGrid.extParams.filter;
                    if(gridFilterStr&&gridFilterStr.trim()!=""){
                        gridFilter=JSON.parse(gridFilterStr);
                        filter=gridFilter;
                    }
                   
                    var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                    for (var i in girdSearchTexts) {
                        var name = girdSearchTexts[i].getName();
                        var value = girdSearchTexts[i].getValue();

                        //判断gridFilter是否包含此值。
                        var isExist=false;
                        for(var j=0;j<gridFilter.length;j++){
                            if(gridFilter[j].field==name){
                                filter[j]={"type": "string", "value": value, "field": name, "comparison": ""};
                                isExist=true;
                                break;
                            }
                        }
                        if(isExist==false)
                            filter.push({"type": "string", "value": value, "field": name, "comparison": ""});
                    }

                   
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams.filter = JSON.stringify(filter);
                    store.loadPage(1);
                }
                return false;
            }
        },
    }
});

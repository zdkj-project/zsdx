
Ext.define("core.oa.roomterminal.controller.OtherController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.roomterminal.otherController',

    /*把不需要使用的组件，移除掉*/
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
   
    init: function() {
        var self = this;

        this.control({
            /*
            "button": {
                click : function(btn) {
                    console.log(this);
                   
                }
            }*/
            "baseformwin[funCode=roomterminal_detail] button[ref=formSave]": {
                beforeclick: function(btn) {
                    self.doSave(btn);
                    return false;
                }
            },

            "mtfuncwindow button[ref=ssOkBtn]":{
                beforeclick:function(btn){
                    console.log("重写mtfuncwindow的确定按钮");
                }
            }
    	

        });
    },

    //保存处理的操作
    doSave: function(btn) {
        var self = this;
        var win = btn.up('window');
        var funCode = win.funCode;
        var doType = win.cmd;
        var grid = win.funData.grid;
        var roomId = funData.roomId;
        var roomName = funData.roomName;
        var filter = funData.filter;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();
        var upData = new Array();
        var termId, termCode, mpNumb;
        for (var i = 0; i < 5; i++) {
            var k = i + 1;
            termId = Ext.valueFrom(formObj.findField("termId" + k).getValue(), null);
            termCode = Ext.valueFrom(formObj.findField("termCode" + k).getValue(), null);
            mpNumb = Ext.valueFrom(formObj.findField("houseNumb" + k).getValue(), null);
            if (!Ext.isEmpty(mpNumb)) {
                for (var j = i + 1; j < 5; j++) {
                    var l = j + 1
                    var termCodett = Ext.valueFrom(formObj.findField("termCode" + l).getValue(), null);
                    if (termCode!=null&&termCode === termCodett) {
                        self.Warning("终端配置重复，请重新设置");
                        return false;
                        break;
                    } else {
                        if (!Ext.isEmpty(mpNumb) && !(Ext.isEmpty(termCode))) {
                            upData.push("{'uuid':'" + termId + "','termCode':'" + termCode + "','houseNumb':'" + mpNumb + "'}");
                        }
                        break;
                    }
                }
            } else {
                break;
            }
        }
        var resObj = self.ajax({
            url: funData.action + "/doSetTerminal",
            params: {
                terminals: "[" + upData.join(",") + "]",
                roomId: roomId,
                roomName: roomName
            }
        });
        if (resObj.success) {
            self.Info(resObj.obj);
            var store = grid.getStore();
            var proxy = store.getProxy();
            proxy.extraParams = {
                filter: filter,
            }
            store.load();
            win.close();
        } else {
            if (!Ext.isEmpty(resObj.obj)) self.Info(resObj.obj);
        }
    }

});
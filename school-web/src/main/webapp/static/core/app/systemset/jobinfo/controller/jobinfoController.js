Ext.define("core.systemset.jobinfo.controller.jobinfoController", {
    extend: "Ext.app.ViewController",
   
	
    alias: 'controller.jobinfo.jobinfoController',
	
    
    
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
/*    views: [
        "core.systemset.jobinfo.view.jobinfoMainLayout",
        "core.systemset.jobinfo.view.jobinfoDetailLayout",
        "core.systemset.jobinfo.view.jobinfoGrid",
        "core.systemset.jobinfo.view.jobinfoDetailForm"
    ],*/
    init: function() {
        var self = this;
        
        this.control({
            "basegrid[funCode=jobinfo_main] button[ref=gridFastSearchBtn]": {
                beforeclick:function(btn){                
                    //得到组件
                    var baseGrid = btn.up("basegrid");
                        
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();


                    var jobName=baseGrid.down("textfield[name=jobName]").getValue();               
                    
                    proxy.extraParams.filter = '[{"type":"string","value":"'+jobName+'","field":"jobName","comparison":""}]';
                    store.loadPage(1);

                    return false;
                }
            },
        	
        	
            "panel[funCode=jobinfo_main] button[ref=sync]": {
                beforeclick: function(btn) {
                	 var resObj = self.ajax({
                         url: "/usersync" + "/job"
                     });
                     if (resObj.success) {
                    	 self.msgbox("同步成功!");
                    	 btn.up("basegrid").getStore().load();
                     }else{
                    	 //self.msgbox("同步成功!");
                     }
                     return false;
                }
            },
        	
        
            /**
             * 增加按钮事件响应
             * @type {[type]}
             */
            "basegrid[funCode=jobinfo_main] button[ref=gridAdd]": {
                beforeclick: function(btn) {
                    self.doDetail(btn, "add");
                    return false;
                }
            },
            /**
             * 修改按钮事件响应
             * @type {[type]}
             */
            "basegrid[funCode=jobinfo_main] button[ref=gridEdit]": {
                beforeclick: function(btn) {
                    self.doDetail(btn, "edit");
                    return false;
                }
            }
        });
        

    },
    //数据维护操作
    doDetail: function(btn, cmd) {
        var self = this;
        var baseGrid = btn.up("basegrid");
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;
        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });
        var iconCls = "x-fa fa-plus-circle";
        if (cmd=="edit" || cmd=="detail"){
            if (cmd=="edit")
                iconCls = "x-fa fa-pencil-square";
            else
                iconCls = "x-fa fa-pencil-square";

            var rescords = baseGrid.getSelectionModel().getSelection();
            if (rescords.length != 1){
                 self.msgbox("请选择数据");
                 return;
            }
            insertObj = rescords[0].data;
        }
        var winId = detCode + "_win";
        var win = Ext.getCmp(winId);
        if (!win) {
            win = Ext.create('core.base.view.BaseFormWin', {
                id: winId,
                width: 400,
                height: 210,
                resizable:false,
                iconCls: iconCls,
                operType: cmd,
                funData: popFunData,
                funCode: detCode,
                items: [{
                    xtype: detLayout
                }]
            });
        }
        win.show();
        var detailPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetForm = detailPanel.down("baseform[funCode=" + detCode + "]");
        var formDeptObj = objDetForm.getForm();

        //表单赋值
        self.setFormValue(formDeptObj, insertObj);
        //根据操作设置是否只读
        if (cmd == "detail") {
            self.setFuncReadOnly(funData, objDetForm, true);
        }
        //执行回调函数
        if (btn.callback) {
            btn.callback();
        }
    }
});
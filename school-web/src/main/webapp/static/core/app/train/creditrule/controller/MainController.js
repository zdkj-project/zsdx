Ext.define("core.train.creditrule.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.creditrule.mainController',
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
        "basegrid button[ref=gridAdd]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid button[ref=gridDetail]": {
            beforeclick: function(btn) {
                console.log(btn);
                //return false;
            }
        },

        "basegrid[xtype=creditrule.maingrid] button[ref=gridEdit]": {
            beforeclick: function(btn) {
                this.doDetail(btn,"edit");

                return false;
            }
        },

        "basegrid[xtype=creditrule.maingrid] button[ref=gridDelete]": {
            beforeclick: function(btn) {
                var self=this;

                var baseGrid = btn.up("basegrid");
             
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length > 0) {
                    var mark=0;
                    Ext.each(records, function(rec) {                       
                        if(rec.get("startUsing")==1){                            
                            mark=1;
                            return;
                        }
                    });
                    if(mark==1){
                        self.Info("不能删除已启用的规则！");
                        return false;
                    }
                }
                
            }
        },

        "basegrid[xtype=creditrule.maingrid] button[ref=gridStartUsing]": {
            beforeclick: function(btn) {
                var self=this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length > 0) {
                    //封装ids数组
                    Ext.Msg.confirm('提示', '是否启用规则？', function(btn, text) {
                        if (btn == 'yes') {
                            var ids = new Array();
                            Ext.each(records, function(rec) {
                                if(rec.get("startUsing")==0){   //只保存还未启用的规则
                                    var pkValue = rec.get(pkName);
                                    ids.push(pkValue);
                                }
                            });

                            if(ids.length==0){
                                self.Info("没有选择需要启用的规则！");
                                return false;
                            }

                            //发送ajax请求
                            var resObj = self.ajax({
                                url: funData.action + "/doStartUsing",
                                params: {
                                    ids: ids.join(","),
                                    pkName: pkName
                                }
                            });
                            if (resObj.success) {
                                baseGrid.getStore().load();
                                self.msgbox(resObj.obj);
                            } else {
                                self.Error(resObj.obj);
                            }
                        }
                    });
                } else {
                    self.Warning("请选择数据！");
                }
                return false;
            }
        },

        "basegrid[xtype=creditrule.maingrid]  actioncolumn": {
            editClick: function(data) {
                var baseGrid=data.view;
                var record=data.record;

                this.doDetail(null,"edit",baseGrid,record);

                return false;
            },
            detailClick: function(data) {
                console.log(data);

            },
            deleteClick: function(data) {  
                var self=this;
                           
                var record=data.record;
                if(record.get("startUsing")==1){
                    self.Info("已启用的规则，不允许被删除！");
                    return false;
                }
            },
            startUsingClick:function(data) {
                var self=this;
                //得到组件                    
                var baseGrid=data.view;
                var record=data.record;

                if(record.get("startUsing")==1){
                    self.Info("此规则已经启用！");
                    return false;
                }

                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
              
                    
                Ext.Msg.confirm('提示', '是否启用规则？', function(btn, text) {
                    if (btn == 'yes') {                        
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: funData.action + "/doStartUsing",
                            params: {
                                ids: record.get(pkName),
                                pkName: pkName
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();                            

                            self.msgbox(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            },
        }
    },

    doDetail:function(btn,cmd,grid,record){
        
        var self=this;
        var baseGrid;
        var recordData;

        if(btn){
            baseGrid = btn.up("basegrid");
        }else{
            baseGrid=grid;
            recordData=record.data;
        }
       
        //得到模型
        var store = baseGrid.getStore();
        var Model = store.model;
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
        //得到配置信息
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
    
        //关键：window的视图控制器
        var otherController=basePanel.otherController;  
        if(!otherController)
            otherController='';

        //处理特殊默认值
        var defaultObj = funData.defaultObj;
        var insertObj = self.getDefaultValue(defaultObj);

        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        var width = 1000;
        var height = 650;
        if (funData.width)
            width = funData.width;
        if (funData.height)
            height = funData.height;

        var iconCls='x-fa fa-plus-circle';
        var operType=cmd;
        var title="增加";

        switch(cmd){
            case "edit":

                if(btn){
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData=rescords[0].data;
                }
                
                insertObj = recordData;

                iconCls='x-fa fa-pencil-square',
                operType="edit";
                title="编辑";
                break;
            case "detail":

                if(btn){
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择一条数据！");
                        return;
                    }
                    recordData=rescords[0].data;
                }
                
                insertObj = recordData;

                iconCls='x-fa fa-file-text',
                operType="detail";
                title="详情";
                break;
        }

        var win = Ext.create('core.base.view.BaseFormWin', {
            title:title,
            iconCls: iconCls,
            operType: operType,
            width: width,
            height: height,
            controller:otherController,
            funData: popFunData,
            funCode: detCode,
            insertObj:insertObj,
            items: [{
                xtype: detLayout
            }]
        });
        win.show();
        var detPanel = win.down("basepanel[funCode=" + detCode + "]");
        var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");
        var formDeptObj = objDetForm.getForm();
        self.setFormValue(formDeptObj, insertObj);

        //若是编辑，则隐藏启用规则字段
        if(cmd=="edit")
            formDeptObj.findField("startUsing").hide();
        else if(cmd=="detail")
            self.setFuncReadOnly(funData, objDetForm, true);
    }    
});
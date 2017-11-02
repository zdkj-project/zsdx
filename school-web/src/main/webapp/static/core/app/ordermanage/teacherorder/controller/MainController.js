Ext.define("core.ordermanage.teacherorder.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.teacherorder.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function() {
        /*control事件声明代码，可以写在这里
        this.control({

        });
        */
    },
    control: {
        'basepanel[xtype=teacherorder.mainlayout]':{
            afterrender: function (panel, eOpts) {
                var self=this;

                //var objForm = form.up("baseform[xtype=orderdesc.mainform]");

                var loading = new Ext.LoadMask(panel, {
                    msg: '正在读取数据...',
                    removeMask: true// 完成后移除
                });
                loading.show();

                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainTeacherOrderDesc/getOrderDesc",
                    //回调代码必须写在里面
                    success: function (response) {
                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if (data.success) {
                            
                            var orderDescInfo = panel.down("container[ref=orderDescInfo]");
                            orderDescInfo.setData(data.obj);
                          
                        }
                        loading.hide();
                    },failure: function(response) {
                        loading.hide();
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                    }
                });
                
           }
        },

        "basegrid[xtype=teacherorder.maingrid]  actioncolumn": {
            gridSelect: function(data) {
                var self=this;
                var baseGrid = data.view;
                var record = data.record;
                var dinnerGroup = data.value;

                var loading = new Ext.LoadMask(baseGrid, {
                    msg: '正在提交数据...',
                    removeMask: true// 完成后移除
                });
                loading.show();
                var uuid=record.get("uuid");
                var act= Ext.isEmpty(uuid)?"doAdd":"doUpdate";
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainTeacherOrder/"+act,
                    params:{
                        dinnerGroup:dinnerGroup,
                        dinnerDate:record.get("dinnerDate"),
                        uuid:uuid
                    },
                    //回调代码必须写在里面
                    success: function (response) {
                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if (data.success) {
                            self.Info("订餐成功！");
                            record.set("uuid",data.obj.uuid);
                            record.set("dinnerGroup",data.obj.dinnerGroup);
                            record.commit();
                        }else{
                            self.Error(data.obj);
                        }
                        loading.hide();
                    },failure: function(response) {
                        loading.hide();
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                    }
                });


                return false;
            },
            gridCancel: function(data) {
                var self=this;
                var baseGrid = data.view;
                var record = data.record;
                var dinnerGroup = data.value;

                var loading = new Ext.LoadMask(baseGrid, {
                    msg: '正在提交数据...',
                    removeMask: true// 完成后移除
                });
                loading.show();
                var uuid=record.get("uuid");
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainTeacherOrder/doCancel",
                    params:{
                        uuid:uuid
                    },
                    //回调代码必须写在里面
                    success: function (response) {
                        data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if (data.success) {
                            self.Info("取消订餐成功！");
                            record.set("uuid",null);
                            record.set("dinnerGroup",0);
                            record.set("remark",null);
                            record.commit();
                        }else{
                            self.Error(data.obj);
                        }
                        loading.hide();
                    },failure: function(response) {
                        loading.hide();
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);                            
                    }
                });


                return false;
            },
            gridRemark: function(data) {
                var self = this;
                var baseGrid = data.view;
                var record = data.record;
                
                var basePanel = baseGrid.up("basepanel[funCode=teacherorder_main]");            
            
                            
                //关键：window的视图控制器
                var otherController ='teacherorder.otherController';
            
                var insertObj=record.getData();

                var popFunData = Ext.apply(basePanel.funData, {
                    grid: baseGrid
                });

                var width = 500;
                var height = 200;      

                var iconCls = 'x-fa fa-plus-circle';
                var operType = "edit";
                var title = "备注信息";
                        


                var win = Ext.create('core.base.view.BaseFormWin', {
                    title: title,
                    iconCls: iconCls,
                    operType: operType,
                    width: width,
                    height: height,
                    controller: otherController,
                    funData: popFunData,
                    funCode: "teacherorder_detail",    //修改此funCode，方便用于捕获window的确定按钮
                    insertObj: insertObj,
                    record:record,
                    items: [{
                        xtype:'teacherorder.detaillayout',
                        minWidth:200,                
                    }]
                });
                win.show();

                var objDetForm = win.down("baseform[xtype=teacherorder.mainform]");
                var formDeptObj = objDetForm.getForm();

                 self.setFormValue(formDeptObj, insertObj);


                return false
            },
        }
        
       
    }

});

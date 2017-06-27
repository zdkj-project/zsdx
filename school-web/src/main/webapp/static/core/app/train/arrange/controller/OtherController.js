/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.train.arrange.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.arrange.otherController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'

    },
    init: function() {},
    /** 该视图内的组件事件注册 */
    control: {

        
        "grid[ref=arrangeRoomGrid]  button[ref=gridCancelRoom]": {
            beforeclick: function(btn) {        
                var self = this;

                var baseGrid = btn.up("grid[ref=arrangeRoomGrid]");
                var basePanel = baseGrid.up("basepanel[xtype=arrange.detaillayout]");
                var baseWin=basePanel.up("window[funCode=arrange_roomDetail]");

                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length == 0) {
                    self.msgbox("请选择需要取消宿舍的学员！");
                    return false;
                }

                Ext.Msg.confirm('温馨提示', '是否取消这些学员的宿舍？', function(btn, text) {
                    if (btn == 'yes') {                    
                        var ids=[];
                        for(var i in records) {                                           
                            ids.push(records[i].get("uuid"));
                        };

                        //提交设置班级学员的房间信息
                        self.asyncAjax({
                            url: comm.get("baseUrl")  + "/TrainClasstrainee/cancelRoomInfo",
                            params: {                 
                                ids:ids.join(",")
                            },
                            //回调代码必须写在里面
                            success: function(response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                if(data.success){
                                    self.msgbox(data.obj);
                                    //baseGrid.getStore().getProxy().extraParams.filter="[]";
                                    //查询班级的学员信息
                                    self.asyncAjax({
                                        url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassRoomTrainees",
                                        params: {
                                            classId: baseWin.insertObj.uuid,
                                            page:1,
                                            start:0,
                                            limit:-1    //-1表示不分页
                                        },
                                        //回调代码必须写在里面
                                        success: function(response) {
                                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                            var rows=data.rows;
                                            //console.log(rows);
                                            if(rows!=undefined){  //若存在rows数据，则表明请求正常
                                                //获取班级学员列表信息
                                                baseGrid.getStore().loadData(rows);                                          
                                            }else{
                                                self.Error(data.obj);                                  
                                            }
                                        }
                                    });                              
                                }else{
                                    self.Error(data.obj);
                                }
                            }
                        });     
                    }
                });

               


                

                return false;

            }
        },

        

        "window[funCode=arrangeroom_detail] button[ref=formSave]":{
            beforeclick: function(btn) {               
                var self=this;

                var win=btn.up("window[funCode=arrangeroom_detail]");
                var baseGrid=win.down("grid[xtype=room.RoomGrid]");        

                                    
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.msgbox("请选择一间宿舍！");
                    return false;
                }

                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
            
                var roomId=records[0].get(pkName);
                var roomName=records[0].get("roomName");
                var classTraineIds=win.insertObj.ids;
                var xbm=win.insertObj.xbm;
        
                //提交设置班级学员的房间信息
                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClasstrainee/updateRoomInfo",
                    params: {
                        roomId: roomId,
                        roomName:roomName,
                        ids:classTraineIds.join(","),
                        xbm:xbm
                    },
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if(data.success){
                            self.msgbox(data.obj);
                            //baseGrid.getStore().getProxy().extraParams.filter="[]";
                            //查询班级的学员信息
                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassRoomTrainees",
                                params: {
                                    classId: win.insertObj.classId,
                                    page:1,
                                    start:0,
                                    limit:-1    //-1表示不分页
                                },
                                //回调代码必须写在里面
                                success: function(response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    var rows=data.rows;
                                    //console.log(rows);
                                    if(rows!=undefined){  //若存在rows数据，则表明请求正常
                                        //获取班级学员列表信息
                                        win.funData.grid.getStore().loadData(rows);  
                                        win.close();
                                    }else{
                                        self.Error(data.obj);
                                        win.close();
                                    }
                                }
                            });                              
                        }else{
                            self.Error(data.obj);
                        }
                    }
                });                      

                return false;
            }
        },



        "grid[ref=arrangeSiteGrid]  button[ref=gridCancelRoom]": {
            beforeclick: function(btn) {        
                var self = this;

                var baseGrid = btn.up("grid[ref=arrangeSiteGrid]");
                var basePanel = baseGrid.up("basepanel[xtype=arrange.detaillayout]");
                var baseWin=basePanel.up("window[funCode=arrange_siteDetail]");

                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length == 0) {
                    self.msgbox("请选择需要取消场地的课程！");
                    return false;
                }

                Ext.Msg.confirm('温馨提示', '是否取消这些课程的场地？', function(btn, text) {
                    if (btn == 'yes') {                    
                        var ids=[];
                        for(var i in records) {                                           
                            ids.push(records[i].get("uuid"));
                        };
                        
                        //提交设置班级课程的房间信息
                        self.asyncAjax({
                            url: comm.get("baseUrl")  + "/TrainClassschedule/cancelRoomInfo",
                            params: {                 
                                ids:ids.join(",")
                            },
                            //回调代码必须写在里面
                            success: function(response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                if(data.success){
                                    self.msgbox(data.obj);
                                    //baseGrid.getStore().getProxy().extraParams.filter="[]";
                              
                                    baseGrid.getStore().load();

                                }else{
                                    self.Error(data.obj);
                                }
                            }
                        });     
                    }
                });

                       
                return false;

            }
        },

        "grid[ref=arrangeSiteGrid]  button[ref=gridSetRoom]": {
            beforeclick: function(btn) {
                
                var self = this;
            
                var baseGrid = btn.up("grid[ref=arrangeSiteGrid]");
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length == 0) {
                    self.msgbox("请选择需要设置场地的课程！");
                    return false;
                }

                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var basePanel = basetab.down("basepanel[funCode=" + detCode + "]");            
                var objForm = basePanel.down("baseform[funCode=" + detCode + "]");
                var formObj = objForm.getForm();
                var classId = formObj.findField("uuid").getValue();

                var ids=[];            
                for(var i in records) {                  
                    ids.push(records[i].get("uuid"));
                };

                            
                //关键：window的视图控制器
                var otherController ='arrange.otherController';
            
                var insertObj = {
                    classId:classId, //上一个窗口，存放的数据
                    ids:ids
                };

                var popFunData = Ext.apply(basePanel.funData, {
                    grid: baseGrid
                });

                var width = 1200;
                var height = 600;      

                var iconCls = 'x-fa fa-plus-circle';
                var operType = "edit";
                var title = "选择场地";
                        


                var win = Ext.create('core.base.view.BaseFormWin', {
                    title: title,
                    iconCls: iconCls,
                    operType: operType,
                    width: width,
                    height: height,
                    controller: otherController,
                    funData: popFunData,
                    funCode: "arrangesite_detail",    //修改此funCode，方便用于捕获window的确定按钮
                    insertObj: insertObj,
                    items: [{
                        xtype:'room.mainlayout',
                        bodyPadding:5,
                        items: [{
                            collapsible: true,
                            split: true,
                            xtype: "room.areagrid",                      
                            tbar:null,
                            region: "west",
                            style:{
                                border: '1px solid #ddd'
                            },
                            width:450
                        }, {
                            xtype: "room.RoomGrid",                          
                            tbar:null,
                            style:{
                                border: '1px solid #ddd'
                            },
                            selModel: {
                                type: "checkboxmodel",   
                                headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
                                mode:'single', 
                            },
                            region: "center"
                        }]
                    }]
                });
                win.show();
                

                return false;
            }
        },

        "window[funCode=arrangesite_detail] button[ref=formSave]":{
            beforeclick: function(btn) {               
                var self=this;

                var win=btn.up("window[funCode=arrangesite_detail]");
                var baseGrid=win.down("grid[xtype=room.RoomGrid]");        

                                    
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.msgbox("请选择一个场地！");
                    return false;
                }

                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
            
                var roomId=records[0].get(pkName);
                var roomName=records[0].get("roomName");
                var classCourseIds=win.insertObj.ids;              
        
                //提交设置班级学员的房间信息
                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClassschedule/updateRoomInfo",
                    params: {
                        roomId: roomId,
                        roomName:roomName,
                        ids:classCourseIds.join(",")                        
                    },
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if(data.success){
                            self.msgbox(data.obj);
                            //baseGrid.getStore().getProxy().extraParams.filter="[]";
                                                    
                            win.funData.grid.getStore().load();  
                            win.close();
                                                          
                        }else{
                            self.Error(data.obj);
                        }
                    }
                });                      

                return false;
            }
        },
    },

    
});

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
        "baseformtab[detCode=arrange_roomDetail] button[ref=formClose]": {
            beforeclick: function (btn) {
                //得到组件
                var basetab = btn.up('baseformtab');
                var tabPanel = btn.up("tabpanel[xtype=app-main]");

                var grid = basetab.down("grid[ref=arrangeRoomGrid]");

                var roomStore=grid.getStore();
                var len=roomStore.data.length;
                var size=0;
                for(var i=0;i<len;i++){
                    var rec=roomStore.getAt(i);
                    if(rec.get("roomId")==null || rec.get("roomId")=="" )
                        size++;
                }    

                var str="<span style='color:red'>所有学员都已安排宿舍！</span><br/><br/>点击【是】完成操作，点击【否】继续安排！";

                if(size>0)
                    str="<span style='color:red'>存在 "+size+" 个学员未安排住宿！</span><br/><br/>点击【是】完成操作，点击【否】继续安排！";

                Ext.Msg.confirm('温馨提示', str, function(btn, text) {
                    if (btn == 'yes') {   
                        var tabItemId = basetab.tabItemId;
                        var tabItem = tabPanel.getComponent(tabItemId);

                        // var grid = win.funData.grid; //窗体是否有grid参数
                        // if (!Ext.isEmpty(grid)) {
                        //     grid.getStore().load(); //刷新父窗体的grid
                        // }
                        //关闭tab
                        tabPanel.remove(tabItem);   
                    }
                });
                return false;
            }
        },
        "baseformtab[detCode=arrange_siteDetail] button[ref=formClose]": {
            beforeclick: function (btn) {
                //得到组件
                var basetab = btn.up('baseformtab');
                var tabPanel = btn.up("tabpanel[xtype=app-main]");

                var grid = basetab.down("grid[ref=arrangeSiteGrid]");

                var roomStore=grid.getStore();
                var len=roomStore.data.length;
                var size=0;
                for(var i=0;i<len;i++){
                    var rec=roomStore.getAt(i);
                    if(rec.get("roomId")==null || rec.get("roomId")=="" )
                        size++;
                }    

                var str="<span style='color:red'>所有课程都已安排场地！</span><br/><br/>点击【是】完成操作，点击【否】继续安排！";

                if(size>0)
                    str="<span style='color:red'>存在 "+size+" 门课程未安排场地！</span><br/><br/>点击【是】完成操作，点击【否】继续安排！";

                Ext.Msg.confirm('温馨提示', str, function(btn, text) {
                    if (btn == 'yes') {   
                        var tabItemId = basetab.tabItemId;
                        var tabItem = tabPanel.getComponent(tabItemId);

                        // var grid = win.funData.grid; //窗体是否有grid参数
                        // if (!Ext.isEmpty(grid)) {
                        //     grid.getStore().load(); //刷新父窗体的grid
                        // }
                        //关闭tab
                        tabPanel.remove(tabItem);   
                    }
                });

                return false;
            }
        },
        "panel[xtype=arrange.imagesdeailform] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;

                var dicForm = btn.up('panel[xtype=arrange.imagesdeailform]');
                var objForm = dicForm.getForm();
                var params = self.getFormValue(objForm);
                if (objForm.isValid()) {
                    
                    objForm.submit({
                        url: comm.get('baseUrl') + "/BaseAttachment/doUploadImage",
                        waitMsg: '正在上传文件...',
                        timeout : 300,
                        success: function(form, action) {
                            self.Info("上传图片成功！");

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure:function(form, action){
                            if(action.result==undefined){
                                self.Error("上传失败，文件超过限制大小！");
                            }else{
                                self.Error("请求失败，请重试或联系管理员！");
                            }   
                          
                        }
                    });
                } else {
                    self.Error("请按要求填入各项信息！")
                }
                
                return false 
            }
        },


        "panel[xtype=arrange.imagesdeailform] button[ref=formClose]": {
            click: function(btn) {
                var win = btn.up('window');
                //var grid = win.grid;
                //刷新列表
                //grid.getStore().load();
                //关闭窗体
                win.close();
                return false;
            }
        },
        
        "grid[ref=arrangeRoomGrid]  button[ref=gridCancelRoom]": {
            beforeclick: function(btn) {        
                var self = this;

                var baseGrid = btn.up("grid[ref=arrangeRoomGrid]");
                var basePanel = baseGrid.up("basepanel[xtype=arrange.detaillayout]");
                var baseTab= basePanel.up("baseformtab[detCode=arrange_roomDetail]");

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
                                    self.Info(data.obj);
                                    //baseGrid.getStore().getProxy().extraParams.filter="[]";
                                    //查询班级的学员信息
                                    self.asyncAjax({
                                        url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassRoomTrainees",
                                        params: {
                                            classId: baseTab.insertObj.uuid,
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
                var baseGrid=win.down("grid[xtype=pubselect.selectroomgrid]");        

                                    
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
                        roomName: roomName,
                        ids:classTraineIds.join(","),
                        xbm:xbm
                    },
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if(data.success){
                            self.Info(data.obj);
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
                                    self.Info(data.obj);
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
                    if( records[i].get("isDelete")==1){
                        self.Warning("不能给删除状态的课程设置场地！");
                        return false;
                    }    
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
                        xtype:'pubselect.selectroomlayout',
                        items: [{
                            xtype:'pubselect.selectroomgrid',
                            //width:600,
                            flex:1,
                            region: "center",
                            margin:'5',
                            extParams: {
                                whereSql: "",
                                //查询的过滤字段
                                //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
                                filter: "[{\"type\":\"string\",\"comparison\":\"=\",\"value\":\"3\",\"field\":\"roomType\"}]"
                            },
                            columns: {
                                defaults: {
                                    titleAlign: "center"
                                },
                                items: [{
                                    xtype: "rownumberer",
                                    flex: 0,
                                    width: 50,
                                    text: '序号',
                                    align: 'center'
                                }, {
                                    width: 100,
                                    text: "所属楼栋",
                                    dataIndex: "areaUpName"
                                }, {
                                    width: 100,
                                    text: "所属楼层",
                                    dataIndex: "areaName"
                                }, {
                                    flex: 100,
                                    text: "场地名称",
                                    dataIndex: "roomName"
                                }, {
                                    width: 100,
                                    text: "场地类型",
                                    dataIndex: "roomType",
                                    columnType: "basecombobox",
                                    ddCode: "FJLX"
                                }]
                            },
                        }, {
                            xtype: "pubselect.isselectroomgrid",
                            region: "east",
                            width:500,
                            margin:'5',
                            columns: {
                                defaults: {
                                    titleAlign: "center"
                                },
                                items: [{
                                    xtype: "rownumberer",
                                    flex: 0,
                                    width: 50,
                                    text: '序号',
                                    align: 'center'
                                }, {
                                    width: 100,
                                    text: "所属楼栋",
                                    dataIndex: "areaUpName"
                                }, {
                                    width: 100,
                                    text: "所属楼层",
                                    dataIndex: "areaName"
                                }, {
                                    flex: 1,
                                    text: "场地名称",
                                    dataIndex: "roomName"
                                }]
                            },
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
                var baseGrid=win.down("grid[xtype=pubselect.isselectroomgrid]");        
       
                var records = baseGrid.getStore().getData();
                if (records.length ==0) {
                    self.msgbox("请选择场地！");
                    return false;
                }

                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var ids=new Array();
                var roomNames=new Array();
                for(var i=0;i<records.length;i++){
                    ids.push(records.getAt(i).get("uuid"));
                    roomNames.push(records.getAt(i).get("roomName"));
                }

                var classCourseIds=win.insertObj.ids;              
        
                //提交设置班级学员的房间信息
                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClassschedule/updateRoomInfo",
                    params: {
                        roomIds: ids.join(","),
                        roomNames:roomNames.join(","),
                        ids:classCourseIds.join(",")                        
                    },
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if(data.success){
                           
                            //baseGrid.getStore().getProxy().extraParams.filter="[]";
                                                    
                            win.funData.grid.getStore().load();  
                            win.close();
                            self.Info(data.obj);                              
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

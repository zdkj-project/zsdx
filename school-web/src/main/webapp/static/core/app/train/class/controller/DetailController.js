/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.train.class.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.class.detailController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
    init: function() {          /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");     
    },
    /** 该视图内的组件事件注册 */
    control: {
        //快速搜索按按钮
        "grid[ref=traineeFoodGrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                var self=this;

                var baseformtab=btn.up("baseformtab");
                var insertObj=baseformtab.insertObj;
                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;

                var store = btn.up("grid").getStore();

                var girdSearchTexts = toolBar.query("textfield");                        
                var nameValue = girdSearchTexts[0].getValue();                    
                var traineeNumberValue = girdSearchTexts[1].getValue();                  
                           
                //查询班级的就餐信息
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
                    params: {
                        classId: insertObj.uuid,
                        xm: nameValue,
                        traineeNumber: traineeNumberValue,
                        page: 1,
                        start: 0,
                        limit: -1    //-1表示不分页
                    },
                    //回调代码必须写在里面
                    success: function (response) {rows;                     
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        var rows = data.rows;
                        //console.log(rows);
                        if (rows != undefined) {  //若存在rows数据，则表明请求正常
                            //获取班级学员就餐信息                        
                            store.loadData(rows);
                        } else {
                            self.Error(data.obj);
                        }

                    }
                });

                return false;
            }
        },
        //快速搜索文本框回车事件
        "grid[ref=traineeFoodGrid] textfield": {
            specialkey: function (field, e) {
                console.log(11);
                if (e.getKey() == e.ENTER) {

                    var self=this;

                    var baseformtab=field.up("baseformtab");
                    var insertObj=baseformtab.insertObj;
                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;

                    var store = field.up("grid").getStore();

                    var girdSearchTexts = toolBar.query("textfield");                        
                    var nameValue = girdSearchTexts[0].getValue();                    
                    var traineeNumberValue = girdSearchTexts[1].getValue();                  
                               
                    //查询班级的就餐信息
                    self.asyncAjax({
                        url: comm.get("baseUrl") + "/TrainClasstrainee/getClassFoodTrainees",
                        params: {
                            classId: insertObj.uuid,
                            xm: nameValue,
                            traineeNumber: traineeNumberValue,
                            page: 1,
                            start: 0,
                            limit: -1    //-1表示不分页
                        },
                        //回调代码必须写在里面
                        success: function (response) {rows;                     
                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                            var rows = data.rows;
                            //console.log(rows);
                            if (rows != undefined) {  //若存在rows数据，则表明请求正常
                                //获取班级学员就餐信息                        
                                store.loadData(rows);
                            } else {
                                self.Error(data.obj);
                            }

                        }
                    });
                }
                return false;
            }
        },

        //快速搜索按按钮
        "grid[ref=traineeRoomGrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                var self=this;

                var baseformtab=btn.up("baseformtab");
                var insertObj=baseformtab.insertObj;
                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;

                var store = btn.up("grid").getStore();

                var girdSearchTexts = toolBar.query("textfield");                        
                var nameValue = girdSearchTexts[0].getValue();                    
                var traineeNumberValue = girdSearchTexts[1].getValue();                  
                           
                //查询班级的就餐信息
                self.asyncAjax({
                    url: comm.get("baseUrl") + "/TrainClasstrainee/getClassRoomTrainees",
                    params: {
                        classId: insertObj.uuid,
                        xm: nameValue,
                        traineeNumber: traineeNumberValue,
                        page: 1,
                        start: 0,
                        limit: -1    //-1表示不分页
                    },
                    //回调代码必须写在里面
                    success: function (response) {rows;                     
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        var rows = data.rows;
                        //console.log(rows);
                        if (rows != undefined) {  //若存在rows数据，则表明请求正常
                            //获取班级学员就餐信息                        
                            store.loadData(rows);
                        } else {
                            self.Error(data.obj);
                        }

                    }
                });

                return false;
            }
        },
        //快速搜索文本框回车事件
        "grid[ref=traineeRoomGrid] textfield": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {

                    var self=this;

                    var baseformtab=field.up("baseformtab");
                    var insertObj=baseformtab.insertObj;
                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;

                    var store = field.up("grid").getStore();

                    var girdSearchTexts = toolBar.query("textfield");                        
                    var nameValue = girdSearchTexts[0].getValue();                    
                    var traineeNumberValue = girdSearchTexts[1].getValue();                  
                               
                    //查询班级的就餐信息
                    self.asyncAjax({
                        url: comm.get("baseUrl") + "/TrainClasstrainee/getClassRoomTrainees",
                        params: {
                            classId: insertObj.uuid,
                            xm: nameValue,
                            traineeNumber: traineeNumberValue,
                            page: 1,
                            start: 0,
                            limit: -1    //-1表示不分页
                        },
                        //回调代码必须写在里面
                        success: function (response) {rows;                     
                            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                            var rows = data.rows;
                            //console.log(rows);
                            if (rows != undefined) {  //若存在rows数据，则表明请求正常
                                //获取班级学员就餐信息                        
                                store.loadData(rows);
                            } else {
                                self.Error(data.obj);
                            }

                        }
                    });
                }
                return false;
            }
        },

        "basegrid[xtype=class.classcoursegrid]": {
             afterrender: function (grid, eOpts) {
                var btnSetRoom = grid.down("button[ref=gridSetRoom]");
                var btnCancelRoom = grid.down("button[ref=gridCancelRoom]");
                var basetab = grid.up('baseformtab');
                if(basetab.insertObj.isarrange==1){
                    btnSetRoom.setHidden(true);
                    btnCancelRoom.setHidden(true);   
                }     
             },
         },
        /*课程选择场地*/
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridSetRoom]": {
            beforeclick: function(btn) {
                
                var self = this;
            
                var baseGrid = btn.up("grid[xtype=class.classcoursegrid]");
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
                var otherController ='class.otherController';
            
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
                    funCode: "coursesite_detail",    //修改此funCode，方便用于捕获window的确定按钮
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
        //弹出tab中的班级住宿信息提交事件
        "basepanel[xtype=class.classdetaillayout] baseform[xtype=class.roomdetailform] button[ref=submitBtn]":{
            beforeclick:function(btn){
                var self=this;

                Ext.Msg.confirm('温馨提示', '您确定要将此信息保存入库吗？' , function(btn2, text) {
                    if (btn2 == 'yes') {
                        var detPanel = btn.up("basepanel[xtype=class.classdetaillayout]");
                        var baseform=btn.up("baseform[xtype=class.roomdetailform]");
                        var formObj = baseform.getForm();

                        var params = self.getFormValue(formObj);
                        var funData = detPanel.funData;
                        var pkName = funData.pkName;
                        var pkField = formObj.findField(pkName);

                        
                        //处理提交数据
                        var datas=[];
                        var traineeRoomGrid=detPanel.down("grid[ref=traineeRoomGrid]");      
                        var traineeRoomStore=traineeRoomGrid.getStore();

                        var loading = new Ext.LoadMask(traineeRoomGrid,{
                            msg : '正在提交，请稍等...',
                            removeMask : true// 完成后移除
                        });            
                        loading.show();

                        for(var i=0;i<traineeRoomStore.getCount();i++){
                            var rowData=traineeRoomStore.getAt(i).getData();   
                    
                            datas.push({
                                uuid: rowData.uuid,
                                xm: rowData.xm,
                                xbm: rowData.xbm,
                                siesta: rowData.siesta==true?1:0,
                                sleep: rowData.sleep==true?1:0
                            });
                        }
                        params.classRoomInfo=JSON.stringify(datas);


                        self.asyncAjax({
                            url: comm.get("baseUrl")  + "/TrainClass/doEditClassRoom",
                            params: params,
                            //回调代码必须写在里面
                            success: function(response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                if(data.success){  //若存在rows数据，则表明请求正常 
                                    loading.hide();
                                    self.Info("暂存住宿信息成功!");

                                    //刷新外部表格的数据
                                    var basetab = btn.up('baseformtab');
                                    var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                    if (!Ext.isEmpty(grid)) {
                                        var store = grid.getStore();
                                        store.load(); //刷新父窗体的grid
                                    }

                                   
                                }else{
                                    loading.hide();
                                    self.Error(data.obj);                            
                                }
                            },
                            failure: function(response) {
                                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                loading.hide();
                            }
                        }); 
                    }
                });

                return false;
            }
        },

        //弹出tab中的班级订餐信息提交事件
        "basepanel[xtype=class.classdetaillayout] baseform[xtype=class.fooddetailform] button[ref=submitBtn]":{
            beforeclick:function(btn){
                var self=this;

                Ext.Msg.confirm('温馨提示', '您确定要将此信息保存入库吗？' , function(btn2, text) {
                    if (btn2 == 'yes') {
                        var detPanel = btn.up("basepanel[xtype=class.classdetaillayout]");
                        var baseform=btn.up("baseform[xtype=class.fooddetailform]");
                        var formObj = baseform.getForm();

                        var params = self.getFormValue(formObj);
                        var funData = detPanel.funData;
                        var pkName = funData.pkName;
                        var pkField = formObj.findField(pkName);

                        var excuteFunc=function(){
                            //处理提交数据
                            var datas=[];
                            var traineeFoodGrid=detPanel.down("grid[ref=traineeFoodGrid]");      
                            var traineeFoodStore=traineeFoodGrid.getStore();

                            var loading = new Ext.LoadMask(traineeFoodGrid,{
                                msg : '正在提交，请稍等...',
                                removeMask : true// 完成后移除
                            });            
                            loading.show();

                            if(params.dinnerType==3){
                                for(var i=0;i<traineeFoodStore.getCount();i++){
                                    var rowData=traineeFoodStore.getAt(i).getData();   
                                    
                                    var lunch=rowData.lunch==true?1:0;
                                    
                                    // if(params.eatNumber==0&&params.avgNumber==0)    //若这两个参数为0，则自动勾选午餐
                                    //     lunch=1;

                                    datas.push({
                                        uuid: rowData.uuid,
                                        xm: rowData.xm,
                                        breakfast: rowData.breakfast==true?1:0,
                                        lunch: lunch,
                                        dinner: rowData.dinner==true?1:0
                                    });
                                }
                            }

                            params.classFoodInfo=JSON.stringify(datas);
                         
                            self.asyncAjax({
                                url: comm.get("baseUrl")  + "/TrainClass/doEditClassFood",
                                params: params,
                                //回调代码必须写在里面
                                success: function(response) {
                                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                    if(data.success){  //若存在rows数据，则表明请求正常 
                                        loading.hide();
                                        self.Info("暂存就餐信息成功!");

                                        //刷新外部表格的数据
                                        var basetab = btn.up('baseformtab');
                                        var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                        if (!Ext.isEmpty(grid)) {
                                            var store = grid.getStore();
                                            store.load(); //刷新父窗体的grid
                                        }

                                        
                                    }else{
                                         loading.hide();
                                        self.Error(data.obj);
                                       
                                    }
                                },
                                failure: function(response) {
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            }); 
                        }

                        excuteFunc();
                    }
                });
                
                return false;
            }
        },

        //弹出tab中的班级信息表单提交事件
        "basepanel[xtype=class.classdetaillayout] baseform[xtype=class.detailform] button[ref=submitBtn]":{
            beforeclick:function(btn){
                var self=this;


                var detPanel = btn.up("basepanel[xtype=class.classdetaillayout]");
                var baseform=btn.up("baseform[xtype=class.detailform]");
                var formObj = baseform.getForm();

                var params = self.getFormValue(formObj);
                //如果勾选了考勤，则清除规则的参数
                if(params.needChecking==true){
                    if(!params.checkruleId || !params.creditsruleId){
                        self.Warning("请选择考勤规则和学分规则！");
                        return false;
                    }                
                }

                Ext.Msg.confirm('温馨提示', '您确定要将此信息保存入库吗？' , function(btn2, text) {
                    if (btn2 == 'yes') {
                        var funData = detPanel.funData;
                        var pkName = funData.pkName;
                        var pkField = formObj.findField(pkName);
            
                        //把checkbox的值转换为数字
                        params.needChecking=params.needChecking==true?1:0;
                        params.needSynctrainee=params.needSynctrainee==true?1:0;
                        
                        //如果没有勾选考勤，则清除规则的参数
                        if(params.needChecking==0){
                            params.checkruleId="";
                            params.checkruleName="";
                            params.creditsruleId="";
                            params.creditsruleName="";
                        }

                        //判断当前是保存还是修改操作
                        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
                        if (formObj.isValid()) {

                            var loading = new Ext.LoadMask(baseform,{
                               msg : '正在提交，请稍等...',
                               removeMask : true// 完成后移除
                            });            
                            loading.show();

                            self.asyncAjax({
                                url: funData.action + "/" + act,
                                params: params,
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                         loading.hide();
                                        self.Info("暂存班级信息成功!");
                                       
                                        if(act=="doadd"){
                                            //给表单设置初始化数据
                                            self.setFormValue(formObj,data.obj);

                                            //初始化一些数据
                                            var classCourseGrid = detPanel.down("basegrid[xtype=class.classcoursegrid]");
                                            classCourseGrid.getStore().getProxy().extraParams.filter='[{"type":"string","comparison":"=","value":"'+data.obj.uuid+'","field":"classId"}]';

                                            var classStudentGrid = detPanel.down("basegrid[xtype=class.classstudentgrid]");
                                            classStudentGrid.getStore().getProxy().extraParams.filter='[{"type":"string","comparison":"=","value":"'+data.obj.uuid+'","field":"classId"}]';
                                            
                                            var foodDetailForm = detPanel.down("baseform[xtype=class.fooddetailform]");
                                            self.setFormValue(foodDetailForm.getForm(), data.obj);

                                            var roomDetailForm = detPanel.down("baseform[xtype=class.roomdetailform]");
                                            roomDetailForm.getForm().findField("uuid").setValue(data.obj.uuid);

                                        }
                                        
                                        //刷新外部表格的数据
                                        var basetab = btn.up('baseformtab');
                                        var grid = basetab.funData.grid; //此tab是否保存有grid参数
                                        if (!Ext.isEmpty(grid)) {
                                            var store = grid.getStore();
                                            store.load(); //刷新父窗体的grid
                                        }

                                       
                                    } else {
                                        loading.hide();
                                        self.Error(data.obj);
                                        
                                    }
                                },
                                failure: function(response) {
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    loading.hide();
                                }
                            });             
                                        
                        } else {
                            var errors = ["前台验证失败，错误信息："];
                            formObj.getFields().each(function(f) {
                                if (!f.isValid()) {
                                    errors.push("<font color=red>" + f.fieldLabel + "</font>：" + f.getErrors().join(","));
                                }
                            });
                            self.msgbox(errors.join("<br/>"));
                        }
                    }
                });

                return false;
            }
        },

        /*导入excel文件*/
        "basepanel basegrid[xtype=class.coursegrid] button[ref=gridImport]": {
            beforeclick: function(btn) {   
                var self=this;   
                
                var win = btn.up("window");     //若从操作列打开此功能，则使用window中的数据
                var baseGrid = btn.up("basegrid");

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级
                var classId;
                var className;
                var classDetailForm =  Ext.ComponentQuery.query("baseform[xtype=class.detailform]");
                if(classDetailForm.length>0){
                    classId=classDetailForm[0].getForm().findField("uuid").getValue();
                    className=classDetailForm[0].getForm().findField("className").getValue();
                }else{
                    classId = win.insertObj.uuid;
                    className = win.insertObj.className;
                }
                

                if(!classId){
                    self.Warning("信息有误，请选择班级！");
                    return false;
                }

                var win = Ext.create('Ext.Window', {
                    title: "导入Excel文件",
                    iconCls: 'x-fa fa-clipboard',
                    width: 450,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller:'class.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "class.courseimportform"
                    }]
                });
                win.show();

                var objDetForm = win.down("form[xtype=class.courseimportform]");               
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("classId").setValue(classId);
                formDeptObj.findField("className").setValue(className);

                return false;
            }
        },
        

        "basepanel basegrid[xtype=class.coursegrid] button[ref=gridAdd]": {
            beforeclick: function(btn) {                
                var self=this;   

                //得到组件
                var win = btn.up("window");  //若从操作列打开此功能，则使用window中的数据
                var baseGrid = btn.up("basegrid");
                var store = baseGrid.getStore();
                //得到模型
                var Model = store.model;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级
                var classId;
                var className;
                var classDetailForm =  Ext.ComponentQuery.query("baseform[xtype=class.detailform]");
                if(classDetailForm.length>0){
                    classId=classDetailForm[0].getForm().findField("uuid").getValue();
                    className=classDetailForm[0].getForm().findField("className").getValue();
                }else{
                    classId = win.insertObj.uuid;
                    className = win.insertObj.className;
                }
                

                if(!classId){
                    self.Warning("信息有误，请选择班级！");
                    return false;
                }

                //得到配置信息
                var funData = basePanel.funData;
                var detCode = basePanel.detCode;        //这个值换为其他，防止多个window误入other控制器中的同一个事件
                var detLayout = basePanel.detLayout;
                var defaultObj = funData.defaultObj;

                //关键：window的视图控制器
                var otherController=basePanel.otherController;  
                if(!otherController)
                    otherController='';

                var width = 600;
                var height = 300;
                
                //处理特殊默认值
                var insertObj = self.getDefaultValue(defaultObj);
                //填入选择的班级的值
                insertObj = Ext.apply(insertObj, {
                    classId: classId,
                    className: className,

                });

               
                var popFunData = Ext.apply(funData, {
                    grid: baseGrid,
                    filter: "[{'type':'string','comparison':'=','value':'" + insertObj.classId + "','field':'classId'}]"
                });
                var win = Ext.create('core.base.view.BaseFormWin', {
                    iconCls: 'x-fa fa-plus-circle',
                    operType: 'add',
                    width: width,
                    height: height,
                    controller:otherController, //指定视图控制器，从而能够使指定的控制器的事件生效
                    funData: popFunData,
                    funCode: detCode,
                    insertObj:insertObj,
                    items: [{
                        xtype: detLayout,
                        funCode:detCode,    //这里将funcode修改为刚刚的detcode值
                        funData: {
                            action: comm.get("baseUrl") + "/TrainClassschedule", //请求Action
                            whereSql: "", //表格查询条件
                            orderSql: "", //表格排序条件
                            pkName: "uuid",
                            defaultObj: {}
                        },
                        items: [{
                            xtype: "class.coursedetailform",
                            funCode:detCode,    //这里将funcode修改为刚刚的detcode值
                        }]
                    }]
                });
                win.show();
                var detPanel = win.down("basepanel[funCode=" + detCode + "]");
                var objDetForm = detPanel.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();

                self.setFormValue(formDeptObj, insertObj);

                //执行回调函数
                if (btn.callback) {
                    btn.callback();
                }
                return false;
            }
        },


        //班级课程信息 快速搜索按按钮
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridFastSearchBtn]": {
            click:function(btn){             
                this.doFastSearch(btn);

                console.log(1);
                return false;
            }
        },
        //班级课程信息 快速搜索文本框回车事件
        "basepanel basegrid[xtype=class.classcoursegrid] field[funCode=girdFastSearchText]":{
            specialkey: function(field, e){
                if (e.getKey() == e.ENTER) {
                    this.doFastSearch(field);                 
                    return false;
                }
            }
        },
        //班级课程表格的打开关闭评价按钮
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridIsEval]":{
            beforeclick: function(btn){
                var self=this;

                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length != 1) {
                    self.Warning("请选择一条需要操作的课程数据！");
                    return false;
                }

                var record=records[0]; 
                var value=0;
                if(record.get("isEval")==1){
                    var title='是否要关闭评价？';
                    value=0;
                }
                else{
                    var title='是否要开启评价？';
                    value=1;
                }

                Ext.Msg.confirm('提示', title, function(btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                                "/TrainClassschedule/doUpdateEval",
                            params: {
                                id: record.get(pkName),
                                val: value
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();

                            self.Info(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });
                return false;
            }
        },    
        //班级课程表格的删除按钮
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridDelete]":{
            beforeclick: function(btn){
                var self=this;

                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length == 0) {
                    self.Warning("请选择需要删除的数据！");
                    return false;
                }
                            
                Ext.Msg.confirm('提示', '是否删除数据?', function(btn, text) {
                    if (btn == 'yes') {

                        var ids = new Array();
                        Ext.each(records, function (rec) {
                            var pkValue = rec.get(pkName);
                            ids.push(pkValue);
                        });

                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                                "/TrainClassschedule/dodelete",
                            params: {
                                ids: ids.join(","),
                                classId:records[0].get("classId")
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();

                            //baseGrid.getStore().remove(record); //不刷新的方式
                            //baseGrid.getView().refresh();
                            self.Info(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            }
        },
        //班级课程信息 操作列
        "basepanel basegrid[xtype=class.classcoursegrid]  actioncolumn": {
            setEvalClick: function(data) {            
                var self = this;

                var baseGrid = data.view;
                var record = data.record;
                var value = data.value;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;

                if(value==1)
                    var title='是否要开启评价？';
                else
                    var title='是否要关闭评价？';

                Ext.Msg.confirm('提示', title, function(btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                                "/TrainClassschedule/doUpdateEval",
                            params: {
                                id: record.get(pkName),
                                val:value
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();

                            self.Info(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });


            },
            editClick_Tab: function(data) {
                console.log(3);

                return false;
            },
            deleteClick: function(data) {
              
                var self = this;

                var baseGrid = data.view;
                var record = data.record;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;


                Ext.Msg.confirm('提示', '是否删除数据?', function(btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                                "/TrainClassschedule/dodelete",
                            params: {
                                ids: record.get(pkName),
                                classId:record.get("classId")
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();

                            //baseGrid.getStore().remove(record); //不刷新的方式
                            //baseGrid.getView().refresh();
                            self.Info(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            },
        },
        //导入课程信息界面
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridImport]": {
            beforeclick: function(btn) {
                var self = this;

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级

                //得到组件
                var classCourseGrid = btn.up("basegrid[xtype=class.classcoursegrid]");

                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var objForm = basetab.down("baseform[funCode=" + detCode + "]");
                var formObj = objForm.getForm();

                var classId = formObj.findField("uuid").getValue();
                var className = formObj.findField("className").getValue();

                if (!classId) {
                    self.Warning("请先暂存班级信息！");
                    return false;
                }

                var win = Ext.create('Ext.Window', {
                    title: "导入班级课程",
                    iconCls: 'x-fa fa-clipboard',
                    width: 450,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'class.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: classCourseGrid,
                    items: [{
                        xtype: "class.courseimportform"
                    }]
                });
                win.show();

                var objDetForm = win.down(
                    "form[xtype=class.courseimportform]");
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("classId").setValue(classId);
                formDeptObj.findField("className").setValue(
                    className);

                return false;
            }
        },
        //添加班级课程界面
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridAdd_Tab]":{
            beforeclick:function(btn){
                this.doAddCourseDetail_Tab(btn, "add", null, null);
                return false;
            }
        },
        //编辑班级课程界面
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridEdit_Tab]":{
            beforeclick:function(btn){
                this.doAddCourseDetail_Tab(btn, "edit", null, null);
                return false;
            }
        },
        "basepanel basegrid[xtype=class.classcoursegrid] button[ref=gridDownTemplate]": {
            beforeclick: function(btn) {        
                var self = this;
                var title = "下载班级课程导入模板";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        //window.location.href = comm.get('baseUrl') + "/static/upload/template/classCourse.xls";    
                        window.open(comm.get('baseUrl') + "/static/upload/template/classCourse.xls");                  
                    }
                });
                return false;
            }
        },

        //快速搜索按按钮
        "basepanel basegrid[xtype=class.classstudentgrid] button[ref=gridFastSearchBtn]": {
            click:function(btn){             
                this.doFastSearch(btn);
                return false;
            }
        },
        //快速搜索文本框回车事件
        "basepanel basegrid[xtype=class.classstudentgrid] field[funCode=girdFastSearchText]":{
            specialkey: function(field, e){
                if (e.getKey() == e.ENTER) {
                    this.doFastSearch(field);                
                    return false;
                }
            }
        },
        "basepanel basegrid[xtype=class.classstudentgrid] button[ref=gridDelete]":{
            beforeclick: function(btn){
                var self=this;

                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                if (records.length == 0) {
                    self.Warning("请选择需要删除的数据！");
                    return false;
                }
                            
    

                Ext.Msg.confirm('提示', '是否删除数据?', function(btn, text) {
                    if (btn == 'yes') {

                        var ids = new Array();
                        Ext.each(records, function (rec) {
                            var pkValue = rec.get(pkName);
                            ids.push(pkValue);
                        });

                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                                "/TrainClasstrainee/dodelete",
                            params: {
                                ids: ids.join(","),
                                classId:records[0].get("classId")
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();

                            //baseGrid.getStore().remove(record); //不刷新的方式
                            //baseGrid.getView().refresh();
                            self.Info(resObj.obj);

                            //同时刷新住宿和就餐的人员名单。
                            var basetab = baseGrid.up('baseformtab');
                            var funCode = basetab.funCode;      //mainLayout的funcode
                            var detCode = basetab.detCode;      //detailLayout的funcode

                            var objForm = basetab.down("baseform[funCode=" + detCode + "]");
                            var formObj = objForm.getForm();
                            var classId = formObj.findField("uuid").getValue();
                            
                            //查询班级的就餐学员信息
                            var traineeFoodGrid=basetab.down("grid[ref=traineeFoodGrid]");
                            if(traineeFoodGrid){
                                self.asyncAjax({
                                    url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassFoodTrainees",
                                    params: {
                                        classId: classId,
                                        page:1,
                                        start:0,
                                        limit:-1    //-1表示不分页
                                    },
                                    //回调代码必须写在里面
                                    success: function(response) {
                                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                        var rows=data.rows;
                                        //console.log(rows);
                                        if(rows!=undefined){ //若存在rows数据，则表明请求正常
                                          
                                            traineeFoodGrid.getStore().loadData(rows);

                                        }else{
                                            self.Error(data.obj);
                                        }
                                    }
                                });    
                            }

                            //查询班级的住宿学员信息
                            var traineeRoomGrid=basetab.down("grid[ref=traineeRoomGrid]");                        
                            if(traineeRoomGrid){
                                self.asyncAjax({
                                    url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassRoomTrainees",
                                    params: {
                                        classId:classId,
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
                                
                                            traineeRoomGrid.getStore().loadData(rows);

                                        }else{
                                            self.Error(data.obj);
                                        }
                                    }
                                });  

                            }
                            
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            }
        },
        //班级学员信息 操作列
        "basepanel basegrid[xtype=class.classstudentgrid]  actioncolumn": {            
            deleteClick: function(data) {
                var self = this;

                var baseGrid = data.view;
                var record = data.record;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;


                Ext.Msg.confirm('提示', '是否删除数据?', function(btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                                "/TrainClasstrainee/dodelete",
                            params: {
                                ids: record.get(pkName),
                                classId:record.get("classId")
                            }
                        });
                        if (resObj.success) {
                            
                            baseGrid.getStore().load();

                            //baseGrid.getStore().remove(record); //不刷新的方式
                            //baseGrid.getView().refresh();
                            self.Info(resObj.obj);

                            //同时刷新住宿和就餐的人员名单。
                            var basetab = baseGrid.up('baseformtab');
                            var funCode = basetab.funCode;      //mainLayout的funcode
                            var detCode = basetab.detCode;      //detailLayout的funcode

                            var objForm = basetab.down("baseform[funCode=" + detCode + "]");
                            var formObj = objForm.getForm();
                            var classId = formObj.findField("uuid").getValue();
                            
                            //查询班级的就餐学员信息
                            var traineeFoodGrid=basetab.down("grid[ref=traineeFoodGrid]");
                            if(traineeFoodGrid){
                                self.asyncAjax({
                                    url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassFoodTrainees",
                                    params: {
                                        classId: classId,
                                        page:1,
                                        start:0,
                                        limit:-1    //-1表示不分页
                                    },
                                    //回调代码必须写在里面
                                    success: function(response) {
                                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                        var rows=data.rows;
                                        //console.log(rows);
                                        if(rows!=undefined){ //若存在rows数据，则表明请求正常
                                          
                                            traineeFoodGrid.getStore().loadData(rows);

                                        }else{
                                            self.Error(data.obj);
                                        }
                                    }
                                });    
                            }

                            //查询班级的住宿学员信息
                            var traineeRoomGrid=basetab.down("grid[ref=traineeRoomGrid]");                        
                            if(traineeRoomGrid){
                                self.asyncAjax({
                                    url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassRoomTrainees",
                                    params: {
                                        classId:classId,
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
                                
                                            traineeRoomGrid.getStore().loadData(rows);

                                        }else{
                                            self.Error(data.obj);
                                        }
                                    }
                                });  

                            }
                            
                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });

                return false;
            },
        },
        //导入班级学员界面
        "basepanel basegrid[xtype=class.classstudentgrid]  button[ref=gridImport]": {
            beforeclick: function(btn) {
                
                var self = this;
                //判断是否选择了班级，判断是添加新班级 或是 编辑班级
            
                //得到组件
                var classStudentGrid = btn.up("basegrid[xtype=class.classstudentgrid]");

                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var objForm = basetab.down("baseform[funCode=" + detCode + "]");
                var formObj = objForm.getForm();
                
                var classId = formObj.findField("uuid").getValue();
                var className = formObj.findField("className").getValue();

                if (!classId) {
                    self.Warning("请先暂存班级信息！");
                    return false;
                }

                var win = Ext.create('Ext.Window', {
                    title: "导入班级学员",
                    iconCls: 'x-fa fa-clipboard',
                    width: 450,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'class.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: classStudentGrid,
                    items: [{
                        xtype: "class.traineeimportform"
                    }]
                });
                win.show();

                var objDetForm = win.down(
                    "form[xtype=class.traineeimportform]");
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("classId").setValue(classId);
                formDeptObj.findField("className").setValue(
                    className);
                
                return false;
            }
        },
        "basepanel basegrid[xtype=class.classstudentgrid] button[ref=gridDownTemplate]": {
            beforeclick: function(btn) {        
                var self = this;
                var title = "下载班级学员导入模板";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        //window.location.href = comm.get('baseUrl') + "/static/upload/template/trainee.xls";    
                        window.open(comm.get('baseUrl') + "/static/upload/template/classTrainee.xls");                  
                    }
                });
                return false;
            }
        },
        //班级学员同步到学员库
        "basepanel basegrid[xtype=class.classstudentgrid]  button[ref=gridSyncTrainee]": {
            beforeclick: function(btn) {
                
                var self = this;
                //判断是否选择了班级，判断是添加新班级 或是 编辑班级
            
                //得到组件
                var classStudentGrid = btn.up("basegrid[xtype=class.classstudentgrid]");

                var basetab = btn.up('baseformtab');
                var funCode = basetab.funCode;      //mainLayout的funcode
                var detCode = basetab.detCode;      //detailLayout的funcode

                var objForm = basetab.down("baseform[funCode=" + detCode + "]");
                var formObj = objForm.getForm();
                
                var classId = formObj.findField("uuid").getValue();

                if (!classId) {
                    self.Warning("请先暂存班级信息！");
                    return false;
                }

                Ext.Msg.confirm('提示', '是否将班级学员信息同步到学员库?', function(btn, text) {
                    if (btn == 'yes') {
                        self.asyncAjax({
                            url: comm.get("baseUrl")  + "/TrainClasstrainee/syncClassTrainee",
                            params: {
                                classId:classId
                            },
                            //回调代码必须写在里面
                            success: function(response) {
                                var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                                if(data.success){  
                                    self.Info(data.obj);
                                }else{
                                    self.Error(data.obj);
                                }
                            },
                            failure: function(response) {
                                Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                loading.hide();
                            }
                        }); 
                   
                   }
               });

              
                return false;
            }
        },
        
    },

    //快速搜索
    doFastSearch:function(component){
        //得到组件 
        var baseGrid = component.up("basegrid"); 
        if(!baseGrid)
            return false;

        var toolBar= component.up("toolbar");
        if(!toolBar)
            return false;

        var detPanel = baseGrid.up("basepanel[funCode=" +  baseGrid.funCode + "]");
        var funData = detPanel.funData;
        var pkValue = detPanel.down("field[name='"+funData.pkName+"']").getValue();

        var filter= [{'type':'string','comparison':'=','value':pkValue,'field':'classId'}];
        var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
        for(var i in girdSearchTexts){
            var name = girdSearchTexts[i].getName();
            var value = girdSearchTexts[i].getValue();

            filter.push({"type":"string","value":value,"field":name,"comparison":""});
        }


        var store = baseGrid.getStore();
        var proxy = store.getProxy();                        
        proxy.extraParams.filter = JSON.stringify(filter);
        store.loadPage(1);
    },

    doAddCourseDetail_Tab:function(btn, cmd, grid, record) {

        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.data;
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var basePanel = baseGrid.up("basepanel[funCode=" + funCode +"]");
        var tabPanel=baseGrid.up("tabpanel[xtype=app-main]");

      
        var objForm = basePanel.down("baseform[funCode=" + basePanel.detCode + "]");
        var formObj = objForm.getForm();

        var classId = formObj.findField("uuid").getValue();
        var className = formObj.findField("className").getValue();

        if (!classId) {
            self.Warning("请先暂存班级信息！");
            return false;
        }

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = "class_addCourseDetail";   //修改此funCode，方便用于捕获window的确定按钮
        var detLayout = "class.coursedetaillayout";
        var defaultObj = funData.defaultObj;
            
        //关键：window的视图控制器
        var otherController = "class.otherController";

        //处理特殊默认值
        var insertObj = { classId:classId,className:className };
        var popFunData = Ext.apply(funData, {
            grid: baseGrid
        });

        //根据cmd操作类型，来设置不同的值
        var tabTitle = "添加班级课程"; 
        //设置tab页的itemId
        var tabItemId=funCode+"_gridClassCourseAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var pkValue= null;
        var operType=cmd;

        switch (cmd) {
            case "edit":

                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "编辑班级课程";
                tabItemId=funCode+"_gridClassCourseEdit"; 

                //获取主键值
                var pkName = funData.pkName;
                pkValue= recordData[pkName];

                break;
            case "detail":                
                if (btn) {
                    var rescords = baseGrid.getSelectionModel().getSelection();
                    if (rescords.length != 1) {
                        self.msgbox("请选择1条数据！");
                        return;
                    }
                    recordData = rescords[0].data;
                }

                insertObj = recordData;
                tabTitle = "班级课程详情";
                tabItemId=funCode+"_gridClassCourseDetail"+insertObj.classNumb; 
                break;
        }

        //获取tabItem；若不存在，则表示要新建tab页，否则直接打开
        var tabItem=tabPanel.getComponent(tabItemId);
        if(!tabItem){
    
            tabItem=Ext.create({
                xtype:'container',
                title: tabTitle,
                //iconCls: 'x-fa fa-clipboard',
                scrollable :true, 
                itemId:tabItemId,
                itemPKV:pkValue,      //保存主键值
                layout:'fit', 
            });
            tabPanel.add(tabItem); 

            //延迟放入到tab中
            setTimeout(function(){
                //创建组件
                var item=Ext.widget("baseformtab",{
                    operType:operType,                            
                    controller:otherController,         //指定重写事件的控制器
                    funCode:funCode,                    //指定mainLayout的funcode
                    detCode:detCode,                    //指定detailLayout的funcode
                    tabItemId:tabItemId,                //指定tab页的itemId
                    insertObj:insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items:[{
                        xtype:detLayout,
                        funCode: detCode, 
                        funData: {
                            action: comm.get("baseUrl") + "/TrainClassschedule", //请求Action
                            whereSql: "", //表格查询条件
                            orderSql: "", //表格排序条件
                            pkName: "uuid",
                            defaultObj: {}
                        },
                        items: [{
                            xtype: "class.coursedetailform",
                            //funCode:detCode,    //这里将funcode修改为刚刚的detcode值
                        }]
                    }]
                }); 
                tabItem.add(item);  

                var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                var formDeptObj = objDetForm.getForm();
                self.setFormValue(formDeptObj, insertObj);
            
                if(insertObj.beginTime){
                    var beginTimeDate=new Date(insertObj.beginTime);
                    var startDate=Ext.Date.dateFormat(beginTimeDate, 'Y-m-d H:i');
                    startDate=startDate.split(" ");
                    formDeptObj.findField("courseDate").setValue(beginTimeDate);
                    formDeptObj.findField("courseBeginTime").setValue(startDate[1]);
                }
                if(insertObj.endTime){
                    var endTimeDate=new Date(insertObj.endTime);
                    var endDate=Ext.Date.dateFormat(endTimeDate, 'Y-m-d H:i');
                    endDate=endDate.split(" ");
                    formDeptObj.findField("courseDate").setValue(endTimeDate);
                    formDeptObj.findField("courseEndTime").setValue(endDate[1]);
                }
            },30);
                           
        }else if(tabItem.itemPKV&&tabItem.itemPKV!=pkValue){     //判断是否点击的是同一条数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab( tabItem);

    },
});
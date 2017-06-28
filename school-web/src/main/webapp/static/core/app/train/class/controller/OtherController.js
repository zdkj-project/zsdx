/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，用于注册window之类的组件的事件，该类组件不属于 mainLayout和detailLayout范围内。
    但需要在创建window中，使用controller属性来指定此视图控制器，才可生效
*/
Ext.define("core.train.class.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.class.otherController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        
    },
    init: function() {
    },
    /** 该视图内的组件事件注册 */
    control: {
        //弹出tab中的班级就餐信息提交事件
        "baseformtab[detCode=class_foodDetail] button[ref=formSave]":{
            beforeclick:function(btn){
                var self=this;

                //得到组件
                var basetab = btn.up('baseformtab');
                var tabPanel=btn.up("tabpanel[xtype=app-main]");

                var tabItemId=basetab.tabItemId;   
                var tabItem=tabPanel.getComponent(tabItemId);

                var detPanel = basetab.down("basepanel[funCode=" + basetab.detCode + "]");
                var baseform = detPanel.down("baseform[funCode=" + basetab.detCode + "]");
                var formObj = baseform.getForm();

                var params = self.getFormValue(formObj);
                var funData = detPanel.funData;
                var pkName = funData.pkName;
                var pkField = formObj.findField(pkName);

                
            
                //处理提交数据
                var datas=[];
                var traineeFoodGrid=detPanel.down("grid[ref=traineeFoodGrid]");      
                var traineeFoodStore=traineeFoodGrid.getStore();

                var loading = new Ext.LoadMask(basetab,{
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

                            self.msgbox("编辑就餐信息成功!");
                            loading.hide();
                          
                            var grid = basetab.funData.grid; //窗体是否有grid参数
                            if (!Ext.isEmpty(grid)) {
                                grid.getStore().load(); //刷新父窗体的grid
                            }  
                            //关闭tab
                            tabPanel.remove(tabItem);  

                        }else{
                            self.Error(data.obj);
                            loading.hide();
                        }
                    },
                    failure: function(response) {
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                        loading.hide();
                    }
                }); 

                return false;
            }
        },
        //弹出tab中的班级住宿信息提交事件
        "baseformtab[detCode=class_roomDetail] button[ref=formSave]":{
            beforeclick:function(btn){
                var self=this;

                //得到组件
                var basetab = btn.up('baseformtab');
                var tabPanel=btn.up("tabpanel[xtype=app-main]");

                var tabItemId=basetab.tabItemId;   
                var tabItem=tabPanel.getComponent(tabItemId);

                var detPanel = basetab.down("basepanel[funCode=" + basetab.detCode + "]");
                var baseform = detPanel.down("baseform[funCode=" + basetab.detCode + "]");
                var formObj = baseform.getForm();

                var params = self.getFormValue(formObj);
                var funData = detPanel.funData;
                var pkName = funData.pkName;
                var pkField = formObj.findField(pkName);

                
                //处理提交数据
                var datas=[];
                var traineeRoomGrid=detPanel.down("grid[ref=traineeRoomGrid]");      
                var traineeRoomStore=traineeRoomGrid.getStore();

                var loading = new Ext.LoadMask(basetab,{
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
                            self.msgbox("编辑住宿信息成功!");

                            loading.hide();

                            var grid = basetab.funData.grid; //窗体是否有grid参数
                            if (!Ext.isEmpty(grid)) {
                                grid.getStore().load(); //刷新父窗体的grid
                            }  
                            
                            //关闭tab
                            tabPanel.remove(tabItem);  
                        }else{
                            self.Error(data.obj);
                            loading.hide();
                        }
                    },
                    failure: function(response) {
                        Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                        loading.hide();
                    }
                }); 

                return false;
            }
        },

        "baseformtab[funCode=class_main] button[ref=formSave]": {
            beforeclick: function(btn) {   
                //得到组件
                var basetab = btn.up('baseformtab');
                var tabPanel=btn.up("tabpanel[xtype=app-main]");

                var tabItemId=basetab.tabItemId;   
                var tabItem=tabPanel.getComponent(tabItemId);

                var grid = basetab.funData.grid; //窗体是否有grid参数
                if (!Ext.isEmpty(grid)) {
                    grid.getStore().load(); //刷新父窗体的grid
                }        

                //关闭tab
                tabPanel.remove(tabItem);
                return false;
            },
        },

        /*导入基础学员*/
        "basegrid[xtype=class.studentgrid] button[ref=gridTraineeImport]": {
            beforeclick: function(btn) {       
                var self=this;

                //判断是否选择了班级，判断是添加新班级 或是 编辑班级

                //得到组件
                var baseGrid = btn.up("basegrid");
                            
                var win = Ext.create('Ext.Window', {
                    title: "导入基础学员数据",
                    iconCls: 'x-fa fa-clipboard',
                    width: 400,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller:'class.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "class.basetraineeimportform"
                    }]
                });
                win.show();
/*
                var objDetForm = win.down("form[xtype=trainee.traineeimportform]");               
                var formDeptObj = objDetForm.getForm();
                formDeptObj.findField("classId").setValue(classId);
                formDeptObj.findField("className").setValue(className);
*/
                return false;
            }
        },
        "panel[xtype=class.basetraineeimportform] button[ref=formClose]": {
            beforeclick: function(btn) {
                var win = btn.up('window');                         
                //关闭窗体
                win.close();
                return false;
            }
        },
        
        "panel[xtype=class.basetraineeimportform] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;
                var dicForm = btn.up('panel[xtype=class.basetraineeimportform]');
                var objForm = dicForm.getForm();
                if (objForm.isValid()) {
                    objForm.submit({
                        url: comm.get('baseUrl') + "/TrainTrainee/importData",
                        waitMsg: '正在导入文件...',
                        success: function(form, action) {
                            self.msgbox("导入成功！");

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure:function(form, action){
                            if(action.result==undefined){
                                self.Error("文件导入失败，文件有误或超过限制大小！");
                            }else{
                                self.Error(action.result.obj);
                            }
                          
                        }
                    });
                } else {
                    self.Error("请选择要上传Excel文件！")
                }
                
                return false 
            }
        },


        "baseformwin[funCode=class_detail] button[ref=formContinue]": {
            beforeclick: function(btn) {
                this.doSave(btn, "saveContinue");
                return false;
            }
        },

        "baseformwin[funCode=class_detail] button[ref=formSave]": {
            beforeclick: function(btn) {
                this.doSave(btn, "save");

                return false;
            }
        },

        "baseformwin[funCode=class_footDetail] button[ref=formSave]": {
            beforeclick: function(btn) {
                this.doClassFoodSave(btn, "save");
                return false;
            }
        },

        "baseformwin[funCode=class_roomDetail] button[ref=formSave]": {
            beforeclick: function(btn) {
                this.doClassRoomSave(btn, "save");
                return false;
            }
        },
        

        "baseformwin button[ref=formClose]": {
            beforeclick: function(btn) {
                console.log(btn);
            }
        },
        //选择课程确定按钮
        "window[funcPanel=class.coursemainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {
                
                var win=btn.up("window[funcPanel=class.coursemainlayout]");              
                var baseGrid=win.down("basegrid");
                var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                
                //var dataField=win.dataField;
                var store=baseGrid.getStore();
                var nameArray=new Array();
                var idArray=new Array();               

                for(var i=0;i<store.getCount();i++){
                    if(idArray.indexOf(store.getAt(i).get("uuid"))==-1){
                        nameArray.push(store.getAt(i).get("courseName"));
                        idArray.push(store.getAt(i).get("uuid"));                          
                    }                        
                }
                            
                formpanel.getForm().findField("courseId").setValue(idArray.join(","));;
                formpanel.getForm().findField("courseName").setValue(nameArray.join(","));  
                            
                //baseGrid.getStore().getProxy().extraParams.filter="[{'type':'string','comparison':'','value':'学生','field':'jobName'}]";                
                win.close();
                return false;
            }
        },
        "window[funcPanel=class.selectstudent.mainlayout]":{
            afterrender:function(win){
                var grid=win.down("grid[ref=selectedStudentGrid]")
                var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                var traineeNameStr=formpanel.getForm().findField("traineeName").getValue();
                var traineeIdStr=formpanel.getForm().findField("traineeId").getValue();
                var traineeXbmStr=formpanel.getForm().findField("traineeXbm").getValue();
                var classTraineeIdStr=formpanel.getForm().findField("classTraineeId").getValue();
                
                var datas=[];
                if(traineeNameStr.trim()!=""){
                    var traineeNames=traineeNameStr.split(",");
                    var traineeIds=traineeIdStr.split(",");                
                    var traineeXbms=traineeXbmStr.split(","); 
                    var classTraineeIds=classTraineeIdStr.split(","); 

                    for(var i=0;i<traineeNames.length;i++){
                        var obj={};
                        obj.xm=traineeNames[i];
                        obj.uuid=traineeIds[i];
                        obj.xbm=traineeXbms[i];
                        obj.classTraineeId=classTraineeIds[i];
                        datas.push(obj);
                    }
                }
                if(datas.length>0){                       
                    grid.getStore().loadData(datas);
                }
                else{
                    grid.getStore().removeAll();
                }
                return false;
            }
        },
        "window[funcPanel=class.selectstudent.mainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {
                
                var win=btn.up("window[funcPanel=class.selectstudent.mainlayout]");
                var grid=win.down("grid[ref=selectedStudentGrid]");
                var baseGrid=win.down("basegrid");
                var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                
                //var dataField=win.dataField;
                var store=grid.getStore();
                var nameArray=new Array();
                var idArray=new Array();
                var xbmArray=new Array();
                var phoneArray=new Array();
                var classTraineeIdArray=new Array();
                var sfzjhArray=new Array();

                for(var i=0;i<store.getCount();i++){
                    if(idArray.indexOf(store.getAt(i).get("uuid"))==-1||store.getAt(i).get("uuid")=="null"){
                        nameArray.push(store.getAt(i).get("xm"));
                        idArray.push(store.getAt(i).get("uuid")?store.getAt(i).get("uuid"):" ");  //为空的数据，要使用一个空格号隔开，否则后台split分割有误
                        xbmArray.push(store.getAt(i).get("xbm")?store.getAt(i).get("xbm"):" ");
                        phoneArray.push(store.getAt(i).get("mobilePhone")?store.getAt(i).get("mobilePhone"):" ");
                        classTraineeIdArray.push(store.getAt(i).get("classTraineeId")?store.getAt(i).get("classTraineeId"):" "); //若此时不为null，则表明为旧的学员
                        sfzjhArray.push(store.getAt(i).get("sfzjh")?store.getAt(i).get("sfzjh"):" ");
                    }                        
                }
                            
                formpanel.getForm().findField("traineeId").setValue(idArray.join(","));;
                formpanel.getForm().findField("traineeName").setValue(nameArray.join(","));  
                formpanel.getForm().findField("traineeXbm").setValue(xbmArray.join(","));                     
                formpanel.getForm().findField("traineePhone").setValue(phoneArray.join(","));   
                formpanel.getForm().findField("classTraineeId").setValue(classTraineeIdArray.join(","));                   
                formpanel.getForm().findField("traineeSfzjh").setValue(sfzjhArray.join(","));         

                //baseGrid.getStore().getProxy().extraParams.filter="[{'type':'string','comparison':'','value':'学生','field':'jobName'}]";
                baseGrid.getStore().getProxy().extraParams.filter="[]";
                win.close();
                return false;
            }
        },


        //选择班主任，界面加载事件
        "window[funcPanel=class.selectbzr.mainlayout]":{
            afterrender:function(win){

                var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem=tabPanel.getActiveTab();
                var formpanel=tabItem.down('form[xtype=' + win.formPanel + ']');
                //var formpanel = Ext.ComponentQuery.query('form[xtype=' + win.formPanel + ']')[0];
                var classId = formpanel.getForm().findField("uuid").getValue();
                var grid = win.down("grid[ref=isselectedbzrgrid]");
                var store = grid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    classId: classId
                };
                store.load();
                return false;
            }
        },
        //选择班主任，勾选后提交事件
        "window[funcPanel=class.selectbzr.mainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {
                
                var win=btn.up("window[funcPanel=class.selectbzr.mainlayout]");
                var grid=win.down("grid[ref=isselectedbzrgrid]");
                var baseGrid=win.down("basegrid");

                var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem=tabPanel.getActiveTab();
                var formpanel=tabItem.down('form[xtype=' + win.formPanel + ']');
                //var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                
                //var dataField=win.dataField;
                var store=grid.getStore();
                var nameArray=new Array();
                var idArray=new Array();                            

                for(var i=0;i<store.getCount();i++){
                    if(idArray.indexOf(store.getAt(i).get("uuid"))==-1||store.getAt(i).get("uuid")=="null"){
                        nameArray.push(store.getAt(i).get("xm"));
                        idArray.push(store.getAt(i).get("uuid")?store.getAt(i).get("uuid"):" ");  //为空的数据，要使用一个空格号隔开，否则后台split分割有误                        
                    }                        
                }
                            
                formpanel.getForm().findField("bzrId").setValue(idArray.join(","));;
                formpanel.getForm().findField("bzrName").setValue(nameArray.join(","));                  

                //baseGrid.getStore().getProxy().extraParams.filter="[{'type':'string','comparison':'','value':'学生','field':'jobName'}]";
                baseGrid.getStore().getProxy().extraParams.filter="[]";
                win.close();
                return false;
            }
        },


        //添加班级课程的 讲师加载h事件
        "window[funcPanel=class.selectteacher.mainlayout]":{
            afterrender:function(win){
                
                var grid=win.down("grid[ref=isselectedteachergrid]");
               
                var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem=tabPanel.getActiveTab();
                var formpanel=tabItem.down('form[xtype=' + win.formPanel + ']');
                //var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                var formObj=formpanel.getForm();

                var mainTeacherNameStr=formObj.findField("mainTeacherName").getValue();
                var mainTeacherIdStr=formObj.findField("mainTeacherId").getValue();
                var mainTeacherXbmStr=formObj.findField("mainTeacherXbm").getValue();
                var mainTeacherWorkStr=formObj.findField("mainTeacherWork").getValue();
                
                var datas=[];
                if(mainTeacherNameStr.trim()!=""){
                    var mainTeacherNames=mainTeacherNameStr.split(",");
                    var mainTeacherIds=mainTeacherIdStr.split(",");                
                    var mainTeacherXbms=mainTeacherXbmStr.split(","); 
                    var mainTeacherWorks=mainTeacherWorkStr.split(","); 

                    for(var i=0;i<mainTeacherNames.length;i++){
                        var obj={};
                        obj.xm=mainTeacherNames[i];
                        obj.uuid=mainTeacherIds[i];
                        obj.xbm=mainTeacherXbms[i];
                        obj.workUnits=mainTeacherWorks[i];
                        datas.push(obj);
                    }
                }
                if(datas.length>0){                       
                    grid.getStore().loadData(datas);
                }
                else{
                    grid.getStore().removeAll();
                }
                return false;
            }
        },
        //添加班级课程的 选择讲师，勾选后提交事件
        "window[funcPanel=class.selectteacher.mainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {
                
                var win=btn.up("window[funcPanel=class.selectteacher.mainlayout]");
                var grid=win.down("grid[ref=isselectedteachergrid]");
                var baseGrid=win.down("basegrid");

                var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                var tabItem=tabPanel.getActiveTab();
                var formpanel=tabItem.down('form[xtype=' + win.formPanel + ']');
                //var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
             
                //标准的赋值到表单方式
                var store=grid.getStore();
                var dataField=win.dataField;   
                var gridField=win.gridField;   
                var formObj=formpanel.getForm();
                var data= store.getData().items;
                Ext.each(dataField,function(f,index){                    
                    var valueArray=new Array();
                    Ext.each(data,function(r){
                        valueArray.push(r.get(gridField[index]));
                    });
                    
                    var bff=formObj.findField(f);
                    if(bff){
                        bff.setValue(valueArray.join(","));
                    }
                    
                });

                baseGrid.getStore().getProxy().extraParams.filter="[]";
                win.close();
                return false;
            }
        },

        "window[funcPanel=class.selectstudent.mainlayout] button[ref=ssCancelBtn]":{
            beforeclick: function(btn) {  
                //清除上次选择
                var win=btn.up("window[funcPanel=class.selectstudent.mainlayout]");
                var baseGrid=win.down("basegrid");
                baseGrid.getStore().getProxy().extraParams.filter="[]";
                win.close();
                return false;
             }
        },

        //课程选择确定事件
        "window[funcPanel=course.mainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {      
                    
                var win=btn.up("window[funcPanel=course.mainlayout]");
                var baseGrid=win.down("basegrid");
                var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                var baseForm=formpanel.getForm();

                //若选择了课程数据，那么禁止手动输入，并且将讲师的数据也填入进去
                var records=baseGrid.getSelectionModel().getSelection();

                if(records.length>0){
                    if(records[0].get("mainTeacherName")){
                        baseForm.findField("mainTeacherName").setValue(records[0].get("mainTeacherName"));
                        baseForm.findField("mainTeacherId").setValue(records[0].get("mainTeacherId"));

                        baseForm.findField("mainTeacherName").setEditable(false);
                    }else{

                    }
                    
                    baseForm.findField("courseName").setEditable(false);                   
                }

                //...在此之后继续执行公共的click事件
            }
        },
       
        //课程教师选择确定事件
        "window[funcPanel=teacher.mainlayout] button[ref=ssOkBtn]":{
            beforeclick: function(btn) {      
                    
                var win=btn.up("window[funcPanel=teacher.mainlayout]");
                var baseGrid=win.down("basegrid");
                var formpanel=Ext.ComponentQuery.query('form[xtype='+win.formPanel+']')[0];
                var baseForm=formpanel.getForm();

                //若选择了教师数据，那么禁止手动输入
                var records=baseGrid.getSelectionModel().getSelection();

                if(records.length>0){
                    baseForm.findField("mainTeacherName").setEditable(false);                    
                }

                //...在此之后继续执行公共的click事件
            }
        },

        "panel[xtype=class.classimportform] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;
                var dicForm = btn.up('panel[xtype=class.classimportform]');
                var objForm = dicForm.getForm();
                if (objForm.isValid()) {
                    objForm.submit({
                        url: comm.get('baseUrl') + "/TrainClass/importData",
                        waitMsg: '正在导入文件...',
                        success: function(form, action) {
                            self.msgbox("导入成功！");

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure:function(form, action){
                            if(action.result==undefined){
                                self.Error("文件导入失败，文件有误或超过限制大小！");
                            }else{
                                self.Error(action.result.obj);
                            }
                          
                        }
                    });
                } else {
                    self.Error("请选择要上传Excel文件！")
                }
                
                return false 
            }
        },
        "panel[xtype=class.classimportform] button[ref=formClose]": {
            click: function(btn) {
                var win = btn.up('window');                         
                //关闭窗体
                win.close();
                return false;
            }
        },
        
        //导入班级课程
        "panel[xtype=class.courseimportform] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;
                var dicForm = btn.up('panel[xtype=class.courseimportform]');
                var objForm = dicForm.getForm();
                if (objForm.isValid()) {
                    objForm.submit({
                        url: comm.get('baseUrl') + "/TrainClassschedule/importData",
                        waitMsg: '正在导入文件...',
                        timeout: 1000*30*60,
                        success: function(form, action) {
                            if(action.result.obj=="-1"){
                                self.Info("数据部分导入成功！详情见导出的excel文档");
                                
                                var url=comm.get('baseUrl') + "/TrainClassschedule/downNotImportInfo";

                                //window.open(url);
                                window.location.href=url;
                            } else {
                                self.msgbox("数据全部导入成功！");
                                
                            }

                            var win = btn.up('window');
                            var grid = win.grid;
                            //刷新列表
                            grid.getStore().load();
                            win.close();
                        },
                        failure:function(form, action){
                            if(action.result==undefined){
                                self.Error("文件导入失败，文件有误或超过限制大小！");
                            }else{
                                self.Error(action.result.obj);
                            }                        
                        }
                    });
                } else {
                    self.Error("请选择要上传Excel文件！")
                }
                
                return false 
            }
        },
        "panel[xtype=class.courseimportform] button[ref=formClose]": {
            click: function(btn) {
                var win = btn.up('window');                         
                //关闭窗体
                win.close();
                return false;
            }
        },

        //导入班级学员
        "panel[xtype=class.traineeimportform] button[ref=formSave]": {
            beforeclick: function(btn) {
                var self=this;
                var dicForm = btn.up('panel[xtype=class.traineeimportform]');
                var objForm = dicForm.getForm();

                if (objForm.isValid()) {

                    var needSync=objForm.findField("needSynctrainee").getValue();
                    var message="";
                    if(needSync==true){
                        message="您选择了同步学员库操作，是否确定导入？";
                        needSync=1;
                    }else{
                        message="您未选择同步学员库操作，是否确定导入？";
                        needSync=0;
                    }
                    Ext.Msg.confirm('温馨提示', message, function(btn2, text) {
                        if (btn2 == 'yes') {
                                
                            var uuid=objForm.findField("classId").getValue();
                            var tabPanel=Ext.ComponentQuery.query('tabpanel[xtype=app-main]')[0];
                            var tabItem=tabPanel.getActiveTab();
                            
                            objForm.submit({
                                url: comm.get('baseUrl') + "/TrainClasstrainee/importData",
                                params:{
                                    needSync:needSync
                                },
                                waitMsg: '正在导入文件...',
                                success: function(form, action) {
                                    self.msgbox("导入成功！");

                                    //刷新学员订餐、住宿列表
                                    //查询班级的就餐学员信息
                                    var traineeFoodGrid=tabItem.down("grid[ref=traineeFoodGrid]");
                                    if(traineeFoodGrid){
                                        self.asyncAjax({
                                            url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassFoodTrainees",
                                            params: {
                                                classId: uuid,
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
                                    var traineeRoomGrid=tabItem.down("grid[ref=traineeRoomGrid]");
                                    if(traineeRoomGrid){
                                        self.asyncAjax({
                                            url: comm.get("baseUrl")  + "/TrainClasstrainee/getClassRoomTrainees",
                                            params: {
                                                classId:uuid,
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
                                    var win = btn.up('window');
                                    var grid = win.grid;
                                    //刷新列表
                                    grid.getStore().load();
                                    win.close();
                                },
                                failure:function(form, action){
                                    if(action.result==undefined){
                                        self.Error("文件导入失败，文件有误或超过限制大小！");
                                    }else{
                                        self.Error(action.result.obj);
                                    }
                                  
                                }
                            });
                        }
                    });
                } else {
                    self.Error("请选择要上传Excel文件！")
                }
                
                return false 
            }
        },
        "panel[xtype=class.traineeimportform] button[ref=formClose]": {
            click: function(btn) {
                var win = btn.up('window');                         
                //关闭窗体
                win.close();
                return false;
            }
        },

        "window[funCode=selectstudent_detail] button[ref=formSave]":{
            beforeclick: function(btn) {               
                var self=this;

                var win=btn.up("window[funCode=selectstudent_detail]");
                var grid=win.down("grid[ref=selectedStudentGrid]");
                var baseGrid=win.down("basegrid");

                var classId=win.insertObj.uuid;

                //var dataField=win.dataField;
                var store=grid.getStore();
                var nameArray=new Array();
                var idArray=new Array();
                var xbmArray=new Array();
                var phoneArray=new Array();
                var classTraineeIdArray=new Array();
                var sfzjhArray=new Array();

                for(var i=0;i<store.getCount();i++){
                    if(idArray.indexOf(store.getAt(i).get("uuid"))==-1||store.getAt(i).get("uuid")=="null"){
                        nameArray.push(store.getAt(i).get("xm"));
                        idArray.push(store.getAt(i).get("uuid")?store.getAt(i).get("uuid"):" ");  //为空的数据，要使用一个空格号隔开，否则后台split分割有误
                        xbmArray.push(store.getAt(i).get("xbm")?store.getAt(i).get("xbm"):" ");
                        phoneArray.push(store.getAt(i).get("mobilePhone")?store.getAt(i).get("mobilePhone"):" ");
                        classTraineeIdArray.push(store.getAt(i).get("classTraineeId")?store.getAt(i).get("classTraineeId"):" "); //若此时不为null，则表明为旧的学员
                        sfzjhArray.push(store.getAt(i).get("sfzjh")?store.getAt(i).get("sfzjh"):" ");
                    }                        
                }
                
                //提交修改的班级学员信息
                self.asyncAjax({
                    url: comm.get("baseUrl")  + "/TrainClass/doupdate",
                    params: {
                        uuid: classId,
                        classTraineeId:classTraineeIdArray.join(","),
                        traineeId:idArray.join(","),
                        traineeXbm:xbmArray.join(","),    
                        traineeName:nameArray.join(","),
                        traineePhone:phoneArray.join(","),
                        traineeSfzjh:sfzjhArray.join(",")
                    },
                    //回调代码必须写在里面
                    success: function(response) {
                        var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if(data.success){

                            baseGrid.getStore().getProxy().extraParams.filter="[]";
                            win.funData.grid.getStore().load();
                            win.close();

                        }else{
                            self.Warning(data.obj);
                        }
                    }
                });                        

                return false;
            }
        },
    },

    doSave:function(btn,cmd){

        var self=this;

        var win = btn.up('window');
        var funCode = win.funCode;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();
        var funData = basePanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);
        
        //把checkbox的值转换为数字
        params.needChecking=params.needChecking==true?1:0;
        params.needSynctrainee=params.needSynctrainee==true?1:0;
        
        
        //判断当前是保存还是修改操作
        var act = Ext.isEmpty(pkField.getValue()) ? "doadd" : "doupdate";
        if (formObj.isValid()) {
                                 
            var resObj = self.ajax({
                url: funData.action + "/" + act,
                params: params
            });
            if (resObj.success) {
                //采用返回的数据刷新表单
                //self.setFormValue(formObj, resObj.obj);

                self.msgbox("保存成功!");

                if(cmd=="saveContinue"){
                    formObj.reset();
                   
                    //给窗体赋默认值
                    var insertObj = win.insertObj;                   
                    self.setFormValue(formObj, insertObj);
                    
                }else{
                     win.close();
                }

                var grid = win.funData.grid; //窗体是否有grid参数
                if (!Ext.isEmpty(grid)) {
                    var store = grid.getStore();
                    /*
                    var proxy = store.getProxy();
                    proxy.extraParams = {
                        whereSql: win.funData.whereSql,
                        orderSql: win.funData.orderSql,
                        filter: win.funData.filter
                    };*/
                    store.loadPage(1); //刷新父窗体的grid
                }
               
            } else {
                if (!Ext.isEmpty(resObj.obj)) self.Info(resObj.obj);
            }
            

        } else {
            
            var errors = ["前台验证失败，错误信息："];
            formObj.getFields().each(function(f) {
                if (!f.isValid()) {
                    errors.push("<font color=red>" + f.fieldLabel + "</font>:" + f.getErrors().join(","));
                }
            });
            self.msgbox(errors.join("<br/>"));
        }
        
    },

    doClassFoodSave:function(btn,cmd){

        var self=this;

        var win = btn.up('window');
        var funCode = win.funCode;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();
        var funData = basePanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);
        

        var excuteFunc=function(){
            var datas=[];
            var traineeFoodGrid=win.down("grid[ref=traineeFoodGrid]");      
            var traineeFoodStore=traineeFoodGrid.getStore();
            for(var i=0;i<traineeFoodStore.getCount();i++){
                var rowData=traineeFoodStore.getAt(i).getData();   
                
                var lunch=rowData.lunch==true?1:0;
                if(params.eatNumber==0&&params.avgNumber==0)    //若这两个参数为0，则自动勾选午餐
                    lunch=1;

                datas.push({
                    uuid: rowData.uuid,
                    xm: rowData.xm,
                    breakfast: rowData.breakfast==true?1:0,
                    lunch: lunch,
                    dinner: rowData.dinner==true?1:0
                });
            }

            params.classFoodInfo=JSON.stringify(datas);
         
            self.asyncAjax({
                url: comm.get("baseUrl")  + "/TrainClass/doEditClassFood",
                params: params,
                //回调代码必须写在里面
                success: function(response) {
                    var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                    if(data.success){  //若存在rows数据，则表明请求正常 

                        self.msgbox(data.obj);

                        //刷新列表
                        var grid = win.funData.grid;                
                        grid.getStore().load();
                        win.close();

                    }else{
                        self.Warning(data.obj);
                    }
                }
            }); 
        }

        if(params.eatNumber==0&&params.avgNumber==0){
            Ext.MessageBox.confirm('温馨提示', '<p>您当前没有输入用餐围数和每围人数的数据，将自动勾选所有学员的午餐选项！</p><p style="color:red;font-size:14px;font-weight: 400;">你确定提交吗？</p>', function(btn, text) {
                if (btn == 'yes') {
                    excuteFunc();
                }
            });
        }else{
            excuteFunc();
        }
        
    },

    doClassRoomSave:function(btn,cmd){

        var self=this;

        var win = btn.up('window');
        var funCode = win.funCode;
        var basePanel = win.down("basepanel[funCode=" + funCode + "]");
        var objForm = basePanel.down("baseform[funCode=" + funCode + "]");
        var formObj = objForm.getForm();
        var funData = basePanel.funData;
        var pkName = funData.pkName;
        var pkField = formObj.findField(pkName);
        var params = self.getFormValue(formObj);
        
       
        var datas=[];
        var traineeRoomGrid=win.down("grid[ref=traineeRoomGrid]");      
        var traineeRoomStore=traineeRoomGrid.getStore();
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

                    self.msgbox(data.obj);

                    //刷新列表
                    var grid = win.funData.grid;                
                    grid.getStore().load();
                    win.close();

                }else{
                    self.Warning(data.obj);
                }
            }
        }); 

    }

});
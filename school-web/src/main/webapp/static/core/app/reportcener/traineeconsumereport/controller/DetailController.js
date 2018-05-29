/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.reportcenter.traineeconsumereport.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.traineeconsumereport.detailController',
    mixins: {
        
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil',
        queryUtil:"core.util.QueryUtil"
        
    },
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");
    },
    /** 该视图内的组件事件注册 */
    control: {    

        /**
         * 导出班级就餐详情数据
         */
        "basepanel basegrid[xtype=traineeconsumereport.classdetailgrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid=btn.up("basegrid");
                var baseFormTab=baseGrid.up("baseformtab");

                var classId=baseFormTab.insertObj.CLASS_ID;

                var params="?classId="+classId;
                var title = "确定要导出班级就餐明细信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassrealdinner/exportDinnerDetailExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainReport/checkExportDinnerDetailEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        component.destroy();
                                        self.Info(data.obj);                                
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            component.destroy(); 
                                            self.Error("导出失败，请重试或联系管理员！");                                       
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },
        

        /**
         * 导出班级学员就餐UP详情数据
         */
        "basepanel basegrid[xtype=traineeconsumereport.traineedetailgrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid=btn.up("basegrid");
                var baseFormTab=baseGrid.up("baseformtab");

                 //获取表格参数
                var extraParams = baseGrid.getStore().getProxy().extraParams;
                var params="?t=1";
                
                for(var i in extraParams){
                    if(extraParams[i]!=undefined)
                        params+="&"+i+"="+extraParams[i];
                }

                var title = "确定要导出班级点券就餐明细信息吗？";
            
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        //window.location.href = comm.get('baseUrl') + "/TrainClass/exportExcel?ids=" + ids.join(",");
                        var component=Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height:0,
                            hidden:true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainReport/exportTraineeConsumeDetailExcel' + params + '"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        
                       
                        var time=function(){
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainReport/checkExportTraineeConsumeDetailEnd',
                                timeout: 1000*60*30,        //半个小时         
                                //回调代码必须写在里面
                                success: function(response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if(data.success){
                                        Ext.Msg.hide();
                                        component.destroy(); 
                                        self.Info(data.obj);                               
                                    }else{                                    
                                        if(data.obj==0){    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            component.destroy();
                                            self.Error("导出失败，请重试或联系管理员！");                                        
                                        }else{
                                            setTimeout(function(){time()},1000);
                                        }
                                    }               
                                },
                                failure: function(response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                }
                            });
                        }
                        setTimeout(function(){time()},1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },

        "basepanel basegrid[xtype=traineeconsumereport.traineedetailgrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                if (!baseGrid)
                    return false;

                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;
                

                var EmployeeName="";
                var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                EmployeeName = girdSearchTexts[0].getValue();
               

                var store = baseGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams.EmployeeName = EmployeeName;
                //store.loadPage(1);

                //加载数据，并且加载汇总数据
                this.loadTraineeDetailDatas(store,baseGrid);

                return false;
            }
        },
        //快速搜索文本框回车事件
        "basepanel basegrid[xtype=traineeconsumereport.traineedetailgrid] field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {
                    //得到组件                 
                    var baseGrid = field.up("basegrid");
                    if (!baseGrid)
                        return false;

                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;
                    
                    var EmployeeName="";
                    var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                    EmployeeName = girdSearchTexts[0].getValue();
                   

                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();
                    proxy.extraParams.EmployeeName = EmployeeName;
                    //store.loadPage(1);

                    //加载数据，并且加载汇总数据
                    this.loadTraineeDetailDatas(store,baseGrid);
                }
                return false;
            }
        },     
        "basequeryform[xtype=traineeconsumereport.traineequerypanel] field":{
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {
                    //得到组件                 
                    //self.Info("暂时无法搜索！");                    
                    var queryPanel = field.up("basequeryform");
                   
                    var obj={};
                    var queryFields=queryPanel.query("basequeryfield");                  
                    Ext.each(queryFields,function(queryField){
                        var fieldName=queryField.name;                       
                        var valueField=queryField.down("field[name="+fieldName+"_field]");
                        var value=valueField.getValue();
                        if(value)
                            obj[fieldName]=value;
                    });

                    //处理分组类别
                    var GROUP_TYPES='';
                    var groupTypes=queryPanel.down("checkboxgroup").getValue().GROUP_TYPE;
                    if(Array.isArray(groupTypes)){
                        GROUP_TYPES=groupTypes.join(',');
                    }else{
                        GROUP_TYPES=groupTypes;
                    }
                    if(GROUP_TYPES!=undefined&&GROUP_TYPES!=""){
                        if(GROUP_TYPES.indexOf("DAY(ConsumeDate)")!=-1){
                            if(GROUP_TYPES.indexOf("MONTH(ConsumeDate)")==-1){
                                GROUP_TYPES="YEAR(ConsumeDate),MONTH(ConsumeDate),"+GROUP_TYPES;
                            }
                            if(GROUP_TYPES.indexOf("YEAR(ConsumeDate)")==-1){
                                GROUP_TYPES="YEAR(ConsumeDate),"+GROUP_TYPES;
                            }
                        }
                        
                        if(GROUP_TYPES.indexOf("MONTH(ConsumeDate)")!=-1){
                            if(GROUP_TYPES.indexOf("YEAR(ConsumeDate)")==-1){
                                GROUP_TYPES="YEAR(ConsumeDate),"+GROUP_TYPES;
                            }
                        }
                    }
                    obj.GROUP_TYPE=GROUP_TYPES;

                    var funCode = queryPanel.funCode;
                    var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");                        
                    var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");            
                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();

                    obj.CLASS_ID= proxy.extraParams.CLASS_ID;
                    proxy.extraParams=obj;
                    //store.loadPage(1);

                    //加载数据，并且加载汇总数据
                    this.loadTraineeDetailDatas(store,baseGrid);
                }
                return false;
            }
        },
        "basequeryform[xtype=traineeconsumereport.traineequerypanel] button[ref=gridSearchFormOk]":{
            beforeclick:function(btn){        
                //self.Info("暂时无法搜索！");                    
                var queryPanel = btn.up("basequeryform");
                
                var obj={};
                var queryFields=queryPanel.query("basequeryfield");                  
                Ext.each(queryFields,function(queryField){
                    var fieldName=queryField.name;                       
                    var valueField=queryField.down("field[name="+fieldName+"_field]");
                    var value=valueField.getValue();
                    if(value)
                        obj[fieldName]=value;
                });

                //处理分组类别
                var GROUP_TYPES='';
                var groupTypes=queryPanel.down("checkboxgroup").getValue().GROUP_TYPE;
                if(Array.isArray(groupTypes)){
                    GROUP_TYPES=groupTypes.join(',');
                }else{
                    GROUP_TYPES=groupTypes;
                }
                if(GROUP_TYPES!=undefined&&GROUP_TYPES!=""){
                    if(GROUP_TYPES.indexOf("DAY(ConsumeDate)")!=-1){
                        if(GROUP_TYPES.indexOf("MONTH(ConsumeDate)")==-1){
                            GROUP_TYPES="YEAR(ConsumeDate),MONTH(ConsumeDate),"+GROUP_TYPES;
                        }
                        if(GROUP_TYPES.indexOf("YEAR(ConsumeDate)")==-1){
                            GROUP_TYPES="YEAR(ConsumeDate),"+GROUP_TYPES;
                        }
                    }
                    
                    if(GROUP_TYPES.indexOf("MONTH(ConsumeDate)")!=-1){
                        if(GROUP_TYPES.indexOf("YEAR(ConsumeDate)")==-1){
                            GROUP_TYPES="YEAR(ConsumeDate),"+GROUP_TYPES;
                        }
                    }
                }
                obj.GROUP_TYPE=GROUP_TYPES;

                var funCode = queryPanel.funCode;
                var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");                        
                var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");            
                var store = baseGrid.getStore();
                var proxy = store.getProxy();


                obj.CLASS_ID= proxy.extraParams.CLASS_ID;
                proxy.extraParams=obj;
                //store.loadPage(1);  

                //加载数据，并且加载汇总数据
                this.loadTraineeDetailDatas(store,baseGrid);            

                return false;
            }
        },
        "basequeryform[xtype=traineeconsumereport.traineequerypanel] button[ref=gridSearchFormReset]":{
            beforeclick:function(btn){      
                var self=this;
                          
                var queryPanel=btn.up("basequeryform");
                self.resetQueryPanel(queryPanel);

                queryPanel.down("checkboxgroup").reset();

                //触发点击提交事件
                var submitBtn=queryPanel.down("button[ref=gridSearchFormOk]");            
                submitBtn.fireEvent('beforeclick',submitBtn);

                /*
                var funCode = queryPanel.funCode;
                var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");
                var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
                var store = baseGrid.getStore();
                var proxy = store.getProxy();
            
                //只重置这两个数据，还有一个extParams参数是来自baseGrid中设置的，不用去改变。
                proxy.extraParams=null;
                store.load();
                */
                return false;
            }
        },  
    },

    loadTraineeDetailDatas:function(store,baseGrid){
        var self=this;
        store.loadPage(1,{
            scope: this,
            callback: function(records, operation, success) {
                
                self.syncAjax({
                    url:comm.get('baseUrl') + "/TrainReport/getTeacherConsumeDetailDatas",
                    timeout: 1000*60*30,        //半个小时         
                    //回调代码必须写在里面
                    success: function(response) {
                        var result = Ext.decode(Ext.valueFrom(response.responseText, '{}'));

                        if(result.success){
                            var data=result.obj;
                            var html="交易笔数："+data.ConsumeNumber+" 笔&nbsp;&nbsp;&nbsp;消费总额："+data.ConsumeValue+" 元";
                        
                            baseGrid.down('panel[ref=traineeDetailInfo]').setHtml(html);                              
                        }else{                                    
                            baseGrid.down('panel[ref=traineeDetailInfo]').setHtml("请求失败！");   
                        }               

                    },
                    failure: function(response) {                                
                        self.Error("请求失败！");
                    }
                });
            
            }
        });
    }    
});

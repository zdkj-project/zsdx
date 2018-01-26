/**
 * This class is the controller for the main view for the application. It is specified as
 * the "controller" of the Main view class.
 *
 * TODO - Replace this content of this view to suite the needs of your application.
 */
Ext.define('core.main.controller.MainController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.main.mainController',
    
   
    init: function() {
        var self = this;
        //在主视图中写一次，即可加载，重复在不同的是视图中写，会多次加载,造成事件方法会执行多次
        //console.log("初始化controller");


        var baseController =  Ext.create('core.base.controller.MainController');
        baseController.init();
        baseController.inited = true;
        

        //console.log(this.getView().getViewModel().get('systemMenu' ));

        this.control({   //与以前一样的写法
            
            //只要一经声明，则该视图范围内生效
            /*
            "button": {
                click : function(btn) {
                    console.log(11);
                   
                }
            }
            */
            
        });
    
        //调用此方法，自动定时获取收信数量
        this.doGetNewInfoNum();
    },
    //若与其他控制器存在相同的方法名，则仅仅会执行这个view里面的事件
    onItemSelected: function (sender, record) {
        Ext.Msg.confirm('Confirm', 'Are you sure?', 'onConfirm', this);
    },

    onConfirm: function (choice) {
        if (choice === 'yes') {
            //
        }
    },

    onExitSystem:function(){
        Ext.Msg.confirm('提示', '确定要退出系统么?', function(btn, text) {
            if (btn == 'yes') {
                window.location.href = comm.get("baseUrl") + "/login/logout";               
            }
        });
    },

    onWipeCache:function(btn){
        Ext.Msg.confirm('提示', '确定要清除缓存么?', function(btn, text) {
            if (btn == 'yes') {
                //前台清理
                factory.DDCache.clearAll();
                //后台清理
                var suppUtil = Ext.create("core.util.MessageUtil");

                Ext.Ajax.request({
                    url: comm.get('baseUrl') + '/login/clearCache',
                    method: "POST",
                    async: true,
                    success: function(response) {
                        var resObj = Ext.decode(response.responseText);
                        if (resObj.success) {
                            suppUtil.msgbox(resObj.obj);
                        } else {
                            suppUtil.Error(resObj.obj)
                        }
                    },
                    failure: function(response) {
                        suppUtil.Error('数据请求出错了！！！！\n错误信息：\n' + response.responseText);
                    }
                })
            }
        });
    },

    onChangePassword:function(btn){
        Ext.widget("main.changepwd").show();
    },

    onChangeMainMenu:function(btn){
        this.getView().getViewModel().set("menuType.value",btn.changeType);
    },

    onChangeMainHeader:function(btn){
        this.getView().getViewModel().set("headerType.value",btn.changeType);
    },
    onShowSystemPanel:function(btn,e){
        var id= sessionStorage.systemMenuWindowId;
        
        var win=Ext.getCmp(id); 
        if(!win){
            win = Ext.create("Ext.window.Window", {
                //bodyPadding: '0 10 10 0',
                width:570,
                //height:200,
                margin:'0 0 0 10',
                scrollable:true, 
                title:null,
                draggable :false,
                resizable :false,
                closable:false,
                tpl:new Ext.XTemplate(
                    '<tpl for=".">',
                        '<tpl if="url== \'\'">',
                            '<a href="javascript:Ext.Msg.alert(\'温馨提示\',\'此系统暂无超链接！\');" target="_bank" style="color: #000;">',
                                 '<div class="mainMenuIcon-wrap" style="padding: 10px 0px;margin:0px;width: 80px;">',                                    
                                '<img src="{bigIcon}" class="mainMenuIcon-img" style="width:50px;height:50px;"/>',                                
                                '<br/><span class="mainMenuIcon-text" style="padding-top: 0px;">{text}</span>',
                                '</div>',
                            '</a>',
                        '<tpl else>',
                            '<a href="{url}" target="_bank" style="color: #000;">',
                                '<div class="mainMenuIcon-wrap" style="padding: 10px 0px;margin:0px;width: 80px;">',                                    
                                '<img src="{bigIcon}" class="mainMenuIcon-img" style="width:50px;height:50px;border-radius: 0px;"/>',                                
                                '<br/><span class="mainMenuIcon-text" style="padding-top: 0px;">{text}</span>',
                            '</div>',
                            '</a>',
                        '</tpl>',           
                    '</tpl>'
                ),
                data:this.getView().getViewModel().get("otherSystemList") 
            }); 
        
            var x=btn.getXY()[0];
            var y=btn.getXY()[1]+40;
            win.showAt(x,y);


            sessionStorage.systemMenuWindowId=win.getId();
        } else{
            if(win.hidden){
                var x=btn.getXY()[0];
                var y=btn.getXY()[1]+40;
                win.showAt(x,y);
            }else{
                win.hide();
            }
            
        }

    },


    /*
    *菜单栏显示子菜单
    */
    onViewIconItemClick:function(view,record,item,index){
        

        var viewport=this.getView();    
        var tabPanel=viewport.down("tabpanel[xtype=app-main]");
            
        var data=record.data;
        var tabItem=tabPanel.getComponent(data.menuCode);
        if(!tabItem){

            if(data.menuType=="MENU"){

                //小图标 
                var smallIconCls=data.smallIcon;
                if (!smallIconCls) {
                    smallIconCls="x-fa fa-th";
                }

                tabPanel.add({
                    title: data.text,
                    //iconCls: smallIconCls,
                    scrollable :true, 
                    itemId:data.menuCode,
                    // The following grid shares a store with the classic version's grid as well!
                    items: [{
                        xtype: 'main.mainmenuicon',
                        datas:data.children
                    }]
                });
            }
            else  if(data.menuType=="FUNC"){    
                
                //小图标 
                var smallIconCls=data.smallIcon;
                if (!smallIconCls) {
                    smallIconCls="x-fa fa-bars";
                }
          
                var targetStr=data.menuTarget.split(',');
                if(targetStr.length==2){                
                    try{           
                        //Ext.create(targetStr[1]); //不用创建controller         
                        //var application = coreApplication.getApplication();//也不用这样去获取
                        //application.getViewController(targetStr[1]);    //zzk：重写了aplication组件，使其支持getViewController方法
                        //最后声明：视图控制器会随着视图的创建而创建，销毁而销毁，不必手动去创建
                                             
                        
                        //zzk 2017-6-1：优化细节
                        tabItem=Ext.create({
                            xtype:'container',
                            title: data.text,
                            //iconCls: smallIconCls,
                            scrollable :true, 
                            itemId:data.menuCode,
                            layout:'fit', 
                        });
                        tabPanel.add(tabItem);

                        //zzk 2017-5-26：防止加载打开tab页面时卡顿
                        // tabPanel.add({   //原始写法，不太好
                        //     title: data.text,
                        //     //iconCls: smallIconCls,
                        //     scrollable :true, 
                        //     itemId:data.menuCode,
                        //     layout:'fit',                        
                        //     // The following grid shares a store with the classic version's grid as well!
                        //     //items: [tabItem]
                        // }); 

                        //延迟放入到tab中
                        setTimeout(function(){
                            //创建组件
                            var item=Ext.widget(targetStr[0]); 
                            tabItem.add(item);

                            //tabPanel.getComponent(data.menuCode).add(tabItem);    //原始写法，不太好    
                        },30);
                                        


                    }catch(e){
                        Ext.Msg.alert('提示', '此模块功能待开发中！');
                        //console.log(e);
                        return false;
                    }
                }else{
                    Ext.Msg.alert('提示', '加载失败，请联系管理员！');
                }
            }
            else if(data.menuType=="IFRAME"){
                //小图标 
                var smallIconCls=data.smallIcon;
                if (!smallIconCls) {
                    smallIconCls="x-fa fa-link";
                }

                tabPanel.add({
                    title: data.text,
                    //iconCls: smallIconCls,
                    scrollable :true, 
                    itemId:data.menuCode,
                    layout:'fit',    
                    items: [{
                        xtype: 'container',                                                  
                        html:'<iframe src="http://www.baidu.com" width="100%" height="100%"   frameborder=0  ></iframe>'
                    }]
                });
            }
           
            tabPanel.setActiveTab( tabPanel.items.length-1);
        }else{
             tabPanel.setActiveTab( tabItem);
        }   
    },

    onMainMenuTreeClick:function(view,record){

        var viewport=this.getView();    
        var tabPanel=viewport.down("tabpanel[xtype=app-main]");
         
        var data=record.data;

        var tabItem=tabPanel.getComponent(data.menuCode);
        if(!tabItem){
            if(data.menuType=="MENU"){

                //图标 
                var smallIconCls="";
                if (!data.bigIcon) {
                    smallIconCls="x-fa fa-th";
                }

                tabPanel.add({
                    title: data.text,       

                    //icon: data.bigIcon,
                    //iconCls:smallIconCls+' MainMenuTreeIconStyle',

                    scrollable :true, 
                    itemId:data.menuCode,
                    // The following grid shares a store with the classic version's grid as well!
                    items: [{
                        xtype: 'main.mainmenuicon',
                        datas:data.children
                    }]
                });
            }
            else  if(data.menuType=="FUNC"){

                //图标 
                var smallIconCls="";
                if (!data.bigIcon) {
                    smallIconCls="x-fa fa-bars";
                }

                var targetStr=data.menuTarget.split(',');
                if(targetStr.length==2){
                    try{           
                        //Ext.create(targetStr[1]); //不用创建controller         
                        //var application = coreApplication.getApplication();//也不用这样去获取
                        //application.getViewController(targetStr[1]);    //zzk：重写了aplication组件，使其支持getViewController方法
                        //最后声明：视图控制器会随着视图的创建而创建，销毁而销毁，不必手动去创建
                        

                        //zzk 2017-5-26：防止加载打开tab页面时卡顿
                        tabItem=Ext.create({
                            xtype:'container',
                            title: data.text,
                            //iconCls: smallIconCls,
                            scrollable :true, 
                            itemId:data.menuCode,
                            layout:'fit', 
                        });
                        tabPanel.add(tabItem);

                        // tabPanel.add({
                        //     title: data.text,
                        //     //iconCls: smallIconCls,
                        //     scrollable :true, 
                        //     itemId:data.menuCode,
                        //     layout:'fit',                        
                        //     // The following grid shares a store with the classic version's grid as well!
                        //     //items: [tabItem]
                        // }); 

                        //延迟放入到tab中
                        setTimeout(function(){
                            //创建组件
                            var item=Ext.widget(targetStr[0]); 
                            tabItem.add(item);
                            //tabPanel.getComponent(data.menuCode).add(tabItem);        
                        },30);

                    }catch(e){
                        Ext.Msg.alert('提示', '此模块功能待开发中！');
                        //console.log(e);
                        return false;
                    }
                }else{
                    Ext.Msg.alert('提示', '加载失败，请联系管理员！');
                }
            }
            else if(data.menuType=="IFRAME"){
        
                //图标 
                var smallIconCls="";
                if (!data.bigIcon) {
                    smallIconCls="x-fa fa-link";
                }

                tabPanel.add({
                    title: data.text,
                    
                    //icon: data.bigIcon,
                    //iconCls:smallIconCls+' MainMenuTreeIconStyle',

                    scrollable :true, 
                    itemId:data.menuCode,
                    layout:'fit',    
                    items: [{
                        xtype: 'container',                                                  
                        html:'<iframe src="http://www.baidu.com" width="100%" height="100%"   frameborder=0  ></iframe>'
                    }]
                });
            }
           
            tabPanel.setActiveTab( tabPanel.items.length-1);
        }else{
             tabPanel.setActiveTab( tabItem);
        }   
    },
    
    onMenuItemClick:function(menu,item){
        if(!item){
            return false;
        }
        
        //这个菜单，默认点击不了第二层
        if(item.menuType=="MENU"&&item.menuParent=="ROOT"){  
            return false;
        }

        var viewport=this.getView();    
        var tabPanel=viewport.down("tabpanel[xtype=app-main]");
         
       
        var tabItem=tabPanel.getComponent(item.menuCode);
        if(!tabItem){

            //小图标 
            var smallIconCls=item.smallIcon;
            if (!smallIconCls) {
                smallIconCls="x-fa fa-th";
            }

            if(item.menuType=="MENU"){
                tabPanel.add({
                    title: item.textBase,
                    //iconCls: smallIconCls,
                    scrollable :true, 
                    itemId:item.menuCode,
                    // The following grid shares a store with the classic version's grid as well!
                    items: [{
                        xtype: 'main.mainmenuicon',
                        datas:item.children
                    }]
                });
            }
            else  if(item.menuType=="FUNC"){    
                
                //小图标 
                var smallIconCls=item.smallIcon;
                if (!smallIconCls) {
                    smallIconCls="x-fa fa-bars";
                }
                /*以下方式不行，找到的controller的真实路径总带有Controller，与我们目录结构不符合*/
                //var application = coreApplication.getApplication();
                //var mycontrol = application.getController('core.good.news.controller.MainController');
                
                //使用create的方式来创建控制器，在控制器中使用了一个静态变量来防止多次初始化。
                //var myController =  Ext.create('core.good.news.controller.MainController');
                //myController.init();  //mainLayout中直接使用了控制器，所以，不必再手动init了;
          
                var targetStr=item.menuTarget.split(',');
                if(targetStr.length==2){                   

                    try{     
                        
                        //Ext.create(targetStr[1]); //不用创建controller         
                        //var application = coreApplication.getApplication();//也不用这样去获取
                        //var controller=application.getViewController(targetStr[1]);    //zzk：重写了aplication组件，使其支持getViewController方法
                        //最后声明：视图控制器会随着视图的创建而创建，销毁而销毁，不必手动去创建
                     

                        //zzk 2017-5-26：防止加载打开tab页面时卡顿
                        tabItem=Ext.create({
                            xtype:'container',
                            title: item.textBase,
                            //iconCls: smallIconCls,
                            scrollable :true, 
                            itemId:item.menuCode,
                            layout:'fit', 
                        });
                        tabPanel.add(tabItem);

                        // tabPanel.add({
                        //     title: item.textBase,
                        //     //iconCls: smallIconCls,
                        //     scrollable :true, 
                        //     itemId:item.menuCode,
                        //     layout:'fit',                        
                        //     // The following grid shares a store with the classic version's grid as well!
                        //     //items: [tabItem]
                        // }); 

                        //延迟放入到tab中
                        setTimeout(function(){
                            //创建组件
                            var item=Ext.widget(targetStr[0]); 
                            tabItem.add(item);
                            //tabPanel.getComponent(item.menuCode).add(tabItem);        
                        },30);

                    }catch(e){
                        Ext.Msg.alert('提示', '此模块功能待开发中！！');
                        //console.log(e);
                        return false;
                    }

                }else{
                    Ext.Msg.alert('提示', '加载失败，请联系管理员！');
                }
            }
            else if(item.menuType=="IFRAME"){
                //小图标 
                var smallIconCls=item.smallIcon;
                if (!smallIconCls) {
                    smallIconCls="x-fa fa-link";
                }
                
                tabPanel.add({
                    title: item.textBase,
                    //iconCls: smallIconCls,
                    scrollable :true, 
                    itemId:item.menuCode,
                    layout:'fit',    
                    items: [{
                        xtype: 'container',                                                  
                        html:'<iframe src="http://www.baidu.com" width="100%" height="100%"   frameborder=0  ></iframe>'
                    }]
                });
            }
           
            tabPanel.setActiveTab( tabPanel.items.length-1);
        }else{
             tabPanel.setActiveTab( tabItem);
        }   
    },


    onShowQrCode:function(btn){
        var win=Ext.get("app-qrcode");
        if(!win){
            win=Ext.widget("main.qrcode");
            var x=btn.getXY()[0];
            var y=btn.getXY()[1]+40;
            win.showAt(x,y);
        }
        win.show();
    },

    onHideQrCode:function(){
        var win=Ext.get("app-qrcode");
        if(win)
            win.hide();
    },

    doGetNewInfoNum:function(){
        var self=this;
        doAjax();  //率先执行一次
        setInterval(function(){
            doAjax();                
        },1000*60*5);  //每5分钟执行一次

        function doAjax(){
             Ext.Ajax.request({
                url: comm.get('baseUrl') + '/PushInfo/getInfoNum',
                method: "GET",
                async: true,
                success: function(response) {
                    var resObj = Ext.decode(response.responseText);
                    if (resObj.success) {
                        self.getView().getViewModel().set("NewInfoNum",resObj.obj);
                    }
                       
                },
                failure: function(response) {
                }
            })
        }
    },

    onOpenPushInfo:function(btn){
        var menus = this.getView().getViewModel().get('systemMenu');
    
        for(var i=0;i<menus.length;i++){
            if(menus[i].menuCode=="MESSAGE_INFO"){
                menus[i].textBase=menus[i].text;
                this.onMenuItemClick(null,menus[i]);
                break;
            }
        }
    }
    
}); 

Ext.define('core.main.view.Viewport', {
    extend: 'Ext.container.Container',

    xtype: 'app-viewport',
    requires: [
        'core.base.controller.MainController',
        'core.main.controller.MainController',
        'core.main.model.MainModel',
        'Ext.layout.container.VBox',
        'Ext.plugin.Viewport',
        "core.main.view.Main",
        "core.main.view.Header",  
        "core.main.view.HeaderSmall",            
        //"core.main.view.Footer",
        //"core.main.view.menu.MainMenu",
        "core.main.view.menu.MainMenuTree",
        "core.main.view.menu.MainMenuPanel",
        "core.main.view.ChangePwd",

        //"core.train.teacher.view.DetailHtmlPanel" //若要使在sass-src目录下定义的样式生效，则必须要将文件加载。
        //"core.good.activity.view.MainLayout"  //只有当需要的js文件 requires进来的时候，才会被自动打包
    ],

    controller: 'main.mainController',
    viewModel: 'main.mainModel',

    /*设置最小宽度*/
    minWidth:1000,
   // scrollable:true,

    /*
    layout: {
        type: 'vbox',
        align: 'stretch'
    },
    items: [
        {
            xtype: 'app-header',
            height: 65,
            id: 'app-header'
        },
        {
            xtype: 'app-main', flex: 1
        }
    ]*/
    initComponent : function() {  
        var me = this;
        
        Ext.Ajax.request({
            url: comm.get('baseUrl')+'/sysuser/getUserMenuTree',
            method: "POST",
            async: false,
            timeout: 60000,        
            params:{
                excludes:'checked'
            },  
            success: function(response, opts) {
                try{
                    var result = Ext.decode(response.responseText);    
                    //console.log(result);
                    if(result.success==false){
                        Ext.MessageBox.show({
                            title: "警告",
                            msg: result.obj,
                            buttons: Ext.MessageBox.OK,
                            icon: Ext.MessageBox.WARNING,
                            fn: function(btn) {
                                location.reload()                                 
                            }
                        });      
                    }else{
                        me.getViewModel().set('systemMenu' , result);
                    }
                }catch(err){
                    //如果出现错误，则表明返回的不是json数据，所以，回到登录界面
                    window.location.href = comm.get("baseUrl") + "/login.jsp";
                }
            },

            failure: function(response, opts) {
                 console.log('server-side failure with status code ' + response.status);
                 //如果出现错误，则表明返回的不是json数据，所以，回到登录界面
                 window.location.href = comm.get("baseUrl") + "/login.jsp";
            }
        });

        Ext.Ajax.request({
            url: comm.get('baseUrl')+'/login/getOnlineCount',
            method: "POST",
            async: false,
            timeout: 60000,                    
            success: function(response, opts) {
                try{
                    var obj = Ext.decode(response.responseText);
                    //console.log(obj);
                    me.getViewModel().set('onlineNum' , obj.obj);   //此值后台返回
                }catch(err){
                    //如果出现错误，则表明返回的不是json数据，所以，回到登录界面
                    window.location.href = comm.get("baseUrl") + "/login.jsp";
                }
            },

            failure: function(response, opts) {
                 console.log('server-side failure with status code ' + response.status);
            }
        });

        me.getViewModel().set('currentUser' , comm.get("xm"));


     

       //currentDateWeek:Ext.Date.format(new Date(), 'Y年n月j日 D'),      

        
        //初始化菜单数据，可以通过ajax来获取，再设置
        /*
        this.getViewModel().set('systemMenu' , 
            [
                {
                    "parent": "ROOT",
                    "menuLeaf": "GENERAL",
                    "bigIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/paike_icon.png",
                    "level": 1,
                    "nodeCode": "",
                    "leaf": false,
                    "isHidden": "1",
                    "treeid": "02356e7e-c501-4f57-be82-bce8f62d85f0",
                    "menuTarget": "",
                    "children": [
                        {
                            "parent": "02356e7e-c501-4f57-be82-bce8f62d85f0",
                            "menuLeaf": "LEAF",
                            "bigIcon":  comm.get("baseUrl")+"/static/core/resources/images/icon/index_xueyuanxinxi.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "",
                            "menuTarget": "activity.mainlayout,core.good.activity.controller.MainController",
                            "children": [],
                            "menuCode": "SCHOOLCOURSE1",
                            "orderIndex": 1,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_xueyuanxinxi.png",
                            "id": "8a8a8834533fa25c01533fa260a40065",
                            "text": "学员信息",
                            "iconCls": "",
                            "issystem": 0
                        },{
                            "parent": "02356e7e-c501-4f57-be82-bce8f62d85f0",
                            "menuLeaf": "LEAF",
                            "bigIcon":  comm.get("baseUrl")+"/static/core/resources/images/icon/index_tongjibaobiao.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "",
                            "menuTarget": "news.mainlayout,core.good.news.controller.MainController",
                            "children": [],
                            "menuCode": "SCHOOLCOURSE2",
                            "orderIndex": 2,
                            "menuType": "FUNC",
                            "smallIcon":  comm.get("baseUrl")+"/static/core/resources/images/icon/index_tongjibaobiao.png",
                            "id": "8a8a8834533fa25c01533fa260a40705",
                            "text": "统计报表",
                            "iconCls": "",
                            "issystem": 0
                        },{
                            "parent": "02356e7e-c501-4f57-be82-bce8f62d85f0",
                            "menuLeaf": "LEAF",
                            "bigIcon":  comm.get("baseUrl")+"/static/core/resources/images/icon/index_paikeguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "",
                            "menuTarget": "",
                            "children": [],
                            "menuCode": "SCHOOLCOURSE3",
                            "orderIndex": 3,
                            "menuType": "IFRAME",
                            "smallIcon":  comm.get("baseUrl")+"/static/core/resources/images/icon/index_paikeguanli.png",
                            "id": "8a8a8834533fa25c01533fa260a1005",
                            "text": "排课管理",
                            "iconCls": "",
                            "issystem": 0
                        },{
                            "parent": "02356e7e-c501-4f57-be82-bce8f62d85f0",
                            "menuLeaf": "LEAF",
                            "bigIcon":  comm.get("baseUrl")+"/static/core/resources/images/icon/index_kechenguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "",
                            "menuTarget": "menu.mainlayout,core.system.menu.controller.MenuController",
                            "children": [],
                            "menuCode": "SCHOOLCOURSE4",
                            "orderIndex": 4,
                            "menuType": "FUNC",
                            "smallIcon":  comm.get("baseUrl")+"/static/core/resources/images/icon/index_kechenguanli.png",
                            "id": "8a8a8834533fa25c01533fa260a40045",
                            "text": "菜单管理",
                            "iconCls": "",
                            "issystem": 0
                        },{
                            "parent": "02356e7e-c501-4f57-be82-bce8f62d85f0",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_shiziguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "",
                            "menuTarget": "signup.mainlayout,core.good.signup.controller.MainController",
                            "children": [],
                            "menuCode": "SCHOOLCOURSE5",
                            "orderIndex": 5,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_shiziguanli.png",
                            "id": "8a8a8834533fa25c01533fa260a40005",
                            "text": "师资管理",
                            "iconCls": "",
                            "issystem": 0
                        }
                    ],
                    "menuCode": "EDURESOURCE",
                    "orderIndex": 1,
                    "menuType": "MENU",
                    "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/paike_icon.png",
                    "id": "02356e7e-c501-4f57-be82-bce8f62d85f0",
                    "text": "排课管理",
                    "iconCls": "",
                    "issystem": 1
                },
                {
                    "parent": "ROOT",
                    "menuLeaf": "GENERAL",
                    "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/jiucan_icon.png",
                    "level": 1,
                    "nodeCode": "",
                    "leaf": true,
                    "isHidden": "1",
                    "treeid": "33c0c66e-a54e-42f8-8dfd-e02ef4b0cba8",
                    "menuTarget": "activity.mainlayout,core.good.activity.controller.MainController",                 
                    "children": [],
                    "menuCode": "PERSON1",
                    "orderIndex": 2,
                    "menuType": "FUNC",
                    "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/jiucan_icon.png",
                    "id": "33c0c66e-a54e-42f8-8dfd-e02ef4b0cb28",
                    "text": "就餐管理",
                    "iconCls": "",
                    "issystem": 1
                },
                {
                    "parent": "ROOT",
                    "menuLeaf": "GENERAL",
                    "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/kaoqin_icon.png",
                    "level": 2,
                    "nodeCode": "",
                    "leaf": false,
                    "isHidden": "0",
                    "treeid": "",
                    "menuTarget": "",
                    "children": [
                        {
                            "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kechenkaoqin.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "signup.mainlayout,core.good.signup.controller.MainController",
                            "children": [],
                            "menuCode": "STUDENTCLASS1",
                            "orderIndex": 1,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kechenkaoqin.png",
                            "id": "8077B9ED-32FB-485A-A865-F9EECD10A231",
                            "text": "课程考勤",
                            "iconCls": "",
                            "issystem": 0
                        }, {
                            "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                            "menuLeaf": "LEAF",
                            "bigIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_jixujiaoyu.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "activity.mainlayout,core.good.activity.controller.MainController",
                            "children": [],
                            "menuCode": "STUDENTCLASS2",
                            "orderIndex": 2,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_jixujiaoyu.png",
                            "id": "8077B9ED-32FB-485A-A865-F9EECD10A232",
                            "text": "继续教育考勤",
                            "iconCls": "",
                            "issystem": 0
                        }, {
                            "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_jiaozhigong.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": false,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "",
                            "children": [{
                                "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                                "menuLeaf": "LEAF",
                                "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kaoqintongji.png",
                                "level": 4,
                                "nodeCode": "",
                                "leaf": true,
                                "isHidden": "0",
                                "treeid": "",
                                "menuTarget": "",
                                "children": [],
                                "menuCode": "STUDENTCLASS3",
                                "orderIndex": 1,
                                "menuType": "IFRAME",
                                "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kaoqintongji.png",
                                "id": "8077B9ED-32FB-485A-A865-F9EECD10A233",
                                "text": "考勤统计",
                                "iconCls": "",
                                "issystem": 0
                            }, {
                                "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                                "menuLeaf": "LEAF",
                                "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kaoqinjilu.png",
                                "level": 4,
                                "nodeCode": "",
                                "leaf": true,
                                "isHidden": "0",
                                "treeid": "",
                                "menuTarget": "signup.mainlayout,core.good.signup.controller.MainController",
                                "children": [],
                                "menuCode": "STUDENTCLASS4",
                                "orderIndex": 2,
                                "menuType": "FUNC",
                                "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kaoqinjilu.png",
                                "id": "8077B9ED-32FB-485A-A865-F9EECD10A234",
                                "text": "考勤记录",
                                "iconCls": "",
                                "issystem": 0
                            }, {
                                "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                                "menuLeaf": "LEAF",
                                "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_huiyidaoru.png",
                                "level": 4,
                                "nodeCode": "",
                                "leaf": true,
                                "isHidden": "0",
                                "treeid": "",
                                "menuTarget": "news.mainlayout,core.good.news.controller.MainController",
                                "children": [],
                                "menuCode": "STUDENTCLASS5",
                                "orderIndex": 3,
                                "menuType": "FUNC",
                                "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_huiyidaoru.png",
                                "id": "8077B9ED-32FB-485A-A865-F9EECD10A235",
                                "text": "会议导入",
                                "iconCls": "",
                                "issystem": 0
                            }],
                            "menuCode": "STUDENTCLASS6",
                            "orderIndex": 3,
                            "menuType": "MENU",
                            "smallIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_jiaozhigong.png",
                            "id": "8077B9ED-32FB-485A-A865-F9EECD10A237",
                            "text": "教职工考勤",
                            "iconCls": "",
                            "issystem": 0
                        }
                    ],
                    "menuCode": "NEWSTUDENT",
                    "orderIndex": 3,
                    "menuType": "MENU",
                    "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/kaoqin_icon.png",
                    "id": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                    "text": "考勤管理",
                    "iconCls": "",
                    "issystem": 0
                },
                {
                    "parent": "ROOT",
                    "menuLeaf": "GENERAL",
                    "bigIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/jichu_icon.png",
                    "level": 2,
                    "nodeCode": "",
                    "leaf": false,
                    "isHidden": "0",
                    "treeid": "",
                    "menuTarget": "",
                    "children": [
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_xuexiaoxinxi.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "",
                            "menuTarget": "",
                            "children": [],
                            "menuCode": "SCHOOLINFO",
                            "orderIndex": 1,
                            "menuType": "IFRAME",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_xuexiaoxinxi.png",
                            "id": "8a8a8834533fa25c01533fa260a40001",
                            "text": "学校信息",
                            "iconCls": "",
                            "issystem": 0
                        },
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_xiaoquxinxi.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "",
                            "menuTarget": "news.mainlayout,core.good.news.controller.MainController",
                            "children": [],
                            "menuCode": "CAMPUS",
                            "orderIndex": 2,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_xiaoquxinxi.png",
                            "id": "8a8a8834533fa25c01533fa260a40011",
                            "text": "校区信息",
                            "iconCls": "",
                            "issystem": 0
                        },                                            
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_zuzhijiagou.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "1",
                            "treeid": "null,ffdf24ef-6751-4343-836b-dca31282e972",
                            "menuTarget": "dept.mainlayout,core.system.dept.controller.Controller",
                            "children": [],
                            "menuCode": "SBGL",
                            "orderIndex": 10,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_zuzhijiagou.png",
                            "id": "ffdf24ef-6751-4343-836b-dca31282e972",
                            "text": "组织架构",
                            "iconCls": "",
                            "issystem": 1
                        },
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_zhiwuguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "8a8a8834533fa25c01533fa260a40000,C8E64000-4188-4AC4-979C-B39B9B850276",
                            "menuTarget": "jobinfo.mainlayout,core.systemset.jobinfo.controller.jobinfoController",
                            "children": [],
                            "menuCode": "JOBINFO",
                            "orderIndex": 19,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_zhiwuguanli.png",
                            "id": "C8E64000-4188-4AC4-979C-B39B9B850276",
                            "text": "职务管理",
                            "iconCls": "",
                            "issystem": 0
                        },
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_shujuzidian.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "dictionary.mainlayout,core.systemset.dictionary.controller.dicController",
                            "children": [],
                            "menuCode": "DICTIONARY",
                            "orderIndex": 20,
                            "menuType": "FUNC",
                            "smallIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_shujuzidian.png",
                            "id": "C133849D-6E8C-4F2C-99AF-33E46CD47FA6",
                            "text": "数据字典",
                            "iconCls": "",
                            "issystem": 0
                        },
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_fanjianxixin.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "",
                            "children": [],
                            "menuCode": "COURSEINFO",
                            "orderIndex": 21,
                            "menuType": "IFRAME",
                            "smallIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_fanjianxixin.png",
                            "id": "8a8a8834533fa25c01533fa260a40004",
                            "text": "房间信息",
                            "iconCls": "",
                            "issystem": 0
                        },
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_xinxizhongduan.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "signup.mainlayout,core.good.signup.controller.MainController",
                            "children": [],
                            "menuCode": "TEACHERINFO",
                            "orderIndex": 22,
                            "menuType": "FUNC",
                            "smallIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_xinxizhongduan.png",
                            "id": "5C617941-AC88-405A-BCFE-6EE290BB93AF",
                            "text": "信息终端",
                            "iconCls": "",
                            "issystem": 0
                        },
                        {
                            "parent": "8a8a8834533fa25c01533fa260a40000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_renyuanxinxi.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "news.mainlayout,core.good.news.controller.MainController",
                            "children": [],
                            "menuCode": "STUDENTINFO",
                            "orderIndex": 23,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_renyuanxinxi.png",
                            "id": "C9CC2963-E115-45AC-A884-3ED00FFD4BA2",
                            "text": "人员信息",
                            "iconCls": "",
                            "issystem": 0
                        }
                    ],
                    "menuCode": "BASEDATA",
                    "orderIndex": 4,
                    "menuType": "MENU",
                    "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/jichu_icon.png",
                    "id": "8a8a8834533fa25c01533fa260a40000",
                    "text": "基础数据",
                    "iconCls": "",
                    "issystem": 0
                },
                {
                    "parent": "ROOT",
                    "menuLeaf": "GENERAL",
                    "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/xitong_icon.png",
                    "level": 2,
                    "nodeCode": "",
                    "leaf": false,
                    "isHidden": "0",
                    "treeid": "",
                    "menuTarget": "",
                    "children": [                        
                        {
                            "parent": "8a8a8834533fa25c01533fa260a30000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_caidanshouquan.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "roleright.mainlayout,core.system.roleright.controller.RoleRightController",
                            "children": [],
                            "menuCode": "ROLERIGHT",
                            "orderIndex": 5,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_caidanshouquan.png",
                            "id": "8a8a8834533fabde01533fabe2bf0003",
                            "text": "菜单授权",
                            "iconCls": "",
                            "issystem": 0
                        },
                        {
                            "parent": "8a8a8834533fa25c01533fa260a30000",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_jiaoseguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "role.mainlayout,core.system.role.controller.RoleController",
                            "children": [],
                            "menuCode": "SYSROLE",
                            "orderIndex": 3,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_jiaoseguanli.png",
                            "id": "8a8a8834533fabde01533fabe2b70001",
                            "text": "角色管理",
                            "iconCls": "",
                            "issystem": 0
                        },            
                        {
                            "parent": "8a8a8834533fa25c01533fa260a30000",
                            "menuLeaf": "LEAF",
                            "bigIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_yonghuguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "user.mainlayout,core.system.user.controller.DeptUserController",
                            "children": [],
                            "menuCode": "SYSRENYUAN",
                            "orderIndex": 6,
                            "menuType": "FUNC",
                            "smallIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_yonghuguanli.png",
                            "id": "8a8a8834533fabde04533fabe29f0020",
                            "text": "用户管理",
                            "iconCls": "",
                            "issystem": 0
                        }
                    ],
                    "menuCode": "SYSTEMMANAGEMENT",
                    "orderIndex": 20,
                    "menuType": "MENU",
                    "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/xitong_icon.png",
                    "id": "8a8a8834533fa25c01533fa260a30000",
                    "text": "系统管理",
                    "iconCls": "",
                    "issystem": 0
                },{
                    "parent": "ROOT",
                    "menuLeaf": "GENERAL",
                    "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/kaoqin_icon.png",
                    "level": 2,
                    "nodeCode": "",
                    "leaf": false,
                    "isHidden": "0",
                    "treeid": "",
                    "menuTarget": "",
                    "children": [
                        {
                            "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                            "menuLeaf": "LEAF",
                            "bigIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kechenkaoqin.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "signup.mainlayout,core.good.signup.controller.MainController",
                            "children": [],
                            "menuCode": "STUDENTCLASS1",
                            "orderIndex": 1,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_kechenkaoqin.png",
                            "id": "8077B9ED-32FB-485A-A865-F9EECD101111",
                            "text": "教职工消费参数",
                            "iconCls": "",
                            "issystem": 0
                        }, {
                            "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                            "menuLeaf": "LEAF",
                            "bigIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_jiaoseguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "news.mainlayout,core.good.news.controller.MainController",
                            "children": [],
                            "menuCode": "STUDENTCLASS2",
                            "orderIndex": 2,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_jiaoseguanli.png",
                            "id": "8077B9ED-32FB-485A-A865-F9EECD10A212",
                            "text": "学员消费参数",
                            "iconCls": "",
                            "issystem": 0
                        },{
                            "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                            "menuLeaf": "LEAF",
                            "bigIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_jixujiaoyu.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "activity.mainlayout,core.good.activity.controller.MainController",
                            "children": [],
                            "menuCode": "STUDENTCLASS2",
                            "orderIndex": 2,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_jixujiaoyu.png",
                            "id": "8077B9ED-32FB-485A-A865-F9EECD101232",
                            "text": "教职工消费统计",
                            "iconCls": "",
                            "issystem": 0
                        },{
                            "parent": "D84F6F7A-B65C-47DA-9A50-7B2075CBE4D6",
                            "menuLeaf": "LEAF",
                            "bigIcon":comm.get("baseUrl")+"/static/core/resources/images/icon/index_yonghuguanli.png",
                            "level": 3,
                            "nodeCode": "",
                            "leaf": true,
                            "isHidden": "0",
                            "treeid": "",
                            "menuTarget": "activity.mainlayout,core.good.activity.controller.MainController",
                            "children": [],
                            "menuCode": "STUDENTCLASS2",
                            "orderIndex": 2,
                            "menuType": "FUNC",
                            "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/index_yonghuguanli.png",
                            "id": "8077B9ED-32FB-485A-A865-F9EECD10A342",
                            "text": "学员消费统计",
                            "iconCls": "",
                            "issystem": 0
                        },
                    ],
                    "menuCode": "NEWSTUDENT11",
                    "orderIndex": 3,
                    "menuType": "MENU",
                    "smallIcon": comm.get("baseUrl")+"/static/core/resources/images/icon/kaoqin_icon.png",
                    "id": "D84F6F7A-B65C-47DA-9A50-7B2075CBE336",
                    "text": "消费管理",
                    "iconCls": "",
                    "issystem": 0
                }
            ]
        );
        */
        this.callParent(arguments);  
    },
    layout:  'border',
    items : [{  
            xtype: 'app-header',
            height: 100,
            padding:0,  
            id: 'app-header',
            region : 'north', // 把他放在maintop的下面  
            bind:{
                hidden:'{!isMainBigHeader}'
            },  
        },{  
            xtype: 'app-header-small',
            height: 50,
            padding:0,  
            id: 'app-header-small',
            region : 'north', // 把他放在maintop的下面  
            hidden:true,
            bind:{
                hidden:'{!isMainSmallHeader}'
            },  
        },{  
            xtype : 'main.mainmenutree',  
            region : 'west', // 左边面板  
            width : 200,          
            split : true,
            collapsible:true,
            hidden:true,
            bind:{
                hidden:'{!isMainMenuTree}'
            },           
        },{  
            xtype : 'main.mainmenupanel',  
            region : 'west', // 左边面板  
            width : 200,          
            split : true,
            collapsible:true,
           
            bind:{
                hidden:'{!isMainMenu}'
            },           
        }, {  
            region : 'center', // 中间面版  
            xtype: 'app-main',
            minWidth:800
        }]  
});
Ext.define("core.main.view.Header",{
    extend: "Ext.toolbar.Toolbar",
    requires: [
        'core.main.view.menu.ButtonMainMenu'
    ],

    cls: 'toolbar-btn-shadow',

    xtype: 'app-header',
    items: [{ 
        	xtype: 'tbtext',  
            width:410,         
            html:'<div class="top_title">'+
                '<img class="index_logo" src="static/core/resources/images/login_logo.png" />'+
                '<img class="index_title" src="static/core/resources/images/index_title.png" />'+
            '</div>',
            /*
        	bind:{
				text: '{name}标题', 
        	},*/
         
         	id: 'app-header-title' 
        }, {
            // xtype: 'button', // default for Toolbars
            tooltip: '点击打开/关闭面板', 
            text:null,
            //text: '<span style="color:#fff;font-size: 14px;">收起</span>',
            iconCls: 'x-fa fa-chevron-circle-down header-button-color header-button-icon-size', 
            cls: 'core-header-button', 
            //overCls: '', 
            focusCls : '',    
            width:40,
            height:40,   
            listeners:{
                click:'onShowSystemPanel' 
            }
        },
        '->',{
            xtype:'container',    
            height:100,
            layout:'vbox',
            items:[{
                flex:1,
                xtype:'toolbar',
                cls:'appHeader-btnTbar',
                style:{
                    background: 'none'
                },
                items:[
                    '->',
                    { 
                        //tooltip: '手机考勤APP下载', 
                        text: '<span style="color:#fff;font-size: 14px;">手机考勤APP下载</span>',
                        iconCls: 'x-fa fa-android header-button-color', 
                        cls: 'core-header-button', 
                        //overCls: '', 
                        focusCls : '', 
                        changeType:'mainsmallheader',
                        listeners:{
                            mouseover : 'onShowQrCode',
                            mouseout:'onHideQrCode'
                        }
                    },
                    { 
                        tooltip: '收起', 
                        text: '<span style="color:#fff;font-size: 14px;">收起</span>',
                        iconCls: 'x-fa fa-angle-double-up header-button-color', 
                        cls: 'core-header-button', 
                        //overCls: '', 
                        focusCls : '', 
                        changeType:'mainsmallheader',
                        listeners:{
                            click:'onChangeMainHeader' 
                        },
                    },{ 
                        tooltip: '今日收信', 
                        bind:{
                            text: '<span style="color:#fff;font-size: 14px;">今日收信（{NewInfoNum}）</span>',
                        },
                        iconCls: 'x-fa fa-envelope header-button-color', 
                        cls: 'core-header-button', 
                        //overCls: '', 
                        focusCls : '', 
                        listeners:{
                            click:'onOpenPushInfo' 
                        },
                        //handler: 'onChangePassword' 
                    },
                    { 
                        tooltip: '清除缓存', 
                        text: '<span style="color:#fff;font-size: 14px;">清除缓存</span>',
                        iconCls: 'x-fa fa-eraser header-button-color', 
                        cls: 'core-header-button', 
                        //overCls: '', 
                        focusCls : '', 
                        listeners:{
                            click:'onWipeCache'
                        },
                    },
                    /*{ 
                        tooltip: '修改密码', 
                        text: '<span style="color:#fff;font-size: 14px;">修改密码</span>',
                        iconCls: 'x-fa fa-key header-button-color', 
                        cls: 'core-header-button', 
                        //overCls: '', 
                        focusCls : '', 
                        listeners:{
                            click:'onChangePassword' 
                        },
                        //handler: 'onChangePassword' 
                    },*/
                    { 
                        tooltip: '退出', 
                        text: '<span style="color:#fff;font-size: 14px;">退出</span>',
                        cls: 'core-header-button', 
                        //overCls: '', 
                        focusCls : '', 
                        iconCls: 'x-fa fa-sign-out header-button-color', 
                        listeners:{
                            click:'onExitSystem' 
                        },
                        //handler: 'onExitSystem' 
                    }
                ]
            },{
                flex:1,
                xtype: 'tbtext',
                width:'100%',
                bind:{
                    html: '<div class="index_welcome" style="text-align:right;margin-right: 10px;">'+
                        '<img style="float:none" src="static/core/resources/images/index_user_icon.png" width="20px" />'+
                        '<span style="float:right">欢迎您，{currentUser}！ 今天是{currentDateWeek} | 当前在线人数：{onlineNum}人</span>'+
                    '</div>', 
                },
            }]
        }
        
    ]
});
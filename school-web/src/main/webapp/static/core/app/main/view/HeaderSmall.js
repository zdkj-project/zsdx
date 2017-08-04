Ext.define("core.main.view.HeaderSmall",{
    extend: "Ext.toolbar.Toolbar",
    requires: [
        'core.main.view.menu.ButtonMainMenu'
    ],

    cls: 'toolbar-btn-shadow',

    xtype: 'app-header-small',
    items: [{ 
        	xtype: 'tbtext',           
            html:'<div class="top_title">'+
                '<img class="index_logo" src="static/core/resources/images/login_logo.png" />'+
                '<img class="index_title" src="static/core/resources/images/index_title.png" />'+
            '</div>',
            /*
        	bind:{
				text: '{name}标题', 
        	},*/
         
         	id: 'app-header-small-title' 
        },{
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
            tooltip: '展开', 
            text: '<span style="color:#fff;font-size: 14px;">展开</span>',
            iconCls: 'x-fa fa-angle-double-down header-button-color', 
            cls: 'core-header-button', 
            //overCls: '', 
            focusCls : '', 
            changeType:'mainbigheader',
            listeners:{
                click:'onChangeMainHeader' 
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
            //handler: 'onChangePassword' 
        },
        { 
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
        },
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
});
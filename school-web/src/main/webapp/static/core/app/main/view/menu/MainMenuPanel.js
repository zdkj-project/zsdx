

Ext.define('core.main.view.menu.MainMenuPanel', {  
    extend : 'Ext.panel.Panel',   //直接使用view，之前是view作为item，但是有bug
 
    alias : 'widget.main.mainmenupanel',         
    
    title : '功能菜单', 

    //iconCls:'x-fa fa-calendar-minus-o',
    header: {            
        title : '功能菜单',  
        iconCls:'x-fa fa-list',
        cls:'core-header-menu',
        items: [
            { 
                xtype: 'button', 
                padding:'0 5 0 0',
                cls: 'core-header-button', 
                iconCls: 'x-fa fa-toggle-on', 
                listeners:{
                    click:'onChangeMainMenu'
                },
                //handler: 'onChangeMainMenu',
                changeType:'mainmenutree',
                tooltip: '切换菜单' 
            }
        ]
    },
    
    layout:'fit',
 
    items:[{
        xtype:'menu',
        cls:'mainMenuCls',
        plain: true,
        floating: false,  // usually you want this set to True (default)
        items: [],
        defaults:{
             padding:'15 10',
             cls:'mainMenuItemCls',            
        },
        listeners:{
            click:'onMenuItemClick'
        }
    }/*{
        xtype:'menu',
        plain: true,
        floating: false,  // usually you want this set to True (default)
        items: [{
            text: 'plain item 1',
            iconCls:'x-fa fa-list'
        },{
            text: 'plain item 2'
        },{
            text: 'plain item 2'
        },{
            text: 'plain item 3',
            menu: {
                items: [
                    { text: 'plain item 1'} ,
                    { text: 'plain item 1'} ,   
                    { text: 'plain item 1'} ,
                    { text: 'plain item 1'} ,
                    { text: 'plain item 1'} ,
                    { text: 'plain item 1'} , 
                ]
            }
        }],
        listeners:{
            click:function( menu , item ){
                console.log(item);
            }
        } 
    }*/],
  
   
    initComponent : function() { 

                
       
        var datas = []; 
        var viweport=this.up("container[xtype=app-viewport]");  //获取主视图，然后再去取得它的viewport，
        var menus = viweport.getViewModel().get('systemMenu');  //而不能直接 this.getViewModel().get('systemMenu')，因为这个view没有声明viewModel
        
        var menusItems=[];
        //组装第一层菜单
        for (var i in menus) {  
            var menugroup = menus[i];  
            var children=menugroup.children;

            var menusItem={
                //text:'<img src="/static/core/resources/images/icon/index_zhiwuguanli.png" class="mainMenu-img"/><span style="font-size:15px;font-family:微软雅黑">'+menugroup.text+'</span>',               
                text:'<img src="'+menugroup.bigIcon+'" class="mainMenuPanel-img"/> '+menugroup.text,
                textBase:menugroup.text,
                //iconCls: "x-fa fa-link mainMenu-iconCls",
                menuCode:menugroup.menuCode,
                menuType: menugroup.menuType,  
                children: menugroup.children,
                smallIcon:menugroup.smallIcon,
                bigIcon: menugroup.bigIcon,
                menuTarget:menugroup.menuTarget,
                menuParent:menugroup["parent"]
            };

            var menuSecondItem=[];
            for(var j in children){
                var menuChild = children[j];    
                //小图标 
                var smallIconCls=menuChild.smallIcon;
                if (!smallIconCls) {
                    smallIconCls="x-fa fa-bars";
                }

                menuSecondItem.push({
                    //text:'<img src="'+menuChild.bigIcon+'" class="mainMenuPanel-img" style="width:20px;height:20px;margin-top: 5px;"/> '+menuChild.text,
                    text:menuChild.text,
                    textBase:menuChild.text,
                    //iconCls: "x-fa fa-bars",
                    iconCls:smallIconCls+" mainMenuIconCls",
                    menuCode:menuChild.menuCode,
                    menuType: menuChild.menuType,  
                    children: menuChild.children,
                    smallIcon:menuChild.smallIcon,
                    bigIcon: menuChild.bigIcon,
                    menuTarget:menuChild.menuTarget,
                    menuParent:menuChild["parent"]
                });
            }

            if(menuSecondItem.length>0){
                menusItem.menu={
                    defaults:{
                        padding:'3',
                        cls:'mainMenuSecondItemCls',              
                    },
                    items:menuSecondItem,                
                    listeners:{
                        click:'onMenuItemClick'
                    }
                }
            }
/*
            datas.push({                  
                text :menugroup.text,                               
                iconCls : '',   //使用系统自定义几种，即可，在显示tab时判断来设置此值
                menuCode:menugroup.menuCode,
                menuType: menugroup.menuType,  
                children: menugroup.children,
                bigIcon: menugroup.bigIcon,
                menuTarget:menugroup.menuTarget
            });  
*/
            menusItems.push(menusItem);
        }  

        this.items[0].items=menusItems;

        /*之前使用button的做法
        this.items = []; 
        var viweport=this.up("container[xtype=app-viewport]");  //获取主视图，然后再去取得它的viewport，
        var menus = viweport.getViewModel().get('systemMenu');  //而不能直接 this.getViewModel().get('systemMenu')，因为这个view没有声明viewModel
        
        //组装第一层菜单
        for (var i in menus) {  
            var menugroup = menus[i];  

            if(!menugroup.iconCls)  //若没有图标，使用一个默认的
                menugroup.iconCls='x-fa fa-bars';

            this.items.push({  
                xtype : 'buttontransparent',  
                text : "<span style='color:red;font-size:14px'>"+this.addSpace(menugroup.text, 12)+"</span>",   
                textValue:menugroup.text,                               
                iconCls : menugroup.iconCls+' mainmenu-button-icon-color',
                iconClsValue:menugroup.iconCls,
                handler : 'onMainMenuClick',
                menuCode:menugroup.menuCode,
                width: '100%', 
                cls: 'main-mainmenu-button', 
                menuType: menugroup.menuType,  
                children: menugroup.children                        
            });  
        }  */

        this.callParent(arguments);  
    },  

    addSpace : function(text, len) {  
        var result = text;  
        for (var i = text.length; i < len; i++) {  
            result += '　';  
        }  
        return result;  
    }  
}) 
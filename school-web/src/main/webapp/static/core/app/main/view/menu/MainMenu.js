

Ext.define('core.main.view.menu.MainMenu', {  
    extend : 'Ext.view.View',   //直接使用view，之前是view作为item，但是有bug
    /* 
    requires: [
        'Ext.ux.ButtonTransparent'
    ],
    */
    alias : 'widget.main.mainmenu',         
    //viewModel : 'main.mainModel',     若在多个view 中声明，则会生成多份,所以，在下面的代码中，从viewport中获取viewmodel数据
    
    /*
    title : '功能菜单',  
    //iconCls:'x-fa fa-calendar-minus-o',
    header: {
       
        title : '功能菜单',  
        iconCls:'x-fa fa-calendar-minus-o',
        cls:'core-header-menu',
        items: [
            { 
                xtype: 'button', 
                padding:0,
                cls: 'core-header-button', 
                iconCls: 'x-fa fa-exchange', 
                handler: 'onChangeMainMenu',
                changeType:'mainmenutree',
                tooltip: '切换菜单' 
            }
        ]
    },
    */
    //layout:'column',
    //scrollable :true,

    /*使用dataview来显示列表*/
    layout:'fit',    
    //items:[{
    //    xtype:'dataview',
    tpl:new Ext.XTemplate(
        '<tpl for=".">',
            '<div class="mainMenu-wrap">',
                '<tpl if="bigIcon !=\'\' ">',  // <-- Note that the > is encoded
                    '<img src="{bigIcon}" class="mainMenu-img"/>',
                '<tpl elseif="iconCls !=\'\' ">',
                    '<i class="{iconCls} mainMenu-icon" aria-hidden="true"></i>', 
                '<tpl else>',
                    '<i class="fa fa-university mainMenuIcon-icon" aria-hidden="true"></i>',
                '</tpl>',            
                '<span class="mainMenu-text">{text}</span>',
            '</div>',
        '</tpl>'
    ),
    itemSelector: 'div.mainMenu-wrap',
    emptyText: '没有下级菜单了',
    scrollable :true,
  
    listeners: {
        itemclick:'onViewIconItemClick'  //会去控制器里找到对应的方法
    },
    //}],
    initComponent : function() { 

        var dataview=this;
       
        var datas = []; 
        var viweport=this.up("container[xtype=app-viewport]");  //获取主视图，然后再去取得它的viewport，
        var menus = viweport.getViewModel().get('systemMenu');  //而不能直接 this.getViewModel().get('systemMenu')，因为这个view没有声明viewModel
        
        //组装第一层菜单
        for (var i in menus) {  
            var menugroup = menus[i];  
         
            datas.push({                  
                text :menugroup.text,                               
                iconCls : '',   //使用系统自定义几种，即可，在显示tab时判断来设置此值
                menuCode:menugroup.menuCode,
                menuType: menugroup.menuType,  
                children: menugroup.children,
                bigIcon: menugroup.bigIcon,
                menuTarget:menugroup.menuTarget
            });  
        }  

        dataview.store = Ext.create('Ext.data.Store', {    
            fields: [
                'id','text', 'iconCls', 'leaf','children','menuCode','menuType','bigIcon','menuTarget'
            ],
            data:datas                
        });

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
/** 
 * 树状菜单，显示在主界面的左边 
 */  
Ext.define('core.main.view.menu.MainMenuTree', {  
            extend : 'Ext.tree.Panel',  
            alias : 'widget.main.mainmenutree',  
            title : '功能菜单',  
            //iconCls:'x-fa fa-calendar-minus-o',
            rootVisible : false,  
            lines : true,  
            header: {
           
                title : '功能菜单',  
                iconCls:'x-fa fa-list',
                cls:'core-header-menu',
                items: [
                    { 
                        xtype: 'button', 
                        padding:'0 5 0 0',
                        cls: 'core-header-button', 
                        iconCls:'x-fa fa-toggle-off', 
                        listeners:{
                            click:'onChangeMainMenu'
                        },
                        //handler: 'onChangeMainMenu',

                        changeType:'mainmenu',
                        tooltip: '切换菜单' 
                    }
                ]
            },
            listeners:{
                itemclick :'onMainMenuTreeClick'
            },
            useArrows:true,
            cls:'mainmenutree-cls',
            // /viewModel : 'main.mainModel',  
            

            initComponent : function() {  

                var viweport=this.up("container[xtype=app-viewport]");  //获取主视图，然后再去取得它的viewport，
                var menus = viweport.getViewModel().get('systemMenu');  //而不能直接 this.getViewModel().get('systemMenu')，因为这个view没有声明viewModel



                this.store = Ext.create('Ext.data.TreeStore', {  
                    root : {  
                        text : '系统菜单',  
                        leaf : false,  
                        expanded : true  
                    }  
                });  


                var root = this.store.getRootNode();  
                for (var i in menus) {  
                    var menugroup = menus[i];  
                             
                    var childrens=[];
                    function getChildren(children){     //递归的方式 拼接child
                        var child=[];
                        for (var j in children) {  
                            var menumodule = children[j];  
                            var isLeaf=true;

                            var nextchild=menumodule.children;
                            
                            if(nextchild.length==0){
                                isLeaf=true;                        
                            }else{
                                isLeaf=false;
                                nextchild=getChildren(nextchild);
                            }
                            var childnode = {                   
                                text : menumodule.text,  
                                leaf : isLeaf,
                                menuCode:menumodule.menuCode,                            
                                menuType: menumodule.menuType,                          
                                menuTarget:menumodule.menuTarget,
                                children: nextchild,
                                bigIcon:menumodule.bigIcon ,
                                icon: menumodule.bigIcon,
                                iconCls:'MainMenuTreeIconStyle'
                            };  
                            child.push(childnode);  
                        }  
                        return  child;              
                    }
                    childrens=getChildren(menugroup.children);
                
                    var menuitem = root.appendChild({  
                        text : menugroup.text,  
                        expanded : menugroup.expanded, 
                        menuCode:menugroup.menuCode,                            
                        menuType: menugroup.menuType,  
                        children: childrens,
                        menuTarget:menugroup.menuTarget,
                        bigIcon:menugroup.bigIcon ,
                        icon: menugroup.bigIcon,
                        iconCls:'MainMenuTreeIconStyle'
                    }); 
                } 

                this.callParent(arguments);  
            }  
        })  
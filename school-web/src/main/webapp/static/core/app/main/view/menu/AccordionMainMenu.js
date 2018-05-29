Ext.define('core.main.view.menu.AccordionMainMenu', {  
            extend : 'Ext.panel.Panel',  
            alias : 'widget.main.mainmenuaccordion',  
            title : '功能菜单',  
            //iconCls:'x-fa fa-calendar-minus-o',
            
            header: {
           
                title : '功能菜单',  
                iconCls:'x-fa fa-calendar-minus-o',
                items: [
                    { 
                        xtype: 'button', 
                        padding:0,
                        cls: 'core-header-button', 
                        iconCls: 'x-fa fa-exchange', 
                        handler: 'onChangeMainMenu',
                        changeType:'mainmenu',
                        tooltip: '切换菜单' 
                    }
                ]
            },

            layout : {  
                type : 'accordion',  
                animate : true  
            },  
            
            viewModel : 'main.mainModel',  
  
            initComponent : function() {  
                this.items = [];  
                var menus = this.getViewModel().get('systemMenu');  
                for (var i in menus) {  
                    var menugroup = menus[i];  
                    var accpanel = {  
                        menuAccordion : true,  
                        xtype : 'panel',  
                        title : menugroup.text,  
                        bodyStyle : {  
                            padding : '10px'  
                        },  
                        layout : 'fit',  
                        dockedItems : [{  
                                    dock : 'left',  
                                    xtype : 'toolbar',  
                                    items : []  
                                }],  
                        //iconCls:menugroup.iconCls,
                    };  
                    for (var j in menugroup.items) {  
                        var menumodule = menugroup.items[j];  
                        accpanel.dockedItems[0].items.push({  
                                    xtype : 'buttontransparent',  
                                    text : this.addSpace(menumodule.text, 12),                                  
                                    iconCls : menumodule.iconCls,
                                    handler : 'onMainMenuClick'  
                                });  
                    }  
                    this.items.push(accpanel);  
                }  
                this.callParent(arguments);  
            },  
  
            addSpace : function(text, len) {  
                console.log(text.length);  
                var result = text;  
                for (var i = text.length; i < len; i++) {  
                    result += '　';  
                }  
                return result;  
            }  
    }) 
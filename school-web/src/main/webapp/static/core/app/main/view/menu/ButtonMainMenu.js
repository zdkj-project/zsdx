/** 
 * 显示在顶部的按钮菜单，可以切换至标准菜单，菜单树 
 */  
Ext.define('core.main.view.menu.ButtonMainMenu', {  

    extend : 'Ext.ux.ButtonTransparent',  

    alias : 'widget.main.buttonmainmenu',  

    viewModel : 'main.mainModel',  

    text : '菜单',  
    iconCls:'x-fa fa-calendar-minus-o header-button-color',
    cls: 'simplecms-header-button',
    initComponent : function() {  
      
        this.menu = this.getViewModel().getMenus();  

        this.callParent();  
    }  

})  
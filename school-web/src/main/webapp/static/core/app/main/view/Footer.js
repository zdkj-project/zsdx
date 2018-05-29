Ext.define("core.main.view.Footer",{
    extend: "Ext.toolbar.Toolbar",

    cls: 'toolbar-btn-shadow',

    xtype: 'app-footer',

    items: [
        { 
        	xtype: 'tbtext',
        	
			
                bind:{
                    html: '当前在线人数：{onlineNum}人</span>', 
                },
        	
        },
        '->',
        { 
            xtype: 'tbtext',
            bind:{
                text: '{name}', 
            },
        },
    ]
});
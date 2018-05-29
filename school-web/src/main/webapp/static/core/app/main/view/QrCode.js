
Ext.define('core.main.view.QrCode', {
    extend: 'Ext.window.Window',

    alias : 'widget.main.qrcode', 
    id:'app-qrcode',

    width: 300,
    layout: 'fit', 
    title:null,
    draggable :false,
    resizable :false,
    closable:false,
    items: {  // Let's put an empty grid in just to illustrate fit layout
        xtype:'image',
        src: 'static/core/resources/images/QrCode.png',
        width: 300,
        height: 300
    }
});
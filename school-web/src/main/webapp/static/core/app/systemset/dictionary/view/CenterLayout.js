Ext.define("core.systemset.dictionary.view.CenterLayout", {
    extend: "Ext.panel.Panel",
    alias: 'widget.dic.centerlayout',
    layout: "border",
    border:false,
    items: [{
        xtype: "dic.itemgrid",
        region: "center"
    }]
})
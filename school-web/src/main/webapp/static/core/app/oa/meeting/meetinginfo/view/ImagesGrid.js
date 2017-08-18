

var imageTpl = new Ext.XTemplate(
    '<tpl for=".">',
        '<div class="imagegrid thumb-wrap">',
            '<div class="thumb"><div class="img" style="background-image:url({attachUrl}) ; " title="{attachName}"></div></div>',
            '<span class="x-editable">{attachName}</span>',
        '</div>',
    '</tpl>',
    '<div class="x-clear"></div>'
/*
    '<tpl for=".">',
        '<div style="margin-bottom: 10px;" class="imagegrid thumb-wrap">',
          '<img src="{imageUrl}" />',
          '<br/><span>{imageTitle}</span>',
        '</div>',
    '</tpl>'*/
);


Ext.define("core.oa.meeting.meetinginfo.view.ImagesGrid", {
    extend: "Ext.panel.Panel",
    alias: "widget.meetinginfo.imagesgrid",
    frame: false,
    //width: 535,
    autoHeight: true,
    //collapsible: true,
    layout: 'fit',
    //title:'考勤信息图片',
    items:[{
        xtype:'dataview',
        padding:5,
        store: {
            type: 'meetinginfo.imageinfoStore',
        },
        tpl: imageTpl,
        style:{
            overflow:'overlay'
        },
        trackOver: true,  
        overItemCls: 'x-item-hover',  
        itemSelector:'div.thumb-wrap',
        multiSelect: true,
        emptyText:'<div style="width:100%;line-height: 100px;text-align:center">暂无活动图片数据！</div>',
        // plugins: [
        //     Ext.create('Ext.ux.DataView.DragSelector', {})
        // ]
    }],
    tbar: [
        { xtype: 'button', text: '上传图片', ref: 'gridUploadImage', iconCls: 'x-fa fa-plus-circle'},
        { xtype: 'button', text: '删除图片', ref: 'gridDeleteImage', iconCls: 'x-fa fa-minus-circle'},
    ],
    dockedItems: [{
        xtype: 'pagingtoolbar',
        //将stroe的数据源，放置在控制器中进行绑定，在这里写有bug
        /*store:{
            type: 'meetinginfo.imageinfoStore',
        },*/
        //store: Ext.data.StoreManager.lookup('meetinginfo.imageinfoStore'),
        dock: 'bottom',
        displayInfo: true
    }],
   
    //排序字段及模式定义
});
Ext.define("core.ordermanage.teacherorder.view.DetailHtmlPanel", {
	extend:"Ext.Container",
	alias: "widget.teacherorder.detailhtmlpanel",
	
    //bodyPadding: '0 10 10 0',
    margin:'0 0 0 10',
    scrollable:true, 
    width:'99%',
	items: [{        
        xtype:'container',
        ref:'orderDescInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo" style="padding: 5px 20px 5px 5px">',
                '<div class="trainClass_title">订餐说明：</div>',
                '<ul style="height: 190px;overflow: auto;">'+
                    '<li style="width:100%">{orderDesc}</li>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>'
        ),
        data:{  }
        
    }]
});

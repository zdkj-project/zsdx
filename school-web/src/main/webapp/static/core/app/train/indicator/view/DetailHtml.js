Ext.define("core.train.indicator.view.DetailHtml", {
    extend:"Ext.Container",
    alias: "widget.indicator.detailhtml",

    //bodyPadding: '0 10 10 0',
    margin:'0 0 0 10',
    scrollable:true,
    width:'100%',
    items: [{
        xtype:'container',
        ref:'indicatorInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
            '<div class="trainClass_title">指标基本信息：</div>',
            '<ul>'+
            '<li>指标名称：{indicatorName}</li>',
            // '<li>评估对象：{indicatorObject}</li>',
            '<tpl if="indicatorObject==1">',
            '<li>评价类型：课程评价</li>',
            '<tpl elseif="indicatorObject==2">',
            '<li>评价类型：管理评价</li>',
            '<tpl elseif="indicatorObject==3">',
            '<li>评价类型：课程/管理评价</li>',
            '</tpl>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data:{  }
    },{
        xtype:'container',
        ref:"indicatorStand",
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
            '<div class="trainClass_title">指标评价标准列表：</div>',
            '<ul class="trainClass_gridUl">',
            '<li><span style="width:5%">序号</span><span style="width:90%" data-align="center">评价标准</span></li>',
            '{% if (values.length == 0) %}',
            '<li style="width:100%;font-size: 20px;font-weight: 400;text-align: center;line-height: 100px;">此指标暂无评价标准...</li>',
            '{% if (values.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
            '<tpl for="rows">',
            '<li><span style="width:5%">{[xindex]}</span><span style="width: 90%;text-align:left;" >{indicatorStand}</span></li>',
            '</tpl>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data:{  }
    }]
});

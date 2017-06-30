Ext.define("core.train.course.view.DetailHtmlPanel", {
    extend: "Ext.Container",
    alias: "widget.course.detailhtmlpanel",
    layout: "form", //从上往下布局
    autoHeight: true,
    frame: false,
    //bodyPadding: '0 10 10 0',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: "：", // 分隔符
        msgTarget: "qtip",
        labelWidth: 100,
        labelAlign: "right"
    },
    scrollable:true,
    /*
    html:'<div class="teacherDetailPanel">'+
    
        '<img alt="" src="/u/cms/www/201408/11170153xsa9.png"/>'+
       
        '<p>'+
            '<strong>陈松生，</strong>讲师，硕士研究生，信息网络中心副主任。'+
        '</p>'+    
        '<p>'+
            '<strong>主要研究方向：</strong>'+
            '政府信息化、媒介素养、网络素养、网络舆情、应急管理等。'+
        '</p>'+        
        '<p>'+
            '<strong>主要研究成果：</strong>'+
            '主持和参与《新媒体背景下中山市公务员媒介素养现状与对策研究》、《大数据时代中山市政府公共服务的转型升级研究》等十多项课题研究。发表学术论文十余篇。'+
        '</p>'+        
        '<p>'+
            '<strong>主要讲授专题：</strong>'+
            '《新媒体时代干部媒介素养的提升》、《运用新媒体创新社会治理》、《运用新媒体创新青年工作》、《网络舆情的引导与应对》、《大数据时代政府的转型升级》、《走好网上群众路线》、《突发事件的应急管理与媒体应对》、《如何让你的PPT高大上》等专题。'+
        '</p>'+     
        '<div stlye="clear:both;"></div>'+  
    '</div>'*/
    tpl: new Ext.XTemplate(
        '<tpl for=".">',
            '<div class="trainTeacher_teacherInfo" style="width: 98%; margin: 10px auto 20px auto;  border-radius: 10px;box-shadow: 0px 0px 10px hsl(4, 30%, 87%);">',
                '<div class="trainTeacher_left">',
                    '<span class="zp" style="background-image: url({zp})"></span>',
                    '<span class="xm">{xm}</span>',
                    '<span>性别：{xbm}</span>',
                    '<span>职称：{technical}</span>',
                    '<span title={position}>职务：{position}</span>',
                    '<span>学历：{xlm}</span>',
                    '<span>专业：{zym}</span>',
                '</div>',
                '<div class="trainTeacher_right">',
                    '<ul style="padding-top: 10px;">',
                        '<li>电话：{mobilePhone}</li>',
                        '<li title={sfzjh}>身份证号码：{sfzjh}</li>',
                        '<li>电子邮件：{dzxx}</li>',
                        '<li>校内/校外：{inout}</li>',
                        '<li>行政级别：{headshipLevel}</li>',
                        '<li title={workUnits}>工作单位：{workUnits}</li>',
                        '<div style="clear:both"></div>',
                    '</ul>',
                    '<div class="trainTeacher_desc">',
                        '<div class="wrap">',
                            '<strong>教师简介：</strong>',
                            '<span>{teaDesc}</span>',
                        '</div>',    
                        '<div class="wrap">',
                            '<strong>主要研究方向：</strong>',
                            '<span>{researchArea}</span>',
                        '</div>',    
                        '<div class="wrap">',
                            '<strong>主要研究成果：</strong>',
                            '<span>{researchResult}</span>',
                        '</div>', 
                        '<div class="wrap">',                        
                            '<strong>主要讲授专题：</strong>',
                            '<span>{teachingProject}</span>',                           
                        '</div>',  
                    '</div>',
                '</div>',
            '</div>',
        '</tpl>'
    ),
    data:{  }
    /*
    tpl: new Ext.XTemplate(
        '<div class="teacherDetailSlideDiv">',
            '{% if (values.length == 0) %}', 
                '<div style="font-size: 20px;font-weight: 400;text-align: center;line-height: 400px;">没有设置主讲老师</div>',
            '{% if (values.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
                '<i id="teacherDetailSlideDiv_left" class="fa fa-chevron-left" aria-hidden="true" style="left: 0px;top: 45%;"></i>',
                '<i id="teacherDetailSlideDiv_right" class="fa fa-chevron-right" aria-hidden="true" style="right: 0px;top: 45%;"></i>',
                '<div id="teacherDetailPanelMaxing_Wrap" class="teacherDetailPanelMaxing" style="width:{[820*values.length]}px" >',
                '<tpl for=".">',
                    '<div class="teacherDetailPanel">',
                        '<img alt="" src="{zp}"/>',
                        '<p>',
                            '<strong>{xm}</strong>',
                            '<tpl if="position">，{position}</tpl>',
                            '<tpl if="xlm">，{xlm}</tpl>', 
                            '<tpl if="teaDesc">，{teaDesc}</tpl>',
                        '</p>',    
                        '<p>',
                            '<strong>主要研究方向：</strong>',
                            '{resarchArea}',
                        '</p>',    
                        '<p>',
                            '<strong>主要研究成果：</strong>',
                            '{researchResult}',
                        '</p>', 
                        '<p>',
                            '<tpl if="teachingProject">',
                                '<strong>主要讲授专题：</strong>',
                                '{teachingProject}',
                            '</tpl>',      
                        '</p>',  
                        '<div stlye="clear:both;"></div>',
                    '</div>',
                '</tpl>',           
                '</div>',   
        '</div>'
    ),
    data:{  }*/
});
Ext.define("core.train.calcucredit.view.DetailHtml", {
    extend:"Ext.Container",
    alias: "widget.calcucredit.detailhtml",

    //bodyPadding: '0 10 10 0',
    margin:'0 0 0 10',
    scrollable:true,
    width:'100%',
    items: [{
        xtype:'container',
        ref:'classTraineeInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
            '<div class="trainClass_title">学员基本信息：</div>',
            '<ul>'+
            '<li>姓名：{xm}</li>',
            '<tpl if="xbm==1">',
            '<li>性别：男</li>',
            '<tpl elseif="xbm==2">',
            '<li>性别：女</li>',
            '</tpl>',
            '<li>移动电话：{mobilePhone}</li>',
            '<li>职务：{position}</li>',
            '<li>所在单位：{workUnit}</li>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data:{  }
    },{
        xtype:'container',
        ref:"classTraineeCredits",
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
            '<div class="trainClass_title">所获学分明细：</div>',
            '<ul class="trainClass_gridUl trainClassTrainee_gridUl">',
            '<li><span style="width:5%">序号</span><span style="width:20%" data-align="center">班级名称</span><span style="width:20%" data-align="center">课程名称</span>' +
            '<span style="width:10%" data-align="center">上课日期</span><span style="width:10%" data-align="center">上课时间</span>' +
            '<span style="width:10%" data-align="center">课程学分</span></li>',
            '{% if (values.length == 0) %}',
            '<li style="width:100%;font-size: 20px;font-weight: 400;text-align: center;line-height: 100px;">此指标暂无评价标准...</li>',
            '{% if (values.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
            '<tpl for="rows">',
            '<li><span style="width:5%">{[xindex]}</span><span style="width: 20%;text-align:left;" >{className}</span><span style="width: 20%;text-align:left;" >{courseName}</span>' +
            '<span style="width: 10%;text-align:left;" >{courseDate}</span><span style="width: 10%;text-align:left;" >{courseTime}</span>' +
            '<span style="width: 10%;text-align:left;" >{courseCredits}</span></li>',
            '</tpl>',
            '<div style="clear:both"></div>',
            '</ul>',
            '</div>'
        ),
        data:{  }
    }]
});

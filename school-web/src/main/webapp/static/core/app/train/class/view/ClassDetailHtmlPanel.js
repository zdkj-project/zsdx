Ext.define("core.train.class.view.ClassDetailHtmlPanel", {
	extend:"Ext.Container",
	alias: "widget.class.classdetailhtmlpanel",
	
    //bodyPadding: '0 10 10 0',
    margin:'0 0 0 10',
    scrollable:true, 
    width:'100%',
	items: [{        
        xtype:'container',
        ref:'classInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
                '<div class="trainClass_title">班级基本信息列表：</div>',
                '<ul>'+
                    '<li>班级名称：{className}</li>',
                    '<li>班级类型：{classCategory}</li>',
                    '<li>开始日期：{beginDate}</li>',
                    '<li>结束日期：{endDate}</li>',
                    '<li>班主任：{bzr}</li>',
                    '<li>联系人：{contactPerson}</li>',
                    '<li>联系电话：{contactPhone}</li>',
                    '<li>是否考勤：{needChecking}</li>',
                    '<li>提交状态：{isUse}</li>',
                    '<li>学员人数：{traineeNum}人</li>',
                    '<li>早餐人数：{foodBreakfastNum}人</li>',
                    '<li>午餐人数：{foodLunchNum}人</li>',
                    '<li>晚餐人数：{foodDinnerNum}人</li>',
                    '<li>午休人数：{roomSiestaNum}人</li>',
                    '<li>晚宿人数：{roomSleepNum}人</li>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>'
        ),
        data:{  }
        
    },{
        xtype:'container',
        ref:'classTraineeInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
                '<div class="trainClass_title">班级学员列表：</div>',
                '<ul class="trainClass_gridUl">',
                    '<li><span style="width:5%">序号</span><span style="width:10%">姓名</span><span style="width:5%">性别</span><span>移动电话</span><span>身份证号码</span><span style="width:20%">所在单位</span><span style="width:10%">职务</span><span style="width:10%">行政级别</span><span style="width:10%">学员状态</span></li>',                        
                '{% if (values.length == 0) %}', 
                    '<li style="width:100%;font-size: 13px;font-weight: 400;text-align: center;line-height: 100px;">此班级暂无学员信息...</li>',
                '{% if (values.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
                    '<tpl for=".">',
                        '<li><span style="width:5%">{[xindex]}</span><span style="width:10%">{xm}</span><span style="width:5%">{xbm}</span><span>{phone}</span><span title="{sfzjh}">{sfzjh}</span><span style="width:20%" title="{workUnit}">{workUnit}</span><span style="width:10%">{position}</span><span style="width:10%">{headshipLevel}</span><span style="width:10%">{isDelete}</span></li>',        
                    '</tpl>',           
                    '<div style="clear:both"></div>',
                '</ul>',   
            '</div>'
        ),
        data:{  }
    },{
        xtype:'container',
        ref:'classCourseInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
                '<div class="trainClass_title">班级课程列表：</div>',
                '<ul class="trainClass_gridUl">',
                    '<li><span style="width:5%">序号</span><span style="width:20%">课程名称</span><span style="width:10%">教学形式</span><span>开始时间</span><span>结束时间</span><span style="width:10%">讲师</span><span style="width:8%">授课地点</span><span style="width:7%">是否评价</span><span style="width:10%">课程状态</span></li>',
                '{% if (values.length == 0) %}', 
                    '<li style="width:100%;font-size: 13px;font-weight: 400;text-align: center;line-height: 100px;">此班级暂无课程信息...</li>',
                '{% if (values.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
                    '<tpl for=".">',
                        '<li><span style="width:5%">{[xindex]}</span><span style="width:20%" title="{courseName}">{courseName}</span><span style="width:10%">{teachType}</span><span>{beginTime}</span><span>{endTime}</span><span style="width:10%"  title="{teacher}">{teacher}</span><span style="width:8%" title="{address}">{address}</span><span style="width:7%">{isEval}</span><span style="width:10%">{isDelete}</span></li>',
                    '</tpl>',           
                    '<div style="clear:both"></div>',
                '</ul>',   
            '</div>'
        ),
        data:{  }    
    },{
        xtype:'container',
        ref:'classFoodInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
                '<div class="trainClass_title">学员就餐信息：</div>',
                '<div class="trainClass_classFoodInfo">',
                '<ul>',
                '<tpl if="dinnerType==1">',
                    '<li>就餐类型：{dinnerTypeName}</li>',
                    '<li style="width:80%">每围人数：{avgNumber}人</li>',
                '<tpl elseif="dinnerType==2">',
                    '<li style="width:100%">就餐类型：{dinnerTypeName}</li>',
                '<tpl else>',
                    '<li>就餐类型：{dinnerTypeName}</li>',
                '</tpl>',
                '<li>早餐餐标：{breakfastStand}元</li>',
                '<li>午餐餐标：{lunchStand}元</li>',
                '<li>晚餐餐标：{dinnerStand}元</li>',
                '<tpl if="dinnerType==1">',
                    '<li>早餐围数：{breakfastCount}围</li>',
                    '<li>午餐围数：{lunchCount}围</li>',
                    '<li>晚餐围数：{dinnerCount}围</li>',
                '<tpl elseif="dinnerType==2">',
                    '<li>早餐人数：{breakfastCount}人</li>',
                    '<li>午餐人数：{lunchCount}人</li>',
                    '<li>晚餐人数：{dinnerCount}人</li>',
                '</tpl>',
                '<div style="clear:both"></div>',   
                '</ul></div>',
                '{% if (values.dinnerType == 1 || values.dinnerType == 2) return  %}', 
                '<ul class="trainClass_gridUl" style="border-top:none;">',
                    '<li><span style="width:5%">序号</span><span style="width:20%">姓名</span><span style="width:5%">性别</span><span style="width:20%">是否早餐</span><span style="width:20%">是否午餐</span><span style="width:20%">是否晚餐</span><span style="width:10%">学员状态</span></li>',
                    '{% if (values.rows.length == 0) %}', 
                    '<li style="width:100%;font-size: 13px;font-weight: 400;text-align: center;line-height: 100px;">此班级暂无学员就餐信息...</li>',
                    '{% if (values.rows.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
                    '<tpl for="rows">',
                        '<li><span style="width:5%">{[xindex]}</span><span style="width:20%">{xm}</span>',
                            '<span style="width:5%"><tpl if="xbm == 1">男<tpl else>女</tpl></span>',
                            '<span style="width:20%"><tpl if="breakfast == 1">是<tpl else>否</tpl></span>',
                            '<span style="width:20%"><tpl if="lunch == 1">是<tpl else>否</tpl></span>',
                            '<span style="width:20%"><tpl if="dinner == 1">是<tpl else>否</tpl></span>',
                            '<span style="width:10%"><tpl if="isDelete == 0">正常<tpl elseif="isDelete == 1">取消<tpl elseif="isDelete == 2">新增</tpl></span>',
                        '</li>',
                    '</tpl>',           
                    '<div style="clear:both"></div>',
                '</ul>',   
            '</div>'
        ),
        data:{  } 
    },{
        xtype:'container',
        ref:'classRoomInfo',
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classTraineeInfo">',
                '<div class="trainClass_title">学员住宿信息：</div>',
                '<ul class="trainClass_gridUl">',
                    '<li><span style="width:5%">序号</span><span style="width:20%">姓名</span><span style="width:5%">性别</span><span style="width:30%">是否午休</span><span style="width:30%">是否晚宿</span><span style="width:10%">学员状态</span></li>',
                '{% if (values.length == 0) %}', 
                    '<li style="width:100%;font-size:13px;font-weight: 400;text-align: center;line-height: 100px;">此班级暂无学员住宿信息...</li>',
                '{% if (values.length == 0 ) return  %}',   //reutrun 表示不执行下面的了，在for里面可以使用break、continue
                    '<tpl for=".">',
                        '<li><span style="width:5%">{[xindex]}</span><span style="width:20%">{xm}</span>',
                            '<span style="width:5%"><tpl if="xbm == 1">男<tpl else>女</tpl></span>',
                            '<span style="width:30%"><tpl if="siesta == 1">是<tpl else>否</tpl></span>',
                            '<span style="width:30%"><tpl if="sleep == 1">是<tpl else>否</tpl></span>',
                            '<span style="width:10%"><tpl if="isDelete == 0">正常<tpl elseif="isDelete == 1">取消<tpl elseif="isDelete == 2">新增</tpl></span>',
                        '</li>',
                    '</tpl>',           
                    '<div style="clear:both"></div>',
                '</ul>',   
            '</div>'
        ),
        data:{  }    
    }]
});

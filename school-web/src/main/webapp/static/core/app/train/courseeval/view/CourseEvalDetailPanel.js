    
    /**
     * Created by luoyibo on 2017-06-06.
     */
    Ext.define('core.train.courseeval.view.CourseEvalDetailPanel', {
        extend: 'Ext.Container',
        alias: "widget.courseeval.coursevaldetailpanel",
        //xtype:'courseeval.coursevaldetailpanel',
        style: {
            border: '1px solid #ddd'
        },
       
        margin:'0 0 0 10',
        scrollable:true, 
        width:'100%',
      
/*
       data:{
                "courseName": "行政决策能力",
                "teachTypeName": "现场教学",
                "classScheduleId": "2a52e332-0284-48cc-bffb-8bdb762c5ada",
                "teacherName": "梁艳霞",
                "advise": "|sdsdf",
                "verySatisfaction": "6.25",
                "standList": [
          [
            {
                "INDICATOR_NAME": "教学内容",
                "INDICATOR_STAND": "紧密联系实际，针对性、实用性、系统性强;内容丰富，前瞻性、新颖性、时效性强。",
                "VERY_SATISFACTIONCOUNT": 1,
                "SATISFACTIONCOUNT": 2,
                "BAS_SATISFACTIONCOUNT": 0,
                "NO_SATISFACTIONCOUNT": 1,
                "VERY_SATISFACTION": 25,
                "SATISFACTION": 75,
                "CLASS_SCHEDULE_ID": "2a52e332-0284-48cc-bffb-8bdb762c5ada",
                "INDICATOR_ID": "69bfb3d8-cecd-483e-a930-dd5e20e7cdcd"
            }
        ],
        [
            {
                "INDICATOR_NAME": "教学效果",
                "INDICATOR_STAND": "激发学习兴趣，启发思维，课堂气氛活跃；开阔视野、拓宽思路、增长知识，实用性强。",
                "VERY_SATISFACTIONCOUNT": 1,
                "SATISFACTIONCOUNT": 0,
                "BAS_SATISFACTIONCOUNT": 2,
                "NO_SATISFACTIONCOUNT": 1,
                "VERY_SATISFACTION": 25,
                "SATISFACTION": 25,
                "CLASS_SCHEDULE_ID": "2a52e332-0284-48cc-bffb-8bdb762c5ada",
                "INDICATOR_ID": "98375FEC-E1C4-4E99-B635-648F2C1B2FDB"
            }
        ],
        [
            {
                "INDICATOR_NAME": "教学水平",
                "INDICATOR_STAND": "运用新的教学理念、手段；教学方法多样化；教学态度认真，思路清晰、表达生动。",
                "VERY_SATISFACTIONCOUNT": 1,
                "SATISFACTIONCOUNT": 2,
                "BAS_SATISFACTIONCOUNT": 1,
                "NO_SATISFACTIONCOUNT": 0,
                "VERY_SATISFACTION": 25,
                "SATISFACTION": 75,
                "CLASS_SCHEDULE_ID": "2a52e332-0284-48cc-bffb-8bdb762c5ada",
                "INDICATOR_ID": "E8FCB0B6-FCBC-4697-B911-E570FA73B733"
            }
        ]
    ],
    "className": "tttttt",
    "satisfaction": "14.58"
},

*/
        tpl:new Ext.XTemplate(
        '<div>',
        '<table  class="pg"  cellspacing=0 cellpadding=0 style="font-family:microsoft yahei;">',
            '<tr style="line-height:25px">',
                '<th class="coursename" colspan=8; style="width:1000px">{className}</br>教学评估表</th>',
            '</tr>',
            '<tr style="line-height:45px">',
              '<td id="ten" style="width:100px">课程名称</td><td colspan=7; style="width:90%;border-right: 1px solid #cecece;">{courseName}</td>',
            '</tr>',

            '<tr style="line-height:45px">',
                '<td id="ten" style="width:100px">教学形式</td><td colspan=7; style="width:90%;border-right: 1px solid #cecece;">{teachTypeName}</td>',
            '</tr>',

            '<tr style="line-height:45px">',
               '<td id="ten" style="width:100px">上课教师</td><td colspan=7; style="width:90%;border-right: 1px solid #cecece;">{teacherName}</td>',
            '</tr>',

            '<tr style="line-height:45px">',
                  '<td id="ten" style="width:100px">评估指标</td><td id="ten" style="width:300px">评估标准</td>',
                  '<td id="ten" style="width:10%">很满意</td><td id="ten" style="width:10%">满意</td>',
                  '<td id="ten" style="width:10%">基本满意</td>',
                  '<td id="ten" style="width:10%">不满意</td><td id="ten" style="width:10%">很满意度</td>',
                  '<td id="ten" style="width:10%;border-right: 1px solid #cecece;">满意度</td>',
            '</tr>',


    // 循环
            '<tpl for="standList">',
                '<tpl for=".">',
                  '<tr>',
                      '<tpl if="xcount == 1">',
                        '<td id="ten"  style="width:100px">{INDICATOR_NAME}</td>',
                      '<tpl elseif="xcount &gt; 1 && xindex==1">',
                        '<td id="ten" style="width:100px;position: relative;border-bottom: none;">',
                          '<span style="position: absolute;line-height: {[ (xcount)*50]}px; display: block; top: 0; width: 100%; z-index: 999;">{INDICATOR_NAME}</span>',
                        '</td>',
                      '<tpl elseif="xcount &gt; 1 && xindex != xcount">',
                        '<td id="ten"  style="width:100px;border-bottom: none;"></td>',                 
                      '<tpl else>',
                        '<td id="ten"  style="width:100px"></td>',
                      '</tpl>',
                      '<td style="padding: 5px;display: inline-block;height: 0px;white-space: normal; overflow: hidden;width: 300px;height: 50px;line-height: 20px;" >{INDICATOR_STAND}</td>',
                      '<td>{VERY_SATISFACTIONCOUNT}</td><td>{SATISFACTIONCOUNT}</td><td>{BAS_SATISFACTIONCOUNT}</td><td>{NO_SATISFACTIONCOUNT}</td><td>{VERY_SATISFACTION}</td><td style="border-right: 1px solid #cecece;">{SATISFACTION}</td>',
                  '</tr>',
                '</tpl>',
            '</tpl>',
            '<tr>',
                  '<td  colspan=6 style="line-height:25px;background:#FFE4F0;">汇总：</td>',
                  '<td>{verySatisfaction}</td><td style="border-right: 1px solid #cecece;">{satisfaction}</td>',
            '</tr>',
            
            '<tr style="line-height: 45px;">',
                  '<td id="ten"  style="width:100px" >意见建议</td>',
                  '<td width="300px" colspan=7 style="text-align:left;padding:5px;border-right: 1px solid #cecece;">',
                       '{advise}',
                   '</td>',
            '</tr>',
        '</table>',
        '</div>'
            ),

     

    });
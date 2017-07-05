    
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
      

       data:{
    "courseName": "行政决策能力",
    "teachTypeName": "现场教学",
    "classScheduleId": "2a52e332-0284-48cc-bffb-8bdb762c5ada",
    "teacherName": "梁艳霞",
    "advise": "|sad",
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
                  '<td>25.00</td><td style="border-right: 1px solid #cecece;">25.00</td>',
            '</tr>',
            
            '<tr >',
                  '<td id="ten"  style="width:100px">意见建议</td>',
                  '<td width="300px" colspan=7 style="text-align:left;padding:5px;border-right: 1px solid #cecece;">',
                        '1.理论系统，对时政分析到位，语言丰富幽默。</br>', 
                        '2.希望老师可以更好地把握时间，多讲内容。</br>',
                        '3.普通话水平有待提高。</br>',
                        '4.结合事例再深入讲解会更易理解。多讲几节课。</br>',
                        '5.精辟入里，受益匪浅。很好。</br>',
                        '6.刘老师授课方式生动，启发性强，学生对授课内容印象深刻，对初入职公务员端正工作思想有很好警示，建议更多地引入政治体制常识普及，增强学员的理论基础。</br>',
                        '7.生动形象，希望可以一直有这样的课。</br>',
                        '8.可稍加政治政策内容解读，以便更好地了解政策方向。</br>',
                        '9.延长授课时间。</br>',
                        '10.讲座标题广，讲课时间较短。</br>',
                        '11.刘老师讲课生动，理论结合案例，有针性，为刘老师点赞！</br>',
                        '12.建议课后可以加强沟通交流。</br>',
                        '13.课时可适当延长，增加分享内容。</br>',
                       ' 14.普通话更标准会更好。</br>',
                   '</td>',
            '</tr>',
        '</table>',
        '</div>'
            ),

     

    });
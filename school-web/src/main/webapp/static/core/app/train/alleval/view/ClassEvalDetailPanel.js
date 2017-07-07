    
    /**
     * Created by luoyibo on 2017-06-06.
     */
    Ext.define('core.train.alleval.view.ClassEvalDetailPanel', {
        extend: 'Ext.Container',
        alias: "widget.alleval.classevaldetailpanel",
        
        style: {
            border: '1px solid #ddd'
        },
        
        margin:'0 0 0 10',
        scrollable:true, 
        width:'100%',


    //   data:{
    //       "holdUnit": "",
    //       "evalCount": "0",
    //       "verySatisfaction": null,
    //       "standList": [
    //           [
    //         {
    //             "INDICATOR_NAME": "培训实施",
    //             "INDICATOR_STAND": "课程设置、内容安排科学合理",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "da87e67d-5ac4-4cc0-9e75-6037b14883ca"
    //         },
    //         {
    //             "INDICATOR_NAME": "培训实施",
    //             "INDICATOR_STAND": "教学流程控制严谨有序、张弛有度，讲究实效",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "da87e67d-5ac4-4cc0-9e75-6037b14883ca"
    //         },
    //         {
    //             "INDICATOR_NAME": "培训实施",
    //             "INDICATOR_STAND": "班主任工作认真负责，管理细致，服务热情",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "da87e67d-5ac4-4cc0-9e75-6037b14883ca"
    //         },
    //         {
    //             "INDICATOR_NAME": "培训实施",
    //             "INDICATOR_STAND": "优选师资、配备适当",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "da87e67d-5ac4-4cc0-9e75-6037b14883ca"
    //         }
    //     ],
    //     [
    //         {
    //             "INDICATOR_NAME": "培训保障",
    //             "INDICATOR_STAND": "教学设施、培训资源满足需要，食宿卫生安全",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "f848cc11-764b-4103-aa29-169b0a6c8007"
    //         },
    //         {
    //             "INDICATOR_NAME": "培训保障",
    //             "INDICATOR_STAND": "服务管理严格规范，后勤保障到位",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "f848cc11-764b-4103-aa29-169b0a6c8007"
    //         }
    //     ],
    //     [
    //         {
    //             "INDICATOR_NAME": "培训效果",
    //             "INDICATOR_STAND": "有效提升党性修养、文化素养等综合素质",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "3ab7553f-4b9a-45d4-9a4f-02e911de561f"
    //         },
    //         {
    //             "INDICATOR_NAME": "培训效果",
    //             "INDICATOR_STAND": "有效提高政策理论水平、业务工作能力",
    //             "VERY_SATISFACTIONCOUNT": 0,
    //             "SATISFACTIONCOUNT": 0,
    //             "BAS_SATISFACTIONCOUNT": 0,
    //             "NO_SATISFACTIONCOUNT": 0,
    //             "VERY_SATISFACTION": 0,
    //             "SATISFACTION": 0,
    //             "INDICATOR_ID": "3ab7553f-4b9a-45d4-9a4f-02e911de561f"
    //         }
    //     ]
    // ],
    //         "className": "tttttt",
    //         "satisfaction": null,
    //         "undertaker": "",
    //         "head": [
    //             "评估指标",
    //             "评估标准",
    //             "很满意",
    //             "满意",
    //             "基本满意",
    //             "不满意",
    //             "很满意度",
    //             "满意度"
    //         ],
    //         "trainTime": "2017-06-08 - 2017-06-29 共 21 天 ",
    //         "advise": "",
    //         "traineeCount": "3",
    //         "head1": [
    //             "评估内容",
    //             "评估等级"
    //         ],
    //         "columnWidth": [
    //             20,
    //             60,
    //             15,
    //             15,
    //             15,
    //             15,
    //             15,
    //             15
    //         ]
    //     },

     tpl:new Ext.XTemplate(
          '<div>',
          '<table  class="pg"  cellspacing=0 cellpadding=0 style="font-family:microsoft yahei;">',
                '<tr style="line-height:25px">',
                    '<th class="coursename" colspan=8; style="width:1000px">{className}</br>评价汇总表</th>',

                 '<tr style="line-height:45px">',
                     '<td id="ten" style="width:100px">主办单位</td><td colspan=7; style="width:90%;border-right: 1px solid #cecece;">{Zb}</td>',
                 '</tr>',

                  '<tr style="line-height:45px">',
                      '<td id="ten" style="width:100px">承办单位</td><td colspan=7; style="width:90%;border-right: 1px solid #cecece;">{cb}</td>',
                 '</tr>',
                '</tr>',
                 '<tr style="line-height:45px">',
                      '<td id="ten" style="width:100px">培训时间</td><td colspan=7; style="width:90%;border-right: 1px solid #cecece;">{trainTime}</td>',
                 '</tr>',
                 '<tr style="line-height:45px">',
                      '<td id="ten" style="width:100px">学员人数</td><td colspan=7; style="width:90%;border-right: 1px solid #cecece;">{traineeCount}</td>',
                 '</tr>',
                  '<tr style="line-height:45px">',
                      '<td id="ten" colspan=2; style="width:300px">评估内容</td><td id="ten" colspan=6; style="width:70%;border-right: 1px solid #cecece;">评估等级</td>',
                 '</tr>',
     
                  '<tr style="line-height:45px">',
                      '<td id="ten" style="width:100px">评估指标</td><td id="ten" style="width:300px">评估标准</td>',
                      '<td id="ten" style="width:10%">很满意</td><td id="ten" style="width:10%">满意</td>',
                      '<td id="ten" style="width:10%">基本满意</td>',
                      '<td id="ten" style="width:10%">不满意</td><td id="ten" style="width:10%">很满意度</td>',
                      '<td id="ten" style="width:10%;border-right: 1px solid #cecece;">满意度</td>',
                  '</tr>',
 /* 
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
   */                  
                  
                  '<tpl for="standList">',   
                    '<tpl for=".">',
                      '<tr>',
                        '<tpl if="xcount== 1">',
                             '<td id="ten" style="width:100px">{INDICATOR_NAME}</td>',
                          '<tpl elseif="xcount &gt; 1 && xindex ==1">',
                              '<td id="ten" style="width:100px;position:relative;border-bottom:none;">',
                              '<span style="position:absolute; line-height:{[ (xcount)*50]}px; display:block;top:0;width:100%;z-index:999;">{INDICATOR_NAME}</span>',
                          '</td>',
                          '<tpl elseif="xcount &gt; 1 && xindex != xcount">',
                             '<td id="ten" style="width:100px;border-bottom:none;"></td>',
                          '<tpl else>',
                              '<td id="ten" style="width:100px"></td>',
                          '</tpl>',
                            '<td style="display:inline-block;height:0px;white-space:normal;overflow:hidden;width:300px;height:50px;line-height:50px;">{INDICATOR_STAND}</td>',
                            '<td>{VERY_SATISFACTIONCOUNT}</td><td>{SATISFACTIONCOUNT}</td><td>{BAS_SATISFACTIONCOUNT}</td><td>{NO_SATISFACTIONCOUNT}</td><td>{VERY_SATISFACTION}</td><td style="border-right:1px solid #cecece;">{SATISFACTION}</td>',
                      '</tr>',
                    '</tpl>',
                  '</tpl>',

                    // '<tr>',
                    //      '<td id="ten"  style="width:100px">教学内容</td>',
                    //      '<td style="padding:8px">紧密联系实际，针对性、实用性、系统性强;内容丰富，前瞻性、新颖性、时效性强。</td>',
                    //      '<td>64</td><td>12</td><td>0</td><td>0</td><td>84.2%</td><td style="border-right: 1px solid #cecece;">100%</td>',
                    // '</tr>',

                    // '<tr>',
                    //      '<td id="ten"  style="width:100px">教学水平</td>',
                    //      '<td style="padding:8px">运用新的教学理念、手段；教学方法多样化；教学态度认真，思路清晰、表达生动。</td>',
                    //      '<td>64</td><td>12</td><td>0</td><td>0</td><td>84.2%</td><td style="border-right: 1px solid #cecece;">100%</td>',
                    // '</tr>',

                    // '<tr >',
                    //       '<td id="ten"  style="width:100px">教学效果</td>',
                    //       '<td style="padding:8px">激发学习兴趣，启发思维，课堂气氛活跃；开阔视野、拓宽思路、增长知识，实用性强。</td>',
                    //       '<td>64</td><td>12</td><td>0</td><td>0</td><td>84.2%</td><td style="border-right: 1px solid #cecece;">100%</td>',
                    // '</tr>',

                    '<tr>',
                          '<td  colspan=6 style="line-height:45px;background:#FFE4F0;">汇总：</td>',
                          '<td>{verySatisfaction}</td><td style="border-right: 1px solid #cecece;">{satisfaction}</td>',
                    '</tr>',
                    
                    '<tr  style="line-height: 45px;">',
                          '<td id="ten"  style="width:100px">意见建议</td>',
                          '<td width="300px" colspan=7 style="text-align:left;padding:5px;border-right: 1px solid #cecece;">',
                               '{advise}',
                           '</td>',
                    '</tr>',
                '</table>',
                '</div>'
      ),

    
    });

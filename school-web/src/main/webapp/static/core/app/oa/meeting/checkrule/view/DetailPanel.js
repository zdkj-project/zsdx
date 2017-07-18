    /**
     * Created by lhy on 2017-06-06.
     */
    Ext.define('core.oa.meeting.checkrule.view.DetailPanel', {
        extend: 'Ext.Container',
        alias: "widget.checkrule.DetailPanel",
        layout: "form", //从上往下布局
        autoHeight: true,
        frame: false,
        style: {
            border: '1px solid #ddd'
        },
        fieldDefaults: { // 统一设置表单字段默认属性
            labelSeparator: "：", // 分隔符
            msgTarget: "qtip",
            labelWidth: 100,
            labelAlign: "right"
        },
        scrollable:true,
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
                '<div class="trainClass_title">会议详情：</div>',
                '<ul>',
                    '<li style="width:30%">规则名称：{ruleName}</li>',
                    '<li style="width:30%">签到提前分钟：{inBefore}</li>',
                    '<li style="width:30%">迟到分钟：{beLate}</li>',
                    '<li style="width:30%">缺勤分钟：{absenteeism}</li>',
                    '<li style="width:30%">是否需要签退：{needCheckout}</li>',
                    '<li style="width:30%">规则是否启用：{startUsing}</li>',
                    '<li style="width:30%">签退提前分钟：{outBefore}</li>',
                    '<li style="width:30%">签退延迟分钟：{outLate}</li>',
                    '<li style="width:30%">早退分钟：{leaveEarly}</li>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>',
            '<div class="trainClass_classInfo">',
                '<div class="trainClass_title">规则说明：</div>',
                '<ul>',
                  '<tpl if="!ruleDesc">',
                        '<span>请输入规则说明!</span>',
                    '<tpl else>',
                        '<span>{ruleDesc}</span>',
                    '</tpl>',
                    '<div style="clear:both"></div>',
                '</ul>',
            '</div>'
         ),
        data:{ }
    });
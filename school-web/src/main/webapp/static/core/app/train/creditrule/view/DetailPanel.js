    /**
     * Created by lhy on 2017-07-19.
     */
    Ext.define('core.train.creditrule.view.DetailPanel', {
        extend: 'Ext.Container',
        alias: "widget.creditrule.DetailPanel",
        layout: "form", //从上往下布局
        autoHeight: true,
        frame: false,
        /*style: {
            border: '1px solid #ddd'
        },*/
        fieldDefaults: { // 统一设置表单字段默认属性
            labelSeparator: "：", // 分隔符
            msgTarget: "qtip",
            labelWidth: 100,
            labelAlign: "right"
        },
        scrollable:true,
        tpl: new Ext.XTemplate(
            '<div class="trainClass_classInfo">',
                '<div class="trainClass_title">规则详请：</div>',
                '<ul>',
                    '<li style="width:30%">规则名称：{ruleName}</li>',
                    '<li style="width:30%">迟到扣除学分：{lateCredits}</li>',
                    '<li style="width:30%">早退扣除学分：{earlyCredits}</li>',
                    '<li style="width:30%">早退扣除学分：{[values.lateCredits + values.earlyCredits]}</li>',
                    '<li style="width:30%">缺勤学分：0</li>',
                    '<li style="width:30%">规则是否启用：<tpl if="startUsing == 1">已启用<tpl else>不启用</tpl></li>',
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
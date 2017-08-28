Ext.define("core.oa.terminal.view.DetailHtml", {
    extend: "Ext.Container",
    alias: "widget.terminal.detailhtml",

    //bodyPadding: '0 10 10 0',
    margin: '0 0 0 10',
    scrollable: true,
    width: '100%',

    tpl: new Ext.XTemplate(
        '<div class="trainClass_classInfo">',
        '<div class="trainClass_title">终端基本信息：</div>',
        '<ul>' ,
        '<li>终端号：{termCode}</li>',
        '<li>终端类型：{termType}</li>',
        '<li>使用状态：<tpl if="isUse == 1">已使用<tpl else>未使用</tpl></li>',
        '<li>房间名称：{roomName}</li>',
        '<li>规格：{termSpec}</li>',
        '<div style="clear:both"></div>',
        '</ul>',
        '</div>'
    ),
    data: {}
});
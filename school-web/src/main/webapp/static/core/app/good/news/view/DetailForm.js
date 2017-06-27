Ext.define("core.good.news.view.DetailForm", {
    extend: "core.base.view.BaseForm",
    alias: "widget.news.deailform",
    layout: "anchor",
    autoHeight: true,
    frame: true,
    bodyPadding : '0',
    fieldDefaults: { // 统一设置表单字段默认属性
        labelSeparator: '：', // 分隔符
        msgTarget: 'none',
        labelWidth: 100,
        labelAlign: "right",
        anchor:'100%'
    },
    items: [{
        beforeLabelTextTpl: comm.get('required'),
        allowBlank: false,
        maxLength: 64,
        emptyText: '请输入快讯标题(最大64个字符)',
        blankText: "快讯标题不能为空",
        fieldLabel: '快讯标题',
        name: "newTitle"
    },{
    	beforeLabelTextTpl: comm.get('required'),
        fieldLabel: '快讯摘要',
        name: "newIntro",
        xtype: 'textfield',
        allowBlank: false,
        maxLength: 50,
        emptyText: '请输入快讯摘要(最大50个字符)',
        blankText: "快讯摘要不能为空",
    },{
        xtype:'container',
        layout: "hbox",
        margin:'0',
        items:[{
            beforeLabelTextTpl: comm.get("required"),
            width:500,
            margin:'10 0 0 0 ',
            xtype: 'filefield',
            fieldLabel: '快讯封面图片',
            fileUpload: true,
            name: 'file',
            allowBlank: false,
            blankText: '请上传文件',
            buttonText:"选择封面图片",
            emptyText :'图片推荐比例4：3（支持文件格式：PNG | JPG | JPEG）'
        },{
            width:60,
            height:40,
            margin:'0 0 0 10',
            xtype:'image',
            ref:'newsImage',
            src: '',
        }]
    },{
        fieldLabel: '快讯内容',
        name: "newContent",
        //id: 'newContent',	
        xtype: "ueditor",
      //  anchor: '-20',
        height: 355,
        listeners: {
            'change': function() {
                var me = this;
                me.isChanged = true;
            }
        }
    }, {
        fieldLabel: '主键',
        name: "uuid",
        hidden: true
    }]
});
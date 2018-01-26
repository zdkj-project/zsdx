Ext.define("core.train.pushinfo.view.MainGrid", {
	extend: "core.base.view.BaseGrid",
	alias: "widget.pushinfo.maingrid",
	dataUrl: comm.get("baseUrl") + "/PushInfo/list", //数据获取地址
    model: "com.zd.school.push.model.PushInfo", //对应的数据模型
	
	//工具栏操作按钮
    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'tbtext',
            bind:{
                text: '{textName}',
            },        
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800,
                lineHeight:'32px'
            }
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{                
            xtype: "datetimefield",
            name: "regTime",
            queryType: "datetimefield",
            dateType:'date',        //指定这个组件的格式，date或者datetime
            funCode: 'girdFastSearchText',
            //value:new Date(),
            emptyText: '发现开始日期',
            format: "Y年m月d日",   //显示的格式
            triggers: {
                picker: {
                    handler: 'onTriggerClick',
                    scope: 'this',
                    focusOnMousedown: true
                },
                clear: {
                    cls:'x-fa fa-times',
                    handler:function(btn){
                        var me=this;
                        me.reset();
                    },
                    weight:-1,
                    scope: 'this',
                    focusOnMousedown: true
                },
            },
        },{                
            xtype: "datetimefield",
            name: "regTime",
            queryType: "datetimefield",
            dateType:'date',        //指定这个组件的格式，date或者datetime
            funCode: 'girdFastSearchText',
            //value:new Date(),
            emptyText: '发信结束日期',
            format: "Y年m月d日",   //显示的格式
            triggers: {
                picker: {
                    handler: 'onTriggerClick',
                    scope: 'this',
                    focusOnMousedown: true
                },
                clear: {
                    cls:'x-fa fa-times',
                    handler:function(btn){
                        var me=this;
                        me.reset();
                    },
                    weight:-1,
                    scope: 'this',
                    focusOnMousedown: true
                },
            },
        },{
            xtype: 'button',            
            ref: 'gridFastSearchBtn',  
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型  
            iconCls: 'x-fa fa-search',  
        },' ']
    },
    panelButtomBar:null,
    
    defSort: [{
    	property: 'regTime',
    	direction: 'DESC'
    }],

	//扩展参数
	extParams: {
		infoType:1
	},
	columns: [{
		text: "主键",
		dataIndex: "uuid",
		hidden: true
	},{
        text: "发信人",
        dataIndex: "sendUser",
        width:100,
    },{
        text: "收信人",
        dataIndex: "emplName",
        width:100,
    },{
        text: "手机号码",
        dataIndex: "emplNo",
        width:100
    },{
        text: "发信类型",
        dataIndex: "eventType",
        width:100,
    },{
        text: "信息状态",
        dataIndex: "pushStatus",
        width:80,
        renderer: function(value, metaData) {
            if(value==1)
                return "<span style='color:#ff7e22'>正在发送</span>";
            else if(value==2)
                return "<span style='color:green'>发送成功</span>";
            else if(value==-1)
                return "<span style='color:red'>发送失败</span>";
            else 
                return "<span style='color:#666'>未发送</span>";
        }
    },{
        text: "信息内容",
        dataIndex: "regStatus",
        flex:1,
        minWidth:200,
        renderer: function(value, metaData) {
            var title = "信息内容";
            var html = value;
            metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + html + '"';
            return value;
        }
    },{
        text: "发信时间",
        dataIndex: "regTime",
        width:140,
        renderer: function(value, metaData) {
            var date = value.replace(new RegExp(/-/gm), "/");
            var title = "发信时间";
            var ss = Ext.Date.format(new Date(date), 'Y-m-d H:i')
            return ss;
        }
    }],
});
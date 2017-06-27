Ext.define("core.train.class.view.ClassDetailLayout", {
	extend:"core.base.view.BasePanel",
	alias: "widget.class.classdetaillayout",
	funCode: "class_classdetail",
	layout:'vbox',
	funData: {
		action: comm.get("baseUrl") + "/TrainClass", //请求Action
		whereSql: "", //表格查询条件
		orderSql: "", //表格排序条件
		pkName: "uuid",
		defaultObj: {}
	},
    /*关联此视图控制器*/
    controller: 'class.detailController',
    /*设置最小宽度*/
    minWidth:1200,

    margin:'0 0 0 10',
    scrollable:true, 
    defaults:{
        xtype:'fieldset',
        border:1,
        width:'99%',
        margin:'10 0 10 0',
        padding:'5 5 0 5',
        style: {
            //backgroundColor: '#f5f5f5',
            fontSize:'14px',
            fontFamily: '微软雅黑',
            fontWeight:800,
            color:'#C3190C',
            borderColor: '#C6CBD6',
            borderStyle: 'solid'
        },
    },
  
	items: [{
        title: '班级基本信息',
        style: {
            backgroundColor: '#f5f5f5',
            fontSize:'14px',
            fontFamily: '微软雅黑',
            fontWeight:800,
            color:'#C3190C',
            borderColor: '#C6CBD6',
            borderStyle: 'solid'
        },
        items:[{
            xtype: "class.detailform",
            margin:0,
            height:150,
            bodyPadding: '0 20 0 5',
            bodyStyle: {
                background: '#f5f5f5',        
            },
            buttonAlign :'center',
            buttons: [{
                text: '暂存班级信息',
                ref: 'submitBtn',
                iconCls: 'x-fa fa-check-square',
                formBind: true, //only enabled once the form is valid
                disabled: true
            }/*, {
                text: '重置',
                ref: 'resetBtn',
                iconCls: 'x-fa fa-undo',
                handler:function() {
                    this.up('form').getForm().reset();
                }
            }*/] 
        }]
	},{
        title: '班级学员列表',
        style: {
            //backgroundColor: '#f5f5f5',
            fontSize:'14px',
            fontFamily: '微软雅黑',
            fontWeight:800,
            color:'#C3190C',
            borderColor: '#C6CBD6',
            borderStyle: 'solid'
        },
        items:[{
            xtype:'class.classstudentgrid',
            height:300
        }]
    },{
        title: '班级课程列表',
        items:[{
            xtype:'class.classcoursegrid',
            height:300,
        }]
    },{
        title: '就餐申请',
        style: {
            backgroundColor: '#f5f5f5',
            fontSize:'14px',
            fontFamily: '微软雅黑',
            fontWeight:800,
            color:'#C3190C',
            borderColor: '#C6CBD6',
            borderStyle: 'solid'
        },
        items:[{
            xtype:'class.fooddetailform',
            height:510,
            margin:0,
            bodyPadding: '0 20 0 5',
            bodyStyle: {
                background: '#f5f5f5',        
            },
            buttonAlign :'center',
            buttons: [{
                text: '暂存就餐信息',
                ref: 'submitBtn',
                iconCls: 'x-fa fa-check-square',
                formBind: true, //only enabled once the form is valid
                disabled: true
            }/*, {
                text: '重置',
                ref: 'resetBtn',
                iconCls: 'x-fa fa-undo',
                handler:function() {
                    this.up('form').getForm().reset();
                }
            }*/] 
        }]
    },{
        title: '住宿申请',
        style: {
            backgroundColor: '#f5f5f5',
            fontSize:'14px',
            fontFamily: '微软雅黑',
            fontWeight:800,
            color:'#C3190C',
            borderColor: '#C6CBD6',
            borderStyle: 'solid'
        },
        items:[{
            xtype:'class.roomdetailform',
            height:430,
            margin:0,
            bodyPadding: '0 20 0 5',
            bodyStyle: {
                background: '#f5f5f5',        
            },
            buttonAlign :'center',
            buttons: [{
                text: '暂存住宿信息',
                ref: 'submitBtn',
                iconCls: 'x-fa fa-check-square',
                formBind: true, //only enabled once the form is valid
                disabled: true
            }/*, {
                text: '重置',
                ref: 'resetBtn',
                iconCls: 'x-fa fa-undo',
                handler:function() {
                    this.up('form').getForm().reset();
                }
            }*/] 
        }]
    }]
});

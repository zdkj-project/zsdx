/*
图标子菜单，用于显示第二层的图标菜单
*/
Ext.define('core.main.view.menu.MainMenuIcon', {  
    extend : 'Ext.view.View',  
    
    alias : 'widget.main.mainmenuicon',  
    //title : '功能菜单',  
    //iconCls:'x-fa fa-calendar-minus-o',

    viewModel : 'main.mainModel',  
    
    tpl:new Ext.XTemplate(
        '<tpl for=".">',
            '<div class="mainMenuIcon-wrap">',
                '<tpl if="taskNumber != null && taskNumber &gt; 0 ">',  // <-- Note that the > is encoded
                    '<span class="mainMenuIcon-tag">{taskNumber}</span>',
                '</tpl>',
                '<tpl if="bigIcon !=\'\' ">',  // <-- Note that the > is encoded
                    '<img src="{bigIcon}" class="mainMenuIcon-img"/>',
                '<tpl elseif="iconCls !=\'\' ">',
                    '<i class="{iconCls} mainMenuIcon-icon" aria-hidden="true"></i>', 
                '<tpl else>',
                    '<i class="fa fa-calendar-minus-o mainMenuIcon-icon" aria-hidden="true"></i>',
                '</tpl>',            
                '<br/><span class="mainMenuIcon-text">{text}</span>',
            '</div>',
        '</tpl>'
    ),
    itemSelector: 'div.mainMenuIcon-wrap',

    emptyText: '没有下级菜单了',
    
    scrollable :true,
    datas:[],   //菜单项
    
    initComponent : function() {  
        var me =this;
        var iconStore=Ext.create('Ext.data.Store', {
            fields: [
                'id','text', 'iconCls', 'leaf','children','menuCode','menuType','bigIcon','smallIcon','taskNumber'
            ],
        });

        //读取任务数值
        Ext.Ajax.request({
            url: comm.get('baseUrl')+'/sysuser/getUserMenuTask',
            method: "POST",
            async: false,
            timeout: 60000,                
            success: function(response, opts) {
                var result = Ext.decode(response.responseText);
                //console.log(result);
                if(result.success){
                    var objList=result.obj;
                    var iconDatas=me.datas;
                    for(var i=0;i<objList.length;i++){
                        var obj=objList[i];
                        for(var j=0;j<iconDatas.length;j++){
                            if(obj.name==iconDatas[j].menuCode){
                                iconDatas[j].taskNumber=obj.value;
                                break;
                            }
                        }
                    }                
                }
                iconStore.loadData(me.datas);
                me.store=iconStore;
            },

            failure: function(response, opts) {
                iconStore.loadData(me.datas);
                me.store=iconStore;
            }
        });            

        iconStore.loadData(me.datas);
        me.store=iconStore;

        this.callParent(arguments);  
    },

    listeners: {
        itemclick:'onViewIconItemClick'  //会去控制器里找到对应的方法
    }
}) 
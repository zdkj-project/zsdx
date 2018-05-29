Ext.define("core.base.view.BaseGrid", {
    extend: "Ext.grid.Panel",
    alias: "widget.basegrid",

    border: false,
    //forceFit: false, //自动填充表格列宽度   //zzk：不推荐设置为true，一直刷新表格界面column宽度会一直变化，动态隐藏列出现bug
    frame: false,   //提供更良好的体验
    layout: 'fit',  
    enableKeyNav: true, //可以使用键盘控制上下
    columnLines: false, //不展示竖线
    loadMask: true,
    multiSelect: true,
    selModel: {
        type: "checkboxmodel",   
        headerWidth:50,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        //mode:'single',  //multi,simple,single；默认为多选multi
        //checkOnly:false,    //如果值为true，则只用点击checkbox列才能选中此条记录
        //allowDeselect:true, //如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    },
    viewConfig: {
        stripeRows: false
    },
    //获取数据的地址
    dataUrl: '',
    //排序字段及模式定义
    defSort: [],
    emptyText:'<span style="display: block;text-align:center">没有需要显示的数据！</span>',
    //分组字段
    defGroup: null,
    //扩展参数
    extParams: {},
    
    noPagging: false,   //是否要在这里装载分页栏
    bottomInfoPanelRef:null,   //是否显示底部消息栏，并且此值作为该组件ref的值
    model: '',          //model的名称
    storePageSize:20,
    store:'',           //store的名称，若为空，则在下面的初始化代码中加载，否则不再加载
    al: true,           //是否store自动加载
    remoteSort: true,   //是否远程排序
    pageDisplayInfo:true,   //是否显示分页栏的信息
 
    columns: [],
    /*
    tbar: [{
        xtype: 'button',
        text: '添加',
        ref: 'gridAdd',
        iconCls: 'x-fa fa-plus-circle'
    },{
        xtype: 'button',
        text: '编辑',
        ref: 'gridEdit',
        disabled:true,
        iconCls: 'x-fa fa-pencil-square'
    },{
        xtype: 'button',
        text: '详细',
        ref: 'gridDetail',
        disabled:true,
        iconCls: 'x-fa fa-file-text'
    },{
        xtype: 'button',
        text: '删除',
        ref: 'gridDelete',
        disabled:true,
        iconCls: 'x-fa fa-minus-circle'
    },{
        xtype: 'button',
        text: '导入',
        ref: 'gridImport',
        disabled:true,
        iconCls: 'x-fa fa-clipboard'
    },{
        xtype: 'button',
        text: '导出',
        ref: 'gridExport',
        disabled:true,
        iconCls: 'x-fa fa-file'
    }],*/

    panelTopBar:{
        xtype:'toolbar',
        items: [{
            xtype: 'button',
            text: '添加',
            ref: 'gridAdd',
            funCode:'girdFuntionBtn',   //指定此类按钮为girdFuntionBtn类型，用于于右边的按钮进行功能区分
            iconCls: 'x-fa fa-plus-circle'
        },{
            xtype: 'button',
            text: '编辑',
            ref: 'gridEdit',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-pencil-square'
        },{
            xtype: 'button',
            text: '详细',
            ref: 'gridDetail',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-file-text'
        },{
            xtype: 'button',
            text: '删除',
            ref: 'gridDelete',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-minus-circle'
        },{
            xtype: 'button',
            text: '导入',
            ref: 'gridImport',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-clipboard'
        },{
            xtype: 'button',
            text: '导出',
            ref: 'gridExport',
            funCode:'girdFuntionBtn',
            disabled:true,
            iconCls: 'x-fa fa-file'
        },'->',{
            xtype: 'tbtext', 
            html:'快速搜索：'
        },{
            xtype:'textfield',
            name:'name',
            funCode:'girdFastSearchText', 
            emptyText: '请输入姓名'
        },{
            xtype: 'button',
            funCode:'girdSearchBtn',    //指定此类按钮为girdSearchBtn类型
            ref: 'gridFastSearchBtn',   
            iconCls: 'x-fa fa-search',  
        },/*{
            xtype: 'button',
            text: '本月',
            funCode:'girdSearchBtn',
            ref: 'gridSearchMonth',  
        },{
            xtype: 'button',
            text: '本周',
            funCode:'girdSearchBtn',          
            ref: 'gridSearchWeek',   
        },{
            xtype: 'button',
            text: '本日',
            funCode:'girdSearchBtn',
            ref: 'gridSearchDay',
        },{
            xtype: 'button',
            text: '全 部',
            funCode:'girdSearchBtn',
            ref: 'gridSearchAll',
            iconCls: 'x-fa fa-star'
        },*/' ',{
            xtype: 'button',
            text: '高级搜索',
            ref: 'gridHignSearch',
            iconCls: 'x-fa fa-sliders'
        }],
    },

    panelButtomBar:{
        xtype:'basequeryform',
    },

    initComponent: function() {

        if(!this.store){
            this.store = Ext.create('Ext.data.Store', {
                pageSize: this.storePageSize,
                remoteSort: this.remoteSort,
                autoLoad: this.al,
                model: factory.ModelFactory.getModelByName(this.model, "checked").modelName,
                sorters: this.defSort,
                //grouper: this.defGroup,
                //groupField:this.defGroup,
                proxy: {
                    type: 'ajax',
                    url: this.dataUrl,
                    extraParams: this.extParams,
                    reader: {
                        type: 'json',
                        rootProperty: 'rows',
                        totalProperty: 'totalCount'
                    },
                    writer: {
                        type: 'json'
                    }
                },
                listeners:{
                    load:function( store , records , successful , operation , eOpts ){
                                        
                        //(处理服务器登录超时的解决方式)若为false，则表明返回的数据不是proxy指定的格式；则弹出提示
                        if(successful==false) {    
                           
                            if(operation.getResponse()==null){      //请求无响应出错的时候      
                                return;
                            }

                            var result=Ext.decode(Ext.valueFrom(  operation.getResponse().responseText, '{}')); 
                            var msg=result.obj;
                            if(!msg||typeof(msg) != "string")
                                msg="请求失败，请刷新页面重试！";
                                                    
                            Ext.MessageBox.show({
                                title: "警告",
                                msg: msg,
                                buttons: Ext.MessageBox.OK,
                                icon: Ext.MessageBox.WARNING,
                                fn: function(btn) {
                                    location.reload()       
                                }
                            });
                            
                        }                    
                    }    
                }
            });

             //处理store分组的时候，但是autoLoad失效，会立即加载
            if(this.defGroup!=null){
                this.store.setGroupField(this.defGroup);
            }
        }
        
        /*表格底部的Bar*/
        /*更新：表格底部加入一行作为显示统计数据的panel*/
        var pagingToolBar=null;    
        var buttomInfoPanel=null;
        if (this.noPagging == false) {
            pagingToolBar = Ext.create('Ext.PagingToolbar', {   
                cls:'basegridPagingToolbar',         
                store: this.store,
                pageSize: this.storePageSize,
                displayInfo: this.pageDisplayInfo,
                plugins: [
                    //Ext.create('Ext.ux.ProgressBarPager', {}),
                    Ext.create('Ext.ux.ComboPageSize', {})
                ],
                items: [],
                emptyMsg: "没有可显示的数据"
            });
        }
        if(this.bottomInfoPanelRef){
            buttomInfoPanel=Ext.create('Ext.panel.Panel',{
                ref:this.bottomInfoPanelRef,
                height:25,
                bodyStyle :{
                    backgroundColor:'#f6dfdf',
                   // border:'1px solid #99bce8',
                    padding:'5px 0px 0px 5px',
                    color:'#a70c00',
                    fontWeight: 400
                }        
            });    
        }
        if(pagingToolBar||buttomInfoPanel){
            var dockedItems=[];

            if(buttomInfoPanel)
                dockedItems.push(Ext.apply(buttomInfoPanel,{ dock:'top'}));

            if(pagingToolBar)
                dockedItems.push(Ext.apply(pagingToolBar,{ dock:'bottom'}));        
            this.bbar=Ext.create('Ext.panel.Panel',{
                dockedItems:dockedItems
            })
        }
       

        /*表格顶部的Bar*/
        if(this.panelTopBar||this.panelButtomBar){
            /*
            var items={
                xtype:'panel',
                dock:'top',
                dockedItems:[]
            };
            */
            var dockedItems=[];
            if(this.panelTopBar){
                dockedItems.push(Ext.apply(this.panelTopBar,{ dock:'top',  ref:'panelTopBar' }));
            }
            if(this.panelButtomBar){
                dockedItems.push(Ext.apply(this.panelButtomBar,{ dock:'bottom' ,hidden:true}));
            }

            this.tbar=Ext.create('Ext.panel.Panel',{
                dockedItems:dockedItems
            })
        }

      

        var columns = new Array();

        var columnIitems = this.columns.items;  //取items里的数据，现在多了个配置项defaults
        if(!columnIitems)
            columnIitems=this.columns;          //若没有，则取columns
        
        Ext.each(columnIitems, function(col) {         
            //字典项转换
            if (col.columnType == "basecombobox" || (col.field && col.field.xtype && col.field.xtype == "basecombobox")) {
                col.renderer = function(value, data, record) {                	
                    var val = value;
                    var ddCode = col.ddCode;
                    var ddItem = factory.DDCache.getItemByDDCode(ddCode);
        
                    for (var i = 0; i < ddItem.length; i++) {
                        var ddObj = ddItem[i];
                        var displayField = 'itemName';
                        var valueField = 'itemCode';
                        if (col.field && col.field.displayField) {
                            displayField = column.field.displayField;
                        } else if (col.displayField) {
                            displayField = col.displayField;
                        }
                        if (col.field && col.field.valueField) {
                            displayField = col.field.valueField;
                        } else if (col.displayField) {
                            displayField = col.displayField;
                        }
                        if (value == ddObj[valueField]) {
                            val = ddObj[displayField];
                            break;
                        }
                    }
                    return val;
                }
            }
            columns.push(col);
        });

        this.callParent(arguments);
    }
});
Ext.define("core.system.appupdate.view.AppGrid", {
    extend: 'core.base.view.BaseGrid',
    alias: "widget.appupdate.appgrid",
    //store: 'core.system.appupdate.store.AppInfoStore',   
    dataUrl: comm.get('baseUrl') + "/SysAppinfo/list",
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysAppinfo", "checked").modelName,

    emptyText:'<div style="width:100%;line-height: 100px;text-align:center">暂无APP！</div>',
    selModel: Ext.create('Ext.selection.CheckboxModel', {
        injectCheckbox:1,//checkbox位于哪一列，默认值为0
        mode:'single',//multi,simple,single；默认为多选multi
        checkOnly:false,//如果值为true，则只用点击checkbox列才能选中此条记录
        allowDeselect:true,//如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    }),
    defSort: [{
        property: 'createTime',
        direction: 'DESC'
    },{
        property: 'appType',
        direction: 'ASC'
    }],
    //frame: true, 
    //title:'APP信息列表',
    tbar: [
        { xtype: 'button', text: '启用', ref: 'gridAdd', iconCls: 'x-fa fa-plus-circle' },
        { xtype: 'button', text: '取消启用', ref: 'gridRemove', iconCls: 'x-fa fa-minus-circle' },    
        { xtype: 'button', text: '上传APP', ref: 'gridUpload', iconCls: 'fa fa-upload' },
    ],
    panelButtomBar: null,
    panelTopBar:null,

    columns:  {
        defaults: {
            titleAlign: "center"
        },
        items:[{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        },{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            text: "APP名称",
            dataIndex: "appTitle",
            flex:1
        }, {
            text: "APP类型",
            dataIndex: "appType",
            ddCode: "APPTYPE",
            columnType: "basecombobox"
        },{
            text: "APP版本号",
            dataIndex: "appVersion",
            width:150
        },{
            text: "APP描述",
            dataIndex: "appIntro",
            flex:1,
            renderer: function(value,metaData) {  

                //var title="适用年级";

                //metaData.tdAttr = 'data-qtitle="' + title + '" data-qtip="' + value + '"';  
                metaData.tdAttr = '" data-qtip="' + value + '"';  
                return value;  
            }
        },{
            text: "上传时间",
            dataIndex: "createTime",
            width:150,
            renderer: function(value,metaData) {          
                if(value!=null){      
                    return Ext.Date.format(new Date(value), 'Y-m-d H:i:s');
                }
                        
            }
        },{
            text: "是否启用",
            dataIndex: "appIsuse",
            width:100,
            renderer: function(value,metaData) {          
                if(value==1){
                    return "<span style='color:green'>已启用</span>";
                }else{
                    return "<span style='color:red'>未启用</span>";
                }
            
            }
        }]
    }
    /*
    dockedItems: [{
        xtype: 'pagingtoolbar',
        store: 'core.system.appupdate.store.AppInfoStore',   // same store GridPanel is using
        dock: 'bottom',
        displayInfo: true,
        plugins: [
            Ext.create('Ext.ux.ComboPageSize', {  
                pageSizes: [20, 50, 100, 1000, 10000]
            })
        ],
        emptyMsg: "没有可显示的数据"
    }]*/
});
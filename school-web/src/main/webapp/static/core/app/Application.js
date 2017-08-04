/**
 * The main application class. An instance of this class is created by app.js when it
 * calls Ext.application(). This is the ideal place to handle application launch and
 * initialization details.
 */
Ext.define('core.Application', {
    extend: 'Ext.app.Application',
    
    name: 'core',
    //scope :this,        
    stores: [
        // TODO: add global / shared stores here
    ],
    
    launch: function () {
        coreApplication=this;
        /**
         * 程序外部组件引用
         */
        //初始化Ext.QuickTips，以使得tip提示可用
        
        Ext.QuickTips.init();
        //初始化提示工具框
        Ext.tip.QuickTipManager.init();

        //启动动态加载机制
        Ext.Loader.setConfig({
            enabled: true,
            paths: {
                baseUx: comm.get('baseUrl') + "/static/core/app/ux/base",
                factory: comm.get('baseUrl') + "/static/core/app/util/factory",
                'Ext.ux': comm.get('baseUrl') + "/static/core/app/ux"
            }
        })

        //同步加载
        Ext.syncRequire ([
            // EXT 6.0无法使用，需要找新的插件
            //"baseUx.form.datetime.DateTimePicker",
            //"baseUx.form.datetime.DateTime", 
            "Ext.ux.DateTimePicker",
            "Ext.ux.DateTimeField",
            "factory.ModelFactory",
            "factory.DDCache",
            "Ext.ux.UEditor",
            "Ext.ux.form.MultiSelect",
            "Ext.ux.form.ItemSelector",
            "Ext.ux.grid.Actiontextcolumn",
            
        ]);
        // TODO - Launch the application
    },
    onAppUpdate: function () {
    	 //window.location.reload();
    }
});

Ext.define("core.mjmanage.roomallot.store.IsSelectTeacherStore",{
    extend:"Ext.data.Store",

    alias: 'store.mjmanage.roomallot.isselectteacherstore',

 
    model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysUser", "checked").modelName,
});

Ext.define("core.mjmanage.deviceallot.store.IsSelectStore",{
    extend:"Ext.data.Store",
    alias: 'store.mjmanage.deviceallot.isselectstore',
    model: factory.ModelFactory.getModelByName("com.zd.school.control.device.model.PtTerm", "checked").modelName,
  
});
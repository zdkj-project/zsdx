/*弃用*/
 Ext.define("core.train.arrange.store.ArrangeSiteGridStore",{
	extend:"Ext.data.Store",
	
	alias: 'store.arrange.arrangesitegridStore',

 	fields: ['uuid', 'courseName','courseMode','roomName','isDelete']     

});
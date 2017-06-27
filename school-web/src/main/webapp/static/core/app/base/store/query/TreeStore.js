Ext.define("core.base.store.query.TreeStore",{
	extend:"Ext.data.TreeStore",
	defaultRootId:"ROOT",
	//model:factory.ModelFactory.getModelByName("com.desktop.model.extjs.JSONTreeNode","checked").modelName,
	listeners : {
		load : function(store){
			
			/*	
    		var async = false;
    		
    		//此代码无法执行，原因不明
    		var cbb = store.treeObj.down('combobox[ref=queryTreeCBB]');
    		var nh = store.tree.nodeHash;
    		cbb.store.removeAll();
    		var parentText = '';//父节点文字
    		for(var p in nh){
    			var node = nh[p];
    			var text = node.get('text');
    			var id = Ext.value(node.get('id'),'');

    			if(id == 'ROOT'){
    				parentText = text;
    			}
    			if(id.toUpperCase() != 'ROOT'){
    				cbb.store.add({text : text,id : id,parentText : parentText});
    			}
    		}*/
		}
	},
	constructor: function(config) {
		this.proxy=Ext.create("Ext.data.proxy.Ajax",{
			url:config.url,
			async : false, //同步的操作
			actionMethods:{
				 create : 'POST',
				 read   : 'POST',
				 update : 'POST',
			     destroy: 'POST'
			},
			extraParams :{},
			reader: 'json'
		});
	    this.callParent(arguments);
	},
	autoLoad: true
})
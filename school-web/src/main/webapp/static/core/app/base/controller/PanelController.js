/**
 * 表单控制器
 */
 
Ext.define("core.base.controller.PanelController",{
	extend:"Ext.app.Controller",
	initPanel:function(){
		var self=this;
		var panelCtr={
			"basepanel":{
		
//				activate:function(panel){
//					var funCode=panel.funCode;
////					if(!panel.funData.isChildren)
//					panel.itemId=funCode+"_basepanel";
////					if(!panel.funData.isChildren){`
////						var form=panel.down("baseform");
////						var tabs=form.down("tabpanel");
////						Ext.each(form.query("basepanel"),function(b){							
////							b.up("panel").tab.show();
////							tabs.setActiveTab(b);
////						})
////					}
//				}
			},
			"basecenterpanel":{
				render:function(panel){
					var basePanel=panel.up("basepanel");
					var funCode=basePanel.funCode;
					panel.funCode=funCode;
					//panel.itemId=funCode+"_basecenterpanel";
				}
			}
		}
		Ext.apply(self.ctr,panelCtr);
	}
});
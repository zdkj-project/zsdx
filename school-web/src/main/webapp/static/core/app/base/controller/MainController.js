/**
 * 程序主控制器
 */
Ext.define("core.base.controller.MainController",{
	extend:"Ext.app.Controller",
	mixins: {
		btnCtr:"core.base.controller.ButtonController",
		formCtr:"core.base.controller.FormController",
		gridCtr:"core.base.controller.GridController",
		panelCtr:"core.base.controller.PanelController",
		queryCtr:"core.base.controller.QueryController",
		gridActionUtil:"core.util.GridActionUtil",
		suppleUtil:"core.util.SuppleUtil",
		messageUtil:"core.util.MessageUtil",
		formUtil:"core.util.FormUtil",
		sqlUtil:"core.util.SqlUtil",
		queryUtil:"core.util.QueryUtil"
	},
	ctr:{},
	init:function(){
		console.log("初始化基础控制器");
		var self=this;
		coreApp=self;
		self.initBtn();
		self.initForm();
		self.initGrid();
		self.initPanel();
		self.initQuery();
		Ext.apply(self.ctr,{
			"westview treepanel":{
				itemclick:function(tree,record,item,index,e,eOpts){
					var westView=tree.up("westview");
					var centerView=westView.up("mainView").down("centerview");
					var raw=record.raw;
					if(raw.nodeInfoType=='MENU')return;
					var item=centerView.getComponent(raw.id);
					if(!item){
						item={
								iconCls : 'css-'+raw.id,
			            		title: raw.text,
			            		xtype:"panel",
			            		layout:"fit",
			            		itemId:raw.id,
			            		boder:0,
			            		bodyPadding :1,
								closable: true,
								tabConfig  : {
									listeners:{	
					            		beforeclose : function(tab){//关闭不销毁，只隐藏
									        var tb = tab.tabBar,card = tab.card,tabPanel = tb.tabPanel,nextTab;
									        if (tab.active && tb.items.getCount() > 1) {
									            nextTab = tab.next('tab[hidden=false]') ||  tab.prev('tab[hidden=false]')  ;
									            nextTab = nextTab.isHidden() ? tb.items.items[0] : nextTab;
									            tb.setActiveTab(nextTab);
									            if (tabPanel) {
									                tabPanel.setActiveTab(nextTab.card);
									            }
									        }
									        card.isShow = false;
					            			tab.hide();		//隐藏标签
					            			return false;
					            		}
									}
								}
							};
						//如果点击的为功能
						if(raw.nodeInfoType=="FUNC"){
							var nodeInfo=raw.nodeInfo;
        					var config=nodeInfo.split(",");
							var con=self.application.getController(config[1]);
							if(con.inited && con.inited==true){									
							}else{
								con.init();
							}
							item.items={
								xtype:config[0]
							}
							//如果点击的为iframe
						}else if(raw.nodeInfoType=="IFRAME"){
							item.items=[{
								xtype:"panel",
								html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src='+ raw.nodeInfo + '></iframe>'
							}];
							
						}
						item=centerView.add(item);
						item.tab.show();
						item.isShow=true;
						centerView.setActiveTab(item);
					//如果功能已经打开过
					}else{
						item.isShow=true;
						item.tab.show();
						centerView.setActiveTab(item);
					}
				}
			}
		});
		//注册事件
		this.control(self.ctr);
	},
	

	requires:[
		"core.base.view.CenterView", 
		"core.base.view.WestView",
		"core.base.view.MainView",
		"core.base.view.BaseGrid",
		"core.base.view.BaseForm",
		"core.base.view.BaseQueryForm",
		"core.base.view.BasePanel",
		"core.base.view.BaseQueryPanel",
		"core.base.view.BaseCenterPanel",
		"core.base.view.BaseExportExcel",
		"core.base.view.BaseImportExcel"
	]
});
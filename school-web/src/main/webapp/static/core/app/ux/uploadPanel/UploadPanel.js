/**
 * 多文件上传组件 
 * for extjs4.0
 * @author caizhiping
 * @since 2012-11-15
 */
Ext.define('Ext.ux.uploadPanel.UploadPanel',{
	extend : 'Ext.grid.Panel',
	alias : 'widget.uploadpanel',
	width : 700,
	height : 300,
	columns : [
        {xtype: 'rownumberer'},
		{text: '文件名', width: 130,dataIndex: 'name'},
		/*{text: '自定义文件名', width: 130,dataIndex: 'fileName',editor: {xtype: 'textfield'}},*/
        {text: '类型', width: 70,dataIndex: 'type'},
        {text: '大小', width: 70,dataIndex: 'size',renderer:function(v){
        	return Ext.util.Format.fileSize(v);
        }},
        {text: '进度', width: 130,dataIndex: 'percent',renderer:function(v){        	
			var stml =
				'<div>'+
					'<div style="border:1px solid #008000;height:10px;width:115px;margin:2px 0px 1px 0px;float:left;">'+		
						'<div style="float:left;background:#FFCC66;width:'+v+'%;height:8px;"><div></div></div>'+
					'</div>'+
				//'<div style="text-align:center;float:right;width:40px;margin:3px 0px 1px 0px;height:10px;font-size:12px;">{3}%</div>'+			
			'</div>';
			return stml;
        }},
        {text: '状态', width: 80,dataIndex: 'status',renderer:function(v){
			var status;
			if(v==0){
				status = "已入库";
			}else if(v==-1){
				status = "等待上传";
			}else if(v==-2){
				status =  "上传中...";
			}else if(v==-3){
				status =  "<div style='color:red;'>上传失败</div>";
			}else if(v==-4){
				status =  "上传成功";
			}else if(v==-5){
				status =  "停止上传";
			}		
			return status;
		}},
        {
            xtype:'actioncolumn',
            width:50,
            items: [{
                icon: comm.get('baseUrl') + '/static/core/ux/uploadPanel/icons/delete.gif',
                tooltip: '移除文件',
                handler: function(grid, rowIndex, colIndex) {
                	var row= grid.store.getAt(rowIndex);
                	var id = row.get('id');

                	var me = grid.up('panel');
                	//ajax请求移除数据，弹出确认框 fileId
                	var fileId = row.get('fileId');
                	if(fileId==1){
                		Ext.Msg.show({
							title : '提示',
							msg : "此文件已入库，暂不允许被删除！",
							width : 250,
							icon : Ext.Msg.INFO,
							buttons :Ext.Msg.OK
						});
						return;
                	}
					if(fileId!=0){
						Ext.Msg.confirm("移除数据库文件", "您确定要从数据库中移除【" + row.get('name') + "】文件吗？", function (btn) {
                        
                            if (btn == 'yes') {
                                //var sss= Ext.Msg.wait('Status', 'Changes saved successfully.');
                                //发送ajax请求
                                Ext.Ajax.request({
								    url: me.delete_url,
								    params: {
                                        fileIds: fileId
                                    },
								    success: function(response){
								    	var result = JSON.parse(response.responseText);
								        if (result.success) {
		                                    grid.store.remove(grid.store.getAt(rowIndex));
		                                    Ext.Msg.show({
												title : '提示',
												msg : result.obj,
												width : 250,
												icon : Ext.Msg.INFO,
												buttons :Ext.Msg.OK
											});
		                                } else {
		                                    Ext.Msg.show({
												title : '提示',
												msg : result.obj,
												width : 250,
												icon : Ext.Msg.ERROR,
												buttons :Ext.Msg.OK
											});
		                                }   
								    }
								});        
                            } 
                        });
                    }else{
                    	grid.store.remove(grid.store.getAt(rowIndex));
                    }
                   
                }
            }]
        }
    ],
    plugins: [
        Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })
    ],    
    store : Ext.create('Ext.data.JsonStore',{
    	autoLoad : false,
    	fields : ['id','name','type','size','percent','status','fileName','fileId']
    }),
	addFileBtnText : 'Add File',
	uploadBtnText : 'Upload',
	removeBtnText : 'Remove All',
	cancelBtnText : 'Cancel',
	debug : false,
	file_size_limit : 100,//MB
	file_types : '*.*',
	file_types_description : 'All Files',
	file_upload_limit : 50,
	file_queue_limit : 0,
	post_params : {},
	upload_url : 'test.do',
	delete_url:'test.do',
	flash_url : comm.get('baseUrl') + "/static/assets/js/swfupload/swfupload.swf",
	flash9_url : comm.get('baseUrl') + "/static/assets/js/swfupload/swfupload_fp9.swf",
	initComponent : function(){		
		this.dockedItems = [{
		    xtype: 'toolbar',
		    dock: 'top',
		    items: [
		        { 
			        xtype:'button',
			        ref: 'addFileBtn',
			        iconCls : 'add',
			        //id : '_btn_for_swf_',
			        text : this.addFileBtnText
		        }/*,{ xtype: 'tbseparator' },{
		        	xtype : 'button',
		        	ref : 'uploadBtn',
		        	iconCls : 'up',
		        	text : this.uploadBtnText,
		        	scope : this,
		        	handler : this.onUpload
		        }*/,{ xtype: 'tbseparator' },{
		        	xtype : 'button',
		        	ref : 'removeBtn',
		        	iconCls : 'trash',
		        	text : this.removeBtnText,
		        	scope : this,
		        	handler : this.onRemoveForBtn
		        },{ xtype: 'tbseparator' },{
		        	xtype : 'button',
		        	ref : 'cancelBtn',
		        	iconCls : 'cancel',
		        	disabled : true,
		        	text : this.cancelBtnText,
		        	scope : this,
		        	handler : this.onCancelUpload
		        }
		    ]
		}];
		
		this.callParent();
		this.down('button[ref=addFileBtn]').on({			
			afterrender : function(btn){
				var config = this.getSWFConfig(btn);		
				this.swfupload = new SWFUpload(config);
				Ext.get(this.swfupload.movieName).setStyle({
					position : 'absolute',
					top : 0,
					left : -2
				});	
			},
			scope : this,
			buffer:300
		});
	},
	getSWFConfig : function(btn){
		var me = this;
		var placeHolderId = Ext.id();
		var em = btn.getEl().child('em');
		if(em==null){
			em = Ext.get(btn.getId()+'-btnWrap');
		}		
		em.setStyle({
			position : 'relative',
			display : 'block'
		});
		em.createChild({
			tag : 'div',
			id : placeHolderId
		});
		return {
			debug: me.debug,
			flash_url : me.flash_url,
			flash9_url : me.flash9_url,	
			upload_url: me.upload_url,
			delete_url: me.delete_url,
			post_params: me.post_params||{savePath:'upload\\'},
			file_size_limit : (me.file_size_limit*1024),
			file_types : me.file_types,
			file_types_description : me.file_types_description,
			file_upload_limit : me.file_upload_limit,
			file_queue_limit : me.file_queue_limit,
			button_width: em.getWidth(),
			button_height: em.getHeight(),
			button_window_mode: SWFUpload.WINDOW_MODE.TRANSPARENT,
			button_cursor: SWFUpload.CURSOR.HAND,
			button_placeholder_id: placeHolderId,
			custom_settings : {
				scope_handler : me
			},
			swfupload_preload_handler : me.swfupload_preload_handler,
			file_queue_error_handler : me.file_queue_error_handler,
			swfupload_load_failed_handler : me.swfupload_load_failed_handler,
			upload_start_handler : me.upload_start_handler,
			upload_progress_handler : me.upload_progress_handler,
			upload_error_handler : me.upload_error_handler,
			upload_success_handler : me.upload_success_handler,
			upload_complete_handler : me.upload_complete_handler,
			file_queued_handler : me.file_queued_handler/*,
			file_dialog_complete_handler : me.file_dialog_complete_handler*/
		};
	},
	swfupload_preload_handler : function(){
		if (!this.support.loading) {
			Ext.Msg.show({
				title : '提示',
				msg : '浏览器Flash Player版本太低,不能使用该上传功能！',
				width : 250,
				icon : Ext.Msg.ERROR,
				buttons :Ext.Msg.OK
			});
			return false;
		}
	},
	file_queue_error_handler : function(file, errorCode, message){
		switch(errorCode){
			case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED : msg('上传文件列表数量超限,不能选择！');
			break;
			case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT : msg('文件大小超过限制, 不能选择！');
			break;
			case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE : msg('该文件大小为0,不能选择！');
			break;
			case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE : msg('该文件类型不允许上传！');
			break;
		}
		function msg(info){
			Ext.Msg.show({
				title : '提示',
				msg : info,
				width : 250,
				icon : Ext.Msg.WARNING,
				buttons :Ext.Msg.OK
			});
		}
	},
	swfupload_load_failed_handler : function(){
		Ext.Msg.show({
			title : '提示',
			msg : 'SWFUpload加载失败！',
			width : 180,
			icon : Ext.Msg.ERROR,
			buttons :Ext.Msg.OK
		});
	},
	upload_start_handler : function(file){
		var me = this.settings.custom_settings.scope_handler;
		//me.down('#cancelBtn').setDisabled(false);	
	 	me.down('button[ref=cancelBtn]').setDisabled(false);
		//var rec = me.store.getById(file.id);

		//this.setFilePostName(encodeURIComponent(rec.get('fileName')));
	},
	upload_progress_handler : function(file, bytesLoaded, bytesTotal){
		var me = this.settings.custom_settings.scope_handler;		
		var percent = Math.ceil((bytesLoaded / bytesTotal) * 100);
		percent = percent == 100? 99 : percent;
       	var rec = me.store.getById(file.id);
       	if(rec==null){	//这里有时会取不到数据
       		setTimeout(function(){
				rec = me.store.getById(file.id);
				if(rec!=null){
					rec.set('percent', percent);
					rec.set('status', file.filestatus);
					rec.commit();
				}	
       		},500);
       	}else{
	       	rec.set('percent', percent);
			rec.set('status', file.filestatus);
			rec.commit();
		}
	},
	upload_error_handler : function(file, errorCode, message){
		var me = this.settings.custom_settings.scope_handler;		
		var rec = me.store.getById(file.id);
       	rec.set('percent', 0);
		rec.set('status', file.filestatus);
		rec.commit();
	},
	upload_success_handler : function(file, serverData, responseReceived){
		var me = this.settings.custom_settings.scope_handler;		
		var rec = me.store.getById(file.id);

		if(rec==null){
       		setTimeout(function(){
				rec = me.store.getById(file.id);
				if(rec!=null){
					if(Ext.JSON.decode(serverData).success){			
				       	rec.set('percent', 100);
						rec.set('status', file.filestatus);			
					}else{
						rec.set('percent', 0);
						rec.set('status', SWFUpload.FILE_STATUS.ERROR);
					}
					rec.commit();
				}	
       		},500);
       	}else{
			if(Ext.JSON.decode(serverData).success){			
		       	rec.set('percent', 100);
				rec.set('status', file.filestatus);			
			}else{
				rec.set('percent', 0);
				rec.set('status', SWFUpload.FILE_STATUS.ERROR);
			}
			rec.commit();
		}

		if (this.getStats().files_queued > 0 && this.uploadStopped == false) {
			this.startUpload();
		}else{
			me.showBtn(me,true);
		}
	},
	upload_complete_handler : function(file){
		var me = this.settings.custom_settings.scope_handler;
		if(file.filestatus!=-4){
			Ext.Msg.show({
				title : '提示',
				msg : "文件上传失败，请稍后重试。",
				width : 250,
				icon : Ext.Msg.WARNING,
				buttons :Ext.Msg.OK
			});
		}else{	
			//当上传成功了，就设置这个标识，代表暂时不允许删除
			var rec = me.store.getById(file.id);
       		rec.set('fileId', 1);
		}

		//me.down('#removeBtn').setDisabled(true);
	    me.down('button[ref=removeBtn]').setDisabled(true);
	},
	file_queued_handler : function(file){
		var me = this.settings.custom_settings.scope_handler;
		me.store.add({
			id : file.id,
			name : file.name,
			//fileName : file.name,
			size : file.size,
			type : file.type,
			status : file.filestatus,
			percent : 0,
			fileId:0,
			fileUrl:''
		});
	},
	onUpload : function(url,params){

		if (this.swfupload&&this.store.getCount()>0) {

			if (this.swfupload.getStats().files_queued > 0) {
				if(url)
					this.swfupload.setUploadURL(url);		//设置上传的url
				if(params)
					this.swfupload.setPostParams(params);	//设置请求的参数

				this.swfupload.setFilePostName("file");		//设置上传文件的参数名

				this.showBtn(this,false);
				this.swfupload.uploadStopped = false;		
				this.swfupload.startUpload();

			}
		}
	},
	showBtn : function(me,bl){
		me.down('button[ref=addFileBtn]').setDisabled(!bl);
		//me.down('#uploadBtn').setDisabled(!bl);
		me.down('button[ref=removeBtn]').setDisabled(!bl);
		me.down('button[ref=cancelBtn]').setDisabled(bl);
		if(bl){
			me.down('actioncolumn').show();
		}else{
			me.down('actioncolumn').hide();
		}		
	},
	onRemove : function(){
		if (this.swfupload) {
			var ds = this.store;

			for(var i=0;i<ds.getCount();i++){
				var record =ds.getAt(i);
				var file_id = record.get('id');
				this.swfupload.cancelUpload(file_id,false);	
			}
		
	        ds.removeAll();
			this.swfupload.uploadStopped = false;
        }
	},
	onRemoveForBtn : function(btn){
		var ds = this.store;

		var fileIds=new Array();
		for(var i=0;i<ds.getCount();i++){
			var record =ds.getAt(i);
			var file_id = record.get('id');

			var fileId=record.get('fileId');
			if(fileId!=0)
				fileIds.push(fileId);

			this.swfupload.cancelUpload(file_id,false);	
		}

		var me = btn.up('panel');
		if(fileIds.length>0){
			Ext.Msg.confirm("移除所有文件", "您确定要从数据库中移除这些文件吗？", function (btn) {
              
                if (btn == 'yes') {
                    //var sss= Ext.Msg.wait('Status', 'Changes saved successfully.');
                    //发送ajax请求
                    Ext.Ajax.request({
					    url: me.delete_url,
					    params: {
                            fileIds:  fileIds.join(",")
                        },
					    success: function(response){
					    	var result = JSON.parse(response.responseText);
					        if (result.success) {
					        	ds.removeAll();
					        	//this.swfupload.uploadStopped = false;

                                Ext.Msg.show({
									title : '提示',
									msg : result.obj,
									width : 250,
									icon : Ext.Msg.INFO,
									buttons :Ext.Msg.OK
								});
                            } else {
                                Ext.Msg.show({
									title : '提示',
									msg : result.obj,
									width : 250,
									icon : Ext.Msg.ERROR,
									buttons :Ext.Msg.OK
								});
                            }   
					    }
					});        
                } 
            });
        }else{
        	ds.removeAll();
			this.swfupload.uploadStopped = false;
        }
	},
	onCancelUpload : function(){
		if (this.swfupload) {
			this.swfupload.uploadStopped = true;
			this.swfupload.stopUpload();
			this.showBtn(this,true);
		}
	}
});
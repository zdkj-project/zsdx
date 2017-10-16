Ext.define('core.main.view.ChangePwd', {
	extend: 'Ext.window.Window',

	alias : 'widget.main.changepwd', 

	title: '修改登录密码',
	width: 350,
	resizable: false,
	draggable: true,
	layout: 'fit',
	plain: false,
	modal: true,
	border: false,
	iconCls: 'x-fa fa-key',

	initComponent: function() {
		var me = this;
		me.form = me.createForm();
		me.items = [me.form];
		me.buttons = [{
			text: '提交',
			handler: me.onSubmit,
			scale: 'medium',
			width: 100,
			height: 35,
			iconCls: 'x-fa fa-check-square',
			scope: me
		}, {
			text: '重置',
			handler: me.onReset,
			scale: 'medium',
			width: 100,
			height: 35,
			iconCls: "x-fa fa-undo",
			scope: me
		}, {
			text: '退出',
			handler: function() {
				this.close();
			},
			scale: 'medium',
			width: 100,
			height: 35,
			iconCls: "x-fa fa-reply",
			scope: me
		}];
		me.callParent();

	},
	createForm: function() {
		var me = this;
		var form = new Ext.form.FormPanel({
			bodyStyle: "padding: 20px 50px",
			border: false,
			frame: false,
			defaults: {
				width: 270,
				labelWidth: 60,
				labelAlign: 'right'
			},
			items: [{
				xtype: 'hidden',
				fieldLabel: '用户名',
				name: 'userName',
				value: comm.get('userName')
			},{
				xtype: 'textfield',
				inputType: 'password',
				fieldLabel: '旧密码',
				name: 'oldPwd',
				allowBlank: false 
			}, {
				xtype: 'textfield',
				inputType: 'password',
				name: 'newPwd',
				fieldLabel: '新密码',
				allowBlank: false,
				vtype: 'password',
				initialPassField: 'newConfirmPwd',
				minLength: '6',
				minLengthText: '密码最少要6位'
			}, {
				xtype: 'textfield',
				inputType: 'password',
				name: 'newConfirmPwd',
				fieldLabel: '确认密码',
				allowBlank: false,
				vtype: 'password',
				initialPassField: 'newPwd',
				minLength: '6',
				minLengthText: '密码最少要6位'
			}]
		});
		return form;
	},
	onSubmit: function() {
		var me = this;

		Ext.MessageBox.show({
			title: '密码修改',
			msg: '密码修改成功，请重新登录',
			buttons: Ext.MessageBox.OK,
			icon: Ext.MessageBox.OK
		});

		
		me.form.form.submit({
			url: comm.get('baseUrl') + '/login/changepwd',
			success: function(form, action) {
				Ext.MessageBox.show({
					title: '密码修改',
					msg: '密码修改成功，请重新登录',
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.OK
				});
				window.location.href = comm.get("baseUrl") + "/login/webLogout";
			},
			failure: function(form, action) {
				Ext.MessageBox.show({
					title: '密码修改',
					msg: '原密码错误，无法重设密码',
					buttons: Ext.MessageBox.OK,
					icon: Ext.MessageBox.ERROR
				})
			}
		});
	},
	onReset: function() {
		var me = this;
		me.form.form.reset();
	}
});
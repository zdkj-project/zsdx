/**
用于菜单功能，暂时无用
*/
Ext.define("core.util.MenuUtil", {
	mixins: {
		suppleUtil: "core.util.SuppleUtil"
	},
	/**
	 * 加载当前登录的人权限按钮
	 */
	initMenu: function() {
		var self = this;
		var data = self.ajax({
			url: comm.get('baseUrl') + "/sysuser/getUserMenuTree",
			params: {
				excludes: "checked"
			}
		});
		var menuTreeStore = Ext.create("Ext.data.TreeStore", {
			model: factory.ModelFactory.getModelByName("com.zd.school.plartform.system.model.SysMenuTree", "checked").modelName,
			defaultRootId: "ROOT",
			root: {
				text: "ROOT",
				code: "ROOT",
				children: data
			}
		});
		comm.add("menuTreeStore", menuTreeStore);
	},
	/**
	 * 构建菜单数据
	 * @param {} node
	 * @return {}
	 */
	buildMenuData: function(node) {
		var data = new Array();
		node.eachChild(function(n) {
			data.push(n.raw);
		});
		return data;
	},
	/**
	 * 初始化树形数据
	 */
	initMenuData: function() {
		var self = this;
		var data = self.ajax({
			url: comm.get('baseUrl') + "/sysuser/getUserMenuTree",
			params: {
				excludes: "checked"
			}
		});
		var cssStr = self.earchCss(data);
		Ext.util.CSS.createStyleSheet(cssStr, 'menuIconCss');
		comm.add("initMenuData", data);
	},
	/**
	 * 递归生成样式
	 * @param {} data
	 * @return {}
	 */
	earchCss: function(data) {
		var cssStr = '.css-field-width : {width : 100%;}';
		Ext.each(data, function(d) { //循环生成菜单
			if (!Ext.isEmpty(d.icon)) {
				cssStr += '.css-' + d.id + ' {background-image: url(' + comm.get('baseUrl') + d.smallIcon + ') !important;}';
			}
			if (!Ext.isEmpty(d.bigIcon)) {
				cssStr += '.css-32-' + d.id + ' {background-image: url(' + comm.get('baseUrl') + d.bigIcon + ') !important;}';
			}
			cssStr += this.earchCss(d.children);
		}, this);
		return cssStr;
	},
	/**
	 * 构建普通布局的菜单树
	 * @return {}
	 */
	buildMenuTree: function() {
		var self = this;
		var data = comm.get('initMenuData');
		var trees = [];
		Ext.each(data, function(d) { //循环生成菜单
			var child = d.children; //树形菜单数据
			var treeInfo = {
				xtype: 'treepanel',
				title: d.text,
				text: d.text,
				useArrows: true,
				border: 0,
				itemId: d.id,
				iconCls: 'css-' + d.id,
				bigIconCls: 'css-32-' + d.id,
				rootVisible: false,
				root: {
					text: '0',
					id: d.id,
					expanded: true,
					children: child
				}
			};
			trees.push(treeInfo);
		});
		return trees;
	},
	/**
	 * 构建开始菜单中的项
	 * @param {} node
	 */
	buildStartMenu: function(root, me) {
		var rootMenu = {};
		var eachMenus = function(node, menu) {
			node.eachChild(function(n) {
				var menuObj = {
					text: n.get("text"),
					icon: comm.get('baseUrl') + n.get("smallIcon"),
					handler: function() {
						me.desktop.onShortcutItemClick(null, n);
					}
				};
				if (menu.menu) {
					menu.menu.items.push(menuObj);
				} else {
					menu.menu = {
						items: [menuObj]
					}
				}
				eachMenus(n, menuObj);
			});
		}
		eachMenus(root, rootMenu);
		if (rootMenu.menu) {
			return rootMenu.menu.items;
		} else {
			return [];
		}
	}
});
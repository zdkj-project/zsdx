/**
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.system.roleright.controller.DetailController", {
    extend: "Ext.app.ViewController",

    alias: 'controller.roleright.detailController',

    /*把不需要使用的组件，移除掉*/
    mixins: {
        /*
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
        */
    },
   
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");           
    },

    /*该视图内的组件事件注册*/
    control:{
        "treepanel[xtype=roleright.selectmenugrid]": {
            checkchange: function(node, checked,options) {
                var self=this;
                // record.expand();
                // record.set( 'checked', flag);
                // if (flag){
                //  console.log(record.parentNode.data);
                //  //record.parentNode.data.checked=flag;
                //  //record.parentNode.updateInfo({checked:true});
                // }
                // if (record.childNodes.length > 0) {
                //  Ext.each(record.childNodes, function(rec) {
                //      rec.set( 'checked', flag);
                //      //rec.updateInfo({checked:flag});
                //  }, this);
                // } 
                //debugger;
                if (node.data.leaf == false) {
                    if (checked) {
                        //打开节点
                        node.expand();
                        //遍历孩子
                        node.eachChild(function(n) {
                            n.set( 'checked', checked);
                            //n.data.checked = true;
                            // n.updateInfo({
                            //  checked: true
                            // });
                        });
                    } else {
                        node.expand();
                        node.eachChild(function(n) {
                            n.set( 'checked', checked);
                            // n.updateInfo({
                            //  checked: false
                            // });
                        });
                    }
                } else { //单击叶子时候
                    if (checked) { //未被选中时，取消父节点的选择状态
                        node.parentNode.set('checked', checked);
                        //node.parentNode.data.checked = true;
                        // node.parentNode.updateInfo({
                        //  checked: true
                        // });
                    }
                }
                return false;
            }
        },
        /**
         * 菜单设置grid展开按钮事件，无效，原因不明
         * @type {[type]}
         */
        "panel[xtype=roleright.selectmenugrid] button[ref=gridExpand]": {
            click: function(btn) {
                var baseGrid = btn.up("basetreegrid");
                baseGrid.expandAll();
            }
        },
        /**
         * 菜单设置grid折叠按钮事件
         * @type {[type]}
         */
        "panel[xtype=roleright.selectmenugrid] button[ref=gridCollapse]": {
            click: function(btn) {
                var baseGrid = btn.up("basetreegrid");
                baseGrid.collapseAll();
            }
        },
    }

});
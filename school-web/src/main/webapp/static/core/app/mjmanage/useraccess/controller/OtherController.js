Ext.define("core.mjmanage.useraccess.controller.OtherController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.mjmanage.useraccess.othercontroller',
    mixins: {
    	suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function () {
    	var self = this;
    },
    /** 该视图内的组件事件注册 */
    control: {

    	//选择人员窗口的提交按钮
		"baseformwin[funCode=useraccess_detail] button[ref=formSave]": {
			beforeclick: function (btn) {    
				this.saveDetail_Win(btn);
				return false;
			}
		},
	   "basepanel basegrid[xtype=pubselect.selectusergrid] field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                var self = this;
                if (e.getKey() == e.ENTER) {
                    self.doFastSearch(field);
                    return false;
                }
            }
        },
        "basepanel basegrid[xtype=pubselect.selectusergrid] button[ref=gridFastSearchBtn]": {
            beforeclick: function (btn) {
                var self = this;
                self.doFastSearch(btn);
                return false;
            }
        },
    },
    
    //选择人员保存事件
    saveDetail_Win:function(btn){
        var self=this;
        var win = btn.up('window');
        var termId = win.termid;
        //var termSN = win.termSN;
        //var termName = win.termName;

        var employeeID = ""; //人员id
        var stuId = new Array();

        var tabPanel = Ext.ComponentQuery.query("tabpanel[xtype=app-main]")[0];
        var mainlayout = tabPanel.getActiveTab(); 
        var usExistGrid = mainlayout.down('panel[xtype=mjmanage.useraccess.maingrid]');

        var gridStore = usExistGrid.getStore();
        var gridCount = usExistGrid.getStore().getCount()
       
        var selectuserlayout = win.down("basepanel[xtype=pubselect.selectuserlayout]");
        var isSelectUserGrid = selectuserlayout.down("panel[xtype=pubselect.isselectusergrid]");
        var isSelectUserStore = isSelectUserGrid.getStore();
        var isSelectUserCount = isSelectUserStore.getCount();
        if (isSelectUserCount <= 0) {
            self.msgbox("有数据才能继续操作!");
            return;
        }
       for (var i = 0; i < isSelectUserCount; i++) {
            employeeID = isSelectUserStore.getAt(i).data.uuid;
            //已存的人员，去掉提示
       /*     for (var k = 0; k < gridCount; k++) {
                 var employeeid = gridStore.getAt(k).data.stuId;
                 var xm = gridStore.getAt(k).data.xm;
                if (employeeID == employeeid) {
                    self.msgbox("姓名为：" + xm + "的已存在!");
                    return ;
                }
            };*/
            if(stuId.indexOf(employeeID)==-1)
              stuId.push(employeeID);
          // stuId += employeeID + "," ;          
        }
        // var loading= self.LoadMask(win,"正在处理中...,请等待！");
        self.asyncAjax({
            url: comm.get('baseUrl') + "/BaseMjUserright/doAddMj",
            params: {
              userId: stuId.join(","),
              termId: termId,
          },          
        //回调代码必须写在里面
        success: function (response) {
            var data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
            if (data.success) {
                self.msgbox("提交成功!");
                usExistGrid.getStore().load();                         
                // loading.hide();
                win.close();

            } else {
                // loading.hide();
                self.Warning(data.obj);
                win.close();
            }
        },
        failure: function(response) {                   
            Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
            // loading.hide();
        }
    });

},
    /**
     * 执行快速搜索
     * @param component
     * @returns {boolean}
     */
    doFastSearch: function (component) {
        //得到组件
        var baseGrid = component.up("basegrid");
        if (!baseGrid)
            return false;

        var toolBar = component.up("toolbar");
        if (!toolBar)
            return false;


        var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
        //这里快速搜索就姓名与部门，固定写死查询的条件
        var filter = new Array();
        filter.push("{'type': 'string', 'comparison': '', 'value':'1', 'field': 'category'}");
        if (girdSearchTexts[0].getValue() != "")
        filter.push("{'type': 'string', 'comparison': '', 'value':'" + girdSearchTexts[0].getValue() + "', 'field': 'xm'}");
        if (girdSearchTexts[1].getValue() != "")
            filter.push("{'type': 'string', 'comparison': '=', 'value':'" + girdSearchTexts[1].getValue() + "', 'field': 'deptId'}");
        filter = "[" + filter.join(",") + "]";

        var selectStore = baseGrid.getStore();
        var selectProxy = selectStore.getProxy();
        selectProxy.extraParams = {
            filter: filter
        };
        selectStore.loadPage(1);
    }, 
});
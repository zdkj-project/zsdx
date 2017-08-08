Ext.define("core.train.course.controller.MainController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.course.mainController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil',
        sqlUtil: 'core.util.SqlUtil',
        treeUtil: "core.util.TreeUtil"

    },
    init: function () {
    },
    control: {
    	//关于权限的判断
    	"basegrid[xtype=course.maingrid]": {
            afterrender: function (grid, eOpts) {
                var btnAdd = grid.down("button[ref=gridAdd_Tab]");
                var btnDelete = grid.down("button[ref=gridDelete]");
                var btnGridImport = grid.down("button[ref=gridImport]");
                var btnGridExport = grid.down("button[ref=gridExport]");
                var btnGridDownTemplate = grid.down("button[ref=gridDownTemplate]");
                var roleKey = comm.get("roleKey");
                if (roleKey.indexOf("ROLE_ADMIN") == -1 && roleKey.indexOf("SCHOOLADMIN") == -1 && roleKey.indexOf("TCMANAGER") == -1) {
                    btnAdd.setHidden(true);
                    btnDelete.setHidden(true);
                    btnGridImport.setHidden(true);
                    btnGridExport.setHidden(true);
                    btnGridDownTemplate.setHidden(true);
                }
            }
        },
    	
        //重写高级查询
        "basequeryform button[ref=gridSearchFormOk]":{
            beforeclick:function(btn){     
                var self=this;

                //self.Info("暂时无法搜索！");                    
                var queryPanel = btn.up("basequeryform");
                var querySql1 = self.getQuerySql(queryPanel);
                var querySql = self.getQureyFilter(queryPanel);
                var funCode = queryPanel.funCode;
                var basePanel = queryPanel.up("basepanel[funCode=" + funCode + "]");

                //加入basegrid默认的filter
                var baseGrid = basePanel.down("basegrid[funCode=" + funCode + "]");
                var gridFilter=[];
                var filter=[];

                //获取baseGrid中编写的默认filter值
                var gridFilterStr=baseGrid.extParams.filter;
                if(gridFilterStr&&gridFilterStr.trim()!=""){
                    gridFilter=JSON.parse(gridFilterStr); 
                }

                if (querySql.trim().length > 0) {
                    filter=JSON.parse(querySql);  
                
                    for(var i=0;i<gridFilter.length;i++){
                        //判断gridFilter是否包含此值。
                        var isExist=false;
                        for(var j=0;j<filter.length;j++){
                            if(filter[j].field==gridFilter[i].field){                   
                                isExist=true;
                                break;
                            }
                        }
                        if(isExist==false)
                            filter.push(gridFilter[i]);
                 
                    }
                }else{
                    if(gridFilter.length>0){
                        filter=gridFilter;
                    }
                }    
                
                //判断是否选择的分类
                var categoryIdText=queryPanel.down("textfield[name=categoryId]");
                var categoryNameText=queryPanel.down("textfield[name=categoryName]");
                if(categoryIdText&&categoryIdText.getValue()){  
                    if(categoryNameText&&categoryNameText.getValue())   //当隐藏文本 和 显式的文本都有值的时候，才查询（点击小叉叉按钮，默认不会清除隐藏域文本）
                        filter.push({"type":"string","value":categoryIdText.getValue(),"field":"categoryId","comparison":""});
                }    

                var store = baseGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams.querySql = querySql1;
                proxy.extraParams.filter = JSON.stringify(filter);
                store.loadPage(1);
                
                return false;
            }
        },
        //追加重置功能
        "basequeryform button[ref=gridSearchFormReset]":{
            clicked:function(btn){
                //得到组件
                //btn.up("form").reset();
                //zzk 2017-4-5 修改        

                var queryPanel=btn.up("basequeryform");
              
                var categoryId=queryPanel.down("textfield[name=categoryId]");
                var categoryName=queryPanel.down("textfield[name=categoryName]");
                if(categoryId)
                    categoryId.reset();
                if(categoryName)
                    categoryName.reset();

            }
        },
        /**
         * 导入
         */
        "basegrid[xtype=course.maingrid] button[ref=gridImport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var win = Ext.create('Ext.Window', {
                    title: "导入课程数据",
                    iconCls: 'x-fa fa-clipboard',
                    width: 450,
                    resizable: false,
                    constrain: true,
                    autoHeight: true,
                    modal: true,
                    controller: 'course.otherController',
                    closeAction: 'close',
                    plain: true,
                    grid: baseGrid,
                    items: [{
                        xtype: "course.courseimportform"
                    }]
                });
                win.show();
                return false;
            }
        },
        /**
         * 导出
         */
        "basegrid[xtype=course.maingrid] button[ref=gridExport]": {
            beforeclick: function (btn) {
                var self = this;
                //得到组件
                var baseGrid = btn.up("basegrid");
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" + funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;
                //得到选中数据
                var records = baseGrid.getSelectionModel().getSelection();
                var title = "将导出所有的课程信息";
                var ids = new Array();
                if (records.length > 0) {
                    title = "将导出所选课程的信息";
                    Ext.each(records, function (rec) {
                        var pkValue = rec.get(pkName);
                        ids.push(pkValue);
                    });

                }
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainCourseinfo/exportExcel?ids=' + ids.join(",") + '&orderSql=order by categoryOrderindex,courseCode "></iframe>',
                            renderTo: Ext.getBody()
                        });

                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainCourseinfo/checkExportEnd',
                                timeout: 1000 * 60 * 30,        //半个小时
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                        Ext.Msg.hide();
                                        self.msgbox(data.obj);
                                        component.destroy();
                                    } else {
                                        setTimeout(function () {
                                            time()
                                        }, 1000);
                                    }
                                },
                                failure: function (response) {
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
                                    clearInterval(interval)
                                }
                            });
                        }
                        setTimeout(function () {
                            time()
                        }, 1000);    //延迟1秒执行
                    }
                });
                return false;
            }
        },
        /**
         * 下载模板
         */
        "basegrid[xtype=course.maingrid] button[ref=gridDownTemplate]": {
            beforeclick: function (btn) {
                var self = this;
                var title = "下载导入模板";
                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在下载模板,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: 'HelloWorld',
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/static/upload/template/course.xls"></iframe>',
                            renderTo: Ext.getBody()
                        });
                        setTimeout(function () {
                            Ext.Msg.hide();
                            component.destroy();
                        }, 2000);    //延迟1秒执行
                        //window.location.href = comm.get('baseUrl') + "/static/upload/template/course.xls";
                    }
                });
                return false;
            }
        },
        /**
         * 课程树点击事件
         */
        "panel[xtype=course.coursecategorytree]": {
            itemclick: function (tree, record, item, index, e) {
                var self = this;
                var mainLayout = tree.up("panel[xtype=course.mainlayout]");
                var categoryName = record.get("text");
                var categoryId = record.get("id");
                var categoryCode = record.get("nodeCode");
                var map = self.eachChildNode(record);
                var ids = new Array();
                map.eachKey(function (key) {
                    ids.push(key);
                });

                var filter = "[{'type':'string','comparison':'in','value':'" +
                    ids.join(",") + "','field':'categoryId'}]";

                var funData = mainLayout.funData;
                mainLayout.funData = Ext.apply(funData, {
                    filter: filter,
                    categoryName: categoryName,
                    categoryId: categoryId,
                    categoryCode: categoryCode
                });
                record.expand();
                // 加载相应的公告信息
                var storeyGrid = mainLayout.down("panel[xtype=course.maingrid]");
                var store = storeyGrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    filter: filter
                };
                store.loadPage(1); // 给form赋值
            }
        },

        /**
         * 课程列表增加按钮响应事件
         */
        "basegrid[xtype=course.maingrid]  button[ref=gridAdd_Tab]": {
            beforeclick: function (btn) {
                this.doCourseDetail_Tab(btn, "add");
                return false;
            }
        },
        /**
         * 课程列表操作列事件
         */
        "basegrid  actioncolumn": {
            //查看教师简介
            gridTeacherClick: function (data) {
                this.doCourseDetail_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            //查看课程简介
            gridCourseClick: function (data) {
                this.doCourseDetail_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            //查看课程评价情况
            gridEvalClick: function (data) {
                this.doCourseDetail_Tab(null, data.cmd, data.view, data.record);
                return false;
            },
            //编辑
            editClick_Tab: function (data) {
                this.doCourseDetail_Tab(null, "edit", data.view, data.record);
                return false;
            }
        }
    },
    /**
     * 课程各种详细页面的处理
     * @param btn
     * @param cmd
     * @param grid
     * @param record
     */
    doCourseDetail_Tab: function (btn, cmd, grid, record) {
        var self = this;
        var baseGrid;
        var recordData;

        if (btn) {
            baseGrid = btn.up("basegrid");
        } else {
            baseGrid = grid;
            recordData = record.getData();
        }

        //得到组件
        var funCode = baseGrid.funCode;
        var tabPanel = baseGrid.up("tabpanel[xtype=app-main]"); //标签页
        var basePanel = baseGrid.up("panel[funCode=" + funCode + "]");

        //得到配置信息
        var funData = basePanel.funData;
        var detCode = basePanel.detCode;
        var detLayout = basePanel.detLayout;
        var defaultObj = funData.defaultObj;

        var categoryId = funData.categoryId;  //选择的分类ID
        var categoryName = funData.categoryName; //选择的分类名称
        var categoryCode = funData.categoryCode; //选择的分类 的编码

        var operType = cmd;
        var pkValue = null;

        //关键：window的视图控制器
        var otherController = basePanel.otherController;
        if (!otherController)
            otherController = '';

        //处理特殊默认值
        var insertObj = self.getDefaultValue(defaultObj);
        insertObj = Ext.apply(insertObj, {
            categoryId: categoryId,
            categoryName: categoryName,
            categoryCode: categoryCode
        });
        //一些要传递的参数
        var popFunData = Ext.apply(funData, {
            grid: baseGrid,
            whereSql: " and isDelete='0' ",
            categoryId: categoryId,
            categoryName: categoryName,
            categoryCode: categoryCode
        });

        //默认的tab参数
        var tabTitle = funData.tabConfig.addTitle; //标签页的标题
        var tabItemId = funCode + "_gridAdd";     //命名规则：funCode+'_ref名称',确保不重复
        var itemXtype = "course.detailform";

        //根据操作命令组装不同的数据
        if (cmd != "add") {
            if (btn) {
                var rescords = baseGrid.getSelectionModel().getSelection();
                if (rescords.length != 1) {
                    self.msgbox("请选择1条数据！");
                    return;
                }
                recordData = rescords[0].getData();
            }

            insertObj = recordData;
            //获取主键值
            var pkName = funData.pkName;
            pkValue = recordData[pkName];
            switch (cmd) {
                case "edit":
                    tabTitle = funData.tabConfig.editTitle;
                    tabItemId = funCode + "_gridEdit";
                    break
                case "courseEval":
                    tabTitle = "课程评价_" + insertObj.courseName;
                    tabItemId = funCode + "_gridCourseEval";
                    itemXtype = "course.courseevalpanel";
                    break;
                case"coursDesc":
                    tabTitle = "课程简介_" + insertObj.courseName;
                    tabItemId = funCode + "_gridCourseDesc";
                    itemXtype = "course.coursedescpanel";
                    break;
                case "teacherDesc":
                    tabTitle = "教师简介_" + insertObj.courseName;
//                    tabItemId = funCode + "_gridDeacherDesc";
                    tabItemId ="";
                    itemXtype = "course.detailhtmlpanel";
                    break
            }
        }
        //获取tabItem；若不存在，则表示·要新建tab页，否则直接打开
        var tabItem = tabPanel.getComponent(tabItemId);

        //判断是否已经存在tab了
        if (!tabItem) {
            tabItem = Ext.create({
                xtype: 'container',
                title: tabTitle,
                scrollable: true,
                itemId: tabItemId,
                itemPKV: pkValue,    //保存主键值
                layout: 'fit',
                margin: 5
            });
            tabPanel.add(tabItem);

            //延迟放入到tab中
            setTimeout(function () {
                //创建组件
                var item = Ext.widget("baseformtab", {
                    operType: operType,
                    controller: otherController,         //指定重写事件的控制器
                    funCode: funCode,                    //指定mainLayout的funcode
                    detCode: detCode,                    //指定detailLayout的funcode
                    tabItemId: tabItemId,                //指定tab页的itemId
                    insertObj: insertObj,                //保存一些需要默认值，提供给提交事件中使用
                    funData: popFunData,                //保存funData数据，提供给提交事件中使用
                    items: [{
                        xtype: detLayout,
                        items: [{
                            xtype: itemXtype
                        }]
                    }]
                });
                tabItem.add(item);

                //根据不同的操作进行初始化
                switch (cmd) {
                    case "edit":
                        var objDetForm = item.down("baseform[funCode=" + detCode + "]");
                        var formDeptObj = objDetForm.getForm();
                        var courseDesc = objDetForm.down("htmleditor");

                        self.setFormValue(formDeptObj, insertObj);
                        courseDesc.setValue(insertObj.courseDesc);
                        if (insertObj.courseMode === 2) {
                            formDeptObj.findField("courseMode").setValue(true);
                        } else
                            formDeptObj.findField("courseMode").setValue(false);
                        //根据需要设置一些字段为只读
                        formDeptObj.findField("courseName").setDisabled(true);
                        formDeptObj.findField("courseName").setReadOnly(true);
                        formDeptObj.findField("courseMode").setDisabled(true);
                        formDeptObj.findField("courseMode").setReadOnly(true);
                        formDeptObj.findField("mainTeacherName").setDisabled(true);
                        formDeptObj.findField("mainTeacherName").setReadOnly(true);
                        break;
                    case "coursDesc":
                        var detailhtmlpanel = item.down("container[xtype=course.coursedescpanel]");
                        detailhtmlpanel.setData(recordData);
                        //console.log(recordData);
                        //detailhtmlpanel.show();
                        break;
                    case "teacherDesc":
                        
                        // var ddCode="XLM";
                        // var store=Ext.create("Ext.data.Store",{
                        //     fields:factory.ModelFactory.getFields({modelName:"com.zd.school.plartform.baseset.model.BaseDicitem",excludes:""}),
                        //     data:factory.DDCache.getItemByDDCode(ddCode)
                        // });
                        self.asyncAjax({
                            url: funData.action + "/courseteacher",
                            params: {
                                courseId:insertObj.uuid
                            },
                            //回调代码必须写在里面
                            success: function(response) {
                                data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                //console.log(data);
                                if(data.rows!=undefined){
                                    //将数据字典数据赋值到组件属性上
                                    //var storeData=store.getData();
                                    // for(var i=0;i<rescordsData.length;i++){
                                    //     for(var j=0;j<storeData.length;j++){
                                    //         var rec=storeData.items[j];
                                    //         if(rec.get("itemCode")==rescordsData[i].xlm){
                                    //             rescordsData[i].xlm=rec.get("itemName");
                                    //             break;
                                    //         }
                                    //     }
                                    // }
                                    var rescordsData=data.rows;
                                    var obj=[];
                                    for(var index=0;index<rescordsData.length;index++){
                                        var recordData=rescordsData[index];
                                        var ddCodes=['XBM','TECHNICAL','XLM','INOUT','HEADSHIPLEVEL'];
                                        var propNames=['xbm','technical','xlm','inout','headshipLevel'];
                                        for(var i=0;i<ddCodes.length;i++){                
                                            var ddItem = factory.DDCache.getItemByDDCode(ddCodes[i]);
                                            var resultVal="";
                                            var value=recordData[propNames[i]];
                                            for (var j = 0; j < ddItem.length; j++) {
                                                var ddObj = ddItem[j];
                                                if (value == ddObj["itemCode"]) {
                                                    resultVal = ddObj["itemName"];
                                                    break;
                                                }
                                            }
                                            recordData[propNames[i]]=resultVal;
                                        }

                                        //当登录用户不等于这个详细记录的人员，则隐藏身份证
                                        if(comm.get("xm")!=recordData.xm && recordData.sfzjh){
                                            recordData.sfzjh="**************"+recordData.sfzjh.substring(14);
                                        }

                                        obj.push(recordData);
                                    }
                                                                                                        
                                    var detailhtmlpanel = tabItem.down("container[xtype=course.detailhtmlpanel]");
                                    detailhtmlpanel.setData(obj);
                                    detailhtmlpanel.show() ;



                                    // var leftDiv=Ext.get("teacherDetailSlideDiv_left");
                                    // if(leftDiv)
                                    //     Ext.get("teacherDetailSlideDiv_left").on('click',function(e){

                                    //         var wrap=Ext.get("teacherDetailPanelMaxing_Wrap");

                                    //         var myMarginLeft = Ext.get("teacherDetailPanelMaxing_Wrap").myMarginLeft;
                                    //         myMarginLeft=myMarginLeft?myMarginLeft:0;
                                    //         //var currentMarginLeft=wrap.getStyle("margin-left").replace("px","")*1;
                                    //         if(myMarginLeft<0){
                                    //             var val=myMarginLeft+820;
                                    //             wrap.setStyle({'margin-left':val+'px'});    //因为存在动画事件，所以会有延迟
                                    //             Ext.get("teacherDetailPanelMaxing_Wrap").myMarginLeft=val;  //所以使用这个属性代替
                                    //         }else{
                                    //             self.msgbox("已经是第一张了！");
                                    //         }
                                    //     });

                                    // var rightDiv=Ext.get("teacherDetailSlideDiv_right");
                                    // if(rightDiv)
                                    //     Ext.get("teacherDetailSlideDiv_right").on('click',function(e){

                                    //         var wrap=Ext.get("teacherDetailPanelMaxing_Wrap");

                                    //         var myMarginLeft = Ext.get("teacherDetailPanelMaxing_Wrap").myMarginLeft;
                                    //         myMarginLeft=myMarginLeft?myMarginLeft:0;
                                    //         //var currentMarginLeft=wrap.getStyle("margin-left").replace("px","")*1;
                                    //         if(myMarginLeft>-820*(rescordsData.length-1)){
                                    //             var val=myMarginLeft+(-820);
                                    //             wrap.setStyle({'margin-left':val+'px'});
                                    //             Ext.get("teacherDetailPanelMaxing_Wrap").myMarginLeft=val;
                                    //         }else{
                                    //             self.msgbox("已经是最后一张了！");
                                    //         }
                                    //     });

                                }else{
                                    self.Error(data.obj?data.obj:"数据读取失败，请刷新页面！");
                                }
                            }
                        });
                        break;
                    case "courseEval":
                        var trainRecordGrid = tabItem.down("grid[xtype=course.courseevalpanel]");
                        var proxy = trainRecordGrid.getStore().getProxy();
                        proxy.extraParams.propName = "courseId,courseName";
                        proxy.extraParams.propValue = record.get("uuid") + "," + record.get("courseName");
                        proxy.extraParams.joinMethod = "or";
                        trainRecordGrid.getStore().loadPage(1);
                        break;
                }
            }, 30);

        } else if (tabItem.itemPKV && tabItem.itemPKV != pkValue) {     //判断是否点击的是同一条数据，不同则替换数据
            self.Warning("您当前已经打开了一个编辑窗口了！");
            return;
        }

        tabPanel.setActiveTab(tabItem);
    }
});

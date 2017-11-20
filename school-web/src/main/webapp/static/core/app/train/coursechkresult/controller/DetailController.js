/**
    ( *非必须，只要需要使用时，才创建他 )
    此视图控制器，提供于DeatilLayout范围内的界面组件注册事件
*/
Ext.define("core.train.coursechkresult.controller.DetailController", {
    extend: "Ext.app.ViewController",
    alias: 'controller.coursechkresult.detailController',
    mixins: {
        suppleUtil: "core.util.SuppleUtil",
        messageUtil: "core.util.MessageUtil",
        formUtil: "core.util.FormUtil",
        gridActionUtil: "core.util.GridActionUtil",
        dateUtil: 'core.util.DateUtil'
    },
    init: function() {
        /*执行一些初始化的代码*/
        //console.log("初始化 detail controler");     
    },
    /** 该视图内的组件事件注册 */
    control: {
        /*导出班级中的某个课程的考勤数据*/
        "basegrid[xtype=coursechkresult.classcoursegrid] button[ref=gridExport]": {
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
                if (records.length == 0) {
                    self.Warning("请选择一个或多个课程进行导出操作！");
                    return;
                }


                var title = "确定要导出所选的课程考勤信息吗？";

                var ids = new Array();      
                var classId = records[0].get("classId");      
                Ext.each(records, function (rec) {
                    var pkValue = rec.get(pkName);
                    ids.push(pkValue);
                });

                Ext.Msg.confirm('提示', title, function (btn, text) {
                    if (btn == "yes") {
                        Ext.Msg.wait('正在导出中,请稍后...', '温馨提示');
                        var component = Ext.create('Ext.Component', {
                            title: "HelloWord",
                            width: 0,
                            height: 0,
                            hidden: true,
                            html: '<iframe src="' + comm.get('baseUrl') + '/TrainClassschedule/exportCourseAttendExcel?classId='+classId+'&ids=' + ids.join(",") + '"></iframe>',
                            renderTo: Ext.getBody()
                        });


                        var time = function () {
                            self.syncAjax({
                                url: comm.get('baseUrl') + '/TrainClassschedule/checkCourseAttendExcelEnd',
                                timeout: 1000 * 60 * 30,        //半个小时
                                //回调代码必须写在里面
                                success: function (response) {
                                    data = Ext.decode(Ext.valueFrom(response.responseText, '{}'));
                                    if (data.success) {
                                        Ext.Msg.hide();
                                        self.Info(data.obj);
                                        component.destroy();
                                    } else {
                                        if (data.obj == 0) {    //当为此值，则表明导出失败
                                            Ext.Msg.hide();
                                            self.Error("导出失败，请重试或联系管理员！");
                                            component.destroy();
                                        } else {
                                            setTimeout(function () {
                                                time()
                                            }, 1000);
                                        }
                                    }
                                },
                                failure: function (response) {
                                    Ext.Msg.hide();
                                    Ext.Msg.alert('请求失败', '错误信息：\n' + response.responseText);
                                    component.destroy();
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

        "basegrid[xtype=coursechkresult.classcoursegrid]": {
            beforeitemclick: function (grid, record, item, index, e, eOpts) {
                var self = this;

                //var mainLayout = grid.up("panel[xtype=coursechkresult.detaillayout]");
                var mainTab = grid.up("baseformtab");

                //var classId = mainTab.funData.classId;
                var maingrid = mainTab.down("panel[xtype=coursechkresult.classstudentgrid]");
                var classId = record.get("classId");
                var classScheduleId = record.get("uuid");
                var store = maingrid.getStore();
                var proxy = store.getProxy();
                proxy.extraParams = {
                    classId: classId,
                    classScheduleId: classScheduleId
                };
                store.loadPage(1);

                return false;
            }
        },

        //快速搜索按按钮
        "basepanel basegrid button[ref=gridFastSearchBtn]": {
            click: function (btn) {
                //得到组件                 
                var baseGrid = btn.up("basegrid");
                if (!baseGrid)
                    return false;

                var toolBar = btn.up("toolbar");
                if (!toolBar)
                    return false;
                
                var store = baseGrid.getStore();
                var proxy = store.getProxy();

                var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                for (var i in girdSearchTexts) {
                    var name = girdSearchTexts[i].getName();
                    var value = girdSearchTexts[i].getValue();

                    proxy.extraParams[name]=value;
                }

                store.loadPage(1);
                return false;
            }
        },
        //快速搜索文本框回车事件
        "basepanel basegrid field[funCode=girdFastSearchText]": {
            specialkey: function (field, e) {
                if (e.getKey() == e.ENTER) {

                    //得到组件                 
                    var baseGrid = field.up("basegrid");
                    if (!baseGrid)
                        return false;

                    var toolBar = field.up("toolbar");
                    if (!toolBar)
                        return false;
                  

                    var store = baseGrid.getStore();
                    var proxy = store.getProxy();

                    var girdSearchTexts = toolBar.query("field[funCode=girdFastSearchText]");
                    for (var i in girdSearchTexts) {
                        var name = girdSearchTexts[i].getName();
                        var value = girdSearchTexts[i].getValue();

                        proxy.extraParams[name]=value;
                    }

                    store.loadPage(1);
                }
                return false;
            }
        },

        "basegrid[xtype=coursechkresult.classstudentgrid]  actioncolumn": {        
            gridRemark: function(data) {
                var self = this;
                var baseGrid = data.view;
                var record = data.record;
                
                var basePanel = baseGrid.up("basepanel[funCode=coursechkresult_detail]");            
            
                            
                //关键：window的视图控制器
                var otherController ='coursechkresult.otherController';
            
                var insertObj=record.getData();

                var popFunData = Ext.apply(basePanel.funData, {
                    grid: baseGrid
                });

                var width = 500;
                var height = 220;      

                var iconCls = 'x-fa fa-plus-circle';
                var operType = "edit";
                var title = "备注信息";
                        


                var win = Ext.create('core.base.view.BaseFormWin', {
                    title: title,
                    iconCls: iconCls,
                    operType: operType,
                    width: width,
                    height: height,
                    controller: otherController,
                    funData: popFunData,
                    funCode: "remark_detail",    //修改此funCode，方便用于捕获window的确定按钮
                    insertObj: insertObj,
                    record:record,
                    items: [{
                        xtype:'meetinginfo.detaillayout',
                        minWidth:200, 
                        items: [{
                            xtype: "coursechkresult.remarkform"
                        }]               
                    }]
                });
                win.show();

                var objDetForm = win.down("baseform[xtype=coursechkresult.remarkform]");
                var formDeptObj = objDetForm.getForm();

                self.setFormValue(formDeptObj, insertObj);


                return false
            },
            gridSetLeave: function(data) {            
                var self = this;

                var baseGrid = data.view;
                var record = data.record;
                var value = data.value;
                var funCode = baseGrid.funCode;
                var basePanel = baseGrid.up("basepanel[funCode=" +
                    funCode + "]");
                //得到配置信息
                var funData = basePanel.funData;
                var pkName = funData.pkName;

                if(value==1)
                    var title='是否要请假？';
                else
                    var title='是否要取消请假？';

                Ext.Msg.confirm('提示', title, function(btn, text) {
                    if (btn == 'yes') {
                        //发送ajax请求
                        var resObj = self.ajax({
                            url: comm.get("baseUrl") +
                                "/TrainCourseattend/doUpdateLeave",
                            params: {
                                classId: record.get("classId"),
                                classScheduleId:record.get("classScheduleId"),
                                classTraineeId:record.get("classTraineeId"),
                                val:value
                            }
                        });
                        if (resObj.success) {
                            baseGrid.getStore().load();

                            self.Info(resObj.obj);

                        } else {
                            self.Error(resObj.obj);
                        }
                    }
                });


            },
        }
    }   
});
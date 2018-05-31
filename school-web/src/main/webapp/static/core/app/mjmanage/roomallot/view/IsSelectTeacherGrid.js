Ext.define("core.mjmanage.roomallot.view.IsSelecTeacherGrid", {
    extend: "Ext.grid.Panel",
    alias: "widget.mjmanage.roomallot.isselectteachergrid",
    extParams: {},
    title: "<font color=#ffeb00>已选教师(选中后向左拖动或双击删除)</font>",
    columnLines: true,
    loadMask: true,
    multiSelect: true,
    selModel: {
        type: "checkboxmodel",   
        headerWidth:30,    //设置这个值为50。 但columns中的defaults中设置宽度，会影响他
        //mode:'single',  //multi,simple,single；默认为多选multi
        checkOnly:true,    //如果值为true，则只用点击checkbox列才能选中此条记录
        //allowDeselect:true, //如果值true，并且mode值为单选（single）时，可以通过点击checkbox取消对其的选择
    },
     store: {
        type: "mjmanage.roomallot.isselectteacherstore"
    },
    viewConfig: {
        stripeRows: true
    },
    columns: {
        defaults: {
            titleAlign: "center"
        },
        items: [{
            text: "主键",
            dataIndex: "uuid",
            hidden: true
        }, {
            minWidth:120,
            flex:1,
            text: "教师工号",
            dataIndex: "userNumb",            
        }, {
            width:100,
            text: "教师姓名",
            dataIndex: "xm",            
        }, {
            width:100,
            text: "教师性别",
            dataIndex: "xbm",
            renderer: function(value) {
                switch (value) {
                    case '1':
                    return '男';
                    break;
                    case '2':
                    return '女';
                    break;
                }
            }
        },{
            width:100,
            text: "部门",
            dataIndex: "deptName"
        }, {
            width:100,
            text: "岗位",
            dataIndex: "jobName"
        }]
    },
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            ddGroup: "DrapDropGroup"
        },
        listeners: {
            drop: function(node, data, dropRec, dropPosition) {
                //var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
            },
            beforedrop:function(node, data, overModel, dropPosition, dropHandlers){             
                var newRec=data.records;
                var arrays=new Array();
                
                var isSelectStore = this.grid.getStore();
                var oldRec=isSelectStore.getData().items;
                var isExist=null;
                for(var i in newRec){
                    isExist=false;
                    for(var j in oldRec){
                        if(newRec[i].get("uuid")==oldRec[j].get("uuid")){
                            //isSelectStore.remove(oldRec[j]);   //方式一：移除右边的原有数据
                            //this.refresh();
                            isExist=true;
                            break;
                        }                  
                    }
                    if(isExist==false)
                        arrays.push(newRec[i]);                        
                }
                
                if(arrays.length==0)
                    return false;
                else /*if(newRec.length==arrays.length)*/
                    data.records=arrays;    //方式二：移除左边的数据
                //return false;
            },
            beforeitemdblclick: function (grid, record, item, index, e, eOpts) {
                IsSelectStore = grid.getStore();
                IsSelectStore.removeAt(index);

                var basePanel = grid.up("basepanel[xtype=mjmanage.roomallot.selectteacherlayout]");
                var selectGrid = basePanel.down("basegrid[xtype=mjmanage.roomallot.selectteachergrid]");
                var selectStore = selectGrid.getStore();
                selectStore.insert(0, [record]);
                return false;
            }
        }
    },
})
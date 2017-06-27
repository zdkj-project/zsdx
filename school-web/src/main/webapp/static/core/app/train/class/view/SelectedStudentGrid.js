Ext.define("core.train.class.view.SelectedStudentGrid", {
    extend: "Ext.grid.Panel",
    alias: "widget.class.selectedstudentgrid",
    ref:'selectedStudentGrid',
    title: "<font color='#ffeb00'>已选学员(选中后向左拖动移除）</font>",
    columnLines: true,
    loadMask: true,
    multiSelect: true,      
    selModel: {
        selType: "checkboxmodel",
        width:10
    },
    viewConfig: {
        stripeRows: true
    },
    store:{
        type:"class.selectedstudentgridStore"
    },
    columns: [
        { text: '班级学生ID',  dataIndex: 'classTraineeId', hidden:true },
        { text: '学生ID',  dataIndex: 'uuid', hidden:true },
        { text: '电话号码',  dataIndex: 'mobilePhone', hidden:true },
        { text: '身份证号码',  dataIndex: 'sfzjh', hidden:true },
        { text: '姓名', dataIndex: 'xm', flex: 1.5 },
        { text: '性别',  dataIndex: 'xbm', flex: 1,
            renderer:function(value){
                if(value=="1")
                    return "男";
                else if(value=="2")
                    return "女";
                else
                    return "";
            }
        }
    ],
    viewConfig: {
        plugins: {
            ptype: 'gridviewdragdrop',
            dragGroup: 'secondGridDDGroup',
            dropGroup: 'firstGridDDGroup'
        },
        listeners: {
            drop: function(node, data, dropRec, dropPosition) {
                var dropOn = dropRec ? ' ' + dropPosition + ' ' + dropRec.get('name') : ' on empty view';
                //Ext.example.msg("Drag from right to left", 'Dropped ' + data.records[0].get('name') + dropOn);  
            }
        }
    },
});
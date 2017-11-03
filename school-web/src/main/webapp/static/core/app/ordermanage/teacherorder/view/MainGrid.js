Ext.define("core.ordermanage.teacherorder.view.MainGrid", {
    extend: "core.base.view.BaseGrid",
    alias: "widget.teacherorder.maingrid",
    frame: false,
    columnLines: false,
    sortableColumns :false,
    dataUrl: comm.get("baseUrl") + "/TrainTeacherOrder/getOrderList", //数据获取地址
    model: "com.zd.school.jw.train.model.TrainTeacherOrder", //对应的数据模型
    /**
     * 高级查询面板
     */
    panelButtomBar: null,
    /**
     * 工具栏操作按钮
     * 继承自core.base.view.BaseGrid可以在此覆盖重写
     */
    panelTopBar:{
        xtype:'toolbar',
        items: [ {
            xtype: 'tbtext',
            html: '教职工订餐',
            style: {
                fontSize: '16px',
                color: '#C44444',
                fontWeight:800
            }
        }]
    },
    /** 排序字段定义 */
    defSort: [
//    	{
//        property: "createTime", //字段名
//        direction: "DESC" //升降序
//    },
    {
        property: "dinnerDate", //字段名
        direction: "ASC" //升降序
    }
    	],
    /** 扩展参数 */
    extParams: {
        whereSql: "",
        //查询的过滤字段
        //type:字段类型 comparison:过滤的比较符 value:过滤字段值 field:过滤字段名
        //zzk:2017-6-19 若写在这，必须按标准格式编写json字符串（即属性和值使用双引号扩囊）
        //filter: '[{"type":"numeric","comparison":"!=","value":0,"field":"isuse"}]' 
    },
    columns: {
        defaults: {
            align: 'center',
            titleAlign: "center"
        },
        items: [{
            xtype: "rownumberer",
            flex: 0,
            width: 50,
            text: '序号',
            align: 'center'
        }, {
            width: 250,
            text: "订餐日期",
            dataIndex: "dinnerDate",
            renderer: function(value, metaData) {
                var date = value.replace(new RegExp(/-/gm), "/");    
                var ss = Ext.Date.format(new Date(date), 'Y年m月d日 - D')           
                return ss;
            }
        }, {
            width: 200,
            text: "订餐情况",
            dataIndex: "dinnerGroup",
            align:'center',
            renderer: function(value, metaData) {
                if(value==1)
                    return "<span style='color:green'>A套餐</span>";
                else if(value==2)
                    return "<span style='color:blue'>B套餐</span>";
                else
                    return "<span style='color:red'>未订餐</span>";
            }
        },{
            flex:1,
            minWidth: 200,
            text: "备注",
            dataIndex: "remark",
            renderer: function (value, metaData) {
                var title = " 备注 ";
                metaData.tdAttr = 'data-qtitle="' + title +
                    '" data-qtip="' + value + '"';
                return value;
            }
        },{
            xtype: 'actiontextcolumn',
            text: "订餐操作",
            width: 300,
            align:'center',
            fixed: true,
            items: [{
                text: ' A套餐 ',
                style: 'font-size:13px;',
                tooltip: 'A套餐',
                ref: 'gridArrangeSite',
               
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridSelect', {
                        view: view.grid,
                        record: rec,
                        value:1
                    });
                }
            }, {
                text: ' B套餐 ',
                style: 'font-size:13px;',
                tooltip: 'B套餐',
                ref: 'gridArrangeRoom',
                handler: function (view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridSelect', {
                        view: view.grid,
                        record: rec,
                        value:2
                    });
                }
            }, {
                text: ' 取消订餐 ',
                style: 'font-size:13px;',
                tooltip: '取消订餐',
                ref: 'gridFoodDetail',        
                handler: function (view, rowIndex,colIndex, item) {
                    var rec = view.getStore().getAt(
                        rowIndex);
                    this.fireEvent('gridCancel', {
                        view: view.grid,
                        record: rec
                    });
                },
                getClass :function(v,metadata,record){
                
                    if(record.get("dinnerGroup")!=1 && record.get("dinnerGroup")!=2){
                        return 'x-hidden-display';
                    }
                    else
                        return null;
                }, 
            }, {
                text:' 备注 ',  
                style:'font-size:13px;',  
                tooltip: '备注',
                ref: 'gridDetail',
                handler: function(view, rowIndex, colIndex, item) {
                    var rec = view.getStore().getAt(rowIndex);
                    this.fireEvent('gridRemark', {
                        view: view.grid,
                        record: rec
                    });
                },
                getClass :function(v,metadata,record){
                
                    if(record.get("dinnerGroup")!=1 && record.get("dinnerGroup")!=2){
                        return 'x-hidden-display';
                    }
                    else
                        return null;
                }, 
            }]
        }]
    },
});

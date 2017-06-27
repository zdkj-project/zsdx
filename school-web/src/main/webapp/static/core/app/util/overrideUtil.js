/*
重写组件
*/
/**
 * 字段基类加载的时侯增加funCode属性
 */
Ext.override(Ext.form.field.Base,{
    onRender : function() {
        var me = this;
        var form = me.up('baseform');
        if(form&&!me.isNotForm){        //zzk:2017-6-2 对表单下的表格中的快速搜索造成影响，特加入一个判断条件
            me.funCode=form.funCode;
        }
        me.callParent(arguments);
     }
});
/**
 * 重写数值框字段，设置默认和最小值
 */
Ext.override(Ext.form.field.Number,{
    value:0,
    minValue:0
})
/**
 * 重写baseForm
 */
Ext.override(Ext.form.Basic,{
    constructor :  function(owner, config){
        var me = this;
        //增加只读所有字段的方法
        me.setItemsReadOnly = function(flag){
            var fs = this.getFields();
            fs.each(function(f){
                if(!f.initialConfig.readOnly){
                    f.setReadOnly(flag);
                }
            })
        };
        me.callParent(arguments);
    },
    //如果是功能配置的form则只获取当前form下的字段
    getFields : function(){
        var fields = this._fields;
        if (!fields) {
            fields = this._fields = Ext.create('Ext.util.MixedCollection');
            if(this.owner.funCode){
                fields.addAll(this.owner.query('[isFormField][funCode='+this.owner.funCode+']'));
            }else{
                fields.addAll(this.owner.query('[isFormField]'));
            }
        }
        return fields
    }
});
Ext.override(Ext.form.field.Trigger,{
    setReadOnly: function(readOnly){
        var me = this;
        //console.log(me.readOnlyCls);
        me[readOnly ? 'addCls' : 'removeCls'](me.readOnlyCls);
        this.callParent(arguments);

    }
});
/**
 * 增加按钮的点击前点击后事件
 */
 
Ext.override(Ext.button.Button,{
    /*
    initComponent: function() {
        debugger
        var me = this;
        if(!Ext.isEmpty(me.handler) && Ext.isString(me.handler)){
            me.handler = eval(me.handler);
        }
        //注：5.0版本后，此方法已被移除；  此代码：self.fireEvent('beforeclick', self)可以直接生效，不需要事先addEvents； zzk
        //me.addEvents('beforeclick','clicked');
        me.callParent(arguments);
    },*/
    onClick : function(e){
        
        var me = this;
        if (me.preventDefault || (me.disabled && me.getHref()) && e) {
            e.preventDefault();
        }
        if (e.button !== 0) {
            return;
        }
        if (!me.disabled) {
            if (me.enableToggle && (me.allowDepress !== false || !me.pressed)) {
                me.toggle();
            }
            if (me.menu && !me.hasVisibleMenu() && !me.ignoreNextClick) {
                me.showMenu();
            }
            var flag = me.fireEvent('beforeclick', me, e);//单击前
            if(flag != false){
                me.onBlur();

                me.fireEvent('click', me, e);
                
                /*此处有bug，因此 button的handler属性暂时不可用*/
                if(!Ext.isEmpty(me.handler) && Ext.isString(me.handler)){
                    me.handler = eval(me.handler);
                }
                if (me.handler) {
                    me.handler.call(me.scope || me, me, e);
                }
                
              
            }else{                
                //me.onBlur();  暂时去除，不明确有什么效果
            }
            me.fireEvent('clicked', me, e);//单击后
        }
    }
});

/**附件字段的改造*/
// Ext.override(Ext.form.field.File,{
//  setReadOnly : function(readOnly){
//      var me = this;
//      if(me.buttonEl)me.buttonEl.setVisible(!readOnly);//隐藏浏览按钮
//         me.callParent(arguments);
//  },
//  buttonText: '浏览',
//  setValue : function(v){
//         var me = this,inputEl = me.inputEl;
//      var data = {docName : ''};
//      if(!Ext.isEmpty(v)){
//          //截取文件名
//          var index = v.lastIndexOf('/');
//          if(index == -1){
//              index = v.lastIndexOf('\\');
//          }
//          var fn = v.substring(index+1,v.length);
//          data.docName=fn;

//      }
//      v = Ext.value(v,'');
//      if(me.hiddenEl){//设置下载信息
//          if(me.hiddenElType == 'img'){
//              me.hiddenEl.dom.src = v;
//              me.hiddenEl.dom.alt = Ext.value(data.docName,v);
//          }
//      }
//      data.address=v;
//      //如果没有附件，则以自己本身的值作为路径
//      data.address = Ext.value(data.address,v);
//      me.fileData = data;
//         if (inputEl && me.emptyText && !Ext.isEmpty(value)) {
//             inputEl.removeCls(me.emptyCls);
//         }
//      if(inputEl){
//          inputEl.dom.value = Ext.value(data.docName,v);
//      }
//         me.callParent(arguments);
//         me.applyEmptyText();
//  },
//  getAddress:function(){
//      return this.fileData.address;
//  },
//  afterRender: function(){
//      var me = this;
//         me.callParent();
//          //下载链接
//      var html = "<a style='color : red;' href = '#' target='_black'></a>";
//         if(!Ext.isEmpty(me.configInfo)){
//          var cs = me.configInfo.split(',');
//          var type = cs[0];
//          var formWH = Ext.value(cs[1],'');
//          var width=Ext.isEmpty(formWH.split('*')[0]) ? '' : ' width = '+formWH.split('*')[0];
//          var height=Ext.isEmpty(formWH.split('*')[1]) ? '' : ' height = '+formWH.split('*')[1];
//          if('img' == type){
//              me.hiddenElType = 'img';
//          }
//          html = "<img src ='' "+width+" "+height+" >";
//      }
//      me.hiddenEl = me.bodyEl.insertHtml('afterBegin',html,true);
//      me.inputEl.dom.onclick = function(){
//          if(me.fileData && !Ext.isEmpty(me.fileData.address)){
//              window.open(me.fileData.address);
//          }
//      };
//      me.inputEl.dom.style.textDecoration = 'underline';
//      me.inputEl.dom.style.color = 'blue';
//     },
//     getText : function(){
//      return this.fileData.docName;
//     }
// });



Ext.override(Ext.form.field.Base,{
    /*
    fieldSubTpl: [
        '<input id="{id}" type="{type}" {inputAttrTpl}',
            ' size="1"',
            '<tpl if="name"> name="{name}"</tpl>',
            '<tpl if="value"> value="{value}"</tpl>',
            '<tpl if="placeholder"> placeholder="{placeholder}"</tpl>',
            '<tpl if="maxLength !== undefined"> maxlength="{maxLength}"</tpl>',
            '<tpl if="readOnly"> readonly="readonly"</tpl>',
            '<tpl if="disabled"> disabled="disabled"</tpl>',
            '<tpl if="tabIdx"> tabIndex="{tabIdx}"</tpl>',
            '<tpl if="fieldStyle"> style="{fieldStyle}
            <tpl if="readOnly">;background:#E6E6E6;</tpl>"</tpl>',
        ' class="{fieldCls} {typeCls} {editableCls}" autocomplete="off"/>',
        {
            disableFormats: true
        }
    ]*/
    fieldSubTpl: [ // note: {id} here is really {inputId}, but {cmpId} is available 
        '<input id="{id}" data-ref="inputEl" type="{type}" {inputAttrTpl}',
            ' size="1"', // allows inputs to fully respect CSS widths across all browsers 
            '<tpl if="name"> name="{name}"</tpl>',
            '<tpl if="value"> value="{[Ext.util.Format.htmlEncode(values.value)]}"</tpl>',
            '<tpl if="placeholder"> placeholder="{placeholder}"</tpl>',
            '{%if (values.maxLength !== undefined){%} maxlength="{maxLength}"{%}%}',
            '<tpl if="readOnly"> readonly="readonly"</tpl>',
            '<tpl if="disabled"> disabled="disabled"</tpl>',
            '<tpl if="tabIdx != null"> tabindex="{tabIdx}"</tpl>',
            '<tpl if="fieldStyle"> style="{fieldStyle}"</tpl>',
            '<tpl if="ariaEl == \'inputEl\'">',
                '<tpl foreach="ariaElAttributes"> {$}="{.}"</tpl>',
            '</tpl>',
            '<tpl foreach="inputElAriaAttributes"> {$}="{.}"</tpl>',
        ' class="{fieldCls} {typeCls} {typeCls}-{ui} {editableCls} {inputCls}" autocomplete="off"/>',
        {
            disableFormats: true
        }
    ],
    /*
    fieldSubTpl:[
        '<input id="{id}" data-ref="inputEl" type="{type}" {inputAttrTpl}',
         ' size="1"', // allows inputs to fully respect CSS widths across all browsers 
         '<tpl if="name"> name="{name}"</tpl>', 
         '<tpl if="value"> value="{[Ext.util.Format.htmlEncode(values.value)]}"</tpl>', 
         '<tpl if="placeholder"> placeholder="{placeholder}"</tpl>', 
         '{%if (values.maxLength !== undefined){%} maxlength="{maxLength}"{%}%}', 
         '<tpl if="readOnly"> readonly="readonly" style="background:#f5f5f5;"</tpl>',
         '<tpl if="disabled"> disabled="disabled"</tpl>', 
         '<tpl if="tabIdx != null"> tabindex="{tabIdx}"</tpl>', 
         '<tpl if="fieldStyle"> style="{fieldStyle}"</tpl>', 
         '<tpl if="ariaEl == \'inputEl\'">', 
         '<tpl foreach="ariaElAttributes"> {$}="{.}"</tpl>',
     '</tpl>', 
     '<tpl foreach="inputElAriaAttributes"> {$}="{.}"</tpl>', 
     ' class="{fieldCls} {typeCls} {typeCls}-{ui} {editableCls} {inputCls}" autocomplete="off"/>',
      { disableFormats: true } 
    ]*/
})


//重写表格列， 列表头对齐方式
Ext.override(Ext.grid.column.Column,{
    //要重写的方法  
    afterRender: function() {  
       
        var me = this,  
                el = me.el;  
            me.callParent(arguments); 
     
        me.titleAlign = me.titleAlign || me.align;  
        el.addCls(Ext.baseCSSPrefix + 'column-header-align-' + me.titleAlign).addClsOnOver(me.overCls);  
    }

});


Ext.override(Ext.app.Application,{
   
    getViewController: function(name, preventCreate) {
        var me = this,
            controllers = me.controllers,
            className, controller, len, i, c, all;
 
        // First check with the passed value if we have an explicit id 
        controller = controllers.get(name);
        
        // In a majority of cases, the controller id will be the same as the name. 
        // However, when a controller is manually given an id, it will be keyed 
        // in the collection that way. So if we don't find it, we attempt to loop 
        // over the existing controllers and find it by classname 
        if (!controller) {
            all = controllers.items;
            for (i = 0, len = all.length; i < len; ++i) {
                c = all[i];
                className = c.moduleClassName;
                if (className && className === name) {
                    controller = c;
                    break;
                }
            }
        }
 
        if (!controller && !preventCreate) {
            className  = name;
            
            controller = Ext.create(className, {
                application: me,
                moduleClassName: className
            });
 
            controllers.add(controller);
            
            /*会自动执行init
            if (me._initialized) {
                controller.init(me);
            }
            */
        }
 
        return controller;
    },
});




Ext.override(Ext.grid.column.Column,{
    renderTpl: [
        '<div id="{id}-titleEl" data-ref="titleEl" role="presentation"',
            '{tipMarkup}class="', Ext.baseCSSPrefix, 'column-header-inner<tpl if="!$comp.isContainer"> ', Ext.baseCSSPrefix, 'leaf-column-header</tpl>',
            '<tpl if="empty"> ', Ext.baseCSSPrefix, 'column-header-inner-empty</tpl>">',
            // 
            // TODO: 
            // When IE8 retires, revisit https://jsbin.com/honawo/quiet for better way to center header text 
            // 
            '<div id="{id}-textContainerEl" data-ref="textContainerEl" role="presentation" class="', Ext.baseCSSPrefix, 'column-header-text-container">',
                '<div role="presentation" class="', Ext.baseCSSPrefix, 'column-header-text-wrapper">',
                    '<div id="{id}-textEl" data-ref="textEl" role="presentation" class="', Ext.baseCSSPrefix, 'column-header-text',
                        '{childElCls}">',
                        '{%',//为了实现将表头的checkbox选择框放置于text左边
                            'values.$comp.afterText(out, values);',
                        '%}',
                        '<span id="{id}-textInnerEl" data-ref="textInnerEl" role="presentation" class="', Ext.baseCSSPrefix, 'column-header-text-inner">{text}</span>',
                    '</div>',
                    /*原版的位置
                    '{%',
                        'values.$comp.afterText(out, values);',
                    '%}',
                    */            
                '</div>',
            '</div>',
            '<tpl if="!menuDisabled">',
                '<div id="{id}-triggerEl" data-ref="triggerEl" role="presentation" unselectable="on" class="', Ext.baseCSSPrefix, 'column-header-trigger',
                '{childElCls}" style="{triggerStyle}"></div>',
            '</tpl>',
        '</div>',
        '{%this.renderContainer(out,values)%}'
    ],
});


/**
重写下拉选择框，加入清除按钮
*/
Ext.override(Ext.form.field.ComboBox,{
    triggers: {
        clear: {
            cls:Ext.baseCSSPrefix + 'form-clear-trigger',
            handler:function(btn){
                var me=this;
                me.reset();
            },
            weight:-1,
            scope: 'this',
            focusOnMousedown: true
        },
    }
})
//重写timefield获取值的方法
Ext.override(Ext.form.field.Time,{
    
    getValue: function () {
        var me = this;

        if(me.multiSelect)
            return me.rawToValue(me.callParent(arguments)); //原有的处理方式
        else{
            var format = me.submitFormat || me.format,
                value = me.value;
     
            return value ? Ext.Date.format(value, format) : null;
        }
    }
});



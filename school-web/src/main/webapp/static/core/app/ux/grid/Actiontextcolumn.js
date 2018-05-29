/**
 * @Description 列表的操作列显示文章
 * @author		zzk
 */
Ext.define('Ext.ux.grid.Actiontextcolumn', {
    extend: 'Ext.grid.column.Action',
    alias: ['widget.actiontextcolumn'],
   
    
    defaultRenderer: function(v, cellValues, record, rowIdx, colIdx, store, view) {
  
        var me = this,
            scope = me.origScope || me,
            items = me.items,
            len = items.length,
            i, item, ret, disabled, tooltip, altText, icon, glyph, tabIndex, ariaRole,text,style;
 
        // Allow a configured renderer to create initial value (And set the other values in the "metadata" argument!) 
        // Assign a new variable here, since if we modify "v" it will also modify the arguments collection, meaning 
        // we will pass an incorrect value to getClass/getTip 
        ret = Ext.isFunction(me.origRenderer) ? me.origRenderer.apply(scope, arguments) || '' : '';
 
        cellValues.tdCls += ' ' + Ext.baseCSSPrefix + 'action-col-cell';
        for (i = 0; i < len; i++) {
            item = items[i];
            icon = item.icon;
            glyph = item.glyph;
 
            disabled = item.disabled || (item.isDisabled ? Ext.callback(item.isDisabled, item.scope || me.origScope, [view, rowIdx, colIdx, item, record], 0, me) : false);
            tooltip  = item.tooltip  || (item.getTip     ? Ext.callback(item.getTip, item.scope || me.origScope, arguments, 0, me) : null);
            altText  =                   item.getAltText ? Ext.callback(item.getAltText, item.scope || me.origScope, arguments, 0, me) : item.altText || me.altText;
    
            text= item.text?item.text:'';
            style = item.style?item.style:null;
            // Only process the item action setup once. 
            if (!item.hasActionConfiguration) {
                // Apply our documented default to all items 
                item.stopSelection = me.stopSelection;
                item.disable = Ext.Function.bind(me.disableAction, me, [i], 0);
                item.enable = Ext.Function.bind(me.enableAction, me, [i], 0);
                item.hasActionConfiguration = true;
            }
 
            // If the ActionItem is using a glyph, convert it to an Ext.Glyph instance so we can extract the data easily. 
            if (glyph) {
                glyph = Ext.Glyph.fly(glyph);
            }
 
            // Pull in tabIndex and ariarRols from item, unless the item is this, in which case 
            // that would be wrong, and the icon would get column header values. 
            tabIndex = (item !== me && item.tabIndex !== undefined) ? item.tabIndex : me.itemTabIndex;
            ariaRole = (item !== me && item.ariaRole !== undefined) ? item.ariaRole : me.itemAriaRole;
 
            ret += '<div' + 
                (typeof tabIndex === 'number' ? ' tabIndex="' + tabIndex + '"' : '') +
                (ariaRole ? ' role="' + ariaRole + '"' : ' role="presentation"') +
                (icon ? (' alt="' + altText + '" src="' + item.icon + '"') : '') +
                ' class=" x-action-col-mytext ' + Ext.baseCSSPrefix + 'action-col-' + String(i) + ' ' +
                (disabled ? me.disabledCls + ' ' : ' ') +
                (item.hidden ? Ext.baseCSSPrefix + 'hidden-display ' : '') +
                (item.getClass ? Ext.callback(item.getClass, item.scope || me.origScope, arguments, undefined, me) : (item.iconCls || me.iconCls || '')) + '"' +
                (tooltip ? ' data-qtip="' + tooltip + '"' : '') +
                (style ? ' style="' + style + '"' : '') + '>'+text+'</div>';
            
        }
        
        return ret;
    }
    



    
 
});
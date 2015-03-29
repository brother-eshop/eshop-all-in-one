/*
Copyright 2014, KISSY v1.47
MIT Licensed
build time: May 22 12:28
*/
KISSY.add("filter-menu/render-xtpl",["component/extension/content-xtpl"],function(j,h,l,i){return function(b){var a,e;a=this.config.utils;"undefined"!==typeof i&&i.kissy&&(e=i);var g=a.renderOutput,f=a.runInlineCommand,c=a.getPropertyOrRunCommand;a='<div id="ks-filter-menu-input-wrap-';var d=c(this,b,{},"id",0,1);a+=g(d,!0);a+='"\n     class="';var d={},k=[];k.push("input-wrap");d.params=k;d=f(this,b,d,"getBaseCssClasses",2);a+=g(d,!0);a+='">\n    <div id="ks-filter-menu-placeholder-';d=c(this,b,
{},"id",0,3);a+=g(d,!0);a+='"\n         class="';d={};k=[];k.push("placeholder");d.params=k;d=f(this,b,d,"getBaseCssClasses",4);a+=g(d,!0);a+='">\n        ';d=c(this,b,{},"placeholder",0,5);a+=g(d,!0);a+='\n    </div>\n    <input id="ks-filter-menu-input-';c=c(this,b,{},"id",0,7);a+=g(c,!0);a+='"\n           class="';c={};d=[];d.push("input");c.params=d;c=f(this,b,c,"getBaseCssClasses",8);a+=g(c,!0);a+='"\n            autocomplete="off"/>\n</div>\n';c={};d=[];d.push("component/extension/content-xtpl");
c.params=d;e&&(h("component/extension/content-xtpl"),c.params[0]=e.resolveByName(c.params[0]));b=f(this,b,c,"include",11);return a+=g(b,!1)}});
KISSY.add("filter-menu/render",["menu","./render-xtpl","component/extension/content-render"],function(j,h){var l=h("menu"),i=h("./render-xtpl"),b=h("component/extension/content-render");return l.getDefaultRender().extend([b],{beforeCreateDom:function(a,b){j.mix(b,{placeholderEl:"#ks-filter-menu-placeholder-{id}",filterInputWrap:"#ks-filter-menu-input-wrap-{id}",filterInput:"#ks-filter-menu-input-{id}"})},getKeyEventTarget:function(){return this.control.get("filterInput")},_onSetPlaceholder:function(a){this.control.get("placeholderEl").html(a)}},
{ATTRS:{contentTpl:{value:i}},HTML_PARSER:{placeholderEl:function(a){return a.one("."+this.getBaseCssClass("placeholder"))},filterInputWrap:function(a){return a.one("."+this.getBaseCssClass("input-wrap"))},filterInput:function(a){return a.one("."+this.getBaseCssClass("input"))}}})});
KISSY.add("filter-menu",["menu","filter-menu/render"],function(j,h){var l=h("menu"),i=h("filter-menu/render");return l.extend({bindUI:function(){this.get("filterInput").on("valuechange",this.handleFilterEvent,this)},handleMouseEnterInternal:function(b){this.callSuper(b);this.view.getKeyEventTarget()[0].select()},handleFilterEvent:function(){var b;b=this.get("filterInput");var a=this.get("highlightedItem");this.set("filterStr",b.val());b=b.val();this.get("allowMultiple")&&(b=b.replace(/^.+,/,""));
if(!b&&a)a.set("highlighted",!1);else if(b&&(!a||!a.get("visible")))(a=this._getNextEnabledHighlighted(0,1))&&a.set("highlighted",!0)},_onSetFilterStr:function(b){this.filterItems(b)},filterItems:function(b){var a=this.get("prefixCls"),e=this.get("placeholderEl"),g=this.get("filterInput");e[b?"hide":"show"]();if(this.get("allowMultiple")){var e=[],f;f=b.match(/(.+)[,\uff0c]\s*([^\uff0c,]*)/);var c=[];f&&(c=f[1].split(/[,\uff0c]/));/[,\uff0c]$/.test(b)?(e=[],f&&(e=c,f=c[c.length-1],(c=(c=this.get("highlightedItem"))&&
c.get("content"))&&-1<c.indexOf(f)&&f&&(e[e.length-1]=c),g.val(e.join(",")+",")),b=""):(f&&(b=f[2]||""),e=c);this.get("enteredItems").length!==e.length&&this.set("enteredItems",e)}var g=this.get("children"),d=b&&RegExp(j.escapeRegExp(b),"ig");j.each(g,function(c){var e=c.get("content");b?-1<e.indexOf(b)?(c.set("visible",!0),c.get("el").html(e.replace(d,function(b){return'<span class="'+a+'menuitem-hit">'+b+"</span>"}))):c.set("visible",!1):(c.get("el").html(e),c.set("visible",!0))})},reset:function(){this.set("filterStr",
"");this.set("enteredItems",[]);this.get("filterInput").val("")}},{ATTRS:{allowTextSelection:{value:!0},placeholder:{view:1},filterStr:{},enteredItems:{value:[]},allowMultiple:{value:!1},xrender:{value:i}},xclass:"filter-menu"})});

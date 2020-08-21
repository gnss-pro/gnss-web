//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
function eXcell_stree(a){if(a){this.cell=a;this.grid=this.cell.parentNode.grid;if(!this.grid._sub_trees)return;this._sub=this.grid._sub_trees[a._cellIndex];if(!this._sub)return;this._sub=this._sub[0]}this.getValue=function(){return this.cell._val};this.setValue=function(b){this.cell._val=b;b=this._sub.getItemText(this.cell._val);this.setCValue(b||"&nbsp;",b)};this.edit=function(){this._sub.parentObject.style.display="block";var b=this.grid.getPosition(this.cell);this._sub.parentObject.style.top=b[1]+
"px";this._sub.parentObject.style.left=b[0]+"px";this._sub.parentObject.style.position="absolute";var a=this.grid.editStop;this.grid.editStop=function(){};this.grid.editStop=a};this.detach=function(){this._sub.parentObject.style.display="none";if(this.grid._sub_id!=null){var a=this.cell._val;this.setValue(this._sub.getSelectedItemId());this.grid._sub_id=null;return this.cell._val!=a}}}eXcell_stree.prototype=new eXcell;
dhtmlXGridObject.prototype.setSubTree=function(a,b){if(!this._sub_trees)this._sub_trees=[];this._sub_trees[b]=[a];a.parentObject.style.display="none";var c=this;a.parentObject.onclick=function(a){(a||window.event).cancelBubble=!0;return!1};a.ev_onDblClick=null;a.attachEvent("onDblClick",function(a){c._sub_id=a;c.editStop();return!0});a._chRRS=!0};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
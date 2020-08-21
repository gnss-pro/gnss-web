//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
function eXcell_context(a){if(a){this.cell=a;this.grid=this.cell.parentNode.grid;if(!this.grid._sub_context)return;this._sub=this.grid._sub_context[a._cellIndex];if(!this._sub)return;this._sindex=this._sub[1];this._sub=this._sub[0]}this.getValue=function(){return _isIE?this.cell.innerText:this.cell.textContent};this.setValue=function(b){this.cell._val=b;var a=this._sub.itemPull[this._sub.idPrefix+this.cell._val],b=a?a.title:b;this.setCValue(b||"&nbsp;",b)};this.edit=function(){var b=this.grid.getPosition(this.cell);
this._sub.showContextMenu(b[0]+this.cell.offsetWidth,b[1]);var a=this.grid.editStop;this.grid.editStop=function(){};this.grid.editStop=a};this.detach=function(){if(this.grid._sub_id!=null){var a=this.cell._val;this.setValue(this.grid._sub_id);this.grid._sub_id=null;return this.cell._val!=a}this._sub.hideContextMenu()}}eXcell_context.prototype=new eXcell;
dhtmlXGridObject.prototype.setSubContext=function(a,b,d){var c=this;a.attachEvent("onClick",function(b){c._sub_id=b;c.editStop();a.hideContextMenu();return!0});if(!this._sub_context)this._sub_context=[];this._sub_context[b]=[a,d];a.hideContextMenu()};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
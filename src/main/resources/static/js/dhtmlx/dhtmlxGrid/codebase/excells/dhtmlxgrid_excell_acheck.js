//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
function eXcell_acheck(a){try{this.cell=a,this.grid=this.cell.parentNode.grid,this.cell.obj=this}catch(b){}this.changeState=function(){if(this.grid.isEditable&&!this.cell.parentNode._locked&&!this.isDisabled())this.grid.callEvent("onEditCell",[0,this.cell.parentNode.idd,this.cell._cellIndex])!=!1?(this.val=this.getValue(),this.val=="1"?this.setValue("<checkbox state='false'>"):this.setValue("<checkbox state='true'>"),this.cell.wasChanged=!0,this.grid.callEvent("onEditCell",[1,this.cell.parentNode.idd,
this.cell._cellIndex]),this.grid.callEvent("onCheckbox",[this.cell.parentNode.idd,this.cell._cellIndex,this.val!="1"])):this.editor=null};this.getValue=function(){try{return this.cell.chstate.toString()}catch(a){return null}};this.isCheckbox=function(){return!0};this.isChecked=function(){return this.getValue()=="1"?!0:!1};this.setChecked=function(a){this.setValue(a.toString())};this.detach=function(){return this.val!=this.getValue()};this.drawCurrentState=function(){return this.cell.chstate==1?"<div  onclick='(new eXcell_acheck(this.parentNode)).changeState(); (arguments[0]||event).cancelBubble=true;'  style='cursor:pointer; font-weight:bold; text-align:center; '><img height='13px' src='"+
this.grid.imgURL+"green.gif'>&nbsp;Yes</div>":"<div  onclick='(new eXcell_acheck(this.parentNode)).changeState(); (arguments[0]||event).cancelBubble=true;' style='cursor:pointer;  text-align:center; '><img height='13px' src='"+this.grid.imgURL+"red.gif'>&nbsp;No</div>"}}eXcell_acheck.prototype=new eXcell;
eXcell_acheck.prototype.setValue=function(a){a=(a||"").toString();a.indexOf("1")!=-1||a.indexOf("true")!=-1?(a="1",this.cell.chstate="1"):(a="0",this.cell.chstate="0");var b=this;this.setCValue(this.drawCurrentState(),this.cell.chstate)};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
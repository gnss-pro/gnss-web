//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
document.write("<script src='"+_js_prefix+"calendar/YAHOO.js'><\/script>");document.write("<script src='"+_js_prefix+"calendar/event.js'><\/script>");document.write("<script src='"+_js_prefix+"calendar/calendar.js'><\/script>");document.write("<script src='"+_js_prefix+"calendar/calendar_init.js'><\/script>");document.write("<link rel='stylesheet' type='text/css' href='"+_js_prefix+"calendar/calendar.css'></link>");
function eXcell_calendar(b){try{this.cell=b,this.grid=this.cell.parentNode.grid}catch(d){}this.edit=function(){window._grid_calendar||_grid_calendar_init();var a=this.grid.getPosition(this.cell),b=this._date2str2(this.cell.val||new Date);window._grid_calendar.render(a[0],a[1]+this.cell.offsetHeight,this,b);var c=function(a){(a||event).cancelBubble=!0};dhtmlxEvent(window._grid_calendar.table.parentNode.parentNode,"click",c);this.cell._cediton=!0;this.val=this.cell.val};this.getValue=function(){return this.cell.val?
this._date2str2(this.cell.val):this.cell.innerHTML.toString()._dhx_trim()};this.getContent=function(){return this.cell.innerHTML.toString()._dhx_trim()};this.detach=function(){var a=window._grid_calendar.getSelectedDates()[0];window._grid_calendar.hide();if(!this._skip_detach&&a.getFullYear())return this.cell.val=new Date(a.valueOf()),this.setCValue(this._date2str(a),a),!this.val?!0:a.valueOf()!=this.val.valueOf()};this._2dg=function(a){return a.toString().length==1?"0"+a.toString():a};this._date2str2=
function(a){return"m/d/y".replace("m",this._2dg(a.getMonth()*1+1)).replace("d",this._2dg(a.getDate())).replace("y",this._2dg(a.getFullYear()*1))};this._date2str=function(a){return(this.grid._dtmask||"m/d/y").replace("m",this._2dg(a.getMonth()*1+1)).replace("d",this._2dg(a.getDate())).replace("y",this._2dg(a.getFullYear()*1))}}eXcell_calendar.prototype=new eXcell;
eXcell_calendar.prototype.setValue=function(b){if(!b||b.toString()._dhx_trim()=="")b="";this.cell.val=new Date(b.toString());this.cell.val=="NaN"||this.cell.val=="Invalid Date"?(this.cell.val="",this.setCValue("&nbsp;",0)):this.setCValue(this._date2str(this.cell.val),this.cell.val)};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
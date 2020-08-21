//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
function eXcell_3but(a){this.cell=a;this.grid=this.cell.parentNode.grid;this.edit=function(){};this.isDisabled=function(){return!0};this.detach=function(){};this.setValue=function(a){this.cell.val=a;this.cell.innerHTML="<input type='button' value='1'/><input type='button' value='2'/><input type='button' value='3'/>";this.cell.childNodes[0].onclick=function(){a3but_f1(this.parentNode.parentNode.idd,this.parentNode._cellIndex)};this.cell.childNodes[1].onclick=function(){a3but_f2(this.parentNode.parentNode.idd,
this.parentNode._cellIndex)};this.cell.childNodes[2].onclick=function(){a3but_f3(this.parentNode.parentNode.idd,this.parentNode._cellIndex)}};this.getValue=function(){return this.cell.val||""}}eXcell_3but.prototype=new eXcell;

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
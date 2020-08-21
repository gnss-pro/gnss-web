//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
function eXcell_cntr(b){this.cell=b;this.grid=this.cell.parentNode.grid;if(!this.grid._ex_cntr_ready&&!this._realfake)this.grid._ex_cntr_ready=!0,this.grid._h2&&this.grid.attachEvent("onOpenEn",function(){this.resetCounter(b._cellIndex)}),this.grid.attachEvent("onBeforeSorting",function(){var a=this;window.setTimeout(function(){a.resetCounter&&(a._fake&&!a._realfake&&b._cellIndex<a._fake._cCount?a._fake.resetCounter(b._cellIndex):a.resetCounter(b._cellIndex))},1);return!0});this.edit=function(){};
this.getValue=function(){return this.cell.innerHTML};this.setValue=function(){this.cell.style.paddingRight="2px";var a=this.cell;window.setTimeout(function(){if(a.parentNode){var b=a.parentNode.rowIndex;if(a.parentNode.grid.currentPage||b<0||a.parentNode.grid._srnd)b=a.parentNode.grid.rowsBuffer._dhx_find(a.parentNode)+1;if(!(b<=0))a.innerHTML=b,a.parentNode.grid._fake&&a._cellIndex<a.parentNode.grid._fake._cCount&&a.parentNode.grid._fake.rowsAr[a.parentNode.idd]&&a.parentNode.grid._fake.cells(a.parentNode.idd,
a._cellIndex).setCValue(b),a=null}},100)}}dhtmlXGridObject.prototype.resetCounter=function(b){this._fake&&!this._realfake&&b<this._fake._cCount&&this._fake.resetCounter(b,this.currentPage);var a=b||0;this.currentPage&&(a=(this.currentPage-1)*this.rowsBufferOutSize);for(a=0;a<this.rowsBuffer.length;a++)if(this.rowsBuffer[a]&&this.rowsBuffer[a].tagName=="TR"&&this.rowsAr[this.rowsBuffer[a].idd])this.rowsAr[this.rowsBuffer[a].idd].childNodes[b].innerHTML=a+1};eXcell_cntr.prototype=new eXcell;

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
dhtmlXGridObject.prototype.enableSmartRendering=function(b,a,d){arguments.length>2&&(a&&!this.rowsBuffer[a-1]&&(this.rowsBuffer[a-1]=0),a=d);this._srnd=convertStringToBoolean(b);this._srdh=this._srdh||20;this._dpref=a||0};dhtmlXGridObject.prototype.enablePreRendering=function(b){this._srnd_pr=parseInt(b||50)};
dhtmlXGridObject.prototype.forceFullLoading=function(b,a){for(var d=0;d<this.rowsBuffer.length;d++)if(!this.rowsBuffer[d]){var c=b||this.rowsBuffer.length-d;if(this.callEvent("onDynXLS",[d,c])){var e=this;this.load(this.xmlFileUrl+getUrlSymbol(this.xmlFileUrl)+"posStart="+d+"&count="+c,function(){window.setTimeout(function(){e.forceFullLoading(b,a)},100)},this._data_type)}return}a&&a.call(this)};dhtmlXGridObject.prototype.setAwaitedRowHeight=function(b){this._srdh=parseInt(b)};
dhtmlXGridObject.prototype._get_view_size=function(){return Math.floor(parseInt(this.entBox.offsetHeight)/this._srdh)+2};
dhtmlXGridObject.prototype._add_filler=function(b,a,d,c){if(!a)return null;var e="__filler__",f=this._prepareRow(e);f.firstChild.style.width="1px";for(var g=1;g<f.childNodes.length;g++)f.childNodes[g].style.display="none";f.firstChild.style.height=a*this._srdh+"px";(d=d||this.rowsCol[b])&&d.nextSibling?d.parentNode.insertBefore(f,d.nextSibling):_isKHTML?this.obj.appendChild(f):this.obj.rows[0].parentNode.appendChild(f);this.callEvent("onAddFiller",[b,a,f,d,c]);return[b,a,f]};
dhtmlXGridObject.prototype._update_srnd_view=function(){var b=Math.floor(this.objBox.scrollTop/this._srdh),a=b+this._get_view_size();if(this.multiLine){for(var d=this.objBox.scrollTop,b=0;d>0;)d-=this.rowsCol[b]?this.rowsCol[b].offsetHeight:this._srdh,b++;a=b+this._get_view_size();b>0&&b--}a+=this._srnd_pr||0;if(a>this.rowsBuffer.length)a=this.rowsBuffer.length;for(var c=b;c<a;c++)if(!this.rowsCol[c]){var e=this._add_from_buffer(c);if(e==-1){if(this.xmlFileUrl){if(this._dpref&&this.rowsBuffer[a-1]){var f=
this._dpref?this._dpref:a-c,g=Math.max(0,a-this._dpref);this._current_load=[g,a-g]}else this._current_load=[c,this._dpref?this._dpref:a-c];this.callEvent("onDynXLS",this._current_load)&&this.load(this.xmlFileUrl+getUrlSymbol(this.xmlFileUrl)+"posStart="+this._current_load[0]+"&count="+this._current_load[1],this._data_type)}return}else if(this._tgle&&(this._updateLine(this._h2.get[this.rowsBuffer[c].idd],this.rowsBuffer[c]),this._updateParentLine(this._h2.get[this.rowsBuffer[c].idd],this.rowsBuffer[c])),
c&&c==(this._realfake?this._fake:this)._r_select)this.selectCell(c,this.cell?this.cell._cellIndex:0,!0)}if(this._fake&&!this._realfake&&this.multiLine)this._fake.objBox.scrollTop=this.objBox.scrollTop};
dhtmlXGridObject.prototype._add_from_buffer=function(b){var a=this.render_row(b);if(a==-1)return-1;if(a._attrs.selected||a._attrs.select)this.selectRow(a,!1,!0),a._attrs.selected=a._attrs.select=null;if(this._cssSP){if(this._h2){var d=this._h2.get[a.idd];a.className+=" "+(d.level%2?this._cssUnEven+" "+this._cssUnEven:this._cssEven+" "+this._cssEven)+"_"+d.level+(this.rowsAr[d.id]._css||"")}}else if(this._cssEven&&b%2==0)a.className=this._cssEven+(a.className.indexOf("rowselected")!=-1?" rowselected ":
" ")+(a._css||"");else if(this._cssUnEven&&b%2==1)a.className=this._cssUnEven+(a.className.indexOf("rowselected")!=-1?" rowselected ":" ")+(a._css||"");for(var c=0;c<this._fillers.length;c++){var e=this._fillers[c];if(e&&e[0]<=b&&e[0]+e[1]>b){var f=b-e[0];f==0?(this._insert_before(b,a,e[2]),this._update_fillers(c,-1,1)):f==e[1]-1?(this._insert_after(b,a,e[2]),this._update_fillers(c,-1,0)):(this._fillers.push(this._add_filler(b+1,e[1]-f-1,e[2],1)),this._insert_after(b,a,e[2]),this._update_fillers(c,
-e[1]+f,0));break}}};dhtmlXGridObject.prototype._update_fillers=function(b,a,d){var c=this._fillers[b];c[1]+=a;c[0]+=d;c[1]?(c[2].firstChild.style.height=parseFloat(c[2].firstChild.style.height)+a*this._srdh+"px",this.callEvent("onUpdateFiller",[c[2]])):(this.callEvent("onRemoveFiller",[c[2]]),c[2].parentNode.removeChild(c[2]),this._fillers.splice(b,1))};
dhtmlXGridObject.prototype._insert_before=function(b,a,d){d.parentNode.insertBefore(a,d);this.rowsCol[b]=a;this.callEvent("onRowInserted",[a,null,d,"before"])};dhtmlXGridObject.prototype._insert_after=function(b,a,d){d.nextSibling?d.parentNode.insertBefore(a,d.nextSibling):d.parentNode.appendChild(a);this.rowsCol[b]=a;this.callEvent("onRowInserted",[a,null,d,"after"])};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
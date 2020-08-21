//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
dhtmlxValidation=function(){};
dhtmlxValidation.prototype={trackInput:function(a,b,c,d){dhtmlxEvent(a,"keyup",function(){if(dhtmlxValidation._timer)window.clearTimeout(dhtmlxValidation._timer),dhtmlxValidation._timer=null;dhtmlxValidation._timer=window.setTimeout(function(){if(dhtmlxValidation.checkInput(a,b))a.className=a.className.replace(/[ ]*dhtmlx_live_validation_error/g,""),d&&d(a,a.value,b);else if(!c||c(a,a.value,b))a.className+=" dhtmlx_live_validation_error"},250)})},checkInput:function(a,b){return this.checkValue(a.value,
b)},checkValue:function(a,b){typeof b=="string"&&(b=b.split(","));for(var c=!0,d=0;d<b.length;d++)this["is"+b[d]]?c=c&&this["is"+b[d]](a):alert("Incorrect validation rule: "+b[d]);return c},isEmpty:function(a){return a==""},isNotEmpty:function(a){return!a==""},isValidBoolean:function(a){return!!a.toString().match(/^(0|1|true|false)$/)},isValidEmail:function(a){return!!a.toString().match(/(^[a-z0-9]([0-9a-z\-_\.]*)@([0-9a-z_\-\.]*)([.][a-z]{3})$)|(^[a-z]([0-9a-z_\.\-]*)@([0-9a-z_\-\.]*)(\.[a-z]{2,4})$)/i)},
isValidInteger:function(a){return!!a.toString().match(/(^-?\d+$)/)},isValidNumeric:function(a){return!!a.toString().match(/(^-?\d\d*[\.|,]\d*$)|(^-?\d\d*$)|(^-?[\.|,]\d\d*$)/)},isValidAplhaNumeric:function(a){return!!a.toString().match(/^[_\-a-z0-9]+$/gi)},isValidDatetime:function(a){var b=a.toString().match(/^(\d{4})-(\d{2})-(\d{2})\s(\d{2}):(\d{2}):(\d{2})$/);return b&&!!(b[1]<=9999&&b[2]<=12&&b[3]<=31&&b[4]<=59&&b[5]<=59&&b[6]<=59)||!1},isValidDate:function(a){var b=a.toString().match(/^(\d{4})-(\d{2})-(\d{2})$/);
return b&&!!(b[1]<=9999&&b[2]<=12&&b[3]<=31)||!1},isValidTime:function(a){var b=a.toString().match(/^(\d{1,2}):(\d{1,2}):(\d{1,2})$/);return b&&!!(b[1]<=24&&b[2]<=59&&b[3]<=59)||!1},isValidIPv4:function(a){var b=a.toString().match(/^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/);return b&&!!(b[1]<=255&&b[2]<=255&&b[3]<=255&&b[4]<=255)||!1},isValidCurrency:function(a){return a.toString().match(/^\$?\s?\d+?[\.,\,]?\d+?\s?\$?$/)&&!0||!1},isValidSSN:function(a){return a.toString().match(/^\d{3}\-?\d{2}\-?\d{4}$/)&&
!0||!1},isValidSIN:function(a){return a.toString().match(/^\d{9}$/)&&!0||!1}};dhtmlxValidation=new dhtmlxValidation;dhtmlXGridObject.prototype.enableValidation=function(a,b){this._validators=(a=convertStringToBoolean(a))?{data:[]}:!1;if(arguments.length>1)this._validators._live=b;if(!this._validators._event)this._validators._event=this.attachEvent("onEditCell",this.validationEvent)};
dhtmlXGridObject.prototype.setColValidators=function(a){this._validators||this.enableValidation(!0);typeof a=="string"&&(a=a.split(this.delim));this._validators.data=a};
dhtmlXGridObject.prototype.validationEvent=function(a,b,c,d){var f=this._validators;if(!f)return!0;var g=f.data[c]||this.cells(b,c).getAttribute("validate")||"";if(a==1&&g){var e=this.editor||(this._fake||{}).editor;if(!e)return!0;e.cell.className=e.cell.className.replace(/[ ]*dhtmlx_validation_error/g,"");if(f._live){var h=this;dhtmlxValidation.trackInput(e.getInput(),g,function(a,d,e){return h.callEvent("onLiveValidationError",[b,c,d,a,e])},function(a,d,e){return h.callEvent("onLiveValidationCorrect",
[b,c,d,a,e])})}}a==2&&this.validateCell(b,c,g,d);return!0};
dhtmlXGridObject.prototype.validateCell=function(a,b,c,d){c=c||this._validators.data[b]||this.cells(a,b).getAttribute("validate");d=d||this.cells(a,b).getValue();if(c){var f=this.cells(a,b).cell,g=!0;typeof c=="string"&&(c=c.split(this.delim));for(var e=0;e<c.length;e++)dhtmlxValidation.checkValue(d,c[e])||(this.callEvent("onValidationError",[a,b,d,c[e]])&&(f.className+=" dhtmlx_validation_error"),g=!1);if(g)this.callEvent("onValidationCorrect",[a,b,d,c]),f.className=f.className.replace(/[ ]*dhtmlx_validation_error/g,
"");return g}};

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
You allowed to use this component or parts of it under GPL terms
To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
*/
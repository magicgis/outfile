/* jshint forin:true, noarg:true, noempty:true, eqeqeq:true, boss:true, undef:true, curly:true, browser:true, jquery:true */
/*
 * jQuery MultiSelect UI Widget 1.14pre
 * Copyright (c) 2012 Eric Hynds
 *
 * http://www.erichynds.com/jquery/jquery-ui-multiselect-widget/
 *
 * Depends:
 *   - jQuery 1.4.2+
 *   - jQuery UI 1.8 widget factory
 *
 * Optional:
 *   - jQuery UI effects
 *   - jQuery UI position utility
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 */
(function($, undefined) {
  var multiselectID = 0;
  var $doc = $(document);
  var multiValues = "";
  var selectIdList="";
  var initedList="";
  var tree="";
  $.widget("ech.multiselect", {

    // default options
    options: {
      header: true,
      height: 175,
      minWidth: 175,
      classes: '',
      checkAllText: '全选',
      uncheckAllText: '全不选',
      noneSelectedText: '===请选择===',
      selectedText: '# 项已选中',
	  selectedList: 0,//页面选项框里最多显示选中项条数，超过后显示 *selected（以选中*条选项）
      show: null,
      hide: null,
      autoOpen: false,
      multiple: true,
      position: {},
      appendTo: "body",
	  url:	'',
	  data:	null,//用于自动创建选项的json对象
	  executeMessage:	'加载中...',
	  successMessage:	'===请选择===',//Finished loading
	  errorMessage:		'加载出错',//Error loading
	  type: '',//''为普通单选或多选，'tree'为下拉树选项，（待添加其他类型选项）
	  setting:{},//zTree的配置参数集合
	  parentCheckable:true,//父节点是否可已勾选（不决定是否选如文本框）
	  isAddParentToLabel:true, //父节点可勾选时，是否要将父节点一起添加到文本框中
	  async:false,//是否开启异步加载，默认不开启
	  autoParam:[]//进行异步加载时需要自动提交父节点属性的参数，格式为：["id", "name"]
    },
    
    _create: function() {
		  //alert(this.options.url);
      var el = this.element.hide();
	
      var o = this.options;
	  if (o.setting&&o.setting.check&&o.setting.check.enable==false){
				o.header="";
	  }
      this.speed = $.fx.speeds._default; // default speed for effects
      this._isOpen = false; // assume no

      // create a unique namespace for events that the widget
      // factory cannot unbind automatically. Use eventNamespace if on
      // jQuery UI 1.9+, and otherwise fallback to a custom string.
      this._namespaceID = this.eventNamespace || ('multiselect' + multiselectID);
      var buttonlabelHeight=15;
      if ( $.browser.version=="6.0"){
    	  buttonlabelHeight=18;
      }else if( $.browser.version=="8.0"){
    	  buttonlabelHeight=22;
      }else if( $.browser.version=="9.0"){
    	  buttonlabelHeight=22;
      }else if($.browser.version=="7.0"){
    	  buttonlabelHeight=22;
      }
      var button = (this.button = $('<button type="button" ><span class="ui-icon ui-icon-triangle-1-s"></span></button>'))
        .addClass('ui-multiselect ui-widget ui-state-default ui-corner-all')
        .addClass(o.classes)
        .attr({ 'title':el.attr('title'), 'aria-haspopup':true, 'tabIndex':el.attr('tabIndex') })
        .insertAfter(el),
       
        buttonlabel = (this.buttonlabel = $('<div style=\"height:'+buttonlabelHeight+'px;overflow:hidden\"/>'))
          .html(o.noneSelectedText)
          .appendTo(button),
        menu = (this.menu = $('<div />'))
          .addClass('ui-multiselect-menu ui-widget ui-widget-content ui-corner-all')
          .addClass(o.classes)
          .appendTo($(o.appendTo)),

        header = (this.header = $('<div />'))
          .addClass('ui-widget-header ui-corner-all ui-multiselect-header ui-helper-clearfix')
          .appendTo(menu),

        headerLinkContainer = (this.headerLinkContainer = $('<ul />'))
          .addClass('ui-helper-reset')
          .html(function() {
            if(o.header === true) {
				if('tree'==o.type){
					if(o.setting.check&&"radio"==o.setting.check.chkStyle){
						return '';
					}
					return '<li><a class="ui-multiselect-none" href="#"><span class="ui-icon ui-icon-closethick"></span><span>' + o.uncheckAllText + '</span></a></li>';
				}else{
					return '<li><a class="ui-multiselect-all" href="#"><span class="ui-icon ui-icon-check"></span><span>' + o.checkAllText + '</span></a></li><li><a class="ui-multiselect-none" href="#"><span class="ui-icon ui-icon-closethick"></span><span>' + o.uncheckAllText + '</span></a></li>';
				}
            } else if(typeof o.header === "string") {
              return '<li>' + o.header + '</li>';
            } else {
              return '';
            }
          })
          .append('<li class="ui-multiselect-close"><a href="#" class="ui-multiselect-close"><span class="ui-icon ui-icon-circle-close"></span></a></li>')
          .appendTo(header),

        checkboxContainer = (this.checkboxContainer = $('<ul />'))
          .addClass('ui-multiselect-checkboxes ui-helper-reset')
          .appendTo(menu);

        // perform event bindings
        this._bindEvents();

        // build menu
        this.refresh(true);

        // some addl. logic for single selects
        if(!o.multiple) {
          menu.addClass('ui-multiselect-single');
        }

        // bump unique ID
        multiselectID++;
    },
  
    _init: function() {
      if(this.options.header === false) {
        this.header.hide();
      }
      if(!this.options.multiple) {
        this.headerLinkContainer.find('.ui-multiselect-all, .ui-multiselect-none').hide();
      }
      if(this.options.autoOpen) {
        this.open();
      }
      if(this.element.is(':disabled')) {
        this.disable();
      }
	  this.element.attr("url",this.options.url).attr("multipleFlag",this.options.multiple);
      
    },
    refresh: function(init) {
      var el = this.element;
      var o = this.options;
      var menu = this.menu;
      var checkboxContainer = this.checkboxContainer;
      var optgroups = [];
      var html = "";
      var id = el.attr('id') || multiselectID++; // unique ID for the label & option tags


      // build items
      el.find('option').each(function(i) {
        var parent = this.parentNode;
        var description = this.innerHTML;
        var title = this.title;
        var value = this.value;
        var inputID = 'ui-multiselect-' + (this.id || id + '-option-' + i);
        var isDisabled = this.disabled;
        var isSelected = this.selected;
        var labelClasses = [ 'ui-corner-all' ];
        var liClasses = (isDisabled ? 'ui-multiselect-disabled ' : ' ') + this.className;
        var optLabel;

        // is this an optgroup?
        if(parent.tagName === 'OPTGROUP') {
          optLabel = parent.getAttribute('label');

          // has this optgroup been added already?
          if($.inArray(optLabel, optgroups) === -1) {
            html += '<li class="ui-multiselect-optgroup-label ' + parent.className + '" ><a href="#">' + optLabel + '</a></li>';
            optgroups.push(optLabel);
          }
        }

        if(isDisabled) {
          labelClasses.push('ui-state-disabled');
        }

        // browsers automatically select the first option
        // by default with single selects
        if(isSelected && !o.multiple) {
          labelClasses.push('ui-state-active');
        }


        var style_lable="";
        if ( i%2==0 ){
        	style_lable="background:#efefef;";
        }else{
        	style_lable="background:#ffffff;";
        }
       
        html += '<li class="' + liClasses + '"   style="'+style_lable+'" >';

        // create the label
        html += '<label for="' + inputID + '" title="' + title + '" class="' + labelClasses.join(' ') + '"  >';
        html += '<input  id="' + inputID + '" name="multiselect_' + id + '" type="' + (o.multiple ? "checkbox" : "radio") + '" value="' + value + '" title="' + title + '"';

        // pre-selected?
        if(isSelected) {
          html += ' checked="checked"';
          html += ' aria-selected="true"';
        }

        // disabled?
        if(isDisabled) {
          html += ' disabled="disabled"';
          html += ' aria-disabled="true"';
        }

        // add the title and close everything off
        html += ' /><span>' + description + '</span></label></li>';
      });

      // insert into the DOM
      checkboxContainer.html(html);
	  
	  
	  
	  if("tree"==o.type){
		  this.createTree();
	  }
      // cache some moar useful elements
      this.labels = menu.find('label');
      this.inputs = this.labels.children('input');

      // set widths
      this._setButtonWidth();
      this._setMenuWidth();
      // remember default value
      this.button[0].defaultValue = this.update();
      // broadcast refresh event; useful for widgets
	
      var selid=this.element.attr("id");
      var isOpen=false;
      var selids=selectIdList.split(",");
	  $.each(selids,function(){
		if(selid==this){
			isOpen=true;
			return;
		}
	  });
	  if("tree"==o.type&&!isOpen){
		    var defValue=$("#def_select_ztree"+selid).val();
			var text=$("#def_select_ztree"+selid).attr("text");
			if(defValue&&undefined!=defValue){
				this._setButtonValue(text, defValue);
			}
	  }
      if(!init) {
        this._trigger('refresh');
	  }
    },
    MyValues:function(){
        return multiValues;
    },
    setValues:function(values,fn){
    	if(values){
    		multiValues = values;
    		if(!this.options.multiple){
    			values = [values];
    		}else{
    			values = values.split(",");
    		}
    		var $inputs = this.inputs;
			var text = $.map(values,function(v){
				var t="";
				$.each($inputs,function(){
					if(this.value==v){
						t=$(this).attr("checked",true).next("span").html();
						return ;
					}
				});
				return t;
				//return $("[value="+v+"]",$inputs).attr("checked",true).next("span").html();//该句选择器未能正确选择input的checkbox标签，操作失败同时取值失败
			}).join(",");
			this._setButtonValue(text,multiValues);
    		//this._setButtonValue
    		if(fn && $.isFunction(fn)){
        		fn.apply(this,[values]);
        	};
    	}
    	
    },
    // updates the button text. call refresh() to rebuild
    update: function() {
	  var value="";
	  multiValues="";
	  var o = this.options;
	  if("tree"==o.type){
	  
	  
		  var treeID=this.menu.find("ul[id]").attr("id");
		  var zTree=$.fn.zTree.getZTreeObj(treeID);
		  var checked;
		  if(zTree){
			  if(o.setting.check.enable){
				  checked=	zTree.getCheckedNodes(true);
			  }else{
				  checked=zTree.getSelectedNodes();
			  }
			  if(checked.length){
				$.each(checked,function(){
					if(o.isAddParentToLabel){//是否将选中的父节点回填到文本框中
						value+=this.name+",";
						multiValues+=this.value+",";
					}else{
						if(!this.isParent||!this.children&&this.children!=undefined){//选中的不是父节点才回填到文本框中
							value+=this.name+",";
							multiValues+=this.value+",";
						}
					}
				});
				value=""==value?o.noneSelectedText:value.substring(0,value.length-1);
				multiValues=multiValues.substring(0,multiValues.length-1);
			  }else{
				value=o.noneSelectedText;
			  }
		  }else{
			value=o.noneSelectedText;
		  }
		 
	  }else{
		  var $inputs = this.inputs;
		  var $checked = $inputs.filter(':checked');
		  var numChecked = $checked.length;
		  

		  if(numChecked === 0) {
			value = o.noneSelectedText;
		  } else {
			if($.isFunction(o.selectedText)) {
			  value = o.selectedText.call(this, numChecked, $inputs.length, $checked.get());
			}
			/*
			else if(/\d/.test(o.selectedList) && o.selectedList > 0 && numChecked <= o.selectedList) {
			  value = $checked.map(function() { return $(this).next().html(); }).get().join(', ');
			}
			*/
			else {
			  //value = o.selectedText.replace('#', numChecked).replace('#', $inputs.length);//原来的显示*selected
			  value = $checked.map(function() { return $(this).next().html(); }).get().join(',');
			}
			multiValues = $checked.map(function () { return $(this).val(); }).get().join(',');
		  }
	  }
//	  initedList
	  var isFirst=true;//初始化set值标志
	  var selid=this.element.attr("id");
	  var num=0;
	  var selids=initedList.split(",");
	  $.each(selids,function(){
		if(selid==this){
			num=num+1;
			return;
		}
	  });
	  if(num>2){
		  isFirst=false;
	  }
	  if(isFirst){
		  initedList+=selid+",";
	  }
      this._setButtonValue(value,multiValues,isFirst);

      return value;
    },

    // this exists as a separate method so that the developer 
    // can easily override it.
    _setButtonValue: function(key,value,init) {
    	key=key.replace("&gt;",">").replace("&lt;","<");
    	init=init==true?true:false;
      this.buttonlabel.html(key);
	  if(("tree"==this.options.type||this.options.multiple)&&(!init||(value&&'undefined'!=value&&undefined!=value))){
		  this.element.html("");
		  this.element.append("<option value='"+value+"' selected=selected>"+key+"</option>");
	  }
		  this.button.attr("title",key);
    },

    // binds events
    _bindEvents: function() {
      var self = this;
      var button = this.button;

      function clickHandler() {
        self[ self._isOpen ? 'close' : 'open' ]();
        return false;
      }

      // webkit doesn't like it when you click on the span :(
      button
        .find('span')
        .bind('click.multiselect', clickHandler);

      // button events
      button.bind({
        click: clickHandler,
        keypress: function(e) {
          switch(e.which) {
            case 27: // esc
              case 38: // up
              case 37: // left
              self.close();
            break;
            case 39: // right
              case 40: // down
              self.open();
            break;
          }
        },
        mouseenter: function() {
          if(!button.hasClass('ui-state-disabled')) {
            $(this).addClass('ui-state-hover');
          }
        },
        mouseleave: function() {
          $(this).removeClass('ui-state-hover');
        },
        focus: function() {
          if(!button.hasClass('ui-state-disabled')) {
            $(this).addClass('ui-state-focus');
          }
        },
        blur: function() {
          $(this).removeClass('ui-state-focus');
        }
      });

      // header links
      this.header.delegate('a', 'click.multiselect', function(e) {
        // close link
        if($(this).hasClass('ui-multiselect-close')) {
          self.close();

          // check all / uncheck all
        } else {
          self[$(this).hasClass('ui-multiselect-all') ? 'checkAll' : 'uncheckAll']();
        }

        e.preventDefault();
      });

      // optgroup label toggle support
      this.menu.delegate('li.ui-multiselect-optgroup-label a', 'click.multiselect', function(e) {
        e.preventDefault();

        var $this = $(this);
        var $inputs = $this.parent().nextUntil('li.ui-multiselect-optgroup-label').find('input:visible:not(:disabled)');
        var nodes = $inputs.get();
        var label = $this.parent().text();

        // trigger event and bail if the return is false
        if(self._trigger('beforeoptgrouptoggle', e, { inputs:nodes, label:label }) === false) {
          return;
        }

        // toggle inputs
        self._toggleChecked(
          $inputs.filter(':checked').length !== $inputs.length,
          $inputs
        );

        self._trigger('optgrouptoggle', e, {
          inputs: nodes,
          label: label,
          checked: nodes[0].checked
        });
      })
      .delegate('label', 'mouseenter.multiselect', function() {
        if(!$(this).hasClass('ui-state-disabled')) {
          self.labels.removeClass('ui-state-hover');
          $(this).addClass('ui-state-hover').find('input').focus();
        }
      })
      .delegate('label', 'keydown.multiselect', function(e) {
        e.preventDefault();

        switch(e.which) {
          case 9: // tab
            case 27: // esc
            self.close();
          break;
          case 38: // up
            case 40: // down
            case 37: // left
            case 39: // right
            self._traverse(e.which, this);
          break;
          case 13: // enter
            $(this).find('input')[0].click();
          break;
        }
      })
      .delegate('input[type="checkbox"], input[type="radio"]', 'click.multiselect', function(e) {
        var $this = $(this);
        var val = this.value;
        var checked = this.checked;
        var tags = self.element.find('option');

        // bail if this input is disabled or the event is cancelled
        if(this.disabled || self._trigger('click', e, { value: val, text: this.title, checked: checked }) === false) {
          e.preventDefault();
          return;
        }

        // make sure the input has focus. otherwise, the esc key
        // won't close the menu after clicking an item.
        $this.focus();

        // toggle aria state
        $this.attr('aria-selected', checked);

        // change state on the original option tags
        tags.each(function() {
          if(this.value === val) {
            this.selected = checked;
          } else if(!self.options.multiple) {
            this.selected = false;
          }
        });

        // some additional single select-specific logic
        if(!self.options.multiple) {
          self.labels.removeClass('ui-state-active');
          $this.closest('label').toggleClass('ui-state-active', checked);

          // close menu
          self.close();
        }

        // fire change on the select box
        self.element.trigger("change");

        // setTimeout is to fix multiselect issue #14 and #47. caused by jQuery issue #3827
        // http://bugs.jquery.com/ticket/3827
        setTimeout($.proxy(self.update, self), 10);
      });

      // close each widget when clicking on any other element/anywhere else on the page
      $doc.bind('mousedown.' + this._namespaceID, function(event) {
        var target = event.target;

        if(self._isOpen
            && target !== self.button[0]
            && target !== self.menu[0]
            && !$.contains(self.menu[0], target)
            && !$.contains(self.button[0], target)
          ) {
          self.close();
        }
      });

      // deal with form resets.  the problem here is that buttons aren't
      // restored to their defaultValue prop on form reset, and the reset
      // handler fires before the form is actually reset.  delaying it a bit
      // gives the form inputs time to clear.
      $(this.element[0].form).bind('reset.multiselect', function() {
        setTimeout($.proxy(self.refresh, self), 10);
      });
    },

    // set button width
    _setButtonWidth: function() {
      var width = this.element.outerWidth();
      var o = this.options;

      if(/\d/.test(o.minWidth) && width < o.minWidth) {
        width = o.minWidth;
      }

      // set widths
      this.button.outerWidth(width);
      this.buttonlabel.width(width-35);
    },

    // set menu width
    _setMenuWidth: function() {
      var m = this.menu;
      m.outerWidth(this.button.outerWidth());
    },

    // move up or down within the menu
    _traverse: function(which, start) {
      var $start = $(start);
      var moveToLast = which === 38 || which === 37;

      // select the first li that isn't an optgroup label / disabled
      var $next = $start.parent()[moveToLast ? 'prevAll' : 'nextAll']('li:not(.ui-multiselect-disabled, .ui-multiselect-optgroup-label)').first();

      // if at the first/last element
      if(!$next.length) {
        var $container = this.menu.find('ul').last();

        // move to the first/last
        this.menu.find('label')[ moveToLast ? 'last' : 'first' ]().trigger('mouseover');

        // set scroll position
        $container.scrollTop(moveToLast ? $container.height() : 0);

      } else {
        $next.find('label').trigger('mouseover');
      }
    },

    // This is an internal function to toggle the checked property and
    // other related attributes of a checkbox.
    //
    // The context of this function should be a checkbox; do not proxy it.
    _toggleState: function(prop, flag) {
      return function() {
        if(!this.disabled) {
          this[ prop ] = flag;
        }
        if(flag) {
          this.setAttribute('aria-selected', true);
        } else {
          this.removeAttribute('aria-selected');
        }
      };
    },

    _toggleChecked: function(flag, group) {
      var $inputs = (group && group.length) ?  group : this.inputs;
      var self = this;

      // toggle state on inputs
      
      $inputs.each(
    		  //self._toggleState('checked', flag)
    	function(i){
		   if(i>=998&&flag){
			    if ( !window.message  ){
				    alert("选中记录数不能超过1000条，超过1000条记录只能选中前1000条记录！");
			    }else{
				    window.message.alert({msg:'选中记录数不能超过1000条，超过1000条记录只能选中前1000条记录！',type:'warn'}); 
			    }
			  return false;
		    }
	    	if(!this.disabled) {
	          this[ 'checked' ] = flag;
	        }
	        if(flag) {
	          this.setAttribute('aria-selected', true);
	        } else {
	          this.removeAttribute('aria-selected');
	        }
    	  
      }
      	
      );

      // give the first input focus
      $inputs.eq(0).focus();

      // update button text
      this.update();

      // gather an array of the values that actually changed
      var values = $inputs.map(function() {
        return this.value;
      }).get();

      // toggle state on original option tags
      this.element
        .find('option')
        .each(function() {
          if(!this.disabled && $.inArray(this.value, values) > -1) {
            self._toggleState('selected', flag).call(this);
          }
        });
		

      // trigger the change event on the select
      if($inputs.length) {
        this.element.trigger("change");
      }
    },

    _toggleDisabled: function(flag) {
      this.button.attr({ 'disabled':flag, 'aria-disabled':flag })[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');

      var inputs = this.menu.find('input');
      var key = "ech-multiselect-disabled";

      if(flag) {
        // remember which elements this widget disabled (not pre-disabled)
        // elements, so that they can be restored if the widget is re-enabled.
        inputs = inputs.filter(':enabled').data(key, true);
      } else {
        inputs = inputs.filter(function() {
          return $.data(this, key) === true;
        }).removeData(key);
      }

      inputs
        .attr({ 'disabled':flag, 'arial-disabled':flag })
        .parent()[ flag ? 'addClass' : 'removeClass' ]('ui-state-disabled');

      this.element.attr({
        'disabled':flag,
        'aria-disabled':flag
      });
    },
    

    // open the menu
    open: function(e) {
      var self = this;
      var button = this.button;
      var menu = this.menu;
      var speed = this.speed;
      var o = this.options;
      var args = [];

      // bail if the multiselectopen event returns false, this widget is disabled, or is already open
      if(this._trigger('beforeopen') === false || button.hasClass('ui-state-disabled') || this._isOpen) {
        return;
      }
      var $container = menu.children("ul").first();
      var effect = o.show;

      // figure out opening effects/speeds
      if($.isArray(o.show)) {
        effect = o.show[0];
        speed = o.show[1] || self.speed;
      }

      // if there's an effect, assume jQuery UI is in use
      // build the arguments to pass to show()
      if(effect) {
        args = [ effect, speed ];
      }
/*========================================================================*/
      //add by huangjinmao 2013/12/11 有url参数时自动创建选项
	  var isFirst=true;//只动态生成一次标志
	  var selid=this.element.attr("id");
	  var selids=selectIdList.split(",");
	  $.each(selids,function(){
		if(selid==this){
			isFirst=false;
			return;
		}
	  });
	  if(isFirst){
		selectIdList+=selid+",";
	  }
     
	  if(o.url&&isFirst&&!o.type&&!o.async){
		  var noneSelectedText=o.noneSelectedText;
		  o['noneSelectedText'] = o.executeMessage; 
          this.refresh();
		  $.ajax({
              type: "post",
              url: o.url,
              data: null,
              datatype: "json",
              success: function (data) {
            	  var html="";
            	  var fail=false;
            	  if(data){
            		  var dataObj=eval("("+data+")");//转换为json对象
            		  if(dataObj.flag=="fail"){
            			  fail=true;
            		  }else{
            			  $.each(dataObj,function(){//根据返回的json对象添加选项
            				  var text=this.text;
            				  var value=this.value;
            				  if(undefined==text||!text){
            					  text=this.TEXT;
            				  }
            				  if(undefined==value||!value){
            					  value=this.VALUE;
            				  }
            				  if(undefined==value||!value){
            					  return;
            				  }
            				  html+="<option value='"+value+"'>"+text+"</option>";
            			  });
            			  if(!o.multiple&&html){
            				  html="<option value=''>===请选择===</option>"+html;
            			  }
            			  $(self.element).html(html);
            			  
            		  }
            	  }
                  if(o.data){//有url参数同时又data参数时
                	  self.element.addOptionsByData();
                  }
              
               if(html){
            	   o['noneSelectedText'] = noneSelectedText;
               }else if(fail){
            	   o['noneSelectedText'] = o['errorMessage']?o['errorMessage']:noneSelectedText; 
               }else{
            	   o['noneSelectedText'] ="加载完成，无数据";
               }
               self.refresh();
               /*setTimeout(function(){
            	   if(o['successMessage']){
            		   o['noneSelectedText'] = noneSelectedText; 
            		   self.refresh();
            	   }
               }, 100);*/
              },
              error: function(XMLHttpRequest, textStatus, errorThrown){
            	  //f('404'==XMLHttpRequest.status){
	            	  o['noneSelectedText'] = o['errorMessage']?o['errorMessage']:noneSelectedText; 
	                  self.refresh();
            	  //}
              }
          });
	  }else if(o.data&&isFirst&&!o.type){
    	  this.addOptionsByData();
	  }else if('tree'==o.type){
		 
		// this.createTree();
		 //this._trigger('refresh');
	  }
	 
 /*========================================================================*/     

      // set the scroll of the checkbox container
		$container.scrollTop(0).height(o.height);

      // positon
      this.position();

      // show the menu, maybe with a speed/effect combo
      $.fn.show.apply(menu, args);

      // select the first not disabled option
      // triggering both mouseover and mouseover because 1.4.2+ has a bug where triggering mouseover
      // will actually trigger mouseenter.  the mouseenter trigger is there for when it's eventually fixed
      this.labels.filter(':not(.ui-state-disabled)').eq(0).trigger('mouseover').trigger('mouseenter').find('input').trigger('focus');

      button.addClass('ui-state-active');
      this._isOpen = true;
      this._trigger('open');
    },
//=========================================================================================
    //addOptionsByData  data is a json Object 根据json类型数据data自动添加选项
    addOptionsByData:function(){
    	 var opt=null,self =this;
         $.each(this.options.data,function(){//根据返回的json对象添加选项
       	  opt = $('<option />', {
				  value: this.value,
				  text: this.text
			   });
			   opt.appendTo(self.element);
			   self.refresh();
       	  
         });
    },
    
    //创建下拉树选项
	createTree:function(){
		var o=this.options;
		var self=this;
		
		
		var setting=o.setting;
		var check=setting.check;
		var selid=this.element.attr("id");
		if (tree!=""  &&tree.setting.treeId=="ztree"+selid){
			tree.destroy();
		}
		 if ($("#ztree"+selid).attr('id')==undefined)
		 {
			var html ="<input type='hidden' value='' id='def_select_ztree"+selid+"'/><ul id='ztree"+selid+"' class='ztree'></ul>";
			this.checkboxContainer.html(html);
		 }
		 
		 if(check){//setting参数中是否设置了setting.check属性值
			 if(!check.enable&&check.enable!=false){//未传入该参数时（是否显示checkbox/radio），默认跟mutselect的multiple（是否多选）一致，并且为true时zTree默认就是显示checkbox
				check.enable=o.multiple;
			 }
			 if("radio"==check.chkStyle&&"level"!=check.radioType){//当选项为radio并且未传入该参数值未“level”时，对整棵树单选,整棵树当成一个组
				check.radioType="all";
			 }
			 if(!check.chkboxType){//未传入该参数时取消子父选项关联
				check.chkboxType={ "Y": "", "N": "" };
			 }
			
		 }else{
			setting.check={
				enable:o.multiple,
				chkboxType:{ "Y": "", "N": "" }
			};
		 }
		 
		 
		 
		 var val_temp=self.element.val();
		 var opt=self.element[0].options;
		 var text=null;
		 if(opt&&0!=opt.length){
			 text=opt[0].text;
		 }
		 if(text){
			 $("#def_select_ztree"+selid).attr("text",text);
		 }
		 $("#def_select_ztree"+selid).val(val_temp);
		  if(!setting.callback){
			setting.callback={};
		  }
		 if(!setting.callback.onClick){
			setting.callback.onClick=function(event, treeId, treeNode){
				 var def=$("#def_select_ztree"+selid);//默认值div
				  if(def&&def.val()){
					  def.val(null);
					  self._setButtonValue(self.options.noneSelectedText, "");
				  }
				if(!self.options.multiple||!self.options.setting.check.enable){
					if(self.options.isAddParentToLabel){//是否将选中的父节点回填到文本框中
						self._setButtonValue(treeNode.name,treeNode.value);
						multiValues=treeNode.value;
					}else{
						if(!treeNode.isParent||!treeNode.children&&treeNode.children!=undefined){//选中的不是父节点才回填到文本框中
							self._setButtonValue(treeNode.name,treeNode.value);
							multiValues=treeNode.value;
						}
					}
			
				}
			};
		 }
		 
		 if(!setting.callback.onDblClick){
			 setting.callback.onDblClick=function(event, treeId, treeNode){
				 var def=$("#def_select_ztree"+selid);//默认值div
				  if(def&&def.val()){
					  def.val(null);
					  self._setButtonValue(self.options.noneSelectedText, "");
				  }
				if(!self.options.multiple||!self.options.setting.check.enable){
					if(self.options.isAddParentToLabel){//是否将选中的父节点回填到文本框中
						self._setButtonValue(treeNode.name,treeNode.value);
						multiValues=treeNode.value;
						self.close();
					}else{
						if(!treeNode.isParent||!treeNode.children&&treeNode.children!=undefined){//选中的不是父节点才回填到文本框中
							self._setButtonValue(treeNode.name,treeNode.value);
							multiValues=treeNode.value;
							self.close();
						}
					}
			
				}
			}; 
		 }
		  if(!setting.callback.onCheck){
			setting.callback.onCheck=function(event, treeId, treeNode){
				 var def=$("#def_select_ztree"+selid);//默认值div
				  if(def&&def.val()){
					  def.val(null);
					  self._setButtonValue(self.options.noneSelectedText, "");
				  }
				var zTree = $.fn.zTree.getZTreeObj(treeId);
				var checked = zTree.getCheckedNodes(true),
					o=self.options,
					multiValues="";
					value="";
					if(checked.length){
						$.each(checked,function(){
							if(o.isAddParentToLabel){//是否将选中的父节点回填到文本框中
								value+=this.name+",";
								multiValues+=this.value+",";
							}else{
								if(!this.isParent||!this.children&&this.children!=undefined){//选中的不是父节点才回填到文本框中
									value+=this.name+",";
									multiValues+=this.value+",";
								}
							}
						});
						multiValues=multiValues.substring(0,multiValues.length-1);
						value=value==""?o.noneSelectedText:value.substring(0,value.length-1);
					 }else{
						value=o.noneSelectedText;
					 }
				  self._setButtonValue(value,multiValues);
			};
			
		  }
		  if(!setting.callback.onNodeCreated){
			setting.callback.onNodeCreated=function(event, treeId, treeNode,select){
				if(self.options.async){
					var treeNodeSelectId='';
					var selectValues= $("#def_select_"+treeId).val();
					var selectIdTeam="";
					if(selectValues){
						selectIdTeam=selectValues.split(",");
					}
					for(var i=0 ;i<selectIdTeam.length;i++){
						treeNodeSelectId=selectIdTeam[i];
						if(treeNodeSelectId){
							var zTree=$.fn.zTree.getZTreeObj(treeId);
								var node=zTree.getNodeByParam("id", treeNodeSelectId, null);
								if(node&&0!=node.level){
									node=zTree.getNodeByParam("id", treeNodeSelectId, treeNode);
									if(node){
										zTree.expandNode(treeNode,true,true,true);
									}
								}
								if(treeNodeSelectId==treeNode.id){
									if(!self.options.multiple||!self.options.setting.check.enable){//单选
										zTree.selectNode(treeNode);
									}else{//多选
										zTree.checkNode(treeNode,true,true);
									}
									if(self.options.isAddParentToLabel){//是否将选中的父节点回填到文本框中
										self._setButtonValue(treeNode.name,treeNode.value);
										multiValues=treeNode.value;
									}else{
										if(!treeNode.isParent||!treeNode.children&&treeNode.children!=undefined){//选中的不是父节点才回填到文本框中
											self._setButtonValue(treeNode.name,treeNode.value);
											multiValues=treeNode.value;
										}
									}
									
								}
								
							
						}
						
					
					}
				}
			};
		  }
		 
		 
		 
		
			
		
		 if( !setting.async&&o.url&&o.async){//没有设置异步加载参数并且有url参数时
			setting.async={
				enable: true,
				url:o.url,
				dataType: "text",
				autoParam:o.autoParam,
				dataFilter: function(treeId, parentNode, responseData){
					self._ajaxDataFilter(treeId, parentNode, responseData);//异步加载完成后对返回数据处理
				    return responseData;
				}
			};
			setting.data={
				simpleData: {
			     enable: true
			    }
			};
		 }else if (o.url&&o.async){
			setting.async.url=o.url;
		 
		 }
		var zNodes =o.data;
		if(!o.parentCheckable&&zNodes){
			$.each(zNodes,function(){
				self._changeProperty(this,'nocheck',true);
			});
		}
		tree=$.fn.zTree.init($("#ztree"+selid), setting, zNodes);
	},
	//完成异步加载树后对返回数据进行处理
	_ajaxDataFilter:function(treeId, parentNode, responseData){
		var o=this.options;
		var self=this;
		if (!responseData) return null;
		if(!o.parentCheckable){//父节点不可选时
			$.each(responseData,function(){
				self._changeProperty(this,'nocheck',true);
			});
		}
		/*for (var i=0, l=responseData.length; i<l; i++) {
			responseData[i].name = responseData[i].name.replace(/\.n/g, '.');
		}*/
		return responseData;
	
	},

	/*主要针对下拉树修改节点属性：1、递归判断是否父节点,是则去掉前面的checkbox/radio
	 * jsonData要修改属性的对象
	 * key 要修改的属性名称
	 * value 要修改的属性的值
	*/
	_changeProperty: function(jsonData,key,value){
		var self=this;
		var children=jsonData.children;
		if('nocheck'==key){
			if(jsonData.isParent||children){
				jsonData[key]=value;
				if(children&&0!=children.length){
					$.each(jsonData.children,function(){
						self._changeProperty(this,key,value);
					});
				}
			}
			
		}
	},


//========================================================================================
    // close the menu
    close: function() {
      if(this._trigger('beforeclose') === false) {
        return;
      }

      var o = this.options;
      var effect = o.hide;
      var speed = this.speed;
      var args = [];

      // figure out opening effects/speeds
      if($.isArray(o.hide)) {
        effect = o.hide[0];
        speed = o.hide[1] || this.speed;
      }

      if(effect) {
        args = [ effect, speed ];
      }

      $.fn.hide.apply(this.menu, args);
      this.button.removeClass('ui-state-active').trigger('blur').trigger('mouseleave');
      this._isOpen = false;
      this._trigger('close');
    },

    enable: function() {
      this._toggleDisabled(false);
    },

    disable: function() {
      this._toggleDisabled(true);
    },

    checkAll: function(e) {
      this._toggleChecked(true);
      this._trigger('checkAll');
    },

    uncheckAll: function() {
	  if("tree"==this.options.type){
			var treeID=this.menu.find("ul[id]").attr("id");
		  	$.fn.zTree.getZTreeObj(treeID).checkAllNodes(false);
			multiValues="";
			this._setButtonValue(this.options.noneSelectedText,multiValues);			
	  }else{
		this._toggleChecked(false);
	  }
      this._trigger('uncheckAll');
    },

    getChecked: function() {
      return this.menu.find('input').filter(':checked');
    },

    destroy: function() {
      // remove classes + data
    	var self=this;
    	if($(this.element).multiselectfilter()){
    		$(this.element).multiselectfilter("destroy");
    	}
    	var selid=this.element.attr("id");
    	if(selectIdList){
    		var selids=selectIdList.split(",");
    		$.each(selids,function(){
    			if(selid==this){
    				selectIdList=selectIdList.replace(selid+",", "");
    				self.element.empty();
    				return;
    			}
    		});
    	}
      $.Widget.prototype.destroy.call(this);

      // unbind events
      $doc.unbind(this._namespaceID);

      this.button.remove();
      this.menu.remove();
      this.element.show();

      return this;
    },

    isOpen: function() {
      return this._isOpen;
    },

    widget: function() {
      return this.menu;
    },

    getButton: function() {
      return this.button;
    },

    position: function() {
      var o = this.options;

      // use the position utility if it exists and options are specifified
      if($.ui.position && !$.isEmptyObject(o.position)) {
        o.position.of = o.position.of || this.button;

        this.menu
          .show()
          .position(o.position)
          .hide();

        // otherwise fallback to custom positioning
      } else {
        var pos = this.button.offset();

        this.menu.css({
          top: pos.top + this.button.outerHeight(),
          left: pos.left
        });
      }
    },

    // react to option changes after initialization
    _setOption: function(key, value) {
      var menu = this.menu;
	  var o=this.options;

      switch(key) {
        case 'header':
			if (o.setting&&o.setting.check&&o.setting.check.enable==false){
				value=false;
			}else
          menu.find('div.ui-multiselect-header')[value ? 'show' : 'hide']();
          break;
        case 'checkAllText':
          menu.find('a.ui-multiselect-all span').eq(-1).text(value);
          break;
        case 'uncheckAllText':
          menu.find('a.ui-multiselect-none span').eq(-1).text(value);
          break;
        case 'height':
          menu.children('ul').first().height(parseInt(value, 10));
          break;
        case 'minWidth':
          o[key] = parseInt(value, 10);
          this._setButtonWidth();
          this._setMenuWidth();
          break;
        case 'selectedText':
        case 'selectedList':
        case 'noneSelectedText':
          o[key] = value; // these all needs to update immediately for the update() call
          this.update();
          break;
        case 'classes':
          menu.add(this.button).removeClass(o.classes).addClass(value);
          break;
        case 'multiple':
          menu.toggleClass('ui-multiselect-single', !value);
          o.multiple = value;
          this.element[0].multiple = value;
          this.refresh();
          break;
        case 'position':
          this.position();
		  break;
		case 'url':
		  o.url=value;  
		  break;
		case 'data':
		  o.data=value;
		  break;
		case 'executeMessage':
			o.executeMessage=value;
			break;
		case 'successMessage':
			o.successMessage=value;
			break;
		case 'errorMessage':
			o.errorMessage=value;
			break;
		case 'type':
			o.type=value;
			break;
		case 'setting':
			o.setting=value;
			break;
		case 'parentCheckable':
			o.parentCheckable=value;
			break;
		case 'isAddParentToLabel':
			o.isAddParentToLabel=value;
			break;
		case 'async':
			o.async=value;
			break;
		case 'autoParam':
			o.autoParam=value;
			break;
      }

      $.Widget.prototype._setOption.apply(this, arguments);
    }
  });

})(jQuery);

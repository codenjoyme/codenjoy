/*-
 * #%L
 * The MIT License (MIT)
 * %%
 * Copyright (c) 2014 Gregory Jacobs (http://greg-jacobs.com)
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * #L%
 */
(function($){
  var Pair = function(key, val){
    this.key = key;
    this.val = val;
  };

  Pair.prototype.getKeyMarkup = function(){
    return '<span class="key">' + this.key + '</span>';
  };

  Pair.prototype.getValType = function(){
    return typeof this.val;
  };

  Pair.prototype.getValInnerMarkup = function(){
    return JSON.stringify(this.val);
  };

  Pair.prototype.createTagInnerMarkup = function(){
    return this.getKeyMarkup() + ': <span class="val ' + this.getValType() + '">' + this.getValInnerMarkup() + '</span>';
  };

  Pair.prototype.getClass = function(){
    return 'pair';
  };

  Pair.prototype.createTag = function(){
    var $li = $('<li class="' + this.getClass() + '">');
    $li.html(this.createTagInnerMarkup());
    return $li;
  };

  Pair.prototype.render = function(){
    var $li = this.createTag();
    this.$el = $li;
  };


  var SimplePair = function(key, val){
    Pair.call(this, key, val);
  };

  $.extend(SimplePair.prototype, Pair.prototype);

  SimplePair.prototype.getClass = function(){
    return Pair.prototype.getClass.call(this) + ' simple';
  };

  SimplePair.prototype.getValInnerMarkup = function(){
    var valStr = Pair.prototype.getValInnerMarkup.call(this);
    return valStr;
  };


  var ExpandablePair = function(key, val){
    Pair.call(this, key, val);
  };

  $.extend(ExpandablePair.prototype, Pair.prototype);

  ExpandablePair.prototype.getValType = function(){
    return $.isArray(this.val) ? 'array' : 'object';
  };

  ExpandablePair.prototype.getClass = function(){
    return Pair.prototype.getClass.call(this) + ' expandable';
  };

  ExpandablePair.prototype.getValInnerMarkup = function(){
    var valStr = Pair.prototype.getValInnerMarkup.call(this);
    // truncate the array/object preview
    var valMatch = valStr.match(/^([\{\[])(.*)([\}\]])$/);
    return valMatch[1] + '<span class="val-inner">' + valMatch[2] + '</span>' + valMatch[3];
  };

  ExpandablePair.prototype.createTagInnerMarkup = function(){
    var $expander = $('<a class="expander" href="#">');
    $expander.on('click', $.proxy(this.onKeyClick, this));

    var innerMarkup = Pair.prototype.createTagInnerMarkup.call(this);
    $expander.append(innerMarkup);
    return $expander;
  };

  ExpandablePair.prototype.isExpanded = function(){
    return this.$el.hasClass('expanded');
  };

  ExpandablePair.prototype.expand = function(){
    // open new panel
    Panel.renderToEl(this.$el, {
      data: this.val
    });
    this.$el.addClass('expanded');
  };

  ExpandablePair.prototype.collapse = function(){
    this.$el.children('.panel').remove();
    this.$el.removeClass('expanded');
  };

  ExpandablePair.prototype.onKeyClick = function(e){
    if (this.isExpanded()){
      this.collapse();
    } else {
      this.expand();
    }

    e.stopPropagation();
    e.preventDefault();
  };



  // factory
  Pair.create = function(key, val){
    if ($.isPlainObject(val) || $.isArray(val)){
      return new ExpandablePair(key, val);
    } else {
      return new SimplePair(key, val);
    }
  };



  var Panel = function(options){
    this.options = options;
  };

  Panel.prototype.getData = function(){
    return this.options.data;
  };

  Panel.prototype.isArray = function(){
    return $.isArray(this.getData());
  };

  Panel.prototype.createListTag = function(){
    if (this.isArray()){
      return $('<ol class="list" start="0">');
    } else {
      return $('<ul class="list">');
    }
  };

  Panel.prototype.render = function(){
    var data = this.getData(),
      $list = this.createListTag(),
      self = this;

    $.each(data, function(key, val){
      var $li = self.createListItem(key, val);
      $list.append($li);
    });

    var $listWrap = $('<div class="panel">');
    $listWrap.html($list);

    this.$el = $listWrap;
    return this;
  };

  Panel.prototype.createListItem = function(key, val){
    var pair = Pair.create(key, val);
    pair.render();
    return pair.$el;
  };

  Panel.renderToEl = function($container, options){
    var panel = new Panel(options);
    panel.render();
    $container.append(panel.$el);
    return panel;
  }



  $.fn.jsonpanel = function(options){
    return Panel.renderToEl($(this), options);
  };
})(jQuery);

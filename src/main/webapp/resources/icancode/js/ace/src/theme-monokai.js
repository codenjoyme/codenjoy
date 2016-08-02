/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
ace.define("ace/theme/monokai",["require","exports","module","ace/lib/dom"], function(require, exports, module) {

exports.isDark = true;
exports.cssClass = "ace-monokai";
exports.cssText = ".ace-monokai .ace_gutter {\
background: #2F3129;\
color: #8F908A\
}\
.ace-monokai .ace_print-margin {\
width: 1px;\
background: #555651\
}\
.ace-monokai {\
background-color: #272822;\
color: #F8F8F2\
}\
.ace-monokai .ace_cursor {\
color: #F8F8F0\
}\
.ace-monokai .ace_marker-layer .ace_selection {\
background: #49483E\
}\
.ace-monokai.ace_multiselect .ace_selection.ace_start {\
box-shadow: 0 0 3px 0px #272822;\
}\
.ace-monokai .ace_marker-layer .ace_step {\
background: rgb(102, 82, 0)\
}\
.ace-monokai .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid #49483E\
}\
.ace-monokai .ace_marker-layer .ace_active-line {\
background: #202020\
}\
.ace-monokai .ace_gutter-active-line {\
background-color: #272727\
}\
.ace-monokai .ace_marker-layer .ace_selected-word {\
border: 1px solid #49483E\
}\
.ace-monokai .ace_invisible {\
color: #52524d\
}\
.ace-monokai .ace_entity.ace_name.ace_tag,\
.ace-monokai .ace_keyword,\
.ace-monokai .ace_meta.ace_tag,\
.ace-monokai .ace_storage {\
color: #F92672\
}\
.ace-monokai .ace_punctuation,\
.ace-monokai .ace_punctuation.ace_tag {\
color: #fff\
}\
.ace-monokai .ace_constant.ace_character,\
.ace-monokai .ace_constant.ace_language,\
.ace-monokai .ace_constant.ace_numeric,\
.ace-monokai .ace_constant.ace_other {\
color: #AE81FF\
}\
.ace-monokai .ace_invalid {\
color: #F8F8F0;\
background-color: #F92672\
}\
.ace-monokai .ace_invalid.ace_deprecated {\
color: #F8F8F0;\
background-color: #AE81FF\
}\
.ace-monokai .ace_support.ace_constant,\
.ace-monokai .ace_support.ace_function {\
color: #66D9EF\
}\
.ace-monokai .ace_fold {\
background-color: #A6E22E;\
border-color: #F8F8F2\
}\
.ace-monokai .ace_storage.ace_type,\
.ace-monokai .ace_support.ace_class,\
.ace-monokai .ace_support.ace_type {\
font-style: italic;\
color: #66D9EF\
}\
.ace-monokai .ace_entity.ace_name.ace_function,\
.ace-monokai .ace_entity.ace_other,\
.ace-monokai .ace_entity.ace_other.ace_attribute-name,\
.ace-monokai .ace_variable {\
color: #A6E22E\
}\
.ace-monokai .ace_variable.ace_parameter {\
font-style: italic;\
color: #FD971F\
}\
.ace-monokai .ace_string {\
color: #E6DB74\
}\
.ace-monokai .ace_comment {\
color: #75715E\
}\
.ace-monokai .ace_indent-guide {\
background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAEklEQVQImWPQ0FD0ZXBzd/wPAAjVAoxeSgNeAAAAAElFTkSuQmCC) right repeat-y\
}";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});

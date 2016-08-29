/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

var game = game || {};

function loadData(url, onLoad) {
    var ctx = (!!game.contextPath) ? game.contextPath : '';
    $.get(ctx + url, {}, function (data) {
        onLoad(data);
    });
}

loadData('../rest/context', function(ctx) {
    game.contextPath = ctx;
    $(document).ready(function() {
        $('body').trigger("context-loaded", ctx);
    });
});

// TODO continue with this
//function loadAllData(urls, onAllLoad) {
//    var url = url.shift();
//    var onCurrentSuccess = function() {
//        if (urls.length == 0) {
//            onAllLoad();
//        } else {
//            var nextUrl = url.shift();
//            loadData(nextUrl, onCurrentSuccess);
//        }
//    }
//
//    loadData(url, onCurrentSuccess);
//}
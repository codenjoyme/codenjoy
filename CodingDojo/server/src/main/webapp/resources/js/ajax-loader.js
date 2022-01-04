/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

var setup = setup || {};

function getContext() {
    var pathFromUrl = '/' + location.pathname.split('/')[1];
    var ctx = (!!setup.contextPath) ? setup.contextPath : pathFromUrl;
    return ctx;
}

// TODO to use this method everywhere and convert it to $.ajax({
function loadData(url, onLoad) {
    $.get(getContext() + url, {}, function (data) {
        onLoad(data);
    });
}

// TODO to use this method everywhere
function sendData(url, jsonData, onSend, onError) {
    $.ajax({
        type: "POST",
        url: getContext() + url,
        data: JSON.stringify(jsonData),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: onSend,
        error: function (error) {
            console.log(error.responseText);
            if (!!onError) {
                onError(error);
            }
        }
    });
}

function deleteData(url, onSend, onError) {
    $.ajax({
        type: "DELETE",
        url: getContext() + url,
        success: onSend,
        error: function (error) {
            console.log(error.responseText);
            if (!!onError) {
                onError(error);
            }
        }
    });
}

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

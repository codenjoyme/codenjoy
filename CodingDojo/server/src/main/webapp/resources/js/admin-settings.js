/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

var AdminAjax = function(contextPath, url) {
    var url = contextPath + '/' + url;

    var load = function(onSuccess, onError) {
        $.ajax({
            type: "GET",
            url: url,
            data: null,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: onSuccess,
            error: function (error) {
                console.log(error.responseText);
                if (!!onError) {
                    onError(error);
                }
            }
        });       
    }

    var save = function(info, onSuccess, onError) {
        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(info),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: onSuccess,
            error: function (error) {
                console.log(error.responseText);
                if (!!onError) {
                    onError(error);
                }
            }
        });
    }

    return {
        load : load,
        save : save
    }
}

var AdminSettings = function(contextPath, gameName, settingsName) {
    var url = 'rest/settings/' + gameName + '/' + settingsName;
    var ajax = new AdminAjax(contextPath, url);

    return {
        load : ajax.load,
        save : ajax.save
    }
}


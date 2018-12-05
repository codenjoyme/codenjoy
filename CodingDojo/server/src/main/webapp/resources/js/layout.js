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
function initLayout(gameName, pageName, contextPath, transformations, scriptSources, onPageLoad) {

    var appendUrl = function(string, search, substring) {
        $.each(search, function(index, found) {
            string = string.split(found).join(found + substring);
        });
        return string;
    }

    var loadLayout = function(onLoad) {
        var resource = "/resources/" + gameName + "/";
        $.ajax({ url:contextPath + resource + pageName,
            success:function (data) {
                var found = ['<link href="', '<img src="', '<script src="'];
                data = appendUrl(data, found, contextPath + resource);
                if (!!onLoad) {
                    onLoad(data);
                }
            },
            cache:false,
            timeout:30000
        });
    }

    loadLayout(function(page) {
        var bodyWasVisible = $(document.body).is(":visible");
        $(document.body).hide();

        $(page).prependTo($("#board_page"));
        if (!!transformations) {
            transformations();
        } else {
            $("#glasses").insertBefore($("#main_board"));
            $("#main_board").remove();

            $("#leaderboard").insertBefore($("#main_leaderboard"));
            $("#main_leaderboard").remove();
        }

        // because http://stackoverflow.com/questions/5085228/does-jquery-append-behave-asynchronously
        setTimeout(function() {
            if (bodyWasVisible) {
                $(document.body).show();
            }
            $.each(scriptSources, function(index, script) {
                $("head").append('<script type="text/javascript" src="' +
                        game.contextPath + '/resources/' + gameName + '/' + script +
                        '"></script>');
            });
            if (!!onPageLoad) {
                onPageLoad();
            }
        }, 300);
    })
}

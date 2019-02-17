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
pages = pages || {};

pages.rules = function() {
    var parts = window.location.pathname.split('/');
    var contextPath = parts[1];
    var base = window.location.origin + '/' + contextPath + '/';
    document.head.innerHTML = document.head.innerHTML + "<base target='_blank' href='" + base + "' />";

    $('pre').each(function(i, e) {
        var content = $(this).html();
        content = content.replace('codenjoy-contest', contextPath);
        $(this).html(content);
    });


    game.gameName = parts[parts.length - 1].split('.')[0].split('-')[0];
    game.contextPath = '/' + contextPath;

    initHotkeys();
}

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
/**
 * Created by Mikhail_Udalyi on 09.08.2016.
 */

var boardAllPageLoad = function() {

    if (game.debug) {
        game.debugger();
    }

    initLeadersTable(game.contextPath, game.playerName, game.code,
        function(count, you, link, name, score, maxLength, level) {
            var star = '';
            if (count == 1) {
                star = 'first';
            } else if (count <= 3) {
                star = 'second';
            }
            return '<tr>' +
                '<td><span class="' + star + ' star">' + count + '<span></td>' +
                '<td>' + you + '<a href="' + link + '">' + name + '</a></td>' +
                '<td class="center">' + score + '</td>' +
                '</tr>';
        });

    $('#table-logs').removeClass('table');
    $('#table-logs').removeClass('table-striped');
    $(document.body).show();
}
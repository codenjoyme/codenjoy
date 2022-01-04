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
pages = pages || {};

pages.main = function() {
    initHotkeys();

    bindActiveGameCheck();
};


function bindActiveGameCheck() {
    $('.gameView').click(function(e) {

        e.preventDefault();

        var contextPath = 'codenjoy-contest';
        var game = $(this).attr('game');
        var viewLink = $(this);

        $.get('/' + contextPath + '/rest/' + game + '/status',
            null,
            function(statusData) {
                if (!statusData) {
                    return;
                }

                if (!statusData.active === true) {
                    if (confirm('Nobody is playing this game yet. Would you like to rejoin and become first?')) {
                        $('#rejoin-' + game)[0].click();
                    }
                } else {
                    viewLink.unbind('click');
                    viewLink[0].click();
                }
            },
            'json'
        )
    })
}
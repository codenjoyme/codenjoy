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

setup.enableJoystick = false;

var description = null;
var setDescription = function(text) {
    description = text;
}

var showDescriptionOnClick = function() {
    if (!setup.registered) {
        return;
    }
    var container = "#div_" + setup.playerId;
    $(container + " #player_name").click(function(){
        if (!!description) {
            alert(description.replace(/\\n/g, "\n"));
        }
    });
}

setup.onBoardPageLoad = function() {
    showDescriptionOnClick();
}

setup.onBoardAllPageLoad = function() {
    showDescriptionOnClick();
}


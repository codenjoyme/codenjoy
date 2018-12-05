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
function setupCanvasMouseWheel(canvas) {
    var width = $('#players_container canvas').first().css("width");
    if (width != "0px" && width != "0") {
        canvas.css("width", width);
    }
    canvas.mousewheel(function(event) {
        if (!event.originalEvent.ctrlKey) {
            return;
        }

        var delta;
        if (event.originalEvent.wheelDelta !== undefined) {
            delta = event.originalEvent.wheelDelta;
        } else {
            delta = event.originalEvent.deltaY * -1;
        }

        if (delta > 0) {
            $(".player-canvas canvas").css("width", "+=10");
        } else {
            $(".player-canvas canvas").css("width", "-=10");
        }

        event.preventDefault();
    });
}
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
function setupMouseWheelZoom() {
    function getScrollDelta(event) {
        if (event.wheelDelta !== undefined) {
            return event.wheelDelta;
        } else {
            return event.deltaY * -1;
        }
    }

    function change(component, delta, css, value) {
        if (delta > 0) {
            component.css(css, '+=' + value);
        } else {
            component.css(css, '-=' + value);
        }
    }

    $('[zoom-on-wheel]').mousewheel(function(event) {
        var element = $(event.target);

        var component = element.parents('[zoom-on-wheel]');
        var delta = getScrollDelta(event.originalEvent);
        if (event.originalEvent.ctrlKey) {
            change(component, delta, 'zoom', "0.02");
            event.preventDefault();
        } else if (event.originalEvent.altKey) {
            change(component, delta, 'width', "20");
            event.preventDefault();
        } else if (event.originalEvent.shiftKey) {
            var attribute = (component.css('position') == 'absolute') ? "left" : 'margin-left';
            change(component, delta, attribute, "10");
            event.preventDefault();
        }
    });
}
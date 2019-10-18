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
function initLogger() {
    // ----------------------- init logger -------------------
    var container = $('#ide-console');
    var wrapper = $('.console-wrapper');
    container.empty();

    var print = function(message) {
        var autoScroll = Math.abs(wrapper.scrollTop() + wrapper.height() - wrapper[0].scrollHeight) < 20;

        container.append('> ' + message + '<br>');

        if (autoScroll) {
            wrapper.scrollTop(wrapper[0].scrollHeight);
        }
    }

    var error = function(error, functionName) {
        console.log(error);
        print('Error: ' + error.message);
        print('For details check browser console. ' +
                'For debug open /resouces/js/icancode.js and ' +
                'set breakpoint into "' + functionName + '" function. ' +
                'Fix your bug then try again.');
    }

    var clean = function() {
        container.empty();
    }

    return {
        print : print,
        error : error,
        clean : clean
    };
};
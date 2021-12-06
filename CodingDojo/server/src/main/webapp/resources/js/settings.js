/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

function subOrUnSub() {
    var x = document.getElementById('form');
    if (x.hidden) {
        x.hidden = false;
    } else {
        x.hidden = true;
    }
}

function pop() {
    var popup = document.getElementById('myPopup');
    popup.classList.toggle('show');
}

function changeValue(name) {
    var x = document.getElementById(name);
    if(x.value == 'true'){
        x.value = 'false';
        x.checked = false;
    } else {
        x.value = 'true';
        x.checked = true;
    }
}

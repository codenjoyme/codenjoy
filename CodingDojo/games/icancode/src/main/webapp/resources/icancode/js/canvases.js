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

var setup = setup || {};

// так как спрайты icancode вылазят за сетку элемента,
// то надо рисовать всегда все спрайты
setup.isDrawOnlyChanges = false;

const PARAM_GAME_MODE = 'gameMode';

const SPRITES_EKIDS = 'ekids';
const SPRITES_ROBOT = 'robot';

const MODE_JS = 'javascript';
const MODE_EKIDS = 'ekids';
const MODE_BEFUNGE = 'befunge';
const MODE_CONTEST = 'contest';

setup.setupSprites = function() {
    var toLowerCase = function(param) {
        return (!!param) ? param.toLowerCase() : param;
    }

    setup.gameMode = toLowerCase(getSettings(PARAM_GAME_MODE, '#query'));
    setup.onlyControls = getSettings('controlsOnly', '#query');

    if (setup.onlyControls) {
        setup.drawCanvases = false;
        setup.enableHeader = false;
        setup.enableFooter = false;
        if (!setup.gameMode) { // TODO удалить if после изменения линков на dojorena
            setup.gameMode = MODE_JS;
        }
    } else {
        setup.enableHeader = true;
        setup.enableFooter = true;
    }

    if (!setup.gameMode) {
        // check KEYS constants in register.js
        setup.gameMode = toLowerCase(localStorage.getItem(PARAM_GAME_MODE));

        // TODO почему-то сторится в сторадж строчка "undefined"
        if (setup.gameMode == 'undefined') {
            localStorage.removeItem(PARAM_GAME_MODE);
            setup.gameMode = null;
        }
    }

    // TODO это тут надо потому что join на main page и
    //      форма регистрации иногда отпускает без указания мода
    if (!setup.gameMode) {
        setup.gameMode = MODE_JS;
    }

    if (setup.gameMode == MODE_JS) {
        setup.enableBefunge = false;
        setup.sprites = SPRITES_ROBOT;
    } else if (setup.gameMode == MODE_EKIDS) {
        setup.enableBefunge = true;
        setup.sprites = SPRITES_EKIDS;
    } else if (setup.gameMode == MODE_BEFUNGE) {
        setup.enableBefunge = true;
        setup.sprites = SPRITES_ROBOT;
    } else if (setup.gameMode == MODE_CONTEST) {
        setup.enableBefunge = false;
        setup.sprites = SPRITES_ROBOT;
        setup.onlyLeaderBoard = true;
    } else {
        throw new Error("Unknown iCanCode mode: " + setup.gameMode);
    }
    setup.isDrawByOrder = true;
}
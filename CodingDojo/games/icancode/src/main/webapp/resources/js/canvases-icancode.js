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

const PARAM_GAME_MODE = 'gameMode';
const STORAGE_GAME_TYPE = 'gameType';

const SPRITES_EKIDS = 'ekids';
const SPRITES_ROBOT = 'robot';

const MODE_JS = 'JavaScript';
const MODE_EKIDS = 'eKids';
const MODE_BEFUNGE = 'Befunge';
const MODE_CONTEST = 'Contest';

setup.setupSprites = function() {
    var url = new URL(window.location.href);

    if (url.searchParams.has(PARAM_GAME_MODE)) {
        setup.gameMode = url.searchParams.get(PARAM_GAME_MODE);
    }

    var onlyControls = url.searchParams.has('controlsOnly')
        && (url.searchParams.get('controlsOnly') == 'true');
    if (onlyControls) {
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
        setup.gameMode = localStorage.getItem(STORAGE_GAME_TYPE);
    }

    if (!setup.gameMode) { // TODO это тут надо потому что join на main page и форма регистрации иногда отпускает без указания мода
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
    setup.isDrawByOrder = (setup.sprites == SPRITES_EKIDS);
}
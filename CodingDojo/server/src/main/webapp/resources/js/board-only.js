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

pages.boardOnly = function() {
    setup.enableChat = false;
    setup.enableDonate = false;
    setup.enableJoystick = false;
    setup.enableAlways = false;
    setup.enablePlayerInfo = false;
    setup.enablePlayerInfoLevel = false;
    setup.enableLeadersTable = false;
    setup.enableForkMe = false;
    setup.enableInfo = false;
    setup.enableHotkeys = true;
    setup.enableAdvertisement = false;
    setup.showBody = true;
    setup.sprites = null;
    setup.heroInfo = null;
    setup.unauthorized = false;
    
    if (window.location.href.includes("click=true")) {
        setup.canvasCursor = 'pointer';
    
        $('body').click(function() {
            window.open(window.location.href.replace('only=true', 'only=false'));            
        });
    }

    pages.board();
}
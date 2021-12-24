package com.codenjoy.dojo.icancode.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2021 Codenjoy
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

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;

public class TestGameSettings extends GameSettings {

    public TestGameSettings() {
        bool(CHEATS, true);
        integer(PERK_ACTIVITY, 10);
        integer(PERK_AVAILABILITY, 10);
        integer(PERK_DROP_RATIO, 100);
        integer(DEATH_RAY_PERK_RANGE, 10);
        integer(GUN_RECHARGE, 2);
        integer(GUN_REST_TIME, 4);
        integer(GUN_SHOT_QUEUE, 2);
        integer(TICKS_PER_NEW_ZOMBIE, 5);
        string(DEFAULT_PERKS, "ajm,ajm");
    }
}

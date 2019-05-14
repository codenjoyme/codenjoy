package com.codenjoy.dojo.lemonade.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.lemonade.services.ScoreMode;
import com.codenjoy.dojo.services.settings.Settings;

public class GameSettings {

    private final Settings settings;

    public GameSettings(Settings settings) {
        this.settings = settings;
    }

    public int getLimitDays(){
        return settings.getParameter("Limit days").type(Integer.class).getValue();
    }

    public ScoreMode getScoreMode(){
        return getLimitDays() > 0
                ? ScoreMode.LAST_DAY_ASSETS
                : ScoreMode.SUM_OF_PROFITS;
    }
}

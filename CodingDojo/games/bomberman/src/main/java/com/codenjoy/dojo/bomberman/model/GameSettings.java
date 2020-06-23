package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;

public interface GameSettings {

    Dice getDice();

    Level getLevel();

    Walls getWalls();

    Hero getHero(Level level);

    Parameter<Integer> getBoardSize();

    Parameter<Boolean> isMultiple();

    Parameter<Boolean> isBigBadaboom();

    Parameter<Integer> getPlayersPerRoom();

    RoundSettingsWrapper getRoundSettings();

    Parameter<Integer> diePenalty();

    Parameter<Integer> killOtherHeroScore();

    Parameter<Integer> killMeatChopperScore();

    Parameter<Integer> killWallScore();

    Parameter<Integer> winRoundScore();

    Parameter<Integer> getDestroyWallCount();

    Parameter<Integer> getBombPower();

    Parameter<Integer> getBombsCount();

    Parameter<Integer> getMeatChoppersCount();

    Parameter<Integer> catchPerkScore();
}

package com.codenjoy.dojo.expansion.model;

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


import com.codenjoy.dojo.expansion.model.levels.Level;
import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.model.levels.items.HeroForces;
import com.codenjoy.dojo.expansion.model.levels.items.Start;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.layeredview.LayeredBoardReader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IField extends GameField<Player>  {

    void increase(Hero hero, List<ForcesMoves> increase);

    void move(Hero hero, List<ForcesMoves> movements);

    Start getBaseOf(Hero hero);

    @Nullable
    Start getFreeBase();

    HeroForces startMoveForces(Hero item, int x, int y, int count);

    void removeForces(Hero hero, int x, int y);

    void reset();

    void removeFromCell(Hero hero);

    int totalRegions();

    int regionsCount(Hero hero);

    default BoardReader reader() {
        return null;
    }

    int getViewSize();

    int size();

    Level getCurrentLevel();

    boolean isMultiplayer();

    void newGame(Player player);

    void remove(Player player);

    List<Player> getPlayers();

    boolean isFree();

    int freeBases();

    int allBases();

    int getRoundTicks();

    String id();

    LayeredBoardReader layeredReader();

    long ticker();
}

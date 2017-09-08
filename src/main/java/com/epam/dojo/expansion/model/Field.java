package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.epam.dojo.expansion.model.levels.items.BaseItem;
import com.epam.dojo.expansion.model.levels.items.Hero;
import com.epam.dojo.expansion.model.levels.items.HeroForces;
import com.epam.dojo.expansion.model.levels.items.Start;
import com.epam.dojo.expansion.model.levels.Cell;
import com.epam.dojo.expansion.model.levels.Item;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Field {

    void increase(Hero hero, List<ForcesMoves> increase);

    void move(Hero hero, List<ForcesMoves> movements);

    boolean isBarrier(int x, int y);

    Start getBaseOf(Hero hero);

    @Nullable
    Start getFreeBase();

    Cell getEndPosition();

    HeroForces startMoveForces(Hero item, int x, int y, int count);
    int leaveForces(Hero item, int x, int y, int count);
    int countForces(Hero hero, int x, int y);
    void removeForces(Hero hero, int x, int y);

    Cell getCell(int x, int y);

    Item getIfPresent(Class<? extends BaseItem> clazz, int x, int y);

    boolean isAt(int x, int y, Class<? extends BaseItem>... clazz);

    void reset();

    void removeFromCell(Hero hero);

    int totalRegions();
    int regionsCount(Hero hero);
}

package com.codenjoy.dojo.icancode.model;

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


import com.codenjoy.dojo.icancode.model.items.*;
import com.codenjoy.dojo.icancode.model.items.perks.Perk;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.layeredview.LayeredBoardReader;

import java.util.List;
import java.util.Optional;

public interface Field extends GameField<Player> {

    boolean isBarrier(Point pt);

    Cell getStartPosition();

    Cell getEndPosition();

    void move(Item item, Point pt);

    Optional<Perk> perkAt(Point pt);

    Optional<Perk> dropNextPerk();

    void dropPickedGold(Hero hero);

    Cell getCell(Point pt);

    <T extends BaseItem> T getIf(Class<T> clazz, Point pt);

    boolean isAt(Point pt, Class<? extends BaseItem>... clazz);

    void reset();

    boolean isContest();

    LayeredBoardReader layeredReader();

    Dice dice();

    Level getLevel();

    void fire(Direction direction, Point from, FieldItem owner);

    int size();

    List<Zombie> zombies();

    List<Laser> lasers();

    List<Gold> pickedGold();

    List<LaserMachine> laserMachines();

    List<ZombiePot> zombiePots();

    List<Floor> floor();

    List<Perk> availablePerks();
}

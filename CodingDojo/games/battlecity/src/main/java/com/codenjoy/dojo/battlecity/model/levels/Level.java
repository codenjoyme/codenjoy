package com.codenjoy.dojo.battlecity.model.levels;

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


import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.battlecity.model.items.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.codenjoy.dojo.games.battlecity.Element.*;
import static com.codenjoy.dojo.services.Direction.*;

public class Level extends AbstractLevel {

    private Dice dice;

    public Level(String map, Dice dice) {
        super(map);
        this.dice = dice;
    }

    public List<Wall> walls() {
        return find(Wall::new, WALL);
    }

    public List<River> rivers() {
        return find(River::new, RIVER);
    }

    public List<Ice> ice() {
        return find(Ice::new, ICE);
    }

    public List<Tree> trees() {
        return find(Tree::new, TREE);
    }

    public List<Tank> aiTanks() {
        return new LinkedList<>(){{
            addAll(find((pt, el) -> new AITank(pt, DOWN, dice),  AI_TANK_DOWN));
            addAll(find((pt, el) -> new AITank(pt, UP, dice),    AI_TANK_UP));
            addAll(find((pt, el) -> new AITank(pt, LEFT, dice),  AI_TANK_LEFT));
            addAll(find((pt, el) -> new AITank(pt, RIGHT, dice), AI_TANK_RIGHT));
        }};
    }

    public List<Tank> tanks() {
        return new LinkedList<>(){{
            addAll(find((pt, el) -> new Tank(pt, DOWN),  TANK_DOWN,  OTHER_TANK_DOWN));
            addAll(find((pt, el) -> new Tank(pt, UP),    TANK_UP,    OTHER_TANK_UP));
            addAll(find((pt, el) -> new Tank(pt, LEFT),  TANK_LEFT,  OTHER_TANK_LEFT));
            addAll(find((pt, el) -> new Tank(pt, RIGHT), TANK_RIGHT, OTHER_TANK_RIGHT));
        }};
    }

    public List<Border> borders() {
        return find(Border::new, BATTLE_WALL);
    }

    @Override
    public void addAll(Consumer<Iterable<? extends Point>> processor) {
        processor.accept(borders());
        processor.accept(walls());
        processor.accept(aiTanks());
        processor.accept(ice());
        processor.accept(rivers());
        processor.accept(trees());
    }
}

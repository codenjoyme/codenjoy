package com.codenjoy.dojo.clifford.model.levels;

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


import com.codenjoy.dojo.games.clifford.Element;
import com.codenjoy.dojo.clifford.model.Hero;
import com.codenjoy.dojo.clifford.model.items.*;
import com.codenjoy.dojo.clifford.model.items.Potion.PotionType;
import com.codenjoy.dojo.clifford.model.items.robber.Robber;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.services.field.PointField;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import static com.codenjoy.dojo.games.clifford.Element.*;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    @Override
    protected void fill(PointField field) {
        field.addAll(borders());
        field.addAll(pipe());
        field.addAll(ladder());
        field.addAll(bricks());
        field.addAll(backways());
        field.addAll(potions());
        field.addAll(knifeClue());
        field.addAll(gloveClue());
        field.addAll(ringClue());
        field.addAll(robbers());
    }

    public List<Hero> heroes() {
        EnumSet<Element> left = EnumSet.of(
                HERO_CRACK_LEFT, HERO_LEFT, HERO_FALL_LEFT, HERO_PIPE_LEFT,
                HERO_MASK_CRACK_LEFT,
                HERO_MASK_LEFT, HERO_MASK_FALL_LEFT, HERO_MASK_PIPE_LEFT);

        EnumSet<Element> right = EnumSet.of(
                HERO_CRACK_RIGHT, HERO_RIGHT, HERO_FALL_RIGHT, HERO_PIPE_RIGHT,
                HERO_MASK_CRACK_RIGHT,
                HERO_MASK_RIGHT, HERO_MASK_FALL_RIGHT, HERO_MASK_PIPE_RIGHT);

        return find(new HashMap<>() {{
            left.forEach(element -> put(element, pt -> new Hero(pt, Direction.LEFT)));
            right.forEach(element -> put(element, pt -> new Hero(pt, Direction.RIGHT)));
        }});
    }

    public List<Brick> bricks() {
        return find(Brick::new, BRICK);
    }

    public List<Border> borders() {
        return find(Border::new, STONE);
    }

    public List<ClueKnife> knifeClue() {
        return find(ClueKnife::new, CLUE_KNIFE);
    }

    public List<ClueGlove> gloveClue() {
        return find(ClueGlove::new, CLUE_GLOVE);
    }

    public List<ClueRing> ringClue() {
        return find(ClueRing::new, CLUE_RING);
    }

    public List<Ladder> ladder() {
        return find(Ladder::new, LADDER);
    }

    public List<Pipe> pipe() {
        return find(Pipe::new, PIPE);
    }

    public List<Robber> robbers() {
        return find(new HashMap<>() {{
            put(ROBBER_LEFT, pt -> new Robber(pt, Direction.LEFT));
            put(ROBBER_RIGHT, pt -> new Robber(pt, Direction.RIGHT));
        }});
    }

    public List<Potion> potions() {
        return find(pt -> new Potion(pt, PotionType.MASK_POTION), MASK_POTION);
    }

    public List<Backway> backways() {
        return find(Backway::new, BACKWAY);
    }
}
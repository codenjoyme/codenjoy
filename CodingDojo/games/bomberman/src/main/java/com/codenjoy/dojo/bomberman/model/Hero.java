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


import com.codenjoy.dojo.bomberman.model.perks.HeroPerks;
import com.codenjoy.dojo.bomberman.model.perks.Perk;
import com.codenjoy.dojo.bomberman.model.perks.PerkOnBoard;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.round.RoundPlayerHero;

import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Bomberman.ALL;
import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.bomberman.model.StateUtils.filter;
import static com.codenjoy.dojo.bomberman.model.StateUtils.filterOne;

public class Hero extends RoundPlayerHero<Field> implements State<Elements, Player> {

    private static final boolean WITHOUT_MEAT_CHOPPER = false;
    private Level level;
    private Dice dice;
    private boolean bomb;
    private Direction direction;
    private int score;

    private HeroPerks perks = new HeroPerks();

    public Hero(Level level, Dice dice) {
        this.level = level;
        this.dice = dice;
        score = 0;
        direction = null;
    }

    public void init(Field field) {
        super.init(field);
        int count = 0;
        do {
            move(dice.next(field.size()), dice.next(field.size()));
            while (isBusy(this) && !isOutOf(field.size())) {
                x++;
                if (isBusy(this)) {
                    y++;
                }
            }
        } while ((isBusy(this) || isOutOf(field.size())) && count++ < 1000);

        if (count >= 1000) {
            throw new RuntimeException("Dead loop at MyBomberman.init(Board)!");
        }
    }

    private boolean isBusy(Point pt) {
        for (Hero hero : field.heroes(ALL)) {
            if (hero != null && hero.itsMe(this) && hero != this) {
                return true;
            }
        }

        return field.walls().itsMe(pt);
    }

    @Override
    public void right() {
        if (!isActiveAndAlive()) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void down() {
        if (!isActiveAndAlive()) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!isActiveAndAlive()) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!isActiveAndAlive()) return;

        direction = Direction.LEFT;
    }

    @Override
    public void act(int... p) {
        if (!isActiveAndAlive()) return;

        if (direction != null) {
            bomb = true;
        } else {
            setBomb(x, y);
        }
    }

    public void apply() {
        if (!isActiveAndAlive()) return;

        if (direction == null) {
            return;
        }

        int newX = direction.changeX(x);
        int newY = direction.changeY(y);

        if (!field.isBarrier(newX, newY, WITHOUT_MEAT_CHOPPER)) {
            move(newX, newY);
            PerkOnBoard perk = ((Bomberman) field).pickPerkAtPoint(newX, newY);
            if (perk != null) {
                addPerk(perk.getPerk());
            }
        }
        direction = null;

        if (bomb) {
            setBomb(x, y);
            bomb = false;
        }
    }

    private void setBomb(int bombX, int bombY) {
        Perk bombCount = perks.getPerk(BOMB_COUNT_INCREASE);

        if (field.bombs(this).size() < level.bombsCount() + (bombCount != null ? bombCount.getValue() : 0)) {
            Perk bombBlastInc = perks.getPerk(BOMB_BLAST_RADIUS_INCREASE);
            int boost = bombBlastInc != null ? bombBlastInc.getValue() : 0;

            field.drop(new Bomb(this, bombX, bombY, level.bombsPower() + boost, field));
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Bomb bomb = filterOne(alsoAtPoint, Bomb.class);
        List<Hero> heroes = filter(alsoAtPoint, Hero.class);
        MeatChopper meat = filterOne(alsoAtPoint, MeatChopper.class);

        // player наблюдатель содержится в той же клетке которую прорисовываем
        if (heroes.contains(player.getHero())) {
            // герой наблюдателя неактивен или его вынесли
            if (!player.getHero().isActiveAndAlive()) {
                return DEAD_BOMBERMAN;
            }

            // герой наблюдателя жив и активен

            // под ним бомба
            if (bomb != null) {
                return BOMB_BOMBERMAN;
            }

            return BOMBERMAN;
        }

        // player наблюдает за клеткой в которой не находится сам

        // в клетке только трупики?
        if (heroes.stream().noneMatch(Hero::isActiveAndAlive)) {
            // если в клеточке с героем митчопер, рисуем его
            if (meat != null) {
                return meat.state(player, alsoAtPoint);
            }

            // если митчопера нет, следующий по опасности - бобма
            if (bomb != null) {
                return bomb.state(player, alsoAtPoint);
            }

            // и если опасности нет, тогда уже рисуем останки
            return OTHER_DEAD_BOMBERMAN;
        }

        // в клетке есть другие активные и живые герои

        // под ними бомба
        if (bomb != null) {
            return OTHER_BOMB_BOMBERMAN;
        }

        return OTHER_BOMBERMAN;
    }

    public Dice getDice() {
        return dice;
    }

    @Override
    public void tick() {
        perks.tick();
    }

    public List<Perk> getPerks() {
        return perks.getPerksList();
    }

    public void addPerk(Perk perk) {
        perks.add(perk);
    }

    public Perk getPerk(Elements element) {
        return perks.getPerk(element);
    }

    public int scores() {
        return score;
    }

    public void addScore(int score) {
        this.score += score;
    }
}


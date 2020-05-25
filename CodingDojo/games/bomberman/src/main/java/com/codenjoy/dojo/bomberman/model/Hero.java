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

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static java.util.stream.Collectors.toList;

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
            while (isBusy(x, y) && !isOutOfBoard(x, y)) {
                x++;
                if (isBusy(x, y)) {
                    y++;
                }
            }
        } while ((isBusy(x, y) || isOutOfBoard(x, y)) && count++ < 1000);

        if (count >= 1000) {
            throw new RuntimeException("Dead loop at MyBomberman.init(Board)!");
        }
    }

    private boolean isBusy(int x, int y) {
        for (Hero hero : field.heroes()) {
            if (hero != null && hero.itsMe(this) && hero != this) {
                return true;
            }
        }

        return field.walls().itsMe(x, y);
    }

    private boolean isOutOfBoard(int x, int y) {
        return x >= field.size() || y >= field.size() || x < 0 || y < 0;
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
        List<Bomb> bombs = filter(alsoAtPoint, Bomb.class);
        List<Hero> heroes = filter(alsoAtPoint, Hero.class);

        if (isActiveAndAlive()) {
            // есть другие герои в этой же клетке
            if (heroes.size() > 1) {
                // player наблюдатель содержится в той же клетке что и другие герои
                if (heroes.contains(player.getHero())) {
                    // герой наблюдателя активен и его еще не вынесли?
                    if (player.getHero().isActiveAndAlive()) {
                        return BOMBERMAN;
                    } else {
                        return DEAD_BOMBERMAN;
                    }
                    // player наблюдает за клеткой в которой один жив, другой мертв
                } else {
                    return OTHER_BOMBERMAN;
                }
            } else {
                if (this == player.getHero()) {
                    if (bombs.isEmpty()) {
                        return BOMBERMAN;
                    } else {
                        return BOMB_BOMBERMAN;
                    }
                } else {
                    if (bombs.isEmpty()) {
                        return OTHER_BOMBERMAN;
                    } else {
                        return OTHER_BOMB_BOMBERMAN;
                    }
                }
            }
        } else {
            if (heroes.contains(player.getHero())) {
                return DEAD_BOMBERMAN;
            } else {
                return OTHER_DEAD_BOMBERMAN;
            }
        }
    }

    private <T extends Point> List<T> filter(Object[] array, Class<T> clazz) {
        return (List)Arrays.stream(array)
                .filter(it -> it != null)
                .filter(it -> it.getClass().equals(clazz))
                .collect(toList());
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


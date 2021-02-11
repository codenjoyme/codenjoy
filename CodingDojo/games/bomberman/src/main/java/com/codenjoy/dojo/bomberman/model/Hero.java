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
import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.round.RoundPlayerHero;

import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.bomberman.model.Field.FOR_HERO;
import static com.codenjoy.dojo.services.StateUtils.filter;
import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Hero extends RoundPlayerHero<Field> implements State<Elements, Player> {

    public static final int MAX = 1000;

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

        int iteration = 0;
        while (iteration++ < MAX) {
            Point pt = PointImpl.random(dice, field.size());

            if (field.isBarrier(pt, !FOR_HERO)) {
                continue;
            }

            move(pt);
            break;
        }

        if (iteration >= MAX) {
            System.out.println("Dead loop at Hero.init(Board)!");
        }
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

        Point pt = direction.change(this);

        if (!field.isBarrier(pt, FOR_HERO)) {
            move(pt);
            PerkOnBoard perk = field.pickPerk(pt);
            if (perk != null) {
                addPerk(perk.getPerk());
                event(Events.CATCH_PERK);
            }
        }
        direction = null;

        if (bomb) {
            setBomb(x, y);
            bomb = false;
        }
    }

    private void setBomb(int x, int y) {
        List<Bomb> bombs = field.bombs(this);

        Perk remotePerk = perks.getPerk(BOMB_REMOTE_CONTROL);
        if (remotePerk != null) {
            // activate bombs that were set on remote control previously
            if (tryActivateRemote(bombs)) {
                remotePerk.decrease();
                return;
            }
        }

        Perk countPerk = perks.getPerk(BOMB_COUNT_INCREASE);
        if (!сanDrop(countPerk, bombs)) {
            return;
        }

        Perk blastPerk = perks.getPerk(BOMB_BLAST_RADIUS_INCREASE);
        int boost = (blastPerk == null) ? 0 : blastPerk.getValue();
        Bomb bomb = new Bomb(this, x, y, level.bombsPower() + boost, field);

        if (remotePerk != null) {
            bomb.putOnRemoteControl();
        }

        field.drop(bomb);
    }

    private boolean tryActivateRemote(List<Bomb> bombs) {
        boolean activated = false;
        for (Bomb bomb : bombs) {
            if (bomb.isOnRemote()) {
                bomb.activateRemote();
                activated = true;
            }
        }
        return activated;
    }

    private boolean сanDrop(Perk countPerk, List<Bomb> bombs) {
        // сколько бомб уже оставили?
        int placed = bombs.size();
        // дополнение от перка, если он есть
        int boost = (countPerk == null) ? 0 : countPerk.getValue();

        // сколько я всего могу
        int allowed = level.bombsCount() + boost;

        return placed < allowed;
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


package com.codenjoy.dojo.sample.model;

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


import com.codenjoy.dojo.games.sample.Element;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.round.RoundPlayerHero;

/**
 * Это реализация героя. Обрати внимание, что он реализует интерфейс
 * {@link Joystick}, а значит может быть управляем фреймворком.
 *
 * Так же он реализует {@link Tickable}, что значит - есть
 * возможность его оповещать о каждом тике игры (оповещением занимается поле).
 *
 * Ну и конечно же он реализует {@link State},
 * что значит герой может быть прорисован на поле.
 *
 * Часть этих интерфейсов объявлены в {@link RoundPlayerHero}, который
 * вместе с {@link com.codenjoy.dojo.services.round.RoundField} отвечает за логику
 * переключения раундов.
 */
public class Hero extends RoundPlayerHero<Field> implements State<Element, Player> {

    private int score;
    private Direction direction;
    private boolean bomb;

    public Hero(Point pt) {
        super(pt);
        score = 0;
        direction = null;
        bomb = false;
    }

    /**
     * @param field Поле которым инициализируется герой после его создания.
     */
    @Override
    public void init(Field field) {
        super.init(field);

        field.heroes().add(this);
    }

    /**
     * Один из методов {@link Joystick}. Реагируй на них только,
     * если герой жив и активен. Обычно тут сохраняется намерение и
     * лишь затем оно реализуется в методе {@link #tick()}.
     */
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
    public void right() {
        if (!isActiveAndAlive()) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        if (!isActiveAndAlive()) return;

        bomb = true;
    }

    /**
     * Сигнал сердцебиения. Так поле оповещает героя, что пришел тик.
     */
    @Override
    public void tick() {
        if (!isActiveAndAlive()) return;

        if (bomb) {
            field.setBomb(this);
            bomb = false;
        }

        if (direction != null) {
            Point to = direction.change(this.copy());

            if (field.bombs().contains(to)) {
                die();
                field.bombs().removeAt(to);
            }

            if (!field.isBarrier(to)) {
                move(to);
            }
        }
        direction = null;
    }

    /**
     * @param player Игрок для которого мы печатаем поле на экране.
     * @param alsoAtPoint Объекты, кто еще был в этой клетке на поле.
     * @return Символ, которым мы отрисуем героя при данных условиях.
     */
    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!isActiveAndAlive()) {
            if (this == player.getHero()) {
                return Element.DEAD_HERO;
            } else {
                return Element.OTHER_DEAD_HERO;
            }
        }

        if (this == player.getHero()) {
            return Element.HERO;
        } else {
            return Element.OTHER_HERO;
        }
    }

    /**
     * @return Очки, которые успел за время раунда заработать герой.
     * В спорных вопросах (время раунда вышло) на основе этого значения
     * будет приниматься решение о присуждении победы одному из оставшихся
     * на поле после таймаута героев.
     */
    @Override
    public int scores() {
        return score;
    }

    public void clearScores() {
        score = 0;
    }

    public void addScore(int added) {
        score = Math.max(0, score + added);
    }
}

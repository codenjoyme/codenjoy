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


import com.codenjoy.dojo.sample.model.items.Bomb;
import com.codenjoy.dojo.sample.model.items.Gold;
import com.codenjoy.dojo.sample.model.items.Wall;
import com.codenjoy.dojo.sample.services.Event;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то
 * у нее, то лучше ему дать интерфейс {@link Field}.
 *
 * Борда реализует метод {@link #tickField()} чтобы быть
 * уведомленной о каждом тике игры.
 *
 * Чтобы поддерживать много-раундовые матчи борда наследует
 * {@link RoundField}. Этот малый берет на себя всю логистику
 * связанную с ожиданием игроков между раундами, обратным отсчетом
 * времени перед стартом раунда и т.д.
 */
public class Sample extends RoundField<Player> implements Field {

    private Level level;
    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    public Sample() {
        // do nothing, for testing only
    }

    public Sample(Dice dice, Level level, GameSettings settings) {
        super(Event.START_ROUND, Event.WIN_ROUND, settings);

        this.level = level;
        this.dice = dice;
        this.settings = settings;
        this.field = new PointField();
        this.players = new LinkedList<>();

        clearScore();
    }

    @Override
    public void clearScore() {
        level.saveTo(field);
        field.init(this);

        // other clear score actions

        super.clearScore();
    }

    @Override
    public void onAdd(Player player) {
        player.newHero(this);
    }

    @Override
    public void onRemove(Player player) {
        heroes().removeExact(player.getHero());
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    @Override
    public void cleanStuff() {
        // clean all temporary stuff before next tick
    }

    @Override
    protected void setNewObjects() {
        // add new object after rewarding winner
    }

    /**
     * Сердце поля. Каждую секунду фреймворк будет тикать этот метод.
     * Важно помнить, что если раунд не начался - сигнал сюда не дойдет.
     */
    @Override
    public void tickField() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();

            if (gold().contains(hero)) {
                gold().removeAt(hero);

                player.event(Event.WIN);

                freeRandom(null)
                        .ifPresent(point -> field.add(new Gold(point)));
            }
        }
    }

    public int size() {
        return field.size();
    }

    @Override
    public boolean isBarrier(Point pt) {
        return pt.isOutOf(size())
                || walls().contains(pt)
                || heroes().contains(pt);
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        return BoardUtils.freeRandom(size(), dice, this::isFree);
    }

    @Override
    public boolean isFree(Point pt) {
        return !(gold().contains(pt)
                || bombs().contains(pt)
                || walls().contains(pt)
                || heroes().contains(pt));
    }

    @Override
    public void setBomb(Point pt) {
        if (!bombs().contains(pt)) {
            bombs().add(new Bomb(pt));
        }
    }

    @Override
    public void remove(Player player) {
        if (players.remove(player)) {
            heroes().removeExact(player.getHero());
        }
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    /**
     * @return Объект участвующий в прорисовке поля.
     *
     */
    @Override
    public BoardReader<Player> reader() {
        /**
         * Внимание! Порядок важен.
         * В этом порядке будут опрашиваться состояния через метод
         * {@link com.codenjoy.dojo.services.State#state(Object, Object...)}
         */
        return field.reader(
                Hero.class,
                Wall.class,
                Gold.class,
                Bomb.class);
    }

    /**
     * Метод для быстрой инициализации текущего поля из строкового представления.
     *
     * Актуально для сервиса, отвечающего на вопрос - каким будет следующий тик
     * при такой конфигурации поля.
     *
     * @param board Текстовое представление поля.
     * @param creator Метод создающий объекты-игроков для этих целей.
     * @return Список созданных игроков в новом измененном поле.
     */
    // TODO test me
    @Override
    public List<Player> load(String board, Supplier<Player> creator) {
        Level level = new Level(board);
        List<Player> result = new LinkedList<>();
        level.heroes().forEach(hero -> {
            Player player = creator.get();
            player.setHero(hero);
            result.add(player);

        });
        level.saveTo(field);
        return result;
    }

    /**
     * Дальше идут методы для получения быстрого доступа
     * к объектам разных типов на поле.
     */

    @Override
    public Accessor<Gold> gold() {
        return field.of(Gold.class);
    }

    @Override
    public Accessor<Hero> heroes() {
        return field.of(Hero.class);
    }

    @Override
    public Accessor<Wall> walls() {
        return field.of(Wall.class);
    }

    @Override
    public Accessor<Bomb> bombs() {
        return field.of(Bomb.class);
    }

}
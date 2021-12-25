package com.codenjoy.dojo.snakebattle.model.board;

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


import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;
import com.codenjoy.dojo.snakebattle.model.Level;
import com.codenjoy.dojo.snakebattle.model.Player;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.objects.*;
import com.codenjoy.dojo.snakebattle.services.Event;
import com.codenjoy.dojo.snakebattle.services.GameSettings;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.codenjoy.dojo.snakebattle.model.hero.Hero.NEXT_TICK;
import static java.util.stream.Collectors.toList;

public class SnakeBoard extends RoundField<Player> implements Field {

    private List<Wall> walls;
    private List<StartFloor> starts;
    private List<Apple> apples;
    private List<Stone> stones;
    private List<FlyingPill> flyingPills;
    private List<FuryPill> furyPills;
    private List<Gold> gold;

    private List<Player> players;
    private int size;
    private Dice dice;
    private GameSettings settings;

    public SnakeBoard(Level level, Dice dice, GameSettings settings) {
        super(Event.START, Event.WIN, Event.DIE, settings);
        this.dice = dice;
        walls = level.walls();
        starts = level.startPoints();
        apples = level.apples();
        stones = level.stones();
        flyingPills = level.flyingPills();
        furyPills = level.furyPills();
        gold = level.gold();
        size = level.size();
        this.settings = settings;
        players = new LinkedList<>();
    }

    @Override
    protected void cleanStuff() {
        snakesClear();
    }

    @Override
    public void tickField() {
        snakesMove();
        snakesFight();
        snakesEat();
        // после еды у змеек отрастают хвосты, поэтому столкновения нужно повторить
        // чтобы обработать ситуацию "кусь за растущий хвост", иначе eatTailThatGrows тесты не пройдут
        snakesFight();

        setNewObjects();
    }

    private void snakesClear() {
        players.stream()
                .filter(p -> p.isActive() && !p.isAlive())
                .forEach(p -> p.getHero().clear());
    }

    @Override
    public void setNewObjects() {
        int max = (players.size() / 2) + 1;
        int i = dice.next(50);
        Optional<Point> pt = freeRandom();
        if (!pt.isPresent()) {
            return;
        }

        if (i == 42 && furyPills.size() < max) {
            setFuryPill(pt.get());
        }

        if (i == 32 && flyingPills.size() < max) {
            setFlyingPill(pt.get());
        }

        if (i == 21 && gold.size() < max*2) {
            setGold(pt.get());
        }

        if ((i == 11 && stones.size() < size / 2) || stones.isEmpty()) {
            setStone(pt.get());
        }

        if ((i < 10 && apples.size() < max*10) || apples.size() < max*2) {
            setApple(pt.get());
        }
    }

    @Override
    public Optional<Point> freeRandom() {
        return BoardUtils.freeRandom(size, dice, pt -> isFree(pt));
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    private void snakesMove() {
        for (GamePlayer<Hero, Field> player : aliveActive()) {
            Hero hero = player.getHero();
            hero.tick();
        }
    }

    private static class ReduceInfo {
        Hero attacker;
        Hero pray;
        int reduce;

        public ReduceInfo(Hero attacker, Hero pray, int reduce) {
            this.attacker = attacker;
            this.pray = pray;
            this.reduce = reduce;
        }
    }

    @FunctionalInterface
    private interface Reduce {
        void doit(Hero attacker, Hero pray, int size);
    }

    private static class FightDetails  {
        private List<ReduceInfo> info = new LinkedList<>();

        public void cutOff(Hero attacker, Hero pray, int size) {
            info.add(new ReduceInfo(attacker, pray, size));
        }

        public boolean alreadyCut(Hero pray) {
            return info.stream()
                    .filter(info -> info.pray == pray)
                    .findAny()
                    .isPresent();
        }

        public void forEach(Reduce action) {
            info.forEach(info -> action.doit(info.attacker, info.pray, info.reduce));
        }
    }

    private void snakesFight() {
        FightDetails info = new FightDetails();

        notFlyingHeroes().forEach(hero -> {
            Hero enemy = enemyCrossedWith(hero);
            if (enemy != null) {
                if (enemy.isFlying()) {
                    return;
                }
                if (hero.isFury() && !enemy.isFury()) {
                    if (enemy.isAlive()) {
                        enemy.die();
                        info.cutOff(hero, enemy, enemy.size());
                    }
                } else if (!hero.isFury() && enemy.isFury()) {
                    if (hero.isAlive()) {
                        hero.die();
                        info.cutOff(enemy, hero, hero.size());
                    }
                } else {
                    if (!info.alreadyCut(hero)) {
                        int len1 = hero.reduce(enemy.size(), NEXT_TICK);
                        info.cutOff(enemy, hero, len1);
                    }

                    if (!info.alreadyCut(enemy)) {
                        int len2 = enemy.reduce(hero.size(), NEXT_TICK);
                        info.cutOff(hero, enemy, len2);
                    }
                }
                return;
            }

            enemy = enemyEatenWith(hero);
            if (enemy != null) {
                if (hero.isFury()) {
                    int len = enemy.reduceFrom(hero.head());
                    info.cutOff(hero, enemy, len);
                } else {
                    hero.die();
                    info.cutOff(enemy, hero, hero.size());
                }
            }
        });

        info.forEach((attacker, pray, reduce) -> {
            if (attacker.isAlive()) {
                attacker.event(Event.EAT.apply(reduce));
            }
        });
    }

    private void snakesEat() {
        for (GamePlayer<Hero, Field> player : aliveActive()) {
            Hero hero = player.getHero();
            Point head = hero.head();
            hero.eat();

            if (apples.contains(head)) {
                apples.remove(head);
                player.event(Event.APPLE);
            }
            if (stones.contains(head) && !hero.isFlying()) {
                stones.remove(head);
                if (player.isAlive()) {
                    player.event(Event.STONE);
                }
            }
            if (gold.contains(head)) {
                gold.remove(head);
                player.event(Event.GOLD);
            }
            if (flyingPills.contains(head)) {
                flyingPills.remove(head);
                player.event(Event.FLYING);
            }
            if (furyPills.contains(head)) {
                furyPills.remove(head);
                player.event(Event.FURY);
            }
        }
    }

    private Stream<Hero> notFlyingHeroes() {
        return aliveActive().stream()
                .map(player -> player.getHero())
                .filter(h -> !h.isFlying());
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(Point p) {
        return p.isOutOf(size) || walls.contains(p) || starts.contains(p);
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        for (int i = 0; i < 10 && !starts.isEmpty(); i++) {
            StartFloor start = starts.get(dice.next(starts.size()));
            if (freeOfHero(start)) {
                return Optional.of(start);
            }
        }
        for (StartFloor start : starts) {
            if (freeOfHero(start)) {
                return Optional.of(start);
            }
        }
        return Optional.empty();
    }

    public boolean isFree(Point pt) {
        return isFreeOfObjects(pt) && freeOfHero(pt);
    }

    public boolean isFreeForStone(Point pt) {
        Point leftSide = pt.copy();
        leftSide.move(Direction.LEFT);
        return isFree(pt) && !starts.contains(leftSide);
    }

    public boolean isFreeOfObjects(Point pt) {
        return !(apples.contains(pt) ||
                stones.contains(pt) ||
                walls.contains(pt) ||
                starts.contains(pt) ||
                flyingPills.contains(pt) ||
                furyPills.contains(pt) ||
                gold.contains(pt));
    }

    private boolean freeOfHero(Point pt) {
        for (Hero h : getHeroes()) {
            if (h != null && h.body().contains(pt) &&
                    !pt.equals(h.getTailPoint()))
                return false;
        }
        return true;
    }

    @Override
    public boolean isApple(Point p) {
        return apples.contains(p);
    }

    @Override
    public boolean isStone(Point p) {
        return stones.contains(p);
    }

    @Override
    public boolean isFlyingPill(Point p) {
        return flyingPills.contains(p);
    }

    @Override
    public boolean isFuryPill(Point p) {
        return furyPills.contains(p);
    }

    @Override
    public boolean isGold(Point p) {
        return gold.contains(p);
    }

    @Override
    public Hero enemyEatenWith(Hero me) {
        return aliveEnemies(me)
                .filter(h -> !h.isFlying())
                .filter(h -> h.body().contains(me.head()))
                .findFirst()
                .orElse(null);
    }

    private Stream<Hero> aliveEnemies(Hero me) {
        return aliveActive().stream()
                .map(player -> player.getHero())
                .filter(h -> !h.equals(me));
    }

    private Hero enemyCrossedWith(Hero me) {
        return aliveEnemies(me)
                .filter(h -> me.isHeadIntersect(h))
                .findFirst()
                .orElse(null);
    }

    public void addToPoint(Point p) {
        if (p instanceof Apple)
            setApple(p);
        else if (p instanceof Stone)
            setStone(p);
        else if (p instanceof FlyingPill)
            setFlyingPill(p);
        else if (p instanceof FuryPill)
            setFuryPill(p);
        else if (p instanceof Gold)
            setGold(p);
        else
            fail("Невозможно добавить на поле объект типа " + p.getClass());
    }

    @Override
    public void setApple(Point p) {
        if (isFree(p))
            apples.add(new Apple(p));
    }

    @Override
    public boolean setStone(Point p) {
        if (isFreeForStone(p)) {
            stones.add(new Stone(p));
            return true;
        }
        return false;
    }

    @Override
    public void setFlyingPill(Point p) {
        if (isFree(p))
            flyingPills.add(new FlyingPill(p));
    }

    @Override
    public void setFuryPill(Point p) {
        if (isFree(p))
            furyPills.add(new FuryPill(p));
    }

    @Override
    public void setGold(Point p) {
        if (isFree(p))
            gold.add(new Gold(p));
    }

    public List<Apple> getApples() {
        return apples;
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    protected void onAdd(Player player) {
        player.newHero(this);
    }

    @Override
    protected void onRemove(Player player) {
        // do nothing
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<StartFloor> getStarts() {
        return starts;
    }

    public List<FlyingPill> getFlyingPills() {
        return flyingPills;
    }

    public List<FuryPill> getFuryPills() {
        return furyPills;
    }

    public List<Gold> getGold() {
        return gold;
    }

    public List<Stone> getStones() {
        return stones;
    }

    public BoardReader reader() {
        return new BoardReader<Player>() {
            private int size = SnakeBoard.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
                processor.accept(new LinkedHashSet<>(){{
                    drawHeroes(hero -> !hero.isAlive(), hero -> Arrays.asList(hero.head()));
                    drawHeroes(hero -> hero.isFlying(), hero -> hero.reversedBody());
                    drawHeroes(hero -> !hero.isFlying(), hero -> hero.reversedBody());

                    addAll(getWalls());
                    addAll(getApples());
                    addAll(getStones());
                    addAll(getFlyingPills());
                    addAll(getFuryPills());
                    addAll(getGold());
                    addAll(getStarts());

                    for (Point p : this.toArray(new Point[0])) {
                    if (p.isOutOf(SnakeBoard.this.size())) {
                                remove(p);
                        }
                    }
                }

                    private void drawHeroes(Predicate<Hero> filter,
                                    Function<Hero, List<? extends Point>> getElements)
                    {
                        SnakeBoard.this.getHeroes().stream()
                                .filter(filter)
                                .sorted(Comparator.comparingInt(Hero::size))
                                        .forEach(hero -> addAll(getElements.apply(hero)));
                    }
                });
            }
        };
    }

    private void fail(String message) {
        throw new RuntimeException(message);
    }

    public Point getOn(Point pt) {
        if (apples.contains(pt)) {
            return new Apple(pt);
        }
        if (stones.contains(pt)) {
            return new Stone(pt);
        }
        if (flyingPills.contains(pt)) {
            return new FlyingPill(pt);
        }
        if (furyPills.contains(pt)) {
            return new FuryPill(pt);
        }
        if (gold.contains(pt)) {
            return new Gold(pt);
        }
        if (starts.contains(pt)) {
            return new StartFloor(pt);
        }
        if (walls.contains(pt)) {
            return new Wall(pt);
        }
        for (Player player : players) {
            if (player.getHero().body().contains(pt)) {
                return player.getHero().neck(); // это просто любой объект типа Tail
            }
        }
        return null;
    }
}

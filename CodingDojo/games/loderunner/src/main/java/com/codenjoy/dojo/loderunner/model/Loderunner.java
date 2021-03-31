package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.loderunner.model.Pill.PillType;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.*;
import java.util.function.Function;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static java.util.stream.Collectors.toList;

public class Loderunner implements Field {

    private int size;
    private Level level;
    private Players players;
    private List<Enemy> enemies;
    private List<YellowGold> yellowGold;
    private List<GreenGold> greenGold;
    private List<RedGold> redGold;
    private List<Pill> pills;
    private List<Portal> portals;
    private List<Border> borders;
    private List<Brick> bricks;
    private List<Ladder> ladder;
    private List<Pipe> pipe;
    private int portalsTicksLive;
    private Dice dice;
    private GameSettings settings;
    private List<Function<Point, Point>> finder;

    public Loderunner(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        this.level = level;
        this.settings = settings;
        players = new Players(this);
        enemies = new LinkedList<>();

        finder = new ArrayList<>(){{
            add(pt -> getFrom(getHeroes(), pt));
            add(pt -> getFrom(enemies(), pt));
            add(pt -> getFrom(yellowGold(), pt));
            add(pt -> getFrom(greenGold(), pt));
            add(pt -> getFrom(redGold(), pt));
            add(pt -> getFrom(borders(), pt));
            add(pt -> getFrom(bricks(), pt));
            add(pt -> getFrom(ladder(), pt));
            add(pt -> getFrom(pills(), pt));
            add(pt -> getFrom(pipe(), pt));
            add(pt -> getFrom(portals(), pt));
        }};

        init();
    }

    private void init() {
        size = level.getSize();
        borders = level.getBorders();
        bricks = level.getBricks();
        ladder = level.getLadder();
        pipe = level.getPipe();
        yellowGold = level.getYellowGold();
        greenGold = level.getGreenGold();
        redGold = level.getRedGold();
        pills = level.getPills();
        portals = level.getPortals();

        enemies = level.getEnemies();
        for (Enemy enemy : enemies) {
            enemy.init(this);
        }

        players.resetAll();

        generatePills();
        generateGold();
        generatePortals();
        generateEnemies();
    }

    @Override
    public void tick() {
//        if (!level.getMapUUID().equals(mapUUID)) {
//            init();
//        } TODO сделать по другому автоперезагрузку уровней

        Set<Player> die = new LinkedHashSet<>();

        heroesGo();
        die.addAll(getDied());

        enemiesGo();
        die.addAll(getDied());

        die.addAll(bricksGo());

        generateGold();
        portalsGo();

        for (Player player : die) {
            player.event(Events.KILL_HERO);
            Hero deadHero = player.getHero();
            rewardMurderers(deadHero);
        }
        generatePills();
        generateEnemies();
    }

    private void rewardMurderers(Point pt) {
        players.stream()
                .filter(player -> player.getHero().under(PillType.SHADOW_PILL))
                .filter(shadow -> shadow.getHero().itsMe(pt))
                .forEach(murderer -> murderer.event(Events.KILL_ENEMY));

    }

    private void generatePills() {
        int count = shadowPills();

        if (count <= pills.size()) {
            pills = pills.stream().limit(count).collect(toList());
            return;
        }
        count = count - pills.size();
        for (int i = 0; i < Math.abs(count); i++) {
            Optional<Point> pt = freeRandom();
            if (pt.isPresent()) {
                leavePill(pt.get(), PillType.SHADOW_PILL);
            }
        }
    }

    private int shadowPills() {
        int count = settings.integer(SHADOW_PILLS_COUNT);
        return count < 0 ? 0 : count;
    }

    private void generateEnemies() {
        int count = enemiesCount();

        if (count < enemies.size()) {
            enemies = enemies.stream().limit(count).collect(toList());
            return;
        }
        count = count - enemies.size();
        for (int i = 0; i < Math.abs(count); i++) {
            Optional<Point> pt = freeRandom();
            if (pt.isPresent()) {
                Enemy enemy = new Enemy(pt.get(), Direction.LEFT, level.getAi(), dice);
                enemies.add(enemy);
                enemy.init(this);
            }
        }
    }

    private int enemiesCount() {
        int count = settings.integer(ENEMIES_COUNT);
        return count < 0 ? 0 : count;
    }

    private void generatePortals() {
        int ticks = portalTicks();
        this.portalsTicksLive = ticks;

        int count = settings.integer(PORTALS_COUNT);

        portals.clear();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Optional<Point> pt = freeRandom();
                if (pt.isPresent()) {
                    leavePortal(pt.get());
                }
            }
        }
    }

    private int portalTicks() {
        int ticks = settings.integer(PORTAL_TICKS);
        return ticks < 1 ? 1 : ticks;
    }

    private List<Player> getDied() {
        return players.stream()
                .filter(player -> !player.isAlive())
                .collect(toList());
    }

    public BoardReader reader() {
        return new BoardReader() {

            @Override
            public int size() {
                return Loderunner.this.size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<>() {{
                    addAll(Loderunner.this.getHeroes());
                    addAll(Loderunner.this.enemies());
                    addAll(Loderunner.this.yellowGold());
                    addAll(Loderunner.this.greenGold());
                    addAll(Loderunner.this.redGold());
                    addAll(Loderunner.this.borders());
                    addAll(Loderunner.this.bricks());
                    addAll(Loderunner.this.ladder());
                    addAll(Loderunner.this.pills());
                    addAll(Loderunner.this.pipe());
                    addAll(Loderunner.this.portals());
                }};
            }
        };
    }

    private List<Player> bricksGo() {
        List<Player> die = new LinkedList<>();

        bricks.forEach(Brick::tick);

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                Optional<Brick> brick = getBrick(hero);
                if (!brick.isPresent()) continue;

                // Умер от того что кто-то просверлил стенку
                die.add(player);

                Hero killer = brick.get().getDrilledBy();
                Player killerPlayer = players.getPlayer(killer);
                if (killerPlayer != null && killerPlayer != player) {
                    killerPlayer.event(Events.KILL_ENEMY);
                }
            }
        }

        return die;
    }

    private Optional<Brick> getBrick(Point pt) {
        return bricks.stream()
                .filter(brick -> brick.equals(pt))
                .findFirst();
    }

    public List<Point> get(Point at) {
        if (at.isOutOf(size)) {
            return Arrays.asList(); // TODO это кажется только в тестах юзается, убрать бы отсюда для производительности
        }

        return finder.stream()
                .map(function -> function.apply(at))
                .filter(pt -> pt != null)
                .collect(toList());
    }

    public Point getFrom(List<? extends Point> elements, Point pt) {
        int index = elements.indexOf(pt);
        if (index == -1) {
            return null;
        } else {
            return elements.get(index);
        }
    }

    private void heroesGo() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();
            if (yellowGold.contains(hero)) {
                yellowGold.remove(hero);
                getGoldEvent(player, Events.GET_YELLOW_GOLD, YellowGold.class);
            } else if (greenGold.contains(hero)) {
                greenGold.remove(hero);
                getGoldEvent(player, Events.GET_GREEN_GOLD, GreenGold.class);
            } else if (redGold.contains(hero)) {
                redGold.remove(hero);
                getGoldEvent(player, Events.GET_RED_GOLD, RedGold.class);
            }

            if (pills.contains(hero)) {
                pills.remove(hero);
                hero.pick(PillType.SHADOW_PILL);
            }

            if (portals.contains(hero)) {
                transport(hero);
            }
        }
    }

    private void transport(PointImpl point) {
        for (int i = 0; i < portals.size(); i++) {
            if (portals.get(i).equals(point)) {
                Portal portalToMove = portals.get(i < portals.size() - 1 ? i + 1 : 0);
                point.move(portalToMove.getX(), portalToMove.getY());
                return;
            }
        }
    }

    private void enemiesGo() {
        for (Enemy enemy : enemies) {
            enemy.tick();

            if (yellowGold.contains(enemy) && !enemy.withGold()) {
                yellowGold.remove(enemy);
                enemy.getGold(YellowGold.class);
            } else if (greenGold.contains(enemy) && !enemy.withGold()) {
                greenGold.remove(enemy);
                enemy.getGold(GreenGold.class);
            } else if (redGold.contains(enemy) && !enemy.withGold()) {
                redGold.remove(enemy);
                enemy.getGold(RedGold.class);
            }

            if (portals.contains(enemy)) {
                transport(enemy);
            }
        }
    }

    private void portalsGo() {
        if (this.portalsTicksLive == 0) {
            generatePortals();
        } else {
            this.portalsTicksLive--;
        }
    }

    @Override
    public boolean isBarrier(Point pt) {
          return pt.getX() > size - 1 || pt.getX() < 0
                || pt.getY() < 0 || pt.getY() > size - 1
                || isFullBrick(pt)
                || isBorder(pt)
                || (isHeroAt(pt) && !under(pt, PillType.SHADOW_PILL));
    }

    @Override
    public void suicide(Hero hero) {
        players.getPlayer(hero).event(Events.SUICIDE);
    }

    @Override
    public boolean tryToDrill(Hero byHero, Point pt) {
        if (!isFullBrick(pt)) {
            return false;
        }

        Point over = Direction.UP.change(pt);
        if (isLadder(over)
                || yellowGold.contains(over)
                || greenGold.contains(over)
                || redGold.contains(over)
                || isFullBrick(over)
                || getHeroes().contains(over)
                || enemies.contains(over)) {
            return false;
        }

        Optional<Brick> brick = getBrick(pt);
        if (brick.isPresent()) {
            brick.get().drill(byHero);
        }

        return true;
    }

    @Override
    public boolean isPit(Point pt) {
        Point under = Direction.DOWN.change(pt);

        return !(isFullBrick(under)
                || isLadder(under)
                || isBorder(under)
                || isHeroAt(under)
                || enemies.contains(under));
    }

    @Override
    public boolean isFullBrick(Point pt) {
        // do not use streams here, optimized for performance
        int index = bricks.indexOf(pt);
        if (index == -1) {
            return false;
        }
        return bricks.get(index).state(null) == Elements.BRICK;
    }

    @Override
    public Optional<Point> freeRandom() {
        return BoardUtils.freeRandom(size, dice, pt -> isFree(pt));
    }

    @Override
    public boolean isLadder(Point pt) {
        return ladder.contains(pt);
    }

    @Override
    public boolean isPipe(Point pt) {
        return pipe.contains(pt);
    }

    @Override
    public boolean isFree(Point pt) {
        return get(pt).isEmpty();
    }

    @Override
    public boolean isHeroAt(Point pt) {
        return getHeroes().contains(pt);
    }

    @Override
    public boolean isBrick(Point pt) {
        return bricks.contains(pt);
    }

    @Override
    public boolean isEnemyAt(Point pt) {
        List<Hero> shadows = players.stream()
                .filter(player -> player.getHero().under(PillType.SHADOW_PILL))
                .map(Player::getHero)
                .collect(toList());
        return enemies.contains(pt) || shadows.contains(pt);
    }

    @Override
    public void leaveGold(Point pt, Class type) {
        if (type == YellowGold.class) {
            yellowGold.add(new YellowGold(pt));
        } else if (type == GreenGold.class) {
            greenGold.add(new GreenGold(pt));
        } else if (type == RedGold.class) {
            redGold.add(new RedGold(pt));
        }
    }

    @Override
    public void leavePill(Point pt, PillType pill) {
        pills.add(new Pill(pt, pill));
    }

    @Override
    public void leavePortal(Point pt) {
        portals.add(new Portal(pt));
    }

    @Override
    public boolean under(Point pt, PillType pill) {
        return players.stream()
                .map(Player::getHero)
                .filter(hero -> hero.equals(pt))
                .anyMatch(hero -> hero.under(pill));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isBorder(Point pt) {
        return borders.contains(pt);
    }

    @Override
    public List<Hero> getHeroes() {
        return players.heroes();
    }

    public void newGame(Player player) {
        players.add(player);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    private void getGoldEvent(Player player, Events event, Class type) {
        player.event(event);
        Optional<Point> pt = freeRandom();
        if (pt.isPresent()) {
            leaveGold(pt.get(), type);
        }
    }

    private void generateGold()  {
        int yellow = settings.integer(GOLD_COUNT_YELLOW);
        int green = settings.integer(GOLD_COUNT_GREEN);
        int red = settings.integer(GOLD_COUNT_RED);
        green = Math.max(green, 0);
        red = Math.max(red, 0);

        if (yellow >= 0 && yellow <= yellowGold.size()) {
            yellowGold = yellowGold.stream()
                    .limit(yellow)
                    .collect(toList());
        }
        if (green <= greenGold.size()) {
            greenGold = greenGold.stream()
                    .limit(green)
                    .collect(toList());
        }
        if (red <= redGold.size()) {
            redGold = redGold.stream()
                    .limit(red)
                    .collect(toList());
        }

        yellow = yellow - yellowGold.size();
        for (int i = 0; i < Math.max(0, yellow); i++) {
            Optional<Point> pt = freeRandom();
            if (pt.isPresent()) {
                yellowGold.add(new YellowGold(pt.get()));
            }
        }

        green = green - greenGold.size();
        for (int i = 0; i < Math.max(0, green); i++) {
            Optional<Point> pt = freeRandom();
            if (pt.isPresent()) {
                greenGold.add(new GreenGold(pt.get()));
            }
        }

        red = red - redGold.size();
        for (int i = 0; i < Math.max(0, red); i++) {
            Optional<Point> pt = freeRandom();
            if (pt.isPresent()) {
                redGold.add(new RedGold(pt.get()));
            }
        }
    }

    public List<Portal> portals() {
        return portals;
    }

    public List<YellowGold> yellowGold() {
        return yellowGold;
    }

    public List<GreenGold> greenGold() {
        return greenGold;
    }

    public List<RedGold> redGold() {
        return redGold;
    }

    public List<Border> borders() {
        return borders;
    }

    public List<Enemy> enemies() {
        return enemies;
    }

    public List<Brick> bricks() {
        return bricks;
    }

    public List<Ladder> ladder() {
        return ladder;
    }

    public List<Pill> pills() {
        return pills;
    }

    public List<Pipe> pipe() {
        return pipe;
    }

    // only for testing
    void resetHeroes() {
        players.resetHeroes();
    }
}

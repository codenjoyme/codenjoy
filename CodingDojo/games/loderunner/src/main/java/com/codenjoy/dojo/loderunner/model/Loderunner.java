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


import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

import com.codenjoy.dojo.loderunner.model.Pill.PillType;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Settings;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Loderunner implements Field {

    private final Settings settings;
    private int size;
    private Level level;
    private UUID mapUUID;
    private Point[][] field;
    private List<Player> players;
    private List<Enemy> enemies;
    private List<YellowGold> yellowGold;
    private List<GreenGold> greenGold;
    private List<RedGold> redGold;
    private List<Pill> pills;
    private List<Portal> portals;
    private Integer portalsTicksLive;
    private Dice dice;

    public Loderunner(Level level, Dice dice, Settings settings) {
        this.level = level;
        this.dice = dice;
        this.settings = settings;
        players = new LinkedList<>();
        init();
    }

    private void init() {
        mapUUID = level.getMapUUID();
        size = level.getSize();
        field = new Point[size][size];

        toField(level.getBorders());
        toField(level.getBricks());
        toField(level.getLadder());
        toField(level.getPipe());

        yellowGold = level.getYellowGold();
        greenGold = level.getGreenGold();
        redGold = level.getRedGold();
        pills = level.getPills();
        portals = level.getPortals();

        enemies = level.getEnemies();
        for (Enemy enemy : enemies) {
            enemy.init(this);
        }

        for (Player player : players) {
            player.newHero(this);
        }

        generatePills();
        generateGold();
        generatePortals();
    }

    private void toField(List<? extends Point> elements) {
        for (Point element : elements) {
            field[element.getX()][element.getY()] = element;
        }
    }

    @Override
    public void tick() {
        if (!level.getMapUUID().equals(mapUUID)) {
            init();
        }

        Set<Player> die = new HashSet<>();

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
            penaltySuicide(player);
            rewardMurderers(deadHero.getX(), deadHero.getY());
        }
        generatePills();
    }

    private void penaltySuicide(Player deadHero) {
        if (deadHero.getHero().isSuicide()) {
            deadHero.event(Events.SUICIDE);
        }
    }

    private void rewardMurderers(int x, int y) {
        players.stream()
                .filter(player -> player.getHero().isUnderThePill(PillType.SHADOW_PILL))
                .filter(shadow -> shadow.getHero().itsMe(x, y))
                .forEach(murderer -> murderer.event(Events.KILL_ENEMY));

    }

    private void generatePills() {
        Integer shadowPillsCount = settings
                .<Integer>getParameter("The shadow pills count")
                .getValue();

        shadowPillsCount = shadowPillsCount < 0 ? 0 : shadowPillsCount;

        if (shadowPillsCount <= pills.size()) {
            pills = pills.subList(0, shadowPillsCount);
            return;
        }
        shadowPillsCount = shadowPillsCount - pills.size();
        for (int i = 0; i < Math.abs(shadowPillsCount); i++) {
            Point pos = getFreeRandom();
            leavePill(pos.getX(), pos.getY(), PillType.SHADOW_PILL);
        }
    }

    private void generatePortals() {
        Integer portalsTicksLive = settings
                .<Integer>getParameter("Number of ticks that the portals will be active")
                .getValue();

        portalsTicksLive = portalsTicksLive < 1 ? 1 : portalsTicksLive;

        this.portalsTicksLive = portalsTicksLive;

        Integer portalsCount = settings
                .<Integer>getParameter("The portals count")
                .getValue();

        portals.clear();
        if (portalsCount > 0) {
            for (int i = 0; i < portalsCount; i++) {
                Point pos = getFreeRandom();
                leavePortal(pos.getX(), pos.getY());
            }
        }
    }

    private Set<Player> getDied() {
        Set<Player> die = new HashSet<>();

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                die.add(player);
            }
        }

        return die;
    }

    public boolean is(Point pt, Class<? extends Point> elementType) {
        return is(pt.getX(), pt.getY(), elementType);
    }

    public boolean is(int x, int y, Class<? extends Point> elementType) {
        Point at = getAt(x, y);
        if (at == null) return false;
        return at.getClass().equals(elementType);
    }

    public BoardReader reader() {
        return new BoardReader() {

            private int size = Loderunner.this.size;
            private Point[][] field = Loderunner.this.field;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(Loderunner.this.getHeroes());
                    addAll(Loderunner.this.getEnemies());
                    addAll(Loderunner.this.getYellowGold());
                    addAll(Loderunner.this.getGreenGold());
                    addAll(Loderunner.this.getRedGold());
                    addAll(Loderunner.this.getFieldElements());
                    addAll(Loderunner.this.getPills());
                    addAll(Loderunner.this.getPortals());
                }};
            }
        };
    }

    public List<Point> getFieldElements() {
        List<Point> result = new LinkedList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point el = field[x][y];
                if (el != null) {
                    result.add(el);
                }
            }
        }
        return result;
    }

    private void forAll(ElementsIterator iterator) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                iterator.it(field[x][y]);
            }
        }
    }

    private List<Player> bricksGo() {
        List<Player> die = new LinkedList<>();

        forAll(element -> {
            if (element instanceof Brick) {
                ((Brick) element).tick();
            }
        });

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                Point element = getAt(hero);
                if (!(element instanceof Brick)) continue;

                // Умер от того что кто-то просверлил стенку
                die.add(player);

                Brick brick = (Brick) element;
                Hero killer = brick.getDrilledBy();
                Player killerPlayer = getPlayer(killer);
                if (killerPlayer != null && killerPlayer != player) {
                    killerPlayer.event(Events.KILL_ENEMY);
                }
            }
        }

        return die;
    }

    public Point getAt(Point pt) {
        return getAt(pt.getX(), pt.getY());
    }

    public Point getAt(int x, int y) {
        if (x == -1 || y == -1)
            return null; // TODO это кажется только в тестах юзается, убрать бы отсюда для производительности
        return field[x][y];
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
                hero.swallowThePill(PillType.SHADOW_PILL);
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

    private Player getPlayer(Hero hero) {
        for (Player player : players) {
            if (player.getHero() == hero) {
                return player;
            }
        }
        return null;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0
                || y < 0 || y > size - 1
                || isFullBrick(x, y)
                || is(pt, Border.class)
                || (isHeroAt(x, y) && !isUnderThePillAt(x, y, PillType.SHADOW_PILL));
    }

    @Override
    public boolean tryToDrill(Hero byHero, int x, int y) {
        Point pt = pt(x, y);
        if (!isFullBrick(x, y)) {
            return false;
        }

        Point over = pt(x, y + 1);
        if (is(over, Ladder.class)
                || yellowGold.contains(over)
                || greenGold.contains(over)
                || redGold.contains(over)
                || isFullBrick(over.getX(), over.getY())
                || getHeroes().contains(over)
                || enemies.contains(over)) {
            return false;
        }

        Point el = getAt(pt);
        if (el instanceof Brick) {
            Brick brick = (Brick) el;
            brick.drill(byHero);
        }

        return true;
    }

    @Override
    public boolean isPit(int x, int y) {
        Point pt = pt(x, y - 1);

        return !(isFullBrick(pt.getX(), pt.getY())
                || is(pt, Ladder.class)
                || is(pt, Border.class)
                || getHeroes().contains(pt)
                || enemies.contains(pt));
    }

    @Override
    public boolean isFullBrick(int x, int y) {
        Point el = getAt(x, y);
        return (el instanceof Brick)
                && ((Brick) el).state(null) == Elements.BRICK;
    }

    @Override
    public Point getFreeRandom() {
        return BoardUtils.getFreeRandom(size, dice, pt -> isFree(pt));
    }

    private boolean isGround(int x, int y) {
        Point under = pt(x, y - 1);

        return is(under, Border.class)
                && is(under, Brick.class)
                && is(under, Ladder.class);
    }

    @Override
    public boolean isLadder(int x, int y) {
        return is(x, y, Ladder.class);
    }

    @Override
    public boolean isPipe(int x, int y) {
        return is(x, y, Pipe.class);
    }

    @Override
    public boolean isFree(Point pt) {
        return !(redGold.contains(pt)
                || greenGold.contains(pt)
                || yellowGold.contains(pt)
                || pills.contains(pt)
                || portals.contains(pt)
                || is(pt, Border.class)
                || is(pt, Brick.class)
                || getHeroes().contains(pt)
                || is(pt, Pipe.class)
                || is(pt, Ladder.class));
    }

    @Override
    public boolean isHeroAt(int x, int y) {
        return getHeroes().contains(pt(x, y));
    }

    @Override
    public boolean isBrick(int x, int y) {
        return is(x, y, Brick.class);
    }

    @Override
    public boolean isEnemyAt(int x, int y) {
        List<Hero> shadows = players.stream()
                .filter(player -> player.getHero().isUnderThePill(PillType.SHADOW_PILL))
                .map(Player::getHero)
                .collect(toList());
        Point point = pt(x, y);
        return enemies.contains(point) || shadows.contains(point);
    }

    @Override
    public void leaveGold(int x, int y, Class clazz) {
        if (clazz == YellowGold.class) {
            yellowGold.add(new YellowGold(x, y));
        } else if (clazz == GreenGold.class) {
            greenGold.add(new GreenGold(x,y));
        } else if (clazz == RedGold.class) {
            redGold.add(new RedGold(x,y));
        }
    }

    @Override
    public void leavePill(int x, int y, PillType pillType) {
        pills.add(new Pill(x, y, pillType));
    }

    @Override
    public void leavePortal(int x, int y) {
        portals.add(new Portal(x, y));
    }

    @Override
    public boolean isUnderThePillAt(int x, int y, PillType pillType) {
        Point pt = pt(x, y);
        return players.stream()
                .map(Player::getHero)
                .filter(hero -> hero.equals(pt))
                .anyMatch(hero -> hero.isUnderThePill(pillType));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isBorder(int x, int y) {
        return is(x, y, Border.class);
    }

    public List<YellowGold> getYellowGold() {
        return yellowGold;
    }

    public List<GreenGold> getGreenGold() {
        return greenGold;
    }

    public List<RedGold> getRedGold() {
        return redGold;
    }

    public List<Pill> getPills() {
        return pills;
    }

    public List<Portal> getPortals() {
        return portals;
    }

    @Override
    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    interface ElementsIterator {
        void it(Point element);
    }

    private void getGoldEvent(Player player, Events event, Class ptType) {
        player.event(event);
        Point pos = getFreeRandom();
        leaveGold(pos.getX(), pos.getY(), ptType);
    }

    private void generateGold()  {
        int yellowTypeGoldCount = (Integer) settings.getParameter("yellow type gold count").getValue();
        int greenTypeGoldCount = (Integer) settings.getParameter("green type gold count").getValue();
        int redTypeGoldCount = (Integer) settings.getParameter("red type gold count").getValue();
        greenTypeGoldCount = greenTypeGoldCount < 0 ? 0 : greenTypeGoldCount;
        redTypeGoldCount = redTypeGoldCount < 0 ? 0 : redTypeGoldCount;

        if (yellowTypeGoldCount >= 0 && yellowTypeGoldCount <= yellowGold.size()) {
            yellowGold = yellowGold.subList(0, yellowTypeGoldCount);
        }
        if (greenTypeGoldCount <= greenGold.size()) {
            greenGold = greenGold.subList(0, greenTypeGoldCount);
        }
        if (redTypeGoldCount <= redGold.size()) {
            redGold = redGold.subList(0, redTypeGoldCount);
        }

        yellowTypeGoldCount = yellowTypeGoldCount - yellowGold.size();
        for (int i = 0; i < Math.max(0, yellowTypeGoldCount); i++) {
            Point pos = getFreeRandom();
            yellowGold.add(new YellowGold(pos.getX(), pos.getY()));
        }

        greenTypeGoldCount = greenTypeGoldCount - greenGold.size();
        for (int i = 0; i < Math.max(0, greenTypeGoldCount); i++) {
            Point pos = getFreeRandom();
            greenGold.add(new GreenGold(pos.getX(), pos.getY()));
        }

        redTypeGoldCount = redTypeGoldCount - redGold.size();
        for (int i = 0; i < Math.max(0, redTypeGoldCount); i++) {
            Point pos = getFreeRandom();
            redGold.add(new RedGold(pos.getX(), pos.getY()));
        }
    }
}

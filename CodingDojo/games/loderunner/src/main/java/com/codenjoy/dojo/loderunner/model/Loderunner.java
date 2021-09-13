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


import com.codenjoy.dojo.games.loderunner.Element;
import com.codenjoy.dojo.loderunner.model.items.*;
import com.codenjoy.dojo.loderunner.model.items.Pill.PillType;
import com.codenjoy.dojo.loderunner.model.items.enemy.Enemy;
import com.codenjoy.dojo.loderunner.model.levels.Level;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;
import com.codenjoy.dojo.services.round.RoundPlayerHero;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.field.Generator.generate;
import static java.util.stream.Collectors.toList;

public class Loderunner extends RoundField<Player> implements Field {

    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    private int portalsTimer;
    private List<Function<Point, Point>> finder;

    public Loderunner(Dice dice, GameSettings settings) {
        super(Events.START_ROUND, Events.WIN_ROUND, Events.KILL_HERO, settings);

        this.dice = dice;
        this.settings = settings;
        this.field = new PointField();
        this.players = new LinkedList<>();

        finder = new ArrayList<>(){{
            add(pt -> getFrom(heroes().all(), pt));
            add(pt -> getFrom(enemies().all(), pt));
            add(pt -> getFrom(yellowGold().all(), pt));
            add(pt -> getFrom(greenGold().all(), pt));
            add(pt -> getFrom(redGold().all(), pt));
            add(pt -> getFrom(borders().all(), pt));
            add(pt -> getFrom(bricks().all(), pt));
            add(pt -> getFrom(ladder().all(), pt));
            add(pt -> getFrom(pills().all(), pt));
            add(pt -> getFrom(pipe().all(), pt));
            add(pt -> getFrom(portals().all(), pt));
        }};

        clearScore();
    }

    private void generateAll() {
        generatePills();
        generateGold();
        generatePortals();
        generateEnemies();
    }

    @Override
    public void clearScore() {
        settings.level().saveTo(field);
        field.init(this);

        resetPortalsTimer();
        enemies().forEach(enemy -> enemy.init(this));
        generateAll();

        super.clearScore();
    }

    @Override
    public List<Player> players() {
        return players;
    }

    @Override
    protected void cleanStuff() {
        // do nothing
    }

    @Override
    protected void tickField() {
//        if (!level.getMapUUID().equals(mapUUID)) {
//            init();
//        } TODO сделать по другому автоперезагрузку уровней

        Set<Player> die = new LinkedHashSet<>();

        heroesGo();
        die.addAll(getDied());

        enemiesGo();
        die.addAll(getDied());

        die.addAll(bricksGo());

        portalsGo();

        for (Player player : die) {
            Hero deadHero = player.getHero();
            rewardMurderers(deadHero);
        }

        generateAll();
    }

    @Override
    protected void setNewObjects() {
        // do nothing
    }

    private void rewardMurderers(Point pt) {
        heroes().stream()
                .filter(hero -> hero.under(PillType.SHADOW_PILL))
                .filter(shadow -> shadow.itsMe(pt))
                .forEach(murderer -> murderer.event(Events.KILL_ENEMY));
    }

    private void generateGold()  {
        generate(yellowGold(),
                settings, GOLD_COUNT_YELLOW,
                player -> freeRandom((Player) player),
                pt -> new YellowGold(pt));

        generate(greenGold(),
                settings, GOLD_COUNT_GREEN,
                player -> freeRandom((Player) player),
                pt -> new GreenGold(pt));

        generate(redGold(),
                settings, GOLD_COUNT_RED,
                player -> freeRandom((Player) player),
                pt -> new RedGold(pt));
    }

    private void generatePills() {
        generate(pills(),
                settings, SHADOW_PILLS_COUNT,
                player -> freeRandom((Player) player),
                pt -> new Pill(pt, PillType.SHADOW_PILL));
    }

    private void generateEnemies() {
        generate(enemies(),
                settings, ENEMIES_COUNT,
                player -> freeRandom((Player) player),
                pt -> {
                    Enemy enemy = new Enemy(pt, Direction.LEFT);
                    enemy.init(this);
                    return enemy;
                });
    }

    private void generatePortals() {
        generate(portals(), settings, PORTALS_COUNT,
                player -> freeRandom((Player) player),
                pt -> new Portal(pt));
    }

    private List<Player> getDied() {
        return players.stream()
                .filter(player -> !player.isAlive())
                .collect(toList());
    }

    public BoardReader reader() {
        return new BoardReader<Player>() {

            @Override
            public int size() {
                return Loderunner.this.size();
            }

            @Override
            public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
                processor.accept(heroes());
                processor.accept(enemies().all());
                processor.accept(yellowGold().all());
                processor.accept(greenGold().all());
                processor.accept(redGold().all());
                processor.accept(borders().all());
                processor.accept(bricks().all());
                processor.accept(ladder().all());
                processor.accept(pills().all());
                processor.accept(pipe().all());
                processor.accept(portals().all());
            }
        };
    }

    private List<Player> bricksGo() {
        List<Player> die = new LinkedList<>();

        bricks().forEach(Brick::tick);

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                List<Brick> bricks = bricks().getAt(hero);
                if (bricks.isEmpty()) continue;

                // Умер от того что кто-то просверлил стенку
                die.add(player);

                Hero killer = bricks.get(0).getDrilledBy();
                if (killer == null) continue;

                Player killerPlayer = (Player) killer.getPlayer();
                if (killerPlayer != null & killerPlayer != player) {
                    killerPlayer.event(Events.KILL_ENEMY);
                }
            }
        }

        return die;
    }

    private Optional<Brick> getBrick(Point pt) {
        return bricks().stream()
                .filter(brick -> brick.equals(pt))
                .findFirst();
    }

    public List<Point> get(Point at) {
        if (at.isOutOf(size())) {
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
            if (yellowGold().contains(hero)) {
                yellowGold().removeAt(hero);
                getGoldEvent(player, Events.GET_YELLOW_GOLD, YellowGold.class);
            } else if (greenGold().contains(hero)) {
                greenGold().removeAt(hero);
                getGoldEvent(player, Events.GET_GREEN_GOLD, GreenGold.class);
            } else if (redGold().contains(hero)) {
                redGold().removeAt(hero);
                getGoldEvent(player, Events.GET_RED_GOLD, RedGold.class);
            }

            if (pills().contains(hero)) {
                pills().removeAt(hero);
                hero.pick(PillType.SHADOW_PILL);
            }

            if (portals().contains(hero)) {
                transport(hero);
            }
        }
    }

    private void transport(PointImpl point) {
        List<Portal> portals = portals().all();
        for (int i = 0; i < portals.size(); i++) {
            if (portals.get(i).equals(point)) {
                Portal portalToMove = portals.get(i < portals.size() - 1 ? i + 1 : 0);
                point.move(portalToMove.getX(), portalToMove.getY());
                return;
            }
        }
    }

    private void enemiesGo() {
        for (Enemy enemy : enemies().copy()) {
            enemy.tick();

            if (yellowGold().contains(enemy) && !enemy.withGold()) {
                yellowGold().removeAt(enemy);
                enemy.getGold(YellowGold.class);
            } else if (greenGold().contains(enemy) && !enemy.withGold()) {
                greenGold().removeAt(enemy);
                enemy.getGold(GreenGold.class);
            } else if (redGold().contains(enemy) && !enemy.withGold()) {
                redGold().removeAt(enemy);
                enemy.getGold(RedGold.class);
            }

            if (portals().contains(enemy)) {
                transport(enemy);
            }
        }
    }

    // TODO сделать чтобы каждый портал сам тикал свое время
    private void portalsGo() {
        if (portalsTimer == 0) {
            resetPortalsTimer();
            portals().clear();
            generatePortals();
        } else {
            portalsTimer--;
        }
    }

    private void resetPortalsTimer() {
        portalsTimer = Math.max(1, settings.integer(PORTAL_TICKS));
    }

    @Override
    public boolean isBarrier(Point pt) {
          return pt.getX() > size() - 1 || pt.getX() < 0
                || pt.getY() < 0 || pt.getY() > size() - 1
                || isFullBrick(pt)
                || isBorder(pt)
                || (isHeroAt(pt) && !under(pt, PillType.SHADOW_PILL));
    }

    @Override
    public void suicide(Hero hero) {
        hero.getPlayer().event(Events.SUICIDE);
    }

    @Override
    public boolean tryToDrill(Hero byHero, Point pt) {
        if (!isFullBrick(pt)) {
            return false;
        }

        Point over = Direction.UP.change(pt);
        if (isLadder(over)
                || yellowGold().contains(over)
                || greenGold().contains(over)
                || redGold().contains(over)
                || isFullBrick(over)
                || activeHeroes().contains(over)
                || enemies().contains(over)) {
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
                || enemies().contains(under));
    }

    @Override
    public boolean isFullBrick(Point pt) {
        return bricks().getAt(pt).stream()
                .anyMatch(brick -> brick.state(null) == Element.BRICK);
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        return BoardUtils.freeRandom(size(), dice, pt -> isFree(pt));
    }

    @Override
    public boolean isLadder(Point pt) {
        return ladder().contains(pt);
    }

    @Override
    public boolean isPipe(Point pt) {
        return pipe().contains(pt);
    }

    @Override
    public boolean isFree(Point pt) {
        return get(pt).isEmpty();
    }

    @Override
    public boolean isHeroAt(Point pt) {
        return activeHeroes().contains(pt);
    }

    // TODO test
    //      может ли пройти через него охотник - да
    //      можно ли сверлить под ним - да
    //      является ли место с ним дыркой - да
    //      является ли место с ним препятствием - нет
    @Override
    public List<Hero> activeHeroes() {
        return aliveActive().stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public Accessor<Hero> heroes() {
        return field.of(Hero.class);
    }

    @Override
    public boolean isBrick(Point pt) {
        return bricks().contains(pt);
    }

    @Override
    public boolean isEnemyAt(Point pt) {
        List<Hero> shadows = heroes().stream()
                .filter(hero -> hero.under(PillType.SHADOW_PILL))
                .collect(toList());
        return enemies().contains(pt) || shadows.contains(pt);
    }

    @Override
    public void leaveGold(Point pt, Class type) {
        if (type == YellowGold.class) {
            yellowGold().add(new YellowGold(pt));
        } else if (type == GreenGold.class) {
            greenGold().add(new GreenGold(pt));
        } else if (type == RedGold.class) {
            redGold().add(new RedGold(pt));
        }
    }

    @Override
    public boolean under(Point pt, PillType pill) {
        return heroes().stream()
                .filter(hero -> hero.equals(pt))
                .anyMatch(hero -> hero.under(pill));
    }

    @Override
    public int size() {
        return field.size();
    }

    @Override
    public boolean isBorder(Point pt) {
        return borders().contains(pt);
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
        removeAloneHeroes();
    }

    @Override
    public void remove(Player player) {
        super.remove(player);
        removeAloneHeroes();
    }

    // TODO DF3D попробовать избавиться от этого метода
    private void removeAloneHeroes() {
        List<Hero> aloneHeroes = players.stream()
                .map(GamePlayer::getHero)
                .collect(toList());
        heroes().removeNotSame(aloneHeroes);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    private void getGoldEvent(Player player, Events event, Class type) {
        player.event(event);
        Optional<Point> pt = freeRandom(null);
        if (pt.isPresent()) {
            leaveGold(pt.get(), type);
        }
    }

    public Accessor<Portal> portals() {
        return field.of(Portal.class);
    }

    public Accessor<YellowGold> yellowGold() {
        return field.of(YellowGold.class);
    }

    public Accessor<GreenGold> greenGold() {
        return field.of(GreenGold.class);
    }

    public Accessor<RedGold> redGold() {
        return field.of(RedGold.class);
    }

    public Accessor<Border> borders() {
        return field.of(Border.class);
    }

    @Override
    public Accessor<Enemy> enemies() {
        return field.of(Enemy.class);
    }

    @Override
    public Accessor<Brick> bricks() {
        return field.of(Brick.class);
    }

    @Override
    public List<Hero> visibleHeroes() {
        return activeHeroes().stream()   // TODO test что охотники не гонятся за точками спауна
                .filter(Hero::isVisible)
                .collect(toList());
    }

    public Accessor<Ladder> ladder() {
        return field.of(Ladder.class);
    }

    public Accessor<Pill> pills() {
        return field.of(Pill.class);
    }

    public Accessor<Pipe> pipe() {
        return field.of(Pipe.class);
    }

    public int getPortalsTimer() {
        return portalsTimer;
    }
}

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


import com.codenjoy.dojo.bomberman.model.perks.*;
import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundFactory;
import com.codenjoy.dojo.services.round.RoundField;
import com.codenjoy.dojo.services.settings.Parameter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class Bomberman extends RoundField<Player> implements Field {

    public static final boolean ACTIVE_ALIVE = true;
    public static final boolean ALL = !ACTIVE_ALIVE;

    private final List<Player> players = new LinkedList<>();

    private final Walls walls;
    private final Parameter<Integer> size;
    private final List<Bomb> bombs = new LinkedList<>();
    private final List<Blast> blasts = new LinkedList<>();
    private final GameSettings settings;
    private final List<Wall> destroyedWalls = new LinkedList<>();
    private final List<Bomb> destroyedBombs = new LinkedList<>();
    private final Dice dice;
    private List<PerkOnBoard> perks = new LinkedList<>();

    public Bomberman(GameSettings settings) {
        super(RoundFactory.get(settings.getRoundSettings()),
                Events.START_ROUND, Events.WIN_ROUND, Events.DIED);

        this.settings = settings;

        dice = settings.getDice();
        size = settings.getBoardSize();
        walls = settings.getWalls();
        walls.init(this);
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    public GameSettings settings() {
        return settings;
    }

    @Override
    public List<PerkOnBoard> perks() {
        return perks;
    }

    public PerkOnBoard pickPerk(Point pt) {
        int index = perks.indexOf(pt);
        if (index == -1) {
            return null;
        }
        return perks.remove(index);
    }

    @Override
    public Dice dice() {
        return dice;
    }

    @Override
    public int size() {
        return size.getValue();
    }

    @Override
    public void cleanStuff() {
        removeBlasts();
    }

    @Override
    protected void setNewObjects() {
        // do nothing
    }

    @Override
    public void tickField() {
        applyAllHeroes();       // герои ходят
        meatChopperEatHeroes(); // омномном
        walls.tick();           // разрушенные стены появляются, а митчоперы водят свой холровод
        meatChopperEatHeroes(); // омномном
        disableBombRemote();    // если остались remote бомбы без хозяев, взрываем
        tactAllBombs();         // все что касается бомб и взрывов
        tactAllPerks();         // тикаем перки на поле
        tactAllHeroes();        // в том числе и перки
    }

    private void disableBombRemote() {
        for (Bomb bomb : bombs) {
            Hero owner = bomb.getOwner();
            if (!owner.isActiveAndAlive()) {
                if (bomb.isOnRemote()) {
                    bomb.activateRemote();
                    owner.getPerk(Elements.BOMB_REMOTE_CONTROL).decrease();
                }
            }
        }
    }

    private void tactAllPerks() {
        // тикаем счетчик перка на поле и если просрочка, удаляем
        perks.forEach(perk -> perk.tick());
        perks = perks.stream()
            .filter(perk -> perk.getPerk().getPickTimeout() > 0)
            .collect(toList());
    }

    private void tactAllHeroes() {
        for (Player p : players) {
            p.getHero().tick();
        }
    }

    private void applyAllHeroes() {
        for (Player player : players) {
            player.getHero().apply();
        }
    }

    private void removeBlasts() {
        blasts.clear();

        for (Wall wall : destroyedWalls) {
            if (wall instanceof DestroyWall) {
                dropPerk(wall, dice);
            }
            walls.destroy(wall);
        }

        destroyedWalls.clear();
    }

    private void meatChopperEatHeroes() {
        for (MeatChopper chopper : walls.listSubtypes(MeatChopper.class)) {
            for (Player player : players) {
                Hero hero = player.getHero();
                if (hero.isAlive() && chopper.itsMe(hero)) {
                    player.getHero().die();
                }
            }
        }
    }

    private void tactAllBombs() {
        for (Bomb bomb : bombs) {
            bomb.tick();
        }

        do {
            makeBlastsFromDestoryedBombs();

            if (settings.isBigBadaboom().getValue()) {

                // если бомбу зацепила взрывная волна и ее тоже подрываем
                for (Bomb bomb : bombs) {
                    if (blasts.contains(bomb)) {
                        bomb.boom();
                    }
                }
            }

            // и повторяем все, пока были взорванные бомбы
        } while(!destroyedBombs.isEmpty());

        // потому уже считаем скоры за разрушения
        killAllNear(blasts);

        // убираем взрывную волну над обнаженными перками, тут взрыв сделал свое дело
        List<Blast> blastsOnPerks = blasts.stream()
                .filter(blast -> perks.contains(blast))
                .collect(toList());
        blasts.removeAll(blastsOnPerks);

    }

    private void makeBlastsFromDestoryedBombs() {
        // все взрываем, чтобы было пекло
        for (Bomb bomb : destroyedBombs) {
            bombs.remove(bomb);

            List<Blast> blast = makeBlast(bomb);
            blasts.addAll(blast);
        }
        destroyedBombs.clear();
    }

    @Override
    public List<Bomb> bombs() {
        return bombs;
    }

    @Override
    public List<Bomb> bombs(Hero hero) {
        return bombs.stream()
            .filter(bomb -> bomb.itsMine(hero))
            .collect(toList());
    }

    @Override
    public List<Blast> blasts() {
        return blasts;
    }

    @Override
    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            bombs.add(bomb);
        }
    }

    @Override
    public void remove(Bomb bomb) {
        destroyedBombs.add(bomb);
    }

    @Override
    public void remove(Wall wall) {
        destroyedWalls.add(wall);
    }

    private List<Blast> makeBlast(Bomb bomb) {
        List barriers = walls.listSubtypes(Wall.class);
        barriers.addAll(heroes(ACTIVE_ALIVE));

        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, size.getValue(), bomb, bomb.getPower());   // TODO move bomb inside BoomEngine
    }

    private void killAllNear(List<Blast> blasts) {
        killHeroes(blasts);
        killPerks(blasts);
        killWallsAndChoppers(blasts);
    }

    private void killWallsAndChoppers(List<Blast> blasts) {
        // собираем все разрушаемые стенки которые уже есть в радиусе
        // надо определить кто кого чем кикнул (ызрывные волны могут пересекаться)
        List<Wall> all = walls.listSubtypes(Wall.class);
        Multimap<Hero, Wall> deathMatch = HashMultimap.create();
        for (Blast blast : blasts) {
            Hero hunter = blast.owner();
            int index = all.indexOf(blast);
            if (index != -1) {
                Wall wall = all.get(index);
                deathMatch.put(hunter, wall);
            }
        }

        // у нас есть два списка, прибитые стенки
        // и те, благодаря кому они разрушены
        Set<Wall> preys = new HashSet<>(deathMatch.values());
        Set<Hero> hunters = new HashSet<>(deathMatch.keys());

        // вначале прибиваем стенки
        preys.forEach(wall -> {
            if (wall instanceof MeatChopperHunter) {
                ((MeatChopperHunter)wall).die();
            } else {
                destroyedWalls.add(wall);
            }
        });

        // а потом все виновники получают свои ачивки
        hunters.forEach(hunter -> {
            if (!hunter.hasPlayer()) {
                return;
            }

            deathMatch.get(hunter).forEach(wall -> {
                if (wall instanceof MeatChopper) {
                    hunter.event(Events.KILL_MEAT_CHOPPER);
                } else if (wall instanceof DestroyWall) {
                    hunter.event(Events.KILL_DESTROY_WALL);
                }
            });
        });
    }

    private void killPerks(List<Blast> blasts) {
        // собираем все перки которые уже есть в радиусе
        // надо определить кто кого чем кикнул (ызрывные волны могут пересекаться)
        Multimap<Hero, PerkOnBoard> deathMatch = HashMultimap.create();
        for (Blast blast : blasts) {
            Hero hunter = blast.owner();
            int index = perks.indexOf(blast);
            if (index != -1) {
                PerkOnBoard perk = perks.get(index);
                deathMatch.put(hunter, perk);
            }
        }

        // у нас есть два списка, прибитые перки
        // и те, благодаря кому
        Set<PerkOnBoard> preys = new HashSet<>(deathMatch.values());
        Set<Hero> hunters = new HashSet<>(deathMatch.keys());

        // вначале прибиваем перки
        preys.forEach(perk -> pickPerk(perk));

        // а потом все виновники получают свои результаты )
        hunters.forEach(hunter -> {
            if (!hunter.hasPlayer()) {
                return;
            }

            deathMatch.get(hunter).forEach(perk -> {
                hunter.event(Events.DROP_PERK);

                // TODO может это делать на этапе, когда balsts развиднеется в removeBlasts
                blasts.remove(perk);
                walls.add(new MeatChopperHunter(perk, hunter));
            });
        });
    }

    private void killHeroes(List<Blast> blasts) {
        // беремся за бомберов, если у них только нет иммунитета
        // надо определить кто кого чем кикнул (ызрывные волны могут пересекаться)
        Multimap<Hero, Hero> deathMatch = HashMultimap.create();
        for (Blast blast : blasts) {
            Hero hunter = blast.owner();
            for (Player player : aliveActive()) {
                Hero prey = player.getHero();
                if (prey.itsMe(blast)) {
                    Perk immune = prey.getPerk(Elements.BOMB_IMMUNE);
                    if (immune == null) {
                        deathMatch.put(hunter, prey);
                    }
                }
            }
        }

        // у нас есть два списка, те кого прибили
        // и те, благодаря кому
        Set<Hero> preys = new HashSet<>(deathMatch.values());
        Set<Hero> hunters = new HashSet<>(deathMatch.keys());

        // вначале прибиваем жертв
        preys.forEach(hero -> {
            if (!hero.hasPlayer()) {
                return;
            }

            hero.die();
        });

        // а потом все, кто выжил получают за это очки за всех тех, кого зацепили взрывной волной
        // не стоит беспокоиться что они погибли сами - за это есть регулируемые штрафные очки
        hunters.forEach(hunter -> {
            if (!hunter.hasPlayer()) {
                return;
            }

            deathMatch.get(hunter).forEach(prey -> {
                    if (hunter != prey) {
                        hunter.event(Events.KILL_OTHER_HERO);
                    }
                }
            );
        });
    }

    private boolean dropPerk(Point pt, Dice dice) {
        Elements element = PerksSettingsWrapper.nextPerkDrop(dice);
        PerkSettings settings = PerksSettingsWrapper.getPerkSettings(element);

        switch (element) {
            case BOMB_BLAST_RADIUS_INCREASE:
                setup(pt, new BombBlastRadiusIncrease(settings.value(), settings.timeout()));
                return true;

            case BOMB_COUNT_INCREASE:
                setup(pt, new BombCountIncrease(settings.value(), settings.timeout()));
                return true;

            case BOMB_IMMUNE:
                setup(pt, new BombImmune(settings.timeout()));
                return true;

            case BOMB_REMOTE_CONTROL:
                setup(pt, new BombRemoteControl(settings.value(), settings.timeout()));
                return true;

            default:
                return false;
        }
    }

    private void setup(Point pt, Perk perk) {
        perk.setPickTimeout(PerksSettingsWrapper.getPickTimeout());
        perks.add(new PerkOnBoard(pt, perk));
    }

    private boolean existAtPlace(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Walls walls() {
        return walls;
    }

    // препятствие это все, чем может быть занята клеточка
    // но если мы для героя смотрим - он может пойти к чоперу и на перк
    @Override
    public boolean isBarrier(Point pt, boolean isForHero) {
        List<Player> players = isForHero ? aliveActive() : players();
        for (Player player : players) {
            if (player.getHero().itsMe(pt)) {
                return true;
            }
        }

        for (Bomb bomb : bombs) {
            if (bomb.itsMe(pt)) {
                return true;
            }
        }

        if (!isForHero) {     // TODO test me митчопер или стена не могут появиться на перке
            if (perks.contains(pt)) {
                return true;
            }
        }

        for (Wall wall : walls) {
            if (!wall.itsMe(pt)) {
                continue;
            }

            // TODO test me стенка или другой чопер не могут появиться на чопере
            // TODO но герой может пойти к нему на встречу
            if (isForHero && wall instanceof MeatChopper) {
                return false;
            }
            return true;
        }
        return pt.isOutOf(size());
    }

    @Override
    public List<Hero> heroes(boolean activeAliveOnly) {
        return players.stream()
                .map(Player::getHero)
                .filter(hero -> !activeAliveOnly || hero.isActiveAndAlive())
                .collect(toList());
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private final int size = Bomberman.this.size();

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> elements = new LinkedList<>();

                elements.addAll(Bomberman.this.heroes(ALL));
                Bomberman.this.walls().forEach(elements::add);
                elements.addAll(Bomberman.this.bombs());
                elements.addAll(Bomberman.this.blasts());
                elements.addAll(Bomberman.this.perks());

                return elements;
            }
        };
    }
}

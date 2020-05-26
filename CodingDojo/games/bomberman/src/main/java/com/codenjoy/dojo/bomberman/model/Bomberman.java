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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundFactory;
import com.codenjoy.dojo.services.round.RoundField;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
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
    private final List<Point> destroyedWalls = new LinkedList<>();
    private final List<Bomb> destroyedBombs = new LinkedList<>();
    private Map<Point, PerkOnBoard> perks = new HashMap<>();

    public Bomberman(GameSettings settings) {
        super(RoundFactory.get(settings.getRoundSettings()),
                Events.START_ROUND, Events.WIN_ROUND, Events.DIED);

        this.settings = settings;

        size = settings.getBoardSize();
        walls = settings.getWalls(this);  // TODO как-то красивее сделать
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    public GameSettings settings() {
        return settings;
    }

    public List<PerkOnBoard> perks() {
        return new ArrayList<>(perks.values());
    }

    public PerkOnBoard pickPerk(Point pt) {
        PerkOnBoard perk = perks.get(pt);

        if (perk != null) {
            perks.remove(pt);
        }

        return perk;
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
        tactAllBombermans();
        meatChopperEatBombermans();
        walls.tick();
        meatChopperEatBombermans();
        tactAllBombs();
        tactAllPerks();
        tactAllHeroes();
    }

    private void tactAllPerks() {
        List<Blast> blastsWithoutPerks = blasts.stream().filter(blast -> !perks.containsKey(blast)).collect(toList());
        blasts.clear();
        blasts.addAll(blastsWithoutPerks);

        Map<Point, PerkOnBoard> alivePerks = new HashMap<>();

        perks.forEach((point, perkOnBoard) -> {
            if (perkOnBoard.getPerk().getPickTimeout() > 0) {
                alivePerks.put(point, perkOnBoard);
            }
        });

        this.perks = alivePerks;
    }

    private void tactAllHeroes() {
        for (Player p : players) {
            p.getHero().tick();
        }
    }

    private void tactAllBombermans() {
        for (Player player : players) {
            player.getHero().apply();
        }
    }

    private void removeBlasts() {
        blasts.clear();
        for (Point pt : destroyedWalls) {
            walls.destroy(pt);
        }
        destroyedWalls.clear();
    }

    private void wallDestroyed(Wall wall, Blast blast) {
        for (Player player : players) {
            if (blast.itsMine(player.getHero())) {
                if (wall instanceof MeatChopper) {
                    player.event(Events.KILL_MEAT_CHOPPER);
                } else if (wall instanceof DestroyWall) {
                    player.event(Events.KILL_DESTROY_WALL);
                } else if (wall instanceof PerkOnBoard) {
                    player.event(Events.DROP_PERK); // TODO test me
                }
            }
        }
    }

    private void meatChopperEatBombermans() {
        for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
            for (Player player : players) {
                Hero bomberman = player.getHero();
                if (bomberman.isAlive() && chopper.itsMe(bomberman)) {
                    player.getHero().die();
                }
            }
        }
    }

    private void tactAllBombs() {
        for (Bomb bomb : bombs) {
            bomb.tick();
        }

        for (Bomb bomb : destroyedBombs) {
            bombs.remove(bomb);

            List<Blast> blast = makeBlast(bomb);
            killAllNear(blast);
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
    public void removeBomb(Bomb bomb) {
        destroyedBombs.add(bomb);
    }

    private List<Blast> makeBlast(Bomb bomb) {
        List barriers = walls.subList(Wall.class);
        barriers.addAll(heroes(ACTIVE_ALIVE));

        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, size.getValue(), bomb, bomb.getPower());   // TODO move bomb inside BoomEngine
    }

    private void killAllNear(List<Blast> blasts) {
        for (Blast blast : blasts) {
            if (walls.itsMe(blast)) {
                if (dropPerk(blast)) {
                    walls.destroy(blast);
                } else {
                    destroyedWalls.add(blast);
                }
                Wall wall = walls.get(blast);
                wallDestroyed(wall, blast);
            }
        }
        for (Blast blast : blasts) {
            for (Player dead : aliveActive()) {
                if (dead.getHero().itsMe(blast)) {
                    Perk bombImmunePerk = dead.getHero().getPerk(Elements.BOMB_IMMUNE);

                    if (bombImmunePerk == null) {
                        dead.getHero().die();
                    }

                    for (Player bombOwner : players) {
                        if (dead != bombOwner && blast.itsMine(bombOwner.getHero())) {
                            bombOwner.event(Events.KILL_OTHER_HERO);
                        }
                    }
                }
            }
        }
    }

    // need to refactor this method ... it became too dirty
    @Deprecated
    private boolean dropPerk(Blast blast) {
        Player bombOwner = getBombOwner(blast);
        boolean result = false;
        if (bombOwner != null) {
            Elements perkElement = PerksSettingsWrapper.nextPerkDrop(bombOwner.getHero().getDice());
            PerkSettings ps = PerksSettingsWrapper.getPerkSettings(perkElement);
            switch (perkElement) {
                case BOMB_BLAST_RADIUS_INCREASE:
                    BombBlastRadiusIncrease bbri = new BombBlastRadiusIncrease(ps.getValue(), ps.getTimeout());
                    bbri.setPickTimeout(PerksSettingsWrapper.getPickTimeout());
                    perks.put(blast, new PerkOnBoard(blast.getX(), blast.getY(), bbri));
                    result = true;
                    break;
                case BOMB_COUNT_INCREASE:
                    BombCountIncrease bci = new BombCountIncrease(ps.getValue(), ps.getTimeout());
                    bci.setPickTimeout(PerksSettingsWrapper.getPickTimeout());
                    perks.put(blast, new PerkOnBoard(blast.getX(), blast.getY(), bci));
                    result = true;
                    break;
                case BOMB_IMMUNE:
                    BombImmune bi = new BombImmune(ps.getTimeout());
                    bi.setPickTimeout(PerksSettingsWrapper.getPickTimeout());
                    perks.put(blast, new PerkOnBoard(blast.getX(), blast.getY(), bi));
                    result = true;
                    break;
                case BOMB_REMOTE_CONTROL:
                    BombRemoteControl brc = new BombRemoteControl(ps.getTimeout());
                    brc.setPickTimeout(PerksSettingsWrapper.getPickTimeout());
                    perks.put(blast, new PerkOnBoard(blast.getX(), blast.getY(), brc));
                    result = true;
                    break;
                default:
            }
        }

        return result;
    }

    private Player getBombOwner(Blast blast) {
        for (Player bombOwner : players) {
            if (blast.itsMine(bombOwner.getHero())) {
                return bombOwner;
            }
        }
        return null;
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
        return new WallsImpl(walls);
    }

    @Override
    public boolean isBarrier(Point pt, boolean withMeatChopper) {
        for (Player player : aliveActive()) {
            if (player.getHero().itsMe(pt)) {
                return true;
            }
        }
        for (Bomb bomb : bombs) {
            if (bomb.itsMe(pt)) {
                return true;
            }
        }
        for (Wall wall : walls) {
            if (wall instanceof MeatChopper && !withMeatChopper) {
                continue;
            }
            if (wall.itsMe(pt)) {
                return true;
            }
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

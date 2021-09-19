package com.codenjoy.dojo.clifford.model;

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


import com.codenjoy.dojo.games.clifford.Element;
import com.codenjoy.dojo.clifford.model.items.*;
import com.codenjoy.dojo.clifford.model.items.Potion.PotionType;
import com.codenjoy.dojo.clifford.model.items.robber.Robber;
import com.codenjoy.dojo.clifford.services.Events;
import com.codenjoy.dojo.clifford.services.GameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;

import java.util.*;

import static com.codenjoy.dojo.clifford.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.field.Generator.generate;
import static java.util.stream.Collectors.toList;

public class DetectiveClifford extends RoundField<Player> implements Field {

    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    private int backwaysTimer;

    public DetectiveClifford(Dice dice, GameSettings settings) {
        super(Events.START_ROUND, Events.WIN_ROUND, Events.HERO_DIE, settings);
        this.dice = dice;
        this.settings = settings;
        this.field = new PointField();
        this.players = new LinkedList<>();

        clearScore();
    }

    private void generateAll() {
        generatePotions();
        generateClue();
        generateBackways();
        generateRobbers();
    }

    @Override
    public void clearScore() {
        settings.level().saveTo(field);
        field.init(this);

        resetBackwaysTimer();
        robbers().forEach(robber -> robber.init(this));
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

        robbersGo();
        die.addAll(getDied());

        die.addAll(bricksGo());

        backwaysGo();

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

    private void rewardMurderers(Hero deadHero) {
        int deadHeroTeam = deadHero.getPlayer().getTeamId();
        heroes().stream()
                .filter(hero -> hero.under(PotionType.MASK_POTION))
                .filter(mask -> mask.itsMe(deadHero))
                .forEach(murderer -> {
                    int murderTeam = murderer.getPlayer().getTeamId();
                    if (murderTeam == deadHeroTeam) {
                        murderer.event(Events.KILL_HERO);
                    } else {
                        murderer.event(Events.KILL_ENEMY);
                    }
                });
    }

    private void generateClue()  {
        generate(knifeClue(),
                settings, CLUE_COUNT_KNIFE,
                player -> freeRandom((Player) player),
                pt -> new ClueKnife(pt));

        generate(gloveClue(),
                settings, CLUE_COUNT_GLOVE,
                player -> freeRandom((Player) player),
                pt -> new ClueGlove(pt));

        generate(ringClue(),
                settings, CLUE_COUNT_RING,
                player -> freeRandom((Player) player),
                pt -> new ClueRing(pt));
    }

    private void generatePotions() {
        generate(potions(),
                settings, MASK_POTIONS_COUNT,
                player -> freeRandom((Player) player),
                pt -> new Potion(pt, PotionType.MASK_POTION));
    }

    private void generateRobbers() {
        generate(robbers(),
                settings, ROBBERS_COUNT,
                player -> freeRandom((Player) player),
                pt -> {
                    Robber robber = new Robber(pt, Direction.LEFT);
                    robber.init(this);
                    return robber;
                });
    }

    private void generateBackways() {
        generate(backways(), settings, BACKWAYS_COUNT,
                player -> freeRandom((Player) player),
                pt -> new Backway(pt));
    }

    private List<Player> getDied() {
        return players.stream()
                .filter(player -> !player.isAlive())
                .collect(toList());
    }

    public BoardReader reader() {
        return field.reader(
                Hero.class,
                Robber.class,
                ClueKnife.class,
                ClueGlove.class,
                ClueRing.class,
                Border.class,
                Brick.class,
                Ladder.class,
                Potion.class,
                Pipe.class,
                Backway.class);
    }

    private List<Player> bricksGo() {
        List<Player> die = new LinkedList<>();

        bricks().forEach(Brick::tick);

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                List<Brick> bricks = bricks().getAt(hero);
                if (bricks.isEmpty()) continue;

                // Умер от того что кто-то прострелил стенку
                die.add(player);

                Hero killer = bricks.get(0).getCrackedBy();
                if (killer == null) continue;

                Player killerPlayer = (Player) killer.getPlayer();
                if (killerPlayer != null & killerPlayer != player) {
                    if (killerPlayer.getTeamId() == player.getTeamId()) {
                        killerPlayer.event(Events.KILL_HERO);
                    } else {
                        killerPlayer.event(Events.KILL_ENEMY);
                    }
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

    private void heroesGo() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();
            if (knifeClue().contains(hero)) {
                knifeClue().removeAt(hero);
                getClueEvent(player, Events.GET_KNIFE_CLUE, ClueKnife.class);
            } else if (gloveClue().contains(hero)) {
                gloveClue().removeAt(hero);
                getClueEvent(player, Events.GET_GLOVE_CLUE, ClueGlove.class);
            } else if (ringClue().contains(hero)) {
                ringClue().removeAt(hero);
                getClueEvent(player, Events.GET_RING_CLUE, ClueRing.class);
            }

            if (potions().contains(hero)) {
                potions().removeAt(hero);
                hero.pick(PotionType.MASK_POTION);
            }

            if (backways().contains(hero)) {
                transport(hero);
            }
        }
    }

    private void transport(PointImpl point) {
        List<Backway> backways = backways().all();
        for (int i = 0; i < backways.size(); i++) {
            if (backways.get(i).equals(point)) {
                Backway backwayToMove = backways.get(i < backways.size() - 1 ? i + 1 : 0);
                point.move(backwayToMove.getX(), backwayToMove.getY());
                return;
            }
        }
    }

    private void robbersGo() {
        for (Robber robber : robbers().copy()) {
            robber.tick();

            if (knifeClue().contains(robber) && !robber.withClue()) {
                knifeClue().removeAt(robber);
                robber.getClue(ClueKnife.class);
            } else if (gloveClue().contains(robber) && !robber.withClue()) {
                gloveClue().removeAt(robber);
                robber.getClue(ClueGlove.class);
            } else if (ringClue().contains(robber) && !robber.withClue()) {
                ringClue().removeAt(robber);
                robber.getClue(ClueRing.class);
            }

            if (backways().contains(robber)) {
                transport(robber);
            }
        }
    }

    // TODO сделать чтобы каждый черный ход сам тикал свое время
    private void backwaysGo() {
        if (backwaysTimer == 0) {
            resetBackwaysTimer();
            backways().clear();
            generateBackways();
        } else {
            backwaysTimer--;
        }
    }

    private void resetBackwaysTimer() {
        backwaysTimer = Math.max(1, settings.integer(BACKWAY_TICKS));
    }

    @Override
    public boolean isBarrier(Point pt) {
          return pt.getX() > size() - 1 || pt.getX() < 0
                || pt.getY() < 0 || pt.getY() > size() - 1
                || isFullBrick(pt)
                || isBorder(pt)
                || (isHeroAt(pt) && !under(pt, PotionType.MASK_POTION));
    }

    @Override
    public void suicide(Hero hero) {
        hero.getPlayer().event(Events.SUICIDE);
    }

    @Override
    public boolean tryToCrack(Hero byHero, Point pt) {
        if (!isFullBrick(pt)) {
            return false;
        }

        Point over = Direction.UP.change(pt);
        if (isLadder(over)
                || knifeClue().contains(over)
                || gloveClue().contains(over)
                || ringClue().contains(over)
                || isFullBrick(over)
                || activeHeroes().contains(over)
                || robbers().contains(over)) {
            return false;
        }

        Optional<Brick> brick = getBrick(pt);
        if (brick.isPresent()) {
            brick.get().crack(byHero);
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
                || robbers().contains(under));
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
        return field.get(pt).isEmpty();
    }

    @Override
    public boolean isHeroAt(Point pt) {
        return activeHeroes().contains(pt);
    }

    // TODO test
    //      может ли пройти через него вор - да
    //      можно ли простреливать под ним - да
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
    public boolean isRobberAt(Point pt) {
        List<Hero> masks = heroes().stream()
                .filter(hero -> hero.under(PotionType.MASK_POTION))
                .collect(toList());
        return robbers().contains(pt) || masks.contains(pt);
    }

    @Override
    public void leaveClue(Point pt, Class type) {
        if (type == ClueKnife.class) {
            knifeClue().add(new ClueKnife(pt));
        } else if (type == ClueGlove.class) {
            gloveClue().add(new ClueGlove(pt));
        } else if (type == ClueRing.class) {
            ringClue().add(new ClueRing(pt));
        }
    }

    @Override
    public boolean under(Point pt, PotionType potion) {
        return heroes().stream()
                .filter(hero -> hero.equals(pt))
                .anyMatch(hero -> hero.under(potion));
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

    private void getClueEvent(Player player, Events event, Class type) {
        player.event(event);
        Optional<Point> pt = freeRandom(null);
        if (pt.isPresent()) {
            leaveClue(pt.get(), type);
        }
    }

    public Accessor<Backway> backways() {
        return field.of(Backway.class);
    }

    public Accessor<ClueKnife> knifeClue() {
        return field.of(ClueKnife.class);
    }

    public Accessor<ClueGlove> gloveClue() {
        return field.of(ClueGlove.class);
    }

    public Accessor<ClueRing> ringClue() {
        return field.of(ClueRing.class);
    }

    public Accessor<Border> borders() {
        return field.of(Border.class);
    }

    @Override
    public Accessor<Robber> robbers() {
        return field.of(Robber.class);
    }

    @Override
    public Accessor<Brick> bricks() {
        return field.of(Brick.class);
    }

    @Override
    public List<Hero> visibleHeroes() {
        return activeHeroes().stream()   // TODO test что воры не гонятся за точками спауна
                .filter(Hero::isVisible)
                .collect(toList());
    }

    public Accessor<Ladder> ladder() {
        return field.of(Ladder.class);
    }

    public Accessor<Potion> potions() {
        return field.of(Potion.class);
    }

    public Accessor<Pipe> pipe() {
        return field.of(Pipe.class);
    }

    public int getBackwaysTimer() {
        return backwaysTimer;
    }
}

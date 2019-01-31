package com.codenjoy.dojo.hex.model;

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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class Hex implements Field {

    private List<Wall> walls;
    private List<Player> players;

    private int size;
    private Dice dice;

    public Hex(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        size = level.getSize();
        players = new LinkedList<>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            player.tick();
        }

        if (isGameOver()) return;

        List<Hero> newHeroes = new LinkedList<>();
        for (Player player : players) {
            if (player.getNewHero() != null) {
                newHeroes.add(player.getNewHero());
            }
        }

        List<Hero> annigilateHeroes = new LinkedList<>();
        List<Hero> removedHeroes = new LinkedList<>();
        for (int index = 0; index < newHeroes.size(); index++) {
            for (int jndex = index; jndex < newHeroes.size(); jndex++) {
                if (index == jndex) continue;

                Hero hero1 = newHeroes.get(index);
                Hero hero2 = newHeroes.get(jndex);
                if (hero1.equals(hero2)) {
                    walls.add(new Wall(hero1));
                    removedHeroes.add(hero1);
                    removedHeroes.add(hero2);
                } else if (isNear(hero1, hero2)) {
                    annigilateHeroes.add(hero1);
                    annigilateHeroes.add(hero2);
                }
            }
        }

        for (Player player : players) {
            for (Hero hero : removedHeroes) {
                player.remove(hero);
            }
        }

        Map<Player, List<Hero>> transitions = new HashMap<>();

        for (Player player : players) {
            Hero newHero = player.getNewHero();
            if (annigilateHeroes.contains(newHero)) continue;
            if (newHero == null) continue;

            transitions.put(player, new LinkedList<>());

            for (Player otherPlayer : players) {
                if (player == otherPlayer) continue;

                for (Hero otherHero : otherPlayer.getHeroes()) {
                    if (isNear(newHero, otherHero)) {
                        transitions.get(player).add(otherHero);

                        otherPlayer.getHeroes().remove(otherHero);
                        otherPlayer.loose(1);
                    }
                }
            }
        }

        for (Map.Entry<Player, List<Hero>> entry : transitions.entrySet()) {
            Player player = entry.getKey();

            for (Hero hero : entry.getValue()) {
                player.getHeroes().add(hero, player);
                player.win(1);
            }
        }

        for (Player player : players) {
            player.applyNew();
        }

        for (Player player : players) {
            player.fireEvents();
        }
    }

    private boolean isGameOver() {
        boolean result = false;
        for (Player player : players) {
            result |= !player.isAlive();
        }

        result |= noMoreSpace();

        if (result) {
            for (Player player : players) {
                player.die();
            }
        }
        return result;
    }

    private boolean noMoreSpace() {
        List<Hero> heroes = getHeroes();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = pt(x, y);
                if (!heroes.contains(pt) && !walls.contains(pt)) return false;
            }
        }

        return true;
    }

    private boolean isNear(Hero hero1, Hero hero2) {
        return (Math.abs(hero1.getX() - hero2.getX()) <= 1) &&
                ((Math.abs(hero1.getY() - hero2.getY()) <= 1));
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);

        List<Hero> heroes = getHeroes();
        for (Player player : players) {
            heroes.remove(player.getNewHero());
        }
        return x > size - 1 || x < 0
                || y < 0 || y > size - 1
                || walls.contains(pt)
                || heroes.contains(pt);
    }

    @Override
    public Point getFreeRandom() {
        return BoardUtils.getFreeRandom(size, dice, pt -> isFree(pt));
    }

    @Override
    public boolean isFree(Point pt) {
        return !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public void addHero(int x, int y, Hero hero) {
        Hero newHero = new Hero(x, y, hero.getElement());
        newHero.init(this);
        addHeroToOwner(hero, newHero);
    }

    private void addHeroToOwner(Hero hero, Hero newHero) {
        for (Player player : players) {
            if (player.itsMine(hero)) {
                player.addHero(newHero);
            }
        }
    }

    @Override
    public void jumpHero(int x, int y, Hero hero) {
        hero.move(x, y);
        addHeroToOwner(hero, hero);
    }

    @Override
    public Hero getHero(int x, int y) {
        return getHeroes().stream()
                .filter(it -> it.equals(pt(x, y)))
                .findFirst()
                .orElse(null);
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .flatMap(player -> player.getHeroes().stream())
                .collect(toList());
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            player.setElement(getNextElement());
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public Elements getNextElement() {
        List<Elements> busy = players.stream()
                .map(Player::getElement)
                .collect(toList());

        List<Elements> free = Elements.heroesElements().stream()
                .filter(el -> !busy.contains(el))
                .collect(toList());

        if (free.isEmpty()) {
            return Elements.HERO11; // TODO надо много героев наплодить либо запретить регистрацию, если привысили их число
        }

        return free.get(0);
    }

    public List<Player> getPlayers() {
        return players;
    }


    public BoardReader reader() {
        return new BoardReader() {
            private int size = Hex.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(Hex.this.getHeroes());
                    addAll(Hex.this.getWalls());
                }};
            }
        };
    }
}

package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.hex.services.HexEvents;
import com.codenjoy.dojo.services.*;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Hex implements Tickable, Field {

    private final List<Point> walls;
    private List<Player> players;

    private final int size;
    private Dice dice;

    public Hex(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        size = level.getSize();
        players = new LinkedList<Player>();
    }

    @Override
    public void tick() {
        for (Player player : players) {
            player.tick();
        }

        if (isGameOver()) return;

        List<Hero> newHeroes = new LinkedList<Hero>();
        for (Player player : players) {
            if (player.newHero != null) {
                newHeroes.add(player.newHero);
            }
        }

        List<Hero> annigilateHeroes = new LinkedList<Hero>();
        List<Hero> removedHeroes = new LinkedList<Hero>();
        for (int index = 0; index < newHeroes.size(); index++) {
            for (int jndex = index; jndex < newHeroes.size(); jndex++) {
                if (index == jndex) continue;

                Hero hero1 = newHeroes.get(index);
                Hero hero2 = newHeroes.get(jndex);
                if (hero1.equals(hero2)) {
                    walls.add(hero1);
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

        Map<Player, List<Hero>> transitions = new HashMap<Player, List<Hero>>();

        for (Player player : players) {
            Hero newHero = player.newHero;
            if (annigilateHeroes.contains(newHero)) continue;
            if (newHero == null) continue;

            transitions.put(player, new LinkedList<Hero>());

            for (Player otherPlayer : players) {
                if (player == otherPlayer) continue;

                List<Hero> otherHeroes = new LinkedList<Hero>(otherPlayer.getHeroes());
                for (Hero otherHero : otherHeroes) {
                    if (isNear(newHero, otherHero)) {
                        transitions.get(player).add(otherHero);
                        otherPlayer.getHeroes().remove(otherHero);
                        otherPlayer.event(HexEvents.LOOSE);
                    }
                }
            }
        }

        for (Map.Entry<Player, List<Hero>> entry : transitions.entrySet()) {
            Player player = entry.getKey();
            for (Hero hero : entry.getValue()) {
                player.getHeroes().add(hero);
                player.event(HexEvents.WIN);
            }
        }

        for (Player player : players) {
            player.applyNew();
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

    public int getSize() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);

        List<Hero> heroes = getHeroes();
        for (Player player : players) {
            heroes.remove(player.newHero);
        }
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || heroes.contains(pt);
    }

    @Override
    public Point getFreeRandom() { // TODO найти место чтобы вокруг было свободно
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (!isFree(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return pt(0, 0);
        }

        return pt(rndX, rndY);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = pt(x, y);

        return !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public void addHero(int newX, int newY, Hero hero) {
        Hero newHero = new Hero(pt(newX, newY));
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
    public void jumpHero(int newX, int newY, Hero hero) {
        hero.move(newX, newY);
        addHeroToOwner(hero, hero);
    }

    @Override
    public Hero getHero(int x, int y) {
        List<Hero> heroes = getHeroes();
        int index = heroes.indexOf(pt(x, y));
        if (index != -1) {
            return heroes.get(index);
        }
        return null; // TODO
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<Hero>(players.size());
        for (Player player : players) {
            result.addAll(player.getHeroes());
        }
        return result;
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero();
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Point> getWalls() {
        return walls;
    }
}

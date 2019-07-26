package com.codenjoy.dojo.crossyroad.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.crossyroad.services.Events;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Crossyroad implements Field {

    public static final int MAX_CAR_NUMBER = 10;
    public static final int NEW_APPEAR_PERIOD = 3;
    private final CarGenerator carGenerator;
    private Level level;
    private List<Player> players;
    private List<Stone> stones;
    private boolean isNewStone = true;
    private Dice dice;
    private int countStone = 0;
    private List<Car> cars;
    private int tickCounter;

    private final int size;
    private List<Wall> walls;

    public Crossyroad(Dice dice, Level level) {
        this.dice = dice;
        this.level = level;
        size = level.getSize();
        players = new LinkedList<>();
        stones = new LinkedList<>();
        carGenerator = new CarGenerator(dice, size, MAX_CAR_NUMBER);
    }

    @Override
    public void tick() {
        tickCounter++;
        createStone();
        removeStoneOutOfBoard();
        cars.addAll(carGenerator.generateRandomPlatforms(getStones(), getCars()));
        // перемещение героя
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
            Direction directionHero = hero.getDirection();
            if (directionHero==Direction.UP){
                for (Car car : cars) {
                    car.down();
                }
                for (Stone stone : stones) {
                    stone.down();
                }
            }
        }
        // перемещение машин
        for (Car car : cars) {
            car.tick();
        }
        // добавление камней
        for (Stone stone : stones) {
            stone.tick();
        }
        // убираем машины, вышедшие за экран
        for (Car car : cars.toArray(new Car[0])) {
            if (car.getX() < 1 && car.getDirection().equals(Direction.LEFT)){
                car.move(size-2, car.getY());
            }
            if (car.getX() > size-2 && car.getDirection().equals(Direction.RIGHT)){
                car.move(1, car.getY());
            }
            if (car.getY() < 0) cars.remove(car);
            /*if (car.isOutOf(size)) {
                if (car.getDirection().equals(Direction.LEFT)){
                    car.move(size-1, car.getY());
                }
                else{
                    car.move(1, car.getY());
                }
                if (car.getY() < 0) cars.remove(car);
            }*/
        }
        // реализация механики прыжка(из другой игры)
       /* for (Player player : players) {
            Hero hero = player.getHero();

            // moving hero
            if (hero.getStatus() == HeroStatus.FALLING) {
                //Very podozritelno
                if (!platforms.contains(pt(hero.getX() + 1, hero.getY() - 1))) {
                    hero.falls();
                }
            } else if (hero.getStatus() == HeroStatus.JUMPING) {
                hero.jumps();
            }

            // status changing
            boolean isPlatformUnderHero = platforms.contains(pt(hero.getX(), hero.getY() - 1));
            boolean isPlatformUnderHeroOnNextStep = platforms.contains(pt(hero.getX() + 1, hero.getY() - 1));
            if (isPlatformUnderHero || isPlatformUnderHeroOnNextStep) {
                hero.setStatus(HeroStatus.IDLE);
            } else {
                if (hero.getStatus() == HeroStatus.IDLE) {
                    hero.setAlreadyJumped(1);
                }
                hero.setStatus(HeroStatus.FALLING);
            }

            // kill hero in wall(spikes)
            if (walls.contains(hero)) {
                loseGame(player, hero);
            }
        }
        */
        // если игрок попадает под машину или камень, выходит за границы поля, то умирает
        for (Player player : players) {
            Hero hero = player.getHero();
            for (Car p : cars){
                if (hero.getX() == p.getX() && hero.getY() == p.getY())
                    loseGame(player, hero);
            }
            for (Stone s : stones){
                if (hero.getX() == s.getX() && hero.getY() == s.getY())
                    loseGame(player, hero);
            }
            if (player.getHero().getX() < 1 || player.getHero().getX() >= size){
                loseGame(player, player.getHero());
            }
        }
        //начисление очков за движение вперед
        for (Player player : players) {
            Hero hero = player.getHero();
            //player.event(Events.STILL_ALIVE);
            if(Direction.UP.equals(hero.getDirection())){
                player.event(Events.GO_UP);
            }
        }
    }

    // создание камней;
    private void createStone() {
       boolean isHeroMoving = false;
       for (Player p : players){
           if (p.getHero().getDirection().equals(Direction.UP)){
               isHeroMoving = true;
               break;
           }
       }
       if(isHeroMoving){countStone++;}
       if (countStone == NEW_APPEAR_PERIOD) {
           int x = dice.next(size-1)+1;
           if (x > 0) {
               addStone(x);
           }
           countStone = 0;
        }
    }


    private void loseGame(Player player, Hero hero) {
        player.event(Events.LOSE);
        carGenerator.setPreviousY(2);
        hero.dies();
    }

    public int size() {
        return size;
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);

        walls = level.getWalls();
        cars = level.getPlatforms();
        stones = level.getStones();
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Crossyroad.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(getHeroes());
                    if (walls != null) addAll(walls);
                    if (cars != null) addAll(cars);
                    addAll(stones);
                }};
            }
        };
    }

    public List<Hero> getHeroes() {
        List<Hero> heroes = new LinkedList<Hero>();
        for (Player player : players) {
            heroes.add(player.getHero());
        }
        return heroes;
    }

    private void removeStoneOutOfBoard() {
        for (Iterator<Stone> stone = stones.iterator(); stone.hasNext();){
            if (stone.next().isOutOf(size)){
                stone.remove();
            }
        }
    }
    public void addStone(int x) {
        stones.add(new Stone(x,size-1));
               isNewStone = false;// где это используется...
    }

    List<Stone> getStones() {
        return stones;
    }// шобы было

    List<Car> getCars() {
        return cars;
    }// шобы было

    public int getTickCounter() {
        return tickCounter;
    }
}

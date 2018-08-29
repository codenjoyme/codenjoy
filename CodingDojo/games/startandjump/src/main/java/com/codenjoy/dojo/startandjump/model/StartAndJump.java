package com.codenjoy.dojo.startandjump.model;

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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.startandjump.services.Events;
import com.codenjoy.dojo.startandjump.services.HeroStatus;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class StartAndJump implements Field {

    public static final int MAX_PLATFORM_LENGTH = 3;
    private final PlatformGenerator platformGenerator;
    private Level level;
    private List<Player> players;
    private List<Platform> platforms;
    private int tickCounter;

    private final int size;
    private List<Wall> walls;

    public StartAndJump(Dice dice, Level level) {
        this.level = level;
        size = level.getSize();
        players = new LinkedList<>();
        platformGenerator = new PlatformGenerator(dice, size, MAX_PLATFORM_LENGTH);
    }

    @Override
    public void tick() {
        tickCounter++;
        platforms.addAll(platformGenerator.generateRandomPlatforms());

        // set player JUMPING status and/or jumpCounter since last IDLE
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
        }

        // move world
        for (Platform platform : platforms) {
            platform.tick();
        }

        // remove platforms that out of the world
        for (Platform platform : platforms.toArray(new Platform[0])) {
            if (platform.isOutOf(size)) {
                platforms.remove(platform);
            }
        }

        // moving hero and status changing
        for (Player player : players) {
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

        // kill hero inside platforms
        for (Player player : players) {
            Hero hero = player.getHero();
            if (platforms.contains(hero)) {
                loseGame(player, hero);
            }
        }
        for (Player player : players) {
            player.event(Events.STILL_ALIVE);
        }
    }

    private void loseGame(Player player, Hero hero) {
        player.event(Events.LOSE);
        platformGenerator.setPreviousY(2);
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
        platforms = level.getPlatforms();
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = StartAndJump.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(getHeroes());
                    if (walls != null) addAll(walls);
                    if (platforms != null) addAll(platforms);
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

    List<Platform> getPlatforms() {
        return platforms;
    }

    public int getTickCounter() {
        return tickCounter;
    }
}

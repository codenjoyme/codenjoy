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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class AITest {

    private AI ai;
    private Loderunner loderunner;
    private LevelImpl level;

    @Test
    public void shouldGeneratePossibleWays1() {
        assertP("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ◄ ☼" +
                "☼###☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,2]=[LEFT, RIGHT], [2,3]=[DOWN], " +
                        "[3,2]=[LEFT], [3,3]=[DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withLadder() {
        assertP("☼☼☼☼☼" +
                "☼  H☼" +
                "☼ ◄H☼" +
                "☼###☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,2]=[LEFT, RIGHT], [2,3]=[DOWN], " +
                        "[3,2]=[LEFT, UP], [3,3]=[LEFT, DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withPipe() {
        assertP("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄~~☼" +
                "☼#  ☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,1]=[RIGHT], [2,2]=[LEFT, RIGHT, DOWN], [2,3]=[DOWN], " +
                        "[3,1]=[LEFT], [3,2]=[LEFT, DOWN], [3,3]=[DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withLadder2() {
        assertP("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄HH☼" +
                "☼#  ☼" +
                "☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,3]=[DOWN], " +
                        "[2,1]=[RIGHT], [2,2]=[LEFT, RIGHT, UP, DOWN], [2,3]=[LEFT, RIGHT, DOWN], " +
                        "[3,1]=[LEFT], [3,2]=[LEFT, UP, DOWN], [3,3]=[LEFT, DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_withoutBorders() {
        assertP("   H" +
                "~~ H" +
                "H  H" +
                "H  H",

                "{[0,0]=[RIGHT, UP], [0,1]=[RIGHT, UP, DOWN], [0,2]=[RIGHT, DOWN], [0,3]=[DOWN], " +
                "[1,0]=[LEFT, RIGHT], [1,1]=[DOWN], [1,2]=[LEFT, RIGHT, DOWN], [1,3]=[DOWN], " +
                "[2,0]=[LEFT, RIGHT], [2,1]=[DOWN], [2,2]=[DOWN], [2,3]=[DOWN], " +
                "[3,0]=[LEFT, UP], [3,1]=[LEFT, UP, DOWN], [3,2]=[LEFT, UP, DOWN], [3,3]=[LEFT, DOWN]}");
    }

    @Test
    public void shouldGeneratePossibleWays_whenFromLadderOnPipe() {
        assertP("☼☼☼☼☼" +
                "☼   ☼" +
                "☼~~H☼" +
                "☼  H☼" +
                "☼☼☼☼☼",

                "{[1,1]=[RIGHT], [1,2]=[RIGHT, DOWN], [1,3]=[DOWN], " +
                        "[2,1]=[LEFT, RIGHT], [2,2]=[LEFT, RIGHT, DOWN], [2,3]=[DOWN], " +
                        "[3,1]=[LEFT, UP], [3,2]=[LEFT, UP, DOWN], [3,3]=[LEFT, DOWN]}");

    }

    @Test
    public void shouldGeneratePossibleWays_whenFromLadderOnPipe2() {
        assertP("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼◄~~~ ☼" +
                "☼#  #H☼" +
                "☼    H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,4]=[RIGHT], [1,5]=[DOWN], " +
                        "[2,2]=[LEFT, RIGHT], [2,3]=[DOWN], [2,4]=[LEFT, RIGHT, DOWN], [2,5]=[DOWN], " +
                        "[3,2]=[LEFT, RIGHT], [3,3]=[DOWN], [3,4]=[LEFT, RIGHT, DOWN], [3,5]=[DOWN], " +
                        "[4,2]=[LEFT, RIGHT], [4,4]=[LEFT, RIGHT], [4,5]=[DOWN], " +
                        "[5,2]=[LEFT, UP], [5,3]=[UP, DOWN], [5,4]=[LEFT, DOWN], [5,5]=[DOWN]}");

    }

    @Test
    public void shouldGeneratePossibleWays_whenFromLadderOnPipe3() {
        assertP("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼◄~~~H☼" +
                "☼#   H☼" +
                "☼    H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼",

                "{[1,2]=[RIGHT], [1,4]=[RIGHT], [1,5]=[DOWN], " +
                        "[2,2]=[LEFT, RIGHT], [2,3]=[DOWN], [2,4]=[LEFT, RIGHT, DOWN], [2,5]=[DOWN], " +
                        "[3,2]=[LEFT, RIGHT], [3,3]=[DOWN], [3,4]=[LEFT, RIGHT, DOWN], [3,5]=[DOWN], " +
                        "[4,2]=[LEFT, RIGHT], [4,3]=[DOWN], [4,4]=[LEFT, RIGHT, DOWN], [4,5]=[DOWN], " +
                        "[5,2]=[LEFT, UP], [5,3]=[LEFT, UP, DOWN], [5,4]=[LEFT, UP, DOWN], [5,5]=[LEFT, DOWN]}");

    }

    private void assertP(String map, String expected) {
        setupAI(map);

        Map<Point, List<Direction>> result = new TreeMap<Point, List<Direction>>();
        for (Map.Entry<Point, List<Direction>> entry : ai.possibleWays.entrySet()) {
            List<Direction> value = entry.getValue();
            if (!value.isEmpty()) {
                result.put(entry.getKey(), value);
            }
        }

        assertEquals(expected, result.toString());
    }

    private void setupAI(String map) {
        level = new LevelImpl(map);
        loderunner = new Loderunner(level, mock(Dice.class));

        for (Hero hero : level.getHeroes()) {
            Player player = new Player(mock(EventListener.class));
            loderunner.newGame(player);
            player.hero = hero;
            hero.init(loderunner);
        }

        ai = new AI();
        ai.setupPossibleWays(loderunner);
    }

    private void assertD(String expected) {
        assertEquals(expected, ai.getPath(loderunner.size(), level.getEnemies().get(0), level.getHeroes().get(0)).toString());
    }

    @Test
    public void shouldOnlyLeft() {
        setupAI("☼☼☼☼☼" +
                "☼   ☼" +
                "☼◄ «☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertD("[LEFT, LEFT]");
    }

    @Test
    public void shouldOnlyRight() {
        setupAI("☼☼☼☼☼" +
                "☼   ☼" +
                "☼« ◄☼" +
                "☼###☼" +
                "☼☼☼☼☼");

        assertD("[RIGHT, RIGHT]");
    }

    @Test
    public void shouldUpWithLadder() {
        setupAI("☼☼☼☼☼☼" +
                "☼   ◄☼" +
                "☼   H☼" +
                "☼«  H☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        assertD("[RIGHT, RIGHT, RIGHT, UP, UP]");
    }

    @Test
    public void shouldUpWithLadderThenPipe() {
        setupAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼◄~~~ ☼" +
                "☼#  #H☼" +
                "☼«   H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼");

        assertD("[RIGHT, RIGHT, RIGHT, RIGHT, UP, UP, LEFT, LEFT, LEFT, LEFT]");
    }

    @Test
    public void shouldUpWithLadderThenPipe2() {
        setupAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼◄~~~H☼" +
                "☼#   H☼" +
                "☼«   H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼");

        assertD("[RIGHT, RIGHT, RIGHT, RIGHT, UP, UP, LEFT, LEFT, LEFT, LEFT]");
    }

    @Test
    public void shouldTwoEqualsWays() {
        setupAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~◄~ ☼" +
                "☼H # H☼" +
                "☼H « H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼");

        assertD("[LEFT, LEFT, UP, UP, RIGHT, RIGHT]");
    }

    @Test
    public void shouldTwoNotEqualsWays() {
        setupAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ◄~~ ☼" +
                "☼H#  H☼" +
                "☼H « H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼");

        assertD("[LEFT, LEFT, UP, UP, RIGHT]");
    }

    @Test
    public void shouldTwoNotEqualsWays2() {
        setupAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ◄~~ ☼" +
                "☼H#  H☼" +
                "☼H  «H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼");

        assertD("[LEFT, LEFT, LEFT, UP, UP, RIGHT]");
    }

    @Test
    public void shouldTwoNotEqualsWays3() {
        setupAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ◄~ ☼" +
                "☼H## H☼" +
                "☼H  «H☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼");

        assertD("[RIGHT, UP, UP, LEFT, LEFT]");
    }

    @Test
    public void shouldOnPipe() {
        setupAI("☼☼☼☼☼☼☼" +
                "☼#####☼" +
                "☼ « « ☼" +
                "☼◄###◄☼" +
                "☼#####☼" +
                "☼#####☼" +
                "☼☼☼☼☼☼☼");

        Enemy enemy1 = level.getEnemies().get(0);
        assertEquals("[2,4]", enemy1.toString());

        Hero hero1 = loderunner.getHeroes().get(0);
        assertEquals("[1,3]", hero1.toString());

        Enemy enemy2 = level.getEnemies().get(1);
        assertEquals("[4,4]", enemy2.toString());

        Hero hero2 = loderunner.getHeroes().get(1);
        assertEquals("[5,3]", hero2.toString());

        assertEquals(Direction.RIGHT, ai.getDirection(loderunner, hero2, enemy1));
        assertEquals("[RIGHT, RIGHT, RIGHT, DOWN]", ai.getPath(loderunner.size(), enemy1, hero2).toString());

        assertEquals(Direction.LEFT, ai.getDirection(loderunner, hero1, enemy2));
        assertEquals("[LEFT, LEFT, LEFT, DOWN]", ai.getPath(loderunner.size(), enemy2, hero1).toString());
    }

    @Test
    public void shouldTwoEnemiesWithTwoHero() {
        setupAI("☼☼☼☼☼☼☼☼" +
                "☼ ◄  ◄ ☼" +
                "☼H#  #H☼" +
                "☼H    H☼" +
                "☼H    H☼" +
                "☼H «« H☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // проверяем следующую команду для первого чертика
        Enemy enemy1 = level.getEnemies().get(0);
        Hero hero1 = loderunner.getHeroes().get(0);
        assertEquals("[3,2]", enemy1.toString());
        assertEquals(Direction.LEFT, ai.getDirection(loderunner, hero1, enemy1));

        // проверяем весь путь для первого чертика
        assertEquals("[2,6]", hero1.toString());
        assertEquals("[LEFT, LEFT, UP, UP, UP, UP, RIGHT]", ai.getPath(loderunner.size(), enemy1, hero1).toString());

        // проверяем следующую команду для второго чертика
        Enemy enemy2 = level.getEnemies().get(1);
        Hero hero2 = loderunner.getHeroes().get(1);
        assertEquals("[4,2]", enemy2.toString());
        assertEquals(Direction.RIGHT, ai.getDirection(loderunner, hero2, enemy2));

        // проверяем весь путь для второго чертика
        assertEquals("[5,6]", hero2.toString());
        assertEquals("[RIGHT, RIGHT, UP, UP, UP, UP, LEFT]", ai.getPath(loderunner.size(), enemy2, hero2).toString());
    }

    // из за того, что чертики друг для друга препятствие - не каждый чертик может охотится за любым героем
    // но они будут пытаться, в надежде, что другой чертик не будет стоять на месте
    // TODO тут возможен случай, когда они друг друга анигилируют :)
    @Test
    public void shouldTwoEnemiesWithTwoHero_enemyIsBarrier() {
        setupAI("☼☼☼☼☼☼☼☼" +
                "☼ ◄  ◄ ☼" +
                "☼H#  #H☼" +
                "☼H    H☼" +
                "☼H    H☼" +
                "☼H «« H☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        // пробуем чтобы первый чертик пошел за вторым игроком
        Enemy enemy2 = level.getEnemies().get(1);
        assertEquals("[4,2]", enemy2.toString());
        Hero hero1 = loderunner.getHeroes().get(0);
        assertEquals("[2,6]", hero1.toString());
        assertEquals("[LEFT, LEFT, LEFT, UP, UP, UP, UP, RIGHT]", ai.getPath(loderunner.size(), enemy2, hero1).toString());

        // пробуем чтобы второй чертик пошел за первым игроком
        Enemy enemy1 = level.getEnemies().get(0);
        assertEquals("[3,2]", enemy1.toString());
        Hero hero2 = loderunner.getHeroes().get(1);
        assertEquals("[5,6]", hero2.toString());
        assertEquals("[RIGHT, RIGHT, RIGHT, UP, UP, UP, UP, LEFT]", ai.getPath(loderunner.size(), enemy1, hero2).toString());
    }



}

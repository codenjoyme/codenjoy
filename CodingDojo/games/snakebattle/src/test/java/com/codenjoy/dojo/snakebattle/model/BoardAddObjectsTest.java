package com.codenjoy.dojo.snakebattle.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.TestGameSettings;
import com.codenjoy.dojo.snakebattle.model.board.SnakeBoard;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.objects.*;
import com.codenjoy.dojo.snakebattle.services.GameSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * @author K.ilya
 */
@RunWith(Parameterized.class)
public class BoardAddObjectsTest {

    private SnakeBoard game;

    private Point addition;
    boolean add;

    public BoardAddObjectsTest(Point addition, boolean add) {
        this.addition = addition;
        this.add = add;
    }

    private void givenFl(String board) {
        Level level = new Level(board);
        GameSettings settings = new TestGameSettings();
        game = new SnakeBoard(level, mock(Dice.class), settings);
        Hero hero = level.hero(game);
        EventListener listener = mock(EventListener.class);
        Player player = new Player(listener, settings);
        player.setHero(hero);
        game.newGame(player);
        hero.setActive(true);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] params = new Object[][]{
                // нельзя ставить яблоки на яблоки,камни,таблетки,золото,стены
                {new Apple(pt(2, 2)), false},
                {new Apple(pt(2, 1)), false},
                {new Apple(pt(3, 3)), false},
                {new Apple(pt(3, 2)), false},
                {new Apple(pt(3, 1)), false},
                {new Apple(pt(3, 0)), false},
                // нельзя ставить камни на яблоки,камни,таблетки,золото,стены и справа от выходов
                {new Stone(pt(2, 3)), false},
                {new Stone(pt(2, 2)), false},
                {new Stone(pt(2, 1)), false},
                {new Stone(pt(3, 3)), false},
                {new Stone(pt(3, 2)), false},
                {new Stone(pt(3, 1)), false},
                {new Stone(pt(3, 0)), false},
                // нельзя ставить таблетки полёта на яблоки,камни,таблетки,золото,стены
                {new FlyingPill(pt(2, 2)), false},
                {new FlyingPill(pt(2, 1)), false},
                {new FlyingPill(pt(3, 3)), false},
                {new FlyingPill(pt(3, 2)), false},
                {new FlyingPill(pt(3, 1)), false},
                {new FlyingPill(pt(3, 0)), false},
                // нельзя ставить таблетки ярости на яблоки,камни,таблетки,золото,стены
                {new FuryPill(pt(2, 2)), false},
                {new FuryPill(pt(2, 1)), false},
                {new FuryPill(pt(3, 3)), false},
                {new FuryPill(pt(3, 2)), false},
                {new FuryPill(pt(3, 1)), false},
                {new FuryPill(pt(3, 0)), false},
                // нельзя ставить золото на яблоки,камни,таблетки,золото,стены
                {new Gold(pt(2, 2)), false},
                {new Gold(pt(2, 1)), false},
                {new Gold(pt(3, 3)), false},
                {new Gold(pt(3, 2)), false},
                {new Gold(pt(3, 1)), false},
                {new Gold(pt(3, 0)), false},
                // можно ставить яблоки,камни,таблетки и золото в пустое место
                {new Apple(pt(4, 2)), true},
                {new Stone(pt(4, 2)), true},
                {new FlyingPill(pt(4, 2)), true},
                {new FuryPill(pt(4, 2)), true},
                {new Gold(pt(4, 2)), true},
        };
        return Arrays.asList(params);
    }

    @Test
    public void oneOrLessObjectAtPoint() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼# ©  ☼" +
                "☼ ®○  ☼" +
                "☼ $●  ☼" +
                "☼☼☼☼☼☼☼");
        int before = 1;
        Point object = game.getOn(addition);
        game.addToPoint(addition);
        game.tick();
        int objectsAfter = 0;
        String objType = addition.getClass().toString().replaceAll(".*\\.", "");
        switch (objType) {
            case "Apple":
                objectsAfter = game.getApples().size();
                break;
            case "Stone":
                objectsAfter = game.getStones().size();
                break;
            case "FlyingPill":
                objectsAfter = game.getFlyingPills().size();
                break;
            case "FuryPill":
                objectsAfter = game.getFuryPills().size();
                break;
            case "Gold":
                objectsAfter = game.getGold().size();
                break;
            default:
                fail("Отсутствуют действия на объект типа " + objType);
        }
        if (add)
            assertEquals("Новый объект '" + objType + "' не был добавлен на поле!",
                    before + 1, objectsAfter);
        else
            assertEquals("Добавился новый объект '" + objType + "'" + " поверх существующего объекта!" +
                            (object == null ? null : object.getClass()),
                    before, objectsAfter);
    }

}

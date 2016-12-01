package com.codenjoy.dojo.snake.battle.model.board;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.battle.model.Player;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;
import com.codenjoy.dojo.snake.battle.model.level.LevelImpl;
import com.codenjoy.dojo.snake.battle.model.objects.Apple;
import com.codenjoy.dojo.snake.battle.model.objects.Stone;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * @author K.ilya
 */
@RunWith(Parameterized.class)
public class BoardAddObjectsTest {

    private SnakeBoard game;

    private Point additionObject;
    boolean shouldAdd;

    public BoardAddObjectsTest(Point additionObject, boolean shouldAdd) {
        this.additionObject = additionObject;
        this.shouldAdd = shouldAdd;
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        List<Hero> heroes = level.getHero();
        Hero hero = heroes.isEmpty() ? null : heroes.get(0);

        game = new SnakeBoard(level, mock(Dice.class));
        game.debugMode = true;
        EventListener listener = mock(EventListener.class);
        Player player = new Player(listener);
        game.newGame(player);
        if (hero != null) {
            player.setHero(hero);
            hero.init(game);
        }
        Hero hero1 = game.getHeroes().get(0);
        hero1.setActive(true);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] params = new Object[][]{
                // нельзя ставить яблоки на яблоки,камни,стены
                {new Apple(3, 4), false},
                {new Apple(3, 5), false},
                {new Apple(3, 6), false},
                // нельзя ставить камни на яблоки,камни,стены
                {new Stone(3, 4), false},
                {new Stone(3, 5), false},
                {new Stone(3, 6), false},
                // можно ставить яблоки,камни в пустое место
                {new Apple(4, 5), true},
                {new Stone(4, 5), true},
        };
        return Arrays.asList(params);
    }

    @Test
    public void oneOrLessObjectAtPoint() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼  ○  ☼" +
                "☼  ●  ☼" +
                "☼☼☼☼☼☼☼");
        int objectsBefore = 1;
        game.addToPoint(additionObject);
        game.tick();
        int objectsAfter = 0;
        String objType = additionObject.getClass().toString().replaceAll(".*\\.", "");
        switch (objType) {
            case "Apple":
                objectsAfter = game.getApples().size();
                break;
            case "Stone":
                objectsAfter = game.getStones().size();
                break;
            default:
                fail("Отсутствуют действия на объект типа " + objType);
        }
        if (shouldAdd)
            assertEquals("Новый объект '" + objType + "' не был добавлен на поле!",
                    objectsBefore + 1, objectsAfter);
        else
            assertEquals("Добавился новый объект '" + objType + "' поверх существующего объекта!",
                    objectsBefore, objectsAfter);
    }

}

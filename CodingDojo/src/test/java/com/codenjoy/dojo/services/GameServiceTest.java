package com.codenjoy.dojo.services;

import com.codenjoy.dojo.loderunner.services.LoderunnerGame;
import com.codenjoy.dojo.services.mocks.MockPlayerService;
import com.codenjoy.dojo.services.mocks.MockTimerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 18:51
 */
@ContextConfiguration(classes = {
        GameServiceImpl.class,
        MockTimerService.class,
        MockPlayerService.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class GameServiceTest {

    @Autowired private GameServiceImpl gameService;
    @Autowired private TimerService timer;
    @Autowired private PlayerService players;

    @Before
    public void setup() {
        reset(timer, players);
    }

    @Test
    public void shouldGetGameNames() {
        assertEquals("[SnakeGame, BombermanGame, MinesweeperGame, BattlecityGame, LoderunnerGame]", gameService.getGameNames().toString());
    }

    @Test
    public void shouldGetGames() {
        assertEquals("[class com.codenjoy.dojo.snake.services.SnakeGame, " +
                "class com.codenjoy.dojo.bomberman.services.BombermanGame, " +
                "class com.codenjoy.dojo.minesweeper.services.MinesweeperGame, " +
                "class com.codenjoy.dojo.battlecity.services.BattlecityGame, " +
                "class com.codenjoy.dojo.loderunner.services.LoderunnerGame]", gameService.getGames().toString());
    }

    @Test
    public void shouldGetSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();
        assertEquals("{battlecity=[battle_ground, battle_wall, bang, construction, construction_destroyed_down, construction_destroyed_up, " +
                "construction_destroyed_left, construction_destroyed_right, construction_destroyed_down_twice, " +
                "construction_destroyed_up_twice, construction_destroyed_left_twice, construction_destroyed_right_twice, " +
                "construction_destroyed_left_right, construction_destroyed_up_down, construction_destroyed_up_left, " +
                "construction_destroyed_right_up, construction_destroyed_down_left, construction_destroyed_down_right, " +
                "construction_destroyed, bullet, tank_up, tank_right, tank_down, tank_left, other_tank_up, other_tank_right, " +
                "other_tank_down, other_tank_left, ai_tank_up, ai_tank_right, ai_tank_down, ai_tank_left], " +

                "snake=[bad_apple, good_apple, break, head_down, head_left, head_right, head_up, " +
                "tail_end_down, tail_end_left, tail_end_up, tail_end_right, tail_horizontal, tail_vertical, " +
                "tail_left_down, tail_left_up, tail_right_down, tail_right_up, space], " +

                "minesweeper=[bang, here_is_bomb, detector, flag, hidden, one_mine, two_mines, three_mines, four_mines, " +
                "five_mines, six_mines, seven_mines, eight_mines, border, no_mine, destroyed_bomb], " +

                "loderunner=[none, brick, pit_fill_1, pit_fill_2, pit_fill_3, pit_fill_4, undestroyable_wall, " +
                "drill_space, drill_pit, enemy_ladder, enemy_left, enemy_right, enemy_pipe_left, enemy_pipe_right, " +
                "enemy_pit, gold, hero_die, hero_drill_left, hero_drill_right, hero_ladder, hero_left, hero_right, " +
                "hero_fall_left, hero_fall_right, hero_pipe_left, hero_pipe_right, ladder, pipe], " +

                "bomberman=[bomberman, bomb_bomberman, dead_bomberman, boom, bomb_five, bomb_four, bomb_three, " +
                "bomb_two, bomb_one, wall, destroy_wall, destroyed_wall, meat_chopper, dead_meat_chopper, empty, " +
                "other_bomberman, other_bomb_bomberman, other_dead_bomberman]}", sprites.toString());
    }

    @Test
    public void shouldGetPngForSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();

        List<String> errors = new LinkedList<String>();
        for (Map.Entry<String, List<String>> entry : sprites.entrySet()) {
            for (String sprite : entry.getValue()) {
                File file = new File(String.format("src/main/webapp/resources/sprite/%s/%s.png", entry.getKey(), sprite));
                if (!file.exists()) {
                    errors.add("Файл не найден: " + file.getAbsolutePath());
                }
            }
        }

        assertTrue(errors.toString().replace(',', '\n'), errors.isEmpty());
    }

    @Test
    public void shouldGetSelectedGame() {
        GameType oldGame = gameService.getSelectedGame();

        gameService.selectGame(LoderunnerGame.class.getSimpleName());
        verify(players).removeAll();
        verify(timer).pause();

        GameType newGame = gameService.getSelectedGame();
        assertEquals("loderunner", newGame.gameName());

        assertNotSame(oldGame, newGame);
    }

    @Test
    public void shouldChangeDecoderWhenSelectGame() {
        shouldGetSelectedGame();

        String board = "";
        for (Enum e : gameService.getSelectedGame().getPlots()) {
            board += e.toString();
        }
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ01", gameService.getDecoder().encode(board));
    }
}

package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.mocks.MockPlayerService;
import com.codenjoy.dojo.services.mocks.MockTimerService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.reset;

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

    @Ignore // TODO test me
    @Test
    public void shouldGetGameNames() {
        assertEquals("[rubicscube, battlecity, snake, sudoku, " +
                        "football, hex, minesweeper, moebius, " +
                        "loderunner, bomberman, a2048, " +
                        "sample, collapse]",
                gameService.getGameNames().toString());
    }

    @Ignore // TODO test me
    @Test
    public void shouldGetSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();
        assertEquals(
                "{rubicscube=[none, red, green, blue, white, yellow, orange], " +

                "battlecity=[none, battle_wall, bang, construction, construction_destroyed_down, construction_destroyed_up, " +
                "construction_destroyed_left, construction_destroyed_right, construction_destroyed_down_twice, " +
                "construction_destroyed_up_twice, construction_destroyed_left_twice, construction_destroyed_right_twice, " +
                "construction_destroyed_left_right, construction_destroyed_up_down, construction_destroyed_up_left, " +
                "construction_destroyed_right_up, construction_destroyed_down_left, construction_destroyed_down_right, " +
                "construction_destroyed, bullet, tank_up, tank_right, tank_down, tank_left, other_tank_up, other_tank_right, " +
                "other_tank_down, other_tank_left, ai_tank_up, ai_tank_right, ai_tank_down, ai_tank_left], " +

                "snake=[bad_apple, good_apple, break, head_down, head_left, head_right, head_up, " +
                "tail_end_down, tail_end_left, tail_end_up, tail_end_right, tail_horizontal, tail_vertical, " +
                "tail_left_down, tail_left_up, tail_right_down, tail_right_up, none], " +

                "sudoku=[none, border, one, two, three, four, five, six, seven, eight, nine], " +

                "football=[none, wall, hero, hero_w_ball, ball, top_goal, bottom_goal, hited_goal, team_member, team_member_w_ball, enemy, enemy_w_ball], " +

                "hex=[none, wall, my_hero, hero1, hero2, hero3, hero4, hero5, hero6, hero7, hero8, hero9, hero10, hero11], " +

                "minesweeper=[bang, here_is_bomb, detector, flag, hidden, one_mine, two_mines, three_mines, four_mines, " +
                "five_mines, six_mines, seven_mines, eight_mines, border, none, destroyed_bomb], " +

                "moebius=[left_up, up_right, right_down, down_left, left_right, up_down, cross, empty], " +

                "loderunner=[none, brick, pit_fill_1, pit_fill_2, pit_fill_3, pit_fill_4, undestroyable_wall, " +
                "drill_pit, enemy_ladder, enemy_left, enemy_right, enemy_pipe_left, enemy_pipe_right, " +
                "enemy_pit, gold, hero_die, hero_drill_left, hero_drill_right, hero_ladder, hero_left, hero_right, " +
                "hero_fall_left, hero_fall_right, hero_pipe_left, hero_pipe_right, other_hero_die, other_hero_left, " +
                "other_hero_right, other_hero_ladder, other_hero_pipe_left, other_hero_pipe_right, ladder, pipe], " +

                "bomberman=[bomberman, bomb_bomberman, dead_bomberman, " +
                "other_bomberman, other_bomb_bomberman, other_dead_bomberman, " +
                "bomb_timer_5, bomb_timer_4, bomb_timer_3, bomb_timer_2, bomb_timer_1, boom, wall, " +
                "destroy_wall, destroyed_wall, meat_chopper, dead_meat_chopper, none], " +

                "a2048=[_x, _2, _4, _8, _16, _32, _64, _128, _256, _512, _1024, _2048, _4096, " +
                "_8192, _16384, _32768, _65536, _131072, _262144, _524288, _1048576, _2097152, _4194304, none], " +

                "collapse=[none, border, one, two, three, four, five, six, seven, eight, nine], " +

                "sample=[none, wall, hero, other_hero, dead_hero, gold, bomb]" +

                "}", sprites.toString());
    }

    @Ignore
    @Test
    public void shouldGetPngForSprites() {
        Map<String, List<String>> sprites = gameService.getSprites();

        List<String> errors = new LinkedList<String>();
        for (Map.Entry<String, List<String>> entry : sprites.entrySet()) {
            for (String sprite : entry.getValue()) {
                File file = new File(String.format("target/codenjoy-contest/resources/sprite/%s/%s.png", entry.getKey(), sprite));
                if (!file.exists()) {
                    errors.add("Файл не найден: " + file.getAbsolutePath());
                }
            }
        }

        assertTrue(errors.toString().replace(',', '\n'), errors.isEmpty());
    }

}

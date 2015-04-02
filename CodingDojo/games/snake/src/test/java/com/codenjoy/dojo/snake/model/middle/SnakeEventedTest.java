package com.codenjoy.dojo.snake.model.middle;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.snake.model.Hero;
import com.codenjoy.dojo.snake.model.SnakeEvented;
import com.codenjoy.dojo.snake.services.SnakeEvents;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:30 AM
 */
@RunWith(MockitoJUnitRunner.class)
public class SnakeEventedTest {

    @Mock
    private EventListener listener;
    private Hero snake;

    @Before
    public void setup() {
        snake = new SnakeEvented(listener, 0, 0);
    }

    @Test
    public void shouldListenerHearWhenSnakeEatApple() {
        snake.grow();

        verify(listener).event(SnakeEvents.EAT_APPLE);
    }

    @Test
    public void shouldListenerHearWhenSnakeEatStone() {
        snake.eatStone();

        verify(listener).event(SnakeEvents.EAT_STONE);
    }

    @Test
    public void shouldListenerHearWhenSnakeIsDead() {
        snake.killMe();

        verify(listener).event(SnakeEvents.KILL);
    }
}

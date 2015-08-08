package com.codenjoy.dojo.football.model;

import com.codenjoy.dojo.football.model.LevelImpl;
import com.codenjoy.dojo.football.model.Player;
import com.codenjoy.dojo.football.model.Football;
import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.services.Events;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactoryImpl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class FootballTest {

    private Football game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);

        game = new Football(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }
    
    @Test
    public void heroCanMove() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        
        hero.up();
        game.tick();

        assertE("☼☼☼☼☼" +
        		"☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        
        hero.left();
        game.tick();
        
        assertE("☼☼☼☼☼" +
        		"☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        
        hero.right();
        game.tick();
        
        assertE("☼☼☼☼☼" +
        		"☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        
        hero.down();
        game.tick();
        
        assertE("☼☼☼☼☼" +
                "☼   ☼" +
        		"☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }
    
    @Test
    public void heroCannotPassThroughWalls() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }
    
    @Test
    public void ballIsPresent() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  ∙   ☼" +
                "☼☺     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        assertE("☼☼☼☼☼☼☼☼" +
                "☼  ∙   ☼" +
                "☼☺     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void playerCanBeWithBall() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☺∙    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

    }
    
    public void ballCanMove() {
        
    	givenFl("☼☼☼☼☼☼☼☼" +
                "☼∙     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.getBall(2, 2).right(1);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
        		"☼ *    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
    }
    
    @Test
    public void playerCanMoveWithBall() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☺*    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(game.getBalls().size(), 1);
        assertEquals(true, game.isBall(2, 4));
        
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        assertEquals(true, game.isBall(2, 4));
        
        hero.right();
        assertEquals(true, game.getBall(2, 4) != null);
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        
        assertEquals(true, game.getBall(3, 4) != null);
        
        assertE("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void playerCanNotMoveWithBallThrowWall() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼*☺    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼☻     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        hero.left();
        game.tick();
        
        hero.left();
        game.tick();
        
        assertE("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼☻     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        hero.right();
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        
        assertE("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void gameStartsFromPlayerWithBallState() {
        givenFl("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼☻     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void playerHitsBallRight() {
        givenFl("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺*  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺ * ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void playerHitsTheGate() {
        givenFl("☼☼☼┴┴☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
        
        hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼x⌂☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼==☼☼☼");
    }
    
    @Test
    public void ballStopsWhetGetWall() {
        givenFl("☼☼☼☼☼☼☼☼" +
        		"☼      ☼" +
                "☼      ☼" +
                "☼    ☻ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼    ☺*☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼    ☺∙☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    public void resetFieldAfterGoalAndNewGameCall() {
        
    	givenFl("☼☼☼┴┴☼☼☼" +
        		"☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
        
    	hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        dice(2, 2);
    	game.newGame(player);
    	
        assertE("☼☼☼┴┴☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   *  ☼" +
                "☼      ☼" +
                "☼ ☺    ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
    }
    
    @Ignore
    @Test
    public void resetFieldAfterHittenGoal() {
        
    	givenFl("☼☼☼┴┴☼☼☼" +
        		"☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
        
    	hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        dice(2, 2);
        game.tick();
        verify(listener, only()).event(Events.TOP_GOAL);
        
        assertE("☼☼☼┴┴☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   *  ☼" +
                "☼      ☼" +
                "☼ ☺    ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
    }
    
	@Ignore
    @Test
    public void resetFieldByNewGameCall() {
        
    	givenFl("☼☼☼x┴┴☼☼☼" +
        		"☼       ☼" +
                "☼  ☺    ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼┬┬┬☼☼☼");
        
    	dice(2, 2);
    	game.tick();
    	verify(listener, only()).event(Events.TOP_GOAL);
        
        assertE("☼☼☼┴┴┴☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼   *   ☼" +
                "☼       ☼" +
                "☼ ☺     ☼" +
                "☼       ☼" +
                "☼☼☼┬┬┬☼☼☼");
    }
    
	@Test
    public void scoreShouldIncreaseAfterTopGoalHited() {
        
    	givenFl("☼☼☼┴┴☼☼☼" +
        		"☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
        
    	assertEquals(0, player.getScore());
    	hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        game.tick();
        assertEquals(1, player.getScore());
        
	}
	
}

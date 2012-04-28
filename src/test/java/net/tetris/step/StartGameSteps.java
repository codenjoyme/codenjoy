package net.tetris.step;

import net.tetris.dom.*;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StartGameSteps {

    private GameConsole console;
    private TetrisGame game;
    @Captor private ArgumentCaptor<Integer> xCaptor;
    @Captor private ArgumentCaptor<Integer> yCaptor;
    
    @Given("a game console")
    public void givenGameConsole() {
        MockitoAnnotations.initMocks(this);
        console = mock(GameConsole.class);
    }

    @When("game starts")
    public void whenGameStarts() {
        game = new TetrisGame(console, new TetrisQueue(), new TetrisScoreBoard(), new TetrisGlass(10, 20));
    }

    @Then("I see a random figure at console position $x, $y")
    public void thenRandomFigureAt(@Named("x") int x, @Named("y") int y) {
        verify(console).figureAt((Figure) notNull(), xCaptor.capture(), yCaptor.capture());
        assertEquals(x, xCaptor.getValue().intValue());
        assertEquals(y, yCaptor.getValue().intValue());
    }

    private static class TetrisScoreBoard implements ScoreBoard {
        public void glassOverflown() {

        }
    }

}

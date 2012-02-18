package net.tetris.step;

import net.tetris.dom.Figure;
import net.tetris.dom.GameConsole;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static org.mockito.Mockito.mock;

public class StartGameSteps {

    private GameConsole console;

    @Given("a game console")
    public void givenGameConsole() {
        console = mock(GameConsole.class);
    }

    @When("game starts")
    public void whenGameStarts() {
        console.startGame();
    }

    @Then("I see a random figure at console position $x, $y")
    public void thenRandomFigureAt(@Named("x") int x, @Named("y") int y) {
        Figure figure = mock(Figure.class);
        console.figureAt(figure, x, y);
    }
}

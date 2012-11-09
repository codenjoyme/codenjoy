package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {
	private Reader input;
	private Printer printer;
	private GameController gameController;

	@Before
	public void setUp() {
		gameController = itinializeGameController();
	}

	private GameController itinializeGameController() {
		input = mock(Reader.class);
		printer = mock(Printer.class);
		return new GameController(printer, input);

	}
	
	@Test
	public void shouldGameController(){
		assertNotNull(gameController);
	}

}

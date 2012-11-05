package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {
	private Reader input;
	private Printer printer;
	private GameController gameController;

	@Test
	public void shouldGameController() {
		// given
		input = mock(Reader.class);
		printer = mock(Printer.class);
		// when
		gameController = new GameController(printer, input);
		// then
		verify(input).setPrinter(printer);
		assertNotNull(gameController);
	}

}

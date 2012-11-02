package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {
	private Reader input = mock(Reader.class);

	private Printer printer = mock(Printer.class);
	private GameController gameController;

	@Before
	public void setUp() {
		gameController = new GameController(printer, input);
	}

	@Test
	public void testGameController() {
		verify(gameController);
	}

}

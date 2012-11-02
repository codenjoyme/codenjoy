package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.Reader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;

public class GameControllerTest {
	private Reader input = createNiceMock(Reader.class);

	private Printer printer = createNiceMock(Printer.class);
	private GameController gameController;

	@Before
	public void setUp() {
		gameController = new GameController(printer, input);
	}

	@Test
	public void testGameController() {
		assertNotNull(gameController);
	}

}

package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input;

import static org.junit.Assert.*;


import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.impl.ConsoleReader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.impl.ConsolePrinter;

public class ConsoleReaderTest {
	private Printer printer;
	private Reader consoleReader;

	@Before
	public void setUp() throws Exception {
		consoleReader = new ConsoleReader();
		printer = EasyMock.createMock(ConsolePrinter.class);

	}
	
	@Test
	public void shouldReaderContainsPrinter_whenSet() {
		EasyMock.replay(printer);
		consoleReader.setPrinter(printer);
		EasyMock.verify(printer);
	}

}

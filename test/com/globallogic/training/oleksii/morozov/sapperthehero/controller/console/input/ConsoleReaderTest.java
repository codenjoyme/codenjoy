package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input.impl.ConsoleReader;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;
import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.impl.ConsolePrinter;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Scanner.class, ConsoleReader.class})
public class ConsoleReaderTest {
	private Printer printer;
	private Reader consoleReader;

	@Before
	public void setUp() throws Exception {
		consoleReader = new ConsoleReader();
		printer = PowerMock.createMock(ConsolePrinter.class);

	}
	
	@Test
	public void shouldReaderContainsPrinter_whenSet() {
		PowerMock.replay(printer);
		consoleReader.setPrinter(printer);
		PowerMock.verify(printer);
		assertNotNull(printer);
	}

}

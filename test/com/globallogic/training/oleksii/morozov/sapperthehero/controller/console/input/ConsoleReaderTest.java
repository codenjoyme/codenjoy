package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.input;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.globallogic.training.oleksii.morozov.sapperthehero.controller.console.output.Printer;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleReaderTest {
	private Printer printer;
	private Reader consoleReader;

	@Before
	public void setUp() throws Exception {
		consoleReader = new ConsoleReader();
		printer = mock(Printer.class);

	}
	 
	@Test
	public void shouldReaderContainsPrinter_whenSet() {
		consoleReader.setPrinter(printer);
	}

}

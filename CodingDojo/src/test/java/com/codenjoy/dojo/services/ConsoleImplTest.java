package com.codenjoy.dojo.services;

import org.apache.commons.io.input.ReaderInputStream;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 21:23
 */
public class ConsoleImplTest {

    @Test
    public void testPrint() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        Console console = new ConsoleImpl();
        console.print("qwe");
        console.print("asd");

        assertEquals("qwe\n\n\n" +
                "asd\n\n\n", out.toString());
    }

    @Test
    public void testRead() {
        System.setIn(new ReaderInputStream(new StringReader("qwe\nasd\n")));

        Console console = new ConsoleImpl();
        assertEquals("qwe", console.read());
        assertEquals("asd", console.read());
    }
}

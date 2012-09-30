package com.globallogic.snake.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleImpl implements Console {

	private BufferedReader reader;

	public ConsoleImpl() {
		reader = new BufferedReader(new InputStreamReader(System.in));		
	}
	
	public String read() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException("Read line error!"); 
		}
	}

	public void print(String string) {
		System.out.println(string);
		System.out.println("");
		System.out.println("");
	}
}

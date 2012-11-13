package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

public class IllegalConsoleCommandException extends IllegalArgumentException {

	private static final String MESSAGE = "You try to read not a command";

	public IllegalConsoleCommandException() {
		super(MESSAGE);
	}
}

package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

class IllegalConsoleCommandException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String MESSAGE = "You try to read not a command";

	public IllegalConsoleCommandException() {
		super(MESSAGE);
	}
}

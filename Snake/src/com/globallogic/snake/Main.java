package com.globallogic.snake;

public class Main {

	private static final int BOARD_SIZE = 7;
	
	public static void main(String[] args) {
		Board board = new BoardImpl(new RandomArtifactGenerator(), BOARD_SIZE);
		SnakePrinter printer = new SnakePrinterImpl();
		Console console = new ConsoleImpl();
		
		new SnakeRunner(board, printer, console).playGame();
	} 

}

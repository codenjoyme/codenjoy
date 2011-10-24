package com.globallogic.snake;


public class SnakeRunner {
	
	private static final int BOARD_SIZE = 7;
	private Board board;
	private SnakePrinter printer;
	private Console console;
	
	public static void main(String[] args) {
		Board board = new BoardImpl(new RandomArtifactGenerator(), BOARD_SIZE);
		SnakePrinter printer = new SnakePrinterImpl();
		Console console = new ConsoleImpl();
		
		new SnakeRunner(board, printer, console).playGame();
	}
	
	public SnakeRunner(Board board, SnakePrinter printer, Console console) {
		this.board = board;
		this.printer = printer;
		this.console = console;
	}

	public void playGame() {
		Snake snake = board.getSnake();
		
		while (!board.isGameOver()) {		
			printBoard();
			
			String line = console.read();
			if (line.length() != 0) {
				int ch = line.charAt(0);			
				
				if (ch == 115) {
					snake.turnDown();
				} else if (ch == 97) {
					snake.turnLeft();
				} else if (ch == 100) {
					snake.turnRight();
				} else if (ch == 119) {
					snake.turnUp();
				} 				
			}
			board.tact();						
		}
		printBoard();
		console.print("Game over!");
	}
	
	private void printBoard() {
		console.print(printer.print(board));	
	}

}

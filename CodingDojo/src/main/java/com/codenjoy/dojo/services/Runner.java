package com.codenjoy.dojo.services;

public class Runner {
	
	private Game game;
	private Console console;
		
	public Runner(Game game, Console console) {
		this.game = game;
		this.console = console;
	}

	public void playGame() {
        Joystick joystick = game.getJoystick();

		do {
			printBoard();
			
			String line = console.read();
            boolean bomb = false;
            boolean move = false;
			for (Character ch : line.toCharArray()) {
				if (ch == 's' || ch == 'ы') {
                    if (move) {
                        game.tick();
                        bomb = false;
                    }
                    joystick.down();
                    move = true;
				} else if (ch == 'a' || ch == 'ф') {
                    if (move) {
                        game.tick();
                        bomb = false;
                    }
					joystick.left();
                    move = true;
				} else if (ch == 'd' || ch == 'в') {
                    if (move) {
                        game.tick();
                        bomb = false;
                    }
					joystick.right();
                    move = true;
				} else if (ch == 'w' || ch == 'ц') {
                    if (move) {
                        game.tick();
                        bomb = false;
                    }
					joystick.up();
                    move = true;
				} else if (ch == ' ') {
                    if (bomb) {
                        game.tick();
                        move = false;
                    }
                    joystick.act();
                    bomb = true;
                }
            }
            game.tick();
		} while (!game.isGameOver());
		
		printBoard();
		console.print("Game over!");
	}
	
	private void printBoard() {
		console.print(game.toString());
	}

}

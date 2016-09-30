package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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
		console.print(game.getBoardAsString().toString());
	}

}

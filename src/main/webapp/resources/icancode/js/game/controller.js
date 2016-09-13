/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
function initController(socket, runner, console, buttons, getRobot) {
    
    if (game.debug) {
        debugger;
    }
    
    var controlling = false;
    var commands = [];
    var command = null;
    board = null;

    var finish = function() {
        controlling = false;
        commands = [];
        command = null;
        board = null;
        runner.cleanProgram();
        buttons.enableAll();
    }

    var currentCommand = function() {
        if (commands.length != 0) {
            return commands[0];
        } else {
            return null;
        }
    }

    var hasCommand = function(cmd) {
        return (commands.indexOf(cmd) != -1);
    }

    var processCommands = function(newBoard) {
        board = newBoard;

        if (board) {
            var b = new Board(board);
            var hero = b.getHero();
            var exit = b.getExit();
        }

        var finished = !!b && hero.toString() == exit.toString();
        var stopped = currentCommand() == 'STOP';
        if (!controlling || stopped || finished) {
            finish();
            if (finished) {
                console.clean();
                console.printCongrats();
            } else if (stopped) {
                console.clean();
                console.printHello();
            }
            return;
        }
        if (hasCommand('STOP')) {
            commands = ['RESET', 'STOP'];
        } else if (currentCommand() == 'RESET') {
            // do nothing
        } else if (currentCommand() == 'WAIT') {
            // do nothing
        } else if (!board) {
            commands = ['WAIT'];
        } else {
            if (runner.isProgramCompiled()) {
// TODO for debug
//                try {
                    runner.runProgram(getRobot());
//                } catch (e) {
//                    console.error(e.message);
//                    console.print('Please try again.');
//                    buttons.enableAll();
//                    return;
//                }
            } else {
                console.error('function program(robot) not implemented!');
                console.print('Info: if you clean your code you will get info about commands')
            }
        }
        if (commands.length == 0) {
            commands = ['WAIT'];
        }
        if (controlling) {
            if (commands.length > 0) {
                command = commands.shift();
                socket.send(command);
            }
        }
    }

    var compileCommands = function(onSuccess) {
        console.print('Uploading program...');
// TODO for debug
//        try {
            var robot = getRobot();
            runner.compileProgram(robot);
//        } catch (e) {
//            console.error(e.message);
//            console.print('Please try again.');
//            buttons.enableAll();
//            return;
//        }
        onSuccess();
    }

    var cleanCommand = function() {
        commands = [];
    }

    var addCommand = function(command) {
        commands.push(command);
    }

    var resetCommand = function() {
        commands = ['RESET'];
    }

    var stopCommand = function() {
        commands.push('STOP');
    }

    var waitCommand = function() {
        commands.push('WAIT');
    }

    var winCommand = function() {
        socket.send('WIN');
    }

    var popLastCommand = function() {
        var result = command;
        command == null;
        return command;
    }

    var reset = function() {
        resetCommand();
        stopCommand();
        if (!controlling) {
            controlling = true;
            processCommands();
        }
    }

    var commit = function() {
        cleanCommand();
        compileCommands(function() {
            resetCommand();
            controlling = true;
            processCommands();

            buttons.enableReset();
        });
    }

    var sleep = function(onSuccess) {
        setTimeout(function(){
            onSuccess();
        }, 1000);
    }

    var reconnect = function() {
        controlling = false;

        sleep(function() {
            socket.connect(function() {
                if (controlling) {
                    controlling = true;
                    processCommands();
                }
            });
        });
    }

    var onMessage = function(data) {
        var command = popLastCommand();
        if (!!command && command != 'WAIT') {
            console.print('Robot do ' + command);
			if (game.demo) {
				if (command == 'RESET') {
					runner.cleanProgram();
				}
			}
        } else {
			console.print('Waiting for next command...');			
		}
        processCommands(data);
    }

    return {
        commit : commit,
        reset : reset,
        onMessage : onMessage,
        reconnect : reconnect,
        cleanCommand : cleanCommand,
        addCommand : addCommand,
        waitCommand : waitCommand,
        winCommand : winCommand
    };
}
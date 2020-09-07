/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
function initController(socket, runner, logger, buttons, levelProgress, getRobot) {
    
    if (game.debug) {
        game.debugger();
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
            var levelFinished = b.isLevelFinished();
        }

        var finished = !!b && levelFinished && !levelProgress.isCurrentLevelMultiple();
        var stopped = currentCommand() == 'STOP';
        if (!controlling || stopped || finished) {
            finish();
            buttons.enableAll();
            if (finished) {
                logger.clean();
                logger.printCongrats();
            } else if (stopped) {
                // do nothing
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
                try {
                    runner.runProgram(getRobot());
                } catch (e) {
                    logger.error(e, false);
                    finish();
                    buttons.error();
                    return;
                }
            } else {
                logger.print('function program(robot) not implemented!');
                logger.print('Info: if you clean your code you will get info about commands')
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

    var printError = function(error) {
        console.error(error.message);
        console.print('Please check browser console and try again.');
        buttons.enableAll();
        throw error;
    }

    var compileCommands = function(onSuccess) {
        logger.print('Uploading program...');
        try {
            var robot = getRobot();
            runner.compileProgram(robot);
        } catch (e) {
            logger.error(e, true);
            finish();
            buttons.error();
            buttons.enableReset();
            return;
        }
        onSuccess();
    }

    var partsQueue = [];
    var sendCode = function() {
        var date = new Date();
        var code = runner.getValue();
        var MAX_LENGTH = 16000;
        var count = Math.ceil(code.length / MAX_LENGTH);
        partsQueue = [];
        while (code.length > MAX_LENGTH) {
            var part = code.substring(0, MAX_LENGTH);
            code = code.substring(MAX_LENGTH, code.length);
            partsQueue.push(part);
        }
        if (code != '') {
            partsQueue.push(code);
        }

        sendAllParts(date, 0, count);
    }

    var partsToSend = [];

    var sendAllParts = function(date, index, count) {
        if (partsQueue.length == 0) {
            return;
        }

        while (partsQueue.length != 0) {
            var part = partsQueue.shift();
            partsToSend.push(preparePart(date, part, index, count));
            index++;
        }
    }

    var preparePart = function(date, part, index, count) {
        var SEP = "|$%&|";
        return "message('" +
                    game.playerId + SEP +
                    date.getTime() + SEP +
                    index + SEP +
                    count + SEP +
                    part +
                "')";
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
        logger.clean();
        logger.printHello();
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
        if (partsToSend.length != 0) {
            socket.send(partsToSend.shift());
            return;
        }

        var command = popLastCommand();
        if (!!command && command != 'WAIT') {
            logger.print('Hero do "' + command + '"');
            if (game.demo) {
                if (command == 'RESET') {
                    runner.cleanProgram();
                }
            }
        } else {
            // logger.print('Waiting for next command...');
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
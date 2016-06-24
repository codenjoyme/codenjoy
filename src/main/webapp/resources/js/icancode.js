game.enableDonate = false;
game.enableJoystick = true;
game.enableAlways = true;
game.enablePlayerInfo = false;
game.enableLeadersTable = false;
game.enableChat = false;
game.enableHotkeys = true;
game.enableAdvertisement = false;
game.showBody = false;

game.onMainPageLoad = function() {
    window.location.replace('/register');
}

game.onRegistrationPageLoad = function() {
    initLayout('icancode', 'register.html', game.contextPath,
        ['js/ace/src/ace.js'],
        function() {
            $(document.body).show();
        });
}

game.onBoardAllPageLoad = function() {
    initLayout('icancode', 'leaderboard.html', game.contextPath,
        [],
        function() {
            initLeadersTable(game.contextPath, game.playerName, game.code,
                    function() {
                    },
                    function(count, you, link, name, score, maxLength, level) {
                        var star = '';
                        if (count == 1) {
                            star = 'first';
                        } else if (count < 3) {
                            star = 'second';
                        }
                        return '<tr>' +
                                '<td><span class="' + star + ' star">' + count + '<span></td>' +
                                '<td>' + you + '<a href="' + link + '">' + name + '</a></td>' +
                                '<td class="center">' + score + '</td>' +
                            '</tr>';
                    });
            $('#table-logs').removeClass('table');
            $('#table-logs').removeClass('table-striped');
            $(document.body).show();
        });
}

game.onBoardPageLoad = function() {
    initLayout('icancode', 'board.html', game.contextPath,
        ['js/ace/src/ace.js'],
        function() {
            var starting = true;

            var editor = ace.edit('ide-block');
            var defaultEditorValue = editor.getValue();
            editor.setTheme('ace/theme/monokai');
            editor.session.setMode('ace/mode/javascript');
            editor.setOptions({
                fontSize: '14pt'
            });
            editor.on('focus', function() {
                game.enableJoystick = false;
            });
            editor.on('blur', function() {
                game.enableJoystick = true;
            });
            var typeCounter = 0;
            editor.on('change', function() {
                if (!starting && editor.getValue() == '') {
                    editor.setValue(defaultEditorValue, 1);
                }
                if (typeCounter++ % 10 == 0) {
                    saveSettings();
                }
            });

            var resetButton = $('#ide-reset');
            var releaseButton = $('#ide-release');
            var commitButton = $('#ide-commit');
            var console = $("#ide-console");
            console.empty();

            var print = function(message) {
                console.append('> ' + message + '<br>')
                console.animate({scrollTop: console.prop('scrollHeight')});
            }

            var error = function(message) {
                print("Error: " + message);
            }

            var replace = function(string, from, to) {
                return string.split(from).join(to);
            }

            var commands = [];
            var controlling = false;
            var lastCommand = null;

            var robot = {
                goLeft : function() {
                    commands.push("LEFT");
                },
                goRight : function() {
                    commands.push("RIGHT");
                },
                goUp : function() {
                    commands.push("UP");
                },
                goDown : function() {
                    commands.push("DOWN");
                },
                jump : function() {
                    commands.push("JUMP");
                },
                jumpLeft : function() {
                    commands.push("JUMP,LEFT");
                    commands.push("WAIT");
                },
                jumpRight : function() {
                    commands.push("JUMP,RIGHT");
                    commands.push("WAIT");
                },
                jumpUp : function() {
                    commands.push("JUMP,UP");
                    commands.push("WAIT");
                },
                jumpDown : function() {
                    commands.push("JUMP,DOWN");
                    commands.push("WAIT");
                }
            };

            var sleep = function(onSuccess) {
                setTimeout(function(){
                    onSuccess();
                }, 1000);
            }

            var send = function(command) {
                if (socket == null) {
                    connect(function() {
                        socket.send(command);
                    });
                } else {
                    socket.send(command);
                }
            }

            var compileCommands = function(onSuccess) {
                var code = editor.getValue();
                print('Compiling program...');
                try {
                    eval(code);
                } catch (e) {
                    error(e.message);
                    print('Please try again.');
                    enableAll();
                    return;
                }

                sleep(function() {
                    print('Uploading program...');

                    sleep(function() {
                        print('Running program...');

                        try {
                            justDoIt(robot);
                        } catch (e) {
                            error(e.message);
                            print('Please try again.');
                            enableAll();
                            return;
                        }

                        sleep(function() {
                            onSuccess();
                        });
                    });
                });
            }

            var encode = function(command) {
                command = replace(command, 'WAIT', '');
                command = replace(command, 'JUMP', 'ACT(1)');
                command = replace(command, 'RESET', 'ACT(0)');
                return command;
            }

            var processCommands = function() {
                if (commands.length == 0) {
                    controlling = false;
                    enableAll();
                }
                if (controlling) {
                    if (commands.length > 0) {
                        var command = commands.shift();
                        lastCommand = command;
                        send(encode(command));
                    }
                }
            }

            var socket = null;
            var connect = function(onSuccess) {
                var hostIp = window.location.hostname;
                var port = window.location.port;
                var server = 'ws://' + hostIp + ':' + port + '/codenjoy-contest/ws';

                socket = new WebSocket(server + '?user=' + game.playerName);

                socket.onopen = function() {
                    print('Connected to Robo!');
              		print('Hi ' + game.playerName + '! I am Robo! You can code your program for me.');
                    if (!!onSuccess) {
                        onSuccess();
                    }
                }

                socket.onclose = function(event) {
                    controlling = false;
                    if (event.wasClean) {
                        print('Disconnected successfully!');
                    } else {
                        print('Signal lost! Code: ' + event.code + ' reason: ' + event.reason);
                    }
                }

                socket.onmessage = function(event) {
                    var data = event.data;
                    if (lastCommand != null) {
                        if (lastCommand != 'WAIT') {
                            print('Robo do ' + lastCommand);
                        }
                        lastCommand = null;
                    }
                    processCommands();
                }

                socket.onerror = function(error) {
                    error(error);
                    socket = null;
                }
            }

            var cleanCommands = function() {
                commands = [];
            }

            var resetCommand = function() {
                commands = ['RESET'];
            }

            var jumpCommand = function() {
                commands = ['JUMP'];
            }

            var startControlling = function() {
                controlling = true;
            }

            var enable = function(button, enable) {
                button.prop("disabled", !enable);
            }

            var enableAll = function() {
                enable(resetButton, true);
                enable(releaseButton, true);
                enable(commitButton, true);
            }

            var disableAll = function() {
                enable(resetButton, false);
                enable(releaseButton, false);
                enable(commitButton, false);
            }

            resetButton.click(function() {
                disableAll();

                resetCommand();

                if (!controlling) {
                    startControlling();
                    processCommands();
                }
            });

            releaseButton.click(function() {
                disableAll();

                cleanCommands();
                resetCommand();
                compileCommands(function() {
                    startControlling();
                    processCommands();

                    enable(resetButton, true);
                });
            });

            commitButton.click(function() {
                disableAll();

                cleanCommands();
                compileCommands(function() {
                    startControlling();
                    processCommands();

                    enable(resetButton, true);
                });
            });

            disableAll();
            $(document.body).show();

            var saveSettings = function() {
                localStorage.setItem('editor.code', editor.getValue());
                var position =  editor.selection.getCursor();
                localStorage.setItem('editor.cursor.position.column', position.column);
                localStorage.setItem('editor.cursor.position.row', position.row);
                editor.selection.getCursor()
            }
            var loadSettings = function() {
                try {
                    editor.setValue(localStorage.getItem('editor.code'));
                    var column = localStorage.getItem('editor.cursor.position.column');
                    var row = localStorage.getItem('editor.cursor.position.row');
                    editor.focus();
                    editor.selection.moveTo(row, column);
                } catch (e) {
                    // do nothing
                }
            }
            $(window).unload(saveSettings);

            loadSettings();

            if (!!game.code) {
                connect(function() {
                    enableAll();
                });
            }
            starting = false;
        });
}
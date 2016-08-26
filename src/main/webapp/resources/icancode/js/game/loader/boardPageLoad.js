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
/**
 * Created by Mikhail_Udalyi on 08.08.2016.
 */

var currentLevel = -1;

var boardPageLoad = function() {
    var starting = true;

    var libs = game.contextPath + 'resources/' + game.gameName + '/js';
    if (game.demo) {
        libs = 'js';
    }

    // ----------------------- disable backspace -------------------
    $(document).on('keydown', function(e) {
        if (e.which === 8 && !$(e.target).is('input, textarea')) {
            e.preventDefault();
        }
    });
    // ----------------------- init scrollbar -------------------
    $(".content").mCustomScrollbar({
        theme:'dark-2',
        axis: 'yx',
        mouseWheel : { enable : true }
    });

    // ----------------------- init progressbar scrollbar -------------------
    var scrollProgress = function() {
        $(".trainings").mCustomScrollbar("scrollTo", ".level-current");
    }

    // ----------------------- init befunge -------------------
    initBefunge();

    // ----------------------- init ace editor -------------------
// TODO uncomment editor
//    var editor = initEditor(libs, 'ide-block', autocomplete);
//    editor.on('focus', function() {
//        game.enableJoystick = false;
//    });
//    editor.on('blur', function() {
//        game.enableJoystick = true;
//    });
    var typeCounter = 0;
    var clean = null;
// TODO uncomment editor
//    editor.on('change', function() {
//        if (!game.code) {
//            return;
//        }
//
//        if (!starting && editor.getValue() == '') {
//            clean = 0;
//        }
//
//        if (typeCounter++ % 10 == 0) {
//            saveSettings();
//        }
//    });
    $('body').bind("tick", function() {
        if (!game.code) {
            return;
        }

        if (clean != null) {
            clean++;
            if (clean == 2) {
                clean = null;
// TODO uncomment editor
//                if (editor.getValue() == '') {
//                    editor.setValue(getDefaultEditorValue(), 1);
//                } else if (editor.getValue() == 'win') {
//                    editor.setValue(getWinEditorValue(), 1);
//                } else if (editor.getValue() == 'ref') {
//                    editor.setValue(getRefactoringEditorValue(), 1);
//                }
            }
        }
    });

    // ----------------------- init progressbar -------------------
    var progressBar = initProgressbar('progress-bar');
    progressBar.setProgress = function(current, lastPassed) {
        for (var i = 0; i <= lastPassed; ++i) {
            this.done(i);
        }
        this.process(lastPassed + 1);
        this.active(current);
    }
    progressBar.click(function(event) {
        if (!game.code) {
            return;
        }

        var element = $(event.target);

        if (element.hasClass('level-not-active')) {
            return;
        }

        var level = element.attr('level');
        if (currentLevel == level - 1) {
            return;
        }

        socket.send('LEVEL' + level);
    });

    // ----------------------- init tooltip -------------------
    $('[data-toggle="tooltip"]').tooltip();

    var oldLastPassed = 0;
    // ----------------------- update progressbar -------------------
    $('body').bind("board-updated", function(events, data) {
        if (game.playerName == '' || !data[game.playerName]) {
            return;
        }

        $('body').trigger("tick");

        var board = JSON.parse(data[game.playerName].board);

        var level = board.levelProgress.current;
        var multiple = board.levelProgress.multiple;
        var lastPassed = board.levelProgress.lastPassed;
        level = multiple ? (progressBar.length - 1) : level;

        if (oldLastPassed < lastPassed) {
            oldLastPassed = lastPassed;
            showWinWindow();
        }

        if (currentLevel == level) {
            return;
        }
        currentLevel = level;
        initAutocomplete(level);

        progressBar.setProgress(currentLevel, lastPassed);

        scrollProgress();
    });

    var getDefaultEditorValue = function() {
        return getLevelInfo().defaultCode;
    }

    var getWinEditorValue = function() {
        return getLevelInfo().winCode;
    }

    var getRefactoringEditorValue = function() {
        var code = getLevelInfo().refactoringCode;
        if (!!code) {
            return code;
        } else {
            return getWinEditorValue();
        }
    }

    // ----------------------- init console -------------------
    var console = initConsole();
    console.printCongrats = function() {
        console.print('Congrats ' + game.playerName + '! You have passed the puzzle!!!');
    }

    console.printHello = function() {
        console.print('Hello ' + game.playerName + '! I am Robot! Please write your code and press Commit.');
    }

    // ----------------------- init slider -------------------
    var setupSlider = function() {
        $("#console-panel").click(function(){
            if ($("#console").hasClass("open")) {
                $("#console").removeClass("open").addClass("close");
                $("#block").removeClass("console-open").addClass("console-close");
                $("#console-panel-icon").removeClass("fa-angle-right").addClass("fa-angle-left");
            } else {
                $("#console").removeClass("close").addClass("open");
                $("#block").removeClass("console-close").addClass("console-open");
                $("#console-panel-icon").removeClass("fa-angle-left").addClass("fa-angle-right");
            }
        });

        $("#editor-panel").click(function(){
            if (!$("#main").hasClass("editor-fullscreen")) {
                $("#main").addClass("editor-fullscreen");
                $("#editor-panel-icon").removeClass("fa-angle-left").addClass("fa-angle-right");
            } else {
                $("#main").removeClass("editor-fullscreen");
                $("#editor-panel-icon").removeClass("fa-angle-right").addClass("fa-angle-left");
            }
        });
    }
    setupSlider();

    // ----------------------- win window --------------
    var showWinWindow = function() {
        $("#modal-level").removeClass("close");
    };

    var hideWinWindow = function() {
        $("#modal-level").addClass("close");
    };

    var setupWinWindow = function() {
        $("#close-level-modal").click(function(){
            hideWinWindow();
        });
        $("#next-level-modal").click(function(){
            hideWinWindow();
        });
        $("#previous-level-modal").click(function(){
            hideWinWindow();
            $(progressBar[0]).click();
        });
        $("body").keydown(function(event){
            if (event.which == 27){
                $("#close-level-modal").click();
            }
        });
    };
    setupWinWindow();

    // ----------------------- init buttons -------------------
    var onCommitClick = function() {
        buttons.disableAll();
        resetRobot();
        controller.commit();
    }
    var onResetClick = function() {
        buttons.disableAll();
        controller.reset();
    }
    // ----------------------------------------------------------
    var sleep = function(onSuccess) {
        setTimeout(function(){
            onSuccess();
        }, 1000);
    }

    var buttons = initButtons(onCommitClick, onResetClick);

    var onSocketMessage = function(data) {
        controller.onMessage(data);
    }
    var onSocketClose = function() {
        controller.reconnect();
    }
    var socket = initSocket(game, buttons, console, onSocketMessage, onSocketClose);

    var controller = initController(socket, console, buttons, function() {
        return robot;
    });

    var robot = null;
    var resetRobot = function() {
        robot = initRobot(console, controller);
    }
    resetRobot();

    // ----------------------- save ide code -------------------
    var saveSettings = function() {
// TODO uncomment editor
//        var text = editor.getValue();
//        if (!!text && text != '') {
//            localStorage.setItem('editor.code', editor.getValue());
//            var position =  editor.selection.getCursor();
//            localStorage.setItem('editor.cursor.position.column', position.column);
//            localStorage.setItem('editor.cursor.position.row', position.row);
//            editor.selection.getCursor()
//        }
    }
    var loadSettings = function() {
// TODO uncomment editor
//        try {
//            var text = localStorage.getItem('editor.code');
//            if (!!text && text != '') {
//                editor.setValue(text);
//                var column = localStorage.getItem('editor.cursor.position.column');
//                var row = localStorage.getItem('editor.cursor.position.row');
//                editor.focus();
//                editor.selection.moveTo(row, column);
//            } else {
//                editor.setValue(getDefaultEditorValue());
//            }
//        } catch (e) {
//            // do nothing
//        }
    }
    $(window).on('unload', saveSettings);


    // ----------------------- starting UI -------------------
    if (game.demo) {
        var data = '{"' + game.playerName + '":{"board":"{\\"levelProgress\\":{\\"total\\":18,\\"current\\":3,\\"lastPassed\\":2,\\"multiple\\":false},\\"layers\\":[\\"OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCDDDDEOOOOOOOOOOJaBB9FOOOOOOOOOOIHHHHGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO\\",\\"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\\"]}","gameName":"icancode","score":150,"maxLength":0,"length":0,"level":1,"boardSize":16,"info":"","scores":"{\\"fasdfddd@gmail.com\\":150,\\"SDAsd@sas.as\\":2250}","coordinates":"{\\"fasdfddd@gmail.com\\":{\\"y\\":8,\\"x\\":9},\\"SDAsd@sas.as\\":{\\"y\\":8,\\"x\\":9}}"}}';
        $('body').trigger('board-updated', JSON.parse(data));
    }
    buttons.disableAll();
    $(document.body).show();

    if (!!game.code) {
        loadSettings();

        socket.connect(function() {
            buttons.enableAll();
        });
    } else {
        buttons.enable(helpButton, false);

        var link = $('#register-link').attr('href');
        console.print('<a href="' + link + '">Please register</a>');

// TODO uncomment editor
//        editor.setValue(
//            'function program(robot) {\n' +
//            '    // PLEASE REGISTER\n' +
//            '}');
    }
    starting = false;
};
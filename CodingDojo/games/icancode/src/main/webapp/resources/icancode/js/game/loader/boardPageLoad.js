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
/**
 * Created by Mikhail_Udalyi on 08.08.2016.
 */

var boardPageLoad = function() {

    if (game.debug) {
        game.debugger();
    }

    var libs = game.contextPath + '/resources/' + game.gameName + '/js';
    if (game.demo) {
        libs = 'js';
    }

    // ----------------------- disable backspace -------------------
    $(document).on('keydown', function(e) {
        if ((e.which === 8 || e.which === 32
            || e.which === 37 || e.which === 38
            || e.which === 39 || e.which === 40)
                && !$(e.target).is('input, textarea'))
        {
            e.preventDefault();
        }
    });
    // ----------------------- init tooltip -------------------
    $('[data-toggle="tooltip"]').tooltip();

    // ----------------------- init logger -------------------
    var logger = initLogger();
    logger.printCongrats = function() {
        logger.print('Congrats ' + game.readableName + '! You have passed the puzzle!!!');
    }

    logger.printHello = function() {
        logger.print('Hello ' + game.readableName + ', I am Hero! Waiting for your command...');
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
            levelProgress.selectLevel(1);
        });
        $("body").keydown(function(event){
            if (event.which == 27){
                $("#close-level-modal").click();
            }
        });
    };
    setupWinWindow();

    // ----------------------- init help modal -------------------
    $("#close").click(function(){
        $("#modal").addClass("close");
    });
    $("body").keydown(function(event){
        if (event.which == 27){
            $("#close").click();
        }
    });

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
    var onHelpClick = function() {
        var level = levelProgress.getCurrentLevel();
        var multiple = levelProgress.isCurrentLevelMultiple();

        if (!multiple) {
            var help = levelInfo.getInfo(level).help;
            $('#ide-help-window').html(help);
            $("#modal").removeClass("close");
        } else {
            window.open('/codenjoy-contest/resources/icancode/landing-training.html', '_blank');
            window.focus();
        }
    };
    var buttons = initButtons(onCommitClick, onResetClick, onHelpClick);

    // ----------------------- init storage -------------------
    var storage = {
        getKey : function(property) {
            return property + '[' + game.playerName + ']';
        },
        load : function(property) {
            return JSON.parse(localStorage.getItem(this.getKey(property)));
        },
        save : function(property, data) {
            localStorage.setItem(this.getKey(property), JSON.stringify(data));
        }
    };

    // ----------------------- init runner -------------------
    var runner = null;
    if (game.enableBefunge) {
        runner = initRunnerBefunge(logger, storage);
    } else {
        var getCurrentLevelInfo = function(){
            return levelInfo.getInfo(levelProgress.getCurrentLevel());
        };
        runner = initRunnerJs(game, libs, getCurrentLevelInfo, storage);
    }

    // ------------------------ init socket ----------------------
    var onSocketMessage = function(data) {
        controller.onMessage(data);
    }
    var onSocketClose = function() {
        controller.reconnect();
    }
    var socket = initSocket(game, buttons, logger, onSocketMessage, onSocketClose);

    // ----------------------- init progressbar -------------------
    var oldLastPassed = -1;
    var onUpdate = function(level, multiple, lastPassed) {
        if (oldLastPassed < lastPassed) {
            var isFirstWin = (lastPassed == 0 && level == 1 && oldLastPassed == -1);
            if (isFirstWin || oldLastPassed != -1) {
                showWinWindow();
            }
            oldLastPassed = lastPassed;
        }

        if (game.enableBefunge) {
            runner.levelUpdate(level, multiple, lastPassed);
        }
    }

    var onChangeLevel = function(level) {
        initAutocomplete(level, levelInfo);
    }
    var levelProgress = initLevelProgress(game, socket, onUpdate, onChangeLevel);

    // ------------------------ init controller ----------------------

    var controller = initController(socket, runner, logger, buttons, levelProgress, function() {
        return robot;
    });

    var robot = null;
    var resetRobot = function() {
        robot = initRobot(logger, controller);
    }
    resetRobot();

    // ----------------------- init level info -----------------------------
    var levelInfo = initLevelInfo();

    // ----------------------- starting UI -------------------
    if (game.demo) {
        var data = '{"' + game.playerName + '":{"board":"{"levelProgress":{"total":18,"current":3,"lastPassed":15,"multiple":false},"layers":["OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCDDDDEOOOOOOOOOOJXBBYFOOOOOOOOOOIHHHHGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"]}","gameName":"icancode","score":150,"maxLength":0,"length":0,"level":1,"boardSize":16,"info":"","scores":"{"' + game.playerName + '":150}","coordinates":"{"' + game.playerName + '":{"y":8,"x":9}}"}}';
        $('body').trigger('board-updated', JSON.parse(data));
    }
    buttons.disableAll();
    $(document.body).show();

    if (!!game.code) {
        runner.loadSettings();

        socket.connect(function() {
            buttons.enableAll();
        });
    } else {
        buttons.disableHelp();

        var link = $('#register-link').attr('href');
        logger.print('<a href="' + link + '">Please register</a>');

        runner.setStubValue();
    }
};
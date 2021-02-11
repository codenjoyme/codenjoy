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

    // ------------------------ headers/footers/board --------------------
    if (!game.enableHeader) {
        $('.header-container').hide();
    }
    if (!game.enableFooter) {
        $('footer.footer').hide();
    }
    if (!game.drawCanvases) {
        $('#main').addClass('editor-fullscreen');
        $('#editor-panel').hide();
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
    var initWin = function() {
        var close = $("#close-level-modal");
        var next = $("#next-level-modal");
        var previous = $("#previous-level-modal");

        var show = function() {
            $("#modal-level").removeClass("close");
        };

        var hide = function() {
            $("#modal-level").addClass("close");
        };

        close.click(function(){
            hide();
        });

        next.click(function(){
            hide();
        });

        previous.click(function(){
            hide();
            levelProgress.selectLevel(levelsStartsFrom1);
        });

        $("body").keydown(function(event){
            if (event.which == 27){
                close.click();
            }
        });
        return {
            show : show,
            hidePrevious: function() {
                // previous.hide() мы не можем использовать потому
                // что элемент еще не отображен
                previous.css('visibility', 'hidden');
            }
        };
    };
    var win = initWin();
    if (game.enableBefunge) {
        win.hidePrevious();
    }

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
            var help = levelInfo.getLevel(level).help;
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
            return property + '[' + game.playerId + ']';
        },
        load : function(property) {
            return JSON.parse(localStorage.getItem(this.getKey(property)));
        },
        save : function(property, data) {
            localStorage.setItem(this.getKey(property), JSON.stringify(data));
        }
    };
    // ----------------------- init level info -----------------------------
    var levelInfo = initLevelInfo(game.contextPath);
    levelInfo.load(
        function() {
            nextStep();
        },
        function(error){
            alert('Error when loading levels from server: ' + error);
        });
    var getCurrentLevelInfo = function(level){
        var forLevel = (typeof level != 'undefined') ? level : levelProgress.getCurrentLevel();
        return levelInfo.getLevel(forLevel);
    };

    var runner = null;
    var levelProgress = null;
    var controller = null;
    var resetRobot = null;

    var nextStep = function() {
        // ----------------------- init runner -------------------
        if (game.enableBefunge) {
            runner = initRunnerBefunge(logger, getCurrentLevelInfo, storage);
        } else {
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
        var onChangeLevel = function(level, multiple, lastPassed, isLevelIncreased, isWin) {
            if (isWin) {
                win.show();
            }
            if (isLevelIncreased) {
                runner.levelUpdate(level, multiple, lastPassed);
            }
            initAutocomplete(level, levelInfo);
        }
        levelProgress = initLevelProgress(game, onChangeLevel);

        // ------------------------ init controller ----------------------
        controller = initController(socket, runner, logger, buttons, levelProgress, function() {
            return robot;
        });

        var robot = null;
        resetRobot = function() {
            robot = initRobot(logger, controller);
        }
        resetRobot();

        // ----------------------- starting UI -------------------
        if (game.demo) {
            var data = '{"' + game.playerId + '":{"board":"{"levelProgress":{"total":18,"current":3,"lastPassed":15,"multiple":false},"layers":["OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCDDDDEOOOOOOOOOOJXBBYFOOOOOOOOOOIHHHHGOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"]}","gameName":"icancode","score":150,"maxLength":0,"length":0,"level":1,"boardSize":16,"info":"","scores":"{"' + game.playerId + '":150}","coordinates":"{"' + game.playerId + '":{"y":8,"x":9}}"}}';
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
    }
};
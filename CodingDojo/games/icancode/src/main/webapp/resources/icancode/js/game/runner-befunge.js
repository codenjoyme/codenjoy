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

/*
 * Based on work by http://www.elated.com is licensed under a Creative Commons Attribution 3.0 Unported License (http://creativecommons.org/licenses/by/3.0/)
 * From http://www.elated.com/res/File/articles/development/javascript/jquery/drag-and-drop-with-jquery-your-essential-guide/card-game.html
 **/
function initRunnerBefunge(logger, getLevelInfo, storage) {

    if (game.debug) {
        game.debugger();
    }

    var defaultSize = 11;
    var width = defaultSize;
    var height = defaultSize;

    var readyForSaving = false;

    var container = $('#ide-content');
    container.empty();
    container.append(
        '<div id="cardPile" class="pile-container"></div>' +
        '<div class="slots-parent">' +
        '<div id="cardSlots" class="slots-container"></div>' +
        '</div>');

    $('.autocomplete').hide();
    $('#ide-help').hide();

    var createButton = function(id, name, onClick) {
        $('.bottom-panel').append('<button class="button help" id="' + id + '">' + name + '</button>')
        var button = $('#' + id);
        button.click(function () {
            onClick(button);
        });
        return button;
    }

    // TODO дублирование с buttons.js
    var enableButton = function(button, enable) {
        button.prop('disabled', !enable);
    }

    var cleanField = function() {
        moveAllCardsToCardPile();
        resetRows();
    }

    var cleanButton = createButton('ide-clean', 'Clean', function(button) {
        cleanField();
        saveState();
    });

    var undoButton = createButton('ide-undo', 'Undo', function(button) {
        if (undo.length == 0) {
            return;
        }

        undo.pop(); // удаляем текущее состояние
        var data = undo[undo.length - 1]; // грузим прошлое
        updateUndoButton();
        cleanField();
        loadStateFromData(data);
    });

    var commandAllowed = function(level, id) {
        return getLevelInfo(level).befungeCommands.split('\n').includes(id);
    }

    // ------------------------------------- state -----------------------------------
    var cursor = null;
    var direction = Direction.RIGHT;
    var stack = [];
    var proceduralStack = [];
    var running = false;
    var robot = null;

    // ------------------------------------- befunge commands -----------------------------------
    // TODO extract to another class

    var popFromStack = function() {
        if (stack.length == 0) {
            logger.print('Не указано значение для команды. Укажите значение!');
            forceFinish();
            return;
        }
        var value = stack.pop();
        return value;
    }

    // команда берет вершину стека и клонирует ее для возможности использовать дважды
    // если в стеке ничего нет, тогда там появится value null
    // используется для того, чтобы проанализировать вершину стека (например IF командой)
    // не нарушая его структуры для других команд
    var checkStack = function() {
        if (stack.length == 0) {
            stack.push(null);
        } else {
            stack.push(stack[stack.length - 1]);
        }
    }

    var startCommand = function(x, y) {
        cursor = pt(x, y);
        direction = Direction.RIGHT;
        stack = [];
        proceduralStack = [];
        running = true;
    }

    var forceFinish = function() {
        proceduralStack = [];
        finishCommand();
    }

    var justFinishedProcedure = null;
    var finishCommand = function() {
        if (proceduralStack.length != 0) {
            var data = proceduralStack.pop();
            justFinishedProcedure = data.name;
            direction = data.direction;
            cursor = data.pt;
            return true;
        } else {
            stack = [];
            running = false;
            return false;
        }
    }

    var activateProcedure = function(procedureName, x, y) {
        if (justFinishedProcedure == procedureName) {
            justFinishedProcedure = null;
            return false;
        }

        if (proceduralStack.length != 0 && proceduralStack[proceduralStack.length - 1].name == procedureName) {
            return false;
        }

        var toPoint = board.find(procedureName, {x: x, y: y});
        if (!toPoint) {
            return false;
        }

        proceduralStack.push({name: procedureName, direction: direction, pt: pt(x, y)});
        cursor = pt(toPoint.x, toPoint.y);
        direction = Direction.RIGHT;
        return true;
    };

    var cursorMirrorTopBottomCommand = function(x, y) {
        direction = direction.mirrorTopBottom();
    }

    var cursorMirrorBottomTopCommand = function(x, y) {
        direction = direction.mirrorBottomTop();
    }

    var printStackCommand = function(x, y) {
        logger.print('Stack [' + [].concat(stack).reverse() + ']');
    }

    var activateProcedure1Command = function(x, y) {
        return activateProcedure('procedure-1', x, y);
    }

    var activateProcedure2Command = function(x, y) {
        return activateProcedure('procedure-2', x, y);
    }

    var activateProcedure3Command = function(x, y) {
        return activateProcedure('procedure-3', x, y);
    }

    var scannerAtCommand = function(x, y) {
        var oldValue = popFromStack();
        try {
            var value = robot.getScanner().at(oldValue);
            if (value == 'LASER_LEFT' || value == 'LASER_RIGHT' || value == 'LASER_UP' || value == 'LASER_DOWN') {
                value = 'LASER'; // TODO это костыль, надо сделать потом все направления лазеров
            }
            if (value == 'LASER_MACHINE_READY' || value == 'LASER_MACHINE') {
                value = 'LASER_MACHINE'; // TODO это тоже костыль
            }
            if (value == 'ZOMBIE_DIE') {
                value = 'NONE'; // TODO это тоже костыль
            }
        } catch (error) {
            logger.print('Неправильно значение для сканнера: "' + oldValue + '"');
            forceFinish();
            return;
        }
        stack.push(value);
    }

    var robotCameFromCommand = function(x, y) {
        var value = robot.cameFrom();
        stack.push(value);
    }

    var robotPreviousDirectionCommand = function(x, y) {
        var value = robot.previousDirection();
        stack.push(value);
    }

    var robotShortestWay = function(x, y) {
        var value = popFromStack();
        var scanner = robot.getScanner();
        var ptFrom = scanner.getMe();

        var path = scanner.getShortestWay(value);
        for (var index in path) {
            if (index == 0) {
                continue;
            }

            var ptTo = path[index];
            var direction = Direction.where(ptFrom, ptTo);
            // добавляем не в конец стека, а в голову
            stack.unshift(direction.name());
            ptFrom = ptTo;
        }
    }

    var robotGoLeftCommand = function(x, y) {
        robot.go('LEFT');
    }

    var robotGoRightCommand = function(x, y) {
        robot.go('RIGHT');
    }

    var robotGoUpCommand = function(x, y) {
        robot.go('UP');
    }

    var robotGoDownCommand = function(x, y) {
        robot.go('DOWN');
    }

    var robotGoCommand = function(x, y) {
        var value = popFromStack();
        robot.go(value);
    }

    var robotJumpLeftCommand = function(x, y) {
        robot.jump('LEFT');
    }

    var robotJumpRightCommand = function(x, y) {
        robot.jump('RIGHT');
    }

    var robotJumpUpCommand = function(x, y) {
        robot.jump('UP');
    }

    var robotJumpDownCommand = function(x, y) {
        robot.jump('DOWN');
    }

    var robotJumpCommand = function(x, y) {
        var value = popFromStack();
        robot.jump(value);
    }

    var robotPullCommand = function(x, y) {
        var value = popFromStack();
        robot.pull(value);
    }

    var robotFireCommand = function(x, y) {
        var value = popFromStack();
        robot.fire(value);
    }

    var ifCommand = function(x, y) {
        var leftValue = popFromStack();
        var point = direction.change(cursor);
        board.processCard(point.getX(), point.getY(), true);
        var rightValue = popFromStack();
        if (leftValue == rightValue) {
            direction = direction.contrClockwise();
        } else {
            direction = direction.clockwise();
        }
    }

    var valueLeftCommand = function(x, y) {
        stack.push('LEFT');
    }

    var valueRightCommand = function(x, y) {
        stack.push('RIGHT');
    }

    var valueUpCommand = function(x, y) {
        stack.push('UP');
    }

    var valueDownCommand = function(x, y) {
        stack.push('DOWN');
    }

    var valueGroundCommand = function(x, y) {
        stack.push('NONE');
    }

    var valueWallCommand = function(x, y) {
        stack.push('WALL');
    }

    var valueNullCommand = function(x, y) {
        stack.push(null);
    }

    var valueStartCommand = function(x, y) {
        stack.push('START');
    }

    var valueEndCommand = function(x, y) {
        stack.push('EXIT');
    }

    var valueBoxCommand = function(x, y) {
        stack.push('BOX');
    }

    var valueHoleCommand = function(x, y) {
        stack.push('HOLE');
    }

    var valueGoldCommand = function(x, y) {
        stack.push('GOLD');
    }

    var valueZombieCommand = function(x, y) {
        stack.push('ZOMBIE');
    }

    var valueLaserMachineCommand = function(x, y) {
        stack.push('LASER_MACHINE');
    }

    var valueLaserCommand = function(x, y) {
        stack.push('LASER');
    }

    // ------------------------------------- commands -----------------------------------
    var gameName = game.sprites;
    var commands = [
        {
            id: 'start',
            type: 1,
            title: 'start',
            process: startCommand,
            description: 'Выполнение команд начинается тут.',
            // hidden: true, // так можно сделать если хочется временно спрятать команду
            img1: 'start.png'
        },

        {
            id: 'finish',
            type: 1,
            title: 'finish',
            process: finishCommand,
            description: 'Выполнение команд останавливается тут.',
            img1: 'finish.png'
        },

        {
            id: 'mirror-top-bottom',
            type: 1,
            title: 'mirror-top-bottom',
            process: cursorMirrorTopBottomCommand,
            description: 'Зеркало изменяет направление движения командного курсора.',
            img1: 'mirror-top-bottom.png'
        },

        {
            id: 'mirror-bottom-top',
            type: 1,
            title: 'mirror-bottom-top',
            process: cursorMirrorBottomTopCommand,
            description: 'Зеркало изменяет направление движения командного курсора.',
            img1: 'mirror-bottom-top.png'
        },

        {
            id: 'print-stack',
            type: 1,
            title: 'print-stack',
            process: printStackCommand,
            description: 'Напечатать в консоли все значения, сохраненные командами.',
        },

        {
            id: 'shortest-way',
            type: 1,
            title: 'shortest-way', // TODO дублирование с id устранить
            process: robotShortestWay,
            description: 'Найти кратчайший путь к элементу. Весь путь сохранится для последующих команд.',
        },

        {
            id: 'check-stack',
            type: 1,
            title: 'check-stack',
            process: checkStack,
            description: 'Если другие команды ранее не сохраняли никаких значений - ведет себя как NULL.',
        },

        {
            id: 'procedure-1',
            type: 1,
            title: 'procedure-1',
            process: activateProcedure1Command,
            description: 'Вызов воспомогательной процедуры №1. Процедура должна быть так же объявлена на поле.',
            img1: 'procedure-1-1.png',
            img2: null,
            img3: 'procedure-1.png',
        },

        {
            id: 'procedure-2',
            type: 1,
            title: 'procedure-2',
            process: activateProcedure2Command,
            description: 'Вызов воспомогательной процедуры №2. Процедура должна быть так же объявлена на поле.',
            img1: 'procedure-2-1.png',
            img2: null,
            img3: 'procedure-2.png',
        },

        {
            id: 'procedure-3',
            type: 1,
            title: 'procedure-3',
            process: activateProcedure3Command,
            description: 'Вызов воспомогательной процедуры №3. Процедура должна быть так же объявлена на поле.',
            img1: 'procedure-3-1.png',
            img2: null,
            img3: 'procedure-3.png',
        },

        {
            id: 'if',
            type: 1,
            title: 'if',
            process: ifCommand,
            description: 'Оператор ветвления. Если значения по обе стороны команды равны - поворот командного курсора направо, если не равны - поворот курсора налево.',
            img1: 'if-1.png',
            img2: 'if.png'
        },

        {
            id: 'scanner-at',
            type: 1,
            title: 'scanner-at',
            process: scannerAtCommand,
            description: 'Сканер позволяет определить, что находится на поле вокруг героя. Сторону необходимо указать предварительно.',
            img1: 'scanner-at-left.png',
            img2: 'scanner-at-right.png',
            img3: gameName + '/value-left.png',
            img4: gameName + '/value-right.png'
        },

        {
            id: 'robot-came-from',
            type: 1,
            title: 'robot-came-from',
            process: robotCameFromCommand,
            description: 'Указывает откуда пришел герой только что. Если герой не двигался - команда вернет NULL.',
            img1: 'robot-came-from.png'
        },

        {
            id: 'robot-previous-direction',
            type: 1,
            title: 'robot-previous-direction',
            process: robotPreviousDirectionCommand,
            description: 'Указывает куда ходил герой в прошлый раз. Если герой не двигался - команда вернет NULL.',
            img1: 'robot-previous-direction.png'
        },

        {
            id: 'robot-left',
            type: 2,
            title: 'robot-left',
            process: robotGoLeftCommand,
            description: 'Команда герою двигаться влево.',
            img1: 'robot-left-1.png',
            img2: gameName + '/robot-left.png'
        },

        {
            id: 'robot-right',
            type: 2,
            title: 'robot-right',
            process: robotGoRightCommand,
            description: 'Команда герою двигаться вправо.',
            img1: 'robot-right-1.png',
            img2: gameName + '/robot-right.png'
        },

        {
            id: 'robot-up',
            type: 2,
            title: 'robot-up',
            process: robotGoUpCommand,
            description: 'Команда герою двигаться вверх.',
            img1: 'robot-up-1.png',
            img2: gameName + '/robot-up.png'
        },

        {
            id: 'robot-down',
            type: 2,
            title: 'robot-down',
            process: robotGoDownCommand,
            description: 'Команда герою двигаться вниз.',
            img1: 'robot-down-1.png',
            img2: gameName + '/robot-down.png'
        },

        {
            id: 'robot-go',
            type: 2,
            title: 'robot-go',
            process: robotGoCommand,
            description: 'Команда герою двигаться в заданном направлении. Сторону необходимо указать предварительно.',
            img1: 'robot-go-left.png',
            img2: 'robot-go-right.png',
            img3: gameName + '/robot-left.png',
            img4: gameName + '/robot-right.png'
        },

        {
            id: 'robot-jump-left',
            type: 2,
            title: 'robot-jump-left',
            process: robotJumpLeftCommand,
            description: 'Команда герою прыгнуть влево.',
            img1: 'robot-jump-left-1.png',
            img2: gameName + '/robot-jump-left.png'
        },

        {
            id: 'robot-jump-right',
            type: 2,
            title: 'robot-jump-right',
            process: robotJumpRightCommand,
            description: 'Команда герою прыгнуть направо.',
            img1: 'robot-jump-right-1.png',
            img2: gameName + '/robot-jump-right.png'
        },

        {
            id: 'robot-jump-up',
            type: 2,
            title: 'robot-jump-up',
            process: robotJumpUpCommand,
            description: 'Команда герою прыгнуть вверх.',
            img1: 'robot-jump-up-1.png',
            img2: gameName + '/robot-jump-up.png'
        },

        {
            id: 'robot-jump-down',
            type: 2,
            title: 'robot-jump-down',
            process: robotJumpDownCommand,
            description: 'Команда герою прыгнуть вниз.',
            img1: 'robot-jump-down-1.png',
            img2: gameName + '/robot-jump-down.png'
        },

        {
            id: 'robot-jump',
            type: 2,
            title: 'robot-jump',
            process: robotJumpCommand,
            description: 'Команда герою прыгнуть в заданном направлении. Cторону необходимо указать предварительно.',
            img1: 'jump-left.png',
            img2: 'jump-right.png',
            img3: gameName + '/robot-jump-left.png',
            img4: gameName + '/robot-jump-right.png'
        },

        {
            id: 'robot-pull',
            type: 2,
            title: 'robot-pull',
            process: robotPullCommand,
            description: 'Команда герою тянуть/толкать в заданном направлении. Cторону необходимо указать предварительно.',
            img1: 'pull-left.png',
            img2: 'pull-right.png',
            img3: gameName + '/robot-pull-left.png',
            img4: gameName + '/robot-pull-right.png'
        },

        {
            id: 'robot-fire',
            type: 2,
            title: 'robot-fire',
            process: robotFireCommand,
            description: 'Команда герою стрелять в заданном направлении. Cторону необходимо указать предварительно.',
            img1: 'fire-left.png',
            img2: 'fire-right.png',
            img3: gameName + '/robot-fire-left.png',
            img4: gameName + '/robot-fire-right.png'
        },

        {
            id: 'value-left',
            type: 3,
            title: 'value-left',
            process: valueLeftCommand,
            description: 'Указание направления "влево". Используется совместно с другими командами.',
            img1: 'value-left-2.png',
            img2: 'value-left-1.png',
            img3: gameName +'/value-left.png'
        },

        {
            id: 'value-right',
            type: 3,
            title: 'value-right',
            process: valueRightCommand,
            description: 'Указание направления "направо". Используется совместно с другими командами.',
            img1: 'value-right-2.png',
            img2: 'value-right-1.png',
            img3: gameName + '/value-right.png'
        },

        {
            id: 'value-up',
            type: 3,
            title: 'value-up',
            process: valueUpCommand,
            description: 'Указание направления "вверх". Используется совместно с другими командами.',
            img1: 'value-up-2.png',
            img2: 'value-up-1.png',
            img3: gameName + '/value-up.png'
        },

        {
            id: 'value-down',
            type: 3,
            title: 'value-down',
            process: valueDownCommand,
            description: 'Указание направления "вниз". Используется совместно с другими командами.',
            img1: 'value-down-2.png',
            img2: 'value-down-1.png',
            img3: gameName + '/value-down.png'
        },

        {
            id: 'value-null',
            type: 3,
            title: 'value-null',
            process: valueNullCommand,
            description: 'Специальное значение NULL. Возвращается командой, когда ей нечего ответить.',
        },

        {
            id: 'value-wall',
            type: 3,
            title: 'value-wall',
            process: valueWallCommand,
            description: 'Значние "Недосягаемо". Испольузется совместно с другими командами.',
            img1: gameName + '/cloud.png'
        },

        {
            id: 'value-ground',
            type: 3,
            title: 'value-ground',
            process: valueGroundCommand,
            description: 'Значние "Земля". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/floor.png'
        },

        {
            id: 'value-start',
            type: 3,
            title: 'value-start',
            process: valueStartCommand,
            description: 'Значние "Точка старта". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/start.png'
        },

        {
            id: 'value-end',
            type: 3,
            title: 'value-end',
            process: valueEndCommand,
            description: 'Значние "Точка финиша". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/exit.png'
        },

        {
            id: 'value-gold',
            type: 3,
            title: 'value-gold',
            process: valueGoldCommand,
            description: 'Значние - "Золото". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/gold.png'
        },

        {
            id: 'value-box',
            type: 3,
            title: 'value-box',
            process: valueBoxCommand,
            description: 'Значние - "Препятствие". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/box.png'
        },

        {
            id: 'value-hole',
            type: 3,
            title: 'value-hole',
            process: valueHoleCommand,
            description: 'Значние - "Яма". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/hole.png'
        },

        {
            id: 'value-laser-machine',
            type: 3,
            title: 'value-laser-machine',
            process: valueLaserMachineCommand,
            description: 'Значние - "Лазерная машина". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/laser_machine_charging_down.png'
        },

        {
            id: 'value-laser',
            type: 3,
            title: 'value-laser',
            process: valueLaserCommand,
            description: 'Значние - "Лазер". Испольузется совместно с другими командами.',
            img1: '../' + gameName + '/laser_down.png'
        },

        {
            id: 'value-zombie',
            type: 3,
            title: 'value-zombie',
            process: valueZombieCommand,
            description: 'Значние - "Зомби". Испольузется совместно с другими командами.',
            img1: 'zombies.png'
        }
    ];

    // ------------------------------------- save state -----------------------------------
    var undo = [];
    var updateUndoButton = function() {
        enableButton(undoButton, undo.length > 0);
        undoButton.text('Undo (' + undo.length + ')');
    }
    updateUndoButton();
    var pushSave = function(data) {
        undo.push(data);
        if (undo.length > 100) {
            undo.shift();
        }
        updateUndoButton();
    }
    var saveState = function() {
        if (!readyForSaving) {
            return;
        }

        var data = [];

        for (var y = 0; y < height; y++) {
            data[y] = [];

            for (var x = 0; x < width; x++) {
                data[y][x] = !!board.getSlot(x, y).data('parked') ? board.getCardId(x, y) : null;
            }
        }

        pushSave(data);
        storage.save('editor', data);
    };

    // -------------------------------------- load state -----------------------------------
    var loadState = function() {
        var data = null;
        readyForSaving = false;
        try {
            data = storage.load('editor');
            pushSave(data);
        } catch (err) {
            readyForSaving = true;
            return;
        }
        loadStateFromData(data);
    }

    var loadStateFromData = function(data) {
        readyForSaving = false;

        if (!data || data.length != height) {
            readyForSaving = true;
            return;
        }

        var diff = data[0].length - width;

        for (var i = 0; i < diff; ++i) {
            $('.slot-line').each(function(y, line) {
                var element = $('<div class="card-slot"></div>')
                    .appendTo(line);
                mapSlots[y].push(element);
                initDroppable(element);
            });
            width++;
        }

        if (data[0].length > width) {
            for (var i = data[0].length - width; i > 0; --i) {
                $('#cardSlots').click();
            }
        }

        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                var id = data[y][x];

                if (id == null) {
                    continue;
                }

                $('#cardPile .card-item').each(function(index, element) {
                    var card = $(this);

                    if (card.data('data-type').id != id) {
                        return;
                    }

                    var slot = board.getSlot(x, y);

                    cloneCardOnSlot(card, slot);
                });

            }
        }
        readyForSaving = true;
    };

    // --------------------------------------- cards building ---------------------------------
    var mapSlots = [];
    for (var y = 0; y < height; y++) {
        mapSlots[y] = [];
        var line = $('<div class="slot-line"></div>')
            .appendTo('#cardSlots');
        for (var x = 0; x < width; x++) {
            var element = $('<div class="card-slot"></div>').appendTo(line);
            mapSlots[y][x] = element;
        }
    }

    var buildPileSlots = function() {
        for (var index = 0; index < commands.length; index++) {
            $('<div class="card-slot hidden"></div>')
                .data('data-type', commands[index])
                .appendTo('#cardPile');
        }
    };
    buildPileSlots();

    var addRowBefore = function() {
        $('.slot-line').each(function(y, line) {
            var element = $('<div class="card-slot"></div>')
                .prependTo(line);
            mapSlots[y].unshift(element);
            initDroppable(element);
        });
        width++;

        saveState();
    }

    var addRowAfter = function() {
        $('.slot-line').each(function(y, line) {
            var element = $('<div class="card-slot"></div>')
                .appendTo(line);
            mapSlots[y].push(element);
            initDroppable(element);
        });
        width++;

        saveState();
    }

    var resetRows = function() {
        var delta = width - defaultSize;
        $('.slot-line').each(function(y, line) {
            for (var i = 0; i < delta; i++) {
                mapSlots[y].pop();
                var lastElement = $(line).children().last().remove();
            }
        });
        width = defaultSize;
    }

    $('<div id="add-left" class="add-left">+</div>').appendTo('#cardSlots').click(addRowBefore);
    $('<div id="add-right" class="add-right">+</div>').appendTo('#cardSlots').click(addRowAfter);

    var mapCards = [];
    var createNewOnPile = function(element) {
        var type = $(element).data('data-type');
        var appended = $('<div class="card-item type-' + type.type + ' ' + type.title + '"></div>')
            .data('data-type', type)
            .appendTo(element);
        mapCards.push(appended);
        $('[data-toggle="tooltip"]').tooltip();
    }

    var buildPileCards = function() {
        $('#cardPile .card-slot').each(function(index, element) {
            createNewOnPile(element);
        });
    };
    buildPileCards();

    // -------------------------------------- tooltips -----------------------------------
    // TODO to extract whole html to board.js as template

    var buildTooltips = function() {
        jQuery.each(commands, function(index) {
            var elem =
                '<div class="img-tooltip" style="visibility: hidden">' +
                    '<div class="img-container">' +
                        ((!!commands[index].img1)?'<img src = "../../resources/sprite/icancode/befunge/' + commands[index].img1 + '">':'') +
                        ((!!commands[index].img2)?'<img src = "../../resources/sprite/icancode/befunge/' + commands[index].img2 + '">':'') +
                    '</div>' +
                    '<div class="img-container">' +
                        ((!!commands[index].img3)?'<img src = "../../resources/sprite/icancode/befunge/' + commands[index].img3 + '">':'') +
                        ((!!commands[index].img4)?'<img src = "../../resources/sprite/icancode/befunge/' + commands[index].img4 + '">':'') +
                    '</div>' +
                    '<span class="tooltip-desc">' + commands[index].description + '</span>' +
                '</div>';

            var currentTooltip = null;
            var touchMode = false;
            var showTooltip = function(event) {
                if (touchMode) return false;
         
                var card = $(this);
                var slot = card.parent();
                currentTooltip = card.data('data-type').id;
                touchMode = event.type === 'touchstart';

                setTimeout(function() {
                    var tooltip = card.data('data-type').id;
                    if (tooltip == currentTooltip) {
                        slot.append(elem);
                        var el = slot.find('.img-tooltip');

                        el.prepend('<div class="before-tooltip"></div>');
                        el.append('<div class="after-tooltip"></div>');

                        var changeLeft = function(element, moveLeft) {
                            element.offset({left: element.offset().left + moveLeft});
                        }

                        var rightBound = function(element) {
                            return element.offset().left + element.width();
                        }

                        var leftBound = function(element) {
                            return element.offset().left;
                        }

                        var onLoad = function() {
                            var before = el.find('.before-tooltip');
                            var after = el.find('.after-tooltip');

                            var right = rightBound(el) - rightBound($('#cardPile'));
                            var left = leftBound($('#cardPile')) - leftBound(el);
                            if (right <= 0) {
                                right = 0;
                            }
                            if (left <= 0) {
                                left = 0;
                            }
                            if (right > 0 || left > 0) {
                                var delta = right - left;
                                changeLeft(el, -delta);
                                changeLeft(before, delta);
                                changeLeft(after, delta);
                            }

                            // display:hidden мы не можем использовать потому
                            // что оперируем позицией элемента до его отображения
                            el.css("visibility", "visible");
                        }

                        // если есть изображения в тултипе ждем пока они все загузятся,
                        // иначе ширина тултипа будеь рссчитана неверно
                        var images = $(el).find('img');
                        var loaded = images.length;
                        if (loaded == 0) {
                            onLoad();
                        } else {
                            images.load(function () {
                                loaded--;
                                if (loaded == 0) {
                                    onLoad();
                                }
                            });
                        }
                    } else {
                        slot.find('.img-tooltip').remove();
                    }
                }, 500);
            }

            var hideTooltip = function() {
                if (touchMode) return false;

                var card = $(this);
                var slot = card.parent();
                var tooltip = card.data('data-type').id;
                if (tooltip == currentTooltip) {
                    currentTooltip = null;
                }
                slot.find('.img-tooltip').remove();
            }

            var handleTouchEnd = function(evt) {
                touchMode = false;
                hideTooltip.call(this, evt);
            }

            var card = $("#cardPile ." + commands[index].title);
            card[0].addEventListener('touchstart', showTooltip);
            card[0].addEventListener('touchend', handleTouchEnd);
            card[0].addEventListener('touchmove', handleTouchEnd);
            card.mouseenter(showTooltip);
            card.mousedown(hideTooltip);
            card.mouseleave(hideTooltip);
        })
    };
    buildTooltips();

    // -------------------------------------- ball -----------------------------------
    var idMove = null;
    var idHide = null;
    var turnAnimation = [];
    var ballAnimation = false;
    var moveBallTo = function(x, y, start) {
        var ballTick = 10;
        var ballTail = ballTick*5;
        var ballHide = 200;
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        var animateDiv = function(div, style, value) {
            var oldValue = div.css(style);
            div.css(style, value);

            var css = {};
            css[style] = oldValue;
            div.animate(css, ballTail, function() {
                div.css(style, '');
            });
        }

        var animateBackground = function(x, y) {
            var div = board.getCard(x, y);
            if (div == null) {
                div = board.getSlot(x, y);
            }
            if (div != false) {
                animateDiv(div, "background-color", "#0c6050");
            }
        }

        var resetBall = function(x, y) {
            var offset = board.getSlot(x, y).offset();
            $("#ball").removeClass('hidden');
            $("#ball").offset(offset);

            if (idMove) {
                clearInterval(idMove);
                idMove = null;
            }

            turnAnimation.length = 0;
        };

        var ball, fromOffset, toOffset, speed;
        var calculate = function() {
            ball = $("#ball");

            fromOffset = ball.offset();
            var point = turnAnimation.shift();
            animateBackground(point.x, point.y);

            var slot = board.getSlot(point.x, point.y);
            if (!slot) {
                return false;
            }

            toOffset = slot.offset();

            speed = {
                x: (toOffset.left - fromOffset.left) / 1,
                y: (toOffset.top - fromOffset.top) / 1
            };

            return true;
        }

        var frame = function() {
            var curOffset = ball.offset();

            if (turnAnimation.length == 0 || !calculate()) {
                clearInterval(idMove);
                idMove = null;
                idHide = setInterval(function() {
                    ball.addClass('hidden');
                    ballAnimation = false;
                }, ballHide);
            } else {
                curOffset.left += speed.x;
                curOffset.top += speed.y;
                ball.offset(curOffset);
            }
        }

        if (!!start || !ballAnimation) {
            ballAnimation = true;
            resetBall(x, y);
        }

        turnAnimation.push({x: x, y: y});

        if (idMove) {
            return;
        }

        if (idHide) {
            clearInterval(idHide);
            idHide = null;
        }

        calculate();
        idMove = setInterval(frame, ballTick);
    };

    var buildBoll = function() {
        var ball = '<div id="ball" class="ball hidden"><img src = "../../resources/icancode/../sprite/icancode/befunge/ball.png"></div>';
        $("#cardSlots").append(ball);
    };

    buildBoll();

    // -------------------------------------- board -----------------------------------
    var initBoard = function() {
        var processCard = function(x, y, doNotMove) {
            var moved = false;
            var card = getCard(x, y);
            if (!!card) {
                moved = card.data('data-type').process(x, y);
            } else if (card == null) {
                // do nothing - skip empty cell
            } else {
                moved = finishCommand();
            }
            if (!doNotMove && !moved) {
                cursor = direction.change(cursor);
            }
        }

        var getSlot = function(x, y) {
            return mapSlots[y][x];
        }

        var getCard = function(x, y) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                // out of the board
                return false;
            }
            var card = getSlot(x, y).data('parked');
            if (!card) {
                return null;
            }
            return card;
        }

        var find = function(id, exclusion) {
            for (var y = 0; y < height; y++) {
                for (var x = 0; x < width; x++) {
                    var slot = getSlot(x, y);
                    var card = slot.data('parked');
                    if (!card || card.data('data-type').id != id) {
                        continue;
                    }

                    if (!!exclusion && exclusion.x == x && exclusion.y == y) {
                        continue;
                    }

                    return pt(x, y);
                }
            }
            logger.print('Команда "' + id + '" не найдена');
            return null;
        }

        var start = function() {
            var point = find('start');
            if (!point) {
                logger.print("Ошибка: Укажите точку старта выполнения программы!");
                return;
            }
            animate(point.getX(), point.getY(), true);
            processCard(point.getX(), point.getY());
        }

        var animate = function(x, y, startNewAnimation) {
            moveBallTo(x, y, startNewAnimation);
        }

        var goNext = function() {
            animate(cursor.getX(), cursor.getY(), false);
            processCard(cursor.getX(), cursor.getY());
        }

        var getCardId = function(x, y) {
            return getSlot(x, y).data('parked').data('data-type').id;
        }

        return {
            start: start,
            goNext: goNext,
            processCard: processCard,
            find: find,
            getCard: getCard,
            getSlot: getSlot,
            getCardId : getCardId
        }
    }

    // ------------------------------------- cards drag & drop -----------------------------------
    var park = function(card, slot) {
        var fromSlot = card.data('parkedTo');
        if (!!fromSlot) {
            fromSlot.data('parked', null);
        }
        var initialSlot = card.data('initial');
        if (!initialSlot) {
            card.data('initial', slot);
        }
        slot.data('parked', card);
        card.data('parkedTo', slot);
        card.position({of: slot, my: 'left top', at: 'left top'});

        saveState();
    }

    var isOnCardPile = function(slot) {
        return slot.parent().attr('id') == 'cardPile';
    }

    var onDragRevert = function(event, ui) {
        if (typeof event == 'object') {
            var slot = event;
            var card = $(this);
            var parked = slot.data('parked');
            if (!parked) {
                return false;
            }

            // если мы переместили в то же место откуда начали
            if (parked[0] == card[0]) {
                return false;
            }

            if (doNotRevert) {
                doNotRevert = false;
                return false;
            }

            return true;
        }
        return !event;
    }

    var doNotRevert = false;
    // переносим из палитры команду
    $('#cardPile .card-item').draggable({
        helper: "clone",
        cursor: 'move',
        revert: onDragRevert
    })

    var moveToInitial = function(card) {
        var slot = card.data('initial');
        park(card, slot);
        card.css({top: '0px', left: '0px'});
    }

    var moveCartToCardPile = function(card) {
        var fromSlot = card.data('parkedTo');
        if (!!fromSlot) {
            fromSlot.data('parked', null);
        }
        card.tooltip("hide");
        card.remove();
    }

    var moveAllCardsToCardPile = function() {
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                var slot = board.getSlot(x, y);
                var card = slot.data('parked');
                if (!!card) {
                    moveCartToCardPile(card);
                }
            }
        }
    }

    $('.card-item').each(function(index, element) {
        var card = $(this);
        var slot = card.parent();
        park(card, slot);
    })

    var cloneCard = function(card) {
        var newCard = card.clone();
        newCard.attr("class", card.attr("class"));
        newCard.data('data-type', card.data('data-type'));
        return newCard;
    }

    var initDroppable = function(elements) {
        elements.droppable({
            hoverClass: 'hovered',
            drop: function(event, ui) {
                var slot = $(this);
                if (slot.hasClass('ui-draggable')) {
                    slot = slot.data('parkedTo');
                }
                var card = ui.draggable;

                // переместили в заняый слот
                var busy = !!slot.data('parked');
                // переместили в ту же клеточку с которой начали
                var sameSlot = slot[0] == card.data('parkedTo')[0];

                if (busy && !sameSlot) {
                    if (isOnCardPile(slot) && !isOnCardPile(card.parent())) {
                        //doNotRevert = true;
                        //moveToInitial(card);
                        moveCartToCardPile(card);
                    }
                    return;
                }

                if (isOnCardPile(slot)) {
                    moveToInitial(card);
                } else {
                    if (!isOnCardPile(card.parent())) {
                        // TODO вообщето тут физически див остается в старом месте меняется только его координаты и привязка в данных, это работает но это не совсем ок
                        park(card, slot);
                    } else {
                        doNotRevert = true;
                        cloneCardOnSlot(card, slot);
                    }
                }
            }
        });
    }

    var cloneCardOnSlot = function(card, slot) {
        var newCard = cloneCard(card);
        slot.append(newCard);
        // переносим карту из поля
        $(newCard).draggable({
            cursor: 'move',
            revert: onDragRevert
        });
        newCard.data('initial', card.data('initial'));
        park(newCard, slot);
        newCard.dblclick(function(element) {
            var card = $(this);
            moveCartToCardPile(card);
        });
        $('[data-toggle="tooltip"]').tooltip();
    }

    // -------------------------------------- levelUpdate -----------------------------------
    var levelUpdate = function(level, multiple, lastPassed) {
        $('#cardPile .card-slot').each(function(index, element) {
            var slot = $(this);
            var data = slot.data('data-type');
            var forcedHidden = data.hasOwnProperty("hidden") && data.hidden;

            if (commandAllowed(level, data.id) && !forcedHidden) {
                slot.removeClass('hidden');
            } else {
                slot.addClass('hidden');
            }
        });
    };

    initDroppable($('.card-slot'));
    readyForSaving = true;

    var board = initBoard();

    return {
        setStubValue: function() {
            // TODO implement me
        },
        loadSettings: function() {
            loadState();
        },
        getValue : function() {
            // TODO implement me for saving on server
        },
        compileProgram: function(robot) {
            // do nothing
        },
        cleanProgram: function() {
            running = false;
        },
        isProgramCompiled: function() {
            return true;
        },
        runProgram: function(input) {
            robot = input;
            board.start();
            var deadLoopCounter = 0;
            while (++deadLoopCounter < 200 && running) {
                board.goNext();
            }
        },
        levelUpdate: function(level, multiple, lastPassed) {
            levelUpdate(level, multiple, lastPassed);
        }
    }

}

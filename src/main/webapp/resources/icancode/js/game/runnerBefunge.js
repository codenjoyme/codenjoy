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

/*
 * Based on work by http://www.elated.com is licensed under a Creative Commons Attribution 3.0 Unported License (http://creativecommons.org/licenses/by/3.0/)
 * From http://www.elated.com/res/File/articles/development/javascript/jquery/drag-and-drop-with-jquery-your-essential-guide/card-game.html
 **/
function initRunnerBefunge(console) {

    if (game.debug) {
        debugger;
    }

    var width = 11;
    var height = 11;

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
    $('.bottom-panel').append('<button class="button help" id="ide-clean">Clean</button>')
    $('#ide-clean').click(function () {
        moveAllCardsToCardPile();
    });

    var cursor = null;
    var direction = Direction.RIGHT;
    var stack = [];
    var proceduralStack = [];
    var running = false;
    var robot = null;

    var finishCommand = function () {
        if (proceduralStack.length != 0) {
            var data = proceduralStack[proceduralStack.length - 1];
            direction = data.direction;
            cursor = data.pt;
            //cursor = direction.change(cursor);
        } else {
            stack = [];
            running = false;
        }
    }

    var popFromStack = function () {
        if (stack.length == 0) {
            console.print('Не указано значение для команды. Укажите значение!');
            finishCommand();
            return;
        }
        var value = stack.pop();
        return value;
    }

    var activateProcedure = function (procedureName, x, y) {
        if (proceduralStack.length != 0 && proceduralStack[proceduralStack.length - 1].name == procedureName) {
            proceduralStack.pop();
            return;
        }

        var toPoint = board.find(procedureName, {x: x, y: y});
        if (!toPoint) {
            return;
        }

        proceduralStack.push({name: procedureName, direction: direction, pt: pt(x, y)});
        cursor = pt(toPoint.x, toPoint.y);
        direction = Direction.RIGHT;
    };

    var commands = [
        {
            id: 'start',
            type: 1,
            title: 'start',
            process: function (x, y) {
                cursor = pt(x, y);
                direction = Direction.RIGHT;
                stack = [];
                proceduralStack = [];
                running = true;
            },
            description: 'Выполнение команд начинается тут.',
            minLevel: 0,
            img1: 'img/sprite/start.png'
        },

        {
            id: 'finish',
            type: 1,
            title: 'finish',
            process: finishCommand,
            description: 'Выполнение команд останавливается тут.',
            minLevel: 0,
            img1: 'img/sprite/finish.png'
        },

        {
            id: 'cursor-right',
            type: 1,
            title: 'cursor-right',
            process: function (x, y) {
                direction = Direction.RIGHT;
            },
            description: 'Командный курсор двигайся вправо.',
            minLevel: 3,
            hidden: true
        },

        {
            id: 'cursor-left',
            type: 1,
            title: 'cursor-left',
            process: function (x, y) {
                direction = Direction.LEFT;
            },
            description: 'Командный курсор двигайся влево.',
            minLevel: 3,
            hidden: true
        },

        {
            id: 'cursor-up',
            type: 1,
            title: 'cursor-up',
                process: function (x, y) {
                direction = Direction.UP;
            },
            description: 'Командный курсор двигайся вверх.',
            minLevel: 3,
            hidden: true
        },

        {
            id: 'cursor-down',
            type: 1,
            title: 'cursor-down',
            process: function (x, y) {
                direction = Direction.DOWN;
            },
            description: 'Командный курсор двигайся вниз.',
            minLevel: 3,
            hidden: true
        },

        {
            id: 'mirror-top-bottom',
            type: 1,
            title: 'mirror-top-bottom',
            process: function (x, y) {
                direction = direction.mirrorTopBottom();
            },
            description: 'Зеркало изменяет направление движения командного курсора.',
            minLevel: 3,
            img1: 'img/sprite/mirror-top-bottom.png'
        },

        {
            id: 'mirror-bottom-top',
            type: 1,
            title: 'mirror-bottom-top',
            process: function (x, y) {
                direction = direction.mirrorBottomTop();
            },
            description: 'Зеркало изменяет направление движения командного курсора.',
            minLevel: 3,
            img1: 'img/sprite/mirror-bottom-top.png'
        },

        {
            id: 'print-stack',
            type: 1,
            title: 'print-stack',
            process: function (x, y) {
                console.print('Stack [' + stack + ']');
            },
            description: 'Напечатать в консоли все значения, сохраненные командами.',
            minLevel: 3
        },

        {
            id: 'procedure-1',
            type: 1,
            title: 'procedure-1',
            process: function (x, y) {
                activateProcedure('procedure-1', x, y);
            },
            description: '',
            minLevel: 4,
            img1: 'img/sprite/procedure-1-1.png',
            img2: 'img/sprite/procedure-1.png',
        },

        {
            id: 'procedure-2',
            type: 1,
            title: 'procedure-2',
            process: function (x, y) {
                activateProcedure('procedure-2', x, y);
            },
            description: '',
            minLevel: 4,
            img1: 'img/sprite/procedure-2-1.png',
            img2: 'img/sprite/procedure-2.png',
        },

        {
            id: 'procedure-3',
            type: 1,
            title: 'procedure-3',
            process: function (x, y) {
                activateProcedure('procedure-3', x, y);
            },
            description: '',
            minLevel: 4,
            img1: 'img/sprite/procedure-3-1.png',
            img2: 'img/sprite/procedure-3.png',
        },

        {
            id: 'if',
            type: 1,
            title: 'if',
            process: function (x, y) {
                var leftValue = popFromStack();
                var point = direction.change(cursor);
                board.processCard(point.getX(), point.getY());
                var rightValue = popFromStack();
                if (leftValue == rightValue) {
                    direction = direction.contrClockwise();
                } else {
                    direction = direction.clockwise();
                }
            },
            description: 'Оператор ветвления. Если значения по обе стороны команды равны - поворот командного курсора направо, если не равны - поворот курсора налево.',
            minLevel: 1,
            img1: 'img/sprite/if-1.png',
            img2: 'img/sprite/if.png'
        },

        {
            id: 'scanner-at',
            type: 1,
            title: 'scanner-at',
            process: function (x, y) {
                var oldValue = popFromStack();
                var value = robot.getScanner().at(oldValue);
                stack.push(value);
            },
            description: 'Сканер позволяет определить, что находится на поле вокруг героя. Сторону необходимо указать предварительно.',
            minLevel: 1,
            img1: 'img/sprite/scanner-at-left.png',
            img2: 'img/sprite/scanner-at-right.png',
            img3: 'img/sprite/value-left.png',
            img4: 'img/sprite/value-right.png'
        },

        {
            id: 'robot-came-from',
            type: 1,
            title: 'robot-came-from',
            process: function (x, y) {
                var value = robot.cameFrom();
                stack.push(value);
            },
            description: 'Указывает откуда пришел герой только что. Если герой не двигался - команда вернет Null.',
            minLevel: 2,
            img1: 'img/sprite/robot-came-from.png'
        },

        {
            id: 'robot-previous-direction',
            type: 1,
            title: 'robot-previous-direction',
            process: function (x, y) {
                var value = robot.previousDirection();
                stack.push(value);
            },
            description: 'Указывает куда ходил герой в прошлый раз. Если герой не двигался - команда вернет Null.',
            minLevel: 2,
            img1: 'img/sprite/robot-previous-direction.png'
        },

        {
            id: 'robot-left',
            type: 2,
            title: 'robot-left',
            process: function (x, y) {
                robot.go('LEFT');
            },
            description: 'Команда герою двигаться влево.',
            minLevel: 0,
            img1: 'img/sprite/robot-left-1.png',
            img2: 'img/sprite/robot-left.png'
        },

        {
            id: 'robot-right',
            type: 2,
            title: 'robot-right',
            process: function (x, y) {
                robot.go('RIGHT');
            },
            description: 'Команда герою двигаться вправо.',
            minLevel: 0,
            img1: 'img/sprite/robot-right-1.png',
            img2: 'img/sprite/robot-right.png'
        },

        {
            id: 'robot-up',
            type: 2,
            title: 'robot-up',
            process: function (x, y) {
                robot.go('UP');
            },
            description: 'Команда герою двигаться вверх.',
            minLevel: 0,
            img1: 'img/sprite/robot-up-1.png',
            img2: 'img/sprite/robot-up.png'
        },

        {
            id: 'robot-down',
            type: 2,
            title: 'robot-down',
            process: function (x, y) {
                robot.go('DOWN');
            },
            description: 'Команда герою двигаться вниз.',
            minLevel: 0,
            img1: 'img/sprite/robot-down-1.png',
            img2: 'img/sprite/robot-down.png'
        },

        {
            id: 'robot-go',
            type: 2,
            title: 'robot-go',
            process: function (x, y) {
                var value = popFromStack();
                robot.go(value);
            },
            description: 'Команда герою двигаться в заданном направлении. Сторону необходимо указать предварительно.',
            minLevel: 2,
            img1: 'img/sprite/robot-go-left.png',
            img2: 'img/sprite/robot-go-right.png',
            img3: 'img/sprite/robot-left.png',
            img4: 'img/sprite/robot-right.png'
        },

        {
            id: 'robot-jump-left',
            type: 2,
            title: 'robot-jump-left',
            process: function (x, y) {
                robot.jump('LEFT');
            },
            description: 'Команда герою прыгнуть влево.',
            minLevel: 10,
            img1: 'img/sprite/robot-jump-left-1.png',
            img2: 'img/sprite/robot-jump-left.png'
        },

        {
            id: 'robot-jump-right',
            type: 2,
            title: 'robot-jump-right',
            process: function (x, y) {
                robot.jump('RIGHT');
            },
            description: 'Команда герою прыгнуть направо.',
            minLevel: 10,
            img1: 'img/sprite/robot-jump-right-1.png',
            img2: 'img/sprite/robot-jump-right.png'
        },

        {
            id: 'robot-jump-up',
            type: 2,
            title: 'robot-jump-up',
            process: function (x, y) {
                robot.jump('UP');
            },
            description: 'Команда герою прыгнуть вверх.',
            minLevel: 10,
            img1: 'img/sprite/robot-jump-up-1.png',
            img2: 'img/sprite/robot-jump-up.png'
        },

        {
            id: 'robot-jump-down',
            type: 2,
            title: 'robot-jump-down',
            process: function (x, y) {
                robot.jump('DOWN');
            },
            description: 'Команда герою прыгнуть вниз.',
            minLevel: 10,
            img1: 'img/sprite/robot-jump-down-1.png',
            img2: 'img/sprite/robot-jump-down.png'
        },

        {
            id: 'robot-jump',
            type: 2,
            title: 'robot-jump',
            process: function (x, y) {
                var value = popFromStack();
                robot.jump(value);
            },
            description: 'Команда герою прыгнуть в заданном направлении. Cторону необходимо указать предварительно.',
            minLevel: 10,
            img1: 'img/sprite/jump-left.png',
            img2: 'img/sprite/jump-right.png',
            img3: 'img/sprite/robot-jump-left.png',
            img4: 'img/sprite/robot-jump-right.png'
        },


        {
            id: 'value-left',
            type: 3,
            title: 'value-left',
            process: function (x, y) {
                stack.push('LEFT');
            },
            description: 'Указание направления "влево". Используется совместно с другими командами.',
            minLevel: 2,
            img1: 'img/sprite/value-left-2.png',
            img2: 'img/sprite/value-left-1.png',
            img3: 'img/sprite/value-left.png'
        },

        {
            id: 'value-right',
            type: 3,
            title: 'value-right',
            process: function (x, y) {
                stack.push('RIGHT');
            },
            description: 'Указание направления "направо". Используется совместно с другими командами.',
            minLevel: 2,
            img1: 'img/sprite/value-right-2.png',
            img2: 'img/sprite/value-right-1.png',
            img3: 'img/sprite/value-right.png'
        },

        {
            id: 'value-up',
            type: 3,
            title: 'value-up',
            process: function (x, y) {
                stack.push('UP');
            },
            description: 'Указание направления "вверх". Используется совместно с другими командами.',
            minLevel: 2,
            img1: 'img/sprite/value-up-2.png',
            img2: 'img/sprite/value-up-1.png',
            img3: 'img/sprite/value-up.png'
        },

        {
            id: 'value-down',
            type: 3,
            title: 'value-down',
            process: function (x, y) {
                stack.push('DOWN');
            },
            description: 'Указание направления "вниз". Используется совместно с другими командами.',
            minLevel: 2,
            img1: 'img/sprite/value-down-2.png',
            img2: 'img/sprite/value-down-1.png',
            img3: 'img/sprite/value-down.png'
        },

        {
            id: 'value-null',
            type: 3,
            title: 'value-null',
            process: function (x, y) {
                stack.push(null);
            },
            description: 'Специальное значение NULL. Возвращается командой, когда ей нечего ответить.',
            minLevel: 2
        },

        {
            id: 'value-wall',
            type: 3,
            title: 'value-wall',
            process: function (x, y) {
                stack.push('WALL');
            },
            description: 'Значние "Обрыв". Испольузется совместно с другими командами.',
            minLevel: 1,
            img1: 'img/sprite/cloud.png'
        },

        {
            id: 'value-none',
            type: 3,
            title: 'value-none',
            process: function (x, y) {
                stack.push('NONE');
            },
            description: 'Значние "Земля". Испольузется совместно с другими командами.',
            minLevel: 1,
            img1: '../sprite/icancode/ekids/floor.png'
        },

        {
            id: 'value-start',
            type: 3,
            title: 'value-start',
            process: function (x, y) {
                stack.push('START');
            },
            description: 'Значние "Точка старта". Испольузется совместно с другими командами.',
            minLevel: 11,
            img1: '../sprite/icancode/ekids/start.png'
        },

        {
            id: 'value-end',
            type: 3,
            title: 'value-end',
            process: function (x, y) {
                stack.push('END');
            },
            description: 'Значние "Точка финиша". Испольузется совместно с другими командами.',
            minLevel: 11,
            img1: '../sprite/icancode/ekids/exit.png'
        },

        {
            id: 'value-gold',
            type: 3,
            title: 'value-gold',
            process: function (x, y) {
                stack.push('GOLD');
            },
            description: 'Значние - "Золото". Испольузется совместно с другими командами.',
            minLevel: 11,
            img1: '../sprite/icancode/ekids/gold.png'
        },

        {
            id: 'value-box',
            type: 3,
            title: 'value-box',
            process: function (x, y) {
                stack.push('BOX');
            },
            description: 'Значние - "Камень". Испольузется совместно с другими командами.',
            minLevel: 11,
            img1: '../sprite/icancode/ekids/box.png'
        },

        {
            id: 'value-hole',
            type: 3,
            title: 'value-hole',
            process: function (x, y) {
                stack.push('HOLE');
            },
            description: 'Значние - "Яма". Испольузется совместно с другими командами.',
            minLevel: 11,
            img1: '../sprite/icancode/ekids/hole.png'
        }
    ];

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

    var buildPileSlots = function () {
        for (var index = 0; index < commands.length; index++) {
            $('<div class="card-slot hidden"></div>')
                .data('data-type', commands[index])
                .appendTo('#cardPile');

            $("#cardPile ." + commands[index].title).hover(function () {
                $(this).append('<div class="img-tooltip"><span class="tooltip-desc">Tells character to jump in the direction of VALUE</span></div>');
            }, function () {
                $(this).empty();
            });
        }
    };
    buildPileSlots();


    $('<div id="add-left" class="add-left">+</div>').appendTo('#cardSlots').click(function () {
        $('.slot-line').each(function (y, line) {
            var element = $('<div class="card-slot"></div>')
                .prependTo(line);
            mapSlots[y].unshift(element);
            initDroppable(element);
        });
        width++;
        if (readyForSaving) {
            saveState();
        }
    });
    $('<div id="add-right" class="add-right">+</div>').appendTo('#cardSlots').click(function () {
        $('.slot-line').each(function (y, line) {
            var element = $('<div class="card-slot"></div>')
                .appendTo(line);
            mapSlots[y].push(element);
            initDroppable(element);
        });
        width++;
        if (readyForSaving) {
            saveState();
        }
    });

    var mapCards = [];
    var createNewOnPile = function (element) {
        var type = $(element).data('data-type');
        var appended = $('<div class="card-item type-' + type.type + ' ' + type.title + '"></div>')
            .data('data-type', type)
            .appendTo(element);
        mapCards.push(appended);
        $('[data-toggle="tooltip"]').tooltip();
    }

    var buildPileCards = function () {
        $('#cardPile .card-slot').each(function (index, element) {
            createNewOnPile(element);
        });
    };
    buildPileCards();


    var buildTooltips = function () {


        jQuery.each(commands, function (index) {
            var elem;
            if (commands[index].img1 && commands[index].img2 && commands[index].img3 && commands[index].img4) {
                elem = '<div class="img-tooltip">' +
                    '<div class="img-container">' +
                    '<img src = "../../resources/icancode/' + commands[index].img1 + '">' +
                    '<img src = "../../resources/icancode/' + commands[index].img2 + '">' +
                    '</div>' +
                    '<div class="img-container">' +
                    '<img src = "../../resources/icancode/' + commands[index].img3 + '">' +
                    '<img src = "../../resources/icancode/' + commands[index].img4 + '">' +
                    '</div>' +
                    '<span class="tooltip-desc">' + commands[index].description + '</span>' +
                    '</div>';
            } else if (commands[index].img1 && commands[index].img2 && commands[index].img3) {
                elem = '<div class="img-tooltip">' +
                    '<div class="img-container">' +
                    '<img src = "../../resources/icancode/' + commands[index].img1 + '">' +
                    '</div>' +
                    '<div class="img-container">' +
                    '<img src = "../../resources/icancode/' + commands[index].img2 + '">' +
                    '<img src = "../../resources/icancode/' + commands[index].img3 + '">' +
                    '</div>' +
                    '<span class="tooltip-desc">' + commands[index].description + '</span>' +
                    '</div>';
            } else if (commands[index].img1 && commands[index].img2) {
                elem = '<div class="img-tooltip">' +
                    '<img src = "../../resources/icancode/' + commands[index].img1 + '">' +
                    '<img src = "../../resources/icancode/' + commands[index].img2 + '">' +
                    '<span class="tooltip-desc">' + commands[index].description + '</span>' +
                    '</div>';
            } else if (commands[index].img1) {
                elem = '<div class="img-tooltip">' +
                    '<img src = "../../resources/icancode/' + commands[index].img1 + '">' +
                    '<span class="tooltip-desc">' + commands[index].description + '</span>' +
                    '</div>';
            } else {
                elem = '<div class="img-tooltip">' +
                    '<span class="tooltip-desc">' + commands[index].description + '</span>' +
                    '</div>';
            }

            $("#cardPile ." + commands[index].title).hover(function () {
                $(this).append(elem);
            }, function () {
                $(this).empty();
            });

            $("#cardPile ." + commands[index].title).mousedown(function () {
                $(this).empty();
                $(this).css("z-index", "99");
            });

            $("#cardPile ." + commands[index].title).mouseleave(function () {
                $(this).empty();
                $(this).css("z-index", "auto");
            });
        })
    };

    buildTooltips();

    var setPositionBallBySlot = function (x, y) {
        var offset = mapSlots[y][x].offset();
        $("#ball").offset(offset);
        $("#ball").removeClass('hidden');

        if (idMove) {
            clearInterval(idMove);
            idMove = null;
        }

        turnAnimation.length = 0;
    };

    var idMove = null;
    var idHide = null;
    var turnAnimation = [];
    var moveBallTo = function (x, y) {
        turnAnimation.push({x: x, y: y});

        if (idMove) {
            return;
        }

        if (idHide) {
            clearInterval(idHide);
            idHide = null;
        }

        idMove = setInterval(frame, 10);
        var ball, fromOffset, toOffset, speed;

        function calculate() {
            ball = $("#ball");

            fromOffset = ball.offset();
            var coords = turnAnimation.shift();

            if (!mapSlots[coords.y][coords.x]) {
                return false;
            }

            toOffset = mapSlots[coords.y][coords.x].offset();

            speed = {
                x: (toOffset.left - fromOffset.left) / 1,
                y: (toOffset.top - fromOffset.top) / 1
            };

            return true;
        }

        calculate();

        function frame() {
            var curOffset = ball.offset();

            if (speed.x >= 0 ? curOffset.left >= toOffset.left : curOffset.left <= toOffset.left
                && speed.y >= 0 ? curOffset.top >= toOffset.top : curOffset.top <= toOffset.top) {
                ball.offset(toOffset);

                if (turnAnimation.length == 0 || !calculate()) {
                    clearInterval(idMove);
                    idMove = null;
                    idHide = setInterval(function () {
                        ball.addClass('hidden');
                    }, 500);
                }
            } else {
                curOffset.left += speed.x;
                curOffset.top += speed.y;
                ball.offset(curOffset);
            }
        }
    };

    var buildBoll = function () {
        var ball = '<div id="ball" class="ball hidden"><img src = "../../resources/icancode/img/sprite/ball.png"></div>';
        $("#cardSlots").append(ball);
    };

    buildBoll();

    var initBoard = function () {
        var processCard = function (x, y) {
            var card = getCard(x, y);
            if (!!card) {
                card.data('data-type').process(x, y);
            } else if (card == null) {
                // do nothing - skip empty cell
            } else {
                // do nothing - we are out of the board
            }
        }

        var getSlot = function (x, y) {
            return mapSlots[y][x];
        }

        var getCard = function (x, y) {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                // out of the board
                finishCommand();
                return false;
            }
            var card = getSlot(x, y).data('parked');
            if (!card) {
                return null;
            }
            return card;
        }

        var find = function (id, exclusion) {
            for (var y = 0; y < height; y++) {
                for (var x = 0; x < width; x++) {
                    var slot = mapSlots[y][x];
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
            console.print('Card with id "' + id + '" not found!');
            return null;
        }

        var start = function () {
            var point = find('start');
            if (!point) {
                console.print("Ошибка: Укажите точку старта выполнения программы!");
                return;
            }
            setPositionBallBySlot(point.getX(), point.getY());
            animate(point.getX(), point.getY());
            processCard(point.getX(), point.getY());
        }

        var animateDiv = function (div, style, value) {
            var oldValue = div.css(style);
            var css = {};
            css[style] = value;
            div.animate(css, {
                duration: "fast", complete: function () {
                    css[style] = oldValue;
                    div.animate(css, "fast");
                }
            });
        }

        var animate = function (x, y) {
            var div = getCard(x, y);
            if (div == null) {
                div = getSlot(x, y);
            }
            if (div != false) {
                animateDiv(div, "background-color", "#000");
            }
        }

        var goNext = function () {
            cursor = direction.change(cursor);
            animate(cursor.getX(), cursor.getY());
            moveBallTo(cursor.getX(), cursor.getY());
            processCard(cursor.getX(), cursor.getY());
        }

        return {
            start: start,
            goNext: goNext,
            processCard: processCard,
            find : find
        }
    }

    // ----------------------- save state -------------------
    var saveState = function () {
        var data = [];

        for (var y = 0; y < height; y++) {
            data[y] = [];

            for (var x = 0; x < width; x++) {
                data[y][x] = !!mapSlots[y][x].data('parked') ? getCardIDByCoords(x, y) : null;
            }
        }

        localStorage.setItem('editor.cardcode', JSON.stringify(data));
    };

    var park = function (card, slot) {
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

        if (readyForSaving) {
            saveState();
        }
    }

    var isOnCardPile = function (slot) {
        return slot.parent().attr('id') == 'cardPile';
    }

    var onDragRevert = function (event, ui) {
        if (typeof event == 'object') {
            var slot = event;
            var card = $(this);
            var parked = slot.data('parked');
            if (!parked) {
                return false;
            }

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
    $('#cardPile .card-item').draggable({
        helper: "clone",
        cursor: 'move',
        revert: onDragRevert
    })

    var moveToInitial = function (card) {
        var slot = card.data('initial');
        park(card, slot);
        card.css({top: '0px', left: '0px'});
    }

    var moveCartToCardPile = function (card) {
        var fromSlot = card.data('parkedTo');
        if (!!fromSlot) {
            fromSlot.data('parked', null);
        }
        card.tooltip("hide");
        card.remove();
    }

    var moveAllCardsToCardPile = function () {
        for (var y = 0; y < height; y++) {
            for (var x = 0; x < width; x++) {
                var slot = mapSlots[y][x];
                var card = slot.data('parked');
                if (!!card) {
                    moveCartToCardPile(card);
                }
            }
        }
    }

    $('.card-item').each(function (index, element) {
        var card = $(this);
        var slot = card.parent();
        park(card, slot);
    })

    var cloneCard = function (card) {
        var newCard = card.clone();
        newCard.attr("class", card.attr("class"));
        newCard.data('data-type', card.data('data-type'));
        return newCard;
    }

    var initDroppable = function (elements) {
        elements.droppable({
            hoverClass: 'hovered',
            drop: function (event, ui) {
                var slot = $(this);
                if (slot.hasClass('ui-draggable')) {
                    slot = slot.data('parkedTo');
                }
                var card = ui.draggable;

                var busy = !!slot.data('parked')
                if (busy) {
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
                        park(card, slot);
                    } else {
                        doNotRevert = true;
                        cloneCardOnSlot(card, slot);
                    }
                }
            }
        });
    }

    var cloneCardOnSlot = function (card, slot) {
        var newCard = cloneCard(card);
        slot.append(newCard);
        $(newCard).draggable({
            cursor: 'move',
            revert: onDragRevert
        });
        newCard.data('initial', card.data('initial'));
        park(newCard, slot);
        newCard.dblclick(function (element) {
            var card = $(this);
            moveCartToCardPile(card);
        });
        $('[data-toggle="tooltip"]').tooltip();
    }

    var getCardIDByCoords = function (x, y) {
        return mapSlots[y][x].data('parked').data('data-type').id;
    };

    var loadState = function () {
        readyForSaving = false;
        try {
            var data = JSON.parse(localStorage.getItem('editor.cardcode'));
        } catch (err) {
            readyForSaving = true;
            return;
        }

        if (!data || data.length != height) {
            return;
        }

        var diff = data[0].length - width;

        for (var i = 0; i < diff; ++i) {
            $('.slot-line').each(function (y, line) {
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

                $('#cardPile .card-item').each(function (index, element) {
                    var card = $(this);

                    if (card.data('data-type').id != id) {
                        return;
                    }

                    var slot = mapSlots[y][x];

                    cloneCardOnSlot(card, slot);
                });

            }
        }
        readyForSaving = true;
    };

    var oldLastPassed = -2;
    var levelUpdate = function (level, multiple, lastPassed) {
        if (oldLastPassed != lastPassed) {
            oldLastPassed = lastPassed;

            $('#cardPile .card-slot').each(function (index, element) {
                var slot = $(this);
                var data = slot.data('data-type');
                var forcedHidden = data.hasOwnProperty("hidden") && data.hidden;

                if (data.minLevel <= lastPassed + 1 && !forcedHidden) {
                    slot.removeClass('hidden');
                } else {
                    slot.addClass('hidden');
                }
            });
        }
    };

    initDroppable($('.card-slot'));
    readyForSaving = true;

    var board = null;

    return {
        setStubValue: function () {
            // TODO implement me
        },
        loadSettings: function () {
            loadState();
        },
        compileProgram: function (robot) {
            // do nothing
        },
        cleanProgram: function () {
            running = false;
            board = initBoard();
        },
        isProgramCompiled: function () {
            return true;
        },
        runProgram: function (r) {
            robot = r;
            board.start();
            var deadLoopCounter = 0;
            while (++deadLoopCounter < 100 && running) {
                board.goNext();
            }
        },
        levelUpdate: function (level, multiple, lastPassed) {
            levelUpdate(level, multiple, lastPassed);
        }
    }

}

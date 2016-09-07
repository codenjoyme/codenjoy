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
    $('#ide-clean').click(function() {
        moveAllCardsToCardPile();
    });

    var cursor = null;
	var direction = Direction.RIGHT;
    var stack = [];
    var running = false;
    var robot = null;

    var finishCommand = function() {
        stack = [];
        running = false;
    }

    var popFromStack = function() {
        if (stack.length == 0) {
            console.print('No value saved for command! Please use VALUE command.');
            finishCommand();
            return;
        }
        var value = stack.pop();
        return value;
    }

    var commands = [
        {id:'start', type:9, title:'start', process: function(x, y) {
            cursor = pt(x, y);
            direction = Direction.RIGHT;
            stack = [];
            running = true;
        }, description:'Start reading commands on the right'},

        {id:'finish', type:9, title:'finish', process: finishCommand,
            description:'Finish reading commands'},

        {id:'cursor-right', type:1, title:'cursor-right', process: function(x, y) {
            direction = Direction.RIGHT;
        }, description:'Change direction of reading commands to the right'},

        {id:'cursor-left', type:1, title:'cursor-left', process: function(x, y) {
            direction = Direction.LEFT;
        }, description:'Change direction of reading commands to the left'},

        {id:'cursor-up', type:1, title:'cursor-up', process: function(x, y) {
            direction = Direction.UP;
        }, description:'Change direction of reading commands to the top'},

        {id:'cursor-down', type:1, title:'cursor-down', process: function(x, y) {
            direction = Direction.DOWN;
        }, description:'Change direction of reading commands to the bottom'},

        {id:'robot-left', type:5, title:'robot-left', process: function(x, y) {
            robot.go('LEFT');
        }, description:'Tells character to go left'},

        {id:'robot-right', type:5, title:'robot-right', process: function(x, y) {
            robot.go('RIGHT');
        }, description:'Tells character to go right'},

        {id:'robot-up', type:5, title:'robot-up', process: function(x, y) {
            robot.go('UP');
        }, description:'Tells character to go up'},

        {id:'robot-down', type:5, title:'robot-down', process: function(x, y) {
            robot.go('DOWN');
        }, description:'Tells character to go down'},

        {id:'robot-go', type:5, title:'robot-go', process: function(x, y) {
            var value = popFromStack();
            robot.go(value);
        }, description:'Tells character to go in the direction of VALUE'},

        {id:'robot-jump-left', type:8, title:'robot-jump-left', process: function(x, y) {
            robot.jump('LEFT');
        }, description:'Tells character to jump left'},

        {id:'robot-jump-right', type:8, title:'robot-jump-right', process: function(x, y) {
            robot.jump('RIGHT');
        }, description:'Tells character to jump right'},

        {id:'robot-jump-up', type:8, title:'robot-jump-up', process: function(x, y) {
            robot.jump('UP');
        }, description:'Tells character to jump up'},

        {id:'robot-jump-down', type:8, title:'robot-jump-down', process: function(x, y) {
            robot.jump('DOWN');
        }, description:'Tells character to jump down'},

        {id:'robot-jump', type:8, title:'robot-jump', process: function(x, y) {
            var value = popFromStack();
            robot.jump(value);
        }, description:'Tells character to jump in the direction of VALUE'},

        {id:'if', type:7, title:'if', process: function(x, y) {
            var leftValue = popFromStack();
            var point = direction.change(cursor);
            board.processCard(point.getX(), point.getY());
            var rightValue = popFromStack();
			if (leftValue == rightValue) {
                direction = direction.contrClockwise();
            } else {
                direction = direction.clockwise();
            }
        }, description:'Compares current VALUE with if\'s VALUE. Direction of reading commands depends on logical outcome'},

        {id:'scanner-at', type:7, title:'scanner-at', process: function(x, y) {
            var oldValue = popFromStack();
            var value = robot.getScanner().at(oldValue);
            stack.push(value);
        }, description:'Gets the value of the object in the pointed direction'},

        {id:'robot-came-from', type:10, title:'robot-came-from', process: function(x, y) {
            var value = robot.cameFrom();
            stack.push(value);
        }, description:'Assigns the direction character came from to the VALUE'},

        {id:'robot-previous-direction', type:10, title:'robot-previous-direction', process: function(x, y) {
            var value = robot.previousDirection();
            stack.push(value);
        }, description:'Assigns the direction character was moving to the VALUE'},

        {id:'value-left', type:4, title:'value-left', process: function(x, y) {
            stack.push('LEFT');
        }, description:'Assigns LEFT to the VALUE'},

        {id:'value-right', type:4, title:'value-right', process: function(x, y) {
            stack.push('RIGHT');
        }, description:'Assigns RIGHT to the VALUE'},

        {id:'value-up', type:4, title:'value-up', process: function(x, y) {
            stack.push('UP');
        }, description:'Assigns UP to the VALUE'},

        {id:'value-down', type:4, title:'value-down', process: function(x, y) {
            stack.push('DOWN');
        }, description:'Assigns DOWN to the VALUE'},

        {id:'value-null', type:6, title:'value-null', process: function(x, y) {
            stack.push(null);
        }, description:'Assigns NULL to the VALUE'},

        {id:'print-stack', type:11, title:'print-stack', process: function(x, y) {
            console.print('Stack [' + stack + ']');
        }, description:'Print VALUES STACK to console'},

        {id:'value-wall', type:3, title:'value-wall', process: function(x, y) {
            stack.push('WALL');
        }, description:'Assigns ABYSS to the VALUE'},

        {id:'value-none', type:3, title:'value-none', process: function(x, y) {
            stack.push('NONE');
        }, description:'Assigns GROUND to the VALUE'},

        {id:'value-start', type:3, title:'value-start', process: function(x, y) {
            stack.push('START');
        }, description:'Assigns START to the VALUE'},

        {id:'value-end', type:3, title:'value-end', process: function(x, y) {
            stack.push('END');
        }, description:'Assigns END to the VALUE'},

        {id:'value-gold', type:3, title:'value-gold', process: function(x, y) {
            stack.push('GOLD');
        }, description:'Assigns GOLD to the VALUE'},

        {id:'value-box', type:3, title:'value-box', process: function(x, y) {
            stack.push('BOX');
        }, description:'Assigns BRICK to the VALUE'},

        {id:'value-hole', type:3, title:'value-hole', process: function(x, y) {
            stack.push('HOLE');
        }, description:'Assigns HOLE to the VALUE'}
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

    for (var index = 0; index < commands.length; index++) {
        $('<div class="card-slot"></div>')
            .data('data-type', commands[index])
            .appendTo('#cardPile');
    }
    $('<div id="add-left" class="add-left">+</div>').appendTo('#cardSlots').click(function(){
        $('.slot-line').each(function(y, line) {
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
    $('<div id="add-right" class="add-right">+</div>').appendTo('#cardSlots').click(function(){
        $('.slot-line').each(function(y, line) {
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
    var createNewOnPile = function(element) {
        var type = $(element).data('data-type');
        var appended = $('<div data-toggle="tooltip" data-placement="bottom" class="card-item type-' + type.type + ' '+ type.title +'" title="' + type.description + '"></div>')
            .data('data-type', type)
            .appendTo(element);
        mapCards.push(appended);
       $('[data-toggle="tooltip"]').tooltip();
    }

    $('#cardPile .card-slot').each(function(index, element) {
        createNewOnPile(element);
    });

    var initBoard = function() {
        var processCard = function(x, y) {
            var card = getCard(x, y);
            if (!!card) {
                card.data('data-type').process(x, y);
            } else if (card == null) {
                // do nothing - skip empty cell
            } else {
                // do nothing - we are out of the board
            }
        }

        var getSlot = function(x, y) {
            return mapSlots[y][x];
        }

        var getCard = function(x, y) {
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

        var find = function(id) {
            for (var y = 0; y < height; y++) {
                for (var x = 0; x < width; x++) {
                    var slot = mapSlots[y][x];
                    var card = slot.data('parked');
                    if (!card || card.data('data-type').id != id) {
                        continue;
                    }

                    return pt(x, y);
                }
            }
            console.print('Card with id "' + id + '" not found!');
        }

        var start = function() {
            var point = find('start');
            if (!point) {
                console.print("Error: Create start point!");
                return;
            }
            animate(point.getX(), point.getY());
            processCard(point.getX(), point.getY());
        }

        var animateDiv = function(div, style, value) {
            var oldValue = div.css(style);
            var css = {};
            css[style] = value;
            div.animate(css, {duration : "fast", complete: function () {
                css[style] = oldValue;
                div.animate(css, "fast");
            }});
        }

        var animate = function(x, y) {
            var div = getCard(x, y);
            if (div == null) {
                div = getSlot(x, y);
            }
            if (div != false) {
                animateDiv(div, "background-color", "#000");
            }
        }

        var goNext = function() {
            cursor = direction.change(cursor);
            animate(cursor.getX(), cursor.getY());
            processCard(cursor.getX(), cursor.getY());
        }

        return {
            start : start,
            goNext : goNext,
            processCard : processCard
        }
    }

    // ----------------------- save state -------------------
    var saveState = function() {
        var data = [];

        for (var y = 0; y < height; y++) {
            data[y] = [];

            for (var x = 0; x < width; x++) {
                data[y][x] = !!mapSlots[y][x].data('parked') ? getCardIDByCoords(x, y) : null;
            }
        }

        localStorage.setItem('editor.cardcode', JSON.stringify(data));
    };

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

        if (readyForSaving) {
            saveState();
        }
    }

    var isOnCardPile = function(slot) {
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
                doNotRevert	= false;
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

    var moveToInitial = function(card) {
        var slot = card.data('initial');
        park(card, slot);
        card.css({top : '0px', left : '0px'});
    }

    var moveCartToCardPile = function(card) {
        var fromSlot = card.data('parkedTo');
        if (!!fromSlot) {
            fromSlot.data('parked', null);
        }
        card.remove();
    }

    var moveAllCardsToCardPile = function() {
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
	
	var cloneCardOnSlot = function(card, slot) {
		var newCard = cloneCard(card);
		slot.append(newCard);
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

    var getCardIDByCoords = function(x, y) {
        return mapSlots[y][x].data('parked').data('data-type').id;
    };

    var loadState = function() {
        try {
            var data = JSON.parse(localStorage.getItem('editor.cardcode'));
        } catch (err) {
            return;
        }

        if (!data || data.length != height) {
            return;
        }

        var diff = data[0].length - width;

        for(var i = 0; i < diff; ++i) {
            $('.slot-line').each(function(y, line) {
                var element = $('<div class="card-slot"></div>')
                    .appendTo(line);
                mapSlots[y].push(element);
                initDroppable(element);
            });
            width++;
        }

        if (data[0].length > width) {
            for (var i = data[0].length - width; i > 0 ; --i) {
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

                    var slot = mapSlots[y][x];

					cloneCardOnSlot(card, slot);
                });

            }
        }
    };

    var levelUpdate = function(level, multiple, lastPassed){

    };

    initDroppable($('.card-slot'));
    readyForSaving = true;

    var board = null;

    return {
        setStubValue : function() {
            // TODO implement me
        },
        loadSettings : function() {
            loadState();
        },
        compileProgram : function(robot) {
            // do nothing
        },
        cleanProgram : function() {
            running = false;
            board = initBoard();
        },
        isProgramCompiled : function() {
            return true;
        },
        runProgram : function(r) {
            robot = r;
            board.start();
            var deadLoopCounter = 0;
            while (++deadLoopCounter < 100 && running) {
                board.goNext();
            }
        },
        levelUpdate: function(level, multiple, lastPassed) {
            levelUpdate(level, multiple, lastPassed);
        }
    }

}

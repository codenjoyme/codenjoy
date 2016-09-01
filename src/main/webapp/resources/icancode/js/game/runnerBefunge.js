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

    var size = 11;

    var container = $('#ide-content');
    container.empty();
    container.append('<div id="cardPile"></div>' +
                     '<div id="cardSlots"></div>');

    $('.autocomplete').hide();
    $('#ide-help').hide();
    $('.bottom-panel').append('<button class="button help" id="ide-clean">Clean</button>')
    $('#ide-clean').click(function() {
        moveAllCardsToCardPile();
    });

	var cursor = null;
	var value = null;
	var running = false;
	var robot = null;
	
	var finishCommand = function() {
		value = null;
		running = false;
		// console.print('// FINISH');
	}
	
	var commands = [
			{id:'start', type:9, title:'start', process: function(x, y) {
				cursor = pt(x, y);
				direction = Direction.RIGHT;
				value = null;
				running = true;
				// console.print('// BEGIN');
			}, description:'Start reading commands on the right'},

			{id:'finish', type:9, title:'finish', process: finishCommand,
			description:'Finish reading commands'},

			{id:'cursor-right', type:1, title:'cursor-right', process: function(x, y) {
				direction = Direction.RIGHT;
				// console.print('// change processor direction to RIGHT');
			}, description:'Change direction of reading commands to the right'},

			{id:'cursor-left', type:1, title:'cursor-left', process: function(x, y) {
				direction = Direction.LEFT;
				// console.print('// change processor direction to LEFT');
			}, description:'Change direction of reading commands to the left'},

			{id:'cursor-up', type:1, title:'cursor-up', process: function(x, y) {
				direction = Direction.UP;
				// console.print('// change processor direction to UP');
			}, description:'Change direction of reading commands to the top'},

			{id:'cursor-down', type:1, title:'cursor-down', process: function(x, y) {
				direction = Direction.DOWN;
				// console.print('// change processor direction to DOWN');
			}, description:'Change direction of reading commands to the bottom'},

			{id:'robot-left', type:5, title:'robot-left', process: function(x, y) {
				robot.go('LEFT');
				// console.print('robot.goLeft();');
			}, description:'Tells character to go left'},

			{id:'robot-right', type:5, title:'robot-right', process: function(x, y) {
				robot.go('RIGHT');
				// console.print('robot.goRight();');
			}, description:'Tells character to go right'},

			{id:'robot-up', type:5, title:'robot-up', process: function(x, y) {
				robot.go('UP');
				// console.print('robot.goUp();');
			}, description:'Tells character to go up'},

			{id:'robot-down', type:5, title:'robot-down', process: function(x, y) {
				robot.go('DOWN');
				// console.print('robot.goDown();');
			}, description:'Tells character to go down'},

			{id:'robot-go', type:5, title:'robot-go', process: function(x, y) {
				robot.go(value);
				// console.print('robot.go("' + value + '");');
			}, description:'Tells character to go in the direction of VALUE'},

			{id:'robot-jump-left', type:8, title:'robot-jump-left', process: function(x, y) {
                robot.jump('LEFT');
                // console.print('robot.jumpLeft();');
            }, description:'Tells character to jump left'},

            {id:'robot-jump-right', type:8, title:'robot-jump-right', process: function(x, y) {
                robot.jump('RIGHT');
                // console.print('robot.jumpRight();');
            }, description:'Tells character to jump right'},

            {id:'robot-jump-up', type:8, title:'robot-jump-up', process: function(x, y) {
                robot.jump('UP');
                // console.print('robot.jumpUp();');
            }, description:'Tells character to jump up'},

            {id:'robot-jump-down', type:8, title:'robot-jump-down', process: function(x, y) {
                robot.jump('DOWN');
                // console.print('robot.jumpDown();');
            }, description:'Tells character to jump down'},

            {id:'robot-jump', type:8, title:'robot-jump', process: function(x, y) {
                robot.jump(value);
                // console.print('robot.jump("' + value + '");');
            }, description:'Tells character to jump in the direction of VALUE'},

			{id:'if', type:7, title:'if', process: function(x, y) {
				var leftOperand = value;
				var point = direction.change(cursor);
				var rightValue = board.process(point.getX(), point.getY());
				var expression = (leftOperand == rightValue);
				if (expression) {
					// console.print('if ("' + leftOperand + '" == "' + rightValue + '") { !!! } else { ... }');
					direction = direction.contrClockwise();
				} else {
					// console.print('if ("' + leftOperand + '" == "' + rightValue + '") { ... } else { !!! }');
					direction = direction.clockwise();
				}
			}, description:'Compares current VALUE with if\'s VALUE. Direction of reading commands depends on logical outcome'},

			{id:'scanner-at', type:7, title:'scanner-at', process: function(x, y) {
				var oldValue = value;
				value = robot.getScanner().at(oldValue);
				// console.print('value = robot.getScanner().at("' + oldValue + '"); = ' + value)
			}, description:'Gets the value of the object in the pointed direction'},

			{id:'robot-came-from', type:10, title:'robot-came-from', process: function(x, y) {
				value = robot.cameFrom();
				// console.print('value = cameFrom() = ' + value);
			}, description:'Assigns the direction character came from to the VALUE'},

			{id:'robot-previous-direction', type:10, title:'robot-previous-direction', process: function(x, y) {
				value = robot.previousDirection();
				// console.print('value = previousDirection() = ' + value);
			}, description:'Assigns the direction character was moving to the VALUE'},

			{id:'value-left', type:4, title:'value-left', process: function(x, y) {
				value = 'LEFT';
				// console.print('value = "LEFT"');
			}, description:'Assigns LEFT to the VALUE'},

			{id:'value-right', type:4, title:'value-right', process: function(x, y) {
				value = 'RIGHT';
				// console.print('value = "RIGHT"');
			}, description:'Assigns RIGHT to the VALUE'},

			{id:'value-up', type:4, title:'value-up', process: function(x, y) {
				value = 'UP';
				// console.print('value = "UP"');
			}, description:'Assigns UP to the VALUE'},

			{id:'value-down', type:4, title:'value-down', process: function(x, y) {
				value = 'DOWN';
				// console.print('value = "DOWN"');
			}, description:'Assigns DOWN to the VALUE'},

			{id:'value-null', type:6, title:'value-null', process: function(x, y) {
				value = null;
				// console.print('value = null');
			}, description:'Assigns NULL to the VALUE'},

			{id:'value-wall', type:3, title:'value-wall', process: function(x, y) {
				value = 'WALL';
				// console.print('value = "ABYSS"');
			}, description:'Assigns ABYSS to the VALUE'},

			{id:'value-none', type:3, title:'value-none', process: function(x, y) {
				value = 'NONE';
				// console.print('value = "GROUND"');
			}, description:'Assigns GROUND to the VALUE'},

			{id:'value-start', type:3, title:'value-start', process: function(x, y) {
				value = 'START';
				// console.print('value = "START"');
			}, description:'Assigns START to the VALUE'},

			{id:'value-end', type:3, title:'value-end', process: function(x, y) {
				value = 'END';
				// console.print('value = "END"');
			}, description:'Assigns END to the VALUE'},

			{id:'value-gold', type:3, title:'value-gold', process: function(x, y) {
				value = 'GOLD';
				// console.print('value = "GOLD"');
			}, description:'Assigns GOLD to the VALUE'},

			{id:'value-box', type:3, title:'value-box', process: function(x, y) {
				value = 'BOX';
				// console.print('value = "BOX"');
			}, description:'Assigns BRICK to the VALUE'},

			{id:'value-hole', type:3, title:'value-hole', process: function(x, y) {
				value = 'HOLE';
				// console.print('value = "HOLE"');
			}, description:'Assigns HOLE to the VALUE'}
		];

	var mapSlots = [];
	for (var y = 0; y < size; y++) {
		mapSlots[y] = [];
		for (var x = 0; x < size; x++) {
			var element = $('<div></div>')
				.data('data-point', pt(x, y))
				.appendTo('#cardSlots');
			mapSlots[y][x] = element;
		}
	}
	
	for (var index = 0; index < commands.length; index++) {
		for (var count = 0; count < 1; count++) {
			$('<div></div>')
				.data('data-type', commands[index])
				.appendTo('#cardPile');
			
		}
	}

	var mapCards = [];
	var createNewOnPile = function(element) {
		var type = $(element).data('data-type');
		var appended = $('<div class="type-' + type.type + ' '+ type.title +'" title="' + type.description + '"></div>')
			.data('data-type', type)
			.appendTo(element);	
		mapCards.push(appended);
	}
	
	$('#cardPile>div').each(function(index, element) {
		createNewOnPile(element);	
	});
	
	var initBoard = function() {
		var processCard = function(card, x, y) {
			if (!!card) {
				card.data('data-type').process(x, y);
			} else if (card == null) {
				// console.print('// skip empty cell');
			} else {
				// do nothing - we are out of the board
			}
		}
	
		var getSlot = function(x, y) {
			return mapSlots[y][x];
		}
	
		var getCard = function(x, y) {
			if (pt(x, y).isBad(size)) {
				// console.print('// out of the board');
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
            for (var y = 0; y < size; y++) {
                for (var x = 0; x < size; x++) {
                    var slot = mapSlots[y][x];
                    var card = slot.data('parked');
                    if (!card || card.data('data-type').id != id) {
                        continue;
                    }

                    return card;
                }
            }
			console.print('Card with id "' + id + '" not found!');
		}
		
		var start = function() {
			var startCard = find('start');
			if (!startCard) {
			    console.print("Error: Create start point!");
			    return;
			}
			var point = startCard.data('parkedTo').data('data-point');
			animate(point.getX(), point.getY());
			processCard(startCard, point.getX(), point.getY());
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
			// console.print('// processor go ' + direction.name());
			process(cursor.getX(), cursor.getY());
		}
		
		var process = function(x, y) {
			var card = getCard(x, y);
			processCard(card, x, y);
			return value;
		}
		
		return {
			start : start,
			goNext : goNext, 
			process : process
		}
	}
	
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
	$('#cardPile>div>div').draggable({
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
        for (var y = 0; y < size; y++) {
            for (var x = 0; x < size; x++) {
                var slot = mapSlots[y][x];
                var card = slot.data('parked');
                if (!!card) {
                    moveCartToCardPile(card);
                }
            }
        }
	}

	$('#cardPile>div>div').each(function(index, element) {
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

    $('#cardSlots>div, #cardPile>div').droppable({
		hoverClass: 'hovered',
		drop: function (event, ui) {
			var slot = $(this);
			if (slot.hasClass('ui-draggable')) {
				slot = slot.data('parkedTo');
			}
			var card = ui.draggable;

			var busy = !!slot.data('parked')
			if (busy) {
				if (isOnCardPile(slot)) {
					doNotRevert = true;
					moveToInitial(card);
				}
				return;
			}			
			
			if (isOnCardPile(slot)) {
				moveToInitial(card);
			} else {
			    if (!isOnCardPile(card.parent())) {
			        park(card, slot);
			    } else {
                    var newCard = cloneCard(card);
                    slot.append(newCard);
                    $(newCard).draggable({
                        cursor: 'move',
                        revert: onDragRevert
                    });
                    doNotRevert = true;
                    newCard.data('initial', card.data('initial'));
                    park(newCard, slot);
                    newCard.dblclick(function(element) {
                        var card = $(this);
                        moveCartToCardPile(card);
                    });
                }
			}
		}
	});
	
	var board = null;

	return {
	    setStubValue : function() {
            // TODO implement me
	    },
	    loadSettings : function() {
            // TODO implement me
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
        }
	}
	
}
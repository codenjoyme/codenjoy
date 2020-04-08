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
function initRobot(logger, controller) {
    
    if (game.debug) {
        game.debugger();
    }
    
    var memory = null;
    var goThere = null;

    memory = [];
    goThere = null;
    var doTogether = function(direction, command, move) {
        if (!!direction && !validateDirection(direction)) {
            return;
        }
        if (['JUMP', 'PULL'].includes(command)) {
            goThere = direction;
        }
        controller.cleanCommand();
        if (!direction) {
            controller.addCommand(command);
            controller.waitCommand();
        } else {
            controller.addCommand(command + ',' + direction);
            controller.waitCommand();
        }
    }

    var badDirection = function(direction) {
        logger.print("Unexpected direction value '" + direction +
                        "' please use: 'UP', 'DOWN', 'LEFT' or 'RIGHT'.");
    }

    var badDirectionOrPoint = function(directionOrPoint) {
        logger.print("Expected direction or point but was '" + directionOrPoint +
                        "' please use: 'UP', 'DOWN', 'LEFT', 'RIGHT' or 'new Point(x, y)'.");
    }

    var isDirection = function(object) {
        if (!object) {
            return null;
        }
        return Direction.get(object) != null;
    }

    var validateDirection = function(direction) {
        var valid = isDirection(direction);
        if (!valid) {
            badDirection(direction);
        }
        return valid;
    }

    var isTwoInteger = function(arg) {
        return (arg.length == 2 && typeof arg[0] == 'number' && typeof arg[1] == 'number');
    }

    var validateTwoInteger = function(arg, size) {
        var valid = isTwoInteger(arg) && isPoint(pt(arg[0], arg[1]));
        if (!valid) {
            logger.print("You tried to call function(x, y) where 'x' and 'y' are numbers, with parameters [" + Array.from(arg).join(',') + "].");
        } else {
            valid = validateIsOutOf(pt(arg[0], arg[1]), size);
        }
        return valid;
    }

    var validateIsOutOf = function(point, size) {
        if (isOutOf(point, size)) {
            logger.print("Your point is out of board: " + point + ".");
            return false;
        }
        return true;
    }

    var validateTwoIntegerAndElements = function(arg, size) {
        var valid = (arg.length == 3 &&
                typeof arg[0] == 'number' &&
                typeof arg[1] == 'number' &&
                isValidElements(arg[2]));
        if (!valid) {
            logger.print("You tried to call function(x, y, elements) where 'x' and 'y' are numbers, and 'elements' is string or array of strings, with parameters [" + Array.from(arg).join(',') + "].");
        } else {
            valid = validateIsOutOf(pt(arg[0], arg[1]), size);
        }
        return valid;
    }

    var isArrayTypeIs = function(array, type) {
        for (var index in array) {
            if (typeof array[index] != type) {
                return false;
            }
        }
        return true;
    }

    var isValidElements = function(object) {
        var type = 'string';
        return (typeof object == type) ||
            (Array.isArray(object) && isArrayTypeIs(object, type));
    }

    var validateElements = function(arg) {
        var valid = (arg.length == 1 && isValidElements(arg[0]));
        if (!valid) {
            logger.print("You tried to call function(elements) where 'elements' is string or array of strings, with parameters [" + Array.from(arg).join(',') + "].");
        } else {
            var data = arg[0];
            var isArray = Array.isArray(data);
            var data = (!isArray) ? [data] : data;
            valid = data.length == 0 || data.every(e => Element.getElementsTypes().indexOf(e) !== -1);
            if (!valid) {
                logger.print("You tried to call function(elements) where 'elements' is string or array of strings, for non exists element [" + Array.from(arg).join(',') + "].");
            }
        }
        return valid;
    }

    var isOutOf = function(point, size) {
        if (size == undefined) return false;
        var x = point.getX();
        var y = point.getY();
        return x < 0 || x >= size || y < 0 || y >= size;
    }

    var isPoint = function(object) {
        if (!object) {
            return null;
        }
        return (typeof object.getX == 'function' &&
            typeof object.getY == 'function');
    }

    var isArgumentPoint = function(arg) {
        return (arg.length == 1 && isPoint(arg[0]));
    }

    var validatePoint = function(arg, size) {
        var valid = isArgumentPoint(arg);
        if (!valid) {
            logger.print("You tried to call function(point) with parameters [" + Array.from(arg).join(',') + "].");
        } else {
            valid = validateIsOutOf(arg[0], size);
        }
        return valid;
    }

    var validate2Points = function(arg, size) {
        var valid = (arg.length == 2 && isPoint(arg[0]) && isPoint(arg[1]));
        if (!valid) {
            logger.print("You tried to call function(point, point) with parameters [" + Array.from(arg).join(',') + "].");
        } else {
            valid = validateIsOutOf(arg[0], size) && validateIsOutOf(arg[1], size);
        }
        return valid;
    }

    var collect = function(e1, e2, e3) {
        var elements = [];

        if (Array.isArray(e1)) {
            elements = elements.concat(e1);
        } else {
            elements.push(e1);
        }

        if (Array.isArray(e2)) {
            elements = elements.concat(e2);
        } else {
            elements.push(e2);
        }

        if (Array.isArray(e3)) {
            elements = elements.concat(e3);
        } else {
            elements.push(e3);
        }

        var result = [];
        elements.forEach(function(e) {
            if (e != 'NONE' && result.indexOf(e) < 0) {
                result.push(e);
            }
        });

        if (result.length == 0) {
            result.push('NONE');
        }
        return result;
    }

    return {
        nextLevel: function() {
            controller.winCommand();
            controller.waitCommand();
        },
        reset: function() {
            goThere = null;
            controller.cleanCommand();
        },
        log : function(message) {
            if (typeof message == 'function') {
                message = message();
            }
            if (typeof message == 'object') {
                message = JSON.stringify(message);
            }
            logger.print(message);
        },
        invert : function(direction) {
            if (!validateDirection(direction)) {
                return;
            }
            return Direction.get(direction).inverted().name();
        },
        cameFrom : function() {
            if (goThere == null) {
                return null;
            }

            return this.invert(goThere);
        },
        previousDirection : function() {
            if (goThere == null) {
                return null;
            }

            return goThere;
        },
        go : function(direction) {
            if (!validateDirection(direction)) {
                return;
            }
            goThere = direction;
            controller.cleanCommand();
            controller.addCommand(direction);
        },
        goLeft : function() {
            this.go(Direction.LEFT.name());
        },
        goRight : function() {
            this.go(Direction.RIGHT.name());
        },
        goUp : function() {
            this.go(Direction.UP.name());
        },
        goDown : function() {
            this.go(Direction.DOWN.name());
        },
        jump : function(direction) {
           doTogether(direction, 'JUMP');
        },
        jumpLeft : function() {
            this.jump(Direction.LEFT.name());
        },
        jumpRight : function() {
            this.jump(Direction.RIGHT.name());
        },
        jumpUp : function() {
            this.jump(Direction.UP.name());
        },
        jumpDown : function() {
            this.jump(Direction.DOWN.name());
        },
        pull : function(direction) {
            doTogether(direction, 'PULL');
        },
        pullLeft : function() {
            this.pull(Direction.LEFT.name());
        },
        pullRight : function() {
            this.pull(Direction.RIGHT.name());
        },
        pullUp : function() {
            this.pull(Direction.UP.name());
        },
        pullDown : function() {
            this.pull(Direction.DOWN.name());
        },
        fire : function(direction) {
            doTogether(direction, 'FIRE');
        },
        fireLeft : function() {
            this.fire(Direction.LEFT.name());
        },
        fireRight : function() {
            this.fire(Direction.RIGHT.name());
        },
        fireUp : function() {
            this.fire(Direction.UP.name());
        },
        fireDown : function() {
            this.fire(Direction.DOWN.name());
        },
        getMemory : function() {
            return {
                has : function(key) {
                    return memory[key] != undefined;
                },
                save : function(key, value) {
                    memory[key] = value;
                },
                remove : function(key) {
                    var old = memory[key];
                    delete memory[key];
                    return old;
                },
                load : function(key) {
                    return memory[key];
                },
                clean : function() {
                    memory = [];
                }
            };
        },
        getScanner : function() {
            var b = new Board(board);
            var size = b.size();
            var hero = b.getHero();

            var forAll = function(elementTypes, doThat) {
                if (!Array.isArray(elementTypes)) {
                    elementTypes = [elementTypes];
                }
                for (var index in elementTypes) {
                    var elementType = elementTypes[index];
                    var elements = Element.getElementsOfType(elementType);
                    for (var index in elements) {
                        var element = elements[index];
                        if (!!doThat) {
                            doThat(element);
                        }
                    }
                }
            }

            var atNearRobot = function(dx, dy) {
                if (!validateTwoInteger(arguments, undefined)) {
                    return null;
                }

                var x = hero.getX() + dx;
                var y = hero.getY() + dy;

                if (!validateIsOutOf(pt(x, y), size)) {
                    return null;
                }

                var element1 = b.getAt(x, y, LAYER1);
                var element2 = b.getAt(x, y, LAYER2);
                var element3 = b.getAt(x, y, LAYER3);

                return collect(element1.type, element2.type, element3.type);
            }

            var getMe = function() {
                return hero;
            }

            var isAt = function(x, y, elementTypes) {
                if (!validateTwoIntegerAndElements(arguments, size)) {
                    return false;
                }

                if (!Array.isArray(elementTypes)) {
                    elementTypes = [elementTypes];
                }

                for (var index in elementTypes) {
                    var elementType = elementTypes[index];

                    var found = false;
                    forAll(elementType, function(element) {
                        if (b.isAt(x, y, LAYER1, element) ||
                            b.isAt(x, y, LAYER2, element) ||
                            b.isAt(x, y, LAYER3, element))
                        {
                            if (!found) {
                                found = true;
                            }
                        }
                    });
                    if (!found) {
                        return false;
                    }
                }
                return true;
            }

            var getAt = function(x, y) {
                if (!validateTwoInteger(arguments, size)) {
                    return null;
                }

                var element1 = b.getAt(x, y, LAYER1);
                var element2 = b.getAt(x, y, LAYER2);
                var element3 = b.getAt(x, y, LAYER3);

                return collect(element1.type, element2.type, element3.type);
            }

            var findAll = function(elementTypes) {
                if (!validateElements(arguments)) {
                    return null;
                }

                var result = [];
                forAll(elementTypes, function(element) {
                    var found = b.findAll(element, LAYER1);
                    for (var index in found) {
                        result.push(found[index]);
                    }
                    found = b.findAll(element, LAYER2);
                    for (var index in found) {
                        result.push(found[index]);
                    }
                    found = b.findAll(element, LAYER3);
                    for (var index in found) {
                        result.push(found[index]);
                    }
                });
                return result;
            }

            var isAnyOfAt = function(x, y, elementTypes) {
                if (!validateTwoIntegerAndElements(arguments, size)) {
                    return false;
                }

                var elements = [];
                forAll(elementTypes, function(element) {
                    elements.push(element);
                });

                if (b.isAnyOfAt(x, y, LAYER1, elements) ||
                    b.isAnyOfAt(x, y, LAYER2, elements) ||
                    b.isAnyOfAt(x, y, LAYER3, elements))
                {
                    return true;
                }
                return false;
            }

            var isNear = function(x, y, elementTypes) {
                if (!validateTwoIntegerAndElements(arguments, size)) {
                    return false;
                }

                var found = false;
                forAll(elementTypes, function(element) {
                    if (b.isNear(x, y, LAYER1, element) ||
                        b.isNear(x, y, LAYER2, element) ||
                        b.isNear(x, y, LAYER3, element))
                    {
                        found = true;
                    }
                });
                return found;
            }

            var isBarrierAt = function(x, y) {
                if (!validateTwoInteger(arguments, size)) {
                    return false;
                }

                return b.isBarrierAt(x, y);
            }

            var countNear = function(x, y, elementTypes) {
                if (!validateTwoIntegerAndElements(arguments, size)) {
                    return 0;
                }

                var count = 0;
                forAll(elementTypes, function(element) {
                    count += b.countNear(x, y, LAYER1, element);
                    count += b.countNear(x, y, LAYER2, element);
                    count += b.countNear(x, y, LAYER3, element);
                });
                return count;
            }

            var getOtherRobots = function() {
                return b.getOtherHeroes();
            }

            var getLaserMachines = function() {
                return b.getLaserMachines();
            }

            var getLasers = function() {
                return b.getLasers();
            }

            var getWalls = function() {
                return b.getWalls();
            }

            var getBoxes = function() {
                return b.getBoxes();
            }

            var getGold = function() {
                return b.getGold();
            }

            var getZombieStart = function() {
                return b.getZombieStart();
            }

            var getStart = function() {
                return b.getStart();
            }

            var getExit = function() {
                return b.getExit();
            }

            var getHoles = function() {
                return b.getHoles();
            }

            var isMyRobotAlive = function() {
                return b.isMyRobotAlive();
            }

            var getBarriers = function() {
                return b.getBarriers();
            }

            var getElements = function() {
                return Element.getElementsTypes();
            }

            var at = function(directionOrPoint) {
                if (isTwoInteger(arguments)) {
                    directionOrPoint = new Point(arguments[0], arguments[1]);
                }

                if (isPoint(directionOrPoint)) {
                    var point = directionOrPoint;
                    if (!validateIsOutOf(point, size)) {
                        return null;
                    }
                    return getAt(point.getX(), point.getY());
                }

                if (isDirection(directionOrPoint)) {
                    var direction = Direction.get(directionOrPoint);
                    return atNearRobot(direction.changeX(0), direction.changeY(0));
                }

                badDirectionOrPoint(directionOrPoint);
                return null;
            }

            var atLeft = function() {
                return at(Direction.LEFT);
            }

            var atRight = function() {
                return at(Direction.RIGHT);
            }

            var atUp = function() {
                return at(Direction.UP);
            }

            var atDown = function() {
                return at(Direction.DOWN);
            }

            var getShortestWayTo = function(elementType) {
                var points = findAll(elementType);
                var paths = [];
                for (var index in points) {
                    var point = points[index];

                    var path = getShortestWay(point)
                    paths.push(path);
                }

                var shortest = 1000;
                var shortestIndex = -1;
                for (var index in paths) {
                    var path = paths[index];

                    if (path.length == 0) {
                        continue;
                    }

                    if (path.length < shortest) {
                        shortest = path.length;
                        shortestIndex = index;
                    }
                }

                if (shortestIndex == -1) {
                    return;
                }

                return paths[shortestIndex];
            }

            var getShortestWay = function(pt1OrElementType, pt2) {
                if (!!pt2) {
                    var pt1 = pt1OrElementType;
                    if (!validate2Points(arguments, size)) {
                        return null;
                    }
                    return b.getShortestWay(pt1, pt2);
                }

                if (isArgumentPoint(arguments)) {
                    var elementType = pt1OrElementType;
                    if (!validatePoint(arguments, size)) {
                        return null;
                    }
                    return b.getShortestWay(hero, elementType);
                }

                if (!validateElements(arguments)) {
                    return null;
                }
                var pt = pt1OrElementType;
                return getShortestWayTo(pt);
            }

            var getWholeBoard = function() {
                return b.getWholeBoard();
            }

            var getScannerOffset = function() {
                return b.getScannerOffset();
            }

            return {
                at : at,
                atLeft : atLeft,
                atRight : atRight,
                atUp : atUp,
                atDown : atDown,
                atNearRobot : atNearRobot,
                getMe : getMe,
                isAt : isAt,
                getAt : getAt,
                findAll : findAll,
                isAnyOfAt : isAnyOfAt,
                isNear : isNear,
                isBarrierAt : isBarrierAt,
                countNear : countNear,
                getOtherRobots : getOtherRobots,
                getLaserMachines : getLaserMachines,
                getLasers : getLasers,
                getWalls : getWalls,
                getBoxes : getBoxes,
                getGold : getGold,
                getStart : getStart,
                getZombieStart : getZombieStart,
                getExit : getExit,
                getHoles : getHoles,
                isMyRobotAlive : isMyRobotAlive,
                getBarriers : getBarriers,
                getElements : getElements,
                getShortestWay : getShortestWay,
                getWholeBoard : getWholeBoard,
                getScannerOffset : getScannerOffset
            }
        }
    };
};
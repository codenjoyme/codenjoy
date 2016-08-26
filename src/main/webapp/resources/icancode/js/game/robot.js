var initRobot = function(console, controller) {
    var memory = null;
    var goThere = null;

    memory = [];
    goThere = null;
    var doTogether = function(direction, command) {
        goThere = direction;
        controller.cleanCommand();
        if (!direction) {
            controller.addCommand(command);
        } else {
            controller.addCommand(command + ',' + direction);
            controller.waitCommand();
        }
    }

    return {
        nextLevel: function() {
            controller.winCommand();
            controller.waitCommand();
        },
        log : function(message) {
            if (typeof message == 'fucntion') {
                message = message();
            }
            if (typeof message == 'object') {
                message = JSON.stringify(message);
            }
            console.print("Robot says: " + message);
        },
        invert : function(direction) {
            if (direction == "LEFT") return "RIGHT"; // TODO to use Direction.inverted()
            if (direction == "RIGHT") return "LEFT";
            if (direction == "DOWN") return "UP";
            if (direction == "UP") return "DOWN";
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
            var hero = b.getHero();

            var forAll = function(elementType, doThat) {
                var elements = Element.getElementsOfType(elementType);
                for (var index in elements) {
                    var element = elements[index];
                    if (!!doThat) {
                        doThat(element);
                    }
                }
            }

            var atNearRobot = function(dx, dy) {
                var element1 = b.getAt(hero.getX() + dx, hero.getY() + dy, LAYER1);
                var element2 = b.getAt(hero.getX() + dx, hero.getY() + dy, LAYER2);

                var result = [];
                result.push(element1.type);
                if (element2.type != 'NONE') {
                    result.push(element2.type);
                }
                return result;
            }

            var getMe = function() {
                return hero;
            }

            var isAt = function(x, y, elementType) {
                var found = false;
                forAll(elementType, function(element) {
                    if (b.isAt(x, y, LAYER1, element) ||
                        b.isAt(x, y, LAYER2, element))
                    {
                        found = true;
                    }
                });
                return found;
            }

            var getAt = function(x, y) {
                var result = [];
                var atLayer1 = b.getAt(x, y, LAYER1).type;
                var atLayer2 = b.getAt(x, y, LAYER2).type;
                if (atLayer1 != 'NONE') {
                    result.push(atLayer1);
                }
                if (atLayer2 != 'NONE') {
                    result.push(atLayer2);
                }
                if (result.length == 0) {
                    result.push('NONE');
                }
                return result;
            }

            var findAll = function(elementType) {
                var result = [];
                forAll(elementType, function(element) {
                    var found = b.findAll(element, LAYER1);
                    for (var index in found) {
                        result.push(found[index]);
                    }
                    found = b.findAll(element, LAYER2);
                    for (var index in found) {
                        result.push(found[index]);
                    }
                });
                return result;
            }

            var isAnyOfAt = function(x, y, elementTypes) {
                var elements = [];
                for (var index in elementTypes) {
                    var elementType = elementTypes[index];
                    forAll(elementType, function(element) {
                        elements.push(element);
                    });
                }

                if (b.isAnyOfAt(x, y, LAYER1, elements) ||
                    b.isAnyOfAt(x, y, LAYER2, elements))
                {
                    return true;
                }
                return false;
            }

            var isNear = function(x, y, elementTypes) {
                if (!Array.isArray(elementTypes)) {
                    elementTypes = [elementTypes];
                }
                var found = false;
                for(var index in elementTypes) {
                    forAll(elementTypes[index], function(element) {
                        if (b.isNear(x, y, LAYER1, element) ||
                            b.isNear(x, y, LAYER2, element))
                        {
                            found = true;
                        }
                    });
                }
                return found;
            }

            var isBarrierAt = function(x, y) {
                return b.isBarrierAt(x, y);
            }

            var countNear = function(x, y, elementType) {
                var count = 0;
                forAll(elementType, function(element) {
                    count += b.countNear(x, y, LAYER1, element);
                    count += b.countNear(x, y, LAYER2, element);
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

            var at = function(direction) {
                if (!!direction && typeof direction.getX == 'function') {
                    var point = direction;
                    return getAt(point.getX(), point.getY());
                } else {
                    var d = Direction.get(direction);
                    return atNearRobot(d.changeX(0), d.changeY(0));
                }
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

            var getShortestWay = function(to) {
                return b.getShortestWay(getMe(), to)[1];
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
                getExit : getExit,
                getHoles : getHoles,
                isMyRobotAlive : isMyRobotAlive,
                getBarriers : getBarriers,
                getElements : getElements,
                getShortestWay : getShortestWay
            }
        }
    };
}

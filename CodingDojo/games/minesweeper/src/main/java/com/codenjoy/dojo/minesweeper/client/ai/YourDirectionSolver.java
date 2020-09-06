package com.codenjoy.dojo.minesweeper.client.ai;

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


import com.Direction;
import com.DirectionSolver;
import com.codenjoy.dojo.services.Dice;
import com.logic.Field;
import com.logic.PlayField;
import com.utils.BoardImpl;
import com.utils.Point;
import com.wave.WaveField;

import java.util.*;

// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
// \com\vaa25\minesweeper-ai\1.0\minesweeper-ai-1.0.jar!\com\YourDirectionSolver.class
// because of Random in code - I want to test this logic
// please check TODO FIXED
public class YourDirectionSolver implements DirectionSolver {
    static List<Direction> path = new LinkedList();
    private Dice dice;
    private BoardImpl board;
    private Point myCoord;
    private int[][] field;
    private StringBuilder turns = new StringBuilder();
    private char movedTo;
    private List<Direction> safePath = new ArrayList();
    private final String user;

    public YourDirectionSolver(Dice dice) {
        this.dice = dice;
        this.user = "minesweeper-super-ai";
    }

    public YourDirectionSolver(Dice dice, String user) {
        this.dice = dice;
        this.user = user;
    }

    public String get(BoardImpl board) {
        System.out.println(board.toString());
        String result;
        if(board.isGameOver()) {
            StringBuilder playField = new StringBuilder();
            this.buildMethodCaption(board, playField);
            playField.append(this.turns);
            playField.append("        String production=\"\"\n").append(board.toString()).append("        ;\n");
            playField.append("        System.out.println(\"Production is:\\n\"+production);\n");
            playField.append("        System.out.println(\"Test is:\");\n").append("        System.out.println(printerFactory.getPrinter(game.reader(), null).print());\n");
            playField.append("        assertBoard(production);\n    }\n");
            this.field = (int[][])null;
            result = Direction.STOP.toString();
        } else {
            this.board = board;
            if(this.isFirstTurn()) {
                result = Direction.UP.toString();
            } else {
                if(this.field == null) {
                    this.createField();
                }

                this.field = this.fillFieldWithBoard();
                PlayField playField1 = new PlayField(this.field, 0);
                Field field = new Field(playField1);
                field.setMyCoord(this.myCoord);

                try {
                    field.play(this.user);
                    Point[] e = field.getToMark();
                    Point[] toOpen = field.getToOpen();
                    if(this.isOnJustMarked(e) || this.movedTo == 42 && e.length == 0 && field.getMinPossibility() > 0.0D) {
                        result = this.getEscapeTo();
                    } else {
                        Map newPoint = this.toMap(e, toOpen);
                        Map.Entry closest = this.getClosest(newPoint);
                        if(closest != null) {
                            if(this.isNeighbours((Point)closest.getKey(), this.myCoord)) {
                                result = this.getAction(closest).toString();
                            } else {
                                this.setSafePathTo((Point)closest.getKey());
                                result = whereToGo();
                            }
                        } else {
                            result = this.getEscapeTo();
                        }
                    }

                    if(result.startsWith("ACT,") && this.movedTo == 45) {
                        result = this.getEscapeTo();
                    }

                    if(result.startsWith("ACT,")) {
                        Direction newPoint1 = Direction.valueOf(result.split(",")[1]);
                        this.turns.append("        unbomb");
                        this.movedTo = 45;
                    } else {
                        this.turns.append("        move");
                        Point newPoint2 = this.getChangedPoint(board.getMe(), Direction.valueOf(result));
                        this.movedTo = board.getAt(newPoint2.getX(), newPoint2.getY()).getChar();
                    }

                    if(result.endsWith("RIGHT")) {
                        this.turns.append("Right();\n");
                    }

                    if(result.endsWith("LEFT")) {
                        this.turns.append("Left();\n");
                    }

                    if(result.endsWith("UP")) {
                        this.turns.append("Up();\n");
                    }

                    if(result.endsWith("DOWN")) {
                        this.turns.append("Down();\n");
                    }
                } catch (Exception var9) {
                    result = this.getEscapeTo();
                }
            }
        }

        System.out.println(Calendar.getInstance().getTime() + ":   " + result);
        return result;
    }

    private String whereToGo() {
        List<Direction> safePath = this.safePath;
        Collections.sort(safePath); // TODO FIXED добавлена эта строчка, потому что если несколько направлений куда можно пойти то они рендомно приходят что приводит к гейзенбаге
        return safePath.remove(0).toString();
    }

    private boolean isNeighbours(Point point1, Point point2) {
        return point1.distance(point2) <= 1.0D;
    }

    private boolean isFirstTurn() {
        return this.board.getAt(1, this.board.size() - 3).getChar() == 42 && this.board.getAt(2, this.board.size() - 2).getChar() == 42;
    }

    private Point getChangedPoint(Point point, Direction direction) {
        return new Point(direction.changeX(point.getX()), direction.changeY(point.getY()));
    }

    private void buildMethodCaption(BoardImpl board, StringBuilder test) {
        test.append("    @Test\n").append("    public void findBug() {\n").append("        size=").append(board.size()).append(";\n").append("        detectorCharge=").append(this.countDetectorCharge(board)).append(";\n").append("        shouldBoardWith(\n                new Sapper(1, 1)\n");
        this.buildMines(board, test);
        test.append("        );\n");
    }

    private void buildMines(BoardImpl board, StringBuilder caption) {
        for(int i = 0; i < board.size(); ++i) {
            for(int j = 0; j < board.size(); ++j) {
                char c = board.getAt(i, j).getChar();
                if(c == 9787 || c == 120 || c == 1120) {
                    caption.append("                ,new Mine(").append(i).append(',').append(board.size() - 1 - j).append(")\n");
                }
            }
        }

    }

    private int countDetectorCharge(BoardImpl board) {
        int detectorCharge = 0;

        for(int i = 0; i < board.size(); ++i) {
            for(int j = 0; j < board.size(); ++j) {
                char c = board.getAt(i, j).getChar();
                if(c == 9787 || c == 120 || c == 1120) {
                    ++detectorCharge;
                }
            }
        }

        return detectorCharge;
    }

    private String getEscapeTo() {
        int width = this.field.length;
        int height = this.field[0].length;
        return this.myCoord.getX() > 0 && this.field[this.myCoord.getX() - 1][this.myCoord.getY()] != 9?Direction.LEFT.toString():(this.myCoord.getX() < width - 1 && this.field[this.myCoord.getX() + 1][this.myCoord.getY()] != 9?Direction.RIGHT.toString():(this.myCoord.getY() > 0 && this.field[this.myCoord.getX()][this.myCoord.getY() - 1] != 9?Direction.UP.toString():(this.myCoord.getY() < height - 1 && this.field[this.myCoord.getX()][this.myCoord.getY() + 1] != 9?Direction.DOWN.toString():null)));
    }

    private void createField() {
        this.field = new int[this.board.size() - 2][this.board.size() - 2];

        for(int i = 0; i < this.field.length; ++i) {
            for(int j = 0; j < this.field[i].length; ++j) {
                this.field[i][j] = 9;
            }
        }

    }

    private boolean isOnJustMarked(Point[] toMark) {
        Point[] arr$ = toMark;
        int len$ = toMark.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Point point = arr$[i$];
            if(point.equals(this.myCoord)) {
                return true;
            }
        }

        return false;
    }

    private String getAction(Map.Entry<Point, Boolean> destination) {
        int dx = ((Point)destination.getKey()).getX() - this.myCoord.getX();
        int dy = ((Point)destination.getKey()).getY() - this.myCoord.getY();
        String result;
        Point neighbour;
        if(Math.abs(dx) > Math.abs(dy)) {
            neighbour = new Point(this.myCoord.getX() + (int)Math.signum((float)dx), this.myCoord.getY());
            if(this.field[neighbour.getX()][neighbour.getY()] == 9 && !neighbour.equals(destination.getKey())) {
                result = this.getDirectionBydY(dy);
            } else {
                result = this.getDirectionBydX(dx);
            }
        } else {
            neighbour = new Point(this.myCoord.getX(), this.myCoord.getY() + (int)Math.signum((float)dy));
            if(this.field[neighbour.getX()][neighbour.getY()] == 9 && !neighbour.equals(destination.getKey())) {
                result = this.getDirectionBydX(dx);
            } else {
                result = this.getDirectionBydY(dy);
            }
        }

        if(neighbour.equals(destination.getKey()) && !((Boolean)destination.getValue()).booleanValue()) {
            result = Direction.ACT.toString() + ',' + result;
        }

        return result;
    }

    private void setSafePathTo(Point target) {
        WaveField waveField = new WaveField(this.board);
        target = new Point(target.getX() + 1, target.getY() + 1);
        this.safePath = waveField.findWay(target);
    }

    private String getDirectionBydX(int dx) {
        String result;
        if(dx > 0) {
            result = Direction.RIGHT.toString();
        } else if(dx < 0) {
            result = Direction.LEFT.toString();
        } else {
            // TODO FIXED тут убран Random
            result = Direction.valueOf(dice.next(2)).toString();
        }

        return result;
    }

    private String getDirectionBydY(int dy) {
        String result;
        if(dy < 0) {
            result = Direction.UP.toString();
        } else if(dy > 0) {
            result = Direction.DOWN.toString();
        } else {
            // TODO FIXED тут убран Random
            result = Direction.valueOf(dice.next(2) + 2).toString();
        }

        return result;
    }

    private Map<Point, Boolean> toMap(Point[] toMark, Point[] toOpen) {
        HashMap result = new HashMap();
        Point[] arr$ = toMark;
        int len$ = toMark.length;

        int i$;
        Point point;
        for(i$ = 0; i$ < len$; ++i$) {
            point = arr$[i$];
            result.put(point, Boolean.valueOf(false));
        }

        arr$ = toOpen;
        len$ = toOpen.length;

        for(i$ = 0; i$ < len$; ++i$) {
            point = arr$[i$];
            result.put(point, Boolean.valueOf(true));
        }

        return result;
    }

    private Map.Entry<Point, Boolean> getClosest(Map<Point, Boolean> points) {
        double minDistance = 1.7976931348623157E308D;
        Map.Entry result = null;
        Iterator i$ = points.entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry entry = (Map.Entry)i$.next();
            if(!((Point)entry.getKey()).equals(this.myCoord)) {
                double distance = this.myCoord.distance((Point)entry.getKey());
                if(distance < minDistance) {
                    minDistance = distance;
                    result = entry;
                }
            }
        }

        return result;
    }

    private int[][] fillFieldWithBoard() {
        int[][] result = new int[this.board.size() - 2][this.board.size() - 2];

        for(int i = 0; i < result.length; ++i) {
            for(int j = 0; j < result[i].length; ++j) {
                char element = this.board.getAt(i + 1, j + 1).getChar();
                if(element > 48 && element < 57) {
                    result[i][j] = Character.getNumericValue(element);
                } else if(element == 42) {
                    result[i][j] = 9;
                } else if(element == 8252) {
                    result[i][j] = 11;
                } else if(element == 32) {
                    result[i][j] = 0;
                } else if(element == 1120) {
                    result[i][j] = 12;
                } else if(element == 9786) {
                    this.myCoord = new Point(i, j);
                    result[i][j] = this.field[i][j];
                }
            }
        }

        return result;
    }
}


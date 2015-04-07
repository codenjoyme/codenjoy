package com.codenjoy.dojo.minesweeper.client.ai.vaa25;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Field;
import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.PlayField;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.RandomDice;

import java.util.*;

/**
 * @author Alexander Vlasov
 */
public class MySolver implements Solver<Board> {
    static List<Direction> path = new LinkedList<Direction>();
    private final int UNKNOWN = 9;
    private final int FLAG = 11;
    private final int EXPLODE = 12;
    private Dice dice;
    private Board board;
    private int mines;
    private Point myCoord;
    private char myChar;
    private int[][] field;
    private StringBuilder turns = new StringBuilder();
    private char movedTo;
    private boolean prevWasAct;
    private List<Direction> safePath = new ArrayList<Direction>();
//    private BugResolver bugResolver;


    public MySolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        String result;
        System.out.println(board.toString());
        if (board.isGameOver()) {
            StringBuilder test = new StringBuilder();
            buildMethodCaption(board, test);
            test.append(turns);
            test.append("        String production=\"\"\n").append(board.toString()).append("        ;\n");
            test.append("        System.out.println(\"Production is:\\n\"+production);\n");
            test.append("        System.out.println(\"Test is:\");\n")
                    .append("        System.out.println(printerFactory.getPrinter(game.reader(), null).print());\n");
            test.append("        assertBoard(production);\n    }\n");
//            System.out.println(test.toString());
//            System.exit(0);
            field = null;
//            bugResolver=null;
            result = Direction.STOP.toString();
        } else {
            this.board = board;
            if (field == null) initField();
            field = createField();
            PlayField playField = new PlayField(field, 0);
            Field field = new Field(playField);
            field.setMyCoord(myCoord);
            try {
                field.play();

                Point[] toMark = field.getToMark();
                Point[] toOpen = field.getToOpen();

                if (isFirstTurn()) {
                    result = Direction.UP.toString();
                } else {
                    if (isOnJustMarked(toMark) || (movedTo == '*' && toMark.length == 0 && field.getMinPossibility() > 0)) {
                        result = getEscapeTo();
                    } else {
                        Map<Point, Boolean> map = toMap(toMark, toOpen);   //true - toOpen, false - toMark
                        Map.Entry<Point, Boolean> closest = getClosest(map);
                        if (closest != null) {
                            if (isNeighbours(closest.getKey(), myCoord)) {
                                result = getAction(closest).toString();
                            } else {
                                setSafePathTo(closest.getKey());
                                result = safePath.remove(0).toString();
                            }
                        } else {
                            result = getEscapeTo();
                        }

                    }

                }
                if (result.startsWith("ACT,")) {
                    if (movedTo == '-') {
                        result = getEscapeTo();
                    }
                }
                if (result.startsWith("ACT,")) {
                    Direction direction = Direction.valueOf(result.split(",")[1]);
//                    bugResolver=new BugResolver(board,direction);

                    turns.append("        unbomb");

                    movedTo = '-';
                } else {
                    turns.append("        move");
                    Point newPoint = getChangedPoint(board.getMe(), Direction.valueOf(result));
                    movedTo = board.getAt(newPoint.getX(), newPoint.getY()).ch();
                }

                if (result.endsWith("RIGHT")) turns.append("Right();\n");
                if (result.endsWith("LEFT")) turns.append("Left();\n");
                if (result.endsWith("UP")) turns.append("Up();\n");
                if (result.endsWith("DOWN")) turns.append("Down();\n");
//            }
            } catch (Exception e) {
                result = getEscapeTo();
            }
        }

        System.out.println(Calendar.getInstance().getTime() + ":   " + result);
        return result;
    }

    private boolean isNeighbours(Point point1, Point point2) {
        return (point1.distance(point2) <= 1);
    }

    private boolean isFirstTurn() {
//        System.out.println(board.getMe().getX()+" "+board.getMe().getY()+" "+board.size());
        return board.getAt(1, board.size() - 3).ch() == '*'
                && board.getAt(board.size() - 3, 1).ch() == '*';
    }

    private Point getChangedPoint(Point point, Direction direction) {
        return new PointImpl(direction.changeX(point.getX()), direction.changeY(point.getY()));
    }

    private void buildMethodCaption(Board board, StringBuilder test) {
        test.append("    @Test\n")
                .append("    public void findBug() {\n")
                .append("        size=").append(board.size()).append(";\n")
                .append("        detectorCharge=").append(countDetectorCharge(board)).append(";\n")
                .append("        shouldBoardWith(\n                new Sapper(1, 1)\n");
        buildMines(board, test);
        test.append("        );\n");
    }

    private void buildMines(Board board, StringBuilder caption) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                char c = board.getAt(i, j).ch();
                if (c == '☻' || c == 'x' || c == 'Ѡ') {
                    caption.append("                ,new Mine(")
                            .append(i)
                            .append(',')
                            .append(board.size() - 1 - j)
                            .append(")\n");
                }
            }
        }
    }

    private int countDetectorCharge(Board board) {
        int detectorCharge = 0;
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                char c = board.getAt(i, j).ch();
                if (c == '☻' || c == 'x' || c == 'Ѡ') {
                    detectorCharge++;
                }
            }
        }
        return detectorCharge;
    }

    private String getEscapeTo() {
        int width = field.length;
        int height = field[0].length;
        if (myCoord.getX() > 0 && field[myCoord.getX() - 1][myCoord.getY()] != 9) return Direction.LEFT.toString();
        if (myCoord.getX() < width - 1 && field[myCoord.getX() + 1][myCoord.getY()] != 9)
            return Direction.RIGHT.toString();
        if (myCoord.getY() > 0 && field[myCoord.getX()][myCoord.getY() - 1] != 9) return Direction.UP.toString();
        if (myCoord.getY() < height - 1 && field[myCoord.getX()][myCoord.getY() + 1] != 9)
            return Direction.DOWN.toString();
        return null;
    }

    private void initField() {
        field = new int[board.size() - 2][board.size() - 2];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = 9;
            }
        }
    }

    private boolean isOnJustMarked(Point[] toMark) {
        for (Point point : toMark) {
            if (point.equals(myCoord)) return true;
        }
        return false;
    }

    private boolean isOnMark(Point[] toMark, Point victim) {
        for (Point point : toMark) {
            if (point.equals(victim)) return true;
        }
        return false;
    }

    private String getAction(Map.Entry<Point, Boolean> destination) {
        String result;
        int dx = destination.getKey().getX() - myCoord.getX();
        int dy = destination.getKey().getY() - myCoord.getY();
        Point neighbour;
        if (Math.abs(dx) > Math.abs(dy)) {
            neighbour = new PointImpl(myCoord.getX() + (int) Math.signum(dx), myCoord.getY());
            if (field[neighbour.getX()][neighbour.getY()] != 9
                    || neighbour.equals(destination.getKey())) {
                result = getDirectionBydX(dx);
            } else {
                result = getDirectionBydY(dy);
            }
        } else {
            neighbour = new PointImpl(myCoord.getX(), myCoord.getY() + (int) Math.signum(dy));
            if (field[neighbour.getX()][neighbour.getY()] != 9
                    || neighbour.equals(destination.getKey())) {
                result = getDirectionBydY(dy);
            } else {
                result = getDirectionBydX(dx);
            }
        }

        if (neighbour.equals(destination.getKey()) && destination.getValue() == false) {
            result = Direction.ACT.toString() + ',' + result;
        }
        return result;
    }

    private void setSafePathTo(Point target) {
        PathFinderAdapter pathFinderAdapter = new PathFinderAdapter(board, target);
        safePath = pathFinderAdapter.execute();
    }

    private String getDirectionBydX(int dx) {
        String result;
        if (dx > 0) result = Direction.RIGHT.toString();
        else if (dx < 0) result = Direction.LEFT.toString();
        else result = Direction.valueOf(new Random().nextInt(2)).toString();
        return result;
    }

    private String getDirectionBydY(int dy) {
        String result;
        if (dy < 0) result = Direction.UP.toString();
        else if (dy > 0) result = Direction.DOWN.toString();
        else result = Direction.valueOf(new Random().nextInt(2) + 2).toString();
        return result;
    }

    private Map<Point, Boolean> toMap(Point[] toMark, Point[] toOpen) {
        Map<Point, Boolean> result = new HashMap<Point, Boolean>();
        for (Point point : toMark) {
            result.put(point, false);
        }
        for (Point point : toOpen) {
            result.put(point, true);
        }
        return result;
    }

    private Map.Entry<Point, Boolean> getClosest(Map<Point, Boolean> points) {
        double minDistance = Double.MAX_VALUE;
        Map.Entry<Point, Boolean> result = null;
        for (Map.Entry<Point, Boolean> entry : points.entrySet()) {
            if (!entry.getKey().equals(myCoord)) {
                double distance = myCoord.distance(entry.getKey());
                if (distance < minDistance) {
                    minDistance = distance;
                    result = entry;
                }
            }
        }
        return result;
    }

    private int[][] createField() {
        mines = 0;
        int[][] result = new int[board.size() - 2][board.size() - 2];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                char element = board.getAt(i + 1, j + 1).ch();
                if (element > '0' && element < '9') result[i][j] = Character.getNumericValue(element);
                else if (element == '*') result[i][j] = UNKNOWN;
                else if (element == '‼') {
                    result[i][j] = FLAG;
                    mines++;
                } else if (element == ' ') result[i][j] = 0;
                else if (element == 'Ѡ') result[i][j] = EXPLODE;
                else if (element == '☺') {
                    myCoord = new PointImpl(i, j);
                    result[i][j] = field[i][j];
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new MySolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

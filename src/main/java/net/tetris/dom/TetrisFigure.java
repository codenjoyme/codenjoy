package net.tetris.dom;

public class TetrisFigure implements Figure {
    private int centerX;
    private int centerY;
    private Type type;
    public String[] rows = new String[]{"#"};
    private int[] codes;
    private FigurePattern[] rotationPatterns;
    private FigurePattern currentPattern;

    TetrisFigure() {
        this(0, 0, "#");

    }

    public TetrisFigure(int centerX, int centerY, String... rows) {
        this(centerX, centerY, Type.I, rows);

    }

    public TetrisFigure(int centerX, int centerY, Type type, String... rows) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.type = type;
        parseRows(rows);
    }

    private void parseRows(String... rows) {
        this.rows = rows;
        codes = new int[rows.length];
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            codes[i] = Integer.parseInt(row.replace('#', '1').replace(' ', '0'), 2);
        }
    }

    public int getLeft() {
        return centerX;
    }

    public int getRight() {
        return rows[0].length() - centerX - 1;
    }

    public int getTop() {
        return centerY;
    }

    public int getBottom() {
        return rows.length - centerY - 1;
    }

    @Override
    public int[] getRowCodes() {
        return codes;
    }

    public void rotate(int times) {
        for (int i = 0; i < times; i++) {
            performRotate();
        }
    }

    private void performRotate() {
        char newRows[][] = new char[getWidth()][rows.length];
        int newX = rows.length - centerY - 1;
        int newY = getLeft();
        int cos90 = 0;
        int sin90 = 1;
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                int shiftedX = x - centerX;
                int shiftedY = y - centerY;

                int xCoord = shiftedX * cos90 - shiftedY * sin90;
                int yCoord = shiftedX * sin90 + shiftedY * cos90;
                newRows[yCoord + newY][xCoord + newX] = c;
            }
        }

        String[] rows = new String[newRows.length];
        for (int i = 0; i < newRows.length; i++) {
            rows[i] = String.copyValueOf(newRows[i]);
        }

        parseRows(rows);
        centerX = newX;
        centerY = newY;
    }

    @Override
    public int getWidth() {
        return getLeft() + getRight() + 1;
    }

    @Override
    public Type getType() {
        return type;
    }
}

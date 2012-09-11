package net.tetris.dom;

public class TetrisFigure implements Figure, Cloneable {
    private int centerX;
    private int centerY;
    private Type type;
    public String[] rows = new String[]{"#"};
    private int[] codes;

    @Deprecated
    public TetrisFigure() {
        this(0, 0, "#");
    }

    @Deprecated
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

    public Figure rotate(int times) {
        int realRotates = times % 4;
        for (int i = 0; i < realRotates; i++) {
            performRotate();
        }
        return this;
    }

    private void performRotate() {
        char newRows[][] = new char[getWidth()][rows.length];
        int newX = rows.length - centerY - 1;
        int newY = getLeft();
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                int shiftedX = x - centerX;
                int shiftedY = y - centerY;
                newRows[shiftedX + newY][- shiftedY + newX] = c;
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
    public Figure getCopy() {
        try {
            return (Figure) clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Type getType() {
        return type;
    }
}

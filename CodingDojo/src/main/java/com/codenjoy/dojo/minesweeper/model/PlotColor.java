package com.codenjoy.dojo.minesweeper.model;

/**
 * User: oleksandr.baglai
 * Date: 3/24/13
 * Time: 12:03 AM
 */
public enum PlotColor {
    BANG('Ѡ'), HERE_IS_BOMB('☻'), DETECTOR('☺'), FLAG('‼'), HIDDEN('*'),
    ONE_MINE('1'), TWO_MINES('2'), THREE_MINES('3'), FOUR_MINES('4'),
    FIVE_MINES('5'), SIX_MINES('6'), SEVEN_MINES('7'), EIGHT_MINES('8'),
    BORDER('☼'), NO_MINE(' ');

    private char ch;

    PlotColor(char ch) {
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static PlotColor printMinesCount(int minesNear) {
        switch (minesNear) {
            case 1 : return PlotColor.ONE_MINE;
            case 2 : return PlotColor.TWO_MINES;
            case 3 : return PlotColor.THREE_MINES;
            case 4 : return PlotColor.FOUR_MINES;
            case 5 : return PlotColor.FIVE_MINES;
            case 6 : return PlotColor.SIX_MINES;
            case 7 : return PlotColor.SEVEN_MINES;
            case 8 : return PlotColor.EIGHT_MINES;
            default : return PlotColor.DETECTOR;
        }
    }
}

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
}

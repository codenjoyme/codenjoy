package com.codenjoy.dojo.services;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 3:43 PM
 */
public class GuiPlotColorDecoder { // TODO test me

    private static String GUI = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0";
    private Object[] values;

    public GuiPlotColorDecoder(Object[] values) {
        this.values = values;
    }

    private char getGuiChar(char consoleChar) {
        return GUI.charAt(getIndex(consoleChar));
    }

    private int getIndex(char consoleChar) {
        for (int index = 0; index < values.length; index++) {
            if (values[index].toString().equals(String.valueOf(consoleChar))) {
                return index;
            }
        }
        throw new IllegalArgumentException("Not enum symbol '" + consoleChar + "'");
    }

    public String encode(String board) {
        char[] chars = board.replace("\n", "").toCharArray();
        for (int index = 0; index < chars.length; index++) {
            chars[index] = getGuiChar(chars[index]);
        }
        return String.copyValueOf(chars);
    }

}

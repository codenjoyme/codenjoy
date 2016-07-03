package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class GuiPlotColorDecoder {

    public static String GUI = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    private Object[] values;

    public GuiPlotColorDecoder(Object[] values) {
        this.values = values;
    }

    private char getGuiChar(char consoleChar) {
//        try {
            return GUI.charAt(getIndex(consoleChar));
//        } catch (Exception e) {
//            System.out.println(consoleChar);
//            return ' ';
//        }
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
        board = oneLine(board);
        if (board.startsWith("{\"")) {
            List<String> encodedLayers = new LinkedList<>();
            JSONObject object = new JSONObject(board);
            String key = "layers";
            JSONArray layers = object.getJSONArray(key);
            for (int i = 0; i < layers.length(); i++) {
                String layer = layers.getString(i);
                String encoded = encodeBoard(layer);
                encodedLayers.add(encoded);
            }
            object.remove(key);
            object.put(key, new JSONArray(encodedLayers));
            return object.toString();
        } else {
            return encodeBoard(board);
        }
    }

    private String encodeBoard(String board) {
        char[] chars = board.toCharArray();
        for (int index = 0; index < chars.length; index++) {
            chars[index] = getGuiChar(chars[index]);
        }
        return String.copyValueOf(chars);
    }

    private String oneLine(String string) {
        return string.replace("\n", "");
    }

}

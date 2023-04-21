package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.services.multiplayer.types.LevelsType;
import com.codenjoy.dojo.services.printer.CharElement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.stream.Collector;

@Slf4j
@AllArgsConstructor
public class GuiPlotColorDecoder {

    public static String GUI = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõöøùúûüýþÿ";
    private CharElement[] values;

    private char getGuiChar(char ch) {
        return GUI.charAt(getIndex(ch));
    }

    private int getIndex(char ch) {
        for (int index = 0; index < values.length; index++) {
            if (values[index].ch() == ch) {
                return index;
            }
        }
        throw new IllegalArgumentException("Not enum symbol '" + ch + "'");
    }

    public String encodeForClient(Object board) {
        return board.toString().replaceAll("\n", "");
    }

    public Object encodeForBrowser(Object board) {
        if (board instanceof String) {
            return encode((String)board);
        }

        if (!(board instanceof JSONObject)) {
            throw new IllegalArgumentException("You can use only String or JSONObject as board");
        }

        JSONObject result = (JSONObject)board;

        // TODO а что если придумать другой формат и не делать этого двойного безобразия?
        String key = LevelsType.LAYERS;
        if (result.has(key)) {
            JSONArray encoded = new JSONArray();
            for (Object layer : result.getJSONArray(key)) {
                encoded.put(encode((String)layer));
            }
            result.put(key, encoded);
        }

        return result;
    }

    private String encode(String board) {
        char[] chars = board.replaceAll("\n", "").toCharArray();
        for (int index = 0; index < chars.length; index++) {
            chars[index] = getGuiChar(chars[index]);
        }
        return String.copyValueOf(chars);
    }

    @Override
    public String toString() {
        String values = getValues();
        return String.format("[%s -> %s]",
                values,
                GUI.substring(0, values.length()));
    }

    private String getValues() {
        return Arrays.stream(values)
                .map(CharElement::ch)
                .collect(asString());
    }

    public static Collector<Character, StringBuilder, String> asString() {
        return Collector.of(
                    StringBuilder::new,
                    StringBuilder::append,
                    StringBuilder::append,
                    StringBuilder::toString);
    }

    public ImmutablePair<Object, Object>  buildBoards(Game game, String playerId) {
        // TODO вот например для mollymage всем отдаются одни и те же борды, отличие только в паре спрайтов
        try {
            // TODO дольше всего выполняется getBoardAsString, прооптимизировать!
            Object screenBoard = game.getBoardAsString(true);
            Object clientBoard = game.sameBoard() ? screenBoard : game.getBoardAsString(false);

            return new ImmutablePair(encodeForBrowser(screenBoard), encodeForClient(clientBoard));
        } catch (Exception e) {
            log.error("Error during draw board for player: " + playerId, e);
            throw e;
        }
    }

}

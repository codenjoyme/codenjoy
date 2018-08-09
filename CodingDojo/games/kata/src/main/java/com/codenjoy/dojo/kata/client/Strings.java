package com.codenjoy.dojo.kata.client;

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


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Strings implements Iterable<String> {

    private List<String> strings;

    public Strings(String... strings) {
        this(Arrays.asList(strings));
    }

    public Strings(List<String> strings) {
        this();
        this.strings.addAll(strings);
    }

    public Strings() {
        strings = new LinkedList<>();
    }

    public List<String> getStrings() {
        return strings;
    }

    public void add(String string) {
        strings.add(string);
    }

    @Override
    public Iterator<String> iterator() {
        return strings.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        String separator = "";
        for (String string : strings) {
            builder.append(separator);
            builder.append('\'').append(string).append('\'');
            separator = ", ";
        }
        builder.append(']');
        return builder.toString();
    }

    public int size() {
        return strings.size();
    }
}

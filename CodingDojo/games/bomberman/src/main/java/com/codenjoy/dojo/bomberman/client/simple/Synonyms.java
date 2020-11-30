package com.codenjoy.dojo.bomberman.client.simple;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

public class Synonyms {
    private Map<Character, List<Character>> map;

    public Synonyms() {
        map = new TreeMap<>();
    }
    
    public void add(Character synonym, String characters) {
        map.put(synonym, 
                characters.chars()
                    .mapToObj(c -> (char) c)
                        .collect(toList()));
    }
    
    public boolean match(Character mask, char real) {
        return map.containsKey(mask) && map.get(mask).contains(real);     
    }

    public Collection<Character> chars() {
        return map.keySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}

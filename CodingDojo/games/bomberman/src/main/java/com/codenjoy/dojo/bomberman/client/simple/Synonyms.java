package com.codenjoy.dojo.bomberman.client.simple;

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
}

package com.codenjoy.dojo.bomberman.client.simple;

import com.codenjoy.dojo.services.Direction;

public class Rule {

    private String pattern;
    private Direction direction;

    public Rule(String pattern, Direction direction) {
        this.pattern = pattern;
        this.direction = direction;
    }

    public String pattern() {
        return pattern;
    }

    public Direction direction() {
        return direction;
    }
}

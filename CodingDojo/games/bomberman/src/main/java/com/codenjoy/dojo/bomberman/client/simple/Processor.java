package com.codenjoy.dojo.bomberman.client.simple;

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.services.Direction;

import java.util.LinkedList;
import java.util.List;

public class Processor {

    private List<Rule> rules = new LinkedList<>();

    public void addIf(Direction direction, String pattern) {
        rules.add(new Rule(pattern, direction));
    }

    public Direction process(Board board) {
        return rules.stream()
                .filter(rule -> board.isNearMe(rule.pattern()))
                .findFirst()
                .orElse(new Rule("", Direction.STOP))
                .direction();
    }
}

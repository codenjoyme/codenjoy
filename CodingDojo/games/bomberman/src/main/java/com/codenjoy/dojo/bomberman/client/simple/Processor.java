package com.codenjoy.dojo.bomberman.client.simple;

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;

import static com.codenjoy.dojo.bomberman.client.simple.RuleReader.MAIN_RULE_FILE_NAME;

public class Processor {

    private RuleReader reader;
    private File rulesFile;
    private Dice dice;
    private Deque<Direction> commands;
    private Rules rules;
    
    public Processor(String rulesPlace, Dice dice) {
        rulesFile = new File(rulesPlace + MAIN_RULE_FILE_NAME);
        this.dice = dice;
        commands = new LinkedList<>();
        reader = getReader();
    }

    protected RuleReader getReader() {
        return new RuleReader();
    }

    public Direction next(Board board) {
        if (board.isMyBombermanDead()) {
            return Direction.STOP;
        }
        
        if (commands.isEmpty()) {
            rules = new Rules();
            
            reader.load(rules, rulesFile);

            if (reader.hasErrors()) {
                reader.errors().forEach(System.out::println);
                reader.cleanErrors();
            }

            commands.addAll(rules.process(board));
        }
        
        return commands.removeFirst();
    }
}

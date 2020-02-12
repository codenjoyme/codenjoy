package com.codenjoy.dojo.bomberman.client.simple;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;

import static com.codenjoy.dojo.bomberman.client.simple.RuleReader.MAIN_RULE_FILE_NAME;

public class Processor {

    private Consumer<ErrorMessage> console;
    private RuleReader reader;
    private File rulesFile;
    private Dice dice;
    private Deque<Direction> commands;
    private Rules rules;

    public Processor(String rulesPlace, Dice dice, Consumer<ErrorMessage> console) {
        rulesFile = new File(rulesPlace + MAIN_RULE_FILE_NAME);
        this.dice = dice;
        commands = new LinkedList<>();
        reader = getReader();
        this.console = console;
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
                reader.errors().forEach(console);
                reader.cleanErrors();
            }

            commands.addAll(rules.process(board));
        }
        
        return commands.removeFirst();
    }
}

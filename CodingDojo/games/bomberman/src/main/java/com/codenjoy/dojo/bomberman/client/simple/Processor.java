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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

import java.io.File;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.codenjoy.dojo.bomberman.client.simple.RuleReader.MAIN_RULE_FILE_NAME;

public class Processor {

    private Consumer<Message> console;
    private RuleReader reader;
    private File rulesFile;
    private Dice dice;
    private Deque<Direction> commands;
    private Rules rules;

    public Processor(String rulesPlace, Dice dice, Consumer<Message> console) {
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
            rules = new Rules(console);
            
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

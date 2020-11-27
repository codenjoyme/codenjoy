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

import com.codenjoy.dojo.services.Direction;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Rules {

    private List<Rule> rules = new LinkedList<>();
    private Consumer<Message> console;

    public Rules(Consumer<Message> console) {
        this.console = console;
    }

    public void addIf(List<Direction> directions, Pattern pattern) {
        rules.add(new RuleChild(pattern, directions));
    }

    public Rules addSubIf(Pattern pattern) {
        Rules rules = new Rules(console);
        this.rules.add(new RuleNode(pattern, rules));
        return rules;
    }

    public List<Direction> process(Board board) {
        return findFor(board)
                .orElse(new RuleStop())
                .directions(board);
    }

    @Override
    public String toString() {
        return rules.stream()
                .map(Rule::toString)
                .collect(Collectors.toList())
                .toString();
    }
    
    public Optional<Rule> findFor(Board board) {
        Optional<Rule> result = rules.stream()
                .map(rule -> rule.findFor(board))
                .filter(Objects::nonNull)
                .findFirst();

        if (result.isPresent()) {
            console.accept(Message.get("Mach rule: ", result.get()));
        }

        return result;
    }
}
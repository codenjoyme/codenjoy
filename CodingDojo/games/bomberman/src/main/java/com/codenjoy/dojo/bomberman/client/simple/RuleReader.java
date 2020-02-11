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

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.google.common.primitives.Chars;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RuleReader {

    public static final String DIRECTIVE_RULE = "RULE ";

    public void load(Rules rules, File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            Supplier<String> lines = () -> {
                try {
                    return reader.readLine();
                } catch (IOException e) {
                    System.out.println("[ERROR] " + Messages.READING_FILE_ERROR + 
                            ": " + file.toString());
                    return null;
                }
            };

            processLines(rules, file, lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processLines(Rules rules, File file, Supplier<String> lines)  {
        String line;
        String pattern = "";
        int number = 0;
        do {
            line = lines.get();
            number++;

            if (line == null) {
                if (!StringUtils.isEmpty(pattern)) {
                    if (isValidPattern(pattern)) {
                        error(Messages.DIRECTION_IS_EMPTY_FOR_PATTERN, file, number, pattern);
                    } else {
                        error(Messages.PATTERN_IS_NOT_VALID, file, number, pattern);
                    }
                }
                break;
            }
            if (StringUtils.isEmpty(line)) {
                continue;
            }

            boolean isRuleDirective = line.toUpperCase().startsWith(DIRECTIVE_RULE);
            boolean isDirectionDirective = Direction.isValid(line);

            if (isRuleDirective || isDirectionDirective) {
                if (!isValidPattern(pattern)) {
                    error(Messages.PATTERN_IS_NOT_VALID, file, number, pattern);
                    continue;
                }
            }

            if (!isRuleDirective && !isDirectionDirective) {
                pattern += line;
                continue;
            }

            if (isRuleDirective) {
                String fileName = line.substring(DIRECTIVE_RULE.length());
                File subFile = new File(file.getParent() + "/" + fileName + ".rule");

                Rules sub = rules.addSubIf(pattern);
                this.load(sub, subFile);

            } else if (isDirectionDirective) {
                rules.addIf(Direction.valueOf(line), pattern);
            }
            pattern = "";
        } while (line != null);
    }

    private void error(String message, File file, int number, String pattern) {
        System.out.printf("[ERROR] " + message + ": '%s' at %s:%s%n",
                pattern, file.getName(), number);
    }

    private boolean isValidPattern(String pattern) {
        return isValidPatternLength(pattern) && isValidPatternSymbols(pattern);
    }

    private boolean isValidPatternSymbols(String pattern) {
        List<Character> allow = Arrays.stream(Elements.values())
                .map(e -> e.ch())
                .collect(Collectors.toList());
        allow.add(Board.ANY_CHAR);

        return new LinkedList<>(Chars.asList(pattern.toCharArray())).stream()
                .filter(ch -> !allow.contains(ch))
                .count() == 0;
    }

    private boolean isValidPatternLength(String pattern) {
        double sqrt = Math.sqrt(pattern.length());
        return sqrt == Math.floor(sqrt);
    }

}

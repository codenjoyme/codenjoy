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

import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.client.Encoding;
import com.codenjoy.dojo.services.Direction;
import com.google.common.primitives.Chars;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static com.codenjoy.dojo.bomberman.client.simple.Messages.*;
import static java.util.stream.Collectors.toList;

public class RuleReader {

    public static final String DIRECTIVE_RULE = "RULE ";
    public static final String DIRECTIVE_SYNONYM = "LET ";
    public static final String MAIN_RULE_FILE_NAME = "/main.rule";
    
    private List<Message> errors = new LinkedList<>();

    public void load(Rules rules, File file) {
        validate(file);
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Encoding.UTF8))) {

            Supplier<String> lines = () -> {
                try {
                    return reader.readLine();
                } catch (IOException e) {
                    errors.add(Message.error(READING_FILE_ERROR, file));
                    return null;
                }
            };

            processLines(rules, file, lines);

        } catch (IOException e) {
            errors.add(Message.error(READING_FILE_ERROR, file));
            return;
        }
    }

    private void validate(File file) {
        File directory = file.getParentFile();
        if (directory == null || !directory.exists() || !directory.isDirectory()) {
            errors.add(Message.error(RULES_DIRECTORY_NOT_FOUND_HERE,
                    (directory != null) ? directory.getAbsolutePath() : null));
            return;
        }
        if (!file.exists() || !file.isFile()) {
            errors.add(Message.error(MAIN_RULE_FILE_NOT_FOUND_HERE,
                    directory.getAbsolutePath()));
            return;
        }
    }

    protected void processLines(Rules rules, File file, Supplier<String> lines)  {
        onLinesStart();
        
        String line;
        String pattern = StringUtils.EMPTY;
        Synonyms synonyms = new Synonyms();
        int number = 0;
        do {
            line = lines.get();
            number++;

            if (line == null) {
                if (!StringUtils.isEmpty(pattern)) {
                    if (isValidPattern(new Pattern(pattern, synonyms))) {
                        errors.add(Message.error(DIRECTIONS_IS_EMPTY_FOR_PATTERN, file, number, pattern));
                    } else {
                        errors.add(Message.error(PATTERN_IS_NOT_VALID, file, number, pattern));
                    }
                }
                
                pattern = StringUtils.EMPTY;
                continue;
            }
            if (StringUtils.isEmpty(line)) {
                continue;
            }

            boolean isSynonymDirective = line.toUpperCase().startsWith(DIRECTIVE_SYNONYM);
            if (isSynonymDirective) {
                String substring = line.substring(DIRECTIVE_SYNONYM.length());
                String[] split = substring.split("=");

                if (split.length != 2 || split[0].length() < 1 || split[0].length() > 2 || split[1].length() <= 1) {
                    errors.add(Message.error(SYNONYM_IS_NOT_VALID, file, number, line));
                    continue;
                }
                String variable = split[0];
                String values = split[1];

                if (variable.endsWith("!")) {
                    values = invert(values);
                }

                synonyms.add(variable.charAt(0), values);
                continue;
            }
            
            boolean isRuleDirective = line.toUpperCase().startsWith(DIRECTIVE_RULE);
            boolean isDirectionsDirective = isValidDirections(line);
            boolean isJustComma = isJustComma(line);
            boolean isContainsDirection = isContainsDirection(line);
            
            if (isRuleDirective || (isDirectionsDirective && !isJustComma)) {
                if (!isValidPattern(new Pattern(pattern, synonyms))) {
                    errors.add(Message.error(PATTERN_IS_NOT_VALID, file, number, pattern));
                    
                    pattern = StringUtils.EMPTY;
                    continue;
                }
            } else if (isJustComma || isContainsDirection) {
                errors.add(Message.error(DIRECTIONS_IS_NOT_VALID_FOR_PATTERN,
                        file, number, pattern, line));

                pattern = StringUtils.EMPTY;
                continue;    
            }

            if (!isRuleDirective && !isDirectionsDirective) {
                pattern += line;
                continue;
            }

            if (isRuleDirective) {
                String fileName = line.substring(DIRECTIVE_RULE.length());
                File subFile = new File(file.getParent() + "/" + fileName + ".rule");

                Rules sub = rules.addSubIf(new Pattern(pattern, synonyms));
                this.load(sub, subFile);

                pattern = StringUtils.EMPTY;
                continue;

            } else if (isDirectionsDirective) {
                List<Direction> directions = 
                        parseDirections(line)
                                .stream()
                                .map(s -> Direction.valueOf(s))
                                .collect(toList());
                
                if (directions.isEmpty()) {
                    errors.add(Message.error(DIRECTIONS_IS_NOT_VALID_FOR_PATTERN,
                            file, number, pattern, line));

                    pattern = StringUtils.EMPTY;
                    continue;
                }
                
                rules.addIf(directions, new Pattern(pattern, synonyms));

                pattern = StringUtils.EMPTY;
                continue;
            }
        } while (line != null);

        onLinesFinish();
    }

    private String invert(String chars) {
        return Arrays.stream(Elements.values())
                .map(el -> el.ch())
                .filter(ch -> chars.indexOf(ch) == -1)
                .collect(asString());
    }

    private Collector<Character, StringBuilder, String> asString() {
        return Collector.of(
                StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append,
                StringBuilder::toString);
    }

    protected void onLinesFinish() {
        // do nothing
    }
    
    protected void onLinesStart() {
        // do nothing
    }

    private boolean isJustComma(String string) {
        String trimmed = string.trim();
        return trimmed.startsWith(",") || trimmed.endsWith(",");
    }

    private boolean isContainsDirection(String string) {
        return Arrays.asList(Direction.values()).stream()
                .map(d -> d.toString().toUpperCase())
                .filter(d -> string.toUpperCase().contains(d))
                .count() > 0;
    }

    private boolean isValidDirections(String string) {
        return !parseDirections(string).stream()
                .anyMatch(d -> !Direction.isValid(d));
    }

    private List<String> parseDirections(String string) {
        return Arrays.asList(string.replaceAll(" ?", "")
                .split(","));
    }
    
    private boolean isValidPattern(Pattern pattern) {
        return isValidPatternLength(pattern.pattern()) && isValidPatternSymbols(pattern);
    }

    private boolean isValidPatternSymbols(Pattern pattern) {
        List<Character> allow = Arrays.stream(Elements.values())
                .map(e -> e.ch())
                .collect(toList());
        allow.add(Board.ANY_CHAR);
        allow.addAll(pattern.synonyms().chars());

        return new LinkedList<>(Chars.asList(pattern.pattern().toCharArray())).stream()
                .filter(ch -> !allow.contains(ch))
                .count() == 0;
    }

    private boolean isValidPatternLength(String pattern) {
        double sqrt = Math.sqrt(pattern.length());
        return sqrt == Math.floor(sqrt);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<Message> errors() {
        return errors;
    }

    public void cleanErrors() {
        errors.clear();
    }
}

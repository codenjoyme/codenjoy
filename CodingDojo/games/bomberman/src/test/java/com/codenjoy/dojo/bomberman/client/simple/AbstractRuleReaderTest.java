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

import org.junit.Before;

import java.io.File;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractRuleReaderTest {

    protected List<File> subFiles;
    protected RuleReader reader;
    protected File file;
    protected Rules rules;
    
    protected List<Supplier<String>> lines;
    private int linesIndex;

    @Before
    public void setup() {
        // given
        file = new File("directory/main.rule");
        rules = new Rules(message -> {});
        subFiles = new LinkedList<>();

        cleanLns();
        
        reader = new RuleReader() {
            @Override
            public void load(Rules rules, File file) {
                subFiles.add(file);

                processLines(rules, file, lines.get(linesIndex));
            }

            @Override
            protected void onLinesStart() {
                linesIndex++;
            }
            
            @Override
            protected void onLinesFinish() {
                linesIndex--;
            }
        };
    }

    protected void cleanLns() {
        lines = new LinkedList<>();
        linesIndex = 0;
    }

    protected void loadLns(String... input) {
        Deque<String> list = new LinkedList<>(Arrays.asList(input));
        lines.add(() -> {
            if (list.isEmpty()) {
                return null;
            }
            return list.removeFirst();
        });
    }
}

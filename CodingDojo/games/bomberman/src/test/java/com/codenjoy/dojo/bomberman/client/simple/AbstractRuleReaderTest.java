package com.codenjoy.dojo.bomberman.client.simple;

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
    protected Supplier<String> lines;
    protected Supplier<String> lines2;
    protected File file;
    protected Rules rules;

    @Before
    public void setup() {
        // given
        file = new File("directory/main.rule");
        rules = new Rules();
        subFiles = new LinkedList<>();
        reader = new RuleReader() {
            @Override
            public void load(Rules rules, File file) {
                subFiles.add(file);

                processLines(rules, file, lines2);
            }
        };
    }

    protected Supplier<String> load(String... input) {
        Deque<String> list = new LinkedList<>(Arrays.asList(input));
        return () -> {
            if (list.isEmpty()) {
                return null;
            }
            return list.removeFirst();
        };
    }
}

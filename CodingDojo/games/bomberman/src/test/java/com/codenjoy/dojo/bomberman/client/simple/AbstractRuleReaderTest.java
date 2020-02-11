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
    protected File file;
    protected Rules rules;
    
    protected List<Supplier<String>> lines;
    private int linesIndex;

    @Before
    public void setup() {
        // given
        file = new File("directory/main.rule");
        rules = new Rules();
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

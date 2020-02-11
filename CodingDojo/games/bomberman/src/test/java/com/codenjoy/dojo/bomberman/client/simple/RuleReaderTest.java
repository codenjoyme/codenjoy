package com.codenjoy.dojo.bomberman.client.simple;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class RuleReaderTest {

    private File file;
    private List<File> subFiles;
    private RuleReader reader;
    private Supplier<String> lines;
    private Supplier<String> lines2;
    private Rules rules;

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
    
    private Supplier<String> load(String... input) {
        Deque<String> list = new LinkedList<>(Arrays.asList(input));
        return () -> {
            if (list.isEmpty()) {
                return null;
            }
            return list.removeFirst();
        };
    }

    @Test
    public void shouldNoRules_whenEmptyFile() {
        // given
        lines = load("");

        // when
        reader.processLines(rules, file, lines);

        // then
        assertEquals("[]", rules.toString());

    }

    @Test
    public void shouldSeveralRules_whenSingleFile() {
        // given
        lines = load(
                "???",
                "♥☺?",
                "???",
                "RIGHT",
                "",
                "???",
                "?☺♥",
                "???",
                "LEFT",
                "",
                "?♥?",
                "?☺?",
                "???",
                "DOWN",
                "",
                "?☼?",
                "☼☺?",
                "?♥?",
                "RIGHT",
                "",
                "???",
                "?☺?",
                "?♥?",
                "UP");

        // when
        reader.processLines(rules, file, lines);

        // then
        assertEquals(
                "[[???♥☺???? > [RIGHT]], " +
                "[????☺♥??? > [LEFT]], " +
                "[?♥??☺???? > [DOWN]], " +
                "[?☼?☼☺??♥? > [RIGHT]], " +
                "[????☺??♥? > [UP]]]", rules.toString());
    }

    @Test
    public void shouldLoadRules_whenRuleDirective() {
        // given
        lines = load(
                "?☼?",
                "?☺?",
                "???",
                "DOWN",
                "",
                "????????",
                "????????",
                "????????",
                "☺",
                "????????",
                "????????",
                "????????",
                "RULE right",
                "",
                "???",
                "☼☺☼",
                "?#?",
                "UP");
        
        lines2 = load(
                "?☼?",
                "?☺ ",
                "?☼?",
                "RIGHT",
                "",
                "?☼?",
                "?☺ ",
                "?#?",
                "RIGHT",
                "",
                "?#?",
                "?☺ ",
                "?☼?",
                "RIGHT",
                "",
                "?#?",
                "?☺ ",
                "?#?",
                "RIGHT");

        // when
        reader.processLines(rules, file, lines);

        // then
        assertEquals(
                "[[?☼??☺???? > [DOWN]], " +
                "[????????????????????????☺???????????????????????? > [" +
                    "[?☼??☺ ?☼? > [RIGHT]], " +
                    "[?☼??☺ ?#? > [RIGHT]], " +
                    "[?#??☺ ?☼? > [RIGHT]], " +
                    "[?#??☺ ?#? > [RIGHT]]]" +
                "], " +
                "[???☼☺☼?#? > [UP]]]", rules.toString());
    }

    @Test
    public void shouldSeveralDirections_whenOneRule() {
        // given
        lines = load(
                "   ",
                "   ",
                "   ",
                "RIGHT,LEFT,DOWN",
                "",
                "   ",
                "   ",
                "   ",
                "  LEFT  , RIGHT,   UP",
                "",
                "   ",
                "   ",
                "   ",
                "DOWN, UP  ",
                "",
                "   ",
                "   ",
                "   ",
                "RIGHT",
                "",
                "   ",
                "   ",
                "   ",
                "    UP ,  DOWN, LEFT, RIGHT,RIGHT");

        // when
        reader.processLines(rules, file, lines);

        // then
        assertEquals(
                "[[          > [RIGHT, LEFT, DOWN]], " +
                "[          > [LEFT, RIGHT, UP]], " +
                "[          > [DOWN, UP]], " +
                "[          > [RIGHT]], " +
                "[          > [UP, DOWN, LEFT, RIGHT, RIGHT]]]", 
                rules.toString());
    }
    
}
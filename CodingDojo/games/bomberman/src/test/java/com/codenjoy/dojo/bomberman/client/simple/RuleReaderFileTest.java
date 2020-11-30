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
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RuleReaderFileTest {

    public static final String SEP = FileSystems.getDefault().getSeparator();
    
    private RuleReader reader;
    private Rules rules;

    @Before
    public void setup() {
        // given
        reader = new RuleReader();
        rules = new Rules(message -> {});
    }
    
    @Test
    public void shouldErrorMessage_whenFileWithoutDirectory() {
        // when
        File file = new File("badFile.rule");
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Rules directory not found here: 'null', [ERROR] Reading file error: 'badFile.rule']", reader.errors().toString());
    }

    @Test
    public void shouldErrorMessage_whenFileNotExists_withNotExistsDirectory() {
        // when
        File file = new File("badDirectory/badFile.rule");
        reader.load(rules, file);

        // then
        List<Message> errors = reader.errors();
        
        assertEquals(2, errors.size());
        
        String message1 = errors.get(0).toString();
        assertEquals(message1, true, message1.startsWith("[ERROR] Rules directory not found here: "));
        assertEquals(message1, true, message1.endsWith(SEP + "badDirectory'"));
        
        String message2 = errors.get(1).toString();
        assertEquals(message2, true, message2.startsWith("[ERROR] Reading file error: 'badDirectory" + SEP + "badFile.rule'"));
    }

    @Ignore // TODO в travis валится, надо разобраться
    @Test
    public void shouldErrorMessage_whenFileNotExists_withExistsDirectory() {
        // when
        File file = new File("src/test/resources/subDirectory/main.rule");
        reader.load(rules, file);

        // then
        List<Message> errors = reader.errors();

        assertEquals(2, errors.size());

        String message1 = errors.get(0).toString();
        assertEquals(message1, true, message1.startsWith("[ERROR] Main rule file not found here: "));
        String dir = "src" + SEP + "test" + SEP + "resources" + SEP + "subDirectory";
        assertEquals(message1, true, message1.endsWith(SEP + dir + "'"));

        String message2 = errors.get(1).toString();
        assertEquals(message2, true, message2.startsWith("[ERROR] Reading file error: " +
                "'" + dir + SEP + "main.rule'"));
    }

    @Ignore // TODO в travis валится, надо разобраться
    @Test
    public void shouldErrorMessage_whenMainRuleDirectoryLikeFile() {
        // when
        File file = new File("src/test/resources/subDirectory2/main.rule");
        reader.load(rules, file);

        // then
        List<Message> errors = reader.errors();

        assertEquals(2, errors.size());

        String message1 = errors.get(0).toString();
        assertEquals(message1, true, message1.startsWith("[ERROR] Main rule file not found here: "));
        String dir = "src" + SEP + "test" + SEP + "resources" + SEP + "subDirectory2";
        assertEquals(message1, true, message1.endsWith(SEP + dir + "'"));

        String message2 = errors.get(1).toString();
        assertEquals(message2, true, message2.startsWith("[ERROR] Reading file error: " +
                "'" + dir + SEP + "main.rule'"));
    }

    @Test
    public void shouldLoadFile_whenEverythingIsOk() {
        // when
        File file = new File("src/test/resources/main.rule");
        reader.load(rules, file);
        
        // then 
        assertEquals("[[\n" +
                "?☼?\n" +
                "?☺ \n" +
                "?☼?\n" +
                "synonyms: {} \n" +
                " >>> [RIGHT]]]", rules.toString());
    }
}

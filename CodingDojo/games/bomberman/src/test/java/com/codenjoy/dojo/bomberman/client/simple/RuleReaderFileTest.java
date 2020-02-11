package com.codenjoy.dojo.bomberman.client.simple;

import org.junit.Before;
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
        rules = new Rules();
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
        List<ErrorMessage> errors = reader.errors();
        
        assertEquals(2, errors.size());
        
        String message1 = errors.get(0).toString();
        assertEquals(message1, true, message1.startsWith("[ERROR] Rules directory not found here: "));
        assertEquals(message1, true, message1.endsWith(SEP + "badDirectory'"));
        
        String message2 = errors.get(1).toString();
        assertEquals(message2, true, message2.startsWith("[ERROR] Reading file error: 'badDirectory" + SEP + "badFile.rule'"));
    }

    @Test
    public void shouldErrorMessage_whenFileNotExists_withExistsDirectory() {
        // when
        File file = new File("src/test/resources/subDirectory/main.rule");
        reader.load(rules, file);

        // then
        List<ErrorMessage> errors = reader.errors();

        assertEquals(2, errors.size());

        String message1 = errors.get(0).toString();
        assertEquals(message1, true, message1.startsWith("[ERROR] Main rule file not found here: "));
        String dir = "src" + SEP + "test" + SEP + "resources" + SEP + "subDirectory";
        assertEquals(message1, true, message1.endsWith(SEP + dir + "'"));

        String message2 = errors.get(1).toString();
        assertEquals(message2, true, message2.startsWith("[ERROR] Reading file error: " +
                "'" + dir + SEP + "main.rule'"));
    }

    @Test
    public void shouldErrorMessage_whenMainRuleDirectoryLikeFile() {
        // when
        File file = new File("src/test/resources/subDirectory2/main.rule");
        reader.load(rules, file);

        // then
        List<ErrorMessage> errors = reader.errors();

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
        assertEquals("[[?☼??☺ ?☼? > [RIGHT]]]", rules.toString());
    }
}
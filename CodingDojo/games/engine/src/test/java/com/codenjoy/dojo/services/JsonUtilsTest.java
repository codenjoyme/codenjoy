package com.codenjoy.dojo.services;

import org.json.JSONArray;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by indigo on 2017-03-04.
 */
public class JsonUtilsTest {
    @Test
    public void testGetStrings() throws Exception {
        assertGetStrings("['string1', 'string2', 'string3']",
                "[string1, string2, string3]");

        assertGetStrings("['string']",
                "[string]");

        assertGetStrings("[]",
                "[]");
    }

    private void assertGetStrings(String source, String expected) {
        assertEquals(expected, JsonUtils.getStrings(new JSONArray(source)).toString());
    }

    static class SomeObject {
        private String field1;
        private List<String> field2;

        SomeObject(String field1, List<String> field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public String getField1() {
            return field1;
        }

        public List<String> getField2() {
            return field2;
        }
    }

    @Test
    public void testPrettyPrint() throws Exception {
        assertEquals("{\n" +
                "  \"field1\": \"string1\",\n" +
                "  \"field2\": [\n" +
                "    \"string2\",\n" +
                "    \"string3\"\n" +
                "  ]\n" +
                "}",
                JsonUtils.prettyPrint(
                    new SomeObject("string1",
                            Arrays.asList("string2", "string3"))));
    }

    @Test
    public void testPrettyPrintString() throws Exception {
        assertEquals("{\n" +
                        "  \"field1\": \"string1\",\n" +
                        "  \"field2\": [\n" +
                        "    \"string2\",\n" +
                        "    \"string3\"\n" +
                        "  ]\n" +
                        "}",
                JsonUtils.prettyPrint("{'field1':'string1','field2':['string2','string3']}"));
    }

}
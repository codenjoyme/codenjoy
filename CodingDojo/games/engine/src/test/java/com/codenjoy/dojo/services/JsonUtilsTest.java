package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;
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

    public static class SomeObject {
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
                "  'field1':'string1',\n" +
                "  'field2':[\n" +
                "    'string2',\n" +
                "    'string3'\n" +
                "  ]\n" +
                "}",
                JsonUtils.prettyPrint(
                    new SomeObject("string1",
                            Arrays.asList("string2", "string3"))));
    }

    @Test
    public void testPrettyPrintString() throws Exception {
        assertEquals("{\n" +
                        "  'field1':'string1',\n" +
                        "  'field2':[\n" +
                        "    'string2',\n" +
                        "    'string3'\n" +
                        "  ]\n" +
                        "}",
                JsonUtils.prettyPrint("{'field1':'string1','field2':['string2','string3']}"));
    }

    @Test
    public void testPrettyPrintStringWithString() throws Exception {
        assertEquals("{\n" +
                        "  'current':0,\n" +
                        "  'lastPassed':-1,\n" +
                        "  'multiple':false,\n" +
                        "  'scores':true,\n" +
                        "  'total':1\n" +
                        "}",
                JsonUtils.prettyPrint("{\"total\":1,\"scores\":true,\"current\":0,\"lastPassed\":-1,\"multiple\":false}"));
    }

    @Test
    public void testPrettyPrintStringWithJsonObject() throws Exception {
        assertEquals("{\n" +
                        "  'current':0,\n" +
                        "  'lastPassed':-1,\n" +
                        "  'multiple':false,\n" +
                        "  'scores':true,\n" +
                        "  'total':1\n" +
                        "}",
                JsonUtils.prettyPrint(new JSONObject("{\"total\":1,\"scores\":true,\"current\":0,\"lastPassed\":-1,\"multiple\":false}")));
    }

    @Test
    public void testPrettyPrintStringWithJsonArray() throws Exception {
        assertEquals("[\n" +
                        "  '1, 2, 3, 4'\n" +
                        "]",
                JsonUtils.prettyPrint(new JSONArray(Arrays.asList("1, 2, 3, 4"))));
    }

}

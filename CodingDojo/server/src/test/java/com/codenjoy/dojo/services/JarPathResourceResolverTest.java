package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.support.ServletContextResource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.codenjoy.dojo.utils.TestUtils.isMatch;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

@Slf4j
public class JarPathResourceResolverTest {


    @SneakyThrows
    public String load(Resource resource) {
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    public void shouldProcessAllCases() {
        assertAll(
                "SERVLET /resolver/ file1.txt=>ServletContext resource [/resolver/file1.txt]=>one\n" +
                "CLASSPATH classpath:/resolver file2.txt=>class path resource [resolver/file2.txt]=>two\n" +
                "CLASSPATH classpath*:META-INF NOTICE=>URL [jar:file:*.jar!/META-INF/NOTICE]=>EXISTS\n" +
                "URL file:src/test/resources/resolver file3.txt=>URL [file:src/test/resources/resolver/file3.txt]=>three\n" +
                "URL file:../games/*/src/main/** Sample.java=>file [*/server/../games/sample/src/main/java/com/codenjoy/dojo/sample/model/Sample.java]=>EXISTS\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/** file4.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar4.jar!/resources/subfolder/file4.txt]=>four\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/** file5.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar5.jar!/resources/file5.txt]=>five\n" +
        "");
    }

    @Test
    public void shouldAddTrainingSlash() {
        assertAll(
                // NULL потому что для Serlvet нужен слеш в конце
                "SERVLET /resolver file1.txt=>NULL=>NULL\n" +
                "SERVLET /resolver/ file1.txt=>ServletContext resource [/resolver/file1.txt]=>one\n" +
                "SERVLET /resolver /file1.txt=>NULL=>NULL\n" +
                "SERVLET /resolver/ /file1.txt=>ServletContext resource [/resolver/file1.txt]=>one\n" +

                "CLASSPATH classpath:/resolver file2.txt=>class path resource [resolver/file2.txt]=>two\n" +
                "CLASSPATH classpath:/resolver/ file2.txt=>class path resource [resolver/file2.txt]=>two\n" +
                "CLASSPATH classpath:/resolver// file2.txt=>class path resource [resolver/file2.txt]=>two\n" +
                "CLASSPATH classpath:/resolver /file2.txt=>class path resource [resolver/file2.txt]=>two\n" +
                "CLASSPATH classpath:/resolver //file2.txt=>class path resource [resolver/file2.txt]=>two\n" +
                "CLASSPATH classpath:/resolver/ /file2.txt=>class path resource [resolver/file2.txt]=>two\n" +
                "CLASSPATH classpath:/resolver// //file2.txt=>class path resource [resolver/file2.txt]=>two\n" +

                "CLASSPATH classpath*:META-INF NOTICE=>URL [jar:file:*.jar!/META-INF/NOTICE]=>EXISTS\n" +
                "CLASSPATH classpath*:META-INF/ NOTICE=>URL [jar:file:*.jar!/META-INF/NOTICE]=>EXISTS\n" +
                "CLASSPATH classpath*:META-INF /NOTICE=>URL [jar:file:*.jar!/META-INF/NOTICE]=>EXISTS\n" +
                "CLASSPATH classpath*:META-INF/ /NOTICE=>URL [jar:file:*.jar!/META-INF/NOTICE]=>EXISTS\n" +

                "URL file:src/test/resources/resolver file3.txt=>URL [file:src/test/resources/resolver/file3.txt]=>three\n" +
                "URL file:src/test/resources/resolver/ file3.txt=>URL [file:src/test/resources/resolver/file3.txt]=>three\n" +
                "URL file:src/test/resources/resolver /file3.txt=>URL [file:src/test/resources/resolver/file3.txt]=>three\n" +
                "URL file:src/test/resources/resolver/ /file3.txt=>URL [file:src/test/resources/resolver/file3.txt]=>three\n" +

                "URL file:../games/*/src/main/** Sample.java=>file [*/server/../games/sample/src/main/java/com/codenjoy/dojo/sample/model/Sample.java]=>EXISTS\n" +
                "URL file:../games/*/src/main/**/ Sample.java=>file [*/server/../games/sample/src/main/java/com/codenjoy/dojo/sample/model/Sample.java]=>EXISTS\n" +
                "URL file:../games/*/src/main/** /Sample.java=>file [*/server/../games/sample/src/main/java/com/codenjoy/dojo/sample/model/Sample.java]=>EXISTS\n" +
                "URL file:../games/*/src/main/**/ /Sample.java=>file [*/server/../games/sample/src/main/java/com/codenjoy/dojo/sample/model/Sample.java]=>EXISTS\n" +

                "URL file:src/test/resources/resolver/*.jar!/resources/** file4.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar4.jar!/resources/subfolder/file4.txt]=>four\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/**/ file4.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar4.jar!/resources/subfolder/file4.txt]=>four\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/** /file4.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar4.jar!/resources/subfolder/file4.txt]=>four\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/**/ /file4.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar4.jar!/resources/subfolder/file4.txt]=>four\n" +

                "URL file:src/test/resources/resolver/*.jar!/resources/** file5.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar5.jar!/resources/file5.txt]=>five\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/**/ file5.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar5.jar!/resources/file5.txt]=>five\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/** /file5.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar5.jar!/resources/file5.txt]=>five\n" +
                "URL file:src/test/resources/resolver/*.jar!/resources/**/ /file5.txt=>URL [jar:file:*/server/src/test/resources/resolver/jar5.jar!/resources/file5.txt]=>five\n" +
        "");
    }

    private void assertAll(String data) {
        assertEquals(data,
                Arrays.stream(data.split("\n"))
                        .peek(line -> log.info(String.format("Processing: '%s'\n", line)))
                        .map(line -> line.split("=>"))
                        .map(array -> array[0] + "=>" + call(array[0], array[1], array[2]))
                        .collect(joining("\n")) + "\n");
    }

    private String call(String request, String expectedResource, String expectedContent) {
        String[] parts = request.split(" ");
        String type = parts[0];
        String location = parts[1];
        String file = parts[2];

        // when
        JarPathResourceResolver resolver = new JarPathResourceResolver();
        Resource resource = resolver.getResource(file, withResource(type, location));

        // then
        return String.format("%s=>%s",
                getResource(expectedResource, resource),
                getContent(expectedContent, resource));
    }

    private String getResource(String expectedResource, Resource resource) {
        if (expectedResource.contains("*") && isMatch(expectedResource, toString(resource))) {
            return expectedResource;
        }
        return toString(resource);
    }

    private String getContent(String content, Resource resource) {
        if (resource == null) {
            return "NULL";
        }
        String exists = resource.exists() ? "EXISTS" : "NOT EXISTS";
        return content.equals("EXISTS") ? exists : load(resource);
    }

    @SneakyThrows
    private Resource withResource(String type, String location) {
        switch (type) {
            case "SERVLET":
                return new ServletContextResource(new MockServletContext(), location);
            case "CLASSPATH":
                return new ClassPathResource(location);
            case "URL":
                return new UrlResource(location);
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private String toString(Resource resource) {
        if (resource == null) {
            return "NULL";
        }
        return resource.toString().replaceAll("\\\\", "/");
    }
}
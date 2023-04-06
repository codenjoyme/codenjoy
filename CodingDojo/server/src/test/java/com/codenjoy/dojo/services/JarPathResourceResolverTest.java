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
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.support.ServletContextResource;

import java.nio.charset.StandardCharsets;

import static com.codenjoy.dojo.utils.TestUtils.assertMatch;
import static org.junit.Assert.assertEquals;

public class JarPathResourceResolverTest {

    private JarPathResourceResolver resolver = new JarPathResourceResolver();

    @SneakyThrows
    public String load(Resource resource)  {
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    public void shouldLoadFromServletContext() {
        // when
        Resource resource = resolver.getResource("file1.txt",
                new ServletContextResource(new MockServletContext(), "/resolver/"));

        // then
        assertEquals("ServletContext resource [/resolver/file1.txt]", resource.toString());
        assertEquals("one", load(resource));
    }

    @Test
    public void shouldLoadFromClasspath() {
        // when
        Resource resource = resolver.getResource("file2.txt",
                new ClassPathResource("classpath:/resolver/"));

        // then
        assertEquals("class path resource [resolver/file2.txt]", resource.toString());
        assertEquals("two", load(resource));
    }

    @Test
    public void shouldLoadFromClasspathIncludingJars() {
        // when
        Resource resource = resolver.getResource("NOTICE",
                new ClassPathResource("classpath*:META-INF/"));

        // then
        assertMatch("URL [jar:file:*.jar!/META-INF/NOTICE]", resource.toString());
        assertEquals(true, resource.exists());
    }

    @Test
    public void shouldLoadFromFileSystem() throws Exception {
        // when
        Resource resource = resolver.getResource("file3.txt",
                new UrlResource("file:src/test/resources/resolver/"));

        // then
        assertEquals("URL [file:src/test/resources/resolver/file3.txt]", resource.toString());
        assertEquals("three", load(resource));
    }

    @Test
    public void shouldLoadFromJarsInFileSystem_case1() throws Exception {
        // when
        Resource resource = resolver.getResource("file4.txt",
                new UrlResource("file:src/test/resources/resolver/*.jar!/resources/**/"));

        // then
        assertMatch("URL [jar:file:*/server/src/test/resources/resolver/jar4.jar!/resources/subfolder/file4.txt]", resource.toString());
        assertEquals("four", load(resource));
    }

    @Test
    public void shouldLoadFromJarsInFileSystem_case2() throws Exception {
        // when
        Resource resource = resolver.getResource("file5.txt",
                new UrlResource("file:src/test/resources/resolver/*.jar!/resources/**/"));

        // then
        assertMatch("URL [jar:file:*/server/src/test/resources/resolver/jar5.jar!/resources/file5.txt]", resource.toString());
        assertEquals("five", load(resource));
    }
}
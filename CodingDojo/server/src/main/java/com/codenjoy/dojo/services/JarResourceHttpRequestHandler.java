package com.codenjoy.dojo.services;

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

import lombok.SneakyThrows;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class JarResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    private static final String JAR = "jar";
    public static final String PREFIX = JAR + ":file:";

    public JarResourceHttpRequestHandler(String... locations) {
        setLocationValues(Arrays.asList(
                locations
        ));

        setResourceResolvers(Arrays.asList(new PathResourceResolver() {
            @Override
            @SneakyThrows
            protected Resource getResource(String resourcePath, Resource location) {
                Resource relative = location.createRelative(resourcePath);
                String path = relative.getURI().toString();

                if (path.startsWith(PREFIX) && path.contains("*." + JAR)) {
                    String[] split = path.split("\\*\\." + JAR);
                    String left = split[0].substring(PREFIX.length());
                    String right = split[1];
                    String middle = right.substring(0, right.indexOf(resourcePath));

                    File directory = new File(left);
                    List<File> jars = Arrays.asList(directory.listFiles((dir, name) -> name.endsWith("." + JAR)));

                    Collections.sort(jars,
                            (jar1, jar2) -> compare(jar1.getPath(), jar2.getPath(), resourcePath));

                    for (File jar : jars) {
                        Resource result = getResource(resourcePath, middle, jar);
                        if (result != null) {
                            return result;
                        }
                    }
                }

                return super.getResource(resourcePath, location);
            }

            private Resource getResource(String resourcePath, String middle, File jar) throws IOException {
                URL url = new URL(PREFIX + jar.getPath().replace('\\', '/') + middle);
                FileUrlResource resource = new FileUrlResource(url);
                return super.getResource(resourcePath, resource);
            }
        }));
    }

    private int compare(String left1, String left2, String right) {
        // разбираем все на токены из слов
        String regex = "[.\\/-_]";
        String[] splitL1 = left1.split(regex);
        String[] splitL2 = left2.split(regex);
        String[] splitR = right.split(regex);

        // и сравниваем какие пары между собой глубже совпадают
        return Integer.compare(overlap(splitL2, splitR), overlap(splitL1, splitR));
    }

    private int overlap(String[] left, String[] right) {
        // тут хитро, узнаем насколько уменьшилось количество элементов, если мы все запакуем в Set (и устраним дубликаты)
        return left.length + right.length -
                new HashSet<String>(){{
                    addAll(Arrays.asList(left));
                    addAll(Arrays.asList(right));
                }}.size();
    }
}

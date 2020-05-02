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
import java.net.URL;
import java.util.Arrays;
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
                    for (File jar : jars) {
                        URL url = new URL(PREFIX + jar.getPath().replace('\\', '/') + middle);
                        FileUrlResource resource = new FileUrlResource(url);
                        // TODO тут есть возможность оптимизнуть - что если заглянуть сразу в мой jar глядя на запрос и на название jar и там и там фигурирует имя игры
                        Resource result = super.getResource(resourcePath, resource);
                        if (result != null) {
                            return result;
                        }
                    }
                }

                return super.getResource(resourcePath, location);
            }
        }));
    }
}

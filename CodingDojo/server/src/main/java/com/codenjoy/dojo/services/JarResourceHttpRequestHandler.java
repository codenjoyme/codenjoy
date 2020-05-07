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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JarResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    private static final String JAR = "jar";
    public static final String PREFIX = JAR + ":file:";

    private Map<String, Map<String, File>> jarsCache = map().get();
    private Map<String, Resource> resourcesCache = map().get();

    private Supplier<ConcurrentHashMap> map() {
        return () -> new ConcurrentHashMap<>();
    }

    public JarResourceHttpRequestHandler(String... locations) {
        setLocationValues(Arrays.asList(locations));

        setResourceResolvers(Arrays.asList(new PathResourceResolver() {
            @Override
            @SneakyThrows
            protected Resource getResource(String resource, Resource location) {
                if (resourcesCache.containsKey(resource)) {
                    return resourcesCache.get(resource);
                }

                Resource relative = location.createRelative(resource);
                String path = relative.getURI().toString();

                if (path.startsWith(PREFIX) && path.contains("*." + JAR)) {
                    String[] split = path.split("\\*\\." + JAR);
                    String jarFolder = split[0].substring(PREFIX.length());
                    String fileInJar = split[1];
                    String rootInJar = fileInJar.substring(0, fileInJar.indexOf(resource));

                    List<File> jars = getJars(jarFolder, resource);

                    for (File jar : jars) {
                        Resource result = getResource(resource, rootInJar, jar);
                        if (result != null) {
                            resourcesCache.put(resource, result);
                            return result;
                        }
                    }
                }

                return super.getResource(resource, location);
            }

            private Resource getResource(String resourcePath, String fileInJar, File jar) throws IOException {
                URL url = new URL(PREFIX + jar.getPath().replace('\\', '/') + fileInJar);
                FileUrlResource resource = new FileUrlResource(url);
                return super.getResource(resourcePath, resource);
            }
        }));
    }

    private List<File> getJars(String jarFolder, String resource) {
        if (!jarsCache.containsKey(jarFolder)) {
            jarsCache.put(jarFolder, map().get());

            Map<String, File> jars = jarsCache.get(jarFolder);
            File directory = new File(jarFolder);
            File[] files = directory.listFiles((dir, name) -> name.endsWith("." + JAR));
            Arrays.stream(files)
                    .forEach(file -> jars.put(file.getName().split("[\\.\\-_]")[0], file));
        }

        Map<String, File> jars = jarsCache.get(jarFolder);

        List<File> found = jars.entrySet().stream()
                .filter(entry -> resource.contains(entry.getKey()))
                .map(entry -> entry.getValue())
                .collect(Collectors.toList());

        return found;
    }

}

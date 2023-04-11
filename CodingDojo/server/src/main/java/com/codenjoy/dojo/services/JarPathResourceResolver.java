package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2023 Codenjoy
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.stripEnd;
import static org.apache.commons.lang3.StringUtils.stripStart;

@Slf4j
public class JarPathResourceResolver extends PathResourceResolver {

    private static final String MASK = "*";
    private static final String SEPARATOR = "!";
    private static final String EXT = ".jar" + SEPARATOR;
    private static final String PREFIX = "jar:";

    private Map<String, Resource[]> jarsCache = map().get();
    private Map<String, Resource> resourcesCache = map().get();

    private Supplier<ConcurrentHashMap> map() {
        return ConcurrentHashMap::new;
    }

    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    @Override
    protected Resource getResource(String path, Resource location) {
        Resource result = resourcesCache.get(path);
        if (result != null) {
            return result;
        }

        try {
            result = super.getResource(path, location);
        } catch (Exception e) {
            // do nothing
        }

        if (result == null) {
            result = tryGetResource(path, location);
        }

        if (result != null) {
            resourcesCache.put(path, result);
        }
        return result;
    }

    @SneakyThrows
    private Resource tryGetResource(String file, Resource location) {
        String base = getBase(location);
        if (base == null) {
            return null;
        }

        List<Resource> resources = null;

        String path = join(file, base, "/");

        if (path.contains(EXT)) {
            resources = getResourcesFromJar(path);
        }

        if (resources == null) {
            resources = getResources(path).collect(toList());
        }

        if (resources.isEmpty()) {
            return null;
        }

        if (resources.size() > 1) {
            log.warn("Found more than one resource for path: '{}' in location: '{}'",
                    file, location);
            resources.stream()
                    .map(this::getUri)
                    .map(Object::toString)
                    .map(it -> "\t\t" + it)
                    .forEach(log::warn);
        }

        return resources.get(0);
    }

    private String join(String file, String base, String separator) {
        return stripEnd(base, separator) + separator + stripStart(file, separator);
    }

    private String getBase(Resource location) throws IOException {
        if (location instanceof ServletContextResource) {
            return null;
        }

        if (location instanceof ClassPathResource) {
            return ((ClassPathResource) location).getPath();
        }

        if (location instanceof UrlResource) {
            return location.getURI().toString();
        }

        return StringUtils.EMPTY;
    }

    private List<Resource> getResourcesFromJar(String path) {
        String jarPattern = path.substring(0, path.indexOf(EXT) + EXT.length() - SEPARATOR.length());
        String filePattern = path.substring(path.indexOf(EXT) + EXT.length());

        if (!jarPattern.contains(MASK)) {
            return null;
        }

        return getResources(jarPattern)
                .map(this::getUri)
                .flatMap(jar -> getResources(PREFIX + jar + SEPARATOR + filePattern))
                .collect(toList());
    }

    @SneakyThrows
    private String getUri(Resource resource) {
        return resource.getURI().toString();
    }

    @SneakyThrows
    private Stream<Resource> getResources(String pattern) {
        Resource[] result = jarsCache.get(pattern);

        if (result == null) {
            result = resolver.getResources(pattern);

            jarsCache.put(pattern, result);
        }

        return Arrays.stream(result)
                .filter(Resource::exists);
    }
}

package com.codenjoy.dojo.utils;

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

import lombok.experimental.UtilityClass;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Collection;

@UtilityClass
public class ReflectUtils {

    public static <T> Collection<? extends Class<? extends T>> findInPackage(String packageName, Class<T> subtype) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .filterInputsBy(
                                // Consider only .class files (to avoid debug messages etc. on .dlls, etc
                                new FilterBuilder().exclude("^(?!.*\\.class$).*$")
                        ).forPackages(packageName)
                        .setScanners(new SubTypesScanner()));

        return reflections.getSubTypesOf(subtype);
    }

}
